package org.game.scratch.rewards;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;

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

    public void setRewardMultiplier(double rewardMultiplier) {
        this.rewardMultiplier = rewardMultiplier;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public abstract boolean checkWinCombination(String symbol, String[][] array2D, int symbolCount);
}
