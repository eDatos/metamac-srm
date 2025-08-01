package org.siemac.metamac.srm.web.concept.model.record;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeVisualisationDto;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeVisualisationDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ConceptSchemeVisualisationRecord extends ListGridRecord {

    public ConceptSchemeVisualisationRecord(String code, String name, String urn, ConceptSchemeVisualisationDto conceptSchemeVisualisationDto) {
        setCode(code);
        setName(name);
        setUrn(urn);
        setConceptSchemeVisualisationDto(conceptSchemeVisualisationDto);
    }

    public void setCode(String code) {
        setAttribute(ConceptSchemeVisualisationDS.CODE, code);
    }

    public String getCode() {
        return getAttributeAsString(ConceptSchemeVisualisationDS.CODE);
    }

    public void setName(String name) {
        setAttribute(ConceptSchemeVisualisationDS.NAME, name);
    }

    public String getName() {
        return getAttributeAsString(ConceptSchemeVisualisationDS.NAME);
    }

    public void setUrn(String urn) {
        setAttribute(ConceptSchemeVisualisationDS.URN, urn);
    }

    public String getUrn() {
        return getAttributeAsString(ConceptSchemeVisualisationDS.URN);
    }

    public void setConceptSchemeVisualisationDto(ConceptSchemeVisualisationDto conceptSchemeVisualisationDto) {
        setAttribute(ConceptSchemeVisualisationDS.DTO, conceptSchemeVisualisationDto);
    }

    public ConceptSchemeVisualisationDto getConceptSchemeVisualisationDto() {
        return (ConceptSchemeVisualisationDto) getAttributeAsObject(ConceptSchemeVisualisationDS.DTO);
    }
}
