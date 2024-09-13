package org.kie.akrivis.quarkus;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import org.akrivis.dbmodel.*;
import org.akrivis.dbmodel.result.Configuration;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.yard.api.model.YaRD;
import org.kie.yard.api.model.YaRD_JsonMapperImpl;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class Processor {


    @Inject
    JobRepository jobRepository;

    @Inject
    CardRepository cardRepository;

    @ConfigProperty(name = "akrivis.evaluator.address")
    String service;

    @Transactional
    @Scheduled(every = "10s")
    public void scores() {

        try (Client client = ClientBuilder.newClient()) {
            final String s = service + "/yard";
            for (Request request : getRequests()) {
                try {
                    final Map mapRecord = client.target(s).request(MediaType.APPLICATION_JSON).post(Entity.entity(request.json, MediaType.APPLICATION_JSON), Map.class);
                    final Integer score = (Integer) mapRecord.get("Score");

                    final RunResult runResult = new RunResult();
                    runResult.measureName = request.yardModel.getName();
                    runResult.status = "Figure this out";
                    runResult.measureValue = score;
                    runResult.maxValue = 100; // TODo 100 will do for now
                    runResult.cardData = request.card.definition;
                    runResult.configurationData = request.card.configuration.definition;
                    runResult.card = request.card;
                    runResult.runTime = Instant.now();

                    cardRepository.getEntityManager().persist(runResult);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<Request> getRequests() {
        final Payloads payloads = getPayloads();
        return cardRepository.findAll().stream().map(card -> {
            try {
                final Map<String, Object> maps = new HashMap();
                final ObjectMapper objectMapper = new ObjectMapper();
                final String confJSON = card.configuration.definition;
                final Configuration configuration = objectMapper.readValue(confJSON, Configuration.class);
                final InputLoader inputLoader = new InputLoader(payloads);

                final YaRD model = new YaRD_JsonMapperImpl().fromJSON(card.definition);
                final Map<String, Object> input = inputLoader.resolve(configuration, model.getInputs());
                maps.put("yard", objectMapper.readValue(card.definition, Map.class));
                maps.put("input", input);

                return new Request(card, model, maps);
            } catch (NotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    private Payloads getPayloads() {
        final Payloads payloads = new Payloads();
        for (Job job : jobRepository.findActiveJobs()) {
            final RawData latestRawDataByJobId = jobRepository.findLatestRawDataByJobId(job.id);
            payloads.getPayloads().add(new Payload(job.endpoint, latestRawDataByJobId.data));
        }
        return payloads;
    }

    private record Request(Card card, YaRD yardModel,  Map<String, Object> json) {
    }
}
