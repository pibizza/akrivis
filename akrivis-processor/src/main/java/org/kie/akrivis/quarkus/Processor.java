package org.kie.akrivis.quarkus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import org.akrivis.dbmodel.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.yard.api.model.YaRD;
import org.kie.yard.api.model.YaRD_YamlMapperImpl;

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
        System.out.println("DONG");

        try (Client client = ClientBuilder.newClient()) {
            final String s = service + "/yard";
            for (Request request : getRequests()) {
                final Map mapRecord = client.target(s).request(MediaType.APPLICATION_JSON).post(Entity.entity(request.json, MediaType.APPLICATION_JSON), Map.class);
                final Integer score = (Integer) mapRecord.get("Score");

                final RunResult runResult = new RunResult();
                runResult.value = score;
                runResult.card = request.card;
                runResult.runTime = Instant.now();
                cardRepository.getEntityManager().persist(runResult);
            }
        }
    }

    private List<Request> getRequests() {
        final Payloads payloads = getPayloads();
        return cardRepository.findAll().stream().map(card -> {
            final Map<String, Object> maps = new HashMap();
            try {
                final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
                final String confYaml = card.configuration.definition;
                final Configuration configuration = objectMapper.readValue(confYaml, Configuration.class);
                final InputLoader inputLoader = new InputLoader(payloads);

                final YaRD model = new YaRD_YamlMapperImpl().read(card.definition);
                final Map<String, Object> input = inputLoader.resolve(configuration, model.getInputs());
                maps.put("yard", objectMapper.readValue(card.definition, Map.class));
                maps.put("input", input);
            } catch (NotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
            return new Request(card, maps);
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

    private record Request(Card card, Map<String, Object> json) {
    }
}
