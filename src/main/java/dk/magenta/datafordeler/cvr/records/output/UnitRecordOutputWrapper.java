package dk.magenta.datafordeler.cvr.records.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.magenta.datafordeler.cvr.records.CompanyUnitRecord;
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
public class UnitRecordOutputWrapper extends RecordOutputWrapper<CompanyUnitRecord> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    @Override
    public Object wrapResult(CompanyUnitRecord record) {
        return this.asRVD(record);
        //return this.asRecord(unitRecord);
    }

    private ObjectNode asRecord(CompanyUnitRecord record) {
        return objectMapper.valueToTree(record);
    }

    protected ObjectNode asRVD(CompanyUnitRecord record) {
        ObjectNode root = this.createNode(record);

        //root.put(CompanyEntity.IO_FIELD_UUID, CompanyUnitRecord.getIdentification().getUuid().toString());
        root.putPOJO("id", record.getIdentification());
        root.put(CompanyUnitRecord.IO_FIELD_P_NUMBER, record.getpNumber());
        root.put(CompanyUnitRecord.IO_FIELD_ADVERTPROTECTION, record.getAdvertProtection());
        root.put(CompanyUnitRecord.IO_FIELD_UNITNUMBER, record.getUnitNumber());
        root.put(CompanyUnitRecord.IO_FIELD_UNITTYPE, record.getUnitType());
        root.put(CompanyUnitRecord.IO_FIELD_INDUSTRY_RESPONSIBILITY_CODE, record.getIndustryResponsibilityCode());


        return root;
    }


    @Override
    protected void fillContainer(OutputContainer container, CompanyUnitRecord record) {
        container.addMember("navn", record.getNames(), true);
        container.addMember(CompanyUnitRecord.IO_FIELD_LOCATION_ADDRESS, record.getLocationAddress());
        container.addMember(CompanyUnitRecord.IO_FIELD_POSTAL_ADDRESS, record.getPostalAddress(), this::createAddressNode);
        container.addMember(CompanyUnitRecord.IO_FIELD_PHONE, record.getPhoneNumber(), true);
        container.addMember(CompanyUnitRecord.IO_FIELD_FAX, record.getFaxNumber(), true);
        container.addMember(CompanyUnitRecord.IO_FIELD_EMAIL, record.getEmailAddress(), true);
        container.addMember(CompanyUnitRecord.IO_FIELD_PRIMARY_INDUSTRY, record.getPrimaryIndustry());
        container.addMember(CompanyUnitRecord.IO_FIELD_SECONDARY_INDUSTRY1, record.getSecondaryIndustry1());
        container.addMember(CompanyUnitRecord.IO_FIELD_SECONDARY_INDUSTRY2, record.getSecondaryIndustry2());
        container.addMember(CompanyUnitRecord.IO_FIELD_SECONDARY_INDUSTRY3, record.getSecondaryIndustry3());
        container.addMember("livscyklusAktiv", record.getLifecycle(), this::createLifecycleNode);
        container.addMember(CompanyUnitRecord.IO_FIELD_YEARLY_NUMBERS, record.getYearlyNumbers());
        container.addMember(CompanyUnitRecord.IO_FIELD_QUARTERLY_NUMBERS, record.getQuarterlyNumbers());
        container.addAttributeMember(CompanyUnitRecord.IO_FIELD_ATTRIBUTES, record.getAttributes());
        container.addMember(CompanyUnitRecord.IO_FIELD_COMPANY_LINK, record.getCompanyLinkRecords(), null, true, true);
        /*
        participants
        metadata
        */
    }

}
