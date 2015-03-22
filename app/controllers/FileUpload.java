package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import play.Logger;
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
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import models.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.io.Files;

public class FileUpload extends Controller{


	public static Result saveFile(){
		

	MultipartFormData body  = request().body().asMultipartFormData();
	  
		FilePart filePart = body.getFile("image");
		Logger.debug("Content type: " + filePart.getContentType());
		Logger.debug("Key: " + filePart.getKey());
		File image = filePart.getFile();
		
		try {
			Files.move(image, new File("public"+File.separator+"images/"+filePart.getFilename()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
					}
		return TODO;
}


}