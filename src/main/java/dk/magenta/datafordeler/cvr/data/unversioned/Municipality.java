package dk.magenta.datafordeler.cvr.data.unversioned;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import org.hibernate.Session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Collections;

/**
 * Created by lars on 26-01-15.
 */
@Entity
@Table(name = "cvr_municipality", indexes = {
        @Index(name = "companyMunicipalityCode", columnList = "code")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Municipality extends UnversionedEntity {

    @JsonProperty(value = "kommuneKode")
    @Column(nullable = false, unique = true)
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }



    @JsonProperty(value = "kommuneNavn")
    @Column(nullable = false)
    private String text;

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public static Municipality getMunicipality(int code, String text, QueryManager queryManager, Session session) {
        if (code != 0) {
            Municipality municipality = queryManager.getItem(session, Municipality.class, Collections.singletonMap("code", code));
            if (municipality == null) {
                municipality = new Municipality();
                municipality.setCode(code);
                municipality.setText(text);
                session.save(municipality);
            }
            return municipality;
        } else {
            return null;
        }
    }

    public static Municipality getMunicipality(Municipality old, QueryManager queryManager, Session session) {
        return getMunicipality(old.getCode(), old.getText(), queryManager, session);
    }
}
