package org.siemac.metamac.srm.core.resource.element.mapper;

import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.srm.core.base.domain.ResourceElement;
import org.siemac.metamac.srm.core.base.dto.ResourceElementDto;
import org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;

@org.springframework.stereotype.Component("resourceElementDo2DtoMapper")
public class ResourceElementDo2DtoMapperImpl extends BaseDo2DtoMapperImpl implements ResourceElementDo2DtoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.category.mapper.CategoriesDo2DtoMapper do2DtoMapperSdmxSrm;

    @Override
    public void resourceElementDoToDto(ResourceElement source, ResourceElementDto target) {
        if (source == null) {
            return;
        }

        target.setShortName(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getShortName()));
        target.setComment(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getComment()));
        target.setValidFrom(CoreCommonUtil.transformDateTimeToDate(source.getValidFrom()));
        target.setValidTo(CoreCommonUtil.transformDateTimeToDate(source.getValidTo()));
        do2DtoMapperSdmxSrm.identifiableArtefactToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getIdentifiableArtefact(), target);

        // Overwrite these values in the final DTO (if not, these values are taken from the AnnotableArtefact entity)
        target.setId(source.getId());
        target.setUuid(source.getUuid());
        target.setVersion(source.getVersion());
        target.setCreatedDate(CoreCommonUtil.transformDateTimeToDate(source.getCreatedDate()));
        target.setCreatedBy(source.getCreatedBy());
        target.setLastUpdated(CoreCommonUtil.transformDateTimeToDate(source.getLastUpdated()));
        target.setLastUpdatedBy(source.getLastUpdatedBy());
        target.setVersionOptimisticLocking(source.getVersion());
    }

    @Override
    public void resourceElementDoToRelatedResourceDto(ResourceElement source, RelatedResourceDto target) {
        if (source == null) {
            return;
        }
        target.setId(source.getId()); // Database id. Only to efficient save operations
        target.setCode(source.getIdentifiableArtefact().getCode());
        target.setTitle(do2DtoMapperSdmxSrm.internationalStringToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getShortName()));
        target.setUrn(source.getIdentifiableArtefact().getUrn());
        target.setUrnProvider(source.getIdentifiableArtefact().getUrnProvider());

        target.setType(null);
    }
}