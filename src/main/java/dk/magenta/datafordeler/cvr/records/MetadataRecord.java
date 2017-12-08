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

    public List<CvrBaseRecord> extractRecords(CompanyRecord companyRecord) {
        /* A subset of data from CVR will store items like 'status' in the metadata object, so we need to retrieve it and reconstruct its assumed bitemporality */
        List<CvrBaseRecord> records = new ArrayList<>();
        CompanyStatusRecord statusRecord = this.getCompanyStatusRecord(companyRecord);
        if (statusRecord != null) {
            records.add(statusRecord);
        }
        return records;
    }

    private CompanyStatusRecord getCompanyStatusRecord(CompanyRecord companyRecord) {
        String status = this.getAggregateStatus();
        LocalDate latestLifecycleStart = LocalDate.MIN;
        LocalDate latestLifecycleEnd = LocalDate.MIN;
        List<LifecycleRecord> lifecycleRecords = companyRecord.getLifecycle();
        if (lifecycleRecords != null && !lifecycleRecords.isEmpty()) {
            for (LifecycleRecord lifecycleRecord : lifecycleRecords) {
                if (lifecycleRecord.getValidFrom() != null && lifecycleRecord.getValidFrom().isAfter(latestLifecycleStart)) {
                    latestLifecycleStart = lifecycleRecord.getValidFrom();
                }
                if (latestLifecycleEnd != null && (lifecycleRecord.getValidTo() == null || lifecycleRecord.getValidTo().isAfter(latestLifecycleEnd))) {
                    latestLifecycleEnd = lifecycleRecord.getValidTo();
                }
            }
        }
        if (status != null && !status.isEmpty()) {
            CompanyStatusRecord statusRecord = new CompanyStatusRecord();
            statusRecord.setStatus(status);
            CvrRecordPeriod recordPeriod = new CvrRecordPeriod();
            LocalDate statusUpdatedTime;
            if (status.equalsIgnoreCase("ophørt")) {
                statusUpdatedTime = latestLifecycleEnd;
            } else {
                statusUpdatedTime = latestLifecycleStart;
            }
            recordPeriod.setValidFrom(statusUpdatedTime);
            recordPeriod.setValidTo(null);
            statusRecord.setValidity(recordPeriod);
            statusRecord.setLastUpdated(OffsetDateTime.of(statusUpdatedTime, LocalTime.MIDNIGHT, ZoneOffset.UTC));
            return statusRecord;
        }
        return null;
    }
}
