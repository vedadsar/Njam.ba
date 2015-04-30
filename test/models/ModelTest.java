package models;

import static org.junit.Assert.*;
import static play.test.Helpers.*;

import java.util.Date;

import models.User;

import org.junit.*;

import Utilites.Hash;
import play.test.WithApplication;

public class ModelTest extends WithApplication  {

	@Before
	public void setUp(){
		fakeApplication(inMemoryDatabase());
	}
	
	@Test
	public void restaurant(){
		//creating userRestaurant model
		User.createRestaurant("Test1", "Test1@test.ba", "test", "24", "Sarajevo", "Fojnicka", "5");
		User u = User.find("Test1@test.ba");		
		assertNotNull(u);		
		assertEquals(User.RESTAURANT, u.role);
		assertEquals(u.email, "Test1@test.ba");
		assertTrue( Hash.checkPassword("test",u.hashedPassword));	
		//Creating restaurant model
		Restaurant.create("Test", null, "24");
		Restaurant r = Restaurant.findByName("Test");
		assertNotNull(r);		
		
	}
	
	@Test
	public void user(){
		User.createUser("Test@test.ba", "test");
		User u = User.find("Test@test.ba");		
		assertNotNull(u);	
		assertEquals(User.USER, u.role);
		assertEquals(u.email, "Test@test.ba");
		assertTrue( Hash.checkPassword("test",u.hashedPassword));
		
		User.deleteUser(u);
		assertNull(User.find("Test@test.ba"));
	}
	
	@Test
	public void updateUser(){
		User.createUser("tester@test.ba", Hash.hashPassword("123456"));
		
		User user = User.find(2);
		user.email = "tester@test.ba";
		user.validated = false;
		assertEquals(user.email, "tester@test.ba");
		assertEquals(user.validated, false );
		
	}
	
	@Test
	public void mealTest(){
		//Testing meal creation
		Meal.create("Test", 5.0, "Burek", null);
		Meal m = Meal.findByName("Test");
		assertNotNull(m);
		assertEquals("Test", m.name);
		
		//Testing meal update
		Meal.modifyMeal(m, "Test1", 2);
		assertEquals("Test1", m.name);
		
		//Testing delete meal
		int id = m.id;
		Meal.delete(id);
		assertNull(Meal.find(id));
	}
	
	@Test
	public void faqTest(){
		Faq.create("Test question", "Test answer");
		Faq f = Faq.findById(2);
		assertNotNull(f);
		assertEquals(f.answer, "Test answer");
		
		// Delete Faq
		int id = f.id;
		Faq.delete(id);
		assertNull(Faq.findById(id));
	}
	
	@Test
	public void updateFaq(){
		Faq f = new Faq("FAQ pitanje", "FAQ odgovor");
		assertNotNull(f);
		assertEquals(f.answer, "FAQ odgovor");

		f.answer = "FAQ edit odgovor";
		f.save();
		assertEquals(f.answer, "FAQ edit odgovor");

	}
	
	@Test
	public void commentTest(){
		User.createUser("test@test.ba", "test");
		User u = User.find("test@test.ba");
		Meal m = Meal.find(1);
		Comment c = new Comment(u, "test", "test", 5, m);
		c.save();
		assertNotNull(c);
		assertEquals("test", c.title);
		assertEquals("test", c.content);
		assertEquals(5, c.rating);

		// Null
		assertNull(Comment.find(100));

	}
	
	@Test
	public void locationTest(){
		
		Location l = new Location("Sarajevo", "Paromlinska", "30");
		l.save();
		assertNotNull(l);
		assertEquals("Sarajevo", l.city);
		assertEquals("Paromlinska", l.street);
		assertEquals("30", l.number);
		
		// Null
		assertNull(Location.findByCity("New York"));
	}
		
	@Test
	public void testFindNonExisting() {
		User u = User.find(1000);
		assertNull(u);
	}
	
	@Test
	public void testFindNonExistingMeal(){
		Meal c = Meal.find(1500);
		assertNull(c);
	}
}