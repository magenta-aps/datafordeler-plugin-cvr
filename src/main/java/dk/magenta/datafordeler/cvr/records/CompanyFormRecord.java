package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;
import org.hibernate.Session;

/**
 * Created by lars on 26-06-17.
 */
public class CompanyFormRecord extends CvrBaseRecord {

    @JsonProperty(value = "virksomhedsformkode")
    private int code;

    @JsonProperty(value = "kortBeskrivelse")
    private String shortDescription;

    @JsonProperty(value = "langBeskrivelse")
    private String longDescription;

    @JsonProperty(value = "ansvarligDataleverandoer")
    private String responsibleDatasource;

    @Override
    public void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        CompanyForm form = CompanyForm.getForm(this.code, this.shortDescription, this.longDescription, this.responsibleDatasource, queryManager, session);
        baseData.setForm(form);
    }

}
