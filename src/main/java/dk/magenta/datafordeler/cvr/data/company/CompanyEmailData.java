package dk.magenta.datafordeler.cvr.data.company;

import javax.persistence.Table;

/**
 * Created by lars on 09-06-17.
 */
@Table(name="cvr_company_email")
public class CompanyEmailData extends CompanySingleTextData {
    @Override
    protected String getFieldName() {
        return "email";
    }
}
