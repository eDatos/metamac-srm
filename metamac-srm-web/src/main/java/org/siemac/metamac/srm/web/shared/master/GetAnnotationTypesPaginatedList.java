package org.siemac.metamac.srm.web.shared.master;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetAnnotationTypesPaginatedList {

    @In(1)
    int                     maxResults;
    @In(2)
    int                     firstResult;

    @Out(1)
    List<AnnotationTypeDto> dtos;

    @Out(2)
    Integer                 totalResults;
}
