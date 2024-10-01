package org.siemac.metamac.srm.web.category.model.record;

import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;
import org.siemac.metamac.srm.web.resource.element.model.ds.ResourceElementDS;
import org.siemac.metamac.srm.web.resource.element.model.record.ResourceElementRecord;
import org.siemac.metamac.web.common.client.utils.BooleanWebUtils;

public class CategoryElementRecord extends ResourceElementRecord {

    public CategoryElementRecord() {
    }

    public CategoryElementRecord(Long id, String code, String shortName, String urn, CategoryElementDto categoryElementDto) {
        super(id, code, shortName, urn);
        setIsVisible(categoryElementDto.getIsVisible());
        setCategoryElementDto(categoryElementDto);
    }

    public void setCategoryElementDto(CategoryElementDto categoryElementDto) {
        setAttribute(ResourceElementDS.DTO, categoryElementDto);
    }

    public CategoryElementDto getCategoryElementDto() {
        return (CategoryElementDto) getAttributeAsObject(ResourceElementDS.DTO);
    }

    public String getIsVisible() {
        return getAttributeAsString(ResourceElementDS.IS_VISIBLE);
    }

    public void setIsVisible(Boolean isVisible) {
        setAttribute(ResourceElementDS.IS_VISIBLE, BooleanWebUtils.getBooleanLabel(isVisible));
    }

}
