package turnip.turnip;

import java.util.ArrayList;

/**
 * Created by jaxbot on 4/7/16.
 */
public class User {
    public String name;
    public int id;
    public String profile_picture_id;
    public Boolean status;
    private Boolean is_friend;

    User(String name, int id, String profile_picture_id, Boolean status) {
        this.name = name;
        this.id = id;
        this.profile_picture_id = profile_picture_id;
        this.status = status;
    }

    public String toString() {
        return this.name;
    }

    public static ArrayList<String> friendNames(ArrayList<User> users) {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            names.add(users.get(i).name);
        }

        return names;
    }

    public String getTurnipStatus() {
        if (this.status) {
            return "Ready to turn up";
        } else {
            return "Not ready to turn up";
        }
    }

    public Boolean getIs_friend() {
        return is_friend;
    }

    public void setIs_friend(Boolean is_friend) {
        this.is_friend = is_friend;
    }
}
