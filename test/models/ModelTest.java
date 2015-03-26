package models;

import static org.junit.Assert.*;
import static play.test.Helpers.*;
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
		User.createRestaurant("Test1", "Test@test.ba", "test", "test", "Test", "test");
		User u = User.find(5);		
		assertNotNull(u);		
		assertEquals(User.RESTAURANT, u.role);
		assertEquals(u.email, "Test@test.ba");
		assertTrue( Hash.checkPassword("test",u.hashedPassword));	
		//Creating restaurant model
		Restaurant.create("Test", null);
		Restaurant r = Restaurant.find(4);
		assertNotNull(r);		
		
	}
	
	@Test
	public void user(){
		User.createUser("Test@test.ba", "test");
		User u = User.find(5);		
		assertNotNull(u);	
		assertEquals(User.USER, u.role);
		assertEquals(u.email, "Test@test.ba");
		assertTrue( Hash.checkPassword("test",u.hashedPassword));
		
		User.deleteUser(5);
		assertNull(User.find(5));
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
		Meal.create("Test", 5, null);
		Meal m = Meal.find(1);
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
		
		int id = f.id;
		Faq.delete(id);
		assertNull(Faq.findById(id));
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