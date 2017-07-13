package dk.magenta.datafordeler.cvr.data.companyunit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.magenta.datafordeler.core.database.Effect;
import dk.magenta.datafordeler.core.fapi.OutputWrapper;
import dk.magenta.datafordeler.cvr.data.shared.LifecycleData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;
import java.time.OffsetDateTime;

public class CompanyUnitOutputWrapper extends OutputWrapper<CompanyUnitEntity> {

  private ObjectMapper objectMapper;

  @Override
  public Object wrapResult(CompanyUnitEntity input) {

    objectMapper = new ObjectMapper();

    // Root
    ObjectNode root = objectMapper.createObjectNode();

    root.put("PNummer", input.getPNumber());
    root.putPOJO("id", input.getIdentifikation());

    // Registreringer
    ArrayNode registreringer = objectMapper.createArrayNode();
    root.set("registreringer", registreringer);

    for(CompanyUnitRegistration companyUnitRegistration : input.getRegistreringer()) {
      registreringer.add(wrapRegistrering(companyUnitRegistration));
    }

    try {
      System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return root;
  }

  protected ObjectNode wrapRegistrering(CompanyUnitRegistration input) {
    ObjectNode output = objectMapper.createObjectNode();

    output.put(
        "registreringFra",
        input.getRegistreringFra() != null ? input.getRegistreringFra().toString() : null
    );
    output.put(
        "registreringTil",
        input.getRegistreringTil() != null ? input.getRegistreringTil().toString() : null
    );

    for(CompanyUnitEffect virkning : input.getVirkninger()) {

      ObjectNode virkningObject = createVirkning(virkning, true);

      for (CompanyUnitBaseData companyUnitBaseData : virkning.getDataItems()) {

        addEffectDataToRegistration(
            output, "produktionsenhed", createVirksomhedObject(virkningObject, companyUnitBaseData)
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

  protected ObjectNode createVirksomhedObject (ObjectNode node, CompanyUnitBaseData produktionsenhed)
  {
    node.put("pNummer", produktionsenhed.getpNummer());

    node.put("produktionsenhedsnavn", produktionsenhed.getProduktionsenhedsnavn());

    ObjectNode beliggenhedsadresseObject = addAdresseObject(produktionsenhed.getBeliggenhedsadresse());
    node.set("beliggenhedsadresse", beliggenhedsadresseObject);

    ObjectNode postadresseObject = addAdresseObject(produktionsenhed.getPostadresse());
    node.set("postadresse", postadresseObject);

    node.put("hovedbranche", produktionsenhed.getHovedbranche());

    node.put("bibbranche1", produktionsenhed.getBibranche1());

    node.put("bibbranche2", produktionsenhed.getBibranche2());

    node.put("bibbranche3", produktionsenhed.getBibranche3());

    node.put("reklamebeskyttelse", produktionsenhed.getReklamebeskyttelse());

    node.put("telefonnummer", produktionsenhed.getTelefonnummer());

    node.put("emailadresse", produktionsenhed.getEmailadresse());

    node.put("telefaxnummer", produktionsenhed.getTelefaxnummer());

    // TODO Tilføj virksomhedsrelationer til json output og i første omgrang til CompanyUnitRecord
    node.set("tilknyttetVirksomhedsCVRNummer", null); // Missing in input

    LifecycleData livsforloeb = produktionsenhed.getLivsforloeb();
    OffsetDateTime produktionsenhedStartdato = null;
    OffsetDateTime produktionsenhedOphørsdato = null;
    if(livsforloeb != null) {
      produktionsenhedStartdato = livsforloeb.getStartDato();
      produktionsenhedOphørsdato = livsforloeb.getSlutDato();
    }
    node.put("produktionsenhedStartdato", produktionsenhedStartdato != null ? produktionsenhedStartdato.toLocalDate().toString() : null);
    node.put("produktionsenhedOphørsdato", produktionsenhedOphørsdato != null ? produktionsenhedOphørsdato.toLocalDate().toString() : null);

    // TODO Tilføj virksomhedsrelationer til json output og i første omgrang til CompanyUnitRecord
    node.set("tilknyttetVirksomhedsStartdato", null); // Missing in input
    node.set("tilknyttetVirksomhedsOphørsdato", null); // Missing in input

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
