package turnip.turnip;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
public class FriendRequestsAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<User> results;
    private final Activity activity;

    public FriendRequestsAdapter(Activity activity, ArrayList<User> results) {
        super(activity, -1, User.friendNames(results));
        this.context = activity;
        this.results = results;
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View searchResults = inflater.inflate(R.layout.friend_requests_list, parent, false);
        TextView name = (TextView) searchResults.findViewById(R.id.name);
        final ImageView imageView = (ImageView) searchResults.findViewById(R.id.icon);
        name.setText(results.get(position).name);
        final Button addButton = (Button) searchResults.findViewById(R.id.addFriendButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        API.acceptFriendRequest(results.get(position).id);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                addButton.setVisibility(View.INVISIBLE);
                            }
                        });
                        return null;
                    }
                }.execute(null, null, null);
            }
        });

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    final Bitmap bmp = API.getImage(results.get(position).profile_picture_id);
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

        return searchResults;
    }
}
