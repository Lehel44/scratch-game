package org.game.scratch.game;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ConfigurationLoader {

    private final String configPath;

    public ConfigurationLoader(String configPath) {
        this.configPath = configPath;
    }

    public JsonNode loadConfig() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(new File("src/main/resources/config.json"));
    }

}
