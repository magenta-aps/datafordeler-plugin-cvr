package dk.magenta.datafordeler.cvr.data.participant;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.magenta.datafordeler.cvr.data.shared.SingleData;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 16-05-17.
 */
@Entity
@Table(name = "cvr_companyunit_company")
public class ParticipantCvrData extends SingleData<Integer> {

    public ParticipantCvrData() {
    }

    public ParticipantCvrData(int cvr) {
        this.setVaerdi(cvr);
    }

    @JsonAnyGetter
    public Map<String, Object> asMap() {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put("cvrNummer", this.getVaerdi());
        return fields;
    }

    /**
     * Return a map of attributes, including those from the superclass
     * @return
     */
    @JsonIgnore
    public Map<String, Object> databaseFields() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("data", this.getVaerdi());
        return map;
    }

}
