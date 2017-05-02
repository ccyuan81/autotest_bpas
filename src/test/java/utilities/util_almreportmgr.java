package utilities;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.codec.binary.Base64;

import com.jacob.com.LibraryLoader;

import atu.alm.wrapper.ALMServiceWrapper;
import atu.alm.wrapper.ITestCase;
import atu.alm.wrapper.ITestCaseRun;
import atu.alm.wrapper.ITestSet;
import atu.alm.wrapper.enums.StatusAs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

public class util_almreportmgr extends util_common 
{ 
	private static ALMServiceWrapper wrapper; 
	private static ITestCase loginTest;
	private static ITestCaseRun loginRun; 
	private static final Logger LOG = LogManager.getLogger(util_almreportmgr.class);
	private static ArrayList listTest = new ArrayList( );
	private static String sTRunName;
	private static String sTSetPath;
	private static String sTSetName; 
	private static int iTSetID; 
	private static String sTCaseName;
	private static int iTResult;
	
	public void establishALMConnection(String sJCobPath, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, String sALMProject, String sTestSetPath, String sTestSetName, int iTestSetID, String sTestCaseName, String sTestRunName)
	{
		 String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		 util_constant.cExp_Result = "Passed - " + sMethod;
		  
		try   
		{
			iTResult = 2;
			byte[] decodedBytes = Base64.decodeBase64("UEBzc3cwcmQ1Mjk5");
			String decryptedPassword = new String(decodedBytes);
			String sPath;
			sTRunName = sTestRunName;
		
			System.setProperty("jacob.dll.path", sJCobPath);
			LibraryLoader.loadJacobLibrary();

			wrapper = new ALMServiceWrapper(sALMURL); 
			wrapper.connect(sALMLogin, sALMPswd, sALMDomain, sALMProject);

			loginTest = wrapper.updateResult(sTestSetPath, sTestSetName, iTestSetID, sTestCaseName,StatusAs.N_A);
			loginTest = wrapper.updateResult(sTestSetPath, sTestSetName, iTestSetID, sTestCaseName,StatusAs.NOT_COMPLETED);

			//reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		} catch(Exception el)
		{
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = el.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
		}
	}
	


	public void closeALMConnection()
	{ 
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cExp_Result = "Passed - " + sMethod;
		 
		try
		{
			String sString;
			String[] sItem;
			Iterator it = listTest.iterator( );
			
			if (iTResult == 1)
			{loginRun = wrapper.createNewRun(loginTest, sTRunName, StatusAs.PASSED);}
			else
			{loginRun = wrapper.createNewRun(loginTest, sTRunName, StatusAs.FAILED);}

			while ( it.hasNext( ) ) 
			{
				sString = (String) it.next();
				
				sItem=sString.split("--");
				
				if (sItem[4].trim().equalsIgnoreCase("passed"))
				{wrapper.addStep(loginRun, sItem[0], StatusAs.PASSED, sItem[1], sItem[2], sItem[3]);}
				else
				{wrapper.addStep(loginRun, sItem[0], StatusAs.FAILED, sItem[1], sItem[2], sItem[3]);}
			}
			
			wrapper.close();
			listTest.clear();
			
			//reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		} catch(Exception el)
		{
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = el.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
		}
	}
	
	
	public void createAndUpdateTestStep(String sTStepName, String sTMethodName, String sExpectedResult, String sActualResult, String sResult)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cExp_Result = "Passed - " + sMethod;
		
		try
		{
			if (sResult.trim().equalsIgnoreCase("passed") && (iTResult !=0))
			{iTResult = 1;}
			else
			{iTResult = 0;}

			listTest.add(sTStepName + "--" + sTMethodName + "--" + sExpectedResult + "--" + sActualResult + "--" + sResult);
			
			//reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		} catch(Exception el)
		{
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = el.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);		
		}
	}
}
