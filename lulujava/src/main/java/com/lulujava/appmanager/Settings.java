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