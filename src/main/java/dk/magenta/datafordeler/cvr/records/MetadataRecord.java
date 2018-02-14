package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.DatabaseEntry;
import org.hibernate.Session;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cvr_record_metadata", indexes = {
        @Index(name = "cvr_record_metadata_company", columnList = MetadataRecord.DB_FIELD_COMPANY + DatabaseEntry.REF),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetadataRecord extends CvrBitemporalDataRecord {

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
