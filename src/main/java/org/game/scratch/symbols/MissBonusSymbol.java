package org.game.scratch.symbols;

public class MissBonusSymbol extends BonusSymbol {

    public MissBonusSymbol() {
    }

    @Override
    public double applyEffect(double bet) {
        return bet;
    }
}
