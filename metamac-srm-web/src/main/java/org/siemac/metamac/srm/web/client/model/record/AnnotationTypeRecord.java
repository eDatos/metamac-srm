package org.siemac.metamac.srm.web.client.model.record;

import org.siemac.metamac.srm.web.client.model.ds.AnnotationTypeDS;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class AnnotationTypeRecord extends ListGridRecord {

    public AnnotationTypeRecord(String uuid, String title, String code, boolean isExternal, AnnotationTypeDto dto) {
        setUuid(uuid);
        setTitle(title);
        setCode(code);
        setExternal(isExternal);
        setDto(dto);
    }

    public void setUuid(String value) {
        setAttribute(AnnotationTypeDS.UUID, value);
    }

    public String getUuid() {
        return getAttributeAsString(AnnotationTypeDS.UUID);
    }

    public void setTitle(String value) {
        setAttribute(AnnotationTypeDS.TITLE, value);
    }

    public String getTitle() {
        return getAttributeAsString(AnnotationTypeDS.TITLE);
    }

    public void setCode(String code) {
        setAttribute(AnnotationTypeDS.CODE, code);
    }

    public String getCode() {
        return getAttributeAsString(AnnotationTypeDS.CODE);
    }

    public void setExternal(boolean isExternal) {
        setAttribute(AnnotationTypeDS.EXTERNAL, isExternal ? "X" : "");
    }

    public String getExternal() {
        return getAttributeAsString(AnnotationTypeDS.EXTERNAL);
    }

    public void setDto(AnnotationTypeDto dto) {
        setAttribute(AnnotationTypeDS.DTO, dto);
    }

    public AnnotationTypeDto getDto() {
        return (AnnotationTypeDto) getAttributeAsObject(AnnotationTypeDS.DTO);
    }

}
