package models;

import java.util.Date;
import java.util.List;
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
	public String hashedPassword;
    @DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date dateCreation;
    
    @OneToOne
    public Restaurant restaurant;
    
    public boolean isAdmin;
    public boolean isRestaurant;
	
    	
	static Finder<Integer, User> find = new Finder<Integer, User>(Integer.class, User.class);
	
	public User(String email, String clearPassword){
		this.email = email;
		this.hashedPassword = Hash.hashPassword(clearPassword);
		this.isAdmin = false;
		this.isRestaurant = false;
		//TODO hashing password.
	}
	
	public User(String email, String password, boolean isRestaurant){
		this.email = email;
		this.hashedPassword = Hash.hashPassword(password);
		this.isRestaurant = isRestaurant;		
	}
	
	public static boolean createRestaurant(String email, String password){
		User check = find.where().eq("email", email).findUnique();
		if(check != null){
			//User already exists !
			return false;
		} else {
			new User(email, password, true).save();
			return true;
		}

	}
	
	/**
	 * Method for creating user.
	 * @param email
	 * @param password
	 */
	public static boolean createUser(String email, String password){
		//First we check if user already exists.
		User check = find.where().eq("email", email).findUnique();
		if(check != null){
			//User already exists !
			return false;
		} else {
			new User(email, password).save();
			return true;
		}
	}
	
	public static boolean createUser(User u){
		//First we check if user already exists.
		User check = find.where().eq("email", u.email).findUnique();
		if(check == null){
			//User already exists !
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
	
		/**
		 * Finds user by id.
		 * @param id  of User
		 * @return User's data.
		 */
		public static User find(int id){
			return find.byId(id);
		}
		
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
}

