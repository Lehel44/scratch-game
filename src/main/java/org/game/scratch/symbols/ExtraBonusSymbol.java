package org.game.scratch.symbols;

public class ExtraBonusSymbol extends BonusSymbol {

    private double extra;

    public ExtraBonusSymbol() {
    }

    @Override
    public double applyEffect(double bet) {
        return bet + extra;
    }

    public double getExtra() {
        return extra;
    }

    public void setExtra(double extra) {
        this.extra = extra;
    }
}
