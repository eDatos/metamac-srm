package org.siemac.metamac.srm.web.category.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeVisualisationDto;
import org.siemac.metamac.srm.web.category.model.record.CategoryElementRecord;
import org.siemac.metamac.srm.web.category.model.record.CategoryRecord;
import org.siemac.metamac.srm.web.category.model.record.CategorySchemeRecord;
import org.siemac.metamac.srm.web.category.model.record.CategorySchemeVisualisationRecord;

import com.smartgwt.client.widgets.grid.ListGridRecord;

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

    // CATEGORY SCHEME VISUALISATIONS

    public static CategorySchemeVisualisationRecord getCategorySchemeVisualisationRecord(CategorySchemeVisualisationDto categorySchemeVisualisationDto) {
        return new CategorySchemeVisualisationRecord(categorySchemeVisualisationDto.getCode(), getLocalisedString(categorySchemeVisualisationDto.getName()),
                categorySchemeVisualisationDto.getUrn(), categorySchemeVisualisationDto);
    }

    public static ListGridRecord[] getCategorySchemeVisualisationRecords(List<CategorySchemeVisualisationDto> categorySchemeVisualisationDtos) {
        ListGridRecord[] records = new ListGridRecord[categorySchemeVisualisationDtos.size()];
        for (int i = 0; i < categorySchemeVisualisationDtos.size(); i++) {
            records[i] = getCategorySchemeVisualisationRecord(categorySchemeVisualisationDtos.get(i));
        }
        return records;
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
