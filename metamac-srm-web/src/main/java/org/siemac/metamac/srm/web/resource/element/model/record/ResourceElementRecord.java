package org.siemac.metamac.srm.web.resource.element.model.record;

import org.siemac.metamac.srm.web.resource.element.model.ds.ResourceElementDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ResourceElementRecord extends ListGridRecord {

    public ResourceElementRecord() {
    }

    public ResourceElementRecord(Long id, String code, String shortName, String urn) {
        setId(id);
        setCode(code);
        setShortName(shortName);
        setUrn(urn);
    }

    public void setId(Long id) {
        setAttribute(ResourceElementDS.ID, id);
    }

    public void setCode(String code) {
        setAttribute(ResourceElementDS.CODE, code);
    }

    public void setShortName(String value) {
        setAttribute(ResourceElementDS.SHORT_NAME, value);
    }

    public void setUrn(String value) {
        setAttribute(ResourceElementDS.URN, value);
    }

    public String getCode() {
        return getAttributeAsString(ResourceElementDS.CODE);
    }

    public String getUrn() {
        return getAttributeAsString(ResourceElementDS.URN);
    }

}
