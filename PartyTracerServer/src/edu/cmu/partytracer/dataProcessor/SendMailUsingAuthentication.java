package edu.cmu.partytracer.dataProcessor;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*

 SMTP_HOST_NAME -- Has your SMTP Host Name
 SMTP_AUTH_USER -- Has your SMTP Authentication UserName
 SMTP_AUTH_PWD  -- Has your SMTP Authentication Password

 emailMsgTxt  -- Message Text for the Email
 emailSubjectTxt  -- Subject for email
 emailFromAddress -- Email Address whose name will appears as "from" address
 emailList -- This String array has List of all Email Addresses to Email Email needs to be sent to.
 Verizon: 10digitphonenumber@vtext.com
 AT&T: 10digitphonenumber@txt.att.net
 Sprint: 10digitphonenumber@messaging.sprintpcs.com
 T-Mobile: 10digitphonenumber@tmomail.net
 Nextel: 10digitphonenumber@messaging.nextel.com
 Cingular: 10digitphonenumber@cingularme.com
 Virgin Mobile: 10digitphonenumber@vmobl.com
 Alltel: 10digitphonenumber@message.alltel.com
 CellularOne: 10digitphonenumber@mobile.celloneusa.com
 Omnipoint: 10digitphonenumber@omnipointpcs.com
 Qwest: 10digitphonenumber@qwestmp.com
 */

public class SendMailUsingAuthentication {

	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_AUTH_USER = "partytracer";
	private static final String SMTP_AUTH_PWD = "test15446";
	private static final String SMTP_PORT_NUBER = "587";

	public void postMail(String recipients[], String subject, String message, String from) throws MessagingException {
		boolean debug = false;

		// Set the host smtp address
		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", SMTP_PORT_NUBER);
		props.put("mail.smtp.starttls.enable", "true");

		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);

		session.setDebug(debug);

		// create a message
		Message msg = new MimeMessage(session);

		// set the from and to address
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		for (int i = 0; i < recipients.length; i++) {
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);

		// Setting the Subject and Content Type
		msg.setSubject(subject);
		msg.setContent(message, "text/plain");
		Transport.send(msg);
	}

	/**
	 * SimpleAuthenticator is used to do simple authentication when the SMTP
	 * server requires it.
	 */
	private class SMTPAuthenticator extends javax.mail.Authenticator {

		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_AUTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}

}
