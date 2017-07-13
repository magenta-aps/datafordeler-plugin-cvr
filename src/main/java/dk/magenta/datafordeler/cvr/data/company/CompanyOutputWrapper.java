package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    root.put("CVRNummer", input.getCVRNummer());
    root.putPOJO("id", input.getIdentifikation());

    // Registreringer
    ArrayNode registreringer = objectMapper.createArrayNode();
    root.set("registreringer", registreringer);

    for(CompanyRegistration companyRegistration : input.getRegistreringer()) {
      registreringer.add(wrapRegistrering(companyRegistration));
    }

    try {
      System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(root));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return root;
  }

  protected ObjectNode wrapRegistrering(CompanyRegistration input) {
    ObjectNode output = objectMapper.createObjectNode();

    output.put(
        "registreringFra",
        input.getRegistreringFra() != null ? input.getRegistreringFra().toString() : null
    );
    output.put(
        "registreringTil",
        input.getRegistreringTil() != null ? input.getRegistreringTil().toString() : null
    );

    for(CompanyEffect virkning : input.getVirkninger()) {

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

  protected ObjectNode createVirksomhedObject (ObjectNode node, CompanyBaseData virksomhed)
  {
    node.put("CVRNummer", virksomhed.getCVRNummer());

    node.put("virksomhedsnavn", virksomhed.getVirksomhedsnavn());

    CompanyForm virksomhedsform = virksomhed.getVirksomhedsform();
    String virksomhedsformkode = null;
    String dataleverandør = null;
    if(virksomhedsform != null) {
      virksomhedsformkode = virksomhedsform.getVirksomhedsformkode();
      dataleverandør = virksomhedsform.getAnsvarligDataleverandoer();
    }
    node.put("virksomhedsform", virksomhedsformkode);
    node.put("dataleverandør", dataleverandør);

    ObjectNode beliggenhedsadresseObject = addAdresseObject(virksomhed.getBeliggenhedsadresse());
    node.set("beliggenhedsadresse", beliggenhedsadresseObject);

    ObjectNode postadresseObject = addAdresseObject(virksomhed.getPostadresse());
    node.set("postadresse", postadresseObject);

    node.put("hovedbranche", virksomhed.getHovedbranche());

    node.put("bibbranche1", virksomhed.getBibranche1());

    node.put("bibbranche2", virksomhed.getBibranche2());

    node.put("bibbranche3", virksomhed.getBibranche3());

    node.putPOJO("kreditOplysning", null); // Missing in input

    node.put("reklamebeskyttelse", virksomhed.getReklamebeskyttelse());

    node.put("telefonnummer", virksomhed.getTelefonnummer());

    node.put("emailadresse", virksomhed.getEmailadresse());

    node.put("telefaxnummer", virksomhed.getTelefaxnummer());

    LifecycleData livsforloeb = virksomhed.getLivsforloeb();
    OffsetDateTime virksomhedStartdato = null;
    OffsetDateTime virksomhedOphørsdato = null;
    if(virksomhedsform != null) {
      virksomhedStartdato = livsforloeb.getStartDato();
      virksomhedOphørsdato = livsforloeb.getSlutDato();
    }
    node.put("virksomhedStartdato", virksomhedStartdato != null ? virksomhedStartdato.toLocalDate().toString() : null);
    node.put("virksomhedOphørsdato", virksomhedOphørsdato != null ? virksomhedOphørsdato.toLocalDate().toString() : null);

    ArrayNode fuldtAnsvarligDeltagerRelation = objectMapper.createArrayNode();
    for (ParticipantRelationData participantRelationData : virksomhed.getDeltagerRelationer()) {
      fuldtAnsvarligDeltagerRelation.addPOJO(participantRelationData.getDeltager());
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
