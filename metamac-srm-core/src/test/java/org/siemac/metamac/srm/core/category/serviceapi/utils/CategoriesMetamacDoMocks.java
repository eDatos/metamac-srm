package org.siemac.metamac.srm.core.category.serviceapi.utils;

import org.joda.time.DateTime;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.category.domain.CategoryElement;
import org.siemac.metamac.srm.core.category.domain.CategoryElementResult;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeOrderVisualisation;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.common.SrmResourceElementMock;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;

import com.arte.statistic.sdmx.srm.core.base.domain.AnnotationType;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
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
        EniSchemaCatalog eniSchema = mockEniSchemeFixedValues("eniSchemeText");
        target.getEniSchemas().add(eniSchema);
        target.setCategoryElement(mockCategoryElementResultFixedValues("categoryElement1"));
        target.getEniSchemaCodes().add(eniSchema.getIdentifier());
        return target;
    }

    public static EniSchemaCatalog mockEniSchemeFixedValues(String baseEniSchemeType) {
        EniSchemaCatalog eniSchemeType = new EniSchemaCatalog(mockInternationalString("desc-" + baseEniSchemeType));
        eniSchemeType.setIdentifier(BASE_ENI_SCHEME_IDENTIFIER + baseEniSchemeType);
        eniSchemeType.setVersion(0L);
        return eniSchemeType;
    }

    public static CategoryElementResult mockCategoryElementResultFixedValues(String baseCategoryElement) {
        CategoryElementResult categoryElementResult = new CategoryElementResult();
        CategoryElement categoryElement = mockCategoryElementFixedValues("categoryElement1");
        categoryElementResult.setAnnotableArtefactIdDatabase(categoryElement.getIdentifiableArtefact().getId());
        categoryElementResult.setCode(categoryElement.getIdentifiableArtefact().getCode());
        categoryElementResult.setUrn(categoryElement.getIdentifiableArtefact().getUrn());
        categoryElementResult.setUrnProvider(categoryElement.getIdentifiableArtefact().getUrnProvider());
        return categoryElementResult;
    }

    public static ItemResult mockCategoryResultFixedValuesWithExtraFields(String resourceID, ItemResult parent) {
        ItemResult itemResult = mockCategoryResultFixedValues(resourceID, parent);
        itemResult.setExtensionPoint(mockCategoryMetamacResultFixedValues());
        return itemResult;
    }

    public static ItemResult mockCategoryResultFixedValuesWithOrderConfiguration(String resourceID, ItemResult parent, int orderConfiguration) {
        ItemResult itemResult = mockCategoryResultFixedValues(resourceID, parent);
        CategoryMetamacResultExtensionPoint extensionPoint = mockCategoryMetamacResultFixedValues();
        extensionPoint.setOrderConfiguration(orderConfiguration);
        itemResult.setExtensionPoint(extensionPoint);
        return itemResult;
    }

    public static CategoryElement mockCategoryElement() {
        CategoryElement categoryElement = new CategoryElement();
        SrmResourceElementMock.mockResourceElement(categoryElement);
        return categoryElement;
    }

    public static CategoryElement mockCategoryElementFixedValues(String codeCategoryElement) {
        CategoryElement categoryElement = new CategoryElement();
        SrmResourceElementMock.mockResourceElementAllMetadata(categoryElement, codeCategoryElement);
        return categoryElement;
    }

    // ------------------------------------------------------------------------------------
    // CATEGORY SCHEME ORDER VISUALISATIONS
    // ------------------------------------------------------------------------------------

    public static CategorySchemeOrderVisualisation mockCategorySchemeOrderVisualisation(AnnotationType annotationType) {
        CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = new CategorySchemeOrderVisualisation();
        categorySchemeOrderVisualisation.setNameableArtefact(mockNameableArtefact("code-" + MetamacMocks.mockString(5), annotationType));
        return categorySchemeOrderVisualisation;
    }

    public static CategorySchemeOrderVisualisation mockCategorySchemeOrderVisualisationFixedValues(String resourceID) {
        CategorySchemeOrderVisualisation target = new CategorySchemeOrderVisualisation();
        target.getUuid(); // generate uuid
        target.setVersion(Long.valueOf(1));
        target.setCreatedBy("userCreatedBy");
        target.setCreatedDate(new DateTime(2012, 10, 1, 10, 12, 13, 14));
        target.setLastUpdatedBy("userLastUpdated");
        target.setLastUpdated(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        target.setUpdateDate(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        target.setNameableArtefact(new NameableArtefact());
        mockNameableArtefactFixedValues(target.getNameableArtefact(), resourceID, null);
        return target;
    }
}
