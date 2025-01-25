package org.game.scratch.symbols;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SymbolDeserializer extends JsonDeserializer<Symbol> {

    @Override
    public Symbol deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);

        // Use "type" as the first discriminator
        String type = node.get("type").asText();

        if ("standard".equals(type)) {
            return mapper.treeToValue(node, StandardSymbol.class);
        } else if ("bonus".equals(type)) {
            // Use "impact" as the secondary discriminator
            String impact = node.get("impact").asText();
            return switch (impact) {
                case "multiply_reward" -> mapper.treeToValue(node, MultiplyBonusSymbol.class);
                case "extra_bonus" -> mapper.treeToValue(node, ExtraBonusSymbol.class);
                case "miss" -> mapper.treeToValue(node, MissBonusSymbol.class);
                default -> throw new IllegalArgumentException("Unknown impact: " + impact);
            };
        }

        throw new IllegalArgumentException("Unknown type: " + type);
    }
}

