package org.siemac.metamac.srm.core.security;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.security.shared.SharedAdministrationSecurityUtils;

public class AdministrationSecurityUtils extends CommonSecurityUtils {

    //
    // ADMINISTRATION
    //

    public static void canRetrieveAnnotationType(ServiceContext ctx) throws MetamacException {
        if (!SharedAdministrationSecurityUtils.canRetrieveAnnotationType(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canCreateAnnotationType(ServiceContext ctx) throws MetamacException {
        if (!SharedAdministrationSecurityUtils.canCreateAnnotationType(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canUpdateAnnotationType(ServiceContext ctx) throws MetamacException {
        if (!SharedAdministrationSecurityUtils.canUpdateAnnotationType(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

    public static void canDeleteAnnotationType(ServiceContext ctx) throws MetamacException {
        if (!SharedAdministrationSecurityUtils.canDeleteAnnotationType(getMetamacPrincipal(ctx))) {
            throwExceptionIfOperationNotAllowed(ctx);
        }
    }

}
