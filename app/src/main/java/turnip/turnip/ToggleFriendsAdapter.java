package turnip.turnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ToggleFriendsAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final Activity activity;
    private final ArrayList<User> friends;

    public ToggleFriendsAdapter(Activity activity, ArrayList<User> friends) {
        super(activity, -1, User.friendNames(friends));
        this.context = activity;
        this.friends = friends;
        this.activity = activity;
        System.out.println(this.friends);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View toggledFriendsList = inflater.inflate(R.layout.toggled_friends_list, parent, false);
        TextView name = (TextView) toggledFriendsList.findViewById(R.id.name);
        TextView status = (TextView) toggledFriendsList.findViewById(R.id.status);
        final ImageView imageView = (ImageView) toggledFriendsList.findViewById(R.id.icon);
        name.setText(friends.get(position).name);
        assert status != null;
        status.setText(friends.get(position).getTurnipStatus());

        toggledFriendsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, WebViewChat.class);
                i.putExtra("with", String.valueOf(friends.get(position).id));
                context.startActivity(i);
            }
        });

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Log.i("Tag", "Loading image");

                try {
                    final Bitmap bmp = API.getImage(friends.get(position).profile_picture_id);
                    Log.i("Tag", "bmppp");
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bmp);
                        }
                    });
                } catch (Exception e) {
                    Log.i("Tag", "failed!");
                    Log.i("Tag", e.toString());
                    System.out.println(e);
                }

                return null;
            }
        }.execute(null, null, null);

        return toggledFriendsList;
    }
}
