package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dk.magenta.datafordeler.core.database.Effect;
import dk.magenta.datafordeler.core.fapi.OutputWrapper;
import dk.magenta.datafordeler.cvr.data.shared.LifecycleData;
import dk.magenta.datafordeler.cvr.data.shared.ParticipantRelationData;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;
import dk.magenta.datafordeler.cvr.data.unversioned.Municipality;
import java.time.OffsetDateTime;

public class CompanyOutputWrapper extends OutputWrapper<CompanyEntity> {

  private ObjectMapper objectMapper;

  @Override
  public Object wrapResult(CompanyEntity input) {

    objectMapper = new ObjectMapper();

    // Root
    ObjectNode root = objectMapper.createObjectNode();

    root.put("UUID", input.getUUID().toString());
    root.put("CVRNummer", input.getCvrNumber());
    root.putPOJO("id", input.getIdentification());

    // Registreringer
    ArrayNode registreringer = objectMapper.createArrayNode();
    root.set("registreringer", registreringer);

    for(CompanyRegistration companyRegistration : input.getRegistrations()) {
      registreringer.add(wrapRegistrering(companyRegistration));
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

    for(CompanyEffect virkning : input.getSortedEffects()) {

      ObjectNode virkningObject = createVirkning(virkning, true);

      for (CompanyBaseData companyUnitBaseData : virkning.getDataItems()) {

        addEffectDataToRegistration(
            output, "virksomhed", createVirksomhedObject(virkningObject, companyUnitBaseData)
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

  protected ObjectNode createVirksomhedObject (ObjectNode node, CompanyBaseData virksomhed)
  {
    node.put("CVRNummer", virksomhed.getCvrNumber());

    node.put("virksomhedsnavn", virksomhed.getCompanyName());

    CompanyForm virksomhedsform = virksomhed.getCompanyForm();
    String virksomhedsformkode = null;
    String dataleverandør = null;
    if(virksomhedsform != null) {
      virksomhedsformkode = virksomhedsform.getCompanyFormCode();
      dataleverandør = virksomhedsform.getResponsibleDataSource();
    }
    node.put("virksomhedsform", virksomhedsformkode);
    node.put("dataleverandør", dataleverandør);

    ObjectNode beliggenhedsadresseObject = addAdresseObject(virksomhed.getLocationAddress());
    node.set("beliggenhedsadresse", beliggenhedsadresseObject);

    ObjectNode postadresseObject = addAdresseObject(virksomhed.getPostalAddress());
    node.set("postadresse", postadresseObject);

    node.put("hovedbranche", virksomhed.getPrimaryIndustry());

    node.put("bibbranche1", virksomhed.getSecondaryIndustry1());

    node.put("bibbranche2", virksomhed.getSecondaryIndustry2());

    node.put("bibbranche3", virksomhed.getSecondaryIndustry3());

    node.putPOJO("kreditOplysning", null); // Missing in input

    node.put("reklamebeskyttelse", virksomhed.getAdvertProtection());

    node.put("telefonnummer", virksomhed.getPhoneNumber());

    node.put("emailadresse", virksomhed.getEmailAddress());

    node.put("telefaxnummer", virksomhed.getFaxNumber());

    LifecycleData livsforloeb = virksomhed.getLifecycleData();
    OffsetDateTime virksomhedStartdato = null;
    OffsetDateTime virksomhedOphørsdato = null;
    if (livsforloeb != null) {
      virksomhedStartdato = livsforloeb.getStartDate();
      virksomhedOphørsdato = livsforloeb.getEndDate();
    }
    node.put("virksomhedStartdato", virksomhedStartdato != null ? virksomhedStartdato.toLocalDate().toString() : null);
    node.put("virksomhedOphørsdato", virksomhedOphørsdato != null ? virksomhedOphørsdato.toLocalDate().toString() : null);

    ArrayNode fuldtAnsvarligDeltagerRelation = objectMapper.createArrayNode();
    for (ParticipantRelationData participantRelationData : virksomhed.getParticipantRelations()) {
      fuldtAnsvarligDeltagerRelation.addPOJO(participantRelationData.getParticipant());
    }
    if(fuldtAnsvarligDeltagerRelation.size() == 0)
      fuldtAnsvarligDeltagerRelation = null;

    node.putPOJO("fuldtAnsvarligDeltagerRelation", fuldtAnsvarligDeltagerRelation);

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
