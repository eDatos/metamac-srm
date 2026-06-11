package org.siemac.metamac.srm.core.category.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.srm.core.category.domain.CategoryElement;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeOrderVisualisation;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeVisualisationDto;
import org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacAsserts;
import org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacDoMocks;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class CategoriesDo2DtoMapperTest extends SrmBaseTest {

    @Autowired
    private CategoriesDo2DtoMapper categoriesDo2DtoMapper;

    @Test
    public void testCategorySchemeMetamacDoToDto() {
        CategorySchemeVersionMetamac entity = CategoriesMetamacDoMocks.mockCategorySchemeFixedValues("agency01", "categoryScheme01", "01.000");
        CategorySchemeMetamacDto dto = categoriesDo2DtoMapper.categorySchemeMetamacDoToDto(entity);
        CategoriesMetamacAsserts.assertEqualsCategoryScheme(entity, dto);
    }

    @Test
    public void testCategorySchemeMetamacDoToBasicDto() {
        CategorySchemeVersionMetamac entity = CategoriesMetamacDoMocks.mockCategorySchemeFixedValues("agency01", "categoryScheme01", "01.000");
        entity.setDefaultOrderVisualisation(mockCategorySchemeOrderVisualisationWithAllMetadata());
        CategorySchemeMetamacBasicDto dto = categoriesDo2DtoMapper.categorySchemeMetamacDoToBasicDto(entity);
        CategoriesMetamacAsserts.assertEqualsCategoryScheme(entity, dto);
    }

    @Test
    public void testCategoryMetamacDoToDto() {
        CategoryMetamac entity = mockCategoryWithAllMetadata();

        CategoryMetamacDto dto = categoriesDo2DtoMapper.categoryMetamacDoToDto(entity);
        CategoriesMetamacAsserts.assertEqualsCategory(entity, dto);
    }

    @Test
    public void testCategoryMetamacDoToBasicDto() {
        CategoryMetamac entity = mockCategoryWithAllMetadata();
        CategoryMetamacBasicDto dto = categoriesDo2DtoMapper.categoryMetamacDoToBasicDto(entity);
        CategoriesMetamacAsserts.assertEqualsCategory(entity, dto);
    }

    private CategoryMetamac mockCategoryWithAllMetadata() {
        CategorySchemeVersionMetamac categoryScheme = CategoriesMetamacDoMocks.mockCategorySchemeFixedValues("agency01", "categoryScheme01", "01.000");
        CategoryMetamac entity = CategoriesMetamacDoMocks.mockCategoryFixedValues("category01", categoryScheme, null);
        return entity;
    }

    @Test
    public void testCategorySchemeOrderVisualisationDoToDto() {
        CategorySchemeOrderVisualisation entity = mockCategorySchemeOrderVisualisationWithAllMetadata();

        CategorySchemeVisualisationDto dto = categoriesDo2DtoMapper.categorySchemeOrderVisualisationDoToDto(entity);
        CategoriesMetamacAsserts.assertEqualsCategorySchemeOrderVisualisation(entity, dto);
    }

    @Test
    public void testCategoryElementDoToDto() {
        CategoryElement entity = mockCategoryElementWithAllMetadata();
        mockCategoryElementWithAllMetadata();

        CategoryElementDto dto = categoriesDo2DtoMapper.categoryElementDoToDto(entity);
        CategoriesMetamacAsserts.assertEqualsCategoryElement(entity, dto);
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCategoriesTest.xml";
    }

    private CategorySchemeOrderVisualisation mockCategorySchemeOrderVisualisationWithAllMetadata() {
        return CategoriesMetamacDoMocks.mockCategorySchemeOrderVisualisationFixedValues("order01");
    }

    private CategoryElement mockCategoryElementWithAllMetadata() {
        return CategoriesMetamacDoMocks.mockCategoryElementFixedValues("categoryElement01");
    }

}