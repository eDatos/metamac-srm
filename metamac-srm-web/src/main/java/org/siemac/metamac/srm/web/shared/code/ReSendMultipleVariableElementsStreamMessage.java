package org.siemac.metamac.srm.web.shared.code;

import java.util.List;

import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class ReSendMultipleVariableElementsStreamMessage {

    @In(1)
    List<String>        urns;

    @In(2)
    String              variable;

    @In(3)
    String              locale;

    @Out(1)
    MetamacWebException notificationException;

    @Out(2)
    Boolean             taskInBackground;
}
