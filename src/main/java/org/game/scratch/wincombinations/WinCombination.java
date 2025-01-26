package org.game.scratch.wincombinations;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "when"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LinearWinCombination.class, name = "linear_symbols"),
        @JsonSubTypes.Type(value = SameSymbolWinCombination.class, name = "same_symbols")
})
public abstract class WinCombination {
    @JsonProperty("reward_multiplier")
    double rewardMultiplier;
    String group;

    public double getRewardMultiplier() {
        return rewardMultiplier;
    }

    public String getGroup() {
        return group;
    }

    public abstract boolean checkWinCombination(final String symbol, final String[][] gameMatrix, final int symbolCount);
}
