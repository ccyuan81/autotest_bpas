package modules;

import org.testng.Assert;
import org.testng.annotations.Test;

import utilities.util_common;
import utilities.util_constant;
import utilities.util_dataprovidermgr;
import utilities.util_seleniumdrivermgr;

public class mod_bpas_invitation extends util_common
{

  public boolean action_EnterInvitationForm(String sID, String sTCName, String sPageTitle, String sUserName, String sPswd) 
  {
	  String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
	  util_constant.cExp_Result = "Passed - " + sMethod;
	  util_constant.cTc_Name = sTCName;
	  
	  try
	  {
		  if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0 )
		  {
			  util_constant.cPage_bpas_invitation.verifyInvitationPageTitle(sPageTitle);
			  Thread.sleep(5000);
			  util_constant.cPage_bpas_invitation.setUserName(sUserName); 
			  util_constant.cPage_bpas_invitation.setPassword(sPswd);
			  
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
  
  public boolean action_SearchCompanyRecord(String sID, String sTCName, String sCompanyName, String sCompany, String sInvPageTitle, String sSupplierName, String sSupplierEmail, String sProduct, String sComponent, String sSite, String sRole, String sComments) 
  {
	  String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
	  util_constant.cExp_Result = "Passed - " + sMethod;
	  util_constant.cTc_Name = sTCName;
	  
	  try
	  {
		  if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0 )
		  {
			  util_constant.cPage_bpas_invitation.setCompanyName(sCompanyName); 
			  util_constant.cPage_bpas_invitation.selectCompany(sCompany);
			  Thread.sleep(10000);		
			  util_constant.cPage_bpas_invitation.setSupplierName(sSupplierName); 
			  util_constant.cPage_bpas_invitation.selectCompany(sCompany);
			  util_constant.cPage_bpas_invitation.clickContinue();
			  Thread.sleep(10000);				  
			  util_constant.cPage_bpas_invitation_supplier_products.verifyInvitationPageTitle(sInvPageTitle);
			  util_constant.cPage_bpas_invitation_supplier_products.verifySupplierName(sSupplierName);
			  util_constant.cPage_bpas_invitation_supplier_products.verifySupplierEmail(sSupplierEmail);
			  util_constant.cPage_bpas_invitation_supplier_products.selectProduct(sProduct);
			  util_constant.cPage_bpas_invitation_supplier_products.selectProduct(sComponent);
			  util_constant.cPage_bpas_invitation_supplier_products.selectProduct(sSite);
			  util_constant.cPage_bpas_invitation_supplier_products.selectProduct(sRole);
			  util_constant.cPage_bpas_invitation_supplier_products.setComments(sComments);
			  util_constant.cPage_bpas_invitation_supplier_products.clickInviteSupplier();
			  Thread.sleep(10000);
						  
				
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
  
  public boolean verify_SubmissionSuccess(String sID, String sTCName, String sConfirmTitle, String sRequestID) 
  {
	  String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
	  util_constant.cExp_Result = "Passed - " + sMethod;
	  util_constant.cTc_Name = sTCName;
	  
	  try
	  {
		  if (sID.equalsIgnoreCase(util_constant.cTc_Id) && util_constant.iExeStatus == 0 )
		  {
			  util_constant.cPage_bpas_confirmation.verifyConfirmationTitle(sConfirmTitle);
			  util_constant.cPage_bpas_confirmation.verifyConfirmationMessage1();
			  util_constant.cPage_bpas_confirmation.verifyConfirmationMessage2();
			  util_constant.cPage_bpas_confirmation.verifyRequestID(sRequestID);
			  util_constant.cPage_bpas_confirmation.clickInviteMoreSuppliers();
			  Thread.sleep(10000);
			  util_constant.cPage_bpas_confirmation.verifyInvitationFormURL();
			  util_constant.cPage_bpas_confirmation.clickMySeagate();
			  Thread.sleep(10000);
			  util_constant.cPage_bpas_confirmation.verifyMySeagateURL();
			  
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

