package Utilites;

import java.util.Iterator;
import java.util.List;

import models.User;
import play.Logger;
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
		mail.setBodyHtml(String.format("<html><body><strong> %s </strong> <p> %s </p> <p> %s </p> </body></html>","Thanks for signing up to njam.ba!", "We wish you a pleasant shopping time.", message));
		MailerPlugin.send(mail);
		
	}
	
	public static void tellUserThatOrderIsApproved(String email, double price,
			String restaurantName) {
		
		String priceString = Double.toString(price);
		Logger.debug(priceString);
		
		Email mail = new Email();
		mail.setSubject("Njam.ba purchase approved!");
		mail.setFrom("Njam.ba <bit.play.test@gmail.com>");
		mail.addTo(email);
		
		mail.setBodyText("");
		
		mail.setBodyHtml(String
				.format("<html>"
						+ "<body>"
						+ "<strong> Order from:  </strong>: " + "%s"
						+ "<br></br>"
						+ "<strong> Amount spent:   </strong>: " + "%s"
						+ "<br></br>"
						+ "<strong> Your order has been successfully approved and payment was completed. Hope you visit us again! </strong>:" 
						+ "<br></br>"
						+ "</body>"
						+ "</html>",
						restaurantName, priceString));
		
		MailerPlugin.send(mail);
		
	}
	
	
	public static void sendSudo(String email,String title, String message){
		
		Email mail = new Email();
		mail.setSubject("Contact Form Notification from: " +email +" Subject: " + title);
		mail.setFrom("Njam.ba <bit.play.test@gmail.com>");
		mail.addTo("Bitter Contact <bit.play.test@gmail.com>");
		mail.addTo(email);
		List<User>adminList = User.findAdmins();
		

		Iterator <User>	sudoIterator=adminList.iterator();
		while (sudoIterator.hasNext()) {
			mail.addTo(sudoIterator.next().email);
			}
				
		

		mail.setBodyText(message);
		mail.setBodyHtml(String.format("<html><body><strong> %s </strong>: <p> %s </p> </body></html>", email, message));
		MailerPlugin.send(mail);
		
	}
	
	
}