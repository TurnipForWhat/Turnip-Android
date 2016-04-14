package turnip.turnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        final Context context = this;

        Button button = (Button) findViewById(R.id.logInButton);

        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txtEmail = (EditText) findViewById(R.id.email);
                EditText txtPassword = (EditText) findViewById(R.id.password);
                assert txtEmail != null;
                assert txtPassword != null;
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                logIn(email, password);
            }
        });

        TextView signup = (TextView) findViewById(R.id.sign_up);
        assert signup != null;
        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void logIn(final String email, final String password) {
        final Activity act = this;

        final Context context = this;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                final Boolean success = API.login(email, password);
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (success) {
                            Intent intent = new Intent(context, ToggleActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            new AlertDialog.Builder(act)
                                .setTitle(R.string.couldNotLogIn)
                                .setMessage(R.string.loginError)
                                .setNeutralButton(R.string.okay, null)
                                .show();
                        }
                    }
                });
                return null;
            }

        }.execute(null, null, null);
    }
}
