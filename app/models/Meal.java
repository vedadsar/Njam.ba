package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.persistence.*;

import models.orders.*;
import Utilites.Session;
import play.Logger;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.mvc.Http.Context;

@Entity
public class Meal extends Model {

	@Id
	public int id;
	@Required
	public String name;
	@Required
	public double price;
	@Required
	public String description;
	@Required
	public String category;
	public static final List<String> categories = new ArrayList<String>();
	
	@ManyToOne 
	public Restaurant restaurant;
	
	@OneToMany(mappedBy="meal",cascade = CascadeType.ALL)
	public List<Comment> comment = new ArrayList<Comment>(0);
	
	@OneToMany(mappedBy="meal",cascade = CascadeType.ALL)
	public List<Image> image=new ArrayList<Image>();

	@OneToMany
	public List<CartItem> cartItems = new ArrayList<CartItem>(0);

	public static Finder<Integer, Meal> find = new Finder<Integer, Meal>(
			Integer.class, Meal.class);
	
	private static void categories(){
		categories.add("Barbecue");
		categories.add("Breakfast");
		categories.add("Bosnian cousine");
		categories.add("Chinese cuisine");
		categories.add("Cold appetizers");
		categories.add("Cooked meals");
		categories.add("Desserts");
		categories.add("Drinks and beverages");
		categories.add("Fast Food");
		categories.add("Fish dishes");
		categories.add("Hot dishes");
		categories.add("Indian cuisine");
		categories.add("Italian cousine");
		categories.add("Main course");
		categories.add("Meat dishes");
		categories.add("Mexican cousine");
		categories.add("Pies and pastries");
		categories.add("Pizza");
		categories.add("Salads");
		categories.add("Sandwiches");
		categories.add("Side dish");
		categories.add("Soups");
		categories.add("Vegetarian food");
		
	}
	
	public static List<String> allCategories(){
		int size = categories.size();
		if(size==0){
			categories();
		}
		System.out.println("broj kategorija: " + size);
		return categories;
	}
	
	public static boolean createCategoriy(String nameCategory){
		if ( nameCategory!= null){
			categories.add(nameCategory);
			Collections.sort(categories);
			return true;
		}
		return false;
	}
	
	public static boolean updateCategory(String oldNameCategory, String newNameCategory){
		Iterator<String> iter = categories.iterator();
		String cat;
		int idx = 0;		
		while (iter.hasNext()){
			cat = iter.next();
			if (cat.toLowerCase().contains(oldNameCategory.toLowerCase())==true){				
				categories.remove(idx);
				categories.add(newNameCategory);
				System.out.println("nova kategorija: " + newNameCategory);
				Collections.sort(categories);
				return true;
			}
			idx++;
		}		
		return false;
	}
	
	public static boolean deleteCategory(String nameCategory){
		if(categories.contains(nameCategory)==true){
			int idx = categories.indexOf(nameCategory);
			categories.remove(idx);
			return true;
		}
		return false;
	}
	

	public Meal(String name, double price, String category, String description) {
		this.name = name;
		this.price = price;
		this.category =category;
		this.description = description;
		this.image= new ArrayList<Image>(0);
	}

	public Meal(String name, double price,String category, Restaurant restaurant) {
		this.name = name;
		this.price = price;
		this.category =category;
		this.restaurant = restaurant;
		this.image= new ArrayList<Image>(0);

	}

	public Meal(String name, double price, Restaurant restaurant, Image image) {
		this.name = name;
		this.price = price;
		this.restaurant = restaurant;
		this.image.add(image);
	}

	public static List<Meal> all(String name) {
		return find.where().eq("name", name).findList();
	}

	public static List<Meal> all() {
		return find.all();
	}

	/**
	 * Method Returns all meals from a certain restaurant owner it accepts the
	 * restaurant user.
	 * 
	 * @param u
	 *            User
	 * @return List of meals belonging to a certain User-restaurant
	 */
	public static List<Meal> allById(User u) {
		int id = u.id;
		Restaurant r = User.find(id).restaurant;
		List<Meal> mealsById = find.where().eq("restaurant", r).findList();
		return mealsById;
	}

	public static boolean createMealImg(Meal m, String imgLocation) {
		Image img = new Image(imgLocation);
		m.image.add(img);
		Logger.debug(String.valueOf(m.image.size()));
		m.save();
		return true;
	}
	
	public  void createMealImgNS(String imgLocation) {
		Image img = new Image(imgLocation);
		img.save();
		this.image.add(img);
		Logger.debug(img.imgLocation);
		
	}

	public static boolean create(String name, double price, String category,
			Restaurant currentUserRestaurant) {
		Meal m = new Meal(name, price, category,currentUserRestaurant);
		m.save();

		return true;
	}

	public static boolean create(Meal m) {
		Meal meal = find.findUnique();
		if (meal != null) {
			return false;
		} else {
			m.save();
		}
		return true;
	}

	public static void delete(int id) {
		find.byId(id).delete();
	}

	/**
	 * 
	 * @param m
	 *            Meal object to be deleted Method deleats a meal Object
	 *            received if the object still exists (not null) after the
	 *            delete function it returns false if the object is successfully
	 *            deleted it returns true
	 * 
	 * @return boolean
	 * 
	 */

	public static boolean delete(Meal m) {
		int idOfMeal = m.id;
		m.delete();
		if (find(idOfMeal) != null) {
			return false;
		}
		return true;
	}

	public static Meal find(int id) {
		return find.byId(id);
	}

	public static Meal findByName(String name) {
		return find.where().eq("name", name).findUnique();

	}


	/**
	 * 
	 * @param m
	 *            Meal object
	 * @param newName
	 *            The new name that the meal object will be changed to
	 * @param newPrice
	 *            The new price that the meal object will be changed to
	 * 
	 *            The method reads an object then modifies its values, then it
	 *            checks if the modified values have changed
	 * @return boolean
	 * 
	 */
	public static boolean modifyMeal(Meal m, String newName, double newPrice, String newCategory) {

		m.name = newName;
		m.price = newPrice;
		m.category = newCategory;
		m.update();

		Meal meal = find(m.id);
		if (!meal.name.equals(newName) || meal.price != newPrice) {
			return false;
		} else
			return true;
	}

	public void addMealToCart(CartItem cartItem) {
		cartItems.add(cartItem);
	}

	
	public static boolean check(int id) {
		return find.where().eq("id", id).findUnique() != null;
	}
	
	
	
	

}
