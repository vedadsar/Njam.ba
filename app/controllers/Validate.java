package controllers;

import models.Newsletter;
import models.User;
import play.Logger;
import play.data.Form;
import play.data.validation.Constraints;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.*;

public class Validate extends Controller {

	public static Result validateEmail(String confirmationString) {

		User user = User.findByConfirmationString(confirmationString);

		if (confirmationString == null) {
			flash("error", Messages.get("error"));
			return redirect("/");
		}

		if (User.confirm(user)) {
			flash("success", Messages.get("Successfully validated"));
			return redirect("/login");
		} else {
			flash("errorLink", Messages.get("error.confirm"));
			return redirect("/");
		}
	}

	public static Result validateNewslettter(String confirmationString) {
		Newsletter newsletter = Newsletter
				.findByConfirmationString(confirmationString);
		if (confirmationString == null) {
			flash("errorS", Messages.get("error"));
			return redirect("/");
		}
		if (Newsletter.confirm(newsletter) == true) {
			flash("successSubscribed", Messages.get("Successfully subscribed"));
			return redirect("/");
		} else {
			flash("errorNewslLink", Messages.get("error.confirm"));
			return redirect("/");
		}
	}
	
	public static Result unsubscribe(String confirmationString) {
		Newsletter newsletter = Newsletter
				.findByConfirmationString(confirmationString);
		if (newsletter == null) {
			flash("errorL", Messages.get("error"));
			return redirect("/");
		}
		if (Newsletter.unsubscribe(newsletter) == true) {
			flash("SucessUns", "Unsubsribed");
			return redirect("/");
		} else {
			flash("errorUsub", "Error while unsubscribing");
			return redirect("/");
		}
	}
}