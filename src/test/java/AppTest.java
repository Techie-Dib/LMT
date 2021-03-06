import org.junit.jupiter.*;

public class AppTest {
        App app_test = null; //Initializes first for the null value
	@BeforeAll
	void setUp() {
		/**
		* Instantiating a simple object of the {@code App}
		* and destroying it afterwards
		*/
		app_test = new App();
	}
	@DisplayName("Test Mode")
	@Test 
	void run() {
		//No tests running since the values doesn't return any values or objects
	}
	@AfterAll
	void destroy() {
		app_test = null; //Setting the value to null again and executing the garbage collector
		System.gc();
	}

}
