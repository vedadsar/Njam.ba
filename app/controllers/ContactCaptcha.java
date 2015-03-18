package controllers;

import Utilites.MailHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;


import models.Meal;
import models.Restaurant;
import models.User;
import play.Play;
import play.data.Form;
import play.i18n.Messages;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.libs.ws.WS;
import play.libs.ws.WSResponse;
import play.mvc.*;
import views.html.*;
import Utilites.AdminFilter;
import Utilites.Session;
import play.data.DynamicForm;
import play.db.ebean.Model.Finder;
import Utilites.*;

public class ContactCaptcha {
	
	
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
//						Form<Contact> submit = Form.form(Contact.class)
//								.bindFromRequest();
//						
//						//check if value of success is true
//						if (json.findValue("success").asBoolean() == true
//								&& !submit.hasErrors()) {
//
//							Contact newMessage = submit.get();
//							String email = newMessage.email;
//							String message = newMessage.message;
//
//							flash("success", "Message sent");
//							MailHelper.send(email, message);
//							return redirect("/contact");
//						} else {
//							flash("error", "There has been a problem");
//							return ok(contact.render(submit));
//
//						}
					}
				});
		//return the promisse
		return holder;
	}
	
}
