package utilities;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.imageio.ImageIO;

import net.sf.cglib.core.Local;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

public class util_mswordreportmgr {
 
	private String sTestEvidenceFolderPath;
	private static final Logger LOG = LogManager.getLogger(util_common.class);
	//WebDriver driver = util_seleniumdrivermgr.getDriver();
	 
	  
	/** 
	 * @Name		:	createTestEvidenceFolder
	 * @return		:	N/A
	 * @Remarks		: 	This sub is used to create Test Evidence Folder
	 * @Author		:	JamesChan
	 */
	   
	private void createTestEvidenceFolder()
	{
		try
		{
			File f=new File(sTestEvidenceFolderPath);
  
		    if(!f.exists())
		    {
		    	f.mkdir();
		    }
		    
		    f = null;

		    LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		}	catch (Exception el)	
		{
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + el.getMessage());
			Assert.fail("Error: " + Local.class.getEnclosingMethod().getName() + " - " + el.getMessage());
		}
	}
	

	/**
	 * @Name		:	captureScreenShot
	 * @param		:	sFolderName			: 	Folder Name where you want to store the screen shot
	 * @param		:	sFileName			: 	File Name of the screen captured
	 * @param		:	sFormat				: 	File format (jpg, png and etc)
	 * @return		:	N/A
	 * @Remarks		: 	This sub is used to capture the screen shot
	 * @Author		:	JamesChan
	 */
	
	public void captureAndSaveScreenShot(int iNewFlag, String sFileName, String sFormat, String sTCID, String sRemarks, String sTestStep) throws IOException
	{
		try
		{
			//if (ExpectedConditions.alertIsPresent() == null)
			if (iNewFlag == 1)
			//if (!util_constant.cSel_Driver.toString().contains("null"))
			{
				Properties p = new Properties();
	    		p.load(new FileInputStream("src//main//resources//properties.ini"));
	    		sTestEvidenceFolderPath = p.getProperty("stestevidencepath");
	    		
	    		createTestEvidenceFolder();
	    		

	    		String sImgFilePath = sTestEvidenceFolderPath.trim() + "\\" + sTCID + "_" + sFileName.trim() + "." + sFormat;
	    		
		        File srcFile = ((TakesScreenshot) util_constant.cSel_Driver).getScreenshotAs(OutputType.FILE);
		        
		        FileUtils.copyFile(srcFile, new File(sImgFilePath));
		        
		        util_constant.sImgPath = sImgFilePath;

	            LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
			}
			
		}	catch (Exception el)	
		{
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + el.getMessage());
			Assert.fail("Error: " + Local.class.getEnclosingMethod().getName() + " - " + el.getMessage());
		}
	}
	
	/*
	public void captureAndSaveScreenShot(int iNewFlag, String sFileName, String sFormat, String sTCID, String sRemarks, String sTestStep) throws IOException
	{
		try
		{
			Robot robot = new Robot();
			
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
            
            File currentDirectory = new File(new File(".").getAbsolutePath());
    		String sFilePath = currentDirectory.getCanonicalPath();

    		Properties p = new Properties();
    		p.load(new FileInputStream("src//main//resources//properties.ini"));

    		sTestEvidenceFolderPath = p.getProperty("stestevidencepath");

    		createTestEvidenceFolder();
    		
    		String sImgFilePath = sTestEvidenceFolderPath.trim() + "\\" + sTCID + "_" + sFileName.trim() + "." + sFormat;

            ImageIO.write(screenFullImage, sFormat, new File(sImgFilePath));

            //appendScreenShotToMSWord(iNewFlag, sRemarks, sImgFilePath, sTestStep);

            robot = null;
            currentDirectory = null;
            
            LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
            
		}	catch (Exception el)	
		{
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + el.getMessage());
			Assert.fail("Error: " + Local.class.getEnclosingMethod().getName() + " - " + el.getMessage());
		}
	}*/
	
	
	/**
	 * @Name		:	appendScreenShotToMSWord
	 * @param		:	iNewFlag			: 	0 - Create New file, 1 - Append existing file
	 * @param		:	sFolderName			: 	Folder name where the test evidence document will be placed
	 * @param		:	sTestCaseName		: 	Test Case Name
	 * @param		:	sImgFilePath		:	Image to append to test evidence document
	 * @param		:	sTestStep			:	Test Step Name
	 * @Remarks		: 	This sub is used to append the screen shot
	 * @Author		:	JamesChan
	 */
	public void appendScreenShotToMSWord(int iNewFlag, String sTestCaseName, String sImgFilePath, String sTestStep) throws InvalidFormatException, FileNotFoundException, IOException
	{
		try
		{
			String sHostName;
			
			File currentDirectory = new File(new File(".").getAbsolutePath());
	 		String sFilePath = currentDirectory.getCanonicalPath();

	 		//sFilePath = sFilePath.trim() + "\\" + sFolderName.trim() + "\\" + sTestCaseName.trim() + "_TestEvidence.docx";
	 		sFilePath = sTestEvidenceFolderPath.trim() + "\\" + sTestCaseName.trim() + "_TestEvidence.docx";
	 		
			File f=new File(sFilePath);

		    XWPFDocument docum;
		    
	    	DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
	    	DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
	    	
			Date date = new Date();

			sHostName = InetAddress.getLocalHost().getHostName();
		    
		    if(f.exists())
		    {
		    	if (iNewFlag == 0)
		    	{
		    		f.delete();
		    		docum = new XWPFDocument();
		    	}
		    	else
		    	{
		    		FileInputStream fis=new FileInputStream(f);
			    	docum= new XWPFDocument(OPCPackage.open(fis)); 
			    	fis.close();
			    	fis = null;
		    	}
		    }
		    else
		    {
		    	docum=new XWPFDocument();
		    }
		    
		    XWPFParagraph p=docum.createParagraph();
		    XWPFRun r=p.createRun();
		    
		    if (iNewFlag == 0)
		    {
		    	r.setText("Automated Testing - Test Evidence Document || " + "Host - " + sHostName + " || Date - " + dateFormat.format(date));
		    }
		 
		    r.addBreak();
		    r.addBreak();
		    r.setText(sTestCaseName + " - " + sTestStep + " (Time: " + dateFormat1.format(date) + ")");
		    r.addBreak();
		    r.addPicture(new FileInputStream(sImgFilePath), Document.PICTURE_TYPE_JPEG, "name", Units.toEMU(500) , Units.toEMU(280));

		    FileOutputStream out = new FileOutputStream(sFilePath);
		    docum.write(out);
		    out.close();
		    docum.close();
		    
		    
		    r = null;
		    f = null;
		    out = null;
		    docum = null;
		    currentDirectory = null;
		    
		    LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
		}
		catch (Exception el)	
		{
			LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + el.getMessage());
			Assert.fail("Error: " + Local.class.getEnclosingMethod().getName() + " - " + el.getMessage());
		}
	}
	
}
