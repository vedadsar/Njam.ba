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
		MailHelper.send(email, "confirmation string");
		return redirect("/");
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
