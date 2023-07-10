package org.siemac.metamac.srm.web.shared.category;

import java.util.List;

import org.siemac.metamac.srm.core.category.domain.shared.CodeCategoryElementNormalisationResult;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class NormaliseCategoryElementsToCategories {

    @In(1)
    String                                       categorySchemeUrn;

    @In(2)
    String                                       locale;

    @In(3)
    Boolean                                      onlyNormaliseCategoriesWithoutCategoryElement;

    @Out(1)
    List<CodeCategoryElementNormalisationResult> codeCategoryElementNormalisationResults;
}
