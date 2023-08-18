package org.siemac.metamac.srm.core.base.mapper;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.exception.MetamacException;

import com.arte.statistic.sdmx.srm.core.base.domain.AnnotationType;
import com.arte.statistic.sdmx.srm.core.common.domain.ExternalItem;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;

public interface BaseDto2DoMapper {

    public ExternalItem externalItemDtoStatisticalOperationsToExternalItemDo(ExternalItemDto source, ExternalItem target, String metadataName) throws MetamacException;

    public AnnotationType annotationTypeDtoToEntity(AnnotationTypeDto source) throws MetamacException;

}
