package dk.magenta.datafordeler.cvr.data.participant;

import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.core.fapi.ParameterMap;
import dk.magenta.datafordeler.core.fapi.QueryField;
import dk.magenta.datafordeler.cvr.data.CvrQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lars on 19-05-17.
 */
public class ParticipantQuery extends CvrQuery<ParticipantEntity> {

    public static final String CVRNUMMER = "CVRNummer";
    public static final String NAVN = "names";

    @QueryField(type = QueryField.FieldType.INT, queryName = CVRNUMMER)
    private Long CVRNummer;

    public Long getCVRNummer() {
        return CVRNummer;
    }

    public void setCVRNummer(Long CVRNummer) {
        this.CVRNummer = CVRNummer;
    }

    @QueryField(type = QueryField.FieldType.STRING, queryName = NAVN)
    private String navne;

    public String getNavne() {
        return navne;
    }

    public void setNavne(String navne) {
        this.navne = navne;
    }

    @Override
    public Map<String, Object> getSearchParameters() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(CVRNUMMER, this.CVRNummer);
        map.put(NAVN, this.navne);
        return map;
    }

    @Override
    public void setFromParameters(ParameterMap parameters) {
        this.setCVRNummer(Long.parseLong(parameters.getFirst(CVRNUMMER)));
        this.setNavne(parameters.getFirst(NAVN));
    }

    @Override
    public Class<ParticipantEntity> getEntityClass() {
        return ParticipantEntity.class;
    }

    @Override
    public Class getDataClass() {
        return ParticipantBaseData.class;
    }



    @Override
    public LookupDefinition getLookupDefinition() {
        LookupDefinition lookupDefinition = new LookupDefinition(this);

        if (this.CVRNummer != null) {
            lookupDefinition.put("unitNumber.value", this.CVRNummer);
        }

        if (this.navne != null) {
            lookupDefinition.put("names.value", this.navne);
        }

        return lookupDefinition;
    }

}
