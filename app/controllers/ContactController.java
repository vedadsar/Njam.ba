package controllers;

import Utilites.MailHelper;
import Utilites.Session;

import com.fasterxml.jackson.databind.JsonNode;

import models.User;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.libs.F.Promise;
import play.libs.F.Function;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;

/**
 * Controller for Contact form.
 * @author amrapoprzanovic
 *
 */
public class ContactController extends Controller{ 
	
	static String email = null;

	public static class Contact {
		@Required
		@Email
		public String email;
		@Required
		public String title;
		@Required
		public String message;
	}
	
	/**
	 * Method that brought you on contact view.
	 * @return contact form view.
	 */
	public static Result contacts(){
		email = session("email");
		return ok(contact.render(new Form<Contact>(Contact.class), email));
	}
	
	/**
	 * Method taht sends email.
	 * @return contactt page with message that email was sent.
	 */
	public static Promise<Result> sendMail() {
		//need this to get the google recapctha value
		final DynamicForm temp = DynamicForm.form().bindFromRequest();
		
		/* send a request to google recaptcha api with the value of our secret code and the value
		 * of the recaptcha submitted by the form */
		Promise<Result> holder = WS
				.url("https://www.google.com/recaptcha/api/siteverify")
				.setContentType("application/x-www-form-urlencoded")
				.post(String.format("secret=%s&response=%s",
						//get the API key from the config file
						Play.application().configuration().getString("recaptchaKey"),
						temp.get("g-recaptcha-response")))
				.map(new Function<WSResponse, Result>() {
					//once we get the response this method is loaded
					public Result apply(WSResponse response) {
						//get the response as JSON
						JsonNode json = response.asJson();
						Form<Contact> submit = Form.form(Contact.class)
								.bindFromRequest();
						
						//check if value of success is true
						if (json.findValue("success").asBoolean() == true
								&& !submit.hasErrors()) {

							Contact newMessage = submit.get();
							String email = newMessage.email;
							String title = newMessage.title;
							String message = newMessage.message;

							flash("success", "Message sent");
							MailHelper.send(email, message);
							email = session("email");
							return redirect("/contact");
						} else {
							flash("error", "There has been a problem");
							return ok(contact.render(submit, email));
						
						}
					}
				});
		//return the promisse
		return holder;
	}
}
