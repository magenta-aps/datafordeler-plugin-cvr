package dk.magenta.datafordeler.cvr.data.unversioned;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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

    private static Logger log = LogManager.getLogger(Municipality.class.getSimpleName());

    public static final String DB_FIELD_CODE = "code";
    public static final String IO_FIELD_CODE = "kommuneKode";

    @JsonProperty(value = IO_FIELD_CODE)
    @XmlElement(name = IO_FIELD_CODE)
    @Column(nullable = false, unique = false)
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
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

    private static HashMap<Integer, Long> municipalityCache = new HashMap<>();

    static {
        QueryManager.addCache(municipalityCache);
    }

    public static void initializeCache(Session session) {
        if (municipalityCache.isEmpty()) {
            log.debug("Prepopulating municipality cache");
            List<Municipality> municipalities = QueryManager.getAllItems(session, Municipality.class);
            for (Municipality municipality : municipalities) {
                municipalityCache.put(municipality.code, municipality.getId());
            }
            log.debug("municipality cache contains " + municipalityCache.size() + " nodes");
        }
    }

    public static Municipality getMunicipality(int code, String name, Session session) {
        if (code > 0) {
            initializeCache(session);
            Municipality municipality = null;
            Long id = municipalityCache.get(code);
            if (id != null) {
                municipality = session.get(Municipality.class, id);
                if (municipality == null) {
                    log.debug("Municipality code "+code+" not found in cache, querying database");
                    municipality = QueryManager.getItem(session, Municipality.class, Collections.singletonMap(DB_FIELD_CODE, code));
                }
            }
            if (municipality == null) {
                log.debug("Municipality "+code+" not found; creating new");
                municipality = new Municipality();
                municipality.setCode(code);
                municipality.setName(name);
                session.save(municipality);
            } else {
                log.debug("Municipality "+code+" found ("+municipality.getId()+")");
            }
            municipalityCache.put(code, municipality.getId());
            return municipality;

        } else {
            return null;
        }
    }

    public static Municipality getMunicipality(Municipality old, Session session) {
        return getMunicipality(old.getCode(), old.getName(), session);
    }

}
