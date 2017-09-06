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

import static dk.magenta.datafordeler.cvr.data.unversioned.Industry.DB_FIELD_CODE;

/**
 * Created by lars on 26-01-15.
 */
@Entity
@Table(name = "cvr_industry", indexes = {
        @Index(name = "industryCode", columnList = DB_FIELD_CODE)
})
public class Industry extends UnversionedEntity {

    public static final String DB_FIELD_CODE = "industryCode";
    public static final String IO_FIELD_CODE = "branchekode";

    @JsonProperty(value = IO_FIELD_CODE)
    @XmlElement(name = IO_FIELD_CODE)
    @Column(nullable = false, unique = true)
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

    public static Industry getIndustry(String branchekode, String branchetekst, QueryManager queryManager, Session session) {
        Industry industry = queryManager.getItem(session, Industry.class, Collections.singletonMap(DB_FIELD_CODE, branchekode));
        if (industry == null) {
            industry = new Industry();
            industry.setIndustryCode(branchekode);
            industry.setIndustryText(branchetekst);
            session.save(industry);
        }
        return industry;
    }

}
