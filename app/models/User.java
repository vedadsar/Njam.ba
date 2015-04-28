package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import models.orders.Cart;
import models.orders.CartItem;

import com.avaje.ebean.annotation.CreatedTimestamp;

import Utilites.*;
import play.Logger;
import play.data.format.Formats.DateTime;
import play.data.validation.Constraints.*;
import play.db.ebean.Model;

@Entity
public class User extends Model {	
	
	@Id
	public int id;	
	@Required
	@Column(unique = true)	
	public String email;
	@Column(unique = true)
	public String username;
	@Required
	@MinLength(6)
	@MaxLength(16)
	public String hashedPassword;
	@CreatedTimestamp
    public Date dateCreation;    
    @OneToOne(cascade=CascadeType.ALL)
    public Restaurant restaurant;
    
	@OneToMany(cascade = CascadeType.ALL)
	public List<Location> locations;
    @OneToOne
    public Cart cart;
    @OneToMany
    public Comment comment;
    
    @OneToMany(mappedBy="user",cascade=CascadeType.ALL)
	public List<Cart> carts = new ArrayList<Cart>();
    
    public String confirmationString;
    public Boolean validated = false;
    
    public String role; // admin, customer, restaurantOwner;
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    public static final String RESTAURANT = "RESTAURANT";
	
    	
	public static Finder<Integer, User> find = new Finder<Integer, User>(Integer.class, User.class);
	public static Finder<Integer, Location> findL = new Finder<Integer, Location>(Integer.class, Location.class);
	
	public User(String email, String clearPassword){
		this.email = email;
		this.hashedPassword = Hash.hashPassword(clearPassword);
		this.dateCreation = new Date();
		this.role = USER;
		this.carts= new ArrayList<Cart>(0);
	}
	
	public User(String email, String password, String role){
		this.email = email;
		this.hashedPassword = Hash.hashPassword(password);
		this.dateCreation = new Date();
		this.carts= new ArrayList<Cart>(0);
		this.role = role;		
	}
	
	public static boolean createRestaurant(String name, String email, String password, String workingTime, String city, String street, String number){
		User check = find.where().eq("email", email).findUnique();
		if(check != null){
			return false;
		} else {
			User u  = new User(email, password, RESTAURANT);
			Location location = new Location(city, street, number);
			location.user = u;
			u.save();
			u.locations.add(location);
			
			
			Restaurant r = new Restaurant(name, find.where().eq("email", email).findUnique(), workingTime);			
			u.restaurant = r;
			u.validated = false;
			Statistics statistic = Statistics.createStatistics(r);
			r.statistic = statistic;
			u.save();
			r.save();
			return true;
		}
	}
	
	public static Restaurant createRestaurantReturnRestaurant(String name, String email, String password, String workingTime, String city, String address, String number){
		User check = find.where().eq("email", email).findUnique();
		if(check != null){
			return null;
		} else {
			User u  = new User(email, password, RESTAURANT);
			Location location = new Location(city, address, number);
			location.user = u;
			u.save();
			u.locations.add(location);
			
			
			Restaurant r = new Restaurant(name, find.where().eq("email", email).findUnique(), workingTime);			
			u.restaurant = r;
			u.validated = false;
			Statistics statistic = Statistics.createStatistics(r);
			r.statistic = statistic;
			u.save();
			r.save();
			return r;
		}
	}
	
	/**
	 * Method for creating user.
	 * @param email
	 * @param password
	 */
	public static boolean createUser(String email, String password) {
		// First we check if user already exists.
		User check = find.where().eq("email", email).findUnique();
		if (check != null) {
			// User already exists !
			return false;
		} else {
			User usr = new User(email, password);
			usr.save();
			return true;
		}
	}
	
	public static User createUserReturnUser(String email, String password) {
		// First we check if user already exists.
		User check = find.where().eq("email", email).findUnique();
		if (check != null) {
			// User already exists !
			return null;
		} else {
			User usr = new User(email, password);
			usr.save();
			return usr;
		}
	}

	public static void createAdmin(String email, String password) {
		User u = new User(email, password, ADMIN);
		u.validated = true;
		u.save();
	}

	public static boolean createUser(User u) {
		// First we check if user already exists.
		User check = find.where().eq("email", u.email).findUnique();
		if (check == null) {
			// User already exists !
			return false;
		}
		u.save();
		return true;
	}

	/**
	 * Method for authenticating user who is trying to login.
	 * First it checks if user with that email exists in our
	 * database and then its checking passwords.
	 * TODO to check hashed password.
	 * @param email
	 * @param password
	 * @return
	 */
	public static boolean authenticate(String email, String password){
		User check = find.where().eq("email", email).findUnique();
		if((check != null) && (check.validated == true)) {
			if(Hash.checkPassword(password, check.hashedPassword))
				return true;
		}		
		return false;
	}	
	
	public static boolean checkA(String email, String password){
		User check = find.where().eq("email", email).findUnique();
		if(check != null) {
			if(Hash.checkPassword(password, check.hashedPassword))
				return true;
		}		
		return false;
	}	
	
	public static String checkRole(String email){
		User u = find.where().eq("email", email).findUnique();			
		return u.role;
	}
	
	
	public static boolean checkIfExists(String email){
		List<User> users = find.all();
		for(User user: users){
			if(user.email.equals(email))
				return true;
		}
		return false;
	}
		
		/**
		 * Finds user by id.
		 * @param id  of User
		 * @return User's data.
		 */
		public static User find(int id){
			return find.byId(id);
		}
		
		
		public static List<User> findAdmins(){
						
			return find.where().eq("role", ADMIN).findList();
		}
		
		public static User find(String email){
			return find.where().eq("email", email).findUnique();
		}
		
	    public static User findByConfirmationString(String confirmationString) {
	        return find.where().eq("confirmationString", confirmationString).findUnique();
	    }
	    
	    public static boolean confirm(User user) {
	        if (user == null) {
	            return false;
	        }
	        user.confirmationString = null;
	        user.validated = true;
	        user.save();
	        return true;
	    }
	    
	    public static boolean deleteUser(User u){
	    	if(u == null)
	    		return false;
	    	u.delete();
	    	return true;
	    }
	    
	    public static boolean deleteUser(int id){
	    	User u =  find.byId(id);
	    	if(u == null)
	    		return false;
	    	u.delete();
	    	return true;
	    }

		
		/**
		 * Method for deleting user.
		 * @param id
		 */
		public static void delete( int id){
			find.byId(id).delete();
		}
		
		public static void update( int id){
			find.byId(id).update();
		}

		
		/**
		 * Method for listing all users ( not restaurants )
		 * @return
		 */
		public static List<User> allUsers(){
			return find.all();
		}
		
		/**
		 * Method for listing all restaurants !
		 * @return
		 */
		public static List<User> allRestaurant(){
			return find.where().eq("isRestaurant", "true").findList();
		}	
		
		/**
		 * 
		 * Method just for global class - testing is easy now.
		 * @return
		 */
		public static boolean check(String mail) {
			return find.where().eq("email", mail).findUnique() != null;
		}
		
		public static boolean activeCartCheck(List<Cart> carts) {
			for(int i=0; i<carts.size(); i++) {
				if(carts.get(i).empty == false && carts.get(i).ordered == false && carts.get(i).paid == false && carts.get(i).timedOut == false) {
					Logger.debug("POSTOJI NEPRAZAN CART");
					return false;
				}
			}
			Logger.debug("NE POSTOJI NEPRAZAN CART");
			return true;
		}
		
		public static List<Location> allLocation(){
			return findL.all();
		}
		
		public static Location lastLocation(){
			List<Location> locations = findL.all();
			int size = locations.size();
			if (size == 0){
				return null;
			}
			Location lastLocation = locations.get(size-1);
			return lastLocation;
		}
		
		public static List<Location> allUserLocation(int userId){
			//User user = find.byId(userId);
			List<Location> locations = findL.where().eq("user_id", userId).findList();
			//user.locations = locations;
			
			return locations;
		}
 
}