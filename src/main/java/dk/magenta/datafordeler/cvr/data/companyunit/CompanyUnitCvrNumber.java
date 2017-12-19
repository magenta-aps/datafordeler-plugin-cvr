package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.cvr.data.shared.SingleData;

import javax.persistence.*;

import static dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitCvrNumber.DB_FIELD_BASEDATA;
import static dk.magenta.datafordeler.cvr.data.shared.SingleData.DB_FIELD_VALUE;

@Entity
@Table(name = "cvr_companyunit_cvr", indexes = {
        @Index(name = "cvr_companyunit_cvr_data", columnList = DB_FIELD_VALUE),
        @Index(name = "cvr_companyunit_cvr_base", columnList = DB_FIELD_BASEDATA + "_id")
})
public class CompanyUnitCvrNumber extends SingleData<Long> {

    public static final String DB_FIELD_BASEDATA = "companyUnitBaseData";

    @ManyToOne(targetEntity = CompanyUnitBaseData.class, optional = false)
    @JoinColumn(name = DB_FIELD_BASEDATA + "_id")
    private CompanyUnitBaseData companyUnitBaseData;

    public void setCompanyUnitBaseData(CompanyUnitBaseData companyUnitBaseData) {
        this.companyUnitBaseData = companyUnitBaseData;
    }
}
