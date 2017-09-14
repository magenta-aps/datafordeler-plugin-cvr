package dk.magenta.datafordeler.cvr.data.participant;

import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.core.fapi.ParameterMap;
import dk.magenta.datafordeler.core.fapi.QueryField;
import dk.magenta.datafordeler.cvr.data.CvrQuery;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.shared.AddressData;
import dk.magenta.datafordeler.cvr.data.shared.IntegerData;
import dk.magenta.datafordeler.cvr.data.shared.TextData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Created by lars on 19-05-17.
 */
public class ParticipantQuery extends CvrQuery<ParticipantEntity> {

    public static final String CVRNUMMER = "CVRNummer";
    public static final String NAVN = "names";
    public static final String KOMMUNEKODE = "kommunekode";



    @QueryField(type = QueryField.FieldType.INT, queryName = CVRNUMMER)
    private String CVRNummer;

    public String getCVRNummer() {
        return CVRNummer;
    }

    public void setCVRNummer(String CVRNummer) {
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



    @QueryField(type = QueryField.FieldType.STRING, queryName = KOMMUNEKODE)
    private String kommunekode;

    public String getKommunekode() {
        return this.kommunekode;
    }

    public void setKommunekode(String kommunekode) {
        this.kommunekode = kommunekode;
    }

    public void setKommunekode(int kommunekode) {
        this.setKommunekode(String.format("%03d", kommunekode));
    }


    @Override
    public Map<String, Object> getSearchParameters() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(CVRNUMMER, this.CVRNummer);
        map.put(NAVN, this.navne);
        map.put(KOMMUNEKODE, this.kommunekode);
        return map;
    }

    @Override
    public void setFromParameters(ParameterMap parameters) {
        this.setCVRNummer(parameters.getFirst(CVRNUMMER));
        this.setNavne(parameters.getFirst(NAVN));
        this.setKommunekode(parameters.getFirst(KOMMUNEKODE));
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
            lookupDefinition.put(ParticipantBaseData.DB_FIELD_UNIT_NUMBER + LookupDefinition.separator + IntegerData.DB_FIELD_VALUE, this.CVRNummer, Integer.class);
        }

        if (this.navne != null) {
            lookupDefinition.put(ParticipantBaseData.DB_FIELD_NAMES + LookupDefinition.separator + TextData.DB_FIELD_VALUE, this.navne, String.class);
        }

        if (this.kommunekode != null) {
            StringJoiner sj = new StringJoiner(LookupDefinition.separator);
            sj.add(ParticipantBaseData.DB_FIELD_LOCATION_ADDRESS);
            sj.add(AddressData.DB_FIELD_ADDRESS);
            sj.add(Address.DB_FIELD_MUNICIPALITY);
            sj.add(Municipality.DB_FIELD_CODE);
            lookupDefinition.put(sj.toString(), this.kommunekode, String.class);
        }
        return lookupDefinition;
    }

}
