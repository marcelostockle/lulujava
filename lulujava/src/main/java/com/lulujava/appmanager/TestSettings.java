package com.lulujava.appmanager;

/**
* LULUSettings.java details a structure listing all the parameters found
* in settings.java under the label "test_settings". The provided constructor
* will automatically unpack the JSONObject with the label "test_settings".
*
* @author  Marcelo Stöckle
* @since   2022-01-24 
*/
import org.json.JSONObject;
public class TestSettings {
    String in_fileA;
    String strArgA;
    String strArgB;
    int intArgA;
    int intArgB;
    public TestSettings(JSONObject root) {
        in_fileA = root.getString("in_fileA");
        strArgA = root.getString("strArgA");
        strArgB = root.getString("strArgB");
        intArgA = root.getInt("intArgA");
        intArgB = root.getInt("intArgB");
    }
}
