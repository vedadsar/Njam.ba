package controllers;

import Utilites.RestaurantFilter;
import Utilites.Session;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;
import models.Image;
import models.User;
import models.Meal;
import models.Restaurant;
import play.Logger;
import play.Play;
import play.data.DynamicForm;
import play.data.Form;
import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model.Finder;
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Security;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import scala.collection.parallel.ParSeqLike.LastIndexWhere;
import views.html.*;
import views.html.admin.*;
import models.*;
import Utilites.RestaurantFilter;

import java.awt.Graphics;
import java.awt.Graphics2D;

import com.avaje.ebean.config.EncryptDeploy.Mode;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.Files;

import java.util.Collections;

import javax.imageio.ImageIO;

import org.imgscalr.*;
import org.imgscalr.Scalr.Method;

import com.cloudinary.*;

public class FileUpload extends Controller {

	private static Meal m;
	private static User u;
	private static File image;
	private static String folderId;
	private static String imageFolder;
	private static String imgFileName;
	private static FilePart filePart;
	private static Map<String, String> cloudConf = new HashMap<String, String>();

	/**
	 * 
	 * @param Path
	 *            String building which removes all invalid characters from
	 *            timestamp using regex
	 * @param
	 * @param
	 * @return Finished String
	 */

	@Security.Authenticated(RestaurantFilter.class)
	public static String locationPath(String folderId, String imageFolder,
			String fileName) {

		String saveLocation = "images" + System.getProperty("file.separator")
				+ "UserId" + folderId + System.getProperty("file.separator")
				+ imageFolder + System.getProperty("file.separator")
				+ new Date().toString().replaceAll("\\D+", "") + fileName;

		return saveLocation;
	}

	/**
	 * Method gets keys for Cloudinary File transfer.
	 * 
	 */

	public static void cloudConfPut() {

		cloudConf.put("cloud_name", Play.application().configuration()
				.getString("CLOUD_API_NAME"));
		cloudConf.put("api_key",
				Play.application().configuration().getString("CLOUD_API_KEY"));
		cloudConf.put("api_secret", Play.application().configuration()
				.getString("CLOUD_API_SECRET"));
	}

	@Security.Authenticated(RestaurantFilter.class)
	public static Result saveMealIMG(int id) {

		cloudConfPut();


		u = Session.getCurrentUser(ctx());
		m = Meal.find(id);

		folderId = String.valueOf(u.restaurant.id);
		imageFolder = "meal";

		// Picture count check
		if (sizeOfList(imageFolder) < 5) {

			MultipartFormData body = request().body().asMultipartFormData();
			
			filePart = body.getFile("image");
			
			
		try {
					
			  if (!filePart.getContentType().contains("image")) {
				    return errorResponce("Not valid image file!");
			   }
			  image = filePart.getFile();
		

		

		    	} catch (NullPointerException e) {
				Logger.debug(("Empty File Upload" + e));

				return ok(views.html.admin.wrong
						.render("OOPS you didnt send a file"));

			}
			

			imgFileName = filePart.getFilename();

			String saveLocation = locationPath(folderId, imageFolder,
					imgFileName);
			saveLocation = saveLocation.substring(0,
					saveLocation.lastIndexOf('.'));
			Logger.debug(saveLocation);

			try {

				Cloudinary cloudinary = new Cloudinary(cloudConf);

				Map params = Cloudinary.asMap("public_id", saveLocation);
				Map uploadResult = cloudinary.uploader().upload(image, params);

				Logger.debug((String) uploadResult.get("url"));

				Meal.createMealImg(m, (String) uploadResult.get("url"));

			} catch (IOException e) {
				Logger.debug(e.toString());
			}

			return ok(views.html.restaurant.fileUploadMeal.render("",
					Session.getCurrentUser(ctx()).email, m, Restaurant.all(),
					m.image));

		} else
			return ok(views.html.admin.wrong.render("LIMIT HAS BEEN REACHED"));
	}

	private static Result errorResponce(String text) {
		return ok(views.html.admin.wrong.render(text));

	}

	/**
	 * Method which saves current active restaurants profile images
	 * 
	 * @return
	 */

	@Security.Authenticated(RestaurantFilter.class)
	public static Result saveRestaurantIMG() {
		User u = Session.getCurrentUser(ctx());
		Restaurant restaurant = u.restaurant;
		List<TransactionU> tobeapproved = restaurant.toBeApproved;

		folderId = String.valueOf(u.restaurant.id);
		imageFolder = "restaurant";
		// Picture count check
		if (sizeOfList(imageFolder) < 3) {
			MultipartFormData body = request().body().asMultipartFormData();
					
			try {
				filePart = body.getFile("image");
				if (!filePart.getContentType().contains("image")) {
					return errorResponce("Not valid image file!");
				}
				image = filePart.getFile();
				imgFileName = filePart.getFilename();
			
			} catch (NullPointerException e) {
				Logger.debug(("Empty File Upload" + e));
				return ok(views.html.admin.wrong
						.render("OOPS you didnt send a file"));
			}


			String saveLocation = locationPath(folderId, imageFolder,
					imgFileName);
			
			

			try {
				
				Cloudinary cloudinary = new Cloudinary(cloudConf);

				Map params = Cloudinary.asMap("public_id", saveLocation);
				Map uploadResult = cloudinary.uploader().upload(image, params);

				Logger.debug((String) uploadResult.get("url"));
								
				// Image file location saving to DB
				Restaurant.createRestaurantImg(u.restaurant, (String) uploadResult.get("url"));
				
			} catch (IOException e) {
				Logger.debug(e.toString());
			}
		

			Logger.debug("Passed resize?");

			List<Restaurant> restaurants = restaurant.all();
			String email = Session.getCurrentUser(ctx()).email;

			return ok(views.html.restaurant.restaurantOwner.render(email,
					restaurant.meals, restaurant, restaurants, tobeapproved));

		} else
			return ok(views.html.admin.wrong.render("LIMIT HAS BEEN REACHED"));
	}

	public static boolean isEmpty(Collection coll) {
		return (coll == null || coll.isEmpty());
	}

	/**
	 * Delete Image method Method receives the id of Meal to be changed and the
	 * string name of the location File in DB
	 * 
	 * @param imgLocation
	 * @param mealID
	 * @return
	 */

	public static Result deleteImg(String imgLocation, int mealID) {
		Meal m = Meal.find(mealID);
		Image.deleteImg(imgLocation);

		return ok(views.html.restaurant.fileUploadMeal.render("",
				Session.getCurrentUser(ctx()).email, m, Restaurant.all(),
				m.image));

	}

	/**
	 * Image array list checker for classes Meal and Restaurant
	 * 
	 * @param modelType
	 * @return
	 */

	public static int sizeOfList(String modelType) {
		if (modelType.equals("meal")) {
			if (isEmpty(m.image)) {
				return 0;
			} else
				Logger.debug(String.valueOf(m.image.size()));
			return m.image.size();
		}
		Restaurant r = Session.getCurrentUser(ctx()).restaurant;
		if (modelType.equals("restaurant")) {
			if (r == null || r.image.isEmpty()) {
				return 0;
			} else
				return r.image.size();
		}
		return -1;

	}

}