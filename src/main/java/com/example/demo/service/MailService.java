package com.example.demo.service;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import static com.example.demo.constant.EmailConstant.*;

@Service
public class MailService {
	
	public void sendNewEmailPassword(String firstName, String password, String email) throws AddressException, MessagingException {
		Message message = createEmail(firstName, password, email);
		Transport transport = getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
	     transport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();
	}
	
	
	private Message createEmail(String firstName, String password, String email) throws AddressException, MessagingException {
		// Create a MimeMessage object. 
		Message message = new MimeMessage(getEmailSession());
		// new InternetAddress() is for validating the email address 
	   message.setFrom(new InternetAddress(FROM_EMAIL)); 
		message.setRecipients(RecipientType.TO, InternetAddress.parse(email, false));
		message.setRecipients(RecipientType.CC, InternetAddress.parse(CC_EMAIL, false));
		message.setSubject(EMAIL_SUBJECT); 
		message.setText("Hello " + firstName + ", \n \n Your new account password is : " + password +  "\n \n The Support Team");
		message.setSentDate(new Date());
		message.saveChanges();
		return message;
		
	}
	
	
	//Create a message using a JavaMail Session object.
    //we set Session prop to create new session with the prop we pass everytime we send request
	private Session getEmailSession() {
		Properties properties = System.getProperties();
		
		properties.put(SMTP_HOST, GMAIL_SMTP_SERVER);
		properties.put(SMTP_AUTH, true);
		properties.put(SMTP_PORT, DEFAULT_PORT);
		 properties.put(SMTP_STARTTLS_ENABLE, true);
	     properties.put(SMTP_STARTTLS_REQUIRED, true); 
	     return Session.getInstance(properties, null);
	} 
	
	
}
