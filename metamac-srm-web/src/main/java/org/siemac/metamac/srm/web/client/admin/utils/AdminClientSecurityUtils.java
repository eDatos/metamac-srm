package org.siemac.metamac.srm.web.client.admin.utils;

import org.siemac.metamac.srm.core.security.shared.SharedAdministrationSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

public class AdminClientSecurityUtils {

    // ADMINISTRATION

    public static boolean canCreateAnnotationType() {
        return SharedAdministrationSecurityUtils.canCreateAnnotationType(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateAnnotationType() {
        return SharedAdministrationSecurityUtils.canUpdateAnnotationType(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canDeleteAnnotationType() {
        return SharedAdministrationSecurityUtils.canDeleteAnnotationType(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canAdministrate() {
        return SharedAdministrationSecurityUtils.canAdministrate(MetamacSrmWeb.getCurrentUser());
    }
}
