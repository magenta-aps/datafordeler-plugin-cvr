package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.cvr.data.DetailData;
import dk.magenta.datafordeler.cvr.data.shared.*;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.Industry;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.*;

/**
 * Created by lars on 12-06-17.
 */
@Entity
@Table(name="cvr_companyunit_basedata")
public class CompanyUnitBaseData extends DataItem<CompanyUnitEffect, CompanyUnitBaseData> {

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private LifecycleData livsforloeb;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private BooleanData reklamebeskyttelse;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private IntegerData pNummer;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private AddressData beliggenhedsadresse;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private AddressData postadresse;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy(value = "aar asc")
    private List<YearlyEmployeeNumbersData> aarsbeskaeftigelse;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy(value = "aar asc, kvartal asc")
    private List<QuarterlyEmployeeNumbersData> kvartalsbeskaeftigelse;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy(value = "aar asc, maaned asc")
    private List<MonthlyEmployeeNumbersData> maanedsbeskaeftigelse;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private IndustryData hovedbranche;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private IndustryData bibranche1;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private IndustryData bibranche2;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private IndustryData bibranche3;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private TextData virksomhedsnavn;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private ContactData telefonnummer;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private ContactData emailadresse;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private ContactData telefaxnummer;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private BooleanData primaer;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyUnitCvrData virksomhedscvr;

    @ManyToMany(mappedBy = "companyUnitBases")
    private Set<ParticipantLink> deltagere = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<AttributeData> attributter = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<ParticipantRelationData> deltagerRelation = new HashSet<>();


    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        if (this.livsforloeb != null) {
            map.put("livsforloeb", this.livsforloeb.asMap());
        }
        if (this.reklamebeskyttelse != null) {
            map.put("reklamebeskyttelse", this.reklamebeskyttelse.getVaerdi());
        }
        if (this.pNummer != null) {
            map.put("pNummer", this.pNummer.getVaerdi());
        }
        if (this.beliggenhedsadresse != null) {
            map.put("beliggenhedsadresse", this.beliggenhedsadresse.getAdresse());
        }
        if (this.postadresse != null) {
            map.put("postadresse", this.postadresse.getAdresse());
        }
        if (this.aarsbeskaeftigelse != null) {
            map.put("aarsbeskaeftigelse", this.aarsbeskaeftigelse);
        }
        if (this.kvartalsbeskaeftigelse != null) {
            map.put("kvartalsbeskaeftigelse", this.kvartalsbeskaeftigelse);
        }
        if (this.maanedsbeskaeftigelse != null) {
            map.put("maanedsbeskaeftigelse", this.maanedsbeskaeftigelse);
        }
        if (this.hovedbranche != null) {
            map.put("hovedbranche", this.hovedbranche.getBranche());
        }
        if (this.bibranche1 != null) {
            map.put("bibranche1", this.bibranche1.getBranche());
        }
        if (this.bibranche2 != null) {
            map.put("bibranche2", this.bibranche2.getBranche());
        }
        if (this.bibranche3 != null) {
            map.put("bibranche3", this.bibranche3.getBranche());
        }
        if (this.virksomhedsnavn != null) {
            map.put("virksomhedsnavn", this.virksomhedsnavn.getVaerdi());
        }
        if (this.telefonnummer != null) {
            map.put("telefonnummer", this.telefonnummer.asMap());
        }
        if (this.emailadresse != null) {
            map.put("emailadresse", this.emailadresse.asMap());
        }
        if (this.telefaxnummer != null) {
            map.put("telefaxnummer", this.telefaxnummer.asMap());
        }
        if (this.primaer != null) {
            map.put("primaer", this.primaer.getVaerdi());
        }
        if (this.virksomhedscvr != null) {
            map.put("virksomhedscvr", this.virksomhedscvr);
        }
        if (this.deltagere != null && !this.deltagere.isEmpty()) {
            map.put("deltagere", this.deltagere);
        }
        if (this.attributter != null && !this.attributter.isEmpty()) {
            map.put("attributter", this.attributter);
        }
        if (this.deltagerRelation != null && !this.deltagerRelation.isEmpty()) {
            map.put("deltagerRelation", this.deltagerRelation);
        }
        return map;
    }

    public void setLivsforloebStart(OffsetDateTime startDate) {
        if (this.livsforloeb == null) {
            this.livsforloeb = new LifecycleData();
        }
        this.livsforloeb.setStartDato(startDate);
    }
    public void setLivsforloebStop(OffsetDateTime endDate) {
        if (this.livsforloeb == null) {
            this.livsforloeb = new LifecycleData();
        }
        this.livsforloeb.setSlutDato(endDate);
    }
    public void setReklamebeskyttelse(boolean advertProtection) {
        if (this.reklamebeskyttelse == null) {
            this.reklamebeskyttelse = new BooleanData(BooleanData.Type.REKLAME_BESKYTTELSE);
        }
        this.reklamebeskyttelse.setVaerdi(advertProtection);
    }
    public void setPNummer(long unitNumber) {
        if (this.pNummer == null) {
            this.pNummer = new IntegerData();
        }
        this.pNummer.setVaerdi(unitNumber);
    }
    public void setBeliggenhedsadresse(Address address) {
        if (this.beliggenhedsadresse == null) {
            this.beliggenhedsadresse = new AddressData();
        }
        this.beliggenhedsadresse.setAdresse(address);
    }
    public void setPostadresse(Address address) {
        if (this.postadresse == null) {
            this.postadresse = new AddressData();
        }
        this.postadresse.setAdresse(address);
    }

    public void addAarsbeskaeftigelse(int year, Integer employeesLow, Integer employeesHigh, Integer fulltimeEquivalentLow, Integer fulltimeEquivalentHigh, Integer includingOwnersLow, Integer includingOwnersHigh) throws ParseException {
        if (this.aarsbeskaeftigelse == null) {
            //this.aarsbeskaeftigelse = new CompanyYearlyEmployeeNumbersData();
            this.aarsbeskaeftigelse = new ArrayList<>();
        }
        YearlyEmployeeNumbersData yearlyEmployeeNumbersData = new YearlyEmployeeNumbersData();
        yearlyEmployeeNumbersData.setAar(year);
        yearlyEmployeeNumbersData.setEmployeesLow(employeesLow);
        yearlyEmployeeNumbersData.setEmployeesHigh(employeesHigh);
        yearlyEmployeeNumbersData.setFullTimeEquivalentLow(fulltimeEquivalentLow);
        yearlyEmployeeNumbersData.setFullTimeEquivalentHigh(fulltimeEquivalentHigh);
        yearlyEmployeeNumbersData.setIncludingOwnersLow(includingOwnersLow);
        yearlyEmployeeNumbersData.setIncludingOwnersHigh(includingOwnersHigh);
        this.aarsbeskaeftigelse.add(yearlyEmployeeNumbersData);
    }
    public void addKvartalsbeskaeftigelse(int year, int quarter, Integer employeesLow, Integer employeesHigh, Integer fulltimeEquivalentLow, Integer fulltimeEquivalentHigh, Integer includingOwnersLow, Integer includingOwnersHigh) throws ParseException {
        if (this.kvartalsbeskaeftigelse == null) {
            //this.kvartalsbeskaeftigelse = new CompanyQuarterlyEmployeeNumbersData();
            this.kvartalsbeskaeftigelse = new ArrayList<>();
        }
        QuarterlyEmployeeNumbersData quarterlyEmployeeNumbersData = new QuarterlyEmployeeNumbersData();
        quarterlyEmployeeNumbersData.setAar(year);
        quarterlyEmployeeNumbersData.setKvartal(quarter);
        quarterlyEmployeeNumbersData.setEmployeesLow(employeesLow);
        quarterlyEmployeeNumbersData.setEmployeesHigh(employeesHigh);
        quarterlyEmployeeNumbersData.setFullTimeEquivalentLow(fulltimeEquivalentLow);
        quarterlyEmployeeNumbersData.setFullTimeEquivalentHigh(fulltimeEquivalentHigh);
        quarterlyEmployeeNumbersData.setIncludingOwnersLow(includingOwnersLow);
        quarterlyEmployeeNumbersData.setIncludingOwnersHigh(includingOwnersHigh);
        this.kvartalsbeskaeftigelse.add(quarterlyEmployeeNumbersData);
    }
    public void addMaanedsbeskaeftigelse(int year, int month, Integer employeesLow, Integer employeesHigh, Integer fulltimeEquivalentLow, Integer fulltimeEquivalentHigh, Integer includingOwnersLow, Integer includingOwnersHigh) throws ParseException {
        if (this.maanedsbeskaeftigelse == null) {
            //this.maanedsbeskaeftigelse = new CompanyMonthlyEmployeeNumbersData();
            this.maanedsbeskaeftigelse = new ArrayList<>();
        }
        MonthlyEmployeeNumbersData monthlyEmployeeNumbersData = new MonthlyEmployeeNumbersData();
        monthlyEmployeeNumbersData.setAar(year);
        monthlyEmployeeNumbersData.setMaaned(month);
        monthlyEmployeeNumbersData.setEmployeesLow(employeesLow);
        monthlyEmployeeNumbersData.setEmployeesHigh(employeesHigh);
        monthlyEmployeeNumbersData.setFullTimeEquivalentLow(fulltimeEquivalentLow);
        monthlyEmployeeNumbersData.setFullTimeEquivalentHigh(fulltimeEquivalentHigh);
        monthlyEmployeeNumbersData.setIncludingOwnersLow(includingOwnersLow);
        monthlyEmployeeNumbersData.setIncludingOwnersHigh(includingOwnersHigh);
        this.maanedsbeskaeftigelse.add(monthlyEmployeeNumbersData);
    }

    public void setHovedbranche(Industry industry) {
        if (this.hovedbranche == null) {
            this.hovedbranche = new IndustryData(true);
        }
        this.hovedbranche.setBranche(industry);
    }
    public void setBibranche1(Industry industry) {
        if (this.bibranche1 == null) {
            this.bibranche1 = new IndustryData(false);
        }
        this.bibranche1.setBranche(industry);
    }
    public void setBibranche2(Industry industry) {
        if (this.bibranche2 == null) {
            this.bibranche2 = new IndustryData(false);
        }
        this.bibranche2.setBranche(industry);
    }
    public void setBibranche3(Industry industry) {
        if (this.bibranche3 == null) {
            this.bibranche3 = new IndustryData(false);
        }
        this.bibranche3.setBranche(industry);
    }

    public void setVirksomhedsnavn(String name) {
        if (this.virksomhedsnavn == null) {
            this.virksomhedsnavn = new TextData(TextData.Type.NAVN);
        }
        this.virksomhedsnavn.setVaerdi(name);
    }
    public void setTelefonnummer(String phone, boolean secret) {
        if (this.telefonnummer == null) {
            this.telefonnummer = new ContactData(ContactData.Type.TELEFONNUMMER);
        }
        this.telefonnummer.setVaerdi(phone);
        this.telefonnummer.setHemmelig(secret);
    }
    public void setEmailadresse(String email, boolean secret) {
        if (this.emailadresse == null) {
            this.emailadresse = new ContactData(ContactData.Type.EMAILADRESSE);
        }
        this.emailadresse.setVaerdi(email);
        this.emailadresse.setHemmelig(secret);
    }
    public void setTelefaxnummer(String fax, boolean secret) {
        if (this.telefaxnummer == null) {
            this.telefaxnummer = new ContactData(ContactData.Type.TELEFAXNUMMER);
        }
        this.telefaxnummer.setVaerdi(fax);
        this.telefaxnummer.setHemmelig(secret);
    }

    public void setIsPrimaer(boolean isPrimary) {
        if (this.primaer == null) {
            this.primaer = new BooleanData(BooleanData.Type.ER_PRIMAER_ENHED);
        }
        this.primaer.setVaerdi(isPrimary);
    }
    public void setVirksomhedscvr(int cvrNumber) {
        if (this.virksomhedscvr == null) {
            this.virksomhedscvr = new CompanyUnitCvrData();
        }
        this.virksomhedscvr.setVaerdi(cvrNumber);
    }
    public void addDeltagere(ParticipantLink participantLink) {
        this.deltagere.add(participantLink);
    }

    public void addAttribut(String type, String valueType, String value, int sequenceNumber) {
        AttributeData attributeData = new AttributeData();
        attributeData.setType(type);
        attributeData.setValueType(valueType);
        attributeData.setValue(value);
        attributeData.setSequenceNumber(sequenceNumber);
        this.addAttribut(attributeData);
    }
    public void addAttribut(AttributeData attributeData) {
        this.attributter.add(attributeData);
    }

    public void addDeltagerRelation(Identification participant, Set<Identification> organizations) {
        ParticipantRelationData participantRelationData = new ParticipantRelationData();
        participantRelationData.setDeltager(participant);
        for (Identification organization : organizations) {
            participantRelationData.addOrganization(organization);
        }
        this.deltagerRelation.add(participantRelationData);
    }

    @Override
    public LookupDefinition getLookupDefinition() {
        LookupDefinition lookupDefinition = new LookupDefinition();
        lookupDefinition.setMatchNulls(true);
        if (this.livsforloeb != null) {
            lookupDefinition.putAll("livsforloeb", this.livsforloeb.databaseFields());
        }
        if (this.reklamebeskyttelse != null) {
            lookupDefinition.putAll("reklamebeskyttelse", this.reklamebeskyttelse.databaseFields());
        }
        if (this.pNummer != null) {
            lookupDefinition.putAll("pNummer", this.pNummer.databaseFields());
        }
        if (this.beliggenhedsadresse != null) {
            lookupDefinition.putAll("beliggenhedsadresse", this.beliggenhedsadresse.databaseFields());
        }
        if (this.postadresse != null) {
            lookupDefinition.putAll("postadresse", this.postadresse.databaseFields());
        }
        /*if (this.aarsbeskaeftigelse != null) {
            lookupDefinition.putAll("aarsbeskaeftigelse", this.aarsbeskaeftigelse.databaseFields());
        }*/
        /*if (this.kvartalsbeskaeftigelse != null) {
            lookupDefinition.putAll("kvartalsbeskaeftigelse", this.kvartalsbeskaeftigelse.databaseFields());
        }*/
        /*if (this.maanedsbeskaeftigelse != null) {
            lookupDefinition.putAll("maanedsbeskaeftigelse", this.maanedsbeskaeftigelse.databaseFields());
        }*/
        if (this.hovedbranche != null) {
            lookupDefinition.putAll("hovedbranche", this.hovedbranche.databaseFields());
        }
        if (this.bibranche1 != null) {
            lookupDefinition.putAll("bibranche1", this.bibranche1.databaseFields());
        }
        if (this.bibranche2 != null) {
            lookupDefinition.putAll("bibranche2", this.bibranche2.databaseFields());
        }
        if (this.bibranche3 != null) {
            lookupDefinition.putAll("bibranche3", this.bibranche3.databaseFields());
        }
        if (this.virksomhedsnavn != null) {
            lookupDefinition.putAll("virksomhedsnavn", this.virksomhedsnavn.databaseFields());
        }
        if (this.telefonnummer != null) {
            lookupDefinition.putAll("telefonnummer", this.telefonnummer.databaseFields());
        }
        if (this.emailadresse != null) {
            lookupDefinition.putAll("emailadresse", this.emailadresse.databaseFields());
        }
        if (this.telefaxnummer != null) {
            lookupDefinition.putAll("telefaxnummer", this.telefaxnummer.databaseFields());
        }
        if (this.primaer != null) {
            lookupDefinition.putAll("primaer", this.primaer.databaseFields());
        }
        if (this.virksomhedscvr != null) {
            lookupDefinition.putAll("virksomhedscvr", this.virksomhedscvr.databaseFields());
        }
        if (this.deltagerRelation != null) {
            lookupDefinition.putAll("deltagerRelation", DetailData.listDatabaseFields(this.deltagerRelation));
        }
        return lookupDefinition;
    }

    public void forceLoad(Session session) {
        Hibernate.initialize(this.aarsbeskaeftigelse);
        Hibernate.initialize(this.kvartalsbeskaeftigelse);
        Hibernate.initialize(this.maanedsbeskaeftigelse);
        Hibernate.initialize(this.deltagere);
        Hibernate.initialize(this.attributter);
        Hibernate.initialize(this.deltagerRelation);
    }
}
