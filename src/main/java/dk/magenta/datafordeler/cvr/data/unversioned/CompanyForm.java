package dk.magenta.datafordeler.cvr.data.unversioned;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.unversioned.UnversionedEntity;

import javax.persistence.*;

/**
 * Created by lars on 26-01-15.
 */
@Entity
@Table(name = "cvr_form", indexes = {@Index(name = "code", columnList = "code")})
public class CompanyForm extends UnversionedEntity {

    public CompanyForm() {
    }

    //----------------------------------------------------

    @JsonProperty
    @Column(nullable = true, unique = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //----------------------------------------------------

    @JsonProperty
    @Column(nullable = false, unique = true)
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @JsonProperty
    @Column
    private String responsibleDatasource;

    public String getResponsibleDatasource() {
        return responsibleDatasource;
    }

    public void setResponsibleDatasource(String responsibleDatasource) {
        this.responsibleDatasource = responsibleDatasource;
    }

    //----------------------------------------------------
/*
    public boolean equals(Object otherObject) {
        if (otherObject == null || otherObject.getClass() != CompanyFormEntity.class) {
            return false;
        }
        return this.equals((CompanyFormEntity) otherObject);
    }

    public boolean equals(CompanyFormEntity otherCompanyFormEntity) {
        return this.code == otherCompanyFormEntity.getCode() &&
                Util.compare(this.name, otherCompanyFormEntity.getName()) &&
                Util.compare(this.responsibleDatasource, otherCompanyFormEntity.getResponsibleDatasource());
    }*/
}
