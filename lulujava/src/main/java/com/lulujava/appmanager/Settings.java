package com.lulujava.appmanager;

import org.json.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
public class Settings {
    private JSONObject root;
    final String settings_filepath = "settings.json";
    TestSettings testSettings;
    LULUSettings luluSettings;
    Program program;
    public Settings() {
        String content;
        try {
            content = Files.readString(Paths.get(settings_filepath));
            root = new JSONObject(content);
            parseJSON();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    private void parseJSON() {
        if (root != null) {
            program = root.getEnum(Program.class, "program");
            testSettings = new TestSettings(root.getJSONObject("test_settings"));
            luluSettings = new LULUSettings(root.getJSONObject("lulu_settings"));
        } else {
            System.err.println("Corrupt settings data at " + settings_filepath);
        }
    }
}
class TestSettings {
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
class LULUSettings {
    String minimum_ratio_type;
    int minimum_ratio;
    int minimum_match;
    double minimum_relative_cooccurence;
    public LULUSettings(JSONObject root) {
        minimum_ratio_type = root.getString("minimum_ratio_type");
        minimum_ratio = root.getInt("minimum_ratio");
        minimum_match = root.getInt("minimum_match");
        minimum_relative_cooccurence = root.getDouble("minimum_relative_cooccurence");
    }
}
