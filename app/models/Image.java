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
	
	@ManyToOne(cascade=CascadeType.ALL) 
	public Restaurant restaurant;
	
	@ManyToOne(cascade=CascadeType.ALL) 
	public Meal meal;
	
	public static Finder<Integer, Image> find = new Finder<Integer, Image>(Integer.class, Image.class);
	
	
	public Image(String imgLocation) {
		this.imgLocation = imgLocation;
			
	}
		
	public static boolean create(String imgLocation) {
		Image img = new Image(imgLocation);
		img.save();
		return true;
	}
	


	public static void delete(int id) {
		find.byId(id).delete();
	}
	

	public static List<Image>  findAllByOwner(Restaurant owner) {

		 List<Image> gallery =find.where().eq("restaurant", owner).findList();
		 
		return gallery;
	}
	
	
	public static List<Image> all() {
		return find.findList();
	}
	
}
