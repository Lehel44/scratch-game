package org.game.scratch.game;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.game.scratch.wincombinations.WinCombination;
import org.game.scratch.wincombinations.WinCombinations;

import java.util.HashMap;
import java.util.Map;

/*
 * This class processes the win combinations for a given game matrix. It evaluates the symbols in
 *  the matrix against the provided win combinations configuration and tracks the winning combinations
 *  for each symbol.
 */
public class WinCombinationProcessor {

    private final String[][] gameMatrix;
    private final JsonNode config;
    private final ObjectMapper mapper;

    public WinCombinationProcessor(final String[][] gameMatrix, final JsonNode config) {
        this.gameMatrix = gameMatrix;
        this.config = config;
        this.mapper = ObjectMapperProvider.getInstance();
    }

    public Map<String, Map<String, WinCombination>> processWinCombinations() throws JsonProcessingException {
        final WinCombinations winCombinations = mapper.treeToValue(config.get("win_combinations"), WinCombinations.class);
        final Map<String, Integer> symbolCountMap = countSameSymbols(gameMatrix);

        final SymbolWinCombinationTracker winCombinationTracker = new SymbolWinCombinationTracker();
        return winCombinationTracker.processWinCombinations(symbolCountMap, winCombinations, gameMatrix);
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
