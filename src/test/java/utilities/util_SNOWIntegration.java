package utilities;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.gargoylesoftware.htmlunit.javascript.host.intl.DateTimeFormat;



public class util_SNOWIntegration {
	
	 CloseableHttpClient httpclient;
	 
	 CloseableHttpResponse response, response1;
	 
	 String  sPropertiesINIPath = "C://Automated Testing Evidence (Selenium)//ciam//TestRunResult//properties.ini", 
			 sDateTime, responseBody, responseBody1, responseBody2, sysIdString, numberString, preReqString, executionStatusString, 
			 patchData = "", sParent, shortDescriptionString, shortDescriptionString2;

	 static String sStartTime = "", sEndTime = "", sExecutionDuration, sExecutionStatus, sShortDesc, sTPStartTime, sTPEndTime, sFirstTestCase="Y", sLastTestCase="N", sTestCaseNumber, sTestCaseID;
	 
	 String[] responseBodyArray, responseBodyArray1, sysIdArray, numberArray, tmTestCaseInstanceArray, preReqArray, executionStatusArray, responseBodyArray2, shortDescriptionArray, shortDescriptionArray2;

	 Date startDate = null, endDate = null;
	 
	 int sysIdLoop, numberLoop, preReqLoop, forLoopCount, shortDescriptionLoop, shortDescriptionLoop2;
	 
	 static int counter, totalTestCasesCount, passedCount, failedCount, totalExecutionCount, totalTestCasesSkipped;;
	 
	 HttpEntity entity;
	 
	 private static String sSnowURL = null, sSnowUserName = null, sSnowPassword = null, sReportFileName = null, 
			 				sReportFilePath = null, sReportHistoryFilePath = null, sJenkinsSrcMainResourcesFilePath = null, 
			 				sTc_ExcelFile = null;
	 
	 private static Component frame = null;
	 
	 private static final Logger LOG = LogManager.getLogger(util_common.class);
		
	 
	
		public void currentDateTime(){
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			Date date = new Date();
			util_constant.cCurrentDateTime = date;
		}
		
		
		
		public void getPropertiesIni(String sPropertiesINI) throws FileNotFoundException, IOException{
			 Properties p = new Properties();
			 File currentDirectory = new File(new File(".").getAbsolutePath());
			 p.load(new FileInputStream(sPropertiesINI));
			
			 util_constant.cSnowURL = p.getProperty("sSnowURL");
			 util_constant.cSnowUserName = p.getProperty("sSnowUserName");
			 util_constant.cSnowPassword = p.getProperty("sSnowPassword");
			 util_constant.cSnowTestPlanTemplate = p.getProperty("sSnowTestPlanTemplate");
			 util_constant.cSnowRerun = p.getProperty("sSnowRerun");
			 util_constant.cSnowTestPlanNumber = p.getProperty("sSnowTestPlanNumber");
			 util_constant.cSnowTestPlanHistoryNumber = p.getProperty("sSnowTestPlanHistoryNumber");
			 util_constant.cTestCasesINI = p.getProperty("sTestCasesINI"); 
			 util_constant.cPropertiesINI = p.getProperty("sPropertiesINI"); 
			 util_constant.cJenkinsSrcMainResourcesFilePath = p.getProperty("sJenkinsSrcMainResourcesPath");
			 util_constant.cTc_ExcelFilePath = p.getProperty("sexcelreportpath");
			 util_constant.cTc_ExcelFileName = p.getProperty("sexcelreportname");

		}
		
		
		
		public void createIniFile(){
			try{
				sReportHistoryFilePath = util_constant.cTc_ExcelFilePath+"\\Result History";
				
				sTc_ExcelFile = util_constant.cTc_ExcelFilePath+"\\"+util_constant.cTc_ExcelFileName+".xlsx";
				File f = new File(sTc_ExcelFile);
				File jenkinsF = new File(util_constant.cJenkinsSrcMainResourcesFilePath);
				
				util_excelreportmgr util_excelreportmgr = new util_excelreportmgr();
				util_excelreportmgr.createResultFolder();;
				
				if (!jenkinsF.exists()){
					System.out.println("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - Jenkins workspace not found");
				}
				
				
				if (!f.exists()){
					 try
				        {			            
				            InputStream input = null;
				            OutputStream output = null;

				            try {
				                input = new FileInputStream(util_constant.cJenkinsSrcMainResourcesFilePath + "\\properties.ini");
				                output = new FileOutputStream(util_constant.cTc_ExcelFilePath + "\\properties.ini");
				                byte[] buf = new byte[1024];
				                int bytesRead;
				                while ((bytesRead = input.read(buf)) > 0) {
				                    output.write(buf, 0, bytesRead);
				                }
				            } finally {
				                input.close();
				                output.close();
				            }
				            

				            try {
				                input = new FileInputStream(util_constant.cJenkinsSrcMainResourcesFilePath + "\\test cases.ini");
				                output = new FileOutputStream(util_constant.cTc_ExcelFilePath + "\\test cases.ini");
				                byte[] buf = new byte[1024];
				                int bytesRead;
				                while ((bytesRead = input.read(buf)) > 0) {
				                    output.write(buf, 0, bytesRead);
				                }
				            } finally {
				                input.close();
				                output.close();
				            }
				            
				        }
				        catch(IOException e)
				        {
				            System.out.println(e);
				        }
				      }
						
				LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
			}
			catch (Exception e)
			{
				LOG.info("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + e.getMessage());
			}
			
		}
	
	
		public void establishSNOWConnection() throws HttpException, IOException {
				System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
				
				 Properties p = new Properties();
				 File currentDirectory = new File(new File(".").getAbsolutePath());
				 p.load(new FileInputStream(util_constant.cPropertiesINI));
				
				 sSnowURL = p.getProperty("sSnowURL");
				 sSnowUserName = p.getProperty("sSnowUserName");
				 sSnowPassword = p.getProperty("sSnowPassword");
				
				
				try{
					final String sPasswordDecrypted = util_passwordmgr.decrypt(sSnowPassword);
					
			 		CredentialsProvider credsProvider = new BasicCredentialsProvider();
			 		
			        credsProvider.setCredentials(
		                new AuthScope(new HttpHost(sSnowURL)),
		                new UsernamePasswordCredentials(sSnowUserName, sPasswordDecrypted)
		               );
			        
	        		httpclient = HttpClients.custom()
	                .setDefaultCredentialsProvider(credsProvider)
	                .build();
				}
				catch (Exception e)
				{
					System.out.println("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + e.getMessage());
				}
		}
		
		
		
		public void disconnectSNOWConnection() throws HttpException, IOException {
			System.out.println("\n\nExecuting method: "+new Object() {}.getClass().getEnclosingMethod().getName());
			
			response.close();
			httpclient.close();
		}
 
	
			// sNumber refer to Test Case number show at SNOW UI
			public void getTestCaseDetail(String sNumber) throws HttpException, IOException {	
				System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
				
			      try {
			          HttpGet httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance?sysparm_query=number%3D"+sNumber+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=number%2Cshort_description%2Cexecution_status%2Csys_id%2Cprereq%2Csys_updated_on%2Cprereq");
			          httpget.setHeader("Accept", "application/json");
			          System.out.println("\n\n------------------------------------------------");
			          System.out.println("\nExecuting Request " + httpget.getRequestLine());
			          response = httpclient.execute(httpget);
			          
			          try {
			              System.out.println("\n" + response.getStatusLine());
			              responseBody = EntityUtils.toString(response.getEntity());
			              
			              responseBodyArray = responseBody.split("[{,}]");
			              
			              for (String responseBodySplit : responseBodyArray) {		            	  
			            	  if (!(responseBodySplit.matches(".*result.*") || responseBodySplit.matches(":\\[") || responseBodySplit.matches("]") || (responseBodySplit.matches("")))){
				                  System.out.println(responseBodySplit);
			            	  }
			              }
			          } finally {
			              response.close();
			          }
			      } finally {
			          httpclient.close();
			      }
		
			}
			
			
			// sTmTestCaseInstance refer to Test Case number show at SNOW UI
			public void getTestStepDetail(String sTmTestCaseInstance) throws HttpException, IOException {
				System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
				
			      try {
			          HttpGet httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_instance?sysparm_query=tm_test_case_instance.number%3D"+sTmTestCaseInstance+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=number%2Cexecution_status%2Csys_id&sysparm_count=true");
			          httpget.setHeader("Accept", "application/json");
			          System.out.println("\n\n------------------------------------------------");
			          System.out.println("\nExecuting Request " + httpget.getRequestLine());
			          response = httpclient.execute(httpget);
			          
			          try {
			              System.out.println("\n" + response.getStatusLine());
			              responseBody = EntityUtils.toString(response.getEntity());
			              
			              responseBodyArray = responseBody.split("[{,}]");
			              
			              for (String responseBodySplit : responseBodyArray) {				            	  
			            	  if (!(responseBodySplit.matches(".*result.*") || responseBodySplit.matches(":\\[") || responseBodySplit.matches("]") || (responseBodySplit.matches("")))){
				                  System.out.println(responseBodySplit);
			            	  }
			              }
			          } finally {
			              response.close();
			          }
			      } finally {
			          httpclient.close();
			      }
		
			}
			
			
			// sTmTestCaseInstance refer to Test Case number show at SNOW UI
			// sExpectedResult referring to expected test result for the step
			// sExecutionStatus can be Unexecuted, Passed or Failed
			public void addTestStep(String sTmTestCaseInstance, String sTest, String sExpectedResult, String sActualResult, String sExecutionStatus) throws HttpException, IOException {	
				System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
				
				if (sExpectedResult.toLowerCase().contains("passed")){
					sExpectedResult = "Passed";
				}else if (sExpectedResult.toLowerCase().contains("failed")){
					sExpectedResult = "Failed";
				}
				
				
					if (sActualResult.toLowerCase().contains("passed")){
						sActualResult = "Passed";
					}else if (sActualResult.toLowerCase().contains("failed")){
						sActualResult = "Failed";
					}
					
						String postData = "{\"tm_test_case_instance\":\""+sTmTestCaseInstance+"\",\"test\":\""+sTest+"\",\"expected_result\":\""+sExpectedResult+"\",\"actual_result\":\""+sActualResult+"\",\"execution_status\":\""+sExecutionStatus+"\",\"sys_updated_on\":\"\"}";
							
						    try {
						    	establishSNOWConnection();	
						    	
						    	HttpPost httpPost = new HttpPost("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_instance");
							 		httpPost.setHeader("Accept", "application/json");
							 		httpPost.setHeader("Content-Type", "application/json");
							        HttpEntity entity = new ByteArrayEntity(postData.getBytes("utf-8"));
							 		httpPost.setEntity(entity);
							    
							 	System.out.println("\n\n------------------------------------------------");	
						        System.out.println("\nExecuting Request " + httpPost.getRequestLine());
						        CloseableHttpResponse response = httpclient.execute(httpPost);
						        try {
						        	 System.out.println("\n" + response.getStatusLine());
						              responseBody = EntityUtils.toString(response.getEntity());
						        } finally {
						            response.close();
						        }
						    } finally {
						        httpclient.close();
						    }
			}
			
				
				// sParent refer to Test Plan number show at SNOW UI
				public void testStepsReset(String sParent) throws HttpException, IOException {		
					System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
					
					 try {
						 establishSNOWConnection();
						 
				          HttpGet httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance?sysparm_query=parent.number%3D"+sParent+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=number");
				          httpget.setHeader("Accept", "application/json");
				          System.out.println("\n\n------------------------------------------------");
				          System.out.println("\nExecuting Request " + httpget.getRequestLine());
				          response = httpclient.execute(httpget);
				          
				          try {
				              System.out.println("\n" + response.getStatusLine());
				              responseBody = EntityUtils.toString(response.getEntity());
				              
				              responseBodyArray = responseBody.split("[{,}]");
				             
				              for (String responseBodySplit : responseBodyArray) {
				            	  if (!(responseBodySplit.matches(".*result.*") || responseBodySplit.matches(":\\[") || responseBodySplit.matches("]") || (responseBodySplit.matches("")))){
					                  System.out.println(responseBodySplit);
					                  
					                  if (responseBodySplit.matches("\"number\":.*")){
					                	  numberString = responseBodySplit;
					                	  numberArray = numberString.split("[\":]");
					                	  
					                	  numberLoop = 0;
					                	  for (String number : numberArray) {
					                		  
					                		  if (numberLoop==numberArray.length-1){  
					                			  httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_instance?sysparm_query=tm_test_case_instance.number%3D"+number+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=sys_id&sysparm_count=true");
										          httpget.setHeader("Accept", "application/json");
										          System.out.println("\n\n------------------------------------------------");
										          System.out.println("\nExecuting Request " + httpget.getRequestLine());
										          response = httpclient.execute(httpget);
										          
										          System.out.println("\n" + response.getStatusLine());
									              responseBody1 = EntityUtils.toString(response.getEntity());
									              
									              responseBodyArray1 = responseBody1.split("[{,}]");
									              
									              for (String responseBodySplit1 : responseBodyArray1) {
									            	  if (!(responseBodySplit1.matches(".*result.*") || responseBodySplit1.matches(":\\[") || responseBodySplit1.matches("]") || (responseBodySplit1.matches("")))){
										                  System.out.println(responseBodySplit1);
										                  
										                  if (responseBodySplit1.matches("\"sys_id\":.*")){
										                	  sysIdString = responseBodySplit1;
										                	  sysIdArray = sysIdString.split("[\":]");
										                	  
										                	  sysIdLoop = 0;
										                	  for (String sysId : sysIdArray) {
										                		  
										                		  if (sysIdLoop==sysIdArray.length-1){
										                			  
										                			HttpDelete httpDelete = new HttpDelete("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_instance/"+sysId+"?sysparm_input_display_value=true");
									                	        	httpDelete.setHeader("Accept", "application/json");
									                	 	        
									                	        	System.out.println("\n\n------------------------------------------------");
									                	            System.out.println("\nExecuting Request " + httpDelete.getRequestLine());
									                	            CloseableHttpResponse response = httpclient.execute(httpDelete);
										                			 		
									                	            System.out.println("\n" + response.getStatusLine());
										                		  }
										                		  sysIdLoop ++;
										                	  }
										                  }
									            	  }
									              }
					                		  }
					                		  numberLoop ++;
					                	  }
					                  }
				            	  }
				              }
				          } 
					
					
						finally {
					              response.close();
					          }
					      } finally {
					          httpclient.close();
					      }
			}
				
				
				// sNumber refer to Test Plan number show at SNOW UI
				public void testPlanReset(String sNumber) throws HttpException, IOException {	
					System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
					
					
					
					
					String patchData = "{\"instructions\":\"<p>Start Date:</p>\\r\\n<p>End Date:</p>\",\"sys_updated_on\":\"\"}";
					
					 try {
						 establishSNOWConnection();
						 
				          HttpGet httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_plan?sysparm_query=number%3D"+sNumber+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=sys_id");
				          httpget.setHeader("Accept", "application/json");
				          System.out.println("\n\n------------------------------------------------");
				          System.out.println("\nExecuting Request " + httpget.getRequestLine());
				          response = httpclient.execute(httpget);
				          
				          try {
				              System.out.println("\n" + response.getStatusLine());
				              responseBody = EntityUtils.toString(response.getEntity());
				              
				              responseBodyArray = responseBody.split("[{,}]");
				              
				              for (String responseBodySplit : responseBodyArray) {
				            	  if (!(responseBodySplit.matches(".*result.*") || responseBodySplit.matches(":\\[") || responseBodySplit.matches("]") || (responseBodySplit.matches("")))){
					                  System.out.println(responseBodySplit);
					                  
					                  if (responseBodySplit.matches("\"sys_id\":.*")){
					                	  sysIdString = responseBodySplit;
					                	  sysIdArray = sysIdString.split("[\":]");
					                	  
					                	  sysIdLoop = 0;
					                	  for (String sysId : sysIdArray) {
					                		  
					                		  if (sysIdLoop==sysIdArray.length-1){
					                			  
					                			HttpPatch httpPatch = new HttpPatch("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_plan/"+sysId+"?sysparm_input_display_value=true");
					  					 		httpPatch.setHeader("Accept", "application/json");
					  					 		httpPatch.setHeader("Content-Type", "application/json");
					  					        HttpEntity entity = new ByteArrayEntity(patchData.getBytes("utf-8"));
					  					 		httpPatch.setEntity(entity);
					                			
					  					 		System.out.println("\n\n------------------------------------------------");
					  					        System.out.println("\nExecuting Request " + httpPatch.getRequestLine());
					  					        CloseableHttpResponse response = httpclient.execute(httpPatch);
					  					        
				                	            System.out.println("\n" + response.getStatusLine());
					                		  }
					                		  sysIdLoop ++;
					                	  }
					                  }
				            	  }
				              }
				          } finally {
				              response.close();
				          }
				      } finally {
				          httpclient.close();
				      }
				}
				
				
				
					// sParent refer to Test Plan number show at SNOW UI
					// sExecutionStatus can be Unexecuted, Passed, Failed, Blocked, In Progress, Retest
					public void testCasesReset(String sParent, String sExecutionStatus) throws HttpException, IOException {	
						System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());	
						
						String patchData = "{\"prereq\":\"<p>Start Date:</p>\\r\\n<p>End Date:</p>\",\"sys_updated_on\":\"\",\"execution_status\":\""+sExecutionStatus+"\"}";
						
						 try {
							 establishSNOWConnection();
							 
					          HttpGet httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance?sysparm_query=parent.number%3D"+sParent+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=sys_id");
					          httpget.setHeader("Accept", "application/json");
					          System.out.println("\n\n------------------------------------------------");
					          System.out.println("\nExecuting Request " + httpget.getRequestLine());
					          response = httpclient.execute(httpget);
					          
					          try {
					              System.out.println("\n" + response.getStatusLine());
					              responseBody = EntityUtils.toString(response.getEntity());
					              
					              responseBodyArray = responseBody.split("[{,}]");
					             
					              for (String responseBodySplit : responseBodyArray) {
					            	  if (!(responseBodySplit.matches(".*result.*") || responseBodySplit.matches(":\\[") || responseBodySplit.matches("]") || (responseBodySplit.matches("")))){
						                  System.out.println(responseBodySplit);
						                  
						                  if (responseBodySplit.matches("\"sys_id\":.*")){
						                	  sysIdString = responseBodySplit;
						                	  sysIdArray = sysIdString.split("[\":]");
						                	  
						                	  sysIdLoop = 0;
						                	  for (String sysId : sysIdArray) {
						                		  
						                		  if (sysIdLoop==sysIdArray.length-1){  
						                			HttpPatch httpPatch = new HttpPatch("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance/"+sysId+"?sysparm_input_display_value=true");
						  					 		httpPatch.setHeader("Accept", "application/json");
						  					 		httpPatch.setHeader("Content-Type", "application/json");
						  					        HttpEntity entity = new ByteArrayEntity(patchData.getBytes("utf-8"));
						  					 		httpPatch.setEntity(entity);
						                			
						  					 		System.out.println("\n\n------------------------------------------------");
						  					        System.out.println("\nExecuting request " + httpPatch.getRequestLine());
						  					        CloseableHttpResponse response = httpclient.execute(httpPatch);
						  					        
					                	            System.out.println("\n" + response.getStatusLine());
					                	            
					                	            response.close();
						                		  }
						                		  sysIdLoop ++;
						                	  }
						                  }
					            	  }
					              }
					          } finally {
					              response.close();
					          }
					      } finally {
					          httpclient.close();
					      }
					}
					
					
					
					// sNumber refer to Test Plan number show at SNOW UI
					public void testPlanStartUpdate(String sNumber) throws HttpException, IOException {	
						System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());						
						
						String patchData = "{\"instructions\":\"<p><b>Start Date:</b> "+util_constant.cCurrentDateTime+"</p>\\r\\n<p><b>End Date: </b></p>\",\"sys_updated_on\":\"\"}";
						
						 try {
							 	  establishSNOWConnection();
								  HttpGet httpget1 = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_plan?sysparm_query=number%3D"+sNumber+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=instructions");
						          httpget1.setHeader("Accept", "application/json");
						          System.out.println("\n\n------------------------------------------------");
						          System.out.println("\nExecuting Request " + httpget1.getRequestLine());
						          response = httpclient.execute(httpget1);
						          
						          System.out.println("\n" + response.getStatusLine());
					              responseBody = EntityUtils.toString(response.getEntity());
					              
					              responseBodyArray = responseBody.split("Start Date:|End Date:");
					              
					              forLoopCount = 0;
					              for (String responseBodySplit:responseBodyArray){
					            	  if (forLoopCount==1){
					            		  if (!responseBodySplit.matches("^.*?(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec).*$")){
					            			  response.close();
					            				
												 
									          HttpGet httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_plan?sysparm_query=number%3D"+sNumber+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=sys_id");
									          httpget.setHeader("Accept", "application/json");
									          System.out.println("\n\n------------------------------------------------");
									          System.out.println("\nExecuting Request " + httpget.getRequestLine());
									          response = httpclient.execute(httpget);
									          
									          try {
									              System.out.println("\n" + response.getStatusLine());
									              responseBody = EntityUtils.toString(response.getEntity());
									              
									              responseBodyArray = responseBody.split("[{,}]");
									              
									              for (String responseBodySplit1 : responseBodyArray) {
									            	  if (!(responseBodySplit1.matches(".*result.*") || responseBodySplit1.matches(":\\[") || responseBodySplit1.matches("]") || (responseBodySplit1.matches("")))){
										                  System.out.println(responseBodySplit1);
										                  
										                  if (responseBodySplit1.matches("\"sys_id\":.*")){
										                	  sysIdString = responseBodySplit1;
										                	  sysIdArray = sysIdString.split("[\":]");
										                	  
										                	  sysIdLoop = 0;
										                	  for (String sysId : sysIdArray) {
										                		  
										                		  if (sysIdLoop==sysIdArray.length-1){
										                			  
										                			HttpPatch httpPatch = new HttpPatch("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_plan/"+sysId+"?sysparm_input_display_value=true");
										  					 		httpPatch.setHeader("Accept", "application/json");
										  					 		httpPatch.setHeader("Content-Type", "application/json");
										  					        HttpEntity entity = new ByteArrayEntity(patchData.getBytes("utf-8"));
										  					 		httpPatch.setEntity(entity);
										                			
										  					 		System.out.println("\n\n------------------------------------------------");
										  					        System.out.println("\nExecuting Request " + httpPatch.getRequestLine());
										  					        CloseableHttpResponse response = httpclient.execute(httpPatch);
										  					        
									                	            System.out.println("\n" + response.getStatusLine());
										                		  }
										                		  sysIdLoop ++;
										                	  }
										                  }
									            	  }
									              }
									              
									          } finally {
									              response.close();
									          }
					            		  }
					            	  }
					            	  forLoopCount ++;
					              }
					              
					             
						      } finally {
						          httpclient.close();
						      }
						 
					}
					
					
					
					// sNumber refer to Test Case number show at SNOW UI
					// sExecutionStatus can be Unexecuted, Passed, Failed, Blocked, In progress, Retest
					public void testCaseStartUpdate(String sNumber, String sExecutionStatus, String sTestCaseId) throws HttpException, IOException {	
						System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
						
						 try {
							 currentDateTime();
							 
							if (util_constant.cSnowRerun.equals("Y")){
								 createTestCasesHistory();
							 }
							
							util_constant.cUtil_ExcelReport.setStartExcelResult(util_constant.cTc_TestCaseName);
							 
							 establishSNOWConnection();
							 
					          HttpGet httpget1 = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance?sysparm_query=number%3D"+sNumber+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=parent.number");
					          httpget1.setHeader("Accept", "application/json");
					          System.out.println("\n\n------------------------------------------------");
					          System.out.println("\nExecuting Request " + httpget1.getRequestLine());
					          response = httpclient.execute(httpget1);
					          
				              System.out.println("\n" + response.getStatusLine());
				              responseBody = EntityUtils.toString(response.getEntity());
				              
				              responseBodyArray = responseBody.split("[{,:}]");
							 
				              for (String responseBodySplit : responseBodyArray) {
				            	  if (responseBodySplit.contains("TMTP")){
				            		  sParent = responseBodySplit.toString().trim();
				            		  sParent = sParent.replace("\"", "");
				            	  }
				              }
				              
				              util_constant.utilSNOWIntegration.testPlanStartUpdate(sParent);
							 
							 String patchData = "{\"prereq\":\"<p><b>Start Date:</b> "+util_constant.cCurrentDateTime+"</p>\\r\\n<p><b>End Date: </b></p>\",\"sys_updated_on\":\"\",\"execution_status\":\""+sExecutionStatus+"\"}";
							 
							 establishSNOWConnection();
							 
					          HttpGet httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance?sysparm_query=number%3D"+util_constant.cSnowTestCaseNumber+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=sys_id");
					          httpget.setHeader("Accept", "application/json");
					          System.out.println("\n\n------------------------------------------------");
					          System.out.println("\nExecuting Request " + httpget.getRequestLine());
					          response = httpclient.execute(httpget);
					          
					          try {
					              System.out.println("\n" + response.getStatusLine());
					              responseBody = EntityUtils.toString(response.getEntity());
					              
					              responseBodyArray = responseBody.split("[{,}]");
					             
					              for (String responseBodySplit : responseBodyArray) {
					            	  if (!(responseBodySplit.matches(".*result.*") || responseBodySplit.matches(":\\[") || responseBodySplit.matches("]") || (responseBodySplit.matches("")))){
						                  System.out.println(responseBodySplit);
						                  
						                  if (responseBodySplit.matches("\"sys_id\":.*")){
						                	  sysIdString = responseBodySplit;
						                	  sysIdArray = sysIdString.split("[\":]");
						                	  
						                	  sysIdLoop = 0;
						                	  for (String sysId : sysIdArray) {
						                		  
						                		  if (sysIdLoop==sysIdArray.length-1){  
						                			HttpPatch httpPatch = new HttpPatch("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance/"+sysId+"?sysparm_input_display_value=true");
						  					 		httpPatch.setHeader("Accept", "application/json");
						  					 		httpPatch.setHeader("Content-Type", "application/json");
						  					        HttpEntity entity = new ByteArrayEntity(patchData.getBytes("utf-8"));
						  					 		httpPatch.setEntity(entity);
						                			
						  					 		System.out.println("\n\n------------------------------------------------");
						  					        System.out.println("\nExecuting request " + httpPatch.getRequestLine());
						  					        CloseableHttpResponse response = httpclient.execute(httpPatch);
						  					        
					                	            System.out.println("\n" + response.getStatusLine());
						                		  }
						                		  sysIdLoop ++;
						                	  }
						                  }
					            	  }
					              }
					          } finally {
					              response.close();
					          }
					      } finally {
					          httpclient.close();
					      }
					      
					}
					
					
					
					// sNumber refer to Test Plan number show at SNOW UI
					public void testPlanEndUpdate(String sNumber) throws HttpException, IOException {						
						System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());						
						
						String patchData = "";
						
						 try {
//							 HttpGet httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_plan?sysparm_query=number%3D"+sNumber+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=instructions");
//					          httpget.setHeader("Accept", "application/json");
//					          System.out.println("\n\n------------------------------------------------");
//					          System.out.println("\nExecuting Request " + httpget.getRequestLine());
//					          response = httpclient.execute(httpget);
//					          
//					          System.out.println("\n" + response.getStatusLine());
//				              responseBody = EntityUtils.toString(response.getEntity());
//				              
//				              responseBodyArray = responseBody.split("[{}]");
//
//				              for (String responseBodySplit : responseBodyArray) {
//					              if (!(responseBodySplit.matches(".*result.*") || responseBodySplit.matches(".*].*") || (responseBodySplit.matches("") || responseBodySplit.matches(".*}.*")))){
//						          
//					            	  System.out.println(responseBodySplit);
//					            	  
//						              int respondBodyLength = responseBodySplit.length();
//						              int respondBodyRightCount = 5; //count for </p>"
//						              int respondBodyLeftCount = respondBodyLength - respondBodyRightCount;
//						              
//						              String patchDataLeft = responseBodySplit.substring(0, respondBodyLeftCount);
//						              
//						              patchData = "{" + patchDataLeft + " " + util_constant.cCurrentDateTime + "</p>\",\"sys_updated_on\":\"\"}";
//					              }
//				              }
							 
							 HttpGet httpget3 = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_plan?sysparm_query=number%3D"+sNumber+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=instructions");
				              httpget3.setHeader("Accept", "application/json");
					          System.out.println("\n\n------------------------------------------------");
					          System.out.println("\nExecuting Request " + httpget3.getRequestLine());
					          response = httpclient.execute(httpget3);
					          
					          System.out.println("\n" + response.getStatusLine());
				              responseBody = EntityUtils.toString(response.getEntity());
				              
				              responseBodyArray = responseBody.split("[{}]");

				              for (String responseBodySplit : responseBodyArray) {
					              if (!(responseBodySplit.matches(".*result.*") || responseBodySplit.matches(".*].*") || (responseBodySplit.matches("") || responseBodySplit.matches(".*}.*")))){
						          
					            	  System.out.println(responseBodySplit);
					            	  
						              int respondBodyLength = responseBodySplit.length();
						              int respondBodyRightCount = 0;
						              int respondBodyLeftCount = 0;
						              
						              String patchDataLeft = "";
						              
						             
						            	  respondBodyRightCount = 33; //count for </p>"
							              respondBodyLeftCount = respondBodyLength - respondBodyRightCount;
							              
							              if (!(respondBodyLeftCount<0)){
							            	  patchDataLeft = responseBodySplit.substring(0, respondBodyLeftCount);
							            	  patchData = "{" + patchDataLeft.replace("<p><b>End Date:</b>", "") + " <p><b>End Date:</b>" + " " + util_constant.cCurrentDateTime + "</p>\",\"sys_updated_on\":\"\"}";
							              }else{
							            	  patchData = "{" + patchDataLeft.replace("<p><b>End Date:</b>", "") + " <p><b>End Date:</b>" + " " + util_constant.cCurrentDateTime + "</p>\",\"sys_updated_on\":\"\"}";
							              }
						              
						              
					              }
				              }
					          
					          
					          try {
					        	  
//					        	  HttpGet httpget1 = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_plan?sysparm_query=number%3D"+sNumber+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=sys_id");
//						          httpget1.setHeader("Accept", "application/json");
//						          System.out.println("\n\n------------------------------------------------");
//						          System.out.println("\nExecuting Request " + httpget1.getRequestLine());
//						          response1 = httpclient.execute(httpget1);
//						          
//					              System.out.println("\n" + response1.getStatusLine());
//					              responseBody1 = EntityUtils.toString(response1.getEntity());
//					              
//					              responseBodyArray1 = responseBody1.split("[{,}]");
//					             
//					              for (String responseBodySplit1 : responseBodyArray1) {
//					            	  if (!(responseBodySplit1.matches(".*result.*") || responseBodySplit1.matches(":\\[") || responseBodySplit1.matches("]") || (responseBodySplit1.matches("")))){
//						                  System.out.println(responseBodySplit1);
//						                  
//						                  if (responseBodySplit1.matches("\"sys_id\":.*")){
//						                	  sysIdString = responseBodySplit1;
//						                	  sysIdArray = sysIdString.split("[\":]");
//						                	  
//						                	  sysIdLoop = 0;
//						                	  for (String sysId : sysIdArray) {
//						                		  
//						                		  if (sysIdLoop==sysIdArray.length-1){  
//						                			HttpPatch httpPatch = new HttpPatch("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_plan/"+sysId+"?sysparm_input_display_value=true");
//						  					 		httpPatch.setHeader("Accept", "application/json");
//						  					 		httpPatch.setHeader("Content-Type", "application/json");
//						  					        HttpEntity entity = new ByteArrayEntity(patchData.getBytes("utf-8"));
//						  					 		httpPatch.setEntity(entity);
//						                			
//						  					 		System.out.println("\n\n------------------------------------------------");
//						  					        System.out.println("\nExecuting request " + httpPatch.getRequestLine());
//						  					        CloseableHttpResponse response = httpclient.execute(httpPatch);
//						  					        
//					                	            System.out.println("\n" + response.getStatusLine());
//						                		  }
//						                		  sysIdLoop ++;
//						                	  }
//						                  }
//					            	  }
//					              }
					        	  
					        	  HttpGet httpget4 = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_plan?sysparm_query=number%3D"+sNumber+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=sys_id");
					        	  httpget4.setHeader("Accept", "application/json");
						          System.out.println("\n\n------------------------------------------------");
						          System.out.println("\nExecuting Request " + httpget4.getRequestLine());
						          response1 = httpclient.execute(httpget4);
						          
					              System.out.println("\n" + response1.getStatusLine());
					              responseBody1 = EntityUtils.toString(response1.getEntity());
					              
					              responseBodyArray1 = responseBody1.split("[{,}]");
					             
					              for (String responseBodySplit1 : responseBodyArray1) {
					            	  if (!(responseBodySplit1.matches(".*result.*") || responseBodySplit1.matches(":\\[") || responseBodySplit1.matches("]") || (responseBodySplit1.matches("")))){
						                  System.out.println(responseBodySplit1);
						                  
						                  if (responseBodySplit1.matches("\"sys_id\":.*")){
						                	  sysIdString = responseBodySplit1;
						                	  sysIdArray = sysIdString.split("[\":]");
						                	  
						                	  sysIdLoop = 0;
						                	  for (String sysId : sysIdArray) {
						                		  
						                		  if (sysIdLoop==sysIdArray.length-1){  
						                			HttpPatch httpPatch = new HttpPatch("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_plan/"+sysId+"?sysparm_input_display_value=true");
						  					 		httpPatch.setHeader("Accept", "application/json");
						  					 		httpPatch.setHeader("Content-Type", "application/json");
						  					        HttpEntity entity = new ByteArrayEntity(patchData.getBytes("utf-8"));
						  					 		httpPatch.setEntity(entity);
						                			
						  					 		System.out.println("\n\n------------------------------------------------");
						  					        System.out.println("\nExecuting request " + httpPatch.getRequestLine());
						  					        CloseableHttpResponse response = httpclient.execute(httpPatch);
						  					        
					                	            System.out.println("\n" + response.getStatusLine());
						                		  }
						                		  sysIdLoop ++;
						                	  }
						                  }
					            	  }
					              }
					          }
					           finally {
					              response.close();
					              response1.close();
					          }
						 }
					      finally {
					          httpclient.close();
					      }
					}
					
					
					
					// sNumber refer to Test Case number show at SNOW UI
					// sExecutionStatus can be Unexecuted, Passed, Failed, Blocked, In progress, Retest
					public void testCaseEndUpdate(String sNumber, String sExecutionStatus) throws HttpException, IOException {	
						System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());					
						
						String patchData = "";
						
						if (sExecutionStatus.toLowerCase().contains("passed")){
							sExecutionStatus = "Passed";
							if (util_constant.cTc_TestCaseFailed == "Y"){
								sExecutionStatus = "Failed";
							}
						}else if (sExecutionStatus.toLowerCase().contains("failed")){
							sExecutionStatus = "Failed";
						}else {
							sExecutionStatus = "In Progress";
						}

						 try {
							 currentDateTime();
							 
							 util_constant.cUtil_ExcelReport.setEndExcelResult(util_constant.cTc_TestCaseName, util_constant.cAct_Result);
							 
							 establishSNOWConnection();
							 
							 HttpGet httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance?sysparm_query=number%3D"+util_constant.cSnowTestCaseNumber+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=prereq");
					          httpget.setHeader("Accept", "application/json");
					          System.out.println("\n\n------------------------------------------------");
					          System.out.println("\nExecuting Request " + httpget.getRequestLine());
					          response = httpclient.execute(httpget);
					          
					          System.out.println("\n" + response.getStatusLine());
				              responseBody = EntityUtils.toString(response.getEntity());
				              
				              responseBodyArray = responseBody.split("[{}]");

				              for (String responseBodySplit : responseBodyArray) {
					              if (!(responseBodySplit.matches(".*result.*") || responseBodySplit.matches(".*].*") || (responseBodySplit.matches("") || responseBodySplit.matches(".*}.*")))){
						          
					            	  System.out.println(responseBodySplit);
					            	  
						              int respondBodyLength = responseBodySplit.length();
						              int respondBodyRightCount = 5; //count for </p>"
						              int respondBodyLeftCount = respondBodyLength - respondBodyRightCount;
						              
						              String patchDataLeft = responseBodySplit.substring(0, respondBodyLeftCount);
						              
						              if (util_constant.cTc_TestCaseFailed == "Y"){
						  				sExecutionStatus = "Failed";
						  			}
						              
						              patchData = "{" + patchDataLeft + " " + util_constant.cCurrentDateTime + "</p>\",\"execution_status\":\""+sExecutionStatus+"\",\"sys_updated_on\":\"\"}";
					              }
				              }
					          
					          
					          try {
					        	  
					        	  HttpGet httpget1 = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance?sysparm_query=number%3D"+util_constant.cSnowTestCaseNumber+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=sys_id");
						          httpget1.setHeader("Accept", "application/json");
						          System.out.println("\n\n------------------------------------------------");
						          System.out.println("\nExecuting Request " + httpget1.getRequestLine());
						          response1 = httpclient.execute(httpget1);
						          
					              System.out.println("\n" + response1.getStatusLine());
					              responseBody1 = EntityUtils.toString(response1.getEntity());
					              
					              responseBodyArray1 = responseBody1.split("[{,}]");
					             
					              for (String responseBodySplit1 : responseBodyArray1) {
					            	  if (!(responseBodySplit1.matches(".*result.*") || responseBodySplit1.matches(":\\[") || responseBodySplit1.matches("]") || (responseBodySplit1.matches("")))){
						                  System.out.println(responseBodySplit1);
						                  
						                  if (responseBodySplit1.matches("\"sys_id\":.*")){
						                	  sysIdString = responseBodySplit1;
						                	  sysIdArray = sysIdString.split("[\":]");
						                	  
						                	  sysIdLoop = 0;
						                	  for (String sysId : sysIdArray) {
						                		  
						                		  if (sysIdLoop==sysIdArray.length-1){  
						                			HttpPatch httpPatch = new HttpPatch("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance/"+sysId+"?sysparm_input_display_value=true");
						  					 		httpPatch.setHeader("Accept", "application/json");
						  					 		httpPatch.setHeader("Content-Type", "application/json");
						  					        HttpEntity entity = new ByteArrayEntity(patchData.getBytes("utf-8"));
						  					 		httpPatch.setEntity(entity);
						                			
						  					 		System.out.println("\n\n------------------------------------------------");
						  					        System.out.println("\nExecuting request " + httpPatch.getRequestLine());
						  					        CloseableHttpResponse response = httpclient.execute(httpPatch);
						  					        
					                	            System.out.println("\n" + response.getStatusLine());
						                		  }
						                		  sysIdLoop ++;
						                	  }
						                  }
					            	  }
					              }
					              
					             
									 
						          HttpGet httpget5 = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance?sysparm_query=number%3D"+util_constant.cSnowTestCaseNumber+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=parent.number");
						          httpget5.setHeader("Accept", "application/json");
						          System.out.println("\n\n------------------------------------------------");
						          System.out.println("\nExecuting Request " + httpget5.getRequestLine());
						          response = httpclient.execute(httpget5);
						          
					              System.out.println("\n" + response.getStatusLine());
					              responseBody = EntityUtils.toString(response.getEntity());
					              
					              responseBodyArray = responseBody.split("[{,:}]");
								 
					              for (String responseBodySplit : responseBodyArray) {
					            	  if (responseBodySplit.contains("TMTP")){
					            		  sParent = responseBodySplit.toString().trim();
					            		  sParent = sParent.replace("\"", "");
					            	  }
					              }
					              
					              testPlanEndUpdate(sParent);
					              
					              
					              
					          }
					           finally {
					              response.close();
					              response1.close();
					          }
						 }
					      finally {
					          httpclient.close();
					      }
					}
					
					
					
					// sNumber refer to Test Plan number show at SNOW UI
					public void calculateTotalExecutionDuration(String sNumber) throws HttpException, IOException {	
						System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());					
						
						
						
						 try {
							 establishSNOWConnection();	
							 
							 HttpGet httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_plan?sysparm_query=number%3D"+sNumber+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=instructions");
					          httpget.setHeader("Accept", "application/json");
					          System.out.println("\n\n------------------------------------------------");
					          System.out.println("\nExecuting Request " + httpget.getRequestLine());
					          response = httpclient.execute(httpget);
					          
					          System.out.println("\n" + response.getStatusLine());
				              responseBody = EntityUtils.toString(response.getEntity());
				              
				              responseBodyArray = responseBody.split("<b>Start Date:</b> |<b>End Date:</b>");
				              
				              forLoopCount = 0;
				              for (String responseBodySplit : responseBodyArray) {
					              if (!responseBodySplit.matches(".*result.*")){
						              
					            	  responseBodySplit = responseBodySplit.trim();
						              String patchDataLeft = responseBodySplit.substring(0, 20);
						              
						              System.out.println(responseBodySplit);
						              
						              if (forLoopCount==1){
						            	  sTPStartTime = patchDataLeft;
						              }
						              

						              if (forLoopCount==2){
						            	  sTPEndTime = patchDataLeft;
						              }
					              }
					              forLoopCount ++;
				              }
				              
				              
				            try{
				                  startDate = new SimpleDateFormat("dd-MMM-yyy hh:mm:ss").parse(sTPStartTime);
				                  endDate = new SimpleDateFormat("dd-MMM-yyy hh:mm:ss").parse(sTPEndTime);
				            } catch (Exception e) {
				                e.printStackTrace();
				            }
				            
							long executionDuration = endDate.getTime() - startDate.getTime();
							long executionDurationHours = TimeUnit.MILLISECONDS.toHours(executionDuration);
							long executionDurationMinutes = TimeUnit.MILLISECONDS.toMinutes(executionDuration) - TimeUnit.HOURS.toMinutes(executionDurationHours);
							long executionDurationSeconds = TimeUnit.MILLISECONDS.toSeconds(executionDuration) - TimeUnit.MINUTES.toSeconds(executionDurationMinutes) - TimeUnit.HOURS.toSeconds(executionDurationHours);
							
							sExecutionDuration = executionDurationHours+" Hours "+executionDurationMinutes+" Minutes "+executionDurationSeconds+" Seconds ";
							
							System.out.println(executionDurationHours+" Hours "+executionDurationMinutes+" Minutes "+executionDurationSeconds+" Seconds ");

						 }
			           finally {
			              response.close();
			          }
						 
					}
					
					
					
					// sNumber refer to Test Plan number show at SNOW UI
					public void getTestResults(String sNumber) throws HttpException, IOException {	
						System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
						
						 try {
							 establishSNOWConnection();
							 
					          HttpGet httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance?sysparm_query=parent.number%3D"+sNumber+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=execution_status");
					          httpget.setHeader("Accept", "application/json");
					          System.out.println("\n\n------------------------------------------------");
					          System.out.println("\nExecuting Request " + httpget.getRequestLine());
					          response = httpclient.execute(httpget);
					          
					          try {
					              System.out.println("\n" + response.getStatusLine());
					              responseBody = EntityUtils.toString(response.getEntity());
					              
					              responseBodyArray = responseBody.split("[{,}]");
					              
					              for (String responseBodySplit : responseBodyArray) {
					            	  if (!(responseBodySplit.matches(".*result.*") || responseBodySplit.matches(":\\[") || responseBodySplit.matches("]") || (responseBodySplit.matches("")))){
						                  System.out.println(responseBodySplit);
						                  
						                  if (responseBodySplit.matches("\"execution_status\":.*")){
						                	  executionStatusString = responseBodySplit;
						                	  executionStatusArray = executionStatusString.split("[\":]");
						                	  
						                	  forLoopCount = 0;
						                	  numberLoop = 0;
						                	  for (String executionStatus : executionStatusArray) {
						                		  if (forLoopCount==executionStatusArray.length-1){
						                			  if (executionStatus.toLowerCase().equals("passed")){
						                				  passedCount = passedCount + 1;
						                			  }
						                			  else if (executionStatus.toLowerCase().equals("failed")){
						                				  failedCount = failedCount + 1;
						                			  }
						                		  }
						                		  forLoopCount ++;
						                	  }
						                  }
					            	  }
					              }
					          } finally {
					              response.close();
					          }
					      } finally {
					          httpclient.close();
					      }
					}
					
					
					
					// sNumber refer to Test Plan number show at SNOW UI
					public void getTotalTestExecuted(String sNumber) throws HttpException, IOException {	
						System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
						
						 try {
							 establishSNOWConnection();
							 
					          HttpGet httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance?sysparm_query=parent.number%3D"+sNumber+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=execution_status");
					          httpget.setHeader("Accept", "application/json");
					          System.out.println("\n\n------------------------------------------------");
					          System.out.println("\nExecuting Request " + httpget.getRequestLine());
					          response = httpclient.execute(httpget);
					          
					          try {
					              System.out.println("\n" + response.getStatusLine());
					              responseBody = EntityUtils.toString(response.getEntity());
					              
					              responseBodyArray = responseBody.split("[{,}]");
					              
					              totalTestCasesCount = 0;
					              
					              for (String responseBodySplit : responseBodyArray) {
					            	  if (!(responseBodySplit.matches(".*result.*") || responseBodySplit.matches(":\\[") || responseBodySplit.matches("]") || (responseBodySplit.matches("")))){
						                  System.out.println(responseBodySplit);
						                  
						                  if (responseBodySplit.matches("\"execution_status\":.*")){
						                	  executionStatusString = responseBodySplit;
						                	  executionStatusArray = executionStatusString.split("[\":]");
						                	  
						                	  forLoopCount = 0;
						                	  numberLoop = 0;
						                	  for (String executionStatus : executionStatusArray) {
						                		  if (forLoopCount==executionStatusArray.length-1){
						                			  if (!executionStatus.toLowerCase().equals("unexecuted")){
						                				  totalExecutionCount = totalExecutionCount + 1;
						                			  }
						                			  totalTestCasesCount ++;
						                		  }
						                		  forLoopCount ++;

						                	  }
						                  }
					            	  }
					              }
					          } finally {
					        	  System.out.println("Total Test Cases: " + totalTestCasesCount);
					        	  System.out.println("Total Test Cases Executed: " + totalExecutionCount);
					        	  
					        	  totalTestCasesSkipped = totalTestCasesCount - totalExecutionCount;
					        	  
					        	  System.out.println("Total Test Cases Passed: " + passedCount);
	                			  System.out.println("Total Test Cases Failed: " + failedCount);
					        	  System.out.println("Total Test Cases Unexecuted: " + totalTestCasesSkipped);
					              response.close();
					          }
					      } finally {
					          httpclient.close();
					      }
					}
					
					
					
					// sParent refer to Test Plan number show at SNOW UI
					public void getTestCasesExecutionDetail(String sParent) throws HttpException, IOException {		
						System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
						
						 try {
							 establishSNOWConnection();
							 
					          HttpGet httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance?sysparm_query=parent.number%3D"+sParent+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=prereq");
					          httpget.setHeader("Accept", "application/json");
					          System.out.println("\n\n------------------------------------------------");
					          System.out.println("\nExecuting Request " + httpget.getRequestLine());
					          response = httpclient.execute(httpget);
					          
					          System.out.println("\n" + response.getStatusLine());
				              responseBody = EntityUtils.toString(response.getEntity());
				              
				              responseBodyArray = responseBody.split("[{]");
				              
				              
				              for (String responseBodySplit : responseBodyArray) {
					              if (!responseBodySplit.matches(".*result.*")){
					            	  if (responseBodySplit.matches(".*prereq.*") && responseBodySplit.matches(".*Date.*")){
					            	  responseBodyArray2 = responseBodySplit.split("<b>Start Date:</b> |<b>End Date: </b>");
					            	  
					            	  forLoopCount = 0;
					            	  	for (String responseBodySplit2 : responseBodyArray2) {
						              
						            	  if (!responseBodySplit2.matches(".*prereq.*")){
						            		  responseBodySplit2 = responseBodySplit2.trim();
								              String patchDataLeft = responseBodySplit2.substring(0, 20);
								              
								              System.out.println(responseBodySplit2);
								              
								              if (forLoopCount==1){
								            	  sStartTime = patchDataLeft;
								            	  System.out.println("sStartTime " + sStartTime);
								              }
								              
		
								              if (forLoopCount==2){
								            	  sEndTime = patchDataLeft;
								            	  System.out.println("sEndTime " + sEndTime);
								              }
						            	  }
						            forLoopCount ++;
					              }
					              }
				              }
				             }
					          
					          
					          
					         
					          } finally {
					              response.close();
					          }
				}
					
					
						
					// sParent refer to Test Plan number show at SNOW UI
					public void sendHtmlEmail(String sParent) throws AddressException,MessagingException, HttpException, IOException {
						
						
						
						System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
					
				        int i;
				        sStartTime = "";
				        sEndTime = "";
				        
						getTestCasesExecutionDetail(sParent);
						
						getTotalTestExecuted(sParent);
						
						getTestResults(sParent);
						
						calculateTotalExecutionDuration(sParent);
						
						
						establishSNOWConnection();						
				        
				        try{ 
					         Properties p = new Properties();
							 File currentDirectory = new File(new File(".").getAbsolutePath());
							 p.load(new FileInputStream(util_constant.cPropertiesINI));
							
							 String sReportName = p.getProperty("sexcelreportname");
							 String sHost = p.getProperty("sHost");
							 String sPort = p.getProperty("sPort");
							 String sMailFrom = p.getProperty("sMailFrom");
							 String sMailToAddress = p.getProperty("sMailToAddress");
							 final String sUserName = p.getProperty("sUserName");
							 
							 String sEncPassword = p.getProperty("sPassword");
							 final String sPassword = util_passwordmgr.decrypt(sEncPassword);
							 
							 
//					          HttpGet httpget1 = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance?sysparm_query=parent.number%3D"+sParent+"%5EORDERBYnumber&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=prereq");
//					          httpget1.setHeader("Accept", "application/json");
//					          System.out.println("\n\n------------------------------------------------");
//					          System.out.println("\nExecuting Request " + httpget1.getRequestLine());
//					          response = httpclient.execute(httpget1);
//					          
//					          System.out.println("\n" + response.getStatusLine());
//				              responseBody = EntityUtils.toString(response.getEntity());
//				              
//				              String in = responseBody;
//			            	  int matchCount = 0;
//			            	  Pattern prereqPatter = Pattern.compile("prereq");
//			            	  Matcher match = prereqPatter.matcher( in );
//			            	  while (match.find()) {
//			            		  matchCount++;
//			            	  }
//				              
//				              responseBodyArray = responseBody.split("[{]");
//				              
//				              preReqLoop = 0;
//				              for (String responseBodySplit : responseBodyArray) {
//					              if (!responseBodySplit.matches(".*result.*")){
//					            	  if (responseBodySplit.matches(".*prereq.*") ){
//					            		  if (responseBodySplit.matches(".*Date.*")){
//							            	  responseBodyArray2 = responseBodySplit.split("<b>Start Date:</b> |<b>End Date: </b>");
//							            	  
//							            	  forLoopCount = 0;
//							            	  	for (String responseBodySplit2 : responseBodyArray2) {
//								              
//								            	  if (!responseBodySplit2.matches(".*prereq.*")){
//								            		  if (!responseBodySplit2.isEmpty()){
//								            		  responseBodySplit2 = responseBodySplit2.trim();
//										              
//										              
//										            
//										              if (!(responseBodySplit2.trim().matches(".*Unexecuted.*")||responseBodySplit2.trim().matches(".*Failed.*")||responseBodySplit2.trim().matches(".*Passed.*"))){
//										            	 String patchDataLeft = responseBodySplit2.substring(0, 20);
//										            	  
//										            	 
//										            	  if (forLoopCount==1){
//											            	  sStartTime = patchDataLeft;
//											            	  System.out.println("sStartTime " + sStartTime);
//											            	  
//											            	  if (sFirstTestCase.equalsIgnoreCase("Y")&&!patchDataLeft.isEmpty()){
//											            		 
//											            		  sTPStartTime = sStartTime;
//											            		  sFirstTestCase = "N";
//
//											            	  }
//											              }
//											              
//					
//											              if (forLoopCount==2){
//											            	  sEndTime = patchDataLeft;
//											            	  System.out.println("sEndTime " + sEndTime);
//											            	  
//											            	  if (preReqLoop==matchCount+1){
//											            		  sTPEndTime = sEndTime;
//											            		  sLastTestCase = "Y";
//											            	  }
//											              } 
//										              }
//										             
//								            	  }
//								            	  }
//								            forLoopCount ++;
//							              }
//					              }
//					            	  }
//					              }
//					              preReqLoop ++;
//				              }
//					            		  
//				              try{
//				                  startDate = new SimpleDateFormat("dd-MMM-yyy hh:mm:ss").parse(sTPStartTime);
//				                  
//				                  if (sTPEndTime==null){
//				                	  sTPEndTime = sEndTime;
//				                  };
//				                  endDate = new SimpleDateFormat("dd-MMM-yyy hh:mm:ss").parse(sTPEndTime);
//				            } catch (Exception e) {
//				                e.printStackTrace();
//				            }
//				            
//							long executionDuration = endDate.getTime() - startDate.getTime();
//							long executionDurationHours = TimeUnit.MILLISECONDS.toHours(executionDuration);
//							long executionDurationMinutes = TimeUnit.MILLISECONDS.toMinutes(executionDuration) - TimeUnit.HOURS.toMinutes(executionDurationHours);
//							long executionDurationSeconds = TimeUnit.MILLISECONDS.toSeconds(executionDuration) - TimeUnit.MINUTES.toSeconds(executionDurationMinutes) - TimeUnit.HOURS.toSeconds(executionDurationHours);
//							
//							sExecutionDuration = executionDurationHours+" Hours "+executionDurationMinutes+" Minutes "+executionDurationSeconds+" Seconds ";
							
					     // outgoing message information
					        String subject = "Test_Automation [" +sReportName+ "] - Regression Test Automation Summary";

							
							StringBuilder buf = new StringBuilder();
										
							buf.append("<META HTTP-EQUIV='Content-Type' CONTENT='text/html; charset=iso-8859-1'>" +
									 "<HTML>" +
									 "<HEAD>" +
										 "<TITLE>QC TestSet Execution - Custom Report</TITLE>" +
										 
										 "<META content='MSHTML 6.00.2800.1106'>" +
									 "</HEAD>" +
									 "<body leftmargin='0' marginheight='0' marginwidth='0' topmargin='0'>" +
										 "<table width='100%' border='0' cellspacing='0' cellpadding='0'>" +
											 "<tr>" +
												 "<td></td>" +
											 "</tr>" +
											 "<tr>" +
												 "<td class='rest'>" +
													 "<table class='space' width='100%' border='0' cellspacing='0' cellpadding='0'>" +
														 "<tr>" +
														 	"<td></td>" +
														 "</tr>" +
													 "</table>" +
													 "<table class='textfont' cellspacing='0' cellpadding='0' width='100%' align='center' border='0' >" +
													 	"<tbody>" +
															 "<tr>" +
																 "<th class='hl' align='left' >"+sReportName+"</th>" +
															 "</tr>" +
															 "<tr>" +
																 "<th class='space' align='left'></th>" +
															 "</tr>" +
															 "<tr>" +
																 "<th class='space' align='left'></th>" +
															 "</tr>" +
														 "</tbody>" +
													 "</table>" +
													 "<table class='textfont' cellspacing='0' cellpadding='0' width='100%' align='center' border='0'>" +
														 "<tbody>" +
															 "<tr>" +
																 "<td height='10'>" +
																 "</td>" +
															 "</tr>" +
														 "</tbody>" +
													 "</table>" +
													 "<table class='textfont' cellspacing='0' cellpadding='0' width='100%' align='center' border='0'>" +
														 "<tbody>" +
															 "<tr>" +
																 "<td>" +
																	 "<table class='textfont' cellspacing='0' cellpadding='0' width='100%' align='center' border='0'>" +
																		 "<tbody>" +
																			 "<tr>" +
																				 "<td height='1'></td>" +
																			 "</tr>" +
																			 "<tr>" +
																				 "<td class='chl' style='width:17%'>Total</td>" +
																				 "<td class='chl' style='width:17%'>Passed</td>" +
																				 "<td class='chl' style='width:17%'>Failed</td>" +
																				 "<td class='chl' style='width:17%'>Unexecuted</td>" +
																			 "</tr>" +
																			 "<tr>" +
																				 "<td class='ctext' style='width:17%'>"+totalTestCasesCount+"</td>" +
																				 "<td class='ctext' style='width:17%'>"+passedCount+"</td>" +
																				 "<td class='ctext' style='width:17%'>"+failedCount+"</td>" +
																				 "<td class='ctext' style='width:17%'>"+totalTestCasesSkipped+"</td>" +
																			 "</tr>" +
																		 "</tbody>" +
																	 "</table>" +
																 "</td>" +
															 "</tr>" +
															 "<tr>" +
																 "<td class='space'></td>" +
															 "</tr>" +
														 "</tbody>" +
													 "</table>" +
													 "<table class='textfont' cellspacing='0' cellpadding='0' width='100%' align='center' border='0'>" +
														 "<tbody>" +
															 "<tr>" +
																 "<td>" +
																	 "<table class='textfont' cellspacing='0' cellpadding='0' width='100%' align='center' border='0'>" +
																		 "<tbody>" +
																		 
																			"<tr>" +
																				 "<td width='20%'>Project Name</td>" +
																				 "<td class='ctext'>"+sReportName+"</td>" +
																			 "</tr>" +
																			 "<tr>" +
																				 "<td width='20%'>Started</td>" +
																				 "<td class='ctext'>"+sTPStartTime+"</td>" +
																			 "</tr>" +
																			 "<tr>" +
																			 	"<td width='20%'>Finished</td>" +
																			 	"<td class='ctext'>"+sTPEndTime+"</td>" +
																			 "</tr>" +
																			 "<tr>" +
																				 "<td width='20%'>Duration</td>" +
																				 "<td class='ctext'>"+sExecutionDuration+"</td>" +
																			 "</tr>" +
																			 "<tr>" +
																				 "<td width='20%'>Contact Person Email</td>" +
																				 "<td class='ctext'>"+sMailFrom+"</td>" +
																		 	"</tr>" +
																		 "</tbody>" +
																	 "</table>" +
																 "</td>" +
															 "</tr>" +
															 "<tr>" +
																 "<td class='space'></td>" +
															 "</tr>" +
														 "</tbody>" +
													 "</table>" +
													 "<table class='textfont' cellspacing='0' cellpadding='0' width='100%' align='center' border='0'>" +
														 "<tbody>" +
															 "<tr>" +
																 "<th class='hl' align='left'></th>" +
															 "</tr>" +
														 "</tbody>" +
													 "</table>" +
													 "<table class='textfont' cellspacing='0' cellpadding='0' width='100%' align='center' border='0'>" +
														 "<tbody>" +
															 "<tr>" +
																 "<th class='space' align='left' height='10'></th>" +
															 "</tr>" +
															 "<tr>" +
																 "<th class='hl' align='left'>Executed Test Case(s) Details</th>" +
															 "</tr>" +
															 "<tr>" +
																 "<th class='space' align='left'></th>" +
															 "</tr>" +
														 "</tbody>" +
													 "</table>" +
													 "<table class='textfont' cellspacing='0' cellpadding='0' width='100%' align='center' border='0'>" +
														 "<tbody>" +
														 "<tr>" +
														 "<td style='font-size: 10px; font-weight: bold; background-color: #eee; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>Test</td>" +
														 "<td style='font-size: 10px; font-weight: bold; background-color: #eee; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>Status</td>" +
														 "<td style='font-size: 10px; font-weight: bold; background-color: #eee; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>Exec Start Time</td>" +
														 "<td style='font-size: 10px; font-weight: bold; background-color: #eee; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>Exec End Time</td>" +
													 "</tr>") ;
							
							
							 try {
						          HttpGet httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance?sysparm_query=parent.number%3D"+sParent+"%5EORDERBYnumber&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=prereq%2Cexecution_status%2Cshort_description");
						          httpget.setHeader("Accept", "application/json");
						          System.out.println("\n\n------------------------------------------------");
						          System.out.println("\nExecuting Request " + httpget.getRequestLine());
						          response = httpclient.execute(httpget);
						          
						          System.out.println("\n" + response.getStatusLine());
					              responseBody = EntityUtils.toString(response.getEntity());
					              
					              responseBodyArray = responseBody.split("[{]");
					              
					              
					              for (String responseBodySplit : responseBodyArray) {
						              if (!responseBodySplit.matches(".*result.*")){
						            	  if (responseBodySplit.matches(".*prereq.*") ){
						            		  if (responseBodySplit.matches(".*Date.*")){
								            	  responseBodyArray2 = responseBodySplit.split("<b>Start Date:</b> |<b>End Date: </b> |,|\"execution_status\":|\"short_description\":");
								            	  
								            	  forLoopCount = 0;
								            	  	for (String responseBodySplit2 : responseBodyArray2) {
									              
									            	  if (!responseBodySplit2.matches(".*prereq.*")){
									            		  if (!responseBodySplit2.isEmpty()){
									            		  responseBodySplit2 = responseBodySplit2.trim();
											              
											              
											              if (forLoopCount==1){
											            	  sShortDesc = responseBodySplit2.trim().replace("\"", "");
											            	  System.out.println("sShortDesc " + sShortDesc);
											              } else if (forLoopCount==3){
											            	  sExecutionStatus = responseBodySplit2.trim().replace("\"", "");
											            	  System.out.println("sExecutionStatus " + sExecutionStatus);
											              }
											              
											              if (!(responseBodySplit2.trim().matches(".*Unexecuted.*")||responseBodySplit2.trim().matches(".*Failed.*")||responseBodySplit2.trim().matches(".*Passed.*")||forLoopCount==1)){
											            	 String patchDataLeft = responseBodySplit2.substring(0, 20);
											            	  
											            	  if (forLoopCount==5){
											            		  if (!sExecutionStatus.trim().equalsIgnoreCase(".*unexecuted.*")){
													            	  sStartTime = patchDataLeft;
													            	  System.out.println("sStartTime " + sStartTime);
											            		  }
												              }
												              
						
												              if (forLoopCount==6){
												            	  if (!sExecutionStatus.trim().equalsIgnoreCase(".*unexecuted.*")){
													            	  sEndTime = patchDataLeft;
													            	  System.out.println("sEndTime " + sEndTime);
												            	  }
												              } 
											              }
											             
									            	  }
									            	  }
									            forLoopCount ++;
								              }
						              }else{
						            	  if (!responseBodySplit.matches(".*Date.*")){
							            	  sStartTime = "";

							            	  sEndTime = "";
							              } 
						            	  
						            	  responseBodyArray2 = responseBodySplit.split("\"execution_status\":|\"short_description\":|,");
						            	  
						            	  forLoopCount = 0;
						            	  	for (String responseBodySplit2 : responseBodyArray2) {
						            	  		
							            	  if (!responseBodySplit2.matches(".*prereq.*")){
							            		  if (!responseBodySplit2.isEmpty()){
							            		  responseBodySplit2 = responseBodySplit2.trim();
									              
									              
									              if (forLoopCount==1){
									            	  sShortDesc = responseBodySplit2.trim();
									            	  System.out.println("sShortDesc " + sShortDesc);
									              } else if (forLoopCount==3){
									            	  sExecutionStatus = responseBodySplit2.trim();
									            	  System.out.println("sExecutionStatus " + sExecutionStatus);
									              }
									              

									              
									              }
									             
							            	  }
							            	  	forLoopCount ++;
						            	  }
						            	  	
							            
						              }
						            		  if (sExecutionStatus.equalsIgnoreCase("passed")){
													buf.append("<tr><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
												       .append(sShortDesc)
												       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white; color: limegreen;'>")
												       .append("Passed")
												       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
												       .append(sStartTime)
												       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
												       .append(sEndTime)
												       .append("</td></tr>");
												}
												else if(sExecutionStatus.equalsIgnoreCase("failed")){
													buf.append("<tr><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
												       .append(sShortDesc)
												       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white; color: red;'>")
												       .append("Failed")
												       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
												       .append(sStartTime)
												       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
												       .append(sEndTime)
												       .append("</td></tr>");
												}
												else if(sExecutionStatus.equalsIgnoreCase("unexecuted")){
													buf.append("<tr><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
												       .append(sShortDesc)
												       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
												       .append(sExecutionStatus)
												       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
												       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
												       .append("</td></tr>");
												}
												else{
													buf.append("<tr><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
												       .append(sShortDesc)
												       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
												       .append(sExecutionStatus)
												       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
												       .append(sStartTime)
												       .append("</td><td style='font-size: 10px; padding-right: 5px; padding-left: 5px; height: 20px; border-bottom: 1px solid white;'>")
												       .append(sEndTime)
												       .append("</td></tr>");
												}
						              }
						            } 
					              }
					             
						          
						          } finally {
						              response.close();
						          }
							
							
							buf.append( "</tbody>" +
										 "</table>" +
										 	"</td>" +
										 		"</tr>" +
										 			"</table>" +
										 				"</body>" +
										 					"</HTML>");
							String message = buf.toString();
							
							
					        // sets SMTP server properties
					        Properties properties = new Properties();
					        properties.put("mail.smtp.host", sHost);
					        properties.put("mail.smtp.port", sPort);
					        properties.put("mail.smtp.auth", "true");
					        properties.put("mail.smtp.starttls.enable", "false");
					 
					        
					        // creates a new session with an authenticator
					        Authenticator auth = new Authenticator() {
					        public PasswordAuthentication getPasswordAuthentication() {
					                return new PasswordAuthentication(sUserName, sPassword);
					            }
					        };
					        
						        
						        Session session = Session.getInstance(properties, auth);
						 
							        // creates a new e-mail message
							        Message msg = new MimeMessage(session);
							 
							        msg.setFrom(new InternetAddress(sUserName));
							        
							        String[] recipientList = sMailToAddress.split(",");
							        InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
							        int counter = 0;
							        for (String recipient : recipientList) {
							            recipientAddress[counter] = new InternetAddress(recipient.trim());
							            counter++;
							        }
							        
							        msg.setRecipients(Message.RecipientType.TO, recipientAddress);
							        msg.setSubject(subject);
							        msg.setSentDate(new Date());
							        // set plain text message
							        msg.setContent(message, "text/html");
							 
							        // sends the e-mail
							        Transport.send(msg);
							        
									JOptionPane.showMessageDialog(frame, "Email successfully sent to "+sMailToAddress);
									
							       System.out.println("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Passed]");
							}
							catch (Exception e)
							{
								System.out.println("Executed Method: " + new Object() {}.getClass().getEnclosingMethod().getName() + " - [Failed] - " + e.getMessage());
							}
				 
				 
				 
				 }
					
					
					
					// sSnowTestPlanTemplate refer to sSnowTestPlanTemplate at properties.ini
					public void createTestPlan(String sSnowTestPlanTemplate) throws HttpException, IOException{
						System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
						
						if (util_constant.cSnowRerun.equals("N")){
							 try {
								 
									 establishSNOWConnection();
									 
							          HttpGet httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_plan?sysparm_query=number%3D"+sSnowTestPlanTemplate+"&sysparm_display_value=true&sysparm_exclude_reference_link=true&sysparm_fields=short_description");
							          httpget.setHeader("Accept", "application/json");
							          System.out.println("\n\n------------------------------------------------");
							          System.out.println("\nExecuting Request " + httpget.getRequestLine());
							          response = httpclient.execute(httpget);
							          
							          try {
							              System.out.println("\n" + response.getStatusLine());
							              responseBody = EntityUtils.toString(response.getEntity());
							              
							              responseBodyArray = responseBody.split("[{,}]");
							             
							              for (String responseBodySplit : responseBodyArray) {
							            	  if (!(responseBodySplit.matches(".*result.*") || responseBodySplit.matches(":\\[") || responseBodySplit.matches("]") || (responseBodySplit.matches("")))){
								                  System.out.println(responseBodySplit);
								                  
								                  if (responseBodySplit.matches("\"short_description\":.*")){
								                	  shortDescriptionString = responseBodySplit;
								                	  shortDescriptionArray = shortDescriptionString.split("[\":]");
								                	  
								                	  shortDescriptionLoop = 0;
								                	  for (String shortDescription : shortDescriptionArray) {
								                		  
								                		  if (shortDescriptionLoop==shortDescriptionArray.length-1){  
								                			
								                			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
							                				Date date = new Date();
							                				util_constant.cCurrentDateTime = date;
								                				
								                			String postData = "{\"short_description\":\""+shortDescription+" ("+dateFormat.format(util_constant.cCurrentDateTime).toString().toUpperCase()+") \"}";
								                			  
								                			HttpPost httpPost = new HttpPost("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_plan");
													 		httpPost.setHeader("Accept", "application/json");
													 		httpPost.setHeader("Content-Type", "application/json");
													        HttpEntity entity = new ByteArrayEntity(postData.getBytes("utf-8"));
													 		httpPost.setEntity(entity);
														    
														 	System.out.println("\n\n------------------------------------------------");	
													        System.out.println("\nExecuting Request " + httpPost.getRequestLine());
													        CloseableHttpResponse response = httpclient.execute(httpPost);
		
													        System.out.println("\n" + response.getStatusLine());
													        responseBody = EntityUtils.toString(response.getEntity());
													        
													        responseBodyArray = responseBody.split("[{,}]");
												             
												              for (String responseBodySplit2 : responseBodyArray) {
												            	  if (!(responseBodySplit2.matches(".*result.*") || responseBodySplit2.matches(":\\[") || responseBodySplit2.matches("]") || (responseBodySplit2.matches("")))){
													                  System.out.println(responseBodySplit2);
													                  
													                  if (responseBodySplit2.matches("\"number\":.*")){
													                	  numberString = responseBodySplit2;
													                	  numberArray = numberString.split("[\":]");
													                	  
													                	  numberLoop = 0;
													                	  for (String number : numberArray) {
													                		  if (numberLoop==numberArray.length-1){
													                			  
													                			  updateIniFile(util_constant.cPropertiesINI, "sSnowTestPlanNumber",number);
													                			  
													                			  createTestPlanHistory(shortDescription);
													                			  
													                			  	util_constant.utilSNOWIntegration.getPropertiesIni(sPropertiesINIPath);
													                				util_constant.utilSNOWIntegration.testPlanReset(util_constant.cSnowTestPlanNumber);
													                			  }
													                		  numberLoop++;  
													                	  }
													                  }
												            	  }
												              }   
								                	  }
								                		  shortDescriptionLoop ++;
								                  }
							            	  }
							              }
							          } 
						         }
									finally {
								              response.close();
								          }
								
						      } finally {
						          httpclient.close();
						      }
						 }
					 }
					
					
					
						// shortDescription refer to short_description for test plan
						public void createTestPlanHistory(String shortDescription) throws HttpException, IOException{
							System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
							
							 if (util_constant.cSnowRerun.equals("N")){
								 try {
									 DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
									 
		                			String postData = "{\"short_description\":\""+shortDescription+" ("+dateFormat.format(util_constant.cCurrentDateTime).toString().toUpperCase()+") - HISTORY\"}";
		                			  
		                			HttpPost httpPost = new HttpPost("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_plan");
							 		httpPost.setHeader("Accept", "application/json");
							 		httpPost.setHeader("Content-Type", "application/json");
							        HttpEntity entity = new ByteArrayEntity(postData.getBytes("utf-8"));
							 		httpPost.setEntity(entity);
								    
								 	System.out.println("\n\n------------------------------------------------");	
							        System.out.println("\nExecuting Request " + httpPost.getRequestLine());
							        CloseableHttpResponse response = httpclient.execute(httpPost);

							        System.out.println("\n" + response.getStatusLine());
							        responseBody = EntityUtils.toString(response.getEntity());
							        
							        responseBodyArray = responseBody.split("[{,}]");
						             
						              for (String responseBodySplit2 : responseBodyArray) {
						            	  if (!(responseBodySplit2.matches(".*result.*") || responseBodySplit2.matches(":\\[") || responseBodySplit2.matches("]") || (responseBodySplit2.matches("")))){
							                  System.out.println(responseBodySplit2);
							                  
							                  if (responseBodySplit2.matches("\"number\":.*")){
							                	  numberString = responseBodySplit2;
							                	  numberArray = numberString.split("[\":]");
							                	  
							                	  numberLoop = 0;
							                	  for (String number : numberArray) {
							                		  if (numberLoop==numberArray.length-1){
							                			  
							                			  updateIniFile(util_constant.cPropertiesINI, "sSnowTestPlanHistoryNumber",number);											                		  }
							                		  
							                		  numberLoop++;  
							                	  }
							                  }
						            	  }
						              }   
		                	  }
								finally {
							              response.close();
							              httpclient.close();
							          }
							 }
								
						 }
						
							
						public void updateIniFile(String sFilePath, String sKey, String sNumber) throws IOException{
							
							System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
							
							BufferedReader  br = new BufferedReader(new FileReader(new File(sFilePath)));
								
							 StringBuilder sb = new StringBuilder();
						        String line = br.readLine();
								    try {								        
								        counter=1;
								        while (line != null) {
								        	if (line.contains(sKey)){			
								        		 String content = new String();
							        	        String editedContent = new String();
							        	        content = readFile(sFilePath);
							        	        editedContent = editLineInContent(content, sKey +" = "+sNumber +"\n", counter);
							        	        writeToFile(sFilePath, editedContent);
								        		
								        	}
								        	line = br.readLine();
								            counter++;
								        }
								    } finally {
								    	br = null;
								    }
						}
						
						
							public void changeALineInATextFile(String fileName, String newLine, int lineNumber) {
								System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
								
						        String content = new String();
						        String editedContent = new String();
						        content = readFile(fileName);
						        editedContent = editLineInContent(content, newLine, lineNumber);
						        writeToFile(fileName, editedContent);
	
						    }
	
								private static int numberOfLinesInFile(String content) {
									System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
									
								    int numberOfLines = 0;
								    int index = 0;
								    int lastIndex = 0;
			
								    lastIndex = content.length() - 1;
			
								    while (true) {
			
								        if (content.charAt(index) == '\n') {
								            numberOfLines++;
			
								        }
			
								        if (index == lastIndex) {
								            numberOfLines = numberOfLines + 1;
								            break;
								        }
								        index++;
			
								    }
			
								    return numberOfLines;
								}
			
									private static String[] turnFileIntoArrayOfStrings(String content, int lines) {
										System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
										
									    String[] array = new String[lines];
									    int index = 0;
									    int tempInt = 0;
									    int startIndext = 0;
									    int lastIndex = content.length() - 1;
				
									    while (true) {
				
									        if (content.charAt(index) == '\n') {
									            tempInt++;
				
									            String temp2 = new String();
									            for (int i = 0; i < index - startIndext; i++) {
									                temp2 += content.charAt(startIndext + i);
									            }
									            startIndext = index;
									            array[tempInt - 1] = temp2;
				
									        }
				
									        if (index == lastIndex) {
				
									            tempInt++;
				
									            String temp2 = new String();
									            for (int i = 0; i < index - startIndext + 1; i++) {
									                temp2 += content.charAt(startIndext + i);
									            }
									            array[tempInt - 1] = temp2;
				
									            break;
									        }
									        index++;
				
									    }
				
									    return array;
									}
				
										private static String editLineInContent(String content, String newLine, int line) {
											System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
					
										    int lineNumber = 0;
										    lineNumber = numberOfLinesInFile(content);
					
										    String[] lines = new String[lineNumber];
										    lines = turnFileIntoArrayOfStrings(content, lineNumber);
					
										    if (line != 1) {
										        lines[line - 1] = "\n" + newLine + "\r";
										    } else {
										        lines[line - 1] = newLine;
										    }
										    content = new String();
					
										    for (int i = 0; i < lineNumber; i++) {
										        content += lines[i];
										    }
					
										    return content;
										}
					
											private static void writeToFile(String filePath, String content) {
												System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
												 File file = new  File(filePath);

//												   try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"))) {
//												       writer.write(content);
												try {
													FileWriter fw = new FileWriter(file.getAbsoluteFile());
													BufferedWriter bw = new BufferedWriter(fw);
													bw.write(content);
													bw.close();
											    } catch (UnsupportedEncodingException e) {
											        e.printStackTrace();
											    } catch (FileNotFoundException e) {
											        e.printStackTrace();
											    } catch (IOException e) {
											        e.printStackTrace();
											    }
											}
						
												private static String readFile(String filename) {
													System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
													
												    String content = null;
												    File file = new File(filename);
												    FileReader reader = null;
												    try {
												        reader = new FileReader(file);
												        char[] chars = new char[(int) file.length()];
												        reader.read(chars);
												        content = new String(chars);
												        reader.close();
												    } catch (IOException e) {
												        e.printStackTrace();
												    } finally {
												        if (reader != null) {
												            try {
												                reader.close();
												            } catch (IOException e) {
												                // TODO Auto-generated catch block
												                e.printStackTrace();
												            }
												        }
												    }
												    return content;
												}
												
										
											// sParent refer to test plan number show at SNOW UI
											public void createTestCases(String sParent) throws HttpException, IOException{
												System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
												
												if (util_constant.cSnowRerun.equals("N")){
													try { 
														 util_constant.utilSNOWIntegration.getPropertiesIni(sPropertiesINIPath);
														 establishSNOWConnection();
														 
												          HttpGet httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance?sysparm_query=parent.number%3D"+sParent+"&sysparm_fields=short_description");
												          httpget.setHeader("Accept", "application/json");
												          System.out.println("\n\n------------------------------------------------");
												          System.out.println("\nExecuting Request " + httpget.getRequestLine());
												          response = httpclient.execute(httpget);
												          
												          try {
												              System.out.println("\n" + response.getStatusLine());
												              responseBody = EntityUtils.toString(response.getEntity());
												              
												              responseBodyArray = responseBody.split("[{,}]");
												             
												              for (String responseBodySplit : responseBodyArray) {
												            	  if (!(responseBodySplit.matches(".*result.*") || responseBodySplit.matches(":\\[") || responseBodySplit.matches("]") || (responseBodySplit.matches("")))){
													                  System.out.println(responseBodySplit);
													                  
													                  if (responseBodySplit.matches("\"short_description\":.*")){
													                	  shortDescriptionString = responseBodySplit;
													                	  shortDescriptionArray = shortDescriptionString.split("[\":]");
													                	  
													                	  shortDescriptionLoop = 0;
													                	  for (String shortDescription : shortDescriptionArray) {
													                		  
													                		  if (shortDescriptionLoop==shortDescriptionArray.length-1){  
													                			String postData = "{\"parent\":\""+util_constant.cSnowTestPlanNumber+"\",\"short_description\":\""+shortDescription+"\",\"execution_status\":\"Unexecuted\"}";
													                			  
													                			HttpPost httpPost = new HttpPost("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance");
																		 		httpPost.setHeader("Accept", "application/json");
																		 		httpPost.setHeader("Content-Type", "application/json");
																		        HttpEntity entity = new ByteArrayEntity(postData.getBytes("utf-8"));
																		 		httpPost.setEntity(entity);
																			    
																			 	System.out.println("\n\n------------------------------------------------");	
																		        System.out.println("\nExecuting Request " + httpPost.getRequestLine());
																		        CloseableHttpResponse response = httpclient.execute(httpPost);
							
																		        System.out.println("\n" + response.getStatusLine());
																		        responseBody2 = EntityUtils.toString(response.getEntity());
																		        
																		        responseBodyArray2 = responseBody2.split("[{,}]");
																		        
																		        for (String responseBodySplit2 : responseBodyArray2) {
																	            	  if (!(responseBodySplit2.matches(".*result.*") || responseBodySplit2.matches(":\\[") || responseBodySplit2.matches("]") || (responseBodySplit2.matches("")))){
																		                  System.out.println(responseBodySplit2);
																		                  
																		                  if (responseBodySplit2.matches("\"number\":.*")){
																		                	  numberString = responseBodySplit2;
																		                	  numberArray = numberString.split("[\":]");
																		                	  
																		                	  numberLoop = 0;
																		                	  for (String number : numberArray) {
																		                		  if (numberLoop==numberArray.length-1){ 
																		                			  sTestCaseNumber = number;
																		                		}
																		                		  numberLoop++;  
																		                	  }
																		                	  
																		                  }else if (responseBodySplit2.matches("\"short_description\":.*")){
																		                	  shortDescriptionString2 = responseBodySplit2;
																		                	  shortDescriptionArray2 = shortDescriptionString2.split("[\":]");
																		                	  
																		                	  shortDescriptionLoop2 = 0;
																		                	  for (String shortDescription2 : shortDescriptionArray2) {
																		                		  if (shortDescriptionLoop2==shortDescriptionArray2.length-1){
																		                			  
																		                			  String[] testCaseIDArray = shortDescription2.split("-");
																		                			  sTestCaseID = testCaseIDArray[0];
																		                		}
																		                		  shortDescriptionLoop2++;  
																		                	  }
																		                	  
																		                  }
																	            	  }
																	            	  
																	              }  
																		        updateIniFile(util_constant.cTestCasesINI, sTestCaseID.trim(), sTestCaseNumber.trim());
																		        
													                	  }
													                		  shortDescriptionLoop ++;
													                  }
												            	  }
												              }
												          } 
											         }
														finally {
													              response.close();
													          }
												        updateIniFile(sPropertiesINIPath, "sSnowRerun","Y");	
											      } finally {
											          httpclient.close();
											      } 
														 
													 }
											}
											
											
											
											
											// sNumber refer to test case number show at SNOW UI
											public void createTestCasesByTestNumber(String sNumber) throws HttpException, IOException{
												System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
												
												if (util_constant.cSnowRerun.equals("Y")){
													try { 
														 util_constant.utilSNOWIntegration.getPropertiesIni(sPropertiesINIPath);
														 establishSNOWConnection();
														 
												          HttpGet httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance?sysparm_query=number%3D"+sNumber+"&sysparm_fields=short_description");
												          httpget.setHeader("Accept", "application/json");
												          System.out.println("\n\n------------------------------------------------");
												          System.out.println("\nExecuting Request " + httpget.getRequestLine());
												          response = httpclient.execute(httpget);
												          
												          try {
												              System.out.println("\n" + response.getStatusLine());
												              responseBody = EntityUtils.toString(response.getEntity());
												              
												              responseBodyArray = responseBody.split("[{,}]");
												             
												              for (String responseBodySplit : responseBodyArray) {
												            	  if (!(responseBodySplit.matches(".*result.*") || responseBodySplit.matches(":\\[") || responseBodySplit.matches("]") || (responseBodySplit.matches("")))){
													                  System.out.println(responseBodySplit);
													                  
													                  if (responseBodySplit.matches("\"short_description\":.*")){
													                	  shortDescriptionString = responseBodySplit;
													                	  shortDescriptionArray = shortDescriptionString.split("[\":]");
													                	  
													                	  shortDescriptionLoop = 0;
													                	  for (String shortDescription : shortDescriptionArray) {
													                		  
													                		  if (shortDescriptionLoop==shortDescriptionArray.length-1){  
													                			String postData = "{\"parent\":\""+util_constant.cSnowTestPlanNumber+"\",\"short_description\":\""+shortDescription+"\",\"execution_status\":\"Unexecuted\"}";
													                			  
													                			HttpPost httpPost = new HttpPost("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance");
																		 		httpPost.setHeader("Accept", "application/json");
																		 		httpPost.setHeader("Content-Type", "application/json");
																		        HttpEntity entity = new ByteArrayEntity(postData.getBytes("utf-8"));
																		 		httpPost.setEntity(entity);
																			    
																			 	System.out.println("\n\n------------------------------------------------");	
																		        System.out.println("\nExecuting Request " + httpPost.getRequestLine());
																		        CloseableHttpResponse response = httpclient.execute(httpPost);
							
																		        System.out.println("\n" + response.getStatusLine());
																		        responseBody2 = EntityUtils.toString(response.getEntity());
																		        
																		        responseBodyArray2 = responseBody2.split("[{,}]");
																		        
																		        for (String responseBodySplit2 : responseBodyArray2) {
																	            	  if (!(responseBodySplit2.matches(".*result.*") || responseBodySplit2.matches(":\\[") || responseBodySplit2.matches("]") || (responseBodySplit2.matches("")))){
																		                  System.out.println(responseBodySplit2);
																		                  
																		                  if (responseBodySplit2.matches("\"number\":.*")){
																		                	  numberString = responseBodySplit2;
																		                	  numberArray = numberString.split("[\":]");
																		                	  
																		                	  numberLoop = 0;
																		                	  for (String number : numberArray) {
																		                		  if (numberLoop==numberArray.length-1){ 
																		                			  sTestCaseNumber = number;
																		                		}
																		                		  numberLoop++;  
																		                	  }
																		                	  
																		                  }else if (responseBodySplit2.matches("\"short_description\":.*")){
																		                	  shortDescriptionString2 = responseBodySplit2;
																		                	  shortDescriptionArray2 = shortDescriptionString2.split("[\":]");
																		                	  
																		                	  shortDescriptionLoop2 = 0;
																		                	  for (String shortDescription2 : shortDescriptionArray2) {
																		                		  if (shortDescriptionLoop2==shortDescriptionArray2.length-1){
																		                			  
																		                			  String[] testCaseIDArray = shortDescription2.split("-");
																		                			  sTestCaseID = testCaseIDArray[0];
																		                		}
																		                		  shortDescriptionLoop2++;  
																		                	  }
																		                	  
																		                  }
																	            	  }
																	            	  
																	              }  
																		        updateIniFile(util_constant.cTestCasesINI, sTestCaseID.trim(), sTestCaseNumber.trim());
																		        
													                	  }
													                		  shortDescriptionLoop ++;
													                  }
												            	  }
												              }
												          } 
											         }
														finally {
													              response.close();
													          }
												        updateIniFile(sPropertiesINIPath, "sSnowRerun","Y");	
											      } finally {
											          httpclient.close();
											      } 
														 
													 }
											}
											
											
											
											// sFilePath refer to file to update
											// sKey refer to property key to update
											public void getTestCasesIniFile(String sFilePath, String sKey) throws IOException{
												System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
												
												try{
												      Properties p = new Properties();
												      p.load(new FileInputStream(sFilePath));

													      util_constant.cSnowTestCaseNumber = p.getProperty(sKey);
													      System.out.println(sKey+ ": " + p.getProperty(sKey));
												      
												      
												      }
												    catch (Exception e) {
												      System.out.println(e);
												      }
											}
											
											
											
												public void createTestCasesHistory() throws IOException, HttpException{
													System.out.println("\n\n\n\nExecuting method >>> "+new Object() {}.getClass().getEnclosingMethod().getName());
													
													String patchData = "{\"parent\":\""+util_constant.cSnowTestPlanHistoryNumber+"\"}";
													
													if (util_constant.cSnowRerun.equals("Y")){
														try{
															establishSNOWConnection();
															 
													          HttpGet httpget = new HttpGet("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance?sysparm_query=number%3D"+util_constant.cSnowTestCaseNumber+"&sysparm_fields=sys_id");
													          httpget.setHeader("Accept", "application/json");
													          System.out.println("\n\n------------------------------------------------");
													          System.out.println("\nExecuting Request " + httpget.getRequestLine());
													          response = httpclient.execute(httpget);
													          
													          try {
													              System.out.println("\n" + response.getStatusLine());
													              responseBody = EntityUtils.toString(response.getEntity());
													              
													              responseBodyArray = responseBody.split("[{,}]");
													             
													              for (String responseBodySplit : responseBodyArray) {
													            	  if (!(responseBodySplit.matches(".*result.*") || responseBodySplit.matches(":\\[") || responseBodySplit.matches("]") || (responseBodySplit.matches("")))){
														                  System.out.println(responseBodySplit);
														                  
														                  if (responseBodySplit.matches("\"sys_id\":.*")){
														                	  sysIdString = responseBodySplit;
														                	  sysIdArray = sysIdString.split("[\":]");
														                	  
														                	  sysIdLoop = 0;
														                	  for (String sysId : sysIdArray) {
														                		  
														                		  if (sysIdLoop==sysIdArray.length-1){  
														                			  HttpPatch httpPatch = new HttpPatch("https://"+util_constant.cSnowURL+"/api/now/table/tm_test_case_instance/"+sysId+"?sysparm_input_display_value=true");
															  					 		httpPatch.setHeader("Accept", "application/json");
															  					 		httpPatch.setHeader("Content-Type", "application/json");
															  					        HttpEntity entity = new ByteArrayEntity(patchData.getBytes("utf-8"));
															  					 		httpPatch.setEntity(entity);
															                			
															  					 		System.out.println("\n\n------------------------------------------------");
															  					        System.out.println("\nExecuting Request " + httpPatch.getRequestLine());
															  					        CloseableHttpResponse response = httpclient.execute(httpPatch);
															  					        
														                	            System.out.println("\n" + response.getStatusLine());
														                	            
														                	            createTestCasesByTestNumber(util_constant.cSnowTestCaseNumber);
														                	            getTestCasesIniFile(util_constant.cTestCasesINI, util_constant.cTestCaseId);
														                	            
//														                	            updateIniFile(util_constant.cTestCasesINI, sTestCaseID.trim(), sTestCaseNumber.trim());
														                		  }
														                		  sysIdLoop ++;
														                	  }
														                	  
														                  }
													            	  }
																	}
													          	}
													          finally {
													              response.close();
													          }    
														}
													finally {
														httpclient.close();
											          }
													}
												}
														





}
