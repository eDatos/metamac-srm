package org.siemac.metamac.srm.core.resource.metadata.file.mapper;

import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.srm.core.base.domain.ResourceMetadataFile;
import org.siemac.metamac.srm.core.base.dto.ResourceMetadataFileDto;
import org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapperImpl;

@org.springframework.stereotype.Component("resourceMetadataFileDo2DtoMapper")
public class ResourceMetadataFileDo2DtoMapperImpl extends BaseDo2DtoMapperImpl implements ResourceMetadataFileDo2DtoMapper {

    public ResourceMetadataFileDto resourceMetadataFileDoToDto(ResourceMetadataFile source) {
        if (source == null) {
            return null;
        }

        ResourceMetadataFileDto target = new ResourceMetadataFileDto();

        target.setFileName(source.getFileName());
        target.setExtension(source.getExtension());
        target.setFullFileName(source.getFileName() + "." + source.getExtension());

        target.setId(source.getId());
        target.setUuid(source.getUuid());
        target.setVersion(source.getVersion());
        target.setCreatedDate(CoreCommonUtil.transformDateTimeToDate(source.getCreatedDate()));
        target.setCreatedBy(source.getCreatedBy());
        target.setLastUpdated(CoreCommonUtil.transformDateTimeToDate(source.getLastUpdated()));
        target.setLastUpdatedBy(source.getLastUpdatedBy());

        return target;
    }
}