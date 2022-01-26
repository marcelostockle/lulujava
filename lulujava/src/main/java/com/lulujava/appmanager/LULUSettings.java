package com.lulujava.appmanager;

/**
* LULUSettings.java details a structure listing all the parameters found
* in settings.java under the label "lulu_settings". The provided constructor
* will automatically unpack the JSONObject with the label "lulu_settings".
*
* @author  Marcelo Stöckle
* @since   2022-01-24 
*/
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
    public int threads;
    public LULUSettings(JSONObject root) {
        otutable_file = root.getString("otutable_file");
        matchlist_file = root.getString("matchlist_file");
        out_otu_map = root.getString("out_otu_map");
        out_curated_table = root.getString("out_curated_table");
        minimum_ratio_type = root.getString("minimum_ratio_type");
        minimum_ratio = root.getDouble("minimum_ratio");
        minimum_match = root.getInt("minimum_match");
        minimum_relative_cooccurence = root.getDouble("minimum_relative_cooccurrence");
        threads = root.getInt("threads");
    }
}
