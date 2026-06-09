package org.siemac.metamac.srm.core.category.mapper;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.base.mapper.BaseDo2DtoMapperImpl;
import org.siemac.metamac.core.common.util.CoreCommonUtil;
import org.siemac.metamac.srm.core.category.domain.CategoryElement;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeOrderVisualisation;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeVisualisationDto;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.siemac.metamac.srm.core.master.mapper.MasterTablesDo2DtoMapper;
import org.siemac.metamac.srm.core.resource.element.mapper.ResourceElementDo2DtoMapper;
import org.siemac.metamac.srm.core.resource.metadata.file.mapper.ResourceMetadataFileDo2DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.v2_1.domain.dto.category.CategorisationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDozerCopyMode;

@org.springframework.stereotype.Component("categoriesDo2DtoMapper")
public class CategoriesDo2DtoMapperImpl extends BaseDo2DtoMapperImpl implements CategoriesDo2DtoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.category.mapper.CategoriesDo2DtoMapper do2DtoMapperSdmxSrm;

    @Autowired
    private MasterTablesDo2DtoMapper                                                do2DtoMapper;

    @Autowired
    private ResourceElementDo2DtoMapper                                             resourceElementDo2DtoMapper;

    @Autowired
    private ResourceMetadataFileDo2DtoMapper                                        resourceMetadataFileDo2DtoMapper;

    @Override
    public CategorySchemeMetamacDto categorySchemeMetamacDoToDto(CategorySchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        CategorySchemeMetamacDto target = new CategorySchemeMetamacDto();
        target.setLifeCycle(lifeCycleDoToDto(source.getLifeCycleMetadata()));
        target.setDefaultOrderVisualisation(categorySchemeOrderVisualisationDoToRelatedResourceDto(source.getDefaultOrderVisualisation()));
        target.setStreamMessageStatus(source.getStreamMessageStatus());

        do2DtoMapperSdmxSrm.categorySchemeDoToDto(source, target);
        return target;
    }

    @Override
    public CategorySchemeMetamacBasicDto categorySchemeMetamacDoToBasicDto(CategorySchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        CategorySchemeMetamacBasicDto target = new CategorySchemeMetamacBasicDto();
        target.setStreamMessageStatus(source.getStreamMessageStatus());
        itemSchemeVersionDoToItemSchemeBasicDto(source, source.getLifeCycleMetadata(), target);
        return target;
    }

    @Override
    public RelatedResourceDto categorySchemeMetamacDoToRelatedResourceDto(CategorySchemeVersionMetamac source) {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = do2DtoMapperSdmxSrm.categorySchemeDoToRelatedResourceDto(source);
        return target;
    }

    @Override
    public List<CategorySchemeMetamacBasicDto> categorySchemeMetamacDoListToDtoList(List<CategorySchemeVersionMetamac> categorySchemeVersions) {
        List<CategorySchemeMetamacBasicDto> categorySchemeMetamacDtos = new ArrayList<CategorySchemeMetamacBasicDto>(categorySchemeVersions.size());
        for (CategorySchemeVersionMetamac categorySchemeVersion : categorySchemeVersions) {
            categorySchemeMetamacDtos.add(categorySchemeMetamacDoToBasicDto(categorySchemeVersion));
        }
        return categorySchemeMetamacDtos;
    }

    @Override
    public CategoryMetamacDto categoryMetamacDoToDto(CategoryMetamac source) {
        if (source == null) {
            return null;
        }
        CategoryMetamacDto target = new CategoryMetamacDto();
        target.setCategoryElement(categoryElementDoToRelatedResourceDto(source.getCategoryElement()));

        for (EniSchemaCatalog eniSchema : source.getEniSchemas()) {
            target.addEniSchema(do2DtoMapper.eniSchemaCatalogToDto(eniSchema));
        }

        do2DtoMapperSdmxSrm.categoryDoToDto(source, target);
        return target;
    }

    @Override
    public CategoryMetamacBasicDto categoryMetamacDoToBasicDto(CategoryMetamac source) {
        if (source == null) {
            return null;
        }
        CategoryMetamacBasicDto target = new CategoryMetamacBasicDto();
        itemDoToItemBasicDto(source, target);
        return target;
    }

    @Override
    public RelatedResourceDto categoryMetamacDoToRelatedResourceDto(CategoryMetamac source) {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = do2DtoMapperSdmxSrm.categoryDoToRelatedResourceDto(source);
        return target;
    }

    @Override
    public CategorisationDto categorisationDoToDto(Categorisation source) {
        return do2DtoMapperSdmxSrm.categorisationDoToDto(source);
    }

    @Override
    public List<CategorisationDto> categorisationDoListToDtoList(List<Categorisation> sources) {
        return do2DtoMapperSdmxSrm.categorisationsDoToDto(sources);
    }

    @Override
    public RelatedResourceDto categorisationDoToRelatedResourceDto(Categorisation source) {
        if (source == null) {
            return null;
        }
        RelatedResourceDto target = do2DtoMapperSdmxSrm.categorisationDoToRelatedResourceDto(source);
        return target;
    }

    @Override
    public CategoryElementDto categoryElementDoToDto(CategoryElement source) {
        if (source == null) {
            return null;
        }
        CategoryElementDto target = new CategoryElementDto();

        resourceElementDo2DtoMapper.resourceElementDoToDto(source, target);
        target.setIsVisible(source.getIsVisible());
        target.setLogo(resourceMetadataFileDo2DtoMapper.resourceMetadataFileDoToDto(source.getLogo()));

        return target;
    }

    @Override
    public RelatedResourceDto categoryElementDoToRelatedResourceDto(CategoryElement source) {
        if (source == null) {
            return null;
        }

        RelatedResourceDto target = new RelatedResourceDto();

        resourceElementDo2DtoMapper.resourceElementDoToRelatedResourceDto(source, target);

        return target;
    }

    @Override
    public List<CategorySchemeVisualisationDto> categorySchemeOrderVisualisationsDoToDto(List<CategorySchemeOrderVisualisation> sources) {
        if (sources == null) {
            return null;
        }
        List<CategorySchemeVisualisationDto> targets = new ArrayList<CategorySchemeVisualisationDto>(sources.size());
        for (CategorySchemeOrderVisualisation source : sources) {
            targets.add(categorySchemeOrderVisualisationDoToDto(source));
        }
        return targets;
    }

    @Override
    public CategorySchemeVisualisationDto categorySchemeOrderVisualisationDoToDto(CategorySchemeOrderVisualisation source) {
        if (source == null) {
            return null;
        }
        CategorySchemeVisualisationDto target = new CategorySchemeVisualisationDto();
        do2DtoMapperSdmxSrm.nameableArtefactToDto(TypeDozerCopyMode.COPY_ALL_METADATA, source.getNameableArtefact(), target);
        target.setCategoryScheme(categorySchemeMetamacDoToRelatedResourceDto(source.getCategorySchemeVersion()));

        // Overwrite these values in the final DTO
        target.setId(source.getId());
        target.setUuid(source.getUuid());
        target.setVersion(source.getVersion());
        target.setCreatedDate(CoreCommonUtil.transformDateTimeToDate(source.getCreatedDate()));
        target.setCreatedBy(source.getCreatedBy());
        target.setLastUpdated(CoreCommonUtil.transformDateTimeToDate(source.getLastUpdated()));
        target.setLastUpdatedBy(source.getLastUpdatedBy());
        target.setVersionOptimisticLocking(source.getVersion());
        return target;
    }

    @Override
    public RelatedResourceDto categorySchemeOrderVisualisationDoToRelatedResourceDto(CategorySchemeOrderVisualisation source) {
        RelatedResourceDto relatedResourceDto = source == null ? null : getRelatedResourceDto(source.getNameableArtefact());
        return relatedResourceDto;
    }
}