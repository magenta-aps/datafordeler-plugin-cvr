package dk.magenta.datafordeler.cvr.data.company;

import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.core.fapi.ParameterMap;
import dk.magenta.datafordeler.core.fapi.QueryField;
import dk.magenta.datafordeler.cvr.data.CvrQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 19-05-17.
 */
public class CompanyQuery extends CvrQuery<CompanyEntity> {

    public static final String CVRNUMMER = "CVRNummer";
    public static final String REKLAMEBESKYTTELSE = "reklamebeskyttelse";
    public static final String NAVN = "virksomhedsnavn";
    public static final String TELEFONNUMMER = "phoneNumber";
    public static final String TELEFAXNUMMER = "faxNumber";
    public static final String EMAILADRESSE = "emailadresse";
    public static final String VIRKSOMHEDSFORM = "virksomhedsform";

    @QueryField(type = QueryField.FieldType.STRING, queryName = CVRNUMMER)
    private String CVRNummer;

    public String getCVRNummer() {
        return CVRNummer;
    }

    public void setCVRNummer(String CVRNummer) {
        this.CVRNummer = CVRNummer;
    }


    @QueryField(type = QueryField.FieldType.STRING, queryName = REKLAMEBESKYTTELSE)
    private String reklamebeskyttelse;

    public String getReklamebeskyttelse() {
        return this.reklamebeskyttelse;
    }

    public void setReklamebeskyttelse(String reklamebeskyttelse) {
        this.reklamebeskyttelse = reklamebeskyttelse;
    }

    @QueryField(type = QueryField.FieldType.STRING, queryName = NAVN)
    private String virksomhedsnavn;

    public String getVirksomhedsnavn() {
        return this.virksomhedsnavn;
    }

    public void setVirksomhedsnavn(String virksomhedsnavn) {
        this.virksomhedsnavn = virksomhedsnavn;
    }


    @QueryField(type = QueryField.FieldType.STRING, queryName = TELEFONNUMMER)
    private String telefonnummer;

    public String getTelefonnummer() {
        return this.telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }



    @QueryField(type = QueryField.FieldType.STRING, queryName = TELEFAXNUMMER)
    private String telefaxnummer;

    public String getTelefaxnummer() {
        return this.telefaxnummer;
    }

    public void setTelefaxnummer(String telefaxnummer) {
        this.telefaxnummer = telefaxnummer;
    }



    @QueryField(type = QueryField.FieldType.STRING, queryName = EMAILADRESSE)
    private String emailadresse;

    public String getEmailadresse() {
        return this.emailadresse;
    }

    public void setEmailadresse(String emailadresse) {
        this.emailadresse = emailadresse;
    }



    @QueryField(type = QueryField.FieldType.STRING, queryName = VIRKSOMHEDSFORM)
    private String virksomhedsform;

    public String getVirksomhedsform() {
        return this.virksomhedsform;
    }

    public void setVirksomhedsform(String virksomhedsform) {
        this.virksomhedsform = virksomhedsform;
    }

    public void setVirksomhedsform(int virksomhedsform) {
        this.setVirksomhedsform(Integer.toString(virksomhedsform));
    }

    @Override
    public Map<String, Object> getSearchParameters() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(CVRNUMMER, this.CVRNummer);
        map.put(REKLAMEBESKYTTELSE, this.reklamebeskyttelse);
        map.put(NAVN, this.virksomhedsnavn);
        map.put(TELEFONNUMMER, this.telefonnummer);
        map.put(TELEFAXNUMMER, this.telefaxnummer);
        map.put(EMAILADRESSE, this.emailadresse);
        map.put(VIRKSOMHEDSFORM, this.virksomhedsform);
        return map;
    }

    @Override
    public void setFromParameters(ParameterMap parameters) {
        this.setCVRNummer(parameters.getFirst(CVRNUMMER));
        this.setReklamebeskyttelse(parameters.getFirst(REKLAMEBESKYTTELSE));
        this.setVirksomhedsnavn(parameters.getFirst(NAVN));
        this.setTelefonnummer(parameters.getFirst(TELEFONNUMMER));
        this.setTelefaxnummer(parameters.getFirst(TELEFAXNUMMER));
        this.setEmailadresse(parameters.getFirst(EMAILADRESSE));
        this.setVirksomhedsform(parameters.getFirst(VIRKSOMHEDSFORM));
    }

    @Override
    public Class<CompanyEntity> getEntityClass() {
        return CompanyEntity.class;
    }

    @Override
    public Class getDataClass() {
        return CompanyBaseData.class;
    }


    @Override
    public LookupDefinition getLookupDefinition() {
        LookupDefinition lookupDefinition = new LookupDefinition(this);

        if (this.CVRNummer != null) {
            lookupDefinition.put(LookupDefinition.entityref + ".cvrNumber", this.CVRNummer);
        }
        if (this.virksomhedsform != null) {
            lookupDefinition.put("companyForm.companyForm.companyFormCode", this.virksomhedsform);
        }
        if (this.reklamebeskyttelse != null) {
            lookupDefinition.put("advertProtection.value", this.reklamebeskyttelse);
        }
        /*if (this.lifecycleData != null) {
            lookupDefinition.putAll("lifecycleData", this.lifecycleData.databaseFields());
        }
        if (this.locationAddressData != null) {
            lookupDefinition.putAll("locationAddressData", this.locationAddressData.databaseFields());
        }
        if (this.postalAddressData != null) {
            lookupDefinition.putAll("postalAddressData", this.postalAddressData.databaseFields());
        }
        if (this.yearlyEmployeeNumbersData != null) {
            lookupDefinition.putAll("yearlyEmployeeNumbersData", this.yearlyEmployeeNumbersData.databaseFields());
        }
        if (this.quarterlyEmployeeNumbersData != null) {
            lookupDefinition.putAll("quarterlyEmployeeNumbersData", this.quarterlyEmployeeNumbersData.databaseFields());
        }*//*
        if (this.primaryIndustryData != null) {
            lookupDefinition.putAll("primaryIndustryData", this.primaryIndustryData.databaseFields());
        }
        if (this.secondaryIndustryData1 != null) {
            lookupDefinition.putAll("secondaryIndustryData1", this.secondaryIndustryData1.databaseFields());
        }
        if (this.secondaryIndustryData2 != null) {
            lookupDefinition.putAll("secondaryIndustryData2", this.secondaryIndustryData2.databaseFields());
        }
        if (this.secondaryIndustryData3 != null) {
            lookupDefinition.putAll("secondaryIndustryData3", this.secondaryIndustryData3.databaseFields());
        }*/
        if (this.virksomhedsnavn != null) {
            lookupDefinition.put("companyName.value", this.virksomhedsnavn);
        }
        if (this.telefonnummer != null) {
            lookupDefinition.put("phoneNumber.value", this.telefonnummer);
        }
        if (this.emailadresse != null) {
            lookupDefinition.put("emailAddress.value", this.emailadresse);
        }
        if (this.telefaxnummer != null) {
            lookupDefinition.put("faxNumber.value", this.telefaxnummer);
        }
        return lookupDefinition;
    }
}
