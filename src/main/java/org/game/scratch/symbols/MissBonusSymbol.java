package org.game.scratch.symbols;

public class MissBonusSymbol extends BonusSymbol {

    @Override
    public double applyEffect(final double bet) {
        return bet;
    }
}
