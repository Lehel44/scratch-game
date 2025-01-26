package org.game.scratch;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.game.scratch.game.ConfigurationLoader;
import org.game.scratch.game.GameMatrixManager;
import org.game.scratch.game.SymbolProcessor;
import org.game.scratch.game.WinCombinationProcessor;
import org.game.scratch.prizes.PrizeCalculator;
import org.game.scratch.process.SymbolWinCombinationTracker;
import org.game.scratch.symbols.Symbol;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Map;


public class Main {


    private static final String CONFIG = "config";
    private static final String BETTING_AMOUNT = "betting-amount";

    public static void main(String[] args) throws IOException {
        final Options options = new Options();

        options.addOption(CONFIG, true, "Path to the configuration JSON file");
        options.addOption(BETTING_AMOUNT, true, "The betting amount to play with");

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd;

        try {
            cmd = parser.parse(options, args);

            if (!cmd.hasOption(CONFIG) || !cmd.hasOption(BETTING_AMOUNT)) {
                printUsage(options);
                return;
            }

            final String configPath = cmd.getOptionValue(CONFIG);
            final double bettingAmount = Double.parseDouble(cmd.getOptionValue("betting_amount"));

            runGame(configPath, bettingAmount);
        } catch (ParseException e) {
            System.out.println("Error parsing command line arguments: " + e.getMessage());
            printUsage(options);
        }

    }

    private static void printUsage(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar <your-jar-file>", options);
    }

    private static void runGame(final String configPath, final double bettingAmount) throws IOException {
        ConfigurationLoader loader = new ConfigurationLoader(configPath);
        final JsonNode config = loader.loadConfig();

        // Configure ObjectMapper with custom deserializer
        final SymbolProcessor symbolProcessor = new SymbolProcessor(config);

        // Deserialze symbols into a (symbol name, symbol object) map
        final Map<String, Symbol> symbols = symbolProcessor.deserializeSymbols();

        // Initialize game matrix
        final int columns = config.get("columns").asInt();
        final int rows = config.get("rows").asInt();
        final GameMatrixManager manager = new GameMatrixManager(rows, columns, config.get("probabilities"));
        manager.initializeGameMatrix();

        // Generate and place bonus symbol in the game matrix
        final String bonusSymbol = symbolProcessor.getBonusSymbol();
        manager.placeBonusSymbol(bonusSymbol);

        // Print game matrix
        manager.printGameMatrix();

        // Process win combinations
        final WinCombinationProcessor processor = new WinCombinationProcessor(manager.getGameMatrix(), config);
        final SymbolWinCombinationTracker tracker = processor.processWinCombinations();

        if (tracker.hasWon()) {
            double winPrize = PrizeCalculator.calculate(bettingAmount, tracker.getAppliedWinCombinationBySymbol(), symbols, bonusSymbol);
            System.out.println("WIN PRIZE: " + winPrize);
        } else {
            System.out.println("NO WIN!");
        }
    }
}