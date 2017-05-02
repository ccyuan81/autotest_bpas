package utilities;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.UnsupportedCommandException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.io.IOException;
  
/***
 * Utilities class that configures and returns a driver in accordance with the browser you wish to run the tests on.
 * The browser can be configured by system property using browser.webdriver, or calling the set method in the tests
 * The browser can also be configured by environment property if required.
 * Contains other utility methods, such as quitting the driver.
 */ 
 
 
public class util_seleniumdrivermgr extends Thread {  

    private static final Logger LOG = LogManager.getLogger(util_seleniumdrivermgr.class);
    private static WebDriver _driver = null;
    public static final String BROWSER_PROPERTY_NAME = "browser.webdriver";
    private static final String DEFAULT_BROWSER = "FIREFOX";
    //private static final String DEFAULT_BROWSER = "HTMLUNIT"; 
    //private static final String DEFAULT_BROWSER = "PHANTOMJS";
    public enum BrowserName{FIREFOX, GOOGLECHROME, IE, HTMLUNIT, EDGE, PHANTOMJS};
    public static BrowserName currentDriver;
    public static BrowserName useThisDriver = null;
	 
  
    /** sets the browser to use - can be used in the tests if required **/
    /*public static void set(BrowserName aBrowser){ 
        useThisDriver = aBrowser;

        if(_driver != null){
            _driver.quit();
            _driver = null;
        }
    }*/

    public static void set(String sBrowserType){
    	if (sBrowserType.toLowerCase() == "ff")
    	{
    		useThisDriver = BrowserName.FIREFOX;
    	}
    	else if (sBrowserType.toLowerCase() == "ie")
    	{
    		useThisDriver = BrowserName.IE;
    	}
    	else if (sBrowserType.toLowerCase() == "chrome")
    	{
    		useThisDriver = BrowserName.GOOGLECHROME;
    	}
        

        if(_driver != null){
            _driver.quit();
            _driver = null;
        }
    }
    
    /**
     * Sets and returns a driver instance with profile configured as required.
     */
    public static WebDriver getDriver() {

        if (useThisDriver == null) {  

            String defaultBrowser = getSystemProperty(BROWSER_PROPERTY_NAME, DEFAULT_BROWSER);

            if (defaultBrowser.toLowerCase().equals("firefox"))
            {
            	useThisDriver = BrowserName.FIREFOX;
            }
            
            else if (defaultBrowser.toLowerCase().equals("chrome"))
            {
            	useThisDriver = BrowserName.GOOGLECHROME;
            }
            
            else if (defaultBrowser.toLowerCase().equals("ie"))
            {
            	useThisDriver = BrowserName.IE;
            }
            
            else if (defaultBrowser.toLowerCase().equals("htmlunit"))
            {
            	useThisDriver = BrowserName.HTMLUNIT;
            }
            
            else if (defaultBrowser.toLowerCase().equals("edge"))
            {
            	useThisDriver = BrowserName.EDGE;
            }
            
            else if (defaultBrowser.toLowerCase().equals("phantomjs"))
            {
            	useThisDriver = BrowserName.PHANTOMJS;
            }
            
            else
            {
            	throw new RuntimeException("Unknown browser in " + BROWSER_PROPERTY_NAME + ": " + defaultBrowser);
            }
        }

        if (_driver == null) {

        	/*if (useThisDriver.equals("FIREFOX"))
        	{
        		String currentDir = System.getProperty("user.dir");
                final String firebugPath = currentDir + "/tools/plugins/firebug-2.0.13-fx.xpi";
                FirefoxProfile profile = new FirefoxProfile();

                try {
                    profile.setEnableNativeEvents(true);
                    profile.addExtension(new File(firebugPath));
                    profile.setPreference("extensions.firebug.currentVersion", "2.0.13");
                    //profile.setPreference("extensions.firebug.showFirstRunPage", false);
                    //profile.setPreference("extensions.firebug.allPagesActivation", "on");
                    //profile.setPreference("extensions.firebug.defaultPanelName", "net");
                    //profile.setPreference("extensions.firebug.net.enableSites", true);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                _driver = new FirefoxDriver(profile);
                currentDriver = BrowserName.FIREFOX;
        	}
        	else if (useThisDriver.equals("HTMLUNIT"))
        	{
        		_driver = new HtmlUnitDriver();
                currentDriver = BrowserName.HTMLUNIT;
        	}
        	else if (useThisDriver.equals("IE"))
        	{
        		setDriverPropertyIfRequired("webdriver.ie.driver", "/tools/iedriver/IEDriverServer.exe");

                DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
                caps.setCapability("ignoreZoomSetting", true);

                _driver = new InternetExplorerDriver(caps);
                currentDriver = BrowserName.IE;
        	}
        	else if (useThisDriver.equals("GOOGLECHROME"))
        	{
        		 setDriverPropertyIfRequired("webdriver.chrome.driver","/tools/chromedriver/chromedriver.exe");

                 ChromeOptions options = new ChromeOptions();
                 options.addArguments("disable-plugins");
                 options.addArguments("disable-extensions");
                 options.addArguments("test-type");

                 _driver = new ChromeDriver(options);
                 currentDriver = BrowserName.GOOGLECHROME;
        	}
        	else if (useThisDriver.equals("EDGE"))
        	{
        		setDriverPropertyIfRequired("webdriver.edge.driver","/tools/edgedriver/MicrosoftWebDriver.exe");

                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setPageLoadStrategy("eager");
                _driver = new EdgeDriver(edgeOptions);
                currentDriver = BrowserName.EDGE;
        	}*/
        	
        	
            switch (useThisDriver) {
            	case PHANTOMJS:
        	        
            		String currentDir1 = System.getProperty("user.dir");
         	        File file = new File(currentDir1 + "/src/main/resources/phantomjsdriver/phantomjs.exe");
         	        
         	        System.setProperty("phantomjs.binary.path", file.getAbsolutePath());	

         	        DesiredCapabilities caps1 = new DesiredCapabilities();
         	        caps1.setJavascriptEnabled(true);
         	        caps1.setCapability("takesScreenshot", true);
         	        caps1.setCapability("screen-resolution", "1280x1024");
                  
         	        caps1.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, file.getAbsolutePath());

         	        String[] phantomArgs = new  String[] {"--webdriver-loglevel=NONE"};
         	        caps1.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomArgs);

         	        _driver = new PhantomJSDriver(caps1);
                	currentDriver = BrowserName.PHANTOMJS;
               
                	break;
                	
                case FIREFOX:

                    String currentDir = System.getProperty("user.dir");
                    final String firebugPath = currentDir + "/src/main/resources/plugins/firebug-2.0.13-fx.xpi";
                    FirefoxProfile profile = new FirefoxProfile();

                    try {
                        profile.setEnableNativeEvents(true);
                        profile.addExtension(new File(firebugPath));
                        profile.setPreference("extensions.firebug.currentVersion", "2.0.13");
                        //profile.setPreference("extensions.firebug.showFirstRunPage", false);
                        //profile.setPreference("extensions.firebug.allPagesActivation", "on");
                        //profile.setPreference("extensions.firebug.defaultPanelName", "net");
                        //profile.setPreference("extensions.firebug.net.enableSites", true);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                    _driver = new FirefoxDriver(profile);
                    currentDriver = BrowserName.FIREFOX;
                    break;
                    

                case HTMLUNIT:

                    _driver = new HtmlUnitDriver();
                    currentDriver = BrowserName.HTMLUNIT;
                    break;

                case IE:

                    //Please note the requirements for using IE here: https://code.google.com/p/selenium/wiki/InternetExplorerDriver#Required_Configuration
                    //IE driver requires the webdriver property to be set
                    //Sets property if not supplied
                    setDriverPropertyIfRequired("webdriver.ie.driver", "/src/main/resources/iedriver/IEDriverServer.exe");

                    DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
                    caps.setCapability("ignoreZoomSetting", true);

                    _driver = new InternetExplorerDriver(caps);
                    currentDriver = BrowserName.IE;
                    break;

                case GOOGLECHROME:

                    //Chrome driver requires the webdriver property to be set
                    //Sets property if not supplied
                    setDriverPropertyIfRequired("webdriver.chrome.driver","/src/main/resources/chromedriver/chromedriver.exe");

                    ChromeOptions options = new ChromeOptions();
                    options.addArguments("disable-plugins");
                    options.addArguments("disable-extensions");
                    options.addArguments("test-type");

                    _driver = new ChromeDriver(options);
                    currentDriver = BrowserName.GOOGLECHROME;
                    break;

                case EDGE:
                    //Edge Driver requires you install the Microsoft Webdriver Server, then set the system property
                    setDriverPropertyIfRequired("webdriver.edge.driver","/src/main/resources/edgedriver/MicrosoftWebDriver.exe");

                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.setPageLoadStrategy("eager");
                    _driver = new EdgeDriver(edgeOptions);
                    currentDriver = BrowserName.EDGE;
            }
            
          }else {
            //originally checked if the window handle exists here, and quit the driver, but this caused issues in the tests
            // leaving it allows the driver instance to simply be returned when called.
           }

        Runtime.getRuntime().addShutdownHook(
                new Thread() {
                    @Override
					public void run() {
                        util_seleniumdrivermgr.quitDriver();
                    }
                }
        );

        return _driver;
    }


    /**set the driver properties for Chrome, IE and Edge drivers if not passed in**/
    public static void setDriverPropertyIfRequired(String propKey, String relativeToUserPath){

        if(!System.getProperties().containsKey(propKey)) {

            String currentDir = System.getProperty("user.dir");
            String driverLocation = currentDir + relativeToUserPath;
            File driverExecutable = new File(driverLocation);

            try {
                if (driverExecutable.exists()) {
                    System.setProperty(propKey, driverLocation);
                }
            } catch (Exception e) {
                LOG.error("The driver does not exist at that location: " + driverLocation);
            }
        }
    }

    public static void quitDriver(){
        if(_driver != null){
            try{
                _driver.quit();
                _driver = null;
                useThisDriver = null; //JAMES - 22 Sept

            }catch(Exception e){
                LOG.error("Driver could not quit: " + e);
            }
        }
    }

    public static WebDriver getDriver(String theURL, boolean maximise){
        getDriver();
        _driver.get(theURL);
        if(maximise){
            try{
            	
                _driver.manage().window().maximize();
            }catch(UnsupportedCommandException e){
                LOG.warn("This driver does not support maximising the window");
            }
        }
        return _driver;
    }

    public static WebDriver getDriver(String theURL){
        return getDriver(theURL, true);
    }


    /**
     * Allows setting the driver using system property or environment variable.
     * Order of precedence is system property, environment variable then default as set above
     **/
    public static String getSystemProperty(String propName, String theDefault){

        String value = System.getProperty(propName);
        if(value == null){

            LOG.info("Property was not found " + propName);

            try {
                value = System.getenv(value);
            } catch (NullPointerException e){

            }

            if(value == null){
                LOG.info("Environment variable was not found " + propName + " using the default driver: " + theDefault);
                value = theDefault;

            }else {
                LOG.info("Using environment variable " + propName + " with value " + value);
            }
        }else {
            LOG.info("Using property " + propName + "with value: " + value);
        }
        return value;
    }
}


