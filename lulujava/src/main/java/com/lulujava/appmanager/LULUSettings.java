package com.lulujava.appmanager;

import org.json.JSONObject;
enum AbundanceEstimator { AVG, MIN };
public class LULUSettings {
    public String otutable_file;
    public String matchlist_file;
    public String minimum_ratio_type;
    public int minimum_ratio;
    public int minimum_match;
    public double minimum_relative_cooccurence;
    public AbundanceEstimator abundanceEstimator;
    public LULUSettings(JSONObject root) {
        otutable_file = root.getString("otutable_file");
        matchlist_file = root.getString("matchlist_file");
        minimum_ratio_type = root.getString("minimum_ratio_type");
        minimum_ratio = root.getInt("minimum_ratio");
        minimum_match = root.getInt("minimum_match");
        minimum_relative_cooccurence = root.getDouble("minimum_relative_cooccurence");
        abundanceEstimator = AbundanceEstimator.AVG;
        if (minimum_ratio_type.compareToIgnoreCase("min") == 0)
            abundanceEstimator = AbundanceEstimator.MIN;
    }
}
