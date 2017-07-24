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

    public static final String CVRNUMBER = "cvrnumber";
    public static final String ADVERTPROTECTION = "advertprotection";
    public static final String NAME = "name";
    public static final String PHONE = "phone";
    public static final String FAX = "fax";
    public static final String EMAIL = "email";
    public static final String FORM = "form";

    @QueryField(type = QueryField.FieldType.STRING, queryName = CVRNUMBER)
    private String cvrNumber;

    public String getCvrNumber() {
        return cvrNumber;
    }

    public void setCvrNumber(String cvrNumber) {
        this.cvrNumber = cvrNumber;
    }


    @QueryField(type = QueryField.FieldType.BOOLEAN, queryName = ADVERTPROTECTION)
    private String advertProtection;

    public String getAdvertProtection() {
        return this.advertProtection;
    }

    public void setAdvertProtection(String advertProtection) {
        this.advertProtection = advertProtection;
    }

    @QueryField(type = QueryField.FieldType.STRING, queryName = NAME)
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @QueryField(type = QueryField.FieldType.STRING, queryName = PHONE)
    private String phone;

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }



    @QueryField(type = QueryField.FieldType.STRING, queryName = FAX)
    private String fax;

    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }



    @QueryField(type = QueryField.FieldType.STRING, queryName = EMAIL)
    private String email;

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    @QueryField(type = QueryField.FieldType.INT, queryName = FORM)
    private String formCode;

    public String getFormCode() {
        return this.formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public void setFormCode(int formCode) {
        this.setFormCode(Integer.toString(formCode));
    }

    @Override
    public Map<String, Object> getSearchParameters() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(CVRNUMBER, this.cvrNumber);
        map.put(ADVERTPROTECTION, this.advertProtection);
        map.put(NAME, this.name);
        map.put(PHONE, this.phone);
        map.put(FAX, this.fax);
        map.put(EMAIL, this.email);
        map.put(FORM, this.formCode);
        return map;
    }

    @Override
    public void setFromParameters(ParameterMap parameters) {
        this.setCvrNumber(parameters.getFirst(CVRNUMBER));
        this.setAdvertProtection(parameters.getFirst(ADVERTPROTECTION));
        this.setName(parameters.getFirst(NAME));
        this.setPhone(parameters.getFirst(PHONE));
        this.setFax(parameters.getFirst(FAX));
        this.setEmail(parameters.getFirst(EMAIL));
        this.setFormCode(parameters.getFirst(FORM));
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

        if (this.cvrNumber != null) {
            lookupDefinition.put(LookupDefinition.entityref + ".cvrNumber", this.cvrNumber);
        }

        /*if (this.lifecycleData != null) {
            lookupDefinition.putAll("lifecycleData", this.lifecycleData.databaseFields());
        }*/
        if (this.formCode != null) {
            lookupDefinition.put("formData.form.code", this.formCode);
        }
        if (this.advertProtection != null) {
            lookupDefinition.put("advertProtectionData.data", this.advertProtection);
        }
        /*if (this.locationAddressData != null) {
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
        if (this.name != null) {
            lookupDefinition.put("nameData.data", this.name);
        }
        if (this.phone != null) {
            lookupDefinition.put("phoneData.data", this.phone);
        }
        if (this.email != null) {
            lookupDefinition.put("emailData.data", this.email);
        }
        if (this.fax != null) {
            lookupDefinition.put("faxData.data", this.fax);
        }
        return lookupDefinition;
    }
}
