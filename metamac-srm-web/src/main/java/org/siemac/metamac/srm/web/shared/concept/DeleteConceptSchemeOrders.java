package org.siemac.metamac.srm.web.shared.concept;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class DeleteConceptSchemeOrders {

    @In(1)
    List<String> orderUrns;
}
