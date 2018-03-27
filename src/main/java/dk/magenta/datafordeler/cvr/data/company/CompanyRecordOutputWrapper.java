package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import dk.magenta.datafordeler.core.fapi.OutputWrapper;
import dk.magenta.datafordeler.core.util.DoubleListHashMap;
import dk.magenta.datafordeler.core.util.ListHashMap;
import dk.magenta.datafordeler.cvr.data.Bitemporality;
import dk.magenta.datafordeler.cvr.data.BitemporalityComparator;
import dk.magenta.datafordeler.cvr.data.shared.LifecycleData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.Industry;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;
import dk.magenta.datafordeler.cvr.records.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

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
public class CompanyRecordOutputWrapper extends OutputWrapper<CompanyRecord> {

    private class OutputContainer extends DoubleListHashMap<Bitemporality, String, JsonNode> {

        private final List<String> removeFieldNames = Arrays.asList(new String[]{"periode", "sidstOpdateret", "sidstIndlaest"});

        public <T extends CvrBitemporalRecord> void addCompanyMember(String key, Set<T> items) {
            this.addCompanyMember(key, items, null, false);
        }

        public <T extends CvrBitemporalRecord> void addCompanyMember(String key, Set<T> items, boolean unwrapSingle) {
            this.addCompanyMember(key, items, null, unwrapSingle);
        }

        public <T extends CvrBitemporalRecord> void addCompanyMember(String key, Set<T> items, Function<T, JsonNode> converter) {
            this.addCompanyMember(key, items, converter, false);
        }

        public <T extends CvrBitemporalRecord> void addCompanyMember(String key, Set<T> items, Function<T, JsonNode> converter, boolean unwrapSingle) {
            for (T item : items) {
                JsonNode value = (converter != null) ? converter.apply(item) : CompanyRecordOutputWrapper.this.objectMapper.valueToTree(item);
                if (value instanceof ObjectNode) {
                    ((ObjectNode) value).remove(removeFieldNames);
                }
                if (unwrapSingle && value.size() == 1) {
                    this.add(item.getBitemporality(), key, value.get(value.fieldNames().next()));
                } else {
                    this.add(item.getBitemporality(), key, value);
                }
            }
        }

        public void addAttributeMember(String key, Set<AttributeRecord> attributes) {
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
                    this.add(bitemporality, CompanyRecord.IO_FIELD_ATTRIBUTES, instance);
                    ArrayNode valueList = (ArrayNode) instance.get(AttributeRecord.IO_FIELD_VALUES);
                    for (AttributeValueRecord valueRecord : valueBuckets.get(bitemporality)) {
                        valueList.add(valueRecord.getValue());
                    }
                }
            }
        }

    }



    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Object wrapResult(CompanyRecord companyRecord) {
        return this.asRVD(companyRecord);
        //return this.asRecord(companyRecord);
    }

    private ObjectNode asRecord(CompanyRecord record) {
        return objectMapper.valueToTree(record);
    }

    private static final Comparator<Bitemporality> effectComparator =
            Comparator.nullsFirst(new BitemporalityComparator(BitemporalityComparator.Type.EFFECT_FROM))
            .thenComparing(Comparator.nullsLast(new BitemporalityComparator(BitemporalityComparator.Type.EFFECT_TO)));

    private ObjectNode asRVD(CompanyRecord companyRecord) {
        ObjectNode root = objectMapper.createObjectNode();
        try {
            //root.put(CompanyEntity.IO_FIELD_UUID, companyRecord.getIdentification().getUuid().toString());
            root.putPOJO("id", companyRecord.getIdentification());
            root.put(CompanyEntity.IO_FIELD_CVR, companyRecord.getCvrNumber());

            //root.put()
            OutputContainer recordOutput = new OutputContainer();
            createCompanyNode(recordOutput, companyRecord);


            ArrayList<Bitemporality> bitemporalities = new ArrayList<>(recordOutput.keySet());
            //bitemporalities.sort(Comparator.nullsFirst(new BitemporalityRegistrationFromComparator()));

            ListHashMap<OffsetDateTime, Bitemporality> startTerminators = new ListHashMap<>();
            ListHashMap<OffsetDateTime, Bitemporality> endTerminators = new ListHashMap<>();

            for (Bitemporality bitemporality : bitemporalities) {
                startTerminators.add(bitemporality.registrationFrom, bitemporality);
                endTerminators.add(bitemporality.registrationTo, bitemporality);
            }

            // Create a sorted list of all timestamps where Bitemporalities either begin or end
            ArrayList<OffsetDateTime> terminators = new ArrayList<>();
            terminators.addAll(startTerminators.keySet());
            terminators.addAll(endTerminators.keySet());
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
                        registrationNode.put("registrationFrom", formatTime(t));
                        registrationNode.put("registrationTo", formatTime(next));
                        root.set("registreringer", registrationNode);
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
                            HashMap<String, ArrayList<JsonNode>> records = recordOutput.get(bitemporality);
                            for (String key : records.keySet()) {
                                ArrayList<JsonNode> r = records.get(key);
                                if (r.size() == 1) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

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

    private void createCompanyNode(OutputContainer container, CompanyRecord item) {
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



/*
        for (CompanyUnitLinkRecord companyUnitLinkRecord : otherRecord.getProductionUnits()) {
            this.addProductionUnit(companyUnitLinkRecord);
        }
        for (CompanyParticipantRelationRecord participantRelationRecord : otherRecord.getParticipants()) {
            //this.addParticipant(participantRelationRecord);
            this.mergeParticipant(participantRelationRecord);
        }
        for (FusionSplitRecord fusionSplitRecord : otherRecord.getFusions()) {
            //this.addFusion(fusionSplitRecord);
            this.mergeFusion(fusionSplitRecord);
        }
        for (FusionSplitRecord fusionSplitRecord : otherRecord.getSplits()) {
            //this.addSplit(fusionSplitRecord);
            this.mergeSplit(fusionSplitRecord);
        }*/
    }





/*

    private ObjectNode createFormNode(Effect virkning, OffsetDateTime lastUpdated, CompanyForm form) {
        ObjectNode formNode = createVirkning(virkning, lastUpdated);
        formNode.put(CompanyForm.IO_FIELD_CODE, form.getCompanyFormCode());
        formNode.put(CompanyForm.IO_FIELD_SOURCE, form.getResponsibleDataSource());
        return formNode;
    }

    private ObjectNode createCompanyParticipantNode(Effect virkning, OffsetDateTime lastUpdated, ParticipantRelationData participantRelation) {
        ObjectNode participantRelationNode = createVirkning(virkning, lastUpdated);
        participantRelationNode.set(ParticipantRelationData.IO_FIELD_PARTICIPANT, this.createIdentificationNode(participantRelation.getParticipant()));
        return participantRelationNode;
    }

    private ObjectNode createCompanyUnitLinkNode(Effect virkning, OffsetDateTime lastUpdated, CompanyUnitLink unitLink) {
        ObjectNode unitLinkNode = createVirkning(virkning, lastUpdated);
        unitLinkNode.put(CompanyUnitLink.IO_FIELD_PNUMBER, unitLink.getpNumber());
        unitLinkNode.set(CompanyUnitLink.IO_FIELD_IDENTIFICATION, this.createIdentificationNode(unitLink.getIdentification()));
        return unitLinkNode;
    }
*/

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
        return objectMapper.createObjectNode();
    }


}
