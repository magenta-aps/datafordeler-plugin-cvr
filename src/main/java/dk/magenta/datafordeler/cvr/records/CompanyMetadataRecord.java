package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.*;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import org.hibernate.Session;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = CompanyMetadataRecord.TABLE_NAME, indexes = {
        @Index(name = CompanyMetadataRecord.TABLE_NAME + "__company", columnList = MetadataRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
        @Index(name = CompanyMetadataRecord.TABLE_NAME + "__unit", columnList = MetadataRecord.DB_FIELD_COMPANYUNIT + DatabaseEntry.REF),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyMetadataRecord extends MetadataRecord {

    public static final String TABLE_NAME = "cvr_record_metadata";

    public static final String DB_FIELD_NEWEST_FORM = "newestForm";
    public static final String IO_FIELD_NEWEST_FORM = "nyesteVirksomhedsform";

    @OneToMany(mappedBy = FormRecord.DB_FIELD_COMPANY_METADATA, targetEntity = FormRecord.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FormRecord> newestForm = new HashSet<>();

    public void setNewestForm(Set<FormRecord> newestForm) {
        this.newestForm = newestForm;
    }

    @JsonSetter(IO_FIELD_NEWEST_FORM)
    public void addNewestForm(FormRecord newestForm) {
        if (newestForm != null && !this.newestForm.contains(newestForm)) {
            newestForm.setCompanyMetadataRecord(this);
            this.newestForm.add(newestForm);
        }
    }

    @JsonIgnore
    public Set<FormRecord> getNewestForm() {
        return this.newestForm;
    }

    @JsonGetter(IO_FIELD_NEWEST_FORM)
    public FormRecord getLatestNewestForm() {
        FormRecord latest = null;
        for (FormRecord formRecord : this.newestForm) {
            if (latest == null || formRecord.getLastUpdated().isAfter(latest.getLastUpdated())) {
                latest = formRecord;
            }
        }
        return latest;
    }





    public static final String DB_FIELD_NEWEST_NAME = "newestName";
    public static final String IO_FIELD_NEWEST_NAME = "nyesteNavn";

    @OneToMany(targetEntity = BaseNameRecord.class, mappedBy = BaseNameRecord.DB_FIELD_COMPANY_METADATA, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(value = IO_FIELD_NEWEST_NAME)
    private Set<BaseNameRecord> newestName = new HashSet<>();

    public void setNewestName(Set<BaseNameRecord> newestName) {
        this.newestName = newestName;
    }

    @JsonSetter(IO_FIELD_NEWEST_NAME)
    public void addNewestName(BaseNameRecord newestName) {
        if (newestName != null && !this.newestName.contains(newestName)) {
            newestName.setMetadataRecord(this);
            this.newestName.add(newestName);
        }
    }

    @JsonIgnore
    public Set<BaseNameRecord> getNewestName() {
        return this.newestName;
    }

    @JsonGetter(IO_FIELD_NEWEST_NAME)
    public BaseNameRecord getLatestNewestName() {
        BaseNameRecord latest = null;
        for (BaseNameRecord nameRecord : this.newestName) {
            if (latest == null || nameRecord.getLastUpdated().isAfter(latest.getLastUpdated())) {
                latest = nameRecord;
            }
        }
        return latest;
    }





    public static final String DB_FIELD_NEWEST_LOCATION = "newestLocation";
    public static final String IO_FIELD_NEWEST_LOCATION = "nyesteBeliggenhedsadresse";

    @OneToMany(targetEntity = AddressRecord.class, mappedBy = AddressRecord.DB_FIELD_COMPANY_METADATA, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(value = IO_FIELD_NEWEST_LOCATION)
    private Set<AddressRecord> newestLocation = new HashSet<>();

    public void setNewestLocation(Set<AddressRecord> newestLocation) {
        this.newestLocation = newestLocation;
    }

    @JsonSetter(IO_FIELD_NEWEST_LOCATION)
    public void addNewestLocation(AddressRecord newestLocation) {
        if (newestLocation != null && !this.newestLocation.contains(newestLocation)) {
            newestLocation.setMetadataRecord(this);
            this.newestLocation.add(newestLocation);
        }
    }

    @JsonIgnore
    public Set<AddressRecord> getNewestLocation() {
        return this.newestLocation;
    }

    @JsonGetter(IO_FIELD_NEWEST_LOCATION)
    public AddressRecord getLatestNewestLocation() {
        AddressRecord latest = null;
        for (AddressRecord nameRecord : this.newestLocation) {
            if (latest == null || nameRecord.getLastUpdated().isAfter(latest.getLastUpdated())) {
                latest = nameRecord;
            }
        }
        return latest;
    }



    public static final String DB_FIELD_NEWEST_PRIMARY_INDUSTRY = "newestPrimaryIndustry";
    public static final String IO_FIELD_NEWEST_PRIMARY_INDUSTRY = "nyesteHovedbranche";

    @OneToMany(targetEntity = CompanyIndustryRecord.class, mappedBy = CompanyIndustryRecord.DB_FIELD_COMPANY_METADATA, cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = CompanyIndustryRecord.DB_FIELD_INDEX+"=0")
    @JsonProperty(value = IO_FIELD_NEWEST_PRIMARY_INDUSTRY)
    private Set<CompanyIndustryRecord> newestPrimaryIndustry = new HashSet<>();

    public void setNewestPrimaryIndustry(Set<CompanyIndustryRecord> newestPrimaryIndustry) {
        this.newestPrimaryIndustry = newestPrimaryIndustry;
    }

    @JsonSetter(IO_FIELD_NEWEST_PRIMARY_INDUSTRY)
    public void addNewestPrimaryIndustry(CompanyIndustryRecord newestPrimaryIndustry) {
        if (newestPrimaryIndustry != null && !this.newestPrimaryIndustry.contains(newestPrimaryIndustry)) {
            newestPrimaryIndustry.setMetadataRecord(this);
            newestPrimaryIndustry.setIndex(0);
            this.newestPrimaryIndustry.add(newestPrimaryIndustry);
        }
    }

    @JsonIgnore
    public Set<CompanyIndustryRecord> getNewestPrimaryIndustry() {
        return this.newestPrimaryIndustry;
    }

    @JsonGetter(IO_FIELD_NEWEST_PRIMARY_INDUSTRY)
    public CompanyIndustryRecord getLatestNewestPrimaryIndustry() {
        CompanyIndustryRecord latest = null;
        for (CompanyIndustryRecord industryRecord : this.newestPrimaryIndustry) {
            if (latest == null || industryRecord.getLastUpdated().isAfter(latest.getLastUpdated())) {
                latest = industryRecord;
            }
        }
        return latest;
    }






    public static final String DB_FIELD_NEWEST_SECONDARY_INDUSTRY1 = "newestSecondaryIndustry1";
    public static final String IO_FIELD_NEWEST_SECONDARY_INDUSTRY1 = "nyesteBibranche1";

    @OneToMany(targetEntity = CompanyIndustryRecord.class, mappedBy = CompanyIndustryRecord.DB_FIELD_COMPANY_METADATA, cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = CompanyIndustryRecord.DB_FIELD_INDEX+"=1")
    @JsonProperty(value = IO_FIELD_NEWEST_SECONDARY_INDUSTRY1)
    private Set<CompanyIndustryRecord> newestSecondaryIndustry1 = new HashSet<>();

    public void setNewestSecondaryIndustry1(Set<CompanyIndustryRecord> newestSecondaryIndustry1) {
        this.newestSecondaryIndustry1 = newestSecondaryIndustry1;
    }

    @JsonSetter(IO_FIELD_NEWEST_SECONDARY_INDUSTRY1)
    public void addNewestSecondaryIndustry1(CompanyIndustryRecord newestSecondaryIndustry1) {
        if (newestSecondaryIndustry1 != null && !this.newestSecondaryIndustry1.contains(newestSecondaryIndustry1)) {
            newestSecondaryIndustry1.setMetadataRecord(this);
            newestSecondaryIndustry1.setIndex(1);
            this.newestSecondaryIndustry1.add(newestSecondaryIndustry1);
        }
    }

    @JsonIgnore
    public Set<CompanyIndustryRecord> getNewestSecondaryIndustry1() {
        return this.newestSecondaryIndustry1;
    }

    @JsonGetter(IO_FIELD_NEWEST_SECONDARY_INDUSTRY1)
    public CompanyIndustryRecord getLatestNewestSecondaryIndustry1() {
        CompanyIndustryRecord latest = null;
        for (CompanyIndustryRecord industryRecord : this.newestSecondaryIndustry1) {
            if (latest == null || industryRecord.getLastUpdated().isAfter(latest.getLastUpdated())) {
                latest = industryRecord;
            }
        }
        return latest;
    }







    public static final String DB_FIELD_NEWEST_SECONDARY_INDUSTRY2 = "newestSecondaryIndustry2";
    public static final String IO_FIELD_NEWEST_SECONDARY_INDUSTRY2 = "nyesteBibranche2";

    @OneToMany(targetEntity = CompanyIndustryRecord.class, mappedBy = CompanyIndustryRecord.DB_FIELD_COMPANY_METADATA, cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = CompanyIndustryRecord.DB_FIELD_INDEX+"=2")
    @JsonProperty(value = IO_FIELD_NEWEST_SECONDARY_INDUSTRY2)
    private Set<CompanyIndustryRecord> newestSecondaryIndustry2 = new HashSet<>();

    public void setNewestSecondaryIndustry2(Set<CompanyIndustryRecord> newestSecondaryIndustry2) {
        this.newestSecondaryIndustry2 = newestSecondaryIndustry2;
    }

    @JsonSetter(IO_FIELD_NEWEST_SECONDARY_INDUSTRY2)
    public void addNewestSecondaryIndustry2(CompanyIndustryRecord newestSecondaryIndustry2) {
        if (newestSecondaryIndustry2 != null && !this.newestSecondaryIndustry2.contains(newestSecondaryIndustry2)) {
            newestSecondaryIndustry2.setMetadataRecord(this);
            newestSecondaryIndustry2.setIndex(2);
            this.newestSecondaryIndustry2.add(newestSecondaryIndustry2);
        }
    }

    @JsonIgnore
    public Set<CompanyIndustryRecord> getNewestSecondaryIndustry2() {
        return this.newestSecondaryIndustry2;
    }

    @JsonGetter(IO_FIELD_NEWEST_SECONDARY_INDUSTRY2)
    public CompanyIndustryRecord getLatestNewestSecondaryIndustry2() {
        CompanyIndustryRecord latest = null;
        for (CompanyIndustryRecord industryRecord : this.newestSecondaryIndustry2) {
            if (latest == null || industryRecord.getLastUpdated().isAfter(latest.getLastUpdated())) {
                latest = industryRecord;
            }
        }
        return latest;
    }






    public static final String DB_FIELD_NEWEST_SECONDARY_INDUSTRY3 = "newestSecondaryIndustry3";
    public static final String IO_FIELD_NEWEST_SECONDARY_INDUSTRY3 = "nyesteBibranche3";

    @OneToMany(targetEntity = CompanyIndustryRecord.class, mappedBy = CompanyIndustryRecord.DB_FIELD_COMPANY_METADATA, cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = CompanyIndustryRecord.DB_FIELD_INDEX+"=3")
    @JsonProperty(value = IO_FIELD_NEWEST_SECONDARY_INDUSTRY3)
    private Set<CompanyIndustryRecord> newestSecondaryIndustry3 = new HashSet<>();

    public void setNewestSecondaryIndustry3(Set<CompanyIndustryRecord> newestSecondaryIndustry3) {
        this.newestSecondaryIndustry3 = newestSecondaryIndustry3;
    }

    @JsonSetter(IO_FIELD_NEWEST_SECONDARY_INDUSTRY3)
    public void addNewestSecondaryIndustry3(CompanyIndustryRecord newestSecondaryIndustry3) {
        if (newestSecondaryIndustry3 != null && !this.newestSecondaryIndustry3.contains(newestSecondaryIndustry3)) {
            newestSecondaryIndustry3.setMetadataRecord(this);
            newestSecondaryIndustry3.setIndex(3);
            this.newestSecondaryIndustry3.add(newestSecondaryIndustry3);
        }
    }

    @JsonIgnore
    public Set<CompanyIndustryRecord> getNewestSecondaryIndustry3() {
        return this.newestSecondaryIndustry3;
    }

    @JsonGetter(IO_FIELD_NEWEST_SECONDARY_INDUSTRY3)
    public CompanyIndustryRecord getLatestNewestSecondaryIndustry3() {
        CompanyIndustryRecord latest = null;
        for (CompanyIndustryRecord industryRecord : this.newestSecondaryIndustry3) {
            if (latest == null || industryRecord.getLastUpdated().isAfter(latest.getLastUpdated())) {
                latest = industryRecord;
            }
        }
        return latest;
    }



    // Apparently a recent addition to metadata, but it's an empty array in all entries
    public static final String DB_FIELD_NEWEST_FAD_CPR = "newestFadCpr";
    public static final String IO_FIELD_NEWEST_FAD_CPR = "nyesteFadCprnumre";

    @Transient
    @JsonProperty(value = IO_FIELD_NEWEST_FAD_CPR)
    private Set<String> newestFadCpr = new HashSet<>();



    public static final String DB_FIELD_NEWEST_STATUS = "newestStatus";
    public static final String IO_FIELD_NEWEST_STATUS = "nyesteStatus";

    @OneToOne(targetEntity = StatusRecord.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(value = IO_FIELD_NEWEST_STATUS)
    private StatusRecord newestStatus;



    public static final String DB_FIELD_UNIT_COUNT = "unitCount";
    public static final String IO_FIELD_UNIT_COUNT = "antalPenheder";

    @Column(name = DB_FIELD_UNIT_COUNT)
    @JsonProperty(value = IO_FIELD_UNIT_COUNT)
    private int unitCount;



    public static final String DB_FIELD_FOUNDING_DATE = "foundingDate";
    public static final String IO_FIELD_FOUNDING_DATE = "stiftelsesDato";

    @Column(name = DB_FIELD_FOUNDING_DATE)
    @JsonProperty(value = IO_FIELD_FOUNDING_DATE)
    private LocalDate foundingDate;



    public static final String DB_FIELD_EFFECT_DATE = "effectDate";
    public static final String IO_FIELD_EFFECT_DATE = "virkningsDato";

    @Column(name = DB_FIELD_EFFECT_DATE)
    @JsonProperty(value = IO_FIELD_EFFECT_DATE)
    private LocalDate effectDate;



    @OneToMany(targetEntity = MetadataContactRecord.class, mappedBy = MetadataContactRecord.DB_FIELD_COMPANY_METADATA, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MetadataContactRecord> metadataContactRecords = new HashSet<>();

    public Set<MetadataContactRecord> getMetadataContactRecords() {
        return this.metadataContactRecords;
    }

    public void setMetadataContactRecords(Set<MetadataContactRecord> metadataContactRecords) {
        this.metadataContactRecords = metadataContactRecords;
        for (MetadataContactRecord metadataContactRecord : metadataContactRecords) {
            metadataContactRecord.setCompanyMetadataRecord(this);
        }
    }





    @Override
    public void wire(Session session) {
        super.wire(session);
        for (FormRecord formRecord : this.newestForm) {
            formRecord.wire(session);
        }
        for (AddressRecord addressRecord : this.newestLocation) {
            addressRecord.wire(session);
        }
    }

    @Override
    public boolean merge(MetadataRecord other) {
        if (other != null && !Objects.equals(this.getId(), other.getId()) && other instanceof CompanyMetadataRecord) {
            CompanyMetadataRecord existing = (CompanyMetadataRecord) other;
            for (BaseNameRecord nameRecord : existing.getNewestName()) {
                this.addNewestName(nameRecord);
            }
            for (FormRecord formRecord : existing.getNewestForm()) {
                this.addNewestForm(formRecord);
            }
            for (AddressRecord addressRecord : existing.getNewestLocation()) {
                this.addNewestLocation(addressRecord);
            }
            for (CompanyIndustryRecord industryRecord : existing.getNewestPrimaryIndustry()) {
                this.addNewestPrimaryIndustry(industryRecord);
            }
            for (CompanyIndustryRecord industryRecord : existing.getNewestSecondaryIndustry1()) {
                this.addNewestSecondaryIndustry1(industryRecord);
            }
            for (CompanyIndustryRecord industryRecord : existing.getNewestSecondaryIndustry2()) {
                this.addNewestSecondaryIndustry2(industryRecord);
            }
            for (CompanyIndustryRecord industryRecord : existing.getNewestSecondaryIndustry3()) {
                this.addNewestSecondaryIndustry3(industryRecord);
            }
            return true;
        }
        return false;
    }
}
