package org.game.scratch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.game.scratch.prizes.PrizeCalculator;
import org.game.scratch.probability.WeightedProbabilitySymbolGenerator;
import org.game.scratch.process.SymbolWinCombinationTracker;
import org.game.scratch.wincombinations.WinCombination;
import org.game.scratch.wincombinations.WinCombinations;
import org.game.scratch.symbols.StandardSymbol;
import org.game.scratch.symbols.Symbol;
import org.game.scratch.symbols.SymbolDeserializer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SplittableRandom;


//TODO unmodifiable getters
public class Main {
    public static void main(String[] args) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Symbol.class, new SymbolDeserializer());
        mapper.registerModule(module);


        JsonNode jsonNode = mapper.readTree(new File("src/main/resources/config.json"));
        String symbolsJson = jsonNode.get("symbols").toString();

        // Deserialize symbols into a Map<String, Symbol>
        Map<String, Symbol> symbols = mapper.readValue(
                symbolsJson,
                mapper.getTypeFactory().constructMapType(Map.class, String.class, Symbol.class)
        );

        // Print the deserialized symbols
        symbols.forEach((key, value) -> System.out.printf("Name: %s, Class: %s%n", key, value.getClass().getSimpleName()));

        int columns = jsonNode.get("columns").asInt();
        int rows = jsonNode.get("rows").asInt();

        String[][] symbol2DArray = new String[rows][columns];

        JsonNode probabilityArray = jsonNode.get("probabilities").get("standard_symbols");

        for (JsonNode node : probabilityArray) {
            int column = node.get("column").asInt();
            int row = node.get("row").asInt();
            Map<String, Double> map = mapper.convertValue(node.get("symbols"), new TypeReference<>() {
            });
            String chosenSymbol = WeightedProbabilitySymbolGenerator.generate(map);
            symbol2DArray[row][column] = chosenSymbol;
        }

        SplittableRandom random = new SplittableRandom();
        int bonusColumn = random.nextInt(0, columns - 1);
        int bonusRow = random.nextInt(0, rows - 1);

        JsonNode jsonNodeBonusSymbols = jsonNode.get("probabilities").get("bonus_symbols").get("symbols");
        Map<String, Double> bonusSymbolMap = mapper.convertValue(jsonNodeBonusSymbols, new TypeReference<>() {
        });

        String bonusSymbol = WeightedProbabilitySymbolGenerator.generate(bonusSymbolMap);

        symbol2DArray[bonusRow][bonusColumn] = bonusSymbol;

        for (int i = 0; i < symbol2DArray.length; i++) {
            for (int j = 0; j < symbol2DArray[i].length; j++) {
                System.out.print(symbol2DArray[i][j] + " ");
            }
            System.out.println();
        }

        JsonNode winCombinationsNode = jsonNode.get("win_combinations");

        WinCombinations winCombinations = mapper.treeToValue(winCombinationsNode, WinCombinations.class);

        Map<String, Integer> symbolCountMap = countSameSymbols(symbol2DArray);

        SymbolWinCombinationTracker winCombinationTracker = new SymbolWinCombinationTracker();
        winCombinationTracker.processWinCombinations(symbolCountMap, winCombinations, symbol2DArray);

        int bet = 100;

        if (winCombinationTracker.hasWon()) {
            double winPrize = PrizeCalculator.calculate(bet, winCombinationTracker.getAppliedWinCombinationBySymbol(), symbols, bonusSymbol);
            System.out.println("WIN PRIZE: " + winPrize);
        }


        System.out.println("asd");
    }


    public static Map<String, Integer> countSameSymbols(String[][] array2D) {
        Map<String, Integer> countSymbolMap = new HashMap<>();
        for (String[] array : array2D) {
            for (String symbol : array) {
                if (countSymbolMap.containsKey(symbol)) {
                    countSymbolMap.put(symbol, countSymbolMap.get(symbol) + 1);
                } else {
                    countSymbolMap.put(symbol, 1);
                }
            }
        }
        return countSymbolMap;
    }
}