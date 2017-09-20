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
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Set;

public class CompanyOutputWrapper extends OutputWrapper<CompanyEntity> {

    private ObjectMapper objectMapper;

    HashMap<Long, ObjectNode> dataObjectCache = new HashMap<>();

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

        ArrayNode effectArray = objectMapper.createArrayNode();

        for (CompanyEffect virkning : input.getEffects()) {
            ObjectNode virkningObject = createVirkning(virkning, true);
            //ArrayNode array = objectMapper.createArrayNode();

            for (CompanyBaseData companyUnitBaseData : virkning.getDataItems()) {
                ObjectNode dataNode = this.dataObjectCache.get(companyUnitBaseData.getId());
                if (dataNode == null) {
                    //dataNode = createDataObject(virkningObject, companyUnitBaseData);
                    dataNode = createDataObject(companyUnitBaseData);
                    this.dataObjectCache.put(companyUnitBaseData.getId(), dataNode);
                }
                virkningObject.setAll(dataNode);
                //array.add(dataNode);
            }
            //output.set("virksomhed", array);
            effectArray.add(virkningObject);
        }
        output.set("virkninger", effectArray);

        return output;
    }

    protected ObjectNode createVirkning(Effect virkning, boolean includeVirkningTil) {
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
        return output;
    }

    protected ObjectNode createDataObject(CompanyBaseData dataItem) {
        ObjectNode node = objectMapper.createObjectNode();
        NodeWrapper wrapper = new NodeWrapper(node);
        wrapper.put("CVRNummer", dataItem.getCvrNumber());

        wrapper.put("virksomhedsnavn", dataItem.getCompanyName());

        CompanyForm virksomhedsform = dataItem.getCompanyForm();
        String virksomhedsformkode = null;
        String dataleverandør = null;
        if (virksomhedsform != null) {
            virksomhedsformkode = virksomhedsform.getCompanyFormCode();
            dataleverandør = virksomhedsform.getResponsibleDataSource();
        }
        wrapper.put("virksomhedsform", virksomhedsformkode);
        wrapper.put("dataleverandør", dataleverandør);

        ObjectNode beliggenhedsadresseObject = addAdresseObject(dataItem.getLocationAddress());
        wrapper.set("beliggenhedsadresse", beliggenhedsadresseObject);

        ObjectNode postadresseObject = addAdresseObject(dataItem.getPostalAddress());
        wrapper.set("postadresse", postadresseObject);

        wrapper.put("hovedbranche", dataItem.getPrimaryIndustry());

        wrapper.put("bibbranche1", dataItem.getSecondaryIndustry1());

        wrapper.put("bibbranche2", dataItem.getSecondaryIndustry2());

        wrapper.put("bibbranche3", dataItem.getSecondaryIndustry3());

        wrapper.putPOJO("kreditOplysning", null); // Missing in input

        wrapper.put("reklamebeskyttelse", dataItem.getAdvertProtection());

        wrapper.put("telefonnummer", dataItem.getPhoneNumber());

        wrapper.put("emailadresse", dataItem.getEmailAddress());

        wrapper.put("telefaxnummer", dataItem.getFaxNumber());


        LifecycleData lifeCycle = dataItem.getLifecycleData();
        if (lifeCycle != null) {
            OffsetDateTime startDate = lifeCycle.getStartDate();
            wrapper.put("virksomhedStartdato", startDate != null ? getUTCDate(startDate).toString() : null);
            OffsetDateTime endDate = lifeCycle.getEndDate();
            wrapper.put("virksomhedOphørsdato", endDate != null ? getUTCDate(endDate).toString() : null);
        }


        Set<ParticipantRelationData> participantRelations = dataItem.getParticipantRelations();
        if (participantRelations != null && !participantRelations.isEmpty()) {
            ArrayNode participantRelationNode = objectMapper.createArrayNode();
            wrapper.set("fuldtAnsvarligDeltagerRelation", participantRelationNode);
            for (ParticipantRelationData participantRelationData : participantRelations) {
                participantRelationNode.add(this.addIdentification(participantRelationData.getParticipant()));
            }
        }


        Set<CompanyUnitLink> unitLinks = dataItem.getUnitData();
        if (unitLinks != null && !unitLinks.isEmpty()) {
            ArrayNode unitLinkNode = objectMapper.createArrayNode();
            wrapper.set("penheder", unitLinkNode);
            for (CompanyUnitLink unitLink : unitLinks) {
                unitLinkNode.add(unitLink.getpNumber());
            }
        }


        return wrapper.getNode();
    }

    private static LocalDate getUTCDate(OffsetDateTime offsetDateTime) {
        return offsetDateTime.atZoneSameInstant(ZoneId.of("UTC")).toLocalDate();
    }

    protected ObjectNode addAdresseObject(Address adresse) {

        ObjectNode adresseObject = objectMapper.createObjectNode();

        if (adresse != null) {
            NodeWrapper inner = new NodeWrapper(objectMapper.createObjectNode());

            inner.put("vejkode", adresse.getRoadCode());
            inner.put("husnummerFra", adresse.getHouseNumberFrom());
            inner.put("etagebetegnelse", adresse.getFloor());
            inner.put("dørbetegnelse", adresse.getDoor());

            String kommunekode = null;
            String kommunenavn = null;
            Municipality kommune = adresse.getMunicipality();
            if (kommune != null) {
                kommunekode = kommune.getCode();
                kommunenavn = kommune.getName();
            }
            inner.put("kommunekode", kommunekode);
            inner.put("kommunenavn", kommunenavn);

            inner.put("postdistrikt", adresse.getPostdistrikt());
            inner.put("vejnavn", adresse.getRoadName());
            inner.put("husnummerTil", adresse.getHouseNumberTo());
            inner.put("postnummer", adresse.getPostnummer());
            inner.put("supplerendeBynavn", adresse.getSupplementalCityName());
            inner.put("adresseFritekst", adresse.getAddressText());
            inner.put("landekode", adresse.getCountryCode());

            adresseObject.set("CVRAdresse", inner.getNode());
            adresseObject.set("adresse1", null); // Missing in input
            adresseObject.set("adresse2", null); // Missing in input
            adresseObject.set("coNavn", null); // Missing in input
        } else
            return null;

        return adresseObject;
    }


    protected ObjectNode addIdentification(Identification identification) {
        ObjectNode identificationObject = objectMapper.createObjectNode();
        identificationObject.put("uuid", identification.getUuid().toString());
        identificationObject.put("domaene", identification.getDomain());
        return identificationObject;
    }

    public class NodeWrapper {
        private ObjectNode node;

        public NodeWrapper(ObjectNode node) {
            this.node = node;
        }

        public ObjectNode getNode() {
            return this.node;
        }

        public void put(String key, Boolean value) {
            if (value != null) {
                this.node.put(key, value);
            }
        }
        public void put(String key, Short value) {
            if (value != null) {
                this.node.put(key, value);
            }
        }
        public void put(String key, Integer value) {
            if (value != null) {
                this.node.put(key, value);
            }
        }
        public void put(String key, Long value) {
            if (value != null) {
                this.node.put(key, value);
            }
        }
        public void put(String key, String value) {
            if (value != null) {
                this.node.put(key, value);
            }
        }
        public void set(String key, JsonNode value) {
            if (value != null) {
                this.node.set(key, value);
            }
        }
        public void putPOJO(String key, Object value) {
            if (value != null) {
                this.node.putPOJO(key, value);
            }
        }
    }
}
