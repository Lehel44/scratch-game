package org.game.scratch.symbols;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Custom deserializer to map symbols to their corresponding subclass.
 */
public final class SymbolDeserializer extends JsonDeserializer<Symbol> {

    /**
     * Deserializes the symbols from a JSON first based on the 'type' and
     * then on the 'impact' attribute.
     *
     * @param jp      Parser used for reading JSON content
     * @param context Context that can be used to access information about
     *                this deserialization activity.
     * @return the deserialized Symbol object
     * @throws IOException if JSON input is invalid
     */
    @Override
    public Symbol deserialize(final JsonParser jp, final DeserializationContext context) throws IOException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode node = mapper.readTree(jp);

        String type = node.get("type").asText();

        if ("standard".equals(type)) {
            return mapper.treeToValue(node, StandardSymbol.class);
        } else if ("bonus".equals(type)) {
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

