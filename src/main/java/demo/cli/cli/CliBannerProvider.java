package demo.cli.cli;

import org.springframework.shell.plugin.BannerProvider;
import org.springframework.stereotype.Component;

/**
 * Created by jellin on 9/18/15.
 */
@Component
public class CliBannerProvider implements BannerProvider {
    @Override
    public String getBanner() {
        return null;
    }

    @Override
    public String getVersion() {
        return "";
    }

    @Override
    public String getWelcomeMessage() {
        return null;
    }

    @Override
    public String getProviderName() {
        return null;
    }
}
