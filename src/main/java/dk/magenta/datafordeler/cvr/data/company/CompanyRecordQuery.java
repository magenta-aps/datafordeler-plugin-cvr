package dk.magenta.datafordeler.cvr.data.company;

import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.core.fapi.ParameterMap;
import dk.magenta.datafordeler.core.fapi.QueryField;
import dk.magenta.datafordeler.cvr.CvrRecordLookupDefinition;
import dk.magenta.datafordeler.cvr.data.CvrQuery;
import dk.magenta.datafordeler.cvr.data.shared.AddressData;
import dk.magenta.datafordeler.cvr.data.shared.BooleanData;
import dk.magenta.datafordeler.cvr.data.shared.ContactData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;
import dk.magenta.datafordeler.cvr.records.*;

import java.util.*;

/**
 * Container for a query for Companies, defining fields and database lookup
 */
public class CompanyRecordQuery extends CompanyQuery {

    @Override
    public void setFromParameters(ParameterMap parameters) {
        this.setCvrNumre(parameters.get(CVRNUMMER));
        this.setReklamebeskyttelse(parameters.getFirst(REKLAMEBESKYTTELSE));
        this.setVirksomhedsnavn(parameters.getFirst(NAVN));
        this.setTelefonnummer(parameters.getFirst(TELEFONNUMMER));
        this.setTelefaxnummer(parameters.getFirst(TELEFAXNUMMER));
        this.setEmailadresse(parameters.getFirst(EMAILADRESSE));
        this.setVirksomhedsform(parameters.getFirst(VIRKSOMHEDSFORM));
        this.setKommunekoder(parameters.get(KOMMUNEKODE));
    }

    @Override
    public Class<CompanyEntity> getEntityClass() {
        return CompanyEntity.class;
    }

    @Override
    public Class getDataClass() {
        return CompanyBaseData.class;
    }


    @Override
    public LookupDefinition getLookupDefinition() {
        CvrRecordLookupDefinition lookupDefinition = new CvrRecordLookupDefinition(this, null);

        if (!this.getCvrNumre().isEmpty()) {
            lookupDefinition.put(LookupDefinition.entityref + LookupDefinition.separator + CompanyRecord.DB_FIELD_CVR_NUMBER, this.getCvrNumre(), Integer.class);
        }
        if (this.getVirksomhedsform() != null) {
            lookupDefinition.put(LookupDefinition.entityref + LookupDefinition.separator + CompanyRecord.DB_FIELD_FORM + LookupDefinition.separator + CompanyFormRecord.DB_FIELD_FORM + LookupDefinition.separator + CompanyForm.DB_FIELD_CODE, this.getVirksomhedsform(), String.class);
        }
        if (this.getReklamebeskyttelse() != null) {
            lookupDefinition.put(LookupDefinition.entityref + LookupDefinition.separator + CompanyRecord.DB_FIELD_ADVERTPROTECTION, this.getReklamebeskyttelse(), Boolean.class);
        }
        if (this.getVirksomhedsnavn() != null) {
            lookupDefinition.put(LookupDefinition.entityref + LookupDefinition.separator + CompanyRecord.DB_FIELD_NAMES + LookupDefinition.separator + NameRecord.DB_FIELD_NAME, this.getVirksomhedsnavn(), String.class);
        }
        if (this.getTelefonnummer() != null) {
            lookupDefinition.put(LookupDefinition.entityref + LookupDefinition.separator + CompanyRecord.DB_FIELD_PHONE + LookupDefinition.separator + ContactRecord.DB_FIELD_DATA, this.getTelefonnummer(), String.class);
        }
        if (this.getTelefaxnummer() != null) {
            lookupDefinition.put(LookupDefinition.entityref + LookupDefinition.separator + CompanyRecord.DB_FIELD_FAX + LookupDefinition.separator +ContactRecord.DB_FIELD_DATA, this.getTelefaxnummer(), String.class);
        }
        if (this.getEmailadresse() != null) {
            lookupDefinition.put(LookupDefinition.entityref + LookupDefinition.separator + CompanyRecord.DB_FIELD_EMAIL + LookupDefinition.separator + ContactRecord.DB_FIELD_DATA, this.getEmailadresse(), String.class);
        }
        if (!this.getKommunekoder().isEmpty()) {
            StringJoiner sj = new StringJoiner(LookupDefinition.separator);
            sj.add(LookupDefinition.entityref);
            sj.add(CompanyRecord.DB_FIELD_LOCATION_ADDRESS);
            sj.add(AddressRecord.DB_FIELD_MUNICIPALITY);
            sj.add(AddressMunicipalityRecord.DB_FIELD_MUNICIPALITY);
            sj.add(Municipality.DB_FIELD_CODE);
            lookupDefinition.put(sj.toString(), this.getKommunekoder(), Integer.class);
        }

        if (!this.getKommunekodeRestriction().isEmpty()) {
            StringJoiner sj = new StringJoiner(LookupDefinition.separator);
            sj.add(LookupDefinition.entityref);
            sj.add(CompanyRecord.DB_FIELD_LOCATION_ADDRESS);
            sj.add(AddressRecord.DB_FIELD_MUNICIPALITY);
            sj.add(AddressMunicipalityRecord.DB_FIELD_MUNICIPALITY);
            sj.add(Municipality.DB_FIELD_CODE);
            lookupDefinition.put(sj.toString(), this.getKommunekodeRestriction(), Integer.class);
        }
        return lookupDefinition;
    }
}
