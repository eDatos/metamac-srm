package org.siemac.metamac.srm.rest.internal.v1_0.mapper.category;

import org.fornax.cartridges.sculptor.framework.domain.Property;
import org.siemac.metamac.rest.common.query.domain.MetamacRestOrder;
import org.siemac.metamac.rest.common.query.domain.MetamacRestQueryPropertyRestriction;
import org.siemac.metamac.rest.search.criteria.SculptorPropertyCriteriaBase;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria;
import org.siemac.metamac.rest.search.criteria.mapper.RestCriteria2SculptorCriteria.CriteriaCallback;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategorisationCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategorisationCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryElementCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategoryElementCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategorySchemeCriteriaPropertyOrder;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.CategorySchemeCriteriaPropertyRestriction;
import org.siemac.metamac.srm.core.category.domain.CategoryElement;
import org.siemac.metamac.srm.core.category.domain.CategoryElementProperties;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacProperties;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacProperties;
import org.siemac.metamac.srm.rest.internal.v1_0.mapper.base.BaseRest2DoMapperV10Impl;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorisationProperties;

@Component
public class CategoriesRest2DoMapperImpl extends BaseRest2DoMapperV10Impl implements CategoriesRest2DoMapper {

    public static final CategorySchemeVersionMetamacProperties          CategoryElmementProperties    = null;
    private RestCriteria2SculptorCriteria<CategorySchemeVersionMetamac> categorySchemeCriteriaMapper  = null;
    private RestCriteria2SculptorCriteria<CategoryMetamac>              categoryCriteriaMapper        = null;
    private RestCriteria2SculptorCriteria<Categorisation>               categorisationCriteriaMapper  = null;
    private RestCriteria2SculptorCriteria<CategoryElement>              categoryElementCriteriaMapper = null;

    public CategoriesRest2DoMapperImpl() {
        super();
        categorySchemeCriteriaMapper = new RestCriteria2SculptorCriteria<>(CategorySchemeVersionMetamac.class, CategorySchemeCriteriaPropertyOrder.class,
                CategorySchemeCriteriaPropertyRestriction.class, new CategorySchemeCriteriaCallback());
        categoryCriteriaMapper = new RestCriteria2SculptorCriteria<>(CategoryMetamac.class, CategoryCriteriaPropertyOrder.class, CategoryCriteriaPropertyRestriction.class,
                new CategoryCriteriaCallback());
        categorisationCriteriaMapper = new RestCriteria2SculptorCriteria<>(Categorisation.class, CategorisationCriteriaPropertyOrder.class, CategorisationCriteriaPropertyRestriction.class,
                new CategorisationCriteriaCallback());
        categoryElementCriteriaMapper = new RestCriteria2SculptorCriteria<>(CategoryElement.class, CategoryElementCriteriaPropertyOrder.class, CategoryElementCriteriaPropertyRestriction.class,
                new CategoryElementCriteriaCallback());
    }

    @Override
    public RestCriteria2SculptorCriteria<CategorySchemeVersionMetamac> getCategorySchemeCriteriaMapper() {
        return categorySchemeCriteriaMapper;
    }

    @Override
    public RestCriteria2SculptorCriteria<CategoryMetamac> getCategoryCriteriaMapper() {
        return categoryCriteriaMapper;
    }

    @Override
    public RestCriteria2SculptorCriteria<Categorisation> getCategorisationCriteriaMapper() {
        return categorisationCriteriaMapper;
    }

    @Override
    public RestCriteria2SculptorCriteria<CategoryElement> getCategoryElementCriteriaMapper() {
        return categoryElementCriteriaMapper;
    }

    private class CategorySchemeCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) {
            CategorySchemeCriteriaPropertyRestriction propertyNameCriteria = CategorySchemeCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return buildSculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, CategorySchemeVersionMetamacProperties.maintainableArtefact());
                case NAME:
                    return buildSculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().name().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case DESCRIPTION:
                    return buildSculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().description().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case VALID_FROM:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, CategorySchemeVersionMetamacProperties.maintainableArtefact().validFrom(),
                            CategorySchemeVersionMetamac.class, false);
                case VALID_TO:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, CategorySchemeVersionMetamacProperties.maintainableArtefact().validTo(),
                            CategorySchemeVersionMetamac.class, false);
                case PROC_STATUS:
                    return buildSculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().procStatus(), PropertyTypeEnum.PROC_STATUS, propertyRestriction);
                case INTERNAL_PUBLICATION_DATE:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, CategorySchemeVersionMetamacProperties.lifeCycleMetadata().internalPublicationDate(),
                            CategorySchemeVersionMetamac.class, true);
                case INTERNAL_PUBLICATION_USER:
                    return buildSculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().internalPublicationUser(), PropertyTypeEnum.STRING, propertyRestriction);
                case EXTERNAL_PUBLICATION_DATE:
                    return buildSculptorPropertyCriteriaForDateProperty(propertyRestriction, CategorySchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationDate(),
                            CategorySchemeVersionMetamac.class, true);
                case EXTERNAL_PUBLICATION_USER:
                    return buildSculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().externalPublicationUser(), PropertyTypeEnum.STRING, propertyRestriction);
                case LATEST:
                    return buildSculptorPropertyCriteria(CategorySchemeVersionMetamacProperties.maintainableArtefact().latestVersionNumberFinal(), PropertyTypeEnum.BOOLEAN, propertyRestriction);
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) {
            CategorySchemeCriteriaPropertyOrder propertyNameCriteria = CategorySchemeCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return CategorySchemeVersionMetamacProperties.maintainableArtefact().code();
                case NAME:
                    return CategorySchemeVersionMetamacProperties.maintainableArtefact().name().texts().label();
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() {
            return CategorySchemeVersionMetamacProperties.maintainableArtefact().code();
        }
    }

    private class CategoryCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) {
            CategoryCriteriaPropertyRestriction propertyNameCriteria = CategoryCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return buildSculptorPropertyCriteria(CategoryMetamacProperties.nameableArtefact().codeFull(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, CategoryMetamacProperties.nameableArtefact());
                case NAME:
                    return buildSculptorPropertyCriteria(CategoryMetamacProperties.nameableArtefact().name().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case DESCRIPTION:
                    return buildSculptorPropertyCriteria(CategoryMetamacProperties.nameableArtefact().description().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case CATEGORY_SCHEME_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact());
                case CATEGORY_SCHEME_PROC_STATUS:
                    return buildSculptorPropertyCriteria(CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact().publicLogic(), PropertyTypeEnum.PROC_STATUS_ITEM_SCHEME_FROM_ITEM,
                            propertyRestriction);
                case CATEGORY_SCHEME_LATEST:
                    return buildSculptorPropertyCriteria(CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact().latestVersionNumberFinal(), PropertyTypeEnum.BOOLEAN,
                            propertyRestriction);
                case PARENT:
                    return buildSculptorPropertyCriteria(CategoryMetamacProperties.parent().nameableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case CATEGORY_ELEMENT_CODE:
                    return buildSculptorPropertyCriteria(CategoryMetamacProperties.categoryElement().identifiableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case CATEGORY_ELEMENT_SHORT_NAME:
                    return buildSculptorPropertyCriteria(CategoryMetamacProperties.categoryElement().shortName().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case CATEGORY_ELEMENT_URN:
                    return buildSculptorPropertyCriteria(CategoryMetamacProperties.categoryElement().identifiableArtefact().urn(), PropertyTypeEnum.STRING, propertyRestriction);
                case CATEGORY_ELEMENT_VISIBLE:
                    return buildSculptorPropertyCriteria(CategoryMetamacProperties.categoryElement().isVisible(), PropertyTypeEnum.BOOLEAN, propertyRestriction);

                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) {
            CategoryCriteriaPropertyOrder propertyNameCriteria = CategoryCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return CategoryMetamacProperties.nameableArtefact().codeFull();
                case NAME:
                    return CategoryMetamacProperties.nameableArtefact().name().texts().label();
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() {
            return CategoryMetamacProperties.nameableArtefact().codeFull();
        }
    }

    private class CategorisationCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) {
            CategorisationCriteriaPropertyRestriction propertyNameCriteria = CategorisationCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return buildSculptorPropertyCriteria(CategorisationProperties.maintainableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, CategorisationProperties.maintainableArtefact());
                case NAME:
                    return buildSculptorPropertyCriteria(CategorisationProperties.maintainableArtefact().name().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                case ARTEFACT_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, CategorisationProperties.artefactCategorised());
                case CATEGORY_URN:
                    return buildSculptorPropertyCriteriaDisjunctionForUrnProperty(propertyRestriction, CategorisationProperties.category().nameableArtefact());
                case LATEST:
                    return buildSculptorPropertyCriteria(CategorisationProperties.maintainableArtefact().latestVersionNumberFinal(), PropertyTypeEnum.BOOLEAN, propertyRestriction);
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) {
            CategorisationCriteriaPropertyOrder propertyNameCriteria = CategorisationCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return CategorisationProperties.maintainableArtefact().code();
                case NAME:
                    return CategorisationProperties.maintainableArtefact().name().texts().label();
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() {
            return CategorisationProperties.maintainableArtefact().code();
        }
    }

    private class CategoryElementCriteriaCallback implements CriteriaCallback {

        @Override
        public SculptorPropertyCriteriaBase retrieveProperty(MetamacRestQueryPropertyRestriction propertyRestriction) {
            CategoryElementCriteriaPropertyRestriction propertyNameCriteria = CategoryElementCriteriaPropertyRestriction.fromValue(propertyRestriction.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return buildSculptorPropertyCriteria(CategoryElementProperties.identifiableArtefact().code(), PropertyTypeEnum.STRING, propertyRestriction);
                case URN:
                    return buildSculptorPropertyCriteria(CategoryElementProperties.identifiableArtefact().urn(), PropertyTypeEnum.STRING, propertyRestriction);
                case NAME:
                    return buildSculptorPropertyCriteria(CategoryElementProperties.shortName().texts().label(), PropertyTypeEnum.STRING, propertyRestriction);
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrder(MetamacRestOrder order) {
            CategoryElementCriteriaPropertyOrder propertyNameCriteria = CategoryElementCriteriaPropertyOrder.fromValue(order.getPropertyName());
            switch (propertyNameCriteria) {
                case ID:
                    return CategoryElementProperties.identifiableArtefact().code();
                default:
                    throw toRestExceptionParameterUnexpected(propertyNameCriteria.name());
            }
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Property retrievePropertyOrderDefault() {
            return CategoryElementProperties.identifiableArtefact().code();
        }
    }

}