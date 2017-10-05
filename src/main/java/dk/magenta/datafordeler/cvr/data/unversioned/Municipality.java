package dk.magenta.datafordeler.cvr.data.unversioned;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import org.hibernate.Session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import java.util.Collections;

import static dk.magenta.datafordeler.cvr.data.unversioned.Municipality.DB_FIELD_CODE;

/**
 * Created by lars on 26-01-15.
 */
@Entity
@Table(name = "cvr_municipality", indexes = {
        @Index(name = "companyMunicipalityCode", columnList = DB_FIELD_CODE)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Municipality extends UnversionedEntity {

    public static final String DB_FIELD_CODE = "code";
    public static final String IO_FIELD_CODE = "kommuneKode";

    @JsonProperty(value = IO_FIELD_CODE)
    @XmlElement(name = IO_FIELD_CODE)
    @Column(nullable = false, unique = true)
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_NAME = "name";
    public static final String IO_FIELD_NAME = "kommuneNavn";

    @JsonProperty(value = IO_FIELD_NAME)
    @XmlElement(name = IO_FIELD_NAME)
    @Column(nullable = false)
    private String name;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //----------------------------------------------------

    public static Municipality getMunicipality(String code, String name, Session session) {
        if (code != null) {
            Municipality municipality = QueryManager.getItem(session, Municipality.class, Collections.singletonMap(DB_FIELD_CODE, code));
            if (municipality == null) {
                municipality = new Municipality();
                municipality.setCode(code);
                municipality.setName(name);
                session.save(municipality);
            }
            return municipality;
        } else {
            return null;
        }
    }

    public static Municipality getMunicipality(Municipality old, Session session) {
        return getMunicipality(old.getCode(), old.getName(), session);
    }
}
