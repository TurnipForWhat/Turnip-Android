package turnip.turnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;

import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.*;

import org.json.JSONObject;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        final Context context = this;
        API.init(context);
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);


        TextView signUp= (TextView) findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SignUpActivity.class);
                startActivity(intent);
            }

        });


        Button button = (Button) findViewById(R.id.logInEmail);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LogInActivity.class);
                startActivity(intent);
            }
        });


        final Activity act = this;
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                System.out.println(loginResult);
                System.out.println(loginResult.getAccessToken().getUserId());
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    final String name = object.getString("name");
                                    final String email = object.getString("email");
                                    final String id = object.getString("id");

                                    new AsyncTask<Void, Void, Void>() {
                                        protected Void doInBackground(Void... params) {
                                            if (API.loginWithFacebook(id)) {

                                            } else {
                                                API.signUpWithFacebook(id, name, email);
                                            }

                                            act.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent i = new Intent(context, ToggleActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            });
                                            return null;
                                        }
                                    }.execute(null, null, null);

                                } catch (Exception e) {
                                    System.out.println(e);
                                }
                            }
                        });

                        Bundle params = new Bundle();
                        params.putString("fields", "id,name,email");
                        request.setParameters(params);
                        request.executeAsync();
                    }
                });
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        final Button fbButton = (Button) findViewById(R.id.logInFacebook);
        fbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(act, Arrays.asList("public_profile", "user_friends", "email"));
            }
        });


        if (!API.authkey.equals("")) {
            Intent i = new Intent(context, ToggleActivity.class);
            startActivity(i);
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
