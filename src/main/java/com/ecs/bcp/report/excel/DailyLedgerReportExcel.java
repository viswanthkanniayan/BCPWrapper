package com.ecs.bcp.report.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ecs.bcp.pojo.PaymentReportDetailsPojo;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.utils.PropertiesReader;
import com.ecs.bcp.utils.ServiceUtils;
import com.ecs.bcp.xsd.EntityRequest;
import com.ecs.bcp.xsd.EntityResponce;

public class DailyLedgerReportExcel {
	

	private static String[] columns = {"Date of Trn", "Trn ID", "Bank Name", "Gross Charges", "Invoice GST", "Total Invoice", "Bank Share","Bank GST", "Bank TDS", "Bank Net Payable", "NeSL Share", "NeSL GST", "NeSL TDS", "NeSL Net Payable","PSBA Share","PSBA GST","PSBA Net Amount"};

	public static EntityResponce dailyLedgerReportExcel(EntityRequest reqXSD) throws Exception {

		String filePath = "";
		String csvPath = "";
		EntityResponce responseXsd = new EntityResponce();

		List<PaymentReportDetailsPojo> pojoList = null;

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  

		try {


			PropertiesReader pro = new PropertiesReader();
			String baseFolder = pro.getUrlProperty("baseFolder");
			String url = pro.getUrlProperty("DomainURL");


			SimpleDateFormat folderDate = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat folderHour = new SimpleDateFormat("HH");
			SimpleDateFormat folderMin= new SimpleDateFormat("mm");
			SimpleDateFormat folderSec= new SimpleDateFormat("ss");

			String folderDateFormat = folderDate.format(new Date());
			String folderHourFormat = folderHour.format(new Date());
			String folderMinFormat = folderMin.format(new Date());
			String folderSecFormat = folderSec.format(new Date());

			String folderPath = baseFolder + "DAILY_LADGER_REPORT/"+folderDateFormat+File.separator+folderHourFormat+File.separator+folderMinFormat+File.separator+folderSecFormat+ File.separator;


			String folderURL = url+ File.separator + "DAILY_LADGER_REPORT/"+folderDateFormat+File.separator+folderHourFormat+File.separator+folderMinFormat+File.separator+folderSecFormat+ File.separator;


			File passportFolder=new File(folderPath);
			if(!passportFolder.exists())
			{
				System.out.println("Audit Log Folder Not Exists***********************");
				passportFolder.mkdirs();
			}


			filePath = folderPath + "LadgerReport"+"_"+reqXSD.getRequestDate() + ".xlsx";

			csvPath = folderURL + "LadgerReport" +"_"+reqXSD.getRequestDate() + ".xlsx";

			String endDate = ServiceUtils.getTomorrowDate(reqXSD.getRequestDate());
			System.out.println("Start Date ------"+reqXSD.getRequestDate());
			System.out.println("End Date ------"+endDate);

			pojoList = EjbLookUps.getPaymentReportDetailsRemote().getReportLedgerByDate(reqXSD.getRequestDate(), endDate);

			if(pojoList==null || pojoList.size()==0) {

				responseXsd.setError(true);
				responseXsd.setErrorDescription("No data is available for the specified date. Please check the date and try again.");
				return responseXsd;
			}

			System.out.println("---file path---"+filePath);

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Daily_Ledger_Report"+folderDate);

			CellStyle headerCellStyle = createHeaderCellStyle(workbook);
			XSSFRow headerRow = sheet.createRow(0);
			sheet.setAutoFilter(CellRangeAddress.valueOf("A1:G1"));


			for(int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerCellStyle);
			}

			int rowNum = 1;
			
			
			double totalGrossInvoice =0;
			double totalGst=0;
			double totalInvoice=0;
			
			double 	totalBankRevenue =0;
			double 	totalBankGST =0;
			double 	totalBankTDS =0;
			double 	totalBankPayable =0;
			
			double totalNeSLRevenue =0;
			double 	totalNeSLGST =0;
			double 	totalNeSLTDS =0;
			double 	totalNeSLPayable =0;
			
			double 	totalPSBARevenue =0;
			double totalPSBAGST =0;
			double totalPSBANetAmount =0;

			for (PaymentReportDetailsPojo pojo : pojoList) {

			    Row row = sheet.createRow(rowNum++);

			    if (!ServiceUtils.isEmpty(reqXSD.getRequestDate())) {

			    	double grossInvoice = Double.parseDouble(pojo.getBankRevenue()) 
		                    + Double.parseDouble(pojo.getNeSLInvoiceCharges()) 
		                    + Double.parseDouble(pojo.getRazorpayRoutCharges()) 
		                    + Double.parseDouble(pojo.getPSBACharges()) 
		                    + Double.parseDouble(pojo.getNeSLRevenue());

			        row.createCell(0).setCellValue(dateFormat.format(pojo.getCreationDate())); // Date of Trn
			        row.createCell(1).setCellValue(pojo.getTxnId()); // Trn ID
			        row.createCell(2).setCellValue(pojo.getBankName()); // Bank Name
			        row.createCell(3).setCellValue(grossInvoice); // Gross invoice
			        
			        row.createCell(4).setCellValue(pojo.getTotalGST()); // Invoice Total GST
			        row.createCell(5).setCellValue(pojo.getTotalInvoice()); // Total Invoice (Gross invoice + GST)
			        
			        row.createCell(6).setCellValue(pojo.getBankRevenue()); // Bank Share
			        row.createCell(7).setCellValue(pojo.getBankRevenueGST()); // Bank GST
			        row.createCell(8).setCellValue(pojo.getBankRevenueTDS()); // Bank TDS
			        row.createCell(9).setCellValue(pojo.getBankRevenuePayable()); // Bank Net Payable
			        
			        row.createCell(10).setCellValue(pojo.getNeSLRevenue()); // NeSL Share
			        row.createCell(11).setCellValue(pojo.getNeSLRevenueGST()); // NeSL GST
			        row.createCell(12).setCellValue(pojo.getNeSLRevenueTDS()); // NeSL TDS
			        row.createCell(13).setCellValue(pojo.getNeSLRevenuePayable()); // NeSL Net Payable
			        
			        row.createCell(14).setCellValue(pojo.getPSBARevenue()); // PSBA Share
			        row.createCell(15).setCellValue(pojo.getPSBARevenueGST()); // PSBA GST
			        row.createCell(16).setCellValue(pojo.getPSBARevenuePayable()); // PSBA Net Amount


			        // Accumulate totals for summary
			        
			        totalGrossInvoice += grossInvoice;
			        totalGst += Double.parseDouble(pojo.getTotalGST());
			        totalInvoice += Double.parseDouble(pojo.getTotalInvoice());
			        
			        totalBankRevenue += Double.parseDouble(pojo.getBankRevenue());
			        totalBankGST += Double.parseDouble(pojo.getBankRevenueGST());
			        totalBankTDS += Double.parseDouble(pojo.getBankRevenueTDS());
			        totalBankPayable += Double.parseDouble(pojo.getBankRevenuePayable());
			        
			        totalNeSLRevenue += Double.parseDouble(pojo.getNeSLRevenue());
			        totalNeSLGST += Double.parseDouble(pojo.getNeSLRevenueGST());
			        totalNeSLTDS += Double.parseDouble(pojo.getNeSLRevenueTDS());
			        totalNeSLPayable += Double.parseDouble(pojo.getNeSLRevenuePayable());
			        
			        totalPSBARevenue += Double.parseDouble(pojo.getPSBARevenue());
			        totalPSBAGST += Double.parseDouble(pojo.getPSBARevenueGST());
			        totalPSBANetAmount += Double.parseDouble(pojo.getPSBARevenuePayable());
			    }
			}

			// Create summary row for totals
			Row summaryRow = sheet.createRow(rowNum++);  
			CellStyle summaryCellStyle = createSummaryCellStyle(workbook);

			sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 1));
			Cell summaryCell = summaryRow.createCell(0);
			summaryCell.setCellValue("DAY END SUMMARY");
			summaryCell.setCellStyle(summaryCellStyle);
			summaryRow.createCell(1).setCellStyle(summaryCellStyle); // Apply style to merged cell

			Cell totalCell = summaryRow.createCell(2);
			totalCell.setCellValue("TOTAL");
			totalCell.setCellStyle(summaryCellStyle);
			
			summaryRow.createCell(3).setCellValue(totalGrossInvoice);
			summaryRow.createCell(4).setCellValue(totalGst); 
			summaryRow.createCell(5).setCellValue(totalInvoice);
			
			summaryRow.createCell(6).setCellValue(totalBankRevenue); 
			summaryRow.createCell(7).setCellValue(totalBankGST); 
			summaryRow.createCell(8).setCellValue(totalBankTDS); 
			summaryRow.createCell(9).setCellValue(totalBankPayable);
			
			summaryRow.createCell(10).setCellValue(totalNeSLRevenue); 
			summaryRow.createCell(11).setCellValue(totalNeSLGST); 
			summaryRow.createCell(12).setCellValue(totalNeSLTDS); 
			summaryRow.createCell(13).setCellValue(totalNeSLPayable); 
			
			summaryRow.createCell(14).setCellValue(totalPSBARevenue);
			summaryRow.createCell(15).setCellValue(totalPSBAGST);
			summaryRow.createCell(16).setCellValue(totalPSBANetAmount);

			// Apply summary cell style to all the cells in the summary row
			for (int i = 2; i <= 16; i++) {
			    Cell cell = summaryRow.getCell(i); 
			    if (cell == null) { 
			        cell = summaryRow.createCell(i); 
			    }
			    cell.setCellStyle(summaryCellStyle);
			}

			// Auto-size columns to fit content
			for (int i = 0; i <= 16; i++) {
			    sheet.autoSizeColumn(i);
			}


			ZipSecureFile.setMaxTextSize(20000000);
			ZipSecureFile.setMinInflateRatio(0.009);

			try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
				workbook.write(fileOut);
			}

			workbook.close();


			System.out.println("File Processed Succesfully.");
			responseXsd.setDownloadPath(csvPath);
			responseXsd.setError(false);
			responseXsd.setErrorDescription("File Processed Succesfully");
			return responseXsd;

		} catch (Exception e) {
			e.printStackTrace();
			responseXsd.setError(true);
			responseXsd.setErrorDescription("Error processing report: " +e.getMessage());
		}

		return responseXsd;

	}


	private static CellStyle createHeaderCellStyle(XSSFWorkbook workbook) {
		Font font = workbook.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 11);
		font.setFontName("Calibri");
		font.setColor(IndexedColors.WHITE.getIndex());

		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		setBorder(style);
		return style;
	}

	private static CellStyle createSummaryCellStyle(XSSFWorkbook workbook) {
		Font font = workbook.createFont();
		font.setBold(true);
		font.setFontHeightInPoints((short) 11);
		font.setFontName("Calibri");
		
		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.BLUE_GREY.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		setBorder(style);
		return style;
	}

	private static void setBorder(CellStyle style) {
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
	}



}
