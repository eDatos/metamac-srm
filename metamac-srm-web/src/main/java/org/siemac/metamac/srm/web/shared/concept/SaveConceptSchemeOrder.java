package org.siemac.metamac.srm.web.shared.concept;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeVisualisationDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveConceptSchemeOrder {

    @In(1)
    String                        conceptSchemeUrn;

    @In(2)
    ConceptSchemeVisualisationDto conceptSchemeOrderVisualisationDto;

    @Out(1)
    ConceptSchemeVisualisationDto conceptSchemeOrderSaved;
}
