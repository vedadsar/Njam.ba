package controllers;
 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
 
import models.*;
import Utilites.AdminFilter;
import Utilites.RestaurantFilter;
import Utilites.Session;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;
 
public class BlogController extends Controller {
 
	static String email;
//	public static List<String> tags = new ArrayList<String>();
 
	public static Result toBlog(){
		List<Blogger> blog = Blogger.allReversePost();
 
		email = session("email");		
		return ok(views.html.blog.blog.render(email, blog));
	}
	
	public static Result showOnePost(int id){
		List<Blogger> bloggers = Blogger.allReversePost();
		Iterator<Blogger> iter = bloggers.iterator();
		
		while(iter.hasNext()){
			Blogger blog = iter.next();
			if (blog.id==id){
				return ok(views.html.blog.post.render(email, blog));
			}
		}
		return redirect("/blog");
	}
	
	public static Result searchPost(String q){
		List<Blogger> blog = Blogger.find.where().ilike("title",  "%"+q+"%").findList();
		return ok(views.html.blog.blog.render(email, blog));
	}
	
	public static Result byTag(String tag){
		return TODO;
		
	}
	
	public static Result toCreatePost(){
		return ok(views.html.blog.createPost.render(email));
	}
	
	@Security.Authenticated(AdminFilter.class)
	public static Result createPost() {
		DynamicForm form = Form.form().bindFromRequest();
 
		String title = form.data().get("title");
		String subtitle = form.data().get("subtitle");
		String tag = form.data().get("tag");
		String content = form.data().get("content");
		User user = User.find("dastko@njam.ba");
 
		if (Blogger.createPost(title, subtitle, tag, content, user) == true) {
			Blogger.tags(tag);
			flash("SuccessCreatePost", "You just successfully cretaed post!");
			return redirect("/blog");
		}
 
		flash("WarningCreatePost",
				"Your post was not created, please try again!");
		return redirect("/blog/createPost");
	}
	
	public static Result toEditPost(int id){
		return TODO;
	}
	public static Result editPost(int id){
		return TODO;
	}
	
	public static Result deletePost(int id){
		return TODO;
	}
}