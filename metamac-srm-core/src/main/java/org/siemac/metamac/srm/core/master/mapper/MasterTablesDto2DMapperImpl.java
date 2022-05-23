package org.siemac.metamac.srm.core.master.mapper;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.mapper.BaseDto2DoMapperImpl;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.siemac.metamac.srm.core.master.serviceapi.MasterTablesService;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component("masterTablesDto2DoMapper")
public class MasterTablesDto2DMapperImpl extends BaseDto2DoMapperImpl implements MasterTablesDto2DoMapper {
    
    @Autowired
    private MasterTablesService masterService;
    
    @Override
    public EniSchemaCatalog eniSchemaCatalogDtoToEntity(Long id, ServiceContext ctx) throws MetamacException {
        if (id == null) {
            return null;
        }

        return masterService.findEniSchemaCatalogById(ctx, id);
        
    }
}
