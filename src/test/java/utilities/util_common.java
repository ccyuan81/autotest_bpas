package utilities;
 
import net.sf.cglib.core.Local;

import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row; 
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.relevantcodes.extentreports.LogStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class util_common {
   
    private static int timeout = 60;
    public WebDriverWait wait;
    //public static WebDriver driver;  JAMES - 22 Sept
    private static final Logger LOG = LogManager.getLogger(util_common.class);
    private String sTestEvidenceFolderPath; 
       
    static util_extentreportmgr objExtReport = new util_extentreportmgr();
	static util_mswordreportmgr objMSWordRptManager = new util_mswordreportmgr();
	static util_almreportmgr objALMDriver = new util_almreportmgr(); 
	util_excelmanipulationmgr objExcelMgr = new util_excelmanipulationmgr();
	  
    public void navigateToURL(String URL){
    	String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		 
        try{
            //driver.navigate().to(URL); JAMES - 22 Sept
            util_constant.cSel_Driver.navigate().to(URL);
            
            wait.until(ExpectedConditions.urlToBe(URL));
            //reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
        }catch(Exception e) {
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
        }
    }

    public void click(By selector){
    	
    	String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();

        WebElement element = getElement(selector);
        waitForElementToBeClickable(selector);
        try{
            element.click();
           // reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
        }catch(Exception e){
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = e.getMessage();
			reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
        }
    }

    public void sendKeys(By selector, String value){
    	String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();

        WebElement element = getElement(selector);

        clearField(element);
        try {
            element.sendKeys(value);
            //reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
        }catch (Exception e){
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = e.getMessage();
			reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
        }
    }  

    public void clearField(WebElement element){
    	String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
        try {
            element.clear();
            waitForElementTextToBeEmpty(element);
           // reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
        }catch (Exception e){
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = e.getMessage();
			reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
        }
    }

    public void waitForElementTextToBeEmpty(WebElement element){
    	String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
        String text;
        try {
            text = element.getText();
            int maxRetries = 3;
            int retry = 0;
            while ((text.length() >= 1) || (retry < maxRetries)){
                retry++;
                text = element.getText();
            }
           // reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
        }catch (Exception e){
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = e.getMessage();
			reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
        }
    }

    public WebElement getElement(By selector){
    	String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
        try {
        	//reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
            //return driver.findElement(selector); JAMES - 22 Sept
        	return util_constant.cSel_Driver.findElement(selector);
        	 
        }catch (Exception e){
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = e.getMessage();
			reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
        }
        return null;
    }

    public String getElementText(By selector){
    	String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();

        try{
            //return driver.findElement(selector).getText(); JAMES - 22 Sept
        	//reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
        	return util_constant.cSel_Driver.findElement(selector).getText();
        }catch (Exception e){
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = e.getMessage();
			reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
        }
        return null;
    }
    
    
    
    public String getElementAttribute(By selector, String value){
    	String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
         try{
             //return driver.findElement(selector).getAttribute(value); JAMES - 22 Sept
        	// reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
        	 return util_constant.cSel_Driver.findElement(selector).getAttribute(value);
         }catch (Exception e){
 			util_constant.iExeStatus = 1;
 			util_constant.cAct_Result = e.getMessage();
 			reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
 			Assert.fail(util_constant.cAct_Result);
         }
         return null;
    }

    public boolean waitForElementToBeVisible(By selector) {
    	String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
        try {
            //wait = new WebDriverWait(driver, timeout); JAMES - 22 Sept
        	wait = new WebDriverWait(util_constant.cSel_Driver, timeout);
            wait.until(ExpectedConditions.presenceOfElementLocated(selector));
           
           // JavascriptExecutor js = (JavascriptExecutor) driver;  JAMES - 22 Sept
            JavascriptExecutor js = (JavascriptExecutor) util_constant.cSel_Driver;
    		//js.executeScript("arguments[0].style.border='3px dotted blue'", driver.findElement(selector)); JAMES - 22 Sept
            js.executeScript("arguments[0].style.border='3px dotted blue'", util_constant.cSel_Driver.findElement(selector));
    		
            //reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
            
    		return true;
        } catch (Exception e) {
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = e.getMessage();
			reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
        	return false;

        }
    }
    

    public boolean waitForElementToBeClickable(By selector) {
    	String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
		
        try {
        	//wait = new WebDriverWait(driver, timeout); JAMES - 22 Sept
        	wait = new WebDriverWait(util_constant.cSel_Driver, timeout);
        	
            wait.until(ExpectedConditions.elementToBeClickable(selector));

            Thread.sleep(1000);
            
            // JavascriptExecutor js = (JavascriptExecutor) driver;  JAMES - 22 Sept
            JavascriptExecutor js = (JavascriptExecutor) util_constant.cSel_Driver;
    		//js.executeScript("arguments[0].style.border='3px dotted blue'", driver.findElement(selector)); JAMES - 22 Sept
            js.executeScript("arguments[0].style.border='3px dotted blue'", util_constant.cSel_Driver.findElement(selector));
            //reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
            
    		return true;
        } catch (Exception e) {
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = e.getMessage();
			reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
        	return false;
        }
    }

    public void selectItemOnHoverOverMenu(By menu, By menuItem){
    	String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
        try {
            WebElement menuElement = getElement(menu);
            //Actions builder = new Actions(driver); JAMES - 22 Sept
            Actions builder = new Actions(util_constant.cSel_Driver);
            
            builder.moveToElement(menuElement).build().perform();

            //allow time for the menu contents to appear
            waitForElementToBeClickable(menuItem);
            WebElement menuOption = getElement(menuItem);
            menuOption.click();
          //  reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
        }catch (Exception e){
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = e.getMessage();
			reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
        }
    }

    public String getCurrentURL(){
    	String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
        try {
            //return driver.getCurrentUrl(); JAMES - 22 Sept
        	//reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
        	return util_constant.cSel_Driver.getCurrentUrl();
        } catch (Exception e){
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
            throw new WebDriverException("Could not get the current URL");
        }
    }

    public List<WebElement> getElements(By selector){
    	String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
        waitForElementToBeVisible(selector);
        try{
            //return driver.findElements(selector); JAMES - 22 Sept
        	//reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
        	return util_constant.cSel_Driver.findElements(selector);
            
        }catch(Exception e){
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = e.getMessage();
			reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
            throw new NoSuchElementException(String.format("The following element did not display: [%s] ", selector.toString()));
        }
    }
    
    public void selectCCBItem(By selector, String value)
    {
    	String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
    	try
    	{
    		//Select select = new Select(driver.findElement(selector)); JAMES - 22 Sept
    		Select select = new Select(util_constant.cSel_Driver.findElement(selector));
    		select.selectByVisibleText(value);
    		//reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
    	}catch(Exception e)
    	{
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = e.getMessage();
			reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
    	}

    }
    
    
    /**
	 * @Name		:	setBrowser			:	Set the browser to launch the target application. 
	 * @param		:	strBrowserType		:	the value giving the browser type
	 * @return		:	N/A
	 * @Author		:	JamesChan
	 */
	public void setBrowser(String strBrowserType)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
		try	
		{   
			if (strBrowserType.toUpperCase().equals("FF"))
			{
				//driver = new FirefoxDriver(); JAMES - 22 Sept
				//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS); JAMES - 22 Sept
				//driver.manage().window().maximize(); JAMES - 22 Sept
				
				util_constant.cSel_Driver = new FirefoxDriver();
				util_constant.cSel_Driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				util_constant.cSel_Driver.manage().window().maximize();
			}
			//reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			
		}	catch (Exception el)	
		{
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = el.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
		}
	}
	
	
	
	/**
	 * @Name		:	launchBrowser	:	Launch the browser of target application. 
	 * @param  		:	strURL  		:	the value giving the browser url to be launched
	 * @return      :	N/A
	 * @Author		:	JamesChan
	 */
	public void launchBrowser(String strURL)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
		try
		{
			//driver.get(strURL); JAMES - 22 Sept
			util_constant.cSel_Driver.get(strURL);
			//reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			
		}	catch (Exception el)
		{
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = el.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
		}
	}
	
	
	
	/**
	 * @Name		:	getStafWebDriverObj		:	return the driver for caller
	 * @param		:	N/A
	 * @return		:	N/A
	 * @Author		:	JamesChan
	 */
	public WebDriver getStafWebDriverObj()
	{
		//return  driver; JAMES - 22 Sept
		return util_constant.cSel_Driver;
	}

	public String getTestEvidenceFolderPath()
	{
		return sTestEvidenceFolderPath;
	}
	

	/**
	 * @Name		:	readExcel	:	return the data from excel sheet
	 * @param		:	filePath	:	location of data file
	 * @param		:	filename	:	name of data file
	 * @param		:	sheetName	: 	name of data sheet
	 * @param		:	tdFilter	:	name of test data row to pull
	 * @return		:	strResult	: 	a combination of string of each row data
	 * @Author		:	JamesChan
	 */
	public static String readExcel(String filePath,String fileName,String sheetName, String tdFilter) throws IOException
	{ 
		String strResult = null;
	    File file =    new File(filePath+"\\"+fileName);
	    FileInputStream inputStream = new FileInputStream(file);
	    Workbook guru99Workbook = null;
	    String fileExtensionName = fileName.substring(fileName.indexOf("."));

	    if(fileExtensionName.equals(".xlsx")){
	    	guru99Workbook = new XSSFWorkbook(inputStream);
	    }
	    else if(fileExtensionName.equals(".xls")){
	        guru99Workbook = new HSSFWorkbook(inputStream);
	    }

	    Sheet guru99Sheet = guru99Workbook.getSheet(sheetName);
	    int rowCount = guru99Sheet.getLastRowNum()-guru99Sheet.getFirstRowNum();

	    for (int i = 0; i < rowCount+1; i++) {
	        Row row = guru99Sheet.getRow(i);

	        if (row.getCell(1).getStringCellValue().toLowerCase()==tdFilter.toLowerCase())
	        {
		        for (int j = 0; j < row.getLastCellNum(); j++) {
		        	strResult = row.getCell(j).getStringCellValue()+"||" + strResult;
		        }
	        }
	    }
	    return strResult;
	 }

	
	 
	/**
	 * @Name		:	writeExcel	:	write the data to excel sheet
	 * @param		:	filePath	:	location of data file
	 * @param		:	filename	:	name of data file
	 * @param		:	sheetName	: 	name of data sheet
	 * @param		:	dataToWrite	:	data to write to excel
	 * @return		:	N/A
	 * @Author		:	JamesChan
	 */
	public void writeExcel(String filePath,String fileName,String sheetName,String[] dataToWrite, String tdFilter) throws IOException{
		 
        File file = new File(filePath+"\\"+fileName);
        FileInputStream inputStream = new FileInputStream(file);
        Workbook guru99Workbook = null;
        String fileExtensionName = fileName.substring(fileName.indexOf("."));
        

        if(fileExtensionName.equals(".xlsx")){
        	guru99Workbook = new XSSFWorkbook(inputStream);
        }
        else if(fileExtensionName.equals(".xls")){
            guru99Workbook = new HSSFWorkbook(inputStream);
        }

        Sheet sheet = guru99Workbook.getSheet(sheetName);

        int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();

        for (int i = 0; i < rowCount+1; i++) {
	        Row row = sheet.getRow(i);

	        if (row.getCell(1).getStringCellValue().toLowerCase()==tdFilter.toLowerCase())
	        {
		        for (int j = 0; j < row.getLastCellNum(); j++) {
		        	String [] sCol = dataToWrite[0].split("||");
		        	int iCol = Integer.parseInt(sCol[0]);
		        	
		        	if (iCol==j)
		        	{
		        		 Cell cell = row.createCell(iCol);
		                 cell.setCellValue(sCol[1]);
		        	}
		        }
	        }
	    }

        inputStream.close();

        FileOutputStream outputStream = new FileOutputStream(file);
        guru99Workbook.write(outputStream);
        outputStream.close();
	}
	
	
	
	/**
	 * @Name		:	encodePassword	:	to encrypt the password
	 * @param		:	String to encode
	 * @return		:	return encodedPassword
	 * @Remarks		: 	This encodedPassword can then be stored in datafile; to decrypt the password you may call decodePassword function.
	 * @Author		:	JamesChan
	 */
	public void encodePassword(String encodeString) throws IOException
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
		try
		{
			byte[] encodedBytes = Base64.encodeBase64(encodeString.getBytes());
			//reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}	catch (Exception el)
		{
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = el.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
		}
	}
	
	
	
	/**
	 * @Name		:	decodePassword	:	to decrypt the password
	 * @param		:	decodedPassword in string format
	 * @return		:	return decodedPassword
	 * @Remarks		: 	This decodePassword can be used to decrypt the password you may call decodePassword function.
	 * @Author		:	JamesChan
	 */
	public String decodePassword(String sPswd) throws IOException
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
		try
		{
			String decryptedPassword;
			byte[] decodedBytes = Base64.decodeBase64(sPswd);
			decryptedPassword = new String(decodedBytes);
			//LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
			//reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			
			return decryptedPassword;
		}	catch (Exception el)	
		{
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = el.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
			return "";
		}
	}
	 

	
	public static void reportResult(int iNewFlag, String sTCID, String sRemarks, String sTSDesc, String sResult, String sTRExpResult, String sTRActResult, String sScreenShotName, String sScreenShotExtension)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();

		try
		{
			if (util_constant.cSel_Driver != null)
			{
				if (sResult.trim().equalsIgnoreCase("passed"))
				{
					//objExtReport.logFile(LogStatus.PASS, sScreenShotName, sScreenShotExtension, sTCID, sRemarks, sTSDesc + "\n" + "Expected: " + sTRExpResult + "\n" + "Actual: " + sTRActResult);
					objExtReport.logFile(iNewFlag, LogStatus.PASS, sScreenShotName, sScreenShotExtension, sTCID, sRemarks, sTSDesc + " --- [Expected: " + sTRExpResult + " ||| Actual: " + sTRActResult + "]");
					LOG.info("Executed Method: " + sTSDesc + " - [Passed]");
				}
				else
				{
					objExtReport.logFile(iNewFlag, LogStatus.FAIL, sScreenShotName, sScreenShotExtension, sTCID, sRemarks, sTSDesc + " --- [Expected: " + sTRExpResult + " ||| Actual: " + sTRActResult + "]");
					LOG.info("Executed Method: " + sTSDesc + " - [Failed]");
				}
				
				objMSWordRptManager.captureAndSaveScreenShot(iNewFlag,sScreenShotName,sScreenShotExtension,sTCID,sRemarks,sTSDesc);
				objALMDriver.createAndUpdateTestStep(sTSDesc, sTSDesc, sTRExpResult, sTRActResult, sResult);
				
				// Added by Edward on 07-Mar-2017 for SNOW Integration
				util_constant.utilSNOWIntegration.addTestStep(util_constant.cSnowTestCaseNumber, sTSDesc, sTRExpResult, sTRActResult, sResult);
				//LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
			}
			else
			{
				if (sResult.trim().equalsIgnoreCase("passed"))
				{
					LOG.info("Executed Method: " + sTSDesc + " - [Passed]");
				}
				else
				{
					LOG.info("Executed Method: " + sTSDesc + " - [Failed]");
				}

				objALMDriver.createAndUpdateTestStep(sTSDesc, sTSDesc, sTRExpResult, sTRActResult, sResult);
				
				// Added by Edward on 07-Mar-2017 for SNOW Integration
				util_constant.utilSNOWIntegration.addTestStep(util_constant.cSnowTestCaseNumber, sTSDesc, sTRExpResult, sTRActResult, sResult);
			}
			
			//Added by Edward 29-Dec-2016 for excel test - Start				
			if (sResult.startsWith("Failed")){
				util_constant.cTc_TestCaseFailed = "Y";
				util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, sResult);
			}				
			//Added by Edward 29-Dec-2016 for excel test - End
			
			//reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}	catch (Exception el)	
		{
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = el.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
		}
	}
	

	
	
	
}
