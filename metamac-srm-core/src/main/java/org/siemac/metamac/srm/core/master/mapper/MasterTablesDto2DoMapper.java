package org.siemac.metamac.srm.core.master.mapper;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.siemac.metamac.srm.core.master.dto.EniSchemaCatalogDto;


public interface MasterTablesDto2DoMapper {

    public EniSchemaCatalog eniSchemaCatalogDtoToEntity(EniSchemaCatalogDto eniSchemaCatalogDto) throws MetamacException ;

}
