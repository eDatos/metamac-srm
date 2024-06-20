package org.siemac.metamac.srm.web.category.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.web.category.model.record.CategoryElementRecord;
import org.siemac.metamac.srm.web.category.model.record.CategoryRecord;
import org.siemac.metamac.srm.web.category.model.record.CategorySchemeRecord;

public class CategoriesRecordUtils extends org.siemac.metamac.srm.web.client.utils.RecordUtils {

    // CATEGORY SCHEMES

    public static CategorySchemeRecord getCategorySchemeRecord(CategorySchemeMetamacBasicDto categorySchemeDto) {
        CategorySchemeRecord record = new CategorySchemeRecord();
        record = (CategorySchemeRecord) getItemSchemeRecord(record, categorySchemeDto, categorySchemeDto.getLifeCycle());
        record.setCategorySchemeBasicDto(categorySchemeDto);
        record.setStreamMessageStatus(categorySchemeDto.getStreamMessageStatus());
        return record;
    }

    // CATEGORIES

    public static CategoryRecord getCategoryRecord(CategoryMetamacBasicDto categoryDto) {
        CategoryRecord record = new CategoryRecord();
        record = (CategoryRecord) getItemRecord(record, categoryDto);
        record.setCategoryBasicDto(categoryDto);
        return record;
    }

    // CATEGORY ELEMENTS

    public static CategoryElementRecord getCategoryElementRecord(CategoryElementDto categoryElementDto) {
        return new CategoryElementRecord(categoryElementDto.getId(), categoryElementDto.getCode(), getLocalisedString(categoryElementDto.getShortName()), categoryElementDto.getUrn(),
                categoryElementDto);
    }

    public static CategoryElementRecord getVariableElementRecord(CategoryElementDto categoryElementDto) {
        CategoryElementRecord categoryElementRecord = new CategoryElementRecord();
        categoryElementRecord.setCode(categoryElementDto.getCode());
        categoryElementRecord.setShortName(getLocalisedString(categoryElementDto.getShortName()));
        categoryElementRecord.setUrn(categoryElementDto.getUrn());
        categoryElementRecord.setIsVisible(categoryElementDto.getIsVisible());
        categoryElementRecord.setCategoryElementDto(categoryElementDto);
        return categoryElementRecord;
    }
}
