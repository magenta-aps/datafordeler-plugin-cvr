package dk.magenta.datafordeler.cvr.data.participant;

import dk.magenta.datafordeler.core.exception.AccessDeniedException;
import dk.magenta.datafordeler.core.exception.AccessRequiredException;
import dk.magenta.datafordeler.core.fapi.FapiService;
import dk.magenta.datafordeler.core.plugin.Plugin;
import dk.magenta.datafordeler.core.user.DafoUserDetails;
import dk.magenta.datafordeler.cvr.CvrAccessChecker;
import dk.magenta.datafordeler.cvr.CvrPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by lars on 19-05-17.
 */
@Controller
@RequestMapping("/cvr/participant/1/rest")
public class ParticipantEntityService extends FapiService<ParticipantEntity, ParticipantQuery> {

    @Autowired
    private CvrPlugin cvrPlugin;

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String getServiceName() {
        return "participant";
    }

    @Override
    protected Class<ParticipantEntity> getEntityClass() {
        return ParticipantEntity.class;
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
    protected ParticipantQuery getEmptyQuery() {
        return new ParticipantQuery();
    }

}
