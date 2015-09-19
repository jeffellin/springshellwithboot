//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package demo.cli.cli;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.shell.CommandLine;
import org.springframework.shell.ShellException;
import org.springframework.shell.SimpleShellCommandLineOptions;
import org.springframework.shell.core.ExitShellRequest;
import org.springframework.shell.core.JLineShellComponent;
import org.springframework.shell.support.logging.HandlerUtils;
import org.springframework.util.StopWatch;

public class BootShim {
    //private static final String[] CONTEXT_PATH = new String[]{"classpath*:/META-INF/spring/spring-shell-plugin.xml"};
    private static BootShim bootstrap;
    private static StopWatch sw = new StopWatch("Spring Shell");
    private static CommandLine commandLine;
    private   ConfigurableApplicationContext ctx;



    public BootShim(String[] args, ConfigurableApplicationContext context) {
        this.ctx = context;

        try {
            commandLine = SimpleShellCommandLineOptions.parseCommandLine(args);
        } catch (IOException var5) {
            throw new ShellException(var5.getMessage(), var5);
        }

        //this.ctx = new GenericApplicationContext();
        //this.ctx.registerShutdownHook();
        this.configureApplicationContext(this.ctx);
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner((BeanDefinitionRegistry) this.ctx);
        if(commandLine.getDisableInternalCommands()) {
            scanner.scan(new String[]{"org.springframework.shell.converters", "org.springframework.shell.plugin.support"});
        } else {
            scanner.scan(new String[]{"org.springframework.shell.commands", "org.springframework.shell.converters", "org.springframework.shell.plugin.support"});
        }

        //XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this.ctx);
        //reader.loadBeanDefinitions(contextPath);
        //this.ctx.refresh();
    }

    public ApplicationContext getApplicationContext() {
        return this.ctx;
    }

    private void configureApplicationContext(ConfigurableApplicationContext annctx) {
        this.createAndRegisterBeanDefinition(annctx, JLineShellComponent.class, "shell");
        annctx.getBeanFactory().registerSingleton("commandLine", commandLine);
    }

    protected void createAndRegisterBeanDefinition(GenericApplicationContext annctx, Class<?> clazz) {
        this.createAndRegisterBeanDefinition(annctx, clazz, (String)null);
    }

    protected void createAndRegisterBeanDefinition(ConfigurableApplicationContext annctx, Class<?> clazz, String name) {
        RootBeanDefinition rbd = new RootBeanDefinition();
        rbd.setBeanClass(clazz);
        DefaultListableBeanFactory bf = (DefaultListableBeanFactory)annctx.getBeanFactory();
        if(name != null) {
            bf.registerBeanDefinition(name, rbd);
        } else {
            bf.registerBeanDefinition(clazz.getSimpleName(), rbd);
        }

    }

    private void setupLogging() {
        Logger rootLogger = Logger.getLogger("");
        HandlerUtils.wrapWithDeferredLogHandler(rootLogger, Level.SEVERE);
        Logger sfwLogger = Logger.getLogger("org.springframework");
        sfwLogger.setLevel(Level.WARNING);
        Logger rooLogger = Logger.getLogger("org.springframework.shell");
        rooLogger.setLevel(Level.FINE);
    }

    public ExitShellRequest run() {
        sw.start();
        String[] commandsToExecuteAndThenQuit = commandLine.getShellCommandsToExecute();
        JLineShellComponent shell = (JLineShellComponent)this.ctx.getBean("shell", JLineShellComponent.class);
        ExitShellRequest exitShellRequest;
        if(null != commandsToExecuteAndThenQuit) {
            boolean successful = false;
            exitShellRequest = ExitShellRequest.FATAL_EXIT;
            String[] arr$ = commandsToExecuteAndThenQuit;
            int len$ = commandsToExecuteAndThenQuit.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String cmd = arr$[i$];
                successful = shell.executeCommand(cmd).isSuccess();
                if(!successful) {
                    break;
                }
            }

            if(successful) {
                exitShellRequest = ExitShellRequest.NORMAL_EXIT;
            }
        } else {
            shell.start();
            shell.promptLoop();
            exitShellRequest = shell.getExitShellRequest();
            if(exitShellRequest == null) {
                exitShellRequest = ExitShellRequest.NORMAL_EXIT;
            }

            shell.waitForComplete();
        }

        sw.stop();
        if(shell.isDevelopmentMode()) {
            System.out.println("Total execution time: " + sw.getLastTaskTimeMillis() + " ms");
        }

        return exitShellRequest;
    }

    public JLineShellComponent getJLineShellComponent() {
        return (JLineShellComponent)this.ctx.getBean("shell", JLineShellComponent.class);
    }
}
