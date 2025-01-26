package org.game.scratch.output;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.game.scratch.game.ObjectMapperProvider;
import org.game.scratch.wincombinations.WinCombination;

import java.util.Map;

public class OutputPrinter {

    private final String[][] gameMatrix;
    private final double gamePrize;
    private final Map<String, Map<String, WinCombination>> appliedWinCombinations;
    private final String bonusSymbol;

    public OutputPrinter(final String[][] gameMatrix, final double gamePrize, final Map<String, Map<String, WinCombination>> appliedWinCombinations,
                         final String bonusSymbol) {
        this.gameMatrix = gameMatrix;
        this.gamePrize = gamePrize;
        this.appliedWinCombinations = appliedWinCombinations;
        this.bonusSymbol = bonusSymbol;
    }

    public void printResult() {
        try {
            ObjectMapper objectMapper = ObjectMapperProvider.getInstance();

            ObjectNode root = objectMapper.createObjectNode();

            ArrayNode matrixNode = root.putArray("matrix");
            for (String[] row : gameMatrix) {
                ArrayNode rowNode = matrixNode.addArray();
                for (String symbol : row) {
                    rowNode.add(symbol);
                }
            }

            root.put("reward", gamePrize);

            ObjectNode appliedCombinationsNode = objectMapper.createObjectNode();
            for (Map.Entry<String, Map<String, WinCombination>> entry : appliedWinCombinations.entrySet()) {
                ArrayNode combinationsNode = appliedCombinationsNode.putArray(entry.getKey());
                for (String group : entry.getValue().keySet()) {
                    combinationsNode.add(group);
                }
            }
            if (!appliedCombinationsNode.isEmpty()) {
                root.set("applied_winning_combinations", appliedCombinationsNode);
                root.put("applied_bonus_symbol", bonusSymbol);
            }


            DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
            prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
            String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
            System.out.println(jsonString);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
