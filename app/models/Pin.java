package models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

@Entity
public class Pin extends Model {

	private static final long serialVersionUID = 1L;

	private static Finder<Integer, Pin> findP = new Finder<Integer, Pin>(
			Integer.class, Pin.class);

	@Id
	public int id;
	@Column(unique = true)
	public String pin;
	public Date date;
	@OneToOne
	public User user;
	public Boolean validated;

	public Pin(String pin, User user) {
		this.pin = pin;
		this.date = new Date();
		this.user = user;
		this.validated = false;
	}

	public static Pin generatePinCode(User user) {
		if (user.pin != null) {
			user.pin.delete();
		}
		String randomPin = UUID.randomUUID().toString().substring(0, 5);
		Pin pin = new Pin(randomPin, user);
		pin.save();
		return pin;
	}

	public static String getPinCode(int id) {
		User user = User.find(id);
		Pin pin = findP.where().eq("user", user).findUnique();
		if (pin != null) {
			return pin.pin;
		}
		return null;
	}
}