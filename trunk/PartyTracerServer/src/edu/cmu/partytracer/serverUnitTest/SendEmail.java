package edu.cmu.partytracer.serverUnitTest;

import edu.cmu.partytracer.dataProcessor.SendMailUsingAuthentication;

public class SendEmail {
	private static final String emailMsgTxt = "SMS is reachable to your phone";
	private static final String emailSubjectTxt = "15446 test message";
	private static final String emailFromAddress = "partytracer@gmail.com";
	// Add List of Email address to who email needs to be sent to
	private static final String[] emailList = { "4129994538@text.att.com",
			"ctrlhxj@163.com" };

	public static void main(String args[]) throws Exception {
		SendMailUsingAuthentication smtpMailSender = new SendMailUsingAuthentication();
		smtpMailSender.postMail(emailList, emailSubjectTxt, emailMsgTxt,
				emailFromAddress);
		System.out.println("Sucessfully Sent mail to All Users");
	}

}
