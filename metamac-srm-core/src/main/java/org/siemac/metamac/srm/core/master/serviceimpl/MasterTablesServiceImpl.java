package org.siemac.metamac.srm.core.master.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalogRepository;
import org.siemac.metamac.srm.core.master.dto.EniSchemaCatalogDto;
import org.siemac.metamac.srm.core.master.exception.EniSchemaCatalogNotFoundException;
import org.siemac.metamac.srm.core.master.mapper.MasterTablesDo2DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of MasterTablesService.
 */
@Service("masterTablesService")
public class MasterTablesServiceImpl extends MasterTablesServiceImplBase {
    
    @Autowired
    private EniSchemaCatalogRepository      eniSchemaCatalogRepository;
    
    @Autowired
    private MasterTablesDo2DtoMapper                           do2DtoMapper;
    
    public MasterTablesServiceImpl() {
        // without impl. For serialize option.
    }

    public List<EniSchemaCatalogDto> findAllEniSchemaCatalogs(ServiceContext ctx) throws MetamacException {

        List<EniSchemaCatalog> eniSchemaCatalogs = eniSchemaCatalogRepository.findAll();
        List<EniSchemaCatalogDto> eniSchemaCatalogDtoList = new ArrayList<>();
        for (EniSchemaCatalog item : eniSchemaCatalogs) {
            eniSchemaCatalogDtoList.add(do2DtoMapper.eniSchemaCatalogToDto(item));
        }
        return eniSchemaCatalogDtoList;

    }
   
    public EniSchemaCatalog findEniSchemaCatalogById(ServiceContext ctx, Long id) throws MetamacException {
        try {
            return eniSchemaCatalogRepository.findById(id);
        } catch (EniSchemaCatalogNotFoundException e) {
            throw new MetamacException(ServiceExceptionType.ENI_SCHEMA_CATALOGNOT_FOUND, id);
        }
    }
    
}
