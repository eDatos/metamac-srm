package org.siemac.metamac.srm.core.category.serviceapi;

import static com.arte.statistic.sdmx.srm.core.base.serviceapi.utils.BaseAsserts.assertEqualsInternationalString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsDate;
import static org.siemac.metamac.common.test.utils.MetamacAsserts.assertEqualsMetamacExceptionItem;
import static org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacAsserts.assertEqualsCategoryeElement;
import static org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacAsserts.assertEqualsCategorySchemeOrderVisualisation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.joda.time.tz.DateTimeZoneBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.common.test.utils.DirtyDatabase;
import org.siemac.metamac.common.test.utils.MetamacAsserts;
import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.base.utils.BaseAsserts;
import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;
import org.siemac.metamac.srm.core.category.domain.CategoryElement;
import org.siemac.metamac.srm.core.category.domain.CategoryElementProperties;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeOrderVisualisation;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacRepository;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacResultSelection;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.category.domain.shared.CategoryMetamacVisualisationResult;
import org.siemac.metamac.srm.core.category.domain.shared.CodeCategoryElementNormalisationResult;
import org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacAsserts;
import org.siemac.metamac.srm.core.category.serviceapi.utils.CategoriesMetamacDoMocks;
import org.siemac.metamac.srm.core.code.domain.TaskImportationInfo;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.task.serviceapi.TasksMetamacService;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.GeneratorUrnUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.task.domain.Task;
import com.arte.statistic.sdmx.srm.core.task.domain.TaskResult;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TaskResultTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TaskStatusTypeEnum;
import com.arte.statistic.sdmx.srm.core.category.domain.Category;
import com.arte.statistic.sdmx.srm.core.category.domain.CategoryProperties;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.serviceapi.utils.CategoriesDoMocks;
import com.arte.statistic.sdmx.srm.core.common.domain.InternationalString;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.LocalisedString;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class CategoriesMetamacServiceTest extends SrmBaseTest implements CategoriesMetamacServiceTestBase {

    @Autowired
    private CategoriesMetamacService      categoriesService;

    @Autowired
    private OrganisationMetamacRepository organisationMetamacRepository;

    @Autowired
    private CategoryMetamacRepository     categoryMetamacRepository;

    @Autowired
    private ItemSchemeVersionRepository   itemSchemeRepository;

    @Autowired
    private ItemRepository                itemRepository;

    @PersistenceContext(unitName = "SrmCoreEntityManagerFactory")
    protected EntityManager               entityManager;

    @Autowired
    protected PlatformTransactionManager  transactionManager;

    @Autowired
    private TasksMetamacService           tasksService;

    @Override
    @Test
    public void testCreateCategoryScheme() throws Exception {

        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_1_V1);
        CategorySchemeVersionMetamac categorySchemeVersion = CategoriesMetamacDoMocks.mockCategoryScheme(organisationMetamac, retrieveAnnotationType_1());
        ServiceContext ctx = getServiceContextAdministrador();

        // Create
        CategorySchemeVersionMetamac categorySchemeVersionCreated = categoriesService.createCategoryScheme(ctx, categorySchemeVersion);
        String urn = categorySchemeVersionCreated.getMaintainableArtefact().getUrn();
        assertEquals("01.000", categorySchemeVersionCreated.getMaintainableArtefact().getVersionLogic());
        assertEquals(ctx.getUserId(), categorySchemeVersionCreated.getCreatedBy());

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        CategorySchemeVersionMetamac categorySchemeVersionRetrieved = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
        assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersionRetrieved.getLifeCycleMetadata().getProcStatus());
        assertFalse(categorySchemeVersionRetrieved.getMaintainableArtefact().getIsExternalReference());
        assertNull(categorySchemeVersionRetrieved.getLifeCycleMetadata().getProductionValidationDate());
        assertNull(categorySchemeVersionRetrieved.getLifeCycleMetadata().getProductionValidationUser());
        assertNull(categorySchemeVersionRetrieved.getLifeCycleMetadata().getDiffusionValidationDate());
        assertNull(categorySchemeVersionRetrieved.getLifeCycleMetadata().getDiffusionValidationUser());
        assertNull(categorySchemeVersionRetrieved.getLifeCycleMetadata().getInternalPublicationDate());
        assertNull(categorySchemeVersionRetrieved.getLifeCycleMetadata().getInternalPublicationUser());
        assertNull(categorySchemeVersionRetrieved.getLifeCycleMetadata().getExternalPublicationDate());
        assertNull(categorySchemeVersionRetrieved.getLifeCycleMetadata().getExternalPublicationUser());
        assertFalse(categorySchemeVersionRetrieved.getMaintainableArtefact().getFinalLogicClient());
        assertEquals(ctx.getUserId(), categorySchemeVersionRetrieved.getCreatedBy());
        CategoriesMetamacAsserts.assertEqualsCategoryScheme(categorySchemeVersion, categorySchemeVersionRetrieved);
    }

    @Test
    public void testCreateCategorySchemeErrorMaintainerNotDefault() throws Exception {
        OrganisationMetamac organisationMetamac = organisationMetamacRepository.findByUrn(AGENCY_ROOT_2_V1);
        CategorySchemeVersionMetamac categorySchemeVersion = CategoriesMetamacDoMocks.mockCategoryScheme(organisationMetamac, retrieveAnnotationType_1());

        try {
            categoriesService.createCategoryScheme(getServiceContextAdministrador(), categorySchemeVersion);
            fail("maintainer not default");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.MAINTAINER_MUST_BE_DEFAULT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(AGENCY_ROOT_2_V1, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(AGENCY_ROOT_1_V1, e.getExceptionItems().get(0).getMessageParameters()[1]);
        }
    }

    @Override
    @Test
    public void testUpdateCategoryScheme() throws Exception {
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_2_V1);
        categorySchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        categorySchemeVersion.getMaintainableArtefact().setName(CategoriesMetamacDoMocks.mockInternationalString("name"));

        ServiceContext ctx = getServiceContextAdministrador();
        CategorySchemeVersion categorySchemeVersionUpdated = categoriesService.updateCategoryScheme(ctx, categorySchemeVersion);
        assertNotNull(categorySchemeVersionUpdated);
        assertEquals("user1", categorySchemeVersionUpdated.getCreatedBy());
        assertEquals(ctx.getUserId(), categorySchemeVersionUpdated.getLastUpdatedBy());
    }

    @Test
    public void testUpdateCategorySchemeChangingCode() throws Exception {
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_2_V1);

        // Change code
        categorySchemeVersion.getMaintainableArtefact().setCode("codeNew");
        categorySchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE);
        CategorySchemeVersion categorySchemeVersionUpdated = categoriesService.updateCategoryScheme(getServiceContextAdministrador(), categorySchemeVersion);
        assertEquals("urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:codeNew(01.000)", categorySchemeVersionUpdated.getMaintainableArtefact().getUrn());
    }

    @Test
    public void testUpdateCategorySchemePublished() throws Exception {
        String[] urns = {CATEGORY_SCHEME_1_V1};
        for (String urn : urns) {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);

            try {
                categorySchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
                categorySchemeVersion = categoriesService.updateCategoryScheme(getServiceContextAdministrador(), categorySchemeVersion);
                fail("wrong proc status");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
                assertEquals(ServiceExceptionParameters.PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
                assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
                assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
                assertEquals(ServiceExceptionParameters.PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
            }
        }
    }

    @Test
    public void testUpdateCategorySchemeErrorExternalReference() throws Exception {
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2);
        categorySchemeVersion.getMaintainableArtefact().setIsExternalReference(Boolean.TRUE);

        try {
            categorySchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
            categorySchemeVersion = categoriesService.updateCategoryScheme(getServiceContextAdministrador(), categorySchemeVersion);
            fail("category scheme cannot be a external reference");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_INCORRECT.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_IS_EXTERNAL_REFERENCE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testUpdateCategorySchemeErrorChangeCodeInCategorySchemeWithVersionAlreadyPublished() throws Exception {
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2);
        categorySchemeVersion.getMaintainableArtefact().setCode("newCode");
        categorySchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE);

        try {
            categorySchemeVersion = categoriesService.updateCategoryScheme(getServiceContextAdministrador(), categorySchemeVersion);
            fail("code can not be changed");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.METADATA_UNMODIFIABLE.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Override
    @Test
    public void testRetrieveCategorySchemeByUrn() throws Exception {

        // Retrieve
        String urn = CATEGORY_SCHEME_1_V1;
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        assertEquals(urn, categorySchemeVersion.getMaintainableArtefact().getUrn());
        MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
        assertEquals("user1", categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-02 02:02:03", categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
        assertEquals("user2", categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        MetamacAsserts.assertEqualsDate("2011-01-03 03:02:03", categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
        assertEquals("user3", categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
        assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
        assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
    }

    @Test
    @Override
    public void testFindCategorySchemesByCondition() throws Exception {

        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CategorySchemeVersionMetamac.class)
                    .orderBy(CategorySchemeVersionMetamacProperties.maintainableArtefact().code()).orderBy(CategorySchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CategorySchemeVersionMetamac> categorySchemeVersionPagedResult = categoriesService.findCategorySchemesByCondition(getServiceContextAdministrador(), conditions,
                    pagingParameter);

            // Validate
            assertEquals(10, categorySchemeVersionPagedResult.getTotalRows());
            int i = 0;
            assertEquals(CATEGORY_SCHEME_1_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_2_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_4_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_5_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_6_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_7_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_7_V2, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_8_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(categorySchemeVersionPagedResult.getTotalRows(), i);
        }

        // Find internally published
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CategorySchemeVersionMetamac.class)
                    .withProperty(CategorySchemeVersionMetamacProperties.lifeCycleMetadata().procStatus()).eq(ProcStatusEnum.INTERNALLY_PUBLISHED)
                    .orderBy(CategorySchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CategorySchemeVersionMetamac> categorySchemeVersionPagedResult = categoriesService.findCategorySchemesByCondition(getServiceContextAdministrador(), conditions,
                    pagingParameter);

            // Validate
            assertEquals(3, categorySchemeVersionPagedResult.getTotalRows());
            int i = 0;
            assertEquals(CATEGORY_SCHEME_1_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_7_V2, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(categorySchemeVersionPagedResult.getTotalRows(), i);
        }

        // Find lasts versions
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CategorySchemeVersionMetamac.class)
                    .withProperty(CategorySchemeVersionMetamacProperties.maintainableArtefact().isLastVersion()).eq(Boolean.TRUE)
                    .orderBy(CategorySchemeVersionMetamacProperties.maintainableArtefact().code()).orderBy(CategorySchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CategorySchemeVersionMetamac> categorySchemeVersionPagedResult = categoriesService.findCategorySchemesByCondition(getServiceContextAdministrador(), conditions,
                    pagingParameter);

            // Validate
            assertEquals(8, categorySchemeVersionPagedResult.getTotalRows());
            int i = 0;
            assertEquals(CATEGORY_SCHEME_1_V2, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_2_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_4_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_5_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_6_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_7_V2, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_8_V1, categorySchemeVersionPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        }
    }

    @Test
    @Override
    public void testFindCategorySchemesWithCategoriesCanBeCategorisationCategoryByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CategorySchemeVersionMetamac.class).orderBy(CategorySchemeVersionMetamacProperties.maintainableArtefact().code())
                .orderBy(CategorySchemeVersionMetamacProperties.maintainableArtefact().urn()).build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<CategorySchemeVersionMetamac> categorySchemesPagedResult = categoriesService
                .findCategorySchemesWithCategoriesCanBeCategorisationCategoryByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

        // Validate
        assertEquals(4, categorySchemesPagedResult.getTotalRows());
        assertEquals(4, categorySchemesPagedResult.getValues().size());
        assertTrue(categorySchemesPagedResult.getValues().get(0) instanceof CategorySchemeVersionMetamac);

        int i = 0;
        assertEquals(CATEGORY_SCHEME_1_V1, categorySchemesPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CATEGORY_SCHEME_3_V1, categorySchemesPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CATEGORY_SCHEME_7_V1, categorySchemesPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());
        assertEquals(CATEGORY_SCHEME_7_V2, categorySchemesPagedResult.getValues().get(i++).getMaintainableArtefact().getUrn());

        assertEquals(categorySchemesPagedResult.getValues().size(), i);
    }

    @Test
    @Override
    public void testRetrieveCategorySchemeVersions() throws Exception {

        // Retrieve all versions
        String urn = CATEGORY_SCHEME_1_V1;
        List<CategorySchemeVersionMetamac> categorySchemeVersions = categoriesService.retrieveCategorySchemeVersions(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(2, categorySchemeVersions.size());
        assertEquals(CATEGORY_SCHEME_1_V1, categorySchemeVersions.get(0).getMaintainableArtefact().getUrn());
        assertEquals(CATEGORY_SCHEME_1_V2, categorySchemeVersions.get(1).getMaintainableArtefact().getUrn());
    }

    @Override
    @Test
    public void testSendCategorySchemeToProductionValidation() throws Exception {

        String urn = CATEGORY_SCHEME_2_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
        }

        // Send to production validation
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.sendCategorySchemeToProductionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertEqualsDate("2011-01-01 01:02:03", categorySchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
        // Validate retrieving
        {
            categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
    }

    @Test
    public void testSendCategorySchemeToProductionValidationInProcStatusRejected() throws Exception {

        String urn = CATEGORY_SCHEME_4_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
        }

        // Send to production validation
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.sendCategorySchemeToProductionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }
        // Validate retrieving
        {
            categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }
    }

    @Test
    public void testSendCategorySchemeToProductionValidationErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            categoriesService.sendCategorySchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendCategorySchemeToProductionValidationErrorWrongProcStatus() throws Exception {

        String urn = CATEGORY_SCHEME_1_V1;

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            categoriesService.sendCategorySchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Test
    public void testSendCategorySchemeToProductionValidationErrorMetadataRequired() throws Exception {

        String urn = CATEGORY_SCHEME_2_V1;

        // Update to clear required metadata to send to production
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        categorySchemeVersion.setIsPartial(null);
        categorySchemeVersion.getMaintainableArtefact().setIsCodeUpdated(Boolean.FALSE);
        categoriesService.updateCategoryScheme(getServiceContextAdministrador(), categorySchemeVersion);

        // Send to production validation
        try {
            categoriesService.sendCategorySchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme metadata required");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());

            MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, urn);
            assertEquals(1, exceptionItem.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_REQUIRED, 1, new String[]{ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL}, exceptionItem.getExceptionItems().get(0));
        }
    }

    @Test
    public void testSendCategorySchemeToProductionValidationErrorTranslations() throws Exception {

        String urn = CATEGORY_SCHEME_2_V1;

        // Update to change metadata to send to production

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            categorySchemeVersion.getMaintainableArtefact().setName(BaseDoMocks.mockInternationalStringFixedValues("en", "label1", "fr", "label2"));
            itemSchemeRepository.save(categorySchemeVersion);
        }
        {
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_2_V1_CATEGORY_1);
            category.getNameableArtefact().setDescription(BaseDoMocks.mockInternationalStringFixedValues("en", "label1", "fr", "label2"));
            itemRepository.save(category);
        }
        {
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_2_V1_CATEGORY_2);
            category.getNameableArtefact().setComment(BaseDoMocks.mockInternationalStringFixedValues("en", "label1", "fr", "label2"));
            itemRepository.save(category);
        }

        entityManager.flush();

        // Send to production validation
        try {
            categoriesService.sendCategorySchemeToProductionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme metadata required");
        } catch (MetamacException e) {
            assertEquals(3, e.getExceptionItems().size());
            int i = 0;
            // CategoryScheme
            {
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, urn);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME},
                        exceptionItem.getExceptionItems().get(0));
            }
            // Categories
            {
                // Category01
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, CATEGORY_SCHEME_2_V1_CATEGORY_1);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION},
                        exceptionItem.getExceptionItems().get(0));
            }
            {
                // Category02
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, CATEGORY_SCHEME_2_V1_CATEGORY_2);
                // children
                assertEquals(1, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_COMMENT},
                        exceptionItem.getExceptionItems().get(0));
            }
            assertEquals(e.getExceptionItems().size(), i);
        }
    }

    @Override
    @Test
    public void testSendCategorySchemeToDiffusionValidation() throws Exception {

        String urn = CATEGORY_SCHEME_5_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }

        // Sends to diffusion validation
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.sendCategorySchemeToDiffusionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertEqualsDate("2011-01-01 01:02:03", categorySchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
        // Validate retrieving
        {
            categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
    }

    @Test
    public void testSendCategorySchemeToDiffusionValidationErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            categoriesService.sendCategorySchemeToDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testSendCategorySchemeToDiffusionValidationErrorWrongProcStatus() throws Exception {

        String urn = CATEGORY_SCHEME_2_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }

        try {
            categoriesService.sendCategorySchemeToDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Override
    @Test
    public void testRejectCategorySchemeProductionValidation() throws Exception {

        String urn = CATEGORY_SCHEME_5_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.PRODUCTION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
        }

        // Reject validation
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.rejectCategorySchemeProductionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertEqualsDate("2011-01-01 01:02:03", categorySchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
        // Validate restrieving
        {
            categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);

            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
    }

    @Test
    public void testRejectCategorySchemeProductionValidationErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            categoriesService.rejectCategorySchemeProductionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRejectCategorySchemeProductionValidationErrorWrongProcStatus() throws Exception {

        String urn = CATEGORY_SCHEME_1_V1;

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            categoriesService.rejectCategorySchemeProductionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Override
    @Test
    public void testRejectCategorySchemeDiffusionValidation() throws Exception {

        String urn = CATEGORY_SCHEME_6_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
        }

        // Reject validation
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.rejectCategorySchemeDiffusionValidation(ctx, urn);

        // Validate response
        {
            assertEquals(ProcStatusEnum.VALIDATION_REJECTED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertEqualsDate("2011-01-01 01:02:03", categorySchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
        // Validate retrieving
        {
            categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);

            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
        }
    }

    @Test
    public void testRejectCategorySchemeDiffusionValidationErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            categoriesService.rejectCategorySchemeDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testRejectCategorySchemeDiffusionValidationErrorWrongProcStatus() throws Exception {

        String urn = CATEGORY_SCHEME_1_V1;

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            categoriesService.rejectCategorySchemeDiffusionValidation(getServiceContextAdministrador(), urn);
            fail("CategoryScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Override
    @Test
    public void testPublishInternallyCategoryScheme() throws Exception {

        String urn = CATEGORY_SCHEME_6_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.DIFFUSION_VALIDATION, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertFalse(categorySchemeVersion.getMaintainableArtefact().getFinalLogic());
            assertFalse(categorySchemeVersion.getMaintainableArtefact().getLatestFinal());
        }

        // Publish internally
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.publishInternallyCategoryScheme(ctx, urn, Boolean.FALSE).getResourceVersion();

        // Validate response
        {
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(categorySchemeVersion.getMaintainableArtefact().getFinalLogicClient());
            assertTrue(categorySchemeVersion.getMaintainableArtefact().getFinalLogic());
            assertTrue(categorySchemeVersion.getMaintainableArtefact().getLatestFinal());
            assertFalse(categorySchemeVersion.getMaintainableArtefact().getPublicLogic());
            assertFalse(categorySchemeVersion.getMaintainableArtefact().getLatestPublic());
            assertEqualsDate("2011-01-01 01:02:03", categorySchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
        // Validate retrieving
        {
            categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);

            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(categorySchemeVersion.getMaintainableArtefact().getFinalLogic());
            assertTrue(categorySchemeVersion.getMaintainableArtefact().getLatestFinal());
        }
    }

    @Test
    public void testPublishInternallyCategorySchemeErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            categoriesService.publishInternallyCategoryScheme(getServiceContextAdministrador(), urn, Boolean.FALSE);
            fail("CategoryScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testPublishInternallyCategorySchemeErrorWrongProcStatus() throws Exception {

        String urn = CATEGORY_SCHEME_1_V1;

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            categoriesService.publishInternallyCategoryScheme(getServiceContextAdministrador(), urn, Boolean.FALSE);
            fail("CategoryScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    @Override
    public void testCheckCategorySchemeVersionTranslations() throws Exception {
        // Tested in testPublishInternallyCategorySchemeCheckTranslations
    }

    @Test
    @Override
    public void testCheckCategorisationsWithRelatedResourcesExternallyPublished() throws Exception {
        // In CategoriesMetamacCategorisationServiceTest
    }

    @Test
    public void testPublishInternallyCategorySchemeCheckTranslations() throws Exception {
        String urn = CATEGORY_SCHEME_8_V1;

        try {
            // Note: publishInternallyCategoryScheme calls to 'checkCategorySchemeVersionTranslates'
            categoriesService.publishInternallyCategoryScheme(getServiceContextAdministrador(), urn, Boolean.FALSE);
            fail("CategoryScheme wrong translations");
        } catch (MetamacException e) {
            assertEquals(4, e.getExceptionItems().size());
            int i = 0;
            // CategoryScheme
            {
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, urn);
                // children
                assertEquals(2, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION},
                        exceptionItem.getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(1));
            }
            // Categories
            {
                // Category01
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, CATEGORY_SCHEME_8_V1_CATEGORY_1);
                // children
                assertEquals(2, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME},
                        exceptionItem.getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_COMMENT},
                        exceptionItem.getExceptionItems().get(1));
            }
            {
                // Category0101
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, CATEGORY_SCHEME_8_V1_CATEGORY_1_1);
                // children
                assertEquals(2, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_DESCRIPTION},
                        exceptionItem.getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(1));
            }
            {
                // Category02
                i++;
                MetamacExceptionItem exceptionItem = assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.RESOURCE_WITH_INCORRECT_METADATA, CATEGORY_SCHEME_8_V1_CATEGORY_2);
                // children
                assertEquals(2, exceptionItem.getExceptionItems().size());
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME},
                        exceptionItem.getExceptionItems().get(0));
                assertEqualsMetamacExceptionItem(ServiceExceptionType.METADATA_WITHOUT_DEFAULT_LANGUAGE, 1, new String[]{ServiceExceptionParameters.ANNOTATION},
                        exceptionItem.getExceptionItems().get(1));
            }
            {
                // Category03: no exception
            }
            assertEquals(e.getExceptionItems().size(), i);
        }
    }

    @Override
    @Test
    public void testPublishExternallyCategoryScheme() throws Exception {

        String urn = CATEGORY_SCHEME_7_V2;
        ServiceContext ctx = getServiceContextAdministrador();

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate());
            assertNull(categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertNull(categorySchemeVersion.getMaintainableArtefact().getValidFrom());
            assertNull(categorySchemeVersion.getMaintainableArtefact().getValidTo());

            CategorySchemeVersionMetamac categorySchemeVersionExternallyPublished = categoriesService.retrieveCategorySchemeByUrn(ctx, CATEGORY_SCHEME_7_V1);
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, categorySchemeVersionExternallyPublished.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersionExternallyPublished.getMaintainableArtefact().getValidFrom());
            assertNull(categorySchemeVersionExternallyPublished.getMaintainableArtefact().getValidTo());
        }

        // Publish externally
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.publishExternallyCategoryScheme(ctx, urn).getResourceVersion();

        // Validate response
        {
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getMaintainableArtefact().getValidFrom().toDate()));
            assertNull(categorySchemeVersion.getMaintainableArtefact().getValidTo());
            assertTrue(categorySchemeVersion.getMaintainableArtefact().getPublicLogic());
            assertTrue(categorySchemeVersion.getMaintainableArtefact().getLatestPublic());
            assertEqualsDate("2011-01-01 01:02:03", categorySchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        }
        // Validate retrieving
        {
            categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(ctx, urn);
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getProductionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getDiffusionValidationUser());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationDate());
            assertNotNull(categorySchemeVersion.getLifeCycleMetadata().getInternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationDate().toDate()));
            assertEquals(ctx.getUserId(), categorySchemeVersion.getLifeCycleMetadata().getExternalPublicationUser());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getMaintainableArtefact().getValidFrom().toDate()));
            assertNull(categorySchemeVersion.getMaintainableArtefact().getValidTo());
        }
        // Validate previous published externally versions
        {
            CategorySchemeVersionMetamac categorySchemeVersionExternallyPublished = categoriesService.retrieveCategorySchemeByUrn(ctx, CATEGORY_SCHEME_7_V1);
            assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, categorySchemeVersionExternallyPublished.getLifeCycleMetadata().getProcStatus());
            assertNotNull(categorySchemeVersionExternallyPublished.getMaintainableArtefact().getValidFrom());
            assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersionExternallyPublished.getMaintainableArtefact().getValidTo().toDate()));
        }
    }

    @Test
    public void testPublishExternallyCategorySchemeErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;
        try {
            categoriesService.publishExternallyCategoryScheme(getServiceContextAdministrador(), urn);
            fail("CategoryScheme not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testPublishExternallyCategorySchemeErrorWrongProcStatus() throws Exception {

        String urn = CATEGORY_SCHEME_1_V2;

        {
            CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
        }

        try {
            categoriesService.publishExternallyCategoryScheme(getServiceContextAdministrador(), urn);
            fail("CategoryScheme wrong proc status");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_INTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
        }
    }

    @Test
    public void testPublishExternallyCategorySchemeErrorRelatedResourcesCategorisationsNotExternallyPublished() throws Exception {

        String urn = CATEGORY_SCHEME_3_V1;
        ServiceContext ctx = getServiceContextAdministrador();

        try {
            categoriesService.publishExternallyCategoryScheme(ctx, urn);
            fail("related resources");
        } catch (MetamacException e) {
            assertEquals(2, e.getExceptionItems().size());

            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CATEGORY_NOT_EXTERNALLY_PUBLISHED, CATEGORY_SCHEME_1_V1_CATEGORY_1);
            assertListContainsExceptionItemOneParameter(e, ServiceExceptionType.CATEGORY_NOT_EXTERNALLY_PUBLISHED, CATEGORY_SCHEME_7_V2_CATEGORY_1);
        }
    }

    @Test
    @Override
    public void testCheckCategorySchemeWithRelatedResourcesExternallyPublished() throws Exception {
        // In testPublishExternally*ErrorRelatedResources
    }

    @Override
    @Test
    public void testResendCategoryScheme() throws Exception {
        // NO TEST
    }

    @Override
    @Test
    public void testDeleteCategoryScheme() throws Exception {

        String urn = CATEGORY_SCHEME_2_V1;

        // Delete category scheme only with version in draft
        categoriesService.deleteCategoryScheme(getServiceContextAdministrador(), urn);

        // Validation
        try {
            categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            fail("CategoryScheme deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testDeleteCategorySchemeWithVersionPublishedAndVersionDraft() throws Exception {

        String urnV1 = CATEGORY_SCHEME_1_V1;
        String urnV2 = CATEGORY_SCHEME_1_V2;

        CategorySchemeVersionMetamac categorySchemeVersionV1 = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnV1);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersionV1.getLifeCycleMetadata().getProcStatus());
        CategorySchemeVersionMetamac categorySchemeVersionV2 = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnV2);
        assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersionV2.getLifeCycleMetadata().getProcStatus());

        categoriesService.deleteCategoryScheme(getServiceContextAdministrador(), urnV2);

        // Validation
        try {
            categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnV2);
            fail("CategoryScheme deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urnV2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
        categorySchemeVersionV1 = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnV1);
        assertTrue(categorySchemeVersionV1.getMaintainableArtefact().getIsLastVersion());
        assertNull(categorySchemeVersionV1.getMaintainableArtefact().getReplacedByVersion());
    }

    @Test
    public void testDeleteCategorySchemeErrorPublished() throws Exception {

        String urn = CATEGORY_SCHEME_1_V1;

        // Validation
        try {
            categoriesService.deleteCategoryScheme(getServiceContextAdministrador(), urn);
            fail("CategoryScheme can not be deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Override
    @Test
    public void testCopyCategoryScheme() throws Exception {

        String urnToCopy = CATEGORY_SCHEME_8_V1;
        String maintainerUrnExpected = ORGANISATION_SCHEME_100_V1_ORGANISATION_01;
        String versionExpected = "01.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME08(01.000)";
        String urnExpectedCategory1 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME08(01.000).CATEGORY01";
        String urnExpectedCategory11 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME08(01.000).CATEGORY01.CATEGORY0101";
        String urnExpectedCategory2 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME08(01.000).CATEGORY02";
        String urnExpectedCategory3 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME08(01.000).CATEGORY03";

        TaskInfo copyResult = categoriesService.copyCategoryScheme(getServiceContextAdministrador(), urnToCopy, null);

        // Validate (only some metadata, already tested in statistic module)
        entityManager.clear();
        CategorySchemeVersionMetamac categorySchemeVersionNewArtefact = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), copyResult.getUrnResult());
        assertEquals(maintainerUrnExpected, categorySchemeVersionNewArtefact.getMaintainableArtefact().getMaintainer().getNameableArtefact().getUrn());
        assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersionNewArtefact.getLifeCycleMetadata().getProcStatus());
        assertEquals(versionExpected, categorySchemeVersionNewArtefact.getMaintainableArtefact().getVersionLogic());
        assertEquals(urnExpected, categorySchemeVersionNewArtefact.getMaintainableArtefact().getUrn());
        assertEquals(null, categorySchemeVersionNewArtefact.getMaintainableArtefact().getReplaceToVersion());
        assertEquals(null, categorySchemeVersionNewArtefact.getMaintainableArtefact().getReplacedByVersion());
        assertTrue(categorySchemeVersionNewArtefact.getMaintainableArtefact().getIsLastVersion());
        CategoriesMetamacAsserts.assertEqualsInternationalString(categorySchemeVersionNewArtefact.getMaintainableArtefact().getName(), "es", "nombre catScheme8-1", null, null);
        CategoriesMetamacAsserts.assertEqualsInternationalString(categorySchemeVersionNewArtefact.getMaintainableArtefact().getDescription(), "en", "description catScheme8-1", "it",
                "descripcion it catScheme8-1");
        assertEquals(null, categorySchemeVersionNewArtefact.getMaintainableArtefact().getComment());

        // Categories
        assertEquals(4, categorySchemeVersionNewArtefact.getItems().size());
        assertListCategoriesContainsCategory(categorySchemeVersionNewArtefact.getItems(), urnExpectedCategory1);
        assertListCategoriesContainsCategory(categorySchemeVersionNewArtefact.getItems(), urnExpectedCategory11);
        assertListCategoriesContainsCategory(categorySchemeVersionNewArtefact.getItems(), urnExpectedCategory2);
        assertListCategoriesContainsCategory(categorySchemeVersionNewArtefact.getItems(), urnExpectedCategory3);

        assertEquals(3, categorySchemeVersionNewArtefact.getItemsFirstLevel().size());
        {
            CategoryMetamac category = assertListCategoriesContainsCategory(categorySchemeVersionNewArtefact.getItemsFirstLevel(), urnExpectedCategory1);
            CategoriesMetamacAsserts.assertEqualsInternationalString(category.getNameableArtefact().getName(), "en", "name cat1", "it", "nombre it cat1");
            CategoriesMetamacAsserts.assertEqualsInternationalString(category.getNameableArtefact().getDescription(), "es", "descripción cat1", "it", "descripción it cat1");
            assertEquals(null, category.getNameableArtefact().getComment());

            assertEquals(1, category.getChildren().size());
            {
                CategoryMetamac categoryChild = assertListCategoriesContainsCategory(category.getChildren(), urnExpectedCategory11);
                assertEquals(0, categoryChild.getChildren().size());
            }

        }
        {
            CategoryMetamac category = assertListCategoriesContainsCategory(categorySchemeVersionNewArtefact.getItemsFirstLevel(), urnExpectedCategory2);
            CategoriesMetamacAsserts.assertEqualsInternationalString(category.getNameableArtefact().getName(), "en", "name cat2", null, null);

            assertEquals(0, category.getChildren().size());
        }
        {
            CategoryMetamac category = assertListCategoriesContainsCategory(categorySchemeVersionNewArtefact.getItemsFirstLevel(), urnExpectedCategory3);
            CategoriesMetamacAsserts.assertEqualsInternationalString(category.getNameableArtefact().getName(), "es", "nombre cat3", null, null);

            assertEquals(0, category.getChildren().size());
        }
    }

    @Override
    @Test
    public void testVersioningCategoryScheme() throws Exception {

        String urn = CATEGORY_SCHEME_3_V1;
        String versionExpected = "02.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME03(02.000)";
        String urnExpectedCategory1 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(02.000).CATEGORY01";
        String urnExpectedCategory2 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(02.000).CATEGORY02";
        String urnExpectedCategory21 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(02.000).CATEGORY02.CATEGORY0201";
        String urnExpectedCategory211 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(02.000).CATEGORY02.CATEGORY0201.CATEGORY020101";
        String urnExpectedCategory22 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(02.000).CATEGORY02.CATEGORY0202";

        CategorySchemeVersionMetamac categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        TaskInfo versioningResult = categoriesService.versioningCategoryScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);

        // Validate response
        entityManager.clear();
        categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        CategorySchemeVersionMetamac categorySchemeVersionNewVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());

        {
            assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            CategoriesMetamacAsserts.assertEqualsCategorySchemeWithoutLifeCycleMetadata(categorySchemeVersionToCopy, categorySchemeVersionNewVersion);
        }

        // Validate retrieving
        // New version
        {
            categorySchemeVersionNewVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals("01.000", categorySchemeVersionNewVersion.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(null, categorySchemeVersionNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(categorySchemeVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            CategoriesMetamacAsserts.assertEqualsCategorySchemeWithoutLifeCycleMetadata(categorySchemeVersionToCopy, categorySchemeVersionNewVersion);

            // Categories
            assertEquals(5, categorySchemeVersionNewVersion.getItems().size());
            assertListCategoriesContainsCategory(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory1);
            assertListCategoriesContainsCategory(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory2);
            assertListCategoriesContainsCategory(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory21);
            assertListCategoriesContainsCategory(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory211);
            assertListCategoriesContainsCategory(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory22);

            assertEquals(2, categorySchemeVersionNewVersion.getItemsFirstLevel().size());
            {
                Category category = assertListCategoriesContainsCategory(categorySchemeVersionNewVersion.getItemsFirstLevel(), urnExpectedCategory1);
                assertEquals(0, category.getChildren().size());
            }
            {
                Category category = assertListCategoriesContainsCategory(categorySchemeVersionNewVersion.getItemsFirstLevel(), urnExpectedCategory2);
                {
                    Category categoryChild = assertListCategoriesContainsCategory(category.getChildren(), urnExpectedCategory21);
                    {
                        Category categoryChildChild = assertListCategoriesContainsCategory(categoryChild.getChildren(), urnExpectedCategory211);
                        assertEquals(0, categoryChildChild.getChildren().size());
                    }
                }
                {
                    Category categoryChild = assertListCategoriesContainsCategory(category.getChildren(), urnExpectedCategory22);
                    assertEquals(0, categoryChild.getChildren().size());
                }
            }
        }

        // Copied version
        {
            categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals("01.000", categorySchemeVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urn, categorySchemeVersionToCopy.getMaintainableArtefact().getUrn());
            assertEquals(null, categorySchemeVersionToCopy.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(versionExpected, categorySchemeVersionToCopy.getMaintainableArtefact().getReplacedByVersion());
            assertFalse(categorySchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());
        }
        // All versions
        {
            List<CategorySchemeVersionMetamac> allVersions = categoriesService.retrieveCategorySchemeVersions(getServiceContextAdministrador(), urn);
            assertEquals(2, allVersions.size());
            assertEquals(urn, allVersions.get(0).getMaintainableArtefact().getUrn());
            assertEquals(urnExpected, allVersions.get(1).getMaintainableArtefact().getUrn());
        }
    }

    @Test
    public void testVersioningCategorySchemeWithTwoVersionsPublished() throws Exception {

        // This test checks the copy from one version but replacing to another one that is last version.

        String urnToCopy = CATEGORY_SCHEME_7_V1;
        String urnLastVersion = CATEGORY_SCHEME_7_V2;
        String versionExpected = "03.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME07(03.000)";

        CategorySchemeVersionMetamac categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnToCopy);
        assertEquals(ProcStatusEnum.EXTERNALLY_PUBLISHED, categorySchemeVersionToCopy.getLifeCycleMetadata().getProcStatus());
        assertFalse(categorySchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());

        CategorySchemeVersionMetamac categorySchemeVersionLast = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnLastVersion);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersionLast.getLifeCycleMetadata().getProcStatus());
        assertTrue(categorySchemeVersionLast.getMaintainableArtefact().getIsLastVersion());

        TaskInfo versioningResult = categoriesService.versioningCategoryScheme(getServiceContextAdministrador(), urnToCopy, VersionTypeEnum.MAJOR);

        // Validate response
        entityManager.clear();
        categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnToCopy);
        CategorySchemeVersionMetamac categorySchemeVersionNewVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());

        {
            assertEquals(versionExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            CategoriesMetamacAsserts.assertEqualsCategorySchemeWithoutLifeCycleMetadata(categorySchemeVersionToCopy, categorySchemeVersionNewVersion);
        }

        // Validate retrieving
        {
            // New version
            categorySchemeVersionNewVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(versionExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals("02.000", categorySchemeVersionNewVersion.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(null, categorySchemeVersionNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(categorySchemeVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            CategoriesMetamacAsserts.assertEqualsCategorySchemeWithoutLifeCycleMetadata(categorySchemeVersionToCopy, categorySchemeVersionNewVersion);

            // Version copied
            categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnToCopy);
            assertEquals("01.000", categorySchemeVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnToCopy, categorySchemeVersionToCopy.getMaintainableArtefact().getUrn());
            assertEquals(null, categorySchemeVersionToCopy.getMaintainableArtefact().getReplaceToVersion());
            assertEquals("02.000", categorySchemeVersionToCopy.getMaintainableArtefact().getReplacedByVersion());
            assertFalse(categorySchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());

            // Last version
            categorySchemeVersionLast = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urnLastVersion);
            assertEquals("02.000", categorySchemeVersionLast.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnLastVersion, categorySchemeVersionLast.getMaintainableArtefact().getUrn());
            assertEquals("01.000", categorySchemeVersionLast.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(versionExpected, categorySchemeVersionLast.getMaintainableArtefact().getReplacedByVersion());
            assertFalse(categorySchemeVersionLast.getMaintainableArtefact().getIsLastVersion());

            // All versions
            List<CategorySchemeVersionMetamac> allVersions = categoriesService.retrieveCategorySchemeVersions(getServiceContextAdministrador(),
                    categorySchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(3, allVersions.size());
            assertEquals(urnToCopy, allVersions.get(0).getMaintainableArtefact().getUrn());
            assertEquals(urnLastVersion, allVersions.get(1).getMaintainableArtefact().getUrn());
            assertEquals(urnExpected, allVersions.get(2).getMaintainableArtefact().getUrn());
        }
    }

    @Test
    public void testVersioningCategorySchemeErrorAlreadyExistsDraft() throws Exception {

        String urn = CATEGORY_SCHEME_1_V1;

        try {
            categoriesService.versioningCategoryScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);
            fail("CategoryScheme already exists in no final");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED_VERSION_NOT_PUBLISHED.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(CATEGORY_SCHEME_1_V2, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    public void testVersioningCategorySchemeErrorNotPublished() throws Exception {

        String urn = CATEGORY_SCHEME_2_V1;

        try {
            categoriesService.versioningCategoryScheme(getServiceContextAdministrador(), urn, VersionTypeEnum.MAJOR);
            fail("CategoryScheme not published");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_INTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_EXTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
        }
    }

    @Override
    @Test
    public void testCreateTemporalVersionCategoryScheme() throws Exception {

        String urn = CATEGORY_SCHEME_3_V1;
        String versionExpected = "01.000" + UrnConstants.URN_SDMX_TEMPORAL_SUFFIX;
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME03(" + versionExpected + ")";
        String urnExpectedCategory1 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(" + versionExpected + ").CATEGORY01";
        String urnExpectedCategory2 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(" + versionExpected + ").CATEGORY02";
        String urnExpectedCategory21 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(" + versionExpected + ").CATEGORY02.CATEGORY0201";
        String urnExpectedCategory211 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(" + versionExpected + ").CATEGORY02.CATEGORY0201.CATEGORY020101";
        String urnExpectedCategory22 = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME03(" + versionExpected + ").CATEGORY02.CATEGORY0202";

        CategorySchemeVersionMetamac categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        TaskInfo versioningResult = categoriesService.createTemporalVersionCategoryScheme(getServiceContextAdministrador(), urn);

        // Validate response
        entityManager.clear();
        categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
        CategorySchemeVersionMetamac categorySchemeVersionNewVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());

        {
            assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertTrue(categorySchemeVersionNewVersion.getMaintainableArtefact().getIsTemporal());
            CategoriesMetamacAsserts.assertEqualsCategorySchemeWithoutLifeCycleMetadata(categorySchemeVersionToCopy, categorySchemeVersionNewVersion);
        }

        // Validate retrieving
        // New version
        {
            categorySchemeVersionNewVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals(ProcStatusEnum.DRAFT, categorySchemeVersionNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, categorySchemeVersionNewVersion.getMaintainableArtefact().getUrn());
            assertEquals("01.000", categorySchemeVersionNewVersion.getMaintainableArtefact().getReplaceToVersion());
            assertEquals(null, categorySchemeVersionNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(categorySchemeVersionNewVersion.getMaintainableArtefact().getIsLastVersion());
            CategoriesMetamacAsserts.assertEqualsCategorySchemeWithoutLifeCycleMetadata(categorySchemeVersionToCopy, categorySchemeVersionNewVersion);

            // Categories
            assertEquals(5, categorySchemeVersionNewVersion.getItems().size());
            assertListCategoriesContainsCategory(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory1);
            assertListCategoriesContainsCategory(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory2);
            assertListCategoriesContainsCategory(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory21);
            assertListCategoriesContainsCategory(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory211);
            assertListCategoriesContainsCategory(categorySchemeVersionNewVersion.getItems(), urnExpectedCategory22);

            assertEquals(2, categorySchemeVersionNewVersion.getItemsFirstLevel().size());
            {
                Category category = assertListCategoriesContainsCategory(categorySchemeVersionNewVersion.getItemsFirstLevel(), urnExpectedCategory1);
                assertEquals(0, category.getChildren().size());
            }
            {
                Category category = assertListCategoriesContainsCategory(categorySchemeVersionNewVersion.getItemsFirstLevel(), urnExpectedCategory2);
                {
                    Category categoryChild = assertListCategoriesContainsCategory(category.getChildren(), urnExpectedCategory21);
                    {
                        Category categoryChildChild = assertListCategoriesContainsCategory(categoryChild.getChildren(), urnExpectedCategory211);
                        assertEquals(0, categoryChildChild.getChildren().size());
                    }
                }
                {
                    Category categoryChild = assertListCategoriesContainsCategory(category.getChildren(), urnExpectedCategory22);
                    assertEquals(0, categoryChild.getChildren().size());
                }
            }
        }

        // Copied version
        {
            categorySchemeVersionToCopy = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), urn);
            assertEquals("01.000", categorySchemeVersionToCopy.getMaintainableArtefact().getVersionLogic());
            assertEquals(urn, categorySchemeVersionToCopy.getMaintainableArtefact().getUrn());
            assertFalse(categorySchemeVersionToCopy.getMaintainableArtefact().getIsLastVersion());
        }
        // All versions
        {
            List<CategorySchemeVersionMetamac> allVersions = categoriesService.retrieveCategorySchemeVersions(getServiceContextAdministrador(), urn);
            assertEquals(2, allVersions.size());
            assertEquals(urn, allVersions.get(0).getMaintainableArtefact().getUrn());
            assertEquals(urnExpected, allVersions.get(1).getMaintainableArtefact().getUrn());
        }
    }

    @Override
    @Test
    public void testCreateVersionFromTemporalCategoryScheme() throws Exception {
        String urn = CATEGORY_SCHEME_3_V1;

        TaskInfo versioningResult1 = categoriesService.createTemporalVersionCategoryScheme(getServiceContextAdministrador(), urn);
        entityManager.clear();
        CategorySchemeVersionMetamac categorySchemeVersionTemporal = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), versioningResult1.getUrnResult());
        assertEquals(3, categorySchemeVersionTemporal.getMaintainableArtefact().getCategorisations().size());
        {
            Categorisation categorisation = assertListContainsCategorisation(categorySchemeVersionTemporal.getMaintainableArtefact().getCategorisations(),
                    "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat1(01.000_temporal)");
            assertTrue(categorisation.getMaintainableArtefact().getFinalLogic());
            assertTrue(categorisation.getMaintainableArtefact().getFinalLogicClient());
            assertTrue(categorisation.getMaintainableArtefact().getLatestFinal());
            assertFalse(categorisation.getMaintainableArtefact().getPublicLogic());
            assertFalse(categorisation.getMaintainableArtefact().getLatestPublic());
        }
        assertListContainsCategorisation(categorySchemeVersionTemporal.getMaintainableArtefact().getCategorisations(),
                "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat2(01.000_temporal)");
        assertListContainsCategorisation(categorySchemeVersionTemporal.getMaintainableArtefact().getCategorisations(),
                "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat3(01.000_temporal)");

        TaskInfo versioningResult2 = categoriesService.createVersionFromTemporalCategoryScheme(getServiceContextAdministrador(), categorySchemeVersionTemporal.getMaintainableArtefact().getUrn(),
                VersionTypeEnum.MAJOR);
        entityManager.clear();
        CategorySchemeVersionMetamac categorySchemeNewVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), versioningResult2.getUrnResult());

        String versionExpected = "02.000";
        String urnExpected = "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME03(" + versionExpected + ")";

        // Validate

        {
            assertEquals(ProcStatusEnum.DRAFT, categorySchemeNewVersion.getLifeCycleMetadata().getProcStatus());
            assertEquals(versionExpected, categorySchemeNewVersion.getMaintainableArtefact().getVersionLogic());
            assertEquals(urnExpected, categorySchemeNewVersion.getMaintainableArtefact().getUrn());

            assertEquals(null, categorySchemeNewVersion.getMaintainableArtefact().getReplacedByVersion());
            assertTrue(categorySchemeNewVersion.getMaintainableArtefact().getIsLastVersion());
            assertFalse(categorySchemeNewVersion.getMaintainableArtefact().getLatestFinal());
            assertFalse(categorySchemeNewVersion.getMaintainableArtefact().getLatestPublic());

            assertEquals(3, categorySchemeNewVersion.getMaintainableArtefact().getCategorisations().size());
            {
                Categorisation categorisation = assertListContainsCategorisation(categorySchemeNewVersion.getMaintainableArtefact().getCategorisations(),
                        "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat4(01.000)");
                assertFalse(categorisation.getMaintainableArtefact().getFinalLogic());
                assertFalse(categorisation.getMaintainableArtefact().getFinalLogicClient());
                assertFalse(categorisation.getMaintainableArtefact().getLatestFinal());
                assertFalse(categorisation.getMaintainableArtefact().getPublicLogic());
                assertFalse(categorisation.getMaintainableArtefact().getLatestPublic());
            }

            assertListContainsCategorisation(categorySchemeNewVersion.getMaintainableArtefact().getCategorisations(), "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat5(01.000)");
            assertListContainsCategorisation(categorySchemeNewVersion.getMaintainableArtefact().getCategorisations(), "urn:sdmx:org.sdmx.infomodel.categoryscheme.Categorisation=SDMX01:cat6(01.000)");
        }
    }

    @Override
    @Test
    public void testMergeTemporalVersion() throws Exception {
        {
            String urn = CATEGORY_SCHEME_3_V1;
            TaskInfo versioningResult = categoriesService.createTemporalVersionCategoryScheme(getServiceContextAdministrador(), urn);

            entityManager.clear();
            CategorySchemeVersionMetamac categorySchemeVersionTemporal = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), versioningResult.getUrnResult());
            // Change temporal version *********************

            // Item scheme: Change Name
            {
                LocalisedString localisedString = new LocalisedString("fr", "fr - text sample");
                categorySchemeVersionTemporal.getMaintainableArtefact().getName().addText(localisedString);
            }

            // Item: change name
            {
                CategoryMetamac categoryTemporal = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), GeneratorUrnUtils.makeUrnAsTemporal(CATEGORY_SCHEME_3_V1_CATEGORY_1));

                categoryTemporal.getNameableArtefact().setName(new InternationalString());
                categoryTemporal.getNameableArtefact().getName().addText(new LocalisedString("fr", "fr - text sample"));
                categoryTemporal.getNameableArtefact().getName().addText(new LocalisedString("es", "es - text sample"));
            }

            // Merge
            categorySchemeVersionTemporal = categoriesService.sendCategorySchemeToProductionValidation(getServiceContextAdministrador(),
                    categorySchemeVersionTemporal.getMaintainableArtefact().getUrn());
            categorySchemeVersionTemporal = categoriesService.sendCategorySchemeToDiffusionValidation(getServiceContextAdministrador(),
                    categorySchemeVersionTemporal.getMaintainableArtefact().getUrn());
            CategorySchemeVersionMetamac categorySchemeVersionMetamac = categoriesService.mergeTemporalVersion(getServiceContextAdministrador(), categorySchemeVersionTemporal);

            // Assert **************************************

            // Item Scheme
            assertEquals(3, categorySchemeVersionMetamac.getMaintainableArtefact().getName().getTexts().size());
            assertEquals("fr - text sample", categorySchemeVersionMetamac.getMaintainableArtefact().getName().getLocalisedLabel("fr"));
            assertNull(categorySchemeVersionMetamac.getMaintainableArtefact().getIsTemporal());

            // Item
            {
                CategoryMetamac categoryTemporal = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_3_V1_CATEGORY_1);
                assertEquals(2, categoryTemporal.getNameableArtefact().getName().getTexts().size());
                assertEquals("fr - text sample", categoryTemporal.getNameableArtefact().getName().getLocalisedLabel("fr"));
                assertEquals("es - text sample", categoryTemporal.getNameableArtefact().getName().getLocalisedLabel("es"));
            }
        }
    }

    @Override
    @Test
    public void testStartCategorySchemeValidity() throws Exception {
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.startCategorySchemeValidity(getServiceContextAdministrador(), CATEGORY_SCHEME_7_V2);

        assertNotNull(categorySchemeVersion.getMaintainableArtefact().getValidFrom());
        assertNull(categorySchemeVersion.getMaintainableArtefact().getValidTo());
    }

    @Test
    public void testStartCategorySchemeValidityErrorWrongProcStatus() throws Exception {
        String[] urns = {CATEGORY_SCHEME_6_V1};
        for (String urn : urns) {
            try {
                categoriesService.startCategorySchemeValidity(getServiceContextAdministrador(), urn);
                fail("wrong procStatus");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
                assertEquals(ServiceExceptionParameters.PROC_STATUS_INTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            }
        }
    }

    @Override
    @Test
    public void testEndCategorySchemeValidity() throws Exception {
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.endCategorySchemeValidity(getServiceContextAdministrador(), CATEGORY_SCHEME_7_V1);

        assertNotNull(categorySchemeVersion.getMaintainableArtefact().getValidTo());
    }

    @Test
    public void testEndCategorySchemeValidityErrorWrongProcStatus() throws Exception {
        String[] urns = {CATEGORY_SCHEME_1_V1, CATEGORY_SCHEME_4_V1, CATEGORY_SCHEME_6_V1};
        for (String urn : urns) {
            try {
                categoriesService.endCategorySchemeValidity(getServiceContextAdministrador(), urn);
                fail("wrong procStatus");
            } catch (MetamacException e) {
                assertEquals(1, e.getExceptionItems().size());
                assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
                assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
                assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
                assertEquals(ServiceExceptionParameters.PROC_STATUS_EXTERNALLY_PUBLISHED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            }
        }
    }

    @Override
    @Test
    public void testCreateCategory() throws Exception {

        CategoryMetamac category = CategoriesMetamacDoMocks.mockCategory(retrieveAnnotationType_1());
        category.setParent(null);
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        // Create
        CategoryMetamac categorySchemeVersionCreated = categoriesService.createCategory(getServiceContextAdministrador(), categorySchemeUrn, category);
        String urn = categorySchemeVersionCreated.getNameableArtefact().getUrn();

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        CategoryMetamac categoryRetrieved = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
        CategoriesMetamacAsserts.assertEqualsCategory(category, categoryRetrieved);

        // Validate new structure
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertEquals(5, categorySchemeVersion.getItemsFirstLevel().size());
        assertEquals(9, categorySchemeVersion.getItems().size());
        assertListCategoriesContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_3);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_4);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItemsFirstLevel(), categoryRetrieved.getNameableArtefact().getUrn());
    }

    /*-------------------------*/
    /* CATEGORY ELEMENTS TESTS */
    /*-------------------------*/

    @Test
    public void testCreateCategoryWithCategoryElement() throws Exception {

        CategoryMetamac category = CategoriesMetamacDoMocks.mockCategory(retrieveAnnotationType_1());
        category.setParent(null);
        category.setCategoryElement(categoriesService.retrieveCategoryElementByUrn(getServiceContextAdministrador(), CATEGORY_ELEMENT_1_URN));
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        // Create
        CategoryMetamac categorySchemeVersionCreated = categoriesService.createCategory(getServiceContextAdministrador(), categorySchemeUrn, category);
        String urn = categorySchemeVersionCreated.getNameableArtefact().getUrn();

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        CategoryMetamac categoryRetrieved = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
        CategoriesMetamacAsserts.assertEqualsCategory(category, categoryRetrieved);

    }

    @Test
    public void testUpdateCodesCategoryElements() throws Exception {
        String categorySchemetUrn = CATEGORY_SCHEME_1_V2;

        CategoryMetamac category1 = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        CategoryMetamac category2 = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
        CategoryMetamac category21 = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_2_1);
        CategoryMetamac category211 = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1);
        CategoryMetamac category3 = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_3);
        CategoryMetamac category4 = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_4);
        CategoryMetamac category41 = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_4_1);

        CategoryElement categoryElement1 = categoriesService.retrieveCategoryElementByUrn(getServiceContextAdministrador(), CATEGORY_ELEMENT_1_URN);
        CategoryElement categoryElement2 = categoriesService.retrieveCategoryElementByUrn(getServiceContextAdministrador(), CATEGORY_ELEMENT_2_URN);
        CategoryElement categoryElement3 = categoriesService.retrieveCategoryElementByUrn(getServiceContextAdministrador(), CATEGORY_ELEMENT_3_URN);

        Map<Long, Long> categoryElementsIdByCodeId = new HashMap<Long, Long>();
        categoryElementsIdByCodeId.put(category1.getId(), categoryElement1.getId());
        categoryElementsIdByCodeId.put(category2.getId(), categoryElement1.getId());
        categoryElementsIdByCodeId.put(category21.getId(), null);
        categoryElementsIdByCodeId.put(category211.getId(), categoryElement3.getId());
        categoryElementsIdByCodeId.put(category3.getId(), null);
        categoryElementsIdByCodeId.put(category4.getId(), categoryElement3.getId());
        categoryElementsIdByCodeId.put(category41.getId(), categoryElement2.getId());

        categoriesService.updateCodesCategoryElements(getServiceContextAdministrador(), categorySchemetUrn, categoryElementsIdByCodeId);

        entityManager.clear();
        {
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), category1.getNameableArtefact().getUrn());
            assertEquals(categoryElement1.getIdentifiableArtefact().getUrn(), category.getCategoryElement().getIdentifiableArtefact().getUrn());
        }
        {
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), category2.getNameableArtefact().getUrn());
            assertEquals(categoryElement1.getIdentifiableArtefact().getUrn(), category.getCategoryElement().getIdentifiableArtefact().getUrn());
        }
        {
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), category21.getNameableArtefact().getUrn());
            assertNull(category.getCategoryElement());
        }
        {
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), category211.getNameableArtefact().getUrn());
            assertEquals(categoryElement3.getIdentifiableArtefact().getUrn(), category.getCategoryElement().getIdentifiableArtefact().getUrn());
        }
        {
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), category3.getNameableArtefact().getUrn());
            assertNull(category.getCategoryElement());
        }
        {
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), category4.getNameableArtefact().getUrn());
            assertEquals(categoryElement3.getIdentifiableArtefact().getUrn(), category.getCategoryElement().getIdentifiableArtefact().getUrn());
        }
        {
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), category41.getNameableArtefact().getUrn());
            assertEquals(categoryElement2.getIdentifiableArtefact().getUrn(), category.getCategoryElement().getIdentifiableArtefact().getUrn());
        }
    }

    @Test
    public void testUpdateCodeCategoryElementInCategorySchemePublished() throws Exception {
        String categoryUrn = CATEGORY_SCHEME_1_V1_CATEGORY_1;
        String categorySchemeUrn = CATEGORY_SCHEME_1_V1;

        CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), categoryUrn);
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertEquals(ProcStatusEnum.INTERNALLY_PUBLISHED, categorySchemeVersion.getLifeCycleMetadata().getProcStatus());
        assertEquals(CATEGORY_ELEMENT_1_URN, category.getCategoryElement().getIdentifiableArtefact().getUrn());

        // Add category element
        String categoryElementUrnNew = CATEGORY_ELEMENT_2_URN;
        categoriesService.updateCategoryElementOfCategory(getServiceContextAdministrador(), category.getNameableArtefact().getUrn(), categoryElementUrnNew);
        category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), categoryUrn);
        assertEquals(categoryElementUrnNew, category.getCategoryElement().getIdentifiableArtefact().getUrn());
    }

    @Test
    public void testRetrieveCategoryByUrnWithoutCategoryElement() throws Exception {
        // Retrieve
        String urn = CATEGORY_SCHEME_3_V1_CATEGORY_1;
        CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        assertEquals(urn, category.getNameableArtefact().getUrn());
        assertNull(category.getCategoryElement());
    }

    @Test
    public void testRetrieveCategoriesByCategorySchemeUrnCheckCategoryElements() throws Exception {

        // Retrieve
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        // LOCALE = 'es'
        {
            String locale = "es";
            List<CategoryMetamacVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(getServiceContextAdministrador(), categorySchemeUrn, locale, null);

            // Validate
            assertEquals(8, categories.size());
            {
                // Code 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_1);
                assertEquals(Long.valueOf(2), category.getCategoryElement().getIdDatabase());
                assertEquals(CATEGORY_ELEMENT_2_URN, category.getCategoryElement().getUrn());
                assertEquals(CATEGORY_ELEMENT_02, category.getCategoryElement().getCode());
                assertEquals("shortName category_element-2", category.getCategoryElement().getShortName());
            }
            {
                // Code 02
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2);
                assertEquals(Long.valueOf(3), category.getCategoryElement().getIdDatabase());
                assertEquals(CATEGORY_ELEMENT_3_URN, category.getCategoryElement().getUrn());
                assertEquals(CATEGORY_ELEMENT_03, category.getCategoryElement().getCode());
                assertEquals("shortName category_element-3", category.getCategoryElement().getShortName());
            }
            {
                // Code 02 01 (validate parent)
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1);
                assertEquals(Long.valueOf(1), category.getCategoryElement().getIdDatabase());
                assertEquals(CATEGORY_ELEMENT_1_URN, category.getCategoryElement().getUrn());
                assertEquals(CATEGORY_ELEMENT_01, category.getCategoryElement().getCode());
                assertEquals("shortName category_element-1", category.getCategoryElement().getShortName());
            }
            {
                // Code 02 01 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1);
                assertEquals(Long.valueOf(1), category.getCategoryElement().getIdDatabase());
                assertEquals(CATEGORY_ELEMENT_1_URN, category.getCategoryElement().getUrn());
                assertEquals(CATEGORY_ELEMENT_01, category.getCategoryElement().getCode());
                assertEquals("shortName category_element-1", category.getCategoryElement().getShortName());
            }
            {
                // Code 03
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_3);
                assertEquals(Long.valueOf(2), category.getCategoryElement().getIdDatabase());
                assertEquals(CATEGORY_ELEMENT_2_URN, category.getCategoryElement().getUrn());
                assertEquals(CATEGORY_ELEMENT_02, category.getCategoryElement().getCode());
                assertEquals("shortName category_element-2", category.getCategoryElement().getShortName());
            }
            {
                // Code 04
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4);
                assertEquals(Long.valueOf(1), category.getCategoryElement().getIdDatabase());
                assertEquals(CATEGORY_ELEMENT_1_URN, category.getCategoryElement().getUrn());
                assertEquals(CATEGORY_ELEMENT_01, category.getCategoryElement().getCode());
                assertEquals("shortName category_element-1", category.getCategoryElement().getShortName());
            }
            {
                // Code 04 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1);
                assertEquals(Long.valueOf(2), category.getCategoryElement().getIdDatabase());
                assertEquals(CATEGORY_ELEMENT_2_URN, category.getCategoryElement().getUrn());
                assertEquals(CATEGORY_ELEMENT_02, category.getCategoryElement().getCode());
                assertEquals("shortName category_element-2", category.getCategoryElement().getShortName());
            }
            {
                // Code 04 01 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1);
                assertEquals(null, category.getCategoryElement());
            }
        }

    }

    @Test
    public void testNormaliseCategoryElementsToCategories() throws Exception {

        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        List<CodeCategoryElementNormalisationResult> results = categoriesService.normaliseCategoryElementsToCategories(getServiceContextAdministrador(), categorySchemeUrn, "es", false);
        assertEquals(8, results.size());

        {
            String categoryUrn = CATEGORY_SCHEME_1_V2_CATEGORY_1;
            CodeCategoryElementNormalisationResult result = assertContainsCodeCategoryElementNormalisationResult(categoryUrn, results);
            assertEquals("Nombre categoryScheme-1-v2-category-1", result.getCode().getName());
            assertEquals(CATEGORY_ELEMENT_4_URN, result.getCategoryElementProposed().getUrn());
            assertEquals("shortname-at-element categoryScheme-1-v2-category-1", result.getCategoryElementProposed().getShortName());
        }
        {
            String categoryUrn = CATEGORY_SCHEME_1_V2_CATEGORY_2;
            CodeCategoryElementNormalisationResult result = assertContainsCodeCategoryElementNormalisationResult(categoryUrn, results);
            assertEquals("Nombre categoryScheme-1-v2-category-2", result.getCode().getName());
            assertEquals(CATEGORY_ELEMENT_5_URN, result.getCategoryElementProposed().getUrn());
            assertEquals("shortname-at-element categoryScheme-1-v2-category-2", result.getCategoryElementProposed().getShortName());
        }
        {
            String categoryUrn = CATEGORY_SCHEME_1_V2_CATEGORY_2_1;
            CodeCategoryElementNormalisationResult result = assertContainsCodeCategoryElementNormalisationResult(categoryUrn, results);
            assertEquals("Nombre categoryScheme-1-v2-category-2-1", result.getCode().getName());
            assertEquals(CATEGORY_ELEMENT_5_URN, result.getCategoryElementProposed().getUrn());
            assertEquals("shortname-at-element categoryScheme-1-v2-category-2", result.getCategoryElementProposed().getShortName());
        }
        {
            String categoryUrn = CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1;
            CodeCategoryElementNormalisationResult result = assertContainsCodeCategoryElementNormalisationResult(categoryUrn, results);
            assertEquals("Nombre categoryScheme-1-v2-category-2-1-1", result.getCode().getName());
            assertEquals(CATEGORY_ELEMENT_5_URN, result.getCategoryElementProposed().getUrn());
            assertEquals("shortname-at-element categoryScheme-1-v2-category-2", result.getCategoryElementProposed().getShortName());
        }
        {
            String categoryUrn = CATEGORY_SCHEME_1_V2_CATEGORY_3;
            CodeCategoryElementNormalisationResult result = assertContainsCodeCategoryElementNormalisationResult(categoryUrn, results);
            assertEquals("nombre category-3", result.getCode().getName());
            assertEquals(CATEGORY_ELEMENT_6_URN, result.getCategoryElementProposed().getUrn());
            assertEquals("shortname-at-element categoryScheme-1-v2-category-3", result.getCategoryElementProposed().getShortName());
        }
        {
            String categoryUrn = CATEGORY_SCHEME_1_V2_CATEGORY_4;
            CodeCategoryElementNormalisationResult result = assertContainsCodeCategoryElementNormalisationResult(categoryUrn, results);
            assertEquals("Lanzarote", result.getCode().getName());
            assertEquals(null, result.getCategoryElementProposed());
        }
        {
            String categoryUrn = CATEGORY_SCHEME_1_V2_CATEGORY_4_1;
            CodeCategoryElementNormalisationResult result = assertContainsCodeCategoryElementNormalisationResult(categoryUrn, results);
            assertEquals("La Gomera", result.getCode().getName());
            assertEquals(null, result.getCategoryElementProposed());
        }
        {
            String categoryUrn = CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1;
            CodeCategoryElementNormalisationResult result = assertContainsCodeCategoryElementNormalisationResult(categoryUrn, results);
            assertEquals("Nombre categoryScheme-1-v2-category-4-1-1 Santa Cruz de Tenerife", result.getCode().getName());
            assertEquals(CATEGORY_ELEMENT_7_URN, result.getCategoryElementProposed().getUrn());
            assertEquals("Santa Cruz de Tenerife", result.getCategoryElementProposed().getShortName());
        }
    }

    @Test
    public void testNormaliseCategoryElementsToCategoriesOnlyWithoutCategoryElements() throws Exception {

        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        List<CodeCategoryElementNormalisationResult> results = categoriesService.normaliseCategoryElementsToCategories(getServiceContextAdministrador(), categorySchemeUrn, "es", true);
        assertEquals(8, results.size());

        {
            String categoryUrn = CATEGORY_SCHEME_1_V2_CATEGORY_1;
            CodeCategoryElementNormalisationResult result = assertContainsCodeCategoryElementNormalisationResult(categoryUrn, results);
            assertEquals("Nombre categoryScheme-1-v2-category-1", result.getCode().getName());
            assertEquals(CATEGORY_ELEMENT_2_URN, result.getCategoryElementProposed().getUrn());
            assertEquals("shortName category_element-2", result.getCategoryElementProposed().getShortName());
        }
        {
            String categoryUrn = CATEGORY_SCHEME_1_V2_CATEGORY_2;
            CodeCategoryElementNormalisationResult result = assertContainsCodeCategoryElementNormalisationResult(categoryUrn, results);
            assertEquals("Nombre categoryScheme-1-v2-category-2", result.getCode().getName());
            assertEquals(CATEGORY_ELEMENT_3_URN, result.getCategoryElementProposed().getUrn());
            assertEquals("shortName category_element-3", result.getCategoryElementProposed().getShortName());
        }
        {
            String categoryUrn = CATEGORY_SCHEME_1_V2_CATEGORY_2_1;
            CodeCategoryElementNormalisationResult result = assertContainsCodeCategoryElementNormalisationResult(categoryUrn, results);
            assertEquals("Nombre categoryScheme-1-v2-category-2-1", result.getCode().getName());
            assertEquals(CATEGORY_ELEMENT_1_URN, result.getCategoryElementProposed().getUrn());
            assertEquals("shortName category_element-1", result.getCategoryElementProposed().getShortName());
        }
        {
            String categoryUrn = CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1;
            CodeCategoryElementNormalisationResult result = assertContainsCodeCategoryElementNormalisationResult(categoryUrn, results);
            assertEquals("Nombre categoryScheme-1-v2-category-2-1-1", result.getCode().getName());
            assertEquals(CATEGORY_ELEMENT_1_URN, result.getCategoryElementProposed().getUrn());
            assertEquals("shortName category_element-1", result.getCategoryElementProposed().getShortName());
        }
        {
            String categoryUrn = CATEGORY_SCHEME_1_V2_CATEGORY_3;
            CodeCategoryElementNormalisationResult result = assertContainsCodeCategoryElementNormalisationResult(categoryUrn, results);
            assertEquals("nombre category-3", result.getCode().getName());
            assertEquals(CATEGORY_ELEMENT_2_URN, result.getCategoryElementProposed().getUrn());
            assertEquals("shortName category_element-2", result.getCategoryElementProposed().getShortName());
        }
        {
            String categoryUrn = CATEGORY_SCHEME_1_V2_CATEGORY_4;
            CodeCategoryElementNormalisationResult result = assertContainsCodeCategoryElementNormalisationResult(categoryUrn, results);
            assertEquals("Lanzarote", result.getCode().getName());
            assertEquals(CATEGORY_ELEMENT_1_URN, result.getCategoryElementProposed().getUrn());
            assertEquals("shortName category_element-1", result.getCategoryElementProposed().getShortName());
        }
        {
            String categoryUrn = CATEGORY_SCHEME_1_V2_CATEGORY_4_1;
            CodeCategoryElementNormalisationResult result = assertContainsCodeCategoryElementNormalisationResult(categoryUrn, results);
            assertEquals("La Gomera", result.getCode().getName());
            assertEquals(CATEGORY_ELEMENT_2_URN, result.getCategoryElementProposed().getUrn());
            assertEquals("shortName category_element-2", result.getCategoryElementProposed().getShortName());
        }
        {
            String categoryUrn = CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1;
            CodeCategoryElementNormalisationResult result = assertContainsCodeCategoryElementNormalisationResult(categoryUrn, results);
            assertEquals("Nombre categoryScheme-1-v2-category-4-1-1 Santa Cruz de Tenerife", result.getCode().getName());
            assertEquals(CATEGORY_ELEMENT_7_URN, result.getCategoryElementProposed().getUrn());
            assertEquals("Santa Cruz de Tenerife", result.getCategoryElementProposed().getShortName());
        }
    }

    private CodeCategoryElementNormalisationResult assertContainsCodeCategoryElementNormalisationResult(String categoryUrnExpected, List<CodeCategoryElementNormalisationResult> actuals) {
        for (CodeCategoryElementNormalisationResult actual : actuals) {
            if (actual.getCode().getUrn().equals(categoryUrnExpected)) {
                return actual;
            }
        }
        fail("not found");
        return null;
    }

    @Test
    @Override
    public void testCreateCategoryElement() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CategoryElement categoryElement = CategoriesMetamacDoMocks.mockCategoryElement();

        // Create
        CategoryElement categoryElementCreated = categoriesService.createCategoryElement(ctx, categoryElement);
        String urn = categoryElementCreated.getIdentifiableArtefact().getUrn();

        // Validate
        assertNotNull(urn);
        CategoryElement categoryElementRetrieved = categoriesService.retrieveCategoryElementByUrn(ctx, urn);

        assertEquals(getServiceContextAdministrador().getUserId(), categoryElementRetrieved.getCreatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), categoryElementRetrieved.getCreatedDate().toDate()));
        assertEquals(getServiceContextAdministrador().getUserId(), categoryElementRetrieved.getLastUpdatedBy());
        assertTrue(DateUtils.isSameDay(new Date(), categoryElementRetrieved.getLastUpdated().toDate()));

        assertEqualsCategoryeElement(categoryElement, categoryElementRetrieved);

    }

    @Test
    @Override
    public void testUpdateCategoryElement() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        DateTime iniDate = new DateTime(2013, 10, 1, 10, 12, 13, 14);
        DateTime endDate = new DateTime(2014, 10, 1, 10, 12, 13, 14);

        CategoryElement categoryElement = categoriesService.retrieveCategoryElementByUrn(getServiceContextAdministrador(), CATEGORY_ELEMENT_1_URN);
        categoryElement.setValidTo(endDate);
        categoryElement.setValidFrom(iniDate);
        categoryElement.getIdentifiableArtefact().setIsCodeUpdated(false);
        categoryElement.setIsVisible(false);
        // Create
        CategoryElement categoryElementUpdated = categoriesService.updateCategoryElement(ctx, categoryElement);
        assertEqualsDate(iniDate, categoryElementUpdated.getValidFrom());
        assertEqualsDate(endDate, categoryElementUpdated.getValidTo());
        assertEquals(false, categoryElementUpdated.getIsVisible());
        // Validate

        CategoryElement categoryElementRetrieved = categoriesService.retrieveCategoryElementByUrn(ctx, CATEGORY_ELEMENT_1_URN);
        assertEqualsDate(iniDate, categoryElementRetrieved.getValidFrom());
        assertEqualsDate(endDate, categoryElementRetrieved.getValidTo());
        assertEquals(false, categoryElement.getIsVisible());

    }

    @Test
    @Override
    public void testRetrieveCategoryElementByUrn() throws Exception {
        CategoryElement categoryElement = categoriesService.retrieveCategoryElementByUrn(getServiceContextAdministrador(), CATEGORY_ELEMENT_1_URN);

        assertEquals(CATEGORY_ELEMENT_1_URN, categoryElement.getIdentifiableArtefact().getUrn());
        assertEquals(CATEGORY_ELEMENT_01, categoryElement.getIdentifiableArtefact().getCode());
        assertEqualsInternationalString(categoryElement.getShortName(), "es", "shortName category_element-1", null, null);
        assertEqualsDate(new DateTime(2023, 01, 02, 02, 02, 04, 0, new DateTimeZoneBuilder().toDateTimeZone("Europe/London", false)), categoryElement.getValidFrom());
        assertEqualsDate(new DateTime(2025, 01, 02, 02, 02, 04, 0, new DateTimeZoneBuilder().toDateTimeZone("Europe/London", false)), categoryElement.getValidTo());

        assertEquals("category-element-1", categoryElement.getUuid());
        assertEquals("user1", categoryElement.getCreatedBy());
        assertEquals("user2", categoryElement.getLastUpdatedBy());
        assertEquals(Long.valueOf(1), categoryElement.getVersion());
        assertEquals(true, categoryElement.getIsVisible());
    }

    @Test
    @Override
    public void testFindCategoryElementsByCondition() throws Exception {
        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CategoryElement.class).orderBy(CategoryElementProperties.identifiableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CategoryElement> result = categoriesService.findCategoryElementsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            assertEquals(7, result.getTotalRows());
            int i = 0;

            assertEquals(CATEGORY_ELEMENT_1_URN, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(CATEGORY_ELEMENT_2_URN, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(CATEGORY_ELEMENT_3_URN, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(CATEGORY_ELEMENT_4_URN, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(CATEGORY_ELEMENT_5_URN, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(CATEGORY_ELEMENT_6_URN, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
            assertEquals(CATEGORY_ELEMENT_7_URN, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
        }
        // Find by urn
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CategoryElement.class).withProperty(CategoryElementProperties.identifiableArtefact().urn())
                    .like(CATEGORY_ELEMENT_4_URN).orderBy(CategoryElementProperties.identifiableArtefact().urn()).build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CategoryElement> result = categoriesService.findCategoryElementsByCondition(getServiceContextAdministrador(), conditions, pagingParameter);
            assertEquals(1, result.getTotalRows());
            int i = 0;
            assertEquals(CATEGORY_ELEMENT_4_URN, result.getValues().get(i++).getIdentifiableArtefact().getUrn());
        }
    }

    @Test
    @Override
    public void testUpdateCategoryElementOfCategory() throws Exception {
        String urn = CATEGORY_SCHEME_1_V2_CATEGORY_1;
        CategoryMetamac categoryMetamac = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
        assertEquals(CATEGORY_ELEMENT_2_URN, categoryMetamac.getCategoryElement().getIdentifiableArtefact().getUrn());

        // Change category element
        String categoryElementUrnNew = CATEGORY_ELEMENT_3_URN;
        categoriesService.updateCategoryElementOfCategory(getServiceContextAdministrador(), categoryMetamac.getNameableArtefact().getUrn(), categoryElementUrnNew);
        categoryMetamac = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
        assertEquals(categoryElementUrnNew, categoryMetamac.getCategoryElement().getIdentifiableArtefact().getUrn());

        // Reset category element
        categoriesService.updateCategoryElementOfCategory(getServiceContextAdministrador(), categoryMetamac.getNameableArtefact().getUrn(), null);
        categoryMetamac = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
        assertNull(categoryElementUrnNew, categoryMetamac.getCategoryElement());
    }

    @Test
    @Override
    public void testDeleteCategoryElement() throws Exception {
        String categoryElementUrn = CATEGORY_ELEMENT_6_URN;
        CategoryElement categoryElement = categoriesService.retrieveCategoryElementByUrn(getServiceContextAdministrador(), categoryElementUrn);
        categoriesService.deleteCategoryElement(getServiceContextAdministrador(), categoryElementUrn);

        try {
            categoriesService.retrieveCategoryElementByUrn(getServiceContextAdministrador(), categoryElementUrn);
            fail("categoryElement already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{categoryElementUrn}, e.getExceptionItems().get(0));
        }
        // Try to delete again the deleted categoryElement
        try {
            categoriesService.deleteCategoryElement(getServiceContextAdministrador(), categoryElementUrn);
            fail("categoryElement already deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{categoryElementUrn}, e.getExceptionItems().get(0));
        }

    }

    @Test
    @Override
    public void testExportCategoryElementsTsv() throws Exception {
        List<String> categoryElementUrn = new ArrayList<String>();
        categoryElementUrn.add(CATEGORY_ELEMENT_1_URN);
        categoryElementUrn.add(CATEGORY_ELEMENT_2_URN);
        categoryElementUrn.add(CATEGORY_ELEMENT_3_URN);
        categoryElementUrn.add(CATEGORY_ELEMENT_4_URN);

        String fileName = categoriesService.exportCategoryElementsTsv(getServiceContextAdministrador(), categoryElementUrn);
        assertNotNull(fileName);

        // Validate
        File file = new File(tempDirPath() + File.separatorChar + fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        List<String> lines = new ArrayList<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line.replaceAll("\t", "\\\\t"));
            lines.add(line);
        }

        Iterator<String> iterator = lines.iterator();

        assertEquals("code\tshort_name#es\tshort_name#pt\tshort_name#en\tshort_name#ca\tis_visible", iterator.next());

        assertTrue(lines.contains("CATEGORY_ELEMENT_01\tshortName category_element-1\t\t\t\ttrue"));
        assertTrue(lines.contains("CATEGORY_ELEMENT_02\tshortName category_element-2\t\t\t\tfalse"));
        assertTrue(lines.contains("CATEGORY_ELEMENT_03\tshortName category_element-3\t\t\t\tfalse"));
        assertTrue(lines.contains("CATEGORY_ELEMENT_04\tshortname-at-element categoryScheme-1-v2-category-1\t\t\t\ttrue"));

        bufferedReader.close();
    }

    @Override
    @Test
    @DirtyDatabase
    public void testImportCategoryElementsTsv() throws Exception {

        String fileName = "importation-category-element-1.tsv";
        File file = new File(this.getClass().getResource("/tsv/" + fileName).getFile());
        boolean updateAlreadyExisting = false;

        TaskImportationInfo taskImportTsvInfo = categoriesService.importCategoryElementsTsv(getServiceContextAdministrador(), file, fileName, updateAlreadyExisting, Boolean.TRUE);

        // Validate
        assertEquals(false, taskImportTsvInfo.getIsPlannedInBackground());
        assertNull(taskImportTsvInfo.getJobKey());
        assertEquals(0, taskImportTsvInfo.getInformationItems().size());

        // Validate category elements
        {
            String code = "CATEGORY_ELEMENT_IMP_01";
            String urn = BASE_URN_CATEGORY_ELEMENT + code;
            CategoryElement categoryElement = categoriesService.retrieveCategoryElementByUrn(getServiceContextAdministrador(), urn);
            assertEquals(code, categoryElement.getIdentifiableArtefact().getCode());
            assertEquals(categoryElement.getIdentifiableArtefact().getUrn(), categoryElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(categoryElement.getShortName(), "es", "shortName category_element_imp_01 es", "en", "shortName category_element_imp_01 en", "ca",
                    "shortName category_element_imp_01 ca");
            assertEquals(null, categoryElement.getValidFrom());
            assertEquals(null, categoryElement.getValidTo());
            assertEquals(Boolean.TRUE, categoryElement.getIsVisible());
            BaseAsserts.assertEqualsDay(new DateTime(), categoryElement.getCreatedDate());
        }
        {
            String code = "CATEGORY_ELEMENT_IMP_02";
            String urn = BASE_URN_CATEGORY_ELEMENT + code;
            CategoryElement categoryElement = categoriesService.retrieveCategoryElementByUrn(getServiceContextAdministrador(), urn);
            assertEquals(code, categoryElement.getIdentifiableArtefact().getCode());
            assertEquals(categoryElement.getIdentifiableArtefact().getUrn(), categoryElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(categoryElement.getShortName(), "es", "shortName category_element_imp_02 es", "en", "shortName category_element_imp_02 en", null, null);
            assertEquals(null, categoryElement.getValidFrom());
            assertEquals(null, categoryElement.getValidTo());
            assertEquals(Boolean.FALSE, categoryElement.getIsVisible());
            BaseAsserts.assertEqualsDay(new DateTime(), categoryElement.getCreatedDate());
        }
        {
            String code = "CATEGORY_ELEMENT_IMP_03";
            String urn = BASE_URN_CATEGORY_ELEMENT + code;
            CategoryElement categoryElement = categoriesService.retrieveCategoryElementByUrn(getServiceContextAdministrador(), urn);
            assertEquals(code, categoryElement.getIdentifiableArtefact().getCode());
            assertEquals(categoryElement.getIdentifiableArtefact().getUrn(), categoryElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(categoryElement.getShortName(), "es", "shortName category_element_imp_03 es", "en", "shortName category_element_imp_03 en", "ca",
                    "shortName category_element_imp_03 ca");
            assertEquals(null, categoryElement.getValidFrom());
            assertEquals(null, categoryElement.getValidTo());
            assertEquals(Boolean.FALSE, categoryElement.getIsVisible());
            BaseAsserts.assertEqualsDay(new DateTime(), categoryElement.getCreatedDate());
        }
        {
            String code = "CATEGORY_ELEMENT_IMP_04";
            String urn = BASE_URN_CATEGORY_ELEMENT + code;
            CategoryElement categoryElement = categoriesService.retrieveCategoryElementByUrn(getServiceContextAdministrador(), urn);
            assertEquals(code, categoryElement.getIdentifiableArtefact().getCode());
            assertEquals(categoryElement.getIdentifiableArtefact().getUrn(), categoryElement.getIdentifiableArtefact().getUrnProvider());
            assertEqualsInternationalString(categoryElement.getShortName(), "es", "shortName category_element_imp_04 es", "en", "shortName category_element_imp_04 en", null, null);
            assertEquals(null, categoryElement.getValidFrom());
            assertEquals(null, categoryElement.getValidTo());
            assertEquals(Boolean.TRUE, categoryElement.getIsVisible());
            BaseAsserts.assertEqualsDay(new DateTime(), categoryElement.getCreatedDate());
        }
    }

    @Test
    @DirtyDatabase
    public void testImportCategoryElementsTsvErrorWithHeaderIncorrect() throws Exception {

        final String fileName = "importation-category-element-2-errors-header.tsv";
        final File file = new File(this.getClass().getResource("/tsv/" + fileName).getFile());
        final boolean updateAlreadyExisting = false;

        try {
            categoriesService.importCategoryElementsTsv(getServiceContextAdministrador(), file, fileName, updateAlreadyExisting, Boolean.TRUE);
        } catch (MetamacException e) {
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR, 1, new Serializable[]{fileName}, e.getPrincipalException());
            assertEquals(1, e.getExceptionItems().size());
            int i = 0;
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, 1, new Serializable[]{ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE},
                    e.getExceptionItems().get(i++));
            assertEquals(e.getExceptionItems().size(), i);
        }
    }

    @Test
    @DirtyDatabase
    public void testImportVariableElementsTsvErrorWithBodyIncorrect() throws Exception {

        // Import
        final String fileName = "importation-category-element-3-errors-body.tsv";
        final File file = new File(this.getClass().getResource("/tsv/" + fileName).getFile());
        final boolean updateAlreadyExisting = false;

        try {
            categoriesService.importCategoryElementsTsv(getServiceContextAdministrador(), file, fileName, updateAlreadyExisting, Boolean.TRUE);
        } catch (MetamacException e) {
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR, 1, new Serializable[]{fileName}, e.getPrincipalException());
            assertEquals(4, e.getExceptionItems().size());
            int i = 0;
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, 1, new Serializable[]{3}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_REQUIRED, 2, new Serializable[]{"categoryElement4WithoutShortName", "parameter.srm.importation.short_name"},
                    e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_INCORRECT_SEMANTIC_IDENTIFIER, 2,
                    new Serializable[]{"#categoryElementNotSemanticCode", "parameter.srm.importation.code"}, e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, 1, new Serializable[]{8}, e.getExceptionItems().get(i++));
            assertEquals(e.getExceptionItems().size(), i);
        }
    }

    @Test
    @DirtyDatabase
    public void testImportCategoryElementsTsvUpdatingAlreadyExistingCodes() throws Exception {

        final boolean updateAlreadyExisting = true;

        final String fileName = "importation-category-element-4.tsv";
        final File file = new File(this.getClass().getResource("/tsv/" + fileName).getFile());

        TaskImportationInfo taskImportTsvInfo = categoriesService.importCategoryElementsTsv(getServiceContextAdministrador(), file, fileName, updateAlreadyExisting, Boolean.TRUE);

        // Validate
        assertEquals(false, taskImportTsvInfo.getIsPlannedInBackground());
        assertNull(taskImportTsvInfo.getJobKey());
        assertEquals(2, taskImportTsvInfo.getInformationItems().size());

        int i = 0;
        assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_UPDATED, 1, new Serializable[]{CATEGORY_ELEMENT_02}, taskImportTsvInfo.getInformationItems().get(i++));
        assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_UPDATED, 1, new Serializable[]{CATEGORY_ELEMENT_04}, taskImportTsvInfo.getInformationItems().get(i++));
        assertEquals(taskImportTsvInfo.getInformationItems().size(), i);

    }

    @Test
    public void testCreateCategorySubcategory() throws Exception {

        CategoryMetamac category = CategoriesMetamacDoMocks.mockCategory(retrieveAnnotationType_1());
        CategoryMetamac categoryParent = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        category.setParent(categoryParent);
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        // Create
        CategoryMetamac categorySchemeVersionCreated = categoriesService.createCategory(getServiceContextAdministrador(), categorySchemeUrn, category);
        String urn = categorySchemeVersionCreated.getNameableArtefact().getUrn();

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        CategoryMetamac categoryRetrieved = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
        CategoriesMetamacAsserts.assertEqualsCategory(category, categoryRetrieved);

        // Validate new structure
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertEquals(4, categorySchemeVersion.getItemsFirstLevel().size());
        assertEquals(9, categorySchemeVersion.getItems().size());

        Category category1 = assertListCategoriesContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        assertListCategoriesContainsCategory(category1.getChildren(), categoryRetrieved.getNameableArtefact().getUrn());
    }

    @Test
    public void testCreateCategoryErrorCategoryImported() throws Exception {
        CategoryMetamac category = CategoriesMetamacDoMocks.mockCategory(retrieveAnnotationType_1());
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        // save to force incorrect metadata
        categorySchemeVersion.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        categorySchemeVersion.getMaintainableArtefact().setMaintainer(retrieveOrganisationAgency1());
        itemSchemeRepository.save(categorySchemeVersion);

        try {
            categoriesService.createCategory(getServiceContextAdministrador(), categorySchemeUrn, category);
            fail("imported");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.STRUCTURE_MODIFICATIONS_NOT_SUPPORTED_IMPORTED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Override
    @Test
    public void testUpdateCategory() throws Exception {

        CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        category.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);
        category.getNameableArtefact().setName(CategoriesDoMocks.mockInternationalString());

        // Update
        CategoryMetamac categoryUpdated = categoriesService.updateCategory(getServiceContextAdministrador(), category);

        // Validate
        CategoriesMetamacAsserts.assertEqualsCategory(category, categoryUpdated);
    }

    @Override
    @Test
    public void testRetrieveCategoryByUrn() throws Exception {
        // Retrieve
        String urn = CATEGORY_SCHEME_1_V2_CATEGORY_1;
        CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
        assertEquals(urn, category.getNameableArtefact().getUrn());

        // Validate (only metadata in SRM Metamac; the others are checked in sdmx project)
        assertEquals(CATEGORY_ELEMENT_2_URN, category.getCategoryElement().getIdentifiableArtefact().getUrn());

    }

    @Test
    public void testRetrieveCategoryByUrnWithParentAndChildren() throws Exception {

        // Retrieve
        String urn = CATEGORY_SCHEME_1_V2_CATEGORY_2_1;
        CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);

        // Validate
        // Parent
        assertTrue(category.getParent() instanceof CategoryMetamac);
        assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, category.getParent().getNameableArtefact().getUrn());
        // Children
        assertEquals(1, category.getChildren().size());
        assertTrue(category.getChildren().get(0) instanceof CategoryMetamac);
        assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, category.getChildren().get(0).getNameableArtefact().getUrn());
    }

    @Override
    @Test
    public void testDeleteCategory() throws Exception {

        String urn = CATEGORY_SCHEME_1_V2_CATEGORY_3;
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertEquals(4, categorySchemeVersion.getItemsFirstLevel().size());
        assertEquals(8, categorySchemeVersion.getItems().size());

        // Delete category
        categoriesService.deleteCategory(getServiceContextAdministrador(), urn);

        // Validation
        try {
            categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
            fail("Category deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        // Check hierarchy
        categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertEquals(3, categorySchemeVersion.getItemsFirstLevel().size());
        assertListCategoriesContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_4);
        assertEquals(7, categorySchemeVersion.getItems().size());
        assertListCategoriesContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_2_1);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_4);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_4_1);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1);
    }

    @Test
    public void testDeleteCategoryWithParentAndChildren() throws Exception {

        String urn = CATEGORY_SCHEME_1_V2_CATEGORY_4_1;
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertEquals(4, categorySchemeVersion.getItemsFirstLevel().size());
        assertEquals(8, categorySchemeVersion.getItems().size());

        // Delete category
        categoriesService.deleteCategory(getServiceContextAdministrador(), urn);

        // Validation
        try {
            categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
            fail("Category deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }

        categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertEquals(4, categorySchemeVersion.getItemsFirstLevel().size());
        assertListCategoriesContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_3);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItemsFirstLevel(), CATEGORY_SCHEME_1_V2_CATEGORY_4);
        assertEquals(6, categorySchemeVersion.getItems().size());
        assertListCategoriesContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_2_1);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_3);
        assertListCategoriesContainsCategory(categorySchemeVersion.getItems(), CATEGORY_SCHEME_1_V2_CATEGORY_4);
    }

    @Test
    public void testDeleteCategoryErrorCategorySchemePublished() throws Exception {

        String urn = CATEGORY_SCHEME_1_V1_CATEGORY_1;
        String categorySchemeUrn = CATEGORY_SCHEME_1_V1;

        // Validation
        try {
            categoriesService.deleteCategory(getServiceContextAdministrador(), urn);
            fail("Category can not be deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.LIFE_CYCLE_WRONG_PROC_STATUS.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(2, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(categorySchemeUrn, e.getExceptionItems().get(0).getMessageParameters()[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DRAFT, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[0]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_PRODUCTION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[1]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_DIFFUSION_VALIDATION, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[2]);
            assertEquals(ServiceExceptionParameters.PROC_STATUS_VALIDATION_REJECTED, ((String[]) e.getExceptionItems().get(0).getMessageParameters()[1])[3]);
        }
    }

    @Test
    public void testDeleteCategoryErrorCategorySchemeImported() throws Exception {
        String urn = CATEGORY_SCHEME_1_V2_CATEGORY_3;
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        // save to force incorrect metadata
        categorySchemeVersion.getMaintainableArtefact().setIsImported(Boolean.TRUE);
        categorySchemeVersion.getMaintainableArtefact().setMaintainer(retrieveOrganisationAgency1());
        itemSchemeRepository.save(categorySchemeVersion);

        try {
            categoriesService.deleteCategory(getServiceContextAdministrador(), urn);
            fail("imported");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.STRUCTURE_MODIFICATIONS_NOT_SUPPORTED_IMPORTED.getCode(), e.getExceptionItems().get(0).getCode());
        }
    }

    @Test
    @Override
    public void testImportCategoriesTsv() throws Exception {
        String categorySchemeUrn = CATEGORY_SCHEME_2_V1;
        String fileName = "importation-category-01.tsv";
        File file = new File(this.getClass().getResource("/tsv/" + fileName).getFile());
        boolean updateAlreadyExisting = false;

        TaskImportationInfo taskImportTsvInfo = categoriesService.importCategoriesTsv(getServiceContextAdministrador(), categorySchemeUrn, file, fileName, updateAlreadyExisting, Boolean.TRUE);

        // Validate
        assertEquals(false, taskImportTsvInfo.getIsPlannedInBackground());
        assertNull(taskImportTsvInfo.getJobKey());
        assertEquals(0, taskImportTsvInfo.getInformationItems().size());

        // Validate item scheme
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertEqualsDate("2011-01-01 01:02:03", categorySchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
        assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        assertEquals(false, categorySchemeVersion.getItemScheme().getIsTaskInBackground());

        // Validate categories

        List<CategoryMetamacVisualisationResult> result = categoriesService.retrieveCategoriesByCategorySchemeUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_2_V1, "es", null);
        assertEquals(result.size(), 16);

        String categorySchemeUrnPart = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME02(01.000).";
        {
            String semanticIdentifier = "impCATEGORY01";
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), categorySchemeUrnPart + semanticIdentifier);
            assertEquals(semanticIdentifier, category.getNameableArtefact().getCode());
            assertEquals(category.getNameableArtefact().getUrn(), category.getNameableArtefact().getUrnProvider());
            assertEquals(null, category.getParent());
            assertEqualsInternationalString(category.getNameableArtefact().getName(), "es", "nombre nuevo 1", "en", "new name 1");
            assertEqualsInternationalString(category.getNameableArtefact().getDescription(), "en", "description new 1", null, null);
            assertEqualsInternationalString(category.getNameableArtefact().getComment(), "es", "Comentario de categoría 1", null, null);
            BaseAsserts.assertEqualsDay(new DateTime(), category.getLastUpdated());
            assertEquals(CATEGORY_ELEMENT_2_URN, category.getCategoryElement().getIdentifiableArtefact().getUrn());
        }
        {
            String semanticIdentifier = "impCATEGORY040102";
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), categorySchemeUrnPart + "impCATEGORY04.impCATEGORY0401." + semanticIdentifier);
            assertEquals(semanticIdentifier, category.getNameableArtefact().getCode());
            assertEquals(category.getNameableArtefact().getUrn(), category.getNameableArtefact().getUrnProvider());
            assertEquals("impCATEGORY0401", category.getParent().getNameableArtefact().getCode());
            assertEqualsInternationalString(category.getNameableArtefact().getName(), "es", "Nombre CATEGORY040102", "en", "name CATEGORY040102", "it", "Nombre it CATEGORY040102");
            assertEqualsInternationalString(category.getNameableArtefact().getDescription(), "es", "Descripción CATEGORY040102", null, null);
            BaseAsserts.assertEqualsDay(new DateTime(), category.getLastUpdated());
            assertEquals(CATEGORY_ELEMENT_1_URN, category.getCategoryElement().getIdentifiableArtefact().getUrn());
        }
        {
            String semanticIdentifier = "impCATEGORY0201";
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), categorySchemeUrnPart + "impCATEGORY01." + semanticIdentifier);
            assertEquals(semanticIdentifier, category.getNameableArtefact().getCode());
            assertEquals(category.getNameableArtefact().getUrn(), category.getNameableArtefact().getUrnProvider());
            assertEquals("impCATEGORY01", category.getParent().getNameableArtefact().getCode());
            assertEqualsInternationalString(category.getNameableArtefact().getName(), "es", "Nombre CATEGORY0201", "en", "name CATEGORY0201", "it", "Nombre it CATEGORY0201");
            assertEqualsInternationalString(category.getNameableArtefact().getDescription(), "es", "Descripción CATEGORY0201", null, null);
            BaseAsserts.assertEqualsDay(new DateTime(), category.getLastUpdated());
            assertEquals(null, category.getCategoryElement());
        }
    }

    @Test
    public void testImportCategoriesThreeLTsv() throws Exception {
        String categorySchemeUrn = CATEGORY_SCHEME_2_V1;
        String fileName = "importation-category-02-three-levels.tsv";
        File file = new File(this.getClass().getResource("/tsv/" + fileName).getFile());
        boolean updateAlreadyExisting = false;

        TaskImportationInfo taskImportTsvInfo = categoriesService.importCategoriesTsv(getServiceContextAdministrador(), categorySchemeUrn, file, fileName, updateAlreadyExisting, Boolean.TRUE);

        // Validate
        assertEquals(false, taskImportTsvInfo.getIsPlannedInBackground());
        assertNull(taskImportTsvInfo.getJobKey());
        assertEquals(0, taskImportTsvInfo.getInformationItems().size());

        // Validate item scheme
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertEqualsDate("2011-01-01 01:02:03", categorySchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
        assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        assertEquals(false, categorySchemeVersion.getItemScheme().getIsTaskInBackground());

        // Validate categories
        List<CategoryMetamacVisualisationResult> result = categoriesService.retrieveCategoriesByCategorySchemeUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_2_V1, "es", null);
        assertEquals(result.size(), 42);
        String categorySchemeUrnPart = "urn:sdmx:org.sdmx.infomodel.categoryscheme.Category=SDMX01:CATEGORYSCHEME02(01.000).";
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat2", null, "cat2", "Estadísticas económicas", "Economic statistics");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat2.cat5", "cat2", "cat5", "Finanzas públicas, fiscales y de estadísticas del sector público",
                "Government finance, fiscal and public sector statistics");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat2.cat4", "cat2", "cat4", "Estadísticas sectoriales", "Sectoral statistics");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat2.cat4.cat5", "cat2.cat4", "cat5", "Turismo", "Tourism");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat2.cat4.cat2", "cat2.cat4", "cat2", "Energía", "Energy");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat2.cat4.cat1", "cat2.cat4", "cat1", "Agricultura, silvicultura y pesca", "Agriculture, forestry, fisheries");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat2.cat4.cat6", "cat2.cat4", "cat6", "Banca, seguros y estadísticas financieras", "Banking, insurance, financial statistics");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat2.cat4.cat3", "cat2.cat4", "cat3", "Minería, manufactura y construcción", "Mining, manufacturing, construction");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat2.cat4.cat4", "cat2.cat4", "cat4", "Transporte", "Transport");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat2.cat7", "cat2", "cat7", "Precios", "Prices");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat2.cat8", "cat2", "cat8", "Coste laboral", "Labor cost");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat2.cat1", "cat2", "cat1", "Estadísticas macroeconómicas", "Macroeconomic statistics");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat2.cat3", "cat2", "cat3", "Estadísticas empresariales", "Business statistics");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat2.cat9", "cat2", "cat9", "Ciencia, tecnología e innovación", "Science, technology and innovation");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat2.cat2", "cat2", "cat2", "Cuentas económicas", "Economic accounts");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat2.cat6", "cat2", "cat6", "Comercio internacional y balanza de pagos", "International trade and balance of payments");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat1", null, "cat1", "Estadísticas demográficas y sociales", "Demographic and social statistics");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat1.cat3", "cat1", "cat3", "Educación", "Education");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat1.cat10", "cat1", "cat10", "Política y otras actividades de la comunidad", "Political and other community activities");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat1.cat4", "cat1", "cat4", "Salud", "Health");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat1.cat5", "cat1", "cat5", "Ingresos y consumo", "Income and consumption");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat1.cat9", "cat1", "cat9", "Cultura", "Culture");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat1.cat1", "cat1", "cat1", "Población y migración", "Population and migration");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat1.cat11", "cat1", "cat11", "Ocio", "Time use");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat1.cat6", "cat1", "cat6", "Protección social", "Social protection");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat1.cat7", "cat1", "cat7", "Asentamientos y vivienda", "Human settlements and housing");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat1.cat2", "cat1", "cat2", "Trabajo", "Labour");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat1.cat8", "cat1", "cat8", "Justicia y crímenes", "Justice and crime");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat3", null, "cat3", "Medio Ambiente y estadísticas multi-dominio", "Environment and multi-domain statistics");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat3.cat3", "cat3", "cat3", "Estadísticas e indicadores multi-dominio", "Multi-domain statistics and indicators");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat3.cat3.cat7", "cat3.cat3", "cat7", "Espíritu empresarial", "Entrepreneurship");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat3.cat3.cat6", "cat3.cat3", "cat6", "Desarrollo sostenible", "Sustainable development");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat3.cat3.cat2", "cat3.cat3", "cat2", "Género y grupos especiales de población", "Gender and special population groups");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat3.cat3.cat3", "cat3.cat3", "cat3", "Sociedad de la información", "Information society");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat3.cat3.cat4", "cat3.cat3", "cat4", "Globalización", "Globalisation");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat3.cat3.cat5", "cat3.cat3", "cat5", "Indicadores relacionados con los Objetivos de Desarrollo del Milenio",
                "Indicators related to the Millennium Development Goals");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat3.cat3.cat1", "cat3.cat3", "cat1", "Condiciones de vida, pobreza y cuestiones sociales transversales",
                "Living conditions, poverty, and cross-cutting social issues");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat3.cat1", "cat3", "cat1", "Medio Ambiente", "Environment");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat3.cat2", "cat3", "cat2", "Estadísticas regionales y en áreas pequeñas", "Regional and small area statistics");
        assertEqualsImportedCategory(categorySchemeUrnPart + "cat3.cat4", "cat3", "cat4", "Anuarios y recopilaciones similares", "Yearbooks and similar compendia");

    }

    private void assertEqualsImportedCategory(String urn, String expectedCategoryParentCodeFull, String expectedCategoryCode, String nameEs, String nameEn) throws MetamacException {
        String codeFull = StringUtils.isNotBlank(expectedCategoryParentCodeFull) ? expectedCategoryParentCodeFull + "." + expectedCategoryCode : expectedCategoryCode;
        CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), urn);
        assertEquals(expectedCategoryCode, category.getNameableArtefact().getCode());
        assertEquals(codeFull, category.getNameableArtefact().getCodeFull());
        assertEquals(category.getNameableArtefact().getUrn(), category.getNameableArtefact().getUrnProvider());
        assertEquals(expectedCategoryParentCodeFull, category.getParent() != null ? category.getParent().getNameableArtefact().getCodeFull() : null);
        assertEqualsInternationalString(category.getNameableArtefact().getName(), "es", nameEs, "en", nameEn);
        BaseAsserts.assertEqualsDay(new DateTime(), category.getLastUpdated());
    }

    @Test
    @Override
    public void testExportCategoriesTsv() throws Exception {
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        String fileName = categoriesService.exportCategoriesTsv(getServiceContextAdministrador(), categorySchemeUrn);
        assertNotNull(fileName);

        // Validate
        File file = new File(tempDirPath() + File.separatorChar + fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        assertEquals(
                "code\tparent\tcategory_element\teni_scheme\tname#es\tname#pt\tname#en\tname#ca\tdescription#es\tdescription#pt\tdescription#en\tdescription#ca\tcomment#es\tcomment#pt\tcomment#en\tcomment#ca",
                bufferedReader.readLine());
        Set<String> lines = new HashSet<String>();
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line.replaceAll("\t", "\\\\t"));
            lines.add(line);
        }
        assertEquals(8, lines.size());

        assertTrue(lines.contains(
                "CATEGORY01\t\tCATEGORY_ELEMENT_02\tSRM-ID2-ENI-SCHEME\tNombre categoryScheme-1-v2-category-1\t\tName categoryScheme-1-v2-category-1\t\tDescripción categoryScheme-1-v2-category-1\t\t\t\t\t\t\t"));
        assertTrue(lines.contains("CATEGORY02\t\tCATEGORY_ELEMENT_03\tSRM-ID3-ENI-SCHEME\tNombre categoryScheme-1-v2-category-2\t\t\t\t\t\t\t\t\t\t\t"));
        assertTrue(lines.contains("CATEGORY03\t\tCATEGORY_ELEMENT_02\tSRM-ID2-ENI-SCHEME\tnombre category-3\t\tname category-3\t\t\t\t\t\t\t\t\t"));
        assertTrue(lines.contains("CATEGORY04\t\tCATEGORY_ELEMENT_01\tSRM-ID1-ENI-SCHEME\tLanzarote\t\t\t\t\t\t\t\t\t\t\t"));
        assertTrue(lines.contains(
                "CATEGORY0201\tCATEGORY02\tCATEGORY_ELEMENT_01\tSRM-ID1-ENI-SCHEME\tNombre categoryScheme-1-v2-category-2-1\t\tName categoryScheme-1-v2-category-2-1\t\tDescripción cat2-1\t\tDescription cat2-1\t\t\t\t\t"));
        assertTrue(lines.contains("CATEGORY0401\tCATEGORY04\tCATEGORY_ELEMENT_02\tSRM-ID2-ENI-SCHEME\tLa Gomera\t\t\t\t\t\t\t\t\t\t\t"));
        assertTrue(lines.contains(
                "CATEGORY020101\tCATEGORY02.CATEGORY0201\tCATEGORY_ELEMENT_01\tSRM-ID1-ENI-SCHEME\tNombre categoryScheme-1-v2-category-2-1-1\t\t\t\tDescripción cat2-1-1\t\t\t\tComment categoryScheme-1-v2-category-2-1-1\t\t\t"));
        assertTrue(lines.contains(
                "CATEGORY040101\tCATEGORY04.CATEGORY0401\t\tSRM-ID3-ENI-SCHEME\tNombre categoryScheme-1-v2-category-4-1-1 Santa Cruz de Tenerife\t\tName categoryScheme-1-v2-category-4-1-1\t\t\t\t\t\t\t\t\t"));
        bufferedReader.close();
    }

    @Test
    public void testRetrieveCategoriesOrderedInDepthByCategorySchemeUrn() throws Exception {

        List<ItemResult> categories = categoryMetamacRepository.findCategoriesByCategorySchemeOrderedInDepth(Long.valueOf(12), null,
                new CategoryMetamacResultSelection(true, true, true, true, false, true, false, true));

        assertEquals(8, categories.size());

        Set<String> readCategoriesCodes = new HashSet<String>();

        for (ItemResult category : categories) {
            if (category.getParent() != null) {
                assertTrue(readCategoriesCodes.contains(category.getParent().getCode()));
            }
            readCategoriesCodes.add(category.getCode());
        }

        assertTrue(readCategoriesCodes.contains("CATEGORY01"));
        assertTrue(readCategoriesCodes.contains("CATEGORY02"));
        assertTrue(readCategoriesCodes.contains("CATEGORY0201"));
        assertTrue(readCategoriesCodes.contains("CATEGORY020101"));
        assertTrue(readCategoriesCodes.contains("CATEGORY03"));
        assertTrue(readCategoriesCodes.contains("CATEGORY04"));
        assertTrue(readCategoriesCodes.contains("CATEGORY0401"));
        assertTrue(readCategoriesCodes.contains("CATEGORY040101"));
    }

    @Override
    @Test
    public void testRetrieveCategoriesByCategorySchemeUrn() throws Exception {

        // Retrieve
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        // LOCALE = 'es'
        {
            String locale = "es";
            List<CategoryMetamacVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(getServiceContextAdministrador(), categorySchemeUrn, locale, null);

            // Validate
            assertEquals(8, categories.size());
            {
                // Category 01 (validate all metadata)
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_1);
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, category.getUrn());
                assertEquals("CATEGORY01", category.getCode());
                assertEquals("Nombre categoryScheme-1-v2-category-1", category.getName());
                assertEquals("Descripción categoryScheme-1-v2-category-1", category.getDescription());
                assertEquals(Long.valueOf(121), category.getItemIdDatabase());
                assertEquals(null, category.getParent());
                assertEquals(null, category.getParentIdDatabase());
                assertEquals(CATEGORY_ELEMENT_02, category.getCategoryElement().getCode());
                MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", category.getCreatedDate());
            }
            {
                // Category 02
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2);
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, category.getUrn());
                assertEquals("CATEGORY02", category.getCode());
                assertEquals("Nombre categoryScheme-1-v2-category-2", category.getName());
                assertEquals(null, category.getDescription());
                assertEquals(CATEGORY_ELEMENT_03, category.getCategoryElement().getCode());
                MetamacAsserts.assertEqualsDate("2011-03-02 04:05:06", category.getCreatedDate());
            }
            {
                // Category 02 01 (validate parent)
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1);
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1, category.getUrn());
                assertEquals("CATEGORY02", category.getParent().getCode());
                assertEquals("Nombre categoryScheme-1-v2-category-2-1", category.getName());
                assertEquals("Descripción cat2-1", category.getDescription());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, category.getParent().getUrn());
                assertEquals(Long.valueOf("122"), category.getParentIdDatabase());
                assertEquals(CATEGORY_ELEMENT_01, category.getCategoryElement().getCode());
            }
            {
                // Category 02 01 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1);
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, category.getUrn());
                assertEquals("CATEGORY0201", category.getParent().getCode());
                assertEquals("Nombre categoryScheme-1-v2-category-2-1-1", category.getName());
                assertEquals("Descripción cat2-1-1", category.getDescription());
                assertEquals(Long.valueOf("1221"), category.getParentIdDatabase());
                assertEquals(CATEGORY_ELEMENT_01, category.getCategoryElement().getCode());
            }
            {
                // Category 03
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_3);
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_3, category.getUrn());
                assertEquals("nombre category-3", category.getName());
                assertEquals(CATEGORY_ELEMENT_02, category.getCategoryElement().getCode());
            }
            {
                // Category 04
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4);
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4, category.getUrn());
                assertEquals("Lanzarote", category.getName());
                assertEquals(CATEGORY_ELEMENT_01, category.getCategoryElement().getCode());
            }
            {
                // Category 04 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1);
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1, category.getUrn());
                assertEquals("La Gomera", category.getName());
                assertEquals(CATEGORY_ELEMENT_02, category.getCategoryElement().getCode());
            }
            {
                // Category 04 01 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1);
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1, category.getUrn());
                assertEquals("CATEGORY0401", category.getParent().getCode());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1, category.getParent().getUrn());
                assertEquals("Nombre categoryScheme-1-v2-category-4-1-1 Santa Cruz de Tenerife", category.getName());
                assertEquals(null, category.getCategoryElement());
            }
        }

        // LOCALE = 'en'
        {
            String locale = "en";
            List<CategoryMetamacVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(getServiceContextAdministrador(), categorySchemeUrn, locale, null);

            // Validate
            assertEquals(8, categories.size());
            {
                // Category 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_1);
                assertEquals("Name categoryScheme-1-v2-category-1", category.getName());
                assertEquals(null, category.getDescription());
            }
            {
                // Category 02
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2);
                assertEquals(null, category.getName());
                assertEquals(null, category.getDescription());
            }
            {
                // Category 02 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1);
                assertEquals("Name categoryScheme-1-v2-category-2-1", category.getName());
                assertEquals("Description cat2-1", category.getDescription());
            }
            {
                // Category 02 01 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1);
                assertEquals(null, category.getName());
                assertEquals(null, category.getDescription());
            }
            {
                // Category 03
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_3);
                assertEquals("name category-3", category.getName());
            }
            {
                // Category 04
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4);
                assertEquals(null, category.getName());
            }
            {
                // Category 04 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1);
                assertEquals(null, category.getName());
            }
            {
                // Category 04 01 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1);
                assertEquals("Name categoryScheme-1-v2-category-4-1-1", category.getName());
            }
        }
    }

    @Override
    @Test
    public void testRetrieveCategoriesByCategorySchemeUrnUnordered() throws Exception {

        // Retrieve
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        CategoryMetamacResultSelection categoryMetamacResultSelection = new CategoryMetamacResultSelection(true, true, true, true, false, true, false, true);
        List<ItemResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrnUnordered(getServiceContextAdministrador(), categorySchemeUrn, categoryMetamacResultSelection);

        // Validate
        assertEquals(8, categories.size());
        {
            // Category 01 (validate all metadata)
            ItemResult category = getItemResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_1);
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, category.getUrn());
            assertEquals("CATEGORY01", category.getCode());
            assertEquals("CATEGORY01", category.getCodeFull());
            assertEquals("Nombre categoryScheme-1-v2-category-1", category.getName().get("es"));
            assertEquals("Name categoryScheme-1-v2-category-1", category.getName().get("en"));
            assertEquals("Descripción categoryScheme-1-v2-category-1", category.getDescription().get("es"));
            assertEquals(null, category.getDescription().get("en"));
            assertEquals(Long.valueOf(121), category.getItemIdDatabase());
            assertEquals(null, category.getParent());
            assertEquals(null, category.getParentIdDatabase());
            assertEquals("SRM-ID2-ENI-SCHEME", ((CategoryMetamacResultExtensionPoint) category.getExtensionPoint()).getEniSchemas().get(0).getIdentifier());
        }
        {
            // Category 02
            ItemResult category = getItemResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2);
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, category.getUrn());
            assertEquals("CATEGORY02", category.getCode());
            assertEquals("Nombre categoryScheme-1-v2-category-2", category.getName().get("es"));
            assertEquals(null, category.getDescription().get("es"));
            assertEquals("SRM-ID3-ENI-SCHEME", ((CategoryMetamacResultExtensionPoint) category.getExtensionPoint()).getEniSchemas().get(0).getIdentifier());
        }
        {
            // Category 02 01 (validate parent)
            ItemResult category = getItemResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1);
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1, category.getUrn());
            assertEquals("CATEGORY0201", category.getCode());
            assertEquals("CATEGORY02.CATEGORY0201", category.getCodeFull());
            assertEquals("CATEGORY02", category.getParent().getCode());
            assertEquals("Nombre categoryScheme-1-v2-category-2-1", category.getName().get("es"));
            assertEquals("Descripción cat2-1", category.getDescription().get("es"));
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, category.getParent().getUrn());
            assertEquals(Long.valueOf("122"), category.getParentIdDatabase());
            assertEquals("SRM-ID1-ENI-SCHEME", ((CategoryMetamacResultExtensionPoint) category.getExtensionPoint()).getEniSchemas().get(0).getIdentifier());
        }
        {
            // Category 02 01 01
            ItemResult category = getItemResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1);
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, category.getUrn());
            assertEquals("CATEGORY0201", category.getParent().getCode());
            assertEquals("Nombre categoryScheme-1-v2-category-2-1-1", category.getName().get("es"));
            assertEquals("Descripción cat2-1-1", category.getDescription().get("es"));
            assertEquals(Long.valueOf("1221"), category.getParentIdDatabase());
            assertEquals("SRM-ID1-ENI-SCHEME", ((CategoryMetamacResultExtensionPoint) category.getExtensionPoint()).getEniSchemas().get(0).getIdentifier());
        }
        {
            // Category 03
            ItemResult category = getItemResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_3);
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_3, category.getUrn());
            assertEquals("nombre category-3", category.getName().get("es"));
            assertEquals("SRM-ID2-ENI-SCHEME", ((CategoryMetamacResultExtensionPoint) category.getExtensionPoint()).getEniSchemas().get(0).getIdentifier());
        }
        {
            // Category 04
            ItemResult category = getItemResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4);
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4, category.getUrn());
            assertEquals("Lanzarote", category.getName().get("es"));
            assertEquals("SRM-ID1-ENI-SCHEME", ((CategoryMetamacResultExtensionPoint) category.getExtensionPoint()).getEniSchemas().get(0).getIdentifier());
        }
        {
            // Category 04 01
            ItemResult category = getItemResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1);
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1, category.getUrn());
            assertEquals("La Gomera", category.getName().get("es"));
            assertEquals("SRM-ID2-ENI-SCHEME", ((CategoryMetamacResultExtensionPoint) category.getExtensionPoint()).getEniSchemas().get(0).getIdentifier());
        }
        {
            // Category 04 01 01
            ItemResult category = getItemResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1);
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1, category.getUrn());
            assertEquals("CATEGORY0401", category.getParent().getCode());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1, category.getParent().getUrn());
            assertEquals("Nombre categoryScheme-1-v2-category-4-1-1 Santa Cruz de Tenerife", category.getName().get("es"));
            assertEquals("SRM-ID3-ENI-SCHEME", ((CategoryMetamacResultExtensionPoint) category.getExtensionPoint()).getEniSchemas().get(0).getIdentifier());
        }
    }

    @Override
    @Test
    public void testFindCategoriesByCondition() throws Exception {

        // Find all
        {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Category.class).orderBy(CategoryProperties.itemSchemeVersion().maintainableArtefact().code())
                    .orderBy(CategoryProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending().orderBy(CategoryProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CategoryMetamac> categoriesPagedResult = categoriesService.findCategoriesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(25, categoriesPagedResult.getTotalRows());
            assertEquals(25, categoriesPagedResult.getValues().size());
            assertTrue(categoriesPagedResult.getValues().get(0) instanceof CategoryMetamac);

            int i = 0;
            assertEquals(CATEGORY_SCHEME_1_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V1_CATEGORY_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_3, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_2_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_2_V1_CATEGORY_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_4_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_5_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_6_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_7_V2_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_8_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_8_V1_CATEGORY_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_8_V1_CATEGORY_3, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_8_V1_CATEGORY_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());

            assertEquals(categoriesPagedResult.getValues().size(), i);
        }

        // Find by name (like), code (like) and category scheme urn
        {
            String name = "Nombre categoryScheme-1-v2-category-2-";
            String code = "CATEGORY02";
            String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Category.class).withProperty(CategoryProperties.itemSchemeVersion().maintainableArtefact().urn())
                    .eq(categorySchemeUrn).withProperty(CategoryProperties.nameableArtefact().code()).like(code + "%").withProperty(CategoryProperties.nameableArtefact().name().texts().label())
                    .like(name + "%").orderBy(CategoryProperties.id()).ascending().distinctRoot().build();
            PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
            PagedResult<CategoryMetamac> categoriesPagedResult = categoriesService.findCategoriesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

            // Validate
            assertEquals(2, categoriesPagedResult.getTotalRows());
            assertEquals(2, categoriesPagedResult.getValues().size());
            assertTrue(categoriesPagedResult.getValues().get(0) instanceof CategoryMetamac);

            int i = 0;
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
            assertEquals(categoriesPagedResult.getValues().size(), i);
        }

        // Find by category scheme urn paginated
        {
            String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Category.class).withProperty(CategoryProperties.itemSchemeVersion().maintainableArtefact().urn())
                    .eq(categorySchemeUrn).orderBy(CategoryProperties.id()).ascending().distinctRoot().build();

            // First page
            {
                PagingParameter pagingParameter = PagingParameter.rowAccess(0, 3, true);
                PagedResult<CategoryMetamac> categoriesPagedResult = categoriesService.findCategoriesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(8, categoriesPagedResult.getTotalRows());
                assertEquals(3, categoriesPagedResult.getValues().size());
                assertTrue(categoriesPagedResult.getValues().get(0) instanceof CategoryMetamac);

                int i = 0;
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_3, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(categoriesPagedResult.getValues().size(), i);
            }
            // Second page
            {
                PagingParameter pagingParameter = PagingParameter.rowAccess(3, 6, true);
                PagedResult<CategoryMetamac> categoriesPagedResult = categoriesService.findCategoriesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(8, categoriesPagedResult.getTotalRows());
                assertEquals(3, categoriesPagedResult.getValues().size());
                assertTrue(categoriesPagedResult.getValues().get(0) instanceof CategoryMetamac);

                int i = 0;
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(categoriesPagedResult.getValues().size(), i);
            }
            // Third page
            {
                PagingParameter pagingParameter = PagingParameter.rowAccess(6, 9, true);
                PagedResult<CategoryMetamac> categoriesPagedResult = categoriesService.findCategoriesByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

                // Validate
                assertEquals(8, categoriesPagedResult.getTotalRows());
                assertEquals(2, categoriesPagedResult.getValues().size());
                assertTrue(categoriesPagedResult.getValues().get(0) instanceof CategoryMetamac);

                int i = 0;
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
                assertEquals(categoriesPagedResult.getValues().size(), i);
            }
        }
    }

    @Override
    public void testFindCategoriesCanBeCategorisationCategoryByCondition() throws Exception {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(Category.class).orderBy(CategoryProperties.itemSchemeVersion().maintainableArtefact().code())
                .orderBy(CategoryProperties.itemSchemeVersion().maintainableArtefact().urn()).ascending().orderBy(CategoryProperties.id()).ascending().distinctRoot().build();
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        PagedResult<CategoryMetamac> categoriesPagedResult = categoriesService.findCategoriesCanBeCategorisationCategoryByCondition(getServiceContextAdministrador(), conditions, pagingParameter);

        // Validate
        assertEquals(7, categoriesPagedResult.getTotalRows());
        assertEquals(7, categoriesPagedResult.getValues().size());
        assertTrue(categoriesPagedResult.getValues().get(0) instanceof CategoryMetamac);

        int i = 0;
        assertEquals(CATEGORY_SCHEME_1_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2_2, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CATEGORY_SCHEME_3_V1_CATEGORY_2_1_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());
        assertEquals(CATEGORY_SCHEME_7_V2_CATEGORY_1, categoriesPagedResult.getValues().get(i++).getNameableArtefact().getUrn());

        assertEquals(categoriesPagedResult.getValues().size(), i);
    }

    @Override
    @Test
    public void testRetrieveCategorySchemeByCategoryUrn() throws Exception {
        // Retrieve
        String urn = CATEGORY_SCHEME_1_V2_CATEGORY_1;
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByCategoryUrn(getServiceContextAdministrador(), urn);

        // Validate
        assertEquals(CATEGORY_SCHEME_1_V2, categorySchemeVersion.getMaintainableArtefact().getUrn());
    }

    @Test
    public void testRetrieveCategorySchemeByCategoryUrnErrorNotExists() throws Exception {

        String urn = NOT_EXISTS;

        try {
            categoriesService.retrieveCategorySchemeByCategoryUrn(getServiceContextAdministrador(), urn);
            fail("not exists");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND.getCode(), e.getExceptionItems().get(0).getCode());
            assertEquals(1, e.getExceptionItems().get(0).getMessageParameters().length);
            assertEquals(urn, e.getExceptionItems().get(0).getMessageParameters()[0]);
        }
    }

    @Test
    @Override
    public void testPreCreateCategoryScheme() throws Exception {
        // tested in createCategory
    }

    @Test
    @Override
    public void testPreCreateCategory() throws Exception {
        // tested in createCategory
    }

    @Override
    public void testPreCreateCategorisation() throws Exception {
        // tested in createCategorisation
    }

    @Override
    public void testPostCreateCategorisation() throws Exception {
        // tested in createCategorisation
    }

    // In CategoriesMetamacCategorisationServiceTest.java
    @Override
    public void testRetrieveCategorisationByUrn() throws Exception {
    }
    @Override
    public void testDeleteCategorisation() throws Exception {
    }
    @Override
    public void testRetrieveCategorisationsByArtefact() throws Exception {
    }
    @Override
    public void testCreateCategorisation() throws Exception {
    }
    @Override
    public void testFindCategorisationsByCondition() throws Exception {
    }
    @Override
    public void testMarkCategorisationAsFinal() throws Exception {
    }
    @Override
    public void testMarkCategorisationAsPublic() throws Exception {
    }
    @Override
    public void testStartCategorisationValidity() throws Exception {
    }
    @Override
    public void testEndCategorisationValidity() throws Exception {
    }
    @Override
    public void testCreateVersionFromTemporalCategorisations() throws Exception {
        // in services of categorised artefacts
    }

    @Test
    @Override
    public void testResendKafkaMessageExternallyCategorySchemes() {
        // No test. cron job process
    }

    @Override
    public void testRetrieveCategoryElementLogo() throws Exception {
        // check in testImportCategoryElementLogo test.

    }

    @Test
    @Override
    public void testDeleteCategoryElementLogo() throws Exception {
        String fileName = "02_AdministracionPublica.svg";
        File file = new File(this.getClass().getResource("/images/" + fileName).getFile());
        CategoryElement categoryElement = null;
        try (InputStream imageStream = new FileInputStream(file)) {

            // save a logo.
            categoriesService.importCategoryElementLogo(getServiceContextAdministrador(), CATEGORY_ELEMENT_1_URN, fileName, imageStream, false);

            // check logo exists
            categoryElement = categoriesService.retrieveCategoryElementByUrn(getServiceContextAdministrador(), CATEGORY_ELEMENT_1_URN);
            assertEquals("02_AdministracionPublica", categoryElement.getLogo().getFileName());
            assertEquals("svg", categoryElement.getLogo().getExtension());
            assertNotNull(categoryElement.getLogo().getFile().getId());

            // delete logo
            categoriesService.deleteCategoryElementLogo(getServiceContextAdministrador(), CATEGORY_ELEMENT_1_URN);

            // check logo not exists
            categoryElement = categoriesService.retrieveCategoryElementByUrn(getServiceContextAdministrador(), CATEGORY_ELEMENT_1_URN);
            assertNull(categoryElement.getLogo());
        }
    }

    @Test
    @Override
    public void testImportCategoryElementLogo() throws Exception {
        String fileName = "02_AdministracionPublica.svg";
        File file = new File(this.getClass().getResource("/images/" + fileName).getFile());
        CategoryElement categoryElement = null;
        try (InputStream imageStream = new FileInputStream(file)) {

            TaskImportationInfo taskResult = categoriesService.importCategoryElementLogo(getServiceContextAdministrador(), CATEGORY_ELEMENT_1_URN, fileName, imageStream, false);
            assertEquals(true, taskResult.getInformationItems().isEmpty());

            categoryElement = categoriesService.retrieveCategoryElementByUrn(getServiceContextAdministrador(), CATEGORY_ELEMENT_1_URN);

            assertEquals("02_AdministracionPublica", categoryElement.getLogo().getFileName());
            assertEquals("svg", categoryElement.getLogo().getExtension());

        }
        try (InputStream imageStream = new FileInputStream(file)) {
            byte[] sourceLogoData = IOUtils.toByteArray(imageStream);
            byte[] savedLogoData = categoryElement.getLogo().getFile().getFileData();
            assertTrue(Arrays.equals(sourceLogoData, savedLogoData));
        }

    }

    @Test
    public void testImportCategoryElementLogoNotAllowed() throws Exception {
        String fileName = "02_AdministracionPublica.err";
        File file = new File(this.getClass().getResource("/images/" + fileName).getFile());

        try (InputStream imageStream = new FileInputStream(file)) {

            categoriesService.importCategoryElementLogo(getServiceContextAdministrador(), CATEGORY_ELEMENT_1_URN, fileName, imageStream, false);
            fail("logo is not allowed");

        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IMPORTATION_LOGO_CATEGORY_ELEMENT_EXTENSION_ERROR.getCode(), e.getExceptionItems().get(0).getCode());
        }

    }

    @Test
    public void testImportCategoryElementLogoMaxSizeError() throws Exception {
        String fileName = "poblacion.svg";
        File file = new File(this.getClass().getResource("/images/" + fileName).getFile());

        try (InputStream imageStream = new FileInputStream(file)) {

            categoriesService.importCategoryElementLogo(getServiceContextAdministrador(), CATEGORY_ELEMENT_1_URN, fileName, imageStream, false);
            fail("logo exceeds max size");

        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEquals(ServiceExceptionType.IMPORTATION_LOGO_CATEGORY_ELEMENT_MAX_SIZE_ERROR.getCode(), e.getExceptionItems().get(0).getCode());
        }

    }

    // ------------------------------------------------------------------------------------
    // ORDER VISUALISATIONS
    // ------------------------------------------------------------------------------------

    @Override
    @Test
    @DirtyDatabase
    public void testCreateCategorySchemeOrderVisualisation() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = CategoriesMetamacDoMocks.mockCategorySchemeOrderVisualisation(retrieveAnnotationType_1());
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        // Create
        CategorySchemeOrderVisualisation categorySchemeOrderVisualisationCreated = categoriesService.createCategorySchemeOrderVisualisation(ctx, categorySchemeUrn,
                categorySchemeOrderVisualisation);
        assertNotNull(categorySchemeOrderVisualisationCreated.getId());
        assertNotNull(categorySchemeOrderVisualisationCreated.getNameableArtefact().getUrn());
        assertEqualsCategorySchemeOrderVisualisation(categorySchemeOrderVisualisation, categorySchemeOrderVisualisationCreated);
        assertEqualsDate("2011-01-01 01:02:03", categorySchemeOrderVisualisationCreated.getCategorySchemeVersion().getItemScheme().getResourceCreatedDate().toDate());
        assertTrue(DateUtils.isSameDay(new Date(), categorySchemeOrderVisualisationCreated.getCategorySchemeVersion().getItemScheme().getResourceLastUpdated().toDate()));

        // Validate categories order (new visualisation uses alphabetical order)
        List<CategoryMetamacVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(getServiceContextAdministrador(), categorySchemeUrn, "es",
                categorySchemeOrderVisualisationCreated.getNameableArtefact().getUrn());
        assertEquals(8, categories.size());
        assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_1).getOrder());
        assertEquals(Integer.valueOf(1), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2).getOrder());
        assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1).getOrder());
        assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1).getOrder());
        assertEquals(Integer.valueOf(2), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_3).getOrder());
        assertEquals(Integer.valueOf(3), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4).getOrder());
        assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1).getOrder());
        assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1).getOrder());
    }

    @Override
    @Test
    @DirtyDatabase
    public void testCreateCategorySchemeOrderVisualisationAlphabetical() throws Exception {
        // Already tested

    }

    @Override
    @Test
    @DirtyDatabase
    public void testUpdateCategorySchemeOrderVisualisation() throws Exception {
        CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = categoriesService.retrieveCategorySchemeOrderVisualisationByUrn(getServiceContextAdministrador(),
                CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_02);
        categorySchemeOrderVisualisation.getNameableArtefact().setName(BaseDoMocks.mockInternationalString());
        categorySchemeOrderVisualisation.getNameableArtefact().setIsCodeUpdated(Boolean.FALSE);

        // Update
        CategorySchemeOrderVisualisation categorySchemeOrderVisualisationUpdated = categoriesService.updateCategorySchemeOrderVisualisation(getServiceContextAdministrador(),
                categorySchemeOrderVisualisation);

        // Validate
        assertEqualsCategorySchemeOrderVisualisation(categorySchemeOrderVisualisation, categorySchemeOrderVisualisationUpdated);
        assertEqualsDate("2011-01-01 01:02:03", categorySchemeOrderVisualisationUpdated.getCategorySchemeVersion().getItemScheme().getResourceCreatedDate().toDate());
        assertTrue(DateUtils.isSameDay(new Date(), categorySchemeOrderVisualisationUpdated.getCategorySchemeVersion().getItemScheme().getResourceLastUpdated().toDate()));
    }

    @Test
    public void testUpdateCategorySchemeOrderVisualisationErrorDuplicatedCategory() throws Exception {
        CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = categoriesService.retrieveCategorySchemeOrderVisualisationByUrn(getServiceContextAdministrador(),
                CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_03);
        categorySchemeOrderVisualisation.getNameableArtefact().setCode("VISUALISATION02");
        categorySchemeOrderVisualisation.getNameableArtefact().setIsCodeUpdated(Boolean.TRUE);
        try {
            categoriesService.updateCategorySchemeOrderVisualisation(getServiceContextAdministrador(), categorySchemeOrderVisualisation);
            fail("duplicated category");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_URN_DUPLICATED, 1, new String[]{CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_02},
                    e.getExceptionItems().get(0));
        }
    }

    @Test
    public void testCreateCategoryTestingOrderVisualisation() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CategoryMetamac category = CategoriesMetamacDoMocks.mockCategory(retrieveAnnotationType_1());
        category.getNameableArtefact().setCode("category029");
        category.setParent(null);

        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        // Create
        CategoryMetamac categoryCreated = categoriesService.createCategory(ctx, categorySchemeUrn, category);

        // Validate visualisations
        // Visualisation 01, alphabetic
        {
            CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = categoriesService.retrieveCategorySchemeOrderVisualisationByUrn(getServiceContextAdministrador(),
                    CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL);

            List<CategoryMetamacVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(ctx, categorySchemeUrn, "es",
                    categorySchemeOrderVisualisation.getNameableArtefact().getUrn());
            assertEquals(9, categories.size());
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_1).getOrder());
            assertEquals(Integer.valueOf(1), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2).getOrder());
            assertEquals(Integer.valueOf(2), getCategoryMetamacVisualisationResult(categories, categoryCreated.getNameableArtefact().getUrn()).getOrder()); // after CATEGORY02
            assertEquals(Integer.valueOf(3), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_3).getOrder());
            assertEquals(Integer.valueOf(4), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4).getOrder());
        }
        // Visualisation 02
        {
            CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = categoriesService.retrieveCategorySchemeOrderVisualisationByUrn(getServiceContextAdministrador(),
                    CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_02);

            List<CategoryMetamacVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(ctx, categorySchemeUrn, "es",
                    categorySchemeOrderVisualisation.getNameableArtefact().getUrn());
            assertEquals(9, categories.size());
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_1).getOrder());
            assertEquals(Integer.valueOf(1), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2).getOrder());
            assertEquals(Integer.valueOf(2), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_3).getOrder());
            assertEquals(Integer.valueOf(3), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4).getOrder());
            assertEquals(Integer.valueOf(4), getCategoryMetamacVisualisationResult(categories, categoryCreated.getNameableArtefact().getUrn()).getOrder());
        }
        // Visualisation 03
        {
            CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = categoriesService.retrieveCategorySchemeOrderVisualisationByUrn(getServiceContextAdministrador(),
                    CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_03);

            List<CategoryMetamacVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(ctx, categorySchemeUrn, "es",
                    categorySchemeOrderVisualisation.getNameableArtefact().getUrn());
            assertEquals(9, categories.size());
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_3).getOrder());
            assertEquals(Integer.valueOf(1), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_1).getOrder());
            assertEquals(Integer.valueOf(2), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2).getOrder());
            assertEquals(Integer.valueOf(3), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4).getOrder());
            assertEquals(Integer.valueOf(4), getCategoryMetamacVisualisationResult(categories, categoryCreated.getNameableArtefact().getUrn()).getOrder());
        }
    }

    @Test
    public void testCreateCategorySubCategoryTestingOrderVisualisation() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CategoryMetamac category = CategoriesMetamacDoMocks.mockCategory(retrieveAnnotationType_1());
        category.getNameableArtefact().setCode("category0200");
        CategoryMetamac categoryParent = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
        category.setParent(categoryParent);

        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        // Create
        CategoryMetamac categoryCreated = categoriesService.createCategory(ctx, categorySchemeUrn, category);

        // Validate visualisations
        // Visualisation 01, alphabetic
        {
            CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = categoriesService.retrieveCategorySchemeOrderVisualisationByUrn(getServiceContextAdministrador(),
                    CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL);

            List<CategoryMetamacVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(ctx, categorySchemeUrn, "es",
                    categorySchemeOrderVisualisation.getNameableArtefact().getUrn());
            assertEquals(9, categories.size());
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, categoryCreated.getNameableArtefact().getUrn()).getOrder());
            assertEquals(Integer.valueOf(1), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1).getOrder());
        }
        // Visualisation 02
        {
            CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = categoriesService.retrieveCategorySchemeOrderVisualisationByUrn(getServiceContextAdministrador(),
                    CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_02);
            List<CategoryMetamacVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(ctx, categorySchemeUrn, "es",
                    categorySchemeOrderVisualisation.getNameableArtefact().getUrn());
            assertEquals(9, categories.size());
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1).getOrder());
            assertEquals(Integer.valueOf(1), getCategoryMetamacVisualisationResult(categories, categoryCreated.getNameableArtefact().getUrn()).getOrder());
        }
        // Visualisation 03
        {
            CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = categoriesService.retrieveCategorySchemeOrderVisualisationByUrn(getServiceContextAdministrador(),
                    CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_03);
            List<CategoryMetamacVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(ctx, categorySchemeUrn, "es",
                    categorySchemeOrderVisualisation.getNameableArtefact().getUrn());
            assertEquals(9, categories.size());
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1).getOrder());
            assertEquals(Integer.valueOf(1), getCategoryMetamacVisualisationResult(categories, categoryCreated.getNameableArtefact().getUrn()).getOrder());
        }
    }

    @Test
    public void testCreateCategorySubCategoryInLevelWithoutOtherCategoriesTestingOrderVisualisation() throws Exception {
        ServiceContext ctx = getServiceContextAdministrador();

        CategoryMetamac category = CategoriesMetamacDoMocks.mockCategory(retrieveAnnotationType_1());
        CategoryMetamac categoryParent = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1);
        category.setParent(categoryParent);

        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        // Create
        CategoryMetamac categoryCreated = categoriesService.createCategory(ctx, categorySchemeUrn, category);

        // Validate visualisations
        // Visualisation 01, alphabetic
        {
            CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = categoriesService.retrieveCategorySchemeOrderVisualisationByUrn(getServiceContextAdministrador(),
                    CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL);

            List<CategoryMetamacVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(ctx, categorySchemeUrn, "es",
                    categorySchemeOrderVisualisation.getNameableArtefact().getUrn());
            assertEquals(9, categories.size());
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, categoryCreated.getNameableArtefact().getUrn()).getOrder());
        }
        // Visualisation 02
        {
            CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = categoriesService.retrieveCategorySchemeOrderVisualisationByUrn(getServiceContextAdministrador(),
                    CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_02);
            List<CategoryMetamacVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(ctx, categorySchemeUrn, "es",
                    categorySchemeOrderVisualisation.getNameableArtefact().getUrn());
            assertEquals(9, categories.size());
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, categoryCreated.getNameableArtefact().getUrn()).getOrder());
        }
        // Visualisation 03
        {
            CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = categoriesService.retrieveCategorySchemeOrderVisualisationByUrn(getServiceContextAdministrador(),
                    CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_03);
            List<CategoryMetamacVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(ctx, categorySchemeUrn, "es",
                    categorySchemeOrderVisualisation.getNameableArtefact().getUrn());
            assertEquals(9, categories.size());
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, categoryCreated.getNameableArtefact().getUrn()).getOrder());
        }
    }

    @Override
    @Test
    @DirtyDatabase
    public void testRetrieveCategorySchemeOrderVisualisationByUrn() throws Exception {
        // Retrieve
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        String orderVisualisationUrn = CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL;
        CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = categoriesService.retrieveCategorySchemeOrderVisualisationByUrn(getServiceContextAdministrador(),
                orderVisualisationUrn);

        // Validate
        assertNotNull(categorySchemeOrderVisualisation);
        assertEquals("ALPHABETICAL", categorySchemeOrderVisualisation.getNameableArtefact().getCode());
        assertEquals(orderVisualisationUrn, categorySchemeOrderVisualisation.getNameableArtefact().getUrn());
        assertEquals(categorySchemeUrn, categorySchemeOrderVisualisation.getCategorySchemeVersion().getMaintainableArtefact().getUrn());
        assertEqualsInternationalString(categorySchemeOrderVisualisation.getNameableArtefact().getName(), "es", "visualizacion - ordenes 1-2-1", "en", "order - visualisation 1-2-1");
        assertEquals(Integer.valueOf(1), categorySchemeOrderVisualisation.getColumnIndex());

        assertEquals("user1", categorySchemeOrderVisualisation.getCreatedBy());
        assertEqualsDate("2011-01-01 01:02:03", categorySchemeOrderVisualisation.getCreatedDate());
        assertEquals("user2", categorySchemeOrderVisualisation.getLastUpdatedBy());
        assertEqualsDate("2011-01-22 01:02:03", categorySchemeOrderVisualisation.getLastUpdated());
    }

    @Override
    @Test
    @DirtyDatabase
    public void testRetrieveCategorySchemeOrderVisualisationByCode() throws Exception {
        // Retrieve
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        String orderVisualisationCode = "ALPHABETICAL";
        CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = categoriesService.retrieveCategorySchemeOrderVisualisationByCode(getServiceContextAdministrador(), categorySchemeUrn,
                orderVisualisationCode);

        // Validate
        assertNotNull(categorySchemeOrderVisualisation);
        assertEquals(orderVisualisationCode, categorySchemeOrderVisualisation.getNameableArtefact().getCode());
        assertEquals(CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL, categorySchemeOrderVisualisation.getNameableArtefact().getUrn());
        assertEquals(categorySchemeUrn, categorySchemeOrderVisualisation.getCategorySchemeVersion().getMaintainableArtefact().getUrn());
    }

    @Override
    @Test
    @DirtyDatabase
    public void testRetrieveCategorySchemeOrderVisualisationsByCategoryScheme() throws Exception {
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        List<CategorySchemeOrderVisualisation> visualisations = categoriesService.retrieveCategorySchemeOrderVisualisationsByCategoryScheme(getServiceContextAdministrador(), categorySchemeUrn);

        assertEquals(3, visualisations.size());
        assertContainsCategorySchemeOrderVisualisation(CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL, visualisations);
        assertContainsCategorySchemeOrderVisualisation(CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_02, visualisations);
        assertContainsCategorySchemeOrderVisualisation(CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_03, visualisations);
    }

    @Override
    @Test
    @DirtyDatabase
    public void testDeleteCategorySchemeOrderVisualisation() throws Exception {
        String urn = CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_03;

        // Check that order columns exist before delete
        {
            CategoryMetamac category1 = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
            assertNotNull(category1.getOrder1());
            assertNotNull(category1.getOrder2());
            assertNotNull(category1.getOrder3());

            CategoryMetamac category2 = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
            assertNotNull(category2.getOrder1());
            assertNotNull(category2.getOrder2());
            assertNotNull(category2.getOrder3());
        }

        categoriesService.deleteCategorySchemeOrderVisualisation(getServiceContextAdministrador(), urn);

        // Retrieve deleted visualisation
        try {
            categoriesService.retrieveCategorySchemeOrderVisualisationByUrn(getServiceContextAdministrador(), urn);
            fail("visualisation deleted");
        } catch (MetamacException e) {
            assertEquals(1, e.getExceptionItems().size());
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND, 1, new String[]{urn}, e.getExceptionItems().get(0));
        }

        entityManager.clear();

        // Check order column was cleared
        {
            CategoryMetamac category1 = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
            assertNotNull(category1.getOrder1());
            assertNotNull(category1.getOrder2());
            assertNull(category1.getOrder3());

            CategoryMetamac category2 = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
            assertNotNull(category2.getOrder1());
            assertNotNull(category2.getOrder2());
            assertNull(category2.getOrder3());
        }
    }

    @Test
    public void testDeleteCategorySchemeOrderVisualisationWhenIsDefault() throws Exception {
        String urn = CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_02;

        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2);
        assertEquals(urn, categorySchemeVersion.getDefaultOrderVisualisation().getNameableArtefact().getUrn());

        categoriesService.deleteCategorySchemeOrderVisualisation(getServiceContextAdministrador(), urn);

        // Check default is null
        categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2);
        assertNull(categorySchemeVersion.getDefaultOrderVisualisation());
    }

    @Override
    @Test
    @DirtyDatabase
    public void testVersioningCategorySchemeOrderVisualisations() throws Exception {
        // Already tested

    }

    @Override
    @Test
    @DirtyDatabase
    public void testRecalculateCategoriesVisualisations() throws Exception {
        // tested in other operations

    }

    @Override
    @Test
    @DirtyDatabase
    public void testUpdateCategoryParent() throws Exception {
        // tested in other operations

    }

    @Override
    @Test
    @DirtyDatabase
    public void testUpdateCategoryInOrderVisualisation() throws Exception {
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        String categoryUrn = CATEGORY_SCHEME_1_V2_CATEGORY_1;
        String visualisationUrn = CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_02;

        categoriesService.updateCategoryInOrderVisualisation(getServiceContextAdministrador(), categoryUrn, visualisationUrn, Integer.valueOf(2));

        // Validate visualisation
        {
            CategorySchemeOrderVisualisation visualisation = categoriesService.retrieveCategorySchemeOrderVisualisationByUrn(getServiceContextAdministrador(), visualisationUrn);
            List<CategoryMetamacVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(getServiceContextAdministrador(), categorySchemeUrn, "es",
                    visualisation.getNameableArtefact().getUrn());
            assertEquals(8, categories.size());
            assertEquals(Integer.valueOf(2), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_1).getOrder()); // changed
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2).getOrder()); // changed
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1).getOrder());
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1).getOrder());
            assertEquals(Integer.valueOf(1), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_3).getOrder()); // changed
            assertEquals(Integer.valueOf(3), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4).getOrder());
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1).getOrder());
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1).getOrder());
        }

        // Validate other visualisation does not change
        {
            CategorySchemeOrderVisualisation visualisation = categoriesService.retrieveCategorySchemeOrderVisualisationByUrn(getServiceContextAdministrador(),
                    CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_03);
            List<CategoryMetamacVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(getServiceContextAdministrador(), categorySchemeUrn, "es",
                    visualisation.getNameableArtefact().getUrn());
            assertEquals(8, categories.size());
            assertEquals(Integer.valueOf(1), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_1).getOrder());
            assertEquals(Integer.valueOf(2), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2).getOrder());
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1).getOrder());
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1).getOrder());
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_3).getOrder());
            assertEquals(Integer.valueOf(3), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4).getOrder());
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1).getOrder());
            assertEquals(Integer.valueOf(0), getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1).getOrder());
        }
    }

    @Override
    @Test
    @DirtyDatabase
    public void testExportCategoryOrdersTsv() throws Exception {
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        String fileName = categoriesService.exportCategoryOrdersTsv(getServiceContextAdministrador(), categorySchemeUrn);
        assertNotNull(fileName);

        // Validate
        File file = new File(tempDirPath() + File.separatorChar + fileName);
        FileInputStream fileInputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        assertEquals("code\tlabel\tlevel\tparent\tALPHABETICAL\tVISUALISATION02\tVISUALISATION03", bufferedReader.readLine());
        assertEquals("CATEGORY01\tNombre categoryScheme-1-v2-category-1\t1\t\t1\t1\t2", bufferedReader.readLine());
        assertEquals("CATEGORY02\tNombre categoryScheme-1-v2-category-2\t1\t\t2\t2\t3", bufferedReader.readLine());
        assertEquals("CATEGORY0201\tNombre categoryScheme-1-v2-category-2-1\t2\tCATEGORY02\t1\t1\t1", bufferedReader.readLine());
        assertEquals("CATEGORY020101\tNombre categoryScheme-1-v2-category-2-1-1\t3\tCATEGORY0201\t1\t1\t1", bufferedReader.readLine());
        assertEquals("CATEGORY03\tnombre category-3\t1\t\t3\t3\t1", bufferedReader.readLine());
        assertEquals("CATEGORY04\tLanzarote\t1\t\t4\t4\t4", bufferedReader.readLine());
        assertEquals("CATEGORY0401\tLa Gomera\t2\tCATEGORY04\t1\t1\t1", bufferedReader.readLine());
        assertEquals("CATEGORY040101\tNombre categoryScheme-1-v2-category-4-1-1 Santa Cruz de Tenerife\t3\tCATEGORY0401\t1\t1\t1", bufferedReader.readLine());
        bufferedReader.close();
    }

    @Test
    @DirtyDatabase
    @Override
    public void testImportCategorySchemeOrdersTsv() throws Exception {

        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        String fileName = "importation-category-orders-01.tsv";
        File file = new File(this.getClass().getResource("/tsv/" + fileName).getFile());

        TaskImportationInfo taskImportTsvInfo = categoriesService.importCategorySchemeOrdersTsv(getServiceContextAdministrador(), categorySchemeUrn, file, fileName, Boolean.TRUE);

        // Validate
        assertEquals(false, taskImportTsvInfo.getIsPlannedInBackground());
        assertNull(taskImportTsvInfo.getJobKey());
        assertEquals(0, taskImportTsvInfo.getInformationItems().size());

        // Validate item scheme
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertEqualsDate("2011-01-01 01:02:03", categorySchemeVersion.getItemScheme().getResourceCreatedDate().toDate());
        assertTrue(DateUtils.isSameDay(new Date(), categorySchemeVersion.getItemScheme().getResourceLastUpdated().toDate()));
        assertEquals(false, categorySchemeVersion.getItemScheme().getIsTaskInBackground());

        // Validate orders
        {
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_1);
            assertEquals(Integer.valueOf(0), category.getOrder1());
            assertEquals(Integer.valueOf(3), category.getOrder2());
            assertEquals(Integer.valueOf(1), category.getOrder3());
            assertEquals(null, category.getOrder4());
        }
        {
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_2);
            assertEquals(Integer.valueOf(1), category.getOrder1());
            assertEquals(Integer.valueOf(2), category.getOrder2());
            assertEquals(Integer.valueOf(0), category.getOrder3());
            assertEquals(null, category.getOrder4());
        }
        {
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_3);
            assertEquals(Integer.valueOf(2), category.getOrder1());
            assertEquals(Integer.valueOf(1), category.getOrder2());
            assertEquals(Integer.valueOf(3), category.getOrder3());
            assertEquals(null, category.getOrder4());
        }
        {
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_4);
            assertEquals(Integer.valueOf(3), category.getOrder1());
            assertEquals(Integer.valueOf(0), category.getOrder2());
            assertEquals(Integer.valueOf(2), category.getOrder3());
            assertEquals(null, category.getOrder4());
        }
        {
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_2_1);
            assertEquals(Integer.valueOf(0), category.getOrder1());
            assertEquals(Integer.valueOf(0), category.getOrder2());
            assertEquals(Integer.valueOf(0), category.getOrder3());
            assertEquals(null, category.getOrder4());
        }
        {
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1);
            assertEquals(Integer.valueOf(0), category.getOrder1());
            assertEquals(Integer.valueOf(0), category.getOrder2());
            assertEquals(Integer.valueOf(0), category.getOrder3());
            assertEquals(null, category.getOrder4());
        }
        {
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_4_1);
            assertEquals(Integer.valueOf(0), category.getOrder1());
            assertEquals(Integer.valueOf(0), category.getOrder2());
            assertEquals(Integer.valueOf(0), category.getOrder3());
            assertEquals(null, category.getOrder4());
        }
        {
            CategoryMetamac category = categoriesService.retrieveCategoryByUrn(getServiceContextAdministrador(), CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1);
            assertEquals(Integer.valueOf(0), category.getOrder1());
            assertEquals(Integer.valueOf(0), category.getOrder2());
            assertEquals(Integer.valueOf(0), category.getOrder3());
            assertEquals(null, category.getOrder4());
        }
    }

    @Test
    @DirtyDatabase
    public void testImportCategoryOrdersTsvInBackground() throws Exception {

        // Import
        final String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        final String fileName = "importation-category-orders-01.tsv";
        final File file = new File(this.getClass().getResource("/tsv/" + fileName).getFile());

        // force background
        long numBytesPlannifyPrevious = SrmConstants.NUM_BYTES_TO_PLANNIFY_TSV_CATEGORIES_ORDERS_IMPORTATION;
        SrmConstants.NUM_BYTES_TO_PLANNIFY_TSV_CATEGORIES_ORDERS_IMPORTATION = 1;
        final StringBuilder jobKey = new StringBuilder();

        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportationInfo taskImportTsvInfo = categoriesService.importCategorySchemeOrdersTsv(getServiceContextAdministrador(), categorySchemeUrn, file, fileName, Boolean.TRUE);
                    jobKey.append(taskImportTsvInfo.getJobKey());

                    CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
                    assertEquals(true, categorySchemeVersion.getItemScheme().getIsTaskInBackground());
                } catch (MetamacException e) {
                    fail("category orders importation failed: " + e.getHumanReadableMessage());
                }
            }
        });
        waitUntilJobFinished();

        SrmConstants.NUM_BYTES_TO_PLANNIFY_TSV_CATEGORIES_ORDERS_IMPORTATION = numBytesPlannifyPrevious;

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FINISHED, task.getStatus());
        assertEquals(0, task.getTaskResults().size());

        // Validate item scheme
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertEquals(false, categorySchemeVersion.getItemScheme().getIsTaskInBackground());
    }

    @Test
    @DirtyDatabase
    public void testImportCategoryOrdersTsvErrorHeaderIncorrectCategoryNotFound() throws Exception {

        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        String fileName = "importation-category-orders-02-errors-header-01.tsv";
        File file = new File(this.getClass().getResource("/tsv/" + fileName).getFile());

        try {
            categoriesService.importCategorySchemeOrdersTsv(getServiceContextAdministrador(), categorySchemeUrn, file, fileName, Boolean.TRUE);
        } catch (MetamacException e) {
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR, 1, new Serializable[]{fileName}, e.getPrincipalException());
            assertEquals(1, e.getExceptionItems().size());
            int i = 0;
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, 1, new Serializable[]{ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE},
                    e.getExceptionItems().get(i++));
            assertEquals(e.getExceptionItems().size(), i);
        }

        // Validate item scheme
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertTrue(BooleanUtils.isNotTrue(categorySchemeVersion.getItemScheme().getIsTaskInBackground()));
    }

    @Test
    @DirtyDatabase
    public void testImportCategoryOrdersTsvErrorHeaderIncorrectCategoryNotFoundInBackground() throws Exception {

        final String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        // Import
        final String fileName = "importation-category-orders-02-errors-header-01.tsv";
        final File file = new File(this.getClass().getResource("/tsv/" + fileName).getFile());

        // force background
        long numBytesPlannifyPrevious = SrmConstants.NUM_BYTES_TO_PLANNIFY_TSV_CATEGORIES_ORDERS_IMPORTATION;
        SrmConstants.NUM_BYTES_TO_PLANNIFY_TSV_CATEGORIES_ORDERS_IMPORTATION = 1;
        final StringBuilder jobKey = new StringBuilder();

        final TransactionTemplate tt = new TransactionTemplate(transactionManager);
        tt.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        tt.execute(new TransactionCallbackWithoutResult() {

            @Override
            public void doInTransactionWithoutResult(TransactionStatus status) {
                try {
                    TaskImportationInfo taskImportTsvInfo = categoriesService.importCategorySchemeOrdersTsv(getServiceContextAdministrador(), categorySchemeUrn, file, fileName, Boolean.TRUE);
                    jobKey.append(taskImportTsvInfo.getJobKey());

                    CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
                    assertEquals(true, categorySchemeVersion.getItemScheme().getIsTaskInBackground());
                } catch (MetamacException e) {
                    fail("importation failed: " + e.getHumanReadableMessage());
                }
            }
        });
        waitUntilJobFinished();

        SrmConstants.NUM_BYTES_TO_PLANNIFY_TSV_CATEGORIES_ORDERS_IMPORTATION = numBytesPlannifyPrevious;

        // Validate
        Task task = tasksService.retrieveTaskByJob(getServiceContextAdministrador(), jobKey.toString());
        assertNotNull(task);
        assertEquals(TaskStatusTypeEnum.FAILED, task.getStatus());
        assertEquals(2, task.getTaskResults().size());
        int i = 0;
        TaskResultTypeEnum type = TaskResultTypeEnum.ERROR;
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_ERROR.getCode(), fileName, Boolean.TRUE, type, task.getTaskResults().get(i++));
        assertEqualsTaskResult(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN.getCode(), ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE, Boolean.FALSE, type,
                task.getTaskResults().get(i++));
        assertEquals(task.getTaskResults().size(), i);

        // Validate item scheme
        CategorySchemeVersionMetamac categorySchemeVersion = categoriesService.retrieveCategorySchemeByUrn(getServiceContextAdministrador(), categorySchemeUrn);
        assertEquals(false, categorySchemeVersion.getItemScheme().getIsTaskInBackground());
    }

    @Test
    @DirtyDatabase
    public void testImportCategoryOrdersTsvErrorHeaderColumnVisualisationNotFound() throws Exception {

        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        String fileName = "importation-category-orders-02-errors-header-02.tsv";
        File file = new File(this.getClass().getResource("/tsv/" + fileName).getFile());

        try {
            categoriesService.importCategorySchemeOrdersTsv(getServiceContextAdministrador(), categorySchemeUrn, file, fileName, Boolean.TRUE);
        } catch (MetamacException e) {
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR, 1, new Serializable[]{fileName}, e.getPrincipalException());
            assertEquals(1, e.getExceptionItems().size());
            int i = 0;
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, 1, new Serializable[]{ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_ORDER},
                    e.getExceptionItems().get(i++));
            assertEquals(e.getExceptionItems().size(), i);
        }
    }

    @Test
    @DirtyDatabase
    public void testImportCategoryOrdersTsvErrorHeaderIncorrectVisualisationAlphabetical() throws Exception {

        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        String fileName = "importation-category-orders-02-errors-header-03-alphabetical.tsv";
        File file = new File(this.getClass().getResource("/tsv/" + fileName).getFile());

        try {
            categoriesService.importCategorySchemeOrdersTsv(getServiceContextAdministrador(), categorySchemeUrn, file, fileName, Boolean.TRUE);
        } catch (MetamacException e) {
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR, 1, new Serializable[]{fileName}, e.getPrincipalException());
            assertEquals(1, e.getExceptionItems().size());
            int i = 0;
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_ALPHABETICAL_VISUALISATION_NOT_SUPPORTED, 0, null, e.getExceptionItems().get(i++));
            assertEquals(e.getExceptionItems().size(), i);
        }
    }

    @Test
    @DirtyDatabase
    public void testImportCategoryOrdersTsvErrorBodyIncorrectNumberCategories() throws Exception {

        final String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        final String fileName = "importation-category-orders-03-errors-body-01.tsv";
        final File file = new File(this.getClass().getResource("/tsv/" + fileName).getFile());

        try {
            categoriesService.importCategorySchemeOrdersTsv(getServiceContextAdministrador(), categorySchemeUrn, file, fileName, Boolean.TRUE);
        } catch (MetamacException e) {
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR, 1, new Serializable[]{fileName}, e.getPrincipalException());
            assertEquals(1, e.getExceptionItems().size());
            int i = 0;
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_INCORRECT_NUMBER_CATEGORIES, 2, new Serializable[]{8, 5}, e.getExceptionItems().get(i++));
            assertEquals(e.getExceptionItems().size(), i);
        }
    }

    @Test
    @DirtyDatabase
    public void testImportCategoryOrdersTsvErrorBodyVisualisationNotFound() throws Exception {

        final String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        final String fileName = "importation-category-orders-03-errors-body-02.tsv";
        final File file = new File(this.getClass().getResource("/tsv/" + fileName).getFile());

        try {
            categoriesService.importCategorySchemeOrdersTsv(getServiceContextAdministrador(), categorySchemeUrn, file, fileName, Boolean.TRUE);
        } catch (MetamacException e) {
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR, 1, new Serializable[]{fileName}, e.getPrincipalException());
            assertEquals(1, e.getExceptionItems().size());
            int i = 0;
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_ORDER_VISUALISATION_NOT_FOUND, 2,
                    new Serializable[]{"VISUALISATION001", "urn:sdmx:org.sdmx.infomodel.categoryscheme.CategoryScheme=SDMX01:CATEGORYSCHEME01(02.000)"}, e.getExceptionItems().get(i++));
            assertEquals(e.getExceptionItems().size(), i);
        }
    }

    @Test
    @DirtyDatabase
    public void testImportCategoryOrdersTsvErrorBodyOrderEmpty() throws Exception {

        final String categorySchemeUrn = CATEGORY_SCHEME_1_V2;
        final String fileName = "importation-category-orders-03-errors-body-03.tsv";
        final File file = new File(this.getClass().getResource("/tsv/" + fileName).getFile());

        try {
            categoriesService.importCategorySchemeOrdersTsv(getServiceContextAdministrador(), categorySchemeUrn, file, fileName, Boolean.TRUE);
        } catch (MetamacException e) {
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR, 1, new Serializable[]{fileName}, e.getPrincipalException());
            assertEquals(2, e.getExceptionItems().size());
            int i = 0;
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_REQUIRED, 2, new Serializable[]{"CATEGORY0201", "parameter.srm.importation.order"},
                    e.getExceptionItems().get(i++));
            assertEqualsMetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_REQUIRED, 2, new Serializable[]{"CATEGORY04", "parameter.srm.importation.order"},
                    e.getExceptionItems().get(i++));
            assertEquals(e.getExceptionItems().size(), i);
        }
    }

    @Test
    public void testRetrieveCategoriesBySchemeUrnCheckOrderVisualisationsAndMetadataInCategoryMetamacVisualisationResult() throws Exception {
        // Retrieve
        String categorySchemeUrn = CATEGORY_SCHEME_1_V2;

        // LOCALE = 'es' and ORDER = ALPHABETICAL (1)
        {
            String locale = "es";
            List<CategoryMetamacVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(getServiceContextAdministrador(), categorySchemeUrn, locale,
                    CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_01_ALPHABETICAL);

            // Validate
            assertEquals(8, categories.size());
            {
                // Category 01 (validate all metadata)
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_1);
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_1, category.getUrn());
                assertEquals("CATEGORY01", category.getCode());
                assertEquals("Nombre categoryScheme-1-v2-category-1", category.getName());
                assertEquals("Descripción categoryScheme-1-v2-category-1", category.getDescription());
                assertEquals(Long.valueOf(121), category.getItemIdDatabase());
                assertEquals(null, category.getParent());
                assertEquals(null, category.getParentIdDatabase());
                assertEquals(Integer.valueOf(0), category.getOrder());
                MetamacAsserts.assertEqualsDate("2011-01-01 01:02:03", category.getCreatedDate());
            }
            {
                // Category 02
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2);
                assertEquals("CATEGORY02", category.getCode());
                assertEquals("Nombre categoryScheme-1-v2-category-2", category.getName());
                assertEquals(null, category.getDescription());
                assertEquals(Integer.valueOf(1), category.getOrder());
                MetamacAsserts.assertEqualsDate("2011-03-02 04:05:06", category.getCreatedDate());
            }
            {
                // Category 02 01 (validate parent)
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1);
                assertEquals("CATEGORY02", category.getParent().getCode());
                assertEquals("Nombre categoryScheme-1-v2-category-2-1", category.getName());
                assertEquals("Descripción cat2-1", category.getDescription());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_2, category.getParent().getUrn());
                assertEquals(Long.valueOf("122"), category.getParentIdDatabase());
                assertEquals(Integer.valueOf(0), category.getOrder());
            }
            {
                // Category 02 01 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1);
                assertEquals("CATEGORY0201", category.getParent().getCode());
                assertEquals("Nombre categoryScheme-1-v2-category-2-1-1", category.getName());
                assertEquals(Long.valueOf("1221"), category.getParentIdDatabase());
                assertEquals(Integer.valueOf(0), category.getOrder());
            }
            {
                // Category 03
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_3);
                assertEquals("nombre category-3", category.getName());
                assertEquals(Integer.valueOf(2), category.getOrder());
            }
            {
                // Category 04
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4);
                assertEquals("Lanzarote", category.getName());
                assertEquals(Integer.valueOf(3), category.getOrder());
            }
            {
                // Category 04 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1);
                assertEquals("La Gomera", category.getName());
                assertEquals(Integer.valueOf(0), category.getOrder());
            }
            {
                // Category 04 01 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1);
                assertEquals("CATEGORY0401", category.getParent().getCode());
                assertEquals("Nombre categoryScheme-1-v2-category-4-1-1 Santa Cruz de Tenerife", category.getName());
                assertEquals(CATEGORY_SCHEME_1_V2_CATEGORY_4_1, category.getParent().getUrn());
                assertEquals(Integer.valueOf(0), category.getOrder());
            }
        }

        // LOCALE = 'en' and ORDER = VISUALISATION03 (3)
        {
            String locale = "en";
            List<CategoryMetamacVisualisationResult> categories = categoriesService.retrieveCategoriesByCategorySchemeUrn(getServiceContextAdministrador(), categorySchemeUrn, locale,
                    CATEGORY_SCHEME_1_V2_ORDER_VISUALISATION_03);

            // Validate
            assertEquals(8, categories.size());
            {
                // Category 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_1);
                assertEquals("Name categoryScheme-1-v2-category-1", category.getName());
                assertEquals(null, category.getDescription()); // no English description
                assertEquals(Integer.valueOf(1), category.getOrder());
            }
            {
                // Category 02
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2);
                assertEquals(null, category.getName()); // no English name
                assertEquals(null, category.getDescription());
                assertEquals(Integer.valueOf(2), category.getOrder());
            }
            {
                // Category 02 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1);
                assertEquals("Name categoryScheme-1-v2-category-2-1", category.getName());
                assertEquals("Description cat2-1", category.getDescription());
                assertEquals(Integer.valueOf(0), category.getOrder());
            }
            {
                // Category 02 01 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_2_1_1);
                assertEquals(null, category.getName()); // no English name
                assertEquals(Integer.valueOf(0), category.getOrder());
            }
            {
                // Category 03
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_3);
                assertEquals("name category-3", category.getName());
                assertEquals(Integer.valueOf(0), category.getOrder());
            }
            {
                // Category 04
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4);
                assertEquals(null, category.getName()); // no English name
                assertEquals(Integer.valueOf(3), category.getOrder());
            }
            {
                // Category 04 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1);
                assertEquals(null, category.getName()); // no English name
                assertEquals(Integer.valueOf(0), category.getOrder());
            }
            {
                // Category 04 01 01
                CategoryMetamacVisualisationResult category = getCategoryMetamacVisualisationResult(categories, CATEGORY_SCHEME_1_V2_CATEGORY_4_1_1);
                assertEquals("CATEGORY0401", category.getParent().getCode());
                assertEquals("Name categoryScheme-1-v2-category-4-1-1", category.getName());
                assertEquals(Integer.valueOf(0), category.getOrder());
            }
        }
    }

    @Override
    @Test
    public void testRetrieveCategoriesByCategorySchemeUrnOrderedInDepth() throws Exception {
        // tested in CategoriesMetamacRepositoryTest

    }

    private void assertEqualsTaskResult(String codeExpected, String infoExpected, Boolean isPrincipalExpected, TaskResultTypeEnum typeExpected, TaskResult actual) {
        assertEquals(codeExpected, actual.getCode());
        assertEquals(infoExpected, actual.getParameters());
        assertEquals(isPrincipalExpected, actual.isPrincipal());
        assertEquals(typeExpected, actual.getResultType());
    }

    private CategorySchemeOrderVisualisation assertContainsCategorySchemeOrderVisualisation(String categorySchemeOrderVisualisationUrnExpected,
            List<CategorySchemeOrderVisualisation> actuals) {
        for (CategorySchemeOrderVisualisation actual : actuals) {
            if (actual.getNameableArtefact().getUrn().equals(categorySchemeOrderVisualisationUrnExpected)) {
                return actual;
            }
        }
        fail("order visualisation not found");
        return null;
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCategoriesTest.xml";
    }
}