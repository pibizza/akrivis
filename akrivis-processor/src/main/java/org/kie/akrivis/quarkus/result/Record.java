package org.kie.akrivis.quarkus.result;

import java.util.ArrayList;
import java.util.List;

public class Record {

    public String status;
    public Integer measureValue;
    public String measureName;

    public Integer maxValue;
    public String yaml;

    public List<Threshold> thresholds = new ArrayList<>();

}
