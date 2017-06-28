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
        @Index(name = "code", columnList = "code")
})
public class PostCode extends UnversionedEntity {


    @JsonProperty(value = "postnummer")
    @Column(nullable = false, unique = true)
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }



    @JsonProperty(value = "postdistrikt")
    @Column
    private String text;

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public static PostCode getPostcode(int code, String text, QueryManager queryManager, Session session) {
        PostCode postCode = queryManager.getItem(session, PostCode.class, Collections.singletonMap("code", code));
        if (postCode == null) {
            postCode = new PostCode();
            postCode.setCode(code);
            postCode.setText(text);
            session.save(postCode);
        }
        return postCode;
    }

    public static PostCode getPostcode(PostCode old, QueryManager queryManager, Session session) {
        return getPostcode(old.getCode(), old.getText(), queryManager, session);
    }
}
