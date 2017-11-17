package dk.magenta.datafordeler.cvr.data.unversioned;

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

import static dk.magenta.datafordeler.cvr.data.unversioned.CompanyStatus.DB_FIELD_NAME;

/**
 * Created by lars on 26-01-15.
 */
@Entity
@Table(name = "cvr_status", indexes = {@Index(name = "cvrStatus", columnList = DB_FIELD_NAME)})
public class CompanyStatus extends UnversionedEntity {

    private static Logger log = LogManager.getLogger(CompanyStatus.class.getSimpleName());

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

    private static boolean prepopulated = false;

    public static void prepopulateCache(Session session) {
        log.debug("Prepopulating status cache");
        List<CompanyStatus> statuses = QueryManager.getAllItems(session, CompanyStatus.class);
        for (CompanyStatus status : statuses) {
            statusCache.put(status.status, status);
        }
        log.debug("companyStatusCache contains "+statusCache.size()+" nodes");
        prepopulated = true;
    }

    public static CompanyStatus getStatus(String statusText, Session session) {
        if (statusText != null) {
            CompanyStatus status = statusCache.get(statusText);
            if (status == null) {
                log.debug("Status "+statusText+" not found in cache");
                if (!prepopulated) {
                    log.debug("Querying database");
                    status = QueryManager.getItem(session, CompanyStatus.class, Collections.singletonMap(DB_FIELD_NAME, statusText));
                }
                if (status == null) {
                    status = new CompanyStatus();
                    status.setStatus(statusText);
                    session.save(status);
                }
                statusCache.put(statusText, status);
            } else {
                log.debug("Status "+statusText+" found in cache ("+status.getId()+")");
            }
            return status;
        } else {
            return null;
        }
    }
}
