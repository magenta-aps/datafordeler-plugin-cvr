package dk.magenta.datafordeler.cvr.data.companyunit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.magenta.datafordeler.core.database.Effect;
import dk.magenta.datafordeler.core.fapi.OutputWrapper;
import dk.magenta.datafordeler.cvr.data.CvrOutputWrapper;
import dk.magenta.datafordeler.cvr.data.shared.LifecycleData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.Industry;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class CompanyUnitOutputWrapper extends CvrOutputWrapper<CompanyUnitEntity> {

    private ObjectMapper objectMapper;

    @Override
    public Object wrapResult(CompanyUnitEntity input) {

        objectMapper = new ObjectMapper();

        // Root
        ObjectNode root = objectMapper.createObjectNode();

        root.put("UUID", input.getUUID().toString());
        root.put(CompanyUnitEntity.IO_FIELD_PNUMBER, input.getPNumber());
        root.putPOJO("id", input.getIdentification());

        // Registreringer
        ArrayNode registreringer = objectMapper.createArrayNode();
        root.set("registreringer", registreringer);

        for (CompanyUnitRegistration companyUnitRegistration : input.getRegistrations()) {
            registreringer.add(wrapRegistrering(companyUnitRegistration));
        }

        return root;
    }

    protected ObjectNode wrapRegistrering(CompanyUnitRegistration input) {
        ObjectNode output = objectMapper.createObjectNode();

        output.put(
                "registreringFra",
                input.getRegistrationFrom() != null ? input.getRegistrationFrom().toString() : null
        );
        output.put(
                "registreringTil",
                input.getRegistrationTo() != null ? input.getRegistrationTo().toString() : null
        );

        for (CompanyUnitEffect virkning : input.getEffects()) {
            for (CompanyUnitBaseData companyUnitBaseData : virkning.getDataItems()) {

                OffsetDateTime timestamp = companyUnitBaseData.getLastUpdated();

                String name = companyUnitBaseData.getName();
                if (name != null) {
                    this.addEffectDataToRegistration(output, CompanyUnitBaseData.IO_FIELD_NAME, createSimpleNode(virkning, timestamp, "navn", name));
                }

                List<Long> associatedCvrNumbers = companyUnitBaseData.getAssociatedCvrNumber();
                if (associatedCvrNumbers != null && !associatedCvrNumbers.isEmpty()) {
                    this.addEffectDataToRegistration(output, CompanyUnitBaseData.IO_FIELD_CVR_NUMBER, createListNode(virkning, timestamp, "numre", associatedCvrNumbers));
                }

                Address locationAddress = companyUnitBaseData.getLocationAddress();
                if (locationAddress != null) {
                    this.addEffectDataToRegistration(output, CompanyUnitBaseData.IO_FIELD_LOCATION_ADDRESS, createAddressNode(virkning, timestamp, locationAddress));
                }

                Address postalAddress = companyUnitBaseData.getLocationAddress();
                if (postalAddress != null) {
                    this.addEffectDataToRegistration(output, CompanyUnitBaseData.IO_FIELD_POSTAL_ADDRESS, createAddressNode(virkning, timestamp, postalAddress));
                }

                Industry primaryIndustry = companyUnitBaseData.getPrimaryIndustry();
                if (primaryIndustry != null) {
                    this.addEffectDataToRegistration(output, CompanyUnitBaseData.IO_FIELD_PRIMARY_INDUSTRY, createIndustryNode(virkning, timestamp, primaryIndustry));
                }

                Industry secondaryIndustry1 = companyUnitBaseData.getSecondaryIndustry1();
                if (secondaryIndustry1 != null) {
                    this.addEffectDataToRegistration(output, CompanyUnitBaseData.IO_FIELD_SECONDARY_INDUSTRY_1, createIndustryNode(virkning, timestamp, secondaryIndustry1));
                }

                Industry secondaryIndustry2 = companyUnitBaseData.getSecondaryIndustry1();
                if (secondaryIndustry2 != null) {
                    this.addEffectDataToRegistration(output, CompanyUnitBaseData.IO_FIELD_SECONDARY_INDUSTRY_2, createIndustryNode(virkning, timestamp, secondaryIndustry2));
                }

                Industry secondaryIndustry3 = companyUnitBaseData.getSecondaryIndustry1();
                if (secondaryIndustry3 != null) {
                    this.addEffectDataToRegistration(output, CompanyUnitBaseData.IO_FIELD_SECONDARY_INDUSTRY_3, createIndustryNode(virkning, timestamp, secondaryIndustry3));
                }

                Boolean advertProtection = companyUnitBaseData.getAdvertProtection();
                if (advertProtection != null) {
                    this.addEffectDataToRegistration(output, CompanyUnitBaseData.IO_FIELD_ADVERTPROTECTION, createSimpleNode(virkning, timestamp, "beskyttelse", advertProtection));
                }

                String phone = companyUnitBaseData.getPhoneNumber();
                if (phone != null) {
                    this.addEffectDataToRegistration(output, CompanyUnitBaseData.IO_FIELD_PHONENUMBER, createSimpleNode(virkning, timestamp, "nummer", phone));
                }

                String fax = companyUnitBaseData.getFaxNumber();
                if (fax != null) {
                    this.addEffectDataToRegistration(output, CompanyUnitBaseData.IO_FIELD_FAXNUMBER, createSimpleNode(virkning, timestamp, "nummer", fax));
                }

                String email = companyUnitBaseData.getFaxNumber();
                if (email != null) {
                    this.addEffectDataToRegistration(output, CompanyUnitBaseData.IO_FIELD_EMAIL, createSimpleNode(virkning, timestamp, "adresse", email));
                }

                LifecycleData lifecycle = companyUnitBaseData.getLifecycleData();
                if (lifecycle != null) {
                    this.addEffectDataToRegistration(output, CompanyUnitBaseData.IO_FIELD_LIFECYCLE, createLifecycleNode(virkning, timestamp, lifecycle));
                }


            }
        }

        return output;
    }

    protected ObjectNode createVirksomhedObject (ObjectNode node, CompanyUnitBaseData produktionsenhed) {

        return node;
    }

    protected ObjectNode addAdresseObject(Address adresse) {

        ObjectNode adresseObject = objectMapper.createObjectNode();

        if (adresse != null) {
            ObjectNode json = objectMapper.createObjectNode();

            json.put("vejkode", adresse.getRoadCode());
            json.put("hunummerFra", adresse.getHouseNumberFrom());
            json.put("etagebetegnelse", adresse.getFloor());
            json.put("dørbetegnelse", adresse.getDoor());

            Municipality kommune = adresse.getMunicipality();
            if (kommune != null) {
                json.put("kommunekode", kommune.getCode());
                json.put("kommunenavn", kommune.getName());
            }

            json.put("postdistrikt", adresse.getPostdistrikt());
            json.put("vejnavn", adresse.getRoadName());
            json.put("husnummerTil", adresse.getHouseNumberTo());
            json.put("postnummer", adresse.getPostnummer());
            json.put("supplerendeBynavn", adresse.getSupplementalCityName());
            json.put("adresseFritekst", adresse.getAddressText());
            json.put("landekode", adresse.getCountryCode());

            adresseObject.set("CVRAdresse", json);
            adresseObject.set("adresse1", null); // Missing in input
            adresseObject.set("adresse2", null); // Missing in input
            adresseObject.set("coNavn", null); // Missing in input
        } else {
            return null;
        }

        return adresseObject;
    }


    private ObjectNode createLifecycleNode(Effect virkning, OffsetDateTime lastUpdated, LifecycleData lifecycle) {
        ObjectNode node = createVirkning(virkning, lastUpdated);
        if (lifecycle.getStartDate() != null) {
            node.put(LifecycleData.IO_FIELD_START, this.getUTCDate(lifecycle.getStartDate()).format(DateTimeFormatter.ISO_DATE));
        }
        if (lifecycle.getEndDate() != null) {
            node.put(LifecycleData.IO_FIELD_END, this.getUTCDate(lifecycle.getEndDate()).format(DateTimeFormatter.ISO_DATE));
        }
        return node;
    }

}
