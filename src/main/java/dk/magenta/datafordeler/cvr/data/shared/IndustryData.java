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
    public IndustryData(boolean isPrimaer) {
        this.isPrimaer = isPrimaer;
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
    private boolean isPrimaer;


    public boolean isPrimaer() {
        return this.isPrimaer;
    }

    public void setPrimaer(boolean primaer) {
        isPrimaer = primaer;
    }



    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("branche", this.branche);
        map.put("isPrimaer", this.isPrimaer);
        return map;
    }

}
