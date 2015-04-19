package controllers;

import java.util.Date;
import java.util.List;

import models.Comment;
import models.Image;
import models.Meal;
import models.Restaurant;
import models.User;
import Utilites.Session;
import Utilites.UserFilter;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class CommentController extends Controller {

	static Form<Comment> comment = new Form<Comment>(Comment.class);
	static String email = null;

	@Security.Authenticated(UserFilter.class)
	public static Result newComment(int id) {

		User user = Session.getCurrentUser(ctx());
		String title = comment.bindFromRequest().field("title").value();
		String content = comment.bindFromRequest().field("content").value();
		Date date = new Date();
		Meal meal = Meal.find(id);

		if (Comment.create(user, date, title, content, meal) == true) {
			email = session("email");
			return redirect("/mealView/" + meal.id);
		}
		flash("ErrorComment", "Error");
		return redirect("/");
	}
}