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
        @Index(name = "industryCode", columnList = "code")
})
public class Industry extends UnversionedEntity {
    
    @JsonProperty(value = "branchekode")
    @XmlElement(name = "branchekode")
    @Column(nullable = false, unique = true)
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }



    @JsonProperty(value = "branchetekst")
    @XmlElement(name = "branchetekst")
    @Column
    private String text;

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public static Industry getIndustry(int code, String text, QueryManager queryManager, Session session) {
        Industry industry = queryManager.getItem(session, Industry.class, Collections.singletonMap("code", code));
        if (industry == null) {
            industry = new Industry();
            industry.setCode(code);
            industry.setText(text);
            session.save(industry);
        }
        return industry;
    }

}
