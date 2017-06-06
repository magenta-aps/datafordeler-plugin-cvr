package dk.magenta.datafordeler.cvr.data.industry;

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
public class IndustryEntityService extends FapiService<IndustryEntity, IndustryQuery> {

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String getServiceName() {
        return "industry";
    }

    @Override
    protected Class<IndustryEntity> getEntityClass() {
        return IndustryEntity.class;
    }

    @Override
    protected IndustryQuery getEmptyQuery() {
        return new IndustryQuery();
    }

}
