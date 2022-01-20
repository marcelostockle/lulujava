package com.lulujava.lulu;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
public class OTUTable {
    public List<String> headers;
    private HashMap<String, Entry> entries;
    public OTUTable(String filepath, int hashCapacity) {
        CSVFormat csvFormat = CSVFormat.Builder.create()
            .setAllowMissingColumnNames(true)
            .setDelimiter('\t')
            .setHeader()
            .setAutoFlush(true)
            .build();
        try {
            entries = new HashMap<>(hashCapacity);
            Reader in = new FileReader(filepath);
            CSVParser records = csvFormat.parse(in);
            headers = records.getHeaderNames();
            Entry newEntry;
            for (CSVRecord record : records) {
                newEntry = new Entry(record);
                entries.put(newEntry.id, newEntry);
            }
                
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Entry find(String key) {
        return entries.get(key);
    }
    public void update(String key, Entry entry) {
        entries.put(key, entry);
    }
    public Iterator<Entry> getIterator() {
        return entries.values().iterator();
    }
    public java.util.Collection<Entry> values() {
        return entries.values();
    }
}
