package org.game.scratch.process;

import org.game.scratch.wincombinations.SameSymbolWinCombination;
import org.game.scratch.wincombinations.WinCombination;
import org.game.scratch.wincombinations.WinCombinations;

import java.util.HashMap;
import java.util.Map;

/**
 * This class checks and processes win combinations for the matrix of symbols of the current game.
 * It manages the tracking and application of winning combinations.
 */
public class SymbolWinCombinationTracker {

    final Map<String, Map<String, WinCombination>> appliedWincombinationBySymbol;

    public SymbolWinCombinationTracker() {
        appliedWincombinationBySymbol = new HashMap<>();
    }

    /**
     * Checks which win combinations applies for the given matrix and applies them.
     *
     * @param symbolCountMap  - map of (symbol names, symbol counts)
     * @param winCombinations - holds a (group, winCombination) map
     * @param gameMatrix   - symbol array of the current game
     */
    public void processWinCombinations(final Map<String, Integer> symbolCountMap, final WinCombinations winCombinations, final String[][] gameMatrix) {
        for (Map.Entry<String, Integer> symbolEntry : symbolCountMap.entrySet()) {
            final String symbolName = symbolEntry.getKey();
            final int symbolCount = symbolEntry.getValue();

            for (WinCombination winCombination : winCombinations.getWinCombinationMap().values()) {
                if (winCombination.checkWinCombination(symbolName, gameMatrix, symbolCount)) {
                    addWinCombination(symbolName, winCombination);
                }
            }
        }
    }

    /**
     * Adds and resolves win combination for a symbol.
     *
     * @param symbolName     - the name of the symbol
     * @param winCombination - the win combination object
     */
    private void addWinCombination(final String symbolName, final WinCombination winCombination) {
        final String group = winCombination.getGroup();

        final Map<String, WinCombination> groupWinCombinationMap = appliedWincombinationBySymbol
                .computeIfAbsent(symbolName, k -> new HashMap<>());

        groupWinCombinationMap.merge(group, winCombination, this::resolveBetterCombination);
    }

    /**
     * Resolves which win combination to apply if there is more than one that applies from a group.
     * In case of SameSymbolWinCombination the one that has a greater 'count' property applies,
     * otherwise (in case of LinearSymbolWinCombination) the first one applies.
     *
     * @param existing - the win combination that has been applied
     * @param current  - the current win combination
     * @return the better win combination
     */
    private WinCombination resolveBetterCombination(final WinCombination existing, final WinCombination current) {
        if (existing instanceof SameSymbolWinCombination existingSameSymbolWinCombination &&
                current instanceof SameSymbolWinCombination currentSameSymbolWinCombination) {
            return currentSameSymbolWinCombination.getCount() > existingSameSymbolWinCombination.getCount() ? current : existing;
        }
        return existing;
    }

    public Map<String, Map<String, WinCombination>> getAppliedWinCombinationBySymbol() {
        return appliedWincombinationBySymbol;
    }

    /**
     * Determines if any winning combination has been applied.
     *
     * @return true if at least one win combination is applied, false otherwise.
     */
    public boolean hasWon() {
        return !appliedWincombinationBySymbol.isEmpty();
    }
}
