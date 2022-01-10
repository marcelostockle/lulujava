package com.lulujava.tests;

import org.json.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
public class JSONTest {
    public static String StringTest(String filename, String param) {
        String content;
        String query = "";
        JSONObject obj;
        try {
            content = Files.readString(Paths.get(filename));
            obj = new JSONObject(content);
            query = obj.getString(param);
        } catch (IOException e){
            e.printStackTrace();
        }
        return query;
    }
}
