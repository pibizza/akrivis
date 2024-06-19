package org.kie.akrivis.quarkus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kie.akrivis.quarkus.model.ScorecardModule;
import org.kie.akrivis.quarkus.model.result.Record;
import org.kie.akrivis.quarkus.model.result.Threshold;
import org.kie.yard.api.model.Input;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class ScorecardRunner {

    @Inject
    ResourceReader reader;

    public Collection<Record> run() {

        final Collection<Record> records = new ArrayList<>();

        for (final ScorecardModule module : reader.readModules()) {

            try {
            final List<Input> inputs = module.model().getInputs();
            final InputLoader inputLoader = new InputLoader();
                final Map<String, Object> resolve = inputLoader.resolve(module.configuration(), inputs);


                final Map<String, Object> result = module.run(resolve);

                records.add(
                        new Record("not used atm",
                                (Number) result.get("Score"),
                                module.model().getName(),
                                100,
                                module.yaml(),
                                new Threshold[]{
                                        new Threshold(
                                                "Blocking",
                                                50
                                        ),
                                        new Threshold(
                                                "Decent",
                                                90
                                        ),
                                }));

                // TODO what to do with results, maybe append to data base
            } catch (NotFoundException | IOException e) {
                throw new RuntimeException("Not found ");
            }
        }

        return records;
    }
}
