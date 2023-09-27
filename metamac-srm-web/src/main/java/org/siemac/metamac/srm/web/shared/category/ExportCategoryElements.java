package org.siemac.metamac.srm.web.shared.category;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class ExportCategoryElements {

    @In(1)
    List<String> urns;

    @Out(1)
    String       fileName;
}
