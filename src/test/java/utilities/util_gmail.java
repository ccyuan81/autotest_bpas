package utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import org.testng.Assert;

public class util_gmail extends util_common {

		private static String  host = "pop.gmail.com";
		private static String username = "bpastest17@gmail.com";
		private static String password = "seagate123321";
		private static Properties properties = new Properties();    
		private static Session emailSession;
		private static Store store;
		private static Folder emailFolder;
		private static Message[] messages;
		private static String sContent;
		private static String sSubject; 
		private static String sFrom;

	   public  static boolean  connectGmail()
	   { 
		    try
		    {
		    	 //properties.put("mail.pop3.host", host);
			     //properties.put("mail.pop3.port", "995");
			     //properties.put("mail.pop3.starttls.enable", "true");
			     //emailSession = Session.getDefaultInstance(properties);
			     //store = emailSession.getStore("pop3s");
			     //store.connect(host, username, password);
			     
			     properties.put("mail.store.protocol", "imaps");
			     emailSession = Session.getDefaultInstance(properties);
			     store = emailSession.getStore();
			     store.connect(host, username, password);

			     return true;
		    }
		    catch (Exception e)
		    {
		    	return false;
		    }
	   }


	   public static void  retrieveInvitationEmailRequestApp(String sBrowserURL)
	   {
		   try
		   {
			   String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
			   String sEmailSubject = null;
			   
			   util_constant.cExp_Result = "Email with subject title: Sandbox: Invitation to register for Seagate Direct Supplier Portal";
			   sEmailSubject = "Sandbox: Invitation to register for Seagate Direct Supplier Portal";
			
			   
			   Thread.sleep(5000);
			   
		      emailFolder = store.getFolder("INBOX");
		      emailFolder.open(Folder.READ_ONLY);
		      
		      messages = emailFolder.getMessages();

		      breakLoop:
		      for (int i = 0, n = messages.length; i < n; i++) 
		      {
			         Message message = messages[i];
			         
			         if (message.getSubject().equalsIgnoreCase(sEmailSubject))
			         {
			        	 	 util_constant.cAct_Result = "Email detected:  Try attempt: " + i;
			        	 	 sSubject = message.getSubject();
					         sContent = message.getContent().toString();
					         
					         if (sContent.contains("You are receiving this message as a confirmation that something in your Seagate profile has been changed."))
					         {
					        	 System.out.println("Seagate Profile has been changed");
					        	 break breakLoop;
					         }					        
					         else if (sContent.contains("You are receiving this message as a confirmation that something in your LaCie profile has been changed."))
					         {
					        	 System.out.println("LaCie Profile has been changed");
					        	 break breakLoop;
					         }					        	 
			         }
		
		        	if (i==n-1)
		        	{
		        		System.out.println("EMAIL NOT FOUND!!!!!!!!!!");
		        		util_constant.cAct_Result = "Email not found. Search attempt: " + i;
		        		break breakLoop;
		        	}
		      }
		   }
		   catch (Exception e)
		   {
		   }
	   }

	   
	   
	   public static void  retrieveEmailContent(String sEmailCounter, String sCRBrowserURL)
	   {
		   String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		   
		   try
		   {
			    emailFolder = store.getFolder("INBOX");
			    emailFolder.open(Folder.READ_WRITE);
			    
			    String sEmailSubject = null;
			    
			    if (sCRBrowserURL.contains("myportaltst.seagate.com"))
			   {
				   util_constant.cExp_Result = "Email with subject title: Seagate Account Verification Request found.";
				   sEmailSubject = "Seagate Account verification request";
			   }
			   else
			   {
				   util_constant.cExp_Result = "Email with subject title: LaCie Account Verification Request found.";
				   sEmailSubject = "LaCie Account verification request";
			   }

			    Message[] messages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

			    breakLoop:
			      for (int i = 0, n = messages.length; i < n; i++) 
			      {

				         if (messages[i].getSubject().equalsIgnoreCase(sEmailSubject))
				         {
				        	 	 util_constant.cAct_Result = "Email detected:  Try attempt: " + i;
				        	 	 
				        	 	 Message message = messages[i];
				        	 	 
				        	 	 sSubject = message.getSubject();
				        	 	 sContent = message.getContent().toString();
	
						         if (sContent.contains("userName=ciamtest16%2B" + sEmailCounter))
						         {
						        	 break breakLoop;
						         }
						         
						         message.setFlag(Flags.Flag.SEEN,true);
				         }
			
			        	if (i==n-1)
			        	{
			        		util_constant.cAct_Result = "Email not found. Search attempt: " + i;
			        		break breakLoop;
			        	}
			      }
			    
			  reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		   }
		   catch (Exception e)
		   {
				  util_constant.cAct_Result = e.getMessage();
				  reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				  Assert.fail(util_constant.cAct_Result);
		   }
	   }
	   
	  
	   
	   public static boolean  disconnectDeleteGmail()
	   {
		   String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		   
		    try
		    {
		        System.out.println("Checking1");
		    	//emailFolder.close(false);
		    	//System.out.println("Checking2");
			    store.close();
			    System.out.println("Checking3");
			    return true;
		    }
		    catch (Exception e)
		    {
		    	System.out.println("disconnect gmail issue for deletion: " + e.getMessage());
		    	 util_constant.cAct_Result = e.getMessage();
				  reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				  Assert.fail(util_constant.cAct_Result);
				  return false;
		    }
	   }
	   
	   public static boolean  disconnectGmail()
	   {
		   String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		   
		    try
		    {
		        emailFolder.close(false);
			    store.close();
			    return true;
		    }
		    catch (Exception e)
		    {
		    	System.out.println("disconnect gmail issue: " + e.getMessage());
		    	 util_constant.cAct_Result = e.getMessage();
				  reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				  Assert.fail(util_constant.cAct_Result);
				  return false;
		    }
	   }
	   
	   public static void clickAccountVerificationLink(String sCRBrowserURL) throws InterruptedException
		{
			//String splitter = "https://logintst.okla.seagate.com/consumer-identity/confirm-registration";
		    String splitter = null;
		    
		    if (sCRBrowserURL.contains("myportaltst.seagate.com"))
		    {
		    	splitter = "https://myportaltst.seagate.com/consumer-identity/confirm-registration";
		    }
		    else
		    {
		    	splitter = "https://myportaltst.lacie.com/consumer-identity/confirm-registration";
		    }
		   
			String splitter2 = "gmail.com&lang=en-us";

			String[] alink = sContent.split(splitter);
			String[] alink2 = alink[1].split(splitter2);
		    String sLink = splitter + alink2[0] + splitter2;
		    

		    //util_seleniumdrivermgr.getDriver(sLink);
		    
		    util_constant.cSel_Driver = util_seleniumdrivermgr.getDriver(sLink);
		    
		    Thread.sleep(3000);
		    
		}



	   
	   public static void  retrieveVerificationEmailContent(String sBrowserURL, String sEmail)
	   {
		   try
		   {
			   String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
			   String sEmailSubject = null;
			   
			   sEmail = sEmail.replace("+", "%2B");
			   sEmail = sEmail.replace("@", "%40");
			   
			   if (sBrowserURL.contains("myportaltst.seagate.com"))
			   {
				   util_constant.cExp_Result = "Email with subject title: Seagate Account Verification Request found.";
				   sEmailSubject = "Seagate Account verification request";
			   }
			   else
			   {
				   util_constant.cExp_Result = "Email with subject title: LaCie Account Verification Request found.";
				   sEmailSubject = "LaCie Account verification request";
			   }
			   
				   
			   Thread.sleep(5000);
			   
		      emailFolder = store.getFolder("INBOX");
		      emailFolder.open(Folder.READ_ONLY);
		      
		      messages = emailFolder.getMessages();

		      breakLoop:
		      for (int i = 0, n = messages.length; i < n; i++) 
		      {
			         Message message = messages[i];
			         
			         if (message.getSubject().equalsIgnoreCase(sEmailSubject))
			         {
			        	 	 util_constant.cAct_Result = "Email detected:  Try attempt: " + i;
			        	 	 sSubject = message.getSubject();
					         sContent = message.getContent().toString();
					         
					         if (sContent.contains("userName=" + sEmail ))
					         //if (sContent.contains("userName=ciamtest16%2B"))
					         {
					        	 System.out.println("EMAIL FOUND!!!!!!!!!!");
					        	 break breakLoop;
					         }
			         }
		
		        	if (i==n-1)
		        	{
		        		System.out.println("EMAIL NOT FOUND!!!!!!!!!!");
		        		util_constant.cAct_Result = "Email not found. Search attempt: " + i;
		        		break breakLoop;
		        	}
		      }
		   }
		   catch (Exception e)
		   {
		   }
	   }

	   
	   public static void  retrieveResetPassNoEmailContent(String sBrowserURL, String sEmail)
	   {
		   try
		   {
			   String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
			   String sEmailSubject = null;
			   
			   sEmail = sEmail.replace("+", "%2B");
			   sEmail = sEmail.replace("@", "%40");
			   
			   if (sBrowserURL.contains("myportaltst.seagate.com"))
			   {
				   util_constant.cExp_Result = "Email with subject title: Seagate Account password reset request.";
				   sEmailSubject = "Seagate Account password reset request";
			   }
			   else
			   {
				   util_constant.cExp_Result = "Email with subject title: LaCie Account password reset request.";
				   sEmailSubject = "LaCie Account password reset request";
			   }
			   		   
			   
			   Thread.sleep(5000);
			   
		      emailFolder = store.getFolder("INBOX");
		      emailFolder.open(Folder.READ_ONLY);
		      
		      messages = emailFolder.getMessages();

		      breakLoop:
		      for (int i = 0, n = messages.length; i < n; i++) 
		      {
			         Message message = messages[i];
			         
			         if (message.getSubject().equalsIgnoreCase(sEmailSubject))
			         {
			        	 	 util_constant.cAct_Result = "Email detected:  Try attempt: " + i;
			        	 	 sSubject = message.getSubject();
					         sContent = message.getContent().toString();
					         
					         
					         if (sContent.contains("userName=" + sEmail ))
					         //if (sContent.contains("userName=ciamtest16%2B"))
					         {
					        	 System.out.println("EMAIL FOUND!!!!!!!!!!");
					        	 break breakLoop;
					         }
			         }
		
		        	if (i==n-1)
		        	{
		        		System.out.println("No email received is correct result");
		        		util_constant.cAct_Result = "Email not found. Search attempt: " + i;
		        		break breakLoop;
		        	}
		      }
		   }
		   catch (Exception e)
		   {
		   }
	   }

	     
	   
	   
	   public static void  retrieveResetPassEmailContent(String sBrowserURL, String sEmail)
	   {
		   try
		   {
			   String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
			   String sEmailSubject = null;
			   
			  sEmail = sEmail.replace("+", "%2B");		   
			  sEmail = sEmail.replace("@", "%40");
			   
			   if (sBrowserURL.contains("myportaltst.seagate.com"))
			   {
				   util_constant.cExp_Result = "Email with subject title: Seagate Account password reset request.";
				   sEmailSubject = "Seagate Account password reset request";
			   }
			   else
			   {
				   util_constant.cExp_Result = "Email with subject title: LaCie Account password reset request.";
				   sEmailSubject = "LaCie Account password reset request";
			   }
			   			   
			   
			   Thread.sleep(5000);
			   
		      emailFolder = store.getFolder("INBOX");
		      emailFolder.open(Folder.READ_ONLY);
		      
		      messages = emailFolder.getMessages();

		      breakLoop:
		      for (int i = 0, n = messages.length; i < n; i++) 
		      {
			         Message message = messages[i];
			         
			         if (message.getSubject().equalsIgnoreCase(sEmailSubject))
			         {
			        	 	 util_constant.cAct_Result = "Email detected:  Try attempt: " + i;
			        	 	 sSubject = message.getSubject();
					         sContent = message.getContent().toString();
					         
					         System.out.println("sEmail: " + sEmail);
					         if (sContent.contains("userName=" + sEmail ))
					         //if (sContent.contains("userName=ciamtest16%2B"))
					         {
					        	 System.out.println("EMAIL FOUND!!!!!!!!!!");
					        	 break breakLoop;
					         }
			         }
		
		        	if (i==n-1)
		        	{
		        		System.out.println("EMAIL NOT FOUND!!!!!!!!!!");
		        		util_constant.cAct_Result = "Email not found. Search attempt: " + i;
		        		break breakLoop;
		        	}
		      }
		   }
		   catch (Exception e)
		   {
		   }
	   }

	   public static void  retrieveResetPassEmailChangedMessage(String sBrowserURL)
	   {
		   try
		   {
			   String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
			   String sEmailSubject = null;
			   
			   if (sBrowserURL.contains("myportaltst.seagate.com"))
			   {
				   util_constant.cExp_Result = "Email with subject title: Seagate Account password reset request.";
				   sEmailSubject = "Seagate Account password reset request";
			   }
			   else
			   {
				   util_constant.cExp_Result = "Email with subject title: LaCie Account password reset request.";
				   sEmailSubject = "LaCie Account password reset request";
			   }
			   			   
			   
			   Thread.sleep(5000);
			   
		      emailFolder = store.getFolder("INBOX");
		      emailFolder.open(Folder.READ_ONLY);
		      
		      messages = emailFolder.getMessages();

		      breakLoop:
		      for (int i = 0, n = messages.length; i < n; i++) 
		      {
			         Message message = messages[i];
			         
			         if (message.getSubject().equalsIgnoreCase(sEmailSubject))
			         {
			        	 	 util_constant.cAct_Result = "Email detected:  Try attempt: " + i;
			        	 	 sSubject = message.getSubject();
					         sContent = message.getContent().toString();
					         
					         if (sContent.contains("You are receiving this message as a confirmation that your password has been changed."))
					         {
					        	 System.out.println("Password has been changed");
					        	 break breakLoop;
					         }
			         }
		
		        	if (i==n-1)
		        	{
		        		System.out.println("EMAIL NOT FOUND!!!!!!!!!!");
		        		util_constant.cAct_Result = "Email not found. Search attempt: " + i;
		        		break breakLoop;
		        	}
		      }
		   }
		   catch (Exception e)
		   {
		   }
	   }



	   
	public static void deleteGmailTrash(String sEmailSubject)
	{
		   String sMethod = new Object() {}.getClass().getEnclosingMethod().getName();
		   
		    try
		    {
		    		System.out.println("Delete Here");
		    		Folder emailFolder = store.getFolder("INBOX");			    	    
		    	    Folder trash = store.getFolder("[Gmail]/Trash"); 
		    	    
		    	    System.out.println("Read");
				    
		    	    emailFolder.open(Folder.READ_WRITE);
				    					   
				    
				    
				    Message[] messages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
				    
				    ArrayList<Message> markedMsgList = new ArrayList<Message>(); 
				      for (int i = 0, n = messages.length; i < n; i++) 
				      {

					         if (messages[i].getSubject().equalsIgnoreCase(sEmailSubject))
					         {
					        	 	 util_constant.cAct_Result = "Email detected:  Try attempt: " + i;
					        	 	 
					        	 	 Message message = messages[i];
					        	 	 message.setFlag(Flags.Flag.DELETED, true);
					        	 							        	 	
					        	 	markedMsgList.add(message);
					        	 	System.out.println("Marked DELETE for message: " + sEmailSubject);
					         }
				
				      }
				      
				      emailFolder.copyMessages(markedMsgList.toArray(new Message[0]), trash);
				      
			      reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Passed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
		    }
		    catch (Exception e)
		    {
		    	System.out.println("Error: " + e.getMessage());
		    	 util_constant.cAct_Result = e.getMessage();
				  reportResult(0, util_constant.cTc_Id, util_constant.cTc_Name, "Check point: " + sMethod, "Failed", util_constant.cExp_Result, util_constant.cAct_Result, sMethod, "jpg");
				  Assert.fail(util_constant.cAct_Result);
		    }
	}

	   

public static void clickAccountPasswordResetVerificationLink(String sBrowserURL) throws InterruptedException
	{
		//String splitter = "https://logintst.okla.seagate.com/consumer-identity/confirm-registration";
	    String splitter = null;
	    
	    if (sBrowserURL.contains("myportaltst.seagate.com"))
	    {
	    	splitter = "https://myportaltst.seagate.com/consumer-identity/password/reset";
	    }
	    else
	    {
	    	splitter = "https://myportaltst.lacie.com/consumer-identity/password/reset";
	    }
	   
		String splitter2 = "gmail.com&lang=en-us";

		String[] alink = sContent.split(splitter);
		String[] alink2 = alink[1].split(splitter2);
	    String sLink = splitter + alink2[0] + splitter2;
	    

	    //util_seleniumdrivermgr.getDriver(sLink);
	    
	    util_constant.cSel_Driver = util_seleniumdrivermgr.getDriver(sLink);
	    
	    Thread.sleep(3000);
	    
	}  

}