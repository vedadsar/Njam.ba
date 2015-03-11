package Utilites;

import play.libs.mailer.Email;
import play.libs.mailer.MailerPlugin;

public class MailHelper {

	public static void send(String email, String message){
		
		Email mail = new Email();
		mail.setSubject("Njam.ba verification mail");
		mail.setFrom("Njam.ba <bit.play.test@gmail.com>");
		mail.addTo("Bitter Contact <bit.play.test@gmail.com>");
		mail.addTo(email);
		
		// adds attachment
//		mail.addAttachment("attachment.pdf", new File("/some/path/attachment.pdf"));
		// adds inline attachment from byte array
//		mail.addAttachment("data.txt", "data".getBytes(), "text/plain", "Simple data", EmailAttachment.INLINE);
		
		// sends text, HTML or both...
		mail.setBodyText(message);
		mail.setBodyHtml(String.format("<html><body><strong> %s </strong>: <p> %s </p> </body></html>", email, message));
		MailerPlugin.send(mail);
		
	}
}