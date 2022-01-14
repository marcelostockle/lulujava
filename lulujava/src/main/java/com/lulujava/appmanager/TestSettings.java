package com.lulujava.appmanager;

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
