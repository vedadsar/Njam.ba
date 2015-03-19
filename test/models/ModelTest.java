package models;

import java.util.List;

import org.junit.*;

import Utilites.Hash;
import play.test.WithApplication;
import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class ModelTest extends WithApplication {

	@Before
	public void setUp(){
		fakeApplication(inMemoryDatabase());
	}
	
	@Test
	public void testCreate(){
		User.createUser("test@email.com", "123456");
		User u = User.find(5);
		assertNotNull(u);
		assertEquals(u.email,"test@email.com");
		assertTrue( Hash.checkPassword("123456", Hash.hashPassword("123456")));
	}
	
	@Test
	public void testFindNonExisting(){
		User u = User.find(100);
//		assertNotNull(u);
		assertNull(u);
	}
	
	@Test
	public void testDelete(){
		User.createUser("test@email.com", "test");
		User.delete(1);
		User u = User.find(1);
		assertNull(u);
		
	}
//	
//	
//	@Test
//	public void testAll(){
//		User[] all = {
//				new User("test1@email.com", "test"),
//				new User("test2@email.com", "test"),
//				new User("test3@email.com", "nijeTest"),
//				new User("test4@email.com", "test")
//		};
//		
//		for ( User u : all){
//			u.save();
//		}
//		
//		assertEquals(2, User.allUsers().size());
//		List<User> base =  User.allUsers();
//		assertTrue(base.contains(all[0]));
//		assertFalse(base.contains(all[2]));
//	}
}