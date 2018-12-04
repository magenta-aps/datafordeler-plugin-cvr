package dk.magenta.datafordeler.cvr.configuration;

import dk.magenta.datafordeler.core.configuration.Configuration;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import dk.magenta.datafordeler.cvr.records.CompanyRecord;
import dk.magenta.datafordeler.cvr.records.CompanyUnitRecord;
import dk.magenta.datafordeler.cvr.records.ParticipantRecord;

import javax.persistence.*;

/**
 * Configuration instance class, holding configuration values in fields
 */
@javax.persistence.Entity
@Table(name="cvr_config")
public class CvrConfiguration implements Configuration {

    public enum RegisterType {
        DISABLED(0),
        LOCAL_FILE(1),
        REMOTE_HTTP(2);

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
    private String pullCronSchedule = null;//"0 0 0 1 1 ?";

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
            "                    \"Vrvirksomhed.sidstOpdateret\": {\n" +
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
    private RegisterType companyUnitRegisterType = RegisterType.DISABLED;

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
            "                    \"VrproduktionsEnhed.sidstOpdateret\": {\n" +
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
    private RegisterType participantRegisterType = RegisterType.DISABLED;

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
            "                    \"Vrdeltagerperson.sidstOpdateret\": {\n" +
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




    public String getQuery(String schema) {
        switch (schema) {
            case CompanyRecord.schema:
                return this.getCompanyRegisterQuery();
            case CompanyUnitRecord.schema:
                return this.getCompanyUnitRegisterQuery();
            case ParticipantRecord.schema:
                return this.getParticipantRegisterQuery();
        }
        return null;
    }

    public RegisterType getRegisterType(String schema) {
        switch (schema) {
            case CompanyRecord.schema:
                return this.getCompanyRegisterType();
            case CompanyUnitRecord.schema:
                return this.getCompanyUnitRegisterType();
            case ParticipantRecord.schema:
                return this.getParticipantRegisterType();
        }
        return null;
    }

    public String getStartAddress(String schema) {
        switch (schema) {
            case CompanyRecord.schema:
                return this.getCompanyRegisterStartAddress();
            case CompanyUnitRecord.schema:
                return this.getCompanyUnitRegisterStartAddress();
            case ParticipantRecord.schema:
                return this.getParticipantRegisterStartAddress();
        }
        return null;
    }

    public String getScrollAddress(String schema) {
        switch (schema) {
            case CompanyRecord.schema:
                return this.getCompanyRegisterScrollAddress();
            case CompanyUnitRecord.schema:
                return this.getCompanyUnitRegisterScrollAddress();
            case ParticipantRecord.schema:
                return this.getParticipantRegisterScrollAddress();
        }
        return null;
    }

    public String getUsername(String schema) {
        switch (schema) {
            case CompanyRecord.schema:
                return this.getCompanyRegisterUsername();
            case CompanyUnitRecord.schema:
                return this.getCompanyUnitRegisterUsername();
            case ParticipantRecord.schema:
                return this.getParticipantRegisterUsername();
        }
        return null;
    }

    public String getPassword(String schema) {
        switch (schema) {
            case CompanyRecord.schema:
                return this.getCompanyRegisterPassword();
            case CompanyUnitRecord.schema:
                return this.getCompanyUnitRegisterPassword();
            case ParticipantRecord.schema:
                return this.getParticipantRegisterPassword();
        }
        return null;
    }


    public void setCompanyRegisterUsername(String companyRegisterUsername) {
        this.companyRegisterUsername = companyRegisterUsername;
    }

    public void setCompanyRegisterPassword(String companyRegisterPassword) {
        this.companyRegisterPassword = companyRegisterPassword;
    }

    public void setCompanyRegisterQuery(String companyRegisterQuery) {
        this.companyRegisterQuery = companyRegisterQuery;
    }

    public void setCompanyUnitRegisterUsername(String companyUnitRegisterUsername) {
        this.companyUnitRegisterUsername = companyUnitRegisterUsername;
    }

    public void setCompanyUnitRegisterPassword(String companyUnitRegisterPassword) {
        this.companyUnitRegisterPassword = companyUnitRegisterPassword;
    }

    public void setCompanyUnitRegisterQuery(String companyUnitRegisterQuery) {
        this.companyUnitRegisterQuery = companyUnitRegisterQuery;
    }

    public void setParticipantRegisterUsername(String participantRegisterUsername) {
        this.participantRegisterUsername = participantRegisterUsername;
    }

    public void setParticipantRegisterPassword(String participantRegisterPassword) {
        this.participantRegisterPassword = participantRegisterPassword;
    }

    public void setParticipantRegisterQuery(String participantRegisterQuery) {
        this.participantRegisterQuery = participantRegisterQuery;
    }

    public void setCompanyRegisterType(RegisterType companyRegisterType) {
        this.companyRegisterType = companyRegisterType;
    }

    public void setCompanyUnitRegisterType(RegisterType companyUnitRegisterType) {
        this.companyUnitRegisterType = companyUnitRegisterType;
    }

    public void setParticipantRegisterType(RegisterType participantRegisterType) {
        this.participantRegisterType = participantRegisterType;
    }
}
