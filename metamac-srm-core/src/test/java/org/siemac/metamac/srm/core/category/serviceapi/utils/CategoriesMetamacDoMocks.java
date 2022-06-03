package org.siemac.metamac.srm.core.category.serviceapi.utils;

import static org.siemac.metamac.common.test.utils.MetamacMocks.mockString;

import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;

import com.arte.statistic.sdmx.srm.core.category.serviceapi.utils.CategoriesDoMocks;
import com.arte.statistic.sdmx.srm.core.common.domain.InternationalString;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.LocalisedString;

public class CategoriesMetamacDoMocks extends CategoriesDoMocks {

    public static String BASE_ENI_SCHEME_IDENTIFIER = "ID - ";
    
    public static CategorySchemeVersionMetamac mockCategoryScheme(OrganisationMetamac maintainer) {
        CategorySchemeVersionMetamac categorySchemeVersion = new CategorySchemeVersionMetamac();
        mockCategoryScheme(categorySchemeVersion, maintainer);
        return categorySchemeVersion;
    }

    public static CategorySchemeVersionMetamac mockCategorySchemeFixedValues(String agencyID, String resourceID, String version) {
        CategorySchemeVersionMetamac target = new CategorySchemeVersionMetamac();
        CategoriesDoMocks.mockCategorySchemeFixedValues(target, agencyID, resourceID, version);

        // metamac
        target.setLifeCycleMetadata(BaseDoMocks.mockLifeCycleExternallyPublished());
        return target;
    }

    public static CategoryMetamac mockCategory() {
        CategoryMetamac category = new CategoryMetamac();
        mockCategory(category);
        return category;
    }

    public static CategoryMetamac mockCategoryFixedValues(String resourceID, CategorySchemeVersionMetamac itemSchemeVersion, CategoryMetamac parent) {
        CategoryMetamac target = new CategoryMetamac();
        CategoriesDoMocks.mockCategoryFixedValues(target, resourceID, itemSchemeVersion, parent);
        return target;
    }
    
    public static CategoryMetamacResultExtensionPoint mockCategoryMetamacResultFixedValues() {
        CategoryMetamacResultExtensionPoint target = new CategoryMetamacResultExtensionPoint();
        target.setEniSchemeCatalog(mockEniSchemeFixedValues("eniSchemeText"));
        target.setEniSchemeIdentifier(target.getEniSchemeCatalog().getIdentifier());
        return target;
    }
    
    public static InternationalString mockInternationalString(String base) {
        InternationalString internationalString = new InternationalString();
        LocalisedString es = new LocalisedString();
        es.setLabel(base + "-" + mockString(10) + " en Español");
        es.setLocale("es");
        LocalisedString en = new LocalisedString();
        en.setLabel(base + "-" + mockString(10) + " in English");
        en.setLocale("en");
        LocalisedString ca = new LocalisedString();
        en.setLabel(base + "-" + mockString(10) + " en catalá");
        en.setLocale("ca");
        internationalString.addText(es);
        internationalString.addText(en);
        internationalString.addText(ca);
        return internationalString;
    }
    
    public static EniSchemaCatalog mockEniSchemeFixedValues(String baseEniSchemeType) {
        EniSchemaCatalog eniSchemeType = new EniSchemaCatalog(mockInternationalString("desc-" + baseEniSchemeType));
        eniSchemeType.setIdentifier(BASE_ENI_SCHEME_IDENTIFIER + baseEniSchemeType);
        eniSchemeType.setVersion(0L);
        return eniSchemeType;
    }
    
    public static ItemResult mockCategoryResultFixedValuesWithExtraFields(String resourceID, ItemResult parent) {
        ItemResult itemResult = mockCategoryResultFixedValues(resourceID, parent);
        itemResult.setExtensionPoint(mockCategoryMetamacResultFixedValues());
        return itemResult;
    }
}
