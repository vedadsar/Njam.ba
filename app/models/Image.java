package models;

import javax.persistence.*;

import play.data.validation.Constraints.*;
import play.db.ebean.Model;

import java.io.File;
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
	
	


	
	@ManyToOne
	public Meal meal;
	
	@ManyToOne
	public Restaurant restaurant;

	public static Finder<Integer, Image> find = new Finder<Integer, Image>(
			Integer.class, Image.class);

	
	public Image(String imgLocation) {
		this.imgLocation = imgLocation;
	}

	public static void deleteImg(String imgLocation) {
		Image temp= find.where().eq("imgLocation",imgLocation).findUnique();
		deleteImgHdd(temp);
		temp.delete();
	}

	public static void deleteImgHdd(Image harakiri) {
		File toDelete = new File("public"+System.getProperty("file.separator")+harakiri.imgLocation);
		toDelete.delete();

	}

	public static void updateImg(int id, String location) {
		Image temp = find.byId(id);
		deleteImgHdd(temp);
		temp.imgLocation = location;
		temp.save();
	}

	public static List<Image> all() {
		return find.findList();
	}
	public static Image findById(int id){
		return find.byId(id);
	}

}
