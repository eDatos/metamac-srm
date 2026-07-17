package org.siemac.metamac.srm.web.category.widgets;

import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.constants.SrmConstants;

public class EditCategorySchemeOrderVisualisationWindow extends EditCategorySchemeVisualisationWindow {

    public EditCategorySchemeOrderVisualisationWindow(String title) {
        super(title);
    }

    @Override
    protected boolean canCodeBeEdited() {
        // The code of an order can be edited when the order is not the alphabetical one and the version is not the temporal one
        return categorySchemeVisualisationDto != null && categorySchemeVisualisationDto.getId() != null
                ? !isAlphabeticalOrderVisualisation() && !UrnUtils.isTemporalUrn(categorySchemeVisualisationDto.getCategoryScheme().getUrn())
                : true;
    }

    private boolean isAlphabeticalOrderVisualisation() {
        return SrmConstants.CATEGORY_SCHEME_ORDER_VISUALISATION_ALPHABETICAL_CODE.equals(categorySchemeVisualisationDto.getCode());
    }
}
