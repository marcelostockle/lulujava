package com.lulujava.lulu;

import java.util.Iterator;
import org.apache.commons.csv.CSVRecord;
public class Entry {
    int[] otu_counts;
    String id;
    int total, spread;
    Entry parent;
    public Entry(CSVRecord line) {
        parent = null;
        total = 0;
        spread = 0;
        Iterator<String> iter = line.iterator();
        id = iter.next();
        otu_counts = new int[line.size() - 1];
        int i = 0;
        while (iter.hasNext()) {
            otu_counts[i] = Integer.valueOf(iter.next());
            total += otu_counts[i];
            if (otu_counts[i] > 0)
                spread++;
            i++;
        }
    }
            
}
