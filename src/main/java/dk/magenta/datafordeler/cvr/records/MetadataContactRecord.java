package dk.magenta.datafordeler.cvr.records;

import dk.magenta.datafordeler.core.database.DatabaseEntry;

import javax.persistence.*;

@Entity
@Table(name = "cvr_record_metadata_contact", indexes = {
        @Index(name = "cvr_record_metadata_contact_companymetadata", columnList = MetadataContactRecord.DB_FIELD_COMPANY_METADATA + DatabaseEntry.REF),
        @Index(name = "cvr_record_metadata_contact_unitmetadata", columnList = MetadataContactRecord.DB_FIELD_UNIT_METADATA + DatabaseEntry.REF),
        @Index(name = "cvr_record_metadata_contact_participantmetadata", columnList = MetadataContactRecord.DB_FIELD_PARTICIPANT_METADATA + DatabaseEntry.REF)
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


    public static final String DB_FIELD_COMPANY_METADATA = "companyMetadataRecord";

    @ManyToOne(targetEntity = CompanyMetadataRecord.class)
    @JoinColumn(name = DB_FIELD_COMPANY_METADATA + DatabaseEntry.REF)
    private MetadataRecord companyMetadataRecord;

    public void setCompanyMetadataRecord(CompanyMetadataRecord companyMetadataRecord) {
        this.companyMetadataRecord = companyMetadataRecord;
    }



    public static final String DB_FIELD_UNIT_METADATA = "unitMetadataRecord";

    @ManyToOne(targetEntity = CompanyUnitMetadataRecord.class)
    @JoinColumn(name = DB_FIELD_UNIT_METADATA + DatabaseEntry.REF)
    private MetadataRecord unitMetadataRecord;

    public void setUnitMetadataRecord(CompanyUnitMetadataRecord unitMetadataRecord) {
        this.unitMetadataRecord = unitMetadataRecord;
    }



    public static final String DB_FIELD_PARTICIPANT_METADATA = "participantMetadataRecord";

    @ManyToOne(targetEntity = ParticipantMetadataRecord.class)
    @JoinColumn(name = DB_FIELD_PARTICIPANT_METADATA + DatabaseEntry.REF)
    private ParticipantMetadataRecord participantMetadataRecord;

    public void setParticipantMetadataRecord(ParticipantMetadataRecord participantMetadataRecord) {
        this.participantMetadataRecord = participantMetadataRecord;
    }
}
