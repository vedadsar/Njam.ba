package controllers;

import play.mvc.Controller;
import play.mvc.*;
import models.*;
import play.data.Form;
import Utilites.AdminFilter;

public class FaqController extends Controller {
	
	static Form<Faq> inputForm = new Form<Faq>(Faq.class);
	
	@Security.Authenticated(AdminFilter.class)
	public static Result create() {
		String question = inputForm.bindFromRequest().field("question").value();
		String answer = inputForm.bindFromRequest().field("answer").value();
		
		boolean success = Faq.create(question, answer);
		
		if (success == true) {
			flash("successFaq", "Succesfuly created FAQ!");
			return redirect("/admin/faq");
		}
		
		flash("failFaq", "Creating FAQ failed!");
		return redirect("/admin/faq");
	}
	
	@Security.Authenticated(AdminFilter.class)
	public static Result delete(int id) {
		Faq.delete(id);
		flash("deleteFaq", "Succesfuly deleted FAQ!");
		return redirect("/admin/faq");
	}
	
}
