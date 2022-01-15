package com.lulujava.lulu;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import com.lulujava.appmanager.LULUSettings;
enum AbundanceEstimator { AVG, MIN };
public class MatchList {
    LULUSettings settings;
    OTUTable otutable;
    AbundanceEstimator abundanceEstimator;
    public MatchList(LULUSettings settings, OTUTable otutable) {
        this.settings = settings;
        this.otutable = otutable;
        abundanceEstimator = AbundanceEstimator.AVG;
        if (settings.minimum_ratio_type.compareToIgnoreCase("min") == 0)
            abundanceEstimator = AbundanceEstimator.MIN;
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
        String daughter_key = record.get(0);
        String parent_key = record.get(1);
        double match_coef = Double.valueOf(record.get(2));
        if (daughter_key.compareTo(parent_key) == 0)
            return false;
        if (parent_key.compareTo("*") == 0)
            return false;
        if (match_coef < settings.minimum_match)
            return false;
        
        Entry daughter = otutable.find(daughter_key);
        if (daughter.parent != null)
            return false;
        
        Entry parent = otutable.find(parent_key);
        double relativeAbundance;
        if (daughter.confidence(parent) < settings.minimum_relative_cooccurence)
            return false;
        if (abundanceEstimator == AbundanceEstimator.AVG)
            relativeAbundance = daughter.mean_relative_abundance(parent);
        else
            relativeAbundance = daughter.min_relative_abundance(parent);
        if (relativeAbundance < settings.minimum_ratio)
            return false;
        if (parent.parent == null)
            daughter.parent = parent;
        else
            daughter.parent = parent.parent;
        otutable.update(daughter_key, daughter);
        return true;
    }
}