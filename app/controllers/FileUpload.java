package controllers;

import Utilites.RestaurantFilter;
import Utilites.Session;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import play.libs.F.Function;
import play.libs.F.Promise;
import play.mvc.Controller;
import play.mvc.Security;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.*;
import models.*;
import Utilites.RestaurantFilter;

import java.awt.Graphics2D;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.Files;

import java.util.Collections;

import javax.imageio.ImageIO;

public class FileUpload extends Controller {

	private static Meal m;
	private static User u;
	private static File image;
	private static String folderId;
	private static String imageFolder;
	private static String imgFileName;

	public static boolean isEmpty(Collection coll) {
		return (coll == null || coll.isEmpty());
	}

	public static int sizeOfList(String modelType) {
		if (modelType.equals("meal")) {
			if (isEmpty(m.image)) {
				return 0;
			} else
				Logger.debug(String.valueOf(m.image.size()));
			return m.image.size();
		}

		if (modelType.equals("restaurant")) {
			if (isEmpty(u.restaurant.image)) {
				return 0;
			} else
				return u.restaurant.image.size();
		}
		return -1;

	}

	@Security.Authenticated(RestaurantFilter.class)
	public static String locationPath(String folderId, String imageFolder,
			String fileName) {

		String saveLocation = "public" + System.getProperty("file.separator")
				+ "images" + System.getProperty("file.separator") + "UserId"
				+ folderId + System.getProperty("file.separator") + imageFolder
				+ System.getProperty("file.separator")
				+ new Date().toString().replaceAll("\\D+", "") + fileName;

		return saveLocation;
	}

	public static void imageResize(int width, int height, File resizeImage,  String fileLocation
			) {
	
		try {
			
			BufferedImage bdest = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			BufferedImage bsrc=ImageIO.read(resizeImage);
	
			Logger.error("converted");
		  File outputfile = new File(fileLocation);
		  Graphics2D g = bdest.createGraphics();
		  AffineTransform newConstraints = AffineTransform.getScaleInstance((double)width/bsrc.getWidth(),
		          (double)height/bsrc.getHeight());
		  g.drawRenderedImage(bsrc, newConstraints);
		  ImageIO.write(bdest, "jpg", outputfile);
		  Logger.error("converted");
		} catch (IOException e) {
			Logger.error("No image found");
		}
	}
	
	
	
	

	public static Result saveMealIMG(int id) {

		u = Session.getCurrentUser(ctx());
		m = Meal.find(id);

		folderId = String.valueOf(u.restaurant.id);
		imageFolder = "meal";

		if (sizeOfList(imageFolder) < 5) {

			MultipartFormData body = request().body().asMultipartFormData();
			FilePart filePart = body.getFile("image");

			try {
				image = filePart.getFile();
			} catch (NullPointerException e) {
				Logger.debug(("Empty File Upload" + e));
				return ok(wrong.render("OOPS you didnt send a file"));
			}

			imgFileName = filePart.getFilename();

			String saveLocation = locationPath(folderId, imageFolder,
					imgFileName);
			File saveFolder = new File(saveLocation).getParentFile();
			saveFolder.mkdirs();
       
			try {
				 File imageFile=new File(saveLocation);
				Files.move(image,imageFile );
				imageResize(800, 500, imageFile,saveLocation);
			} catch (IOException e) {
				Logger.debug(e.toString());
			}
			Logger.debug(saveLocation);
			Meal.createMealImg(m, saveLocation);

            Logger.debug("Passed resize?");
			return ok(fileUploadMeal.render("", "", m, Restaurant.all()));
		} else
			return ok(wrong.render("LIMIT HAS BEEN REACHED"));
	}

	@Security.Authenticated(RestaurantFilter.class)
	public static Result saveRestaurantIMG() {
		User u = Session.getCurrentUser(ctx());

		folderId = String.valueOf(u.restaurant.id);
		imageFolder = "restaurant";

		if (sizeOfList(imageFolder) < 3) {
			MultipartFormData body = request().body().asMultipartFormData();
			FilePart filePart = body.getFile("image");

			try {
				image = filePart.getFile();
			} catch (NullPointerException e) {
				Logger.debug(("Empty File Upload" + e));
				return ok(wrong.render("OOPS you didnt send a file"));
			}

			imgFileName = filePart.getFilename();

			String saveLocation = locationPath(folderId, imageFolder,
					imgFileName);
			File saveFolder = new File(saveLocation).getParentFile();
			saveFolder.mkdirs();

			try {
				Files.move(image, new File(saveLocation));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Logger.debug(e.toString());
			}

			Restaurant.createRestaurantImg(u.restaurant, saveLocation);

			return TODO;
		} else
			return ok(wrong.render("LIMIT HAS BEEN REACHED"));
	}

	@Security.Authenticated(RestaurantFilter.class)
	public static Result deleteImg(int id) {
		Image.deleteImg(id);

		return ok(succsess.render("It has succseeded"));

	}

}