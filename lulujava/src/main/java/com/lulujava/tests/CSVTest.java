package com.lulujava.tests;

import java.io.*;
import java.util.Iterator;
import org.apache.commons.csv.*;
public class CSVTest {
    
    public static Iterator<String> readHeaders(String filename) {
        CSVFormat csvFormat = CSVFormat.Builder.create()
            .setAllowMissingColumnNames(true)
            .setDelimiter('\t')
            .setHeader()
            .setAutoFlush(true)
            .build();
        try {
            Reader in = new FileReader(filename);
            CSVParser records = csvFormat.parse(in);
            return records.getHeaderNames().iterator();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
