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
	public static Result newComment(int id){
		
		User user = Session.getCurrentUser(ctx());
		String title = comment.bindFromRequest().field("title").value();
		String content = comment.bindFromRequest().field("content").value();
		Date date = new Date();

		Meal meal = Meal.find(id);
		List<Image> imgs = meal.image;
		Restaurant restaurant = Restaurant.findByMeal(meal);
		List<Restaurant> restaurants = Restaurant.all();

		if (Comment.create(user, date, title, content, meal) == true) {
//			List<Comment> comments = Comment.findByMeal(meal);
			List<Comment> comments = Comment.find.findList();
			email = session("email");
			return ok(views.html.restaurant.mealView.render(email, meal, imgs,
					restaurant, restaurants, comments));
		}
		flash("ErrorComment", "Error");
		return redirect("/");

	}
	
	
}