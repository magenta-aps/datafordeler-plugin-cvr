package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import dk.magenta.datafordeler.core.database.DatabaseEntry;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cvr_record_unit_metadata", indexes = {
        @Index(name = "cvr_record_metadata_unit", columnList = MetadataRecord.DB_FIELD_COMPANYUNIT + DatabaseEntry.REF),
})
public class CompanyUnitMetadataRecord extends MetadataRecord {

    public static final String DB_FIELD_NEWEST_CVR_RELATION = "newestCvrRelation";
    public static final String IO_FIELD_NEWEST_CVR_RELATION = "nyesteCvrNummerRelation";

    @Column
    @JsonProperty(value = IO_FIELD_NEWEST_CVR_RELATION)
    private int newestCvrRelation;

    public int getNewestCvrRelation() {
        return this.newestCvrRelation;
    }

    public void setNewestCvrRelation(int newestCvrRelation) {
        this.newestCvrRelation = newestCvrRelation;
    }


    @OneToMany(targetEntity = MetadataContactRecord.class, mappedBy = MetadataContactRecord.DB_FIELD_UNIT_METADATA, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MetadataContactRecord> metadataContactRecords = new HashSet<>();

    public Set<MetadataContactRecord> getMetadataContactRecords() {
        return this.metadataContactRecords;
    }

    public void setMetadataContactRecords(Set<MetadataContactRecord> metadataContactRecords) {
        this.metadataContactRecords = metadataContactRecords;
        for (MetadataContactRecord metadataContactRecord : metadataContactRecords) {
            metadataContactRecord.setUnitMetadataRecord(this);
        }
    }


    public static final String DB_FIELD_NEWEST_NAME = "newestName";
    public static final String IO_FIELD_NEWEST_NAME = "nyesteNavn";

    @OneToMany(targetEntity = NameRecord.class, mappedBy = NameRecord.DB_FIELD_UNIT_METADATA, cascade = CascadeType.ALL, orphanRemoval = true)
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

    @Override
    public boolean merge(MetadataRecord other) {
        if (other != null && !other.getId().equals(this.getId()) && other instanceof CompanyUnitMetadataRecord) {
            CompanyUnitMetadataRecord existing = (CompanyUnitMetadataRecord) other;
            for (NameRecord nameRecord : this.getNewestName()) {
                existing.addNewestName(nameRecord);
            }
            return true;
        }
        return false;
    }
}
