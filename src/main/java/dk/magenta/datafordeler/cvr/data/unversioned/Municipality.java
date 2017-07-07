package dk.magenta.datafordeler.cvr.data.unversioned;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
        @Index(name = "companyMunicipalityCode", columnList = "kommunekode")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Municipality extends UnversionedEntity {

    @JsonProperty(value = "kommunekode")
    @Column(nullable = false, unique = true)
    private String kommunekode;

    public String getKommunekode() {
        return kommunekode;
    }

    public void setKommunekode(String kommunekode) {
        this.kommunekode = kommunekode;
    }



    @JsonProperty(value = "kommunenavn")
    @Column(nullable = false)
    private String kommunenavn;

    public String getKommunenavn() {
        return this.kommunenavn;
    }

    public void setKommunenavn(String kommunenavn) {
        this.kommunenavn = kommunenavn;
    }


    public static Municipality getKommune(String kommunekode, String kommunenavn, QueryManager queryManager, Session session) {
        if (kommunekode != null) {
            Municipality municipality = queryManager.getItem(session, Municipality.class, Collections.singletonMap("kommunekode", kommunekode));
            if (municipality == null) {
                municipality = new Municipality();
                municipality.setKommunekode(kommunekode);
                municipality.setKommunenavn(kommunenavn);
                session.save(municipality);
            }
            return municipality;
        } else {
            return null;
        }
    }

    public static Municipality getKommune(Municipality old, QueryManager queryManager, Session session) {
        return getKommune(old.getKommunekode(), old.getKommunenavn(), queryManager, session);
    }
}
