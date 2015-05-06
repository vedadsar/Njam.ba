package models;
 
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
 
import javax.persistence.*;
import javax.validation.constraints.NotNull;
 
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Pattern;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;
 
@Entity
public class Blogger extends Model {
 
	@Id
	public int id;
 
	@Required
	@MinLength(6)
	@MaxLength(165)
	@Pattern(value = "^[A-Za-z0-9 ,.!?_]*[A-Za-z0-9][A-Za-z0-9 ,.!?_]*$", message = "Title not valid, only letters and numbers alowed.")
	public String title;
 
	@Required
	@MinLength(6)
	@MaxLength(165)
	@Pattern(value = "^[A-Za-z0-9 ,.!?_]*[A-Za-z0-9][A-Za-z0-9 ,.!?_]*$", message = "Subtitle not valid, only letters and numbers alowed.")
	public String subtitle;
 
	@Column(columnDefinition = "TEXT")
	@Required
	public String content;
 
	public String image;
 
	public Date date;
 
	@Pattern(value = "^[A-Za-z0-9 ,_]*[A-Za-z0-9][A-Za-z0-9 ,_]*$", message = "Tags input not valid. Only characters and numbers alowed."
			+ " Tags have to be separated with space.")
	public String tag;
 
	public static List<String> tags = new ArrayList<String>();
 
	@OneToOne
	@NotNull
	public User creator;
 
 
	public static final String NO_POST_IMAGE = "NO_IMGE";
	public static final String POST_IMAGE_FOLDER = "blog" + File.separator;
 
	public static Finder<Integer, Blogger> find = new Finder<Integer, Blogger>(
			Integer.class, Blogger.class);
 
	public Blogger() {
		this.title = "Unknown";
		this.subtitle = "Unknown";
		this.content = "Unknown";
		this.image = "images/blog/no-blog.jpg";
		this.date = new Date();
		this.tag = "Unknown";
		this.creator = new User("email@mail.com", "123456");
	}
 
	public Blogger(String title, String subtitle, String tag, String content,
			User creator) {
		this.title = title;
		this.subtitle = subtitle;
		this.content = content;
		this.image = "images/blogPicture/no-img.jpg";
		this.date = new Date();
		this.tag = tag;
		this.creator = creator;
	}
 
	public static boolean createPost(String title, String subtitle, String tag,
			String content, User creator) {
		Blogger post = new Blogger(title, subtitle, tag, content, creator);
		post.save();
		return true;
	}
 
	public static List<Blogger> allReversePost() {
		List<Blogger> all = find.all();
		if (all != null) {
			Collections.reverse(all);
			return all;
		}
		return null;
	}
 
	public static Blogger findPostById(int id) {
		return find.byId(id);
	}
 
	public static List<String> tags(String tagByPost) {
		if (tagByPost == null)
			return null;
		String[] splitTags = tagByPost.split(",");
		for (String tag : splitTags) {
			tags.add(tag);
		}
		return tags;
	}
 
	public static List<String> allTags() {
		return tags;
	}
	public static List<Blogger> allTagsList() {
		return find.findList();
	}
 
	
	public static List<Blogger> allTagsByPost(int id) {
		Blogger blog = find.byId(id);
		List<Blogger> tagsByPost = find.where().eq("tag", blog.tag).findList();
		return tagsByPost;
	}
}