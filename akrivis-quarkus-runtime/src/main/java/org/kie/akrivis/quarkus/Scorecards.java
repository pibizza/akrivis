package org.kie.akrivis.quarkus;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.kie.akrivis.quarkus.model.ScorecardModule;
import org.kie.akrivis.quarkus.model.result.Record;

import java.io.IOException;
import java.util.Collection;

@Path("/scorecards")
public class Scorecards {

    @Inject
    ResourceReader reader;

    @Inject
    ScorecardRunner runner;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<ScorecardModule> list() throws IOException {
        return reader.readModules();
    }

    @GET
    @Path("/run")
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Record> run() throws IOException {
        return runner.run();
    }
}
