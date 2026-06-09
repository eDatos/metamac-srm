package org.siemac.metamac.srm.web.shared.category;

import java.util.List;

import org.siemac.metamac.srm.core.category.domain.shared.CategoryMetamacVisualisationResult;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeVisualisationDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetCategoriesByScheme {

    @In(1)
    String                                   schemeUrn;

    @In(2)
    String                                   locale;

    @In(3)
    String                                   categorySchemeOrderUrn;

    @Out(1)
    List<CategoryMetamacVisualisationResult> categories;

    @Out(2)
    CategorySchemeVisualisationDto           categorySchemeOrderVisualisationDto;
}
