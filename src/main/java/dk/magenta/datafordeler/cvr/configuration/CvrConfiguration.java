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
    private String companyRegisterStartAddress = "http://distribution.virk.dk/cvr-permanent/virksomhed/_search";

    @Column
    private String companyRegisterScrollAddress = "http://distribution.virk.dk/_search/scroll";

    @Column
    private String companyRegisterUsername = "";

    @Column
    private String companyRegisterPassword = "";

    @Column(length = 1024)
    private String companyRegisterQuery = "{\n" +
            "    \"query\": {\n" +
            "        \"filtered\": {\n" +
            "            \"filter\": {\n" +
            "                \"range\": {\n" +
            "                    \"Vrvirksomhed.sidstIndlaest\": {\n" +
            "                        \"gte\": \"%s\"\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}\n";


    public RegisterType getCompanyRegisterType() {
        return this.companyRegisterType;
    }

    public String getCompanyRegisterStartAddress() {
        return this.companyRegisterStartAddress;
    }

    public String getCompanyRegisterScrollAddress() {
        return this.companyRegisterScrollAddress;
    }

    public String getCompanyRegisterUsername() {
        return this.companyRegisterUsername;
    }

    public String getCompanyRegisterPassword() {
        return this.companyRegisterPassword;
    }

    public String getCompanyRegisterQuery() {
        return this.companyRegisterQuery;
    }







    @Column
    @Enumerated(EnumType.ORDINAL)
    private RegisterType companyUnitRegisterType = RegisterType.REMOTE_HTTP;

    @Column
    private String companyUnitRegisterStartAddress = "http://distribution.virk.dk/cvr-permanent/produktionsenhed/_search";

    @Column
    private String companyUnitRegisterScrollAddress = "http://distribution.virk.dk/_search/scroll";

    @Column
    private String companyUnitRegisterUsername = "";

    @Column
    private String companyUnitRegisterPassword = "";

    @Column(length = 1024)
    private String companyUnitRegisterQuery = "{\n" +
            "    \"query\": {\n" +
            "        \"filtered\": {\n" +
            "            \"filter\": {\n" +
            "                \"range\": {\n" +
            "                    \"VrproduktionsEnhed.sidstIndlaest\": {\n" +
            "                        \"gte\": \"%s\"\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}\n";

    public RegisterType getCompanyUnitRegisterType() {
        return this.companyUnitRegisterType;
    }

    public String getCompanyUnitRegisterStartAddress() {
        return this.companyUnitRegisterStartAddress;
    }

    public String getCompanyUnitRegisterScrollAddress() {
        return this.companyUnitRegisterScrollAddress;
    }

    public String getCompanyUnitRegisterUsername() {
        return this.companyUnitRegisterUsername;
    }

    public String getCompanyUnitRegisterPassword() {
        return this.companyUnitRegisterPassword;
    }

    public String getCompanyUnitRegisterQuery() {
        return this.companyUnitRegisterQuery;
    }




    @Column
    @Enumerated(EnumType.ORDINAL)
    private RegisterType participantRegisterType = RegisterType.REMOTE_HTTP;

    @Column
    private String participantRegisterStartAddress = "http://distribution.virk.dk/cvr-permanent/deltager/_search";

    @Column
    private String participantRegisterScrollAddress = "http://distribution.virk.dk/_search/scroll";

    @Column
    private String participantRegisterUsername = "";

    @Column
    private String participantRegisterPassword = "";

    @Column(length = 1024)
    private String participantRegisterQuery = "{\n" +
            "    \"query\": {\n" +
            "        \"filtered\": {\n" +
            "            \"filter\": {\n" +
            "                \"range\": {\n" +
            "                    \"Vrdeltagerperson.sidstIndlaest\": {\n" +
            "                        \"gte\": \"%s\"\n" +
            "                    }\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}\n";


    public RegisterType getParticipantRegisterType() {
        return this.participantRegisterType;
    }

    public String getParticipantRegisterStartAddress() {
        return this.participantRegisterStartAddress;
    }

    public String getParticipantRegisterScrollAddress() {
        return this.participantRegisterScrollAddress;
    }

    public String getParticipantRegisterUsername() {
        return this.participantRegisterUsername;
    }

    public String getParticipantRegisterPassword() {
        return this.participantRegisterPassword;
    }

    public String getParticipantRegisterQuery() {
        return this.participantRegisterQuery;
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
                return this.getCompanyRegisterQuery();
            case CompanyUnitEntity.schema:
                return this.getCompanyUnitRegisterQuery();
            case ParticipantEntity.schema:
                return this.getParticipantRegisterQuery();
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
                return this.getCompanyRegisterStartAddress();
            case CompanyUnitEntity.schema:
                return this.getCompanyUnitRegisterStartAddress();
            case ParticipantEntity.schema:
                return this.getParticipantRegisterStartAddress();
        }
        return null;
    }

    public String getScrollAddress(String schema) {
        switch (schema) {
            case CompanyEntity.schema:
                return this.getCompanyRegisterScrollAddress();
            case CompanyUnitEntity.schema:
                return this.getCompanyUnitRegisterScrollAddress();
            case ParticipantEntity.schema:
                return this.getParticipantRegisterScrollAddress();
        }
        return null;
    }

    public String getUsername(String schema) {
        switch (schema) {
            case CompanyEntity.schema:
                return this.getCompanyRegisterUsername();
            case CompanyUnitEntity.schema:
                return this.getCompanyUnitRegisterUsername();
            case ParticipantEntity.schema:
                return this.getParticipantRegisterUsername();
        }
        return null;
    }

    public String getPassword(String schema) {
        switch (schema) {
            case CompanyEntity.schema:
                return this.getCompanyRegisterPassword();
            case CompanyUnitEntity.schema:
                return this.getCompanyUnitRegisterPassword();
            case ParticipantEntity.schema:
                return this.getParticipantRegisterPassword();
        }
        return null;
    }
}
