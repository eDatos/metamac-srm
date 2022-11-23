package org.siemac.metamac.srm.web.shared.dsd;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class UpdateAttributeInOrder {

    @In(1)
    String  attributeUrn;

    @In(2)
    String  attributeSchemeUrn;

    @In(3)
    Integer newAttributeIndex;
}
