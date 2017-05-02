package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class util_extentreportmgr {
	protected static ExtentReports objExtRpt;
	protected static ExtentTest objExtTest;
	private static String sReportFilePath = null;
	private static String sTestEvidenceFolderPath = null;
	private static String sReportName = null;
	private static String sReportDesc = null;
	private static ArrayList<LogStatus> arrLStatus = new ArrayList<LogStatus>();
	private static ArrayList<String> arrTStepDesc = new ArrayList<String>();
	private static ArrayList<String> arrImg = new ArrayList<String>();
	 
   
	private static final Logger LOG = LogManager.getLogger(util_common.class);
   
	 
	/**
	 * @Name			:	util_extentreportmgr	:	To initialize the extent report manager
	 * @return			:	N/A
	 * @Pre-condition	:	Properties.ini file was created with appropriate parameters and values
	 * @Author			:	JamesChan
	 */
	
	public  void establishExtentReport()
	{ 
		try
		{
	    	Properties p = new Properties();
			File currentDirectory = new File(new File(".").getAbsolutePath());
			p.load(new FileInputStream("src//main//resources//properties.ini"));
			
			sReportFilePath = p.getProperty("sextentreportpath");
			sReportDesc = p.getProperty("sextentreportdescription"); 
			sTestEvidenceFolderPath = p.getProperty("stestevidencepath");
			
	    	DateFormat dateFormat = new SimpleDateFormat("ddMMHHmm");
			Date date = new Date();
			objExtRpt = new ExtentReports((sReportFilePath + dateFormat.format(date) + ".html"),true);
			
			//objExtRpt = new ExtentReports((sReportFilePath + ".html"),true);
			objExtRpt.loadConfig(new File("extentReportConfig.xml"));
			
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		}
		catch (Exception e)
		{
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + e.getMessage());
		}

	}

	
	/**
	 * @Name			:	logFile			: To append the test cases info (result & screen) to Extent report
	 * @Parameter		:	Log Status		: Pass, Fail or NA
	 * @Parameter		:	sFileName		: Extent Report File Name
	 * @Parameter		:	sFormat			: JPG and etc
	 * @Parameter		:	sTCName			: Test Case Name
	 * @Parameter		:	sTestStepDesc	: Test Step Description
	 * @Pre-condition	:	util_extentreportmgr object is declared
	 * @Author			:	JamesChan
	 */
	public void logFile(int iNewFlag, LogStatus logStatus, String sFileName, String sFormat, String sTCID, String sRemarks, String sTestStepDesc)
	{
		try
		{
			String sImg;
			//arrLStatus.add(logStatus);
			//arrTStepDesc.add(sTestStepDesc);
			//arrImg.add(sImg);
			
			//System.out.println("Step Desc: " + sTestStepDesc);
			//String[] sWholeDesc = sTestStepDesc.split(" ---> ");
			
			//System.out.println("Split results: " + sWholeDesc[0] + "--" + sWholeDesc[1]);
			if (sRemarks.trim().equalsIgnoreCase(sReportName) == false)
			{
				sReportName = sRemarks;
				objExtTest = objExtRpt.startTest(sReportName,sReportDesc);
			}

			if (iNewFlag == 1)
			{
				sImg = objExtTest.addScreenCapture(sTestEvidenceFolderPath.trim() + "\\" + sTCID + "_" + sFileName.trim() + "." + sFormat);
				objExtTest.log(logStatus, sTestStepDesc, sImg);
			}
			else
			{
				objExtTest.log(logStatus, sTestStepDesc, "");
			}
			
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		}
		catch (Exception e)
		{
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + e.getMessage());
		}
	}
	
	
	/**
	 * @Name			:	closeExtentRpt			: To close the Extent report objects
	 * @Author			:	JamesChan
	 */
	public static void closeExtentRpt()
	{
		try
		{
			objExtRpt.endTest(objExtTest);
			objExtRpt.flush();
			
			arrLStatus = null;
			arrTStepDesc = null;
			arrImg = null;
			
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		}
		catch (Exception e)
		{
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + e.getMessage());
		}
	}
}
