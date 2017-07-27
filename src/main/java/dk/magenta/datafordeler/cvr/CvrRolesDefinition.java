package dk.magenta.datafordeler.cvr;

import dk.magenta.datafordeler.core.plugin.RolesDefinition;
import dk.magenta.datafordeler.core.role.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jubk on 01-07-2017.
 */
public class CvrRolesDefinition extends RolesDefinition {

    public static ReadServiceRole READ_CVR_ROLE = new ReadServiceRole(
        "Cvr",
        new ReadServiceRoleVersion(1.0f, "Gives access to Cvr related services")
    );

    public static ExecuteCommandRole EXECUTE_CVR_PULL_ROLE = new ExecuteCommandRole(
        "pull",
        new HashMap<String, Object>() {{
        put("plugin", "cvr");
        }},
        new ExecuteCommandRoleVersion(
            1.0f,
            "Role that gives access to start and stop the PULL command for Cvr data"
        )
    );

    @Override
    public List<SystemRole> getRoles() {
        ArrayList<SystemRole> roles = new ArrayList<>();

        roles.add(READ_CVR_ROLE);
        roles.add(EXECUTE_CVR_PULL_ROLE);

        return roles;
    }
}
