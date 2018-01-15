package dk.magenta.datafordeler.cvr.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.magenta.datafordeler.core.database.Effect;
import dk.magenta.datafordeler.core.database.Entity;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.core.fapi.OutputWrapper;
import dk.magenta.datafordeler.core.util.DoubleHashMap;
import dk.magenta.datafordeler.cvr.data.shared.AttributeData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.Industry;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public abstract class CvrOutputWrapper<T extends Entity> extends OutputWrapper<T> {

    private ObjectMapper objectMapper = new ObjectMapper();

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

    protected ObjectNode createIdentificationNode(Identification identification) {
        if (identification != null) {
            ObjectNode identificationObject = objectMapper.createObjectNode();
            identificationObject.put("uuid", identification.getUuid().toString());
            identificationObject.put("domaene", identification.getDomain());
            return identificationObject;
        }
        return null;
    }

    protected ObjectNode createAddressNode(Effect virkning, OffsetDateTime lastUpdated, Address adresse) {
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

    protected ArrayNode createAttributeNode(Effect virkning, OffsetDateTime timestamp, Set<AttributeData> attributes) {
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

    protected ObjectNode createIndustryNode(Effect virkning, OffsetDateTime lastUpdated, Industry industry) {
        ObjectNode industryNode = createVirkning(virkning, lastUpdated);
        industryNode.put("branche", industry.getIndustryText());
        industryNode.put("branchekode", industry.getIndustryCode());
        return industryNode;
    }

    protected ObjectNode createSimpleNode(Effect virkning, OffsetDateTime lastUpdated, String key, Boolean value) {
        ObjectNode node = createVirkning(virkning, lastUpdated);
        node.put(key, value);
        return node;
    }

    protected ObjectNode createSimpleNode(Effect virkning, OffsetDateTime lastUpdated, String key, String value) {
        ObjectNode node = createVirkning(virkning, lastUpdated);
        node.put(key, value);
        return node;
    }

    protected ObjectNode createSimpleNode(Effect virkning, OffsetDateTime lastUpdated, String key, Long value) {
        ObjectNode node = createVirkning(virkning, lastUpdated);
        node.put(key, value);
        return node;
    }

    protected ObjectNode createListNode(Effect virkning, OffsetDateTime lastUpdated, String key, Collection<Long> value) {
        ObjectNode node = createVirkning(virkning, lastUpdated);
        ArrayNode listNode = objectMapper.createArrayNode();
        for (Long v : value) {
            listNode.add(v);
        }
        node.set(key, listNode);
        return node;
    }

    protected static LocalDate getUTCDate(OffsetDateTime offsetDateTime) {
        return offsetDateTime.atZoneSameInstant(ZoneId.of("UTC")).toLocalDate();
    }

}
