package org.game.scratch.wincombinations;

/**
 * Data class for win combinations which apply for the number of a symbol.
 */
public class SameSymbolWinCombination extends WinCombination {

    int count;

    public SameSymbolWinCombination() {
        // No arg constructor for Jackson serialization
    }

    public SameSymbolWinCombination(double rewardMultiplier, int count) {
        this.rewardMultiplier = rewardMultiplier;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public boolean checkWinCombination(final String symbol, final String[][] gameMatrix, final int symbolCount) {
        return checkSameSymbolWinCombination(this, symbolCount);
    }

    /**
     * Checks if the win combination applies to a symbol. Compares the 'count' attribute with
     * the count of a symbol.
     *
     * @param winCombination - that specifies the minimum count of the same symbol for winning
     * @param symbolCount    - the count of the specified symbol
     * @return if the win combination applies
     */
    private boolean checkSameSymbolWinCombination(final SameSymbolWinCombination winCombination, final int symbolCount) {
        return winCombination.getCount() <= symbolCount;
    }
}
