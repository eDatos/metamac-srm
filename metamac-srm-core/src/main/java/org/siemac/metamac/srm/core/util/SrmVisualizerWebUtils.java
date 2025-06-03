package org.siemac.metamac.srm.core.util;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.constants.SdmxConstants;

/**
 * 
 */
public class SrmVisualizerWebUtils {

    private static final String URL_SEPARATOR                               = "/";
    private static final String URL_QUERY_RESOURCE_TYPE_CODELIST            = "codelists/codelists";
    private static final String URL_QUERY_RESOURCE_TYPE_CONCEPTS            = "concepts/concepts";
    private static final String URL_QUERY_RESOURCE_TYPE_CODES               = "codelists/codes";
    private static final String URL_QUERY_RESOURCE_CODELIST_FAMILIES        = "codelists/families";
    private static final String URL_QUERY_RESOURCE_TYPE_CONCEPT_SCHEME      = "concepts/schemes";
    private static final String URL_QUERY_RESOURCE_TYPE_CATEGORY_SHCHEME    = "categories/schemes";
    private static final String URL_QUERY_RESOURCE_TYPE_CATEGORIES          = "categories/categories";
    private static final String URL_QUERY_RESOURCE_TYPE_DATA_STRUCTURE      = "dsd/codes";
    private static final String URL_QUERY_RESOURCE_TYPE_ORGANIZATION_SCHEME = "organisations/schemes";
    private static final String URL_QUERY_RESOURCE_TYPE_ORGANIZATIONS       = "organisations/organisations";
    private static final String URL_DETAIL                                  = "detail";

    public static String buildOrganizationsUnitUrl(MaintainableArtefact source, String code, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(URL_QUERY_RESOURCE_TYPE_ORGANIZATIONS);
        buildQueryParametersForNoVersionableResource(source, code, builder);

        return builder.toString();
    }

    public static String buildConceptsUrl(MaintainableArtefact source, String code, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(URL_QUERY_RESOURCE_TYPE_CONCEPTS);
        buildQueryParametersForNoVersionableResource(source, code, builder);

        return builder.toString();
    }

    public static String buildCodesUrl(MaintainableArtefact source, String code, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(URL_QUERY_RESOURCE_TYPE_CODES);
        buildQueryParametersForNoVersionableResource(source, code, builder);

        return builder.toString();
    }

    public static String buildCategoriesUrl(MaintainableArtefact source, String code, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(URL_QUERY_RESOURCE_TYPE_CATEGORIES);
        buildQueryParametersForNoVersionableResource(source, code, builder);

        return builder.toString();
    }

    public static String buildCodelistFamiliesUrl(String code, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(URL_QUERY_RESOURCE_CODELIST_FAMILIES);
        builder.append(URL_SEPARATOR);
        builder.append(code);
        builder.append(URL_SEPARATOR);
        builder.append(URL_DETAIL);

        return builder.toString();
    }

    private static void buildQueryParametersForNoVersionableResource(MaintainableArtefact source, String code, StringBuilder builder) {
        builder.append(URL_SEPARATOR);
        builder.append(source.getMaintainer() != null ? source.getMaintainer().getIdAsMaintainer() : SdmxConstants.AGENCY_SDMX_CODE);
        builder.append(URL_SEPARATOR);
        builder.append(source.getCode());
        builder.append(URL_SEPARATOR);
        builder.append(code);
        builder.append(URL_SEPARATOR);
        builder.append(source.getVersionLogic());
        builder.append(URL_SEPARATOR);
        builder.append(URL_DETAIL);
    }

    public static String buildCodelistUrl(MaintainableArtefact source, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(URL_QUERY_RESOURCE_TYPE_CODELIST);
        builder.append(URL_SEPARATOR);
        builder.append(buildQueryParametersForVersionableResource(source));
        return builder.toString();
    }

    public static String buildDataStructureUrl(MaintainableArtefact source, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(URL_QUERY_RESOURCE_TYPE_DATA_STRUCTURE);
        builder.append(URL_SEPARATOR);
        builder.append(buildQueryParametersForVersionableResource(source));
        return builder.toString();
    }

    public static String buildCategorySchemeUrl(MaintainableArtefact source, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(URL_QUERY_RESOURCE_TYPE_CATEGORY_SHCHEME);
        builder.append(URL_SEPARATOR);
        builder.append(buildQueryParametersForVersionableResource(source));
        return builder.toString();
    }

    public static String buildConceptSchemeUrl(MaintainableArtefact source, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(URL_QUERY_RESOURCE_TYPE_CONCEPT_SCHEME);
        builder.append(URL_SEPARATOR);
        builder.append(buildQueryParametersForVersionableResource(source));
        return builder.toString();
    }

    public static String buildOrganizationSchemeSchemeUrl(MaintainableArtefact source, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(URL_QUERY_RESOURCE_TYPE_ORGANIZATION_SCHEME);
        builder.append(URL_SEPARATOR);
        builder.append(buildQueryParametersForVersionableResource(source));
        return builder.toString();
    }

    /**
     * @param source
     * @return
     * @throws IllegalArgumentException
     * @see BaseDo2RestMapperV10Impl#toMaintainableArtefact(MaintainableArtefact, SrmLifeCycleMetadata, MaintainableArtefact)
     */
    private static String buildQueryParametersForVersionableResource(MaintainableArtefact source) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(source.getMaintainer() != null ? source.getMaintainer().getIdAsMaintainer() : SdmxConstants.AGENCY_SDMX_CODE);
        builder.append(URL_SEPARATOR);
        builder.append(source.getCode());
        builder.append(URL_SEPARATOR);
        builder.append(source.getVersionLogic());
        builder.append(URL_SEPARATOR);
        builder.append(URL_DETAIL);
        return builder.toString();

    }
    private static String buildEndpointUrl(String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder().append(urlBase);

        if (!StringUtils.isBlank(urlBase) && !urlBase.endsWith(URL_SEPARATOR)) {
            builder.append(URL_SEPARATOR);
        }
        return builder.toString();
    }
}
