package dk.magenta.datafordeler.cvr.service;

import dk.magenta.datafordeler.core.MonitorService;
import dk.magenta.datafordeler.core.arearestriction.AreaRestriction;
import dk.magenta.datafordeler.core.arearestriction.AreaRestrictionType;
import dk.magenta.datafordeler.core.exception.*;
import dk.magenta.datafordeler.core.fapi.FapiBaseService;
import dk.magenta.datafordeler.core.plugin.AreaRestrictionDefinition;
import dk.magenta.datafordeler.core.plugin.Plugin;
import dk.magenta.datafordeler.core.user.DafoUserDetails;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import dk.magenta.datafordeler.cvr.DirectLookup;
import dk.magenta.datafordeler.cvr.access.CvrAccessChecker;
import dk.magenta.datafordeler.cvr.access.CvrAreaRestrictionDefinition;
import dk.magenta.datafordeler.cvr.access.CvrRolesDefinition;
import dk.magenta.datafordeler.cvr.output.ParticipantRecordOutputWrapper;
import dk.magenta.datafordeler.cvr.query.ParticipantRecordQuery;
import dk.magenta.datafordeler.cvr.records.ParticipantRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/cvr/participant/1/rest")
public class ParticipantRecordService extends FapiBaseService<ParticipantRecord, ParticipantRecordQuery> {

    @Autowired
    private CvrPlugin cvrPlugin;

    private Logger log = LogManager.getLogger(CompanyRecordService.class.getCanonicalName());

    @Autowired
    private ParticipantRecordOutputWrapper participantRecordOutputWrapper;

    @Autowired
    private MonitorService monitorService;

    public ParticipantRecordService() {
        super();
    }

    @PostConstruct
    public void init() {
        this.setOutputWrapper(this.participantRecordOutputWrapper);
        this.monitorService.addAccessCheckPoint("/cvr/participant/1/rest/1234");
        this.monitorService.addAccessCheckPoint("/cvr/participant/1/rest/search?deltagernummer=1234");
    }

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String getServiceName() {
        return "participant";
    }

    public static String getDomain() {
        return "https://data.gl/cvr/participant/1/rest/";
    }

    @Override
    protected Class<ParticipantRecord> getEntityClass() {
        return ParticipantRecord.class;
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
    protected void sendAsCSV(Stream<ParticipantRecord> entities, HttpServletRequest request, HttpServletResponse response) throws IOException, HttpNotFoundException {
    }

    @Override
    protected ParticipantRecordQuery getEmptyQuery() {
        return new ParticipantRecordQuery();
    }

    protected void applyAreaRestrictionsToQuery(ParticipantRecordQuery query, DafoUserDetails user) throws InvalidClientInputException {
        Collection<AreaRestriction> restrictions = user.getAreaRestrictionsForRole(CvrRolesDefinition.READ_CVR_ROLE);
        AreaRestrictionDefinition areaRestrictionDefinition = this.cvrPlugin.getAreaRestrictionDefinition();
        AreaRestrictionType municipalityType = areaRestrictionDefinition.getAreaRestrictionTypeByName(CvrAreaRestrictionDefinition.RESTRICTIONTYPE_KOMMUNEKODER);
        for (AreaRestriction restriction : restrictions) {
            if (restriction.getType() == municipalityType) {
                query.addKommunekodeRestriction(restriction.getValue());
            }
        }
    }

    @Override
    public List<ParticipantRecord> searchByQuery(ParticipantRecordQuery query, Session session) {
        List<ParticipantRecord> allRecords = new ArrayList<>();

        List<ParticipantRecord> localResults = super.searchByQuery(query, session);
        if (!localResults.isEmpty()) {
            log.info("There are "+localResults.size()+" local results");
            allRecords.addAll(localResults);
        }

        HashSet<String> eNumbers = new HashSet<>(query.getEnhedsNummer());
        if (!eNumbers.isEmpty()) {
            for (ParticipantRecord record : localResults) {
                eNumbers.remove(record.getUnitNumber());
            }
            query.setEnhedsNummer(eNumbers);
        }

        return allRecords;
    }
}
