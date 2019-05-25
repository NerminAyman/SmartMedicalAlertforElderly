import java.awt.Color;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSending {

	Properties emailProperties;
	Session mailSession;
	MimeMessage emailMessage;

	public void setMailServerProperties() {
		String emailPort = "587";//gmail's smtp port
		emailProperties = System.getProperties();
		emailProperties.put("mail.smtp.port", emailPort);
		emailProperties.put("mail.smtp.auth", "true");
		emailProperties.put("mail.smtp.starttls.enable", "true");
	}

	public void createEmailMessage(String toEmails,String emailBody) throws AddressException,
			MessagingException {
		String emailSubject = "Current Elderly's Vitals";
		mailSession = Session.getDefaultInstance(emailProperties, null);
		emailMessage = new MimeMessage(mailSession);
		emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails));
		emailMessage.setSubject(emailSubject);
		emailMessage.setContent(emailBody, "text/html");
	}

	public void sendEmail() throws AddressException, MessagingException {
		String emailHost = "smtp.gmail.com";
		Transport transport = mailSession.getTransport("smtp");
		transport.connect(emailHost, "currenthealthcondition@gmail.com", "Helloworld2019.");
		transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
		transport.close();
		System.out.println("Email sent successfully.");
		Pop p = new Pop("Email sent successfully",Color.green);
	}

}