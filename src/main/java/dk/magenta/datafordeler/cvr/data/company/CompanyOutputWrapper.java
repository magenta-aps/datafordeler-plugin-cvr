package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.magenta.datafordeler.core.database.Effect;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.core.fapi.OutputWrapper;
import dk.magenta.datafordeler.cvr.data.shared.LifecycleData;
import dk.magenta.datafordeler.cvr.data.shared.ParticipantRelationData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;
import dk.magenta.datafordeler.cvr.data.unversioned.Industry;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Set;

/**
 * A class for formatting a CompanyEntity to JSON, for FAPI output. The data hierarchy
 * under a Company is sorted into this format:
 * {
 *     "UUID": <company uuid>
 *     "CVRNummer": <company cvr number>
 *     "id": {
 *         "domaene": <company domain>
 *     },
 *     registreringer: [
 *          {
 *              "registreringFra": <registrationFrom>,
 *              "registreringTil": <registrationTo>,
 *              "virksomhedsnavn": [
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
public class CompanyOutputWrapper extends OutputWrapper<CompanyEntity> {

    private ObjectMapper objectMapper;

    @Override
    public Object wrapResult(CompanyEntity input) {

        objectMapper = new ObjectMapper();

        // Root
        ObjectNode root = objectMapper.createObjectNode();
        root.put("UUID", input.getUUID().toString());
        root.put("CVRNummer", input.getCvrNumber());
        root.putPOJO("id", input.getIdentification());

        // Registrations
        ArrayNode registreringer = objectMapper.createArrayNode();
        root.set("registreringer", registreringer);
        for (CompanyRegistration companyRegistration : input.getRegistrations()) {
            registreringer.add(wrapRegistrering(companyRegistration));
        }

        return root;
    }

    protected ObjectNode wrapRegistrering(CompanyRegistration input) {
        ObjectNode output = objectMapper.createObjectNode();

        output.put(
                "registreringFra",
                input.getRegistrationFrom() != null ? input.getRegistrationFrom().toString() : null
        );
        output.put(
                "registreringTil",
                input.getRegistrationTo() != null ? input.getRegistrationTo().toString() : null
        );

        for (CompanyEffect virkning : input.getEffects()) {

            for (CompanyBaseData companyBaseData : virkning.getDataItems()) {

                OffsetDateTime timestamp = companyBaseData.getLastUpdated();
                String companyName = companyBaseData.getCompanyName();
                if (companyName != null) {
                    this.addEffectDataToRegistration(output, "virksomhedsnavn", createNameNode(virkning, timestamp, companyName));
                }

                CompanyForm companyForm = companyBaseData.getCompanyForm();
                if (companyForm != null) {
                    this.addEffectDataToRegistration(output, "virksomhedsform", createFormNode(virkning, timestamp, companyForm));
                }

                Address locationAddress = companyBaseData.getLocationAddress();
                if (locationAddress != null) {
                    this.addEffectDataToRegistration(output, "beliggenhedsadresse", createAdresseNode(virkning, timestamp, locationAddress));
                }

                Address postalAddress = companyBaseData.getLocationAddress();
                if (postalAddress != null) {
                    this.addEffectDataToRegistration(output, "postadresse", createAdresseNode(virkning, timestamp, postalAddress));
                }

                Industry primaryIndustry = companyBaseData.getPrimaryIndustry();
                if (primaryIndustry != null) {
                    this.addEffectDataToRegistration(output, "hovedbranche", createIndustryNode(virkning, timestamp, primaryIndustry));
                }

                Industry secondaryIndustry1 = companyBaseData.getSecondaryIndustry1();
                if (secondaryIndustry1 != null) {
                    this.addEffectDataToRegistration(output, "bibranche1", createIndustryNode(virkning, timestamp, secondaryIndustry1));
                }

                Industry secondaryIndustry2 = companyBaseData.getSecondaryIndustry1();
                if (secondaryIndustry2 != null) {
                    this.addEffectDataToRegistration(output, "bibranche2", createIndustryNode(virkning, timestamp, secondaryIndustry2));
                }

                Industry secondaryIndustry3 = companyBaseData.getSecondaryIndustry1();
                if (secondaryIndustry3 != null) {
                    this.addEffectDataToRegistration(output, "bibranche3", createIndustryNode(virkning, timestamp, secondaryIndustry3));
                }

                Boolean advertProtection = companyBaseData.getAdvertProtection();
                if (advertProtection != null) {
                    this.addEffectDataToRegistration(output, "reklamebeskyttelse", createBooleanNode(virkning, timestamp, "beskyttelse", advertProtection));
                }

                String phone = companyBaseData.getPhoneNumber();
                if (phone != null) {
                    this.addEffectDataToRegistration(output, "telefonnummer", createStringNode(virkning, timestamp, "nummer", phone));
                }

                String fax = companyBaseData.getFaxNumber();
                if (fax != null) {
                    this.addEffectDataToRegistration(output, "telefaxnummer", createStringNode(virkning, timestamp, "nummer", fax));
                }

                String email = companyBaseData.getFaxNumber();
                if (email != null) {
                    this.addEffectDataToRegistration(output, "emailadresse", createStringNode(virkning, timestamp, "nummer", email));
                }

                LifecycleData lifecycle = companyBaseData.getLifecycleData();
                if (lifecycle != null) {
                    this.addEffectDataToRegistration(output, "livscyklus", createLifecycleNode(virkning, timestamp, lifecycle));
                }

                Set<ParticipantRelationData> participantRelationData = companyBaseData.getParticipantRelations();
                if (participantRelationData != null && !participantRelationData.isEmpty()) {
                    for (ParticipantRelationData participantRelation : participantRelationData) {
                        this.addEffectDataToRegistration(output, "deltagerRelationer", createCompanyParticipantNode(virkning, timestamp, participantRelation));
                    }
                }

                Set<CompanyUnitLink> unitRelationData = companyBaseData.getUnitData();
                if (unitRelationData != null && !unitRelationData.isEmpty()) {
                    for (CompanyUnitLink unitRelation : unitRelationData) {
                        this.addEffectDataToRegistration(output, "produktionsEnheder", createCompanyUnitLinkNode(virkning, timestamp, unitRelation));
                    }
                }

            }
        }
        return output;
    }
    protected ObjectNode createVirkning(Effect virkning, OffsetDateTime lastUpdated) {
        return this.createVirkning(virkning, true, lastUpdated);
    }


    protected ObjectNode createVirkning(Effect virkning, boolean includeVirkningTil, OffsetDateTime lastUpdated) {
        ObjectNode output = objectMapper.createObjectNode();
        output.put(
                "virkningFra",
                virkning.getEffectFrom() != null ? virkning.getEffectFrom().toString() : null
        );
        if (includeVirkningTil) {
            output.put(
                    "virkningTil",
                    virkning.getEffectTo() != null ? virkning.getEffectTo().toString() : null
            );
        }
        output.put("lastUpdated", lastUpdated != null ? lastUpdated.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) : null);
        return output;
    }


    private static LocalDate getUTCDate(OffsetDateTime offsetDateTime) {
        return offsetDateTime.atZoneSameInstant(ZoneId.of("UTC")).toLocalDate();
    }


    protected ObjectNode addIdentification(Identification identification) {
        ObjectNode identificationObject = objectMapper.createObjectNode();
        identificationObject.put("uuid", identification.getUuid().toString());
        identificationObject.put("domaene", identification.getDomain());
        return identificationObject;
    }

    protected void addEffectDataToRegistration(ObjectNode output, String key, JsonNode value) {
        if (!output.has(key) || output.get(key).isNull()) {
            output.set(key, objectMapper.createArrayNode());
        }
        ((ArrayNode) output.get(key)).add(value);
    }

    private ObjectNode createNameNode(Effect virkning, OffsetDateTime lastUpdated, String name) {
        ObjectNode nameNode = createVirkning(virkning, lastUpdated);
        nameNode.put("navn", name);
        return nameNode;
    }

    private ObjectNode createFormNode(Effect virkning, OffsetDateTime lastUpdated, CompanyForm form) {
        ObjectNode formNode = createVirkning(virkning, lastUpdated);
        formNode.put("formkode", form.getCompanyFormCode());
        formNode.put("dataleverandør", form.getResponsibleDataSource());
        return formNode;
    }


    protected ObjectNode createAdresseNode(Effect virkning, OffsetDateTime lastUpdated, Address adresse) {
        ObjectNode adresseNode = createVirkning(virkning, lastUpdated);

        adresseNode.put("vejkode", adresse.getRoadCode());
        adresseNode.put("husnummerFra", adresse.getHouseNumberFrom());
        adresseNode.put("etagebetegnelse", adresse.getFloor());
        adresseNode.put("dørbetegnelse", adresse.getDoor());

        int kommunekode;
        String kommunenavn = null;
        Municipality kommune = adresse.getMunicipality();
        if (kommune != null) {
            adresseNode.put("kommunekode", kommune.getCode());
            adresseNode.put("kommunenavn", kommune.getName());
        }

        adresseNode.put("postdistrikt", adresse.getPostdistrikt());
        adresseNode.put("vejnavn", adresse.getRoadName());
        adresseNode.put("husnummerTil", adresse.getHouseNumberTo());
        adresseNode.put("postnummer", adresse.getPostnummer());
        adresseNode.put("supplerendeBynavn", adresse.getSupplementalCityName());
        adresseNode.put("adresseFritekst", adresse.getAddressText());
        adresseNode.put("landekode", adresse.getCountryCode());

        return adresseNode;
    }

    private ObjectNode createIndustryNode(Effect virkning, OffsetDateTime lastUpdated, Industry industry) {
        ObjectNode industryNode = createVirkning(virkning, lastUpdated);
        industryNode.put("branche", industry.getIndustryText());
        industryNode.put("branchekode", industry.getIndustryCode());
        return industryNode;
    }

    private ObjectNode createBooleanNode(Effect virkning, OffsetDateTime lastUpdated, String key, Boolean value) {
        ObjectNode protectionNode = createVirkning(virkning, lastUpdated);
        protectionNode.put(key, value);
        return protectionNode;
    }

    private ObjectNode createStringNode(Effect virkning, OffsetDateTime lastUpdated, String key, String value) {
        ObjectNode node = createVirkning(virkning, lastUpdated);
        node.put(key, value);
        return node;
    }

    private ObjectNode createLifecycleNode(Effect virkning, OffsetDateTime lastUpdated, LifecycleData lifecycle) {
        ObjectNode node = createVirkning(virkning, lastUpdated);
        if (lifecycle.getStartDate() != null) {
            node.put("virksomhedStartdato", this.getUTCDate(lifecycle.getStartDate()).format(DateTimeFormatter.ISO_DATE));
        }
        if (lifecycle.getEndDate() != null) {
            node.put("virksomheOphørsdato", this.getUTCDate(lifecycle.getEndDate()).format(DateTimeFormatter.ISO_DATE));
        }
        return node;
    }

    private ObjectNode createCompanyParticipantNode(Effect virkning, OffsetDateTime lastUpdated, ParticipantRelationData participantRelation) {
        ObjectNode participantRelationNode = createVirkning(virkning, lastUpdated);
        participantRelationNode.set("deltager", this.addIdentification(participantRelation.getParticipant()));
        return participantRelationNode;
    }

    private ObjectNode createCompanyUnitLinkNode(Effect virkning, OffsetDateTime lastUpdated, CompanyUnitLink unitLink) {
        ObjectNode unitLinkNode = createVirkning(virkning, lastUpdated);
        unitLinkNode.put("pnummer", unitLink.getpNumber());
        unitLinkNode.set("enhed", this.addIdentification(unitLink.getIdentification()));
        return unitLinkNode;
    }

}
