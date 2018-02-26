package dk.magenta.datafordeler.cvr.records;

import com.fasterxml.jackson.annotation.JsonProperty;
import dk.magenta.datafordeler.core.database.Identification;
import dk.magenta.datafordeler.core.database.QueryManager;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import org.hibernate.Session;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@MappedSuperclass
public abstract class CvrEntityRecord extends CvrBitemporalRecord {

    public abstract List<CvrRecord> getAll();

    public List<CvrRecord> getSince(OffsetDateTime time) {
        ArrayList<CvrRecord> newer = new ArrayList<>();
        for (CvrRecord record : this.getAll()) {
            if (record instanceof CvrBitemporalRecord) {
                OffsetDateTime recordTime = ((CvrBitemporalRecord) record).getLastUpdated();
                if (recordTime == null || time == null || !recordTime.isBefore(time)) {
                    newer.add(record);
                }
            }
        }
        return newer;
    }

    public abstract Map<String, Object> getIdentifyingFilter();

    public abstract UUID generateUUID();

    @Override
    public void save(Session session) {
        CvrEntityRecord existing = QueryManager.getItem(session, this.getClass(), this.getIdentifyingFilter());
        if (this.identification == null) {
            this.identification = QueryManager.getOrCreateIdentification(session, this.generateUUID(), CvrPlugin.getDomain());
        }
        if (existing != null && !existing.getId().equals(this.getId()) && existing instanceof CompanyRecord && this instanceof CompanyRecord) {
            CompanyRecord c = (CompanyRecord) existing;
            CompanyRecord t = (CompanyRecord) this;
            for (NameRecord nameRecord : t.getNames()) {
                c.addName(nameRecord);
            }
            for (NameRecord nameRecord : t.getSecondaryNames()) {
                c.addSecondaryName(nameRecord);
            }
            for (AddressRecord addressRecord : t.getLocationAddress()) {
                c.addLocationAddress(addressRecord);
            }
            for (AddressRecord addressRecord : t.getPostalAddress()) {
                c.addPostalAddress(addressRecord);
            }
            for (ContactRecord contactRecord : t.getPhoneNumber()) {
                c.addPhoneNumber(contactRecord);
            }
            for (ContactRecord contactRecord : t.getSecondaryPhoneNumber()) {
                c.addSecondaryPhoneNumber(contactRecord);
            }
            for (ContactRecord contactRecord : t.getFaxNumber()) {
                c.addFaxNumber(contactRecord);
            }
            for (ContactRecord contactRecord : t.getSecondaryFaxNumber()) {
                c.addSecondaryFaxNumber(contactRecord);
            }
            for (ContactRecord contactRecord : t.getEmailAddress()) {
                c.addEmailAddress(contactRecord);
            }
            for (ContactRecord contactRecord : t.getHomepage()) {
                c.addHomepage(contactRecord);
            }
            for (ContactRecord contactRecord : t.getMandatoryEmailAddress()) {
                c.addMandatoryEmailAddress(contactRecord);
            }
            for (LifecycleRecord lifecycleRecord : t.getLifecycle()) {
                c.addLifecycle(lifecycleRecord);
            }
            for (CompanyIndustryRecord companyIndustryRecord : t.getPrimaryIndustry()) {
                c.addPrimaryIndustry(companyIndustryRecord);
            }
            for (CompanyIndustryRecord companyIndustryRecord : t.getSecondaryIndustry1()) {
                c.addSecondaryIndustry1(companyIndustryRecord);
            }
            for (CompanyIndustryRecord companyIndustryRecord : t.getSecondaryIndustry2()) {
                c.addSecondaryIndustry2(companyIndustryRecord);
            }
            for (CompanyIndustryRecord companyIndustryRecord : t.getSecondaryIndustry3()) {
                c.addSecondaryIndustry3(companyIndustryRecord);
            }
            for (StatusRecord statusRecord : t.getStatus()) {
                c.addStatus(statusRecord);
            }
            for (CompanyStatusRecord statusRecord : t.getCompanyStatus()) {
                c.addCompanyStatus(statusRecord);
            }
            for (CompanyFormRecord formRecord : t.getCompanyForm()) {
                c.addCompanyForm(formRecord);
            }
            for (CompanyYearlyNumbersRecord yearlyNumbersRecord : t.getYearlyNumbers()) {
                c.addYearlyNumbers(yearlyNumbersRecord);
            }
            for (CompanyQuarterlyNumbersRecord quarterlyNumbersRecord : t.getQuarterlyNumbers()) {
                c.addQuarterlyNumbers(quarterlyNumbersRecord);
            }
            for (CompanyMonthlyNumbersRecord monthlyNumbersRecord : t.getMonthlyNumbers()) {
                c.addMonthlyNumbers(monthlyNumbersRecord);
            }
            for (AttributeRecord attributeRecord : t.getAttributes()) {
                c.addAttributes(attributeRecord);
            }
            for (CompanyUnitLinkRecord companyUnitLinkRecord : t.getProductionUnits()) {
                c.addProductionUnit(companyUnitLinkRecord);
            }
            for (CompanyParticipantRelationRecord participantRelationRecord : t.getParticipants()) {
                c.addParticipant(participantRelationRecord);
            }
            for (FusionSplitRecord fusionSplitRecord : t.getFusions()) {
                c.addFusion(fusionSplitRecord);
            }
            for (FusionSplitRecord fusionSplitRecord : t.getSplits()) {
                c.addSplit(fusionSplitRecord);
            }


            existing.save(session);
        } else {
            super.save(session);
            for (CvrRecord record : this.getAll()) {
                record.save(session);
            }
        }
        /*if (existing != null) {
            session.delete(existing);
        }*/
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    protected Identification identification;


    public static final String DB_FIELD_SAMT_ID = "samtId";
    public static final String IO_FIELD_SAMT_ID = "samtId";

    @Column(name = DB_FIELD_SAMT_ID)
    @JsonProperty(value = IO_FIELD_SAMT_ID)
    private long samtId;



    public static final String DB_FIELD_REGISTER_ERROR = "registerError";
    public static final String IO_FIELD_REGISTER_ERROR = "fejlRegistreret";

    @Column(name = DB_FIELD_REGISTER_ERROR)
    @JsonProperty(value = IO_FIELD_REGISTER_ERROR)
    private boolean registerError;



    public static final String DB_FIELD_DATA_ACCESS = "dataAccess";
    public static final String IO_FIELD_DATA_ACCESS = "dataAdgang";

    @Column(name = DB_FIELD_DATA_ACCESS)
    @JsonProperty(value = IO_FIELD_DATA_ACCESS)
    private long dataAccess;



    public static final String DB_FIELD_LOADING_ERROR = "loadingError";
    public static final String IO_FIELD_LOADING_ERROR = "fejlVedIndlaesning";

    @Column(name = DB_FIELD_LOADING_ERROR)
    @JsonProperty(value = IO_FIELD_LOADING_ERROR)
    private boolean loadingError;



    public static final String DB_FIELD_NEAREST_FUTURE_DATE = "nearestFutureDate";
    public static final String IO_FIELD_NEAREST_FUTURE_DATE = "naermesteFremtidigeDato";

    @Column(name = DB_FIELD_NEAREST_FUTURE_DATE)
    @JsonProperty(value = IO_FIELD_NEAREST_FUTURE_DATE)
    private LocalDate nearestFutureDate;

    public void setNearestFutureDate(LocalDate nearestFutureDate) {
        this.nearestFutureDate = nearestFutureDate;
    }

    public LocalDate getNearestFutureDate() {
        return this.nearestFutureDate;
    }



    public static final String DB_FIELD_ERRORDESCRIPTION = "errorDescription";
    public static final String IO_FIELD_ERRORDESCRIPTION = "fejlBeskrivelse";

    @Column(name = DB_FIELD_ERRORDESCRIPTION)
    @JsonProperty(value = IO_FIELD_ERRORDESCRIPTION)
    private String errorDescription;



    public static final String DB_FIELD_EFFECT_AGENT = "effectAgent";
    public static final String IO_FIELD_EFFECT_AGENT = "virkningsAktoer";

    @Column(name = DB_FIELD_EFFECT_AGENT)
    @JsonProperty(value = IO_FIELD_EFFECT_AGENT)
    private String effectAgent;

}
