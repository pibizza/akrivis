package org.kie.akrivis.quarkus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

import jakarta.transaction.Transactional;
import org.akrivis.dbmodel.Card;
import org.akrivis.dbmodel.CardConfiguration;
import org.akrivis.dbmodel.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@QuarkusTest
class ProcessorTest {

    @Inject
    Processor processor;

    @Inject
    CardRepository cardRepository;

    @BeforeEach
    @Transactional
    public void before() throws IOException {

        final String conf = read("src/test/resources/configuration.yml");
        final String code = read("src/test/resources/level-score.yard.yml");

        // make card
        final Card card = new Card();
        card.definition = code;
        final CardConfiguration configuration = new CardConfiguration();
        configuration.definition = conf;
        card.configuration = configuration;

        cardRepository.getEntityManager().persist(configuration);
        cardRepository.persist(card);
    }

    private static String read(String first) throws IOException {
        final Path path = Path.of(first);
        final String yaml = Files.readString(path);
        final Map yamlMap = new ObjectMapper(new YAMLFactory()).readValue(yaml, Map.class);
        return new ObjectMapper().writeValueAsString(yamlMap);
    }

}