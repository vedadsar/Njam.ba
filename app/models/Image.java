package models;

import javax.persistence.*;

import play.data.validation.Constraints.*;
import play.db.ebean.Model;

import java.util.List;

/**
 * 
 * @author Gorjan
 *
 */

@Entity
public class Image extends Model {
	
	@Id
	public int id;
	@Required
	public String imgLocation;
	
	
	public static Finder<Integer, Image> find = new Finder<Integer, Image>(Integer.class, Image.class);
	
	

	public Image( String imgLocation) {
	
		this.imgLocation=imgLocation;
	}
	
	
	
	public static boolean createRestaurantImg(String imgLocation) {
		Image img = new Image(imgLocation);
		img.save();
		return true;
	}
	
	public static boolean createMealImg(String imgLocation) {
		Image img = new Image(imgLocation);
		img.save();
		return true;
	}

	public static void delete(int id) {
		find.byId(id).delete();
	}
	

	public static List<Image>  findAllByOwnerNoMeal(Restaurant owner) {

		 List<Image> gallery =find.where().eq("restaurant.id", owner.id).eq("meal.id", null).findList();
		 
		return gallery;
	}
	
	
	public static List<Image>  findAllByOwnerandMeal(Restaurant owner, Meal m) {

		 List<Image> gallery =find.where().eq("Restaurant.id", owner.id).eq("meal.id", m.id).findList();
		 
		return gallery;
	}
	
	
	public static List<Image> all() {
		return find.findList();
	}
	
}
