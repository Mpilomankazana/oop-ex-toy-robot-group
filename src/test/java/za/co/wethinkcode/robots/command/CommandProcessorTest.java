package za.co.wethinkcode.robots.command;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


//Tests command execution
public class CommandProcessorTest {

    //Launch command should succeed.
    @Test
    void shouldLaunchRobot() {

        CommandProcessor processor = new CommandProcessor();

        String result = processor.execute("launch");

        assertEquals("OK", result);
    }

    //Invalid command should fail.
    @Test
    void shouldRejectInvalidCommand() {

        CommandProcessor processor = new CommandProcessor();

        String result = processor.execute("dance");

        assertEquals("ERROR", result);
    }

    //State command should return robot state.
    @Test
    void shouldReturnRobotState() {

        CommandProcessor processor = new CommandProcessor();

        String result = processor.execute("state");

        assertNotNull(result);
    }

    // Upper case should be accepted.
    @Test
    void shouldAcceptUppercaseCommand() {

        CommandProcessor processor = new CommandProcessor();

        String result = processor.execute("FORWARD");

        assertEquals("OK", result);
    }

    // Empty command should be rejected.
    @Test
    void shouldRejectEmptyCommand() {

        CommandProcessor processor = new CommandProcessor();

        String result = processor.execute("");

        assertEquals("ERROR", result);
    }

    // White space command should be rejected.
    @Test
    void shouldRejectWhitespaceCommand() {

        CommandProcessor processor = new CommandProcessor();

        String result = processor.execute(" ");

        assertEquals("ERROR", result);
    }

    // Reload command should be accepted.
    @Test
    void shouldAcceptReloadCommand() {

        CommandProcessor processor = new CommandProcessor();

        String result = processor.execute("reload");

        assertEquals("OK", result);
    }

    // Repair command should be accepted.
    @Test
    void shouldAcceptRepairCommand() {

        CommandProcessor processor = new CommandProcessor();

        String result = processor.execute("repair");

        assertEquals("OK", result);
    }
}
