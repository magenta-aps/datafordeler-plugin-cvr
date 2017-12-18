package dk.magenta.datafordeler.cvr.configuration;

import dk.magenta.datafordeler.core.configuration.ConfigurationManager;
import dk.magenta.datafordeler.core.database.ConfigurationSessionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CvrConfigurationManager extends ConfigurationManager<CvrConfiguration> {

    @Autowired
    private ConfigurationSessionManager sessionManager;

    private Logger log = LogManager.getLogger("CvrConfigurationManager");

    @PostConstruct
    public void init() {
        // Very important to call init() on ConfigurationManager, or the config will not be loaded
        super.init();
    }

    @Override
    protected Class<CvrConfiguration> getConfigurationClass() {
        return CvrConfiguration.class;
    }

    @Override
    protected CvrConfiguration createConfiguration() {
        return new CvrConfiguration();
    }

    @Override
    protected ConfigurationSessionManager getSessionManager() {
        return this.sessionManager;
    }

    @Override
    protected Logger getLog() {
        return this.log;
    }
}
