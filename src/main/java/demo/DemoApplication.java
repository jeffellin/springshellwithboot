package demo;

import demo.cli.cli.BootShim;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.shell.support.logging.HandlerUtils;

import java.util.logging.Logger;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = SpringApplication.run(DemoApplication.class, args);

        try {
            BootShim bs = new BootShim(args,ctx);
            bs.run();
        } catch (RuntimeException e) {
            throw e;
        } finally {
            HandlerUtils.flushAllHandlers(Logger.getLogger(""));
        }
        }
}
