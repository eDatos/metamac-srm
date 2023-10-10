package org.siemac.metamac.srm.core.category.domain;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;

public class CategoryMetamacResultExtensionPoint {

    private CategoryElementResult  categoryElement;
    private String                 categoryElementCode;
    private List<EniSchemaCatalog> eniSchemas     = new ArrayList<EniSchemaCatalog>();
    private List<String>           eniSchemaCodes = new ArrayList<String>();

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
}
