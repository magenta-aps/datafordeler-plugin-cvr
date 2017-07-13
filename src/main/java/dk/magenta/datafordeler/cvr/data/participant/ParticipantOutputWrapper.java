package dk.magenta.datafordeler.cvr.data.participant;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    root.put("deltagerNummer", input.getDeltagernummer());
    root.putPOJO("id", input.getIdentifikation());

    // Registreringer
    ArrayNode registreringer = objectMapper.createArrayNode();
    root.set("registreringer", registreringer);

    for(ParticipantRegistration participantRegistration : input.getRegistreringer()) {
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
        input.getRegistreringFra() != null ? input.getRegistreringFra().toString() : null
    );
    output.put(
        "registreringTil",
        input.getRegistreringTil() != null ? input.getRegistreringTil().toString() : null
    );

    for(ParticipantEffect virkning : input.getVirkninger()) {

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
        virkning.getVirkningFra() != null ? virkning.getVirkningFra().toString() : null
    );
    if(includeVirkningTil) {
      output.put(
          "virkningTil",
          virkning.getVirkningTil() != null ? virkning.getVirkningTil().toString() : null
      );
    }
    return output;
  }

  protected ObjectNode createDeltagerObject (ObjectNode node, ParticipantBaseData deltager)
  {
    node.put("navne", deltager.getNavne());

    ObjectNode beliggenhedsadresseObject = addAdresseObject(deltager.getBeliggenhedsadresse());
    node.set("beliggenhedsadresse", beliggenhedsadresseObject);

    ObjectNode postadresseObject = addAdresseObject(deltager.getPostadresse());
    node.set("postadresse", postadresseObject);

    ObjectNode forretningsadresseObject = addAdresseObject(deltager.getForretningsadresse());
    node.set("forretningsadresse", forretningsadresseObject);

    node.put("telefonnummer", deltager.getTelefonnummer());

    node.put("emailadresse", deltager.getEmailadresse());

    node.put("telefaxnummer", deltager.getTelefaxnummer());

    node.put("enhedsnummer", deltager.getEnhedsnummer());

    node.put("enhedstype", deltager.getEnhedstype());

    node.put("rolle", deltager.getRolle());

    node.put("status", deltager.getStatus());

    node.putPOJO("attributter", deltager.getAttributter());

    return node;
  }

  protected ObjectNode addAdresseObject(Address adresse) {

    ObjectNode adresseObject = objectMapper.createObjectNode();

    if(adresse != null) {
      ObjectNode json = objectMapper.createObjectNode();

      json.put("vejkode", adresse.getVejkode());
      json.put("hunummerFra", adresse.getHusnummerFra());
      json.put("etagebetegnelse", adresse.getEtagebetegnelse());
      json.put("dørbetegnelse", adresse.getDørbetegnelse());

      String kommunekode = null;
      String kommunenavn = null;
      Municipality kommune = adresse.getKommune();
      if(kommune != null) {
        kommunekode = kommune.getKommunekode();
        kommunenavn = kommune.getKommunenavn();
      }
      json.put("kommunekode", kommunekode);
      json.put("kommunenavn", kommunenavn);

      json.put("postdistrikt", adresse.getPostdistrikt());
      json.put("vejnavn", adresse.getVejnavn());
      json.put("husnummerTil", adresse.getHusnummerTil());
      json.put("postnummer", adresse.getPostnummer());
      json.put("supplerendeBynavn", adresse.getSupplerendeBynavn());
      json.put("adresseFritekst", adresse.getAdresseFritekst());
      json.put("landekode", adresse.getLandekode());

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
