package org.siemac.metamac.srm.web.shared.category;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class UpdateCategoryInOrder {

    @In(1)
    String  categoryUrn;

    @In(2)
    String  categorySchemeUrn;

    @In(3)
    Integer newCategoryIndex;
}
