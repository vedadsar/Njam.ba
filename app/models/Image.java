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
	
		

	public static void delete(int id) {
		find.byId(id).delete();
	}
	

	public static List<Image>  findAllByOwnerNoMeal(Restaurant owner) {

		 List<Image> gallery =find.where().eq("restaurant_id", owner.id).findList();
		 
		return gallery;
	}
	
	
	public static List<Image>  findAllByMeal(Meal meal) {

		 List<Image> gallery =find.where().eq("meal_id", meal.id).findList();
		 
		return gallery;
	}
	
	
	public static List<Image> all() {
		return find.findList();
	}
	
}
