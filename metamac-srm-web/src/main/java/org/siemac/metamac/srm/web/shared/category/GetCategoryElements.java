package org.siemac.metamac.srm.web.shared.category;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;
import org.siemac.metamac.srm.web.shared.criteria.CategoryElementWebCriteria;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetCategoryElements {

    @In(1)
    int                        firstResult;

    @In(2)
    int                        maxResults;

    @In(3)
    CategoryElementWebCriteria criteria;

    @Out(1)
    List<CategoryElementDto>   categoryElements;

    @Out(2)
    Integer                    firstResultOut;

    @Out(3)
    Integer                    totalResults;
}
