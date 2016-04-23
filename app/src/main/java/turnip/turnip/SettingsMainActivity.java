package turnip.turnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_main);
        final Context context = this;
        Button UploadPhotoBtn = (Button)findViewById(R.id.button4);

        UploadPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SettingsPageActivity.class);
                startActivity(intent);
            }

        });

        Button saveProfileSettings = (Button)findViewById(R.id.saveBtn);
        saveProfileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = ((EditText) findViewById(R.id.name)).getText().toString();
                final String email = ((EditText) findViewById(R.id.email)).getText().toString();
                final String password = ((EditText) findViewById(R.id.password)).getText().toString();
                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        final Boolean success = API.updateUserSettings(name, password, email);
                        return null;
                    }
                }.execute(null, null, null);
            }
        });

        final Activity act = this;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                final User user = API.getMe();
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((EditText)findViewById(R.id.name)).setText(user.name);
                        ((EditText)findViewById(R.id.email)).setText(user.email);
                    }
                });
                return null;
            }
        }.execute(null, null, null);
    }




}
