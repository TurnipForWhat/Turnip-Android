package turnip.turnip;
import java.util.ArrayList;
public class UserFeed {
    public boolean status;
    public ArrayList<User> friends;

    UserFeed(boolean status, ArrayList<User> friends) {
        this.status = status;
        this.friends = friends;
    }
}
