package org.siemac.metamac.srm.web.shared.code;

import java.util.Map;

import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class UpdateCodesInRestrictionVisualisation {

    @In(1)
    String                   codelistRestrictionVisualisationUrn;

    @In(2)
    Map<String, Boolean>     restrictions;

    @Out(1)
    CodelistVisualisationDto codelistVisualisationDto;
}
