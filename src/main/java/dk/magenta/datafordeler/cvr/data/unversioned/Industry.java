package dk.magenta.datafordeler.cvr.data.unversioned;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.shared.IndustryData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.xml.bind.annotation.XmlElement;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static dk.magenta.datafordeler.cvr.data.unversioned.Industry.DB_FIELD_CODE;

/**
 * Created by lars on 26-01-15.
 */
@Entity
@Table(name = "cvr_industry", indexes = {
        @Index(name = "industryCode", columnList = DB_FIELD_CODE)
})
public class Industry extends UnversionedEntity {

    private static Logger log = LogManager.getLogger(Industry.class.getSimpleName());

    @OneToMany(mappedBy = "industry")
    private List<IndustryData> industryData;

    public static final String DB_FIELD_CODE = "industryCode";
    public static final String IO_FIELD_CODE = "branchekode";

    @JsonProperty(value = IO_FIELD_CODE)
    @XmlElement(name = IO_FIELD_CODE)
    @Column(nullable = false, unique = false)
    private String industryCode;

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    //----------------------------------------------------


    public static final String DB_FIELD_NAME = "industryText";
    public static final String IO_FIELD_NAME = "branchetekst";

    @JsonProperty(value = IO_FIELD_NAME)
    @XmlElement(name = IO_FIELD_NAME)
    @Column
    private String industryText;

    public String getIndustryText() {
        return this.industryText;
    }

    public void setIndustryText(String industryText) {
        this.industryText = industryText;
    }

    //----------------------------------------------------

    private static HashMap<String, Industry> industryCache = new HashMap<>();

    static {
        QueryManager.addCache(industryCache);
    }

    private static boolean prepopulated = false;

    public static void prepopulateCache(Session session) {
        log.debug("Prepopulating industry cache");
        List<Industry> industries = QueryManager.getAllItems(session, Industry.class);
        for (Industry industry : industries) {
            industryCache.put(industry.industryCode, industry);
        }
        log.debug("industryCache contains "+industryCache.size()+" nodes");
        prepopulated = true;
    }

    public static Industry getIndustry(String branchekode, String branchetekst, Session session) {
        if (branchekode != null) {
            Industry industry = industryCache.get(branchekode);
            if (industry == null) {
                log.debug("Industry code "+branchekode+" not found in cache");
                if (!prepopulated) {
                    log.debug("Querying database");
                    industry = QueryManager.getItem(session, Industry.class, Collections.singletonMap(DB_FIELD_CODE, branchekode));
                }
                if (industry == null) {
                    log.debug("Not found; creating new");
                    industry = new Industry();
                    industry.setIndustryCode(branchekode);
                    industry.setIndustryText(branchetekst);
                    session.save(industry);
                }
                industryCache.put(branchekode, industry);
            } else {
                log.debug("Industry "+branchekode+" found in cache ("+industry.getId()+")");
            }
            return industry;
        } else {
            return null;
        }
    }

}
