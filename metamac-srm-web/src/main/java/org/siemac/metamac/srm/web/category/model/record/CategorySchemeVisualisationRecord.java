package org.siemac.metamac.srm.web.category.model.record;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeVisualisationDto;
import org.siemac.metamac.srm.web.category.model.ds.CategorySchemeVisualisationDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CategorySchemeVisualisationRecord extends ListGridRecord {

    public CategorySchemeVisualisationRecord(String code, String name, String urn, CategorySchemeVisualisationDto categorySchemeVisualisationDto) {
        setCode(code);
        setName(name);
        setUrn(urn);
        setCategorySchemeVisualisationDto(categorySchemeVisualisationDto);
    }

    public void setCode(String code) {
        setAttribute(CategorySchemeVisualisationDS.CODE, code);
    }

    public String getCode() {
        return getAttributeAsString(CategorySchemeVisualisationDS.CODE);
    }

    public void setName(String name) {
        setAttribute(CategorySchemeVisualisationDS.NAME, name);
    }

    public String getName() {
        return getAttributeAsString(CategorySchemeVisualisationDS.NAME);
    }

    public void setUrn(String urn) {
        setAttribute(CategorySchemeVisualisationDS.URN, urn);
    }

    public String getUrn() {
        return getAttributeAsString(CategorySchemeVisualisationDS.URN);
    }

    public void setCategorySchemeVisualisationDto(CategorySchemeVisualisationDto categorySchemeVisualisationDto) {
        setAttribute(CategorySchemeVisualisationDS.DTO, categorySchemeVisualisationDto);
    }

    public CategorySchemeVisualisationDto getCategorySchemeVisualisationDto() {
        return (CategorySchemeVisualisationDto) getAttributeAsObject(CategorySchemeVisualisationDS.DTO);
    }
}
