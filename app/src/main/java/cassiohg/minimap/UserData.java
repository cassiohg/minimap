package cassiohg.minimap;

/**
 * Created by cassiohg on 7/15/15.
 */
public class UserData {

    public static final String PREFS_NAME = "userData";

    public Double latestLatitude;
    public Double latestLongitude;
    String simSerialNumber;
    String simCountryISO;


    private BaseActivity base;

    public UserData(BaseActivity b) {
        base = b;
    }

    public void setSimInfo(String serial, String countryISO) {
        simSerialNumber = serial;
        simCountryISO = countryISO;

    }



}
