package com.lulujava.lulu;

import java.util.Iterator;
import org.apache.commons.csv.CSVRecord;
public class Entry {
    int[] otu_counts;
    String id;
    int total, spread;
    public Entry(CSVRecord line) {
        Iterator<String> iter = line.iterator();
        this.id = iter.next();
        this.otu_counts = new int[line.size() - 1];
        int i = 0;
        while (iter.hasNext()) {
            this.otu_counts[i] = Integer.valueOf(iter.next());
            i++;
        }
    }
            
}
