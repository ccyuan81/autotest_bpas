package modules;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import utilities.util_common;
import utilities.util_constant;
import utilities.util_extentreportmgr;

public class mod_extentReport extends util_common
{
	
  public boolean action_OpenExtentReport () 
  {
	  String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
	  util_constant.cExp_Result = "Passed - " + sMethod;
	  
	  util_constant.cUtil_ExtReport.establishExtentReport();
	  return true;
  }
  
  public boolean action_CloseExtentReport () 
  {
	  String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
	  util_constant.cExp_Result = "Passed - " + sMethod;
	  
	  util_extentreportmgr.closeExtentRpt();
	  return true;
  }
  
}
