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

	/**
	 * Constructor for creating object Image by passing it as parameter name of location were we save image.
	 * @param imgLocation - File name where we are saving image.
	 */
	public Image(String imgLocation) {
		this.imgLocation = imgLocation;
	}

	/**
	 * This method deletes image from file where we are saving image.
	 * As parameter we are sending name of location of image.
	 * @param imgLocation- File name where we are saving image.
	 */
	public static void deleteImg(String imgLocation) {
		Image temp= find.where().eq("imgLocation",imgLocation).findUnique();
		deleteImgHdd(temp);
		temp.delete();
	}

	/**
	 * This method deletes file where we put image.
	 * As parameter we are sending  image.
	 * @param harakiri - Image that we want to delete.
	 */
	public static void deleteImgHdd(Image harakiri) {
		File toDelete = new File("public"+System.getProperty("file.separator")+harakiri.imgLocation);
		toDelete.delete();

	}

	/**
	 * Method that replace old image with new one by deleting old one 
	 * and adding new one.
	 * @param id - id of image that we want to delete.
	 * @param location - File name where image is placed.
	 */
	public static void updateImg(int id, String location) {
		Image temp = find.byId(id);
		deleteImgHdd(temp);
		temp.imgLocation = location;
		temp.save();
	}

	/**
	 * List of all images that one user is having.
	 * @return list of images.
	 */
	public static List<Image> all() {
		return find.findList();
	}
	
	/**
	 * Finds image by forwarding image id.
	 * @param id - of image.
	 * @return return Object Image.
	 */
	public static Image findById(int id){
		return find.byId(id);
	}

}
