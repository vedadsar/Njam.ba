package controllers;

import models.Pin;
import models.User;
import Utilites.Session;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.*;

public class PinController extends Controller {

	public static Result checkPin(String email) {
		User user = Session.getCurrentUser(ctx());
		DynamicForm form = Form.form().bindFromRequest();
		String pin = form.data().get("pin").toString();
		if (Pin.getPinCode(user.id).equals(pin)) {
			user.pin.validated = true;
			user.update();
			flash("smsValidated", "Sucess!");
			return redirect("/user/" + user.email);
		}
		flash("ErrorPin", "Wrong PIN! Try again");
		return redirect("/");
	}
}