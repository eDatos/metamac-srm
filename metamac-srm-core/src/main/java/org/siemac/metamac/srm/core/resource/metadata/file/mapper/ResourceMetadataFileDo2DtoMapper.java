package org.siemac.metamac.srm.core.resource.metadata.file.mapper;

import org.siemac.metamac.srm.core.base.domain.ResourceMetadataFile;
import org.siemac.metamac.srm.core.base.dto.ResourceMetadataFileDto;

public interface ResourceMetadataFileDo2DtoMapper {

    public ResourceMetadataFileDto resourceMetadataFileDoToDto(ResourceMetadataFile source);
}