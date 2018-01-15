package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MetadataRecord {

    @JsonProperty(value = "sammensatStatus")
    private String aggregateStatus;

    public String getAggregateStatus() {
        return this.aggregateStatus;
    }

    public List<CvrBaseRecord> extractRecords(CompanyRecord companyRecord, boolean noDuplicates) {
        /* A subset of data from CVR will store items like 'status' in the metadata object, so we need to retrieve it and reconstruct its assumed bitemporality */
        List<CvrBaseRecord> records = new ArrayList<>();
        if (!noDuplicates || companyRecord.getCompanyStatus().isEmpty()) {
        CompanyStatusRecord statusRecord = this.getCompanyStatusRecord(companyRecord);
            if (statusRecord != null) {
            records.add(statusRecord);
            }
        }
        return records;
    }

    private CompanyStatusRecord getCompanyStatusRecord(CompanyRecord companyRecord) {
        String status = this.getAggregateStatus();
        LocalDate latestStart = LocalDate.MIN;
        LocalDate latestEnd = LocalDate.MIN;
        ArrayList<CvrBaseRecord> timeRecords = new ArrayList<>();
        if (companyRecord.getLifecycle() != null) {
            timeRecords.addAll(companyRecord.getLifecycle());
                }
        if (timeRecords.isEmpty()) {
            if (companyRecord.getNames() != null) {
                timeRecords.addAll(companyRecord.getNames());
            }
        }


        if (!timeRecords.isEmpty()) {
            for (CvrBaseRecord timeRecord : timeRecords) {
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