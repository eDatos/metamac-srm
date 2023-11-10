package org.siemac.metamac.srm.web.shared.code;

import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveVariableElement {

    @In(1)
    VariableElementDto  variableElementDto;

    @Out(1)
    VariableElementDto  savedVariableElementDto;

    @Out(2)
    MetamacWebException notificationException;
}
