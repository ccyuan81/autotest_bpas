package pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import utilities.*;

public class page_bpas_invitation_supplier_products extends util_common {
	
	//sfdc seagate login page
	private final By txt_userName = By.xpath(".//*[@id='username']");
	private final By txt_password = By.xpath(".//*[@id='password']");
	private final By btn_Login = By.xpath("html/body/div[2]/div[2]/div/div/form/div[3]/div/div/button");
	
	//supplier invitation form main page
	private final By lbl_mainTitle = By.xpath("html/body/div[1]/h3");
	private final By lbl_supplierName = By.xpath(".//*[@id='j_id0:j_id4:productForm:pb:uTable:0:j_id64']");
	private final By lbl_supplierEmail = By.xpath(".//*[@id='j_id0:j_id4:productForm:pb:uTable:0:j_id67']");
	private final By lbl_supplierStatus = By.xpath(".//*[@id='j_id0:j_id4:productForm:pb:uTable:0:j_id70']");
	private final By lbl_entitlementStatus = By.xpath(".//*[@id='j_id0:j_id4:productForm:pb:uTable:0:j_id73']");
	private final By lbl_step1 = By.xpath("html/body/div[2]/span[2]/ul/li[1]");
	private final By lbl_step2 = By.xpath("html/body/div[2]/span[2]/ul/li[2]");
	private final By lbl_step3 = By.xpath("html/body/div[2]/span[2]/ul/li[3]");
		
	private final By lin_delete = By.xpath(".//*[@id='j_id0:j_id4:productForm:pb:productInfoTable:0:j_id85']");
	private final By lin_clone = By.xpath(".//*[@id='j_id0:j_id4:productForm:pb:productInfoTable:0:j_id89']");
	
	private final By txt_comments = By.xpath(".//*[@id='j_id0:j_id4:productForm:pb:comment']");
	
	private final By ddl_product = By.xpath(".//*[@id='j_id0:j_id4:productForm:pb:productInfoTable:0:productName']");
	private final By ddl_component = By.xpath(".//*[@id='j_id0:j_id4:productForm:pb:productInfoTable:0:productComp']");
	private final By ddl_site = By.xpath(".//*[@id='j_id0:j_id4:productForm:pb:productInfoTable:0:productSite']");
	private final By ddl_role = By.xpath(".//*[@id='j_id0:j_id4:productForm:pb:productInfoTable:0:productRole']");
	
	private final By btn_addNew = By.xpath("html/body/div[2]/form/div[1]/div/div[1]/table[2]/tfoot/tr/th[1]/input");		
	private final By btn_back = By.xpath("html/body/div[2]/form/div[2]/div/div/div/input[1]");
	private final By btn_reset = By.xpath("html/body/div[2]/form/div[2]/div/div/div/input[2]");
	private final By btn_inviteSupplier = By.xpath("html/body/div[2]/form/div[2]/div/div/div/input[3]");
	
	
	public page_bpas_invitation_supplier_products()
	{
		PageFactory.initElements(util_constant.cSel_Driver, this);
	}

	
	public void selectProduct(String sProduct)
	{
		waitForElementToBeVisible(ddl_product);
		selectCCBItem(ddl_product, sProduct);
	}
	
	public void selectComponent(String sComponent)
	{
		waitForElementToBeVisible(ddl_component);
		selectCCBItem(ddl_component, sComponent);
	}
	
	public void selectSite(String sSite)
	{
		waitForElementToBeVisible(ddl_site);
		selectCCBItem(ddl_site, sSite);
	}
	
		public void selectRole(String sRole)
	{
		waitForElementToBeVisible(ddl_role);
		selectCCBItem(ddl_role, sRole);
	}
	
	public void setComments(String sComments)
	{
		waitForElementToBeVisible(txt_comments);
		sendKeys(txt_comments, sComments);
	}
		
	public void clickInviteSupplier()
	{
		waitForElementToBeVisible(btn_inviteSupplier);
		click(btn_inviteSupplier);
	}
			
		
	public void clickBack()
	{
		waitForElementToBeVisible(btn_back);
		click(btn_back);
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
	
	
	public boolean verifySupplierName(String sSupplierName)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
				
		try
		{
			waitForElementToBeVisible(lbl_supplierEmail);
			
			util_constant.cAct_Result = getElementText(lbl_supplierName);
			util_constant.cExp_Result = sSupplierName;
			
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
	
	
	
	public boolean verifySupplierEmail(String sSupplierEmail)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
				
		try
		{
			waitForElementToBeVisible(lbl_supplierEmail);
			
			util_constant.cAct_Result = getElementText(lbl_supplierEmail);
			util_constant.cExp_Result = sSupplierEmail;
			
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
	
	public boolean verifySupplierStatus(String sSupplierStatus)
	{
		String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		
				
		try
		{
			waitForElementToBeVisible(lbl_supplierStatus);
			
			util_constant.cAct_Result = getElementText(lbl_supplierStatus);
			util_constant.cExp_Result = sSupplierStatus;
			
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
