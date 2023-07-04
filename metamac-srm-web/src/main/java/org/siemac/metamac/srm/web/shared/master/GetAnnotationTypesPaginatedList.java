package org.siemac.metamac.srm.web.shared.master;

import java.util.List;

import org.siemac.metamac.srm.web.shared.criteria.AnnotationTypeCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetAnnotationTypesPaginatedList {

    @In(1)
    AnnotationTypeCriteria  criteria;

    @Out(1)
    List<AnnotationTypeDto> dtos;

    @Out(2)
    Integer                 totalResults;
}
