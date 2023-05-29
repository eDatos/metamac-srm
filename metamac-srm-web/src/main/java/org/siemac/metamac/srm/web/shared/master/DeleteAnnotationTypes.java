package org.siemac.metamac.srm.web.shared.master;

import java.util.List;

import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;

@GenDispatch(isSecure = false)
public class DeleteAnnotationTypes {

    @In(1)
    List<String> uuids;

}
