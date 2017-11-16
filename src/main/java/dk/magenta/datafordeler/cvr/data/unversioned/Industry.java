package dk.magenta.datafordeler.cvr.data.unversioned;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.shared.IndustryData;
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

    public static void prepopulateIndustryCache(Session session) {
        List<Industry> industries = QueryManager.getAllItems(session, Industry.class);
        for (Industry industry : industries) {
            industryCache.put(industry.industryCode, industry);
        }
        prepopulated = true;
    }

    public static Industry getIndustry(String branchekode, String branchetekst, Session session) {
        if (branchekode != null) {
            Industry industry = industryCache.get(branchekode);
            if (industry == null) {
                if (!prepopulated) {
                    industry = QueryManager.getItem(session, Industry.class, Collections.singletonMap(DB_FIELD_CODE, branchekode));
                }
                if (industry == null) {
                    industry = new Industry();
                    industry.setIndustryCode(branchekode);
                    industry.setIndustryText(branchetekst);
                }
                industryCache.put(branchekode, industry);
            } else {
                //industry = (Industry) session.merge(industry);
            }
            return industry;
        } else {
            return null;
        }
    }

}
