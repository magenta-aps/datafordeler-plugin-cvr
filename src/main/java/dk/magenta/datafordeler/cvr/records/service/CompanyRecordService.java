package dk.magenta.datafordeler.cvr.records.service;

import dk.magenta.datafordeler.core.MonitorService;
import dk.magenta.datafordeler.core.arearestriction.AreaRestriction;
import dk.magenta.datafordeler.core.arearestriction.AreaRestrictionType;
import dk.magenta.datafordeler.core.exception.AccessDeniedException;
import dk.magenta.datafordeler.core.exception.AccessRequiredException;
import dk.magenta.datafordeler.core.exception.HttpNotFoundException;
import dk.magenta.datafordeler.core.exception.InvalidClientInputException;
import dk.magenta.datafordeler.core.fapi.FapiBaseService;
import dk.magenta.datafordeler.core.plugin.AreaRestrictionDefinition;
import dk.magenta.datafordeler.core.plugin.Plugin;
import dk.magenta.datafordeler.core.user.DafoUserDetails;
import dk.magenta.datafordeler.cvr.CvrAccessChecker;
import dk.magenta.datafordeler.cvr.CvrAreaRestrictionDefinition;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import dk.magenta.datafordeler.cvr.CvrRolesDefinition;
import dk.magenta.datafordeler.cvr.data.company.CompanyRecordQuery;
import dk.magenta.datafordeler.cvr.records.CompanyRecord;
import dk.magenta.datafordeler.cvr.records.output.CompanyRecordOutputWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Stream;

@RestController
@RequestMapping("/cvr/company/1/rest")
public class CompanyRecordService extends FapiBaseService<CompanyRecord, CompanyRecordQuery> {

    @Autowired
    private CvrPlugin cvrPlugin;

    @Autowired
    private CompanyRecordOutputWrapper companyRecordOutputWrapper;

    @Autowired
    private MonitorService monitorService;

    public CompanyRecordService() {
        super();
    }

    @PostConstruct
    public void init() {
        this.setOutputWrapper(this.companyRecordOutputWrapper);
        this.monitorService.addAccessCheckPoint("/cvr/company/1/rest/1234");
        this.monitorService.addAccessCheckPoint("/cvr/company/1/rest/search?cvrnummer=1234");
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String getServiceName() {
        return "company";
    }

    public static String getDomain() {
        return "https://data.gl/cvr/company/1/rest/";
    }

    @Override
    protected Class<CompanyRecord> getEntityClass() {
        return CompanyRecord.class;
    }

    @Override
    public Plugin getPlugin() {
        return this.cvrPlugin;
    }

    @Override
    protected void checkAccess(DafoUserDetails dafoUserDetails) throws AccessDeniedException, AccessRequiredException {
        CvrAccessChecker.checkAccess(dafoUserDetails);
    }

    @Override
    protected void sendAsCSV(Stream<CompanyRecord> entities, HttpServletRequest request, HttpServletResponse response) throws IOException, HttpNotFoundException {
        /*List<MediaType> acceptedTypes = MediaType.parseMediaTypes
                (request.getHeader("Accept"));

        Iterator<Map<String, Object>> dataIter =
                entities.map(Entity::getRegistrations).flatMap(
                        List::stream
                ).flatMap(
                        r -> ((Registration) r).getEffects().stream()
                ).map(
                        obj -> {
                            Effect e = (Effect) obj;
                            Registration r = e.getRegistration();
                            Map<String, Object> data = e.getData();

                            data.put("effectFrom",
                                    e.getEffectFrom());
                            data.put("effectTo",
                                    e.getEffectTo());
                            data.put("registrationFrom",
                                    r.getRegistrationFrom());
                            data.put("registrationTo",
                                    r.getRegistrationFrom());
                            data.put("sequenceNumber",
                                    r.getSequenceNumber());
                            data.put("uuid", r.getEntity().getUUID());

                            return data;
                        }
                ).iterator();

        if (!dataIter.hasNext()) {
            response.sendError(HttpStatus.NO_CONTENT.value());
            return;
        }

        CsvSchema.Builder builder =
                new CsvSchema.Builder();

        Map<String, Object> first = dataIter.next();
        ArrayList<String> keys =
                new ArrayList<>(first.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            builder.addColumn(new CsvSchema.Column(
                    i, keys.get(i),
                    CsvSchema.ColumnType.NUMBER_OR_STRING
            ));
        }

        CsvSchema schema =
                builder.build().withHeader();

        if (acceptedTypes.contains(new MediaType("text", "tsv"))) {
            schema = schema.withColumnSeparator('\t');
            response.setContentType("text/tsv");
        } else {
            response.setContentType("text/csv");
        }

        SequenceWriter writer =
                csvMapper.writer(schema).writeValues(response.getOutputStream());

        writer.write(first);

        while (dataIter.hasNext()) {
            writer.write(dataIter.next());
        }*/
    }

    @Override
    protected CompanyRecordQuery getEmptyQuery() {
        return new CompanyRecordQuery();
    }

    protected void applyAreaRestrictionsToQuery(CompanyRecordQuery query, DafoUserDetails user) throws InvalidClientInputException {
        Collection<AreaRestriction> restrictions = user.getAreaRestrictionsForRole(CvrRolesDefinition.READ_CVR_ROLE);
        AreaRestrictionDefinition areaRestrictionDefinition = this.cvrPlugin.getAreaRestrictionDefinition();
        AreaRestrictionType municipalityType = areaRestrictionDefinition.getAreaRestrictionTypeByName(CvrAreaRestrictionDefinition.RESTRICTIONTYPE_KOMMUNEKODER);
        for (AreaRestriction restriction : restrictions) {
            if (restriction.getType() == municipalityType) {
                query.addKommunekodeRestriction(restriction.getValue());
            }
        }
    }
}
