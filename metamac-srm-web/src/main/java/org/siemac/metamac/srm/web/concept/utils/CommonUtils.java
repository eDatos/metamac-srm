package org.siemac.metamac.srm.web.concept.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeVisualisationDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptTypeDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityTypeEnum;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.FacetFormUtils;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeVisualisationRecord;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.FacetValueTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CommonUtils {

    public static LinkedHashMap<String, String> getConceptTypeHashMap(List<ConceptTypeDto> conceptTypeDtos) {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
        hashMap.put(new String(), new String());
        for (ConceptTypeDto conceptTypeDto : conceptTypeDtos) {
            hashMap.put(conceptTypeDto.getIdentifier(), CommonWebUtils.getElementName(conceptTypeDto.getIdentifier(), conceptTypeDto.getDescription()));
        }
        return hashMap;
    }

    public static LinkedHashMap<String, String> getConceptSchemeTypeHashMap() {
        LinkedHashMap<String, String> conceptSchemeTypeHashMap = new LinkedHashMap<String, String>();
        conceptSchemeTypeHashMap.put(new String(), new String());
        for (ConceptSchemeTypeEnum c : ConceptSchemeTypeEnum.values()) {
            String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().conceptSchemeTypeEnum() + c.getName());
            conceptSchemeTypeHashMap.put(c.toString(), value);
        }
        return conceptSchemeTypeHashMap;
    }

    public static String getConceptSchemeTypeName(ConceptSchemeTypeEnum conceptSchemeTypeEnum) {
        return conceptSchemeTypeEnum != null ? MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().conceptSchemeTypeEnum() + conceptSchemeTypeEnum.name()) : null;
    }

    public static LinkedHashMap<String, String> getConceptRoleHashMap() {
        LinkedHashMap<String, String> conceptRoleHashMap = new LinkedHashMap<String, String>();
        conceptRoleHashMap.put(new String(), new String());
        for (ConceptRoleEnum c : ConceptRoleEnum.values()) {
            String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().conceptRoleEnum() + c.getName());
            conceptRoleHashMap.put(c.toString(), value);
        }
        return conceptRoleHashMap;
    }

    public static String getConceptRoleName(ConceptRoleEnum conceptRoleEnum) {
        return conceptRoleEnum != null ? MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().conceptRoleEnum() + conceptRoleEnum.name()) : null;
    }

    public static LinkedHashMap<String, String> getConceptFacetValueTypeHashMap() {
        LinkedHashMap<String, String> conceptFacetValueTypeHashMap = new LinkedHashMap<String, String>();
        conceptFacetValueTypeHashMap.put(new String(), new String());
        for (FacetValueTypeEnum f : FacetFormUtils.getConceptFacetValueTypeEnums()) {
            String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().facetValueTypeEnum() + f.getName());
            conceptFacetValueTypeHashMap.put(f.toString(), value);
        }
        return conceptFacetValueTypeHashMap;
    }

    public static String getRelatedOperationCode(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        return conceptSchemeMetamacDto.getRelatedOperation() != null ? conceptSchemeMetamacDto.getRelatedOperation().getCode() : null;
    }

    public static String getRelatedOperationCode(ConceptSchemeMetamacBasicDto conceptSchemeMetamacDto) {
        return conceptSchemeMetamacDto.getRelatedOperation() != null ? conceptSchemeMetamacDto.getRelatedOperation().getCode() : null;
    }

    public static String getQuantityTypeName(QuantityTypeEnum quantityType) {
        return quantityType != null ? MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().quantityTypeEnum() + quantityType.name()) : null;
    }

    public static LinkedHashMap<String, String> getQuantityTypeHashMap() {
        LinkedHashMap<String, String> quantityTypeHashMap = new LinkedHashMap<String, String>();
        quantityTypeHashMap.put(new String(), new String());
        for (QuantityTypeEnum c : QuantityTypeEnum.values()) {
            String value = MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().quantityTypeEnum() + c.getName());
            quantityTypeHashMap.put(c.toString(), value);
        }
        return quantityTypeHashMap;
    }

    public static LinkedHashMap<String, String> getResourceTypeThatCanBeEnumeratedRepresentationForConcept() {
        LinkedHashMap<String, String> typeHashMap = new LinkedHashMap<String, String>();
        typeHashMap.put(RelatedResourceTypeEnum.CODELIST.name(), getConstants().codelist());
        typeHashMap.put(RelatedResourceTypeEnum.CONCEPT_SCHEME.name(), getConstants().conceptScheme());
        return typeHashMap;
    }

    public static RelatedResourceTypeEnum getRelatedResourceTypeEnum(String resourceType) {
        if (!StringUtils.isBlank(resourceType)) {
            try {
                return RelatedResourceTypeEnum.valueOf(resourceType);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    // VISUALISATIONS

    public static List<String> getUrnsFromSelectedConceptSchemeVisualisations(ListGridRecord[] records) {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : records) {
            ConceptSchemeVisualisationRecord conceptSchemeRecord = (ConceptSchemeVisualisationRecord) record;
            urns.add(conceptSchemeRecord.getUrn());
        }
        return urns;
    }

    public static List<String> getVisualisationCodesFromSelectedConceptSchemeVisualisations(ListGridRecord[] records) {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : records) {
            ConceptSchemeVisualisationRecord conceptSchemeRecord = (ConceptSchemeVisualisationRecord) record;
            urns.add(conceptSchemeRecord.getCode());
        }
        return urns;
    }

    public static String getConceptSchemeVisualisationName(ConceptSchemeVisualisationDto conceptSchemeVisualisationDto) {
        return CommonWebUtils.getElementName(conceptSchemeVisualisationDto.getCode(), conceptSchemeVisualisationDto.getName());
    }

    public static LinkedHashMap<String, String> getConceptSchemeVisualisationsHashMap(List<ConceptSchemeVisualisationDto> visualisations) {
        LinkedHashMap<String, String> hashMap = new LinkedHashMap<String, String>();
        hashMap.put(StringUtils.EMPTY, StringUtils.EMPTY);
        for (ConceptSchemeVisualisationDto visualisation : visualisations) {
            hashMap.put(visualisation.getUrn(), getConceptSchemeVisualisationName(visualisation));
        }
        return hashMap;
    }

    /**
     * METADATA VISIBILITY
     */

    public static boolean isMetadataSdmxRelatedArtefactVisible(ConceptSchemeTypeEnum type) {
        return ConceptSchemeTypeEnum.OPERATION.equals(type) || ConceptSchemeTypeEnum.TRANSVERSAL.equals(type);
    }

    public static boolean isMetadataRolesVisible(ConceptSchemeTypeEnum type) {
        return ConceptSchemeTypeEnum.OPERATION.equals(type) || ConceptSchemeTypeEnum.TRANSVERSAL.equals(type) || ConceptSchemeTypeEnum.MEASURE.equals(type);
    }

    public static boolean isMetadataExtendsVisible(ConceptSchemeTypeEnum type) {
        return !ConceptSchemeTypeEnum.ROLE.equals(type);
    }

    public static boolean isMetadataVariableVisible(ConceptSchemeTypeEnum type) {
        return ConceptSchemeTypeEnum.OPERATION.equals(type) || ConceptSchemeTypeEnum.TRANSVERSAL.equals(type);
    }

    //
    // ICONS
    //

    public static FormItemIcon getConceptSchemeTypeInfoIcon() {
        FormItemIcon conceptSchemeTypeInfo = new FormItemIcon();
        conceptSchemeTypeInfo.setSrc(org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.info().getURL());
        conceptSchemeTypeInfo.setHeight(14);
        conceptSchemeTypeInfo.setWidth(14);
        conceptSchemeTypeInfo.setPrompt(getConstants().conceptSchemeTypeInfoEdition());
        return conceptSchemeTypeInfo;
    }
}
