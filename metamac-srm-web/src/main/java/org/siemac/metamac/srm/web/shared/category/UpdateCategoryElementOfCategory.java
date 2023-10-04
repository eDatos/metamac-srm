package org.siemac.metamac.srm.web.shared.category;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class UpdateCategoryElementOfCategory {

    @In(1)
    String             categoryUrn;

    @In(2)
    String             categoryElementUrn;

    @Out(1)
    CategoryMetamacDto categoryMetamacDto;
}
