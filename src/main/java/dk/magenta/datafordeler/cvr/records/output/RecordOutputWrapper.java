package dk.magenta.datafordeler.cvr.records.output;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.magenta.datafordeler.core.fapi.OutputWrapper;
import dk.magenta.datafordeler.core.util.DoubleListHashMap;
import dk.magenta.datafordeler.core.util.ListHashMap;
import dk.magenta.datafordeler.cvr.data.Bitemporality;
import dk.magenta.datafordeler.cvr.data.BitemporalityComparator;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;
import dk.magenta.datafordeler.cvr.records.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

/**
 * A class for formatting a CompanyRecord to JSON, for FAPI output. The data hierarchy
 * in the output should ook like this:
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
 *              "virkninger": [
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
public abstract class RecordOutputWrapper<T extends CvrEntityRecord> extends OutputWrapper<T> {

    protected class OutputContainer extends DoubleListHashMap<Bitemporality, String, JsonNode> {

        private final List<String> removeFieldNames = Arrays.asList(new String[]{"periode", "sidstOpdateret", "sidstIndlaest"});

        private HashSet<String> forcedArrayKeys = new HashSet<>();

        public boolean isArrayForced(String key) {
            return this.forcedArrayKeys.contains(key);
        }

        public <T extends CvrBitemporalRecord> void addMember(String key, Set<T> items) {
            this.addMember(key, items, null, false, false);
        }

        public <T extends CvrBitemporalRecord> void addMember(String key, Set<T> items, boolean unwrapSingle) {
            this.addMember(key, items, null, unwrapSingle, false);
        }

        public <T extends CvrBitemporalRecord> void addMember(String key, Set<T> items, Function<T, JsonNode> converter) {
            this.addMember(key, items, converter, false, false);
        }

        public <T extends CvrBitemporalRecord> void addMember(String key, Set<T> items, Function<T, JsonNode> converter, boolean unwrapSingle, boolean forceArray) {
            ObjectMapper objectMapper = RecordOutputWrapper.this.getObjectMapper();
            for (T item : items) {
                JsonNode value = (converter != null) ? converter.apply(item) : objectMapper.valueToTree(item);
                if (value instanceof ObjectNode) {
                    ((ObjectNode) value).remove(removeFieldNames);
                }
                if (unwrapSingle && value.size() == 1) {
                    this.add(item.getBitemporality(), key, value.get(value.fieldNames().next()));
                } else {
                    this.add(item.getBitemporality(), key, value);
                }
            }
            if (forceArray) {
                this.forcedArrayKeys.add(key);
            }
        }

        public void addAttributeMember(String key, Set<AttributeRecord> attributes) {
            ObjectMapper objectMapper = RecordOutputWrapper.this.getObjectMapper();
            for (AttributeRecord attribute : attributes) {
                ObjectNode attributeNode = objectMapper.createObjectNode();
                attributeNode.put(AttributeRecord.IO_FIELD_TYPE, attribute.getType());
                attributeNode.put(AttributeRecord.IO_FIELD_SEQUENCENUMBER, attribute.getSequenceNumber());
                attributeNode.put(AttributeRecord.IO_FIELD_VALUETYPE, attribute.getValueType());
                attributeNode.set(AttributeRecord.IO_FIELD_VALUES, objectMapper.createArrayNode());

                ListHashMap<Bitemporality, AttributeValueRecord> valueBuckets = new ListHashMap<>();
                for (AttributeValueRecord valueRecord : attribute.getValues()) {
                    valueBuckets.add(valueRecord.getBitemporality(), valueRecord);
                }

                for (Bitemporality bitemporality : valueBuckets.keySet()) {
                    ObjectNode instance = attributeNode.deepCopy();
                    this.add(bitemporality, key, instance);
                    ArrayNode valueList = (ArrayNode) instance.get(AttributeRecord.IO_FIELD_VALUES);
                    for (AttributeValueRecord valueRecord : valueBuckets.get(bitemporality)) {
                        valueList.add(valueRecord.getValue());
                    }
                }
            }
        }

        public ArrayNode toJson() {
            ObjectMapper objectMapper = RecordOutputWrapper.this.getObjectMapper();
            ArrayNode registrationsNode = objectMapper.createArrayNode();
            ArrayList<Bitemporality> bitemporalities = new ArrayList<>(this.keySet());
            //bitemporalities.sort(Comparator.nullsFirst(new BitemporalityRegistrationFromComparator()));

            ListHashMap<OffsetDateTime, Bitemporality> startTerminators = new ListHashMap<>();
            ListHashMap<OffsetDateTime, Bitemporality> endTerminators = new ListHashMap<>();

            for (Bitemporality bitemporality : bitemporalities) {
                startTerminators.add(bitemporality.registrationFrom, bitemporality);
                endTerminators.add(bitemporality.registrationTo, bitemporality);
            }

            HashSet<OffsetDateTime> allTerminators = new HashSet<>();
            allTerminators.addAll(startTerminators.keySet());
            allTerminators.addAll(endTerminators.keySet());
            // Create a sorted list of all timestamps where Bitemporalities either begin or end
            ArrayList<OffsetDateTime> terminators = new ArrayList<>(allTerminators);
            terminators.sort(Comparator.nullsFirst(OffsetDateTime::compareTo));
            terminators.add(null);

            HashSet<Bitemporality> presentBitemporalities = new HashSet<>();

            for (int i=0; i<terminators.size(); i++) {
                OffsetDateTime t = terminators.get(i);
                List<Bitemporality> startingHere = startTerminators.get(t);
                List<Bitemporality> endingHere = endTerminators.get(t);
                if (startingHere != null) {
                    presentBitemporalities.addAll(startingHere);
                }
                if (endingHere != null) {
                    presentBitemporalities.removeAll(endingHere);
                }
                if (i < terminators.size() - 1) {
                    OffsetDateTime next = terminators.get(i + 1);
                    if (!presentBitemporalities.isEmpty()) {
                        ObjectNode registrationNode = objectMapper.createObjectNode();
                        registrationsNode.add(registrationNode);
                        registrationNode.put("registreringFra", formatTime(t));
                        registrationNode.put("registreringTil", formatTime(next));
                        ArrayNode effectsNode = objectMapper.createArrayNode();
                        registrationNode.set("virkninger", effectsNode);
                        ArrayList<Bitemporality> sortedEffects = new ArrayList<>(presentBitemporalities);
                        sortedEffects.sort(effectComparator);
                        Bitemporality lastEffect = null;
                        ObjectNode effectNode = null;
                        for (Bitemporality bitemporality : sortedEffects) {
                            if (lastEffect == null || effectNode == null || !lastEffect.equalEffect(bitemporality)) {
                                effectNode = objectMapper.createObjectNode();
                                effectsNode.add(effectNode);
                            }
                            effectNode.put("virkningFra", formatTime(bitemporality.effectFrom, true));
                            effectNode.put("virkningTil", formatTime(bitemporality.effectTo, true));
                            HashMap<String, ArrayList<JsonNode>> records = this.get(bitemporality);
                            for (String key : records.keySet()) {
                                ArrayList<JsonNode> r = records.get(key);
                                if (r.size() == 1 && !this.isArrayForced(key)) {
                                    effectNode.set(key, r.get(0));
                                } else {
                                    ArrayNode a = objectMapper.createArrayNode();
                                    effectNode.set(key, a);
                                    for (JsonNode q : r) {
                                        a.add(q);
                                    }
                                }
                            }
                            lastEffect = bitemporality;
                        }
                    }
                }
            }
            return registrationsNode;
        }
    }

    protected final ObjectNode createNode(T companyRecord) {
        ObjectMapper objectMapper = this.getObjectMapper();
        ObjectNode root = objectMapper.createObjectNode();
        try {
            //root.put(CompanyEntity.IO_FIELD_UUID, companyRecord.getIdentification().getUuid().toString());

            OutputContainer recordOutput = new OutputContainer();
            this.fillContainer(recordOutput, companyRecord);
            root.set("registrations", recordOutput.toJson());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    protected abstract void fillContainer(OutputContainer container, T item);

    protected abstract ObjectMapper getObjectMapper();

    protected static final Comparator<Bitemporality> effectComparator =
            Comparator.nullsFirst(new BitemporalityComparator(BitemporalityComparator.Type.EFFECT_FROM))
            .thenComparing(Comparator.nullsLast(new BitemporalityComparator(BitemporalityComparator.Type.EFFECT_TO)));

    protected static String formatTime(OffsetDateTime time) {
        return formatTime(time, false);
    }

    protected static String formatTime(OffsetDateTime time, boolean asDateOnly) {
        if (time == null) return null;
        return time.format(asDateOnly ? DateTimeFormatter.ISO_LOCAL_DATE : DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    protected static String formatTime(LocalDate time) {
        if (time == null) return null;
        return time.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    protected JsonNode createAddressNode(AddressRecord record) {
        ObjectNode adresseNode = this.createItemNode(record);
        adresseNode.put(AddressRecord.IO_FIELD_ROADCODE, record.getRoadCode());
        adresseNode.put(AddressRecord.IO_FIELD_HOUSE_FROM, record.getHouseNumberFrom());
        adresseNode.put(AddressRecord.IO_FIELD_FLOOR, record.getFloor());
        adresseNode.put(AddressRecord.IO_FIELD_DOOR, record.getDoor());
        adresseNode.put(AddressRecord.IO_FIELD_POSTDISTRICT, record.getPostdistrikt());
        adresseNode.put(AddressRecord.IO_FIELD_ROADNAME, record.getRoadName());
        adresseNode.put(AddressRecord.IO_FIELD_HOUSE_TO, record.getHouseNumberTo());
        adresseNode.put(AddressRecord.IO_FIELD_POSTCODE, record.getPostnummer());
        adresseNode.put(AddressRecord.IO_FIELD_CITY, record.getSupplementalCityName());
        adresseNode.put(AddressRecord.IO_FIELD_TEXT, record.getAddressText());
        adresseNode.put(AddressRecord.IO_FIELD_COUNTRYCODE, record.getCountryCode());
        AddressMunicipalityRecord kommune = record.getMunicipality();
        if (kommune != null) {
            adresseNode.put(Municipality.IO_FIELD_CODE, kommune.getMunicipalityCode());
            adresseNode.put(Municipality.IO_FIELD_NAME, kommune.getMunicipalityName());
        }
        return adresseNode;
    }

    protected JsonNode createLifecycleNode(LifecycleRecord record) {
        return BooleanNode.getTrue();
    }

    protected static LocalDate getUTCDate(OffsetDateTime offsetDateTime) {
        return offsetDateTime.atZoneSameInstant(ZoneId.of("UTC")).toLocalDate();
    }

    private ObjectNode createItemNode(CvrRecord record) {
        return this.getObjectMapper().createObjectNode();
    }

}
