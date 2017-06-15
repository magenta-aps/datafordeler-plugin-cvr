package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.cvr.data.company.*;

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
        if (this.lifecycleData != null) {
            map.put("lifecycle", this.lifecycleData.asMap());
        }
        if (this.advertProtectionData != null) {
            map.put("advertProtection", this.advertProtectionData.getData());
        }
        if (this.locationAddressData != null) {
            map.put("locationAddress", this.locationAddressData.getAddress());
        }
        if (this.postalAddressData != null) {
            map.put("postalAddress", this.postalAddressData.getAddress());
        }
        if (this.primaryIndustryData != null) {
            map.put("primaryIndustry", this.primaryIndustryData.getIndustry());
        }
        if (this.secondaryIndustryData1 != null) {
            map.put("secondaryIndustry1", this.secondaryIndustryData1.getIndustry());
        }
        if (this.secondaryIndustryData2 != null) {
            map.put("secondaryIndustry2", this.secondaryIndustryData2.getIndustry());
        }
        if (this.secondaryIndustryData3 != null) {
            map.put("secondaryIndustry3", this.secondaryIndustryData3.getIndustry());
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
        if (this.isPrimaryData != null) {
            map.putAll(this.isPrimaryData.asMap());
        }
        return map;
    }

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyUnitMainData mainData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyLifecycleData lifecycleData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyBooleanData advertProtectionData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyAddressData locationAddressData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyAddressData postalAddressData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyIndustryData primaryIndustryData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyIndustryData secondaryIndustryData1;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyIndustryData secondaryIndustryData2;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyIndustryData secondaryIndustryData3;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyTextData nameData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyTextData phoneData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyTextData emailData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyTextData faxData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyBooleanData isPrimaryData;

    public CompanyUnitMainData getMainData() {
        if (this.mainData == null) {
            this.mainData = new CompanyUnitMainData();
        }
        return this.mainData;
    }

    public CompanyLifecycleData obtainLifecycleData() {
        if (this.lifecycleData == null) {
            this.lifecycleData = new CompanyLifecycleData();
        }
        return this.lifecycleData;
    }
    public CompanyBooleanData obtainAdvertProtectionData() {
        if (this.advertProtectionData == null) {
            this.advertProtectionData = new CompanyBooleanData(CompanyBooleanData.Type.ADVERT_PROTECTION);
        }
        return this.advertProtectionData;
    }
    public CompanyAddressData obtainLocationAddressData() {
        if (this.locationAddressData == null) {
            this.locationAddressData = new CompanyAddressData();
        }
        return this.locationAddressData;
    }
    public CompanyAddressData obtainPostalAddressData() {
        if (this.postalAddressData == null) {
            this.postalAddressData = new CompanyAddressData();
        }
        return this.postalAddressData;
    }
    public CompanyIndustryData obtainPrimaryIndustryData() {
        if (this.primaryIndustryData == null) {
            this.primaryIndustryData = new CompanyIndustryData(true);
        }
        return this.primaryIndustryData;
    }
    public CompanyIndustryData obtainSecondaryIndustryData1() {
        if (this.secondaryIndustryData1 == null) {
            this.secondaryIndustryData1 = new CompanyIndustryData(false);
        }
        return this.secondaryIndustryData1;
    }
    public CompanyIndustryData obtainSecondaryIndustryData2() {
        if (this.secondaryIndustryData2 == null) {
            this.secondaryIndustryData2 = new CompanyIndustryData(false);
        }
        return this.secondaryIndustryData2;
    }
    public CompanyIndustryData obtainSecondaryIndustryData3() {
        if (this.secondaryIndustryData3 == null) {
            this.secondaryIndustryData3 = new CompanyIndustryData(false);
        }
        return this.secondaryIndustryData3;
    }

    public CompanyTextData getNameData() {
        if (this.nameData == null) {
            this.nameData = new CompanyTextData(CompanyTextData.Type.NAME);
        }
        return this.nameData;
    }

    public CompanyTextData getPhoneData() {
        if (this.phoneData == null) {
            this.phoneData = new CompanyTextData(CompanyTextData.Type.PHONE);
        }
        return this.phoneData;
    }

    public CompanyTextData getEmailData() {
        if (this.emailData == null) {
            this.emailData = new CompanyTextData(CompanyTextData.Type.EMAIL);
        }
        return this.emailData;
    }

    public CompanyTextData getFaxData() {
        if (this.faxData == null) {
            this.faxData = new CompanyTextData(CompanyTextData.Type.FAX);
        }
        return this.faxData;
    }

    public CompanyBooleanData getIsPrimaryData() {
        if (this.isPrimaryData == null) {
            this.isPrimaryData = new CompanyBooleanData(CompanyBooleanData.Type.IS_PRIMARY_UNIT);
        }
        return this.isPrimaryData;
    }

    @Override
    public LookupDefinition getLookupDefinition() {
        LookupDefinition lookupDefinition = new LookupDefinition();
        if (this.mainData != null) {
            lookupDefinition.putAll("mainData", this.mainData.databaseFields());
        }
        if (this.lifecycleData != null) {
            lookupDefinition.putAll("lifecycleData", this.lifecycleData.databaseFields());
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
        if (this.isPrimaryData != null) {
            lookupDefinition.putAll("isPrimaryData", this.isPrimaryData.databaseFields());
        }
        return lookupDefinition;
    }

}
