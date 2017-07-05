package dk.magenta.datafordeler.cvr.data.unversioned;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import org.hibernate.Session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Collections;

/**
 * Created by lars on 26-01-15.
 */
@Entity
@Table(name = "cvr_postcode", indexes = {
        @Index(name = "companyPostalCode", columnList = "postnummer")
})
public class PostCode extends UnversionedEntity {


    @JsonProperty(value = "postnummer")
    @Column(nullable = false, unique = true)
    private int postnummer;

    public int getPostnummer() {
        return postnummer;
    }

    public void setPostnummer(int postnummer) {
        this.postnummer = postnummer;
    }



    @JsonProperty(value = "postdistrikt")
    @Column
    private String postdistrikt;

    public String getPostdistrikt() {
        return this.postdistrikt;
    }

    public void setPostdistrikt(String postdistrikt) {
        this.postdistrikt = postdistrikt;
    }


    public static PostCode getPostcode(int postnummer, String postdistrikt, QueryManager queryManager, Session session) {
        PostCode post = queryManager.getItem(session, PostCode.class, Collections.singletonMap("postnummer", postnummer));
        if (post == null) {
            post = new PostCode();
            post.setPostnummer(postnummer);
            post.setPostdistrikt(postdistrikt);
            session.save(post);
        }
        return post;
    }

    public static PostCode getPostcode(PostCode old, QueryManager queryManager, Session session) {
        return getPostcode(old.getPostnummer(), old.getPostdistrikt(), queryManager, session);
    }
}
