package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.avaje.ebean.annotation.CreatedTimestamp;

import play.data.validation.Constraints.Email;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.db.ebean.Model.Finder;

@Entity
public class Newsletter extends Model {
	
	@Id
	public int id;
	@Required
	public String confirmationString;
	@Required
	@Email
	public String email;
	@CreatedTimestamp
    public Date dateCreation;    
	@OneToOne
	public User user;
	
	public boolean validated;
	
	public static Finder<Integer, Newsletter> findN = new Finder<Integer, Newsletter>(Integer.class, Newsletter.class);

	public Newsletter(String email){
		this.confirmationString =  UUID.randomUUID().toString();
		this.email = email;
		this.dateCreation = new Date();
		this.validated = false;
	}
	
	public static void subscribeToNewsletter(String email){
		Newsletter subscriber = new Newsletter(email);
		subscriber.save();
	}
	
	public static boolean isSubscribed(String email){
		Newsletter subscriber = findN.where().eq("email", email).findUnique();
		if (subscriber != null) {
			return true;
		}
		return false;
	}
	
	public static Newsletter findByEmail(String email){
		Newsletter subscriber = findN.where().eq("email", email).findUnique();
		return subscriber;
	}

	public static List<String> findAllSubscribers() {
		List<Newsletter> subscribers = findN.all();
		List<String> emails = new ArrayList<String>(0);
		for (Newsletter newsletter : subscribers) {
			if(newsletter.validated == true){
			emails.add(newsletter.email);
			}
		}
		return emails;
	}
	
	public static Newsletter findByConfirmationString(String confirmationString){
		Newsletter news = findN.where().eq("confirmationString", confirmationString).findUnique();
		return news;
	}
	
	public static boolean confirm(Newsletter newsletter) {
		if (newsletter == null) {
			return false;
		}
		newsletter.validated = true;
		newsletter.save();
		return true;
	}
	
	public static boolean unsubscribe(Newsletter newsletter) {
		if (newsletter == null) {
			return false;
		}
		newsletter.validated = false;
		newsletter.update();
		return true;
	}

}