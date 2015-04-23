package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import play.Logger;
import play.data.format.Formats.DateTime;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
	
	@Entity
	public class Comment extends Model {

	@Required
	@Id
	public int id;
	@Required
	@ManyToOne
	public User author;
	@Required
	@DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date dateCreation;
	@Required
	public String title;
	@Required
	public String content;
	public int rating;
	@ManyToOne
	public Meal meal;
	
	public static Finder<Integer, Comment> find = new Finder<Integer, Comment>(Integer.class, Comment.class);


	public Comment(User author, String title, String content, int rating, Meal meal) {
		this.author = author;
		this.dateCreation = new Date();
		this.title = title;
		this.content = content;
		this.meal = meal;
		this.rating = rating;
		
	}
		
	public static boolean create(User author, Date dateCreation, String title,
			String content, int rating, Meal meal) {
		Comment comment = find.where().eq("title", title).where()
				.eq("dateCreation", dateCreation).findUnique();
		if (comment != null) {
			return false;
		}
		comment = new Comment(author, title, content, rating, meal);
		comment.save();
		return true;
	}	
	
	public static Comment find(int id){
		return find.byId(id);
	}
	
	public static List <Comment> findByMeal(int id){
		Meal meal = Meal.find(id);
		List <Comment> comments = find.where().eq("meal", meal).findList();
		return comments;
	}
	
	public static int averageRating(int id) {
		int counter = 0;
		Meal meal = Meal.find(id);
		List<Comment> comments = find.where().eq("meal", meal).findList();
		if (comments.size() == 0) {
			return 6;
		}
		for (Comment comment : comments) {
			counter += comment.rating;
		}
		return counter / comments.size();
	}
	
	public static boolean userReview(int id, String email) {
		Meal meal = Meal.find(id);
		User user = User.find(email);
		List<Comment> comments = find.where().eq("meal", meal).findList();
		for (Comment comment : comments) {
			if(comment.author.id == user.id){
				return true;
			}
		}
		return false;
	}
}