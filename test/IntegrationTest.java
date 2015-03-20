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
	
/*	
  @Test
  public void test() {
      running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>() {
          public void invoke(TestBrowser browser) {
              browser.goTo("http://localhost:3333");              
              assertThat(browser.pageSource()).contains("Njam.ba The tasty way to go.");
              assertThat(browser.pageSource()).contains("Njam.ba");
        //    assertThat(browser.pageSource()).contains("Enjoy delicious food from your favorite restaurant.");
          }
      });
  }
  
  
  
  @Test
  public void testEmail(){
  	running(testServer(333, fakeApplication(inMemoryDatabase())), HTMLUNIT, new Callback<TestBrowser>(){
  		
			public void invoke(TestBrowser browser)  {
				browser.goTo("http://localhost:3333/registration");

				browser.fill("#email").with("rtest@najm.ba");
				browser.fill("#hashedPassword").with("123456");
				browser.submit("#register");
				
				
				assertThat(browser.pageSource()).contains("Please check your email");
			}
 		
  	
  });

  }
  
  */
}