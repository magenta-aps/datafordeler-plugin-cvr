package dk.magenta.datafordeler.cvr.data.company;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.scenario.effect.Offset;
import dk.magenta.datafordeler.core.database.*;
import dk.magenta.datafordeler.core.exception.ParseException;
import dk.magenta.datafordeler.core.fapi.FapiService;
import dk.magenta.datafordeler.core.util.ListHashMap;
import dk.magenta.datafordeler.cvr.data.CvrEntityManager;
import dk.magenta.datafordeler.cvr.data.unversioned.Address;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyForm;
import dk.magenta.datafordeler.cvr.data.unversioned.CompanyStatus;
import dk.magenta.datafordeler.cvr.data.unversioned.Industry;
import javafx.util.Pair;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lars on 16-05-17.
 */
@Component
public class CompanyEntityManager extends CvrEntityManager {

    @Autowired
    private CompanyEntityService companyEntityService;

    @Autowired
    private QueryManager queryManager;

    @Autowired
    private SessionManager sessionManager;

    public CompanyEntityManager() {
        this.managedEntityClass = CompanyEntity.class;
        this.managedEntityReferenceClass = CompanyEntityReference.class;
        this.managedRegistrationClass = CompanyRegistration.class;
        this.managedRegistrationReferenceClass = CompanyRegistrationReference.class;
    }

    @Override
    protected String getBaseName() {
        return "company";
    }

    @Override
    public FapiService getEntityService() {
        return this.companyEntityService;
    }

    @Override
    public String getSchema() {
        return CompanyEntity.schema;
    }

    @Override
    protected RegistrationReference createRegistrationReference(URI uri) {
        return new CompanyRegistrationReference(uri);
    }

    @Override
    public List<Registration> parseRegistration(JsonNode jsonNode) throws ParseException {
        Session session = sessionManager.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        System.out.println("Parse jsonNode");
        jsonNode = this.unwrap(jsonNode);

        HashMap<OffsetDateTime, CompanyRegistration> registrations = new HashMap<>();

        CompanyEntity company = new CompanyEntity(UUID.randomUUID(), "cvr", jsonNode.get("cvrNummer").asInt());

        HashSet<CompanyRecord> records = new HashSet<>();


        for (JsonNode nameNode : jsonNode.get("navne")) {
            records.add(new CompanyNameRecord(nameNode));
        }
        for (JsonNode addressNode : jsonNode.get("beliggenhedsadresse")) {
            records.add(new CompanyLocationAddressRecord(addressNode));
        }
        for (JsonNode addressNode : jsonNode.get("postadresse")) {
            records.add(new CompanyPostalAddressRecord(addressNode));
        }
        for (JsonNode phoneNode : jsonNode.get("telefonNummer")) {
            records.add(new CompanyPhoneRecord(phoneNode));
        }
        for (JsonNode faxNode : jsonNode.get("telefaxNummer")) {
            records.add(new CompanyFaxRecord(faxNode));
        }
        for (JsonNode emailNode : jsonNode.get("elektroniskPost")) {
            records.add(new CompanyEmailRecord(emailNode));
        }
        for (JsonNode homepageNode : jsonNode.get("hjemmeside")) {
            records.add(new CompanyHomepageRecord(homepageNode));
        }
        for (JsonNode mEmailNode : jsonNode.get("obligatoriskEmail")) {
            records.add(new CompanyMandatoryEmailRecord(mEmailNode));
        }
        for (JsonNode lifecycleNode : jsonNode.get("livsforloeb")) {
            records.add(new CompanyLifecycleRecord(lifecycleNode));
        }
        for (JsonNode primaryIndustryNode : jsonNode.get("hovedbranche")) {
            records.add(new CompanyIndustryRecord(primaryIndustryNode, 0));
        }
        for (JsonNode secondaryIndustryNode : jsonNode.get("bibranche1")) {
            records.add(new CompanyIndustryRecord(secondaryIndustryNode, 1));
        }
        for (JsonNode secondaryIndustryNode : jsonNode.get("bibranche2")) {
            records.add(new CompanyIndustryRecord(secondaryIndustryNode, 2));
        }
        for (JsonNode secondaryIndustryNode : jsonNode.get("bibranche3")) {
            records.add(new CompanyIndustryRecord(secondaryIndustryNode, 3));
        }
        for (JsonNode companyStatusNode : jsonNode.get("virksomhedsstatus")) {
            records.add(new CompanyStatusRecord(companyStatusNode));
        }
        for (JsonNode companyFormNode : jsonNode.get("virksomhedsform")) {
            records.add(new CompanyFormRecord(companyFormNode));
        }
        for (JsonNode yearlyNumbersNode : jsonNode.get("aarsbeskaeftigelse")) {
            records.add(new CompanyYearlyNumbersRecord(yearlyNumbersNode));
        }
        for (JsonNode quarterlyNumbersNode : jsonNode.get("kvartalsbeskaeftigelse")) {
            records.add(new CompanyQuarterlyNumbersRecord(quarterlyNumbersNode));
        }
        for (JsonNode monthlyNumbersNode : jsonNode.get("maanedsbeskaeftigelse")) {
            records.add(new CompanyMonthlyNumbersRecord(monthlyNumbersNode));
        }


        ListHashMap<OffsetDateTime, CompanyRecord> ajourRecords = new ListHashMap<>();
        TreeSet<OffsetDateTime> sortedTimestamps = new TreeSet<>();
        for (CompanyRecord record : records) {
            System.out.println("record: "+record);
            OffsetDateTime registrationFrom = record.getLastUpdated();
            ajourRecords.add(registrationFrom, record);
            sortedTimestamps.add(registrationFrom);
        }

        CompanyRegistration lastRegistration = null;
        for (OffsetDateTime registrationFrom : sortedTimestamps) {

            // Get any existing registration that matches this date, or create a new one
            CompanyRegistration registration = company.getRegistration(registrationFrom);
            if (registration == null) {
                registration = new CompanyRegistration();
                registration.setRegistrationFrom(registrationFrom);
                registration.setEntity(company);
            }

            // Copy data over from the previous registration, by cloning all effects and point underlying dataitems to the clones as well as the originals
            if (lastRegistration != null) {
                for (CompanyEffect originalEffect : lastRegistration.getEffects()) {
                    CompanyEffect newEffect = new CompanyEffect(registration, originalEffect.getEffectFrom(), originalEffect.getEffectTo());
                    for (CompanyBaseData originalData : originalEffect.getDataItems()) {
                        originalData.addEffect(newEffect);
                    }
                }
            }

            for (CompanyRecord record : ajourRecords.get(registrationFrom)) {
                //CompanyRegistration registration = registrations.computeIfAbsent(record.getLastUpdated(), k -> new CompanyRegistration());

                CompanyEffect effect = registration.getEffect(record.getValidFrom(), record.getValidTo());
                if (effect == null) {
                    System.out.println("did not find effect on "+record.getValidFrom()+", "+record.getValidTo()+", creating new");
                    effect = new CompanyEffect(registration, record.getValidFrom(), record.getValidTo());
                } else {
                    System.out.println("found effect on "+record.getValidFrom()+", "+record.getValidTo()+", reusing");
                }

                System.out.println("DATAITEMS COUNT: "+effect.getDataItems().size());

                //CompanyBaseData baseData = new CompanyBaseData();
                if (effect.getDataItems().isEmpty()) {
                    CompanyBaseData baseData = new CompanyBaseData();
                    baseData.addEffect(effect);
                }
                for (CompanyBaseData baseData : effect.getDataItems()) {
                    // There really should be only one item for each effect right now
                    record.populateCompanyBaseData(baseData, session);
                }
            }
            lastRegistration = registration;

            /*try {
                System.out.println(getObjectMapper().writeValueAsString(registration));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            System.out.println("==========================================================");*/
        }
        //System.out.println(company.getRegistrations());
        transaction.commit();
        session.close();
        return null;
    }


    private JsonNode unwrap(JsonNode jsonNode) {
        if (jsonNode.has("_source")) {
            jsonNode = jsonNode.get("_source");
        }
        if (jsonNode.has("Vrvirksomhed")) {
            jsonNode = jsonNode.get("Vrvirksomhed");
        }
        return jsonNode;
    }

    private abstract class CvrRecord {
        private OffsetDateTime lastUpdated = null;
        private LocalDate validFrom = null;
        private LocalDate validTo = null;
        private ObjectNode data;

        public CvrRecord(JsonNode node) {
            JsonNode lastUpdated = node.get("sidstOpdateret");
            if (!lastUpdated.isNull()) {
                this.lastUpdated = OffsetDateTime.parse(lastUpdated.asText(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            }

            JsonNode valid = node.get("periode");
            if (valid != null) {
                JsonNode validFrom = valid.get("gyldigFra");
                if (!validFrom.isNull()) {
                    this.validFrom = LocalDate.parse(validFrom.asText(), DateTimeFormatter.ISO_LOCAL_DATE);
                }

                JsonNode validTo = valid.get("gyldigTil");
                if (!validTo.isNull()) {
                    this.validTo = LocalDate.parse(validTo.asText(), DateTimeFormatter.ISO_LOCAL_DATE);
                }
            }

            this.data = (ObjectNode) node;
            this.data.remove("sidstOpdateret");
            this.data.remove("periode");
        }

        public OffsetDateTime getLastUpdated() {
            return this.lastUpdated;
        }

        public LocalDate getValidFrom() {
            return this.validFrom;
        }

        public LocalDate getValidTo() {
            return this.validTo;
        }

        public JsonNode getData() {
            return this.data;
        }
    }

    private abstract class CompanyRecord extends CvrRecord {

        public CompanyRecord(JsonNode node) {
            super(node);
        }

        public abstract void populateCompanyBaseData(CompanyBaseData baseData, Session session);
    }

    private class CompanyNameRecord extends CompanyRecord {

        public CompanyNameRecord(JsonNode node) {
            super(node);
        }

        public void populateCompanyBaseData(CompanyBaseData baseData, Session session) {
            baseData.setName(this.getData().get("navn").asText());
        }
    }

    private class CompanyLocationAddressRecord extends CompanyRecord {

        public CompanyLocationAddressRecord(JsonNode node) {
            super(node);
        }

        @Override
        public void populateCompanyBaseData(CompanyBaseData baseData, Session session) {
            try {
                Address address = getObjectMapper().treeToValue(this.getData(), Address.class);
                baseData.setLocationAddress(address);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }


    private class CompanyPostalAddressRecord extends CompanyRecord {

        public CompanyPostalAddressRecord(JsonNode node) {
            super(node);
        }

        @Override
        public void populateCompanyBaseData(CompanyBaseData baseData, Session session) {
            try {
                Address address = getObjectMapper().treeToValue(this.getData(), Address.class);
                baseData.setPostalAddress(address);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    private class CompanyPhoneRecord extends CompanyRecord {

        public CompanyPhoneRecord(JsonNode node) {
            super(node);
        }

        @Override
        public void populateCompanyBaseData(CompanyBaseData baseData, Session session) {
            baseData.setPhone(this.getData().get("kontaktoplysning").asText(), this.getData().get("hemmelig").asBoolean());
        }
    }

    private class CompanyFaxRecord extends CompanyRecord {

        public CompanyFaxRecord(JsonNode node) {
            super(node);
        }

        @Override
        public void populateCompanyBaseData(CompanyBaseData baseData, Session session) {
            baseData.setFax(this.getData().get("kontaktoplysning").asText(), this.getData().get("hemmelig").asBoolean());
        }
    }

    private class CompanyEmailRecord extends CompanyRecord {

        public CompanyEmailRecord(JsonNode node) {
            super(node);
        }

        @Override
        public void populateCompanyBaseData(CompanyBaseData baseData, Session session) {
            baseData.setEmail(this.getData().get("kontaktoplysning").asText(), this.getData().get("hemmelig").asBoolean());
        }
    }

    private class CompanyHomepageRecord extends CompanyRecord {

        public CompanyHomepageRecord(JsonNode node) {
            super(node);
        }

        @Override
        public void populateCompanyBaseData(CompanyBaseData baseData, Session session) {
            baseData.setHomepage(this.getData().get("kontaktoplysning").asText(), this.getData().get("hemmelig").asBoolean());
        }
    }

    private class CompanyMandatoryEmailRecord extends CompanyRecord {

        public CompanyMandatoryEmailRecord(JsonNode node) {
            super(node);
        }

        @Override
        public void populateCompanyBaseData(CompanyBaseData baseData, Session session) {
            baseData.setMandatoryEmail(this.getData().get("kontaktoplysning").asText(), this.getData().get("hemmelig").asBoolean());
        }
    }

    private class CompanyLifecycleRecord extends CompanyRecord {
        public CompanyLifecycleRecord(JsonNode node) {
            super(node);
        }

        @Override
        public void populateCompanyBaseData(CompanyBaseData baseData, Session session) {
            if (this.getValidFrom() != null) {
                baseData.setLifecycleStartDate(OffsetDateTime.of(this.getValidFrom(), LocalTime.MIDNIGHT, ZoneOffset.UTC));
            }
            if (this.getValidTo() != null) {
                baseData.setLifecycleEndDate(OffsetDateTime.of(this.getValidTo(), LocalTime.MIDNIGHT, ZoneOffset.UTC));
            }
        }
    }

    private class CompanyIndustryRecord extends CompanyRecord {
        int index;
        public CompanyIndustryRecord(JsonNode node, int index) {
            super(node);
            this.index = index;
        }

        @Override
        public void populateCompanyBaseData(CompanyBaseData baseData, Session session) {
            int code = this.getData().get("branchekode").asInt();
            String text = this.getData().get("branchetekst").asText();
            Industry industry = Industry.getIndustry(code, text, queryManager, session);
            switch (this.index) {
                case 0:
                    baseData.setPrimaryIndustry(industry);
                    break;
                case 1:
                    baseData.setSecondaryIndustry1(industry);
                    break;
                case 2:
                    baseData.setSecondaryIndustry2(industry);
                    break;
                case 3:
                    baseData.setSecondaryIndustry3(industry);
                    break;
            }

        }
    }

    private class CompanyStatusRecord extends CompanyRecord {

        public CompanyStatusRecord(JsonNode node) {
            super(node);
        }

        @Override
        public void populateCompanyBaseData(CompanyBaseData baseData, Session session) {
            String statusText = this.getData().get("status").asText();
            CompanyStatus status = CompanyStatus.getStatus(statusText, queryManager, session);
            baseData.setStatus(status);
        }
    }

    private class CompanyFormRecord extends CompanyRecord {

        public CompanyFormRecord(JsonNode node) {
            super(node);
        }

        @Override
        public void populateCompanyBaseData(CompanyBaseData baseData, Session session) {
            int code = this.getData().get("virksomhedsformkode").asInt();
            String shortDescription = this.getData().get("kortBeskrivelse").asText();
            String longDescription = this.getData().get("langBeskrivelse").asText();
            CompanyForm form = CompanyForm.getForm(code, shortDescription, longDescription, queryManager, session);
            baseData.setForm(form);
        }
    }

    private abstract class CompanyNumbersRecord extends CompanyRecord {

        public CompanyNumbersRecord(JsonNode node) {
            super(node);
        }

        Pattern rangePattern = Pattern.compile("^ANTAL_(\\d+)_(\\d+)$");
        protected Pair<Integer, Integer> parseRange(String key) {
            JsonNode node = this.getData().get(key);
            if (node != null) {
                String range = node.asText();
                if (range != null) {
                    Matcher m = rangePattern.matcher(range);
                    if (m.find()) {
                        return new Pair<>(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
                    }
                }
            }
            return new Pair<>(null, null);
        }
    }

    private class CompanyYearlyNumbersRecord extends CompanyNumbersRecord {

        public CompanyYearlyNumbersRecord(JsonNode node) {
            super(node);
        }

        @Override
        public void populateCompanyBaseData(CompanyBaseData baseData, Session session) {
            JsonNode data = this.getData();
            Pair<Integer, Integer> employeeNumbers = parseRange("intervalKodeAntalAnsatte");
            Pair<Integer, Integer> fulltimeEquivalent = parseRange("intervalKodeAntalAarsvaerk");
            Pair<Integer, Integer> includingOwners = parseRange("intervalKodeAntalInklusivEjere");
            baseData.addYearlyEmployeeNumbers(
                    data.get("aar").asInt(),
                    employeeNumbers.getKey(),
                    employeeNumbers.getValue(),
                    fulltimeEquivalent.getKey(),
                    fulltimeEquivalent.getValue(),
                    includingOwners.getKey(),
                    includingOwners.getValue()
            );
        }
    }


    private class CompanyQuarterlyNumbersRecord extends CompanyNumbersRecord {

        public CompanyQuarterlyNumbersRecord(JsonNode node) {
            super(node);
        }

        @Override
        public void populateCompanyBaseData(CompanyBaseData baseData, Session session) {
            JsonNode data = this.getData();
            Pair<Integer, Integer> employeeNumbers = parseRange("intervalKodeAntalAnsatte");
            Pair<Integer, Integer> fulltimeEquivalent = parseRange("intervalKodeAntalAarsvaerk");
            Pair<Integer, Integer> includingOwners = parseRange("intervalKodeAntalInklusivEjere");
            baseData.addQuarterlyEmployeeNumbers(
                    data.get("aar").asInt(),
                    data.get("kvartal").asInt(),
                    employeeNumbers.getKey(),
                    employeeNumbers.getValue(),
                    fulltimeEquivalent.getKey(),
                    fulltimeEquivalent.getValue(),
                    includingOwners.getKey(),
                    includingOwners.getValue()
            );
        }
    }

    private class CompanyMonthlyNumbersRecord extends CompanyNumbersRecord {

        public CompanyMonthlyNumbersRecord(JsonNode node) {
            super(node);
        }

        @Override
        public void populateCompanyBaseData(CompanyBaseData baseData, Session session) {
            JsonNode data = this.getData();
            Pair<Integer, Integer> employeeNumbers = parseRange("intervalKodeAntalAnsatte");
            Pair<Integer, Integer> fulltimeEquivalent = parseRange("intervalKodeAntalAarsvaerk");
            Pair<Integer, Integer> includingOwners = parseRange("intervalKodeAntalInklusivEjere");
            baseData.addMonthlyEmployeeNumbers(
                    data.get("aar").asInt(),
                    data.get("maaned").asInt(),
                    employeeNumbers.getKey(),
                    employeeNumbers.getValue(),
                    fulltimeEquivalent.getKey(),
                    fulltimeEquivalent.getValue(),
                    includingOwners.getKey(),
                    includingOwners.getValue()
            );
        }
    }

}
