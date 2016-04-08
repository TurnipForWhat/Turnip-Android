package turnip.turnip;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

public class ToggleActivity extends AppCompatActivity {
    String[] names = {"Tyler", "Sarah", "Prince", "Emily", "Jonathan", "Brittney", "Ivey",
            "Austin", "Sean", "Jacob", "Eric", "Elise", "Chris"};
    String[] statuses = {"Ready to Turn up", "Ready to Turn up", "Ready to Turn up",
            "Ready to Turn up", "Ready to Turn up", "Ready to Turn up", "Ready to Turn up",
            "Not Ready to Turn up", "Not Ready to Turn up", "Not Ready to Turn up",
            "Not Ready to Turn up", "Not Ready to Turn up", "Ready to Turn up"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toggle);
        Switch s = (Switch) findViewById(R.id.turnipToggle);

        ToggleFriendsAdapter adapter = new ToggleFriendsAdapter(this, names, statuses);

        ListView listView = (ListView) findViewById(R.id.friends_toggle_list);
        listView.setAdapter(adapter);

        final Context context = this;
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView readiness = (TextView) findViewById(R.id.readiness);
                if (isChecked) {
                    readiness.setText(context.getResources().getString(R.string.readyTurnip));
                } else {
                    readiness.setText(context.getResources().getString(R.string.notReadyTurnip));
                }
            }
        });
    }
}
