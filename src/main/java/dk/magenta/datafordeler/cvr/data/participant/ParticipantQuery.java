package dk.magenta.datafordeler.cvr.data.participant;

import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.core.fapi.ParameterMap;
import dk.magenta.datafordeler.core.fapi.QueryField;
import dk.magenta.datafordeler.cvr.data.CvrQuery;
import dk.magenta.datafordeler.cvr.data.shared.AddressData;
import dk.magenta.datafordeler.cvr.data.shared.IntegerData;
import dk.magenta.datafordeler.cvr.data.shared.TextData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;

import java.util.*;

/**
 * Container for a query for Participants, defining fields and database lookup
 */
public class ParticipantQuery extends CvrQuery<ParticipantEntity> {

    public static final String UNITNUMBER = "enhedsNummer";
    public static final String NAVN = "names";
    public static final String KOMMUNEKODE = "kommunekode";



    @QueryField(type = QueryField.FieldType.INT, queryName = UNITNUMBER)
    private String enhedsNummer;

    public String getEnhedsNummer() {
        return enhedsNummer;
    }

    public void setEnhedsNummer(String enhedsNummer) {
        this.enhedsNummer = enhedsNummer;
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
        map.put(UNITNUMBER, this.enhedsNummer);
        map.put(NAVN, this.navne);
        map.put(KOMMUNEKODE, this.kommunekoder);
        return map;
    }

    @Override
    public void setFromParameters(ParameterMap parameters) {
        this.setEnhedsNummer(parameters.getFirst(UNITNUMBER));
        this.setNavne(parameters.getFirst(NAVN));
        if (parameters.containsKey(KOMMUNEKODE)) {
            for (String kommunekode : parameters.get(KOMMUNEKODE)) {
                this.addKommunekode(kommunekode);
            }
        }
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
        LookupDefinition lookupDefinition = new LookupDefinition(this, ParticipantBaseData.class);

        if (this.enhedsNummer != null) {
            lookupDefinition.put(ParticipantBaseData.DB_FIELD_UNIT_NUMBER + LookupDefinition.separator + IntegerData.DB_FIELD_VALUE, this.enhedsNummer, Integer.class);
        }

        if (this.navne != null) {
            lookupDefinition.put(ParticipantBaseData.DB_FIELD_NAMES + LookupDefinition.separator + TextData.DB_FIELD_VALUE, this.navne, String.class);
        }

        if (!this.kommunekoder.isEmpty()) {
            StringJoiner sj = new StringJoiner(LookupDefinition.separator);
            sj.add(ParticipantBaseData.DB_FIELD_LOCATION_ADDRESS);
            sj.add(AddressData.DB_FIELD_ADDRESS);
            sj.add(Address.DB_FIELD_MUNICIPALITY);
            sj.add(Municipality.DB_FIELD_CODE);
            lookupDefinition.put(sj.toString(), this.kommunekoder, Integer.class);
        }
        if (!this.getKommunekodeRestriction().isEmpty()) {
            StringJoiner sj = new StringJoiner(LookupDefinition.separator);
            sj.add(ParticipantBaseData.DB_FIELD_LOCATION_ADDRESS);
            sj.add(AddressData.DB_FIELD_ADDRESS);
            sj.add(Address.DB_FIELD_MUNICIPALITY);
            sj.add(Municipality.DB_FIELD_CODE);
            lookupDefinition.put(sj.toString(), this.getKommunekodeRestriction(), Integer.class);
        }
        return lookupDefinition;
    }

}
