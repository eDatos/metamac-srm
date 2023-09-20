package org.siemac.metamac.srm.core.resource.element.mapper;

import org.siemac.metamac.srm.core.base.domain.ResourceElement;
import org.siemac.metamac.srm.core.base.dto.ResourceElementDto;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public interface ResourceElementDo2DtoMapper {

    public void resourceElementDoToDto(ResourceElement source, ResourceElementDto target);
    public void resourceElementDoToRelatedResourceDto(ResourceElement source, RelatedResourceDto target);
}