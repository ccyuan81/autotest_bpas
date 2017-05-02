package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

public class util_dataprovidermgr {
        
	private static String sDataFilePath;
	private static String sTestDataFileName;
	private static final Logger LOG = LogManager.getLogger(util_dataprovidermgr.class);
	   
	@BeforeSuite
	public void setup() throws Exception
	{  
		Properties p = new Properties();
		File currentDirectory = new File(new File(".").getAbsolutePath());
		p.load(new FileInputStream("src//main//resources//properties.ini"));
		   
		sTestDataFileName = p.getProperty("sdatafilepath"); 
		sDataFilePath = currentDirectory.getCanonicalPath() + sTestDataFileName;
		
		LOG.info("Executing: util_dataprovider");
	}
 
	@DataProvider
	public static Object[][] getConsumerProductRegistrationDP(ITestContext context) throws Exception
	{
		String sXMLTestName = context.getName();
		util_excelmanipulationmgr.setExcelFile(sDataFilePath, "ConsumerProduct_Registration");
	    Object[][] testObjArray = util_excelmanipulationmgr.getTableArray(sXMLTestName);
	    return testObjArray;
	}
	
	@DataProvider
	public static Object[][] getALMConfiDP(ITestContext context) throws Exception
	{
		String sXMLTestName = context.getName();
		util_excelmanipulationmgr.setExcelFile(sDataFilePath, "ALM_Config");
	    Object[][] testObjArray = util_excelmanipulationmgr.getTableArray(sXMLTestName);
	    return testObjArray;
	}
	
	@DataProvider
	public static Object[][] getSFDCDP(ITestContext context) throws Exception
	{
		String sXMLTestName = context.getName();
		util_excelmanipulationmgr.setExcelFile(sDataFilePath, "SFDC_Page");
	    Object[][] testObjArray = util_excelmanipulationmgr.getTableArray(sXMLTestName);
	    return testObjArray;
	}
	
	@DataProvider
	public static Object[][] getTestDataDP(ITestContext context) throws Exception
	{
		String sXMLTestName = context.getName();
		util_excelmanipulationmgr.setExcelFile(sDataFilePath, "Test_SupplierData");
	    Object[][] testObjArray = util_excelmanipulationmgr.getTableArray(sXMLTestName);
	    return testObjArray;
	}	

}
