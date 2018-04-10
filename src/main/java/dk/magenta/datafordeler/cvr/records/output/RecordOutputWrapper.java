package dk.magenta.datafordeler.cvr.records.output;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
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
public abstract class RecordOutputWrapper<E extends CvrEntityRecord> extends OutputWrapper<E> {

    protected class OutputContainer {

        private final List<String> removeFieldNames = Arrays.asList(new String[]{"periode", "sidstOpdateret", "sidstIndlaest", "dafoOpdateret"});

        private DoubleListHashMap<Bitemporality, String, JsonNode> bitemporalData = new DoubleListHashMap<>();

        private ListHashMap<String, JsonNode> nontemporalData = new ListHashMap<>();

        private HashSet<String> forcedArrayKeys = new HashSet<>();

        public boolean isArrayForced(String key) {
            return this.forcedArrayKeys.contains(key);
        }

        public <T extends CvrBitemporalRecord> void addBitemporal(String key, Set<T> records) {
            this.addBitemporal(key, records, null, false, false);
        }

        public <T extends CvrBitemporalRecord> void addBitemporal(String key, Set<T> records, boolean unwrapSingle) {
            this.addBitemporal(key, records, null, unwrapSingle, false);
        }

        public <T extends CvrBitemporalRecord> void addBitemporal(String key, Set<T> records, Function<T, JsonNode> converter) {
            this.addBitemporal(key, records, converter, false, false);
        }

        public <T extends CvrBitemporalRecord> void addBitemporal(String key, Set<T> records, Function<T, JsonNode> converter, boolean unwrapSingle, boolean forceArray) {
            ObjectMapper objectMapper = RecordOutputWrapper.this.getObjectMapper();
            for (T record : records) {
                if (record != null) {
                    JsonNode value = (converter != null) ? converter.apply(record) : objectMapper.valueToTree(record);
                    if (value instanceof ObjectNode) {
                        ObjectNode oValue = (ObjectNode) value;
                        oValue.remove(removeFieldNames);
                        if (unwrapSingle && value.size() == 1) {
                            this.bitemporalData.add(record.getBitemporality(), key, oValue.get(oValue.fieldNames().next()));
                            continue;
                        }
                    }
                    this.bitemporalData.add(record.getBitemporality(), key, value);
                }
            }
            if (forceArray) {
                this.forcedArrayKeys.add(key);
            }
        }

        public void addAttribute(String key, Set<AttributeRecord> attributes) {
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
                    this.bitemporalData.add(bitemporality, key, instance);
                    ArrayNode valueList = (ArrayNode) instance.get(AttributeRecord.IO_FIELD_VALUES);
                    for (AttributeValueRecord valueRecord : valueBuckets.get(bitemporality)) {
                        valueList.add(valueRecord.getValue());
                    }
                }
            }
        }
        public <T extends CvrNontemporalRecord> void addNontemporal(String key, T record) {
            this.addNontemporal(key, Collections.singleton(record), null, false, false);
        }

        public <T extends CvrNontemporalRecord> void addNontemporal(String key, Function<T, JsonNode> converter, T record) {
            this.addNontemporal(key, Collections.singleton(record), converter, false, false);
        }

        public <T extends CvrNontemporalRecord> void addNontemporal(String key, Set<T> records) {
            this.addNontemporal(key, records, null, false, false);
        }

        public <T extends CvrNontemporalRecord> void addNontemporal(String key, Set<T> records, Function<T, JsonNode> converter, boolean unwrapSingle, boolean forceArray) {
            ObjectMapper objectMapper = RecordOutputWrapper.this.getObjectMapper();
            for (T record : records) {
                JsonNode value = (converter != null) ? converter.apply(record) : objectMapper.valueToTree(record);
                if (value instanceof ObjectNode) {
                    ObjectNode oValue = (ObjectNode) value;
                    if (unwrapSingle && value.size() == 1) {
                        this.nontemporalData.add(key, oValue.get(oValue.fieldNames().next()));
                        continue;
                    }
                }
                this.nontemporalData.add(key, value);
            }
            if (forceArray) {
                this.forcedArrayKeys.add(key);
            }
        }

        public void addNontemporal(String key, Boolean data) {
            this.nontemporalData.add(key, data != null ? (data ? BooleanNode.getTrue() : BooleanNode.getFalse()) : null);
        }

        public void addNontemporal(String key, Integer data) {
            this.nontemporalData.add(key, data != null ? new IntNode(data) : null);
        }

        public void addNontemporal(String key, Long data) {
            this.nontemporalData.add(key, data != null ? new LongNode(data) : null);
        }

        public void addNontemporal(String key, String data) {
            this.nontemporalData.add(key, data != null ? new TextNode(data) : null);
        }

        public void addNontemporal(String key, LocalDate data) {
            this.nontemporalData.add(key, data != null ? new TextNode(data.format(DateTimeFormatter.ISO_LOCAL_DATE)) : null);
        }

        public void addNontemporal(String key, OffsetDateTime data) {
            this.nontemporalData.add(key, data != null ? new TextNode(data.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)) : null);
        }


        public ArrayNode getRegistrations(Bitemporality mustOverlap) {

            ObjectMapper objectMapper = RecordOutputWrapper.this.getObjectMapper();
            ArrayNode registrationsNode = objectMapper.createArrayNode();
            ArrayList<Bitemporality> bitemporalities = new ArrayList<>(this.bitemporalData.keySet());

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

                        if (mustOverlap == null || mustOverlap.overlapsRegistration(t, next)) {
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
                                // Implemented in Hibernate filters instead. Each stored effect can be tested against the query filter
                                // on the database level, but registrations are split here and thus cannot be tested in the database
                                // Also, they lack the range end due to the way the incoming data is formatted
                                //if (mustOverlap == null || mustOverlap.overlapsEffect(bitemporality.effectFrom, bitemporality.effectTo)) {
                                    if (lastEffect == null || effectNode == null || !lastEffect.equalEffect(bitemporality)) {
                                        effectNode = objectMapper.createObjectNode();
                                        effectsNode.add(effectNode);
                                    }
                                    effectNode.put("virkningFra", formatTime(bitemporality.effectFrom, true));
                                    effectNode.put("virkningTil", formatTime(bitemporality.effectTo, true));
                                    HashMap<String, ArrayList<JsonNode>> records = this.bitemporalData.get(bitemporality);
                                    for (String key : records.keySet()) {
                                        this.setValue(objectMapper, effectNode, key, records.get(key));
                                    }
                                    lastEffect = bitemporality;
                                //}
                            }
                        }
                    }
                }
            }
            return registrationsNode;
        }

        public ObjectNode getBase() {
            ObjectMapper objectMapper = RecordOutputWrapper.this.getObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            for (String key : this.nontemporalData.keySet()) {
                this.setValue(objectMapper, objectNode, key, this.nontemporalData.get(key));
            }
            return objectNode;
        }

        private void setValue(ObjectMapper objectMapper, ObjectNode objectNode, String key, List<JsonNode> values) {
            if (values.size() == 1 && !this.isArrayForced(key)) {
                objectNode.set(key, values.get(0));
            } else {
                ArrayNode array = objectMapper.createArrayNode();
                objectNode.set(key, array);
                for (JsonNode value : values) {
                    array.add(value);
                }
            }
        }
    }

    protected final ObjectNode getNode(E record, Bitemporality mustContain) {
        ObjectMapper objectMapper = this.getObjectMapper();
        ObjectNode root = objectMapper.createObjectNode();
        try {
            //root.put(CompanyEntity.IO_FIELD_UUID, record.getIdentification().getUuid().toString());

            OutputContainer recordOutput = new OutputContainer();
            this.fillContainer(recordOutput, record);

            root.setAll(recordOutput.getBase());
            root.set("registreringer", recordOutput.getRegistrations(mustContain));

            OutputContainer metadataRecordOutput = new OutputContainer();
            this.fillMetadataContainer(metadataRecordOutput, record);
            ObjectNode metaNode = objectMapper.createObjectNode();
            root.set("metadata", metaNode);
            metaNode.setAll(metadataRecordOutput.getBase());
            metaNode.set("registreringer", metadataRecordOutput.getRegistrations(mustContain));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    protected abstract void fillContainer(OutputContainer container, E item);

    protected abstract void fillMetadataContainer(OutputContainer container, E item);

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
