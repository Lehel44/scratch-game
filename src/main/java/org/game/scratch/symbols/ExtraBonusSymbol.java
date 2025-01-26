package org.game.scratch.symbols;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExtraBonusSymbol extends BonusSymbol {

    @JsonProperty("extra")
    private double extra;

    public ExtraBonusSymbol() {
        // No arg constructor for Jackson serialization
    }

    public ExtraBonusSymbol(double extra) {
        this.extra = extra;
    }

    @Override
    public double applyEffect(final double bet) {
        return bet + extra;
    }
}
