package org.siemac.metamac.srm.core.concept.mapper;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeOrderVisualisation;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeVisualisationDto;

public interface ConceptsDto2DoMapper {

    public ConceptSchemeVersionMetamac conceptSchemeDtoToDo(ConceptSchemeMetamacDto source) throws MetamacException;
    public ConceptMetamac conceptDtoToDo(ConceptMetamacDto source) throws MetamacException;

    // visualisation methods
    public ConceptSchemeOrderVisualisation conceptSchemeOrderVisualisationDtoToDo(ConceptSchemeVisualisationDto source) throws MetamacException;
}