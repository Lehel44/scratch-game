package org.game.scratch;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.game.scratch.process.SymbolWinCombinationTracker;
import org.game.scratch.rewards.LinearWinCombination;
import org.game.scratch.rewards.SameSymbolWinCombination;
import org.game.scratch.rewards.WinCombination;
import org.game.scratch.rewards.WinCombinations;
import org.game.scratch.symbols.BonusSymbol;
import org.game.scratch.symbols.StandardSymbol;
import org.game.scratch.symbols.Symbol;
import org.game.scratch.symbols.SymbolDeserializer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SplittableRandom;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
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
            String chosenSymbol = weightedProbabilitySymbolGenerator(map);
            symbol2DArray[row][column] = chosenSymbol;
        }

        SplittableRandom random = new SplittableRandom();
        int bonusColumn = random.nextInt(0, columns - 1);
        int bonusRow = random.nextInt(0, rows - 1);

        JsonNode jsonNodeBonusSymbols = jsonNode.get("probabilities").get("bonus_symbols").get("symbols");
        Map<String, Double> bonusSymbolMap = mapper.convertValue(jsonNodeBonusSymbols, new TypeReference<>() {
        });

        String bonusSymbol = weightedProbabilitySymbolGenerator(bonusSymbolMap);

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
            double winPrize = calculateWinPrize(bet, winCombinationTracker.getAppliedWinCombinationBySymbol(), symbols, bonusSymbol);
            System.out.println("WIN PRIZE: " + winPrize);
        }


        System.out.println("asd");
    }

    private static double calculateWinPrize(int bet, Map<String, Map<String, WinCombination>> appliedWincombinationBySymbol, Map<String, Symbol> symbols,
                                            String bonusSymbolName) {
        double totalSum = 0;
        double sum;
        for (Map.Entry<String, Map<String, WinCombination>> winCombinationSymbolEntry : appliedWincombinationBySymbol.entrySet()) {
            String symbolName = winCombinationSymbolEntry.getKey();
            Map<String, WinCombination> winCombinationMap = winCombinationSymbolEntry.getValue();
            sum = bet;
            for (Map.Entry<String, WinCombination> groupWinCombinationEntry : winCombinationMap.entrySet()) {
                WinCombination winCombination = groupWinCombinationEntry.getValue();
                double rewardMultiplier = winCombination.getRewardMultiplier();
                double symbolMultiplier = ((StandardSymbol) symbols.get(symbolName)).getRewardMultiplier();
                sum *= rewardMultiplier;
                sum *= symbolMultiplier;
            }
            totalSum += sum;
        }

        Symbol bonusSymbol = symbols.get(bonusSymbolName);
        totalSum = bonusSymbol.applyEffect(totalSum);

        return totalSum;
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

    public static String weightedProbabilitySymbolGenerator(Map<String, Double> probabilityDistribution) {
        double sum = probabilityDistribution.values().stream().mapToDouble(v -> v).sum();
        LinkedHashMap<String, Double> linkedProbDist = new LinkedHashMap<>(probabilityDistribution);

        List<Double> cumulativeProbabilities = new ArrayList<>();
        double cumulative = 0;
        for (Map.Entry<String, Double> entry : probabilityDistribution.entrySet()) {
            cumulative += entry.getValue();
            cumulativeProbabilities.add(cumulative);
        }

        SplittableRandom random = new SplittableRandom();
        double rand = random.nextDouble(0, sum);

        for (int i = 0; i < cumulativeProbabilities.size(); i++) {
            if (rand < cumulativeProbabilities.get(i)) {
                return linkedProbDist.keySet().toArray(new String[0])[i];
            }
        }
        throw new IllegalArgumentException("Invalid probability distribution");
    }
}