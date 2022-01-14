package com.lulujava.lulu;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import com.lulujava.appmanager.LULUSettings;
public class MatchList {
    LULUSettings settings;
    public MatchList(LULUSettings settings) {
        this.settings = settings;
        CSVFormat csvFormat = CSVFormat.Builder.create()
            .setDelimiter('\t')
            .setAutoFlush(true)
            .build();
        try {
            Reader in = new FileReader(settings.matchlist_file);
            CSVParser records = csvFormat.parse(in);
            for (CSVRecord record : records)
                readLine(record);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private boolean readLine(CSVRecord record) {
        String parent_key = record.get(0);
        String daughter_key = record.get(1);
        double match_coef = Double.valueOf(record.get(2));
        if (parent_key.compareTo(daughter_key) == 0)
            return false;
        if (daughter_key.compareTo("*") == 0)
            return false;
        if (match_coef < settings.minimum_match)
            return false;
        return true;
    }
}
