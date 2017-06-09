package dk.magenta.datafordeler.cvr.data.company;

import dk.magenta.datafordeler.cvr.data.CvrData;

import javax.persistence.MappedSuperclass;

/**
 * Created by lars on 09-06-17.
 */
@MappedSuperclass
public abstract class CompanyData extends CvrData<CompanyEffect, CompanyData> {
}
