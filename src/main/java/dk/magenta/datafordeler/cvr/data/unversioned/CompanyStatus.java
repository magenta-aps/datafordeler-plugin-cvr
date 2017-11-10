package dk.magenta.datafordeler.cvr.data.unversioned;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import org.hibernate.Session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import java.util.Collections;
import java.util.HashMap;

import static dk.magenta.datafordeler.cvr.data.unversioned.CompanyStatus.DB_FIELD_NAME;

/**
 * Created by lars on 26-01-15.
 */
@Entity
@Table(name = "cvr_status", indexes = {@Index(name = "cvrStatus", columnList = DB_FIELD_NAME)})
public class CompanyStatus extends UnversionedEntity {

    public CompanyStatus() {
    }

    //----------------------------------------------------

    public static final String DB_FIELD_NAME = "status";
    public static final String IO_FIELD_NAME = "status";

    @JsonProperty(value = IO_FIELD_NAME)
    @XmlElement(name = IO_FIELD_NAME)
    @Column(nullable = true, unique = true)
    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private static HashMap<String, CompanyStatus> statusCache = new HashMap<>();

    public static CompanyStatus getStatus(String statusText, Session session) {
        if (statusText != null) {
            CompanyStatus status = statusCache.get(statusText);
            if (status == null) {
                status = QueryManager.getItem(session, CompanyStatus.class, Collections.singletonMap(DB_FIELD_NAME, statusText));
                if (status == null) {
                    status = new CompanyStatus();
                    status.setStatus(statusText);
                    session.save(status);
                }
                statusCache.put(statusText, status);
            }
            return status;
        } else {
            return null;
        }
    }

    public void save(Session session) {
        session.saveOrUpdate(this);
    }

}
