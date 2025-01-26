package org.game.scratch.symbols;

public class ExtraBonusSymbol extends BonusSymbol {

    private double extra;

    @Override
    public double applyEffect(final double bet) {
        return bet + extra;
    }
}
