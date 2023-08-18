package org.siemac.metamac.srm.web.shared.category;

import java.util.Map;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class UpdateCodesCategoryElements {

    @In(1)
    String          categorySchemetUrn;

    @In(2)
    Map<Long, Long> categoryElementsIdByCodeId;
}
