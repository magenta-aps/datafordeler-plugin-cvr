package dk.magenta.datafordeler.cvr.configuration;

import dk.magenta.datafordeler.core.configuration.Configuration;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantEntity;

import javax.persistence.*;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_config")
public class CvrConfiguration implements Configuration {

    public enum RegisterType {
        DISABLED(-1),
        //LOCAL_FILE(0),
        REMOTE_HTTP(1);

        private int value;
        RegisterType(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }


    @Id
    @Column(name = "id")
    private final String plugin = CvrPlugin.class.getName();

    // Midnight every january 1st
    @Column
    private String pullCronSchedule = "0 0 0 1 1 ?";

    @Column
    private String registerAddress = "http://distribution.virk.dk";



    @Column
    @Enumerated(EnumType.ORDINAL)
    private RegisterType companyRegisterType = RegisterType.REMOTE_HTTP;

    @Column
    private String companyStartAddress = "http://distribution.virk.dk/cvr-permanent/virksomhed/_search";

    @Column
    private String companyScrollAddress = "http://distribution.virk.dk/_search/scroll";

    @Column
    private String companyUsername = "";

    @Column
    private String companyPassword= "";

    @Column
    private String companyQuery = "{" +
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


    public RegisterType getCompanyRegisterType() {
        return this.companyRegisterType;
    }

    public String getCompanyStartAddress() {
        return this.companyStartAddress;
    }

    public String getCompanyScrollAddress() {
        return this.companyScrollAddress;
    }

    public String getCompanyUsername() {
        return this.companyUsername;
    }

    public String getCompanyPassword() {
        return this.companyPassword;
    }

    public String getCompanyQuery() {
        return this.companyQuery;
    }







    @Column
    @Enumerated(EnumType.ORDINAL)
    private RegisterType companyUnitRegisterType = RegisterType.REMOTE_HTTP;

    @Column
    private String companyUnitStartAddress = "http://distribution.virk.dk/cvr-permanent/produktionsenhed/_search";

    @Column
    private String companyUnitScrollAddress = "http://distribution.virk.dk/_search/scroll";

    @Column
    private String companyUnitUsername = "";

    @Column
    private String companyUnitPassword= "";

    @Column
    private String companyUnitQuery = "{" +
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

    public RegisterType getCompanyUnitRegisterType() {
        return this.companyUnitRegisterType;
    }

    public String getCompanyUnitStartAddress() {
        return this.companyUnitStartAddress;
    }

    public String getCompanyUnitScrollAddress() {
        return this.companyUnitScrollAddress;
    }

    public String getCompanyUnitUsername() {
        return this.companyUnitUsername;
    }

    public String getCompanyUnitPassword() {
        return this.companyUnitPassword;
    }

    public String getCompanyUnitQuery() {
        return this.companyUnitQuery;
    }




    @Column
    @Enumerated(EnumType.ORDINAL)
    private RegisterType participantRegisterType = RegisterType.REMOTE_HTTP;

    @Column
    private String participantStartAddress = "http://distribution.virk.dk/cvr-permanent/deltager/_search";

    @Column
    private String participantScrollAddress = "http://distribution.virk.dk/_search/scroll";

    @Column
    private String participantUsername = "";

    @Column
    private String participantPassword= "";

    @Column
    private String participantQuery = "{" +
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


    public RegisterType getParticipantRegisterType() {
        return this.participantRegisterType;
    }

    public String getParticipantStartAddress() {
        return this.participantStartAddress;
    }

    public String getParticipantScrollAddress() {
        return this.participantScrollAddress;
    }

    public String getParticipantUsername() {
        return this.participantUsername;
    }

    public String getParticipantPassword() {
        return this.participantPassword;
    }

    public String getParticipantQuery() {
        return this.participantQuery;
    }









    public String getPullCronSchedule() {
        return this.pullCronSchedule;
    }

    public String getRegisterAddress() {
        return this.registerAddress;
    }




    public String getQuery(String schema) {
        switch (schema) {
            case CompanyEntity.schema:
                return this.getCompanyQuery();
            case CompanyUnitEntity.schema:
                return this.getCompanyUnitQuery();
            case ParticipantEntity.schema:
                return this.getParticipantQuery();
        }
        return null;
    }

    public RegisterType getRegisterType(String schema) {
        switch (schema) {
            case CompanyEntity.schema:
                return this.getCompanyRegisterType();
            case CompanyUnitEntity.schema:
                return this.getCompanyUnitRegisterType();
            case ParticipantEntity.schema:
                return this.getParticipantRegisterType();
        }
        return null;
    }

    public String getStartAddress(String schema) {
        switch (schema) {
            case CompanyEntity.schema:
                return this.getCompanyStartAddress();
            case CompanyUnitEntity.schema:
                return this.getCompanyUnitStartAddress();
            case ParticipantEntity.schema:
                return this.getParticipantStartAddress();
        }
        return null;
    }

    public String getScrollAddress(String schema) {
        switch (schema) {
            case CompanyEntity.schema:
                return this.getCompanyScrollAddress();
            case CompanyUnitEntity.schema:
                return this.getCompanyUnitScrollAddress();
            case ParticipantEntity.schema:
                return this.getParticipantScrollAddress();
        }
        return null;
    }

    public String getUsername(String schema) {
        switch (schema) {
            case CompanyEntity.schema:
                return this.getCompanyUsername();
            case CompanyUnitEntity.schema:
                return this.getCompanyUnitUsername();
            case ParticipantEntity.schema:
                return this.getParticipantUsername();
        }
        return null;
    }

    public String getPassword(String schema) {
        switch (schema) {
            case CompanyEntity.schema:
                return this.getCompanyPassword();
            case CompanyUnitEntity.schema:
                return this.getCompanyUnitPassword();
            case ParticipantEntity.schema:
                return this.getParticipantPassword();
        }
        return null;
    }
}
