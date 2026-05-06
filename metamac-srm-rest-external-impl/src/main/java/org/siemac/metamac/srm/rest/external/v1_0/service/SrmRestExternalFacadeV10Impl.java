package org.siemac.metamac.srm.rest.external.v1_0.service;

import static org.siemac.metamac.srm.rest.external.v1_0.service.utils.SrmRestInternalUtils.hasField;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria.Operator;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.api.export.mapper.PlainTextResource;
import org.siemac.metamac.rest.common.v1_0.domain.ComparisonOperator;
import org.siemac.metamac.rest.enume.utils.ResourcesFormat;
import org.siemac.metamac.rest.enume.utils.ResourcesFormatOperation;
import org.siemac.metamac.rest.exception.RestCommonServiceExceptionType;
import org.siemac.metamac.rest.exception.RestException;
import org.siemac.metamac.rest.exception.utils.RestExceptionUtils;
import org.siemac.metamac.rest.search.criteria.SculptorCriteria;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Agencies;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Agency;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.AgencyScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.AgencySchemes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Categories;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Categorisation;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Categorisations;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Category;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategoryElementResource;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategoryElements;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategoryScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CategorySchemes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Code;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CodeCriteriaPropertyRestriction;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Codelist;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CodelistFamilies;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CodelistFamily;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.CodelistRestrictions;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Codelists;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Codes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Concept;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ConceptScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ConceptSchemes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ConceptTypes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Concepts;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ContentConstraint;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.ContentConstraints;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataConsumer;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataConsumerScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataConsumerSchemes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataConsumers;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataProvider;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataProviderScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataProviderSchemes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataProviders;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataStructure;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.DataStructures;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Organisation;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.OrganisationScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.OrganisationSchemes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.OrganisationUnit;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.OrganisationUnitScheme;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.OrganisationUnitSchemes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.OrganisationUnits;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Organisations;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.RecodedCodes;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.RegionReference;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Variable;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElement;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElements;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableElementsGeoInfo;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableFamilies;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.VariableFamily;
import org.siemac.metamac.rest.structural_resources.v1_0.domain.Variables;
import org.siemac.metamac.srm.core.base.serviceapi.MiscMetamacService;
import org.siemac.metamac.srm.core.category.domain.CategoryElement;
import org.siemac.metamac.srm.core.category.domain.CategoryElementProperties;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacProperties;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacResultSelection;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultSelection;
import org.siemac.metamac.srm.core.code.domain.CodelistFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistOpennessVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistRestrictionVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.ConceptMetamacResultSelection;
import org.siemac.metamac.srm.core.code.domain.RelatedCode;
import org.siemac.metamac.srm.core.code.domain.VariableElementProperties;
import org.siemac.metamac.srm.core.code.domain.VariableElementResult;
import org.siemac.metamac.srm.core.code.domain.VariableElementResultSelection;
import org.siemac.metamac.srm.core.code.domain.VariableFamilyProperties;
import org.siemac.metamac.srm.core.code.domain.VariableProperties;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.GeneratorUrnUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeOrderVisualisation;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.core.dsd.serviceapi.DataStructureDefinitionMetamacService;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacProperties;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.siemac.metamac.rest.common.annotation.CacheableMethod;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.external.constants.SrmRestExternalConstants;
import org.siemac.metamac.srm.rest.external.domain.cache.CategorySchemeCacheableResource;
import org.siemac.metamac.srm.rest.external.domain.cache.CodelistCacheableResource;
import org.siemac.metamac.srm.rest.external.domain.cache.ConceptSchemeCacheableResource;
import org.siemac.metamac.srm.rest.external.domain.cache.OrganisationSchemeCacheableResource;
import org.siemac.metamac.srm.rest.external.domain.cache.VariableElementCacheableResource;
import org.siemac.metamac.srm.rest.external.exception.RestServiceExceptionType;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.category.CategoriesDo2RestMapperV10;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.category.CategoriesRest2DoMapper;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.code.CodesDo2RestMapperV10;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.code.CodesRest2DoMapper;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.concept.ConceptsDo2RestMapperV10;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.concept.ConceptsRest2DoMapper;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.constraint.ContentConstraintsDo2RestMapperV10;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.constraint.ContentConstraintsRest2DoMapper;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.dsd.DataStructuresDo2RestMapperV10;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.dsd.DataStructuresRest2DoMapper;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.organisation.OrganisationsDo2RestMapperV10;
import org.siemac.metamac.srm.rest.external.v1_0.mapper.organisation.OrganisationsRest2DoMapper;
import org.siemac.metamac.srm.rest.external.v1_0.service.utils.SrmRestInternalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.category.domain.CategorisationProperties;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersionProperties;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraintProperties;
import com.arte.statistic.sdmx.srm.core.constraint.domain.RegionValue;
import com.arte.statistic.sdmx.srm.core.constraint.serviceapi.ConstraintsService;
import com.arte.statistic.sdmx.srm.core.structure.domain.DataStructureDefinitionVersionProperties;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationTypeEnum;

@Service("srmRestExternalFacadeV10")
public class SrmRestExternalFacadeV10Impl implements SrmRestExternalFacadeV10 {

    @Autowired
    private ConceptsMetamacService                conceptsService;

    @Autowired
    private CategoriesMetamacService              categoriesService;

    @Autowired
    private OrganisationsMetamacService           organisationsService;

    @Autowired
    private CodesMetamacService                   codesService;

    @Autowired
    private DataStructureDefinitionMetamacService dataStructureDefinitionService;

    @Autowired
    private ConstraintsService                    constraintsService;

    @Autowired
    private MiscMetamacService                    miscMetamacService;

    @Autowired
    private ConfigurationService                  configurationService;

    @Autowired
    private ConceptsRest2DoMapper                 conceptsRest2DoMapper;

    @Autowired
    private CategoriesRest2DoMapper               categoriesRest2DoMapper;

    @Autowired
    private OrganisationsRest2DoMapper            organisationsRest2DoMapper;

    @Autowired
    private CodesRest2DoMapper                    codesRest2DoMapper;

    @Autowired
    private DataStructuresRest2DoMapper           dataStructuresRest2DoMapper;

    @Autowired
    private ConceptsDo2RestMapperV10              conceptsDo2RestMapper;

    @Autowired
    private CategoriesDo2RestMapperV10            categoriesDo2RestMapper;

    @Autowired
    private OrganisationsDo2RestMapperV10         organisationsDo2RestMapper;

    @Autowired
    private CodesDo2RestMapperV10                 codesDo2RestMapper;

    @Autowired
    private ContentConstraintsRest2DoMapper       contentConstraintsRest2DoMapper;

    @Autowired
    private ContentConstraintsDo2RestMapperV10    contentConstraintsDo2RestMapper;

    @Autowired
    private DataStructuresDo2RestMapperV10        dataStructuresDo2RestMapperV10;

    private final ServiceContext                  ctx                      = SrmRestExternalConstants.SERVICE_CONTEXT;
    private final Logger                          logger                   = LoggerFactory.getLogger(SrmRestExternalFacadeV10Impl.class);
    private final PagingParameter                 pagingParameterOneResult = PagingParameter.pageAccess(1, 1, false);

    @Override
    public ConceptSchemes findConceptSchemes(String query, String orderBy, String limit, String offset) {
        return findConceptSchemes(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public ConceptSchemes findConceptSchemes(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findConceptSchemes(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public ConceptSchemes findConceptSchemes(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findConceptSchemes(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public ConceptScheme retrieveConceptScheme(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemePublished(agencyID, resourceID, version);

            // Transform
            return conceptsDo2RestMapper.toConceptScheme(conceptSchemeVersion);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @CacheableMethod(ConceptSchemeCacheableResource.class)
    @Override
    public Concepts findConcepts(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String orderConfiguration, String fields) {
        try {
            Set<String> fieldsToShow = SrmRestInternalUtils.parseFieldsParameter(fields);
            return findConceptsCommon(agencyID, resourceID, version, query, orderBy, limit, offset, orderConfiguration, fieldsToShow);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @CacheableMethod(ConceptSchemeCacheableResource.class)
    @Override
    public Response findConceptsTSV(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String orderConfiguration, String fields) {
        return findConceptsPlainText(agencyID, resourceID, version, query, orderBy, limit, offset, orderConfiguration, fields, "tsv");
    }

    @CacheableMethod(ConceptSchemeCacheableResource.class)
    @Override
    public Response findConceptsCSV(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String orderConfiguration, String fields) {
        return findConceptsPlainText(agencyID, resourceID, version, query, orderBy, limit, offset, orderConfiguration, fields, "csv");
    }

    @CacheableMethod(ConceptSchemeCacheableResource.class)
    @Override
    public Response findConceptsXLSX(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String orderConfiguration, String fields) {
        return findConceptsPlainText(agencyID, resourceID, version, query, orderBy, limit, offset, orderConfiguration, fields, "xlsx");
    }

    private Response findConceptsPlainText(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String orderConfiguration, String fields,
            String format) {
        try {
            Set<String> fieldsToShow = SrmRestInternalUtils.parseFieldsParameter(fields);
            Concepts concepts = findConceptsCommon(agencyID, resourceID, version, query, orderBy, limit, offset, orderConfiguration, fieldsToShow);

            // Transform
            List<PlainTextResource> flattenResource = conceptsDo2RestMapper.toPlainTextConcepts(concepts, configurationService.retrieveLanguages(), fieldsToShow, format);

            String fileNamePrefix = "concepts";
            return Response.status(Status.OK).entity(flattenResource).header("Content-Disposition", getContentDisposition(fileNamePrefix, format)).build();

        } catch (Exception e) {
            return manageExceptionResponse(e);
        }
    }

    private Concepts findConceptsCommon(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String orderConfiguration,
            Set<String> fieldsToShow) throws MetamacException {

        if (mustFindItemsInsteadRetrieveAllItemsOfItemScheme(agencyID, resourceID, version, query, orderBy, limit, offset)) {
            checkParameterEmpty(SrmRestConstants.PARAMETER_ORDER_CONFIGURATION_ID, orderConfiguration);
            // Find. Retrieve concepts paginated
            SculptorCriteria sculptorCriteria = conceptsRest2DoMapper.getConceptCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            List<String> selectedLanguages = configurationService.retrieveLanguages();
            sculptorCriteria.setOrderByCaseCondition(ConceptMetamac.class, ConceptMetamacProperties.nameableArtefact().name().texts().locale(), selectedLanguages, "=", orderBy);

            PagedResult<ConceptMetamac> entitiesPagedResult = findConceptsCore(agencyID, resourceID, version, null, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            return conceptsDo2RestMapper.toConcepts(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit(), fieldsToShow);
        } else {
            // Retrieve all concepts of conceptScheme, without pagination
            ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemePublished(agencyID, resourceID, version);

            String orderConfigurationToUse = getOrderConfigurationToUseToRetrieveConceptScheme(agencyID, resourceID, version, orderConfiguration, conceptSchemeVersion);

            ConceptMetamacResultSelection itemMetamacResultSelection = ConceptMetamacResultSelection.API;
            itemMetamacResultSelection.setDescriptions(SrmRestInternalUtils.containsField(fieldsToShow, SrmRestConstants.FIELD_INCLUDE_DESCRIPTION));
            itemMetamacResultSelection.setAnnotations(SrmRestInternalUtils.containsField(fieldsToShow, SrmRestConstants.FIELD_INCLUDE_ANNOTATIONS));
            itemMetamacResultSelection.setPluralName(SrmRestInternalUtils.containsField(fieldsToShow, SrmRestConstants.FIELD_INCLUDE_PLURAL_NAME));
            List<ItemResult> items = conceptsService.retrieveConceptsByConceptSchemeUrnOrderedInDepth(ctx, conceptSchemeVersion.getMaintainableArtefact().getUrn(), itemMetamacResultSelection,
                    orderConfigurationToUse);

            // Transform
            return conceptsDo2RestMapper.toConcepts(items, conceptSchemeVersion, fieldsToShow);
        }
    }

    @Override
    public Concept retrieveConcept(String agencyID, String resourceID, String version, String conceptID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, conceptID, SrmRestConstants.PARAMETER_CONCEPT_ID);

            // Find one
            PagedResult<ConceptMetamac> entitiesPagedResult = findConceptsCore(agencyID, resourceID, version, conceptID, null, pagingParameterOneResult);
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CONCEPT_NOT_FOUND, conceptID, version, resourceID, agencyID);
                throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
            }

            // Transform
            return conceptsDo2RestMapper.toConcept(entitiesPagedResult.getValues().get(0));
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public ConceptTypes retrieveConceptTypes() {
        try {
            // Retrieve all
            List<ConceptType> entitiesResult = conceptsService.findAllConceptTypes(ctx);

            // Transform
            return conceptsDo2RestMapper.toConceptTypes(entitiesResult);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public CategorySchemes findCategorySchemes(String query, String orderBy, String limit, String offset) {
        return findCategorySchemes(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public CategorySchemes findCategorySchemes(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findCategorySchemes(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public CategorySchemes findCategorySchemes(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findCategorySchemes(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public CategoryScheme retrieveCategoryScheme(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemePublished(agencyID, resourceID, version);

            // Transform
            return categoriesDo2RestMapper.toCategoryScheme(categorySchemeVersion);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @CacheableMethod(CategorySchemeCacheableResource.class)
    @Override
    public Categories findCategories(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String fields) {
        try {
            Set<String> fieldsToShow = getFieldsParameters(fields);

            return findCategoriesCommon(agencyID, resourceID, version, query, orderBy, limit, offset, fieldsToShow);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @CacheableMethod(CategorySchemeCacheableResource.class)
    @Override
    public Response findCategoriesTSV(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String fields) {
        return findCategoriesPlainText(agencyID, resourceID, version, query, orderBy, limit, offset, fields, "tsv");
    }

    @CacheableMethod(CategorySchemeCacheableResource.class)
    @Override
    public Response findCategoriesCSV(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String fields) {
        return findCategoriesPlainText(agencyID, resourceID, version, query, orderBy, limit, offset, fields, "csv");
    }

    @CacheableMethod(CategorySchemeCacheableResource.class)
    @Override
    public Response findCategoriesXLSX(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String fields) {
        return findCategoriesPlainText(agencyID, resourceID, version, query, orderBy, limit, offset, fields, "xlsx");
    }

    private Response findCategoriesPlainText(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String fields, String format) {
        try {

            Set<String> fieldsToShow = getFieldsParameters(fields);

            Categories categories = findCategoriesCommon(agencyID, resourceID, version, query, orderBy, limit, offset, fieldsToShow);

            // Transform
            List<PlainTextResource> flattenResource = categoriesDo2RestMapper.toPlainTextCategories(categories, configurationService.retrieveLanguages(), fieldsToShow, format);

            String fileNamePrefix = "categories";
            return Response.status(Status.OK).entity(flattenResource).header("Content-Disposition", getContentDisposition(fileNamePrefix, format)).build();

        } catch (Exception e) {
            return manageExceptionResponse(e);
        }
    }

    public Categories findCategoriesCommon(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, Set<String> fieldsToShow)
            throws MetamacException {

        if (mustFindItemsInsteadRetrieveAllItemsOfItemScheme(agencyID, resourceID, version, query, orderBy, limit, offset)) {
            // Find. Retrieve categories paginated
            SculptorCriteria sculptorCriteria = categoriesRest2DoMapper.getCategoryCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            List<String> selectedLanguages = configurationService.retrieveLanguages();
            sculptorCriteria.setOrderByCaseCondition(CategoryMetamac.class, CategoryMetamacProperties.nameableArtefact().name().texts().locale(), selectedLanguages, "=", orderBy);

            PagedResult<CategoryMetamac> entitiesPagedResult = findCategoriesCore(agencyID, resourceID, version, null, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            return categoriesDo2RestMapper.toCategories(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit(), fieldsToShow);
        } else {
            // Retrieve all categories of categoryScheme, without pagination
            CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemePublished(agencyID, resourceID, version);

            CategoryMetamacResultSelection categoryMetamacResultSelection = CategoryMetamacResultSelection.API;

            categoryMetamacResultSelection.setAnnotations(SrmRestInternalUtils.containsField(fieldsToShow, SrmRestConstants.FIELD_INCLUDE_ANNOTATIONS));
            categoryMetamacResultSelection.setCategoryElementFull(SrmRestInternalUtils.containsField(fieldsToShow, SrmRestConstants.FIELD_INCLUDE_CATEGORY_ELEMENT));
            categoryMetamacResultSelection.setEniSchemaFull(SrmRestInternalUtils.containsField(fieldsToShow, SrmRestConstants.FIELD_INCLUDE_ENI_SCHEME));
            List<ItemResult> items = categoriesService.retrieveCategoriesByCategorySchemeUrnUnordered(ctx, categorySchemeVersion.getMaintainableArtefact().getUrn(), categoryMetamacResultSelection);

            // Transform
            return categoriesDo2RestMapper.toCategories(items, categorySchemeVersion, fieldsToShow);
        }
    }

    @Override
    public Category retrieveCategory(String agencyID, String resourceID, String version, String categoryID, String fields) {
        try {

            Set<String> validFields = new HashSet<>();
            validFields.add(SrmRestConstants.FIELD_INCLUDE_CATEGORY_ELEMENT);
            Set<String> fieldsToShow = SrmRestInternalUtils.parseFieldsParameter(fields, validFields);

            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, categoryID, SrmRestConstants.PARAMETER_CATEGORY_ID);

            // Find one
            PagedResult<CategoryMetamac> entitiesPagedResult = findCategoriesCore(agencyID, resourceID, version, categoryID, null, pagingParameterOneResult);
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CATEGORY_NOT_FOUND, categoryID, version, resourceID,
                        agencyID);
                throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
            }

            // Transform
            return categoriesDo2RestMapper.toCategory(entitiesPagedResult.getValues().get(0), fieldsToShow);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Categorisations findCategorisations(String query, String orderBy, String limit, String offset) {
        return findCategorisations(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public Categorisations findCategorisations(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findCategorisations(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public Categorisations findCategorisations(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findCategorisations(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public Categorisation retrieveCategorisation(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            PagedResult<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation> entitiesPagedResult = findCategorisationsCore(agencyID, resourceID, version, null, pagingParameterOneResult);
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CATEGORISATION_NOT_FOUND, resourceID, version, agencyID);
                throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
            }

            // Transform
            return categoriesDo2RestMapper.toCategorisation(entitiesPagedResult.getValues().get(0));
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public OrganisationSchemes findOrganisationSchemes(String query, String orderBy, String limit, String offset) {
        return findOrganisationSchemes(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public OrganisationSchemes findOrganisationSchemes(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findOrganisationSchemes(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public OrganisationSchemes findOrganisationSchemes(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findOrganisationSchemes(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public OrganisationScheme retrieveOrganisationScheme(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationSchemePublished(agencyID, resourceID, version);

            // Transform
            return organisationsDo2RestMapper.toOrganisationScheme(organisationSchemeVersion);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @CacheableMethod(OrganisationSchemeCacheableResource.class)
    @Override
    public Organisations findOrganisations(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String fields) {
        try {
            Set<String> fieldsToShow = SrmRestInternalUtils.parseFieldsParameter(fields);
            return findOrganisationsCommon(agencyID, resourceID, version, query, orderBy, limit, offset, fieldsToShow);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @CacheableMethod(OrganisationSchemeCacheableResource.class)
    @Override
    public Response findOrganisationsTSV(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String fields) {
        return findOrganisationsPlainText(agencyID, resourceID, version, query, orderBy, limit, offset, fields, "tsv");
    }

    @CacheableMethod(OrganisationSchemeCacheableResource.class)
    @Override
    public Response findOrganisationsCSV(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String fields) {
        return findOrganisationsPlainText(agencyID, resourceID, version, query, orderBy, limit, offset, fields, "csv");
    }

    @CacheableMethod(OrganisationSchemeCacheableResource.class)
    @Override
    public Response findOrganisationsXLSX(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String fields) {
        return findOrganisationsPlainText(agencyID, resourceID, version, query, orderBy, limit, offset, fields, "xlsx");
    }

    private Response findOrganisationsPlainText(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String fields, String format) {
        try {
            Set<String> fieldsToShow = SrmRestInternalUtils.parseFieldsParameter(fields);
            Organisations organisations = findOrganisationsCommon(agencyID, resourceID, version, query, orderBy, limit, offset, fieldsToShow);

            // Transform
            List<PlainTextResource> flattenResource = organisationsDo2RestMapper.toPlainTextOrganisations(organisations, configurationService.retrieveLanguages(), fieldsToShow, format);
            String fileNamePrefix = "organisations";
            return Response.status(Status.OK).entity(flattenResource).header("Content-Disposition", getContentDisposition(fileNamePrefix, format)).build();

        } catch (Exception e) {
            return manageExceptionResponse(e);
        }
    }

    public Organisations findOrganisationsCommon(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, Set<String> fieldsToShow)
            throws MetamacException {

        if (mustFindItemsInsteadRetrieveAllItemsOfItemScheme(agencyID, resourceID, version, query, orderBy, limit, offset)) {
            // Find. Retrieve organisations paginated
            SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            List<String> selectedLanguages = configurationService.retrieveLanguages();
            sculptorCriteria.setOrderByCaseCondition(OrganisationMetamac.class, OrganisationMetamacProperties.nameableArtefact().name().texts().locale(), selectedLanguages, "=", orderBy);

            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(null, agencyID, resourceID, version, null, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            return organisationsDo2RestMapper.toOrganisations(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit(), fieldsToShow);
        } else {
            // Retrieve all organisations of organisationScheme, without pagination
            OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationSchemePublished(agencyID, resourceID, version);

            ItemMetamacResultSelection itemMetamacResultSelection = ItemMetamacResultSelection.API;
            itemMetamacResultSelection.setAnnotations(SrmRestInternalUtils.containsField(fieldsToShow, SrmRestConstants.FIELD_INCLUDE_ANNOTATIONS));
            List<ItemResult> items = organisationsService.retrieveOrganisationsByOrganisationSchemeUrnUnordered(ctx, organisationSchemeVersion.getMaintainableArtefact().getUrn(),
                    itemMetamacResultSelection);

            // Transform
            return organisationsDo2RestMapper.toOrganisations(items, organisationSchemeVersion, fieldsToShow);
        }
    }

    @Override
    public Organisation retrieveOrganisation(String agencyID, String resourceID, String version, String organisationID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, organisationID, SrmRestConstants.PARAMETER_ORGANISATION_ID);

            // Find one
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(null, agencyID, resourceID, version, organisationID, null, pagingParameterOneResult);
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.ORGANISATION_NOT_FOUND, organisationID, version, resourceID,
                        agencyID);
                throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
            }

            // Transform
            return organisationsDo2RestMapper.toOrganisation(entitiesPagedResult.getValues().get(0));
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public AgencySchemes findAgencySchemes(String query, String orderBy, String limit, String offset) {
        return findAgencySchemes(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public AgencySchemes findAgencySchemes(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findAgencySchemes(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public AgencySchemes findAgencySchemes(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findAgencySchemes(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public AgencyScheme retrieveAgencyScheme(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveAgencySchemePublished(agencyID, resourceID, version);

            // Transform
            return organisationsDo2RestMapper.toAgencyScheme(organisationSchemeVersion);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @CacheableMethod(OrganisationSchemeCacheableResource.class)
    @Override
    public Agencies findAgencies(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String fields) {
        try {
            Set<String> fieldsToShow = SrmRestInternalUtils.parseFieldsParameter(fields);

            if (mustFindItemsInsteadRetrieveAllItemsOfItemScheme(agencyID, resourceID, version, query, orderBy, limit, offset)) {
                // Find. Retrieve organisations paginated
                SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

                List<String> selectedLanguages = configurationService.retrieveLanguages();
                sculptorCriteria.setOrderByCaseCondition(OrganisationMetamac.class, OrganisationMetamacProperties.nameableArtefact().name().texts().locale(), selectedLanguages, "=", orderBy);

                PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.AGENCY, agencyID, resourceID, version, null, sculptorCriteria.getConditions(),
                        sculptorCriteria.getPagingParameter());

                // Transform
                return organisationsDo2RestMapper.toAgencies(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit(), fieldsToShow);
            } else {
                // Retrieve all organisations of organisationScheme, without pagination
                OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveAgencySchemePublished(agencyID, resourceID, version);

                ItemMetamacResultSelection itemMetamacResultSelection = ItemMetamacResultSelection.API;
                itemMetamacResultSelection.setAnnotations(SrmRestInternalUtils.containsField(fieldsToShow, SrmRestConstants.FIELD_INCLUDE_ANNOTATIONS));
                List<ItemResult> items = organisationsService.retrieveOrganisationsByOrganisationSchemeUrnUnordered(ctx, organisationSchemeVersion.getMaintainableArtefact().getUrn(),
                        itemMetamacResultSelection);

                // Transform
                return organisationsDo2RestMapper.toAgencies(items, organisationSchemeVersion, fieldsToShow);
            }
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Agency retrieveAgency(String agencyID, String resourceID, String version, String organisationID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, organisationID, SrmRestConstants.PARAMETER_ORGANISATION_ID);

            // Find one
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.AGENCY, agencyID, resourceID, version, organisationID, null, pagingParameterOneResult);
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.AGENCY_NOT_FOUND, organisationID, version, resourceID,
                        agencyID);
                throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
            }

            // Transform
            return organisationsDo2RestMapper.toAgency(entitiesPagedResult.getValues().get(0));
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public OrganisationUnitSchemes findOrganisationUnitSchemes(String query, String orderBy, String limit, String offset) {
        return findOrganisationUnitSchemes(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public OrganisationUnitSchemes findOrganisationUnitSchemes(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findOrganisationUnitSchemes(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public OrganisationUnitSchemes findOrganisationUnitSchemes(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findOrganisationUnitSchemes(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public OrganisationUnitScheme retrieveOrganisationUnitScheme(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationUnitSchemePublished(agencyID, resourceID, version);

            // Transform
            return organisationsDo2RestMapper.toOrganisationUnitScheme(organisationSchemeVersion);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @CacheableMethod(OrganisationSchemeCacheableResource.class)
    @Override
    public OrganisationUnits findOrganisationUnits(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String fields) {
        try {
            Set<String> fieldsToShow = SrmRestInternalUtils.parseFieldsParameter(fields);

            if (mustFindItemsInsteadRetrieveAllItemsOfItemScheme(agencyID, resourceID, version, query, orderBy, limit, offset)) {
                // Find. Retrieve organisations paginated
                SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

                List<String> selectedLanguages = configurationService.retrieveLanguages();
                sculptorCriteria.setOrderByCaseCondition(OrganisationMetamac.class, OrganisationMetamacProperties.nameableArtefact().name().texts().locale(), selectedLanguages, "=", orderBy);

                PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.ORGANISATION_UNIT, agencyID, resourceID, version, null,
                        sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

                // Transform
                return organisationsDo2RestMapper.toOrganisationUnits(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit(), fieldsToShow);
            } else {
                // Retrieve all organisations of organisationScheme, without pagination
                OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveOrganisationUnitSchemePublished(agencyID, resourceID, version);

                ItemMetamacResultSelection itemMetamacResultSelection = ItemMetamacResultSelection.API;
                itemMetamacResultSelection.setAnnotations(SrmRestInternalUtils.containsField(fieldsToShow, SrmRestConstants.FIELD_INCLUDE_ANNOTATIONS));
                List<ItemResult> items = organisationsService.retrieveOrganisationsByOrganisationSchemeUrnUnordered(ctx, organisationSchemeVersion.getMaintainableArtefact().getUrn(),
                        itemMetamacResultSelection);

                // Transform
                return organisationsDo2RestMapper.toOrganisationUnits(items, organisationSchemeVersion, fieldsToShow);
            }
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public OrganisationUnit retrieveOrganisationUnit(String agencyID, String resourceID, String version, String organisationID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, organisationID, SrmRestConstants.PARAMETER_ORGANISATION_ID);

            // Find one
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.ORGANISATION_UNIT, agencyID, resourceID, version, organisationID, null,
                    pagingParameterOneResult);
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.ORGANISATION_UNIT_NOT_FOUND, organisationID, version,
                        resourceID, agencyID);
                throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
            }

            // Transform
            return organisationsDo2RestMapper.toOrganisationUnit(entitiesPagedResult.getValues().get(0));
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public DataProviderSchemes findDataProviderSchemes(String query, String orderBy, String limit, String offset) {
        return findDataProviderSchemes(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public DataProviderSchemes findDataProviderSchemes(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findDataProviderSchemes(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public DataProviderSchemes findDataProviderSchemes(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findDataProviderSchemes(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public DataProviderScheme retrieveDataProviderScheme(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveDataProviderSchemePublished(agencyID, resourceID, version);

            // Transform
            return organisationsDo2RestMapper.toDataProviderScheme(organisationSchemeVersion);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @CacheableMethod(OrganisationSchemeCacheableResource.class)
    @Override
    public DataProviders findDataProviders(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String fields) {
        try {
            Set<String> fieldsToShow = SrmRestInternalUtils.parseFieldsParameter(fields);

            if (mustFindItemsInsteadRetrieveAllItemsOfItemScheme(agencyID, resourceID, version, query, orderBy, limit, offset)) {
                // Find. Retrieve organisations paginated
                SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);
                PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.DATA_PROVIDER, agencyID, resourceID, version, null, sculptorCriteria.getConditions(),
                        sculptorCriteria.getPagingParameter());

                // Transform
                return organisationsDo2RestMapper.toDataProviders(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit(), fieldsToShow);
            } else {
                // Retrieve all organisations of organisationScheme, without pagination
                OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveDataProviderSchemePublished(agencyID, resourceID, version);

                ItemMetamacResultSelection itemMetamacResultSelection = ItemMetamacResultSelection.API;
                itemMetamacResultSelection.setAnnotations(SrmRestInternalUtils.containsField(fieldsToShow, SrmRestConstants.FIELD_INCLUDE_ANNOTATIONS));
                List<ItemResult> items = organisationsService.retrieveOrganisationsByOrganisationSchemeUrnUnordered(ctx, organisationSchemeVersion.getMaintainableArtefact().getUrn(),
                        itemMetamacResultSelection);

                // Transform
                return organisationsDo2RestMapper.toDataProviders(items, organisationSchemeVersion, fieldsToShow);
            }
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public DataProvider retrieveDataProvider(String agencyID, String resourceID, String version, String organisationID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, organisationID, SrmRestConstants.PARAMETER_ORGANISATION_ID);

            // Find one
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.DATA_PROVIDER, agencyID, resourceID, version, organisationID, null,
                    pagingParameterOneResult);
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.DATA_PROVIDER_NOT_FOUND, organisationID, version, resourceID,
                        agencyID);
                throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
            }

            // Transform
            return organisationsDo2RestMapper.toDataProvider(entitiesPagedResult.getValues().get(0));
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public DataConsumerSchemes findDataConsumerSchemes(String query, String orderBy, String limit, String offset) {
        return findDataConsumerSchemes(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public DataConsumerSchemes findDataConsumerSchemes(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findDataConsumerSchemes(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public DataConsumerSchemes findDataConsumerSchemes(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findDataConsumerSchemes(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public DataConsumerScheme retrieveDataConsumerScheme(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveDataConsumerSchemePublished(agencyID, resourceID, version);

            // Transform
            return organisationsDo2RestMapper.toDataConsumerScheme(organisationSchemeVersion);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @CacheableMethod(OrganisationSchemeCacheableResource.class)
    @Override
    public DataConsumers findDataConsumers(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String fields) {
        try {
            Set<String> fieldsToShow = SrmRestInternalUtils.parseFieldsParameter(fields);

            if (mustFindItemsInsteadRetrieveAllItemsOfItemScheme(agencyID, resourceID, version, query, orderBy, limit, offset)) {
                // Find. Retrieve organisations paginated
                SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);
                PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.DATA_CONSUMER, agencyID, resourceID, version, null, sculptorCriteria.getConditions(),
                        sculptorCriteria.getPagingParameter());

                // Transform
                return organisationsDo2RestMapper.toDataConsumers(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit(), fieldsToShow);
            } else {
                // Retrieve all organisations of organisationScheme, without pagination
                OrganisationSchemeVersionMetamac organisationSchemeVersion = retrieveDataConsumerSchemePublished(agencyID, resourceID, version);

                ItemMetamacResultSelection itemMetamacResultSelection = ItemMetamacResultSelection.API;
                itemMetamacResultSelection.setAnnotations(SrmRestInternalUtils.containsField(fieldsToShow, SrmRestConstants.FIELD_INCLUDE_ANNOTATIONS));
                List<ItemResult> items = organisationsService.retrieveOrganisationsByOrganisationSchemeUrnUnordered(ctx, organisationSchemeVersion.getMaintainableArtefact().getUrn(),
                        itemMetamacResultSelection);

                // Transform
                return organisationsDo2RestMapper.toDataConsumers(items, organisationSchemeVersion, fieldsToShow);
            }
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public DataConsumer retrieveDataConsumer(String agencyID, String resourceID, String version, String organisationID) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, organisationID, SrmRestConstants.PARAMETER_ORGANISATION_ID);

            // Find one
            PagedResult<OrganisationMetamac> entitiesPagedResult = findOrganisationsCore(OrganisationTypeEnum.DATA_CONSUMER, agencyID, resourceID, version, organisationID, null,
                    pagingParameterOneResult);
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.DATA_CONSUMER_NOT_FOUND, organisationID, version, resourceID,
                        agencyID);
                throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
            }

            // Transform
            return organisationsDo2RestMapper.toDataConsumer(entitiesPagedResult.getValues().get(0));
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Codelists findCodelists(String query, String orderBy, String limit, String offset) {
        return findCodelists(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public Codelists findCodelists(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findCodelists(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public Codelists findCodelists(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findCodelists(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public Codelist retrieveCodelist(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            CodelistVersionMetamac codelistVersion = retrieveCodelistPublished(agencyID, resourceID, version);

            // Transform
            return codesDo2RestMapper.toCodelist(codelistVersion);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @CacheableMethod(CodelistCacheableResource.class)
    @Override
    public Codes findCodes(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String order, String openness, String restriction,
            Boolean extended, String fields) {
        try {
            Set<String> fieldsToShow = SrmRestInternalUtils.parseFieldsParameter(fields);
            return findCodesCommon(agencyID, resourceID, version, query, orderBy, limit, offset, order, openness, restriction, extended, fieldsToShow);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @CacheableMethod(CodelistCacheableResource.class)
    @Override
    public Response findCodesTSV(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String order, String openness, String restriction,
            Boolean extended, String fields) {
        return findCodesPlainText(agencyID, resourceID, version, query, orderBy, limit, offset, order, openness, restriction, extended, fields, "tsv");
    }

    @CacheableMethod(CodelistCacheableResource.class)
    @Override
    public Response findCodesCSV(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String order, String openness, String restriction,
            Boolean extended, String fields) {
        return findCodesPlainText(agencyID, resourceID, version, query, orderBy, limit, offset, order, openness, restriction, extended, fields, "csv");
    }

    @CacheableMethod(CodelistCacheableResource.class)
    @Override
    public Response findCodesXLSX(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String order, String openness, String restriction,
            Boolean extended, String fields) {
        return findCodesPlainText(agencyID, resourceID, version, query, orderBy, limit, offset, order, openness, restriction, extended, fields, "xlsx");
    }

    private Response findCodesPlainText(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String order, String openness,
            String restriction, Boolean extended, String fields, String format) {
        try {
            Set<String> fieldsToShow = SrmRestInternalUtils.parseFieldsParameter(fields);
            Codes codes = findCodesCommon(agencyID, resourceID, version, query, orderBy, limit, offset, order, openness, restriction, extended, fieldsToShow);

            // Transform
            List<PlainTextResource> flattenResource = codesDo2RestMapper.toPlainTextCodes(codes, configurationService.retrieveLanguages(), fieldsToShow, format);

            String fileNamePrefix = "codes";
            return Response.status(Status.OK).entity(flattenResource).header("Content-Disposition", getContentDisposition(fileNamePrefix, format)).build();

        } catch (Exception e) {
            return manageExceptionResponse(e);
        }
    }

    @Override
    public RecodedCodes retrieveRecodedCodes(String agencyID1, String resourceID1, String version1, String agencyID2, String resourceID2, String version2) {
        try {

            checkParameterNotWildcardRetrieveItemScheme(agencyID1, resourceID1, version1);
            checkParameterNotWildcardRetrieveItemScheme(agencyID2, resourceID2, version2);

            // Retrieve all codes of codelist
            CodelistVersionMetamac codelistVersionOrigin = retrieveCodelistPublished(agencyID1, resourceID1, version1);

            // Retrieve all codes of codelist
            CodelistVersionMetamac codelistVersionToMatch = retrieveCodelistPublished(agencyID2, resourceID2, version2);

            checkCodelistVersionWithSameVariable(codelistVersionOrigin, codelistVersionToMatch);

            List<RelatedCode> items = codesService.recodeCodes(ctx, codelistVersionOrigin, codelistVersionToMatch);

            return codesDo2RestMapper.toRecodedCodes(codelistVersionOrigin, codelistVersionToMatch, items);

        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public CodelistRestrictions findCodelistRestrictions(String agencyID, String resourceID, String version) {
        try {

            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            CodelistVersionMetamac codelistVersion = retrieveCodelistPublished(agencyID, resourceID, version);

            List<CodelistRestrictionVisualisation> restrictions = codesService.retrieveCodelistRestrictionVisualisationsByCodelist(ctx, codelistVersion.getMaintainableArtefact().getUrn());

            return codesDo2RestMapper.toCodeListRestrictions(restrictions, codelistVersion, agencyID, resourceID, version);

        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private Codes findCodesCommon(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, String order, String openness, String restriction,
            Boolean extended, Set<String> fieldsToShow) throws MetamacException {

        if (isFindByDefaultGeographicalGranularitiesCodelist(agencyID, resourceID, version, query, orderBy, limit, offset)) {
            String codelistUrn = configurationService.retrieveDefaultCodelistGeographicalGranularityUrn();
            String[] codelistUrnSplited = UrnUtils.splitUrnItemScheme(codelistUrn);
            agencyID = codelistUrnSplited[0];
            resourceID = codelistUrnSplited[1];
            version = codelistUrnSplited[2];
            query = null;
            orderBy = null;
            limit = null;
        }

        if (mustFindItemsInsteadRetrieveAllItemsOfItemScheme(agencyID, resourceID, version, query, orderBy, limit, offset)) {
            checkParameterEmpty(SrmRestConstants.PARAMETER_ORDER_ID, order);
            checkParameterEmpty(SrmRestConstants.PARAMETER_OPENNESS_ID, openness);
            checkParameterEmpty(SrmRestConstants.PARAMETER_RESTRICTION_ID, restriction);
            return findCodesInMultipleCodelists(agencyID, resourceID, version, query, orderBy, limit, offset, extended, fieldsToShow);
        } else {
            // Retrieve all codes of codelist
            CodelistVersionMetamac codelistVersion = retrieveCodelistPublished(agencyID, resourceID, version);

            String orderToUse = getOrderToUseToRetrieveCodelist(agencyID, resourceID, version, order, codelistVersion);
            String opennessToUse = getOpennessToUseToRetrieveCodelist(agencyID, resourceID, version, openness, codelistVersion);
            String restrictionToUse = getRestrictionToUseToRetrieveCodelist(agencyID, resourceID, version, restriction, codelistVersion);

            ItemMetamacResultSelection codeMetamacResultSelection = CodeMetamacResultSelection.API;
            codeMetamacResultSelection.setDescriptions(SrmRestInternalUtils.containsField(fieldsToShow, SrmRestConstants.FIELD_INCLUDE_DESCRIPTION));
            codeMetamacResultSelection.setAnnotations(SrmRestInternalUtils.containsField(fieldsToShow, SrmRestConstants.FIELD_INCLUDE_ANNOTATIONS));
            List<ItemResult> items = codesService.retrieveCodesByCodelistUrnOrderedInDepth(ctx, codelistVersion.getMaintainableArtefact().getUrn(), codeMetamacResultSelection, orderToUse,
                    opennessToUse, restrictionToUse);

            return codesDo2RestMapper.toCodes(items, codelistVersion, fieldsToShow, restriction, extended);
        }

    }

    private String getOpennessToUseToRetrieveCodelist(String agencyID, String resourceID, String version, String openness, CodelistVersionMetamac codelistVersion) throws MetamacException {
        String opennessToUse = null;
        if (openness == null) {
            opennessToUse = codelistVersion.getDefaultOpennessVisualisation().getNameableArtefact().getCode();
        } else {
            // check exist
            retrieveCodelistOpennessVisualisation(agencyID, resourceID, version, codelistVersion.getMaintainableArtefact().getUrn(), openness);
            opennessToUse = openness;
        }
        return opennessToUse;
    }

    private String getRestrictionToUseToRetrieveCodelist(String agencyID, String resourceID, String version, String restriction, CodelistVersionMetamac codelistVersion) throws MetamacException {
        String restrictionToUse = null;
        if (restriction != null) {
            // check exist
            retrieveCodelistRestrictionVisualisation(agencyID, resourceID, version, codelistVersion.getMaintainableArtefact().getUrn(), restriction);
            restrictionToUse = restriction;
        }
        return restrictionToUse;
    }

    private String getOrderToUseToRetrieveCodelist(String agencyID, String resourceID, String version, String order, CodelistVersionMetamac codelistVersion) throws MetamacException {
        String orderToUse = null;
        if (order == null) {
            orderToUse = codelistVersion.getDefaultOrderVisualisation().getNameableArtefact().getCode();
        } else {
            // check exist
            retrieveCodelistOrderVisualisation(agencyID, resourceID, version, codelistVersion.getMaintainableArtefact().getUrn(), order);
            orderToUse = order;
        }
        return orderToUse;
    }

    private Codes findCodesInMultipleCodelists(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset, Boolean extended,
            Set<String> fieldsToShow) throws MetamacException {
        // Find. Retrieve codes paginated
        SculptorCriteria sculptorCriteria = codesRest2DoMapper.getCodeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

        List<String> selectedLanguages = configurationService.retrieveLanguages();
        sculptorCriteria.setOrderByCaseCondition(CodeMetamac.class, CodeMetamacProperties.nameableArtefact().name().texts().locale(), selectedLanguages, "=", orderBy);

        PagedResult<CodeMetamac> entitiesPagedResult = findCodesCore(agencyID, resourceID, version, null, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

        // Transform
        return codesDo2RestMapper.toCodes(entitiesPagedResult, agencyID, resourceID, version, query, orderBy, sculptorCriteria.getLimit(), extended, fieldsToShow);
    }

    @Override
    public VariableFamily retrieveVariableFamilyById(String id) {
        try {
            // Find one
            PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> entitiesPagedResult = findVariableFamiliesCore(id, null, pagingParameterOneResult);
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.VARIABLE_FAMILY_NOT_FOUND, id);
                throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
            }

            // Transform
            org.siemac.metamac.srm.core.code.domain.VariableFamily variableFamilyEntity = entitiesPagedResult.getValues().get(0);
            return codesDo2RestMapper.toVariableFamily(variableFamilyEntity);

        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public VariableFamilies findVariableFamilies(String query, String orderBy, String limit, String offset) {
        try {
            // Retrieve variableFamilies by criteria
            SculptorCriteria sculptorCriteria = codesRest2DoMapper.getVariableFamilyCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Retrieve
            PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> entitiesPagedResult = findVariableFamiliesCore(null, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            return codesDo2RestMapper.toVariableFamilies(entitiesPagedResult, query, orderBy, sculptorCriteria.getLimit());
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Variables findVariablesByFamily(String familyID, String query, String orderBy, String limit, String offset) {
        try {
            // Retrieve variables by criteria
            SculptorCriteria sculptorCriteria = codesRest2DoMapper.getVariableCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Retrieve
            PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> entitiesPagedResult = findVariablesCore(null, familyID, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            return codesDo2RestMapper.toVariablesByFamily(familyID, entitiesPagedResult, query, orderBy, sculptorCriteria.getLimit());
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Variable retrieveVariableById(String id) {
        try {
            // Find one
            PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> entitiesPagedResult = findVariablesCore(id, null, null, pagingParameterOneResult);
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.VARIABLE_NOT_FOUND, id);
                throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
            }

            // Transform
            org.siemac.metamac.srm.core.code.domain.Variable variableEntity = entitiesPagedResult.getValues().get(0);
            return codesDo2RestMapper.toVariable(variableEntity);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Variables findVariables(String query, String orderBy, String limit, String offset) {
        try {
            // Retrieve variables by criteria
            SculptorCriteria sculptorCriteria = codesRest2DoMapper.getVariableCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Retrieve
            PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> entitiesPagedResult = findVariablesCore(null, null, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            return codesDo2RestMapper.toVariables(entitiesPagedResult, query, orderBy, sculptorCriteria.getLimit());
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public VariableElement retrieveVariableElementById(String variableID, String resourceID) {
        try {
            checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_VARIABLE_ID, variableID);
            checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_RESOURCE_ID, resourceID);

            // Find one
            PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement> entitiesPagedResult = findVariableElementsCore(variableID, resourceID, false, null, pagingParameterOneResult);
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.VARIABLE_ELEMENT_NOT_FOUND, resourceID, variableID);
                throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
            }

            // Transform
            org.siemac.metamac.srm.core.code.domain.VariableElement variableElementEntity = entitiesPagedResult.getValues().get(0);
            return codesDo2RestMapper.toVariableElement(variableElementEntity);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @CacheableMethod(VariableElementCacheableResource.class)
    @Override
    public VariableElements findVariableElements(String variableID, String query, String orderBy, String limit, String offset, String fields) {
        try {
            Set<String> fieldsToShow = SrmRestInternalUtils.parseFieldsParameter(fields);

            // Retrieve by criteria
            SculptorCriteria sculptorCriteria = codesRest2DoMapper.getVariableElementCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            if (mustFindVariableElementsInsteadRetrieveAllVariableElementsOfVariable(variableID, null, query, sculptorCriteria.getConditions(), orderBy, limit, offset)) {
                // Find. Retrieve variable elements paginated
                PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement> entitiesPagedResult = findVariableElementsCore(variableID, null, false, sculptorCriteria.getConditions(),
                        sculptorCriteria.getPagingParameter());

                // Transform
                return codesDo2RestMapper.toVariableElements(entitiesPagedResult, variableID, query, orderBy, sculptorCriteria.getLimit(), fieldsToShow);
            } else {
                // Retrieve all variable elements of variable, without pagination
                String variableUrn = GeneratorUrnUtils.generateVariableUrn(variableID);
                List<String> variableElementsCodes = null;
                if (query != null) {
                    variableElementsCodes = extractCodesFromQuery(sculptorCriteria.getConditions());
                }
                List<String> geographicalGranularityUrns = null;
                if (query != null) {
                    geographicalGranularityUrns = extractGeographicalGranularityUrnsFromQuery(sculptorCriteria.getConditions());
                }
                VariableElementResultSelection selection = new VariableElementResultSelection(); // do not retrieve nothing extra metadata as default
                List<VariableElementResult> entities;
                try {
                    entities = codesService.findVariableElementsByVariableEfficiently(ctx, variableUrn, variableElementsCodes, selection, geographicalGranularityUrns);
                } catch (MetamacException metamacException) {
                    org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.VARIABLE_NOT_FOUND, variableID);
                    throw new RestException(exception, Status.NOT_FOUND);
                }

                // Transform
                return codesDo2RestMapper.toVariableElements(entities, variableID, query, fieldsToShow);
            }
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Response findVariableElementsGeoInfoJson(String variableID, String resourceID, String query, String orderBy, String limit, String offset, String fields) {
        String target = (String) findVariableElementsGeoInfoCommon(variableID, resourceID, query, orderBy, limit, offset, fields, MediaType.APPLICATION_JSON);
        return Response.status(Status.OK).entity(target).build();
    }

    @Override
    public VariableElementsGeoInfo findVariableElementsGeoInfoXml(String variableID, String resourceID, String query, String orderBy, String limit, String offset, String fields) {
        return (VariableElementsGeoInfo) findVariableElementsGeoInfoCommon(variableID, resourceID, query, orderBy, limit, offset, fields, MediaType.APPLICATION_XML);
    }

    @Override
    public CodelistFamily retrieveCodelistFamilyById(String id) {
        try {
            // Find one
            PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily> entitiesPagedResult = findCodelistFamiliesCore(id, null, pagingParameterOneResult);
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CODELIST_FAMILY_NOT_FOUND, id);
                throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
            }

            // Transform
            org.siemac.metamac.srm.core.code.domain.CodelistFamily codelistFamilyEntity = entitiesPagedResult.getValues().get(0);
            return codesDo2RestMapper.toCodelistFamily(codelistFamilyEntity);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public CodelistFamilies findCodelistFamilies(String query, String orderBy, String limit, String offset) {
        try {
            // Retrieve codelistFamilies by criteria
            SculptorCriteria sculptorCriteria = codesRest2DoMapper.getCodelistFamilyCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            List<String> selectedLanguages = configurationService.retrieveLanguages();
            sculptorCriteria.setOrderByCaseCondition(org.siemac.metamac.srm.core.code.domain.CodelistFamily.class, CodelistFamilyProperties.nameableArtefact().name().texts().locale(),
                    selectedLanguages, "=", orderBy);
            // Retrieve
            PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily> entitiesPagedResult = findCodelistFamiliesCore(null, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            return codesDo2RestMapper.toCodelistFamilies(entitiesPagedResult, query, orderBy, sculptorCriteria.getLimit());
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Code retrieveCode(String agencyID, String resourceID, String version, String codeID, String fields) {
        try {
            checkParameterNotWildcardRetrieveItem(agencyID, resourceID, version, codeID, SrmRestConstants.PARAMETER_CODE_ID);
            Set<String> fieldsToShow = SrmRestInternalUtils.parseFieldsParameter(fields);

            // Find one
            PagedResult<CodeMetamac> entitiesPagedResult = findCodesCore(agencyID, resourceID, version, codeID, null, pagingParameterOneResult);
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CODE_NOT_FOUND, codeID, version, resourceID, agencyID);
                throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
            }

            // Transform
            return codesDo2RestMapper.toCode(entitiesPagedResult.getValues().get(0), fieldsToShow);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public DataStructures findDataStructures(String query, String orderBy, String limit, String offset) {
        return findDataStructures(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public DataStructures findDataStructures(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findDataStructures(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public DataStructures findDataStructures(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findDataStructures(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public DataStructure retrieveDataStructure(String agencyID, String resourceID, String version, String fields) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            Set<String> fieldsToShow = SrmRestInternalUtils.parseFieldsParameter(fields);

            // Find one
            PagedResult<DataStructureDefinitionVersionMetamac> entitiesPagedResult = findDataStructuresCore(agencyID, resourceID, version, null, pagingParameterOneResult);
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.DATA_STRUCTURE_NOT_FOUND, resourceID, version, agencyID);
                throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
            }

            // Transform
            return dataStructuresDo2RestMapperV10.toDataStructure(entitiesPagedResult.getValues().get(0), fieldsToShow);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public ContentConstraints findContentConstraints(String query, String orderBy, String limit, String offset) {
        return findContentConstraints(null, null, null, query, orderBy, limit, offset);
    }

    @Override
    public ContentConstraints findContentConstraints(String agencyID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID);
        return findContentConstraints(agencyID, null, null, query, orderBy, limit, offset);
    }

    @Override
    public ContentConstraints findContentConstraints(String agencyID, String resourceID, String query, String orderBy, String limit, String offset) {
        checkParameterNotWildcardFindItemSchemes(agencyID, resourceID);
        return findContentConstraints(agencyID, resourceID, null, query, orderBy, limit, offset);
    }

    @Override
    public ContentConstraint retrieveContentConstraint(String agencyID, String resourceID, String version) {
        try {
            checkParameterNotWildcardRetrieveItemScheme(agencyID, resourceID, version);

            // Find one
            PagedResult<com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint> entitiesPagedResult = findContentConstraintsCore(agencyID, resourceID, version, null,
                    pagingParameterOneResult);
            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CONTENT_CONSTRAINT_NOT_FOUND, resourceID, version, agencyID);
                throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
            }

            // Transform
            return contentConstraintsDo2RestMapper.toContentConstraint(entitiesPagedResult.getValues().get(0));
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public RegionReference retrieveRegionForContentConstraint(String agencyID, String resourceID, String version, String regionCode) {
        try {
            String contentConstraintUrn = GeneratorUrnUtils.generateSdmxContentConstraintUrn(new String[]{agencyID}, resourceID, version);

            // Retrieve
            RegionValue regionValue = constraintsService.findRegionValueByUrn(ctx, contentConstraintUrn, regionCode);

            if (regionValue == null) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CONTENT_CONSTRAINT_REGION_NOT_FOUND, regionCode,
                        contentConstraintUrn);
                throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
            }

            // If not draft is allowed and isn't final, then is an error.
            if (BooleanUtils.isNotTrue(regionValue.getContentConstraint().getMaintainableArtefact().getFinalLogicClient())) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CONTENT_CONSTRAINT_UNPUBLISHED, regionCode,
                        contentConstraintUrn);
                throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
            }

            // Transform
            return contentConstraintsDo2RestMapper.toRegionReference(contentConstraintUrn, regionValue);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public CategoryElements findCategoryElements(String query, String orderBy, String limit, String offset) {
        try {
            // Retrieve category elements by criteria
            SculptorCriteria sculptorCriteria = categoriesRest2DoMapper.getCategoryElementCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            // Retrieve
            PagedResult<org.siemac.metamac.srm.core.category.domain.CategoryElement> entitiesPagedResult = findCategoryElementsCore(null, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            return categoriesDo2RestMapper.toCategoryElements(entitiesPagedResult, query, orderBy, sculptorCriteria.getLimit());
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private PagedResult<org.siemac.metamac.srm.core.category.domain.CategoryElement> findCategoryElementsCore(String categoryElementCode, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        List<ConditionalCriteria> conditionalCriteria = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(conditionalCriteriaQuery)) {
            conditionalCriteria.addAll(conditionalCriteriaQuery);
        } else {
            // init
            conditionalCriteria.addAll(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.category.domain.CategoryElement.class).distinctRoot().build());
        }

        if (categoryElementCode != null) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.category.domain.CategoryElement.class)
                    .withProperty(CategoryElementProperties.identifiableArtefact().code()).eq(categoryElementCode).buildSingle());
        }

        // Find
        return categoriesService.findCategoryElementsByCondition(ctx, conditionalCriteria, pagingParameter);
    }

    @Override
    public CategoryElementResource retrieveCategoryElementById(String resourceID) {
        try {
            checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_RESOURCE_ID, resourceID);

            // Retrieve
            PagedResult<org.siemac.metamac.srm.core.category.domain.CategoryElement> entitiesPagedResult = findCategoryElementsCore(resourceID, null, pagingParameterOneResult);

            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CATEGORY_ELEMENT_NOT_FOUND, resourceID);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            CategoryElement categoryElementEntity = entitiesPagedResult.getValues().get(0);
            return categoriesDo2RestMapper.toCategoryElementResource(categoryElementEntity);
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    @Override
    public Response retrieveCategoryElementLogo(String resourceID) {
        try {
            checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_RESOURCE_ID, resourceID);

            // Retrieve
            PagedResult<org.siemac.metamac.srm.core.category.domain.CategoryElement> entitiesPagedResult = findCategoryElementsCore(resourceID, null, pagingParameterOneResult);

            if (entitiesPagedResult.getValues().size() != 1) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CATEGORY_ELEMENT_NOT_FOUND, resourceID);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Transform
            CategoryElement categoryElementEntity = entitiesPagedResult.getValues().get(0);

            //
            if (categoryElementEntity.getLogo() == null || categoryElementEntity.getLogo().getFile() == null) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CATEGORY_ELEMENT_LOGO_NOT_FOUND, resourceID);
                throw new RestException(exception, Status.NOT_FOUND);
            }

            // Return
            ResourcesFormatOperation resourcesFormatOperation = new ResourcesFormatOperation();
            ResponseBuilder response = Response.ok(new ByteArrayInputStream(categoryElementEntity.getLogo().getFile().getFileData())).header("Content-Disposition",
                    getContentDisposition(categoryElementEntity.getLogo().getFileName(), categoryElementEntity.getLogo().getExtension()));
            if (ResourcesFormat.SVG.name().equals(categoryElementEntity.getLogo().getExtension().toUpperCase())) {
                response.header("Content-Type", resourcesFormatOperation.getMimeType(ResourcesFormat.SVG.name()));
            }
            return response.build();

        } catch (Exception e) {
            return manageExceptionResponse(e);
        }
    }

    private ConceptSchemes findConceptSchemes(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = conceptsRest2DoMapper.getConceptSchemeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            List<String> selectedLanguages = configurationService.retrieveLanguages();
            sculptorCriteria.setOrderByCaseCondition(ConceptSchemeVersionMetamac.class, ConceptSchemeVersionMetamacProperties.maintainableArtefact().name().texts().locale(), selectedLanguages, "=",
                    orderBy);

            // Find
            PagedResult<ConceptSchemeVersionMetamac> entitiesPagedResult = findConceptSchemesCore(agencyID, resourceID, version, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            return conceptsDo2RestMapper.toConceptSchemes(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private ConceptSchemeVersionMetamac retrieveConceptSchemePublished(String agencyID, String resourceID, String version) throws MetamacException {
        PagedResult<ConceptSchemeVersionMetamac> entitiesPagedResult = findConceptSchemesCore(agencyID, resourceID, version, null, pagingParameterOneResult);
        if (entitiesPagedResult.getValues().size() != 1) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CONCEPT_SCHEME_NOT_FOUND, resourceID, version, agencyID);
            throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
        }
        return entitiesPagedResult.getValues().get(0);
    }

    private PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesCore(String agencyID, String resourceID, String version, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find item schemes by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItemSchemes(agencyID, resourceID, version, conditionalCriteriaQuery,
                ConceptSchemeVersionMetamac.class);

        // Find
        return conceptsService.findConceptSchemesByCondition(ctx, conditionalCriteria, pagingParameter);
    }

    private PagedResult<ConceptMetamac> findConceptsCore(String agencyID, String resourceID, String version, String conceptID, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find items by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItems(agencyID, resourceID, version, conceptID,
                ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact(), ConceptMetamacProperties.nameableArtefact(), conditionalCriteriaQuery, ConceptMetamac.class);

        // Find
        return conceptsService.findConceptsByCondition(ctx, conditionalCriteria, pagingParameter);
    }

    private CategorySchemes findCategorySchemes(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = categoriesRest2DoMapper.getCategorySchemeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            List<String> selectedLanguages = configurationService.retrieveLanguages();
            sculptorCriteria.setOrderByCaseCondition(CategorySchemeVersionMetamac.class, CategorySchemeVersionProperties.maintainableArtefact().name().texts().locale(), selectedLanguages, "=",
                    orderBy);
            // Find
            PagedResult<CategorySchemeVersionMetamac> entitiesPagedResult = findCategorySchemesCore(agencyID, resourceID, version, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            return categoriesDo2RestMapper.toCategorySchemes(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private Categorisations findCategorisations(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = categoriesRest2DoMapper.getCategorisationCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            List<String> selectedLanguages = configurationService.retrieveLanguages();
            sculptorCriteria.setOrderByCaseCondition(com.arte.statistic.sdmx.srm.core.category.domain.Categorisation.class, CategorisationProperties.maintainableArtefact().name().texts().locale(),
                    selectedLanguages, "=", orderBy);

            // Find
            PagedResult<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation> entitiesPagedResult = findCategorisationsCore(agencyID, resourceID, version, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            return categoriesDo2RestMapper.toCategorisations(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private CategorySchemeVersionMetamac retrieveCategorySchemePublished(String agencyID, String resourceID, String version) throws MetamacException {

        SculptorCriteria sculptorCriteria = categoriesRest2DoMapper.getCategorisationCriteriaMapper().restCriteriaToSculptorCriteria(null, null, null, null);

        List<String> selectedLanguages = configurationService.retrieveLanguages();
        sculptorCriteria.setOrderByCaseCondition(CategorySchemeVersionMetamac.class, CategorySchemeVersionMetamacProperties.maintainableArtefact().name().texts().locale(), selectedLanguages, "=",
                null);

        PagedResult<CategorySchemeVersionMetamac> entitiesPagedResult = findCategorySchemesCore(agencyID, resourceID, version, null, pagingParameterOneResult);
        if (entitiesPagedResult.getValues().size() != 1) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CATEGORY_SCHEME_NOT_FOUND, resourceID, version, agencyID);
            throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
        }
        return entitiesPagedResult.getValues().get(0);
    }

    private PagedResult<CategorySchemeVersionMetamac> findCategorySchemesCore(String agencyID, String resourceID, String version, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find item schemes by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItemSchemes(agencyID, resourceID, version, conditionalCriteriaQuery,
                CategorySchemeVersionMetamac.class);

        // Find
        return categoriesService.findCategorySchemesByCondition(ctx, conditionalCriteria, pagingParameter);
    }

    private PagedResult<CategoryMetamac> findCategoriesCore(String agencyID, String resourceID, String version, String categoryID, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find items by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItems(agencyID, resourceID, version, categoryID,
                CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact(), CategoryMetamacProperties.nameableArtefact(), conditionalCriteriaQuery, CategoryMetamac.class);

        // Find
        return categoriesService.findCategoriesByCondition(ctx, conditionalCriteria, pagingParameter);
    }

    private PagedResult<com.arte.statistic.sdmx.srm.core.category.domain.Categorisation> findCategorisationsCore(String agencyID, String resourceID, String version,
            List<ConditionalCriteria> conditionalCriteriaQuery, PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItemSchemes(agencyID, resourceID, version, conditionalCriteriaQuery, Categorisation.class);

        // Find
        return categoriesService.findCategorisationsByCondition(ctx, conditionalCriteria, pagingParameter);
    }

    private OrganisationSchemes findOrganisationSchemes(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationSchemeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            List<String> selectedLanguages = configurationService.retrieveLanguages();
            sculptorCriteria.setOrderByCaseCondition(OrganisationSchemeVersionMetamac.class, OrganisationSchemeVersionMetamacProperties.maintainableArtefact().name().texts().locale(),
                    selectedLanguages, "=", orderBy);

            // Find
            PagedResult<OrganisationSchemeVersionMetamac> entitiesPagedResult = findOrganisationSchemesCore(null, agencyID, resourceID, version, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            return organisationsDo2RestMapper.toOrganisationSchemes(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private OrganisationSchemeVersionMetamac retrieveOrganisationSchemePublished(String agencyID, String resourceID, String version) throws MetamacException {
        return retrieveOrganisationSchemePublished(null, agencyID, resourceID, version, RestServiceExceptionType.ORGANISATION_SCHEME_NOT_FOUND);
    }
    private OrganisationSchemeVersionMetamac retrieveAgencySchemePublished(String agencyID, String resourceID, String version) throws MetamacException {
        return retrieveOrganisationSchemePublished(OrganisationSchemeTypeEnum.AGENCY_SCHEME, agencyID, resourceID, version, RestServiceExceptionType.AGENCY_SCHEME_NOT_FOUND);
    }
    private OrganisationSchemeVersionMetamac retrieveDataConsumerSchemePublished(String agencyID, String resourceID, String version) throws MetamacException {
        return retrieveOrganisationSchemePublished(OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME, agencyID, resourceID, version, RestServiceExceptionType.DATA_CONSUMER_SCHEME_NOT_FOUND);
    }
    private OrganisationSchemeVersionMetamac retrieveDataProviderSchemePublished(String agencyID, String resourceID, String version) throws MetamacException {
        return retrieveOrganisationSchemePublished(OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME, agencyID, resourceID, version, RestServiceExceptionType.DATA_PROVIDER_SCHEME_NOT_FOUND);
    }
    private OrganisationSchemeVersionMetamac retrieveOrganisationUnitSchemePublished(String agencyID, String resourceID, String version) throws MetamacException {
        return retrieveOrganisationSchemePublished(OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME, agencyID, resourceID, version, RestServiceExceptionType.ORGANISATION_UNIT_SCHEME_NOT_FOUND);
    }
    private OrganisationSchemeVersionMetamac retrieveOrganisationSchemePublished(OrganisationSchemeTypeEnum type, String agencyID, String resourceID, String version,
            RestCommonServiceExceptionType exceptionType) throws MetamacException {
        PagedResult<OrganisationSchemeVersionMetamac> entitiesPagedResult = findOrganisationSchemesCore(type, agencyID, resourceID, version, null, pagingParameterOneResult);
        if (entitiesPagedResult.getValues().size() != 1) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(exceptionType, resourceID, version, agencyID);
            throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
        }
        return entitiesPagedResult.getValues().get(0);
    }

    private PagedResult<OrganisationSchemeVersionMetamac> findOrganisationSchemesCore(OrganisationSchemeTypeEnum type, String agencyID, String resourceID, String version,
            List<ConditionalCriteria> conditionalCriteriaQuery, PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find item schemes by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItemSchemes(agencyID, resourceID, version, conditionalCriteriaQuery,
                OrganisationSchemeVersionMetamac.class);
        if (type != null) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(OrganisationSchemeVersionMetamac.class).withProperty(OrganisationSchemeVersionMetamacProperties.organisationSchemeType())
                    .eq(type).buildSingle());
        }

        // Find
        return organisationsService.findOrganisationSchemesByCondition(ctx, conditionalCriteria, pagingParameter);
    }

    private AgencySchemes findAgencySchemes(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationSchemeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            List<String> selectedLanguages = configurationService.retrieveLanguages();
            sculptorCriteria.setOrderByCaseCondition(OrganisationSchemeVersionMetamac.class, OrganisationSchemeVersionMetamacProperties.maintainableArtefact().name().texts().locale(),
                    selectedLanguages, "=", orderBy);

            // Find
            PagedResult<OrganisationSchemeVersionMetamac> entitiesPagedResult = findOrganisationSchemesCore(OrganisationSchemeTypeEnum.AGENCY_SCHEME, agencyID, resourceID, version,
                    sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            return organisationsDo2RestMapper.toAgencySchemes(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private DataProviderSchemes findDataProviderSchemes(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationSchemeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            List<String> selectedLanguages = configurationService.retrieveLanguages();
            sculptorCriteria.setOrderByCaseCondition(OrganisationSchemeVersionMetamac.class, OrganisationSchemeVersionMetamacProperties.maintainableArtefact().name().texts().locale(),
                    selectedLanguages, "=", orderBy);

            // Find
            PagedResult<OrganisationSchemeVersionMetamac> entitiesPagedResult = findOrganisationSchemesCore(OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME, agencyID, resourceID, version,
                    sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            return organisationsDo2RestMapper.toDataProviderSchemes(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private DataConsumerSchemes findDataConsumerSchemes(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationSchemeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            List<String> selectedLanguages = configurationService.retrieveLanguages();
            sculptorCriteria.setOrderByCaseCondition(OrganisationSchemeVersionMetamac.class, OrganisationSchemeVersionMetamacProperties.maintainableArtefact().name().texts().locale(),
                    selectedLanguages, "=", orderBy);

            // Find
            PagedResult<OrganisationSchemeVersionMetamac> entitiesPagedResult = findOrganisationSchemesCore(OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME, agencyID, resourceID, version,
                    sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            return organisationsDo2RestMapper.toDataConsumerSchemes(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private OrganisationUnitSchemes findOrganisationUnitSchemes(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = organisationsRest2DoMapper.getOrganisationSchemeCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            List<String> selectedLanguages = configurationService.retrieveLanguages();
            sculptorCriteria.setOrderByCaseCondition(OrganisationSchemeVersionMetamac.class, OrganisationSchemeVersionMetamacProperties.maintainableArtefact().name().texts().locale(),
                    selectedLanguages, "=", orderBy);

            // Find
            PagedResult<OrganisationSchemeVersionMetamac> entitiesPagedResult = findOrganisationSchemesCore(OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME, agencyID, resourceID, version,
                    sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            return organisationsDo2RestMapper.toOrganisationUnitSchemes(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private Codelists findCodelists(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = codesRest2DoMapper.getCodelistCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            List<String> selectedLanguages = configurationService.retrieveLanguages();
            sculptorCriteria.setOrderByCaseCondition(CodelistVersionMetamac.class, CodelistVersionMetamacProperties.maintainableArtefact().name().texts().locale(), selectedLanguages, "=", orderBy);

            // Find
            PagedResult<CodelistVersionMetamac> entitiesPagedResult = findCodelistsCore(agencyID, resourceID, version, sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            return codesDo2RestMapper.toCodelists(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private CodelistVersionMetamac retrieveCodelistPublished(String agencyID, String resourceID, String version) throws MetamacException {
        PagedResult<CodelistVersionMetamac> entitiesPagedResult = findCodelistsCore(agencyID, resourceID, version, null, pagingParameterOneResult);
        if (entitiesPagedResult.getValues().size() != 1) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CODELIST_NOT_FOUND, resourceID, version, agencyID);
            throw new RestException(exception, Status.NOT_FOUND, new Throwable(exception.getMessage()));
        }
        return entitiesPagedResult.getValues().get(0);
    }

    private CodelistOrderVisualisation retrieveCodelistOrderVisualisation(String agencyID, String resourceID, String version, String codelistUrn, String order) throws MetamacException {
        try {
            return codesService.retrieveCodelistOrderVisualisationByCode(ctx, codelistUrn, order);
        } catch (MetamacException e) {
            if (e.getExceptionItems().size() == 1 && ServiceExceptionType.CODELIST_ORDER_VISUALISATION_NOT_FOUND.getCode().equals(e.getExceptionItems().get(0).getCode())) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CODELIST_ORDER_CONFIGURATION_NOT_FOUND, order, resourceID,
                        version, agencyID);
                throw new RestException(exception, Status.NOT_FOUND, e);
            } else {
                throw e;
            }
        }
    }

    private CodelistOpennessVisualisation retrieveCodelistOpennessVisualisation(String agencyID, String resourceID, String version, String codelistUrn, String openness) throws MetamacException {
        try {
            return codesService.retrieveCodelistOpennessVisualisationByCode(ctx, codelistUrn, openness);
        } catch (MetamacException e) {
            if (e.getExceptionItems().size() == 1 && ServiceExceptionType.CODELIST_OPENNESS_VISUALISATION_NOT_FOUND.getCode().equals(e.getExceptionItems().get(0).getCode())) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CODELIST_OPENNESS_CONFIGURATION_NOT_FOUND, openness,
                        resourceID, version, agencyID);
                throw new RestException(exception, Status.NOT_FOUND, e);
            } else {
                throw e;
            }
        }
    }

    private CodelistRestrictionVisualisation retrieveCodelistRestrictionVisualisation(String agencyID, String resourceID, String version, String codelistUrn, String restriction)
            throws MetamacException {
        try {
            return codesService.retrieveCodelistRestrictionVisualisationByCode(ctx, codelistUrn, restriction);
        } catch (MetamacException e) {
            if (e.getExceptionItems().size() == 1 && ServiceExceptionType.CODELIST_RESTRICTION_VISUALISATION_NOT_FOUND.getCode().equals(e.getExceptionItems().get(0).getCode())) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CODELIST_RESTRICTION_CONFIGURATION_NOT_FOUND, restriction,
                        resourceID, version, agencyID);
                throw new RestException(exception, Status.NOT_FOUND, e);
            } else {
                throw e;
            }
        }
    }

    private PagedResult<CodelistVersionMetamac> findCodelistsCore(String agencyID, String resourceID, String version, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find item schemes by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItemSchemes(agencyID, resourceID, version, conditionalCriteriaQuery, CodelistVersionMetamac.class);

        // Only Codelists with access-type = PUBLIC, are public. But in LLCC-75, the codelist that are access-type = RESTRICTED never were marked as publicLogic=true,
        // for this is unnecessary to check this condition.
        conditionalCriteria
                .add(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.accessType()).eq(AccessTypeEnum.PUBLIC).buildSingle());

        // Find
        return codesService.findCodelistsByCondition(ctx, conditionalCriteria, pagingParameter);
    }

    private PagedResult<OrganisationMetamac> findOrganisationsCore(OrganisationTypeEnum type, String agencyID, String resourceID, String version, String organisationID,
            List<ConditionalCriteria> conditionalCriteriaQuery, PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find items by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItems(agencyID, resourceID, version, organisationID,
                OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact(), OrganisationMetamacProperties.nameableArtefact(), conditionalCriteriaQuery, OrganisationMetamac.class);
        if (type != null) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(OrganisationMetamac.class).withProperty(OrganisationMetamacProperties.organisationType()).eq(type).buildSingle());
        }

        // Find
        return organisationsService.findOrganisationsByCondition(ctx, conditionalCriteria, pagingParameter);
    }

    private PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement> findVariableElementsCore(String variableID, String resourceID, boolean onlyGeographical,
            List<ConditionalCriteria> conditionalCriteriaQuery, PagingParameter pagingParameter) throws MetamacException {

        List<ConditionalCriteria> conditionalCriteria = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(conditionalCriteriaQuery)) {
            conditionalCriteria.addAll(conditionalCriteriaQuery);
        } else {
            // init
            conditionalCriteria.addAll(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.VariableElement.class).distinctRoot().build());
        }
        if (variableID != null && !SrmRestConstants.WILDCARD_ALL.equals(variableID)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.VariableElement.class)
                    .withProperty(VariableElementProperties.variable().nameableArtefact().code()).eq(variableID).buildSingle());
        }
        if (onlyGeographical) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.VariableElement.class).withProperty(VariableElementProperties.variable().type())
                    .eq(VariableTypeEnum.GEOGRAPHICAL).buildSingle());
        }
        if (resourceID != null && !SrmRestConstants.WILDCARD_ALL.equals(resourceID)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.VariableElement.class)
                    .withProperty(VariableElementProperties.identifiableArtefact().code()).eq(resourceID).buildSingle());
        }
        // Find
        return codesService.findVariableElementsByCondition(ctx, conditionalCriteria, pagingParameter);
    }

    private ContentConstraints findContentConstraints(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = contentConstraintsRest2DoMapper.getContentConstraintCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            List<String> selectedLanguages = configurationService.retrieveLanguages();
            sculptorCriteria.setOrderByCaseCondition(com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint.class,
                    ContentConstraintProperties.maintainableArtefact().name().texts().locale(), selectedLanguages, "=", orderBy);

            // Find
            PagedResult<com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint> entitiesPagedResult = findContentConstraintsCore(agencyID, resourceID, version,
                    sculptorCriteria.getConditions(), sculptorCriteria.getPagingParameter());

            // Transform
            return contentConstraintsDo2RestMapper.toContentConstraints(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private DataStructures findDataStructures(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        try {
            SculptorCriteria sculptorCriteria = dataStructuresRest2DoMapper.getDataStructureDefinitionCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            List<String> selectedLanguages = configurationService.retrieveLanguages();
            sculptorCriteria.setOrderByCaseCondition(DataStructureDefinitionVersionMetamac.class, DataStructureDefinitionVersionProperties.maintainableArtefact().name().texts().locale(),
                    selectedLanguages, "=", orderBy);

            // Find
            PagedResult<DataStructureDefinitionVersionMetamac> entitiesPagedResult = findDataStructuresCore(agencyID, resourceID, version, sculptorCriteria.getConditions(),
                    sculptorCriteria.getPagingParameter());

            // Transform
            return dataStructuresDo2RestMapperV10.toDataStructures(entitiesPagedResult, agencyID, resourceID, query, orderBy, sculptorCriteria.getLimit());
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    private PagedResult<com.arte.statistic.sdmx.srm.core.constraint.domain.ContentConstraint> findContentConstraintsCore(String agencyID, String resourceID, String version,
            List<ConditionalCriteria> conditionalCriteriaQuery, PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaContentConstraints(agencyID, resourceID, version, conditionalCriteriaQuery,
                ContentConstraint.class);

        // Find
        return constraintsService.findContentConstraintsByCondition(ctx, conditionalCriteria, pagingParameter);

    }

    private PagedResult<DataStructureDefinitionVersionMetamac> findDataStructuresCore(String agencyID, String resourceID, String version, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaStructures(agencyID, resourceID, version, conditionalCriteriaQuery,
                DataStructureDefinitionVersionMetamac.class);

        // Find
        return dataStructureDefinitionService.findDataStructureDefinitionsByCondition(ctx, conditionalCriteria, pagingParameter);
    }

    private PagedResult<org.siemac.metamac.srm.core.code.domain.VariableFamily> findVariableFamiliesCore(String variableFamilyID, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        List<ConditionalCriteria> conditionalCriteria = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(conditionalCriteriaQuery)) {
            conditionalCriteria.addAll(conditionalCriteriaQuery);
        } else {
            // init
            conditionalCriteria.addAll(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.VariableFamily.class).distinctRoot().build());
        }
        if (variableFamilyID != null) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.VariableFamily.class)
                    .withProperty(VariableFamilyProperties.nameableArtefact().code()).eq(variableFamilyID).buildSingle());
        }
        // Find
        return codesService.findVariableFamiliesByCondition(ctx, conditionalCriteria, pagingParameter);
    }

    private PagedResult<org.siemac.metamac.srm.core.code.domain.Variable> findVariablesCore(String variableID, String familyID, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        List<ConditionalCriteria> conditionalCriteria = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(conditionalCriteriaQuery)) {
            conditionalCriteria.addAll(conditionalCriteriaQuery);
        } else {
            // init
            conditionalCriteria.addAll(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.Variable.class).distinctRoot().build());
        }
        if (variableID != null) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.Variable.class).withProperty(VariableProperties.nameableArtefact().code())
                    .eq(variableID).buildSingle());
        }
        if (familyID != null && !SrmRestConstants.WILDCARD_ALL.equals(familyID)) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.Variable.class).withProperty(VariableProperties.families().nameableArtefact().code())
                    .eq(familyID).buildSingle());
        }
        // Find
        return codesService.findVariablesByCondition(ctx, conditionalCriteria, pagingParameter);
    }

    private PagedResult<CodeMetamac> findCodesCore(String agencyID, String resourceID, String version, String codeID, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        // Criteria to find items by criteria
        List<ConditionalCriteria> conditionalCriteria = SrmRestInternalUtils.buildConditionalCriteriaItems(agencyID, resourceID, version, codeID,
                CodeMetamacProperties.itemSchemeVersion().maintainableArtefact(), CodeMetamacProperties.nameableArtefact(), conditionalCriteriaQuery, CodeMetamac.class);

        // Only Codelists with access-type = PUBLIC, are public. But in LLCC-75, the codelist that are access-type = RESTRICTED never were marked as publicLogic=true,
        // for this is unnecessary to check this condition.
        // For the above comment the following conditions is irrelevant:
        // Only codelists with access == public
        conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class)
                .withProperty(new LeafProperty<CodeMetamac>(CodeMetamacProperties.itemSchemeVersion().getName(), CodelistVersionMetamacProperties.accessType().getName(), false, CodeMetamac.class))
                .eq(AccessTypeEnum.PUBLIC).buildSingle());

        // Find
        return codesService.findCodesByCondition(ctx, conditionalCriteria, pagingParameter);
    }

    private PagedResult<org.siemac.metamac.srm.core.code.domain.CodelistFamily> findCodelistFamiliesCore(String codelistFamilyID, List<ConditionalCriteria> conditionalCriteriaQuery,
            PagingParameter pagingParameter) throws MetamacException {

        List<ConditionalCriteria> conditionalCriteria = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(conditionalCriteriaQuery)) {
            conditionalCriteria.addAll(conditionalCriteriaQuery);
        } else {
            // init
            conditionalCriteria.addAll(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.CodelistFamily.class).distinctRoot().build());
        }
        if (codelistFamilyID != null) {
            conditionalCriteria.add(ConditionalCriteriaBuilder.criteriaFor(org.siemac.metamac.srm.core.code.domain.CodelistFamily.class)
                    .withProperty(CodelistFamilyProperties.nameableArtefact().code()).eq(codelistFamilyID).buildSingle());
        }
        // Find
        return codesService.findCodelistFamiliesByCondition(ctx, conditionalCriteria, pagingParameter);
    }

    private Object findVariableElementsGeoInfoCommon(String variableID, String resourceID, String query, String orderBy, String limit, String offset, String fields, String mediaType) {
        try {
            // Build criteria and selection
            SculptorCriteria sculptorCriteria = codesRest2DoMapper.getVariableElementCriteriaMapper().restCriteriaToSculptorCriteria(query, orderBy, limit, offset);

            VariableElementResultSelection selection = new VariableElementResultSelection();
            selection.setReturnOnlyGeographicalVariableElements(true);
            selection.setLongitudeLatitude(!hasField(fields, SrmRestConstants.FIELD_EXCLUDE_LONGITUDE_LATITUDE));
            selection.setGeographicalGranularity(!hasField(fields, SrmRestConstants.FIELD_EXCLUDE_GEOGRAPHICAL_GRANULARITY));
            if (MediaType.APPLICATION_JSON.equals(mediaType)) {
                selection.setShapeGeojson(!hasField(fields, SrmRestConstants.FIELD_EXCLUDE_GEOMETRY));
            }
            if (MediaType.APPLICATION_XML.equals(mediaType)) {
                selection.setShapeWkt(!hasField(fields, SrmRestConstants.FIELD_EXCLUDE_GEOMETRY));
            }

            Object target = null;
            DateTime lastUpdatedDateVariableElementsGeoInfo = retrieveLastUpdatedDateVariableElementsGeographicalInformation();
            if (mustFindVariableElementsInsteadRetrieveAllVariableElementsOfVariable(variableID, resourceID, query, sculptorCriteria.getConditions(), orderBy, limit, offset)) {
                // Find. Retrieve variable elements paginated
                PagedResult<org.siemac.metamac.srm.core.code.domain.VariableElement> entitiesPagedResult = findVariableElementsCore(variableID, resourceID, true, sculptorCriteria.getConditions(),
                        sculptorCriteria.getPagingParameter());

                // Transform
                if (MediaType.APPLICATION_JSON.equals(mediaType)) {
                    target = codesDo2RestMapper.toVariableElementsGeoJson(entitiesPagedResult, selection, lastUpdatedDateVariableElementsGeoInfo);
                } else if (MediaType.APPLICATION_XML.equals(mediaType)) {
                    target = codesDo2RestMapper.toVariableElementsGeoXml(entitiesPagedResult, selection, lastUpdatedDateVariableElementsGeoInfo);
                }
            } else {
                // Retrieve all variable elements of variable, without pagination
                String variableUrn = GeneratorUrnUtils.generateVariableUrn(variableID);
                List<String> variableElementsCodes = null;
                if (query != null) {
                    variableElementsCodes = extractCodesFromQuery(sculptorCriteria.getConditions());
                }
                List<String> geographicalGranularityUrns = null;
                if (query != null) {
                    geographicalGranularityUrns = extractGeographicalGranularityUrnsFromQuery(sculptorCriteria.getConditions());
                }

                List<VariableElementResult> entities = codesService.findVariableElementsByVariableEfficiently(ctx, variableUrn, variableElementsCodes, selection, geographicalGranularityUrns);

                // Transform
                if (MediaType.APPLICATION_JSON.equals(mediaType)) {
                    target = codesDo2RestMapper.toVariableElementsGeoJson(entities, selection, lastUpdatedDateVariableElementsGeoInfo);
                } else if (MediaType.APPLICATION_XML.equals(mediaType)) {
                    target = codesDo2RestMapper.toVariableElementsGeoXml(entities, selection, lastUpdatedDateVariableElementsGeoInfo);
                }
            }
            return target;
        } catch (Exception e) {
            throw manageException(e);
        }
    }

    /**
     * Throws response error, logging exception
     */
    private RestException manageException(Exception e) {
        logger.error("Error", e);
        if (e instanceof RestException) {
            return (RestException) e;
        } else {
            // do not show information details about exception to user
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestCommonServiceExceptionType.UNKNOWN);
            return new RestException(exception, Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Throws response error, logging exception When the success response is tsv or csv, a response error must be xml because a response error in tsv or csv is not desirable.
     */
    private Response manageExceptionResponse(Exception e) {
        RestException errorResponse = manageException(e);
        return Response.fromResponse(errorResponse.getResponse()).header("Accept", "application/xml").header("Content-Type", "application/xml").build();
    }

    private void checkParameterNotWildcardFindItemSchemes(String agencyID) {
        checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_AGENCY_ID, agencyID);
    }

    private void checkParameterNotWildcardFindItemSchemes(String agencyID, String resourceID) {
        checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_RESOURCE_ID, resourceID);
    }

    private void checkParameterNotWildcardRetrieveItemScheme(String agencyID, String resourceID, String version) {
        checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_AGENCY_ID, agencyID);
        checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_RESOURCE_ID, resourceID);
        checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_VERSION, version);
    }

    private void checkParameterNotWildcardRetrieveItem(String agencyID, String resourceID, String version, String itemID, String parameterNameItemID) {
        checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_AGENCY_ID, agencyID);
        checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_RESOURCE_ID, resourceID);
        checkParameterNotWildcardAll(SrmRestConstants.PARAMETER_VERSION, version);
        checkParameterNotWildcardAll(parameterNameItemID, itemID);
    }

    private void checkCodelistVersionWithSameVariable(CodelistVersionMetamac codelistVersion1, CodelistVersionMetamac codelistVersion2) {
        if (codelistVersion1.getVariable() == null || codelistVersion2.getVariable() == null
                || !codelistVersion1.getVariable().getNameableArtefact().getCode().equals(codelistVersion2.getVariable().getNameableArtefact().getCode())) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.VARIABLE_CODELIST_MUST_BE_THE_SAME_TO_GET_RECODING);
            throw new RestException(exception, Status.BAD_REQUEST, new Throwable(exception.getMessage()));
        }
    }

    /**
     * Check unsupported wildcards in some parameters
     */
    private void checkParameterNotWildcardAll(String parameterName, String parameterValue) {
        if (SrmRestConstants.WILDCARD_ALL.equals(parameterValue)) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.PARAMETER_INCORRECT, parameterName);
            throw new RestException(exception, Status.BAD_REQUEST, new Throwable(exception.getMessage()));
        }
    }

    /**
     * Check parameter is empty
     */
    private void checkParameterEmpty(String parameterName, String parameterValue) {
        if (!StringUtils.isBlank(parameterValue)) {
            org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.PARAMETER_UNEXPECTED, parameterName);
            throw new RestException(exception, Status.BAD_REQUEST, new Throwable(exception.getMessage()));
        }
    }

    private boolean mustFindItemsInsteadRetrieveAllItemsOfItemScheme(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        if (SrmRestConstants.WILDCARD_ALL.equals(agencyID) || SrmRestConstants.WILDCARD_ALL.equals(resourceID) || SrmRestConstants.WILDCARD_ALL.equals(version)) {
            return true;
        }
        if (query != null || orderBy != null || limit != null || offset != null) {
            return true;
        }
        // can retrieve all items of itemScheme
        return false;
    }

    private boolean mustFindVariableElementsInsteadRetrieveAllVariableElementsOfVariable(String variableID, String variableElementID, String query, List<ConditionalCriteria> queries, String orderBy,
            String limit, String offset) {
        if (variableID == null || SrmRestConstants.WILDCARD_ALL.equals(variableID)) {
            // only when it is a specific variable
            return true;
        }
        if (variableElementID != null && !SrmRestConstants.WILDCARD_ALL.equals(variableElementID)) {
            // retrieving only one. do not execute query
            return true;
        }
        if (orderBy != null || limit != null || offset != null) {
            return true;
        }
        if (query != null && !isQueryOnlyByVariableElementCodes(queries)) {
            return true;
        }
        // can retrieve all
        return false;
    }

    /**
     * If query != null, query must have three sentences to can find all variable elements: order, distinctRoot and code (id)
     */
    private boolean isQueryOnlyByVariableElementCodes(List<ConditionalCriteria> queries) {
        if (queries.size() != 3) {
            return false;
        }
        ConditionalCriteria query = extractQueryByCodeWithEqualsOrIn(queries);
        if (query == null) {
            return false;
        }
        return true;
    }

    /**
     * Must be [API]/codelists/~all/~all/~all/codes?query=DEFAULT_GEOGRAPHICAL_GRANULARITIES_CODELIST EQ TRUE
     */
    private boolean isFindByDefaultGeographicalGranularitiesCodelist(String agencyID, String resourceID, String version, String query, String orderBy, String limit, String offset) {
        if (!SrmRestConstants.WILDCARD_ALL.equals(agencyID) || !SrmRestConstants.WILDCARD_ALL.equals(resourceID) || !SrmRestConstants.WILDCARD_ALL.equals(version)) {
            return false;
        }
        if (query == null || orderBy != null || limit != null || offset != null) {
            return false;
        }
        return query.equalsIgnoreCase(CodeCriteriaPropertyRestriction.DEFAULT_GEOGRAPHICAL_GRANULARITIES_CODELIST + " " + ComparisonOperator.EQ.value() + " 'TRUE'");
    }

    private List<String> extractCodesFromQuery(List<ConditionalCriteria> queries) {
        ConditionalCriteria query = extractQueryByCodeWithEqualsOrIn(queries);
        return extractVariableElement(query);
    }

    private List<String> extractGeographicalGranularityUrnsFromQuery(List<ConditionalCriteria> queries) {
        ConditionalCriteria query = extractQueryByGeographicalGranularityUrnWithEqualsOrIn(queries);
        return extractVariableElement(query);
    }

    private ConditionalCriteria extractQueryByCodeWithEqualsOrIn(List<ConditionalCriteria> queries) {
        String propertyFullName = VariableElementProperties.identifiableArtefact().code().toString();
        return extractQueryByPropertyFullNameWithEqualsOrIn(queries, propertyFullName);
    }

    private ConditionalCriteria extractQueryByGeographicalGranularityUrnWithEqualsOrIn(List<ConditionalCriteria> queries) {
        String propertyFullName = VariableElementProperties.geographicalGranularity().nameableArtefact().toString();
        return extractQueryByPropertyFullNameWithEqualsOrIn(queries, propertyFullName);
    }

    private ConditionalCriteria extractQueryByPropertyFullNameWithEqualsOrIn(List<ConditionalCriteria> queries, String propertyFullName) {
        for (ConditionalCriteria query : queries) {
            if (propertyFullName.equals(query.getPropertyFullName()) && (query.getOperator().equals(Operator.In) || query.getOperator().equals(Operator.Equal))) {
                return query;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<String> extractVariableElement(ConditionalCriteria query) {
        if (query == null) {
            return null;
        }
        if (query.getOperator().equals(Operator.In)) {
            return (List<String>) query.getFirstOperant();
        } else if (query.getOperator().equals(Operator.Equal)) {
            return Arrays.asList((String) query.getFirstOperant());
        }
        return null;
    }

    private DateTime retrieveLastUpdatedDateVariableElementsGeographicalInformation() throws MetamacException {
        return miscMetamacService.findLastUpdatedVariableElementsGeographicalInformation(ctx);
    }

    public static String getContentDisposition(String fileNamePrefix, String format) {
        return "inline; filename=" + fileNamePrefix + "." + format;
    }

    private Set<String> getFieldsParameters(String fields) {
        Set<String> validFields = new HashSet<>();
        validFields.add(SrmRestConstants.FIELD_INCLUDE_ENI_SCHEME);
        validFields.add(SrmRestConstants.FIELD_INCLUDE_ANNOTATIONS);
        validFields.add(SrmRestConstants.FIELD_INCLUDE_DESCRIPTION);
        validFields.add(SrmRestConstants.FIELD_INCLUDE_CATEGORY_ELEMENT);
        return SrmRestInternalUtils.parseFieldsParameter(fields, validFields);
    }

    private String getOrderConfigurationToUseToRetrieveConceptScheme(String agencyID, String resourceID, String version, String order, ConceptSchemeVersionMetamac conceptSchemeVersion)
            throws MetamacException {
        String orderConfigurationToUse = null;
        if (order == null) {
            orderConfigurationToUse = conceptSchemeVersion.getDefaultOrderVisualisation().getNameableArtefact().getCode();
        } else {
            // check exist
            retrieveConceptSchemeOrderVisualisation(agencyID, resourceID, version, conceptSchemeVersion.getMaintainableArtefact().getUrn(), order);
            orderConfigurationToUse = order;
        }
        return orderConfigurationToUse;
    }

    private ConceptSchemeOrderVisualisation retrieveConceptSchemeOrderVisualisation(String agencyID, String resourceID, String version, String conceptSchemeUrn, String orderConfiguration)
            throws MetamacException {
        try {
            return conceptsService.retrieveConceptSchemeOrderVisualisationByCode(ctx, conceptSchemeUrn, orderConfiguration);
        } catch (MetamacException e) {
            if (e.getExceptionItems().size() == 1 && ServiceExceptionType.CONCEPT_SCHEME_ORDER_VISUALISATION_NOT_FOUND.getCode().equals(e.getExceptionItems().get(0).getCode())) {
                org.siemac.metamac.rest.common.v1_0.domain.Exception exception = RestExceptionUtils.getException(RestServiceExceptionType.CONCEPT_SCHEME_ORDER_CONFIGURATION_NOT_FOUND,
                        orderConfiguration, resourceID, version, agencyID);
                throw new RestException(exception, Status.NOT_FOUND, e);
            } else {
                throw e;
            }
        }
    }
}