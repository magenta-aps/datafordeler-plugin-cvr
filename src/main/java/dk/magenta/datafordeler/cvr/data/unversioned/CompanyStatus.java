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

    //--------------------------------------------------------------------------

    private static HashMap<String, Long> statusCache = new HashMap<>();

    private static boolean prepopulated = false;

    static {
        QueryManager.addCache(statusCache);
    }

    public static void initializeCache(Session session) {
        if (statusCache.isEmpty()) {
            log.debug("Prepopulating status cache");
            List<CompanyStatus> municipalities = QueryManager.getAllItems(session, CompanyStatus.class);
            for (CompanyStatus status : municipalities) {
                statusCache.put(status.status, status.getId());
            }
            log.debug("status cache contains " + statusCache.size() + " nodes");
        }
    }


    public static CompanyStatus getStatus(String statusText, Session session) {
        if (statusText != null) {
            initializeCache(session);
            CompanyStatus status = null;
            Long id = statusCache.get(statusText);
            if (id != null) {
                status = session.get(CompanyStatus.class, id);
            }
            if (status == null) {
                log.debug("CompanyStatus code "+statusText+" not found in cache, querying database");
                status = QueryManager.getItem(session, CompanyStatus.class, Collections.singletonMap(DB_FIELD_NAME, statusText));
            }
            if (status == null) {
                log.debug("CompanyStatus "+statusText+" not found; creating new");
                status = new CompanyStatus();
                status.setStatus(statusText);
                session.save(status);
            } else {
                log.debug("CompanyStatus "+statusText+" found in cache ("+status.getId()+")");
            }
            statusCache.put(statusText, status.getId());
            return status;
        } else {
            return null;
        }
    }
}
