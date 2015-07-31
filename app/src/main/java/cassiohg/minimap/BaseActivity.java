package cassiohg.minimap;

import cassiohg.minimap.UserData;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by cassiohg on 7/17/15.
 */
public class BaseActivity extends AppCompatActivity {

//    public UserData userData = new UserData(this);


    public static final String PREFS_NAME = "userData";
    SharedPreferences settings;

    public Double latestLatitude;
    public Double latestLongitude;
    String simSerialNumber;
    String simCountryISO;

    public void setSimInfo(String serial, String countryISO) {
        simSerialNumber = serial;
        simCountryISO = countryISO;

    }

    public String toJSON(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("simSerialNumber", simSerialNumber);
            jsonObject.put("simCountryISO", simCountryISO);

            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }

    }

    public void toObject(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            simSerialNumber = jsonObject.getString("simSerialNumber");
            simCountryISO = jsonObject.getString("simCountryISO");
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences(PREFS_NAME, 0);

//        webb.setBaseUri(settings.getString("serverUrl", "http://10.0.0.168:3000"));

    }

}
