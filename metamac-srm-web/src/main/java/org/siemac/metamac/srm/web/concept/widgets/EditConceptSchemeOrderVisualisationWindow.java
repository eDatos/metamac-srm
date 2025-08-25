package org.siemac.metamac.srm.web.concept.widgets;

import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.constants.SrmConstants;

public class EditConceptSchemeOrderVisualisationWindow extends EditConceptSchemeVisualisationWindow {

    public EditConceptSchemeOrderVisualisationWindow(String title) {
        super(title);
    }

    @Override
    protected boolean canCodeBeEdited() {
        // The code of an order can be edited when the order is not the alphabetical one and the version is not the temporal one
        return conceptSchemeVisualisationDto != null && conceptSchemeVisualisationDto.getId() != null
                ? !isAlphabeticalOrderVisualisation() && !UrnUtils.isTemporalUrn(conceptSchemeVisualisationDto.getConceptScheme().getUrn())
                : true;
    }

    private boolean isAlphabeticalOrderVisualisation() {
        return SrmConstants.CONCEPT_SCHEME_ORDER_VISUALISATION_ALPHABETICAL_CODE.equals(conceptSchemeVisualisationDto.getCode());
    }
}
