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

import static dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm.DB_FIELD_CODE;

/**
 * Created by lars on 26-01-15.
 */
@Entity
@Table(name = "cvr_form", indexes = {
        @Index(name = "cvr_company_form_code", columnList = DB_FIELD_CODE)
})
public class CompanyForm extends UnversionedEntity {

    private static Logger log = LogManager.getLogger(CompanyForm.class.getSimpleName());

    //----------------------------------------------------

    public static final String DB_FIELD_DESC_SHORT = "shortDescription";
    public static final String IO_FIELD_DESC_SHORT = "kortBeskrivelse";

    @JsonProperty(value = IO_FIELD_DESC_SHORT)
    @XmlElement(name = IO_FIELD_DESC_SHORT)
    @Column(nullable = true)
    private String shortDescription;

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_DESC_LONG = "longDescription";
    public static final String IO_FIELD_DESC_LONG = "langBeskrivelse";

    @JsonProperty(value = IO_FIELD_DESC_LONG)
    @XmlElement(name = IO_FIELD_DESC_LONG)
    @Column(nullable = true)
    private String longDescription;

    public String getLongDescription() {
        return this.longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_CODE = "companyFormCode";
    public static final String IO_FIELD_CODE = "virksomhedsformkode";

    @JsonProperty(value = IO_FIELD_CODE)
    @XmlElement(name = IO_FIELD_CODE)
    @Column(nullable = false, unique = true)
    private String companyFormCode;

    public String getCompanyFormCode() {
        return companyFormCode;
    }

    public void setCompanyFormCode(String companyFormCode) {
        this.companyFormCode = companyFormCode;
    }

    //----------------------------------------------------

    public static final String DB_FIELD_SOURCE = "responsibleDataSource";
    public static final String IO_FIELD_SOURCE = "ansvarligDataleverandoer";

    @JsonProperty(value = IO_FIELD_SOURCE)
    @XmlElement(name = IO_FIELD_SOURCE)
    @Column
    private String responsibleDataSource;

    public String getResponsibleDataSource() {
        return responsibleDataSource;
    }

    public void setResponsibleDataSource(String responsibleDataSource) {
        this.responsibleDataSource = responsibleDataSource;
    }

    //----------------------------------------------------

    private static HashMap<String, CompanyForm> formCache = new HashMap<>();

    static {
        QueryManager.addCache(formCache);
    }

    private static boolean prepopulated = false;

    public static void prepopulateCache(Session session) {
        log.debug("Prepopulating form cache");
        List<CompanyForm> forms = QueryManager.getAllItems(session, CompanyForm.class);
        for (CompanyForm companyForm : forms) {
            formCache.put(companyForm.companyFormCode, companyForm);
        }
        log.debug("companyFormCache contains "+formCache.size()+" nodes");
        prepopulated = true;
    }

    public static CompanyForm getForm(String companyFormCode, String shortDescription, String longDescription, String responsibleDataSource, Session session) {
        if (companyFormCode != null) {
            CompanyForm form = formCache.get(companyFormCode);
            if (form == null) {
                log.debug("Form code "+companyFormCode+" not found in cache");
                if (!prepopulated) {
                    log.debug("Querying database");
                    form = QueryManager.getItem(session, CompanyForm.class, Collections.singletonMap(DB_FIELD_CODE, companyFormCode));
                }
                if (form == null) {
                    log.debug("Not found; creating new");
                    form = new CompanyForm();
                    form.setCompanyFormCode(companyFormCode);
                    form.setShortDescription(shortDescription);
                    form.setLongDescription(longDescription);
                    form.setResponsibleDataSource(responsibleDataSource);
                    session.save(form);
                }
                formCache.put(companyFormCode, form);
            } else {
                log.debug("Form "+companyFormCode+" found in cache ("+form.getId()+")");
            }
            return form;
        } else {
            return null;
        }
    }

    public static CompanyForm getForm(CompanyForm oldForm, Session session) {
        return getForm(oldForm.getCompanyFormCode(), oldForm.getShortDescription(), oldForm.getLongDescription(), oldForm.getResponsibleDataSource(), session);
    }
}
