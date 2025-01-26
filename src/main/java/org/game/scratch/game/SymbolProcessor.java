package org.game.scratch.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.game.scratch.probability.WeightedProbabilitySymbolGenerator;
import org.game.scratch.symbols.Symbol;

import java.util.Map;

/**
 * This class is responsible for processing the available symbols into a map structure
 * and is able to generate the bonus symbol.
 */
public class SymbolProcessor {

    private final JsonNode config;
    private final ObjectMapper mapper;

    public SymbolProcessor(final JsonNode config) {
        this.config = config;
        mapper = ObjectMapperProvider.getInstance();
    }

    public String getBonusSymbol() {
        final JsonNode jsonNodeBonusSymbols = config.get("probabilities").get("bonus_symbols").get("symbols");
        Map<String, Double> bonusSymbolMap = mapper.convertValue(jsonNodeBonusSymbols, new TypeReference<>() {
        });

        return WeightedProbabilitySymbolGenerator.generate(bonusSymbolMap);
    }

    public Map<String, Symbol> deserializeSymbols() throws JsonProcessingException {
        return mapper.readValue(
                config.get("symbols").toString(),
                mapper.getTypeFactory().constructMapType(Map.class, String.class, Symbol.class)
        );
    }

}
