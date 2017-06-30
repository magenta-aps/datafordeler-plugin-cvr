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

/**
 * Created by lars on 26-01-15.
 */
@Entity
@Table(name = "cvr_status", indexes = {@Index(name = "status", columnList = "status")})
public class CompanyStatus extends UnversionedEntity {

    public CompanyStatus() {
    }

    //----------------------------------------------------

    @JsonProperty
    @XmlElement
    @Column(nullable = true, unique = true)
    private String status;

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public static CompanyStatus getStatus(String statusText, QueryManager queryManager, Session session) {
        CompanyStatus status = queryManager.getItem(session, CompanyStatus.class, Collections.singletonMap("status", statusText));
        if (status == null) {
            status = new CompanyStatus();
            status.setStatus(statusText);
            session.save(status);
        }
        return status;
    }


    public void save(Session session) {
        session.saveOrUpdate(this);
    }

}
