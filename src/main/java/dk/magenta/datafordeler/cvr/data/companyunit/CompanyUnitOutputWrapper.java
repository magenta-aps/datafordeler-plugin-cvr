package dk.magenta.datafordeler.cvr.data.companyunit;

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
    root.putPOJO("id", input.getIdentification());

    // Registreringer
    ArrayNode registreringer = objectMapper.createArrayNode();
    root.set("registreringer", registreringer);

    for(CompanyUnitRegistration companyUnitRegistration : input.getRegistrations()) {
      registreringer.add(wrapRegistrering(companyUnitRegistration));
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

  protected ObjectNode wrapRegistrering(CompanyUnitRegistration input) {
    ObjectNode output = objectMapper.createObjectNode();

    output.put(
        "registreringFra",
        input.getRegistrationFrom() != null ? input.getRegistrationFrom().toString() : null
    );
    output.put(
        "registreringTil",
        input.getRegistrationTo() != null ? input.getRegistrationTo().toString() : null
    );

    for(CompanyUnitEffect virkning : input.getSortedEffects()) {

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

  protected ObjectNode createVirksomhedObject (ObjectNode node, CompanyUnitBaseData produktionsenhed)
  {
    node.put("pNummer", produktionsenhed.getpNumber());

    node.put("produktionsenhedsnavn", produktionsenhed.getName());

    ObjectNode beliggenhedsadresseObject = addAdresseObject(produktionsenhed.getLocationAddress());
    node.set("beliggenhedsadresse", beliggenhedsadresseObject);

    ObjectNode postadresseObject = addAdresseObject(produktionsenhed.getPostalAddress());
    node.set("postadresse", postadresseObject);

    node.put("hovedbranche", produktionsenhed.getPrimaryIndustry());

    node.put("bibbranche1", produktionsenhed.getSecondaryIndustry1());

    node.put("bibbranche2", produktionsenhed.getSecondaryIndustry2());

    node.put("bibbranche3", produktionsenhed.getSecondaryIndustry3());

    node.put("reklamebeskyttelse", produktionsenhed.getAdvertProtection());

    node.put("telefonnummer", produktionsenhed.getPhoneNumber());

    node.put("emailadresse", produktionsenhed.getEmailAddress());

    node.put("telefaxnummer", produktionsenhed.getFaxNumber());

    // TODO Tilføj virksomhedsrelationer til json output og i første omgrang til CompanyUnitRecord
    node.set("tilknyttetVirksomhedsCVRNummer", null); // Missing in input

    LifecycleData livsforloeb = produktionsenhed.getLifecycleData();
    OffsetDateTime produktionsenhedStartdato = null;
    OffsetDateTime produktionsenhedOphørsdato = null;
    if(livsforloeb != null) {
      produktionsenhedStartdato = livsforloeb.getStartDate();
      produktionsenhedOphørsdato = livsforloeb.getEndDate();
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
