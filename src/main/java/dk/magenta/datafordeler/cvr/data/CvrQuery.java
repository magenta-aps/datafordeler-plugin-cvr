package dk.magenta.datafordeler.cvr.data;

import dk.magenta.datafordeler.core.database.Entity;
import dk.magenta.datafordeler.core.fapi.Query;
import dk.magenta.datafordeler.core.fapi.QueryField;
import dk.magenta.datafordeler.core.util.ListHashMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 19-05-17.
 */
public abstract class CvrQuery<E extends Entity> extends Query<E> {

    public static final String SOMECOMMONDATA = "someCommonData";

    @QueryField(type = QueryField.FieldType.STRING)
    private String someCommonData;

    public String getSomeCommonData() {
        return someCommonData;
    }

    public void setSomeCommonData(String someCommonData) {
        this.someCommonData = someCommonData;
    }

    @Override
    public Map<String, Object> getSearchParameters() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("someCommonData", this.someCommonData);
        return map;
    }

    @Override
    public void setFromParameters(ListHashMap<String, String> listHashMap) {
        this.setSomeCommonData(listHashMap.getFirst(SOMECOMMONDATA));
    }
}
