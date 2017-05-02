package modules;

import org.testng.Assert;
import org.testng.annotations.Test;

import utilities.util_common;
import utilities.util_constant;
import utilities.util_dataprovidermgr;
import utilities.util_seleniumdrivermgr;

public class mod_bpas_invitation_supplier_products extends util_common
{

  public boolean action_EnterSupplierProducts(String sID, String sTCName, String sPageTitle, String sProduct, String sComponent, String sSite, String sRole) 
  {
	  String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
	  util_constant.cExp_Result = "Passed - " + sMethod;
	  util_constant.cTc_Name = sTCName;
	  
	  try
	  {
		  if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0 )
		  {
			  util_constant.cPage_bpas_invitation_supplier_products.verifyInvitationPageTitle(sPageTitle);
			  Thread.sleep(5000);
			  util_constant.cPage_bpas_invitation_supplier_products.selectProduct(sProduct); 
			  util_constant.cPage_bpas_invitation_supplier_products.selectComponent(sComponent);
			  util_constant.cPage_bpas_invitation_supplier_products.selectSite(sSite);
			  util_constant.cPage_bpas_invitation_supplier_products.selectRole(sRole);
			  
			  
			  util_constant.cAct_Result = "Passed - " + sMethod;
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
			  util_constant.cAct_Result = e.getMessage();
			  reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			  Assert.fail(util_constant.cAct_Result);
			  return false;			
		}
  }
  
  public boolean verify_InvitationLoginSuccess(String sID, String sTCName, String sPageTitle) 
  {
	  String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
	  util_constant.cExp_Result = "Passed - " + sMethod;
	  util_constant.cTc_Name = sTCName;
	  
	  try
	  {
		  if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0 )
		  {
			  util_constant.cPage_bpas_invitation.clickLogin();
			  Thread.sleep(10000);
			  util_constant.cPage_bpas_invitation.verifyInvitationPageTitle(sPageTitle);
				
			  util_constant.cAct_Result = "Passed - " + sMethod;
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
			  util_constant.cAct_Result = e.getMessage();
			  reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			  Assert.fail(util_constant.cAct_Result);
			  return false;			
		}
  }
  
  public boolean action_SearchUserRecord(String sID, String sTCName, String sEmail, String sAdminTitle) 
  {
	  String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
	  util_constant.cExp_Result = "Passed - " + sMethod;
	  util_constant.cTc_Name = sTCName;
	  
	  try
	  {
		  if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0 )
		  {
		//	  util_constant.cPage_admin_users.setSearchEmail(sEmail);
			//  util_constant.cPage_admin_users.clickSearch();
			  Thread.sleep(10000);				  
		//	  util_constant.cPage_admin_users.verifyValidEmail(sEmail);
			//  util_constant.cPage_admin_users.clickEmail();
			  Thread.sleep(10000);
			//  util_constant.cPage_admin_users.verifyAdminLoginSuccess(sAdminTitle);			  
				
			  util_constant.cAct_Result = "Passed - " + sMethod;
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
			  util_constant.cAct_Result = e.getMessage();
			  reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
			  Assert.fail(util_constant.cAct_Result);
			  return false;			
		}
  }
  
  
   
}

