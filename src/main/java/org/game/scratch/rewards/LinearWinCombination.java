package org.game.scratch.rewards;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

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

    private boolean checkLinearSymbolWinCombination(LinearWinCombination winCombination, String symbol, String[][] array2D) {
        List<List<String>> coveredAreasList = winCombination.getCoveredAreas();
        for (List<String> coveredArea : coveredAreasList) {
            if (isAreaCovered(symbol, array2D, coveredArea)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAreaCovered(String symbol, String[][] array2D, List<String> coveredArea) {
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
