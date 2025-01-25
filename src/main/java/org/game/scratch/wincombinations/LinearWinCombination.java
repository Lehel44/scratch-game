package org.game.scratch.wincombinations;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Data class for win combinations which applies in specified patterns.
 */
public class LinearWinCombination extends WinCombination {

    @JsonProperty("covered_areas")
    private List<List<String>> coveredAreas;

    public List<List<String>> getCoveredAreas() {
        return coveredAreas;
    }

    @Override
    public boolean checkWinCombination(String symbol, String[][] array2D, int symbolCount) {
        return checkLinearSymbolWinCombination(this, symbol, array2D);
    }

    /**
     * For a win combination checks if any of the covered areas on the game matrix matches
     * the given symbol.
     *
     * @param winCombination - the win combination that contains the areas
     * @param symbol         - the symbol that the covered areas may contain
     * @param array2D        - the game matrix
     * @return if there is matching covered area
     */
    private boolean checkLinearSymbolWinCombination(LinearWinCombination winCombination, String symbol, String[][] array2D) {
        List<List<String>> coveredAreasList = winCombination.getCoveredAreas();
        for (List<String> coveredArea : coveredAreasList) {
            if (isAreaCovered(symbol, array2D, coveredArea)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the covered area on the game matrix matches the given symbol.
     *
     * @param symbol      - the current symbol
     * @param array2D     - the game matrix of symbols
     * @param coveredArea - the area that needs to match for the win combination to apply
     * @return true if the covered area of symbol matches the game matrix
     */
    private boolean isAreaCovered(String symbol, String[][] array2D, List<String> coveredArea) {
        for (String coordinateString : coveredArea) {
            String[] coordinates = coordinateString.split(":");
            int row = Integer.parseInt(coordinates[0]);
            int column = Integer.parseInt(coordinates[1]);
            if (!array2D[row][column].equals(symbol)) {
                return false;
            }
        }
        return true;
    }
}
