package org.game.scratch.symbols;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StandardSymbol extends Symbol {

    @JsonProperty("reward_multiplier")
    double rewardMultiplier;

    public StandardSymbol() {
        // No arg constructor for Jackson serialization
    }

    public StandardSymbol(double rewardMultiplier) {
        this.rewardMultiplier = rewardMultiplier;
    }

    @Override
    public double applyEffect(final double bet) {
        return bet * rewardMultiplier;
    }

    public double getRewardMultiplier() {
        return rewardMultiplier;
    }
}
