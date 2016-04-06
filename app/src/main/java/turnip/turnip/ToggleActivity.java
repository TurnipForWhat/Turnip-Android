package turnip.turnip;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class ToggleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toggle);
        Switch s = (Switch) findViewById(R.id.turnipToggle);

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
