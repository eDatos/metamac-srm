package org.siemac.metamac.srm.web.client.admin.utils;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.security.shared.SharedCategoriesSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

public class AdminClientSecurityUtils {

    // CATEGORY SCHEMES

    // TODO EDATOS-4082 PERMISOS PARA ESTOS PANELES

    public static boolean canCreateAnnotationType() {
        return SharedCategoriesSecurityUtils.canCreateCategoryScheme(MetamacSrmWeb.getCurrentUser());
    }

    public static boolean canUpdateAnnotationType() {
        return SharedCategoriesSecurityUtils.canUpdateCategoryScheme(MetamacSrmWeb.getCurrentUser(), ProcStatusEnum.EXTERNALLY_PUBLISHED);
    }

    public static boolean canDeleteAnnotationType() {
        return SharedCategoriesSecurityUtils.canDeleteCategoryScheme(MetamacSrmWeb.getCurrentUser());
    }
}
