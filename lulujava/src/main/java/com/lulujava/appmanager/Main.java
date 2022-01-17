package com.lulujava.appmanager;

import com.lulujava.lulu.*;
import com.lulujava.tests.*;
import java.util.Iterator;
enum Program {
    CSVHeaderTest, JSONTest, OTUTableTest, lulu
}
public class Main {
    static Settings settings;
    public static void main(String[] args) {
        settings = new Settings();
        if (settings.program == Program.lulu)
            lulu();
        else if (settings.program == Program.CSVHeaderTest)
            CSVHeaderTest();
        else if (settings.program == Program.JSONTest)
            JSONTest();
        else if (settings.program == Program.OTUTableTest)
            OTUTableTest();
    }
    public static void lulu() {
        OTUTable table = new OTUTable(
            settings.luluSettings.otutable_file,
            10000000);
        MatchList matchlist = new MatchList(settings.luluSettings, table);
        System.out.println("DONE");
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
    public static void OTUTableTest() {
        OTUTable table = new OTUTable(
                settings.testSettings.in_fileA,
                settings.testSettings.intArgA);
        System.out.println(table.headers.toString());
    }
    
}
