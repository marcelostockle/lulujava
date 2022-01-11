package com.lulujava.tests;

public class Main {
    public static void main(String[] args) {
        JSONTest();
    }
    public static void JSONTest() {
        String s = JSONTest.StringTest("Example_data\\json_example.json", "settings", "id");
        System.out.println(s);
        s = JSONTest.StringTest("Example_data\\json_example.json", "settings", "value");
        System.out.println(s);
    }
}
