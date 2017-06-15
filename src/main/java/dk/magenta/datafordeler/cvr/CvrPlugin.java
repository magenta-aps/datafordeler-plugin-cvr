package dk.magenta.datafordeler.cvr;

import dk.magenta.datafordeler.core.configuration.ConfigurationManager;
import dk.magenta.datafordeler.core.plugin.Plugin;
import dk.magenta.datafordeler.core.plugin.RegisterManager;
import dk.magenta.datafordeler.cvr.configuration.CvrConfigurationManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntityManager;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntityManager;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by lars on 16-05-17.
 */
@Component
public class CvrPlugin extends Plugin {

    @Autowired
    private CvrConfigurationManager configurationManager;

    @Autowired
    private CvrRegisterManager registerManager;

    @Autowired
    private CompanyEntityManager companyEntityManager;

    @Autowired
    private CompanyUnitEntityManager companyUnitEntityManager;


    @Autowired
    private ParticipantEntityManager participantEntityManager;


    @PostConstruct
    public void init() {
        this.registerManager.addEntityManager(this.companyEntityManager);
        this.registerManager.addEntityManager(this.companyUnitEntityManager);
        this.registerManager.addEntityManager(this.participantEntityManager);
    }

    @Override
    public String getName() {
        return "cvr";
    }

    @Override
    public RegisterManager getRegisterManager() {
        return this.registerManager;
    }

    @Override
    public ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

}
