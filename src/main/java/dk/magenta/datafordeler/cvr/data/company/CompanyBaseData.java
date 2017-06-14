package dk.magenta.datafordeler.cvr.data.company;

import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.cvr.data.CvrData;
import dk.magenta.datafordeler.cvr.data.embeddable.*;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by lars on 12-06-17.
 */
@Entity
@Table(name="cvr_company_basedata")
//@MappedSuperclass
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class CompanyBaseData extends DataItem<CompanyEffect, CompanyBaseData> {

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
    private CompanyMainData mainData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyIndustryData industryData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyTextData nameData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyTextData phoneData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyTextData emailData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyTextData faxData;

    public CompanyMainData getMainData() {
        if (this.mainData == null) {
            this.mainData = new CompanyMainData();
        }
        return this.mainData;
    }

    public CompanyIndustryData getIndustryData() {
        if (this.industryData == null) {
            this.industryData = new CompanyIndustryData();
        }
        return this.industryData;
    }

    public CompanyTextData getNameData() {
        if (this.nameData == null) {
            this.nameData = new CompanyTextData(CompanyTextEmbed.Type.NAME);
        }
        return this.nameData;
    }

    public CompanyTextData getPhoneData() {
        if (this.phoneData == null) {
            this.phoneData = new CompanyTextData(CompanyTextEmbed.Type.PHONE);
        }
        return this.phoneData;
    }

    public CompanyTextData getEmailData() {
        if (this.emailData == null) {
            this.emailData = new CompanyTextData(CompanyTextEmbed.Type.EMAIL);
        }
        return this.emailData;
    }

    public CompanyTextData getFaxData() {
        if (this.faxData == null) {
            this.faxData = new CompanyTextData(CompanyTextEmbed.Type.FAX);
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
