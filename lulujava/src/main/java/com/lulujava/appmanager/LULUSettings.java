package com.lulujava.appmanager;

import org.json.JSONObject;
public class LULUSettings {
    public String otutable_file;
    public String matchlist_file;
    public String out_otu_map;
    public String out_curated_table;
    public String minimum_ratio_type;
    public double minimum_ratio;
    public int minimum_match;
    public double minimum_relative_cooccurence;
    public LULUSettings(JSONObject root) {
        otutable_file = root.getString("otutable_file");
        matchlist_file = root.getString("matchlist_file");
        out_otu_map = root.getString("out_otu_map");
        out_curated_table = root.getString("out_curated_table");
        minimum_ratio_type = root.getString("minimum_ratio_type");
        minimum_ratio = root.getDouble("minimum_ratio");
        minimum_match = root.getInt("minimum_match");
        minimum_relative_cooccurence = root.getDouble("minimum_relative_cooccurrence");
    }
}
