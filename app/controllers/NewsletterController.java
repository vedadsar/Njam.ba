package controllers;

import java.util.List;

import Utilites.MailHelper;
import models.Comment;
import models.Meal;
import models.Newsletter;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class NewsletterController extends Controller {
	
	static Form<Newsletter> newsletter = new Form<Newsletter>(Newsletter.class);

	public static Result subscribe() {

		String email = newsletter.bindFromRequest().field("email").value();
		if (email == null) {
			return redirect("/");
		}
		if (Newsletter.isSubscribed(email) == true) {
			flash("Subscribed", "Already Subscribed");
			return redirect("/");
		}
		Newsletter.subscribeToNewsletter(email);
		Newsletter news = Newsletter.findByEmail(email);
		String token = "http://localhost:9000/confirmNewsletter/" + news.confirmationString;
		MailHelper.send(email, token);
		flash("SucessSub", "Sucessfully subscribed! Please check your email to confirm...");
		return redirect("/");
	}
	
	public static Result unsubscribe(String confirmationString) {
		try {
			Newsletter newsletter = Newsletter
					.findByConfirmationString(confirmationString);
			Newsletter.unsubscribe(newsletter);
			flash("SucessS", "Unsubsribed");
			return redirect("/");
		} catch (Exception e) {
			flash("errorUsub", "Error while unsubscribing");
			return redirect("/");
		}
	}
	
	public static Result sendNewsletter() {

		List<String> subscribers = Newsletter.findAllSubscribers();
		List<Meal> meals = Meal.all();
		try {
			MailHelper.sendNewsletterToSubscribers(subscribers, "test",
					"newsletter test", meals);
			Logger.info("Uspjesno");
			return redirect("/");
		} catch (Exception e) {
			Logger.info("Error" + e.getMessage());
			e.printStackTrace();
		}
		Logger.info("Propalo slanje");
		return redirect("/");
	}
}