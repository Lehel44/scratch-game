package org.game.scratch.probability;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SplittableRandom;

public final class WeightedProbabilitySymbolGenerator {

    private WeightedProbabilitySymbolGenerator() {
        // Utility class
    }

    private static final SplittableRandom RANDOM = new SplittableRandom();

    /**
     * Generates a symbol based on the weighted probability distribution.
     *
     * @param probabilityDistribution a map of symbols and probabilities
     * @return the chosen symbol
     */
    public static String generate(final Map<String, Double> probabilityDistribution) {
        validateProbabilityDistribution(probabilityDistribution);

        List<String> symbols = new ArrayList<>();
        List<Double> cumulativeProbabilities = new ArrayList<>();
        double cumulative = 0;

        for (Map.Entry<String, Double> entry : probabilityDistribution.entrySet()) {
            symbols.add(entry.getKey());
            cumulative += entry.getValue();
            cumulativeProbabilities.add(cumulative);
        }

        double totalProbabilitySum = cumulative;
        double rand = RANDOM.nextDouble(0, totalProbabilitySum);

        for (int i = 0; i < cumulativeProbabilities.size(); i++) {
            if (rand < cumulativeProbabilities.get(i)) {
                return symbols.get(i);
            }
        }

        throw new IllegalArgumentException("Invalid probability distribution");
    }

    /**
     * Validates the input probability distribution by checking if all values are non-negative
     * and the map is not empty.
     *
     * @param probabilityDistribution the input distribution
     */
    private static void validateProbabilityDistribution(Map<String, Double> probabilityDistribution) {
        if (probabilityDistribution == null || probabilityDistribution.isEmpty()) {
            throw new IllegalArgumentException("Probability distribution cannot be null or empty.");
        }

        if (probabilityDistribution.values().stream().anyMatch(v -> v < 0)) {
            throw new IllegalArgumentException("Probability values must be non-negative.");
        }
    }
}
