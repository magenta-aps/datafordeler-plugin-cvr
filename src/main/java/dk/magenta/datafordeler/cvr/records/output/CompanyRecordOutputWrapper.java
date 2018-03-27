package dk.magenta.datafordeler.cvr.records.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.magenta.datafordeler.cvr.records.CompanyRecord;
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
public class CompanyRecordOutputWrapper extends RecordOutputWrapper<CompanyRecord> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    @Override
    public Object wrapResult(CompanyRecord record) {
        return this.asRVD(record);
        //return this.asRecord(record);
    }

    private ObjectNode asRecord(CompanyRecord record) {
        return objectMapper.valueToTree(record);
    }

    protected ObjectNode asRVD(CompanyRecord record) {
        ObjectNode root = this.createNode(record);

        //root.put(CompanyEntity.IO_FIELD_UUID, record.getIdentification().getUuid().toString());
        root.putPOJO("id", record.getIdentification());
        root.put(CompanyRecord.IO_FIELD_CVR_NUMBER, record.getCvrNumber());
        root.put(CompanyRecord.IO_FIELD_ADVERTPROTECTION, record.getAdvertProtection());
        root.put(CompanyRecord.IO_FIELD_UNITNUMBER, record.getUnitNumber());
        root.put(CompanyRecord.IO_FIELD_UNITTYPE, record.getUnitType());
        root.put(CompanyRecord.IO_FIELD_INDUSTRY_RESPONSIBILITY_CODE, record.getIndustryResponsibilityCode());

        return root;
    }


    @Override
    protected void fillContainer(OutputContainer container, CompanyRecord record) {
        container.addMember("navn", record.getNames(), true);
        container.addMember(CompanyRecord.IO_FIELD_SECONDARY_NAMES, record.getSecondaryNames());
        container.addMember(CompanyRecord.IO_FIELD_REG_NUMBER, record.getRegNumber(), true);
        container.addMember(CompanyRecord.IO_FIELD_LOCATION_ADDRESS, record.getLocationAddress());
        container.addMember(CompanyRecord.IO_FIELD_POSTAL_ADDRESS, record.getPostalAddress(), this::createAddressNode);
        container.addMember(CompanyRecord.IO_FIELD_PHONE, record.getPhoneNumber(), true);
        container.addMember(CompanyRecord.IO_FIELD_PHONE_SECONDARY, record.getSecondaryPhoneNumber(), true);
        container.addMember(CompanyRecord.IO_FIELD_FAX, record.getFaxNumber(), true);
        container.addMember(CompanyRecord.IO_FIELD_FAX_SECONDARY, record.getSecondaryFaxNumber(), true);
        container.addMember(CompanyRecord.IO_FIELD_EMAIL, record.getEmailAddress(), true);
        container.addMember(CompanyRecord.IO_FIELD_MANDATORY_EMAIL, record.getMandatoryEmailAddress(), true);
        container.addMember(CompanyRecord.IO_FIELD_HOMEPAGE, record.getHomepage(), true);
        container.addMember(CompanyRecord.IO_FIELD_PRIMARY_INDUSTRY, record.getPrimaryIndustry());
        container.addMember(CompanyRecord.IO_FIELD_SECONDARY_INDUSTRY1, record.getSecondaryIndustry1());
        container.addMember(CompanyRecord.IO_FIELD_SECONDARY_INDUSTRY2, record.getSecondaryIndustry2());
        container.addMember(CompanyRecord.IO_FIELD_SECONDARY_INDUSTRY3, record.getSecondaryIndustry3());
        container.addMember(CompanyRecord.IO_FIELD_FORM, record.getCompanyForm());
        container.addMember(CompanyRecord.IO_FIELD_STATUS, record.getStatus());
        container.addMember(CompanyRecord.IO_FIELD_COMPANYSTATUS, record.getCompanyStatus(), true);
        container.addMember("livscyklusAktiv", record.getLifecycle(), this::createLifecycleNode);
        container.addMember(CompanyRecord.IO_FIELD_YEARLY_NUMBERS, record.getYearlyNumbers());
        container.addMember(CompanyRecord.IO_FIELD_QUARTERLY_NUMBERS, record.getQuarterlyNumbers());
        container.addMember(CompanyRecord.IO_FIELD_MONTHLY_NUMBERS, record.getMonthlyNumbers());
        container.addAttributeMember(CompanyRecord.IO_FIELD_ATTRIBUTES, record.getAttributes());
        container.addMember(CompanyRecord.IO_FIELD_P_UNITS, record.getProductionUnits(), null, true, true);
        /*
        participants
        fusions
        splits
        metadata
        */
    }

}
