package turnip.turnip;

/**
 * Created by jaxbot on 4/7/16.
 */
public class User {
    public String name;
    public int id;
    public String profile_picture_id;
    public Boolean status;

    User(String name, int id, String profile_picture_id, Boolean status) {
        this.name = name;
        this.id = id;
        this.profile_picture_id = profile_picture_id;
        this.status = status;
    }
}
