package pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import utilities.*;

public class page_bpas_registration extends util_common {
	
	//sfdc seagate login page
	private final By txt_userName = By.xpath(".//*[@id='username']");
	private final By txt_password = By.xpath(".//*[@id='password']");
	private final By btn_Login = By.xpath("html/body/div[2]/div[2]/div/div/form/div[3]/div/div/button");
	
	//supplier registration form main page
	private final By lbl_mainTitle = By.xpath("html/body/div[1]/h3");
	private final By lbl_subTitle = By.xpath("html/body/div[2]/span[4]/div/div/div/h3");	
	private final By lbl_loginInfo = By.xpath("html/body/div[2]/span[4]/div/form/div[17]/h3");
	private final By lbl_email = By.xpath("html/body/div[2]/span[4]/div/form/div[5]/div[2]");
	private final By lbl_sponsor = By.xpath("html/body/div[2]/span[4]/div/form/span[1]/div[3]/div[2]");
	private final By lbl_companyName = By.xpath("html/body/div[2]/span[4]/div/form/div[9]/div[2]");
	private final By ddl_subTitle = By.xpath(".//*[@id='j_id0:j_id2:regForm:title']");
	private final By ddl_jobFunction = By.xpath(".//*[@id='j_id0:j_id2:regForm:jobFunc']");
	private final By ddl_state = By.xpath(".//*[@id='j_id0:j_id2:regForm:workState']");
	private final By ddl_country = By.xpath(".//*[@id='j_id0:j_id2:regForm:wcCode']");
	private final By ddl_secretquestion = By.xpath(".//*[@id='j_id0:j_id2:regForm:secretQuestion']");
	private final By txt_firstName = By.xpath(".//*[@id='j_id0:j_id2:regForm:j_id69']");
	private final By txt_lastName = By.xpath(".//*[@id='j_id0:j_id2:regForm:j_id74']");
	private final By txt_phone = By.xpath(".//*[@id='j_id0:j_id2:regForm:phone']");
	private final By txt_fax = By.xpath(".//*[@id='j_id0:j_id2:regForm:fax']");
	private final By txt_jobOther = By.xpath(".//*[@id='j_id0:j_id2:regForm:j_id111']");
	private final By txt_address1 = By.xpath(".//*[@id='j_id0:j_id2:regForm:j_id127']");
	private final By txt_address2 = By.xpath(".//*[@id='j_id0:j_id2:regForm:j_id131']");
	private final By txt_city = By.xpath(".//*[@id='j_id0:j_id2:regForm:j_id136']");
	private final By txt_zip = By.xpath(".//*[@id='j_id0:j_id2:regForm:postalCode']");
	private final By txt_userID = By.xpath(".//*[@id='j_id0:j_id2:regForm:loginEmail']");
	private final By txt_userPassword = By.xpath(".//*[@id='j_id0:j_id2:regForm:loginPassword']");
	private final By txt_retypePassword = By.xpath(".//*[@id='j_id0:j_id2:regForm:loginRetypePassword']");
	private final By txt_answerSecret = By.xpath(".//*[@id='j_id0:j_id2:regForm:secretAnswer']");
	private final By opt_TC = By.xpath("html/body/div[2]/span[4]/div/form/div[20]/div/div/input");
		
	private final By btn_back = By.xpath(".//*[@id='j_id0:j_id2:regForm:backButton']");
	private final By btn_reset = By.xpath(".//*[@id='j_id0:j_id2:regForm:resetButton']");
	private final By btn_continue = By.xpath("html/body/div[2]/span[4]/div/form/div[21]/div/div/div/input[3]");
	
	
	public page_bpas_registration()
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
	
	public void selectJobFunction(String sJobFunction)
	{
		waitForElementToBeVisible(ddl_jobFunction);
		selectCCBItem(ddl_jobFunction, sJobFunction);
	}
	
	public void setJobOther(String sJobOther) +++++UNTIL
	{
		waitForElementToBeVisible(txt_jobOther);
		sendKeys(txt_jobOther,sJobOther);
	}
		
	
	public void setFirstName(String sFirstName)
	{
		waitForElementToBeVisible(txt_firstName);
		sendKeys(txt_firstName,sFirstName);
	}
	
	public void setLastName(String sLastName)
	{
		waitForElementToBeVisible(txt_lastName);
		sendKeys(txt_lastName,sLastName);
	}
		
	
	public void setPhone(String sPhone)
	{
		waitForElementToBeVisible(txt_phone);
		sendKeys(txt_phone,sPhone);
	}
	
	public void setFax(String sfax)
	{
		waitForElementToBeVisible(txt_fax);
		sendKeys(txt_fax,sfax);
	}
	
	
	public void selectTitle(String sTitle)
	{
		waitForElementToBeVisible(ddl_subTitle);
		selectCCBItem(ddl_subTitle, sTitle);
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
