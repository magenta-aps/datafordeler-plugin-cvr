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
@Table(name = "cvr_industry", indexes = {
        @Index(name = "industryCode", columnList = "branchekode")
})
public class Industry extends UnversionedEntity {



    @JsonProperty(value = "branchekode")
    @XmlElement(name = "branchekode")
    @Column(nullable = false, unique = true)
    private String branchekode;

    public String getBranchekode() {
        return branchekode;
    }

    public void setBranchekode(String branchekode) {
        this.branchekode = branchekode;
    }



    @JsonProperty(value = "branchetekst")
    @XmlElement(name = "branchetekst")
    @Column
    private String branchetekst;

    public String getBranchetekst() {
        return this.branchetekst;
    }

    public void setBranchetekst(String branchetekst) {
        this.branchetekst = branchetekst;
    }


    public static Industry getIndustry(String branchekode, String branchetekst, QueryManager queryManager, Session session) {
        Industry industry = queryManager.getItem(session, Industry.class, Collections.singletonMap("branchekode", branchekode));
        if (industry == null) {
            industry = new Industry();
            industry.setBranchekode(branchekode);
            industry.setBranchetekst(branchetekst);
            session.save(industry);
        }
        return industry;
    }

}
