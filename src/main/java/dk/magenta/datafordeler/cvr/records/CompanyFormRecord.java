package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;
import org.hibernate.Session;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * Record for Company form.
 */
@Entity
@Table(name = "cvr_record_company_form", indexes = {
        @Index(name = "cvr_record_form_company", columnList = CompanyFormRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
        @Index(name = "cvr_record_form_companyunit", columnList = CompanyFormRecord.DB_FIELD_COMPANYUNIT + DatabaseEntry.REF),
})
public class CompanyFormRecord extends CvrBitemporalDataRecord {

    @JsonProperty(value = "virksomhedsformkode")
    private String companyFormCode;

    @JsonProperty(value = "kortBeskrivelse")
    private String shortDescription;

    @JsonProperty(value = "langBeskrivelse")
    private String longDescription;

    @JsonProperty(value = "ansvarligDataleverandoer")
    private String responsibleDatasource;

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session) {
        CompanyForm form = CompanyForm.getForm(this.companyFormCode, this.shortDescription, this.longDescription, this.responsibleDatasource, session);
        baseData.setCompanyForm(form);
    }

}
