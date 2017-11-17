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

import static dk.magenta.datafordeler.cvr.data.unversioned.PostCode.DB_FIELD_CODE;

/**
 * Created by lars on 26-01-15.
 */
@Entity
@Table(name = "cvr_postcode", indexes = {
        @Index(name = "companyPostalCode", columnList = DB_FIELD_CODE)
})
public class PostCode extends UnversionedEntity {

    private static Logger log = LogManager.getLogger(PostCode.class.getSimpleName());

    public static final String DB_FIELD_CODE = "postCode";
    public static final String IO_FIELD_CODE = "postnummer";

    @JsonProperty(value = IO_FIELD_CODE)
    @XmlElement(name = IO_FIELD_CODE)
    @Column(nullable = false, unique = false, name = DB_FIELD_CODE)
    private int postCode;

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_DISTRICT = "postDistrict";
    public static final String IO_FIELD_DISTRICT = "postdistrikt";

    @JsonProperty(value = IO_FIELD_DISTRICT)
    @XmlElement(name = IO_FIELD_DISTRICT)
    @Column
    private String postDistrict;

    public String getPostDistrict() {
        return this.postDistrict;
    }

    public void setPostDistrict(String postDistrict) {
        this.postDistrict = postDistrict;
    }

    //----------------------------------------------------

    private static HashMap<Integer, PostCode> postCodeCache = new HashMap<>();

    static {
        QueryManager.addCache(postCodeCache);
    }

    private static boolean prepopulated = false;

    public static void prepopulateCache(Session session) {
        log.debug("Prepopulating postcode cache");
        List<PostCode> postCodes = QueryManager.getAllItems(session, PostCode.class);
        for (PostCode postCode : postCodes) {
            postCodeCache.put(postCode.postCode, postCode);
        }
        log.debug("postcodeCache contains "+postCodeCache.size()+" nodes");
        prepopulated = true;
    }

    public static PostCode getPostcode(int postCode, String postDistrict, Session session) {
        if (postCode > 0) {
            PostCode post = postCodeCache.get(postCode);
            if (post == null) {
                log.debug("Postcode "+postCode+" not found in cache");
                if (!prepopulated) {
                    log.debug("Querying database");
                    post = QueryManager.getItem(session, PostCode.class, Collections.singletonMap(DB_FIELD_CODE, postCode));
                }
                if (post == null) {
                    log.debug("Not found; creating new");
                    post = new PostCode();
                    post.setPostCode(postCode);
                    post.setPostDistrict(postDistrict);
                    session.save(post);
                }
                postCodeCache.put(postCode, post);
            } else {
                log.debug("PostCode "+postCode+" found in cache ("+post.getId()+")");
            }
            return post;
        } else {
            return null;
        }
    }

    public static PostCode getPostcode(PostCode old, Session session) {
        return getPostcode(old.getPostCode(), old.getPostDistrict(), session);
    }
}
