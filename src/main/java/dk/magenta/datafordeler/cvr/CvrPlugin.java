package dk.magenta.datafordeler.cvr;

import dk.magenta.datafordeler.core.configuration.ConfigurationManager;
import dk.magenta.datafordeler.core.plugin.Plugin;
import dk.magenta.datafordeler.core.plugin.RegisterManager;
import dk.magenta.datafordeler.core.plugin.RolesDefinition;
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

    private CvrRolesDefinition rolesDefinition = new CvrRolesDefinition();

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

    /**
    * Return the domain for the plugin, used in Identification objects under CVR
    **/
    public static String getDomain() {
        return "cvr";
    }

    /**
    * Return the plugin’s register manager
    **/
    @Override
    public RegisterManager getRegisterManager() {
        return this.registerManager;
    }


    /**
    * Return the plugin’s configuration manager
    **/
    @Override
    public ConfigurationManager getConfigurationManager() {
        return this.configurationManager;
    }

    /**
    * Get a defintion of user roles
    **/
    @Override
    public RolesDefinition getRolesDefinition() {
        return this.rolesDefinition;
    }
}
