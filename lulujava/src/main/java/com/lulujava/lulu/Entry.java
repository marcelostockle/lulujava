package com.lulujava.lulu;

/**
* Entry.java consists of a structure detailing a single entry found in an 
* OTU table type input file. The provided constructor unpacks a single entry
* with the use of the Commons CSV library appended to this project.
* 
* Entry-specific parameters "total" and "spread" are calculated on construction,
* while "rank" is calculated independently by OTUTable.java.
*
* @author  Marcelo Stöckle
* @since   2022-01-24 
*/
import java.util.Iterator;
import org.apache.commons.csv.CSVRecord;
public class Entry implements Comparable<Entry> {
    int[] otu_counts;
    String id;
    int total, spread, rank;
    Entry parent;
    CSVRecord record;
    public Entry(CSVRecord line) {
        this.record = line;
        this.parent = null;
        this.total = 0;
        this.spread = 0;
        Iterator<String> iter = line.iterator();
        this.id = iter.next();
        this.otu_counts = new int[line.size() - 1];
        int i = 0;
        while (iter.hasNext()) {
            this.otu_counts[i] = Integer.valueOf(iter.next());
            this.total += this.otu_counts[i];
            if (this.otu_counts[i] > 0)
                this.spread++;
            i++;
        }
    }
    
    public double confidence(Entry parent) {
        int count_and = 0;
        for (int i = 0; i < this.otu_counts.length; i++) {
            if (this.otu_counts[i] * parent.otu_counts[i] > 0)
                count_and++;
        }
        return (double) count_and / this.spread;
    }
    
    public double mean_relative_abundance(Entry parent) {
        double accumulate = 0;
        for (int i = 0; i < this.otu_counts.length; i++) {
            if (this.otu_counts[i] > 0)
                accumulate += (double) parent.otu_counts[i] / this.otu_counts[i];
        }
        return accumulate / this.spread;
    }
    
    public double min_relative_abundance(Entry parent) {
        double minimum = 1e12;
        for (int i = 0; i < this.otu_counts.length; i++) {
            if (this.otu_counts[i] > 0)
                minimum = Math.min(minimum, (double) parent.otu_counts[i] / this.otu_counts[i]);
        }
        return minimum;
    }
    
    public void addOTUs(Entry daughter) {
        for (int i = 0; i < otu_counts.length; i++)
            this.otu_counts[i] += daughter.otu_counts[i];
    }
    
    public void undoAddOTUs(Entry daughter) {
        for (int i = 0; i < otu_counts.length; i++)
            this.otu_counts[i] -= daughter.otu_counts[i];
    }
    
    public String getParentID() {
        if (this.parent == null)
            return this.id;
        return parent.id;
    }
    
    @Override public int compareTo(Entry e) {
        if (this.spread == e.spread)
            return this.total - e.total;
        return this.spread - e.spread;
    }  
}
