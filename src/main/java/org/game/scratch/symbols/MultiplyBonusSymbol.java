package org.game.scratch.symbols;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MultiplyBonusSymbol extends BonusSymbol {

    @JsonProperty("reward_multiplier")
    private double rewardMultiplier;

    @Override
    public double applyEffect(final double bet) {
        return bet * rewardMultiplier;
    }
}
