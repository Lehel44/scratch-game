package org.game.scratch.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.game.scratch.symbols.Symbol;
import org.game.scratch.symbols.SymbolDeserializer;

/**
 * Singleton class that provides a single reusable instance of ObjectMapper
 * configured with a custom deserializer.
 */
public class ObjectMapperProvider {

    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    private ObjectMapperProvider() {
        // Private constructor for singleton class
    }

    // Singleton accessor
    public static ObjectMapper getInstance() {
        return OBJECT_MAPPER;
    }

    // Create and configure the ObjectMapper instance
    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Symbol.class, new SymbolDeserializer());
        mapper.registerModule(module);
        return mapper;
    }
}
