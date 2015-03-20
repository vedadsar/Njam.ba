package controllers;

import play.mvc.*;
import models.*;
import play.data.DynamicForm;
import play.data.Form;
import Utilites.AdminFilter;
import Utilites.Session;

/**
 * 
 * @author neldindzekovic
 *
 */
public class FaqController extends Controller {
	
	
	
	/**
	 * This method creates new FAQ.
	 * @return Redirects to FAQ page.
	 */
	@Security.Authenticated(AdminFilter.class)
	public static Result create() {
		DynamicForm inputForm = Form.form().bindFromRequest();
		
		String question = inputForm.data().get("question");
		String answer = inputForm.data().get("answer");
		
		boolean success = Faq.create(question, answer);
		
		if (success == true) {
			flash("successFaq", "Succesfuly created FAQ!");

			return redirect("/admin/" + Session.getCurrentUser(ctx()).email);
		}
		
		flash("failFaq", "Creating FAQ failed!");
		return redirect("/admin/" + Session.getCurrentUser(ctx()).email);

	}
	
	/**
	 * This method deletes FAQ.
	 * @param id Id of FAQ to delete.
	 * @return Redirects to FAQ page.
	 */
	@Security.Authenticated(AdminFilter.class)
	public static Result delete(int id) {
		Faq.delete(id);
		flash("deleteFaq", "Succesfuly deleted FAQ!");

		return redirect("/admin/" + Session.getCurrentUser(ctx()).email);
	}
	
	/**
	 * This method edits FAQ.
	 * @param id Id of FAQ to edit.
	 * @return Redirects to FAQ page.
	 */
	@Security.Authenticated(AdminFilter.class)
	public static Result edit(int id) {
		DynamicForm inputForm = Form.form().bindFromRequest();
		Faq f = Faq.findById(id);
		String question = inputForm.data().get("question");
		String answer = inputForm.data().get("answer");
		
		boolean success = Faq.edit(f, question, answer);
		
		if (success == true) {
			flash("successEditFaq", "Succesfuly edited FAQ!");
			return redirect("/admin/" + Session.getCurrentUser(ctx()).email);
		}
		
		flash("failEditFaq", "Editing FAQ failed!");		

		return redirect("/admin/" + Session.getCurrentUser(ctx()).email);

	}
}

