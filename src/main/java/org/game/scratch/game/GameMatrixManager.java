package org.game.scratch.game;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.game.scratch.probability.WeightedProbabilitySymbolGenerator;

import java.util.Map;
import java.util.SplittableRandom;

public class GameMatrixManager {

    private final int rows;
    private final int columns;
    private final JsonNode probabilities;
    private final String[][] gameMatrix;
    private final ObjectMapper objectMapper;

    public GameMatrixManager(final int rows, final int columns, final JsonNode probabilities) {
        this.rows = rows;
        this.columns = columns;
        this.probabilities = probabilities;
        gameMatrix = new String[rows][columns];
        this.objectMapper = ObjectMapperProvider.getInstance();
    }

    public void initializeGameMatrix() {
        final JsonNode probabilityArray = probabilities.get("standard_symbols");

        for (JsonNode node : probabilityArray) {
            final int column = node.get("column").asInt();
            final int row = node.get("row").asInt();
            Map<String, Double> map = objectMapper.convertValue(node.get("symbols"), new TypeReference<>() {
            });
            final String chosenSymbol = WeightedProbabilitySymbolGenerator.generate(map);
            gameMatrix[row][column] = chosenSymbol;
        }
    }

    public void placeBonusSymbol(final String bonusSymbol) {
        final SplittableRandom random = new SplittableRandom();
        int bonusColumn = random.nextInt(0, columns - 1);
        int bonusRow = random.nextInt(0, rows - 1);

        gameMatrix[bonusRow][bonusColumn] = bonusSymbol;
    }

    public void printGameMatrix() {
        for (int i = 0; i < gameMatrix.length; i++) {
            for (int j = 0; j < gameMatrix[i].length; j++) {
                System.out.print(gameMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public String[][] getGameMatrix() {
        return gameMatrix;
    }
}
