package models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.*;

import play.Logger;
import play.data.format.Formats.DateTime;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

@Entity
public class Restaurant extends Model {

	@Id
	public int id;
	@Required
	@MinLength(3)
	@MaxLength(20)
	public String name;

	@DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date dateCreation;

	@OneToOne(cascade = CascadeType.ALL)
	public User user;

	@OneToMany(cascade = CascadeType.ALL)
	public List<Meal> meals;


	public List<Image> image= new ArrayList<Image>();

	public static Finder<Integer, Restaurant> find = new Finder<Integer, Restaurant>(
			Integer.class, Restaurant.class);

	public Restaurant(String name) {
		this.name = name;
	}

	public Restaurant(String name, User u) {
		this.name = name;
		this.user = u;
	}

	public Restaurant(User u, Image image) {
		this.user = u;
		this.image.add(image);
	}

	public static void create(String name, User user) {
		new Restaurant(name, user).save();

	}

	public static boolean createRestaurantImg(Restaurant u, String imgLocation) {
		Image img = new Image(imgLocation);
		u.image.add(img);
		u.save();
		return true;
	}

	public static Restaurant find(int id) {
		return find.byId(id);
	}

	public static Restaurant findByName(String name) {
		return find.where().eq("name", name).findUnique();
	}

	public static boolean delete(int id) {
		Restaurant r = Restaurant.find(id);
		r.delete();
		if (find(id) != null) {
			return false;
		}
		return true;
	}

	public static boolean delete(Restaurant r) {
		if (r == null)
			return false;
		r.delete();
		return true;
	}

	public static List<Restaurant> all(Location location) {
		return find.where().eq("location", location).findList();
	}

	public static List<Restaurant> all() {
		return find.all();
	}

	public static List<Image> findRestaurantIMGS(Restaurant owner) {

		return owner.image;
	}

}