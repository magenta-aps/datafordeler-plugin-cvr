package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.cvr.data.Bitemporality;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;
import org.hibernate.Session;

/**
 * Record for Company form.
 */
public class CompanyFormRecord extends CvrBaseRecord {

    @JsonProperty(value = "virksomhedsformkode")
    private String companyFormCode;

    @JsonProperty(value = "kortBeskrivelse")
    private String shortDescription;

    @JsonProperty(value = "langBeskrivelse")
    private String longDescription;

    @JsonProperty(value = "ansvarligDataleverandoer")
    private String responsibleDatasource;

    @Override
    public void populateBaseData(CompanyBaseData baseData, Session session, Bitemporality bitemporality) {
        CompanyForm form = CompanyForm.getForm(this.companyFormCode, this.shortDescription, this.longDescription, this.responsibleDatasource, session);
        baseData.setCompanyForm(form);
    }

}
