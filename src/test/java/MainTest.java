import org.game.scratch.Main;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MainTest {

    @Test
    void testCommandLineParsing() {
        String[] args = {"-c", "src/test/resources/config.json", "-b", "100.0"};
        assertDoesNotThrow(() -> {
            Main.main(args);
        });
    }

}
