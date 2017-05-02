package pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import utilities.*;

public class page_bpas_workbench extends util_common {
	
	//sfdc seagate login page
	private final By lbl_pageTitle = By.xpath(".//*[@id='no-iframe']/div[2]/div/div/h1");
	private final By lbl_pageEnv = By.xpath(".//*[@id='no-iframe']/div[2]/div/div/h2");
	private final By txt_userName = By.xpath(".//*[@id='username']");
	private final By txt_password = By.xpath(".//*[@id='password']");
	private final By btn_Login = By.xpath(".//*[@id='no-iframe']/div[2]/div/div/form/div[3]/div/div/button");
	
	//sfdc main page
	private final By lbl_mainTitle = By.xpath(".//*[@id='globalHeaderCommunitySwitcher']");
	private final By mn_module = By.xpath(".//*[@id='tsidMenu']");
	private final By lbl_selectedModule = By.xpath(".//*[@id='tsidLabel']");
	
	
	
	public page_bpas_workbench()
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
	
	public boolean verifySFDCLoginPageTitle(String sPageTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
				
		try
		{
			waitForElementToBeVisible(lbl_pageTitle);
			
			util_constant.cAct_Result = getElementText(lbl_pageTitle);
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
	
	
	public boolean verifySFDCPageEnv(String sPageEnv)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
		try
		{
			waitForElementToBeVisible(lbl_pageEnv);
			
			util_constant.cAct_Result = getElementText(lbl_pageEnv);
			util_constant.cExp_Result = sPageEnv;
			
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
	
	public boolean verifyMainPageTitle(String sMainPgTitle)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
		try
		{
			waitForElementToBeVisible(lbl_mainTitle);
			
			util_constant.cAct_Result = getElementText(lbl_mainTitle);
			util_constant.cExp_Result = sMainPgTitle;
			
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
	
	
	
	public void selectSFDCModule(String sModule)
	{
		//List<WebElement> drpListItems = driver.findElements(mn_module); JAMES - 22 Sept
		List<WebElement> drpListItems = util_constant.cSel_Driver.findElements(mn_module);

		for(WebElement temp : drpListItems)
		{ 
			if(temp.getText().trim().equals(sModule))
			{
				temp.click();
				break;
			}
		}
	}
	
	
	
}
