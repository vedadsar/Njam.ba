package Utilites;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import controllers.Default;
import models.Meal;
import models.Newsletter;
import models.MetaItem;
import models.User;
import play.Logger;
import play.Play;
import play.libs.mailer.Email;
import play.libs.mailer.MailerPlugin;


public class MailHelper {

	public static void send(String email, String message){
		try {
			HtmlEmail mail = new HtmlEmail();
			mail.setSubject("Njam.ba verification mail");
			mail.setFrom("Njam.ba <bit.play.test@gmail.com>");
			mail.addTo("Bitter Contact <bit.play.test@gmail.com>");
			mail.addTo(email);
			mail.setMsg(message);
			mail.setHtmlMsg(String
					.format("<html><body><strong> %s </strong> <p> %s </p> <p> %s </p> </body></html>",
							"Thanks for signing up to njam.ba!",
							"We wish you a pleasant shopping time.", message));
			mail.setHostName("smtp.gmail.com");
			mail.setStartTLSEnabled(true);
			mail.setSSLOnConnect(true);
			mail.setAuthenticator(new DefaultAuthenticator(
					Play.application().configuration().getString("EMAIL_USERNAME_ENV"),
					Play.application().configuration().getString("EMAIL_PASSWORD_ENV")
					));
			mail.send();
		} catch (EmailException e) {
			Logger.error(e.getMessage());
		}
	}
	
	public static void tellUserThatOrderIsApproved(String email, double price,
			String restaurantName, List<MetaItem> items) {

		String priceString = Double.toString(price);
		Logger.debug(priceString);

		String details = "<b>Order details: <b>\n\n";

		for (MetaItem item : items) {
			details = details + "\n";
			details = details + item.name + "     "
					+ String.valueOf(item.quantity) + "x" + "     "
					+ String.valueOf(item.totalPrice) + "\n";
		}

		details = details + "\n";
		try {
			HtmlEmail mail = new HtmlEmail();
			mail.setSubject("Your purchase from Njam.ba has been approved!");
			mail.setFrom("Njam.ba <bit.play.test@gmail.com>");
			mail.addTo(email);

			mail.setMsg("");

			mail.setHtmlMsg(String
					.format("<html>"
							+ "<body>"
							+ "<strong> Order from:  </strong>: "
							+ "%s"
							+ "<br></br>"
							+ details
							+ "<br></br>"
							+ "<strong> Amount spent:   </strong>: "
							+ "%s"
							+ "<br></br>"
							+ "<strong> Your order has been successfully approved and payment was completed. Hope you visit us again! </strong>:"
							+ "<br></br>" + "</body>" + "</html>",
							restaurantName, priceString));

			mail.setHostName("smtp.gmail.com");
			mail.setStartTLSEnabled(true);
			mail.setSSLOnConnect(true);
			mail.setAuthenticator(new DefaultAuthenticator(Play.application()
					.configuration().getString("EMAIL_USERNAME_ENV"), Play
					.application().configuration()
					.getString("EMAIL_PASSWORD_ENV")));
			mail.send();
		} catch (EmailException e) {
			Logger.error(e.getMessage());
		}

	}
	
	public static void tellUserThatOrderIsRefused(String email, double price,
			String restaurantName, String message, List<MetaItem> items) {

		String priceString = Double.toString(price);
		Logger.debug(priceString);

		String details = "<b>Order details: <b>" + "\n";

		for (MetaItem item : items) {
			details = details + item.name + "     "
					+ String.valueOf(item.quantity) + "x" + "     "
					+ String.valueOf(item.totalPrice) + "\n";
		}

		details = details + "\n";

		try {
			HtmlEmail mail = new HtmlEmail();
			mail.setSubject("Your purchase from Njam.ba has been refused!");
			mail.setFrom("Njam.ba <bit.play.test@gmail.com>");
			mail.addTo(email);

			mail.setMsg("");

			mail.setHtmlMsg(String
					.format("<html>"
							+ "<body>"
							+ "<br></br>"
							+ "<strong> Unfortunately your order has been refused! </strong>:"
							+ "<br></br>"
							+ "<strong> Order from restaurant:   </strong>: "
							+ "%s" + "<br></br>" + details + "<br></br>"
							+ "<strong> Order price:   </strong>: " + "%s"
							+ "<br></br>" + "<strong> Reason:  </strong>:"
							+ "%s" + "<br></br>" + "</body>" + "</html>",
							restaurantName, priceString, message));

			mail.setHostName("smtp.gmail.com");
			mail.setStartTLSEnabled(true);
			mail.setSSLOnConnect(true);
			mail.setAuthenticator(new DefaultAuthenticator(Play.application()
					.configuration().getString("EMAIL_USERNAME_ENV"), Play
					.application().configuration()
					.getString("EMAIL_PASSWORD_ENV")));
			mail.send();
		} catch (EmailException e) {
			Logger.error(e.getMessage());
		}
	}
	
	public static void sendSudo(String email, String title, String message) {

		try {
			HtmlEmail mail = new HtmlEmail();
			mail.setSubject("Contact Form Notification from: " + email
					+ " Subject: " + title);
			mail.setFrom("Njam.ba <bit.play.test@gmail.com>");
			mail.addTo("Bitter Contact <bit.play.test@gmail.com>");
			mail.addTo(email);
			List<User> adminList = User.findAdmins();

			Iterator<User> sudoIterator = adminList.iterator();
			while (sudoIterator.hasNext()) {
				mail.addTo(sudoIterator.next().email);
			}

			mail.setMsg(message);
			mail.setHtmlMsg(String
					.format("<html><body><strong> %s </strong>: <p> %s </p> </body></html>",
							email, message));
			mail.setHostName("smtp.gmail.com");
			mail.setStartTLSEnabled(true);
			mail.setSSLOnConnect(true);
			mail.setAuthenticator(new DefaultAuthenticator(Play.application()
					.configuration().getString("EMAIL_USERNAME_ENV"), Play
					.application().configuration()
					.getString("EMAIL_PASSWORD_ENV")));
			mail.send();
		} catch (EmailException e) {
			Logger.error(e.getMessage());
		}
	}
	
	public static void sendNewsletterToSubscribers(List<String> emails,
			String title, String messages, List<Meal> meals) {
		try {
			HtmlEmail mail = new HtmlEmail();
			mail.setSubject("Newsletter beta versionen: " + " Subject: "
					+ title);
			mail.setFrom("Njam.ba <bit.play.test@gmail.com>");
			mail.addTo("Bitter Contact <bit.play.test@gmail.com>");

			String message = "";
			Scanner sc = null;
			try {
				sc = new Scanner(new File(
						"./public/newsletter/newsletterTest.html"));
				while (sc.hasNextLine()) {
					message += sc.nextLine();
				}
			} catch (FileNotFoundException e) {
				Logger.error("Error");
			} finally {
				sc.close();
			}
			Document preparedHTML;
			for (String email : emails) {
				mail.addTo(email);
				preparedHTML = getPreparedHTML(email, message, meals);
				preparedHTML.getElementById("appendableText").append(messages);
				mail.setMsg(preparedHTML.toString());
				mail.setHostName("smtp.gmail.com");
				mail.setStartTLSEnabled(true);
				mail.setSSLOnConnect(true);
				mail.setAuthenticator(new DefaultAuthenticator(Play
						.application().configuration()
						.getString("EMAIL_USERNAME_ENV"), Play.application()
						.configuration().getString("EMAIL_PASSWORD_ENV")));
				mail.send();
			}
		} catch (EmailException e) {
			Logger.error(e.getMessage());
		}

	}
	
	private static Document getPreparedHTML(String email,String html, List<Meal> meals){
		
		Document doc = Jsoup.parse(html);

		Element mealOneName = doc.getElementById("meal1-name");
		mealOneName.appendText(meals.get(0).name);
		Element mealOnePrice = doc.getElementById("meal1-price");
		mealOnePrice.appendText(meals.get(0).price + "KM");

		Element mealTwoName = doc.getElementById("meal2-name");
		mealTwoName.appendText(meals.get(1).name);
		Element mealTwoPrice = doc.getElementById("meal2-price");
		mealTwoPrice.appendText(meals.get(1).price + "KM");
		
		Element unsubscribe = doc.getElementById("unsubscribe");
		String token = Newsletter.findByEmail(email).confirmationString;
		String unsubscribeNewsletter = "http://localhost:9000/unsubscribe/" + token;
		unsubscribe.attr("href", unsubscribeNewsletter);

		return doc;
	}
	
	
	public static void sendRefundEmailtoUser(String email, String message){
		
		try{
		HtmlEmail mail = new HtmlEmail();
		mail.setSubject("Njam.ba repayment mail");
		mail.setFrom("Njam.ba <bit.play.test@gmail.com>");
		mail.addTo("Bitter Contact <bit.play.test@gmail.com>");
		mail.addTo(email);
		
		mail.setMsg(message);
		mail.setHtmlMsg(String.format("<html><body><strong> %s </strong> <p> %s </p> <p> %s </p> </body></html>","Your claim for refund is succesfully finished!", "Please come and visit us again!", message));
		mail.setHostName("smtp.gmail.com");
		mail.setStartTLSEnabled(true);
		mail.setSSLOnConnect(true);
		mail.setAuthenticator(new DefaultAuthenticator(Play
				.application().configuration()
				.getString("EMAIL_USERNAME_ENV"), Play.application()
				.configuration().getString("EMAIL_PASSWORD_ENV")));
		mail.send();		
		} catch (EmailException e) {
			Logger.error(e.getMessage());
		}
	}
	
	public static void sendRefundEmailtoRestaurant(String email, String message) {

		try {
			HtmlEmail mail = new HtmlEmail();
			mail.setSubject("Njam.ba repayment mail");
			mail.setFrom("Njam.ba <bit.play.test@gmail.com>");
			mail.addTo("Bitter Contact <bit.play.test@gmail.com>");
			mail.addTo(email);

			mail.setMsg(message);
			mail.setHtmlMsg(String
					.format("<html><body><strong> %s </strong> <p> %s </p> <p> %s </p> </body></html>",
							"Customer aseked for refund of money. We succefully return money to cutomer, because order delivery time was to long! Please, be careful next time!",
							"If you have any questions contact us", message));
			mail.setHostName("smtp.gmail.com");
			mail.setStartTLSEnabled(true);
			mail.setSSLOnConnect(true);
			mail.setAuthenticator(new DefaultAuthenticator(Play.application()
					.configuration().getString("EMAIL_USERNAME_ENV"), Play
					.application().configuration()
					.getString("EMAIL_PASSWORD_ENV")));
			mail.send();
		} catch (EmailException e) {
			Logger.error(e.getMessage());
		}
	}
}