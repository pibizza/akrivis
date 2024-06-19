package org.kie.akrivis.quarkus.model;


import java.util.Optional;

public record ScorecardConfiguration(Optional<ScorecardConfiguration> parent,
                                     Configuration configuration) {

}
