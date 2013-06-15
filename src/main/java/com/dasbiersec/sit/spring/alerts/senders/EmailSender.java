package com.dasbiersec.sit.spring.alerts.senders;

import com.dasbiersec.sit.spring.dto.AlertMessageDTO;
import com.dasbiersec.sit.spring.model.InventoryItem;
import com.dasbiersec.sit.spring.util.ConfigProperties;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

@Component
public class EmailSender implements Sender
{
	final private String userName = ConfigProperties.getProperty("alert.smtp.username");
	final private String password = ConfigProperties.getProperty("alert.smtp.password");
	final private String fromEmail = ConfigProperties.getProperty("alert.email.from");
	final private String toEmail = ConfigProperties.getProperty("alert.email.to");

	public void send(List<AlertMessageDTO> messages)
	{
		Properties props = new Properties();
		props.put("mail.smtp.auth", ConfigProperties.getProperty("alert.smtp.auth"));
		props.put("mail.smtp.starttls.enable", ConfigProperties.getProperty("alert.smtp.tls"));
		props.put("mail.smtp.host", ConfigProperties.getProperty("alert.smtp.host"));
		props.put("mail.smtp.port", ConfigProperties.getProperty("alert.smtp.port"));

		Session session = Session.getInstance(props,
			new Authenticator()
			{
				@Override
				protected PasswordAuthentication getPasswordAuthentication()
				{
					return new PasswordAuthentication(userName, password);
				}
			}
		);

		try
		{
			for (AlertMessageDTO message : messages)
			{
				Message email = new MimeMessage(session);
				email.setFrom(new InternetAddress(fromEmail));
				email.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
				email.setSubject("Item in stock alert");

				StringBuilder messageText = new StringBuilder();
				messageText.append("These items are now in stock:\n");
				for (InventoryItem item : message.getItems())
				{
					messageText.append("* " + item.getName() + " URL: " + item.getUrl()).append("\n");
				}

				email.setText(messageText.toString());

				Transport.send(email);
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
