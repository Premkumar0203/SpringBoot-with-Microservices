// Java Program to Illustrate Creation Of
// Service implementation class

package com.digitalbooks.email;

// Importing required classes
import java.io.File;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.digitalbooks.Models.EmailDetails;


@Service
public class EmailServiceImpl implements EmailService {

	@Autowired private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}") private String sender;

	
	public String sendSimpleMail(EmailDetails details)
	{
		try {
			SimpleMailMessage mailMessage
				= new SimpleMailMessage();
			mailMessage.setFrom(sender);
			mailMessage.setTo(details.getRecipient());
			mailMessage.setText(details.getMsgBody());
			mailMessage.setSubject(details.getSubject());
			javaMailSender.send(mailMessage);
			return "Mail Sent Successfully...";
		}
		catch (Exception e) {
			return "Error while Sending Mail";
		}
	}

	

}
