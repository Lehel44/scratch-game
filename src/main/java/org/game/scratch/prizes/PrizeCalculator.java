package org.game.scratch.prizes;

import org.game.scratch.symbols.StandardSymbol;
import org.game.scratch.symbols.Symbol;
import org.game.scratch.wincombinations.WinCombination;

import java.util.Map;

/**
 * Utility class to calculate win prizes.
 */
public final class PrizeCalculator {

    private PrizeCalculator() {
        // Utility class
    }


    public static double calculate(int bet, Map<String, Map<String, WinCombination>> appliedWincombinationBySymbol, Map<String, Symbol> symbols,
                                    String bonusSymbolName) {
        double totalSum = calculateBaseWinPrize(bet, appliedWincombinationBySymbol, symbols);
        totalSum += applyBonusPrize(totalSum, symbols, bonusSymbolName);

        return totalSum;
    }

    private static double calculateBaseWinPrize(double bet, Map<String, Map<String, WinCombination>> appliedWincombinationBySymbol, Map<String, Symbol> symbols) {
        double sum = bet;
        for (Map.Entry<String, Map<String, WinCombination>> winCombinationSymbolEntry : appliedWincombinationBySymbol.entrySet()) {
            String symbolName = winCombinationSymbolEntry.getKey();
            Map<String, WinCombination> winCombinationMap = winCombinationSymbolEntry.getValue();
            sum += calculateGroupPrize(symbols, winCombinationMap, symbolName, sum);
        }
        return sum;
    }

    private static double calculateGroupPrize(Map<String, Symbol> symbols, Map<String, WinCombination> winCombinationMap, String symbolName, double bet) {
        double sum = bet;
        for (Map.Entry<String, WinCombination> groupWinCombinationEntry : winCombinationMap.entrySet()) {
            WinCombination winCombination = groupWinCombinationEntry.getValue();
            double rewardMultiplier = winCombination.getRewardMultiplier();
            double symbolMultiplier = ((StandardSymbol) symbols.get(symbolName)).getRewardMultiplier();
            sum *= rewardMultiplier;
            sum *= symbolMultiplier;
        }
        return sum;
    }

    private static double applyBonusPrize(double prize, Map<String, Symbol> symbols, String bonusSymbolName) {
        Symbol bonusSymbol = symbols.get(bonusSymbolName);
        return bonusSymbol.applyEffect(prize);
    }
}
