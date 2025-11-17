package org.siemac.metamac.srm.rest.external.v1_0.mapper.base;

import java.util.List;
import java.util.Set;

import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.rest.external.v1_0.service.utils.SrmRestInternalUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.base.domain.Component;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.StructureVersionRepository;
import com.arte.statistic.sdmx.srm.core.structure.mapper.DataStructureDefinitionDo2DtoMapper;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;

public abstract class StructureBaseDo2RestMapperV10Impl extends BaseDo2RestMapperV10Impl {

    @Autowired
    protected CommonDo2RestMapperV10    commonDo2RestMapper;

    @Autowired
    private StructureVersionRepository  structureVersionRepository;

    @Autowired
    DataStructureDefinitionDo2DtoMapper dataStructureDefinitionDo2DtoMapper;

    protected void toStructure(StructureVersion source, SrmLifeCycleMetadata lifeCycleSource, org.siemac.metamac.rest.structural_resources.v1_0.domain.DataStructure target) {

        toMaintainableArtefact(source.getMaintainableArtefact(), lifeCycleSource, target);

        target.setReplaceToVersion(toStructureReplaceToVersion(source));
        target.setReplacedByVersion(toStructureReplacedByVersion(source));
        target.setLastUpdated(toDate(source.getLastUpdated()));

        if (SrmRestInternalUtils.uriMustBeSelfLink(source.getMaintainableArtefact())) {
            target.setUri(target.getSelfLink().getHref());
        }
    }

    protected boolean canResourceBeProvidedByApi(StructureVersion source) {
        return canResourceBeProvidedByApi(source.getMaintainableArtefact()) && canStructureVersionBeProvidedByApi(source);
    }

    protected String toStructureReplaceToVersion(StructureVersion source) {
        return toStructureReplaceMetadataIfResourceCanBeProvidedByApi(source.getStructure().getId(), source.getMaintainableArtefact().getReplaceToVersion());
    }

    protected String toStructureReplacedByVersion(StructureVersion source) {
        return toStructureReplaceMetadataIfResourceCanBeProvidedByApi(source.getStructure().getId(), source.getMaintainableArtefact().getReplacedByVersion());
    }

    private String toStructureReplaceMetadataIfResourceCanBeProvidedByApi(Long structureId, String version) {
        if (version == null) {
            return null;
        }
        StructureVersion structureVersion = structureVersionRepository.retrieveByVersion(structureId, version);
        if (structureVersion != null && canResourceBeProvidedByApi(structureVersion)) {
            return version;
        } else {
            return null;
        }
    }

    protected abstract boolean canStructureVersionBeProvidedByApi(StructureVersion source);

    protected List<Component> getDimensionsOrdered(Set<Component> components, TypeComponentList type) {
        return dataStructureDefinitionDo2DtoMapper.getComponentsOrdered(components, type);
    }

    protected List<Component> getAttributesOrdered(Set<Component> components, TypeComponentList type) {
        return dataStructureDefinitionDo2DtoMapper.getComponentsOrdered(components, type);
    }
}