package dk.magenta.datafordeler.cvr.data.industry;

import dk.magenta.datafordeler.core.fapi.QueryField;
import dk.magenta.datafordeler.core.util.ListHashMap;
import dk.magenta.datafordeler.cvr.data.CvrQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 19-05-17.
 */
public class IndustryQuery extends CvrQuery<IndustryEntity> {

    public static final String CODE = "code";
    public static final String TEXT = "text";

    @QueryField(type = QueryField.FieldType.INT)
    private String code;

    @QueryField(type = QueryField.FieldType.STRING)
    private String text;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Map<String, Object> getSearchParameters() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(CODE, this.code);
        map.put(TEXT, this.text);
        return map;
    }

    @Override
    public void setFromParameters(ListHashMap<String, String> listHashMap) {
        this.setCode(listHashMap.getFirst(CODE));
        this.setText(listHashMap.getFirst(TEXT));
    }

    @Override
    public Class<IndustryEntity> getEntityClass() {
        return IndustryEntity.class;
    }

}
