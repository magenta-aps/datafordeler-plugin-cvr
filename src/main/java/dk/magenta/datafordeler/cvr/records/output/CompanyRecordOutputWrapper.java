package dk.magenta.datafordeler.cvr.records.output;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import dk.magenta.datafordeler.cvr.data.company.CompanyEntity;
import dk.magenta.datafordeler.cvr.records.*;
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
    public Object wrapResult(CompanyRecord companyRecord) {
        return this.asRVD(companyRecord);
        //return this.asRecord(companyRecord);
    }

    private ObjectNode asRecord(CompanyRecord record) {
        return objectMapper.valueToTree(record);
    }

    protected ObjectNode asRVD(CompanyRecord companyRecord) {
        ObjectNode root = this.createNode(companyRecord);

        //root.put(CompanyEntity.IO_FIELD_UUID, companyRecord.getIdentification().getUuid().toString());
        root.putPOJO("id", companyRecord.getIdentification());
        root.put(CompanyEntity.IO_FIELD_CVR, companyRecord.getCvrNumber());

        return root;
    }


    @Override
    protected void fillContainer(OutputContainer container, CompanyRecord item) {
        container.addCompanyMember("navn", item.getNames(), true);
        container.addCompanyMember(CompanyRecord.IO_FIELD_SECONDARY_NAMES, item.getSecondaryNames());
        container.addCompanyMember(CompanyRecord.IO_FIELD_REG_NUMBER, item.getRegNumber(), true);
        container.addCompanyMember(CompanyRecord.IO_FIELD_LOCATION_ADDRESS, item.getLocationAddress());
        container.addCompanyMember(CompanyRecord.IO_FIELD_POSTAL_ADDRESS, item.getPostalAddress(), this::createAddressNode);
        container.addCompanyMember(CompanyRecord.IO_FIELD_PHONE, item.getPhoneNumber(), true);
        container.addCompanyMember(CompanyRecord.IO_FIELD_PHONE_SECONDARY, item.getSecondaryPhoneNumber(), true);
        container.addCompanyMember(CompanyRecord.IO_FIELD_FAX, item.getFaxNumber(), true);
        container.addCompanyMember(CompanyRecord.IO_FIELD_FAX_SECONDARY, item.getSecondaryFaxNumber(), true);
        container.addCompanyMember(CompanyRecord.IO_FIELD_EMAIL, item.getEmailAddress(), true);
        container.addCompanyMember(CompanyRecord.IO_FIELD_MANDATORY_EMAIL, item.getMandatoryEmailAddress(), true);
        container.addCompanyMember(CompanyRecord.IO_FIELD_HOMEPAGE, item.getHomepage(), true);
        container.addCompanyMember(CompanyRecord.IO_FIELD_PRIMARY_INDUSTRY, item.getPrimaryIndustry());
        container.addCompanyMember(CompanyRecord.IO_FIELD_SECONDARY_INDUSTRY1, item.getSecondaryIndustry1());
        container.addCompanyMember(CompanyRecord.IO_FIELD_SECONDARY_INDUSTRY2, item.getSecondaryIndustry2());
        container.addCompanyMember(CompanyRecord.IO_FIELD_SECONDARY_INDUSTRY3, item.getSecondaryIndustry3());
        container.addCompanyMember(CompanyRecord.IO_FIELD_FORM, item.getCompanyForm());
        container.addCompanyMember(CompanyRecord.IO_FIELD_STATUS, item.getStatus());
        container.addCompanyMember(CompanyRecord.IO_FIELD_COMPANYSTATUS, item.getCompanyStatus(), true);
        container.addCompanyMember("livscyklusAktiv", item.getLifecycle(), this::createLifecycleNode);
        container.addCompanyMember(CompanyRecord.IO_FIELD_YEARLY_NUMBERS, item.getYearlyNumbers());
        container.addCompanyMember(CompanyRecord.IO_FIELD_QUARTERLY_NUMBERS, item.getQuarterlyNumbers());
        container.addCompanyMember(CompanyRecord.IO_FIELD_MONTHLY_NUMBERS, item.getMonthlyNumbers());
        container.addAttributeMember(CompanyRecord.IO_FIELD_ATTRIBUTES, item.getAttributes());
        container.addCompanyMember(CompanyRecord.IO_FIELD_P_UNITS, item.getProductionUnits(), null, true, true);
        /*
        participants
        fusions
        splits
        */
    }

}
