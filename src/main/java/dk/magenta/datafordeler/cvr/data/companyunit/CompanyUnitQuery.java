package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.core.fapi.ParameterMap;
import dk.magenta.datafordeler.core.fapi.QueryField;
import dk.magenta.datafordeler.cvr.data.CvrQuery;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.shared.AddressData;
import dk.magenta.datafordeler.cvr.data.shared.IndustryData;
import dk.magenta.datafordeler.cvr.data.shared.IntegerData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.Industry;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;

import java.util.*;

/**
 * Created by lars on 19-05-17.
 */
public class CompanyUnitQuery extends CvrQuery<CompanyUnitEntity> {

    public static final String ASSOCIATED_COMPANY_CVR = "tilknyttetVirksomhedsCVRNummer";
    public static final String PRIMARYINDUSTRY = "hovedbranche";
    public static final String KOMMUNEKODE = "kommunekode";

    @QueryField(type = QueryField.FieldType.INT, queryName = ASSOCIATED_COMPANY_CVR)
    private Long associatedCompanyCvrNumber;

    public Long getAssociatedCompanyCvrNumber() {
        return associatedCompanyCvrNumber;
    }

    public void setAssociatedCompanyCvrNumber(Long associatedCompanyCvrNumber) {
        this.associatedCompanyCvrNumber = associatedCompanyCvrNumber;
    }

    @QueryField(type = QueryField.FieldType.STRING, queryName = PRIMARYINDUSTRY)
    private String primaryIndustry;

    public String getPrimaryIndustry() {
        return primaryIndustry;
    }

    public void setPrimaryIndustry(String primaryIndustry) {
        this.primaryIndustry = primaryIndustry;
    }


    @QueryField(type = QueryField.FieldType.STRING, queryName = KOMMUNEKODE)
    private List<String> kommunekoder = new ArrayList<>();

    public Collection<String> getKommunekoder() {
        return this.kommunekoder;
    }

    public void addKommunekode(String kommunekode) {
        this.kommunekoder.add(kommunekode);
    }

    public void addKommunekode(int kommunekode) {
        this.addKommunekode(String.format("%03d", kommunekode));
    }


    @Override
    public Map<String, Object> getSearchParameters() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ASSOCIATED_COMPANY_CVR, this.associatedCompanyCvrNumber);
        map.put(PRIMARYINDUSTRY, this.primaryIndustry);
        map.put(KOMMUNEKODE, this.kommunekoder);
        return map;
    }

    @Override
    public void setFromParameters(ParameterMap parameters) {
        this.setAssociatedCompanyCvrNumber(Long.parseLong(parameters.getFirst(ASSOCIATED_COMPANY_CVR)));
        this.setPrimaryIndustry(parameters.getFirst(PRIMARYINDUSTRY));
        if (parameters.containsKey(KOMMUNEKODE)) {
            for (String kommunekode : parameters.get(KOMMUNEKODE)) {
                this.addKommunekode(kommunekode);
            }
        }
    }

    @Override
    public Class<CompanyUnitEntity> getEntityClass() {
        return CompanyUnitEntity.class;
    }

    @Override
    public Class getDataClass() {
        return CompanyUnitBaseData.class;
    }

    @Override
    public LookupDefinition getLookupDefinition() {
        LookupDefinition lookupDefinition = new LookupDefinition(this);
/*
        if (this.pNumber != null) {
            lookupDefinition.put(LookupDefinition.entityref + ".pNumber", this.pNumber);
*/
        if (this.associatedCompanyCvrNumber != null) {
            lookupDefinition.put(CompanyUnitBaseData.DB_FIELD_CVR_NUMBER + LookupDefinition.separator + IntegerData.DB_FIELD_VALUE, this.associatedCompanyCvrNumber, Long.class);
        }
        if (this.primaryIndustry != null) {
            lookupDefinition.put(CompanyUnitBaseData.DB_FIELD_PRIMARY_INDUSTRY + LookupDefinition.separator + IndustryData.DB_FIELD_INDUSTRY + LookupDefinition.separator + Industry.DB_FIELD_CODE, this.primaryIndustry, String.class);
            lookupDefinition.put(CompanyUnitBaseData.DB_FIELD_PRIMARY_INDUSTRY + LookupDefinition.separator + IndustryData.DB_FIELD_PRIMARY, true, Boolean.class);
        }
        if (!this.kommunekoder.isEmpty()) {
            StringJoiner sj = new StringJoiner(LookupDefinition.separator);
            sj.add(CompanyUnitBaseData.DB_FIELD_LOCATION_ADDRESS);
            sj.add(AddressData.DB_FIELD_ADDRESS);
            sj.add(Address.DB_FIELD_MUNICIPALITY);
            sj.add(Municipality.DB_FIELD_CODE);
            lookupDefinition.put(sj.toString(), this.kommunekoder, String.class);
        }
        return lookupDefinition;
    }

}
