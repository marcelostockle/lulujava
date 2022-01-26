package com.lulujava.lulu;

/**
* MatchList.java is responsible for reading and parsing the provided Match List
* type input file. Since most crucial tasks in the LULU algorithm happen during
* the scanning of a Match List type input file, the functions detailed in 
* this class encompasses the bulk of the runtime.
* 
* The provided constructor does NOT take care of the tasks outlined above. To
* start the program, use MatchList.run().
*
* @author  Marcelo Stöckle
* @since   2022-01-24 
*/
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import com.lulujava.appmanager.LULUSettings;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.csv.CSVPrinter;
public class MatchList {
    LULUSettings settings;
    OTUTable otutable;
    AbundanceEstimator abundanceEstimator;
    private long initmillis;
    final CSVFormat csvFormat = CSVFormat.Builder.create()
        .setDelimiter('\t')
        .setAutoFlush(true)
        .build();
    public MatchList(LULUSettings settings, OTUTable otutable) {
        this.settings = settings;
        this.otutable = otutable;
        abundanceEstimator = AbundanceEstimator.AVG;
        if (settings.minimum_ratio_type.compareToIgnoreCase("min") == 0)
            abundanceEstimator = AbundanceEstimator.MIN;
    }
    public void run() {
        try {
            this.initmillis = System.currentTimeMillis();
            System.out.println("Starting parent-daughter matching...");
            Reader in = new FileReader(settings.matchlist_file);
            CSVParser records = csvFormat.parse(in);
            long progress = 0;
            long milestone = 10000;
            for (CSVRecord record : records) {
                readLine(record);
                progress++;
                if (progress >= milestone) {
                    System.out.printf("[%d ms] Progress: %d records / ???%n", 
                            System.currentTimeMillis() - initmillis, milestone);
                    milestone += 10000;
                }
            }
            System.out.println("Parent-daughter match complete.");
            System.out.println("Saving results...");
            parseResults();
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
        if (match_coef <= settings.minimum_match)
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
        if (relativeAbundance <= settings.minimum_ratio)
            return false;
        if (parent.parent == null)
            daughter.parent = parent;
        else
            daughter.parent = parent.parent;
        otutable.update(daughter_key, daughter);
        return true;
    }
    private void parseResults() throws IOException {      
        CSVFormat otuMapFormat = CSVFormat.Builder.create()
            .setDelimiter(',')
            .setAutoFlush(true)
            .setHeader("id", "total", "spread", "parent_id", "curated", "rank")
            .build();
        CSVFormat curatedTableFormat = CSVFormat.Builder.create()
            .setDelimiter(',')
            .setAutoFlush(true)
            .build();
        CSVPrinter printerOTUMap = otuMapFormat.print(
                new FileWriter(settings.out_otu_map));
        CSVPrinter printerCuratedTable = curatedTableFormat.print(
                new FileWriter(settings.out_curated_table));
        ArrayList<String> adding = new ArrayList<>(otutable.headers);
        printerCuratedTable.printRecord(adding);
        adding.clear();
        Iterator<Entry> iter = this.otutable.getIterator();
        int curated_count = 0;
        int discarded_count = 0;
        Entry next;
        while (iter.hasNext()) {
            next = iter.next();
            adding.add(next.id);
            adding.add(String.valueOf(next.total));
            adding.add(String.valueOf(next.spread));
            adding.add(next.getParentID());
            if (next.parent == null) {
                curated_count++;
                adding.add("parent");
                printerCuratedTable.printRecord(next.record);
            } else {
                discarded_count++;
                adding.add("merged");
            }
            adding.add(String.valueOf(next.rank));
            printerOTUMap.printRecord(adding);
            adding.clear();
        }
        printerOTUMap.close(true);
        printerCuratedTable.close(true);
        System.out.println("curated_count = " + curated_count);
        System.out.println("discarded_count = " + discarded_count);
    }
}