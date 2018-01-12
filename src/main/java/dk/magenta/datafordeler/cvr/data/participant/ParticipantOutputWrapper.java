package dk.magenta.datafordeler.cvr.data.participant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.magenta.datafordeler.core.database.Effect;
import dk.magenta.datafordeler.core.util.DoubleHashMap;
import dk.magenta.datafordeler.cvr.data.CvrOutputWrapper;
import dk.magenta.datafordeler.cvr.data.shared.AttributeData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;

import java.time.OffsetDateTime;
import java.util.*;

public class ParticipantOutputWrapper extends CvrOutputWrapper<ParticipantEntity> {

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
                    this.addEffectDataToRegistration(output, "beliggenhedsadresse", createAddressNode(virkning, timestamp, locationAddress));
                }

                Address postalAddress = participantBaseData.getLocationAddress();
                if (postalAddress != null) {
                    this.addEffectDataToRegistration(output, "postadresse", createAddressNode(virkning, timestamp, postalAddress));
                }

                Address businessAddress = participantBaseData.getBusinessAddress();
                if (businessAddress != null) {
                    this.addEffectDataToRegistration(output, "forretningsadresse", createAddressNode(virkning, timestamp, businessAddress));
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

    private ObjectNode createNameNode(Effect virkning, OffsetDateTime lastUpdated, Collection<String> names) {
        ArrayNode listNode = objectMapper.createArrayNode();
        for (String name : names) {
            listNode.add(name);
        }
        ObjectNode nameNode = createVirkning(virkning, lastUpdated);
        nameNode.set("navne", listNode);
        return nameNode;
    }


}
