package demo.cli.cli;

import org.springframework.shell.core.CommandMarker;
import org.springframework.shell.core.annotation.CliCommand;
import org.springframework.shell.core.annotation.CliOption;
import org.springframework.stereotype.Component;

/**
 * Created by jellin on 9/18/15.
 */
@Component
public class HelloCommand implements CommandMarker {
    @CliCommand(value = "hw simple", help = "Print a simple hello world message")
    public String simple(
            @CliOption(key = { "message" }, mandatory = true, help = "The hello world message")
            final String message,

            @CliOption(key = { "location" }, mandatory = false,
                    help = "Where you are saying hello", unspecifiedDefaultValue="At work")
            final String location) {

        return "Message = [" + message + "] Location = [" + location + "]";

    }

}
