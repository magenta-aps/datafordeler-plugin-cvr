package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.core.fapi.ParameterMap;
import dk.magenta.datafordeler.core.fapi.QueryField;
import dk.magenta.datafordeler.cvr.data.CvrQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 19-05-17.
 */
public class CompanyUnitQuery extends CvrQuery<CompanyUnitEntity> {

    public static final String ASSOCIATED_COMPANY_CVR = "tilknyttetVirksomhedsCVRNummer";
    public static final String PRIMARYINDUSTRY = "hovedbranche";

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

    @Override
    public Map<String, Object> getSearchParameters() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ASSOCIATED_COMPANY_CVR, this.associatedCompanyCvrNumber);
        map.put(PRIMARYINDUSTRY, this.primaryIndustry);
        return map;
    }

    @Override
    public void setFromParameters(ParameterMap parameters) {
        this.setAssociatedCompanyCvrNumber(Long.parseLong(parameters.getFirst(ASSOCIATED_COMPANY_CVR)));
        this.setPrimaryIndustry(parameters.getFirst(PRIMARYINDUSTRY));
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
            lookupDefinition.put("associatedCvrNumber.value", this.associatedCompanyCvrNumber);
        }

        if (this.primaryIndustry != null) {
            lookupDefinition.put("primaryIndustry.value", this.primaryIndustry);
        }

        return lookupDefinition;
    }

}
