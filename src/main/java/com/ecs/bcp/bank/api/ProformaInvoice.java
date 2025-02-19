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
import java.util.Date;
import java.util.List;

import com.ecs.bcp.pojo.BalanceConfirmPojo;
import com.ecs.bcp.pojo.BankApiLogPojo;
import com.ecs.bcp.pojo.EntityDetailsPojo;
import com.ecs.bcp.pojo.GstMasterPojo;
import com.ecs.bcp.pojo.PaymentReportDetailsPojo;
import com.ecs.bcp.pojo.SettingsPojo;
import com.ecs.bcp.pojo.TdsMasterPojo;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.utils.PropertiesReader;
import com.ecs.bcp.xsd.EntityRequest;
import com.ecs.bcp.xsd.EntityResponce;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class ProformaInvoice {

	

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

	public byte[] genInvoicePdf(String sourceTempPDF, String pan, String countOfAcc, String bankCrg , String TxnId ,String ProfinvoiceNo) throws Exception {
		PdfReader pdfTemplate = null;
		PdfStamper stamper = null;
		DecimalFormat df = new DecimalFormat("0.00");

		EntityDetailsPojo pojo = new EntityDetailsPojo();
		BalanceConfirmPojo balanceDetails = new BalanceConfirmPojo();
		try {

			pdfTemplate = new PdfReader(sourceTempPDF);
			stamper = new PdfStamper(pdfTemplate, this.baos);

			form = stamper.getAcroFields();
			form.setGenerateAppearances(true);

			System.out.println("countOfAcc -----------"+countOfAcc);
			System.out.println("bankCrg -----------"+bankCrg);
			System.out.println("invoiceNo -----------"+TxnId);

			setField("INV_NO", ProfinvoiceNo );


			SimpleDateFormat sysDate = new SimpleDateFormat("dd-MM-yyyy");
			setField("INV_DATE", sysDate.format(new Date()));
			setField("PAN", pan.toUpperCase());
			setField("PRO_BANK_CHARGES",  df.format(Double.parseDouble(bankCrg)));


			setField("R1", df.format(Double.parseDouble(bankCrg)));
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

			setField("R3", df.format(invoiceTotal));
			setField("TAX3", df.format(invoiceTotal));

			double grossInvoice = calculateGrossInvoice(bankCrg, psbaTotal, invoiceTotal);

			setField("TOTAL_TAX_VALUE",  df.format(grossInvoice));

	/*		List<EntityDetailsPojo> userPojo = EjbLookUps.getEntityDetailsRemote().findByPan(pan);
			if (userPojo != null && !userPojo.isEmpty()) {

				pojo = userPojo.get(0);   */
			 balanceDetails = EjbLookUps.getBalanceConfirmRemote().findById(TxnId);
			if (balanceDetails != null) {
			
				EntityDetailsPojo entityPojo = EjbLookUps.getEntityDetailsRemote().getEntityDetailsPojo(pan , "AUDITEE");
				if (entityPojo != null) {


					calculateAndSetGst(entityPojo, grossInvoice, df , TxnId );

					setField("ADDRESS", entityPojo.getBillingAddress().toUpperCase());
					setField("GSTIN_1","test_Invoice");
					setField("NAME",entityPojo.getName().toUpperCase() );
					setField("ADDRESS", entityPojo.getBillingAddress().toUpperCase());
					setField("STATE", entityPojo.getState().toUpperCase());
					setField("PINCODE", entityPojo.getBillingPincode());
					String gstin = entityPojo.getGstNo(); 
					setField("CERT_ID",  balanceDetails.getTxnId());

					if (gstin == null || gstin.isEmpty()) {
					    setField("GSTIN", "Not provided");
					} else {
					    setField("GSTIN", gstin.toUpperCase());
					}

					setField("BANK_NAME", balanceDetails.getBank().toUpperCase());

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
		return Double.parseDouble(bankCrg) + psbaTotal + invoiceTotal;
	}

	private void calculateAndSetGst(EntityDetailsPojo user, double grossInvoice, DecimalFormat df ,String TxnId ) throws IOException, DocumentException {
		String state = user.getState();
		System.out.println("User state  =>   "+state);
		double gstRate = MAHARASHTRA_STATE.equalsIgnoreCase(state) ? 9 : 18;

		
		
		double gstAmount = calculateGst(grossInvoice, gstRate);
		String formattedGstAmount = df.format(gstAmount);
		
		
		
		System.out.println("formattedGstAmount  =>   "+formattedGstAmount);
		if (gstRate == 9) {



			setField("CGST", formattedGstAmount);
			setField("SGST", formattedGstAmount);

			/*	setField("PRO_CGST", "9%");
			setField("PRO_SGST", "9%"); */
			setField("IGST", "0.00");
			
			double GSTTotalAmount = ((Double.parseDouble(formattedGstAmount))*2);
			
			setField("TOTAL_GST", GSTTotalAmount);
			System.out.println("total Gst Amount =>   "+GSTTotalAmount);
			
		} else {
			setField("TOTAL_GST", formattedGstAmount);
			
			setField("IGST", formattedGstAmount);
			
			setField("CGST", "0.00");
			setField("SGST", "0.00");
			//	setField("PRO_IGST", "18%");

			System.out.println("formated gst Amount =>   "+formattedGstAmount);
		}

		double invoiceAmount = grossInvoice + gstAmount * (gstRate == 9 ? 2 : 1);
		
		setField("TOTAL_AMOUNT", df.format(invoiceAmount));
		
		System.out.println("total Amount =>   "+invoiceAmount);
		
	//	int totalAmountInInteger = (int) Math.round(invoiceAmount);
		
	    String totalAmountInWords = NumberToWordsConverter.numberToWords(invoiceAmount);

	   // setField("TOTAL_IN_WORD", totalAmountInWords + " Only");
	  //  setField("TOTAL_IN_WORD", "Rupees " + totalAmountInWords + " Only");
	    
	    setField("TOTAL_IN_WORD", (totalAmountInWords + " Only").toUpperCase());

	    
		try {
			BalanceConfirmPojo balancePojo = new BalanceConfirmPojo();

			balancePojo.setTxnId(TxnId);
			balancePojo.setTotalAmount(df.format(invoiceAmount));
			EjbLookUps.getBalanceConfirmRemote().update(balancePojo);

			PaymentReportDetailsPojo repoPojo = new PaymentReportDetailsPojo();

	        repoPojo.setTxnId(TxnId);
	        repoPojo.setState(state);

	        if (gstRate == 9) {
	          repoPojo.setCGST(formattedGstAmount);
	          repoPojo.setSGST(formattedGstAmount);
	          repoPojo.setIGST("");
	          double GSTTotalAmount = ((Double.parseDouble(formattedGstAmount))*2);
	          System.out.println("Total Gst Amount =>   "+GSTTotalAmount);
	          repoPojo.setTotalGST( Double.toString(GSTTotalAmount));
	        } else {
	          repoPojo.setTotalGST(formattedGstAmount);
	          repoPojo.setCGST("");
	          repoPojo.setSGST("");
	          repoPojo.setIGST(formattedGstAmount);
	          System.out.println("Total Gst Amount _=>   "+formattedGstAmount);
	        }

	        EjbLookUps.getPaymentReportDetailsRemote().update(repoPojo);
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public static double calculateGst(double amount, double gstRate) {
		return amount * gstRate / 100;
	}

	public static EntityResponce ReportRevenueCalculator(String countOfAcc, String bankCrg , String BankName, String Bank ,String ProfinvoiceNo , String TxnId) {

		EntityResponce res = new EntityResponce();
		DecimalFormat df = new DecimalFormat("0.00");
		PaymentReportDetailsPojo repPojo = new PaymentReportDetailsPojo();
		double PSBA_BCP_Revenue_Share = 0;
		double NeSL_BCP_Revenue_Share = 0;
		double BANK_BCP_Revenue_Share = 0;
		double PSBA_TDS_Collection = 0;
		double Total = 0;
		String neslCrg;

		try {

			Date date = new Date();
		    SimpleDateFormat datefor = new SimpleDateFormat("yyyy-MM-dd");

			double count = Double.parseDouble(countOfAcc);
			String psbaCrg = determinePsbaCharge(count);
			
			repPojo.setTxnId(TxnId);
			repPojo.setProfInvoiceNo(ProfinvoiceNo);
			repPojo.setProfInvoiceDate(datefor.format(date));
			repPojo.setCountAcc(countOfAcc);
			repPojo.setBankName(BankName);
			
			SettingsPojo psbaSetting = EjbLookUps.getSettingsRemote().findById(psbaCrg);
			double psbaTotal = calculatePsbaCrgTotal(psbaSetting, count);
			
			System.out.println("PSBA  Charges  :    "+psbaCrg);
			System.out.println("PSBA  Revenue  :    "+psbaTotal);
			
			repPojo.setPSBACharges(df.format(psbaTotal));
			

			neslCrg = determineNESLharge(count);

			SettingsPojo neslSetting = EjbLookUps.getSettingsRemote().findById(neslCrg);
			double neslTotal = calculatePsbaCrgTotal(neslSetting, count);

			System.out.println("NESL  Revenue  :    "+df.format(neslTotal));
			
			repPojo.setNeSLRevenue(df.format(neslTotal));
			
			System.out.println("//-------------------------PSBA - BCP Revenue Share-------------------------------------------");

			//**
			double transRev = psbaTotal - neslTotal ;
			
			
			double PSBA_NESL_Charge = psbaTotal - neslTotal ;

			System.out.println("PSBA transRev  ----- =>  "+df.format(transRev));
			
			repPojo.setPSBARevenue(df.format(transRev));
			
			GstMasterPojo transPSBAGSt = EjbLookUps.getGstMasterRemote().findById(PSBA_AUDITEE);

			System.out.println("PSBA transPSBAGSt%  ----- =>  "+Double.parseDouble(transPSBAGSt.getPercentage())+"%");
			

			//**
			double transRevGST = calculateGst(transRev, Double.parseDouble(transPSBAGSt.getPercentage()));

			System.out.println("PSBA transRevGST  ----- =>  "+df.format(transRevGST));
			
			repPojo.setPSBARevenueGST(df.format(transRevGST));

			SettingsPojo razorpaySetting = EjbLookUps.getSettingsRemote().findById(RAZORPAY_CHARGES);

			//**
			double razorpayAmount = Double.parseDouble(razorpaySetting.getAmount());

			System.out.println("PSBA transRev razorpay charge Amount  ----- =>  "+df.format(razorpayAmount));
			
			repPojo.setRazorpayRoutCharges(df.format(razorpayAmount));

			GstMasterPojo razorpayGST = EjbLookUps.getGstMasterRemote().findById(RAZORPAY_PSBA);

			System.out.println("PSBA razorpayGST%  ----- =>  "+Double.parseDouble(razorpayGST.getPercentage())+"%");

			//**
			double razorpayRoutGST = calculateGst(razorpayAmount, Double.parseDouble(razorpayGST.getPercentage()));

			System.out.println("PSBA razorpayRoutGST  ----- =>  "+df.format(razorpayRoutGST));

			repPojo.setRazorpayRoutGSTCharges(df.format(razorpayRoutGST));

			TdsMasterPojo razorpayTDS = EjbLookUps.getTdsMasterRemote().findById(RAZORPAY_TDS);

			System.out.println("PSBA razorpayGST%  ----- =>  "+Double.parseDouble(razorpayTDS.getPercentage())+"%");

			//**
			double razorpayRoutTDS = calculateGst(razorpayAmount, Double.parseDouble(razorpayTDS.getPercentage()));

			System.out.println("PSBA razorpayRoutTDS  ----- =>  "+df.format(razorpayRoutTDS));
			
			repPojo.setRazorpayRoutTDSCharges(df.format(razorpayRoutTDS));

			//**
			PSBA_BCP_Revenue_Share  = (transRev +transRevGST + razorpayAmount + razorpayRoutGST ) - (razorpayRoutTDS);

			PSBA_BCP_Revenue_Share = Math.round((PSBA_BCP_Revenue_Share)*100.0) / 100.0;

			System.out.println("PSBA_BCP_Revenue_Share  ----- =>  "+ df.format(PSBA_BCP_Revenue_Share));
			
			repPojo.setPSBARevenuePayable(df.format(PSBA_BCP_Revenue_Share));


			//--------------------------------------------------------------------

			System.out.println("//-------------------------NeSL - BCP revenue share-------------------------------------------");



			if (neslTotal != 0) {
				//**
				double neslTransRev = neslTotal ;
				System.out.println("nesltransRev  ----- =>  "+ df.format(neslTransRev));


				GstMasterPojo transNeslGSt = EjbLookUps.getGstMasterRemote().findById(NESL_PSBA);

				System.out.println("NeSL transNeslGSt%  ----- =>  "+Double.parseDouble(transNeslGSt.getPercentage())+"%");

				//**
				double NeslTransRevGST = calculateGst(neslTransRev, Double.parseDouble(transNeslGSt.getPercentage()));

				System.out.println("Nesl  RevGST  -----  =>  "+df.format(NeslTransRevGST));

				repPojo.setNeSLRevenueGST(df.format(NeslTransRevGST));

				TdsMasterPojo NeslTDS = EjbLookUps.getTdsMasterRemote().findById(NESL_TDS);

				System.out.println("Nesl  TDSGST%  ----- =>  "+Double.parseDouble(NeslTDS.getPercentage())+"%");


				double NeslRevTDS = calculateGst(neslTransRev, Double.parseDouble(NeslTDS.getPercentage()));

				System.out.println("Nesl  RevTDS  ----- =>  "+df.format(NeslRevTDS));

				repPojo.setNeSLRevenueTDS(df.format(NeslRevTDS));

				SettingsPojo neslInvoiceCrgPojo = EjbLookUps.getSettingsRemote().findById(NESL_INVOICE_CHARGES);

				//**
				double neslInvoiceCrg = Double.parseDouble(neslInvoiceCrgPojo.getAmount());

				System.out.println("nesl Invoice Crg  ----- =>  "+df.format(neslInvoiceCrg));
				
				repPojo.setNeSLInvoiceCharges(df.format(neslInvoiceCrg));

				//**
				double neslInvoiceCrgGST = calculateGst(neslInvoiceCrg, Double.parseDouble(transNeslGSt.getPercentage()));

				System.out.println("nesl Invoice CrgGST  ----- =>  "+df.format(neslInvoiceCrgGST));
				
				repPojo.setNeSLInvoiceGSTCharges(df.format(neslInvoiceCrgGST));


				System.out.println("nesl InvoiceCrg TDS%  ----- =>  "+Double.parseDouble(NeslTDS.getPercentage())+"%");

				//**
				double neslInvoiceCrgTDS = calculateGst(neslInvoiceCrg, Double.parseDouble(NeslTDS.getPercentage()));

				System.out.println("nesl Invoice CrgTDS  ----- =>  "+df.format(neslInvoiceCrgTDS));

				
				repPojo.setNeSLInvoiceTDSCharges(df.format(neslInvoiceCrgTDS));

				//**
				NeSL_BCP_Revenue_Share  = (neslTransRev +NeslTransRevGST + neslInvoiceCrg + neslInvoiceCrgGST ) - (NeslRevTDS) -(neslInvoiceCrgTDS);

				NeSL_BCP_Revenue_Share = Math.round((NeSL_BCP_Revenue_Share)*100.0) / 100.0;

				System.out.println("NeSL_BCP_Revenue_Share  ----- =>  "+ df.format(NeSL_BCP_Revenue_Share));

				repPojo.setNeSLRevenuePayable(df.format(NeSL_BCP_Revenue_Share));

				//--------------------------------------------------------------------

				System.out.println("//------------------------- BANK BCP Revenue Share-------------------------------------------");

				//**
				double BankRevenue = Double.parseDouble(bankCrg);

				System.out.println("BankRevenue  ----- =>  "+ df.format(BankRevenue));
				
				
				repPojo.setBankRevenue(df.format(BankRevenue));

				GstMasterPojo BankRevenueGStPojo = EjbLookUps.getGstMasterRemote().findById(BANK_PSBA);

				System.out.println("BankRevenueGSt%  ----- =>  "+Double.parseDouble(BankRevenueGStPojo.getPercentage())+"%");

				//**
				double BankRevenueGSt = calculateGst(BankRevenue, Double.parseDouble(BankRevenueGStPojo.getPercentage()));

				System.out.println("BankRevenueGSt  -----  =>  "+df.format(BankRevenueGSt));
				
				repPojo.setBankRevenueGST(df.format(BankRevenueGSt));

				String BankNum = BANKCount(Bank);

				System.out.println("Bank Number  -----  =>  "+BankNum);

				TdsMasterPojo bankBaseAmountPojo = EjbLookUps.getTdsMasterRemote().findById(BankNum);

				System.out.println("bankBaseAmount%  ----- =>  "+Double.parseDouble(bankBaseAmountPojo.getPercentage())+"%");

				//**
				double BankRevenueTDS = calculateGst(BankRevenue, Double.parseDouble(bankBaseAmountPojo.getPercentage()));

				System.out.println("BankRevenueTDS  ----- =>  "+df.format(BankRevenueTDS));

				repPojo.setBankRevenueTDS(df.format(BankRevenueTDS));


				BANK_BCP_Revenue_Share  = (BankRevenue +BankRevenueGSt) - (BankRevenueTDS);

				BANK_BCP_Revenue_Share = Math.round((BANK_BCP_Revenue_Share)*100.0) / 100.0;

				System.out.println("BANK_BCP_Revenue_Share  ----- =>  "+ df.format(BANK_BCP_Revenue_Share));

				repPojo.setBankRevenuePayable(df.format(BANK_BCP_Revenue_Share));

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

				repPojo.setNeSLInvoicePayableCharges(df.format(PSBA_TDS_Collection));

				Total = (PSBA_BCP_Revenue_Share + NeSL_BCP_Revenue_Share + BANK_BCP_Revenue_Share + PSBA_TDS_Collection);

				Total = Math.round((Total)*100.0) / 100.0;		
				
				repPojo.setTotalInvoice(df.format(Total));

				System.out.println("Total  ----- =>  "+df.format(Total));
				
				double Nesl_Razorpay = (neslInvoiceCrg+razorpayAmount);

		        double GrossInvoice = (BankRevenue + PSBA_NESL_Charge + Nesl_Razorpay);
		        
		        repPojo.setGrossInvoice(df.format(GrossInvoice));
				
				EjbLookUps.getPaymentReportDetailsRemote().update(repPojo);

			}
		} catch (Exception e) {
			e.printStackTrace();
			res.setStatus("FAILED");
		}
		res.setStatus("SUCCESS");

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



	public static EntityResponce genProformaInvoice (EntityRequest requestXsd) throws Exception {
		
		EntityResponce responseXsd = new EntityResponce();
	
	List<BankApiLogPojo> bankApi = null;
	PropertiesReader pro = new PropertiesReader();
	BankApiLogPojo pojo = null;
	String pdfPath1 = null;
	
	try {

		System.out.println("Creating PDF - Start");

		ProformaInvoice invoice = new ProformaInvoice();
	//	Invoice taxinvoice = new Invoice();
		
		
		String baseFolder = pro.getUrlProperty("baseFolder");
		String url = pro.getUrlProperty("DomainURL");

		String sourceTempPDF = baseFolder + "profoma.pdf";

		System.out.println("sourceTempPDF-------->>  "+sourceTempPDF);

		String invoicePath = baseFolder + File.separator +"Invoice_PDF" + File.separator ;

		bankApi = EjbLookUps.getBankApiLogRemote().findByProperty("pan", requestXsd.getPan());

		BalanceConfirmPojo balancePojo = EjbLookUps.getBalanceConfirmRemote().findById(requestXsd.getTxnId());

		String pdfPath = invoicePath + "Profoma_Invoice_"+requestXsd.getTxnId()+".pdf";

		pdfPath1 = url + File.separator+"Invoice_PDF" + File.separator + "Profoma_Invoice_"+requestXsd.getTxnId()+".pdf";



		  if((balancePojo.getAccountCount() == null) && (balancePojo.getBankCharges() == null) ) {
			  throw new Exception("AccountCount/BankCharges is not found in the Bank details table!!!"); 
			  }
		 

		 String profInvNo = generateProformaInvoiceNumber();

		byte[] pdfBytP1 = invoice.genInvoicePdf(sourceTempPDF, requestXsd.getPan(), balancePojo.getAccountCount()
				, balancePojo.getBankCharges() , requestXsd.getTxnId(), profInvNo);

		File f1 = new File(invoicePath);
		if (!f1.exists()) {
			f1.mkdirs();
		}
		FileOutputStream fos1 = new FileOutputStream(new File(pdfPath));
		fos1.write(pdfBytP1);
		fos1.flush();
		fos1.close();
		
		EntityResponce entityRes =  ReportRevenueCalculator(balancePojo.getAccountCount(), balancePojo.getBankCharges(),balancePojo.getBank(), requestXsd.getBank(), profInvNo, requestXsd.getTxnId());
	      
	      if(entityRes.getStatus().contains("SUCCESS")) {
	        
	        System.out.println("Payment Report Details created SUCCESS ----------------------------->>>");
	      }


		try {

			BalanceConfirmPojo	balancePojo1 = new BalanceConfirmPojo();

			balancePojo1.setTxnId(requestXsd.getTxnId());
			balancePojo1.setInvoicePath(pdfPath1);
			balancePojo1.setProfInvNo(profInvNo);
			EjbLookUps.getBalanceConfirmRemote().update(balancePojo1);

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


	return responseXsd;}


	private static String generateProformaInvoiceNumber() {

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
			String storedProcedure = "{ CALL GenerateProformaInvoiceNumber(?) }";
			stmt = conn.prepareCall(storedProcedure);

			// Register the OUT parameter
			stmt.registerOutParameter(1, Types.VARCHAR);

			// Execute the stored procedure
			stmt.execute();

			// Retrieve the output parameter value
			String invoiceNumber = stmt.getString(1);

			// Display the invoice number
			System.out.println("Generated PROF Invoice Number: " + invoiceNumber);


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
	
	
	/*
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

	}   */



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
