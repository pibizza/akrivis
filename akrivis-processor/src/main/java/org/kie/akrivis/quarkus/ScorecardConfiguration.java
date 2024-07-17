package org.kie.akrivis.quarkus;


import java.util.Optional;

public record ScorecardConfiguration(Optional<ScorecardConfiguration> parent,
                                     Configuration configuration) {

}
