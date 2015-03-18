package controllers;

import org.junit.*;

import com.google.common.collect.ImmutableMap;

import play.mvc.Result;
import play.test.WithApplication;
import static play.test.Helpers.*;
import static org.junit.Assert.*;
import models.*;

public class LoginTest extends WithApplication {
/*
	
	@Before
	public void setup(){
		fakeApplication(inMemoryDatabase(), fakeGlobal());
	}
	
	@Test
	public void authenticateSucces(){
		Result result = callAction(
				controllers.routes.ref.Application.login(),
				fakeRequest().withFormUrlEncodedBody( ImmutableMap.of(
						"email", "restoran@njam.ba",
						"hashedPassword", "123456"
						))
				);
		assertEquals(200, status(result));
		assertEquals("restoran@njam.ba", session(result).get("email"));
	}
	
	@Test
	public void authenticateFail(){
		Result result = callAction(
				controllers.routes.ref.Application.login(),
				fakeRequest().withFormUrlEncodedBody( ImmutableMap.of(
						"email", "restoran@njam.ba",
						"hashedPassword", "13456"
						))
				);
		assertEquals(200, status(result));
		assertEquals(null, session(result).get("email"));
		assertFalse(session(result).containsKey("email"));
	}
	
	*/
}