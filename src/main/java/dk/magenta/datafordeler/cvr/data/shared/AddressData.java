package dk.magenta.datafordeler.cvr.data.shared;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.DetailData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 09-06-17.
 */
@Entity
@Table(name="cvr_company_address")
public class AddressData extends DetailData {

    @ManyToOne
    @JsonProperty
    @XmlElement
    @Cascade(value = CascadeType.ALL)
    private Address adresse;

    public Address getAdresse() {
        return this.adresse;
    }

    public void setAdresse(Address adresse) {
        this.adresse = adresse;
    }

    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("adresse", this.adresse);
        return map;
    }

}
