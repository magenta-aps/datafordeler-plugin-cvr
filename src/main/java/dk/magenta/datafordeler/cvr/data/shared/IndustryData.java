package dk.magenta.datafordeler.cvr.data.shared;

import dk.magenta.datafordeler.cvr.data.DetailData;
import dk.magenta.datafordeler.cvr.data.unversioned.Industry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 09-06-17.
 */
@Entity
@Table(name="cvr_industrylink")
public class IndustryData extends DetailData {

    public IndustryData() {
        this(false);
    }
    public IndustryData(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }



    @ManyToOne
    private Industry branche;

    public Industry getBranche() {
        return this.branche;
    }

    public void setBranche(Industry branche) {
        this.branche = branche;
    }



    @Column
    private boolean isPrimary;


    public boolean isPrimary() {
        return this.isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }



    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("branche", this.branche);
        map.put("isPrimary", this.isPrimary);
        return map;
    }

}
