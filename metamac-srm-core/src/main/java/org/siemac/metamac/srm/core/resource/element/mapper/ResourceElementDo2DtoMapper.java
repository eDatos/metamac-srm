package org.siemac.metamac.srm.core.resource.element.mapper;

import org.siemac.metamac.srm.core.base.domain.ResourceElement;
import org.siemac.metamac.srm.core.base.dto.ResourceElementDto;

public interface ResourceElementDo2DtoMapper {

    // Category element
    public void resourceElementDoToDto(ResourceElement source, ResourceElementDto target);
}