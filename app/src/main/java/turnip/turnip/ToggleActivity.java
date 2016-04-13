package turnip.turnip;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class ToggleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toggle);
        Switch s = (Switch) findViewById(R.id.turnipToggle);

        getFeed();

        assert s != null;
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setStatus(isChecked);
            }
        });
    }

    void getFeed() {
        final Activity act = this;

        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                final UserFeed feed = API.feed();
                act.runOnUiThread(new Runnable() {
                    public void run() {
                        assert feed != null;
                        Switch s = (Switch) findViewById(R.id.turnipToggle);
                        assert s != null;
                        s.setChecked(feed.status);
                        setStatus(feed.status);
                        fillFeed(feed.friends);
                    }
                });
                return null;
            }
        }.execute(null, null, null);
    }

    void setStatus(final Boolean status) {
        TextView readiness = (TextView) findViewById(R.id.readiness);
        if (status) {
            assert readiness != null;
            readiness.setText(this.getResources().getString(R.string.readyTurnip));
        } else {
            assert readiness != null;
            readiness.setText(this.getResources().getString(R.string.notReadyTurnip));
        }

        new AsyncTask<Void, Void, Void>() {

            protected Void doInBackground(Void... params) {
                API.toggle(status);
                return null;
            }
        }.execute(null, null, null);

    }

    void fillFeed(ArrayList<User> friends) {
        ToggleFriendsAdapter adapter = new ToggleFriendsAdapter(this, friends);

        ListView listView = (ListView) findViewById(R.id.friends_toggle_list);
        assert listView != null;
        listView.setAdapter(adapter);
    }
}
