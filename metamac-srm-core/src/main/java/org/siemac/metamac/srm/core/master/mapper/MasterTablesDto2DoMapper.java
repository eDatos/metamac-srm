package org.siemac.metamac.srm.core.master.mapper;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.siemac.metamac.srm.core.master.dto.EniSchemaCatalogDto;

import com.arte.statistic.sdmx.srm.core.base.domain.AnnotationType;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;

public interface MasterTablesDto2DoMapper {

    public EniSchemaCatalog eniSchemaCatalogDtoToEntity(EniSchemaCatalogDto eniSchemaCatalogDto) throws MetamacException;

    public AnnotationType annotationTypeDtoToEntity(AnnotationTypeDto source) throws MetamacException;

}
