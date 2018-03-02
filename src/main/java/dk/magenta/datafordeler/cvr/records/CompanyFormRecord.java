package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Objects;

/**
 * Record for Company form.
 */
@Entity
@Table(name = "cvr_record_company_form", indexes = {
        @Index(name = "cvr_record_form_company", columnList = CompanyFormRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
        @Index(name = "cvr_record_form_companyunit", columnList = CompanyFormRecord.DB_FIELD_COMPANYUNIT + DatabaseEntry.REF),
        //@Index(name = "cvr_record_form_companymeta", columnList = CompanyFormRecord.DB_FIELD_COMPANY_META + DatabaseEntry.REF),
})
public class CompanyFormRecord extends CvrBitemporalDataRecord {

    public static final String DB_FIELD_COMPANY_METADATA = "companyMetadataRecord";

    @ManyToOne(targetEntity = CompanyMetadataRecord.class)
    @JoinColumn(name = DB_FIELD_COMPANY_METADATA + DatabaseEntry.REF)
    @JsonIgnore
    private CompanyMetadataRecord companyMetadataRecord;

    public void setCompanyMetadataRecord(CompanyMetadataRecord companyMetadataRecord) {
        this.companyMetadataRecord = companyMetadataRecord;
    }



    public static final String IO_FIELD_CODE = "virksomhedsformkode";

    @Transient
    @JsonProperty(value = IO_FIELD_CODE)
    private String companyFormCode;

    public String getCompanyFormCode() {
        if (this.companyForm != null) {
            return this.companyForm.getCompanyFormCode();
        }
        return this.companyFormCode;
    }

    public static final String IO_FIELD_SHORT_DESCRIPTION = "kortBeskrivelse";

    @Transient
    @JsonProperty(value = IO_FIELD_SHORT_DESCRIPTION)
    private String shortDescription;

    public String getShortDescription() {
        if (this.companyForm != null) {
            return this.companyForm.getShortDescription();
        }
        return this.shortDescription;
    }

    public static final String IO_FIELD_LONG_DESCRIPTION = "langBeskrivelse";

    @Transient
    @JsonProperty(value = IO_FIELD_LONG_DESCRIPTION)
    private String longDescription;

    public String getLongDescription() {
        if (this.companyForm != null) {
            return this.companyForm.getLongDescription();
        }
        return this.longDescription;
    }

    public static final String IO_FIELD_RESPONSIBLE_DATASOURCE = "ansvarligDataleverandoer";

    @Transient
    @JsonProperty(value = IO_FIELD_RESPONSIBLE_DATASOURCE)
    private String responsibleDatasource;

    public String getResponsibleDatasource() {
        if (this.companyForm != null) {
            return this.companyForm.getResponsibleDataSource();
        }
        return this.responsibleDatasource;
    }

    public static final String DB_FIELD_FORM = "companyForm";

    @ManyToOne(targetEntity = CompanyForm.class)
    @JsonIgnore
    private CompanyForm companyForm;

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        CompanyForm form = CompanyForm.getForm(this.companyFormCode, this.shortDescription, this.longDescription, this.responsibleDatasource, session);
        baseData.setCompanyForm(form);
    }

    public void wire(Session session) {
        if (this.companyFormCode != null && (this.companyForm == null || !this.companyFormCode.equals(this.companyForm.getCompanyFormCode()))) {
            this.companyForm = CompanyForm.getForm(this.companyFormCode, this.shortDescription, this.longDescription, this.responsibleDatasource, session);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CompanyFormRecord that = (CompanyFormRecord) o;
        return Objects.equals(companyFormCode, that.companyFormCode) &&
                Objects.equals(shortDescription, that.shortDescription) &&
                Objects.equals(longDescription, that.longDescription) &&
                Objects.equals(responsibleDatasource, that.responsibleDatasource) &&
                Objects.equals(companyForm, that.companyForm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), companyFormCode, shortDescription, longDescription, responsibleDatasource, companyForm);
    }
}
