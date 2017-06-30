package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.core.fapi.ParameterMap;
import dk.magenta.datafordeler.core.fapi.QueryField;
import dk.magenta.datafordeler.cvr.data.CvrQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 19-05-17.
 */
public class CompanyUnitQuery extends CvrQuery<CompanyUnitEntity> {

    public static final String PNUMBER = "pnumber";

    @QueryField(type = QueryField.FieldType.STRING)
    private String pNumber;

    public String getpNumber() {
        return pNumber;
    }

    public void setpNumber(String pNumber) {
        this.pNumber = pNumber;
    }

    @Override
    public Map<String, Object> getSearchParameters() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(PNUMBER, this.pNumber);
        return map;
    }

    @Override
    public void setFromParameters(ParameterMap parameters) {
        this.setpNumber(parameters.getFirst(PNUMBER));
    }

    @Override
    public Class<CompanyUnitEntity> getEntityClass() {
        return CompanyUnitEntity.class;
    }

    @Override
    public Class getDataClass() {
        return CompanyUnitBaseData.class;
    }

}
