package modules;

import org.testng.Assert;

import utilities.util_common;
import utilities.util_constant;
import utilities.util_seleniumdrivermgr;

public class mod_browser extends util_common 
{
  
  public boolean action_LaunchBrowser(String sID, String sTCName, String sBrowserURL) 
  {
	  String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
	  util_constant.cExp_Result = "Passed - " + sMethod;
	  util_constant.cTc_Name = sTCName;

	  try
	  {
		  if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0 )
		  //if (sID.equalsIgnoreCase(util_constant.cTc_Id))
		  {
			  util_constant.cSel_Driver = util_seleniumdrivermgr.getDriver(sBrowserURL);
			  
			  util_constant.cAct_Result = "Passed - " + sMethod;
			  reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			  return true;
		  }
		  else
		  {
			  util_constant.cAct_Result = "Failed - Skip Test";
			  reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			  return false;
		  }
	  }
	  catch (Exception e)
	  {
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
			return false;			
	  }
  }
  
  

	public boolean action_CloseBrowser(String sID, String sTCName)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		util_constant.cExp_Result = "Passed - " + sMethod;
		util_constant.cTc_Name = sTCName;
		
		try
		{
			 if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0 )
			 //if (sID.equalsIgnoreCase(util_constant.cTc_Id))
			 {
				 util_constant.cTc_Name = sTCName;
				 util_constant.cAct_Result = "Passed - " + sMethod;
				 reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");

				 util_seleniumdrivermgr.quitDriver();
				 util_constant.cSel_Driver = null;

				 return true;
			 }
			 else
			 {
				 util_constant.cAct_Result = "Failed - Skip Test";
				 reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				 return false;
			 }
		}
		catch (Exception e)
		{
			util_constant.iExeStatus = 1;
			util_constant.cAct_Result = e.getMessage();
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			Assert.fail(util_constant.cAct_Result);
			return false;			
		}
	}
  
}
