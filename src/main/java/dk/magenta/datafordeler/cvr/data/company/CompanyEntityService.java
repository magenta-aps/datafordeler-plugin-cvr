package dk.magenta.datafordeler.cvr.data.company;

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
@RequestMapping("/cvr/company/1/rest")
public class CompanyEntityService extends FapiService<CompanyEntity, CompanyQuery> {

    @Autowired
    private CvrPlugin cvrPlugin;

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public String getServiceName() {
        return "company";
    }

    @Override
    protected Class<CompanyEntity> getEntityClass() {
        return CompanyEntity.class;
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
    protected CompanyQuery getEmptyQuery() {
        return new CompanyQuery();
    }

}
