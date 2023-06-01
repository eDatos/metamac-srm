package org.siemac.metamac.srm.core.security.shared;

import org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum;
import org.siemac.metamac.sso.client.MetamacPrincipal;

public class SharedAdministrationSecurityUtils extends SharedItemsSecurityUtils {

    //
    // ADMINISTRATION
    //

    public static boolean canRetrieveAnnotationType(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, SrmRoleEnum.ADMINISTRADOR);
    }

    public static boolean canCreateAnnotationType(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, SrmRoleEnum.ADMINISTRADOR);
    }

    public static boolean canUpdateAnnotationType(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, SrmRoleEnum.ADMINISTRADOR);
    }

    public static boolean canDeleteAnnotationType(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, SrmRoleEnum.ADMINISTRADOR);
    }
}