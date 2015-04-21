package controllers;

import Utilites.MailHelper;
import models.Comment;
import models.Newsletter;
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
}
