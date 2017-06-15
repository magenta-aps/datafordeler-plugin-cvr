package dk.magenta.datafordeler.cvr.data.unversioned;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Created by lars on 26-01-15.
 */
@Entity
@Table(name = "cvr_municipality", indexes = {@Index(name = "code", columnList = "code")})
public class Municipality extends UnversionedEntity {



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
    @Column(nullable = false, unique = true)
    private String text;

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

}