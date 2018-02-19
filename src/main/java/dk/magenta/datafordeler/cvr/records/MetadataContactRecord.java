package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dk.magenta.datafordeler.core.database.DatabaseEntry;

import javax.persistence.*;

@Entity
@Table(name = "cvr_record_metadata_contact", indexes = {
        @Index(name = "cvr_record_metadata_contact_metadata", columnList = MetadataContactRecord.DB_FIELD_METADATA + DatabaseEntry.REF)
})
public class MetadataContactRecord extends CvrNontemporalRecord {

    public static final String DB_FIELD_DATA = "data";

    @Column(name = DB_FIELD_DATA)
    private String data;

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public static final String DB_FIELD_METADATA = "metadataRecord";

    @ManyToOne(targetEntity = MetadataRecord.class)
    @JoinColumn(name = DB_FIELD_METADATA + DatabaseEntry.REF)
    private MetadataRecord metadataRecord;

    public void setMetadataRecord(MetadataRecord metadataRecord) {
        this.metadataRecord = metadataRecord;
    }

    public static final String DB_FIELD_PARTICIPANT_METADATA = "participantMetadataRecord";

    @ManyToOne(targetEntity = ParticipantMetadataRecord.class)
    @JoinColumn(name = DB_FIELD_PARTICIPANT_METADATA + DatabaseEntry.REF)
    private ParticipantMetadataRecord participantMetadataRecord;

    public void setParticipantMetadataRecord(ParticipantMetadataRecord participantMetadataRecord) {
        this.participantMetadataRecord = participantMetadataRecord;
    }
}
