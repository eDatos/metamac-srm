package org.siemac.metamac.srm.web.category.model.record;

import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;
import org.siemac.metamac.srm.web.resource.element.model.ds.ResourceElementDS;
import org.siemac.metamac.srm.web.resource.element.model.record.ResourceElementRecord;

public class CategoryElementRecord extends ResourceElementRecord {

    public CategoryElementRecord() {
    }

    public CategoryElementRecord(Long id, String code, String shortName, String urn, CategoryElementDto categoryElementDto) {
        super(id, code, shortName, urn);

        setCategoryElementDto(categoryElementDto);
    }

    public void setCategoryElementDto(CategoryElementDto categoryElementDto) {
        setAttribute(ResourceElementDS.DTO, categoryElementDto);
    }

    public CategoryElementDto getCategoryElementDto() {
        return (CategoryElementDto) getAttributeAsObject(ResourceElementDS.DTO);
    }
}
