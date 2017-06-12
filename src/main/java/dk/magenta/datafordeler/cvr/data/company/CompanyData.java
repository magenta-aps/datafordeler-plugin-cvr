package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dk.magenta.datafordeler.cvr.data.CvrData;

import javax.persistence.*;

/**
 * Created by lars on 09-06-17.
 */
//@MappedSuperclass
//@javax.persistence.Entity
//@Table(name="cvr_company_data")
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class CompanyData extends CvrData<CompanyEffect, CompanyData> {
}
