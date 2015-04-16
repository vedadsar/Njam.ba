package models;

import javax.persistence.*;

import play.data.validation.Constraints.*;
import play.db.ebean.Model;

import java.util.List;

/**
 * 
 * @author neldindzekovic
 *
 */
@Entity
public class Faq extends Model {
	
	@Id
	public int id;
	@Required
	public String question;
	@Required
	@Column(columnDefinition="TEXT")
	public String answer;
	
	public static Finder<Integer, Faq> find = new Finder<Integer, Faq>(Integer.class, Faq.class);
	
	// Constructor
	public Faq(String question, String answer) {
		this.question = question;
		this.answer = answer;
	}
	
	/**
	 * This method creates new FAQ!
	 * @param question Question
	 * @param answer Answer
	 * @return True if creating was successful.
	 */
	public static boolean create(String question, String answer) {
		Faq f = new Faq(question, answer);
		f.save();
		return true;
	}
	
	/**
	 * This method edits FAQ.
	 * @param f FAQ to edit.
	 * @param question New question to put in FAQ.
	 * @param answer New answer to put FAQ.
	 * @return True id edit was successful, otherwise false.
	 */
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
	
	/**
	 * This method deletes FAQ.
	 * @param id Id of FAQ to delete.
	 */
	public static void delete(int id) {
		find.byId(id).delete();
	}
	
	/**
	 * This method finds FAQ by id.
	 * @param id Id of FAQ to find.
	 * @return FAQ
	 */
	public static Faq findById(int id) {
		return find.byId(id);
	}
	
	/**
	 * This method finds all FAQs.
	 * @return List of FAQs.
	 */
	public static List<Faq> all() {
		return find.findList();
	}
	
	public static boolean check(String question){
		return find.where().eq("question", question).findUnique() != null;
	}
	
}
