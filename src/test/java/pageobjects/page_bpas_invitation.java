package pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import utilities.*;

public class page_bpas_invitation extends util_common {
	
	//sfdc seagate login page
	private final By txt_userName = By.xpath(".//*[@id='username']");
	private final By txt_password = By.xpath(".//*[@id='password']");
	private final By btn_Login = By.xpath("html/body/div[2]/div[2]/div/div/form/div[3]/div/div/button");
	
	//supplier invitation form main page
	private final By lbl_mainTitle = By.xpath("html/body/div[1]/h3");
	private final By lbl_sponsorEmail = By.xpath("html/body/div[2]/form/div[1]/div/div[1]/span[1]/div[2]/div[2]/span");	
	private final By lbl_step1 = By.xpath("html/body/div[2]/span[2]/ul/li[1]");
	private final By lbl_step2 = By.xpath("html/body/div[2]/span[2]/ul/li[2]");
	private final By lbl_step3 = By.xpath("html/body/div[2]/span[2]/ul/li[3]");
	private final By lin_delete = By.xpath(".//*[@id='j_id0:j_id4:form:pb:nonHazTable:0:j_id114']");
	private final By lin_here = By.xpath("html/body/div[2]/form/div[1]/div/div[1]/div[5]/b/a");
	private final By ddl_company = By.xpath(".//*[@id='j_id0:j_id4:form:pb:title']");
	private final By txt_sponsorName = By.xpath(".//*[@id='j_id0:j_id4:form:pb:corpcodes']");
	private final By txt_companyName = By.xpath("html/body/div[2]/form/div[1]/div/div[1]/div[4]/div[2]/input");
	private final By txt_SupplierName = By.xpath(".//*[@id='j_id0:j_id4:form:pb:nonHazTable:0:j_id121']");
	private final By txt_SupplierEmail = By.xpath("html/body/div[2]/form/div[1]/div/div[1]/span[3]/table/tbody/tr/td[4]/input");
	private final By txt_RowName2 = By.xpath("j_id0:j_id4:form:pb:nonHazTable:1:j_id121']");
	private final By txt_RowEmail2 = By.xpath("html/body/div[2]/form/div[1]/div/div[1]/span[3]/table/tbody/tr[2]/td[4]/input");
	
	
	private final By btn_search = By.xpath("html/body/div[2]/form/div[1]/div/div[1]/div[4]/input");
	private final By btn_addNew = By.xpath("html/body/div[2]/form/div[1]/div/div[1]/span[3]/table/tfoot/tr/td[1]/input");
	private final By btn_reset = By.xpath(".//*[@id='j_id0:j_id4:form:pb:resetButton']");
	private final By btn_continue = By.xpath(".//*[@id='j_id0:j_id4:form:pb:j_id125']");
	
	
	public page_bpas_invitation()
	{
		PageFactory.initElements(util_constant.cSel_Driver, this);
	}

	
	public void setUserName(String sUserName)
	{
		waitForElementToBeVisible(txt_userName);
		sendKeys(txt_userName,sUserName);
	}
	
	public void setPassword(String sPswd)
	{
		waitForElementToBeVisible(txt_password);
		sendKeys(txt_password,sPswd);
	}
	
	public void clickLogin()
	{
		waitForElementToBeVisible(btn_Login);
		click(btn_Login);
	}
	
	
	public void setCompanyName(String sCompanyName)
	{
		waitForElementToBeVisible(txt_companyName);
		sendKeys(txt_companyName,sCompanyName);
	}
	
	public void setSupplierName(String sSupplierName)
	{
		waitForElementToBeVisible(txt_SupplierName);
		sendKeys(txt_SupplierName,sSupplierName);
	}
	
	public void selectCompany(String sCompany)
	{
		waitForElementToBeVisible(ddl_company);
		selectCCBItem(ddl_company, sCompany);
	}
	
	
	public void setSupplierEmail(String sSupplierEmail)
	{
		waitForElementToBeVisible(txt_SupplierEmail);
		sendKeys(txt_SupplierEmail, sSupplierEmail);
	}

	
	public void clickContinue()
	{
		waitForElementToBeVisible(btn_continue);
		click(btn_continue);
	}
	
	public void clickReset()
	{
		waitForElementToBeVisible(btn_reset);
		click(btn_reset);
	}
	
	public void clickAddNew()
	{
		waitForElementToBeVisible(btn_addNew);
		click(btn_addNew);
	}
	
	
	public void clickDelete()
	{
		waitForElementToBeVisible(lin_delete);
		click(lin_delete);
	}
	
	public void clickHere()
	{
		waitForElementToBeVisible(lin_here);
		click(lin_here);
	}
	
	
	
	public boolean verifyInvitationPageTitle(String sPageTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
				
		try
		{
			waitForElementToBeVisible(lbl_mainTitle);
			
			util_constant.cAct_Result = getElementText(lbl_mainTitle);
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
	
	public boolean verifySponsorName(String sSponsorName)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
				
		try
		{
			waitForElementToBeVisible(txt_sponsorName);
			
			util_constant.cAct_Result = getElementText(txt_sponsorName);
			util_constant.cExp_Result = sSponsorName;
			
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
	
	public boolean verifySponsorEmail(String sSponsorEmail)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
				
		try
		{
			waitForElementToBeVisible(lbl_sponsorEmail);
			
			util_constant.cAct_Result = getElementText(lbl_sponsorEmail);
			util_constant.cExp_Result = sSponsorEmail;
			
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
