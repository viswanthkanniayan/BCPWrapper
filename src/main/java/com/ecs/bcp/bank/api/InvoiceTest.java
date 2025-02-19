package com.ecs.bcp.bank.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.ecs.bcp.pojo.BankApiLogPojo;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.utils.PropertiesReader;
import com.ecs.bcp.xsd.EntityRequest;
import com.ecs.bcp.xsd.EntityResponce;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class InvoiceTest {
	
	


    private AcroFields form;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    // Method to set form fields in the PDF
    public void setField(String fieldName, String value) throws IOException, DocumentException {
        if (value != null) {
            this.form.setField(fieldName, value);
        }
    }

    // Method to generate PDF by filling form fields
    public byte[] genPdf(String sourceTempPDF) {
        PdfReader pdfTemplate = null;
        PdfStamper stamper = null;
        
        try {
            // Initialize PdfReader and PdfStamper manually
            FileInputStream input = new FileInputStream(sourceTempPDF);
            pdfTemplate = new PdfReader(input);
            stamper = new PdfStamper(pdfTemplate, this.baos);

            // Get the form fields in the PDF
            this.form = stamper.getAcroFields();
            this.form.setGenerateAppearances(true);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");

            // Set hardcoded values in the form fields
            setField("PRO_DATE", dateFormat.format(new Date()));
            setField("PRO_NO", "1234567890");
            setField("PRO_BANK_CHARGES", "300.00");
            setField("PRO_PSBA_CHARGES", "1250.00");
            setField("PRO_INVO_CHARGES", "6.00");
            setField("PRO_GROSS_INVOICE", "1556.00");
            setField("PRO_IGST", "18%");
            setField("PRO_CGST", "");
            setField("PRO_SGST", "");
            setField("PRO_IGST_AMOUNT", "280.08");
            setField("PRO_CGST_AMOUNT", "");
            setField("PRO_SGST_AMOUNT", "");
            setField("PRO_INVO_AMOUNT", "1836.08");

            // Flatten the form to make fields non-editable
            stamper.setFormFlattening(true);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close PdfStamper and PdfReader manually
            try {
                if (stamper != null) {
                    stamper.close();
                }
                if (pdfTemplate != null) {
                    pdfTemplate.close();
                }
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
            }
        }
        return this.baos.toByteArray();
    }
    
 public static String gendratepdf (EntityRequest requestXsd) throws Exception {
		
		EntityResponce responseXsd = new EntityResponce();
		PropertiesReader prop = new PropertiesReader();
		List<BankApiLogPojo> bankApi = null;
		BankApiLogPojo pojo = null;
		try {

			System.out.println("Creating PDF - Start");
			
			TaxInvoice invoice = new TaxInvoice();
			String baseFolder = prop.getUrlProperty("baseFolder");
			
			String sourceTempPDF = baseFolder + "ProformaInvoiceV1.pdf";
			
			System.out.println("sourceTempPDF-------->>  "+sourceTempPDF);

			String invoicePath = baseFolder + File.separator +"Invoice_PDF" + File.separator ;

			String pdfPath = invoicePath + "Invoice_"+requestXsd.getPan()+".pdf";
			
			bankApi = EjbLookUps.getBankApiLogRemote().findByProperty("pan", requestXsd.getPan());
			
			
			if(bankApi == null) {
				throw new Exception("Pan No not found in the Bank API !!!");
			}
			pojo = bankApi.get(0);
			
			
	//		byte[] pdfBytP1 = invoice.genInvoicePdf(sourceTempPDF, requestXsd.getPan(), pojo.getAccountsCount()
	//				, pojo.getBankCharges() , pojo.getUniqueTxnId());
	      
			File f1 = new File(invoicePath);
	        if (!f1.exists()) {
	          f1.mkdirs();
	        }
	        FileOutputStream fos1 = new FileOutputStream(new File(pdfPath));
	 //       fos1.write(pdfBytP1);
	        fos1.flush();
	        fos1.close();
	        System.out.println("Creating PDF - END");
		}catch (Exception e) {
			e.printStackTrace();
			System.err.println("Exception----"+e.getMessage());
		}
		return "SUCCESS";
			
	}
    
    
    
    
    // Main method to run the PDF generation
    public static void main(String[] args) throws IOException {
        System.out.println("Creating PDF - Start");

        // Initialize the Main class
        InvoiceTest generatePdf = new InvoiceTest();

        // Path to the source template PDF
        String sourceTempPDF = "D:\\Bcp_files\\Proforma Invoice.pdf";

        // Path to save the generated PDF
        String outputPath = "D:\\InvoiceTest\\Invoice.pdf";

        // Generate the PDF with hardcoded values
        byte[] pdfData = generatePdf.genPdf(sourceTempPDF);

        // Write the generated PDF to a file
        try (FileOutputStream out = new FileOutputStream(outputPath)) {
            out.write(pdfData);
        }

        System.out.println("Creating PDF - End");
    }
}


