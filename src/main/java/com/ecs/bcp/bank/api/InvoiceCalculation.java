
package com.ecs.bcp.bank.api;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.ecs.bcp.pojo.EntityDetailsPojo;
import com.ecs.bcp.pojo.SettingsPojo;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.xsd.EntityResponce;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class InvoiceCalculation {


	private static AcroFields form;
	ByteArrayOutputStream baos = new ByteArrayOutputStream();

	public byte[]  genInvoicePdf(String sourceTempPDF,String pan,String countOfAcc,String bankCrg) throws Exception {

		EntityResponce responseXsd = new EntityResponce();
		SettingsPojo setPojo = null;
		List<EntityDetailsPojo> userPojo = null;
		DecimalFormat df = new DecimalFormat("0.00");

		String PsbaCrg ="";
		String NeslInvoiceCrg = "Nesl_Invoicing_Charges_Per_Invoice";
		String razorpayCrg ="Razorpay_Route_Charges_Fixed";
		double GST =0.00;

		try {


			final PdfReader pdfTemplate = new PdfReader(sourceTempPDF);
			final PdfStamper stamper = new PdfStamper(pdfTemplate, this.baos);
			this.form = stamper.getAcroFields();
			this.form.setGenerateAppearances(true);
			SimpleDateFormat sysDate = new SimpleDateFormat("dd-MM-yyyy");

			setField("PRO_DATE", sysDate.format(new Date()));
			setField("PRO_NO", pan+"4");

			setField("PRO_BANK_CHARGES", Double.valueOf(bankCrg));
			
			System.out.println("Integer.parseInt(countOfAcc)-------------------------"+countOfAcc);
			System.out.println("Integer.parseInt(countOfAcc)-------------------------"+Integer.parseInt(countOfAcc));

			System.out.println("Integer.parseInt(PAN)-------------------------"+pan);

			if(Integer.parseInt(countOfAcc) <= 25) {

				System.out.println("value-------A--------->> "+countOfAcc);

				PsbaCrg = "Psba_1-25";

			}else if((Integer.parseInt(countOfAcc) >= 26) && (Integer.parseInt(countOfAcc) <= 100) ) {

				System.out.println("value-------B--------->> "+countOfAcc);

				PsbaCrg = "Psba_26-100";

			}else if(Integer.parseInt(countOfAcc) >= 101) {

				System.out.println("value-------C--------->> "+countOfAcc);

				PsbaCrg = "Psba_ABOVE 100";
			}else {
				System.out.println("value-------D--------->> "+countOfAcc);

				PsbaCrg = "";
			}

			setPojo = EjbLookUps.getSettingsRemote().findById(PsbaCrg);

			if (setPojo != null ) {

				double psbaTotal = (Integer.parseInt(setPojo.getAmount())) * (Integer.parseInt(countOfAcc));

				System.out.println("psbaTotal---->>  "+psbaTotal);

				setField("PRO_PSBA_CHARGES", psbaTotal);

				setPojo = EjbLookUps.getSettingsRemote().findById(NeslInvoiceCrg);

				String neslCharger = setPojo.getAmount();


				setPojo = EjbLookUps.getSettingsRemote().findById(razorpayCrg);

				String razorpayCharger = setPojo.getAmount();


				double invoicTotal = (Integer.parseInt(neslCharger)) + (Integer.parseInt(razorpayCharger));

				System.out.println("invoicTotal---->>  "+invoicTotal);

				setField("PRO_INVO_CHARGES", invoicTotal);

				double GrossInvoce = (Integer.parseInt(bankCrg)) + (psbaTotal) +(invoicTotal);

				System.out.println("GrossInvoce---->>  "+GrossInvoce);

				setField("PRO_GROSS_INVOICE",GrossInvoce);

				userPojo = EjbLookUps.getEntityDetailsRemote().findByPan(pan);


				String state =userPojo.get(0).getState();

				if((userPojo != null) && (state != null)) {

					if(state.equalsIgnoreCase("Maharashtra")) {

						double gst = calculateGst(GrossInvoce , 9);
						String formattedGstAmount9 = df.format(gst);

						setField("PRO_CGST_AMOUNT", formattedGstAmount9);
						setField("PRO_SGST_AMOUNT", formattedGstAmount9);

						setField("PRO_CGST", "9%");
						setField("PRO_SGST", "9%");

//						/GST = (Integer.parseInt(formattedGstAmount9)) + (Integer.parseInt(formattedGstAmount9));
						GST = gst+gst;

					}else {

						double gst = calculateGst(GrossInvoce, 18);

						String formattedGstAmount = df.format(gst);
						setField("PRO_IGST", "18%");
						setField("PRO_IGST_AMOUNT", formattedGstAmount);

						GST = gst;

					}

					System.out.println("GST ---------->>"+GST);

					double invoiceAmount = ( (GrossInvoce) + (GST) );
					String formattedGstAmount = df.format(invoiceAmount);
					setField("PRO_INVO_AMOUNT", formattedGstAmount);

				}
			}

			System.out.println("PdfCreated ---------  ");
			stamper.setFormFlattening(true);
			stamper.close();
			pdfTemplate.close();

		} catch (Exception e) {

			System.out.println("Exception ---------  "+e.getMessage());
			e.printStackTrace();
		}
		return this.baos.toByteArray();
	}

	public void setField(String fieldName, Double value) throws IOException, DocumentException {
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
		double gstAmount = amount * gstRate / 100;
		return  gstAmount;
	}


	

}
