package dk.magenta.datafordeler.cvr.data.company;

import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.core.fapi.ParameterMap;
import dk.magenta.datafordeler.core.fapi.QueryField;
import dk.magenta.datafordeler.cvr.data.CvrQuery;
import dk.magenta.datafordeler.cvr.data.shared.AddressData;
import dk.magenta.datafordeler.cvr.data.shared.BooleanData;
import dk.magenta.datafordeler.cvr.data.shared.ContactData;
import dk.magenta.datafordeler.cvr.data.shared.TextData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;

import java.util.*;

/**
 * Created by lars on 19-05-17.
 */
public class CompanyQuery extends CvrQuery<CompanyEntity> {

    public static final String CVRNUMMER = "CVRNummer";
    public static final String REKLAMEBESKYTTELSE = "reklamebeskyttelse";
    public static final String NAVN = "virksomhedsnavn";
    public static final String TELEFONNUMMER = "telefon";
    public static final String TELEFAXNUMMER = "telefax";
    public static final String EMAILADRESSE = "emailadresse";
    public static final String VIRKSOMHEDSFORM = "virksomhedsform";
    public static final String KOMMUNEKODE = "kommunekode";

    @QueryField(type = QueryField.FieldType.STRING, queryName = CVRNUMMER)
    private String CVRNummer;

    public String getCVRNummer() {
        return CVRNummer;
    }

    public void setCVRNummer(String CVRNummer) {
        this.CVRNummer = CVRNummer;
    }


    @QueryField(type = QueryField.FieldType.STRING, queryName = REKLAMEBESKYTTELSE)
    private String reklamebeskyttelse;

    public String getReklamebeskyttelse() {
        return this.reklamebeskyttelse;
    }

    public void setReklamebeskyttelse(String reklamebeskyttelse) {
        this.reklamebeskyttelse = reklamebeskyttelse;
    }

    @QueryField(type = QueryField.FieldType.STRING, queryName = NAVN)
    private String virksomhedsnavn;

    public String getVirksomhedsnavn() {
        return this.virksomhedsnavn;
    }

    public void setVirksomhedsnavn(String virksomhedsnavn) {
        this.virksomhedsnavn = virksomhedsnavn;
    }


    @QueryField(type = QueryField.FieldType.STRING, queryName = TELEFONNUMMER)
    private String telefonnummer;

    public String getTelefonnummer() {
        return this.telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }



    @QueryField(type = QueryField.FieldType.STRING, queryName = TELEFAXNUMMER)
    private String telefaxnummer;

    public String getTelefaxnummer() {
        return this.telefaxnummer;
    }

    public void setTelefaxnummer(String telefaxnummer) {
        this.telefaxnummer = telefaxnummer;
    }



    @QueryField(type = QueryField.FieldType.STRING, queryName = EMAILADRESSE)
    private String emailadresse;

    public String getEmailadresse() {
        return this.emailadresse;
    }

    public void setEmailadresse(String emailadresse) {
        this.emailadresse = emailadresse;
    }



    @QueryField(type = QueryField.FieldType.STRING, queryName = VIRKSOMHEDSFORM)
    private String virksomhedsform;

    public String getVirksomhedsform() {
        return this.virksomhedsform;
    }

    public void setVirksomhedsform(String virksomhedsform) {
        this.virksomhedsform = virksomhedsform;
    }

    public void setVirksomhedsform(int virksomhedsform) {
        this.setVirksomhedsform(Integer.toString(virksomhedsform));
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
        map.put(CVRNUMMER, this.CVRNummer);
        map.put(REKLAMEBESKYTTELSE, this.reklamebeskyttelse);
        map.put(NAVN, this.virksomhedsnavn);
        map.put(TELEFONNUMMER, this.telefonnummer);
        map.put(TELEFAXNUMMER, this.telefaxnummer);
        map.put(EMAILADRESSE, this.emailadresse);
        map.put(VIRKSOMHEDSFORM, this.virksomhedsform);
        map.put(KOMMUNEKODE, this.kommunekoder);
        return map;
    }

    @Override
    public void setFromParameters(ParameterMap parameters) {
        this.setCVRNummer(parameters.getFirst(CVRNUMMER));
        this.setReklamebeskyttelse(parameters.getFirst(REKLAMEBESKYTTELSE));
        this.setVirksomhedsnavn(parameters.getFirst(NAVN));
        this.setTelefonnummer(parameters.getFirst(TELEFONNUMMER));
        this.setTelefaxnummer(parameters.getFirst(TELEFAXNUMMER));
        this.setEmailadresse(parameters.getFirst(EMAILADRESSE));
        this.setVirksomhedsform(parameters.getFirst(VIRKSOMHEDSFORM));
        if (parameters.containsKey(KOMMUNEKODE)) {
            for (String kommunekode : parameters.get(KOMMUNEKODE)) {
                this.addKommunekode(kommunekode);
            }
        }
    }

    @Override
    public Class<CompanyEntity> getEntityClass() {
        return CompanyEntity.class;
    }

    @Override
    public Class getDataClass() {
        return CompanyBaseData.class;
    }


    @Override
    public LookupDefinition getLookupDefinition() {
        LookupDefinition lookupDefinition = new LookupDefinition(this);

        if (this.CVRNummer != null) {
            lookupDefinition.put(LookupDefinition.entityref + LookupDefinition.separator + CompanyEntity.DB_FIELD_CVR, this.CVRNummer, Integer.class);
        }
        if (this.virksomhedsform != null) {
            lookupDefinition.put(CompanyBaseData.DB_FIELD_FORM + LookupDefinition.separator + CompanyFormData.DB_FIELD_FORM + LookupDefinition.separator + CompanyForm.DB_FIELD_CODE, this.virksomhedsform, String.class);
        }
        if (this.reklamebeskyttelse != null) {
            lookupDefinition.put(CompanyBaseData.DB_FIELD_ADVERTPROTECTION + LookupDefinition.separator + BooleanData.DB_FIELD_VALUE, this.reklamebeskyttelse, Boolean.class);
        }
        if (this.virksomhedsnavn != null) {
            lookupDefinition.put(CompanyBaseData.DB_FIELD_NAME + LookupDefinition.separator + TextData.DB_FIELD_VALUE, this.virksomhedsnavn, String.class);
        }
        if (this.telefonnummer != null) {
            lookupDefinition.put(CompanyBaseData.DB_FIELD_PHONENUMBER + LookupDefinition.separator + ContactData.DB_FIELD_VALUE, this.telefonnummer, String.class);
        }
        if (this.telefaxnummer != null) {
            lookupDefinition.put(CompanyBaseData.DB_FIELD_FAXNUMBER + LookupDefinition.separator + ContactData.DB_FIELD_VALUE, this.telefaxnummer, String.class);
        }
        if (this.emailadresse != null) {
            lookupDefinition.put(CompanyBaseData.DB_FIELD_EMAIL + LookupDefinition.separator + ContactData.DB_FIELD_VALUE, this.emailadresse, String.class);
        }
        if (!this.kommunekoder.isEmpty()) {
            StringJoiner sj = new StringJoiner(LookupDefinition.separator);
            sj.add(CompanyBaseData.DB_FIELD_LOCATION_ADDRESS);
            sj.add(AddressData.DB_FIELD_ADDRESS);
            sj.add(Address.DB_FIELD_MUNICIPALITY);
            sj.add(Municipality.DB_FIELD_CODE);
            lookupDefinition.put(sj.toString(), this.kommunekoder, String.class);
        }
        return lookupDefinition;
    }
}
