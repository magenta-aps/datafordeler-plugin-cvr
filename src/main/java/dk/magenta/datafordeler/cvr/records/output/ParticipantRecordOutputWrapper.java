package dk.magenta.datafordeler.cvr.records.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    public Object wrapResult(ParticipantRecord record) {
        return this.asRVD(record);
        //return this.asRecord(unitRecord);
    }

    private ObjectNode asRecord(ParticipantRecord record) {
        return objectMapper.valueToTree(record);
    }

    protected ObjectNode asRVD(ParticipantRecord record) {
        ObjectNode root = this.createNode(record);

        //root.put(CompanyEntity.IO_FIELD_UUID, ParticipantRecord.getIdentification().getUuid().toString());
        root.putPOJO("id", record.getIdentification());
        root.put(ParticipantRecord.IO_FIELD_UNIT_NUMBER, record.getUnitNumber());
        root.put(ParticipantRecord.IO_FIELD_UNIT_TYPE, record.getUnitType());
        root.put(ParticipantRecord.IO_FIELD_POSITION, record.getPosition());
        root.put(ParticipantRecord.IO_FIELD_BUSINESS_KEY, record.getBusinessKey());
        root.put(ParticipantRecord.IO_FIELD_STATUS_CODE, record.getStatusCode());

        return root;
    }


    @Override
    protected void fillContainer(OutputContainer container, ParticipantRecord record) {
        container.addMember("navn", record.getNames(), true);
        container.addMember(ParticipantRecord.IO_FIELD_LOCATION_ADDRESS, record.getLocationAddress());
        container.addMember(ParticipantRecord.IO_FIELD_POSTAL_ADDRESS, record.getPostalAddress(), this::createAddressNode);
        container.addMember(ParticipantRecord.IO_FIELD_BUSINESS_ADDRESS, record.getBusinessAddress(), this::createAddressNode);
        container.addMember(ParticipantRecord.IO_FIELD_PHONE, record.getPhoneNumber(), true);
        container.addMember(ParticipantRecord.IO_FIELD_FAX, record.getFaxNumber(), true);
        container.addMember(ParticipantRecord.IO_FIELD_EMAIL, record.getEmailAddress(), true);
        container.addAttributeMember(ParticipantRecord.IO_FIELD_ATTRIBUTES, record.getAttributes());
        /*
        metadata
        companyrelation
        */
    }

}
