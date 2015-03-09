package controllers;

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
//            		badRequest(confirm.render(confirmationString));
        }
        
        if (User.confirm(user)) {
            flash("success", Messages.get("Successfully validated"));
            return TODO;
//            		ok(confirm.render(confirmationString));
        }

//        session("email", user.email);
        flash("success", Messages.get("Successful", user.email));
        return TODO;
//        		ok(confirm.render(confirmationString));
    }
}
