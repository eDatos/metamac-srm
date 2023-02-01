package org.siemac.metamac.srm.core.master.mapper;

import org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapperImpl;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.siemac.metamac.srm.core.master.dto.EniSchemaCatalogDto;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;

@org.springframework.stereotype.Component("masterTablesDo2DtoMapper")
public class MasterTablesDo2DtoMapperImpl extends BaseDo2DtoMapperImpl implements MasterTablesDo2DtoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.concept.mapper.ConceptsDo2DtoMapper do2DtoMapperSdmxSrm;
    
    @Override
    public EniSchemaCatalogDto eniSchemaCatalogToDto(EniSchemaCatalog source) {
        if (source == null) {
            return null;
        }

        EniSchemaCatalogDto target = new EniSchemaCatalogDto();
        target.setId(source.getId());
        target.setUuid(source.getUuid());
        target.setVersion(source.getVersion());
        
        //not sdmx fields
        target.setIdentifier(source.getIdentifier());
        target.setDescription(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getDescription()));

        return target;
    }
}
