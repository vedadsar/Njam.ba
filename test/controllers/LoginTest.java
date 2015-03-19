package controllers;

import org.junit.*;

import com.google.common.collect.ImmutableMap;

import play.mvc.Result;
import play.test.WithApplication;
import static play.test.Helpers.*;
import static org.junit.Assert.*;
import models.*;

public class LoginTest extends WithApplication {

	
	@Before
	public void setup(){
		fakeApplication(inMemoryDatabase());
	}
	
	@Test
	public void authenticateSucces(){
		Result result = callAction(
				controllers.routes.ref.Application.login(),
				fakeRequest().withFormUrlEncodedBody( ImmutableMap.of(
						"email", "suad@suad.com",
						"hashedPassword", "123456"
						))
				);
		assertEquals(303, status(result));
		assertEquals("suad@suad.com", session(result).get("email"));
	}
	
	
	@Test
	public void authenticateFail(){
		Result result = callAction(
				controllers.routes.ref.Application.login(),
				fakeRequest().withFormUrlEncodedBody( ImmutableMap.of(
						"email", "suad@suad.com",
						"hashedPassword", "654321"
						))
				);
		assertEquals(303, status(result));
		assertEquals(null, session(result).get("email"));
		assertFalse(session(result).containsKey("email"));
	}
	
	
}