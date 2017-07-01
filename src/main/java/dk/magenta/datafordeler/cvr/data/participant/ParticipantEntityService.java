package dk.magenta.datafordeler.cvr.data.participant;

import dk.magenta.datafordeler.core.exception.AccessDeniedException;
import dk.magenta.datafordeler.core.exception.AccessRequiredException;
import dk.magenta.datafordeler.core.fapi.FapiService;
import dk.magenta.datafordeler.core.user.DafoUserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jws.WebService;
import javax.ws.rs.Path;

/**
 * Created by lars on 19-05-17.
 */
@Controller
@RequestMapping("/cvr/participant/1/rest")
public class ParticipantEntityService extends FapiService<ParticipantEntity, ParticipantQuery> {

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
    protected void checkAccess(DafoUserDetails dafoUserDetails) throws AccessDeniedException, AccessRequiredException {

    }

    @Override
    protected ParticipantQuery getEmptyQuery() {
        return new ParticipantQuery();
    }

}
