package turnip.turnip;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;

/**
 * Created by Brittney on 4/13/2016.
 */
public class Pop extends AppCompatActivity{






    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.6));


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.check_Drinks:
                if (checked)
                {

                }
                // Put some meat on the sandwich
                else
                // Remove the meat
                break;
            case R.id.check_Food:
                if (checked)
                {

                }
                // Cheese me
                else
                // I'm lactose intolerant
                break;
            case R.id.check_NoDrive:
                if (checked)
                {

                }
                // Put some meat on the sandwich
                else
                // Remove the meat
                break;
            case R.id.check_YesDrive:
                if (checked)
                {

                }
                // Put some meat on the sandwich
                else
                // Remove the meat
                break;
            case R.id.check_NoMoney:
                if (checked)
                {

                }
                // Put some meat on the sandwich
                else
                // Remove the meat
                break;
            case R.id.check_YesMoney:
                if (checked)
                {

                }
                // Put some meat on the sandwich
                else
                // Remove the meat
                break;
            case R.id.check_Pickup:
                if (checked)
                {

                }
                // Put some meat on the sandwich
                else
                // Remove the meat
                break;
        }
    }
}
