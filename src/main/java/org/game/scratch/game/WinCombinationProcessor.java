package org.game.scratch.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.game.scratch.process.SymbolWinCombinationTracker;
import org.game.scratch.wincombinations.WinCombinations;

import java.util.HashMap;
import java.util.Map;

public class WinCombinationProcessor {

    private String[][] gameMatrix;
    private JsonNode config;
    private ObjectMapper mapper;

    public WinCombinationProcessor(String[][] gameMatrix, JsonNode config) {
        this.gameMatrix = gameMatrix;
        this.config = config;
        this.mapper = ObjectMapperProvider.getInstance();
    }

    public SymbolWinCombinationTracker processWinCombinations() throws JsonProcessingException {
        final WinCombinations winCombinations = mapper.treeToValue(config.get("win_combinations"), WinCombinations.class);
        final Map<String, Integer> symbolCountMap = countSameSymbols(gameMatrix);

        final SymbolWinCombinationTracker winCombinationTracker = new SymbolWinCombinationTracker();
        winCombinationTracker.processWinCombinations(symbolCountMap, winCombinations, gameMatrix);
        return winCombinationTracker;
    }

    private Map<String, Integer> countSameSymbols(final String[][] gameMatrix) {
        final Map<String, Integer> countSymbolMap = new HashMap<>();
        for (String[] row : gameMatrix) {
            for (String symbol : row) {
                countSymbolMap.merge(symbol, 1, Integer::sum);
            }
        }
        return countSymbolMap;
    }
}
