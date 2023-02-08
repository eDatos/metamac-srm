package org.siemac.metamac.srm.core.master.mapper;

import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.siemac.metamac.srm.core.master.dto.EniSchemaCatalogDto;


public interface MasterTablesDo2DtoMapper {

    // Lists
    public EniSchemaCatalogDto eniSchemaCatalogToDto(EniSchemaCatalog sourceData);

}
