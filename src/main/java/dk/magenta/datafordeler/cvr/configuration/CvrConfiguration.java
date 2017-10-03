package dk.magenta.datafordeler.cvr.configuration;

import dk.magenta.datafordeler.core.configuration.Configuration;
import dk.magenta.datafordeler.cvr.CvrPlugin;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_config")
public class CvrConfiguration implements Configuration {

    @Id
    @Column(name = "id")
    private final String plugin = CvrPlugin.class.getName();

    // Midnight every january 1st
    @Column
    private String pullCronSchedule = "0 0 0 1 1 ?";

    @Column
    private String registerAddress = "http://distribution.virk.dk";

    @Column
    private String username = "Magenta_CVR_I_SKYEN";

    @Column
    private String password = "20ce0f61-3f04-43ec-8119-3a67384e269c";

    public String getPullCronSchedule() {
        return this.pullCronSchedule;
    }

    public String getRegisterAddress() {
        return this.registerAddress;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
