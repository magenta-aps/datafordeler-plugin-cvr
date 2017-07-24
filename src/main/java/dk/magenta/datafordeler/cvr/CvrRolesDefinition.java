package dk.magenta.datafordeler.cvr;

import dk.magenta.datafordeler.core.plugin.RolesDefinition;
import dk.magenta.datafordeler.core.role.ReadServiceRole;
import dk.magenta.datafordeler.core.role.ReadServiceRoleVersion;
import dk.magenta.datafordeler.core.role.SystemRole;
import java.util.Collections;
import java.util.List;

/**
 * Created by jubk on 01-07-2017.
 */
public class CvrRolesDefinition extends RolesDefinition {

  public static ReadServiceRole READ_CVR_ROLE = new ReadServiceRole(
      "Cvr",
      new ReadServiceRoleVersion(1.0f, "Gives access to Cvr related services")
  );

  @Override
  public List<SystemRole> getRoles() {
    return Collections.singletonList(READ_CVR_ROLE);
  }
}
