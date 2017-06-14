package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.database.LookupDefinition;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 12-06-17.
 */
@Entity
@Table(name="cvr_companyunit_basedata")
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class CompanyUnitBaseData extends DataItem<CompanyUnitEffect, CompanyUnitBaseData> {
    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        if (this.mainData != null) {
            map.putAll(this.mainData.asMap());
        }
        if (this.primaryIndustryData != null) {
            map.putAll(this.primaryIndustryData.asMap());
        }
        if (this.secondaryIndustryData1 != null) {
            map.putAll(this.secondaryIndustryData1.asMap());
        }
        if (this.secondaryIndustryData2 != null) {
            map.putAll(this.secondaryIndustryData2.asMap());
        }
        if (this.secondaryIndustryData3 != null) {
            map.putAll(this.secondaryIndustryData3.asMap());
        }
        if (this.nameData != null) {
            map.putAll(this.nameData.asMap());
        }
        if (this.phoneData != null) {
            map.putAll(this.phoneData.asMap());
        }
        if (this.emailData != null) {
            map.putAll(this.emailData.asMap());
        }
        if (this.faxData != null) {
            map.putAll(this.faxData.asMap());
        }
        return map;
    }

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyUnitMainData mainData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyUnitIndustryData primaryIndustryData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyUnitIndustryData secondaryIndustryData1;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyUnitIndustryData secondaryIndustryData2;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyUnitIndustryData secondaryIndustryData3;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyUnitTextData nameData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyUnitTextData phoneData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyUnitTextData emailData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyUnitTextData faxData;

    public CompanyUnitMainData getMainData() {
        if (this.mainData == null) {
            this.mainData = new CompanyUnitMainData();
        }
        return this.mainData;
    }

    public CompanyUnitIndustryData obtainPrimaryIndustryData() {
        if (this.primaryIndustryData == null) {
            this.primaryIndustryData = new CompanyUnitIndustryData(true);
        }
        return this.primaryIndustryData;
    }
    public CompanyUnitIndustryData obtainSecondaryIndustryData1() {
        if (this.secondaryIndustryData1 == null) {
            this.secondaryIndustryData1 = new CompanyUnitIndustryData(false);
        }
        return this.secondaryIndustryData1;
    }
    public CompanyUnitIndustryData obtainSecondaryIndustryData2() {
        if (this.secondaryIndustryData2 == null) {
            this.secondaryIndustryData2 = new CompanyUnitIndustryData(false);
        }
        return this.secondaryIndustryData2;
    }
    public CompanyUnitIndustryData obtainSecondaryIndustryData3() {
        if (this.secondaryIndustryData3 == null) {
            this.secondaryIndustryData3 = new CompanyUnitIndustryData(false);
        }
        return this.secondaryIndustryData3;
    }

    public CompanyUnitTextData getNameData() {
        if (this.nameData == null) {
            this.nameData = new CompanyUnitTextData(CompanyUnitTextData.Type.NAME);
        }
        return this.nameData;
    }

    public CompanyUnitTextData getPhoneData() {
        if (this.phoneData == null) {
            this.phoneData = new CompanyUnitTextData(CompanyUnitTextData.Type.PHONE);
        }
        return this.phoneData;
    }

    public CompanyUnitTextData getEmailData() {
        if (this.emailData == null) {
            this.emailData = new CompanyUnitTextData(CompanyUnitTextData.Type.EMAIL);
        }
        return this.emailData;
    }

    public CompanyUnitTextData getFaxData() {
        if (this.faxData == null) {
            this.faxData = new CompanyUnitTextData(CompanyUnitTextData.Type.FAX);
        }
        return this.faxData;
    }

    @Override
    public LookupDefinition getLookupDefinition() {
        LookupDefinition lookupDefinition = new LookupDefinition();
        if (this.mainData != null) {
            lookupDefinition.putAll("mainData", this.mainData.databaseFields());
        }
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
        }
        if (this.nameData != null) {
            lookupDefinition.putAll("nameData", this.nameData.databaseFields());
        }
        if (this.phoneData != null) {
            lookupDefinition.putAll("phoneData", this.phoneData.databaseFields());
        }
        if (this.emailData != null) {
            lookupDefinition.putAll("emailData", this.emailData.databaseFields());
        }
        if (this.faxData != null) {
            lookupDefinition.putAll("faxData", this.faxData.databaseFields());
        }
        return lookupDefinition;
    }

}
