package org.siemac.metamac.srm.web.shared.master;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class SaveAnnotationType {

    @In(1)
    AnnotationTypeDto dto;

    @Out(1)
    AnnotationTypeDto outputDto;
}
