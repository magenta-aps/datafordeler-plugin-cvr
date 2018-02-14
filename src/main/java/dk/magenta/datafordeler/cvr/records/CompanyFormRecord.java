package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;
import org.hibernate.Session;

import javax.persistence.*;

/**
 * Record for Company form.
 */
@Entity
@Table(name = "cvr_record_company_form", indexes = {
        @Index(name = "cvr_record_form_company", columnList = CompanyFormRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
        @Index(name = "cvr_record_form_companyunit", columnList = CompanyFormRecord.DB_FIELD_COMPANYUNIT + DatabaseEntry.REF),
})
public class CompanyFormRecord extends CvrBitemporalDataRecord {

    @Transient
    @JsonProperty(value = "virksomhedsformkode")
    private String companyFormCode;

    public String getCompanyFormCode() {
        if (this.companyForm != null) {
            return this.companyForm.getCompanyFormCode();
        }
        return this.companyFormCode;
    }

    @Transient
    @JsonProperty(value = "kortBeskrivelse")
    private String shortDescription;

    public String getShortDescription() {
        if (this.companyForm != null) {
            return this.companyForm.getShortDescription();
        }
        return this.shortDescription;
    }

    @Transient
    @JsonProperty(value = "langBeskrivelse")
    private String longDescription;

    public String getLongDescription() {
        if (this.companyForm != null) {
            return this.companyForm.getLongDescription();
        }
        return this.longDescription;
    }

    @Transient
    @JsonProperty(value = "ansvarligDataleverandoer")
    private String responsibleDatasource;

    public String getResponsibleDatasource() {
        if (this.companyForm != null) {
            return this.companyForm.getResponsibleDataSource();
        }
        return this.responsibleDatasource;
    }

    @ManyToOne(targetEntity = CompanyForm.class)
    @JsonIgnore
    private CompanyForm companyForm;

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        CompanyForm form = CompanyForm.getForm(this.companyFormCode, this.shortDescription, this.longDescription, this.responsibleDatasource, session);
        baseData.setCompanyForm(form);
    }

    public void wire(Session session) {
        this.companyForm = CompanyForm.getForm(this.companyFormCode, this.shortDescription, this.longDescription, this.responsibleDatasource, session);
    }

}
