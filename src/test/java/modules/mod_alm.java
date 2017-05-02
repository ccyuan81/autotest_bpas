package modules;

import java.io.FileInputStream;
import java.util.Properties;

import org.testng.Assert;

import utilities.util_common;
import utilities.util_constant;

public class mod_alm extends util_common
{

  public boolean action_ConnectALM(String sID, String sTCName, String sALMURL, String sALMLogin, String sALMPswd, String sALMDomain, String sALMProject, String sALMTSPath, String sALMTSName, String sALMTCID, String sALMTCName, String sALMTRName) 
  {
	  String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
	  util_constant.cTc_Name = sTCName;

	  try
	  {
		  if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0 )
		  {
			  	/*Properties p = new Properties();
				p.load(new FileInputStream("src//main//resources//properties.ini"));

				sALMURL = p.getProperty("stestevidencepath");
				sALMLogin = p.getProperty("stestevidencepath");
				sALMPswd = p.getProperty("stestevidencepath");
				sALMDomain = p.getProperty("stestevidencepath");
				sALMProject = p.getProperty("stestevidencepath");
				sALMTSPath = p.getProperty("stestevidencepath");
				sALMTSName = p.getProperty("stestevidencepath");
				sALMTCID = p.getProperty("stestevidencepath");
				sALMTCName = p.getProperty("stestevidencepath");
				sALMTRName = p.getProperty("stestevidencepath");*/
			  util_constant.cAct_Result = "Passed - " + sMethod;
			  util_constant.cExp_Result = "Passed - " + sMethod;
			  
			  util_constant.cTS_No = Integer.parseInt(sALMTCID);
			  util_constant.cUtil_Alm.establishALMConnection(util_constant.cJacob_Path, sALMURL, sALMLogin, sALMPswd, sALMDomain, sALMProject, sALMTSPath, sALMTSName, util_constant.cTS_No, sALMTCName, sALMTRName);
			  
			  reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
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
  
  public boolean action_DisconnectALM(String sID, String sTCName) 
  {
	  String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
	 
	  System.out.println("SID: " + sID);
	  util_constant.cTc_Name = sTCName;
	  
	  try
	  {
		  if (sID.equalsIgnoreCase(util_constant.cTc_Id))
		  {
			  util_constant.cUtil_Alm.closeALMConnection();

			  //util_constant.cAct_Result = "Passed - " + sMethod;
			  //util_constant.cExp_Result = "Passed - " + sMethod;
			  
			  //reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
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

