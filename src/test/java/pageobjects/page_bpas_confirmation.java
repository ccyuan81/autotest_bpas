package pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import utilities.*;

public class page_bpas_confirmation extends util_common {
	
	//supplier confirmation main page
	private final By lbl_confirmationTitle = By.xpath("html/body/div[1]/div/span[1]");
	private final By lbl_requestID = By.xpath("html/body/div[1]/div/span[2]");
	private final By lbl_invID = By.xpath("html/body/div[1]/div/span[3]");
	private final By lbl_confirmationMsg1 = By.xpath("html/body/div[1]/span[1]");
	private final By lbl_confirmationMsg2 = By.xpath("html/body/div[1]/span[2]");
	private final By lin_inviteMoreSuppliers = By.xpath("html/body/div[1]/a[1]");
	private final By lin_mySeagate = By.xpath("html/body/div[1]/a[2]");
	
	public page_bpas_confirmation()
	{
		PageFactory.initElements(util_constant.cSel_Driver, this);
	}
	
	
	public void clickInviteMoreSuppliers()
	{
		waitForElementToBeVisible(lin_inviteMoreSuppliers);
		click(lin_inviteMoreSuppliers);
	}
	
	public void clickMySeagate()
	{
		waitForElementToBeVisible(lin_mySeagate);
		click(lin_mySeagate);
	}
	
		
	public boolean verifyConfirmationTitle(String sPageTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
				
		try
		{
			waitForElementToBeVisible(lbl_confirmationTitle);
			
			util_constant.cAct_Result = getElementText(lbl_confirmationTitle);
			util_constant.cExp_Result = sPageTitle;
			
			if (util_constant.cAct_Result.equalsIgnoreCase(util_constant.cExp_Result))
			{
				reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				return true;
			}
			else
			{
				reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				return false;
			}
		}
		catch(Exception e)
		{
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, e.getMessage(), sMethod, "jpg");
			return false;
		}
	}	
	
	
	public boolean verifyConfirmationMessage1()
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();

		try
		{
			waitForElementToBeVisible(lbl_confirmationMsg1);
			util_constant.cAct_Result = getElementText(lbl_confirmationMsg1);
			util_constant.cExp_Result = "Thank you for submitting the invitation request.";

			if (util_constant.cAct_Result.equalsIgnoreCase(util_constant.cExp_Result))
			{
				reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				return true;
			}
			else
			{
				reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				return false;
			}
		}
		catch(Exception e)
		{
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, e.getMessage(), sMethod, "jpg");
			return false;
		}
	}
	
	
	public boolean verifyConfirmationMessage2()
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();

		try
		{
			waitForElementToBeVisible(lbl_confirmationMsg2);
			util_constant.cAct_Result = getElementText(lbl_confirmationMsg2);
			util_constant.cExp_Result = "An email has been sent to suppliers to register for the application access.";

			if (util_constant.cAct_Result.equalsIgnoreCase(util_constant.cExp_Result))
			{
				reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				return true;
			}
			else
			{
				reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				return false;
			}
		}
		catch(Exception e)
		{
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, e.getMessage(), sMethod, "jpg");
			return false;
		}
	}
	
	
	public boolean verifyRequestID(String sRequestID)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
		try
		{
			waitForElementToBeVisible(lbl_requestID);
						
			util_constant.cAct_Result = getElementText(lbl_requestID);
			util_constant.cExp_Result = sRequestID;
			
			if (util_constant.cAct_Result.equalsIgnoreCase(util_constant.cExp_Result))
			{
				reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				return true;
			}
			else
			{
				reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				return false;
			}
		}
		catch(Exception e)
		{
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, e.getMessage(), sMethod, "jpg");
			return false;
		}
	}	
	
	
	public boolean verifyInvID(String sInvID)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
		try
		{
			waitForElementToBeVisible(lbl_invID);
						
			util_constant.cAct_Result = getElementText(lbl_invID);
			util_constant.cExp_Result = sInvID;
			
			if (util_constant.cAct_Result.equalsIgnoreCase(util_constant.cExp_Result))
			{
				reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				return true;
			}
			else
			{
				reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				return false;
			}
		}
		catch(Exception e)
		{
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, e.getMessage(), sMethod, "jpg");
			return false;
		}
	}	
	
	
		
	public boolean verifyInvitationFormURL()
	{

		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
		util_constant.cExp_Result  = "https://seagate--test--c.cs61.visual.force.com/apex/SOXBPASSUP_InvitationForm";
		
		try
		{
			util_constant.cAct_Result = getCurrentURL();

			if (util_constant.cAct_Result.equalsIgnoreCase(util_constant.cExp_Result))
			{
				reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				return true;
			}
			else
			{
				reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				return false;
			}
		}
		catch(Exception e)
		{
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, e.getMessage(), sMethod, "jpg");
			return false;
		}
	}
	
	public boolean verifyMySeagateURL()
	{

		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
		util_constant.cExp_Result  = "https://my.seagate.com/myseagate";
		
		try
		{
			util_constant.cAct_Result = getCurrentURL();

			if (util_constant.cAct_Result.equalsIgnoreCase(util_constant.cExp_Result))
			{
				reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				return true;
			}
			else
			{
				reportResult(1, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				return false;
			}
		}
		catch(Exception e)
		{
			reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, e.getMessage(), sMethod, "jpg");
			return false;
		}
	}
	
}
