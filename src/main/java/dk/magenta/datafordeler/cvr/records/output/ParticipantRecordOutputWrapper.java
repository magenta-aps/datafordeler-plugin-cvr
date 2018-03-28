package dk.magenta.datafordeler.cvr.records.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.magenta.datafordeler.core.fapi.Query;
import dk.magenta.datafordeler.cvr.data.Bitemporality;
import dk.magenta.datafordeler.cvr.records.ParticipantMetadataRecord;
import dk.magenta.datafordeler.cvr.records.ParticipantRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
public class ParticipantRecordOutputWrapper extends RecordOutputWrapper<ParticipantRecord> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    @Override
    public Object wrapResult(ParticipantRecord record, Query query) {
        Bitemporality mustContain = new Bitemporality(query.getRegistrationFrom(), query.getRegistrationTo(), query.getEffectFrom(), query.getEffectTo());
        return this.asRVD(record, mustContain);
        //return this.asRecord(unitRecord);
    }

    private ObjectNode asRecord(ParticipantRecord record) {
        return objectMapper.valueToTree(record);
    }

    protected ObjectNode asRVD(ParticipantRecord record, Bitemporality mustContain) {
        ObjectNode root = this.getNode(record, mustContain);

        //root.put(CompanyEntity.IO_FIELD_UUID, ParticipantRecord.getIdentification().getUuid().toString());
        root.putPOJO("id", record.getIdentification());

        return root;
    }


    @Override
    protected void fillContainer(OutputContainer container, ParticipantRecord record) {

        container.addNontemporal(ParticipantRecord.IO_FIELD_UNIT_NUMBER, record.getUnitNumber());
        container.addNontemporal(ParticipantRecord.IO_FIELD_UNIT_TYPE, record.getUnitType());
        container.addNontemporal(ParticipantRecord.IO_FIELD_POSITION, record.getPosition());
        container.addNontemporal(ParticipantRecord.IO_FIELD_BUSINESS_KEY, record.getBusinessKey());
        container.addNontemporal(ParticipantRecord.IO_FIELD_STATUS_CODE, record.getStatusCode());
        container.addNontemporal(ParticipantRecord.IO_FIELD_CONFIDENTIAL_ENRICHED, record.getConfidentialEnriched());

        container.addBitemporal("navn", record.getNames(), true);
        container.addBitemporal(ParticipantRecord.IO_FIELD_LOCATION_ADDRESS, record.getLocationAddress(), this::createAddressNode);
        container.addBitemporal(ParticipantRecord.IO_FIELD_POSTAL_ADDRESS, record.getPostalAddress(), this::createAddressNode);
        container.addBitemporal(ParticipantRecord.IO_FIELD_BUSINESS_ADDRESS, record.getBusinessAddress(), this::createAddressNode);
        container.addBitemporal(ParticipantRecord.IO_FIELD_PHONE, record.getPhoneNumber(), true);
        container.addBitemporal(ParticipantRecord.IO_FIELD_FAX, record.getFaxNumber(), true);
        container.addBitemporal(ParticipantRecord.IO_FIELD_EMAIL, record.getEmailAddress(), true);
        container.addAttribute(ParticipantRecord.IO_FIELD_ATTRIBUTES, record.getAttributes());
        container.addNontemporal(ParticipantRecord.IO_FIELD_SAMT_ID, record.getSamtId());
        container.addNontemporal(ParticipantRecord.IO_FIELD_REGISTER_ERROR, record.getRegisterError());
        container.addNontemporal(ParticipantRecord.IO_FIELD_DATA_ACCESS, record.getDataAccess());
        container.addNontemporal(ParticipantRecord.IO_FIELD_LAST_LOADED, record.getLastLoaded());
        container.addNontemporal(ParticipantRecord.IO_FIELD_LAST_UPDATED, record.getLastUpdated());
        container.addNontemporal(ParticipantRecord.IO_FIELD_LOADING_ERROR, record.getLoadingError());
        container.addNontemporal(ParticipantRecord.IO_FIELD_NEAREST_FUTURE_DATE, record.getNearestFutureDate());
        container.addNontemporal(ParticipantRecord.IO_FIELD_ERRORDESCRIPTION, record.getErrorDescription());
        container.addNontemporal(ParticipantRecord.IO_FIELD_EFFECT_AGENT, record.getEffectAgent());
        /*
        companyrelation
        */
    }

    @Override
    protected void fillMetadataContainer(OutputContainer container, ParticipantRecord record) {
        ParticipantMetadataRecord meta = record.getMetadata();
        container.addBitemporal(ParticipantMetadataRecord.IO_FIELD_NEWEST_LOCATION, meta.getNewestLocation(), this::createAddressNode);
        container.addNontemporal(ParticipantMetadataRecord.IO_FIELD_NEWEST_CONTACT_DATA, meta.getMetadataContactRecords(), null, true, true);
    }

}
