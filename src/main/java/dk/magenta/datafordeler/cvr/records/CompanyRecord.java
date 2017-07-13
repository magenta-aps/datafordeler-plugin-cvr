package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lars on 26-06-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyRecord extends BaseRecord {

    @JsonProperty(value = "cvrNummer")
    private int CVRNummer;

    public int getCVRNummer() {
        return this.CVRNummer;
    }

    @JsonProperty(value = "reklamebeskyttet")
    private boolean reklamebeskyttet;

    @JsonProperty(value = "enhedsNummer")
    private int enhedsnummer;

    @JsonProperty(value = "enhedstype")
    private String enhedstype;

    @JsonProperty(value = "navne")
    private List<NameRecord> navne;

    @JsonProperty(value = "beliggenhedsadresse")
    private List<AddressRecord> beliggenhedsadresse;

    public void setBeliggenhedsadresse(List<AddressRecord> beliggenhedsadresse) {
        for (AddressRecord record : beliggenhedsadresse) {
            record.setType(AddressRecord.Type.LOCATION);
        }
        this.beliggenhedsadresse = beliggenhedsadresse;
    }

    @JsonProperty(value = "postadresse")
    private List<AddressRecord> postadresse;

    public void setPostadresse(List<AddressRecord> postadresse) {
        for (AddressRecord record : postadresse) {
            record.setType(AddressRecord.Type.POSTAL);
        }
        this.postadresse = postadresse;
    }

    @JsonProperty(value = "telefonNummer")
    private List<ContactRecord> telefonnummer;

    public void setTelefonnummer(List<ContactRecord> telefonnummer) {
        for (ContactRecord record : telefonnummer) {
            record.setType(ContactRecord.Type.TELEFONNUMMER);
        }
        this.telefonnummer = telefonnummer;
    }

    @JsonProperty(value = "telefaxNummer")
    private List<ContactRecord> telefaxnummer;

    public void setTelefaxnummer(List<ContactRecord> telefaxnummer) {
        for (ContactRecord record : telefaxnummer) {
            record.setType(ContactRecord.Type.TELEFAXNUMMER);
        }
        this.telefaxnummer = telefaxnummer;
    }

    @JsonProperty(value = "elektroniskPost")
    private List<ContactRecord> emailadresse;

    public void setEmailadresse(List<ContactRecord> emailadresse) {
        for (ContactRecord record : emailadresse) {
            record.setType(ContactRecord.Type.EMAILADRESSE);
        }
        this.emailadresse = emailadresse;
    }

    @JsonProperty(value = "hjemmeside")
    private List<ContactRecord> hjemmeside;

    public void setHjemmeside(List<ContactRecord> hjemmeside) {
        for (ContactRecord record : hjemmeside) {
            record.setType(ContactRecord.Type.HJEMMESIDE);
        }
        this.hjemmeside = hjemmeside;
    }

    @JsonProperty(value = "obligatoriskEmail")
    private List<ContactRecord> obligatoriskEmailadresse;

    public void setObligatoriskEmailadresse(List<ContactRecord> obligatoriskEmailadresse) {
        for (ContactRecord record : obligatoriskEmailadresse) {
            record.setType(ContactRecord.Type.OBLIGATORISK_EMAILADRESSE);
        }
        this.obligatoriskEmailadresse = obligatoriskEmailadresse;
    }

    @JsonProperty(value = "livsforloeb")
    private List<LifecycleRecord> livsforloeb;


    @JsonProperty(value = "hovedbranche")
    private List<CompanyIndustryRecord> hovedbranche;

    public void setHovedbranche(List<CompanyIndustryRecord> hovedbranche) {
        for (CompanyIndustryRecord record : hovedbranche) {
            record.setIndex(0);
        }
        this.hovedbranche = hovedbranche;
    }

    @JsonProperty(value = "bibranche1")
    private List<CompanyIndustryRecord> bibranche1;

    public void setBibranche1(List<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(1);
        }
        this.bibranche1 = secondaryIndustryRecords;
    }

    @JsonProperty(value = "bibranche2")
    private List<CompanyIndustryRecord> bibranche2;

    public void setBibranche2(List<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(2);
        }
        this.bibranche2 = secondaryIndustryRecords;
    }

    @JsonProperty(value = "bibranche3")
    private List<CompanyIndustryRecord> bibranche3;

    public void setBibranche3(List<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(3);
        }
        this.bibranche3 = secondaryIndustryRecords;
    }

    @JsonProperty(value = "virksomhedsstatus")
    private List<CompanyStatusRecord> virksomhedsstatus;


    @JsonProperty(value = "virksomhedsform")
    private List<CompanyFormRecord> virksomhedsform;

    @JsonProperty(value = "aarsbeskaeftigelse")
    private List<CompanyYearlyNumbersRecord> aarsbeskaeftigelse;

    @JsonProperty(value = "kvartalsbeskaeftigelse")
    private List<CompanyQuarterlyNumbersRecord> kvartalsbeskaeftigelse;

    @JsonProperty(value = "maanedsbeskaeftigelse")
    private List<CompanyMonthlyNumbersRecord> maanedsbeskaeftigelse;

    @JsonProperty(value = "attributter")
    private List<AttributeRecord> attributter;

    @JsonProperty(value = "penheder")
    private List<CompanyUnitLinkRecord> penheder;

    @JsonProperty(value = "deltagerRelation")
    private List<CompanyParticipantRelationRecord> deltagere;

    public List<BaseRecord> getAll() {
        ArrayList<BaseRecord> list = new ArrayList<>();
        list.add(this);
        if (this.navne != null) {
            list.addAll(this.navne);
        }
        if (this.beliggenhedsadresse != null) {
            list.addAll(this.beliggenhedsadresse);
        }
        if (this.postadresse != null) {
            list.addAll(this.postadresse);
        }
        if (this.telefonnummer != null) {
            list.addAll(this.telefonnummer);
        }
        if (this.telefaxnummer != null) {
            list.addAll(this.telefaxnummer);
        }
        if (this.emailadresse != null) {
            list.addAll(this.emailadresse);
        }
        if (this.hjemmeside != null) {
            list.addAll(this.hjemmeside);
        }
        if (this.obligatoriskEmailadresse != null) {
            list.addAll(this.obligatoriskEmailadresse);
        }
        if (this.livsforloeb != null) {
            list.addAll(this.livsforloeb);
        }
        if (this.hovedbranche != null) {
            list.addAll(this.hovedbranche);
        }
        if (this.bibranche1 != null) {
            list.addAll(this.bibranche1);
        }
        if (this.bibranche2 != null) {
            list.addAll(this.bibranche2);
        }
        if (this.bibranche3 != null) {
            list.addAll(this.bibranche3);
        }
        if (this.virksomhedsstatus != null) {
            list.addAll(this.virksomhedsstatus);
        }
        if (this.virksomhedsform != null) {
            list.addAll(this.virksomhedsform);
        }
        if (this.aarsbeskaeftigelse != null) {
            list.addAll(this.aarsbeskaeftigelse);
        }
        if (this.kvartalsbeskaeftigelse != null) {
            list.addAll(this.kvartalsbeskaeftigelse);
        }
        if (this.maanedsbeskaeftigelse != null) {
            list.addAll(this.maanedsbeskaeftigelse);
        }
        if (this.attributter != null) {
            for (AttributeRecord attributeRecord : this.attributter) {
                list.addAll(attributeRecord.getValues());
            }
        }
        if (this.penheder != null) {
            list.addAll(this.penheder);
        }
        if (this.deltagere != null) {
            list.addAll(this.deltagere);
        }
        return list;
    }

    public UUID generateUUID() {
        return CompanyEntity.generateUUID(this.CVRNummer);
    }

    @Override
    public void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        baseData.setReklamebeskyttelse(this.reklamebeskyttet);
        baseData.setCVRNummer(this.CVRNummer);
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, QueryManager queryManager, Session session) {
        // noop
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, QueryManager queryManager, Session session) {
        // noop
    }
}
