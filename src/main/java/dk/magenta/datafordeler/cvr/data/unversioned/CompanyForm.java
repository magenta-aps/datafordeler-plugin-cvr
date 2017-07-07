package dk.magenta.datafordeler.cvr.data.unversioned;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import org.hibernate.Session;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.util.Collections;

/**
 * Created by lars on 26-01-15.
 */
@Entity
@Table(name = "cvr_form", indexes = {@Index(name = "companyFormCode", columnList = "virksomhedsformkode")})
public class CompanyForm extends UnversionedEntity {

    public CompanyForm() {
    }

    //----------------------------------------------------

    @JsonProperty(value = "kortBeskrivelse")
    @XmlElement(name = "kortBeskrivelse")
    @Column(nullable = true, unique = true)
    private String kortBeskrivelse;

    public String getKortBeskrivelse() {
        return kortBeskrivelse;
    }

    public void setKortBeskrivelse(String kortBeskrivelse) {
        this.kortBeskrivelse = kortBeskrivelse;
    }

    //----------------------------------------------------

    @JsonProperty(value = "langBeskrivelse")
    @XmlElement(name = "langBeskrivelse")
    @Column(nullable = true, unique = true)
    private String langBeskrivelse;

    public String getLangBeskrivelse() {
        return this.langBeskrivelse;
    }

    public void setLangBeskrivelse(String langBeskrivelse) {
        this.langBeskrivelse = langBeskrivelse;
    }

    //----------------------------------------------------

    @JsonProperty(value = "virksomhedsformkode")
    @XmlElement(name = "virksomhedsformkode")
    @Column(nullable = false, unique = true)
    private String virksomhedsformkode;

    public String getVirksomhedsformkode() {
        return virksomhedsformkode;
    }

    public void setVirksomhedsformkode(String virksomhedsformkode) {
        this.virksomhedsformkode = virksomhedsformkode;
    }

    @JsonProperty(value = "ansvarligDataleverandoer")
    @XmlElement(name = "ansvarligDataleverandoer")
    @Column
    private String ansvarligDataleverandoer;

    public String getAnsvarligDataleverandoer() {
        return ansvarligDataleverandoer;
    }

    public void setAnsvarligDataleverandoer(String ansvarligDataleverandoer) {
        this.ansvarligDataleverandoer = ansvarligDataleverandoer;
    }

    //----------------------------------------------------

    public static CompanyForm getForm(String virksomhedsformkode, String kortBeskrivelse, String langBeskrivelse, String ansvarligDataleverandoer, QueryManager queryManager, Session session) {
        CompanyForm form = queryManager.getItem(session, CompanyForm.class, Collections.singletonMap("virksomhedsformkode", virksomhedsformkode));
        if (form == null) {
            form = new CompanyForm();
            form.setVirksomhedsformkode(virksomhedsformkode);
            form.setKortBeskrivelse(kortBeskrivelse);
            form.setLangBeskrivelse(langBeskrivelse);
            form.setAnsvarligDataleverandoer(ansvarligDataleverandoer);
            session.save(form);
        }
        return form;
    }

    public static CompanyForm getForm(CompanyForm oldForm, QueryManager queryManager, Session session) {
        return getForm(oldForm.getVirksomhedsformkode(), oldForm.getKortBeskrivelse(), oldForm.getLangBeskrivelse(), oldForm.getAnsvarligDataleverandoer(), queryManager, session);
    }
}
