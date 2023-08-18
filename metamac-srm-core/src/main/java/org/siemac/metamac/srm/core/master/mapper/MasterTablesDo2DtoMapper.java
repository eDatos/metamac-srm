package org.siemac.metamac.srm.core.master.mapper;

import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.siemac.metamac.srm.core.master.dto.EniSchemaCatalogDto;

import com.arte.statistic.sdmx.srm.core.base.domain.AnnotationType;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;

public interface MasterTablesDo2DtoMapper {

    // Lists
    public EniSchemaCatalogDto eniSchemaCatalogToDto(EniSchemaCatalog sourceData);

    public AnnotationTypeDto annotationTypeToDto(AnnotationType source);

}
