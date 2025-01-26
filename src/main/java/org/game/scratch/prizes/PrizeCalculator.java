package org.game.scratch.prizes;

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


    public static double calculate(double bet, Map<String, Map<String, WinCombination>> appliedWincombinationBySymbol, Map<String, Symbol> symbols,
                                   String bonusSymbolName) {
        // Bet must be positive
        if (bet <= 0) {
            return 0;
        }

        // In case of loss
        if (appliedWincombinationBySymbol.isEmpty()) {
            return 0;
        }

        double totalSum = calculateBaseWinPrize(bet, appliedWincombinationBySymbol, symbols);
        totalSum = applyBonusPrize(totalSum, symbols, bonusSymbolName);

        return totalSum;
    }

    private static double calculateBaseWinPrize(double bet, Map<String, Map<String, WinCombination>> appliedWincombinationBySymbol, Map<String, Symbol> symbols) {
        double sum = 0;
        for (Map.Entry<String, Map<String, WinCombination>> winCombinationSymbolEntry : appliedWincombinationBySymbol.entrySet()) {
            String symbolName = winCombinationSymbolEntry.getKey();
            Map<String, WinCombination> winCombinationMap = winCombinationSymbolEntry.getValue();
            sum += calculateGroupPrize(symbols, winCombinationMap, symbolName, bet);
        }
        return sum;
    }

    private static double calculateGroupPrize(Map<String, Symbol> symbols, Map<String, WinCombination> winCombinationMap, String symbolName, double bet) {
        double sum = bet;
        double totalMultiplier = 1.0;
        for (Map.Entry<String, WinCombination> groupWinCombinationEntry : winCombinationMap.entrySet()) {
            WinCombination winCombination = groupWinCombinationEntry.getValue();
            totalMultiplier *= winCombination.getRewardMultiplier();
        }

        sum = totalMultiplier * symbols.get(symbolName).applyEffect(sum);

        return sum;
    }

    private static double applyBonusPrize(double prize, Map<String, Symbol> symbols, String bonusSymbolName) {
        Symbol bonusSymbol = symbols.get(bonusSymbolName);
        return bonusSymbol.applyEffect(prize);
    }
}
