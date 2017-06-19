package dk.magenta.datafordeler.cvr.data.participant;

import dk.magenta.datafordeler.core.fapi.ParameterMap;
import dk.magenta.datafordeler.core.fapi.QueryField;
import dk.magenta.datafordeler.cvr.data.CvrQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 19-05-17.
 */
public class ParticipantQuery extends CvrQuery<ParticipantEntity> {

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
    public void setFromParameters(ParameterMap parameters) {
        this.setCvrNumber(parameters.getFirst(CVRNUMBER));
    }

    @Override
    public Class<ParticipantEntity> getEntityClass() {
        return ParticipantEntity.class;
    }

    @Override
    public Class getDataClass() {
        return ParticipantBaseData.class;
    }

}
