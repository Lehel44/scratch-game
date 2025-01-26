package org.game.scratch.prizes;

import org.game.scratch.symbols.ExtraBonusSymbol;
import org.game.scratch.symbols.MissBonusSymbol;
import org.game.scratch.symbols.MultiplyBonusSymbol;
import org.game.scratch.symbols.StandardSymbol;
import org.game.scratch.symbols.Symbol;
import org.game.scratch.wincombinations.LinearWinCombination;
import org.game.scratch.wincombinations.SameSymbolWinCombination;
import org.game.scratch.wincombinations.WinCombination;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrizeCalculatorTest {

    @Test
    void testCalculateZeroBet() {
        final Map<String, Map<String, WinCombination>> winCombinations = createWinCombinations();
        final Map<String, Symbol> symbols = createSymbols();
        final double bet = 0.0;
        final double expectedWinPrize = 0;

        final double winPrize = PrizeCalculator.calculate(bet, winCombinations, symbols, "+500");

        assertEquals(expectedWinPrize, winPrize);
    }

    @Test
    void testCalculateLossWithExtraBonus() {
        final Map<String, Map<String, WinCombination>> winCombinations = Collections.emptyMap();
        final Map<String, Symbol> symbols = createSymbols();
        symbols.put("+500", new ExtraBonusSymbol(500));  // Default bonus symbol
        final double bet = 100.0;
        final double expectedWinPrize = 0;

        final double winPrize = PrizeCalculator.calculate(bet, winCombinations, symbols, "+500");

        assertEquals(expectedWinPrize, winPrize);
    }

    @Test
    void testCalculateWinWithExtraBonus() {
        final Map<String, Map<String, WinCombination>> winCombinations = createWinCombinations();
        final Map<String, Symbol> symbols = createSymbols();
        symbols.put("+500", new ExtraBonusSymbol(500));  // Default bonus symbol
        final double bet = 100.0;
        final double expectedWinPrize = ((100 * 2 * 2) + (100 * 3 * 3)) + 500;

        final double winPrize = PrizeCalculator.calculate(bet, winCombinations, symbols, "+500");

        assertEquals(expectedWinPrize, winPrize);
    }

    @Test
    void testCalculateWinWithMultiplyBonus() {
        final Map<String, Map<String, WinCombination>> winCombinations = createWinCombinations();
        final Map<String, Symbol> symbols = createSymbols();
        symbols.put("10x", new MultiplyBonusSymbol(10));
        final double bet = 100.0;
        final double expectedWinPrize = ((100 * 2 * 2) + (100 * 3 * 3)) * 10;

        final double winPrize = PrizeCalculator.calculate(bet, winCombinations, symbols, "10x");

        assertEquals(expectedWinPrize, winPrize);
    }

    @Test
    void testCalculateWinWithMissBonus() {
        final Map<String, Map<String, WinCombination>> winCombinations = createWinCombinations();
        final Map<String, Symbol> symbols = createSymbols();
        symbols.put("MISS", new MissBonusSymbol());
        final double bet = 100.0;
        final double expectedWinPrize = ((100 * 2 * 2) + (100 * 3 * 3));

        final double winPrize = PrizeCalculator.calculate(bet, winCombinations, symbols, "MISS");

        assertEquals(expectedWinPrize, winPrize);
    }

    @Test
    void testCalculateWinWithMultipleWinCOmbinations() {
        final Map<String, Map<String, WinCombination>> winCombinations = createMultiWinCombinations();
        final Map<String, Symbol> symbols = createSymbols();
        symbols.put("C", new StandardSymbol(4));
        symbols.put("MISS", new MissBonusSymbol());
        final double bet = 100.0;
        final double expectedWinPrize = ((100 * 2 * 3 * 0.4) + (100 * 3 * 5 * 2) + (100 * 4 * 1.2 * 3.5));

        final double winPrize = PrizeCalculator.calculate(bet, winCombinations, symbols, "MISS");

        assertEquals(expectedWinPrize, winPrize);
    }

    private Map<String, Map<String, WinCombination>> createWinCombinations() {
        return Map.of(
                "A", Map.of("combinationA", new SameSymbolWinCombination(2, 0)),
                "B", Map.of("combinationB", new SameSymbolWinCombination(3, 0))
        );
    }

    private Map<String, Map<String, WinCombination>> createMultiWinCombinations() {
        return Map.of(
                "A", Map.of("combinationA1", new SameSymbolWinCombination(3, 0),
                        "combinationA2", new LinearWinCombination(null, 0.4)),
                "B", Map.of("combinationB1", new SameSymbolWinCombination(5, 0),
                        "combinationB2", new SameSymbolWinCombination(2, 0)),
                "C", Map.of("combinationC1", new LinearWinCombination(null, 1.2),
                        "combinationC2", new LinearWinCombination(null, 3.5))
        );
    }

    private Map<String, Symbol> createSymbols() {
        final Map<String, Symbol> symbols = new HashMap<>();
        symbols.put("A", new StandardSymbol(2));
        symbols.put("B", new StandardSymbol(3));
        return symbols;
    }
}

