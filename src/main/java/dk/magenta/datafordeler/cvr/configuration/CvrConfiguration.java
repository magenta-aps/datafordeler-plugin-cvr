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

    @Column
    private String pullCronSchedule = "0 0 * * * ?";

    @Column
    private String registerAddress = "http://localhost:8000";

    public String getPullCronSchedule() {
        return this.pullCronSchedule;
    }

    public String getRegisterAddress() {
        return this.registerAddress;
    }
}
