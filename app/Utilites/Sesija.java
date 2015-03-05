package Utilites;


import controllers.routes;
import models.User;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

public class Sesija extends Security.Authenticator{

	@Override
	public  String getUsername(Context ctx){
		if(!ctx.session().containsKey("email"))
			return null;
		String email = ctx.session().get("email");
		User u = User.find(email);
		if(u != null){
			return u.email;
		}
		return null;	
	}
	
	@Override
	public  Result onUnauthorized(Context ctx){
		return redirect(routes.Application.index());		
	}
	
	public static User getCurrentUser(Context ctx){
		if(!ctx.session().containsKey("email"))
			return null;
		String email =ctx.session().get("email");
		User u = User.find(email);
		return u;
	}
	
	/**
	 * Method which returns role of user.
	 * Sudo, Customer or restaurant.
	 * @param ctx
	 * @return
	 */
	public static String getCurrentRole(Context ctx){
		if(!ctx.session().containsKey("email"))
			return null;		//No active session
		String email = ctx.session().get("email");
		User u = User.find(email);
		return u.role;
	}
}