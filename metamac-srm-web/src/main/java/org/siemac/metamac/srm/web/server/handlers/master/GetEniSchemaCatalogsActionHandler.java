package org.siemac.metamac.srm.web.server.handlers.master;

import java.util.List;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.master.dto.EniSchemaCatalogDto;
import org.siemac.metamac.srm.core.master.serviceapi.MasterTablesService;
import org.siemac.metamac.srm.web.shared.master.GetEniSchemaCatalogsAction;
import org.siemac.metamac.srm.web.shared.master.GetEniSchemaCatalogsResult;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class  GetEniSchemaCatalogsActionHandler extends SecurityActionHandler< GetEniSchemaCatalogsAction,  GetEniSchemaCatalogsResult> {

    @Autowired
    private MasterTablesService masterService;

    public GetEniSchemaCatalogsActionHandler() {
        super(GetEniSchemaCatalogsAction.class);
    }

    @Override
    public GetEniSchemaCatalogsResult executeSecurityAction(GetEniSchemaCatalogsAction action) throws ActionException {
        try {
            List<EniSchemaCatalogDto> eniSchemas = masterService.findAllEniSchemaCatalogs(ServiceContextHolder.getCurrentServiceContext());
            return new GetEniSchemaCatalogsResult(eniSchemas);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }

}
