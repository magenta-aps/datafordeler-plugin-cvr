package dk.magenta.datafordeler.cvr.data.shared;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.DetailData;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by lars on 16-05-17.
 */
@MappedSuperclass
public abstract class SingleData<T> extends DetailData {

    @Column
    @JsonProperty(value = "vaerdi")
    @XmlElement(name = "vaerdi")
    private T vaerdi;

    public T getVaerdi() {
        return this.vaerdi;
    }

    public void setVaerdi(T vaerdi) {
        this.vaerdi = vaerdi;
    }

}
