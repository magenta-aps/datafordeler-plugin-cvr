package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Objects;

/**
 * Record for Company, CompanyUnit or Participant name.
 */
@Entity
@Table(name = "cvr_record_name", indexes = {
        @Index(name = "cvr_record_name_company", columnList = NameRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
        @Index(name = "cvr_record_name_companyunit", columnList = NameRecord.DB_FIELD_COMPANYUNIT + DatabaseEntry.REF),
        @Index(name = "cvr_record_name_participant", columnList = NameRecord.DB_FIELD_PARTICIPANT + DatabaseEntry.REF),
        @Index(name = "cvr_record_name_companymetadata", columnList = NameRecord.DB_FIELD_COMPANY_METADATA + DatabaseEntry.REF),
        @Index(name = "cvr_record_name_unitmetadata", columnList = NameRecord.DB_FIELD_UNIT_METADATA + DatabaseEntry.REF),
        @Index(name = "cvr_record_name_participantrelation", columnList = NameRecord.DB_FIELD_PARTICIPANT_RELATION + DatabaseEntry.REF),
        @Index(name = "cvr_record_name_data", columnList = NameRecord.DB_FIELD_NAME),
})
public class NameRecord extends CvrBitemporalDataMetaRecord {

    public static final String DB_FIELD_NAME = "name";
    public static final String IO_FIELD_NAME = "navn";

    @Column(name = DB_FIELD_NAME, length = 8000)
    @JsonProperty(value = IO_FIELD_NAME)
    private String name;

    public String getName() {
        return this.name;
    }




    public static final String DB_FIELD_SECONDARY = "secondary";

    @Column(name = DB_FIELD_SECONDARY)
    @JsonIgnore
    private boolean secondary;

    public void setSecondary(boolean secondary) {
        this.secondary = secondary;
    }



    public static final String DB_FIELD_PARTICIPANT_RELATION = "participantRelationRecord";

    @ManyToOne(targetEntity = ParticipantRelationRecord.class)
    @JoinColumn(name = DB_FIELD_PARTICIPANT_RELATION + DatabaseEntry.REF)
    @JsonIgnore
    private ParticipantRelationRecord participantRelationRecord;

    public void setParticipantRelationRecord(ParticipantRelationRecord participantRelationRecord) {
        this.participantRelationRecord = participantRelationRecord;
    }



    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        baseData.setCompanyName(this.name);
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, Session session) {
        baseData.setName(this.name);
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, Session session) {
        baseData.addName(this.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NameRecord that = (NameRecord) o;
        return secondary == that.secondary &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, secondary);
    }
}
