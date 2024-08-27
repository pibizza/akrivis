package org.kie.akrivis.scheduler.responses;

import org.akrivis.dbmodel.result.Threshold;

public class Result {

    public Long cardId;

    public Long runTime;
    public String status;
    public Integer measureValue;
    public String measureName;
    public Integer maxValue;
    public String yaml;
    public Threshold[] thresholds;

}
