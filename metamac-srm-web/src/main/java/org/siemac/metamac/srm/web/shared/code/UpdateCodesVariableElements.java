package org.siemac.metamac.srm.web.shared.code;

import java.util.Map;

import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class UpdateCodesVariableElements {

    @In(1)
    String              codelistUrn;

    @In(2)
    Map<Long, Long>     variableElementsIdByCodeId;

    @Out(1)
    MetamacWebException notificationException;
}
