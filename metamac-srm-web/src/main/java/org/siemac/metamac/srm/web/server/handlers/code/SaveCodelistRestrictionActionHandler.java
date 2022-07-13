package org.siemac.metamac.srm.web.server.handlers.code;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.facade.serviceapi.SrmCoreServiceFacade;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistRestrictionAction;
import org.siemac.metamac.srm.web.shared.code.SaveCodelistRestrictionResult;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.server.ServiceContextHolder;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.gwtplatform.dispatch.shared.ActionException;

@Component
public class SaveCodelistRestrictionActionHandler extends SecurityActionHandler<SaveCodelistRestrictionAction, SaveCodelistRestrictionResult> {

    @Autowired
    private SrmCoreServiceFacade srmCoreServiceFacade;

    public SaveCodelistRestrictionActionHandler() {
        super(SaveCodelistRestrictionAction.class);
    }

    @Override
    public SaveCodelistRestrictionResult executeSecurityAction(SaveCodelistRestrictionAction action) throws ActionException {
        try {
            CodelistVisualisationDto restrictionToSave = action.getCodelistRestrictionVisualisationDto();
            CodelistVisualisationDto restrictionSaved = null;

            if (restrictionToSave.getId() == null) {
                // Create
                RelatedResourceDto codelist = RelatedResourceUtils.createRelatedResourceDto(RelatedResourceTypeEnum.CODELIST, action.getCodelistUrn());
                restrictionToSave.setCodelist(codelist);
                restrictionSaved = srmCoreServiceFacade.createCodelistRestrictionVisualisation(ServiceContextHolder.getCurrentServiceContext(), restrictionToSave);
            } else {
                // Update
                restrictionSaved = srmCoreServiceFacade.updateCodelistRestrictionVisualisation(ServiceContextHolder.getCurrentServiceContext(), restrictionToSave);
            }
            return new SaveCodelistRestrictionResult(restrictionSaved);
        } catch (MetamacException e) {
            throw WebExceptionUtils.createMetamacWebException(e);
        }
    }
}
