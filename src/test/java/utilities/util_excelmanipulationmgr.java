package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import net.sf.cglib.core.Local;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
 
public class util_excelmanipulationmgr {
	   
	private static XSSFSheet ExcelWSheet;
	private static XSSFWorkbook ExcelWBook;
	private static XSSFCell Cell;
	private static XSSFRow Row; 
	private static final Logger LOG = LogManager.getLogger(util_common.class);
	         
    public static void setExcelFile(String Path,String SheetName) throws Exception 
    {
    	try {  
				FileInputStream ExcelFile = new FileInputStream(Path);
				ExcelWBook = new XSSFWorkbook(ExcelFile); 
				ExcelWSheet = ExcelWBook.getSheet(SheetName);
			} catch (Exception el){
				Assert.fail("Error: " + Local.class.getEnclosingMethod().getName() + " - " + el.getMessage());
		}
	} 
     
    public static Object[][] getTableArray(String sTestCaseName)    throws Exception //String FilePath, String SheetName, 
	{   
	   String[][] tabArray = null;

	   try{
		   int totalRows = util_excelmanipulationmgr.getRowUsed();
		   int totalCols = util_excelmanipulationmgr.getCellUsed();
		   int iTestCaseRow = util_excelmanipulationmgr.getRowContains(sTestCaseName,0);

		   tabArray=new String[1][totalCols];

		   for (int i=0;i<=totalRows;i++)
		   {
			   if (i == iTestCaseRow)
			   {
				   for (int j=0;j<=totalCols-1;j++)
				   {
					   tabArray[0][j]=util_excelmanipulationmgr.getCellData(i,j);
				   }
			   }
		   }
		}
		catch (FileNotFoundException el)
		{
			Assert.fail("Error: " + Local.class.getEnclosingMethod().getName() + " - " + el.getMessage());
			el.printStackTrace();
		}
		catch (IOException el)
		{
			Assert.fail("Error: " + Local.class.getEnclosingMethod().getName() + " - " + el.getMessage());
			el.printStackTrace();
		}
		return(tabArray);
	}


	public static String getCellData(int RowNum, int ColNum) throws Exception {
		try{
			Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
			
			 if (Cell == null) {
				 return "";
			 }
			 else
			 {
				String CellData = Cell.getStringCellValue();
				return CellData;
			 }
			 
			} catch (Exception el){
				Assert.fail("Error: " + Local.class.getEnclosingMethod().getName() + " - " + el.getMessage());
				return "";
			}
	}
	
	public static void setCellData(String Result, String sPath, int RowNum, int ColNum) throws Exception	{
		 
			try{
				Row  = ExcelWSheet.getRow(RowNum);
				Cell = Row.getCell(ColNum, org.apache.poi.ss.usermodel.Row.RETURN_BLANK_AS_NULL);

			if (Cell == null) {
				Cell = Row.createCell(ColNum);
				Cell.setCellValue(Result);
				} else {
					Cell.setCellValue(Result);
				}
			
  				//FileOutputStream fileOut = new FileOutputStream("E://James//SeleniumTestAutomation//01Project//myPOC01//02 Data//" + "Data_RequestAccess.xlsx");
  				
  				FileOutputStream fileOut = new FileOutputStream(sPath);
  				
  				ExcelWBook.write(fileOut);

  				fileOut.flush();
				fileOut.close();

				}catch(Exception e){
					throw (e);
			}
		}
	
	public static String getTestCaseName(String sTestCase)throws Exception{
		 
		String value = sTestCase;

		try{

			int posi = value.indexOf("@");
			value = value.substring(0, posi);
			posi = value.lastIndexOf(".");	
			value = value.substring(posi + 1);
			return value;
				}catch (Exception e){
			throw (e);
					}
		}


	public static int getRowContains(String sTestCaseName, int colNum) throws Exception{
		 
		int i;

		try {
			int rowCount = util_excelmanipulationmgr.getRowUsed();
			for ( i=0 ; i<=rowCount; i++){
				if  (util_excelmanipulationmgr.getCellData(i,colNum).equalsIgnoreCase(sTestCaseName)){	
					break;
				}
			}
			return i;
				}catch (Exception e){
			throw(e);
			}
		}
	
	
	public static int getRowUsed() throws Exception {
		 
		try{
			int RowCount = ExcelWSheet.getLastRowNum();

			return RowCount;
		}catch (Exception e){
			System.out.println(e.getMessage());
			throw (e);
		}

	}
	
	public static int getCellUsed() throws Exception{
		try{

			int CellCount = ExcelWSheet.getRow(0).getLastCellNum();

			return CellCount;

		}catch (Exception e){

			System.out.println(e.getMessage());

			throw (e);

		}
	}
	
	public void populateSerialFromDataFile(String sID)
	{
		try
		{
			int iRowCount;
			int iSerialQty;
			String sTCID = null;
			String sSerialLst = null;
			String sSerial = null;
			String sSerialQty = null;
			
			Properties p = new Properties();
			File currentDirectory = new File(new File(".").getAbsolutePath());
			p.load(new FileInputStream("src//main//resources//properties.ini"));
			  
			String sTestDataFileName = p.getProperty("sdatafilepath");
			String sDataFilePath = currentDirectory.getCanonicalPath() + sTestDataFileName;
			
			util_excelmanipulationmgr.setExcelFile(sDataFilePath, "Serial_Config");
			iRowCount = util_excelmanipulationmgr.getRowUsed();
			

			for(int i=1; i<iRowCount+1; i++)
			{
				sTCID = util_excelmanipulationmgr.getCellData(i, 0);
				
				if (sID.trim().equalsIgnoreCase(sTCID))
				{
					sSerialQty = util_excelmanipulationmgr.getCellData(i, 7);
					
					iSerialQty = Integer.parseInt(sSerialQty);
					sSerialLst = util_excelmanipulationmgr.getCellData(i, 5);
					
					ArrayList aList= new ArrayList(Arrays.asList(sSerialLst.split(";")));
					
					for(int j=0; j<=iSerialQty-1; j++)
					{
						if (j==0)
						{
							sSerial = (String) aList.get(0) + ";";
						}
						else
						{
							sSerial = (String) aList.get(0) + ";" + sSerial;
						}
						
						aList.remove(0);
					}
					
					System.out.println("here110010010");
					
					String listString = String.join(";", aList);
					//String listString = StringUtils.join(";", aList);
					System.out.println("StringUtil" + listString);
					
					util_excelmanipulationmgr.setCellData(listString, sDataFilePath, i, 5);
					util_excelmanipulationmgr.setCellData(sSerial, sDataFilePath, i, 6);
					
					util_excelmanipulationmgr.setExcelFile(sDataFilePath, "eReturns_WarrantyInfo");
					util_excelmanipulationmgr.setCellData(sSerial, sDataFilePath, i, 5);
					
					util_excelmanipulationmgr.setExcelFile(sDataFilePath, "eReturns_CreateOrders");
					util_excelmanipulationmgr.setCellData(sSerial, sDataFilePath, i, 9);	
				}
	        }
			
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		}	catch (Exception el)	
		{
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + el.getMessage());
			//System.exit(1);
		}
	}
	
	
	public static void updateRMANoToDataFile(String sID, String sRMANo)
	{
		try
		{
			int iRowCount;
			int iSerialQty;
			String sTCID = null;
			
			Properties p = new Properties();
			File currentDirectory = new File(new File(".").getAbsolutePath());
			p.load(new FileInputStream("src//main//resources//properties.ini"));
			  
			String sTestDataFileName = p.getProperty("sdatafilepath");
			String sDataFilePath = currentDirectory.getCanonicalPath() + sTestDataFileName;
			
			util_excelmanipulationmgr.setExcelFile(sDataFilePath, "Serial_Config");
			iRowCount = util_excelmanipulationmgr.getRowUsed();
			
			for(int i=1; i<iRowCount+1; i++)
			{
				sTCID = util_excelmanipulationmgr.getCellData(i, 0);
				
				if (sID.trim().equalsIgnoreCase(sTCID))
				{
					util_excelmanipulationmgr.setExcelFile(sDataFilePath, "eReturns_CreateOrders");
					util_excelmanipulationmgr.setCellData(sRMANo, sDataFilePath, i, 16);
				}
	        }
			
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		}	catch (Exception el)	
		{
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + el.getMessage());
			//System.exit(1);
		}
	}

	
	
	 

	
	
	public void populateConsumerRegistrationEmail(String sID)
	{
		try
		{
			int iRowCount;
			String sTCID = null;

			Properties p = new Properties();
			File currentDirectory = new File(new File(".").getAbsolutePath());
			p.load(new FileInputStream("src//main//resources//properties.ini"));
			  
			String sTestDataFileName = p.getProperty("sdatafilepath");
			String sDataFilePath = currentDirectory.getCanonicalPath() + sTestDataFileName;
			
			//util_excelmanipulationmgr.setExcelFile(sDataFilePath, "ConsumerProduct_Registration");
			util_excelmanipulationmgr.setExcelFile(sDataFilePath, "Test_Data");
			iRowCount = util_excelmanipulationmgr.getRowUsed();
			
			for(int i=1; i<iRowCount+1; i++)
			{
				sTCID = util_excelmanipulationmgr.getCellData(i, 0);
				
				String sShipToNo;
				String sSerialQty;
				
				if (sID.trim().equalsIgnoreCase(sTCID))
				{
					//String sEmailCounter = util_excelmanipulationmgr.getCellData(i, 5);
					//String sEmailPrefix = util_excelmanipulationmgr.getCellData(i, 6);

					String sEmailCounter = util_excelmanipulationmgr.getCellData(i, 15);
					String sEmailPrefix = util_excelmanipulationmgr.getCellData(i, 16);
					String sEmail = null;
					
					int iCounter = Integer.parseInt(sEmailCounter);
					iCounter = iCounter + 1;
					sEmailCounter = Integer.toString(iCounter);
					sEmail = sEmailPrefix + "+" + sEmailCounter + "@gmail.com";
					
					//util_excelmanipulationmgr.setCellData(sEmailCounter, sDataFilePath, i, 5);
					//util_excelmanipulationmgr.setCellData(sEmail, sDataFilePath, i, 7);
					util_excelmanipulationmgr.setCellData(sEmailCounter, sDataFilePath, i, 15);
					util_excelmanipulationmgr.setCellData(sEmail, sDataFilePath, i, 17);
					
					util_constant.cCustEmail = sEmail;
					util_constant.cCustEmailCount = sEmailCounter;
				}
	        }
			
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		}	catch (Exception el)	
		{
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + el.getMessage());
		}
	}
	
	
	public static void updateFileUploadIDtoDataFile(String sID, String sFileUploadRequestID)
	{
		try
		{
			int iRowCount;
			String sTCID = null;
			
			Properties p = new Properties();
			File currentDirectory = new File(new File(".").getAbsolutePath());
			p.load(new FileInputStream("src//main//resources//properties.ini"));
			  
			String sTestDataFileName = p.getProperty("sdatafilepath");
			String sDataFilePath = currentDirectory.getCanonicalPath() + sTestDataFileName;
			
			util_excelmanipulationmgr.setExcelFile(sDataFilePath, "eReturns_CreateOrders");
			iRowCount = util_excelmanipulationmgr.getRowUsed();
			
			for(int i=1; i<iRowCount+1; i++)
			{
				sTCID = util_excelmanipulationmgr.getCellData(i, 0);
				
				if (sID.trim().equalsIgnoreCase(sTCID))
				{
					util_excelmanipulationmgr.setCellData(sFileUploadRequestID, sDataFilePath, i, 19);
					//util_excelmanipulationmgr.setCellData(sID, sDataFilePath, i, 19);
				}
	        }
			
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		}	catch (Exception el)	
		{
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + el.getMessage());
		}
	}
}
