package org.siemac.metamac.srm.core.code.invocation.service;

import java.io.Serializable;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;

public interface NoticesRestInternalService {

    public static final String BEAN_ID = "noticesRestInternalService";

    // Background Notifications
    public void createErrorBackgroundNotification(String user, String actionCode, MetamacException exception);

    public void createSuccessBackgroundNotification(String user, String actionCode, String successMessageCode, Serializable... successMessageParameters);

    public void createSuccessBackgroundNotification(String user, String actionCode, String successMessageCode, MetamacException exception, Serializable... successMessageParameters);

    public void createVariableElementStreamMessageErrorNotification(ServiceContext serviceContext, String user, List<VariableElement> variableElements, TypeExternalArtefactsEnum resourceType)
            throws MetamacException;

    public void createDsdStreamMessageErrorNotification(ServiceContext serviceContext, String sourceResourceUrn, List<DataStructureDefinitionVersionMetamac> failedDsds) throws MetamacException;

}
