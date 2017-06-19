package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.core.exception.AccessDeniedException;
import dk.magenta.datafordeler.core.exception.AccessRequiredException;
import dk.magenta.datafordeler.core.fapi.FapiService;
import dk.magenta.datafordeler.core.user.DafoUserDetails;
import org.springframework.stereotype.Component;

import javax.jws.WebService;
import javax.ws.rs.Path;

/**
 * Created by lars on 19-05-17.
 */
@Path("")
@Component
@WebService
public class CompanyUnitEntityService extends FapiService<CompanyUnitEntity, CompanyUnitQuery> {

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String getServiceName() {
        return "companyunit";
    }

    @Override
    protected Class<CompanyUnitEntity> getEntityClass() {
        return CompanyUnitEntity.class;
    }

    @Override
    protected void checkAccess(DafoUserDetails dafoUserDetails) throws AccessDeniedException, AccessRequiredException {

    }

    @Override
    protected CompanyUnitQuery getEmptyQuery() {
        return new CompanyUnitQuery();
    }

}
