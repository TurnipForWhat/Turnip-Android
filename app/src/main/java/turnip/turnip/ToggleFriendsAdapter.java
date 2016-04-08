package turnip.turnip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ToggleFriendsAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] names;
    private final String[] statuses;

    public ToggleFriendsAdapter(Context context, String[] names, String[] statuses) {
        super(context, -1, names);
        this.context = context;
        this.names = names;
        this.statuses = statuses;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View toggledFriendsList = inflater.inflate(R.layout.toggled_friends_list, parent, false);
        TextView textView = (TextView) toggledFriendsList.findViewById(R.id.name);
        TextView textView2 = (TextView) toggledFriendsList.findViewById(R.id.status);
        ImageView imageView = (ImageView) toggledFriendsList.findViewById(R.id.icon);
        textView.setText(names[position]);
        textView2.setText(statuses[position]);
        imageView.setImageResource(R.drawable.ic_launcher);

        return toggledFriendsList;
    }
}
