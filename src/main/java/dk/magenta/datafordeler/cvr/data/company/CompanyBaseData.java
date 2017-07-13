package dk.magenta.datafordeler.cvr.data.company;

import dk.magenta.datafordeler.core.database.DataItem;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.core.database.LookupDefinition;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.cvr.data.DetailData;
import dk.magenta.datafordeler.cvr.data.shared.*;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyStatus;
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
@Table(name="cvr_company_basedata")
public class CompanyBaseData extends DataItem<CompanyEffect, CompanyBaseData> {

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyFormData virksomhedsform;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private CompanyStatusData status;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private LifecycleData livsforloeb;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private BooleanData reklamebeskyttelse;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private IntegerData CVRNummer;

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
    private ContactData hjemmeside;

    @OneToOne(optional = true, cascade = CascadeType.ALL)
    private ContactData obligatoriskEmail;

    @ManyToMany(mappedBy = "companyBases")
    private Set<CompanyUnitLink> enheder = new HashSet<>();

    @ManyToMany(mappedBy = "companyBases")
    private Set<ParticipantLink> deltagere = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<AttributeData> attributter = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    private Set<ParticipantRelationData> deltagerRelation = new HashSet<>();

    public CompanyForm getVirksomhedsform() {
        if(virksomhedsform != null)
            return virksomhedsform.getVirksomhedsform();
        else
            return null;
    }

    public CompanyStatusData getStatus() {
        return status;
    }

    public LifecycleData getLivsforloeb() {
        return livsforloeb;
    }

    public Boolean getReklamebeskyttelse() {
        if(reklamebeskyttelse != null)
            return reklamebeskyttelse.getVaerdi();
        else
            return null;
    }

    public Long getCVRNummer() {
        if(CVRNummer != null)
            return CVRNummer.getVaerdi();
        else
            return null;
    }

    public Address getBeliggenhedsadresse() {
        if(beliggenhedsadresse != null)
            return beliggenhedsadresse.getAdresse();
        else
            return null;
    }

    public Address getPostadresse() {
        if(postadresse != null)
            return postadresse.getAdresse();
        else
            return null;
    }

    public List<YearlyEmployeeNumbersData> getAarsbeskaeftigelse() {
        return aarsbeskaeftigelse;
    }

    public List<QuarterlyEmployeeNumbersData> getKvartalsbeskaeftigelse() {
        return kvartalsbeskaeftigelse;
    }

    public List<MonthlyEmployeeNumbersData> getMaanedsbeskaeftigelse() {
        return maanedsbeskaeftigelse;
    }

    public String getHovedbranche() {
        if(hovedbranche != null)
            return hovedbranche.getBranche().getBranchekode();
        else
            return null;
    }

    public String getBibranche1() {
        if(bibranche1 != null)
            return bibranche1.getBranche().getBranchekode();
        else
            return null;
    }

    public String getBibranche2() {
        if(bibranche2 != null)
            return bibranche2.getBranche().getBranchekode();
        else
            return null;
    }

    public String getBibranche3() {
        if(bibranche3 != null)
            return bibranche3.getBranche().getBranchekode();
        else
            return null;
    }

    public String getVirksomhedsnavn() {
        if(virksomhedsnavn != null)
            return virksomhedsnavn.getVaerdi();
        else
            return null;
    }

    public String getTelefonnummer() {
        if(telefonnummer != null)
            return telefonnummer.getVaerdi();
        else
            return null;
    }

    public String getEmailadresse() {
        if(emailadresse != null)
            return emailadresse.getVaerdi();
        else
            return null;
    }

    public String getTelefaxnummer() {
        if(telefaxnummer != null)
            return telefaxnummer.getVaerdi();
        else
            return null;
    }

    public ContactData getHjemmeside() {
        return hjemmeside;
    }

    public ContactData getObligatoriskEmail() {
        return obligatoriskEmail;
    }

    public Set<CompanyUnitLink> getEnheder() {
        return enheder;
    }

    public Set<ParticipantLink> getDeltagere() {
        return deltagere;
    }

    public Set<AttributeData> getAttributter() {
        return attributter;
    }

    public Set<ParticipantRelationData> getDeltagerRelationer() {
        return deltagerRelation;
    }


    @Override
    public Map<String, Object> asMap() {
        HashMap<String, Object> map = new HashMap<>();
        if (this.virksomhedsform != null) {
            map.put("virksomhedsform", this.virksomhedsform.getVirksomhedsform());
        }
        if (this.status != null) {
            map.put("status", this.status.getStatus());
        }
        if (this.livsforloeb != null) {
            map.put("livsforloeb", this.livsforloeb.asMap());
        }
        if (this.reklamebeskyttelse != null) {
            map.put("reklamebeskyttelse", this.reklamebeskyttelse.getVaerdi());
        }
        if (this.CVRNummer != null) {
            map.put("CVRNummer", this.CVRNummer.getVaerdi());
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
            map.put("telefonnummer", this.telefonnummer);
        }
        if (this.emailadresse != null) {
            map.put("emailadresse", this.emailadresse);
        }
        if (this.telefaxnummer != null) {
            map.put("telefaxnummer", this.telefaxnummer);
        }
        if (this.hjemmeside != null) {
            map.put("hjemmeside", this.hjemmeside);
        }
        if (this.obligatoriskEmail != null) {
            map.put("obligatoriskEmail", this.obligatoriskEmail);
        }
        if (this.enheder != null && !this.enheder.isEmpty()) {
            map.put("enheder", this.enheder);
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

    public void setVirksomhedsform(CompanyForm form) {
        if (this.virksomhedsform == null) {
            this.virksomhedsform = new CompanyFormData();
        }
        this.virksomhedsform.setVirksomhedsform(form);
    }
    public void setStatus(CompanyStatus status) {
        if (this.status == null) {
            this.status = new CompanyStatusData();
        }
        this.status.setStatus(status);
    }
    public void setLivsforloebStart(OffsetDateTime startDate) {
        if (this.livsforloeb == null) {
            this.livsforloeb = new LifecycleData();
        }
        this.livsforloeb.setStartDato(startDate);
    }
    public void setLivsforloebSlut(OffsetDateTime endDate) {
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
    public void setCVRNummer(long unitNumber) {
        if (this.CVRNummer == null) {
            this.CVRNummer = new IntegerData();
        }
        this.CVRNummer.setVaerdi(unitNumber);
    }




    public void setAdresse(Address address) {
        if (this.beliggenhedsadresse == null) {
            this.beliggenhedsadresse = new AddressData();
        }
        this.beliggenhedsadresse.setAdresse(address);
    }
    public void setPostalAddress(Address address) {
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
    public void setbibranche1(Industry industry) {
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
    public void setHjemmeside(String email, boolean secret) {
        if (this.hjemmeside == null) {
            this.hjemmeside = new ContactData(ContactData.Type.HJEMMESIDE);
        }
        this.hjemmeside.setVaerdi(email);
        this.hjemmeside.setHemmelig(secret);
    }
    public void setObligatoriskEmail(String fax, boolean secret) {
        if (this.obligatoriskEmail == null) {
            this.obligatoriskEmail = new ContactData(ContactData.Type.OBLIGATORISK_EMAILADRESSE);
        }
        this.obligatoriskEmail.setVaerdi(fax);
        this.obligatoriskEmail.setHemmelig(secret);
    }

    public void addProduktionsenhed(CompanyUnitLink produktionsenhedlink) {
        this.enheder.add(produktionsenhedlink);
    }
    public void addDeltager(ParticipantLink deltagerlink) {
        this.deltagere.add(deltagerlink);
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
        if (this.virksomhedsform != null) {
            lookupDefinition.putAll("virksomhedsform", this.virksomhedsform.databaseFields());
        }
        if (this.status != null) {
            lookupDefinition.putAll("status", this.status.databaseFields());
        }
        if (this.reklamebeskyttelse != null) {
            lookupDefinition.putAll("reklamebeskyttelse", this.reklamebeskyttelse.databaseFields());
        }
        if (this.CVRNummer != null) {
            lookupDefinition.putAll("CVRNummer", this.CVRNummer.databaseFields());
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
        if (this.deltagerRelation != null) {
            lookupDefinition.putAll("deltagerRelation", DetailData.listDatabaseFields(this.deltagerRelation));
        }
        return lookupDefinition;
    }

    public void forceLoad(Session session) {
        Hibernate.initialize(this.aarsbeskaeftigelse);
        Hibernate.initialize(this.kvartalsbeskaeftigelse);
        Hibernate.initialize(this.maanedsbeskaeftigelse);
        Hibernate.initialize(this.enheder);
        Hibernate.initialize(this.deltagere);
        Hibernate.initialize(this.attributter);
        Hibernate.initialize(this.deltagerRelation);
    }
}
