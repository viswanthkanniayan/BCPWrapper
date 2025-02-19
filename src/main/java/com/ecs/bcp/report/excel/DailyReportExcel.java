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

import com.ecs.bcp.pojo.BalanceConfirmPojo;
import com.ecs.bcp.pojo.SettingsPojo;
import com.ecs.bcp.utils.EjbLookUps;
import com.ecs.bcp.utils.PropertiesReader;
import com.ecs.bcp.utils.ServiceUtils;
import com.ecs.bcp.xsd.EntityRequest;
import com.ecs.bcp.xsd.EntityResponce;


public class DailyReportExcel {



	private static String[] columns = {"Date of Trn", "Trn ID", "Bank Name", "Total Invoice", "Invoice No", "UTR", "Mode"};

	public static EntityResponce agentReportExcel(EntityRequest reqXSD) throws Exception {
		
			String filePath = "";
			String csvPath = "";
			PropertiesReader pro = new PropertiesReader();
			EntityResponce responseXsd = new EntityResponce();

			BalanceConfirmPojo agentPojo = null;
			List<BalanceConfirmPojo> pojoList = null;


			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  

			try {


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

				String folderPath = baseFolder + "DAILY_REPORT/"+folderDateFormat+File.separator+folderHourFormat+File.separator+folderMinFormat+File.separator+folderSecFormat+ File.separator;


				String folderURL = url+ File.separator + "DAILY_REPORT/"+folderDateFormat+File.separator+folderHourFormat+File.separator+folderMinFormat+File.separator+folderSecFormat+ File.separator;


				File passportFolder=new File(folderPath);
				if(!passportFolder.exists())
				{
					System.out.println("Audit Log Folder Not Exists***********************");
					passportFolder.mkdirs();
				}


				filePath = folderPath + reqXSD.getAuditeePan()+"_"+reqXSD.getRequestDate() + ".xlsx";

				csvPath = folderURL + reqXSD.getAuditeePan() +"_"+reqXSD.getRequestDate() + ".xlsx";

				String endDate = ServiceUtils.getTomorrowDate(reqXSD.getRequestDate());
				System.out.println("Start Date ------"+reqXSD.getRequestDate());
				System.out.println("End Date ------"+endDate);

				pojoList = EjbLookUps.getBalanceConfirmRemote().getReportByDate(reqXSD.getAuditeePan(), reqXSD.getRequestDate(), endDate);

				if(pojoList==null || pojoList.size()==0) {


					//add error response no recoed found
					responseXsd.setError(true);
					responseXsd.setErrorDescription("No Data Found");

					return responseXsd;
				}

				System.out.println("---file path---"+filePath);

				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFSheet sheet = workbook.createSheet("Daily_Report"+folderDate);


				   CellStyle headerCellStyle = createHeaderCellStyle(workbook);
		            XSSFRow headerRow = sheet.createRow(0);
		            sheet.setAutoFilter(CellRangeAddress.valueOf("A1:G1"));
		            
		            for (int i = 0; i < columns.length; i++) {
		                Cell cell = headerRow.createCell(i);
		                cell.setCellValue(columns[i]);
		                cell.setCellStyle(headerCellStyle);
		            }
				int rowNum = 1;
				double totalInvoiceAmount = 0;

				for (BalanceConfirmPojo pojo : pojoList) {

					Row row = sheet.createRow(rowNum++);

					if(!ServiceUtils.isEmpty(reqXSD.getRequestDate())) {


						row.createCell(0).setCellValue(dateFormat.format(pojo.getCreationDate()));
						row.createCell(1).setCellValue(pojo.getTxnId());
						row.createCell(2).setCellValue(pojo.getBank());
						row.createCell(3).setCellValue(pojo.getTotalAmount());
						row.createCell(4).setCellValue(pojo.getTaxInvNo());
						row.createCell(5).setCellValue("");
						row.createCell(6).setCellValue(pojo.getPaymentMode());

						totalInvoiceAmount += Double.parseDouble(pojo.getTotalAmount());
					}

				}
				
				CellStyle totalCellStyle = createSummaryCellStyle(workbook);
		           
				Row totalRow = sheet.createRow(rowNum++);
				Cell totalLabelCell = totalRow.createCell(2);
				totalLabelCell.setCellValue("Total");
				totalLabelCell.setCellStyle(totalCellStyle);

				Cell totalValueCell = totalRow.createCell(3);
				totalValueCell.setCellValue(totalInvoiceAmount);
				totalValueCell.setCellStyle(totalCellStyle); 

				ZipSecureFile.setMaxTextSize(20000000);//20 MB
				ZipSecureFile.setMinInflateRatio(0.009);


				FileOutputStream fileOut = new FileOutputStream(filePath);
				workbook.write(fileOut);
				fileOut.close();

				workbook.close();


				// Add total row


				System.out.println("File Processed Succesfully.");
				responseXsd.setDownloadPath(csvPath);


				responseXsd.setError(false);
				responseXsd.setErrorDescription("File Processed Succesfully");
				return responseXsd;



			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				responseXsd.setError(true);
				responseXsd.setErrorDescription(e.getMessage());
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
      //  style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      //  style.setAlignment(HorizontalAlignment.CENTER);
     //   style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBorder(style);
        return style;
    }

    private static CellStyle createSummaryCellStyle(XSSFWorkbook workbook) {
        CellStyle style = createHeaderCellStyle(workbook);
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
	//	  style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//        style.setAlignment(HorizontalAlignment.CENTER);
//        style.setVerticalAlignment(VerticalAlignment.CENTER);
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
