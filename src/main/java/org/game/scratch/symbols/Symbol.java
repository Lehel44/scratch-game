package org.game.scratch.symbols;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Symbol {

    public abstract double applyEffect(final double bet);
}
