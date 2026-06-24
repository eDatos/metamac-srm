package org.siemac.metamac.srm.core.invocation.utils;

import javax.annotation.PostConstruct;

import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.rest.common.v1_0.domain.ResourceLink;
import org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.utils.RestUtils;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.dsd.domain.DataStructureDefinitionVersionMetamac;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.common.domain.InternationalString;
import com.arte.statistic.sdmx.srm.core.common.domain.LocalisedString;

@Component
public class RestMapper {

    private String                           srmApiInternalEndpointV10;
    private InternalWebApplicationNavigation internalWebApplicationNavigation;

    @Autowired
    private ConfigurationService             configurationService;

    @PostConstruct
    public void init() throws MetamacException {
        String srmInternalWebUrlBase = configurationService.retrieveSrmInternalWebApplicationUrlBase();
        internalWebApplicationNavigation = new InternalWebApplicationNavigation(srmInternalWebUrlBase);

        String srmApiInternalEndpoint = configurationService.retrieveSrmInternalApiUrlBase();

        srmApiInternalEndpointV10 = RestUtils.createLink(srmApiInternalEndpoint, SrmRestConstants.API_VERSION_1_0);

    }

    public ResourceInternal variableElementToResourceInternal(VariableElement variableElement) {
        ResourceInternal resource = new ResourceInternal();
        if (variableElement != null) {
            resource.setId(variableElement.getIdentifiableArtefact().getCode());
            resource.setUrn(variableElement.getIdentifiableArtefact().getUrn());
            resource.setKind(SrmRestConstants.KIND_VARIABLE_ELEMENT);
            resource.setName(toRestInternationalString(variableElement.getShortName()));
            resource.setManagementAppLink(
                    toVariableElementManagementApplicationLink(variableElement.getVariable().getNameableArtefact().getCode(), variableElement.getIdentifiableArtefact().getCode()));
            resource.setSelfLink(toVariableElementSelfLink(variableElement.getVariable().getNameableArtefact().getCode(), variableElement.getIdentifiableArtefact().getCode()));
        }
        return resource;
    }

    public ResourceInternal dsdToResourceInternal(DataStructureDefinitionVersionMetamac dsd) {
        ResourceInternal resource = new ResourceInternal();
        if (dsd != null) {
            String agencyId = dsd.getMaintainableArtefact().getMaintainer().getIdAsMaintainer();
            String code = dsd.getMaintainableArtefact().getCode();
            String version = dsd.getMaintainableArtefact().getVersionLogic();
            String urn = dsd.getMaintainableArtefact().getUrn();

            resource.setId(code);
            resource.setUrn(urn);
            resource.setKind(SrmRestConstants.KIND_DATA_STRUCTURE);
            resource.setName(toRestInternationalString(dsd.getMaintainableArtefact().getName()));
            resource.setManagementAppLink(internalWebApplicationNavigation.buildDsdUrl(UrnUtils.removePrefix(urn)));
            resource.setSelfLink(toDsdSelfLink(agencyId, code, version));
        }
        return resource;
    }

    private String toVariableElementManagementApplicationLink(String variableCode, String variableElementCode) {
        return internalWebApplicationNavigation.buildVariableElementUrl(variableCode, variableElementCode);
    }

    private org.siemac.metamac.rest.common.v1_0.domain.InternationalString toRestInternationalString(InternationalString source) {
        org.siemac.metamac.rest.common.v1_0.domain.InternationalString target = new org.siemac.metamac.rest.common.v1_0.domain.InternationalString();
        if (source != null) {
            for (LocalisedString item : source.getTexts()) {
                org.siemac.metamac.rest.common.v1_0.domain.LocalisedString targetString = new org.siemac.metamac.rest.common.v1_0.domain.LocalisedString();
                targetString.setValue(item.getLabel());
                targetString.setLang(item.getLocale());
                target.getTexts().add(targetString);
            }
        }
        return target;
    }

    // API/variables
    private String toVariablesLink() {
        return RestUtils.createLink(srmApiInternalEndpointV10, SrmRestConstants.LINK_SUBPATH_VARIABLES);
    }

    private String toVariableLink(String variableID) {
        String linkVariables = toVariablesLink();
        return RestUtils.createLink(linkVariables, variableID);
    }

    private String toVariableElementsLink(String variableID) {
        String linkVariable = toVariableLink(variableID);
        return RestUtils.createLink(linkVariable, SrmRestConstants.LINK_SUBPATH_VARIABLE_ELEMENTS);
    }

    private String toVariableElementLink(String variableID, String variableElementID) {
        String linkVariableElements = toVariableElementsLink(variableID);
        return RestUtils.createLink(linkVariableElements, variableElementID);
    }

    private ResourceLink toVariableElementSelfLink(String variableCode, String variableElementCode) {
        ResourceLink target = new ResourceLink();
        target.setKind(SrmRestConstants.KIND_VARIABLE_ELEMENT);
        target.setHref(toVariableElementLink(variableCode, variableElementCode));
        return target;
    }

    // API/datastructures
    private String toDataStructuresLink() {
        return RestUtils.createLink(srmApiInternalEndpointV10, SrmRestConstants.LINK_SUBPATH_DATA_STRUCTURES);
    }

    private String toDataStructureLink(String agencyId, String dsdCode, String version) {
        String link = RestUtils.createLink(toDataStructuresLink(), agencyId);
        link = RestUtils.createLink(link, dsdCode);
        link = RestUtils.createLink(link, version);
        return link;
    }

    private ResourceLink toDsdSelfLink(String agencyId, String dsdCode, String version) {
        ResourceLink target = new ResourceLink();
        target.setKind(SrmRestConstants.KIND_DATA_STRUCTURE);
        target.setHref(toDataStructureLink(agencyId, dsdCode, version));
        return target;
    }
}
