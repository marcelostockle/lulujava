package com.lulujava.tests;

import java.util.Iterator;
public class Main {
    public static void main(String[] args) {
        CSVHeaderTest();
        // JSONTest();
    }
    public static void JSONTest() {
        String s = JSONTest.StringTest("Example_data\\json_example.json", "settings", "id");
        System.out.println(s);
        s = JSONTest.StringTest("Example_data\\json_example.json", "settings", "value");
        System.out.println(s);
    }
    public static void CSVHeaderTest() {
        Iterator<String> iter = CSVTest.readHeaders("Example_data\\otutable_test.txt");
        while (iter.hasNext())
            System.out.println(iter.next());
    }
}
