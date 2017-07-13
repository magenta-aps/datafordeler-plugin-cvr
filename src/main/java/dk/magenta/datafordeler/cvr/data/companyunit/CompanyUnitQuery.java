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

    public static final String TILKNYTTET_VIRKSOMHEDS_CVRNUMMER = "tilknyttetVirksomhedsCVRNummer";
    public static final String HOVEDBRANCHE = "hovedbranche";

    @QueryField(type = QueryField.FieldType.INT, queryName = TILKNYTTET_VIRKSOMHEDS_CVRNUMMER)
    private Long tilknyttetVirksomhedsCVRNummer;

    public Long getTilknyttetVirksomhedsCVRNummer() {
        return tilknyttetVirksomhedsCVRNummer;
    }

    public void setTilknyttetVirksomhedsCVRNummer(Long tilknyttetVirksomhedsCVRNummer) {
        this.tilknyttetVirksomhedsCVRNummer = tilknyttetVirksomhedsCVRNummer;
    }

    @QueryField(type = QueryField.FieldType.STRING, queryName = HOVEDBRANCHE)
    private String hovedbranche;

    public String getHovedbranche() {
        return hovedbranche;
    }

    public void setHovedbranche(String hovedbranche) {
        this.hovedbranche = hovedbranche;
    }

    @Override
    public Map<String, Object> getSearchParameters() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(TILKNYTTET_VIRKSOMHEDS_CVRNUMMER, this.tilknyttetVirksomhedsCVRNummer);
        map.put(HOVEDBRANCHE, this.hovedbranche);
        return map;
    }

    @Override
    public void setFromParameters(ParameterMap parameters) {
        this.setTilknyttetVirksomhedsCVRNummer(Long.parseLong(parameters.getFirst(TILKNYTTET_VIRKSOMHEDS_CVRNUMMER)));
        this.setHovedbranche(parameters.getFirst(HOVEDBRANCHE));
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

        if (this.tilknyttetVirksomhedsCVRNummer != null) {
            lookupDefinition.put(TILKNYTTET_VIRKSOMHEDS_CVRNUMMER + ".vaerdi", this.tilknyttetVirksomhedsCVRNummer);
        }

        if (this.hovedbranche != null) {
            lookupDefinition.put(HOVEDBRANCHE + ".vaerdi", this.hovedbranche);
        }

        return lookupDefinition;
    }

}
