package org.siemac.metamac.srm.web.shared.category;

import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveCategoryElement {

    @In(1)
    CategoryElementDto categoryElementDto;

    @Out(1)
    CategoryElementDto savedCategoryElementDto;
}
