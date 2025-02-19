package com.ecs.bcp.report.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import com.ecs.bcp.pojo.PaymentReportDetailsPojo;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.utils.PropertiesReader;
import com.ecs.bcp.utils.ServiceUtils;
import com.ecs.bcp.xsd.EntityRequest;
import com.ecs.bcp.xsd.EntityResponce;

public class DailyBankReportExcel {



	private static String[] columns = {"Date of Trn", "Trn ID", "Bank Name", "Bank Share", "Bank GST", "Bank TDS", "Bank Net Payable"};

	public static EntityResponce dailyBankReportExcel(EntityRequest reqXSD) throws Exception {

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

			String folderPath = baseFolder + "DAILY_BANK_REPORT/"+folderDateFormat+File.separator+folderHourFormat+File.separator+folderMinFormat+File.separator+folderSecFormat+ File.separator;


			String folderURL = url+ File.separator + "DAILY_BANK_REPORT/"+folderDateFormat+File.separator+folderHourFormat+File.separator+folderMinFormat+File.separator+folderSecFormat+ File.separator;


			File passportFolder=new File(folderPath);
			if(!passportFolder.exists())
			{
				System.out.println("Audit Log Folder Not Exists***********************");
				passportFolder.mkdirs();
			}


			filePath = folderPath + reqXSD.getBank()+"_"+reqXSD.getRequestDate() + ".xlsx";

			csvPath = folderURL + reqXSD.getBank() +"_"+reqXSD.getRequestDate() + ".xlsx";

			String endDate = ServiceUtils.getTomorrowDate(reqXSD.getRequestDate());
			System.out.println("Start Date ------"+reqXSD.getRequestDate());
			System.out.println("End Date ------"+endDate);

			pojoList = EjbLookUps.getPaymentReportDetailsRemote().getReportBankByDate(reqXSD.getBank() ,reqXSD.getRequestDate(), endDate);

			if(pojoList==null || pojoList.size()==0) {

				responseXsd.setError(true);
				responseXsd.setErrorDescription("No Data Found");

				return responseXsd;
			}

			System.out.println("---file path---"+filePath);

			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Daily_Bank_Report"+folderDate);

			CellStyle headerCellStyle = createHeaderCellStyle(workbook);
			XSSFRow headerRow = sheet.createRow(0);
			sheet.setAutoFilter(CellRangeAddress.valueOf("A1:G1"));


			for(int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerCellStyle);
			}

			int rowNum = 1;
			double 	totalBankRevenue =0;
			double 	totalBankGST =0;
			double 	totalBankTDS =0;
			double 	totalBankPayable =0;

			for (PaymentReportDetailsPojo pojo : pojoList) {

				Row row = sheet.createRow(rowNum++);

				if(!ServiceUtils.isEmpty(reqXSD.getRequestDate())) {


					row.createCell(0).setCellValue(dateFormat.format(pojo.getCreationDate()));
					row.createCell(1).setCellValue(pojo.getTxnId());
					row.createCell(2).setCellValue(pojo.getBankName());
					row.createCell(3).setCellValue(pojo.getBankRevenue());
					row.createCell(4).setCellValue(pojo.getBankRevenueGST());
					row.createCell(5).setCellValue(pojo.getBankRevenueTDS());
					row.createCell(6).setCellValue(pojo.getBankRevenuePayable());

					totalBankRevenue += Double.parseDouble(pojo.getBankRevenue());
					totalBankGST += Double.parseDouble(pojo.getBankRevenueGST());
					totalBankTDS += Double.parseDouble(pojo.getBankRevenueTDS());
					totalBankPayable += Double.parseDouble(pojo.getBankRevenuePayable());

				}

			}

			Row summaryRow = sheet.createRow(rowNum);
			CellStyle summaryCellStyle = createSummaryCellStyle(workbook);

			sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 0, 1));
			Cell summaryCell = summaryRow.createCell(0);
			summaryCell.setCellValue("DAY END SUMMARY");
			summaryCell.setCellStyle(summaryCellStyle);
			summaryRow.createCell(1).setCellStyle(summaryCellStyle);

			summaryRow.createCell(3).setCellValue(totalBankRevenue);
			summaryRow.createCell(4).setCellValue(totalBankGST);
			summaryRow.createCell(5).setCellValue(totalBankTDS);
			summaryRow.createCell(6).setCellValue(totalBankPayable);

//			for (int i = 2; i <= 6; i++) {
//				summaryRow.getCell(i).setCellStyle(summaryCellStyle);
//			}

			for (int i = 2; i <= 6; i++) {
			    Cell cell = summaryRow.getCell(i); 
			    if (cell == null) { 
			        cell = summaryRow.createCell(i); 
			    }
			    cell.setCellStyle(summaryCellStyle);
			}

			for (int i = 0; i < columns.length; i++) {
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

		CellStyle style = workbook.createCellStyle();
		style.setFont(font);
		style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
	//	style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	//	style.setAlignment(HorizontalAlignment.CENTER);
	//	style.setVerticalAlignment(VerticalAlignment.CENTER);
		setBorder(style);
		return style;
	}

	private static CellStyle createSummaryCellStyle(XSSFWorkbook workbook) {
		CellStyle style = createHeaderCellStyle(workbook);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	//	style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	//	style.setAlignment(HorizontalAlignment.CENTER);
	//	style.setVerticalAlignment(VerticalAlignment.CENTER);
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
