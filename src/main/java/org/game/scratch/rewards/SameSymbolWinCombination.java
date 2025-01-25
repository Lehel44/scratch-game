package org.game.scratch.rewards;

public class SameSymbolWinCombination extends WinCombination {

    int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean checkWinCombination(String symbol, String[][] array2D, int symbolCount) {
        return checkSameSymbolWinCombination(this, symbolCount);
    }

    private boolean checkSameSymbolWinCombination(SameSymbolWinCombination winCombination, int symbolCount) {
        return winCombination.getCount() <= symbolCount;
    }
}
