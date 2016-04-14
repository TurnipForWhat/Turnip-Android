package turnip.turnip;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Button button = (Button) findViewById(R.id.search_button);
        final Context context = this;

        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText search = (EditText) findViewById(R.id.search_text);
                InputMethodManager imm = (InputMethodManager)getSystemService(context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                assert search != null;
                getResults(search.getText().toString());
            }
        });

        EditText editText = (EditText) findViewById(R.id.search_text);
        assert editText != null;
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    EditText search = (EditText) findViewById(R.id.search_text);
                    assert search != null;
                    getResults(search.getText().toString());
                }
                return false;
            }
        });
    }

    void search() {
    }

    void getResults(final String query) {
        final Activity act = this;

        new AsyncTask<Void, Void, Void>() {
           protected Void doInBackground(Void... params) {
               try {
                   final ArrayList<User> results = API.search(query);

                   act.runOnUiThread(new Runnable() {
                       public void run() {
                           SearchAdapter adapter = new SearchAdapter(act, results);

                           ListView listView = (ListView) findViewById(R.id.search_list);
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
