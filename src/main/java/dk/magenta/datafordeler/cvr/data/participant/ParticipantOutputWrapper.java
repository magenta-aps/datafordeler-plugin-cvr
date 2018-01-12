package dk.magenta.datafordeler.cvr.data.participant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.magenta.datafordeler.core.database.Effect;
import dk.magenta.datafordeler.core.fapi.OutputWrapper;
import dk.magenta.datafordeler.core.util.DoubleHashMap;
import dk.magenta.datafordeler.cvr.data.shared.AttributeData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ParticipantOutputWrapper extends OutputWrapper<ParticipantEntity> {

    private ObjectMapper objectMapper;

    @Override
    public Object wrapResult(ParticipantEntity input) {

        objectMapper = new ObjectMapper();

        // Root
        ObjectNode root = objectMapper.createObjectNode();

        root.put("UUID", input.getUUID().toString());
        root.put("deltagernummer", input.getParticipantNumber());
        root.putPOJO("id", input.getIdentification());

        // Registreringer
        ArrayNode registreringer = objectMapper.createArrayNode();
        root.set("registreringer", registreringer);

        for (ParticipantRegistration participantRegistration : input.getRegistrations()) {
            registreringer.add(wrapRegistrering(participantRegistration));
        }

        return root;
    }

    protected ObjectNode wrapRegistrering(ParticipantRegistration input) {
        ObjectNode output = objectMapper.createObjectNode();

        output.put(
                "registreringFra",
                input.getRegistrationFrom() != null ? input.getRegistrationFrom().toString() : null
        );
        output.put(
                "registreringTil",
                input.getRegistrationTo() != null ? input.getRegistrationTo().toString() : null
        );

        for (ParticipantEffect virkning : input.getEffects()) {

            for (ParticipantBaseData participantBaseData : virkning.getDataItems()) {

                OffsetDateTime timestamp = participantBaseData.getLastUpdated();

                Set<String> names = participantBaseData.getNames();
                if (!names.isEmpty()) {
                    addEffectDataToRegistration(output, "navn", createNameNode(virkning, timestamp, names));
                }

                Address locationAddress = participantBaseData.getLocationAddress();
                if (locationAddress != null) {
                    this.addEffectDataToRegistration(output, "beliggenhedsadresse", createAdresseNode(virkning, timestamp, locationAddress));
                }

                Address postalAddress = participantBaseData.getLocationAddress();
                if (postalAddress != null) {
                    this.addEffectDataToRegistration(output, "postadresse", createAdresseNode(virkning, timestamp, postalAddress));
                }

                Address businessAddress = participantBaseData.getBusinessAddress();
                if (businessAddress != null) {
                    this.addEffectDataToRegistration(output, "forretningsadresse", createAdresseNode(virkning, timestamp, businessAddress));
                }

                String phone = participantBaseData.getPhoneNumber();
                if (phone != null) {
                    this.addEffectDataToRegistration(output, "telefon", createSimpleNode(virkning, timestamp, "nummer", phone));
                }

                String fax = participantBaseData.getFaxNumber();
                if (fax != null) {
                    this.addEffectDataToRegistration(output, "telefax", createSimpleNode(virkning, timestamp, "nummer", fax));
                }

                String email = participantBaseData.getFaxNumber();
                if (email != null) {
                    this.addEffectDataToRegistration(output, "email", createSimpleNode(virkning, timestamp, "adresse", email));
                }


                Long unitNumber = participantBaseData.getUnitNumber();
                if (unitNumber != null) {
                    this.addEffectDataToRegistration(output, "deltagernummer", createSimpleNode(virkning, timestamp, "nummer", unitNumber));
                }

                String unitType = participantBaseData.getUnitType();
                if (unitType != null) {
                    this.addEffectDataToRegistration(output, "enhedstype", createSimpleNode(virkning, timestamp, "type", unitType));
                }

                String role = participantBaseData.getRole();
                if (role != null) {
                    this.addEffectDataToRegistration(output, "rolle", createSimpleNode(virkning, timestamp, "tekst", role));
                }

                String status = participantBaseData.getStatus();
                if (status != null) {
                    this.addEffectDataToRegistration(output, "status", createSimpleNode(virkning, timestamp, "tekst", status));
                }

                Set<AttributeData> attributes = participantBaseData.getAttributes();
                if (attributes != null) {
                    addEffectDataToRegistration(output, "attributter", createAttributeNode(virkning, timestamp, attributes));
                }
            }
        }
        return output;
    }

    protected void addEffectDataToRegistration(ObjectNode output, String key, JsonNode value) {
        if (!output.has(key)) {
            output.set(key, objectMapper.createArrayNode());
        }
        ArrayNode destination = ((ArrayNode) output.get(key));
        if (value.isArray()) {
            destination.addAll((ArrayNode) value);
        } else {
            destination.add(value);
        }
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

    private ObjectNode createNameNode(Effect virkning, OffsetDateTime lastUpdated, Collection<String> names) {
        ArrayNode listNode = objectMapper.createArrayNode();
        for (String name : names) {
            listNode.add(name);
        }
        ObjectNode nameNode = createVirkning(virkning, lastUpdated);
        nameNode.set("navne", listNode);
        return nameNode;
    }

    private ObjectNode createSimpleNode(Effect virkning, OffsetDateTime lastUpdated, String key, Boolean value) {
        ObjectNode node = createVirkning(virkning, lastUpdated);
        node.put(key, value);
        return node;
    }

    private ObjectNode createSimpleNode(Effect virkning, OffsetDateTime lastUpdated, String key, String value) {
        ObjectNode node = createVirkning(virkning, lastUpdated);
        node.put(key, value);
        return node;
    }

    private ObjectNode createSimpleNode(Effect virkning, OffsetDateTime lastUpdated, String key, Long value) {
        ObjectNode node = createVirkning(virkning, lastUpdated);
        node.put(key, value);
        return node;
    }

    private ArrayNode createAttributeNode(Effect virkning, OffsetDateTime timestamp, Set<AttributeData> attributes) {
        ArrayNode listNode = objectMapper.createArrayNode();
        DoubleHashMap<String, String, ArrayList<AttributeData>> sorted = new DoubleHashMap<>();
        for (AttributeData attribute : attributes) {
            ArrayList<AttributeData> list = sorted.get(attribute.getType(), attribute.getValueType());
            if (list == null) {
                list = new ArrayList<>();
                sorted.put(attribute.getType(), attribute.getValueType(), list);
            }
            int sequenceNumber = attribute.getSequenceNumber();
            if (sequenceNumber > list.size()) {
                list.add(attribute);
            } else if (sequenceNumber >= 0) {
                list.add(sequenceNumber, attribute);
            }
        }

        for (String type : sorted.keySet()) {
            HashMap<String, ArrayList<AttributeData>> typeMap = sorted.get(type);
            for (String valuetype : typeMap.keySet()) {
                List<AttributeData> attributeList = typeMap.get(valuetype);
                ObjectNode attributeNode = createVirkning(virkning, timestamp);
                attributeNode.put("type", type);
                attributeNode.put("vaerditype", valuetype);
                ArrayNode valueList = objectMapper.createArrayNode();
                attributeNode.set("vaerdier", valueList);
                for (AttributeData data : attributeList) {
                    valueList.add(data.getValue());
                }
                listNode.add(attributeNode);
            }
        }
        return listNode;
    }
}
