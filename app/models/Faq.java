package models;

import javax.persistence.*;
import play.data.validation.Constraints.*;
import play.db.ebean.Model;

public class Faq extends Model {
	
	@Id
	public int id;
	@Required
	public String question;
	@Required
	public String answer;
	
	public static Finder<Integer, Faq> find = new Finder<Integer, Faq>(Integer.class, Faq.class);
	
	public Faq(String question, String answer) {
		this.question = question;
		this.answer = answer;
	}
	
	public static boolean create(String question, String answer) {
		Faq f = new Faq(question, answer);
		f.save();
		return true;
	}
	
	public static boolean edit(Faq f, String question, String answer) {
		f.question = question;
		f.answer = answer;
		f.update();
		Faq editedFaq = find.byId(f.id);
		if (!editedFaq.question.equals(question) || !editedFaq.answer.equals(answer)) {
			return false;
		} else {
			return true;
		}
	}
	
	public static void delete(int id) {
		find.byId(id).delete();
	}
	
	public static Faq find(int id) {
		return find.byId(id);
	}
	
}
