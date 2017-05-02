package testcases;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
//import autoitx4java.AutoItX;



import utilities.util_common;
import utilities.util_constant;
import utilities.util_dataprovidermgr;

public class TestCases extends util_common
{
	@BeforeSuite  
	public void action_BeforeSuite() throws Exception
	{
		util_constant.cMod_extentReport.action_OpenExtentReport();
		util_constant.cUtil_DataProvider.setup();
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.utilSNOWIntegration.getPropertiesIni("src//main//resources//properties.ini");
		util_constant.utilSNOWIntegration.createIniFile();
		util_constant.utilSNOWIntegration.getPropertiesIni(util_constant.cLocalPropertiesINI);
		util_constant.utilSNOWIntegration.createTestPlan(util_constant.cSnowTestPlanTemplate);
		util_constant.utilSNOWIntegration.createTestCases(util_constant.cSnowTestPlanTemplate);
	} 
	
	@AfterSuite  
	public void action_AfterSuite() throws Exception
	{ 
		util_constant.cMod_extentReport.action_CloseExtentReport();
	}
	
	
	
	@BeforeClass
	public void loadTestName(ITestContext testContext) throws Exception
	{
		util_constant.cTc_Id = testContext.getName();
		
		//Added by Edward 23Dec2016 for excel reporting
		util_constant.cUtil_ExcelReport.createResultFolder();
		util_constant.cUtil_ExcelReport.createExcelResultFile();
	}
	
	@AfterTest
	public void action_AfterTest() throws Exception
	{ 
		util_constant.cMod_alm.action_DisconnectALM(util_constant.cTc_Id, util_constant.cTc_Name);
		
		// Commented by Edward on 07-Mar-2017 as feature integrated to SNOW Integration
//		util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.utilSNOWIntegration.testCaseEndUpdate(util_constant.cSnowTestCaseNumber, util_constant.cAct_Result);
	}
	
	//Added by Edward 01Dec2016
	 @BeforeMethod
     public void loadTestDescription(Method method) {
         Test test = method.getAnnotation(Test.class);
         util_constant.cTc_TestCaseName = test.description();
         util_constant.cTc_TestCaseFailed = "N";
     }
	 
	//Added by Edward 06Dec2016
	 @AfterMethod
	 public void getTestResult(ITestResult result){
		 util_constant.cUtil_ExcelReport.setTestResult(result);
	 }
	
	@Test (description="01.01 [P] Supplier Invitation - Invite New Supplier - Access to Supplier Invitation Page", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0101(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail,
	String sCompanyName, String sCompany, String sInvPageTitle, String sSupplierName, String sSupplierEmail, String sProduct, String sComponent, String sSite, String sRole, String sComments,
	String sConfirmTitle, String sRequestID)
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC01.01";
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_bpas_invitation.action_EnterInvitationForm(sID, sTCName, sPageTitle, sUserName, sPswd);
				util_constant.cMod_bpas_invitation.verify_InvitationLoginSuccess(sID, sTCName, sPageTitle);
				util_constant.cMod_bpas_invitation.action_SearchCompanyRecord(sID,sTCName,sCompanyName,sCompany,sInvPageTitle,sSupplierName,sSupplierEmail,sProduct, sComponent, sSite, sRole, sComments);
				util_constant.cMod_bpas_invitation.verify_SubmissionSuccess(sID, sTCName, sConfirmTitle, sRequestID);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
	}
	
	
		
	@Test (description="TC17.01[P] Seagate - Login-Logout - Login successfully with correct password", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1701(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer, 
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC17.01";
		
				
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);				
				util_constant.cMod_seagate_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_seagate_test_login.verify_PageLoginSuccess(sID, sTCName); 
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				//util_constant.cMod_alm.action_DisconnectALM(sID, sTCName);		
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	
	
	
	
	
	
	
	@Test (description="01.02 [P] Seagate Existing User - Login - Register Product [Valid) - DBs Check (Product Data) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0102(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC01.02";

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterSignInInfo(sID, sTCName, util_constant.cCustEmail, sPassword);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_after_Login(sID, sTCName, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_after_Login(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
	}
	
	@Test (description="01.03 [P] Seagate Existing User - Login - Register Product (Duplicated) - DBs Check - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0103(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC01.03";

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterSignInInfo(sID, sTCName, util_constant.cCustEmail, sPassword);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_after_Login(sID, sTCName, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_after_Login(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
	}
	
	
	@Test (description="01.04 [N] Seagate Existing User - Login - Register Product (Invalid - Missing in EDW) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0104(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC01.04";
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterSignInInfo(sID, sTCName, util_constant.cCustEmail, sPassword);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_after_Login(sID, sTCName, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationFailure_after_Login(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="01.05 [P] Seagate Existing User - No Login - Register Product (Valid) - DBs Check - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0105(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC01.05";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);	
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	@Test (description="01.06 [N] Seagate Existing User - No Login - Register Product (Invalid - Missing in EDW) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0106(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC01.06";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationFailure_before_cR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="02.01 [P] Seagate New User - Register Consumer (Non-embargo) - Activate Consumer - Register Product (Valid) - DBs Check (Product Data) - WS02 Checks - Eloqua Checks", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0201(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC02.01";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sCRBrowserURL);
				util_constant.cMod_ciam_seagate_custReg.action_EnterConsumerRegistrationInfo_NonEmbargo_before_pR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sPhone, sPassword, sQuestion, sAnswer);
				util_constant.cMod_ciam_seagate_custReg.verify_ConsumerRegistration_NonEmbargo_before_pR(sID, sTCName);
				util_constant.cMod_email.action_RetrieveAccountVerificationLink_Gmail(sID, sTCName, util_constant.cCustEmailCount, sCRBrowserURL);
				util_constant.cMod_ciam_seagate_custReg.verify_AccountVerificationRequest(sID, sTCName);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_after_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_after_cR(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02.verify_WS02EmailFilteringSuccess(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);		
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	@Test (description="02.02 [P] Seagate Existing User - Login - Register Product (Valid) - DBs Check (Product Data) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0202(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC02.02";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterSignInInfo(sID, sTCName, util_constant.cCustEmail, sPassword);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_after_Login(sID, sTCName, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_after_Login(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	@Test (description="02.03 [P] Seagate Existing User - No Login - Register Product (Valid) - DBs Check (Product Data) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0203(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC02.03";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="02.04 [N] Seagate Existing User - No Login - Register Product (Invalid - Missing in EDW) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0204(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC02.04";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationFailure_before_cR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="03.01 [P] Seagate New User - Register Product (Valid) - DBs Check (Product Data) - Register Consumer (Non-embargo) - No Activate Consumer", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0301(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC03.01";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_custReg.action_EnterConsumerRegistrationInfo_after_pR(sID, sTCName, sPassword, sQuestion, sAnswer);
				util_constant.cMod_ciam_seagate_custReg.verify_ConsumerRegistrationSuccess_after_pR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);		
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="03.02 [P] Seagate Existing User - No Login - Register Product (Valid) - DBs Check - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0302(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC03.02";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);	
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	
	@Test (description="03.03 [P] Seagate Existing User - No Login - Register Product (Duplicated) - DBs check - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0303(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC03.03";
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);	
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="03.04 [N] Seagate Existing User - No Login - Register Product (Invalid - Missing in EDW) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0304(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC03.04";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationFailure_before_cR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	
	@Test (description="04.01 [P] Seagate New User - Register Consumer (Non-embargo) - No Activate Consumer - Register Product (Valid) - DBs Check (Product Data)", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0401(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC04.01";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sCRBrowserURL);
				util_constant.cMod_ciam_seagate_custReg.action_EnterConsumerRegistrationInfo_NonEmbargo_before_pR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sPhone, sPassword, sQuestion, sAnswer);
				util_constant.cMod_ciam_seagate_custReg.verify_ConsumerRegistration_NonEmbargo_before_pR(sID, sTCName);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_after_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_after_cR(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);	
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="04.02 [P] Seagate Existing User - No Login - Register Product (Valid) - DBs Check (Product Data) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0402(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC04.02";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);		
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	
	@Test (description="04.03 [P] Seagate Existing User - No Login - Register Product (Duplicated) - DBs Check (Product Data) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0403(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC04.03";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="04.04 [N] Seagate Existing User - No Login - Register Product (Invalid - Missing in EDW) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0404(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC04.04";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationFailure_before_cR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="05.01 [P] Seagate New User - Register Product (Valid) - DBs Check (Product Data) - Register Consumer (Embargo) - Activate Consumer - WS02 Checks - Eloqua Checks", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0501(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC05.01";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_custReg.action_EnterConsumerRegistrationInfo_after_pR(sID, sTCName, sPassword, sQuestion, sAnswer);
				util_constant.cMod_ciam_seagate_custReg.verify_ConsumerRegistrationSuccess_after_pR(sID, sTCName);
				util_constant.cMod_email.action_RetrieveAccountVerificationLink_Gmail(sID, sTCName, util_constant.cCustEmailCount, sCRBrowserURL);
				util_constant.cMod_ciam_seagate_custReg.verify_AccountVerificationRequest(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02.verify_WS02EmailFilteringSuccess(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	@Test (description="05.02 [P] Seagate Existing User - Login - Register Product [Valid) - DBs Check (Product Data) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0502(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC05.02";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterSignInInfo(sID, sTCName, util_constant.cCustEmail, sPassword);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_after_Login(sID, sTCName, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_after_Login(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	@Test (description="05.03 [P] Seagate Existing User - Login - Register Product (Duplicated) - DBs Check - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0503(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC05.03";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterSignInInfo(sID, sTCName, util_constant.cCustEmail, sPassword);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_after_Login(sID, sTCName, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_after_Login(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="05.04 [N] Seagate Existing User - Login - Register Product (Invalid - Missing in EDW) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0504(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC05.04";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterSignInInfo(sID, sTCName, util_constant.cCustEmail, sPassword);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_after_Login(sID, sTCName, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationFailure_after_Login(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="05.05 [P] Seagate Existing User - No Login - Register Product (Valid) - DBs Check - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0505(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC05.05";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);	
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	@Test (description="05.06 [N] Seagate Existing User - No Login - Register Product (Invalid - Missing in EDW) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0506(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC05.06";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationFailure_before_cR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="06.01 [P] Seagate New User - Register Consumer (Embargo) - Activate Consumer - Register Product (Valid) - DBs Check (Product Data) - WS02 Checks - Eloqua Checks ", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0601(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC06.01";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sCRBrowserURL);
				util_constant.cMod_ciam_seagate_custReg.action_EnterConsumerRegistrationInfo_Embargo_before_pR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sPhone, sPassword, sQuestion, sAnswer);
				util_constant.cMod_ciam_seagate_custReg.verify_ConsumerRegistration_Embargo_before_pR(sID, sTCName);
				util_constant.cMod_email.action_RetrieveAccountVerificationLink_Gmail(sID, sTCName, util_constant.cCustEmailCount, sCRBrowserURL);
				util_constant.cMod_ciam_seagate_custReg.verify_AccountVerificationRequest(sID, sTCName);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_after_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_after_cR(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02.verify_WS02EmailFilteringSuccess(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	@Test (description="06.02 [P] Seagate Existing User - Login - Register Product (Valid) - DBs Check (Product Data) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0602(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC06.02";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterSignInInfo(sID, sTCName, util_constant.cCustEmail, sPassword);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_after_Login(sID, sTCName, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_after_Login(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	@Test (description="06.03 [P] Seagate Existing User - No Login - Register Product (Valid) - DBs Check (Product Data) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0603(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC06.03";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);	
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="06.04 [N] Seagate Existing User - No Login - Register Product (Invalid - Missing in EDW) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0604(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC06.04";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationFailure_before_cR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="07.01 [P] Seagate New User - Register Product (Valid) - DBs Check (Product Data) - Register Consumer (Embargo) - No Activate Consumer", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0701(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC07.01";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_custReg.action_EnterConsumerRegistrationInfo_after_pR(sID, sTCName, sPassword, sQuestion, sAnswer);
				util_constant.cMod_ciam_seagate_custReg.verify_ConsumerRegistrationSuccess_after_pR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);				
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="07.02 [P] Seagate Existing User - No Login - Register Product (Valid) - DBs Check - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0702(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC07.02";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	
	@Test (description="07.03 [P] Seagate Existing User - No Login - Register Product (Duplicated) - DBs check - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0703(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC07.03";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);	
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="07.04 [N] Seagate Existing User - No Login - Register Product (Invalid - Missing in EDW) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0704(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC07.04";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationFailure_before_cR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	
	@Test (description="08.01 [P] Seagate New User - Register Consumer (Embargo) - No Activate Consumer - Register Product (Valid) - DBs Check (Product Data)", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0801(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC08.01";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sCRBrowserURL);
				util_constant.cMod_ciam_seagate_custReg.action_EnterConsumerRegistrationInfo_Embargo_before_pR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sPhone, sPassword, sQuestion, sAnswer);
				util_constant.cMod_ciam_seagate_custReg.verify_ConsumerRegistration_Embargo_before_pR(sID, sTCName);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_after_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_after_cR(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);	
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="08.02 [P] Seagate Existing User - No Login - Register Product (Valid) - DBs Check (Product Data) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0802(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC08.02";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	
	@Test (description="08.03 [P] Seagate Existing User - No Login - Register Product (Duplicated) - DBs Check (Product Data) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0803(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC08.03";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_seagate_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="08.04 [N] Seagate Existing User - No Login - Register Product (Invalid - Missing in EDW) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0804(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC08.04";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_seagate_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_seagate_prodReg.verify_ProductRegistrationFailure_before_cR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	
	@Test (description="09.01 [P] Lacie New User - Register Product (Valid) - DBs Check (Product Data) - Register Consumer (Non-embargo) - Activate Consumer - WS02 Checks - Eloqua Checks", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0901(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC09.01";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_custReg.action_EnterConsumerRegistrationInfo_after_pR(sID, sTCName, sPassword, sQuestion, sAnswer);
				util_constant.cMod_ciam_lacie_custReg.verify_ConsumerRegistrationSuccess_after_pR(sID, sTCName);
				util_constant.cMod_email.action_RetrieveAccountVerificationLink_Gmail(sID, sTCName, util_constant.cCustEmailCount, sCRBrowserURL);
				util_constant.cMod_ciam_lacie_custReg.verify_AccountVerificationRequest(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02.verify_WS02EmailFilteringSuccess(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	@Test (description="09.02 [P] Lacie Existing User - Login - Register Product [Valid) - DBs Check (Product Data) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0902(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC09.02";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterSignInInfo(sID, sTCName, util_constant.cCustEmail, sPassword);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_after_Login(sID, sTCName, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_after_Login(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	@Test (description="09.03 [P] Lacie Existing User - Login - Register Product (Duplicated) - DBs Check - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0903(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC09.03";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterSignInInfo(sID, sTCName, util_constant.cCustEmail, sPassword);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_after_Login(sID, sTCName, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_after_Login(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="09.04 [N] Lacie Existing User - Login - Register Product (Invalid - Missing in EDW) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0904(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC09.04";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterSignInInfo(sID, sTCName, util_constant.cCustEmail, sPassword);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_after_Login(sID, sTCName, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationFailure_after_Login(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="09.05 [P] Lacie Existing User - No Login - Register Product (Valid) - DBs Check - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0905(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC09.05";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	@Test (description="09.06 [N] Lacie Existing User - No Login - Register Product (Invalid - Missing in EDW) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC0906(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC09.06";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationFailure_before_cR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="10.01 [P] Lacie New User - Register Consumer (Non-embargo) - Activate Consumer - Register Product (Valid) - DBs Check (Product Data) - WS02 Checks - Eloqua Checks", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1001(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC10.01";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sCRBrowserURL);
				util_constant.cMod_ciam_lacie_custReg.action_EnterConsumerRegistrationInfo_NonEmbargo_before_pR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sPhone, sPassword, sQuestion, sAnswer);
				util_constant.cMod_ciam_lacie_custReg.verify_ConsumerRegistration_NonEmbargo_before_pR(sID, sTCName);
				util_constant.cMod_email.action_RetrieveAccountVerificationLink_Gmail(sID, sTCName, util_constant.cCustEmailCount, sCRBrowserURL);
				util_constant.cMod_ciam_lacie_custReg.verify_AccountVerificationRequest(sID, sTCName);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_after_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_after_cR(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02.verify_WS02EmailFilteringSuccess(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	@Test (description="10.02 [P] Lacie Existing User - Login - Register Product (Valid) - DBs Check (Product Data) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1002(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC10.02";

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterSignInInfo(sID, sTCName, util_constant.cCustEmail, sPassword);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_after_Login(sID, sTCName, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_after_Login(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	@Test (description="10.03 [P] Lacie Existing User - No Login - Register Product (Valid) - DBs Check (Product Data) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1003(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC10.03";
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="10.04 [N] Lacie Existing User - No Login - Register Product (Invalid - Missing in EDW) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1004(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC10.04";
		

		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationFailure_before_cR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="11.01 [P] Lacie New User - Register Product (Valid) - DBs Check (Product Data) - Register Consumer (Non-embargo) - No Activate Consumer", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1101(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC11.01";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_custReg.action_EnterConsumerRegistrationInfo_after_pR(sID, sTCName, sPassword, sQuestion, sAnswer);
				util_constant.cMod_ciam_lacie_custReg.verify_ConsumerRegistrationSuccess_after_pR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);			
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="11.02 [P] Lacie Existing User - No Login - Register Product (Valid) - DBs Check - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1102(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC11.02";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);		
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	
	@Test (description="11.03 [P] Lacie Existing User - No Login - Register Product (Duplicated) - DBs check - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1103(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC11.03";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);	
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="11.04 [N] Lacie Existing User - No Login - Register Product (Invalid - Missing in EDW) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1104(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC11.04";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationFailure_before_cR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	
	@Test (description="12.01 [P] Lacie New User - Register Consumer (Non-embargo) - No Activate Consumer - Register Product (Valid) - DBs Check (Product Data)", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1201(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC12.01";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sCRBrowserURL);
				util_constant.cMod_ciam_lacie_custReg.action_EnterConsumerRegistrationInfo_NonEmbargo_before_pR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sPhone, sPassword, sQuestion, sAnswer);
				util_constant.cMod_ciam_lacie_custReg.verify_ConsumerRegistration_NonEmbargo_before_pR(sID, sTCName);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_after_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_after_cR(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="12.02 [P] Lacie Existing User - No Login - Register Product (Valid) - DBs Check (Product Data) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1202(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC12.02";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	
	@Test (description="12.03 [P] Lacie Existing User - No Login - Register Product (Duplicated) - DBs Check (Product Data) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1203(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC12.03";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="12.04 [N] Lacie Existing User - No Login - Register Product (Invalid - Missing in EDW) - Non-embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1204(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC12.04";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_NonEmbargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationFailure_before_cR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="13.01 [P] Lacie New User - Register Product (Valid) - DBs Check (Product Data) - Register Consumer (Embargo) - Activate Consumer - WS02 Checks - Eloqua Checks", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1301(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC13.01";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_custReg.action_EnterConsumerRegistrationInfo_after_pR(sID, sTCName, sPassword, sQuestion, sAnswer);
				util_constant.cMod_ciam_lacie_custReg.verify_ConsumerRegistrationSuccess_after_pR(sID, sTCName);
				util_constant.cMod_email.action_RetrieveAccountVerificationLink_Gmail(sID, sTCName, util_constant.cCustEmailCount, sCRBrowserURL);
				util_constant.cMod_ciam_lacie_custReg.verify_AccountVerificationRequest(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02.verify_WS02EmailFilteringSuccess(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	@Test (description="13.02 [P] Lacie Existing User - Login - Register Product [Valid) - DBs Check (Product Data) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1302(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;
		
		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC13.02";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterSignInInfo(sID, sTCName, util_constant.cCustEmail, sPassword);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_after_Login(sID, sTCName, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_after_Login(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	@Test (description="13.03 [P] Lacie Existing User - Login - Register Product (Duplicated) - DBs Check - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1303(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC13.03";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterSignInInfo(sID, sTCName, util_constant.cCustEmail, sPassword);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_after_Login(sID, sTCName, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_after_Login(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="13.04 [N] Lacie Existing User - Login - Register Product (Invalid - Missing in EDW) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1304(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC13.04";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterSignInInfo(sID, sTCName, util_constant.cCustEmail, sPassword);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_after_Login(sID, sTCName, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationFailure_after_Login(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="13.05 [P] Lacie Existing User - No Login - Register Product (Valid) - DBs Check - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1305(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC13.05";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	@Test (description="13.06 [N] Lacie Existing User - No Login - Register Product (Invalid - Missing in EDW) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1306(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC13.06";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationFailure_before_cR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="14.01 [P] Lacie New User - Register Consumer (Embargo) - Activate Consumer - Register Product (Valid) - DBs Check (Product Data) - WS02 Checks - Eloqua Checks", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1401(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC14.01";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sCRBrowserURL);
				util_constant.cMod_ciam_lacie_custReg.action_EnterConsumerRegistrationInfo_Embargo_before_pR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sPhone, sPassword, sQuestion, sAnswer);
				util_constant.cMod_ciam_lacie_custReg.verify_ConsumerRegistration_Embargo_before_pR(sID, sTCName);
				util_constant.cMod_email.action_RetrieveAccountVerificationLink_Gmail(sID, sTCName, util_constant.cCustEmailCount, sCRBrowserURL);
				util_constant.cMod_ciam_lacie_custReg.verify_AccountVerificationRequest(sID, sTCName);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_after_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_after_cR(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02.verify_WS02EmailFilteringSuccess(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	@Test (description="14.02 [P] Lacie Existing User - Login - Register Product (Valid) - DBs Check (Product Data) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1402(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC14.02";
		

		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterSignInInfo(sID, sTCName, util_constant.cCustEmail, sPassword);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_after_Login(sID, sTCName, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_after_Login(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	@Test (description="14.03 [P] Lacie Existing User - No Login - Register Product (Valid) - DBs Check (Product Data) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1403(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC14.03";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="14.04 [N] Lacie Existing User - No Login - Register Product (Invalid - Missing in EDW) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1404(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC14.04";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationFailure_before_cR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="15.01 [P] Lacie New User - Register Product (Valid) - DBs Check (Product Data) - Register Consumer (Embargo) - No Activate Consumer", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1501(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC15.01";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_custReg.action_EnterConsumerRegistrationInfo_after_pR(sID, sTCName, sPassword, sQuestion, sAnswer);
				util_constant.cMod_ciam_lacie_custReg.verify_ConsumerRegistrationSuccess_after_pR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);		
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="15.02 [P] Lacie Existing User - No Login - Register Product (Valid) - DBs Check - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1502(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC15.02";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	
	@Test (description="15.03 [P] Lacie Existing User - No Login - Register Product (Duplicated) - DBs check - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1503(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC15.03";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);		
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="15.04 [N] Lacie Existing User - No Login - Register Product (Invalid - Missing in EDW) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1504(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC15.04";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationFailure_before_cR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	
	@Test (description="16.01 [P] Lacie New User - Register Consumer (Embargo) - No Activate Consumer - Register Product (Valid) - DBs Check (Product Data)", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1601(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC16.01";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sCRBrowserURL);
				util_constant.cMod_ciam_lacie_custReg.action_EnterConsumerRegistrationInfo_Embargo_before_pR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sPhone, sPassword, sQuestion, sAnswer);
				util_constant.cMod_ciam_lacie_custReg.verify_ConsumerRegistration_Embargo_before_pR(sID, sTCName);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_after_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_after_cR(sID, sTCName, sSerialNo, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="16.02 [P] Lacie Existing User - No Login - Register Product (Valid) - DBs Check (Product Data) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1602(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC16.02";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	
	@Test (description="16.03 [P] Lacie Existing User - No Login - Register Product (Duplicated) - DBs Check (Product Data) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1603(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC16.03";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				//util_constant.cMod_ciam_lacie_custReg.action_PopulateConsumerRegistrationEmail(sID, sTCName);
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationSuccess_before_cR(sID, sTCName, sSerialNo, sPlaceOfPurchase, sTitle, sIncome, sIndustry, sGender, sAge, sMsgPart1, sMsgPart2);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_SFDC_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Eloqua_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Personal_DB(sID, sTCName, util_constant.cCustEmail);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductData_Product_DB(sID, sTCName, sSerialNo, util_constant.cCustEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	@Test (description="16.04 [N] Lacie Existing User - No Login - Register Product (Invalid - Missing in EDW) - Embargo country", dataProvider="getTestDataDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1604(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sCRBrowserURL, String sCountry, String sLanguage, String sEmailCounter, String sEmailPrefix, String sEmail, String sFirstName,
	String sLastName, String sZip, String sPhone, String sPassword, String sQuestion, String sAnswer, String sPRBrowserURL,
	String sSerialNo, String sPlaceOfPurchase, String sTitle, String sIncome, String sIndustry, String sGender, String sAge, String sMsgPart1, String sMsgPart2,
	String sSFDCBrowserURL, String sSFDCPageTitle, String sSFDCPageEnv, String sSFDCUserName, String sSFDCPswd,
	String sEloquaBrowserURL, String sEloquaPageTitle, String sEloquaCompName, String sEloquaUserName, String sEloquaPswd,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		util_constant.cCustEmailCount = sEmailCounter;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC16.04";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sPRBrowserURL);
				util_constant.cMod_ciam_lacie_prodReg.action_EnterProductRegistrationInfo_Embargo_before_cR(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip, sSerialNo, sPlaceOfPurchase);
				util_constant.cMod_ciam_lacie_prodReg.verify_ProductRegistrationFailure_before_cR(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}
	
	
	
	
	@Test (description="TC17.02[P] Seagate - Login-Logout - Forgot password with Q&A not found within time limit email respond", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1702(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
			
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC17.02";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_email.action_Delete_Gmail(sID, sTCName,sBrowserURL);
				util_constant.cMod_test_seagate_reset_password.action_EnterLoginInfo(sID, sTCName, sPageTitle, sUserName);
				util_constant.cMod_test_seagate_reset_password.verify_PageLoginSuccess(sID, sTCName);
				util_constant.cMod_test_seagate_reset_password.action_RetrieveAccountPasswordResetLink_Gmail(sID, sTCName, sBrowserURL, sEmail);
				util_constant.cMod_test_seagate_reset_password.verify_PasswordReset(sID, sTCName, sUserName, sResetPass);
				util_constant.cMod_test_seagate_reset_password.action_RetrieveAccountPasswordChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);						
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC17.03[P] Seagate - Login-Logout - Forgot password with no Q&A selected within time limit email respond", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1703(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
			
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC17.03";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_email.action_Delete_Gmail(sID, sTCName,sBrowserURL);
				util_constant.cMod_test_seagate_reset_password.action_EnterLoginInfo(sID, sTCName, sPageTitle, sUserName);
				util_constant.cMod_test_seagate_reset_password.verify_PageResetSuccess(sID, sTCName);
				util_constant.cMod_test_seagate_reset_password.action_RetrieveAccountPasswordResetLink_Gmail(sID, sTCName, sBrowserURL, sEmail);
				util_constant.cMod_test_seagate_reset_password.verify_PasswordReset(sID, sTCName, sUserName, sResetPass);
				util_constant.cMod_test_seagate_reset_password.action_RetrieveAccountPasswordChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC17.04[P] Seagate - Login-Logout - Forgot password and Q&A within time limit email respond", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1704(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
			
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC17.04";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_email.action_Delete_Gmail(sID, sTCName,sBrowserURL);
				util_constant.cMod_test_seagate_reset_password.action_EnterLoginInfo(sID, sTCName, sPageTitle, sUserName);
				util_constant.cMod_test_seagate_reset_password.verify_PageResetSecretSuccess(sID, sTCName);			    
				util_constant.cMod_test_seagate_reset_password.action_RetrieveAccountPasswordResetLink_Gmail(sID, sTCName, sBrowserURL, sEmail);
				util_constant.cMod_test_seagate_reset_password.verify_PasswordReset(sID, sTCName, sUserName, sResetPass);
				util_constant.cMod_test_seagate_reset_password.action_RetrieveAccountPasswordChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC17.05[N] Seagate - Login-Logout - Forgot password but answer wrong Q&A within time limit email respond", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1705(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
			
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;
		

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC17.05";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_email.action_Delete_Gmail(sID, sTCName,sBrowserURL);
				util_constant.cMod_test_seagate_reset_password.action_EnterLoginInfo(sID, sTCName, sPageTitle, sUserName);
				util_constant.cMod_test_seagate_reset_password.verify_PageResetSecretFail(sID, sTCName, sAnswer);
				util_constant.cMod_test_seagate_reset_password.action_RetrieveAccountPasswordResetLink_Gmail(sID, sTCName, sBrowserURL, sEmail);
				util_constant.cMod_test_seagate_reset_password.verify_PasswordReset(sID, sTCName, sUserName, sResetPass);
				util_constant.cMod_test_seagate_reset_password.action_RetrieveAccountPasswordChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);						
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC17.06[P] Seagate - Login-Logout - Forgot password but answer correct Q&A within time limit email respond", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1706(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
			
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC17.06";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_email.action_Delete_Gmail(sID, sTCName,sBrowserURL);
				util_constant.cMod_test_seagate_reset_password.action_EnterLoginInfo(sID, sTCName, sPageTitle, sUserName);
				util_constant.cMod_test_seagate_reset_password.verify_PageResetSecretTrue(sID, sTCName, sUserName, sResetPass, sAnswer);
				util_constant.cMod_test_seagate_reset_password.action_RetrieveAccountPasswordChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC17.07[P] Seagate - Login-Logout - Login more than 10 times within time limit email respond", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1707(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC17.07";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_seagate_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_seagate_test_login.verify_EnterLoginFailed(sID, sTCName, sUserName, sPswd, sResetPass, sAnswer);
				util_constant.cMod_test_seagate_reset_password.action_RetrieveAccountPasswordChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02_userprofile.verify_WS02EmailFilteringUserProfileSuccess(sID, sTCName);
				util_constant.cMod_ws02_userprofile.action_WS02AccLockedChecked(sID, sTCName);					
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);						
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	@Test (description="TC17.08[P] Seagate - Login/Logout - Forgot password with Q&A found without time limit email respond", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1708(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
			
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC17.08";
		
				
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_test_seagate_reset_password.action_EnterLoginInfo(sID, sTCName, sPageTitle, sUserName);
				util_constant.cMod_test_seagate_reset_password.verify_PageResetSuccess(sID, sTCName);
				util_constant.cMod_test_seagate_reset_password.action_RetrieveAccountPasswordResetLink_Gmail(sID, sTCName, sBrowserURL, sEmail);
				util_constant.cMod_test_seagate_reset_password.verify_PasswordReset(sID, sTCName, sUserName, sResetPass);
				util_constant.cMod_test_seagate_reset_password.action_RetrieveAccountPasswordResetLink_Gmail(sID, sTCName, sBrowserURL, sEmail);
				util_constant.cMod_seagate_test_login.verify_SendExpiredLink(sID, sTCName);				
				util_constant.cMod_test_seagate_reset_password.action_RetrieveAccountPasswordChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_test_seagate_reset_password.action_RetrieveAccountPasswordResetLink_Gmail(sID, sTCName, sBrowserURL, sEmail);
				util_constant.cMod_test_seagate_reset_password.verify_PasswordReset(sID, sTCName, sUserName, sResetPass);
				util_constant.cMod_test_seagate_reset_password.action_RetrieveAccountPasswordChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	
	@Test (description="TC17.09[N] Seagate - Login-Logout - Forgot password with given wrong email address", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1709(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
			
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC17.09";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_test_seagate_reset_password.action_EnterNegativeInfo(sID, sTCName, sBrowserURL, sUserName);				
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);						
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC17.10[P] Seagate - Login-Logout - Login with account locked and unverified", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1710(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC17.10";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_seagate_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_seagate_test_login.verify_EnterLoginAccLocked(sID, sTCName);
				util_constant.cMod_seagate_test_login.action_RetrieveAccountVerificationLink_Gmail(sID, sTCName, sBrowserURL, sEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02_userprofile.verify_WS02EmailFilteringUserProfileSuccess(sID, sTCName);				
				util_constant.cMod_ws02_userprofile.action_WS02AccVerifiedChecked(sID, sTCName);				
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	@Test (description="TC17.11[P] Seagate - Login-Logout - Login with account locked and verified", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1711(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC17.11";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_seagate_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_seagate_test_login.verify_EnterLoginAccLockedVer(sID, sTCName, sUserName);
				util_constant.cMod_seagate_test_login.action_RetrieveAccountPasswordResetLink_NoGmail(sID, sTCName, sBrowserURL, sEmail);				
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC17.12[N] Seagate - Login-Logout - Login with user who does not exist in WSO2 Identity Server", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1712(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC17.12";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_seagate_test_login.action_EnterLoginFailed(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);			
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC18.01[P] LaCie - Login-Logout - Login successfully with correct password", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1801(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
					
	
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC18.01";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_lacie_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_lacie_test_login.verify_PageLoginLaCieSuccess(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);						
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC18.02[P] LaCie - Login-Logout - Forgot password with Q&A not found within time limit email respond", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1802(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
			
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC18.02";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_email.action_DeleteLaCie_Gmail(sID, sTCName,sBrowserURL);
				util_constant.cMod_test_lacie_reset_password.action_EnterLoginInfo(sID, sTCName, sPageTitle, sUserName);
				util_constant.cMod_test_lacie_reset_password.verify_PageLoginSuccess(sID, sTCName);
				util_constant.cMod_test_lacie_reset_password.action_RetrieveLaCieAccountPasswordResetLink_Gmail(sID, sTCName, sBrowserURL, sEmail);
				util_constant.cMod_test_lacie_reset_password.verify_LaCiePasswordReset(sID, sTCName, sUserName, sResetPass);				
				util_constant.cMod_test_lacie_reset_password.action_RetrieveLaCieAccountPasswordChangedMessage_Gmail(sID, sTCName, sBrowserURL);				
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC18.03[P] LaCie - Login-Logout - Forgot password with no Q&A selected within time limit email respond", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1803(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
			
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC18.03";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_email.action_DeleteLaCie_Gmail(sID, sTCName,sBrowserURL);
				util_constant.cMod_test_lacie_reset_password.action_EnterLoginInfo(sID, sTCName, sPageTitle, sUserName);
				util_constant.cMod_test_lacie_reset_password.verify_PageResetSuccess(sID, sTCName);
				util_constant.cMod_test_lacie_reset_password.action_RetrieveLaCieAccountPasswordResetLink_Gmail(sID, sTCName, sBrowserURL, sEmail);
				util_constant.cMod_test_lacie_reset_password.verify_LaCiePasswordReset(sID, sTCName, sUserName, sResetPass);
				util_constant.cMod_test_lacie_reset_password.action_RetrieveLaCieAccountPasswordChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	@Test (description="TC18.04[P] LaCie - Login-Logout - Forgot password and Q&A within time limit email respond", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1804(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
			
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC18.04";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_email.action_DeleteLaCie_Gmail(sID, sTCName,sBrowserURL);
				util_constant.cMod_test_lacie_reset_password.action_EnterLoginInfo(sID, sTCName, sPageTitle, sUserName);
				util_constant.cMod_test_lacie_reset_password.verify_PageResetSecretSuccess(sID, sTCName);
				util_constant.cMod_test_lacie_reset_password.action_RetrieveLaCieAccountPasswordResetLink_Gmail(sID, sTCName, sBrowserURL, sEmail);
				util_constant.cMod_test_lacie_reset_password.verify_LaCiePasswordReset(sID, sTCName, sUserName, sResetPass);
				util_constant.cMod_test_lacie_reset_password.action_RetrieveLaCieAccountPasswordChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC18.05[N] LaCie - Login-Logout - Forgot password but answer wrong Q&A within time limit email respond", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1805(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
			
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC18.05";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_email.action_DeleteLaCie_Gmail(sID, sTCName,sBrowserURL);
				util_constant.cMod_test_lacie_reset_password.action_EnterLoginInfo(sID, sTCName, sPageTitle, sUserName);
				util_constant.cMod_test_lacie_reset_password.verify_PageResetSecretFail(sID, sTCName, sAnswer);
				util_constant.cMod_test_lacie_reset_password.action_RetrieveLaCieAccountPasswordResetLink_Gmail(sID, sTCName, sBrowserURL, sEmail);
				util_constant.cMod_test_lacie_reset_password.verify_LaCiePasswordReset(sID, sTCName, sUserName, sResetPass);
				util_constant.cMod_test_lacie_reset_password.action_RetrieveLaCieAccountPasswordChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);						
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC18.06[P] LaCie - Login-Logout - Forgot password but answer correct Q&A within time limit email respond", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1806(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
			
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC18.06";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_email.action_DeleteLaCie_Gmail(sID, sTCName,sBrowserURL);
				util_constant.cMod_test_lacie_reset_password.action_EnterLoginInfo(sID, sTCName, sPageTitle, sUserName);
				util_constant.cMod_test_lacie_reset_password.verify_PageLaCieResetSecretTrue(sID, sTCName, sUserName, sResetPass, sAnswer);
				util_constant.cMod_test_lacie_reset_password.action_RetrieveLaCieAccountPasswordChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);						
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC18.07[P] LaCie - Login-Logout - Login more than 10 times within time limit email respond", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1807(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	
	
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC18.07";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_lacie_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_lacie_test_login.verify_EnterLaCieLoginFailed(sID, sTCName, sUserName, sPswd, sResetPass, sAnswer);
				util_constant.cMod_test_lacie_reset_password.action_RetrieveLaCieAccountPasswordChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02_userprofile.verify_WS02EmailFilteringUserProfileSuccess(sID, sTCName);
				util_constant.cMod_ws02_userprofile.action_WS02AccLockedChecked(sID, sTCName);								
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);						
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC18.08[P] LaCie - Login/Logout - Forgot password with Q&A not found without time limit email respond", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1808(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
			
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC18.08";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_email.action_DeleteLaCie_Gmail(sID, sTCName,sBrowserURL);
				util_constant.cMod_test_lacie_reset_password.action_EnterLoginInfo(sID, sTCName, sPageTitle, sUserName);
				util_constant.cMod_test_lacie_reset_password.verify_PageResetSuccess(sID, sTCName);
				util_constant.cMod_test_lacie_reset_password.action_RetrieveLaCieAccountPasswordResetLink_Gmail(sID, sTCName, sBrowserURL, sEmail);
				util_constant.cMod_test_lacie_reset_password.verify_LaCiePasswordReset(sID, sTCName, sUserName, sResetPass);
				util_constant.cMod_test_lacie_reset_password.action_RetrieveLaCieAccountPasswordResetLink_Gmail(sID, sTCName, sBrowserURL, sEmail);
				util_constant.cMod_lacie_test_login.verify_SendLaCieExpiredLink(sID, sTCName);				
				util_constant.cMod_test_lacie_reset_password.action_RetrieveLaCieAccountPasswordChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_test_lacie_reset_password.action_RetrieveLaCieAccountPasswordResetLink_Gmail(sID, sTCName, sBrowserURL, sEmail);
				util_constant.cMod_test_lacie_reset_password.verify_LaCiePasswordReset(sID, sTCName, sUserName, sResetPass);
				util_constant.cMod_test_lacie_reset_password.action_RetrieveLaCieAccountPasswordChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);						
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	
	@Test (description="TC18.09[N] LaCie - Login-Logout - Forgot password with given wrong email address", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1809(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
			
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC18.09";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_test_lacie_reset_password.action_EnterNegativeInfo(sID, sTCName, sBrowserURL, sUserName);				
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC18.10[P] LaCie - Login-Logout - Login with account locked and unverified", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1810(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC18.10";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_lacie_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_lacie_test_login.verify_EnterLoginLaCieAccLocked(sID, sTCName);
				util_constant.cMod_lacie_test_login.action_RetrieveLaCieAccountVerificationLink_Gmail(sID, sTCName, sBrowserURL, sEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02_userprofile.verify_WS02EmailFilteringUserProfileSuccess(sID, sTCName);				
				util_constant.cMod_ws02_userprofile.action_WS02AccVerifiedChecked(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);						
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	@Test (description="TC18.11[P] LaCie - Login-Logout - Login with account locked and verified", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1811(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC18.11";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_lacie_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_lacie_test_login.verify_EnterLoginAccLockedVer(sID, sTCName, sUserName);
				util_constant.cMod_lacie_test_login.action_RetrieveLaCieAccountPasswordResetLink_NoGmail(sID, sTCName, sBrowserURL, sEmail);				
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);						
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC18.12[N] LaCie - Login-Logout - Login with user who does not exist in WSO2 Identity Server", dataProvider="getCiamLoginDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1812(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sEmail, String sResetPass, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle)
	
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC18.12";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_lacie_test_login.action_EnterLoginFailed(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	@Test (description="TC19.01[P] Seagate - Consumer Profile - User - Existing user from US country edits their account from opt-in to opt-out", dataProvider="getCiamUserProfileDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1901(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd,
	String sCountry, String sLanguage, String sEmail, String sFirstName, String sLastName, String sZip, String sPhone, 
	String sTitle, String sIncome, String sIndustry, String sGender, String sAge, 
	String sOldPass, String sNewPass, String sConfirmPass, String sQuestion, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle,
	String sOldPassBack, String sNewPassBack, String sConfirmPassBack, String sWCountry, String sWLanguage, String sProIncome, String sProAge)
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC19.01";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_seagate_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_seagate_test_login.verify_PageLoginSuccess(sID, sTCName);				
				util_constant.cMod_ciam_seagate_userprofile.action_MainProfile(sID, sTCName);
				util_constant.cMod_ciam_seagate_userprofile.action_UpdateContactInfoOptOut(sID, sTCName, sCountry, sLanguage, sEmail, sFirstName, sLastName, sZip, sPhone, sWCountry, sWLanguage);				
				util_constant.cMod_admin.action_RetrieveAccountProfileChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02_userprofile.verify_WS02EmailFilteringUserProfileSuccess(sID, sTCName);
				util_constant.cMod_ws02_userprofile.verify_WS02UserProfileSuccess(sID, sTCName, sFirstName, sLastName, sZip, sPhone, sEmail, sWCountry, sWLanguage);				
				util_constant.cMod_ciam_seagate_userprofile.verifyProductData_EditPersonal_DB(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);								
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();			
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
		
	@Test (description="TC19.02[P] Seagate - Consumer Profile - User - Existing user from non-US country edits their account from opt-out to opt-in", dataProvider="getCiamUserProfileDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1902(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd,
	String sCountry, String sLanguage, String sEmail, String sFirstName, String sLastName, String sZip, String sPhone, 
	String sTitle, String sIncome, String sIndustry, String sGender, String sAge, 
	String sOldPass, String sNewPass, String sConfirmPass, String sQuestion, String sAnswer,
    String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle,
    String sOldPassBack, String sNewPassBack, String sConfirmPassBack, String sWCountry, String sWLanguage, String sProIncome, String sProAge)
			
	
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC19.02";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_seagate_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_seagate_test_login.verify_PageLoginSuccess(sID, sTCName);				
				util_constant.cMod_ciam_seagate_userprofile.action_MainProfile(sID, sTCName);
				util_constant.cMod_ciam_seagate_userprofile.action_UpdateContactInfoOptIn(sID, sTCName, sCountry, sLanguage, sEmail, sFirstName, sLastName, sZip, sPhone, sWCountry, sWLanguage);			
				util_constant.cMod_admin.action_RetrieveAccountProfileChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02_userprofile.verify_WS02EmailFilteringUserProfileSuccess(sID, sTCName);
				util_constant.cMod_ws02_userprofile.verify_WS02UserProfileSuccess(sID, sTCName, sFirstName, sLastName, sZip, sPhone, sEmail, sWCountry, sWLanguage);
				util_constant.cMod_ciam_seagate_userprofile.verifyProductData_EditPersonal_DB(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC19.03[P] Seagate - Consumer Profile - User - Existing user edits profile infomation", dataProvider="getCiamUserProfileDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1903(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd,
	String sCountry, String sLanguage, String sEmail, String sFirstName, String sLastName, String sZip, String sPhone, 
	String sTitle, String sIncome, String sIndustry, String sGender, String sAge, 
	String sOldPass, String sNewPass, String sConfirmPass, String sQuestion, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle,
	String sOldPassBack, String sNewPassBack, String sConfirmPassBack, String sWCountry, String sWLanguage, String sProIncome, String sProAge)
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC19.03";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_seagate_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_seagate_test_login.verify_PageLoginSuccess(sID, sTCName);				
				util_constant.cMod_ciam_seagate_userprofile.action_MainProfile(sID, sTCName);
				util_constant.cMod_ciam_seagate_userprofile.action_UpdateProfileInfo(sID, sTCName, sTitle, sIncome, sIndustry, sGender, sAge, sProIncome, sProAge);				
				util_constant.cMod_admin.action_RetrieveAccountProfileChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02_userprofile.verify_WS02EmailFilteringUserProfileSuccess(sID, sTCName);				
				util_constant.cMod_ciam_seagate_userprofile.verifyProductData_EditPersonal_DB(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC19.04[P] Seagate - Consumer Profile - User - Existing user edit password", dataProvider="getCiamUserProfileDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1904(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd,
	String sCountry, String sLanguage, String sEmail, String sFirstName, String sLastName, String sZip, String sPhone, 
	String sTitle, String sIncome, String sIndustry, String sGender, String sAge, 
	String sOldPass, String sNewPass, String sConfirmPass, String sQuestion, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle,
	String sOldPassBack, String sNewPassBack, String sConfirmPassBack, String sWCountry, String sWLanguage, String sProIncome, String sProAge)
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC19.04";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_seagate_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_seagate_test_login.verify_PageLoginSuccess(sID, sTCName);				
				util_constant.cMod_ciam_seagate_userprofile.action_MainProfile(sID, sTCName);
				util_constant.cMod_ciam_seagate_userprofile.action_UpdatePassword(sID, sTCName, sOldPass, sNewPass, sConfirmPass);
				util_constant.cMod_admin.action_RetrieveAccountProfileChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_seagate_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sConfirmPass);
				util_constant.cMod_seagate_test_login.verify_PageLoginSuccess(sID, sTCName);
				util_constant.cMod_ciam_seagate_userprofile.action_ChangeBackPassword(sID, sTCName, sOldPassBack, sNewPassBack, sConfirmPassBack);				
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);						
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC19.05[N] Seagate - Consumer Profile - User - Existing user edit invalid password with validation checking", dataProvider="getCiamUserProfileDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1905(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd,
	String sCountry, String sLanguage, String sEmail, String sFirstName, String sLastName, String sZip, String sPhone, 
	String sTitle, String sIncome, String sIndustry, String sGender, String sAge, 
	String sOldPass, String sNewPass, String sConfirmPass, String sQuestion, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle,
	String sOldPassBack, String sNewPassBack, String sConfirmPassBack, String sWCountry, String sWLanguage, String sProIncome, String sProAge)
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC19.05";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_seagate_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_seagate_test_login.verify_PageLoginSuccess(sID, sTCName);				
				util_constant.cMod_ciam_seagate_userprofile.action_MainProfile(sID, sTCName);
				util_constant.cMod_ciam_seagate_userprofile.action_UpdateInvalidPassword(sID, sTCName, sOldPass, sNewPass, sConfirmPass);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	

	@Test (description="TC19.06 [P] Seagate - Consumer Profile - User - Existing user edit secret questions-answer", dataProvider="getCiamUserProfileDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC1906(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd,
	String sCountry, String sLanguage, String sEmail, String sFirstName, String sLastName, String sZip, String sPhone, 
	String sTitle, String sIncome, String sIndustry, String sGender, String sAge, 
	String sOldPass, String sNewPass, String sConfirmPass, String sQuestion, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle,
	String sOldPassBack, String sNewPassBack, String sConfirmPassBack, String sWCountry, String sWLanguage, String sProIncome, String sProAge)
	
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC19.06";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_seagate_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_seagate_test_login.verify_PageLoginSuccess(sID, sTCName);				
				util_constant.cMod_ciam_seagate_userprofile.action_MainProfile(sID, sTCName);
				util_constant.cMod_ciam_seagate_userprofile.action_UpdateSecretAnswer(sID, sTCName, sQuestion, sAnswer);
				util_constant.cMod_admin.action_RetrieveAccountProfileChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_test_seagate_reset_password.action_EnterLoginInfo(sID, sTCName, sPageTitle, sUserName);
				util_constant.cMod_test_seagate_reset_password.verify_PageResetSecretTrue(sID, sTCName, sUserName, sConfirmPass, sAnswer);
				util_constant.cMod_ciam_seagate_userprofile.action_ChangeBackPassword(sID, sTCName, sOldPassBack, sNewPassBack, sConfirmPassBack);				
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC20.01[P] LaCie - Consumer Profile - User - Existing user from US country edits their account from opt-in to opt-out", dataProvider="getCiamUserProfileDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC2001(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd,
	String sCountry, String sLanguage, String sEmail, String sFirstName, String sLastName, String sZip, String sPhone, 
	String sTitle, String sIncome, String sIndustry, String sGender, String sAge, 
	String sOldPass, String sNewPass, String sConfirmPass, String sQuestion, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle,
	String sOldPassBack, String sNewPassBack, String sConfirmPassBack, String sWCountry, String sWLanguage, String sProIncome, String sProAge)

	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC20.01";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_lacie_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_lacie_test_login.verify_PageLoginLaCieSuccess(sID, sTCName);				
				util_constant.cMod_ciam_lacie_userprofile.action_MainProfile(sID, sTCName);
				util_constant.cMod_ciam_lacie_userprofile.action_UpdateContactInfoOptOut(sID, sTCName, sCountry, sLanguage, sEmail, sFirstName, sLastName, sZip, sPhone, sWCountry, sWLanguage);			
				util_constant.cMod_admin.action_RetrieveAccountProfileChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02_userprofile.verify_WS02EmailFilteringUserProfileSuccess(sID, sTCName);
				util_constant.cMod_ws02_userprofile.verify_WS02LaCieUserProfileSuccess(sID, sTCName, sFirstName, sLastName, sZip, sPhone, sEmail, sWCountry, sWLanguage);				
				util_constant.cMod_ciam_seagate_userprofile.verifyProductData_EditPersonal_DB(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);							
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
		
	@Test (description="TC20.02[P] LaCie - Consumer Profile - User - Existing user from non-US country edits their account from opt-out to opt-in", dataProvider="getCiamUserProfileDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC2002(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd,
	String sCountry, String sLanguage, String sEmail, String sFirstName, String sLastName, String sZip, String sPhone, 
	String sTitle, String sIncome, String sIndustry, String sGender, String sAge, 
	String sOldPass, String sNewPass, String sConfirmPass, String sQuestion, String sAnswer,
    String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle,
    String sOldPassBack, String sNewPassBack, String sConfirmPassBack, String sWCountry, String sWLanguage, String sProIncome, String sProAge)

			
	
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC20.02";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_lacie_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_lacie_test_login.verify_PageLoginLaCieSuccess(sID, sTCName);				
				util_constant.cMod_ciam_lacie_userprofile.action_MainProfile(sID, sTCName);
				util_constant.cMod_ciam_lacie_userprofile.action_UpdateContactInfoOptIn(sID, sTCName, sCountry, sLanguage, sEmail, sFirstName, sLastName, sZip, sPhone, sWCountry, sWLanguage);				
				util_constant.cMod_admin.action_RetrieveAccountProfileChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02_userprofile.verify_WS02EmailFilteringUserProfileSuccess(sID, sTCName);
				util_constant.cMod_ws02_userprofile.verify_WS02LaCieUserProfileSuccess(sID, sTCName, sFirstName, sLastName, sZip, sPhone, sEmail, sWCountry, sWLanguage);
				util_constant.cMod_ciam_seagate_userprofile.verifyProductData_EditPersonal_DB(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC20.03[P] LaCie Consumer Profile - User - Existing user edits profile infomation", dataProvider="getCiamUserProfileDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC2003(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd,
	String sCountry, String sLanguage, String sEmail, String sFirstName, String sLastName, String sZip, String sPhone, 
	String sTitle, String sIncome, String sIndustry, String sGender, String sAge, 
	String sOldPass, String sNewPass, String sConfirmPass, String sQuestion, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle,
	String sOldPassBack, String sNewPassBack, String sConfirmPassBack, String sWCountry, String sWLanguage, String sProIncome, String sProAge)


	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC20.03";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_lacie_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_lacie_test_login.verify_PageLoginLaCieSuccess(sID, sTCName);				
				util_constant.cMod_ciam_lacie_userprofile.action_MainProfile(sID, sTCName);
				util_constant.cMod_ciam_lacie_userprofile.action_UpdateProfileInfo(sID, sTCName, sTitle, sIncome, sIndustry, sGender, sAge, sProIncome, sProAge);				
				util_constant.cMod_admin.action_RetrieveAccountProfileChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02_userprofile.verify_WS02EmailFilteringUserProfileSuccess(sID, sTCName);				
				util_constant.cMod_ciam_seagate_userprofile.verifyProductData_EditPersonal_DB(sID, sTCName, sCountry, sLanguage, util_constant.cCustEmail, sFirstName, sLastName, sZip);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC20.04[P] LaCie - Consumer Profile - User - Existing user edit password", dataProvider="getCiamUserProfileDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC2004(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd,
	String sCountry, String sLanguage, String sEmail, String sFirstName, String sLastName, String sZip, String sPhone, 
	String sTitle, String sIncome, String sIndustry, String sGender, String sAge, 
	String sOldPass, String sNewPass, String sConfirmPass, String sQuestion, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle,
	String sOldPassBack, String sNewPassBack, String sConfirmPassBack, String sWCountry, String sWLanguage, String sProIncome, String sProAge)


	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC20.04";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_lacie_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_lacie_test_login.verify_PageLoginLaCieSuccess(sID, sTCName);				
				util_constant.cMod_ciam_lacie_userprofile.action_MainProfile(sID, sTCName);
				util_constant.cMod_ciam_lacie_userprofile.action_UpdatePassword(sID, sTCName, sOldPass, sNewPass, sConfirmPass);
				util_constant.cMod_admin.action_RetrieveAccountProfileChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_lacie_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sConfirmPass);
				util_constant.cMod_lacie_test_login.verify_PageLoginLaCieSuccess(sID, sTCName);
				util_constant.cMod_ciam_lacie_userprofile.action_ChangeBackPassword(sID, sTCName, sOldPassBack, sNewPassBack, sConfirmPassBack);				
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);						
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	@Test (description="TC20.05[N] LaCie - Consumer Profile - User - Existing user edit invalid password with validation checking", dataProvider="getCiamUserProfileDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC2005(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd,
	String sCountry, String sLanguage, String sEmail, String sFirstName, String sLastName, String sZip, String sPhone, 
	String sTitle, String sIncome, String sIndustry, String sGender, String sAge, 
	String sOldPass, String sNewPass, String sConfirmPass, String sQuestion, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle,
	String sOldPassBack, String sNewPassBack, String sConfirmPassBack, String sWCountry, String sWLanguage, String sProIncome, String sProAge)


	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC20.05";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_lacie_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_lacie_test_login.verify_PageLoginLaCieSuccess(sID, sTCName);				
				util_constant.cMod_ciam_lacie_userprofile.action_MainProfile(sID, sTCName);
				util_constant.cMod_ciam_lacie_userprofile.action_UpdateInvalidPassword(sID, sTCName, sOldPass, sNewPass, sConfirmPass);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);						
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	

	@Test (description="TC20.06[P] LaCie - Consumer Profile - User - Existing user edit secret questions-answer", dataProvider="getCiamUserProfileDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC2006(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd,
	String sCountry, String sLanguage, String sEmail, String sFirstName, String sLastName, String sZip, String sPhone, 
	String sTitle, String sIncome, String sIndustry, String sGender, String sAge, 
	String sOldPass, String sNewPass, String sConfirmPass, String sQuestion, String sAnswer,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle,
	String sOldPassBack, String sNewPassBack, String sConfirmPassBack, String sWCountry, String sWLanguage, String sProIncome, String sProAge)


	
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC20.06";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_lacie_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_lacie_test_login.verify_PageLoginLaCieSuccess(sID, sTCName);				
				util_constant.cMod_ciam_lacie_userprofile.action_MainProfile(sID, sTCName);
				util_constant.cMod_ciam_lacie_userprofile.action_UpdateSecretAnswer(sID, sTCName, sQuestion, sAnswer);
				util_constant.cMod_admin.action_RetrieveAccountProfileChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_test_lacie_reset_password.action_EnterLoginInfo(sID, sTCName, sPageTitle, sUserName);
				util_constant.cMod_test_lacie_reset_password.verify_PageLaCieResetSecretTrue(sID, sTCName, sUserName, sConfirmPass, sAnswer);
				util_constant.cMod_ciam_lacie_userprofile.action_ChangeBackPassword(sID, sTCName, sOldPassBack, sNewPassBack, sConfirmPassBack);
				util_constant.cMod_test_lacie_reset_password.action_RetrieveLaCieAccountPasswordChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);				
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	

	
	
	
	@Test (description="TC21.01[P] Consumer Profile - Admin - Admin edits user profile", dataProvider="getCiamAdminDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC2101(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sAdminURL, String sAdminPageTitle, String sAdminUserName, String sAdminPswd, String sAdminTitle, String sEmail,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sResetPass,
	String sCountry, String sLanguage, String sZip, String sPhone, String sTitle, String sIncome, String sIndustry, String sGender, String sAge,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle, String sFirstName, String sLastName, String sWCountry, String sWLanguage)
	
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC21.01";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sAdminURL);
				util_constant.cMod_email.action_Delete_Gmail(sID, sTCName,sBrowserURL);
				util_constant.cMod_admin.action_EnterAdminLoginInfo(sID, sTCName, sAdminPageTitle, sAdminUserName, sAdminPswd);
				util_constant.cMod_admin.verify_AdminLoginSuccess(sID, sTCName, sAdminTitle);
				util_constant.cMod_admin.action_SearchUserRecord(sID, sTCName, sEmail, sAdminTitle);				
				util_constant.cMod_admin.action_UpdateUserRecord(sID, sTCName, sCountry, sLanguage, sZip, sPhone, sTitle, sIncome, sIndustry, sGender, sAge);
				util_constant.cMod_seagate_test_login.action_RetrieveAccountVerificationLink_Gmail(sID, sTCName, sBrowserURL, sEmail);
				util_constant.cMod_test_seagate_reset_password.action_RetrieveAccountPasswordResetLink_Gmail(sID, sTCName, sBrowserURL, sEmail);				
				util_constant.cMod_test_seagate_reset_password.verify_PasswordReset(sID, sTCName, sUserName, sResetPass);						
				util_constant.cMod_admin.action_RetrieveAccountProfileChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02_userprofile.verify_WS02EmailFilteringUserProfileSuccess(sID, sTCName);	
				util_constant.cMod_ws02_userprofile.verify_WS02UserProfileSuccess(sID, sTCName, sZip, sPhone, sEmail, sFirstName, sLastName, sWCountry, sWLanguage);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);				
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
		
	
	@Test (description="TC21.02[N] Consumer Profile - Admin - Search invalid email address", dataProvider="getCiamAdminDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC2102(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sAdminURL, String sAdminPageTitle, String sAdminUserName, String sAdminPswd, String sAdminTitle, String sEmail,
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sResetPass,
	String sCountry, String sLanguage, String sZip, String sPhone, String sTitle, String sIncome, String sIndustry, String sGender, String sAge,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle, String sFirstName, String sLastName, String sWCountry, String sWLanguage)
	
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC21.02";
		
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sAdminURL);
				util_constant.cMod_admin.action_EnterAdminLoginInfo(sID, sTCName, sAdminPageTitle, sAdminUserName, sAdminPswd);
				util_constant.cMod_admin.verify_AdminLoginSuccess(sID, sTCName, sAdminTitle);
				util_constant.cMod_admin.action_SearchInvalidUserRecord(sID, sTCName, sEmail);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	
	
	@Test (description="TC21.03[P] Consumer Profile - Admin - Locked user is unable to access", dataProvider="getCiamAdminDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC2103(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sAdminURL, String sAdminPageTitle, String sAdminUserName, String sAdminPswd, String sAdminTitle, String sEmail, 
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sResetPass,
	String sCountry, String sLanguage, String sZip, String sPhone, String sTitle, String sIncome, String sIndustry, String sGender, String sAge,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle, String sFirstName, String sLastName, String sWCountry, String sWLanguage)
	
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC21.03";
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sAdminURL);				
				util_constant.cMod_admin.action_EnterAdminLoginInfo(sID, sTCName, sAdminPageTitle, sAdminUserName, sAdminPswd);
				util_constant.cMod_admin.verify_AdminLoginSuccess(sID, sTCName, sAdminTitle);
				util_constant.cMod_admin.action_SearchUserRecord(sID, sTCName, sEmail, sAdminTitle);
				util_constant.cMod_admin.action_UpdateLockedUserRecord(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_seagate_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_seagate_test_login.verify_EnterLoginAccLockedVer(sID, sTCName, sUserName);
				util_constant.cMod_seagate_test_login.action_RetrieveAccountPasswordResetLink_NoGmail(sID, sTCName, sBrowserURL, sEmail);	
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02_userprofile.verify_WS02EmailFilteringUserProfileSuccess(sID, sTCName);
				util_constant.cMod_ws02_userprofile.action_WS02AccLockedChecked(sID, sTCName);		
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);						
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";				
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}	
	
	@Test (description="TC21.04)[P] Consumer Profile - Admin - Locked user is able to access after unlocked by Admin", dataProvider="getCiamAdminDP", dataProviderClass = util_dataprovidermgr.class)
	public void test_TC2104(
	String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, 
	String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName,
	String sAdminURL, String sAdminPageTitle, String sAdminUserName, String sAdminPswd, String sAdminTitle, String sEmail,  
	String sBrowserURL, String sPageTitle, String sUserName, String sPswd, String sResetPass,
	String sCountry, String sLanguage, String sZip, String sPhone, String sTitle, String sIncome, String sIndustry, String sGender, String sAge,
	String sWS02BrowserURL, String sWS02PageTitle, String sWS02UserName, String sWS02Pswd, String sWS02MainTitle, String sFirstName, String sLastName, String sWCountry, String sWLanguage)
	
	
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cTc_Name = sTCName;
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cAct_Result = "Passed";
		util_constant.cCustEmail  = sEmail;

		//Added by Edward 07Mar2017 for SNOW Integration
		util_constant.cTestCaseId = "TC21.04";
		
		
		try
		{
			//Added by Edward 07Mar2017 for SNOW Integration
			util_constant.utilSNOWIntegration.getTestCasesIniFile(util_constant.cLocalTestCasesINI, util_constant.cTestCaseId);
			util_constant.utilSNOWIntegration.testCaseStartUpdate(util_constant.cSnowTestCaseNumber, "In progress", util_constant.cTestCaseId);
			
			//Commented by Edward 07Mar2017 as feature implemented in SNOW Integration
//			util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
			if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0)
			{
				util_constant.cMod_alm.action_ConnectALM(sID, sTCName, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, sALMTCID, sALMTCName, sALMTRName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sAdminURL);
				util_constant.cMod_admin.action_EnterAdminLoginInfo(sID, sTCName, sAdminPageTitle, sAdminUserName, sAdminPswd);
				util_constant.cMod_admin.verify_AdminLoginSuccess(sID, sTCName, sAdminTitle);
				util_constant.cMod_admin.action_SearchUserRecord(sID, sTCName, sEmail, sAdminTitle);
				util_constant.cMod_admin.action_UpdateLockedUserRecord(sID, sTCName);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sBrowserURL);
				util_constant.cMod_seagate_test_login.action_EnterLoginInfo(sID, sTCName, sUserName, sPswd);
				util_constant.cMod_seagate_test_login.verify_PageLoginSuccess(sID, sTCName);
				util_constant.cMod_admin.action_RetrieveAccountProfileChangedMessage_Gmail(sID, sTCName, sBrowserURL);
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);
				util_constant.cMod_browser.action_LaunchBrowser(sID, sTCName, sWS02BrowserURL);
				util_constant.cMod_ws02.action_EnterWS02LoginInfo(sID, sTCName, sWS02PageTitle, sWS02UserName, sWS02Pswd);
				util_constant.cMod_ws02.verify_WS02LoginSuccess(sID, sTCName, sWS02MainTitle);
				util_constant.cMod_ws02.action_FilterUserEmail(sID, sTCName);
				util_constant.cMod_ws02_userprofile.verify_WS02EmailFilteringUserProfileSuccess(sID, sTCName);
				util_constant.cMod_ws02_userprofile.action_WS02AccLockedChecked(sID, sTCName);				
				util_constant.cMod_browser.action_CloseBrowser(sID, sTCName);					
			}
			else
			{
				util_constant.cAct_Result = "Failed - Skip Test";
				reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
				Assert.fail(util_constant.cAct_Result);
			}
		}
		catch (Exception e)
		{
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
			Assert.fail(util_constant.cAct_Result);
		}
		// util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
	}

	public static void main(String[] args) {
		util_constant.cTc_TestCaseFailed = "N";
	}	
	
	

}
