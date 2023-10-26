package org.siemac.metamac.srm.rest.internal.v1_0.category.utils;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.EniSchemaTypes;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.ResourceInternal;
import org.siemac.metamac.srm.core.category.domain.CategoryElement;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.siemac.metamac.srm.rest.common.SrmRestConstants;
import org.siemac.metamac.srm.rest.internal.v1_0.utils.Asserts;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public class CategoriesAsserts extends Asserts {

    public static void assertEqualsResource(CategorySchemeVersionMetamac expected, ResourceInternal actual) {
        MaintainableArtefact maintainableArtefact = expected.getMaintainableArtefact();
        String agency = maintainableArtefact.getMaintainer().getIdAsMaintainer();
        String code = maintainableArtefact.getCode();
        String version = maintainableArtefact.getVersionLogic();
        String expectedSelfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/categoryschemes/" + agency + "/" + code + "/" + version;
        String expectedManagementLink = "http://localhost:8080/metamac-srm-web/#structuralResources/categorySchemes/categoryScheme;id=" + agency + ":" + code + "(" + version + ")";
        Asserts.assertEqualsResource(expected, SrmRestConstants.KIND_CATEGORY_SCHEME, expectedSelfLink, expectedManagementLink, actual);
    }

    public static void assertEqualsResource(ItemSchemeVersion itemSchemeVersion, CategoryMetamac expected, ItemResult expectedItemResult, ResourceInternal actual) {
        MaintainableArtefact maintainableArtefact = itemSchemeVersion.getMaintainableArtefact();
        String agency = maintainableArtefact.getMaintainer().getIdAsMaintainer();
        String codeItemScheme = maintainableArtefact.getCode();
        String version = maintainableArtefact.getVersionLogic();
        String codeFullExpected = null;
        if (expected != null) {
            codeFullExpected = expected.getNameableArtefact().getCodeFull();
        } else if (expectedItemResult != null) {
            codeFullExpected = expectedItemResult.getCodeFull();
        }
        String expectedSelfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/categoryschemes/" + agency + "/" + codeItemScheme + "/" + version + "/categories/" + codeFullExpected;
        String expectedManagementLink = "http://localhost:8080/metamac-srm-web/#structuralResources/categorySchemes/categoryScheme;id=" + agency + ":" + codeItemScheme + "(" + version
                + ")/category;id=" + codeFullExpected;

        if (expected != null) {
            Asserts.assertEqualsResource(expected, SrmRestConstants.KIND_CATEGORY, expectedSelfLink, expectedManagementLink, actual);
        } else if (expectedItemResult != null) {
            Asserts.assertEqualsResource(expectedItemResult, SrmRestConstants.KIND_CATEGORY, expectedSelfLink, expectedManagementLink, actual,
                    itemSchemeVersion.getMaintainableArtefact().getIsImported());
        }
        assertEquals(codeFullExpected, actual.getNestedId());
    }

    public static void assertEqualsResource(Categorisation expected, ResourceInternal actual) {
        MaintainableArtefact maintainableArtefact = expected.getMaintainableArtefact();
        String expectedSelfLink = "http://data.istac.es/apis/structural-resources-internal/v1.0/categorisations/" + maintainableArtefact.getMaintainer().getIdAsMaintainer() + "/"
                + maintainableArtefact.getCode() + "/" + maintainableArtefact.getVersionLogic();

        assertEquals(SrmRestConstants.KIND_CATEGORISATION, actual.getKind());
        assertEquals(expected.getMaintainableArtefact().getCode(), actual.getId());
        assertEquals(expected.getMaintainableArtefact().getUrn(), actual.getUrn());
        if (expected.getMaintainableArtefact().getIsImported()) {
            assertEquals(expected.getMaintainableArtefact().getUrnProvider(), actual.getUrnProvider());
        } else {
            assertEquals(null, actual.getUrnProvider());
        }
        assertEquals(SrmRestConstants.KIND_CATEGORISATION, actual.getSelfLink().getKind());
        assertEquals(expectedSelfLink, actual.getSelfLink().getHref());
        assertEqualsInternationalString(expected.getMaintainableArtefact().getName(), actual.getName());
    }

    public static void assertEqualsCategoryElementResult(CategoryElement categoryElement, ResourceInternal categoryElementResult) {
        String managementAppLinkExpected = "http://localhost:8080/metamac-srm-web/#structuralResources/categoryElement;id=" + categoryElement.getIdentifiableArtefact().getCode();
        String selfLinkHrefExpected = "http://data.istac.es/apis/structural-resources-internal/v1.0/categoryelements/" + categoryElement.getIdentifiableArtefact().getCode();
        String kindExpected = "structuralResources#categoryElement";
        assertEquals(categoryElement.getIdentifiableArtefact().getCode(), categoryElementResult.getId());
        assertEquals(categoryElement.getIdentifiableArtefact().getUrn(), categoryElementResult.getUrn());
        assertEquals(kindExpected, categoryElementResult.getKind());
        assertEquals(managementAppLinkExpected, categoryElementResult.getManagementAppLink());
        assertEquals(selfLinkHrefExpected, categoryElementResult.getSelfLink().getHref());
    }

    public static void assertEqualsEniSchemaResult(List<EniSchemaCatalog> eniSchemas, EniSchemaTypes eniSchemaTypes) {
        assertEquals(eniSchemas.size(), eniSchemaTypes.getEniSchemaTypes().size());
        for (int i = 0; i < eniSchemas.size(); i++) {
            assertEquals(eniSchemas.get(i).getIdentifier(), eniSchemaTypes.getEniSchemaTypes().get(i).getId());
        }
    }
}