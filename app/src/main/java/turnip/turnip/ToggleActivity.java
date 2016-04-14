package turnip.turnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class ToggleActivity extends AppCompatActivity {
    static final String TAG = "ToggleActivity";

    private boolean ignoreInput = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toggle);
        final Context context = this;

        Switch s = (Switch) findViewById(R.id.turnip_toggle);

        getFeed();

        assert s != null;
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (ignoreInput) return;
                setUIStatus(isChecked);
                setStatus(isChecked);
            }
        });

        final ImageButton settings = (ImageButton) findViewById(R.id.settingsButton);

        assert settings != null;
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(ToggleActivity.this, settings);
                popup.getMenuInflater()
                        .inflate(R.menu.settings_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString()) {
                            case ("Search"):
                                Intent searchIntent = new Intent(context, SearchActivity.class);
                                startActivity(searchIntent);
                                break;
                            case ("Settings"):
                                Intent intent = new Intent(context, SettingsMainActivity.class);
                                startActivity(intent);
                                break;
                            case ("Friend Requests"):
                                Intent friendRequests = new Intent(context, FriendRequestActivity.class);
                                startActivity(friendRequests);
                                break;
                            default:
                                API.signOut();
                                Intent signOutIntent = new Intent(context, MainActivity.class);
                                startActivity(signOutIntent);
                                finish();
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        ListView friends = (ListView) findViewById(R.id.friends_toggle_list);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        assert swipeRefreshLayout != null;
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFeed();
            }
        });
    }

    void getFeed() {
        final Activity act = this;

        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                final UserFeed feed = API.feed();
                System.out.println(API.friendRequests());
                act.runOnUiThread(new Runnable() {
                    public void run() {
                        assert feed != null;
                        Switch s = (Switch) findViewById(R.id.turnip_toggle);
                        assert s != null;
                        ignoreInput = true;
                        s.setChecked(feed.status);
                        ignoreInput = false;
                        setUIStatus(feed.status);
                        fillFeed(feed.friends);
                        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
                        assert swipeRefreshLayout != null;
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
                return null;
            }
        }.execute(null, null, null);
    }

    void setUIStatus(final Boolean status) {
        TextView readiness = (TextView) findViewById(R.id.readiness);
        if (status) {
            assert readiness != null;
            readiness.setText(this.getResources().getString(R.string.readyTurnip));
        } else {
            assert readiness != null;
            readiness.setText(this.getResources().getString(R.string.notReadyTurnip));
        }
    }

    void setStatus(final Boolean status) {
        new AsyncTask<Void, Void, Void>() {

            protected Void doInBackground(Void... params) {
                Log.i(TAG, "Updating toggled status");
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
