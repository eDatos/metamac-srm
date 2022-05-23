package org.siemac.metamac.srm.core.master.mapper;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;


public interface MasterTablesDto2DoMapper {

    public EniSchemaCatalog eniSchemaCatalogDtoToEntity(Long id, ServiceContext ctx) throws MetamacException ;

}
