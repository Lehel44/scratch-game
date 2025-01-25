package org.game.scratch.rewards;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class WinCombinations {

    @JsonProperty("win_combinations")
    @JsonAnySetter
    public Map<String, WinCombination> winCombinationMap;

    public Map<String, WinCombination> getWinCombinationMap() {
        return winCombinationMap;
    }
}
