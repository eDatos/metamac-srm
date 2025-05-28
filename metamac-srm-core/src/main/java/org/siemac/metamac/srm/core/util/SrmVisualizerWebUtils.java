package org.siemac.metamac.srm.core.util;

import org.siemac.metamac.core.common.util.shared.StringUtils;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;

public class SrmVisualizerWebUtils {

    private static final String URL_SEPARATOR                          = "/";
    private static final String URL_QUERY_RESOURCE_TYPE_CODELIST       = "codelists/codelists";
    private static final String URL_QUERY_RESOURCE_TYPE_CONCEPTS       = "concepts/concepts";
    private static final String URL_QUERY_RESOURCE_TYPE_CODES          = "codelists/codes";
    private static final String URL_QUERY_RESOURCE_TYPE_CONCEPT_SCHEME = "concepts/schemes";
    private static final String URL_DETAIL                             = "detail";

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

    private static void buildQueryParametersForNoVersionableResource(MaintainableArtefact source, String code, StringBuilder builder) {
        builder.append(URL_SEPARATOR);
        builder.append(source.getMaintainer().getIdAsMaintainer());
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

    public static String buildConceptSchemeUrl(MaintainableArtefact source, String urlBase) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(buildEndpointUrl(urlBase));
        builder.append(URL_QUERY_RESOURCE_TYPE_CONCEPT_SCHEME);
        builder.append(URL_SEPARATOR);
        builder.append(buildQueryParametersForVersionableResource(source));
        return builder.toString();
    }

    private static String buildQueryParametersForVersionableResource(MaintainableArtefact source) throws IllegalArgumentException {
        StringBuilder builder = new StringBuilder();
        builder.append(source.getMaintainer().getIdAsMaintainer());
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
