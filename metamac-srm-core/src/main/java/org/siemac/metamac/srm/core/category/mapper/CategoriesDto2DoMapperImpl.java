package org.siemac.metamac.srm.core.category.mapper;

import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.util.OptimisticLockingUtils;
import org.siemac.metamac.srm.core.base.mapper.BaseDto2DoMapperImpl;
import org.siemac.metamac.srm.core.category.domain.CategoryElement;
import org.siemac.metamac.srm.core.category.domain.CategoryElementRepository;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacRepository;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.master.dto.EniSchemaCatalogDto;
import org.siemac.metamac.srm.core.master.mapper.MasterTablesDto2DoMapper;
import org.siemac.metamac.srm.core.resource.element.mapper.ResourceElementDto2DoMapper;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component("categoriesDto2DoMapper")
public class CategoriesDto2DoMapperImpl extends BaseDto2DoMapperImpl implements CategoriesDto2DoMapper {

    @Autowired
    private com.arte.statistic.sdmx.srm.core.category.mapper.CategoriesDto2DoMapper dto2DoMapperSdmxSrm;

    @Autowired
    private MasterTablesDto2DoMapper                                                masterTablesDto2DoMapper;

    @Autowired
    private CategorySchemeVersionMetamacRepository                                  categorySchemeVersionMetamacRepository;

    @Autowired
    private CategoryMetamacRepository                                               categoryMetamacRepository;

    @Autowired
    private CategoryElementRepository                                               categoryElementRepository;

    @Autowired
    ResourceElementDto2DoMapper                                                     resourceElementDto2DoMapper;

    // ------------------------------------------------------------
    // CATEGORIES
    // ------------------------------------------------------------

    @Override
    public CategorySchemeVersionMetamac categorySchemeMetamacDtoToDo(CategorySchemeMetamacDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        CategorySchemeVersionMetamac target = null;
        if (source.getUrn() == null) {
            target = new CategorySchemeVersionMetamac();
        } else {
            target = categorySchemeVersionMetamacRepository.findByUrn(source.getUrn());
            if (target == null) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(source.getUrn())
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }

        // Modifiable attributes
        target.setStreamMessageStatus(source.getStreamMessageStatus());

        // sdmx
        dto2DoMapperSdmxSrm.categorySchemeDtoToDo(source, target);

        return target;
    }

    @Override
    public CategoryMetamac categoryMetamacDtoToDo(CategoryMetamacDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        CategoryMetamac target = null;
        if (source.getUrn() == null) {
            target = new CategoryMetamac();
        } else {
            target = categoryMetamacRepository.findByUrn(source.getUrn());
            if (target == null) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(source.getUrn())
                        .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
            }
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }

        // Modifiable attributes
        if (source.getCategoryElement() != null) {
            target.setCategoryElement(retrieveCategoryElement(source.getCategoryElement().getUrn()));
        } else {
            target.setCategoryElement(null);
        }

        target.removeAllEniSchemas();
        for (EniSchemaCatalogDto eniSchemaDto : source.getEniSchemas()) {

            target.getEniSchemas().add(masterTablesDto2DoMapper.eniSchemaCatalogDtoToEntity(eniSchemaDto));
        }

        // Sdmx
        dto2DoMapperSdmxSrm.categoryDtoToDo(source, target);

        return target;
    }

    @Override
    public CategoryElement categoryElementDtoToDo(CategoryElementDto source) throws MetamacException {
        if (source == null) {
            return null;
        }

        // If exists, retrieves existing entity. Otherwise, creates new entity.
        CategoryElement target = null;
        if (source.getUrn() == null) {
            target = new CategoryElement();
        } else {
            target = retrieveCategoryElement(source.getUrn());
            OptimisticLockingUtils.checkVersion(target.getVersion(), source.getVersionOptimisticLocking());
        }

        resourceElementDto2DoMapper.resourceElementDtoToDo(source, target);

        return target;
    }

    private CategoryElement retrieveCategoryElement(String urn) throws MetamacException {
        CategoryElement target = categoryElementRepository.findByUrn(urn);
        if (target == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).withLoggedLevel(ExceptionLevelEnum.ERROR)
                    .build();
        }
        return target;
    }
}