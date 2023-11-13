package org.siemac.metamac.srm.core.invocation.utils;

import java.util.HashMap;
import java.util.Map;

import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.navigation.shared.PlaceRequestParams;
import org.springframework.web.util.UriTemplate;

public class InternalWebApplicationNavigation {

    private final String        SEPARATOR                 = "/";
    private static final String PATH_STRUCTURAL_RESOURCES = "#" + NameTokens.structuralResourcesPage;
    private static final String VARIABLE_PARAMETER        = "variableParam";
    private static final String RESOURCE_ID_PARAMETER     = "resourceParam";

    private UriTemplate         variableElementTemplate;

    public InternalWebApplicationNavigation(String webApplicationPath) {
        // Publication
        // @formatter:off
        variableElementTemplate = new UriTemplate(webApplicationPath + SEPARATOR + PATH_STRUCTURAL_RESOURCES + SEPARATOR + NameTokens.variableListPage + SEPARATOR + NameTokens.variablePage + ";"
                + PlaceRequestParams.variableParamId + "=" + "{" + VARIABLE_PARAMETER + "}" + SEPARATOR + NameTokens.variableElementPage + ";" + PlaceRequestParams.variableElementParamId + "=" + "{"
                + RESOURCE_ID_PARAMETER + "}");
    }

    public String buildVariableElementUrl(String variableID, String variableElementID) {
        Map<String, String> parameters = new HashMap<>(2);
        parameters.put(VARIABLE_PARAMETER, variableID);
        parameters.put(RESOURCE_ID_PARAMETER, variableElementID);
        return variableElementTemplate.expand(parameters).toString();
    }

}