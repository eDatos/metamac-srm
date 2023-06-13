package org.siemac.metamac.srm.core.master.mapper;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.mapper.BaseDto2DoMapperImpl;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalogRepository;
import org.siemac.metamac.srm.core.master.dto.EniSchemaCatalogDto;
import org.siemac.metamac.srm.core.master.exception.EniSchemaCatalogNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.AnnotationType;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;

@org.springframework.stereotype.Component("masterTablesDto2DoMapper")
public class MasterTablesDto2DoMapperImpl extends BaseDto2DoMapperImpl implements MasterTablesDto2DoMapper {

    @Autowired
    private EniSchemaCatalogRepository eniSchemaCatalogRepository;

    @Override
    public EniSchemaCatalog eniSchemaCatalogDtoToEntity(EniSchemaCatalogDto eniSchemaCatalogDto) throws MetamacException {
        if (eniSchemaCatalogDto.getId() == null) {
            return null;
        }

        try {
            return eniSchemaCatalogRepository.findById(eniSchemaCatalogDto.getId());
        } catch (EniSchemaCatalogNotFoundException e) {
            throw new MetamacException(ServiceExceptionType.ENI_SCHEMA_CATALOG_NOT_FOUND, eniSchemaCatalogDto.getId());
        }
    }

    @Override
    public AnnotationType annotationTypeDtoToEntity(AnnotationTypeDto source) throws MetamacException {
        return super.annotationTypeDtoToEntity(source);
    }
}
