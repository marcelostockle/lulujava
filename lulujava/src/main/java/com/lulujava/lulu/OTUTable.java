package com.lulujava.lulu;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
public class OTUTable {
    public List<String> headers;
    public List<Entry> entries;
    public OTUTable(String filepath) {
        
        CSVFormat csvFormat = CSVFormat.Builder.create()
            .setAllowMissingColumnNames(true)
            .setDelimiter('\t')
            .setHeader()
            .setAutoFlush(true)
            .build();
        try {
            entries = new ArrayList<>();
            Reader in = new FileReader(filepath);
            CSVParser records = csvFormat.parse(in);
            headers = records.getHeaderNames();
            for (CSVRecord record : records)
                entries.add(new Entry(record));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
