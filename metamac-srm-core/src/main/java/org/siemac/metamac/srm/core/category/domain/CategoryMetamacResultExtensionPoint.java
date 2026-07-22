package org.siemac.metamac.srm.core.category.domain;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;

public class CategoryMetamacResultExtensionPoint {

    private CategoryElementResult  categoryElement;
    private String                 categoryElementCode;
    private List<EniSchemaCatalog> eniSchemas                           = new ArrayList<EniSchemaCatalog>();
    private List<String>           eniSchemaCodes                       = new ArrayList<String>();
    private Integer                orderConfiguration;
    private String                 orderConfigurationConcatenatedByLevel;

    public CategoryElementResult getCategoryElement() {
        return categoryElement;
    }

    public void setCategoryElement(CategoryElementResult categoryElement) {
        this.categoryElement = categoryElement;
    }

    public String getCategoryElementCode() {
        return categoryElementCode;
    }

    public void setCategoryElementCode(String categoryElementCode) {
        this.categoryElementCode = categoryElementCode;
    }

    public List<EniSchemaCatalog> getEniSchemas() {
        return eniSchemas;
    }

    public void setEniSchemas(List<EniSchemaCatalog> eniSchemas) {
        this.eniSchemas = eniSchemas;
    }

    public void addEniSchema(EniSchemaCatalog eniSchemaElement) {
        getEniSchemas().add(eniSchemaElement);
    }

    public List<String> getEniSchemaCodes() {
        return eniSchemaCodes;
    }

    public void setEniSchemaCodes(List<String> eniSchemaCodes) {
        this.eniSchemaCodes = eniSchemaCodes;
    }

    public void addEniSchemaCodes(String eniSchemaCode) {
        getEniSchemaCodes().add(eniSchemaCode);
    }

    public Integer getOrderConfiguration() {
        return orderConfiguration;
    }

    public void setOrderConfiguration(Integer orderConfiguration) {
        this.orderConfiguration = orderConfiguration;
    }

    public String getOrderConfigurationConcatenatedByLevel() {
        return orderConfigurationConcatenatedByLevel;
    }

    public void setOrderConfigurationConcatenatedByLevel(String orderConfigurationConcatenatedByLevel) {
        this.orderConfigurationConcatenatedByLevel = orderConfigurationConcatenatedByLevel;
    }

    public int getLevel() {
        if (orderConfigurationConcatenatedByLevel == null) {
            return 1;
        }
        int count = 0;
        for (int i = 0; i < orderConfigurationConcatenatedByLevel.length(); i++) {
            if (orderConfigurationConcatenatedByLevel.charAt(i) == '/') {
                count++;
            }
        }
        return count;
    }
}
