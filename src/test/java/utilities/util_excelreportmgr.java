package utilities;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

import net.sf.cglib.core.Local;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.IInvokedMethod;
import org.testng.IMethodInstance;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class util_excelreportmgr  {
		
	
	private static String sReportFilePath = null, sReportHistoryFilePath=null;
	private static String sTestEvidenceFolderPath = null;
	private static String sReportName = null;
	private static String sReportDesc = null;
	private static String sReportFileName = null;
	private static XSSFWorkbook workbook;
	private static XSSFSheet sheet;
	private static int rownum = 0, iPass = 0, iFailed = 0, iSkipped = 0, iTotalRun = 0, iLastRow = 0;
	static FileOutputStream fos = null;
	
	private static final Logger LOG = LogManager.getLogger(util_common.class);
	
	//Added by Edward 01Dec2016
	     public static void loadExcelFilePath() {
			 try{
				 Properties p = new Properties();
				 File currentDirectory = new File(new File(".").getAbsolutePath());
				 p.load(new FileInputStream("src//main//resources//properties.ini"));
				
				 sReportFilePath = p.getProperty("sexcelreportpath");
				 sReportFileName = p.getProperty("sexcelreportname");
				
				 util_constant.cTc_ExcelFileName = sReportFilePath;
				 util_constant.cTc_ExcelSheetName = sReportFileName;
				 util_constant.cTc_ExcelFilePath = sReportFilePath+"\\"+sReportFileName+".xlsx";
				
				LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
			}
			catch (Exception e)
			{
				LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + e.getMessage());
			}
	     }
	
	
	public void createResultFolder(){
		try{
			Properties p = new Properties();
			File currentDirectory = new File(new File(".").getAbsolutePath());
			p.load(new FileInputStream("src//main//resources//properties.ini"));
			
			sReportFilePath = p.getProperty("sexcelreportpath");
			File f = new File(sReportFilePath);
			
			if (!f.exists()){
				new File(sReportFilePath).mkdir();
			}
			
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		}
		catch (Exception e)
		{
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + e.getMessage());
		}
		
	}
	
	
	
	public static void createResultHistoryFolder(){
		try{
			Properties p = new Properties();
			File currentDirectory = new File(new File(".").getAbsolutePath());
			p.load(new FileInputStream("src//main//resources//properties.ini"));
			
			sReportFilePath = p.getProperty("sexcelreportpath");
			sReportHistoryFilePath = sReportFilePath+"//"+"Result History";
			File f = new File(sReportFilePath);
			
			if (!f.exists()){
				new File(sReportHistoryFilePath).mkdir();
			}
			
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		}
		catch (Exception e)
		{
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + e.getMessage());
		}
		
	}
	
	
	
	public static void createResultHistory(){
		try{
			Properties p = new Properties();
			File currentDirectory = new File(new File(".").getAbsolutePath());
			p.load(new FileInputStream("src//main//resources//properties.ini"));
			
			sReportFilePath = p.getProperty("sexcelreportpath");
			sReportFileName = p.getProperty("sexcelreportname");
			sReportHistoryFilePath = sReportFilePath+"//Result History";
			File f = new File(sReportFilePath);
			File fHistory = new File(sReportHistoryFilePath);
			
			if (!fHistory.exists()){
				new File(sReportHistoryFilePath).mkdir();
			}
			
			
			 File afile =new File(sReportFilePath+"\\"+sReportFileName+".xlsx");
			 
			 DateFormat dateFormat = new SimpleDateFormat("ddMMMyyyyHHmmss");
			 Date date = new Date();
			    
			afile.renameTo(new File(sReportHistoryFilePath+"//run_"+dateFormat.format(date)+".xlsx"));
						
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		}
		catch (Exception e)
		{
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + e.getMessage());
		}
		
	}
	
	
	
	public void createExcelResultFile(){
		
		try{		
			loadExcelFilePath();
			createResultHistoryFolder();
			
			File f = new File(util_constant.cTc_ExcelFilePath);
			
			
			if (!f.exists()){
				XSSFWorkbook workbook = new XSSFWorkbook(); 
				XSSFSheet sheet = workbook.createSheet("RESULT");
								
				// create header of worksheet
				XSSFRow row = sheet.createRow(0);
				
				Cell cell1 = row.createCell(0);
				cell1.setCellValue("Total");
				
				Cell cell2 = row.createCell(1);
				cell2.setCellValue("Passed");
				
				Cell cell3 = row.createCell(2);
				cell3.setCellValue("Failed");
				
				Cell cell4 = row.createCell(3);
				cell4.setCellValue("Skipped");
				
				Cell cell5 = row.createCell(4);
				cell5.setCellValue("Started");
				
				Cell cell6 = row.createCell(5);
				cell6.setCellValue("Finished");
				
				Cell cell7 = row.createCell(6);
				cell7.setCellValue("Duration");
				
				
				
				XSSFRow row1 = sheet.createRow(1);
				
				Cell cell1_1 = row1.createCell(0);
				cell1_1.setCellFormula("COUNTA(A5:A1000)");
				
				Cell cell2_1 = row1.createCell(1);
				cell2_1.setCellFormula("COUNTIF(D5:D1000,\"passed\")");
				
				Cell cell3_1 = row1.createCell(2);
				cell3_1.setCellFormula("COUNTIF(D5:D1000,\"failed\")");
				
				Cell cell4_1 = row1.createCell(3);
				cell4_1.setCellFormula("COUNTIF(D5:D1000,\"skipped\")");
				
				Cell cell5_1 = row1.createCell(4);
				
				Cell cell6_1 = row1.createCell(5);
				
				Cell cell7_1 = row1.createCell(6);
				
				
				
				XSSFRow row2 = sheet.createRow(2);
				
				Cell cell1_2 = row2.createCell(0);
				cell1_2.setCellValue(".");
				
				Cell cell2_2 = row2.createCell(1);
				cell2_2.setCellValue(".");
				
				Cell cell3_2 = row2.createCell(2);
				cell3_2.setCellValue(".");
				
				Cell cell4_2 = row2.createCell(3);
				cell4_2.setCellValue(".");
				
				Cell cell5_2 = row2.createCell(4);
				cell5_2.setCellValue(".");
				
				Cell cell6_2 = row2.createCell(5);
				cell6_2.setCellValue(".");
				
				Cell cell7_2 = row2.createCell(6);
				cell7_2.setCellValue(".");
				
				
				
				
				
				XSSFRow row3 = sheet.createRow(3);
				
				Cell cell1_3 = row3.createCell(0);
				cell1_3.setCellValue("Test Case");
				
				Cell cell2_3 = row3.createCell(1);
				cell2_3.setCellValue("Start Time");
				
				Cell cell3_3 = row3.createCell(2);
				cell3_3.setCellValue("End Time");
				
				Cell cell4_3 = row3.createCell(3);
				cell4_3.setCellValue("Status");
				
					// create excel file
					fos=new FileOutputStream(new File(sReportFilePath+"\\"+sReportFileName+".xlsx"));
					workbook.write(fos);
			}
			
			
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		}
		catch (Exception e)
		{
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + e.getMessage());
		}
	}
	
	
	
	public void setStartExcelResult(String sTestCaseName){
		loadExcelFilePath();
		
		int i, cellStartTime=1;
		String CellData;
		
		try{
			 
			InputStream myxls = new FileInputStream(util_constant.cTc_ExcelFilePath);
			XSSFWorkbook book = new XSSFWorkbook(myxls);
			XSSFSheet sheet = book.getSheet("RESULT");
			
			
			iLastRow = sheet.getLastRowNum();
			Calendar startDateTime = Calendar.getInstance();
			
			CellStyle cellStyle = book.createCellStyle();
		    CreationHelper createHelper = book.getCreationHelper();
		    short dateFormat = createHelper.createDataFormat().getFormat("dd-MMM-yyyy HH:mm:ss");
		    cellStyle.setDataFormat(dateFormat);
			
			for (i=3 ; i<=iLastRow; i++){
			
				XSSFCell cellA = sheet.getRow(i).getCell(0);
				
				CellData = cellA.getStringCellValue();
				

				
				if (CellData == null) {
					CellData = "";
				}
				 
				
				if (!CellData.equals(sTestCaseName)){
					if (i==iLastRow){
						iLastRow = iLastRow + 1;
						XSSFRow row = sheet.createRow(iLastRow);
						Cell createCell = row.createCell(0);
						createCell.setCellValue(sTestCaseName);
						
					    
					    Cell cellB = row.createCell(cellStartTime);
					    cellB.setCellValue(startDateTime);
					    cellB.setCellStyle(cellStyle);
						
						break;
					}
				}
				else if (CellData.equals(sTestCaseName)){
				    
				    XSSFRow row = sheet.getRow(i);
				    
				    Cell cellB = row.getCell(cellStartTime);
			    	cellB = row.createCell(cellStartTime);
				    cellB.setCellValue(startDateTime);
				    cellB.setCellStyle(cellStyle);
					
					break;
				}
			
			}
			
			
			 XSSFRow row = sheet.getRow(1);
			 Cell cellStarted = row.getCell(4);
			 
			 if (cellStarted==null || cellStarted.getCellType() == Cell.CELL_TYPE_BLANK){
				 cellStarted = row.createCell(4);
				 cellStarted.setCellValue(startDateTime);
				 cellStarted.setCellStyle(cellStyle);
			 }
			
			
			FileOutputStream fileOut = new FileOutputStream(util_constant.cTc_ExcelFilePath);
				
			book.write(fileOut);

			fileOut.flush();
			fileOut.close();
	

	        LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		}
		catch (Exception e)
		{
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + e.getMessage());
		}
	}
	
	
	
	public void setEndExcelResult(String sTestCaseName, String sResult){
		loadExcelFilePath();
		
		int i, cellEndTime=2, cellStatus=3;
		
		sResult = sResult.toLowerCase();
		
		if (!sResult.startsWith("passed")){
			sResult = "Failed";
		}
		
		if (util_constant.cTc_TestCaseFailed == "Y"){
			sResult = "Failed";
		}
		
		sResult = sResult.substring(0, 6);
		
		try{
			 
			InputStream myxls = new FileInputStream(util_constant.cTc_ExcelFilePath);
			XSSFWorkbook book = new XSSFWorkbook(myxls);
			XSSFSheet sheet = book.getSheet("RESULT");
			
			
			iLastRow = sheet.getLastRowNum();
			Calendar finishedDateTime = Calendar.getInstance();
			
			CellStyle cellStyle = book.createCellStyle();
		    CreationHelper createHelper = book.getCreationHelper();
		    short dateFormat = createHelper.createDataFormat().getFormat("dd-MMM-yyyy HH:mm:ss");
		    cellStyle.setDataFormat(dateFormat);
			
			for (i=3 ; i<=iLastRow; i++){
			
				XSSFCell cellA = sheet.getRow(i).getCell(0);
				
				String CellData = cellA.getStringCellValue();
				
				if (CellData == null) {
					CellData = "";
				}
				 
				if (!CellData.equals(sTestCaseName)){
					if (i==iLastRow){
						iLastRow = iLastRow + 1;
						XSSFRow row = sheet.createRow(iLastRow);
						Cell createCell = row.createCell(0);
						createCell.setCellValue(sTestCaseName);
					    
					    Cell cellB = row.createCell(cellEndTime);
					    cellB.setCellValue(finishedDateTime);
					    cellB.setCellStyle(cellStyle);
					    
					    Cell cellC = row.createCell(cellStatus);
					    cellC.setCellValue(sResult);
						
						
						break;
					}
				}
				else if (CellData.equals(sTestCaseName)){
				    
				    XSSFRow row = sheet.getRow(i);
				    Cell cellB = row.createCell(cellEndTime);
				    cellB.setCellValue(finishedDateTime);
				    cellB.setCellStyle(cellStyle);
				    
				    Cell cellC = row.createCell(cellStatus);
				    cellC.setCellValue(sResult);

					
					break;
				}
			
			}
			
			XSSFRow row1 = sheet.getRow(1);
			
			Cell cell1_1 = row1.createCell(0);
			cell1_1.setCellFormula("COUNTA(A5:A1000)");
			
			Cell cell2_1 = row1.createCell(1);
			cell2_1.setCellFormula("COUNTIF(D5:D1000,\"passed\")");
			
			Cell cell3_1 = row1.createCell(2);
			cell3_1.setCellFormula("COUNTIF(D5:D1000,\"failed\")");
			
			Cell cell4_1 = row1.createCell(3);
			cell4_1.setCellFormula("COUNTIF(D5:D1000,\"skipped\")");
			
			Cell cell5_1 = row1.createCell(5);
			cell5_1.setCellValue(finishedDateTime);
			cell5_1.setCellStyle(cellStyle);
			
			FileOutputStream fileOut = new FileOutputStream(util_constant.cTc_ExcelFilePath);
			book.write(fileOut);
			fileOut.flush();
			fileOut.close();
			
			if (util_constant.cTc_TestCaseFailed == "N"){
				util_constant.cTc_TestCaseFailed = "N";
			}
			
	        LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		}
		catch (Exception e)
		{
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + e.getMessage());
		}
	}
	
	
	
public void setTestResult(ITestResult result){
		
		
		try{
//			if(result.getStatus() == ITestResult.SUCCESS)
//		    {
//
//		        iPass = iPass + 1; 
//		        System.out.println("Total Pass: "+iPass);
//		    }
//
//		    else if(result.getStatus() == ITestResult.FAILURE)
//		    {
//		    	iFailed = iFailed + 1; 
//		    	System.out.println("Total Failed: "+iFailed);
//
//		    }
//
//		     else if(result.getStatus() == ITestResult.SKIP ){
//
//		    	 iSkipped = iSkipped + 1; 
//		    	 System.out.println("Total Skipped: "+iSkipped);
//
//		    }
//			
//			iTotalRun = iPass + iFailed + iSkipped;
			
			loadExcelFilePath();

				 
				InputStream myxls = new FileInputStream(util_constant.cTc_ExcelFilePath);
				XSSFWorkbook book = new XSSFWorkbook(myxls);
				XSSFSheet sheet = book.getSheet("RESULT");
				
				
				iLastRow = sheet.getLastRowNum();				
					
					
					XSSFRow row = sheet.getRow(1);
				
					
//					Cell createCellTotalExecuted = row.createCell(0);
//					createCellTotalExecuted.setCellValue(iTotalRun);
//					
//					Cell createCellPass = row.createCell(1);
//					createCellPass.setCellValue(iPass);
//					
//					Cell createCellFailed = row.createCell(2);
//					createCellFailed.setCellValue(iFailed);
//					
//					Cell createCellSkipped = row.createCell(3);
//					createCellSkipped.setCellValue(iSkipped);
					
					CellStyle cellStyle = book.createCellStyle();
					CreationHelper createHelper = book.getCreationHelper();
					short dateFormat = createHelper.createDataFormat().getFormat("dd-MMM-yyyy HH:mm:ss");
					cellStyle.setDataFormat(dateFormat);
				 
					XSSFCell cellStartTime = sheet.getRow(1).getCell(4);
					Date CellStartTime = cellStartTime.getDateCellValue();
					
//					XSSFCell cellEndTime = sheet.getRow(iLastRow).getCell(2);
//					Date CellEndTime = cellEndTime.getDateCellValue();
					
//					Cell createCellStarted = row.createCell(4);
//					createCellStarted.setCellValue(CellStartTime);
//					createCellStarted.setCellStyle(cellStyle);
					
					Cell createCellFinished = row.createCell(5);
					createCellFinished.setCellValue(Calendar.getInstance());
//					createCellFinished.setCellValue(CellEndTime);
					createCellFinished.setCellStyle(cellStyle);
					Date CellEndTime = createCellFinished.getDateCellValue();
					
					
					long executionDuration = CellEndTime.getTime() - CellStartTime.getTime();
					long executionDurationHours = TimeUnit.MILLISECONDS.toHours(executionDuration);
					long executionDurationMinutes = TimeUnit.MILLISECONDS.toMinutes(executionDuration) - TimeUnit.HOURS.toMinutes(executionDurationHours);
					long executionDurationSeconds = TimeUnit.MILLISECONDS.toSeconds(executionDuration) - TimeUnit.MINUTES.toSeconds(executionDurationMinutes) - TimeUnit.HOURS.toSeconds(executionDurationHours);
					
					
					Cell createCellDuration = row.createCell(6);
					createCellDuration.setCellValue(executionDurationHours+" Hours "+executionDurationMinutes+" Minutes "+executionDurationSeconds+" Seconds ");
					
					sheet.setColumnWidth(0, 30*256);
					sheet.autoSizeColumn(1);
					sheet.autoSizeColumn(2);
					sheet.autoSizeColumn(3);
					sheet.autoSizeColumn(4);
					sheet.autoSizeColumn(5);
					sheet.autoSizeColumn(6);
					
					FileOutputStream fileOut = new FileOutputStream(util_constant.cTc_ExcelFilePath);
					book.write(fileOut);
					fileOut.flush();
					fileOut.close();

	        LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		}
		catch (Exception e)
		{
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + e.getMessage());
		}
	}
		
	
	
	public static void sendHtmlEmail() throws AddressException,MessagingException {
	
	        int i;
	 
	        
	        try{
		         Properties p = new Properties();
				 File currentDirectory = new File(new File(".").getAbsolutePath());
				 p.load(new FileInputStream("src//main//resources//properties.ini"));
				
				 String sReportName = p.getProperty("sexcelreportname");
				 String sHost = p.getProperty("sHost");
				 String sPort = p.getProperty("sPort");
				 String sMailFrom = p.getProperty("sMailFrom");
				 String sMailToAddress = p.getProperty("sMailToAddress");
				 final String sUserName = p.getProperty("sUserName");
				 final String sPassword = p.getProperty("sPassword");
					 
		        loadExcelFilePath();
		        
		     // outgoing message information
		        String subject = "Test_Automation [" +sReportName+ "] - Regression Test Automation Summary";
		        	
		        InputStream myxls = new FileInputStream(util_constant.cTc_ExcelFilePath);
				XSSFWorkbook book = new XSSFWorkbook(myxls);
				XSSFSheet sheet = book.getSheet("RESULT");
				
				
					iLastRow = sheet.getLastRowNum();
				
				 	DateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
				 
					XSSFCell cellStartTime = sheet.getRow(1).getCell(4);
					Date CellStartTime = cellStartTime.getDateCellValue();
					String sCellStartTime = df.format(CellStartTime);
					
					XSSFCell cellEndTime = sheet.getRow(1).getCell(5);
					Date CellEndTime = cellEndTime.getDateCellValue();
					String sCellEndTime = df.format(CellEndTime);
					
					XSSFCell cellDuration = sheet.getRow(1).getCell(6);
					String sCellDuration = cellDuration.getStringCellValue();
					
					
					XSSFCell cellTotal = sheet.getRow(1).getCell(0);
					int sCellTotal = (int) cellTotal.getNumericCellValue();
					
					XSSFCell cellPassed = sheet.getRow(1).getCell(1);
					int sCellPassed = (int) cellPassed.getNumericCellValue();
					
					XSSFCell cellFailed = sheet.getRow(1).getCell(2);
					int sCellFailed = (int) cellFailed.getNumericCellValue();
					
					XSSFCell cellSkipped = sheet.getRow(1).getCell(3);
					int sCellSkipped = (int) cellSkipped.getNumericCellValue();
					
					

					

				
				StringBuilder buf = new StringBuilder();
				
				buf.append("<META HTTP-EQUIV='Content-Type' CONTENT='text/html; charset=iso-8859-1'>" +
						 "<HTML>" +
						 "<HEAD>" +
							 "<TITLE>QC TestSet Execution - Custom Report</TITLE>" +
							 
							 "<META content='MSHTML 6.00.2800.1106'>" +
						 "</HEAD>" +
						 "<body leftmargin='0' marginheight='0' marginwidth='0' topmargin='0'>" +
							 "<table width='100%' border='0' cellspacing='0' cellpadding='0'>" +
								 "<tr>" +
									 "<td></td>" +
								 "</tr>" +
								 "<tr>" +
									 "<td class='rest'>" +
										 "<table class='space' width='100%' border='0' cellspacing='0' cellpadding='0'>" +
											 "<tr>" +
											 	"<td></td>" +
											 "</tr>" +
										 "</table>" +
										 "<table class='textfont' cellspacing='0' cellpadding='0' width='100%' align='center' border='0' >" +
										 	"<tbody>" +
												 "<tr>" +
													 "<th class='hl' align='left' >"+sReportName+"</th>" +
												 "</tr>" +
												 "<tr>" +
													 "<th class='space' align='left'></th>" +
												 "</tr>" +
												 "<tr>" +
													 "<th class='space' align='left'></th>" +
												 "</tr>" +
											 "</tbody>" +
										 "</table>" +
										 "<table class='textfont' cellspacing='0' cellpadding='0' width='100%' align='center' border='0'>" +
											 "<tbody>" +
												 "<tr>" +
													 "<td height='10'>" +
													 "</td>" +
												 "</tr>" +
											 "</tbody>" +
										 "</table>" +
										 "<table class='textfont' cellspacing='0' cellpadding='0' width='100%' align='center' border='0'>" +
											 "<tbody>" +
												 "<tr>" +
													 "<td>" +
														 "<table class='textfont' cellspacing='0' cellpadding='0' width='100%' align='center' border='0'>" +
															 "<tbody>" +
																 "<tr>" +
																	 "<td height='1'></td>" +
																 "</tr>" +
																 "<tr>" +
																	 "<td class='chl' style='width:17%'>Total</td>" +
																	 "<td class='chl' style='width:17%'>Passed</td>" +
																	 "<td class='chl' style='width:17%'>Failed</td>" +
																	 "<td class='chl' style='width:17%'>Skipped</td>" +
																 "</tr>" +
																 "<tr>" +
																	 "<td class='ctext' style='width:17%'>"+sCellTotal+"</td>" +
																	 "<td class='ctext' style='width:17%'>"+sCellPassed+"</td>" +
																	 "<td class='ctext' style='width:17%'>"+sCellFailed+"</td>" +
																	 "<td class='ctext' style='width:17%'>"+sCellSkipped+"</td>" +
																 "</tr>" +
															 "</tbody>" +
														 "</table>" +
													 "</td>" +
												 "</tr>" +
												 "<tr>" +
													 "<td class='space'></td>" +
												 "</tr>" +
											 "</tbody>" +
										 "</table>" +
										 "<table class='textfont' cellspacing='0' cellpadding='0' width='100%' align='center' border='0'>" +
											 "<tbody>" +
												 "<tr>" +
													 "<td>" +
														 "<table class='textfont' cellspacing='0' cellpadding='0' width='100%' align='center' border='0'>" +
															 "<tbody>" +
															 
																"<tr>" +
																	 "<td width='20%'>Project Name</td>" +
																	 "<td class='ctext'>"+sReportName+"</td>" +
																 "</tr>" +
																 "<tr>" +
																	 "<td width='20%'>Started</td>" +
																	 "<td class='ctext'>"+sCellStartTime+"</td>" +
																 "</tr>" +
																 "<tr>" +
																 	"<td width='20%'>Finished</td>" +
																 	"<td class='ctext'>"+sCellEndTime+"</td>" +
																 "</tr>" +
																 "<tr>" +
																	 "<td width='20%'>Duration</td>" +
																	 "<td class='ctext'>"+sCellDuration+"</td>" +
																 "</tr>" +
																 "<tr>" +
																	 "<td width='20%'>Contact Person Email</td>" +
																	 "<td class='ctext'>"+sMailFrom+"</td>" +
															 	"</tr>" +
															 "</tbody>" +
														 "</table>" +
													 "</td>" +
												 "</tr>" +
												 "<tr>" +
													 "<td class='space'></td>" +
												 "</tr>" +
											 "</tbody>" +
										 "</table>" +
										 "<table class='textfont' cellspacing='0' cellpadding='0' width='100%' align='center' border='0'>" +
											 "<tbody>" +
												 "<tr>" +
													 "<th class='hl' align='left'></th>" +
												 "</tr>" +
											 "</tbody>" +
										 "</table>" +
										 "<table class='textfont' cellspacing='0' cellpadding='0' width='100%' align='center' border='0'>" +
											 "<tbody>" +
												 "<tr>" +
													 "<th class='space' align='left' height='10'></th>" +
												 "</tr>" +
												 "<tr>" +
													 "<th class='hl' align='left'>Executed Test Case(s) Details</th>" +
												 "</tr>" +
												 "<tr>" +
													 "<th class='space' align='left'></th>" +
												 "</tr>" +
											 "</tbody>" +
										 "</table>" +
										 "<table class='textfont' cellspacing='0' cellpadding='0' width='100%' align='center' border='0'>" +
											 "<tbody>" +
											 "<tr>" +
											 "<td style='font-size: 10px; font-weight: bold; background-color: #eee; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>Test</td>" +
											 "<td style='font-size: 10px; font-weight: bold; background-color: #eee; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>Status</td>" +
											 "<td style='font-size: 10px; font-weight: bold; background-color: #eee; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>Exec Start Time</td>" +
											 "<td style='font-size: 10px; font-weight: bold; background-color: #eee; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>Exec End Time</td>" +
										 "</tr>") ;
				
				
				for (i=4 ; i<=iLastRow; i++){
					
					XSSFCell cellA = sheet.getRow(i).getCell(0);
					String CellDataA = cellA.getStringCellValue();
					
					XSSFCell cellB = sheet.getRow(i).getCell(1);
					Date CellDataB = cellB.getDateCellValue();
					String sCellDataB = df.format(CellDataB);
					
					XSSFCell cellC = sheet.getRow(i).getCell(2);
					Date CellDataC = cellC.getDateCellValue();
					String sCellDataC = df.format(CellDataC);
					
					XSSFCell cellD = sheet.getRow(i).getCell(3);
					String CellDataD = cellD.getStringCellValue();
					
					if (CellDataD=="passed"){
						buf.append("<tr><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
					       .append(CellDataA)
					       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white; color: green;'>")
					       .append("Passed")
					       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
					       .append(sCellDataB)
					       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
					       .append(sCellDataC)
					       .append("</td></tr>");
					}
					else if(CellDataD=="failed"){
						buf.append("<tr><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
					       .append(CellDataA)
					       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white; color: red;'>")
					       .append("Failed")
					       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
					       .append(sCellDataB)
					       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
					       .append(sCellDataC)
					       .append("</td></tr>");
					}
					else{
						buf.append("<tr><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
					       .append(CellDataA)
					       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
					       .append(CellDataD)
					       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
					       .append(sCellDataB)
					       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
					       .append(sCellDataC)
					       .append("</td></tr>");
					}
					
					
				}
				
				
				buf.append( "</tbody>" +
							 "</table>" +
							 	"</td>" +
							 		"</tr>" +
							 			"</table>" +
							 				"</body>" +
							 					"</HTML>");
				String message = buf.toString();
				
		        // sets SMTP server properties
		        Properties properties = new Properties();
		        properties.put("mail.smtp.host", sHost);
		        properties.put("mail.smtp.port", sPort);
		        properties.put("mail.smtp.auth", "true");
		        properties.put("mail.smtp.starttls.enable", "false");
		 
		        
		        // creates a new session with an authenticator
		        Authenticator auth = new Authenticator() {
		        public PasswordAuthentication getPasswordAuthentication() {
		                return new PasswordAuthentication(sUserName, sPassword);
		            }
		        };
		        
			        
			        Session session = Session.getInstance(properties, auth);
			 
				        // creates a new e-mail message
				        Message msg = new MimeMessage(session);
				 
				        msg.setFrom(new InternetAddress(sUserName));
				        
				        String[] recipientList = sMailToAddress.split(",");
				        InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
				        int counter = 0;
				        for (String recipient : recipientList) {
				            recipientAddress[counter] = new InternetAddress(recipient.trim());
				            counter++;
				        }
				        
				        msg.setRecipients(Message.RecipientType.TO, recipientAddress);
				        msg.setSubject(subject);
				        msg.setSentDate(new Date());
				        // set plain text message
				        msg.setContent(message, "text/html");
				 
				        // sends the e-mail
				        Transport.send(msg);
				        
				        Component frame = null;
						JOptionPane.showMessageDialog(frame, "Email successfully sent to "+sMailToAddress);
						
				        LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
				}
				catch (Exception e)
				{
					LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + e.getMessage());
				}
	 
	 
	 
	 }



	public static void main(String[] args) {

//				Method below to add in TestCases.java at @BeforeClass to create result folder and excel file
//				util_constant.cUtil_ExcelReport.createResultFolder();
//				util_constant.cUtil_ExcelReport.createExcelResultFile();
				
//				 Method below to add in TestCases.java to gather test case name annotated
//				 @BeforeMethod
//			     public void loadTestDescription(Method method) {
//			         Test test = method.getAnnotation(Test.class);
//			         util_constant.cTc_TestCaseName = test.description();
//			     }
				
				
				try{		
					loadExcelFilePath();
					createResultHistoryFolder();
					
					File f = new File(util_constant.cTc_ExcelFilePath);
					
				if (f.exists()){
						sendHtmlEmail();
						createResultHistory();
					}
				else if (!f.exists()){
					Component frame = null;
					JOptionPane.showMessageDialog(frame, "File "+util_constant.cTc_ExcelFilePath+" not found", "File Not Found", JOptionPane.WARNING_MESSAGE);
				}
					
					LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
				}
				catch (Exception e)
				{
					LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + e.getMessage());
				}
			
		
	}

}



