package org.siemac.metamac.srm.core.resource.element.mapper;

import org.joda.time.DateTime;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.srm.core.base.domain.ResourceElement;
import org.siemac.metamac.srm.core.base.dto.ResourceElementDto;
import org.siemac.metamac.srm.core.base.mapper.BaseDto2DoMapperImpl;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;

@org.springframework.stereotype.Component("resourceElementDto2DoMapper")
public class ResourceElementDto2DoMapperImpl extends BaseDto2DoMapperImpl implements ResourceElementDto2DoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.category.mapper.CategoriesDto2DoMapper dto2DoMapperSdmxSrm;

    @Override
    public void resourceElementDtoToDo(ResourceElementDto source, ResourceElement target) throws MetamacException {
        if (source == null) {
            return;
        }

        if (target.getId() == null) {
            target.setIdentifiableArtefact(new IdentifiableArtefact());
        }

        target.setShortName(dto2DoMapperSdmxSrm.internationalStringToEntity(source.getShortName(), target.getShortName(), ServiceExceptionParameters.RESOURCE_ELEMENT_SHORT_NAME));
        target.setComment(dto2DoMapperSdmxSrm.internationalStringToEntity(source.getComment(), target.getComment(), ServiceExceptionParameters.RESOURCE_ELEMENT_COMMENT));
        target.setValidFrom(CoreCommonUtil.transformDateToDateTime(source.getValidFrom()));
        target.setValidTo(CoreCommonUtil.transformDateToDateTime(source.getValidTo()));
        // note: replacedBy metadata is ignored, because it will be updated by replaceTo metadata

        target.setIdentifiableArtefact(dto2DoMapperSdmxSrm.identifiableArtefactDtoToEntity(source, target.getIdentifiableArtefact()));
        // Optimistic locking: Update "update date" attribute to force update to root entity, to increment "version" attribute
        target.setUpdateDate(new DateTime());

    }
}