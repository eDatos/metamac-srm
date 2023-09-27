package org.siemac.metamac.srm.core.resource.element.mapper;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.base.domain.ResourceElement;
import org.siemac.metamac.srm.core.base.dto.ResourceElementDto;

public interface ResourceElementDto2DoMapper {

    public void resourceElementDtoToDo(ResourceElementDto source, ResourceElement target) throws MetamacException;
}