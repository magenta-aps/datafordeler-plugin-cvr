package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.magenta.datafordeler.cvr.data.DetailData;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
/**
 * Created by lars on 16-05-17.
 */
@MappedSuperclass
public abstract class CompanySingleData<T> extends DetailData {

    @Column
    @JsonIgnore
    private T data;

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
