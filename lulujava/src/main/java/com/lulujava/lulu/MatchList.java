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
import java.util.Arrays;
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
            long milestone = 100000;
            for (CSVRecord record : records) {
                readLine(record);
                progress++;
                if (progress >= milestone) {
                    System.out.printf("[%d ms] Progress: %d records / ???%n", 
                            System.currentTimeMillis() - initmillis, milestone);
                    milestone += 100000;
                }
            }
            progress = 0; milestone = 100000;
            in.close();
            System.out.println("Parent-daughter 1st round complete.");
            System.out.println("Readjusting parent-daughter pairs...");
            readjustParents();
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
        Entry parent = otutable.find(parent_key);
        if (daughter.parent != null && daughter.parent.rank <= parent.rank)
            return false;
        
        if (tryRelativeAbundance(daughter, parent) 
                && tryRelativeCoocurrence(daughter, parent)) {
            if (daughter.parent != null) {
                daughter.parent.undoAddOTUs(daughter);
                otutable.update(daughter.parent.id, daughter.parent);
            }
            daughter.parent = parent;
            parent.addOTUs(daughter);
            otutable.update(daughter_key, daughter);
            otutable.update(parent_key, parent);
            return true;
        }
        return false;
    }
    private void readjustParents() {
        Iterator<Entry> iter = this.otutable.getIterator();
        Entry next;
        while (iter.hasNext()) {
            next = iter.next();
            if (next.parent != null) {
                while (next.parent.parent != null) {
                    Entry parent = next.parent;
                    Entry gparent = parent.parent;
                    parent.undoAddOTUs(next);
                    gparent.addOTUs(next);
                    next.parent = gparent;
                    otutable.update(parent.id, parent);
                    otutable.update(gparent.id, gparent);
                }
            }
        }
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
        CSVPrinter printerDiscardedTable = curatedTableFormat.print(
                new FileWriter(settings.out_discarded_table));
        ArrayList<String> adding = new ArrayList<>(otutable.headers);
        printerCuratedTable.printRecord(adding);
        printerDiscardedTable.printRecord(adding);
        adding.clear();
        Iterator<Entry> iter = this.otutable.getIterator();
        int curated_count = 0;
        int discarded_count = 0;
        Entry next;
        ArrayList<String> otu_counts = new ArrayList<>();
        while (iter.hasNext()) {
            next = iter.next();
            next.applyAddedOTUs();
            otu_counts.add(next.id);
            for (int i = 0; i < next.otu_counts.length; i++)
                otu_counts.add(Integer.toString(next.otu_counts[i]));
            adding.add(next.id);
            adding.add(String.valueOf(next.total));
            adding.add(String.valueOf(next.spread));
            adding.add(next.getParentID());
            if (next.parent == null) {
                curated_count++;
                adding.add("parent");
                printerCuratedTable.printRecord(otu_counts);
            } else {
                discarded_count++;
                adding.add("merged");
                printerDiscardedTable.printRecord(otu_counts);
            }
            adding.add(String.valueOf(next.rank));
            printerOTUMap.printRecord(adding);
            adding.clear();
            otu_counts.clear();
        }
        printerOTUMap.close(true);
        printerCuratedTable.close(true);
        printerDiscardedTable.close(true);
        System.out.println("curated_count = " + curated_count);
        System.out.println("discarded_count = " + discarded_count);
    }
    private boolean tryRelativeAbundance(Entry daughter, Entry parent) {
        double relativeAbundance;
        if (abundanceEstimator == AbundanceEstimator.AVG)
            relativeAbundance = daughter.mean_relative_abundance(parent);
        else
            relativeAbundance = daughter.min_relative_abundance(parent);
        return relativeAbundance > settings.minimum_ratio;
    }
    private boolean tryRelativeCoocurrence(Entry daughter, Entry parent) {
        return daughter.confidence(parent) >= settings.minimum_relative_cooccurence;
    }
}