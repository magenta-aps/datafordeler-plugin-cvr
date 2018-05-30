package dk.magenta.datafordeler.cvr.records.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.magenta.datafordeler.core.database.Entity;
import dk.magenta.datafordeler.core.fapi.Query;
import dk.magenta.datafordeler.cvr.data.Bitemporality;
import dk.magenta.datafordeler.cvr.records.CompanyMetadataRecord;
import dk.magenta.datafordeler.cvr.records.CompanyRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * A class for formatting a CompanyEntity to JSON, for FAPI output. The data hierarchy
 * under a Company is sorted into this format:
 * {
 *     "UUID": <company uuid>
 *     "cvrnummer": <company cvr number>
 *     "id": {
 *         "domaene": <company domain>
 *     },
 *     registreringer: [
 *          {
 *              "registreringFra": <registrationFrom>,
 *              "registreringTil": <registrationTo>,
 *              "navn": [
 *              {
 *                  "navn": <companyName1>
 *                  "virkningFra": <effectFrom1>
 *                  "virkningTil": <effectTo1>
 *              },
 *              {
 *                  "navn": <companyName2>
 *                  "virkningFra": <effectFrom2>
 *                  "virkningTil": <effectTo2>
 *              }
 *              ]
 *          }
 *     ]
 * }
 */
@Component
public class CompanyRecordOutputWrapper extends RecordOutputWrapper<CompanyRecord> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    @Override
    public Object wrapResult(CompanyRecord record, Query query) {
        Bitemporality mustContain = new Bitemporality(query.getRegistrationFrom(), query.getRegistrationTo(), query.getEffectFrom(), query.getEffectTo());
        return this.asRVD(record, mustContain);
        //return this.asRecord(record);
    }

    private ObjectNode asRecord(CompanyRecord record) {
        return objectMapper.valueToTree(record);
    }

    protected ObjectNode asRVD(CompanyRecord record, Bitemporality mustContain) {
        ObjectNode root = this.getNode(record, mustContain);
        root.put(Entity.IO_FIELD_UUID, record.getIdentification().getUuid().toString());
        root.put(Entity.IO_FIELD_DOMAIN, record.getIdentification().getDomain());
        return root;
    }


    @Override
    protected void fillContainer(OutputContainer container, CompanyRecord record) {

        container.addNontemporal(CompanyRecord.IO_FIELD_CVR_NUMBER, record.getCvrNumber());
        container.addNontemporal(CompanyRecord.IO_FIELD_ADVERTPROTECTION, record.getAdvertProtection());
        container.addNontemporal(CompanyRecord.IO_FIELD_UNITNUMBER, record.getUnitNumber());
        container.addNontemporal(CompanyRecord.IO_FIELD_UNITTYPE, record.getUnitType());
        container.addNontemporal(CompanyRecord.IO_FIELD_INDUSTRY_RESPONSIBILITY_CODE, record.getIndustryResponsibilityCode());

        container.addBitemporal("navn", record.getNames(), true);
        container.addBitemporal(CompanyRecord.IO_FIELD_SECONDARY_NAMES, record.getSecondaryNames());
        container.addBitemporal(CompanyRecord.IO_FIELD_REG_NUMBER, record.getRegNumber(), true);
        container.addBitemporal(CompanyRecord.IO_FIELD_LOCATION_ADDRESS, record.getLocationAddress(), this::createAddressNode);
        container.addBitemporal(CompanyRecord.IO_FIELD_POSTAL_ADDRESS, record.getPostalAddress(), this::createAddressNode);
        container.addBitemporal(CompanyRecord.IO_FIELD_PHONE, record.getPhoneNumber(), true);
        container.addBitemporal(CompanyRecord.IO_FIELD_PHONE_SECONDARY, record.getSecondaryPhoneNumber(), true);
        container.addBitemporal(CompanyRecord.IO_FIELD_FAX, record.getFaxNumber(), true);
        container.addBitemporal(CompanyRecord.IO_FIELD_FAX_SECONDARY, record.getSecondaryFaxNumber(), true);
        container.addBitemporal(CompanyRecord.IO_FIELD_EMAIL, record.getEmailAddress(), true);
        container.addBitemporal(CompanyRecord.IO_FIELD_MANDATORY_EMAIL, record.getMandatoryEmailAddress(), true);
        container.addBitemporal(CompanyRecord.IO_FIELD_HOMEPAGE, record.getHomepage(), true);
        container.addBitemporal(CompanyRecord.IO_FIELD_PRIMARY_INDUSTRY, record.getPrimaryIndustry());
        container.addBitemporal(CompanyRecord.IO_FIELD_SECONDARY_INDUSTRY1, record.getSecondaryIndustry1());
        container.addBitemporal(CompanyRecord.IO_FIELD_SECONDARY_INDUSTRY2, record.getSecondaryIndustry2());
        container.addBitemporal(CompanyRecord.IO_FIELD_SECONDARY_INDUSTRY3, record.getSecondaryIndustry3());
        container.addBitemporal(CompanyRecord.IO_FIELD_FORM, record.getCompanyForm());
        container.addBitemporal(CompanyRecord.IO_FIELD_STATUS, record.getStatus());
        container.addBitemporal(CompanyRecord.IO_FIELD_COMPANYSTATUS, record.getCompanyStatus(), true);
        container.addBitemporal("livscyklusAktiv", record.getLifecycle(), this::createLifecycleNode);
        container.addBitemporal(CompanyRecord.IO_FIELD_YEARLY_NUMBERS, record.getYearlyNumbers());
        container.addBitemporal(CompanyRecord.IO_FIELD_QUARTERLY_NUMBERS, record.getQuarterlyNumbers());
        container.addBitemporal(CompanyRecord.IO_FIELD_MONTHLY_NUMBERS, record.getMonthlyNumbers());
        container.addAttribute(CompanyRecord.IO_FIELD_ATTRIBUTES, record.getAttributes());
        container.addBitemporal(CompanyRecord.IO_FIELD_P_UNITS, record.getProductionUnits(), null, true, true);
        container.addNontemporal(CompanyRecord.IO_FIELD_SAMT_ID, record.getSamtId());
        container.addNontemporal(CompanyRecord.IO_FIELD_REGISTER_ERROR, record.getRegisterError());
        container.addNontemporal(CompanyRecord.IO_FIELD_DATA_ACCESS, record.getDataAccess());
        container.addNontemporal(CompanyRecord.IO_FIELD_LAST_LOADED, record.getLastLoaded());
        container.addNontemporal(CompanyRecord.IO_FIELD_LAST_UPDATED, record.getLastUpdated());
        container.addNontemporal(CompanyRecord.IO_FIELD_LOADING_ERROR, record.getLoadingError());
        container.addNontemporal(CompanyRecord.IO_FIELD_NEAREST_FUTURE_DATE, record.getNearestFutureDate());
        container.addNontemporal(CompanyRecord.IO_FIELD_ERRORDESCRIPTION, record.getErrorDescription());
        container.addNontemporal(CompanyRecord.IO_FIELD_EFFECT_AGENT, record.getEffectAgent());
        /*
        participants
        fusions
        splits
        */
    }

    @Override
    protected void fillMetadataContainer(OutputContainer container, CompanyRecord record) {
        CompanyMetadataRecord meta = record.getMetadata();
        container.addBitemporal(CompanyMetadataRecord.IO_FIELD_NEWEST_NAME, meta.getNewestName());
        container.addBitemporal(CompanyMetadataRecord.IO_FIELD_NEWEST_FORM, meta.getNewestForm());
        container.addBitemporal(CompanyMetadataRecord.IO_FIELD_NEWEST_LOCATION, meta.getNewestLocation(), this::createAddressNode);
        container.addBitemporal(CompanyMetadataRecord.IO_FIELD_NEWEST_PRIMARY_INDUSTRY, meta.getNewestPrimaryIndustry());
        container.addBitemporal(CompanyMetadataRecord.IO_FIELD_NEWEST_SECONDARY_INDUSTRY1, meta.getNewestSecondaryIndustry1());
        container.addBitemporal(CompanyMetadataRecord.IO_FIELD_NEWEST_SECONDARY_INDUSTRY2, meta.getNewestSecondaryIndustry2());
        container.addBitemporal(CompanyMetadataRecord.IO_FIELD_NEWEST_SECONDARY_INDUSTRY3, meta.getNewestSecondaryIndustry3());
        container.addBitemporal(CompanyMetadataRecord.IO_FIELD_NEWEST_STATUS, Collections.singleton(meta.getNewestStatus()));
        container.addNontemporal(CompanyMetadataRecord.IO_FIELD_NEWEST_CONTACT_DATA, meta.getMetadataContactRecords(), null, true, true);
        container.addNontemporal(CompanyMetadataRecord.IO_FIELD_UNIT_COUNT, meta.getUnitCount());
        container.addBitemporal(CompanyMetadataRecord.IO_FIELD_NEWEST_YEARLY_NUMBERS, Collections.singleton(meta.getNewestYearlyNumbers()));
        container.addBitemporal(CompanyMetadataRecord.IO_FIELD_NEWEST_QUARTERLY_NUMBERS, Collections.singleton(meta.getNewestQuarterlyNumbers()));
        container.addBitemporal(CompanyMetadataRecord.IO_FIELD_NEWEST_MONTHLY_NUMBERS, Collections.singleton(meta.getNewestMonthlyNumbers()));
        container.addNontemporal(CompanyMetadataRecord.IO_FIELD_AGGREGATE_STATUS, meta.getAggregateStatus());
        container.addNontemporal(CompanyMetadataRecord.IO_FIELD_FOUNDING_DATE, meta.getFoundingDate());
        container.addNontemporal(CompanyMetadataRecord.IO_FIELD_EFFECT_DATE, meta.getEffectDate());
    }

}
