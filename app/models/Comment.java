package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

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
	@ManyToMany
	public Meal meal;
	
	public static Finder<Integer, Comment> find = new Finder<Integer, Comment>(Integer.class, Comment.class);


	public Comment(User author, String title, String content, Meal meal) {
		this.author = author;
		this.dateCreation = new Date();
		this.title = title;
		this.content = content;
		this.meal = meal;
	}
		
	public static boolean create(User author, Date dateCreation, String title,
			String content, Meal meal) {
		Comment comment = find.where().eq("title", title).where()
				.eq("dateCreation", dateCreation).findUnique();
		if (comment != null) {
			return false;
		}
		comment = new Comment(author, title, content, meal);
		comment.save();
		return true;
	}	
	
	public static Comment find(int id){
		return find.byId(id);
	}
}