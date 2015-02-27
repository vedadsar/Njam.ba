import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;

import static org.fluentlenium.core.filter.FilterConstructor.*;

public class IntegrationTest {

    /**
     * add your integration test here
     * in this example we just check if the welcome page is being shown
     */
  @Test
  public void test() {
      running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
          public void invoke(TestBrowser browser) {
              browser.goTo("http://localhost:3333");
              assertThat(browser.pageSource()).contains("NJAM.BA");
              assertThat(browser.pageSource()).contains("Welcome to Njam.ba");
          }
      });
  }
  
//  @Test
//  public void testEmail(){
//  	running(testServer(333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>(){
//  		
//			public void invoke(TestBrowser browser)  {
//				browser.goTo("http://localhost:3333/");
//				browser.fill("#email").with("test");
//				//dadati u index.scala.html ali gdje tacno? 'id -> "email"

//				browser.submit("#email");
//				browser.fill("#email").with("test@mail.com");
//				browser.submit("#createUser");
//				
//				
//			}
//  		
//  	
//  });
//
//  }
}
