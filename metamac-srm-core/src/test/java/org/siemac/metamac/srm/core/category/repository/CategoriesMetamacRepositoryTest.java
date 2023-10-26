package org.siemac.metamac.srm.core.category.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.srm.core.category.domain.CategoryElementResult;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacRepository;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacResultSelection;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.srm.core.category.serviceapi.utils.CategoriesAsserts;
import com.arte.statistic.sdmx.srm.core.code.serviceapi.utils.CodesAsserts;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class CategoriesMetamacRepositoryTest extends SrmBaseTest {

    @Autowired
    protected CategoryMetamacRepository categoryMetamacRepository;

    @Test
    public void testRetrieveCodesOrderedByCodelistUrn() throws Exception {

        ItemMetamacResultSelection selection = new CategoryMetamacResultSelection(true, true, true, true, false, true, false, false);
        // Retrieve
        List<ItemResult> codes = categoryMetamacRepository.findCategoriesByCategorySchemeOrderedInDepth(Long.valueOf(12), selection);

        // Validate common metadata
        assertEquals(8, codes.size());
        int i = 0;
        {
            ItemResult code = codes.get(i++);
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, code.getUrnProvider());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, code.getUrn());
            assertEquals(null, code.getUriProvider());
            assertEquals("CATEGORY01", code.getCode());
            assertEquals("CATEGORY01", code.getCodeFull());
            assertEquals(null, code.getParent());
            assertEquals(2, code.getName().size());
            CategoriesAsserts.assertEqualsInternationalString(code.getName(), "es", "Nombre categoryScheme-1-v2-category-1", "en", "Name categoryScheme-1-v2-category-1");
            CategoriesAsserts.assertEqualsInternationalString(code.getDescription(), "es", "Descripción categoryScheme-1-v2-category-1", null, null);
            {
                CategoryElementResult categoryElement = SrmServiceUtils.getCategoryItemResultCategoryElementFull(code);
                assertEquals(CATEGORY_ELEMENT_02, categoryElement.getCode());
                assertEquals(CATEGORY_ELEMENT_2_URN, categoryElement.getUrn());
                CodesAsserts.assertEqualsInternationalString(categoryElement.getShortName(), "es", "shortName category_element-2", null, null);
            }

        }
        {
            ItemResult code = codes.get(i++);
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, code.getUrnProvider());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, code.getUrn());
            assertEquals(null, code.getUriProvider());
            assertEquals("CATEGORY02", code.getCode());
            assertEquals("CATEGORY02", code.getCodeFull());
            assertEquals(null, code.getParent());
            assertEquals(1, code.getName().size());
            CategoriesAsserts.assertEqualsInternationalString(code.getName(), "es", "Nombre categoryScheme-1-v2-category-2", null, null);
            CategoriesAsserts.assertEqualsInternationalString(code.getDescription(), null, null, null, null);
            {
                CategoryElementResult categoryElement = SrmServiceUtils.getCategoryItemResultCategoryElementFull(code);
                assertEquals(CATEGORY_ELEMENT_03, categoryElement.getCode());
                assertEquals(CATEGORY_ELEMENT_3_URN, categoryElement.getUrn());
                CodesAsserts.assertEqualsInternationalString(categoryElement.getShortName(), "es", "shortName category_element-3", null, null);
            }

        }
        {
            ItemResult code = codes.get(i++);
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_3, code.getUrnProvider());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_3, code.getUrn());
            assertEquals(null, code.getUriProvider());
            assertEquals("CATEGORY03", code.getCode());
            assertEquals("CATEGORY03", code.getCodeFull());
            assertEquals(null, code.getParent());
            assertEquals(2, code.getName().size());
            CategoriesAsserts.assertEqualsInternationalString(code.getName(), "es", "nombre category-3", "en", "name category-3");
            CategoriesAsserts.assertEqualsInternationalString(code.getDescription(), null, null, null, null);
            {
                CategoryElementResult categoryElement = SrmServiceUtils.getCategoryItemResultCategoryElementFull(code);
                assertEquals(CATEGORY_ELEMENT_02, categoryElement.getCode());
                assertEquals(CATEGORY_ELEMENT_2_URN, categoryElement.getUrn());
                CodesAsserts.assertEqualsInternationalString(categoryElement.getShortName(), "es", "shortName category_element-2", null, null);
            }

        }
        {
            ItemResult code = codes.get(i++);
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4, code.getUrnProvider());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4, code.getUrn());
            assertEquals(null, code.getUriProvider());
            assertEquals("CATEGORY04", code.getCode());
            assertEquals("CATEGORY04", code.getCodeFull());
            assertEquals(null, code.getParent());
            assertEquals(1, code.getName().size());
            CategoriesAsserts.assertEqualsInternationalString(code.getName(), "es", "Lanzarote", null, null);
            CategoriesAsserts.assertEqualsInternationalString(code.getDescription(), null, null, null, null);
            {
                CategoryElementResult categoryElement = SrmServiceUtils.getCategoryItemResultCategoryElementFull(code);
                assertEquals(CATEGORY_ELEMENT_01, categoryElement.getCode());
                assertEquals(CATEGORY_ELEMENT_1_URN, categoryElement.getUrn());
                CodesAsserts.assertEqualsInternationalString(categoryElement.getShortName(), "es", "shortName category_element-1", null, null);
            }

        }
        {
            ItemResult code = codes.get(i++);
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1, code.getUrnProvider());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1, code.getUrn());
            assertEquals(null, code.getUriProvider());
            assertEquals("CATEGORY0201", code.getCode());
            assertEquals("CATEGORY02.CATEGORY0201", code.getCodeFull());
            assertEquals("CATEGORY02", code.getParent().getCode());
            assertEquals(2, code.getName().size());
            CategoriesAsserts.assertEqualsInternationalString(code.getName(), "es", "Nombre categoryScheme-1-v2-category-2-1", "en", "Name categoryScheme-1-v2-category-2-1");
            CategoriesAsserts.assertEqualsInternationalString(code.getDescription(), "es", "Descripción cat2-1", "en", "Description cat2-1");
            {
                CategoryElementResult categoryElement = SrmServiceUtils.getCategoryItemResultCategoryElementFull(code);
                assertEquals(CATEGORY_ELEMENT_01, categoryElement.getCode());
                assertEquals(CATEGORY_ELEMENT_1_URN, categoryElement.getUrn());
                CodesAsserts.assertEqualsInternationalString(categoryElement.getShortName(), "es", "shortName category_element-1", null, null);
            }

        }
        {
            ItemResult code = codes.get(i++);
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1, code.getUrnProvider());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1, code.getUrn());
            assertEquals(null, code.getUriProvider());
            assertEquals("CATEGORY0401", code.getCode());
            assertEquals("CATEGORY04.CATEGORY0401", code.getCodeFull());
            assertEquals("CATEGORY04", code.getParent().getCode());
            assertEquals(1, code.getName().size());
            CategoriesAsserts.assertEqualsInternationalString(code.getName(), "es", "La Gomera", null, null);
            CategoriesAsserts.assertEqualsInternationalString(code.getDescription(), null, null, null, null);
            {
                CategoryElementResult categoryElement = SrmServiceUtils.getCategoryItemResultCategoryElementFull(code);
                assertEquals(CATEGORY_ELEMENT_02, categoryElement.getCode());
                assertEquals(CATEGORY_ELEMENT_2_URN, categoryElement.getUrn());
                CodesAsserts.assertEqualsInternationalString(categoryElement.getShortName(), "es", "shortName category_element-2", null, null);
            }

        }
        {
            ItemResult code = codes.get(i++);
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, code.getUrnProvider());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, code.getUrn());
            assertEquals(null, code.getUriProvider());
            assertEquals("CATEGORY020101", code.getCode());
            assertEquals("CATEGORY02.CATEGORY0201.CATEGORY020101", code.getCodeFull());
            assertEquals("CATEGORY0201", code.getParent().getCode());
            assertEquals(1, code.getName().size());
            CategoriesAsserts.assertEqualsInternationalString(code.getName(), "es", "Nombre categoryScheme-1-v2-category-2-1-1", null, null);
            CategoriesAsserts.assertEqualsInternationalString(code.getDescription(), "es", "Descripción cat2-1-1", null, null);
            {
                CategoryElementResult categoryElement = SrmServiceUtils.getCategoryItemResultCategoryElementFull(code);
                assertEquals(CATEGORY_ELEMENT_01, categoryElement.getCode());
                assertEquals(CATEGORY_ELEMENT_1_URN, categoryElement.getUrn());
                CodesAsserts.assertEqualsInternationalString(categoryElement.getShortName(), "es", "shortName category_element-1", null, null);
            }

        }
        {
            ItemResult code = codes.get(i++);
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1, code.getUrnProvider());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1, code.getUrn());
            assertEquals(null, code.getUriProvider());
            assertEquals("CATEGORY040101", code.getCode());
            assertEquals("CATEGORY04.CATEGORY0401.CATEGORY040101", code.getCodeFull());
            assertEquals("CATEGORY0401", code.getParent().getCode());
            assertEquals(2, code.getName().size());
            CategoriesAsserts.assertEqualsInternationalString(code.getName(), "es", "Nombre categoryScheme-1-v2-category-4-1-1 Santa Cruz de Tenerife", "en",
                    "Name categoryScheme-1-v2-category-4-1-1");
            CategoriesAsserts.assertEqualsInternationalString(code.getDescription(), null, null, null, null);
            {
                CategoryElementResult categoryElement = SrmServiceUtils.getCategoryItemResultCategoryElementFull(code);
                assertEquals(null, categoryElement);
            }

        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCategoriesTest.xml";
    }
}
