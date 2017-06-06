package dk.magenta.datafordeler.cvr.data.productionunit;

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
public class ProductionUnitEntityService extends FapiService<ProductionUnitEntity, ProductionUnitQuery> {

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String getServiceName() {
        return "productionunit";
    }

    @Override
    protected Class<ProductionUnitEntity> getEntityClass() {
        return ProductionUnitEntity.class;
    }

    @Override
    protected ProductionUnitQuery getEmptyQuery() {
        return new ProductionUnitQuery();
    }

}
