package org.kie.akrivis.scheduler.responses;

import org.akrivis.dbmodel.result.Threshold;

public record Result(
    Long cardId,
    Long runTime,
    String status,
    Integer measureValue,
    String measureName,
    Integer maxValue,
    String yaml,
    Threshold[] thresholds

){}
