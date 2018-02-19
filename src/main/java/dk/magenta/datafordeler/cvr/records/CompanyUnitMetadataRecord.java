package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
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
}
