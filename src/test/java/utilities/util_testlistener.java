package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class util_testlistener implements ITestListener {

	public static final Logger LOG = LogManager.getLogger(util_testlistener.class);
	
	public void onFinish(ITestContext arg0) {
	} 
 
	public void onStart(ITestContext arg0) {
		util_constant.iExeStatus = 0;
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult arg0) {
	}

	public void onTestFailure(ITestResult arg0) {
		/*if (!(util_constant.cTc_Name == null))
		{
			util_constant.cUtil_Common.reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, getTestMethodName(arg0), "Failed", util_constant.cExp_Result, getExceptionDesc(arg0), getTestMethodName(arg0), "jpg");
		}
		else
		{
			util_constant.cMod_alm.action_DisconnectALM(util_constant.cTc_Id, util_constant.cTc_Name);
			LOG.info("Executed Method: " + getTestMethodName(arg0) + " - [Failed]" + " - " + "Error: " + getExceptionDesc(arg0));
		}*/
		util_constant.iExeStatus = 1;
	}

	public void onTestSkipped(ITestResult arg0) {
	}

	public void onTestStart(ITestResult arg0) {
	}

	public void onTestSuccess(ITestResult arg0) {
		/*if (!(util_constant.cTc_Name == null))
		{
			util_constant.cUtil_Common.reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, getTestMethodName(arg0), "Passed", util_constant.cExp_Result, util_constant.cAct_Result, getTestMethodName(arg0), "jpg");
		}
		else
		{
			LOG.info("Executed Method: " + getTestMethodName(arg0) + " - [Passed]");
		}*/
	}
	
	private static String getTestMethodName(ITestResult result) 
	{
		return result.getMethod().getConstructorOrMethod().getName();
	}

	private static String getExceptionDesc(ITestResult result)
	{
		Throwable cause = result.getThrowable();
		return cause.toString();
	}


}


