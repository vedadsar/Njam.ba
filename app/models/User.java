package models;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import Utilites.*;
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
	@Required
	@MinLength(6)
	public String hashedPassword;
    @DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date dateCreation;    
    @OneToOne
    public Restaurant restaurant;
    
    public String confirmationString;
    public Boolean validated = false;
    
    public String role; // admin, customer, restaurantOwner;
    public static final String ADMIN = "ADMIN";
    public static final String USER = "USER";
    public static final String RESTAURANT = "RESTAURANT";
	
    	
	static Finder<Integer, User> find = new Finder<Integer, User>(Integer.class, User.class);
	
	public User(String email, String clearPassword){
		this.email = email;
		this.hashedPassword = Hash.hashPassword(clearPassword);
		this.role = USER;
	}
	
	public User(String email, String password, String role){
		this.email = email;
		this.hashedPassword = Hash.hashPassword(password);
		this.role = role;		
	}
	
	public static boolean createRestaurant(String name, String email, String password){
		User check = find.where().eq("email", email).findUnique();
		if(check != null){
			return false;
		} else {
			User u  = new User(email, password, RESTAURANT);	
			u.save();
			Restaurant r = new Restaurant(name, find.where().eq("email", email).findUnique());
			r.save();
			u.restaurant = r;
			u.validated = true;
			u.save();		
			return true;
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
		if(check != null){
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
	    
//	    public static boolean validatedUser(String email, String hashedPassword){
//	    	User user = new User(email, hashedPassword);
//	    	if(user.validated == true){
//		        user.confirmationString = null;
//		        user.validated = true;
//		        user.save();
//	    		return true;
//	    	}
//	    	return false;
//	    }


		
		/**
		 * Method for deleting user.
		 * @param id
		 */
		public static void delete( int id){
			find.byId(id).delete();
		}
		
		/**
		 * Method for listing all users ( not restaurants )
		 * @return
		 */
		public static List<User> allUsers(){
			return find.where().eq("isRestaurant", "false").findList();
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

}