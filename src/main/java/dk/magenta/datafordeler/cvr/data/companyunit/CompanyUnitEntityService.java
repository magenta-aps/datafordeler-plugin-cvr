package dk.magenta.datafordeler.cvr.data.companyunit;

import dk.magenta.datafordeler.core.fapi.FapiService;
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
        return "company";
    }

    @Override
    protected Class<CompanyUnitEntity> getEntityClass() {
        return CompanyUnitEntity.class;
    }

    @Override
    protected CompanyUnitQuery getEmptyQuery() {
        return new CompanyUnitQuery();
    }

}
