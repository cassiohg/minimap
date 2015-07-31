package cassiohg.minimap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by cassiohg on 7/17/15.
 */
public class WelcomeActivity extends BaseActivity {

    private static TelephonyManager mTelephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        buildTelephonyManager(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        simSerialNumber = settings.getString("simSerialNumber", "");
        simCountryISO = settings.getString("simCountryISO", "");

        if (simSerialNumber.isEmpty() && simCountryISO.isEmpty()) {
            validateOrCreateUser();
        } else {
            if(savedSimInfoMatchesPhoneSim()) {
                moveToNextActivity();
            }

        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//    }

    private void validateOrCreateUser() {

        // check whether user has already been created on cloud.
        simSerialNumber = mTelephonyManager.getSimSerialNumber();
        simCountryISO = mTelephonyManager.getSimCountryIso();

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // fetch data
            // go on cloud to check.
            // receive 'user already exists' or 'just created new user'.
            // if user has already been created,
            // download user's preferences.
            // else
            // save user on 'sharedPreferences',
//                SharedPreferences.Editor editor = settings.edit();
//                editor.putString("simSerialNumber", simSerialNumber);
//                editor.putString("simCountryISO", simCountryISO);
//                editor.commit();
            // move to next activity
        } else {
            // display error
        }
    }

    private boolean savedSimInfoMatchesPhoneSim() {
        String serial = mTelephonyManager.getSimSerialNumber();
        String country = mTelephonyManager.getSimCountryIso();
        if (!serial.equals(simSerialNumber) && !country.equals(simCountryISO)) {
            // user is different from last time. different sim card is being used.
            return false;
        } else {
            return true;
        }
    }

    private void moveToNextActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("jsonObject", toJSON());
        startActivity(intent);
        finish();
    }

    public static void buildTelephonyManager(Context context) {
        mTelephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
    }

}
