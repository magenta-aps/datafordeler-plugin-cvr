package dk.magenta.datafordeler.cvr.data.unversioned;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Collections;

/**
 * Created by lars on 26-01-15.
 */
@Entity
@Table(name = "cvr_form", indexes = {@Index(name = "code", columnList = "code")})
public class CompanyForm extends UnversionedEntity {

    public CompanyForm() {
    }

    //----------------------------------------------------

    @JsonProperty
    @Column(nullable = true, unique = true)
    private String shortDescription;

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    //----------------------------------------------------

    @JsonProperty
    @Column(nullable = true, unique = true)
    private String longDescription;

    public String getLongDescription() {
        return this.longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    //----------------------------------------------------

    @JsonProperty
    @Column(nullable = false, unique = true)
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @JsonProperty
    @Column
    private String responsibleDatasource;

    public String getResponsibleDatasource() {
        return responsibleDatasource;
    }

    public void setResponsibleDatasource(String responsibleDatasource) {
        this.responsibleDatasource = responsibleDatasource;
    }

    //----------------------------------------------------

    public static CompanyForm getForm(int code, String shortDescription, String longDescription, String responsibleDatasource, QueryManager queryManager, Session session) {
        CompanyForm form = queryManager.getItem(session, CompanyForm.class, Collections.singletonMap("code", code));
        if (form == null) {
            form = new CompanyForm();
            form.setCode(code);
            form.setShortDescription(shortDescription);
            form.setLongDescription(longDescription);
            form.setResponsibleDatasource(responsibleDatasource);
            session.save(form);
        }
        return form;
    }

    public static CompanyForm getForm(CompanyForm oldForm, QueryManager queryManager, Session session) {
        return getForm(oldForm.getCode(), oldForm.getShortDescription(), oldForm.getLongDescription(), oldForm.getResponsibleDatasource(), queryManager, session);
    }
}
