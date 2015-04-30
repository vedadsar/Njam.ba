package models;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import models.orders.Cart;
import models.orders.CartItem;

import org.junit.*;

import play.test.*;

public class CartTest extends WithApplication  {
	
	@Test
	public void createAndRetrieveItem() {
		User u = User.find("restoran@njam.ba");
		Cart cart = new Cart(u);
		cart.save();
		assertNotNull(cart);
		
		Meal meal = Meal.find(1);
		CartItem ci = new CartItem(cart, 1, 5, meal);
		ci.save();
		
		assertNotNull(ci);
		assertEquals(1, ci.quantity);
	}
	
//	@Test
//	public void removeCartItem() {
//		User u = User.find("restoran@njam.ba");
//		Cart cart = new Cart(u);
//		cart.save();
//		Meal meal = Meal.find(1);
//		CartItem ci = new CartItem(cart, 1, 5, meal);
//		int id = ci.id;
//
//		assertNotNull(ci);
//		assertEquals(1, ci.quantity);
//		
//		ci = CartItem.find.byId(id);
//		ci.delete();
//		cart.save();
//		assertNull(ci);
//	}
	
	@Test
	public void removeItemFromBasket() {
		User u = User.find("restoran1@njam.ba");
		Cart cart = new Cart(u);
		cart.save();
		Meal meal = Meal.find(1);
		CartItem ci = new CartItem(cart, 1, 5, meal);
		ci.save();
		cart.update();

		assertNotNull(cart);
		assertEquals("restoran1@njam.ba", cart.user.email);
//		assertTrue(cart.cartItems.size() > 0);
		
		List<CartItem> newCartItems = cart.cartItems;
		Iterator<CartItem> iter = newCartItems.iterator();
		int quantity = 0;
		while (iter.hasNext()) {
			ci = iter.next();
			if (ci.meal.equals(meal)) {
				quantity = ci.quantity;
			}
		}
		if (quantity > 0) {
			cart.removeMeal(meal, u.id, cart.id);
		}
		assertEquals(0, cart.cartItems.size());
	}


//	@Test
//	public void emptyBasket() {
//		User u = User.find("restoran@njam.ba");
//		Cart cart = new Cart(u);
//		cart.save();
//		Cart basket = Cart.findByUserId(u.id);
//		
//		assertNotNull(basket);
//		assertEquals("restoran@njam.ba", cart.user.email);
//		assertTrue(cart.cartItems.size() > 0);
//		
//		cart.delete();
//		assertTrue(cart.cartItems.size() == 0);
//	}
//
	@Test
	public void findAllItems() {
		List<Meal> meals = Meal.all();

		assertNotNull(meals);
		assertEquals(15, meals.size());
	}
//	
	@Test
	public void findAllItemsInBasket() {
		User u = User.find("restoran@njam.ba");
		Cart cart = new Cart(u);
		cart.save();
		Cart basket = Cart.findByUserId(u.id);
		Meal meal = Meal.find(1);
		CartItem ci = new CartItem(cart, 1, 5, meal);
		ci.save();
		assertNotNull(basket);
		assertEquals("restoran@njam.ba", basket.user.email);
		assertNotNull(basket.cartItems);
		assertEquals(1, basket.cartItems.size());
	}
	@Test
	public void removeBasket() {
		User u = User.find("restoran@njam.ba");
		Cart cart = new Cart(u);
		cart.save();
		Cart basket = Cart.findByUserId(u.id);
		Meal meal = Meal.find(1);
		CartItem ci = new CartItem(cart, 1, 5, meal);
		ci.save();

		assertNotNull(basket);
		assertEquals("restoran@njam.ba", basket.user.email);
		assertNotNull(basket.cartItems);
		assertTrue(basket.cartItems.size() > 0);
		
		basket.delete();
		basket = Cart.find(u.id);
		assertNull(basket);
	}

}
