package org.siemac.metamac.srm.web.category.model.record;

import org.siemac.metamac.srm.core.master.dto.EniSchemaCatalogDto;
import org.siemac.metamac.srm.web.category.model.ds.EniSchemaDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class EniSchemaRecord extends ListGridRecord {

    public EniSchemaRecord() {

    }

    public void setValue(String value) {
        setAttribute(EniSchemaDS.VALUE, value);
    }

    public String getValue() {
        return getAttributeAsString(EniSchemaDS.VALUE);
    }

    public void setIdentifier(String identifier) {
        setAttribute(EniSchemaDS.IDENTIFIER, identifier);
    }

    public String getIdentifier() {
        return getAttributeAsString(EniSchemaDS.IDENTIFIER);
    }

    public void setEniSchemaDto(EniSchemaCatalogDto eniSchemaDto) {
        setAttribute(EniSchemaDS.DTO, eniSchemaDto);
    }

    public EniSchemaCatalogDto getEniSchemaDto() {
        return (EniSchemaCatalogDto) getAttributeAsObject(EniSchemaDS.DTO);
    }
}
