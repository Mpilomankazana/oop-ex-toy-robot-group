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
}
