package turnip.turnip;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        System.out.println("Hi");
        API.authkey = "0.fho837tr7g7ousor";
        //assertEquals(API.createUser("jonathan", "test", "test2"), true);
        //API.login("test2", "test");
        System.out.println(API.toggle(false));
        UserFeed uf = API.feed();
        System.out.println(uf.status);
        System.out.println(uf.friends);

        assertEquals(4, 2 + 2);
    }
}