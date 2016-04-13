package turnip.turnip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ToggleFriendsAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<User> friends;

    public ToggleFriendsAdapter(Context context, ArrayList<User> friends) {
        super(context, -1, User.friendNames(friends));
        this.context = context;
        this.friends = friends;
        System.out.println(this.friends);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View toggledFriendsList = inflater.inflate(R.layout.toggled_friends_list, parent, false);
        TextView name = (TextView) toggledFriendsList.findViewById(R.id.name);
        TextView status = (TextView) toggledFriendsList.findViewById(R.id.status);
        ImageView imageView = (ImageView) toggledFriendsList.findViewById(R.id.icon);
        name.setText(friends.get(position).name);
        status.setText(friends.get(position).getTurnipStatus());
        imageView.setImageResource(R.drawable.ic_launcher);

        return toggledFriendsList;
    }
}
