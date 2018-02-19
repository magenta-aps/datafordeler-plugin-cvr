package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cvr_record_metadata_participant", indexes = {
        @Index(name = "cvr_record_metadata_participant", columnList = ParticipantMetadataRecord.DB_FIELD_PARTICIPANT + DatabaseEntry.REF),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParticipantMetadataRecord extends CvrBitemporalDataRecord {



    public static final String DB_FIELD_NEWEST_LOCATION = "newestLocation";
    public static final String IO_FIELD_NEWEST_LOCATION = "nyesteBeliggenhedsadresse";

    @OneToOne(targetEntity = AddressRecord.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(value = IO_FIELD_NEWEST_LOCATION)
    private AddressRecord newestLocation;



    public static final String DB_FIELD_NEWEST_CONTACT_DATA = "newestContactData";
    public static final String IO_FIELD_NEWEST_CONTACT_DATA = "nyesteKontaktoplysninger";

    @OneToMany(targetEntity = MetadataContactRecord.class, mappedBy = MetadataContactRecord.DB_FIELD_COMPANY_METADATA, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MetadataContactRecord> metadataContactRecords = new HashSet<>();

    @JsonProperty(IO_FIELD_NEWEST_CONTACT_DATA)
    private void setMetadataContactData(Set<String> contactData) {
        HashSet<String> contacts = new HashSet<>(contactData);
        HashSet<MetadataContactRecord> remove = new HashSet<>();
        for (MetadataContactRecord contactRecord : this.metadataContactRecords) {
            String data = contactRecord.getData();
            if (contacts.contains(data)) {
                contacts.remove(data);
            } else {
                remove.add(contactRecord);
            }
        }
        this.metadataContactRecords.removeAll(remove);
        for (String data : contacts) {
            MetadataContactRecord newContactRecord = new MetadataContactRecord();
            newContactRecord.setData(data);
            newContactRecord.setParticipantMetadataRecord(this);
            this.metadataContactRecords.add(newContactRecord);
        }
    }

    @JsonProperty(IO_FIELD_NEWEST_CONTACT_DATA)
    private Set<String> getMetadataContactData() {
        HashSet<String> contacts = new HashSet<>();
        for (MetadataContactRecord metadataContactRecord : this.metadataContactRecords) {
            contacts.add(metadataContactRecord.getData());
        }
        return contacts;
    }

    public void wire(Session session) {
        if (this.newestLocation != null) {
            this.newestLocation.wire(session);
        }
    }

}
