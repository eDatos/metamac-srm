package org.siemac.metamac.srm.web.shared.category;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeVisualisationDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveCategorySchemeOrder {

    @In(1)
    String                        categorySchemeUrn;

    @In(2)
    CategorySchemeVisualisationDto categorySchemeOrderVisualisationDto;

    @Out(1)
    CategorySchemeVisualisationDto categorySchemeOrderSaved;
}
