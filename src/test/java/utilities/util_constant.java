package utilities;

import java.util.Date;

import modules.mod_alm;
import modules.mod_browser;
import modules.mod_extentReport;
import modules.mod_bpas_invitation;
import modules.mod_bpas_invitation_supplier_products;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import pageobjects.page_bpas_invitation;
import pageobjects.page_bpas_invitation_supplier_products;
import pageobjects.page_bpas_confirmation;
import pageobjects.page_bpas_registration;


public class util_constant {

	//ALM constant variables
	public static String cTc_Id = null;
	public static String cTc_Name = null;
	public static int cTS_No = 0;
	
	//LOG constant variables 
	public static Logger cLOG = LogManager.getLogger();
	
	//Jacob constant variables  
	private static String cCurrent_Dir = System.getProperty("user.dir");
	public static String cJacob_Path = cCurrent_Dir + "\\src\\main\\resources\\jacob-1.18-x86.dll";
	
	//Webdriver constant variables
	public static WebDriver cSel_Driver = null;//util_seleniumdrivermgr.getDriver();
	
	//Image constant variables
	public static String sImgPath = null;
	
	//PageObject constant variables
	public static page_bpas_invitation cPage_bpas_invitation = new page_bpas_invitation();
	public static page_bpas_invitation_supplier_products cPage_bpas_invitation_supplier_products = new page_bpas_invitation_supplier_products();
	public static page_bpas_confirmation cPage_bpas_confirmation = new page_bpas_confirmation();
	public static page_bpas_registration cPage_bpas_registration = new page_bpas_registration();
	
	//Modules constant variables
	public static mod_alm cMod_alm = new mod_alm();
	public static mod_browser cMod_browser = new mod_browser();
	public static mod_extentReport cMod_extentReport = new mod_extentReport();
	public static mod_bpas_invitation cMod_bpas_invitation = new mod_bpas_invitation();
	public static mod_bpas_invitation_supplier_products cMod_bpas_invitation_supplier_products = new mod_bpas_invitation_supplier_products();
	
	
	//UtilitiesObject constant variables
	public static util_common cUtil_Common = new util_common();
	public static util_excelmanipulationmgr cUtil_Excel = new util_excelmanipulationmgr();
	public static util_almreportmgr cUtil_Alm  = new util_almreportmgr();
	public static util_dataprovidermgr cUtil_DataProvider = new util_dataprovidermgr();
	public static util_extentreportmgr cUtil_ExtReport = new util_extentreportmgr();
	public static util_mswordreportmgr cUtil_MSWordReport = new util_mswordreportmgr();
	
	//Test constant variables
	public static String cExp_Result = null;
	public static String cAct_Result = null;
	public static String cCustEmail = null;
	public static String cCustEmailCount = null;
	public static int iExeStatus = 0;
	
	//DB constant variables
	public static String cRegistrationID_DB = null;
	
	//Added by Edward 23Dec2016 for excel reporting
	public static util_excelreportmgr cUtil_ExcelReport = new util_excelreportmgr();
	public static String cTc_TestCaseName = null;
	public static String cTc_ExcelFilePath = null;
	public static String cTc_ExcelFileName = null;
	public static String cTc_ExcelSheetName = null;
	public static String cTc_TestCaseFailed = "N";
	
	// Added by Edward 07Mar2016 for SNOW reporting
		public static util_SNOWIntegration utilSNOWIntegration = new util_SNOWIntegration();
		public static String cSnowURL = null;
		public static String cSnowUserName = null;
		public static String cSnowPassword  = null;
		public static String cSnowTestCaseNumber = null;
		public static String cSnowTestPlanTemplate = null;
		public static String cSnowTestPlanNumber = null;
		public static String cSnowTestPlanHistoryNumber = null;
		public static String cSnowRerun = null;
		public static String cTestCasesINI = null;
		public static String cPropertiesINI = null;
		public static String cTestCaseId = null;
		public static Date cCurrentDateTime = null;
		public static String cJenkinsSrcMainResourcesFilePath = null;
		public static String cLocalTestCasesINI = null;
		public static String cLocalPropertiesINI = null;

}
