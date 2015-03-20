package controllers;

import play.mvc.*;
import models.*;
import play.data.DynamicForm;
import play.data.Form;
import Utilites.AdminFilter;
import Utilites.Session;

public class FaqController extends Controller {
	
	
	/**
	 * 
	 * @return
	 */
	@Security.Authenticated(AdminFilter.class)
	public static Result create() {
		
		DynamicForm form = Form.form().bindFromRequest();
		String question = form.data().get("question");
		String answer = form.data().get("answer");
		
		boolean success = Faq.create(question, answer);
		
		if (success == true) {
			flash("successFaq", "Succesfuly created FAQ!");
			return redirect("/admin/" + Session.getCurrentUser(ctx()).email);
		}
		
		flash("failFaq", "Creating FAQ failed!");
		return redirect("/admin/" + Session.getCurrentUser(ctx()).email);
	}
	
	@Security.Authenticated(AdminFilter.class)
	public static Result delete(int id) {
		Faq.delete(id);
		flash("deleteFaq", "Succesfuly deleted FAQ!");
		return redirect("/admin/" + Session.getCurrentUser(ctx()).email);
	}
}
