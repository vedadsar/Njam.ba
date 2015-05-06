package controllers.api;

import java.util.List;

import models.Meal;
import models.User;
import models.orders.Cart;
import models.orders.CartItem;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import Utilites.Session;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class CartApi extends Controller {

	public static Result mealToCart() {

		JsonNode mealId = request().body().asJson();
		if (mealId == null) {
			return badRequest("Expecting Json data");
		}
		String idS = mealId.findPath("id").textValue();
		String idU = mealId.findPath("user_id").textValue();

		int id = Integer.parseInt(idS);
		int user_id = Integer.parseInt(idU);

		Meal meal = Meal.find(id);
		if (meal == null) {
			return badRequest("Wrong meal id");
		}
		try {
			User user = User.find(user_id);
			String mealOwnerRestaurant = meal.restaurant.name;

			Cart cart = null;

			if (user.carts.isEmpty()) {
				cart = new Cart(user, mealOwnerRestaurant);
				user.carts.add(cart);
				cart.addMealToCart(meal);
				return ok(CartApi.cartItemList(cart.cartItems));
			} else {
				for (int i = 0; i < user.carts.size(); i++) {
					if (user.carts.get(i).restaurantName
							.equals(mealOwnerRestaurant)
							&& user.carts.get(i).paid == false
							&& user.carts.get(i).ordered == false
							&& user.carts.get(i).timedOut == false) {
						cart = user.carts.get(i);
						break;
					}
				}
				if (cart != null) {
					if (cart.paid == true
							|| Cart.timeGap(user.id, cart.id) == false) {
						cart.addMealToCart(meal);
					} else {
						cart.addMealToCart(meal);
						cart.update();
					}
				} else {
					cart = new Cart(user, mealOwnerRestaurant);
					user.carts.add(cart);
					if (cart.paid == true
							|| Cart.timeGap(user.id, cart.id) == false) {
						cart.addMealToCart(meal);
					} else {
						cart.addMealToCart(meal);
						cart.update();
					}
				}
			}
			return ok(CartApi.cartItemList(cart.cartItems));
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			return badRequest("Problem with X rays");
		}
	}

	public static ArrayNode cartItemList(List<CartItem> cartItems) {
		ArrayNode array = new ArrayNode(JsonNodeFactory.instance);
		for (CartItem item : cartItems) {
			ObjectNode cartItem = Json.newObject();
			cartItem.put("cart_item_id", item.id);
			cartItem.put("cart_id", item.cart.id);
			cartItem.put("meal_id", item.meal.id);
			cartItem.put("meal_name", item.meal.name);
			cartItem.put("meal_restaurant", item.meal.restaurant.name);
			cartItem.put("meal_price", item.meal.price);
			cartItem.put("meal_quantity", item.quantity);
			cartItem.put("cart_item_totalPrice", item.totalPrice);
			cartItem.put("cart_totalPrice", item.cart.total);
			cartItem.put("cart_paid", item.cart.paid);
			if (item.cart.location !=  null)
				cartItem.put("cart_location", item.cart.location);
			array.add(cartItem);
		}
		return array;
	}
}