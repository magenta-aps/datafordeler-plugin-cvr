package dk.magenta.datafordeler.cvr.data.participant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.magenta.datafordeler.core.database.Effect;
import dk.magenta.datafordeler.core.fapi.OutputWrapper;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;

public class ParticipantOutputWrapper extends OutputWrapper<ParticipantEntity> {

  private ObjectMapper objectMapper;

  @Override
  public Object wrapResult(ParticipantEntity input) {

    objectMapper = new ObjectMapper();

    // Root
    ObjectNode root = objectMapper.createObjectNode();

    root.put("deltagerNummer", input.getParticipantNumber());
    root.putPOJO("id", input.getIdentification());

    // Registreringer
    ArrayNode registreringer = objectMapper.createArrayNode();
    root.set("registreringer", registreringer);

    for(ParticipantRegistration participantRegistration : input.getRegistrations()) {
      registreringer.add(wrapRegistrering(participantRegistration));
    }

    /*
    try {
      System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    */
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

    for(ParticipantEffect virkning : input.getEffects()) {

      ObjectNode virkningObject = createVirkning(virkning, true);

      for (ParticipantBaseData participantBaseData : virkning.getDataItems()) {

        addEffectDataToRegistration(
            output, "deltager", createDeltagerObject(virkningObject, participantBaseData)
        );
      }
    }

    return output;
  }

  protected void addEffectDataToRegistration(ObjectNode output, String key, JsonNode value) {
    if(!output.has(key)) {
      output.set(key, objectMapper.createArrayNode());
    }
    ((ArrayNode)output.get(key)).add(value);
  }

  protected ObjectNode createVirkning(Effect virkning, boolean includeVirkningTil) {

    ObjectNode output = objectMapper.createObjectNode();

    output.put(
        "virkningFra",
        virkning.getEffectFrom() != null ? virkning.getEffectFrom().toString() : null
    );
    if(includeVirkningTil) {
      output.put(
          "virkningTil",
          virkning.getEffectTo() != null ? virkning.getEffectTo().toString() : null
      );
    }
    return output;
  }

  protected ObjectNode createDeltagerObject (ObjectNode node, ParticipantBaseData deltager)
  {
    node.put("navne", deltager.getNames());

    ObjectNode beliggenhedsadresseObject = addAdresseObject(deltager.getLocationAddress());
    node.set("beliggenhedsadresse", beliggenhedsadresseObject);

    ObjectNode postadresseObject = addAdresseObject(deltager.getPostalAddress());
    node.set("postadresse", postadresseObject);

    ObjectNode forretningsadresseObject = addAdresseObject(deltager.getBusinessAddress());
    node.set("forretningsadresse", forretningsadresseObject);

    node.put("telefonnummer", deltager.getPhoneNumber());

    node.put("emailadresse", deltager.getEmailAddress());

    node.put("telefaxnummer", deltager.getFaxNumber());

    node.put("enhedsnummer", deltager.getUnitNumber());

    node.put("enhedstype", deltager.getUnitType());

    node.put("rolle", deltager.getRole());

    node.put("status", deltager.getStatus());

    node.putPOJO("attributter", deltager.getAttributes());

    return node;
  }

  protected ObjectNode addAdresseObject(Address adresse) {

    ObjectNode adresseObject = objectMapper.createObjectNode();

    if(adresse != null) {
      ObjectNode json = objectMapper.createObjectNode();

      json.put("vejkode", adresse.getRoadCode());
      json.put("hunummerFra", adresse.getHouseNumberFrom());
      json.put("etagebetegnelse", adresse.getFloor());
      json.put("dørbetegnelse", adresse.getDoor());

      String kommunekode = null;
      String kommunenavn = null;
      Municipality kommune = adresse.getMunicipality();
      if(kommune != null) {
        kommunekode = kommune.getCode();
        kommunenavn = kommune.getName();
      }
      json.put("kommunekode", kommunekode);
      json.put("kommunenavn", kommunenavn);

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
    }
    else
      return null;

    return adresseObject;
  }
}
