package org.kie.akrivis.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.akrivis.dbmodel.*;
import org.akrivis.dbmodel.result.Configuration;
import org.akrivis.dbmodel.result.Threshold;
import org.kie.akrivis.scheduler.responses.Result;
import org.kie.akrivis.utility.BackstageResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/card")
public class CardResource {

    @Inject
    CardRepository cardRepository;

    @Inject
    ResultRepository resultRepository;
    private static final String DEFINITION = "definition";

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public void add(Map<String, Object> payload) {
        try {

            final CardConfiguration cardConfiguration = getConfiguration(payload.get("configuration"));
            final Card card = getCard(payload.get("card"));
            cardRepository.getEntityManager().persist(cardConfiguration);
            card.configuration = cardConfiguration;
            cardRepository.persist(card);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @Path("/list")
    public BackstageResponse<List<Card>> listCards() {
        return new BackstageResponse<>(cardRepository.findAll().stream().toList());
    }

    @GET
    @Path("{id}/history")
    public BackstageResponse<List<Result>> history(@PathParam("id") Long cardId) {
        // TODO maybe something lighter than the full RunResult would be enough
        final List<Result> list = resultRepository.history(cardId).stream().map(runResult ->
                formResult(runResult)
        ).toList();

        return new BackstageResponse<>(list);
    }

    @GET
    @Path("/results")
    public BackstageResponse<List<Result>> results() {
        final List<Result> results = new ArrayList<>();
        final List<Card> cards = cardRepository.findAll().stream().toList();

        cards.forEach(card -> {
                    final RunResult runResult = resultRepository.latest(card.id);
                    results.add(formResult(runResult));
                }
        );
        return new BackstageResponse<>(results);
    }

    private Result formResult(final RunResult runResult) {
        return new Result() {
            {
                cardId = runResult.card.id;
                runTime = runResult.runTime.getEpochSecond();
                status = runResult.status;
                measureValue = runResult.measureValue;
                measureName = runResult.measureName;
                maxValue = runResult.maxValue;
                yaml = runResult.cardData;
                thresholds = getThresholds(runResult);
            }
        };
    }

    private static Threshold[] getThresholds(RunResult runResult) {

        try {
            final ObjectMapper jsonMapper = new ObjectMapper();
            final Configuration configuration = jsonMapper.readValue(runResult.configurationData, Configuration.class);
            return configuration.thresholds;
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // TODO report/log
        }

        return new Threshold[0];
    }

    private CardConfiguration getConfiguration(Object o) throws JsonProcessingException {
        if (o instanceof Map map) {
            final CardConfiguration configuration = new CardConfiguration();
            if (map.get(DEFINITION) instanceof String yaml) {
                final Map yamlMap = new ObjectMapper(new YAMLFactory()).readValue(yaml, Map.class);
                configuration.definition = new ObjectMapper().writeValueAsString(yamlMap);
            }
            return configuration;
        }
        throw new IllegalArgumentException("Card configuration data was not in correct format.");
    }

    private Card getCard(Object o) throws JsonProcessingException {
        if (o instanceof Map map) {
            final Card card = new Card();
            if (map.get(DEFINITION) instanceof String yaml) {
                final Map yamlMap = new ObjectMapper(new YAMLFactory()).readValue(yaml, Map.class);
                card.definition = new ObjectMapper().writeValueAsString(yamlMap);
            }

            return card;
        }
        throw new IllegalArgumentException("Card data was not in correct format.");
    }
}
