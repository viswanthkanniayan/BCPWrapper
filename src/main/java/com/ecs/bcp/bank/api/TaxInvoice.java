package com.ecs.bcp.bank.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ecs.bcp.pojo.BalanceConfirmPojo;
import com.ecs.bcp.pojo.BankApiLogPojo;
import com.ecs.bcp.pojo.EntityDetailsPojo;
import com.ecs.bcp.pojo.GstMasterPojo;
import com.ecs.bcp.pojo.PaymentReportDetailsPojo;
import com.ecs.bcp.pojo.SettingsPojo;
import com.ecs.bcp.pojo.TdsMasterPojo;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.utils.PropertiesReader;
import com.ecs.bcp.utils.ServiceUtils;
import com.ecs.bcp.xsd.EntityRequest;
import com.ecs.bcp.xsd.EntityResponce;
import com.ecs.bcp.zoho.api.ZohoApiIRN;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PushbuttonField;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TaxInvoice{

	private static final String MAHARASHTRA_STATE = "Maharashtra";
	private static final String PSBA_1_25 = "Psba_1-25";
	private static final String PSBA_26_100 = "Psba_26-100";
	private static final String PSBA_ABOVE_100 = "Psba_ABOVE_100";
	private static final String NESL_INVOICE_CHARGES = "Nesl_Invoicing_Charges_Per_Invoice";
	private static final String RAZORPAY_CHARGES = "Razorpay_Route_Charges_Fixed";
	private static final String NESL_1_100 = "Nesl_1-100";
	private static final String NESL_ABOVE_100 = "Nesl_Above 100 - Flat Amount";
	private static final String PSBA_AUDITEE = "PSBA to Auditee (B2C)";
	private static final String NESL_PSBA = "NeSl to PSBA (B2B)";
	private static final String BANK_PSBA = "Bank to PSBA (B2B)";
	private static final String RAZORPAY_PSBA = "Razorpay to PSBA (B2B)";

	private static final String BANK1_TDS = "Bank 1 TDS on Base Amount";
	private static final String BANK2_TDS = "Bank 2 TDS on Base Amount";
	private static final String BANK3_TDS = "Bank 3 TDS on Base Amount";
	private static final String BANK4_TDS = "Bank 4 TDS on Base Amount";
	private static final String BANK5_TDS = "Bank 5 TDS on Base Amount";
	private static final String NESL_TDS = "NeSL TDS on Base Amount";
	private static final String RAZORPAY_TDS = "Razorpay TDS on Base Amount";



	private static AcroFields form;
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();

	public  byte[] genInvoicePdf(String sourceTempPDF, String pan, String countOfAcc, String bankCrg , String TxnId  ,  String taxInvNo) throws Exception {
		PdfReader pdfTemplate = null;
		PdfStamper stamper = null;
		DecimalFormat df = new DecimalFormat("0.00");

		EntityDetailsPojo pojo = new EntityDetailsPojo();

		try {

			pdfTemplate = new PdfReader(sourceTempPDF);
			stamper = new PdfStamper(pdfTemplate, this.baos);

			form = stamper.getAcroFields();
			form.setGenerateAppearances(true);

			System.out.println("countOfAcc -----------"+countOfAcc);
			System.out.println("bankCrg -----------"+bankCrg);
			System.out.println("invoiceNo -----------"+taxInvNo);

			setField("INV_NO", taxInvNo );


			SimpleDateFormat sysDate = new SimpleDateFormat("dd-MM-yyyy");
			setField("INV_DATE", sysDate.format(new Date()));
			setField("PAN", pan.toUpperCase());
			setField("PRO_BANK_CHARGES",  df.format(Double.parseDouble(bankCrg)));
			setField("POS","Maharashtra");

			setField("R1", df.format(Double.parseDouble(bankCrg)));
			//setField("UNIT", "01");
			setField("TAX1", df.format(Double.parseDouble(bankCrg)));

			
			double count = Double.parseDouble(countOfAcc);
			String psbaCrg = determinePsbaCharge(count);

			SettingsPojo psbaSetting = EjbLookUps.getSettingsRemote().findById(psbaCrg);
			double psbaTotal = calculatePsbaCrgTotal(psbaSetting, count);

			System.out.println("psbaSetting.getAmount() -----------"+psbaSetting.getAmount());

			setField("R2",  df.format(Double.parseDouble(psbaSetting.getAmount())));
			setField("UNIT", countOfAcc);
			setField("TAX2",  df.format(psbaTotal));

			double invoiceTotal = calculateInvoiceTotal();

			
			
			
			System.out.println("invoiceTotal ----------> "+invoiceTotal);
			

			//double grossInvoice = calculateGrossInvoice(bankCrg, psbaTotal, invoiceTotal);
			double paymentGatewayCharges = calculateInvoiceTotal();

			setField("R3", df.format(paymentGatewayCharges));
			//setField("UNIT", "");
			setField("TAX3", df.format(paymentGatewayCharges));
			
			setField("R4", "0.00");
			setField("TAX4", "0.00");

			double grossInvoice = calculateGrossInvoice(bankCrg, psbaTotal, invoiceTotal);

			setField("TOTAL_TAX_VALUE",  df.format(grossInvoice));

		System.out.println("grossInvoice ----------> "+grossInvoice);

			BalanceConfirmPojo balanceDetails = EjbLookUps.getBalanceConfirmRemote().findById(TxnId);
			if (balanceDetails != null) {
				
				setField("BANK_NAME", balanceDetails.getBank() != null ? balanceDetails.getBank().toUpperCase() :"");
				
//				System.out.println("PAY_DATE "+balanceDetails.getPaymentDate());
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				if(balanceDetails.getPaymentDate()!=null)
					setField("PAY_DATE", dateFormat.format(balanceDetails.getPaymentDate()));
			   
				setField("ORDER_ID", balanceDetails.getOrderId());
			  //  setField("PAY_VIA", balanceDetails.getPaymentMode() != null ?  balanceDetails.getPaymentMode().toUpperCase() :"");
			    
				setField("PROFORMA_INV_NO", balanceDetails.getProfInvNo());
				setField("REF_NO", balanceDetails.getPaymentId());
				
				setField("PAY_MODE", balanceDetails.getPaymentMode() != null ? balanceDetails.getPaymentMode().toUpperCase() : "");


				// ZOHO API 
				
				if(!ServiceUtils.isEmpty(balanceDetails.getAckNo())) 
					setField("ACK_NO", balanceDetails.getAckNo());
				
				if(!ServiceUtils.isEmpty(balanceDetails.getAckDate())) 
					setField("ACK_DATE", balanceDetails.getAckDate());
				
				if(!ServiceUtils.isEmpty(balanceDetails.getInvRefNum())) 
					setField("IRN", balanceDetails.getInvRefNum());
				
				String base64 = null;
				// convert qr link to base64
				if(!ServiceUtils.isEmpty(balanceDetails.getQrLink())) {
					 base64 = ZohoApiIRN.getBase64FromQRCodeLink(balanceDetails.getQrLink());
//				System.out.println("image base64 : "+base64.length());
				setImage("QR", Base64.getDecoder().decode(base64) );
				}
				
				EntityDetailsPojo userPojo = EjbLookUps.getEntityDetailsRemote().getEntityDetailsPojo(pan , "AUDITEE");
				if (userPojo != null) {


					calculateAndSetGst(userPojo, grossInvoice, df , TxnId );

					setField("ADDRESS", userPojo.getBillingAddress() != null ? userPojo.getBillingAddress().toUpperCase():"");
					setField("GSTIN_1","test_Invoice");
					setField("NAME",userPojo.getName()!= null ? userPojo.getName().toUpperCase():"" );
					setField("ADDRESS", userPojo.getBillingAddress()!= null ? userPojo.getBillingAddress().toUpperCase():"");
					setField("STATE", userPojo.getState()!= null ? userPojo.getState().toUpperCase():"");
					setField("PINCODE", userPojo.getBillingPincode()!= null ? userPojo.getBillingPincode().toUpperCase():"");
					String gstin = userPojo.getGstNo();
					setField("CERT_ID",  balanceDetails.getTxnId());
					
					
					if (gstin == null || gstin.isEmpty()) {
						setField("GSTIN", "Not provided");
						setField("ACK_NO", "URP");
						setField("ACK_DATE", "URP");
						setField("IRN", "URP");
						
					} else {
					    setField("GSTIN", gstin);
					}

				}
				
			}
			
			
			stamper.setFormFlattening(true);


		} catch (Exception e) {
			throw e;
		} finally {
			if (stamper != null) {
				stamper.close();
			}
			if (pdfTemplate != null) {
				pdfTemplate.close();
			}
		}

		return this.baos.toByteArray();
	}

	private static String determineNESLharge(Double count) {
		if (count == null) {
			throw new IllegalArgumentException("Count cannot be null");
		}
		if (count <= 100) {
			return NESL_1_100;
		} else {
			return NESL_ABOVE_100;
		}
	}

	private static String determinePsbaCharge(Double count) {
		if (count == null) {
			throw new IllegalArgumentException("Count cannot be null");
		}
		if (count <= 25) {
			return PSBA_1_25;
		} else if (count <= 100) {
			return PSBA_26_100;
		} else {
			return PSBA_ABOVE_100;
		}
	}


	private static double calculatePsbaCrgTotal(SettingsPojo setting, Double count) {

		if (setting == null || count == null) {
			return 0.00;
		}

		try {

			if(count > 100) {

				return Double.parseDouble(setting.getAmount());
			}else {
				return Double.parseDouble(setting.getAmount()) * count;
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid amount format in SettingsPojo", e);
		}
	}

	private double calculateInvoiceTotal() throws Exception {
		SettingsPojo neslSetting = EjbLookUps.getSettingsRemote().findById(NESL_INVOICE_CHARGES);
		SettingsPojo razorpaySetting = EjbLookUps.getSettingsRemote().findById(RAZORPAY_CHARGES);

		double neslAmount = 0.0;
		double razorpayAmount = 0.0;

		try {
			if (neslSetting != null && neslSetting.getAmount() != null) {
				neslAmount = Double.parseDouble(neslSetting.getAmount());
			}
			if (razorpaySetting != null && razorpaySetting.getAmount() != null) {
				razorpayAmount = Double.parseDouble(razorpaySetting.getAmount());
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid amount format in one of the settings", e);
		}

		return neslAmount + razorpayAmount;
	}


	private double calculateGrossInvoice(String bankCrg, double psbaTotal, double invoiceTotal) {
		return Double.parseDouble(bankCrg) + psbaTotal + invoiceTotal ;
	}

	private void calculateAndSetGst(EntityDetailsPojo user, double grossInvoice, DecimalFormat df ,String TxnId ) throws IOException, DocumentException {
		String state = user.getState();
		System.out.println("User state  =>   "+state);
		double gstRate = MAHARASHTRA_STATE.equalsIgnoreCase(state) ? 9 : 18;

		double gstAmount = calculateGst(grossInvoice, gstRate);
		String formattedGstAmount = df.format(gstAmount);

		if (gstRate == 9) {



			setField("CGST", formattedGstAmount);
			setField("SGST", formattedGstAmount);

			/*	setField("PRO_CGST", "9%");
			setField("PRO_SGST", "9%"); */
			setField("IGST", "0.00");
			
			double GSTTotalAmount = ((Double.parseDouble(formattedGstAmount))*2);
			
			setField("TOTAL_GST", GSTTotalAmount);

		} else {
			setField("TOTAL_GST", formattedGstAmount);
			
			setField("IGST", formattedGstAmount);
			
			setField("CGST", "0.00");
			setField("SGST", "0.00");
			//	setField("PRO_IGST", "18%");
		}

		double invoiceAmount = grossInvoice + gstAmount * (gstRate == 9 ? 2 : 1);
		
		setField("TOTAL_AMOUNT", df.format(invoiceAmount));
		
		//int totalAmountInInteger = (int) Math.round(invoiceAmount);
	    String totalAmountInWords = NumberToWordsConverter.numberToWords(invoiceAmount);

	    //setField("TOTAL_IN_WORD", totalAmountInWords + " Only");
	    setField("TOTAL_IN_WORD",( totalAmountInWords + " Only")!= null ? ( totalAmountInWords + " Only").toUpperCase():"");

	    
	}

	public static void setField(String fieldName, Double value) throws IOException, DocumentException {
		if (value != null) {
			setField(fieldName, String.valueOf(value));
		}
	}

	public static void setField(String fieldName, String value) throws IOException, DocumentException {
		if (value != null) {
			form.setField(fieldName, value);
		}
	}

	
	private void setImage(String fieldName, byte[] png) throws Exception {
	    if (png == null) {
//	      System.out.println("No PHOTO");
	      return;
	    }

	    PushbuttonField ad = form.getNewPushbuttonFromField(fieldName);
	    ad.setLayout(PushbuttonField.LAYOUT_ICON_ONLY);
	    ad.setProportionalIcon(true);
	    ad.setImage(Image.getInstance(png));
	    form.replacePushbuttonField(fieldName, ad.getField());
	  }
	
	public static double calculateGst(double amount, double gstRate) {
		return amount * gstRate / 100;
	}

	public static EntityResponce RevenueCalculator(String countOfAcc, String bankCrg, String Bank) {

		EntityResponce res = new EntityResponce();
		DecimalFormat df = new DecimalFormat("0.00");
		double PSBA_BCP_Revenue_Share = 0;
		double NeSL_BCP_Revenue_Share = 0;
		double BANK_BCP_Revenue_Share = 0;
		double PSBA_TDS_Collection = 0;
		double Total = 0;
		String neslCrg;

		try {


			double count = Double.parseDouble(countOfAcc);
			String psbaCrg = determinePsbaCharge(count);

			SettingsPojo psbaSetting = EjbLookUps.getSettingsRemote().findById(psbaCrg);
			double psbaTotal = calculatePsbaCrgTotal(psbaSetting, count);

			neslCrg = determineNESLharge(count);

			SettingsPojo neslSetting = EjbLookUps.getSettingsRemote().findById(neslCrg);
			double neslTotal = calculatePsbaCrgTotal(neslSetting, count);

			System.out.println("//-------------------------PSBA - BCP Revenue Share-------------------------------------------");

			//**
			double transRev = psbaTotal - neslTotal ;

			System.out.println("transRev  ----- =>  "+transRev);

			GstMasterPojo transPSBAGSt = EjbLookUps.getGstMasterRemote().findById(PSBA_AUDITEE);

			System.out.println("transPSBAGSt%  ----- =>  "+Double.parseDouble(transPSBAGSt.getPercentage())+"%");

			//**
			double transRevGST = calculateGst(transRev, Double.parseDouble(transPSBAGSt.getPercentage()));

			System.out.println("transRevGST  ----- =>  "+transRevGST);

			SettingsPojo razorpaySetting = EjbLookUps.getSettingsRemote().findById(RAZORPAY_CHARGES);

			//**
			double razorpayAmount = Double.parseDouble(razorpaySetting.getAmount());

			System.out.println("transRev razorpay charge Amount  ----- =>  "+razorpayAmount);

			GstMasterPojo razorpayGST = EjbLookUps.getGstMasterRemote().findById(RAZORPAY_PSBA);

			System.out.println("razorpayGST%  ----- =>  "+Double.parseDouble(razorpayGST.getPercentage())+"%");

			//**
			double razorpayRoutGST = calculateGst(razorpayAmount, Double.parseDouble(razorpayGST.getPercentage()));

			System.out.println("razorpayRoutGST  ----- =>  "+razorpayRoutGST);


			TdsMasterPojo razorpayTDS = EjbLookUps.getTdsMasterRemote().findById(RAZORPAY_TDS);

			System.out.println("razorpayGST%  ----- =>  "+Double.parseDouble(razorpayTDS.getPercentage())+"%");

			//**
			double razorpayRoutTDS = calculateGst(razorpayAmount, Double.parseDouble(razorpayTDS.getPercentage()));

			System.out.println("razorpayRoutTDS  ----- =>  "+razorpayRoutTDS);

			//**
			PSBA_BCP_Revenue_Share  = (transRev +transRevGST + razorpayAmount + razorpayRoutGST ) - (razorpayRoutTDS);

			PSBA_BCP_Revenue_Share = Math.round((PSBA_BCP_Revenue_Share)*100.0) / 100.0;

			System.out.println("PSBA_BCP_Revenue_Share  ----- =>  "+ df.format(PSBA_BCP_Revenue_Share));


			//--------------------------------------------------------------------

			System.out.println("//-------------------------NeSL - BCP revenue share-------------------------------------------");



			if (neslTotal != 0) {
				//**
				double neslTransRev = neslTotal ;
				System.out.println("nesltransRev  ----- =>  "+ df.format(neslTransRev));


				GstMasterPojo transNeslGSt = EjbLookUps.getGstMasterRemote().findById(NESL_PSBA);

				System.out.println("transNeslGSt%  ----- =>  "+Double.parseDouble(transNeslGSt.getPercentage())+"%");

				//**
				double NeslTransRevGST = calculateGst(neslTransRev, Double.parseDouble(transNeslGSt.getPercentage()));

				System.out.println("NeslRevGST  -----  =>  "+NeslTransRevGST);


				TdsMasterPojo NeslTDS = EjbLookUps.getTdsMasterRemote().findById(NESL_TDS);

				System.out.println("NeslTDSGST%  ----- =>  "+Double.parseDouble(NeslTDS.getPercentage())+"%");


				double NeslRevTDS = calculateGst(neslTransRev, Double.parseDouble(NeslTDS.getPercentage()));

				System.out.println("NeslRevTDS  ----- =>  "+NeslRevTDS);


				SettingsPojo neslInvoiceCrgPojo = EjbLookUps.getSettingsRemote().findById(NESL_INVOICE_CHARGES);

				//**
				double neslInvoiceCrg = Double.parseDouble(neslInvoiceCrgPojo.getAmount());

				System.out.println("neslInvoiceCrg  ----- =>  "+neslInvoiceCrg);

				//**
				double neslInvoiceCrgGST = calculateGst(neslInvoiceCrg, Double.parseDouble(transNeslGSt.getPercentage()));

				System.out.println("neslInvoiceCrgGST  ----- =>  "+neslInvoiceCrgGST);


				System.out.println("neslInvoiceCrgTDS%  ----- =>  "+Double.parseDouble(NeslTDS.getPercentage())+"%");

				//**
				double neslInvoiceCrgTDS = calculateGst(neslInvoiceCrg, Double.parseDouble(NeslTDS.getPercentage()));

				System.out.println("neslInvoiceCrgTDS  ----- =>  "+neslInvoiceCrgTDS);


				//**
				NeSL_BCP_Revenue_Share  = (neslTransRev +NeslTransRevGST + neslInvoiceCrg + neslInvoiceCrgGST ) - (NeslRevTDS) -(neslInvoiceCrgTDS);

				NeSL_BCP_Revenue_Share = Math.round((NeSL_BCP_Revenue_Share)*100.0) / 100.0;

				System.out.println("NeSL_BCP_Revenue_Share  ----- =>  "+ df.format(NeSL_BCP_Revenue_Share));


				//--------------------------------------------------------------------

				System.out.println("//------------------------- BANK BCP Revenue Share-------------------------------------------");

				//**
				double BankRevenue = Double.parseDouble(bankCrg);

				System.out.println("BankRevenue  ----- =>  "+ df.format(BankRevenue));


				GstMasterPojo BankRevenueGStPojo = EjbLookUps.getGstMasterRemote().findById(BANK_PSBA);

				System.out.println("BankRevenueGSt%  ----- =>  "+Double.parseDouble(BankRevenueGStPojo.getPercentage())+"%");

				//**
				double BankRevenueGSt = calculateGst(BankRevenue, Double.parseDouble(BankRevenueGStPojo.getPercentage()));

				System.out.println("BankRevenueGSt  -----  =>  "+BankRevenueGSt);

				String BankNum = BANKCount(Bank);

				System.out.println("Bank Number  -----  =>  "+BankNum);

				TdsMasterPojo bankBaseAmountPojo = EjbLookUps.getTdsMasterRemote().findById(BankNum);

				System.out.println("bankBaseAmount%  ----- =>  "+Double.parseDouble(bankBaseAmountPojo.getPercentage())+"%");

				//**
				double BankRevenueTDS = calculateGst(BankRevenue, Double.parseDouble(bankBaseAmountPojo.getPercentage()));

				System.out.println("BankRevenueTDS  ----- =>  "+BankRevenueTDS);



				BANK_BCP_Revenue_Share  = (BankRevenue +BankRevenueGSt) - (BankRevenueTDS);

				BANK_BCP_Revenue_Share = Math.round((BANK_BCP_Revenue_Share)*100.0) / 100.0;

				System.out.println("BANK_BCP_Revenue_Share  ----- =>  "+ df.format(BANK_BCP_Revenue_Share));



				//--------------------------------------------------------------------

				System.out.println("//------------------------- PSBA - TDS Collection Account-------------------------------------------");


				double NeslTransRevenTDS = NeslRevTDS ;

				System.out.println("NeslTransRevenTDS  ----- =>  "+NeslTransRevenTDS);

				double NeslInvoiceRevenTDS = neslInvoiceCrgTDS ;

				System.out.println("neslInvoiceCrgTDS  ----- =>  "+NeslInvoiceRevenTDS);

				double BankRevenTDS = BankRevenueTDS ;

				System.out.println(bankCrg+" "+"RevenTDS  ----- =>  "+BankRevenTDS);

				double RazorpayRoutCrgTDS = razorpayRoutTDS ;

				System.out.println("razorpayRoutTDS  ----- =>  "+RazorpayRoutCrgTDS);


				PSBA_TDS_Collection = (NeslTransRevenTDS + NeslInvoiceRevenTDS + BankRevenTDS + RazorpayRoutCrgTDS);

				PSBA_TDS_Collection = Math.round((PSBA_TDS_Collection)*100.0) / 100.0;				

				System.out.println("PSBA_TDS_Collection  ----- =>  "+PSBA_TDS_Collection);



				Total = (PSBA_BCP_Revenue_Share + NeSL_BCP_Revenue_Share + BANK_BCP_Revenue_Share + PSBA_TDS_Collection);

				Total = Math.round((Total)*100.0) / 100.0;				

				System.out.println("Total  ----- =>  "+df.format(Total));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		res.setErrorCode("SUCCESS");
		res.setPSBABCPRevenueShare(PSBA_BCP_Revenue_Share);
		res.setNeSLBCPRevenue_Share(NeSL_BCP_Revenue_Share);
		res.setBANKBCPRevenue_Share(BANK_BCP_Revenue_Share);
		res.setPSBATDSCollection(PSBA_TDS_Collection);
		res.setTotal(Total);

		return res ;

	}


	private static String BANKCount(String count) {
		if (count == null) {
			throw new IllegalArgumentException("Bank Count cannot be null");
		}

		if (count.equalsIgnoreCase("BANK 1")) {
			return BANK1_TDS;
		} else if (count.equalsIgnoreCase("BANK 2")) {
			return BANK2_TDS;
		} else if (count.equalsIgnoreCase("BANK 3")) {
			return BANK3_TDS;
		} else if (count.equalsIgnoreCase("BANK 4")) {
			return BANK4_TDS;
		} else {
			return BANK5_TDS;
		}
	}



	public static EntityResponce genTaxInvoice (EntityRequest requestXsd) throws Exception {
		
		EntityResponce responseXsd = new EntityResponce();
	
	List<BankApiLogPojo> bankApi = null;
	PropertiesReader pro = new PropertiesReader();
	BankApiLogPojo pojo = null;
	String pdfPath1 = null;
	String pdfPath2 = null;
	
	try {

		System.out.println("Creating PDF - Start");
		Date date = new Date();
	    SimpleDateFormat datefor = new SimpleDateFormat("yyyy-MM-dd");

		TaxInvoice invoice = new TaxInvoice();
		
		
		String baseFolder = pro.getUrlProperty("baseFolder");
		String url = pro.getUrlProperty("DomainURL");


		String invoicePath = baseFolder + File.separator +"Invoice_PDF" + File.separator ;

		bankApi = EjbLookUps.getBankApiLogRemote().findByProperty("pan", requestXsd.getPan());

		BalanceConfirmPojo balancePojo = EjbLookUps.getBalanceConfirmRemote().findById(requestXsd.getTxnId());

		String pdfPath = invoicePath + "Invoice_"+requestXsd.getTxnId()+".pdf";

	
		String taxSourceTempPDF = baseFolder + "tax_invoice.pdf";
		System.out.println("taxSourceTempPDF==> "+taxSourceTempPDF);
		
		String taxPath = baseFolder + File.separator +"Tax_Invoice_PDF" + File.separator ;
		String taxPdfPath = taxPath + "Tax_Invoice_"+requestXsd.getTxnId()+".pdf";
		
		System.out.println("taxPdfPath----> "+taxPdfPath);

		pdfPath1 = url + File.separator+"Tax_Invoice_PDF" + File.separator + "Tax_Invoice_"+requestXsd.getTxnId()+".pdf";

		
		  if((balancePojo.getAccountCount() == null) && (balancePojo.getBankCharges() == null) ) {
			  throw new Exception("AccountCount/BankCharges is not found in the Bank details table!!!"); 
			  }
		 

			String taxInvNo = generateTaxInvoiceNumber();
			System.out.println("invoice number : "+taxInvNo);
			
			System.out.println("AuditeeType--->"+requestXsd.getAuditeeType());
			
		EntityDetailsPojo entityPojo =  EjbLookUps.getEntityDetailsRemote().getEntityDetailsPojo(requestXsd.getPan() ,balancePojo.getAuditeeType());
			System.out.println("entitypojo--->"+entityPojo);
		if(entityPojo!=null) {
			System.out.println("getGstNo--->"+entityPojo.getGstNo());
			if(!ServiceUtils.isEmpty(entityPojo.getGstNo()))
			{
				String zohoTxn = "";
				String sp[] = taxInvNo.split("/");
				//   PSBA/DBCP/FY202425/PI/34
				//PSBA/TI/DBC2425/87
				zohoTxn = sp[2]+"/"+sp[3];
				
				System.out.println("zohoTxn-----> "+zohoTxn);
				if(ServiceUtils.isEmpty(balancePojo.getAckNo()) || ServiceUtils.isEmpty(balancePojo.getInvRefNum()) || ServiceUtils.isEmpty(balancePojo.getQrLink()))
				{
					// Call ZOHO API
				callZohoAPI(entityPojo ,balancePojo, zohoTxn);
				}
			}
		}
			
			
			
			TaxInvoice taxinvoice = new TaxInvoice();
			byte[] pdfBytP2 = taxinvoice.genInvoicePdf(taxSourceTempPDF, requestXsd.getPan(), balancePojo.getAccountCount()
					, balancePojo.getBankCharges() , requestXsd.getTxnId(), taxInvNo);

		File f2 = new File(taxPath);
		if (!f2.exists()) {
			f2.mkdirs();
		}
		FileOutputStream fos2= new FileOutputStream(new File(taxPdfPath));
		fos2.write(pdfBytP2);
		fos2.flush();
		fos2.close();      

		try {

			BalanceConfirmPojo	balancePojo1 = new BalanceConfirmPojo();

			balancePojo1.setTxnId(requestXsd.getTxnId());
			balancePojo1.setTaxInvoicePath(pdfPath1);
			balancePojo1.setTaxInvNo(taxInvNo);
			EjbLookUps.getBalanceConfirmRemote().update(balancePojo1);

			PaymentReportDetailsPojo  repoPojo = new PaymentReportDetailsPojo();

			repoPojo.setTxnId(requestXsd.getTxnId());
			repoPojo.setTaxInvoiceNo(taxInvNo);
			repoPojo.setTaxInvoiceDate(datefor.format(date));
			EjbLookUps.getPaymentReportDetailsRemote().update(repoPojo);


		} catch (Exception e) {
			e.printStackTrace();
		}


		System.out.println("Creating PDF - END");

		responseXsd.setError(false);
		responseXsd.setPdfPath(pdfPath1);
		responseXsd.setPan(requestXsd.getPan());
		responseXsd.setBankCharges(balancePojo.getBankCharges());
		responseXsd.setAccountsCount(balancePojo.getAccountCount());
		responseXsd.setBank(balancePojo.getBank());


	}catch (Exception e) {
		e.printStackTrace();
		responseXsd.setError(true);
		responseXsd.setErrorMessage(e.getMessage());
		System.err.println("Exception----"+e.getMessage());
	}


	return responseXsd;
	}

	private static void callZohoAPI(EntityDetailsPojo entityPojo, BalanceConfirmPojo bcPojo, String invoiceNum) throws JSONException, IOException {
	
		
		try {
			
		// Create root JSON object
		JSONObject root = new JSONObject();

		// Create Contact object
		JSONObject contact = new JSONObject();
		contact.put("contact_name", bcPojo.getAuditeeName());   // M
		contact.put("company_name", entityPojo.getName());	// M
		contact.put("currency_code", "INR");

		// Create Billing Address object
		JSONObject billingAddress = new JSONObject();  // M
		billingAddress.put("attention", entityPojo.getName());
		billingAddress.put("address", entityPojo.getBillingAddress());
		billingAddress.put("street2", entityPojo.getBillingAddress());
		billingAddress.put("state_code", "");
		billingAddress.put("city", entityPojo.getState());
		billingAddress.put("state", entityPojo.getState());
		billingAddress.put("zip", entityPojo.getBillingPincode());
		billingAddress.put("country", "India");
		billingAddress.put("phone", entityPojo.getMobileNo());
		billingAddress.put("fax", "");

		// Add billing and shipping addresses
		contact.put("billing_address", billingAddress);

//		contact.put("gst_no", "27AAECC2628R1Z7");			// M
		contact.put("gst_no", entityPojo.getGstNo());			// M
		contact.put("gst_treatment", "business_gst");		// M

		// Add Contact object to root
		root.put("contact", contact);

		// Create Invoice object
		JSONObject invoice = new JSONObject();
		invoice.put("place_of_supply", "KA");			// M
		invoice.put("invoice_number", invoiceNum);		// M

		Date date = new Date();
		SimpleDateFormat datefor = new SimpleDateFormat("yyyy-MM-dd");
		invoice.put("date", datefor.format(date));			// M

		// Line Items Array
		JSONArray lineItems = new JSONArray();			// M
		JSONObject lineItem = new JSONObject();
		lineItem.put("description", "Balance Certificate");
		lineItem.put("rate", bcPojo.getTotalAmount());
		lineItem.put("quantity", 1);
		//		lineItem.put("unit", "kgs");
		lineItem.put("product_type", "goods");
		lineItem.put("hsn_or_sac", 39089010);
		lineItem.put("tax_name", "GST0");  // IGST18, GST18	
		lineItems.put(lineItem);

		invoice.put("line_items", lineItems);

		invoice.put("seller_gstin", "29AAFCN2842P1ZV");				// M
		invoice.put("shipping_charge_tax_name", "GST0");		
		invoice.put("shipping_charge_sac_code", 39089010);				// M

		// Add Invoice object to root
		root.put("invoice", invoice);

		// Print the JSON string
		System.out.println("ZOHO IRN request"+root); 

		String token = ZohoApiIRN.genToken();
		
		PropertiesReader pro = new PropertiesReader();
		String bcpWebUrl = pro.getUrlProperty("BCPWebURL");

		String Url =bcpWebUrl+"/ZohoIRN";
		
		System.out.println("-------------web url ------------: "+Url);
		
//		String zohoUrl = "https://einvoice.zoho.in/api/v3/einvoices/invoices";
		String zohoUrl = EjbLookUps.getSettingsRemote().findById("ZOHO_IRN_URL").getStringValue();

		
		HttpResponse<String> tokenResponse = Unirest.post(Url)
				.header("Authorization", "Zoho-oauthtoken "+token)
				.header("X-com-zoho-invoice-organizationid", "60034984569")
				.header("Content-Type", "application/json")
			    .header("zoho_url", zohoUrl) // Forward the `api_url` header
			    .body(root.toString()) // Include the JSON payload as the body
			    .asString();

/*		OkHttpClient client = new OkHttpClient().newBuilder()
				.build();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, root.toString() );
		Request request = new Request.Builder()
				.url("https://einvoice.zoho.in/api/v3/einvoices/invoices")
				.method("POST", body)
				.addHeader("Authorization", "Zoho-oauthtoken "+token)
				.addHeader("X-com-zoho-invoice-organizationid", "60034984569")
				.addHeader("Content-Type", "application/json")
				.build();
		Response response = client.newCall(request).execute();

		String resp  = response.body().string();

*/
		String resp  = tokenResponse.getBody();
		
		
		JSONObject responseJson = new JSONObject(resp);

		// Check the code to determine success or failure
		int code = responseJson.getInt("code");
		if (code == 0) { // Success case
			System.out.println("API call successful:");

			// Extract 'einvoice_details' if present
			if (responseJson.has("invoice")) {
				JSONObject invoiceRes = responseJson.getJSONObject("invoice");

				if (invoiceRes.has("einvoice_details")) {
					JSONObject einvoiceDetails = invoiceRes.getJSONObject("einvoice_details");

					// Print extracted einvoice_details
					System.out.println("E-Invoice Details:");
					System.out.println("Is Cancellable: " + einvoiceDetails.getBoolean("is_cancellable"));
					System.out.println("Invoice Reference Number: " + einvoiceDetails.getString("inv_ref_num"));
					System.out.println("Status: " + einvoiceDetails.getString("status"));
					System.out.println("Ack Number: " + einvoiceDetails.getString("ack_number"));
					String link = einvoiceDetails.getString("qr_link");
					System.out.println("QR Link: " + link);
					System.out.println("Ack Date: " + einvoiceDetails.getString("ack_date"));
					
					String ackno = einvoiceDetails.getString("ack_number");
					String ackDate = einvoiceDetails.getString("ack_date");
					String incNumber = einvoiceDetails.getString("inv_ref_num");
					
					
					BalanceConfirmPojo bcUpdatePojo = new BalanceConfirmPojo();
					
					bcUpdatePojo.setTxnId(bcPojo.getTxnId());
					bcUpdatePojo.setAckNo(ackno);
					bcUpdatePojo.setAckDate(ackDate);
					bcUpdatePojo.setInvRefNum(incNumber);
					bcUpdatePojo.setQrLink(link);
					
					System.out.println("zoho api details udpated");
					EjbLookUps.getBalanceConfirmRemote().update(bcUpdatePojo);
					
					
				} else {
					System.out.println("E-Invoice Details not found in the response.");
				}
			} else {
				System.out.println("Invoice details not found in the response.");
			}
		} else { // Failure case
			System.out.println("API call failed with code: " + code);
			System.out.println("Message: " + responseJson.getString("message"));
		}
	
		} catch (Exception e) {
			System.out.println("Error closing resources: " + e.getMessage());
		}
	}

	
	private static String generateTaxInvoiceNumber() {

		String pInvoiceNum = null;
		Connection conn = null;
		CallableStatement stmt = null;

		try {

			String jdbcUrl = EjbLookUps.getSettingsRemote().findById("JDBC_URL").getStringValue(); // Replace with your DB URL
			String dbUser = EjbLookUps.getSettingsRemote().findById("DB_USERNAME").getStringValue();  // Replace with your DB username
			String dbPassword = EjbLookUps.getSettingsRemote().findById("DB_PASSWORD").getStringValue();  // Replace with your DB password
			conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPassword);

			System.out.println("Connected to the database.");

			// Prepare the callable statement for the stored procedure
			String storedProcedure = "{ CALL GenerateTaxInvoiceNumber(?) }";
			stmt = conn.prepareCall(storedProcedure);

			// Register the OUT parameter
			stmt.registerOutParameter(1, Types.VARCHAR);

			// Execute the stored procedure
			stmt.execute();

			// Retrieve the output parameter value
			String invoiceNumber = stmt.getString(1);

			// Display the invoice number
			System.out.println("Generated TAX Invoice Number: " + invoiceNumber);


			return invoiceNumber;

		}catch (Exception e) {
			e.printStackTrace();
		}
	
		finally {
		try {
			// Step 5: Close resources
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		} catch (SQLException e) {
			System.out.println("Error closing resources: " + e.getMessage());
		}
	}
		return pInvoiceNum;

	}


	public static void main(String[] args) {

		String sourceTempPDF = "C:\\Users\\91938\\Desktop\\ProformaInvoiceV1.pdf";
		String outputPdfPath = "C:\\Users\\91938\\Desktop\\Invoice.pdf";

		String pan = "ABCDE1234F";
		String countOfAcc = "100";
		String bankCrg = "300";
		String invoice = "65511365";

		try {
			TaxInvoice invoiceCalc = new TaxInvoice();

			//	byte[] pdfBytes = invoiceCalc.genInvoicePdf(sourceTempPDF, pan, countOfAcc, bankCrg ,invoice);

			//	Files.write(Paths.get(outputPdfPath), pdfBytes);

			System.out.println("Invoice PDF generated successfully at: " + outputPdfPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
