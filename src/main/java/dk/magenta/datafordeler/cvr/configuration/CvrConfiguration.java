package dk.magenta.datafordeler.cvr.configuration;

import dk.magenta.datafordeler.core.configuration.Configuration;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntity;

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
    private String username = "";

    @Column
    private String password = "";

    @Column
    private String companyInitialQuery = "{" +
            "    \"query\": {" +
            "        \"match_all\": {}" +
            "    }," +
            "}";

    @Column
    private String companyUpdateQuery = "{" +
            "    \"query\": {" +
            "        \"filtered\": {" +
            "            \"filter\": {" +
            "                \"range\": {" +
            "                    \"Vrvirksomhed.sidstIndlaest\": {" +
            "                        \"gte\": \"%s\"," +
            "                    }" +
            "                }" +
            "            }" +
            "        }" +
            "    }" +
            "}";

    @Column
    private String companyUnitInitialQuery = "{" +
            "    \"query\": {" +
            "        \"match_all\": {}" +
            "    }," +
            "}";

    @Column
    private String companyUnitUpdateQuery = "{" +
            "    \"query\": {" +
            "        \"filtered\": {" +
            "            \"filter\": {" +
            "                \"range\": {" +
            "                    \"VrproduktionsEnhed.sidstIndlaest\": {" +
            "                        \"gte\": \"%s\"," +
            "                    }" +
            "                }" +
            "            }" +
            "        }" +
            "    }" +
            "}";

    @Column
    private String participantInitialQuery = "{" +
            "    \"query\": {" +
            "        \"match_all\": {}" +
            "    }," +
            "}";

    @Column
    private String participantUpdateQuery = "{" +
            "    \"query\": {" +
            "        \"filtered\": {" +
            "            \"filter\": {" +
            "                \"range\": {" +
            "                    \"Vrdeltagerperson.sidstIndlaest\": {" +
            "                        \"gte\": \"%s\"," +
            "                    }" +
            "                }" +
            "            }" +
            "        }" +
            "    }" +
            "}";

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

    public String getCompanyInitialQuery() {
        return this.companyInitialQuery;
    }

    public String getCompanyUpdateQuery() {
        return this.companyUpdateQuery;
    }

    public String getCompanyUnitInitialQuery() {
        return this.companyUnitInitialQuery;
    }

    public String getCompanyUnitUpdateQuery() {
        return this.companyUnitUpdateQuery;
    }

    public String getParticipantInitialQuery() {
        return this.participantInitialQuery;
    }

    public String getParticipantUpdateQuery() {
        return this.participantUpdateQuery;
    }

    public String getInitialQuery(String schema) {
        switch (schema) {
            case CompanyEntity.schema:
                return this.getCompanyInitialQuery();
            case CompanyUnitEntity.schema:
                return this.getCompanyUnitInitialQuery();
            case ParticipantEntity.schema:
                return this.getParticipantInitialQuery();
        }
        return null;
    }

    public String getUpdateQuery(String schema) {
        switch (schema) {
            case CompanyEntity.schema:
                return this.getCompanyUpdateQuery();
            case CompanyUnitEntity.schema:
                return this.getCompanyUnitUpdateQuery();
            case ParticipantEntity.schema:
                return this.getParticipantUpdateQuery();
        }
        return null;
    }
}
