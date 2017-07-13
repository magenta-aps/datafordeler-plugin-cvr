package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.data.company.CompanyBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitBaseData;
import dk.magenta.datafordeler.cvr.data.companyunit.CompanyUnitEntity;
import dk.magenta.datafordeler.cvr.data.participant.ParticipantBaseData;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lars on 26-06-17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyUnitRecord extends BaseRecord {

    @JsonProperty(value = "pNummer")
    private int pNumber;

    public int getpNumber() {
        return this.pNumber;
    }

    @JsonProperty(value = "reklamebeskyttet")
    private boolean reklamebeskyttet;

    @JsonProperty(value = "enhedsNummer")
    private int enhedsNummer;

    @JsonProperty(value = "navne")
    public List<NameRecord> navne;

    @JsonProperty(value = "beliggenhedsadresse")
    public List<AddressRecord> beliggenhedsadresse;

    public void setBeliggenhedsadresse(List<AddressRecord> beliggenhedsadresse) {
        for (AddressRecord record : beliggenhedsadresse) {
            record.setType(AddressRecord.Type.LOCATION);
        }
        this.beliggenhedsadresse = beliggenhedsadresse;
    }

    @JsonProperty(value = "postadresse")
    public List<AddressRecord> postadresse;

    public void setPostadresse(List<AddressRecord> postadresse) {
        for (AddressRecord record : postadresse) {
            record.setType(AddressRecord.Type.POSTAL);
        }
        this.postadresse = postadresse;
    }

    @JsonProperty(value = "telefonNummer")
    public List<ContactRecord> telefonnummer;

    public void setTelefonnummer(List<ContactRecord> telefonnummer) {
        for (ContactRecord record : telefonnummer) {
            record.setType(ContactRecord.Type.TELEFONNUMMER);
        }
        this.telefonnummer = telefonnummer;
    }

    @JsonProperty(value = "telefaxNummer")
    public List<ContactRecord> telefaxnummer;

    public void setTelefaxnummer(List<ContactRecord> telefaxnummer) {
        for (ContactRecord record : telefaxnummer) {
            record.setType(ContactRecord.Type.TELEFAXNUMMER);
        }
        this.telefaxnummer = telefaxnummer;
    }

    @JsonProperty(value = "elektroniskPost")
    public List<ContactRecord> emailadresse;

    public void setEmailadresse(List<ContactRecord> emailadresse) {
        for (ContactRecord record : emailadresse) {
            record.setType(ContactRecord.Type.EMAILADRESSE);
        }
        this.emailadresse = emailadresse;
    }

    @JsonProperty(value = "livsforloeb")
    public List<LifecycleRecord> livsforloeb;


    @JsonProperty(value = "hovedbranche")
    public List<CompanyIndustryRecord> hovedbranche;

    public void setHovedbranche(List<CompanyIndustryRecord> hovedbranche) {
        for (CompanyIndustryRecord record : hovedbranche) {
            record.setIndex(0);
        }
        this.hovedbranche = hovedbranche;
    }

    @JsonProperty(value = "bibranche1")
    public List<CompanyIndustryRecord> bibranche1;

    public void setBibranche1(List<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(1);
        }
        this.bibranche1 = secondaryIndustryRecords;
    }

    @JsonProperty(value = "bibranche2")
    public List<CompanyIndustryRecord> bibranche2;

    public void setBibranche2(List<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(2);
        }
        this.bibranche2 = secondaryIndustryRecords;
    }

    @JsonProperty(value = "bibranche3")
    public List<CompanyIndustryRecord> bibranche3;

    public void setBibranche3(List<CompanyIndustryRecord> secondaryIndustryRecords) {
        for (CompanyIndustryRecord record : secondaryIndustryRecords) {
            record.setIndex(3);
        }
        this.bibranche3 = secondaryIndustryRecords;
    }

    @JsonProperty(value = "aarsbeskaeftigelse")
    public List<CompanyYearlyNumbersRecord> aarsbeskaeftigelse;

    @JsonProperty(value = "kvartalsbeskaeftigelse")
    public List<CompanyQuarterlyNumbersRecord> kvartalsbeskaeftigelse;

    //@JsonProperty(value = "maanedsbeskaeftigelse")
    //public List<CompanyMonthlyNumbersRecord> monthlyNumbersRecords;

    @JsonProperty(value = "attributter")
    public List<AttributeRecord> attributter;

    @JsonProperty(value = "deltagerRelation")
    private List<CompanyParticipantRelationRecord> deltagere;


    // TODO: Tilføj virksomhedsrelation som mangler i output json
    // enhedstype
    // dataAdgang
    // enhedsNummer
    // reklamebeskyttet
    // deltagere
    // virkningsAktoer
    // brancheAnsvarskode
    // naermesteFremtidigeDato
    // samtId

    public List<BaseRecord> getAll() {
        ArrayList<BaseRecord> list = new ArrayList<>();
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
        if (this.aarsbeskaeftigelse != null) {
            list.addAll(this.aarsbeskaeftigelse);
        }
        if (this.kvartalsbeskaeftigelse != null) {
            list.addAll(this.kvartalsbeskaeftigelse);
        }
        if (this.attributter != null) {
            for (AttributeRecord attributeRecord : this.attributter) {
                list.addAll(attributeRecord.getValues());
            }
        }
        if (this.deltagere != null) {
            list.addAll(this.deltagere);
        }
        return list;
    }


    public UUID generateUUID() {
        return CompanyUnitEntity.generateUUID(this.pNumber);
    }


    @Override
    public void populateBaseData(CompanyBaseData baseData, QueryManager queryManager, Session session) {
        // noop
    }

    @Override
    public void populateBaseData(CompanyUnitBaseData baseData, QueryManager queryManager, Session session) {
        baseData.setReklamebeskyttelse(this.reklamebeskyttet);
        baseData.setPNummer(this.pNumber);
    }

    @Override
    public void populateBaseData(ParticipantBaseData baseData, QueryManager queryManager, Session session) {
        // noop
    }
}
