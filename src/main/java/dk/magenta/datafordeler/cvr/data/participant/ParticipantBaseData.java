package dk.magenta.datafordeler.cvr.data.participant;

import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.cvr.data.shared.CompanyTextData;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@javax.persistence.Entity
@Table(name="cvr_participant_data", indexes = {
        /*@Index(name = "cvrNumber", columnList = "cvrNumber"),
        @Index(name = "type", columnList = "type"),
        @Index(name = "role", columnList = "role"),
        @Index(name = "status", columnList = "status")*/})
public class ParticipantBaseData extends DataItem<ParticipantEffect, ParticipantBaseData> {

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyTextData nameData;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private ParticipantCvrData cvrData;

    @ManyToOne(optional = true)
    private ParticipantType type;

    @ManyToOne(optional = true)
    private ParticipantRole role;

    @ManyToOne(optional = true)
    private ParticipantStatus status;


    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", this.nameData.getData());
        map.put("cvrNumber", this.cvrData.getData());
        map.put("type", this.type.getName());
        map.put("role", this.role.getName());
        map.put("status", this.status.getName());
        return map;
    }


    public void setName(String name) {
        if (this.nameData == null) {
            this.nameData = new CompanyTextData(CompanyTextData.Type.NAME);
        }
        this.nameData.setData(name);
    }

    public void setCvrNumber(int cvrNumber) {
        if (this.cvrData == null) {
            this.cvrData = new ParticipantCvrData();
        }
        this.cvrData.setData(cvrNumber);
    }

    public void setType(ParticipantType type) {
        this.type = type;
    }

    public void setRole(ParticipantRole role) {
        this.role = role;
    }

    public void setStatus(ParticipantStatus status) {
        this.status = status;
    }


    @Override
    public LookupDefinition getLookupDefinition() {
        LookupDefinition lookupDefinition = new LookupDefinition();
        if (this.nameData != null) {
            lookupDefinition.putAll("nameData", this.nameData.databaseFields());
        }
        if (this.cvrData != null) {
            lookupDefinition.putAll("cvrData", this.cvrData.databaseFields());
        }
        if (this.status != null) {
            lookupDefinition.putAll("status", this.status.databaseFields());
        }

        return lookupDefinition;
    }
}
