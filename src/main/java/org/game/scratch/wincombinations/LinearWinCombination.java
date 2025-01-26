package org.game.scratch.wincombinations;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Data class for win combinations which applies in specified patterns.
 */
public class LinearWinCombination extends WinCombination {

    @JsonProperty("covered_areas")
    private List<List<String>> coveredAreas;

    public LinearWinCombination() {
        // No arg constructor for Jackson serialization
    }

    public LinearWinCombination(List<List<String>> coveredAreas, double rewardMultiplier) {
        this.rewardMultiplier = rewardMultiplier;
        this.coveredAreas = coveredAreas;
    }

    public List<List<String>> getCoveredAreas() {
        return coveredAreas;
    }

    @Override
    public boolean checkWinCombination(final String symbol, final String[][] gameMatrix, final int symbolCount) {
        return checkLinearSymbolWinCombination(this, symbol, gameMatrix);
    }

    /**
     * For a win combination checks if any of the covered areas on the game matrix matches
     * the given symbol.
     *
     * @param winCombination - the win combination that contains the areas
     * @param symbol         - the symbol that the covered areas may contain
     * @param gameMatrix     - the game matrix
     * @return if there is matching covered area
     */
    private boolean checkLinearSymbolWinCombination(final LinearWinCombination winCombination, final String symbol, final String[][] gameMatrix) {
        List<List<String>> coveredAreasList = winCombination.getCoveredAreas();
        for (List<String> coveredArea : coveredAreasList) {
            if (isAreaCovered(symbol, gameMatrix, coveredArea)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the covered area on the game matrix matches the given symbol.
     *
     * @param symbol      - the current symbol
     * @param gameMatrix  - the game matrix of symbols
     * @param coveredArea - the area that needs to match for the win combination to apply
     * @return true if the covered area of symbol matches the game matrix
     */
    private boolean isAreaCovered(final String symbol, final String[][] gameMatrix, final List<String> coveredArea) {
        for (String coordinateString : coveredArea) {
            String[] coordinates = coordinateString.split(":");
            int row = Integer.parseInt(coordinates[0]);
            int column = Integer.parseInt(coordinates[1]);
            if (!gameMatrix[row][column].equals(symbol)) {
                return false;
            }
        }
        return true;
    }
}
