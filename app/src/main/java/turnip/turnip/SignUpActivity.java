package turnip.turnip;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button signUp = (Button) findViewById(R.id.signUpButton);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtName = (EditText) findViewById(R.id.txtName);
                EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
                EditText txtPassword = (EditText) findViewById(R.id.txtPassword);

                String name = txtName.getText().toString();
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                signUp(name, email, password);
            }
        });

    }

    void signUp(final String name, final String email, final String password) {
        final Activity act = this;

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                final Boolean success = API.createUser(name, password, email);
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (success) {
                            Intent intent = new Intent(act, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            new AlertDialog.Builder(act)
                                .setTitle("Could not sign up")
                                .setMessage("Make sure that your phone is connected to the internet.")
                                .setNeutralButton("Okay", null)
                                .show();
                        }
                    }
                });
                return null;
            }
        }.execute(null, null, null);


    }

}
