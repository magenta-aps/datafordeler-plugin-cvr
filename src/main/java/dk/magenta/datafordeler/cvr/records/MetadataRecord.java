package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.Session;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class MetadataRecord extends CvrBitemporalDataRecord {

    public static final String DB_FIELD_AGGREGATE_STATUS = "aggregateStatus";
    public static final String IO_FIELD_AGGREGATE_STATUS = "sammensatStatus";

    @Column(name = DB_FIELD_AGGREGATE_STATUS)
    @JsonProperty(value = IO_FIELD_AGGREGATE_STATUS)
    private String aggregateStatus;

    public String getAggregateStatus() {
        return this.aggregateStatus;
    }



    public static final String DB_FIELD_NEWEST_NAME = "newestName";
    public static final String IO_FIELD_NEWEST_NAME = "nyesteNavn";

    @OneToOne(targetEntity = NameRecord.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(value = IO_FIELD_NEWEST_NAME)
    private NameRecord newestName;




    public static final String DB_FIELD_NEWEST_FORM = "newestForm";
    public static final String IO_FIELD_NEWEST_FORM = "nyesteVirksomhedsform";

    @OneToOne(targetEntity = CompanyFormRecord.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(value = IO_FIELD_NEWEST_FORM)
    private CompanyFormRecord newestForm;



    public static final String DB_FIELD_NEWEST_LOCATION = "newestLocation";
    public static final String IO_FIELD_NEWEST_LOCATION = "nyesteBeliggenhedsadresse";

    @OneToOne(targetEntity = AddressRecord.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(value = IO_FIELD_NEWEST_LOCATION)
    private AddressRecord newestLocation;




    public static final String DB_FIELD_NEWEST_PRIMARY_INDUSTRY = "newestPrimaryIndustry";
    public static final String IO_FIELD_NEWEST_PRIMARY_INDUSTRY = "nyesteHovedbranche";

    @OneToOne(targetEntity = CompanyIndustryRecord.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(value = IO_FIELD_NEWEST_PRIMARY_INDUSTRY)
    private CompanyIndustryRecord newestPrimaryIndustry;

    public void setNewestPrimaryIndustry(CompanyIndustryRecord newestPrimaryIndustry) {
        this.newestPrimaryIndustry = newestPrimaryIndustry;
        if (newestPrimaryIndustry != null) {
            newestPrimaryIndustry.setIndex(0);
        }
    }



    public static final String DB_FIELD_NEWEST_SECONDARY_INDUSTRY1 = "newestSecondaryIndustry1";
    public static final String IO_FIELD_NEWEST_SECONDARY_INDUSTRY1 = "nyesteBibranche1";

    @OneToOne(targetEntity = CompanyIndustryRecord.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(value = IO_FIELD_NEWEST_SECONDARY_INDUSTRY1)
    private CompanyIndustryRecord newestSecondaryIndustry1;

    public void setNewestSecondaryIndustry1(CompanyIndustryRecord newestSecondaryIndustry1) {
        this.newestSecondaryIndustry1 = newestSecondaryIndustry1;
        if (newestSecondaryIndustry1 != null) {
            newestSecondaryIndustry1.setIndex(1);
        }
    }



    public static final String DB_FIELD_NEWEST_SECONDARY_INDUSTRY2 = "newestSecondaryIndustry2";
    public static final String IO_FIELD_NEWEST_SECONDARY_INDUSTRY2 = "nyesteBibranche2";

    @OneToOne(targetEntity = CompanyIndustryRecord.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(value = IO_FIELD_NEWEST_SECONDARY_INDUSTRY2)
    private CompanyIndustryRecord newestSecondaryIndustry2;

    public void setNewestSecondaryIndustry2(CompanyIndustryRecord newestSecondaryIndustry2) {
        this.newestSecondaryIndustry2 = newestSecondaryIndustry2;
        if (newestSecondaryIndustry2 != null) {
            newestSecondaryIndustry2.setIndex(2);
        }
    }



    public static final String DB_FIELD_NEWEST_SECONDARY_INDUSTRY3 = "newestSecondaryIndustry3";
    public static final String IO_FIELD_NEWEST_SECONDARY_INDUSTRY3 = "nyesteBibranche3";

    @OneToOne(targetEntity = CompanyIndustryRecord.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(value = IO_FIELD_NEWEST_SECONDARY_INDUSTRY3)
    private CompanyIndustryRecord newestSecondaryIndustry3;

    public void setNewestSecondaryIndustry3(CompanyIndustryRecord newestSecondaryIndustry3) {
        this.newestSecondaryIndustry3 = newestSecondaryIndustry3;
        if (newestSecondaryIndustry3 != null) {
            newestSecondaryIndustry3.setIndex(3);
        }
    }



    public static final String DB_FIELD_NEWEST_STATUS = "newestStatus";
    public static final String IO_FIELD_NEWEST_STATUS = "nyesteStatus";

    @OneToOne(targetEntity = StatusRecord.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty(value = IO_FIELD_NEWEST_STATUS)
    private StatusRecord newestStatus;




    public static final String DB_FIELD_NEWEST_CONTACT_DATA = "newestContactData";
    public static final String IO_FIELD_NEWEST_CONTACT_DATA = "nyesteKontaktoplysninger";

    @JsonIgnore
    public abstract Set<MetadataContactRecord> getMetadataContactRecords();

    public abstract void setMetadataContactRecords(Set<MetadataContactRecord> metadataContactRecords);

    @JsonProperty(IO_FIELD_NEWEST_CONTACT_DATA)
    private void setMetadataContactData(Set<String> contactData) {
        HashSet<String> contacts = new HashSet<>(contactData);
        HashSet<MetadataContactRecord> remove = new HashSet<>();
        Set<MetadataContactRecord> contactRecords = this.getMetadataContactRecords();
        for (MetadataContactRecord contactRecord : contactRecords) {
            String data = contactRecord.getData();
            if (contacts.contains(data)) {
                contacts.remove(data);
            } else {
                remove.add(contactRecord);
            }
        }
        contactRecords.removeAll(remove);
        for (String data : contacts) {
            MetadataContactRecord newContactRecord = new MetadataContactRecord();
            newContactRecord.setData(data);
            contactRecords.add(newContactRecord);
        }
        this.setMetadataContactRecords(contactRecords);
    }

    @JsonProperty(IO_FIELD_NEWEST_CONTACT_DATA)
    private Set<String> getMetadataContactData() {
        HashSet<String> contacts = new HashSet<>();
        for (MetadataContactRecord metadataContactRecord : this.getMetadataContactRecords()) {
            contacts.add(metadataContactRecord.getData());
        }
        return contacts;
    }



    public static final String DB_FIELD_UNIT_COUNT = "unitCount";
    public static final String IO_FIELD_UNIT_COUNT = "antalPenheder";

    @Column(name = DB_FIELD_UNIT_COUNT)
    @JsonProperty(value = IO_FIELD_UNIT_COUNT)
    private int unitCount;



    public static final String DB_FIELD_NEWEST_YEARLY_NUMBERS = "newestYearlyNumbers";
    public static final String IO_FIELD_NEWEST_YEARLY_NUMBERS = "nyesteAarsbeskaeftigelse";

    @OneToOne(cascade = CascadeType.ALL, targetEntity = CompanyYearlyNumbersRecord.class)
    @JsonProperty(value = IO_FIELD_NEWEST_YEARLY_NUMBERS)
    private CompanyYearlyNumbersRecord newestYearlyNumbers;



    public static final String DB_FIELD_NEWEST_QUARTERLY_NUMBERS = "newestQuarterlyNumbers";
    public static final String IO_FIELD_NEWEST_QUARTERLY_NUMBERS = "nyesteKvartalsbeskaeftigelse";

    @OneToOne(cascade = CascadeType.ALL, targetEntity = CompanyQuarterlyNumbersRecord.class)
    @JsonProperty(value = IO_FIELD_NEWEST_QUARTERLY_NUMBERS)
    private CompanyQuarterlyNumbersRecord newestQuarterlyNumbers;



    public static final String DB_FIELD_NEWEST_MONTHLY_NUMBERS = "newestMonthlyNumbers";
    public static final String IO_FIELD_NEWEST_MONTHLY_NUMBERS = "nyesteMaanedsbeskaeftigelse";

    @OneToOne(cascade = CascadeType.ALL, targetEntity = CompanyMonthlyNumbersRecord.class)
    @JsonProperty(value = IO_FIELD_NEWEST_MONTHLY_NUMBERS)
    private CompanyMonthlyNumbersRecord newestMonthlyNumbers;



    public static final String DB_FIELD_FOUNDING_DATE = "foundingDate";
    public static final String IO_FIELD_FOUNDING_DATE = "stiftelsesDato";

    @Column(name = DB_FIELD_FOUNDING_DATE)
    @JsonProperty(value = IO_FIELD_FOUNDING_DATE)
    private LocalDate foundingDate;



    public static final String DB_FIELD_EFFECT_DATE = "effectDate";
    public static final String IO_FIELD_EFFECT_DATE = "virkningsDato";

    @Column(name = DB_FIELD_EFFECT_DATE)
    @JsonProperty(value = IO_FIELD_EFFECT_DATE)
    private LocalDate effectDate;



    public void wire(Session session) {
        if (this.newestForm != null) {
            this.newestForm.wire(session);
        }
        if (this.newestLocation != null) {
            this.newestLocation.wire(session);
        }
    }

    public List<CvrRecord> extractRecords(CompanyRecord companyRecord, boolean noDuplicates) {
        /* A subset of data from CVR will store items like 'status' in the metadata object, so we need to retrieve it and reconstruct its assumed bitemporality */
        List<CvrRecord> records = new ArrayList<>();
        if (!noDuplicates || companyRecord.getCompanyStatus().isEmpty()) {
        CompanyStatusRecord statusRecord = this.getCompanyStatusRecord(companyRecord);
            if (statusRecord != null) {
            records.add(statusRecord);
            }
        }
        return records;
    }

    public CompanyStatusRecord getCompanyStatusRecord(CompanyRecord companyRecord) {
        String status = this.getAggregateStatus();
        LocalDate latestStart = LocalDate.MIN;
        LocalDate latestEnd = LocalDate.MIN;
        ArrayList<CvrBitemporalRecord> timeRecords = new ArrayList<>();
        if (companyRecord.getLifecycle() != null) {
            timeRecords.addAll(companyRecord.getLifecycle());
                }
        if (timeRecords.isEmpty()) {
            if (companyRecord.getNames() != null) {
                timeRecords.addAll(companyRecord.getNames());
            }
        }


        if (!timeRecords.isEmpty()) {
            for (CvrBitemporalRecord timeRecord : timeRecords) {
                if (timeRecord.getValidFrom() != null && timeRecord.getValidFrom().isAfter(latestStart)) {
                    latestStart = timeRecord.getValidFrom();
                }
                if (latestEnd != null && (timeRecord.getValidTo() == null || timeRecord.getValidTo().isAfter(latestEnd))) {
                    latestEnd = timeRecord.getValidTo();
                }
            }
        }
        if (status != null && !status.isEmpty()) {
            CompanyStatusRecord statusRecord = new CompanyStatusRecord();
            statusRecord.setStatus(status);
            CvrRecordPeriod recordPeriod = new CvrRecordPeriod();
            LocalDate statusUpdatedTime;
            if (status.equalsIgnoreCase("ophørt")) {
                statusUpdatedTime = latestEnd;
            } else {
                statusUpdatedTime = latestStart;
            }
            if (statusUpdatedTime == LocalDate.MIN) {
                statusUpdatedTime = null;
            }
            recordPeriod.setValidFrom(statusUpdatedTime);
            recordPeriod.setValidTo(null);
            statusRecord.setValidity(recordPeriod);
            statusRecord.setLastUpdated(statusUpdatedTime != null ? OffsetDateTime.of(statusUpdatedTime, LocalTime.MIDNIGHT, ZoneOffset.UTC) : null);
            return statusRecord;
        }
        return null;
    }
}
