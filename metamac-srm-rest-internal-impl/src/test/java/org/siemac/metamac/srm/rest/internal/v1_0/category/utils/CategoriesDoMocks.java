package org.siemac.metamac.srm.rest.internal.v1_0.category.utils;

import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeOrderVisualisation;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacDoMocks;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public class CategoriesDoMocks {

    public static CategorySchemeVersionMetamac mockCategoryScheme(String agencyID, String resourceID, String version) {
        CategorySchemeVersionMetamac target = CategoriesMetamacDoMocks.mockCategorySchemeFixedValues(agencyID, resourceID, version);
        addConfigurationsMetadatasToCategoryScheme(target);
        return target;
    }

    private static void addConfigurationsMetadatasToCategoryScheme(CategorySchemeVersionMetamac target) {
        target.addOrderVisualisation(mockCategorySchemeOrderVisualisation("order1"));
        target.addOrderVisualisation(mockCategorySchemeOrderVisualisation("order2"));
        target.addOrderVisualisation(mockCategorySchemeOrderVisualisation("order3"));
        target.setDefaultOrderVisualisation(target.getOrderVisualisations().get(0));
    }

    private static CategorySchemeOrderVisualisation mockCategorySchemeOrderVisualisation(String code) {
        CategorySchemeOrderVisualisation target = new CategorySchemeOrderVisualisation();
        target.setNameableArtefact(new NameableArtefact());
        target.getNameableArtefact().setCode(code);
        target.getNameableArtefact().setName(BaseDoMocks.mockInternationalStringFixedValues("order", code));
        return target;
    }

    public static CategoryMetamac mockCategory(String resourceID, CategorySchemeVersionMetamac categoryScheme, CategoryMetamac parent) {
        CategoryMetamac target = CategoriesMetamacDoMocks.mockCategoryFixedValues(resourceID, categoryScheme, parent);
        if (parent == null) {
            target.getNameableArtefact().setCodeFull(resourceID);
        } else {
            target.getNameableArtefact().setCodeFull(parent.getNameableArtefact().getCodeFull() + "." + resourceID);
        }
        target.setCategoryElement(CategoriesMetamacDoMocks.mockCategoryElementFixedValues("categoryElement-" + resourceID));
        return target;
    }

    public static CategoryMetamac mockCategoryWithEniScheme(String resourceID, CategorySchemeVersionMetamac categoryScheme, CategoryMetamac parent) {
        CategoryMetamac target = mockCategory(resourceID, categoryScheme, parent);

        for (int i = 0; i < 2; i++) {
            EniSchemaCatalog eniSchema = CategoriesMetamacDoMocks.mockEniSchemeFixedValues(resourceID + "-" + i);
            target.getEniSchemas().add(eniSchema);
        }

        target.setCategoryElement(CategoriesMetamacDoMocks.mockCategoryElementFixedValues("categoryElement-" + resourceID));
        return target;
    }

    public static CategorySchemeVersionMetamac mockCategorySchemeWithCategories(String agencyID, String resourceID, String version) {

        CategorySchemeVersionMetamac categorySchemeVersion = mockCategoryScheme(agencyID, resourceID, version);

        // categorys
        CategoryMetamac category1 = mockCategoryWithEniScheme("category1", categorySchemeVersion, null);
        CategoryMetamac category2 = mockCategoryWithEniScheme("category2", categorySchemeVersion, null);
        CategoryMetamac category2A = mockCategoryWithEniScheme("category2A", categorySchemeVersion, category2);
        CategoryMetamac category2B = mockCategoryWithEniScheme("category2B", categorySchemeVersion, category2);

        // categorys hierarchy
        categorySchemeVersion.addItem(category1);
        categorySchemeVersion.addItemsFirstLevel(category1);
        categorySchemeVersion.addItem(category2);
        categorySchemeVersion.addItemsFirstLevel(category2);
        categorySchemeVersion.addItem(category2A);
        categorySchemeVersion.addItem(category2B);
        category2.addChildren(category2A);
        category2.addChildren(category2B);

        return categorySchemeVersion;
    }

    public static ItemResult mockCategoryItemResult(String resourceID, ItemResult parent) {
        ItemResult itemResult = CategoriesMetamacDoMocks.mockCategoryResultFixedValues(resourceID, parent);
        itemResult.setUriProvider(null);
        itemResult.setUrn(itemResult.getUrn().replace("urn:sdmx:org.sdmx.infomodel.xxx=", "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=agency1:itemScheme1(01.000)."));
        itemResult.setUrnProvider(itemResult.getUrn());

        itemResult.setExtensionPoint(CategoriesMetamacDoMocks.mockCategoryMetamacResultFixedValues());

        return itemResult;
    }

    public static ItemResult mockCategoryItemResultWithOrderConfiguration(String resourceID, ItemResult parent, int orderConfiguration) {
        ItemResult itemResult = CategoriesMetamacDoMocks.mockCategoryResultFixedValuesWithOrderConfiguration(resourceID, parent, orderConfiguration);
        itemResult.setUriProvider(null);
        itemResult.setUrn(itemResult.getUrn().replace("urn:sdmx:org.sdmx.infomodel.xxx=", "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=agency1:itemScheme1(01.000)."));
        itemResult.setUrnProvider(itemResult.getUrn());
        return itemResult;
    }

    public static ItemResult mockCategoryResultFixedValuesWithExtraFields(String resourceID, ItemResult parent) {
        ItemResult itemResult = CategoriesMetamacDoMocks.mockCategoryResultFixedValuesWithExtraFields(resourceID, parent);
        itemResult.setUriProvider(null);
        itemResult.setUrn(itemResult.getUrn().replace("urn:sdmx:org.sdmx.infomodel.xxx=", "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=agency1:itemScheme1(01.000)."));
        itemResult.setUrnProvider(itemResult.getUrn());
        return itemResult;
    }

    public static Categorisation mockCategorisation(String agencyID, String resourceID, String version) {
        Categorisation target = new Categorisation();
        target.setMaintainableArtefact(new MaintainableArtefact());
        CategoriesMetamacDoMocks.mockMaintainableArtefactFixedValues(target.getMaintainableArtefact(), agencyID, resourceID, version);

        CategorySchemeVersionMetamac categorySchemeVersionMetamac = mockCategoryScheme(agencyID, "categoryScheme-" + resourceID, version);
        target.setArtefactCategorised(categorySchemeVersionMetamac.getMaintainableArtefact());
        target.setCategory(mockCategory("category-" + resourceID, categorySchemeVersionMetamac, null));
        return target;
    }

}