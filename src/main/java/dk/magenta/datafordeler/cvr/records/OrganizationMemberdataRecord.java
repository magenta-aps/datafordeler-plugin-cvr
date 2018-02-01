package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OrganizationMemberdataRecord {

    @JsonProperty(value = "attributter")
    public List<AttributeRecord> attributes;

}
