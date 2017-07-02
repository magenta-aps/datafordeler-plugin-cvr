package dk.magenta.datafordeler.cvr;

import dk.magenta.datafordeler.core.user.DafoUserDetails;

/**
 * Created by jubk on 01-07-2017.
 */
public class CvrAccessChecker {

  public static void checkAccess(DafoUserDetails dafoUserDetails) {
    dafoUserDetails.checkHasSystemRole(CvrRolesDefinition.READ_CVR_ROLE);
  }

}
