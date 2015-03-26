package controllers;

import Utilites.RestaurantFilter;
import Utilites.Session;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import views.html.wrong;
import models.*;
import Utilites.RestaurantFilter;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.Files;

public class FileUpload extends Controller {


	@Security.Authenticated(RestaurantFilter.class)
	public static Result saveMealIMG(int id) {
		Meal m = Meal.find(id);
		User u = Session.getCurrentUser(ctx());
		List<Image> totalMealpics =  Image.findAllByOwnerandMeal(u.restaurant,m);
		if (totalMealpics.size() < 5) {

			MultipartFormData body = request().body().asMultipartFormData();
			FilePart filePart = body.getFile("image");
			File image = filePart.getFile();
			String saveLocation = "public" + File.separator + "images"
					+ File.separator + "UserId" + u.restaurant.id
					+ File.separator + "meal" + File.separator + new Date()
					+ filePart.getFilename();

			File saveFolder = new File(saveLocation).getParentFile();
			saveFolder.mkdirs();
			try {
				Files.move(image, new File(saveLocation));

			} catch (IOException e) {
				 Logger.debug(e.toString());
			}
			Logger.debug(saveLocation);
			Image.createMealImg(saveLocation);
		}
		else{
			return TODO;
		}

		return ok(wrong.render("LIMIT HAS BEEN REACHED"));
	}

	@Security.Authenticated(RestaurantFilter.class)
	public static Result saveRestaurantIMG() {
		User u = Session.getCurrentUser(ctx());
		List <Image> totalRestaurantPics = Image.findAllByOwnerNoMeal(u.restaurant);
        if(totalRestaurantPics.size()<3){
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart filePart = body.getFile("image");

		File image = filePart.getFile();

		String saveLocation = "public" + File.separator + "images"
				+ File.separator + "UserId" + u.restaurant.id + File.separator
				+ "profile" + File.separator + new Date()
				+ filePart.getFilename();

		File saveFolder = new File(saveLocation).getParentFile();
		saveFolder.mkdirs();
		try {
			Files.move(image, new File(saveLocation));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Logger.debug(e.toString());
		}
		Logger.debug(saveLocation);
		Image.createRestaurantImg(saveLocation);
       return TODO;
       }else
		return ok(wrong.render("LIMIT HAS BEEN REACHED"));
	}
}