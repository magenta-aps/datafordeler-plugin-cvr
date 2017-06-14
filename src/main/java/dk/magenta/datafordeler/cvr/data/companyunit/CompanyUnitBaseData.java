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
        if (this.industryData != null) {
            map.putAll(this.industryData.asMap());
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
    private CompanyUnitIndustryData industryData;

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

    public CompanyUnitIndustryData getIndustryData() {
        if (this.industryData == null) {
            this.industryData = new CompanyUnitIndustryData();
        }
        return this.industryData;
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
        if (this.industryData != null) {
            lookupDefinition.putAll("industryData", this.industryData.databaseFields());
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
