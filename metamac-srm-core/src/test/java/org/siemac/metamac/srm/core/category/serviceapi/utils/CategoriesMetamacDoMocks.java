package org.siemac.metamac.srm.core.category.serviceapi.utils;

import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.category.domain.CategoryElement;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.common.SrmResourceElementMock;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;

import com.arte.statistic.sdmx.srm.core.base.domain.AnnotationType;
import com.arte.statistic.sdmx.srm.core.category.serviceapi.utils.CategoriesDoMocks;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public class CategoriesMetamacDoMocks extends CategoriesDoMocks {

    public static String BASE_ENI_SCHEME_IDENTIFIER = "ID - ";

    public static CategorySchemeVersionMetamac mockCategoryScheme(OrganisationMetamac maintainer, AnnotationType annotationType) {
        CategorySchemeVersionMetamac categorySchemeVersion = new CategorySchemeVersionMetamac();
        mockCategoryScheme(categorySchemeVersion, maintainer, annotationType);
        return categorySchemeVersion;
    }

    public static CategorySchemeVersionMetamac mockCategorySchemeFixedValues(String agencyID, String resourceID, String version) {
        CategorySchemeVersionMetamac target = new CategorySchemeVersionMetamac();
        CategoriesDoMocks.mockCategorySchemeFixedValues(target, agencyID, resourceID, version);

        // metamac
        target.setLifeCycleMetadata(BaseDoMocks.mockLifeCycleExternallyPublished());
        return target;
    }

    public static CategoryMetamac mockCategory(AnnotationType annotationType) {
        CategoryMetamac category = new CategoryMetamac();
        mockCategory(category, annotationType);
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

    public static CategoryElement mockCategoryElementGeographical(AnnotationType annotationType) {
        CategoryElement categoryElement = mockCategoryElement(annotationType);
        return categoryElement;
    }

    public static CategoryElement mockCategoryElement(AnnotationType annotationType) {
        CategoryElement categoryElement = new CategoryElement();
        SrmResourceElementMock.mockResourceElement(categoryElement, annotationType);
        return categoryElement;
    }
}
