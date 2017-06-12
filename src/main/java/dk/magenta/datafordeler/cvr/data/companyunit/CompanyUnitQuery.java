package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.core.database.Entity;
import dk.magenta.datafordeler.core.fapi.QueryField;
import dk.magenta.datafordeler.core.util.ListHashMap;
import dk.magenta.datafordeler.cvr.data.CvrQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 19-05-17.
 */
public class CompanyUnitQuery extends CvrQuery<CompanyUnitEntity> {

    public static final String CVRNUMBER = "cvrnumber";

    @QueryField(type = QueryField.FieldType.STRING)
    private String cvrNumber;

    public String getCvrNumber() {
        return cvrNumber;
    }

    public void setCvrNumber(String cvrNumber) {
        this.cvrNumber = cvrNumber;
    }

    @Override
    public Map<String, Object> getSearchParameters() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(CVRNUMBER, this.cvrNumber);
        return map;
    }

    @Override
    public void setFromParameters(ListHashMap<String, String> listHashMap) {
        this.setCvrNumber(listHashMap.getFirst(CVRNUMBER));
    }

    @Override
    public Class<CompanyUnitEntity> getEntityClass() {
        return CompanyUnitEntity.class;
    }

}
