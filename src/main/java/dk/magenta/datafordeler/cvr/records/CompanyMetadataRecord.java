package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.*;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import org.hibernate.Session;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cvr_record_metadata", indexes = {
        @Index(name = "cvr_record_metadata_company", columnList = MetadataRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyMetadataRecord extends MetadataRecord {


    public static final String DB_FIELD_NEWEST_FORM = "newestForm";
    public static final String IO_FIELD_NEWEST_FORM = "nyesteVirksomhedsform";

    @OneToMany(mappedBy = CompanyFormRecord.DB_FIELD_COMPANY_METADATA, targetEntity = CompanyFormRecord.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CompanyFormRecord> newestForm = new HashSet<>();

    public void setNewestForm(Set<CompanyFormRecord> newestForm) {
        this.newestForm = newestForm;
    }

    @JsonSetter(IO_FIELD_NEWEST_FORM)
    public void addNewestForm(CompanyFormRecord newestForm) {
        if (!this.newestForm.contains(newestForm)) {
            newestForm.setCompanyMetadataRecord(this);
            this.newestForm.add(newestForm);
        }
    }

    @JsonIgnore
    public Set<CompanyFormRecord> getNewestForm() {
        return this.newestForm;
    }

    @JsonGetter(IO_FIELD_NEWEST_FORM)
    public CompanyFormRecord getLatestNewestForm() {
        CompanyFormRecord latest = null;
        for (CompanyFormRecord companyFormRecord : this.newestForm) {
            if (latest == null || companyFormRecord.getLastUpdated().isAfter(latest.getLastUpdated())) {
                latest = companyFormRecord;
            }
        }
        return latest;
    }





    public static final String DB_FIELD_NEWEST_NAME = "newestName";
    public static final String IO_FIELD_NEWEST_NAME = "nyesteNavn";

    @OneToMany(targetEntity = NameRecord.class, mappedBy = NameRecord.DB_FIELD_COMPANY_METADATA, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(value = IO_FIELD_NEWEST_NAME)
    private Set<NameRecord> newestName = new HashSet<>();

    public void setNewestName(Set<NameRecord> newestName) {
        this.newestName = newestName;
    }

    @JsonSetter(IO_FIELD_NEWEST_NAME)
    public void addNewestName(NameRecord newestName) {
        if (!this.newestName.contains(newestName)) {
            newestName.setMetadataRecord(this);
            this.newestName.add(newestName);
        }
    }

    @JsonIgnore
    public Set<NameRecord> getNewestName() {
        return this.newestName;
    }

    @JsonGetter(IO_FIELD_NEWEST_NAME)
    public NameRecord getLatestNewestName() {
        NameRecord latest = null;
        for (NameRecord nameRecord : this.newestName) {
            if (latest == null || nameRecord.getLastUpdated().isAfter(latest.getLastUpdated())) {
                latest = nameRecord;
            }
        }
        return latest;
    }





    public static final String DB_FIELD_NEWEST_LOCATION = "newestLocation";
    public static final String IO_FIELD_NEWEST_LOCATION = "nyesteBeliggenhedsadresse";

    @OneToMany(targetEntity = AddressRecord.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(value = IO_FIELD_NEWEST_LOCATION)
    private Set<AddressRecord> newestLocation = new HashSet<>();

    public void setNewestLocation(Set<AddressRecord> newestLocation) {
        this.newestLocation = newestLocation;
    }

    @JsonSetter(IO_FIELD_NEWEST_LOCATION)
    public void addNewestLocation(AddressRecord newestLocation) {
        if (!this.newestLocation.contains(newestLocation)) {
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
        for (CompanyFormRecord companyFormRecord : this.newestForm) {
            companyFormRecord.wire(session);
        }
        for (AddressRecord addressRecord : this.newestLocation) {
            addressRecord.wire(session);
        }
    }

    @Override
    public boolean merge(MetadataRecord other) {
        if (other != null && !other.getId().equals(this.getId()) && other instanceof CompanyMetadataRecord) {
            CompanyMetadataRecord existing = (CompanyMetadataRecord) other;
            for (NameRecord nameRecord : this.getNewestName()) {
                existing.addNewestName(nameRecord);
            }
            for (CompanyFormRecord formRecord : this.getNewestForm()) {
                existing.addNewestForm(formRecord);
            }
            for (AddressRecord addressRecord : this.getNewestLocation()) {
                existing.addNewestLocation(addressRecord);
            }
            return true;
        }
        return false;
    }
}
