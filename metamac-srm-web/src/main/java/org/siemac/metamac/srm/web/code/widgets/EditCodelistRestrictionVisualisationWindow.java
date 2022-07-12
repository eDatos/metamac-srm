package org.siemac.metamac.srm.web.code.widgets;

import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.constants.SrmConstants;

public class EditCodelistRestrictionVisualisationWindow extends EditCodelistVisualisationWindow {

    public EditCodelistRestrictionVisualisationWindow(String title) {
        super(title);
    }

    @Override
    protected boolean canCodeBeEdited() {
        // The code of an restriction can be edited when the restriction  is not the all expanded one and the version is not temporal one
        return codelistVisualisationDto != null && codelistVisualisationDto.getId() != null ? !isAllExpandedRestrictionVisualisation()
                && !UrnUtils.isTemporalUrn(codelistVisualisationDto.getCodelist().getUrn()) : true;
    }

    private boolean isAllExpandedRestrictionVisualisation() {
        return SrmConstants.CODELIST_RESTRICTION_VISUALISATION_ALL_EXPANDED_CODE.equals(codelistVisualisationDto.getCode());
    }
}
