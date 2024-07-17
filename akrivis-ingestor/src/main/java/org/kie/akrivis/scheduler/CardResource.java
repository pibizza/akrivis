package org.kie.akrivis.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import org.akrivis.dbmodel.Card;
import org.akrivis.dbmodel.CardConfiguration;
import org.akrivis.dbmodel.CardRepository;

import java.io.IOException;
import java.util.Map;

@Path("/card")
public class CardResource {

    @Inject
    CardRepository cardRepository;

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

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private CardConfiguration getConfiguration(Object yaml) throws JsonProcessingException {
        final CardConfiguration configuration = new CardConfiguration();
        configuration.definition = new ObjectMapper().writeValueAsString(yaml);
        return configuration;
    }

    private static Card getCard(Object yaml) throws JsonProcessingException {
        final Card card = new Card();
        card.definition = new ObjectMapper().writeValueAsString(yaml);
        return card;
    }

    private static String yamlToJson(String yaml) throws JsonProcessingException {
        final ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        final Object o = yamlMapper.readValue(yaml, Object.class);
        final ObjectMapper jsonMapper = new ObjectMapper();
        return jsonMapper.writeValueAsString(o);
    }
}
