package com.lulujava.appmanager;

import com.lulujava.tests.CSVTest;
import com.lulujava.tests.JSONTest;
import java.util.Iterator;
enum Program {
    CSVHeaderTest, JSONTest
}
public class Main {
    static Settings settings;
    public static void main(String[] args) {
        settings = new Settings();
        if (settings.program == Program.CSVHeaderTest) {
            CSVHeaderTest();
        } else if (settings.program == Program.JSONTest) {
            JSONTest();
        }
    }
    public static void JSONTest() {
        String s = JSONTest.StringTest(
                settings.testSettings.in_fileA,
                settings.testSettings.strArgA,
                settings.testSettings.strArgB);
        System.out.println(s);
    }
    public static void CSVHeaderTest() {
        Iterator<String> iter = CSVTest.readHeaders(settings.testSettings.in_fileA);
        while (iter.hasNext())
            System.out.println(iter.next());
    }
    
}
