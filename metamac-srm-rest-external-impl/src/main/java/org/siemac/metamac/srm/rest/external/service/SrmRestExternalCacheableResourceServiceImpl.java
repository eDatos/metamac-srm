package org.siemac.metamac.srm.rest.external.service;

import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.rest.common.domain.CacheableResource;
import org.siemac.metamac.rest.service.cache.CacheableResourceService;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.serviceapi.ConceptsMetamacService;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.siemac.metamac.srm.core.common.service.utils.GeneratorUrnUtils;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.external.constants.SrmRestExternalConstants;
import org.siemac.metamac.srm.rest.external.domain.cache.CategorySchemeCacheableResource;
import org.siemac.metamac.srm.rest.external.domain.cache.CodelistCacheableResource;
import org.siemac.metamac.srm.rest.external.domain.cache.ConceptSchemeCacheableResource;
import org.siemac.metamac.srm.rest.external.domain.cache.OrganisationSchemeCacheableResource;
import org.siemac.metamac.srm.rest.external.domain.cache.VariableElementCacheableResource;
import org.siemac.metamac.srm.rest.external.v1_0.service.utils.SrmRestInternalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SrmRestExternalCacheableResourceServiceImpl implements CacheableResourceService {

    private static final Logger logger = LoggerFactory.getLogger(SrmRestExternalCacheableResourceServiceImpl.class);

    @Autowired
    private CodesMetamacService         codesService;

    @Autowired
    private ConceptsMetamacService      conceptsService;

    @Autowired
    private CategoriesMetamacService    categoriesService;

    @Autowired
    private OrganisationsMetamacService organisationsService;

    private static final ServiceContext  ctx                      = SrmRestExternalConstants.SERVICE_CONTEXT;
    private static final PagingParameter pagingParameterOneResult = PagingParameter.pageAccess(1, 1, false);

    @Override
    public boolean supports(Class<? extends CacheableResource> resourceType) {
        return CodelistCacheableResource.class.isAssignableFrom(resourceType)
            || ConceptSchemeCacheableResource.class.isAssignableFrom(resourceType)
            || CategorySchemeCacheableResource.class.isAssignableFrom(resourceType)
            || OrganisationSchemeCacheableResource.class.isAssignableFrom(resourceType)
            || VariableElementCacheableResource.class.isAssignableFrom(resourceType);
    }

    @Override
    public DateTime getLastModifiedDate(Class<? extends CacheableResource> resourceType, Map<String, String> pathParams) {
        if (VariableElementCacheableResource.class.isAssignableFrom(resourceType)) {
            return getLastModifiedDateForVariableElements(pathParams.get("variableID"));
        }

        String agencyID   = pathParams.get("agencyID");
        String resourceID = pathParams.get("resourceID");
        String version    = pathParams.get("version");

        if (isWildcard(agencyID) || isWildcard(resourceID) || isWildcard(version)) {
            return null;
        }

        try {
            if (CodelistCacheableResource.class.isAssignableFrom(resourceType)) {
                return getCodelistLastModifiedDate(agencyID, resourceID, version);
            }
            if (ConceptSchemeCacheableResource.class.isAssignableFrom(resourceType)) {
                return getConceptSchemeLastModifiedDate(agencyID, resourceID, version);
            }
            if (CategorySchemeCacheableResource.class.isAssignableFrom(resourceType)) {
                return getCategorySchemeLastModifiedDate(agencyID, resourceID, version);
            }
            if (OrganisationSchemeCacheableResource.class.isAssignableFrom(resourceType)) {
                return getOrganisationSchemeLastModifiedDate(agencyID, resourceID, version);
            }
        } catch (Exception e) {
            logger.warn("Error getting last modified date for {}/{}/{}", new Object[]{agencyID, resourceID, version, e});
        }
        return null;
    }

    private DateTime getLastModifiedDateForVariableElements(String variableID) {
        if (variableID == null || SrmRestConstants.WILDCARD_ALL.equals(variableID)) {
            return null;
        }
        try {
            String variableUrn = GeneratorUrnUtils.generateVariableUrn(variableID);
            return codesService.findMaxLastUpdatedVariableElement(ctx, variableUrn);
        } catch (Exception e) {
            logger.warn("Error getting last modified date for variableID {}", variableID, e);
        }
        return null;
    }

    private DateTime getCodelistLastModifiedDate(String agencyID, String resourceID, String version) throws MetamacException {
        List<ConditionalCriteria> criteria = SrmRestInternalUtils
                .buildConditionalCriteriaItemSchemes(agencyID, resourceID, version, null, CodelistVersionMetamac.class);
        PagedResult<CodelistVersionMetamac> result = codesService.findCodelistsByCondition(ctx, criteria, pagingParameterOneResult);
        CodelistVersionMetamac codelist = result.getValues().isEmpty() ? null : result.getValues().get(0);
        return codelist != null ? codelist.getLastUpdated() : null;
    }

    private DateTime getConceptSchemeLastModifiedDate(String agencyID, String resourceID, String version) throws MetamacException {
        List<ConditionalCriteria> criteria = SrmRestInternalUtils
                .buildConditionalCriteriaItemSchemes(agencyID, resourceID, version, null, ConceptSchemeVersionMetamac.class);
        PagedResult<ConceptSchemeVersionMetamac> result = conceptsService.findConceptSchemesByCondition(ctx, criteria, pagingParameterOneResult);
        ConceptSchemeVersionMetamac conceptScheme = result.getValues().isEmpty() ? null : result.getValues().get(0);
        return conceptScheme != null ? conceptScheme.getLastUpdated() : null;
    }

    private DateTime getCategorySchemeLastModifiedDate(String agencyID, String resourceID, String version) throws MetamacException {
        List<ConditionalCriteria> criteria = SrmRestInternalUtils
                .buildConditionalCriteriaItemSchemes(agencyID, resourceID, version, null, CategorySchemeVersionMetamac.class);
        PagedResult<CategorySchemeVersionMetamac> result = categoriesService.findCategorySchemesByCondition(ctx, criteria, pagingParameterOneResult);
        CategorySchemeVersionMetamac categoryScheme = result.getValues().isEmpty() ? null : result.getValues().get(0);
        return categoryScheme != null ? categoryScheme.getLastUpdated() : null;
    }

    private DateTime getOrganisationSchemeLastModifiedDate(String agencyID, String resourceID, String version) throws MetamacException {
        List<ConditionalCriteria> criteria = SrmRestInternalUtils
                .buildConditionalCriteriaItemSchemes(agencyID, resourceID, version, null, OrganisationSchemeVersionMetamac.class);
        PagedResult<OrganisationSchemeVersionMetamac> result = organisationsService.findOrganisationSchemesByCondition(ctx, criteria, pagingParameterOneResult);
        OrganisationSchemeVersionMetamac organisationScheme = result.getValues().isEmpty() ? null : result.getValues().get(0);
        return organisationScheme != null ? organisationScheme.getLastUpdated() : null;
    }

    private boolean isWildcard(String value) {
        return value == null || SrmRestConstants.WILDCARD_ALL.equals(value);
    }
}
