package turnip.turnip;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class FriendRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        final Activity act = this;

        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                try {
                    final ArrayList<User> results = API.friendRequests();

                    act.runOnUiThread(new Runnable() {
                        public void run() {
                            FriendRequestsAdapter adapter = new FriendRequestsAdapter(act, results);

                            ListView listView = (ListView) findViewById(R.id.friend_request_list);
                            listView.setAdapter(adapter);
                        }
                    });
                } catch (Exception e) {
                    Log.i("oops", "error");
                    Log.i("oops", e.toString());
                    System.out.println(e);
                }
                return null;
            }

        }.execute(null, null, null);
    }

}
