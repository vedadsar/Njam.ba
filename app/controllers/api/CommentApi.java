package controllers.api;

import java.util.List;

import models.Comment;
import models.Meal;
import models.User;
import play.db.ebean.Model.Finder;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CommentApi extends Controller {

	static Finder<Integer, Comment> findC = new Finder<Integer, Comment>(
			Integer.class, Comment.class);

	public static Result comment() {
		JsonNode json = request().body().asJson();
		String id = json.findPath("id").textValue();
		Meal meal = Meal.find(Integer.parseInt(id));
		List<Comment> comments = findC.where().eq("meal", meal).findList();

		if (comments != null) {
			return ok(CommentApi.commentsList(comments));
		}
		return badRequest();
	}

	public static Result createComment() {
		JsonNode json = request().body().asJson();
		String user_id = json.findPath("user_id").textValue();
		String title = json.findPath("title").textValue();
		String content = json.findPath("content").textValue();
		String ratingU = json.findPath("rating").textValue();
		int rating = Integer.parseInt(ratingU);
		String meal_id = json.findPath("meal_id").textValue();
		User user = User.find(Integer.parseInt(user_id));
		Meal meal = Meal.find(Integer.parseInt(meal_id));
		
		if (user.id != meal.comment.iterator().next().author.id) {
			Comment comment = new Comment(user, title, content, rating, meal);
			comment.save();
			return ok(CommentApi.commentToApp(comment));
		} else {
			return badRequest();
		}
	}

	public static ObjectNode commentToApp(Comment c) {
		ObjectNode comment = Json.newObject();
		comment.put("id", c.id);
		comment.put("title", c.title);
		comment.put("content", c.content);
		comment.put("rating", c.rating);
		return comment;
	}

	public static ArrayNode commentsList(List<Comment> comments) {
		ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
		for (Comment comment : comments) {
			ObjectNode commentNode = Json.newObject();
			commentNode.put("id", comment.id);
			commentNode.put("title", comment.title);
			commentNode.put("content", comment.content);
			commentNode.put("rating", comment.rating);
			array.add(commentNode);
		}
		return array;
	}
}
