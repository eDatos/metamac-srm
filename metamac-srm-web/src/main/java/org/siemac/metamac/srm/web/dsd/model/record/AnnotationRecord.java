package org.siemac.metamac.srm.web.dsd.model.record;

import org.siemac.metamac.srm.web.client.model.ds.AnnotationDS;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class AnnotationRecord extends ListGridRecord {

    public AnnotationRecord() {
    }

    public AnnotationRecord(Long id, String code, String title, AnnotationTypeDto type, String url, String text, Boolean isTextEditable, AnnotationDto annotationDto, Long annotationTypeId) {
        setId(id);
        setCode(code);
        setTitle(title);
        setUrl(url);
        setText(text);
        setIsTextEditable(isTextEditable);
        setAnnotationDto(annotationDto);
        setAnnotationTypeId(annotationTypeId);
    }

    public void setAnnotationTypeId(Long id) {
        setAttribute(AnnotationDS.TYPE, id);
    }

    public Long setAnnotationTypeId() {
        return getAttributeAsLong(AnnotationDS.TYPE);
    }

    public void setId(Long id) {
        setAttribute(AnnotationDS.ID, id);
    }

    public Long getId() {
        return getAttributeAsLong(AnnotationDS.ID);
    }

    public void setCode(String value) {
        setAttribute(AnnotationDS.CODE, value);
    }

    public String getCode() {
        return getAttributeAsString(AnnotationDS.CODE);
    }

    public void setTitle(String value) {
        setAttribute(AnnotationDS.TITLE, value);
    }

    public String getTitle() {
        return getAttributeAsString(AnnotationDS.TITLE);
    }

    public void setUrl(String value) {
        setAttribute(AnnotationDS.URL, value);
    }

    public String getUrl() {
        return getAttributeAsString(AnnotationDS.URL);
    }

    public void setText(String value) {
        setAttribute(AnnotationDS.TEXT, value);
    }

    public String getText() {
        return getAttributeAsString(AnnotationDS.TEXT);
    }

    public void setAnnotationDto(AnnotationDto value) {
        setAttribute(AnnotationDS.ANNOTATION_DTO, value);
    }

    public AnnotationDto getAnnotationDto() {
        return (AnnotationDto) getAttributeAsObject(AnnotationDS.ANNOTATION_DTO);
    }

    public void setIsTextEditable(Boolean value) {
        setAttribute(AnnotationDS.IS_TEXT_EDITABLE, value);
    }

    public Boolean getIsTextEditable() {
        return getAttributeAsBoolean(AnnotationDS.IS_TEXT_EDITABLE);
    }
}
