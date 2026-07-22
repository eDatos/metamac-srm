package org.siemac.metamac.srm.core.category.serviceimpl;

import static org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils.addExceptionToExceptionItemsByResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.enume.domain.VersionPatternEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.ExceptionLevelEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.io.FileUtils;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.base.domain.ResourceFile;
import org.siemac.metamac.srm.core.base.domain.ResourceMetadataFile;
import org.siemac.metamac.srm.core.base.domain.ResourceMetadataFileRepository;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.serviceimpl.utils.BaseReplaceFromTemporalMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryElement;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacProperties;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacResultSelection;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeOrderVisualisation;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.category.domain.shared.CategoryElementVisualisationResult;
import org.siemac.metamac.srm.core.category.domain.shared.CategoryMetamacVisualisationResult;
import org.siemac.metamac.srm.core.category.domain.shared.CodeCategoryElementNormalisationResult;
import org.siemac.metamac.srm.core.category.serviceimpl.utils.CategoriesMetamacInvocationValidator;
import org.siemac.metamac.srm.core.category.serviceimpl.utils.CategoriesUtils;
import org.siemac.metamac.srm.core.category.serviceimpl.utils.CategorisationsUtils;
import org.siemac.metamac.srm.core.code.domain.TaskImportationInfo;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.SrmValidation;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.GeneratorUrnUtils;
import org.siemac.metamac.srm.core.common.service.utils.SemanticIdentifierValidationUtils;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;
import org.siemac.metamac.srm.core.common.service.utils.TsvExportationUtils;
import org.siemac.metamac.srm.core.common.service.utils.TsvImportationUtils;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalogRepository;
import org.siemac.metamac.srm.core.normalisation.DiceLuceneRamAproxStringMatch;
import org.siemac.metamac.srm.core.normalisation.MatchResult;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamacProperties;
import org.siemac.metamac.srm.core.organisation.serviceapi.OrganisationsMetamacService;
import org.siemac.metamac.srm.core.serviceapi.StreamMessagingService;
import org.siemac.metamac.srm.core.serviceimpl.result.ResourceVersionPublicationResult;
import org.siemac.metamac.srm.core.serviceimpl.result.SendStreamMessageResult;
import org.siemac.metamac.srm.core.task.domain.ImportationCategoriesTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationCategoryElementsTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationCodeOrdersTsvHeader;
import org.siemac.metamac.srm.core.task.serviceapi.TasksMetamacService;
import org.siemac.metamac.srm.core.util.AlphabeticalComparator.AlphabeticalByLevelComparator;
import org.siemac.metamac.srm.core.util.AlphabeticalComparator.CategorySchemeOrderIndexByLevelComparator;
import org.siemac.metamac.srm.core.util.InternationalStringUtils;
import org.siemac.metamac.srm.core.util.UtilsMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefactRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefactRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.base.serviceapi.BaseService;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.ItemSchemesCopyCallback;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseCopyAllMetadataUtils;
import com.arte.statistic.sdmx.srm.core.category.domain.Categorisation;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorisationRepository;
import com.arte.statistic.sdmx.srm.core.category.domain.Category;
import com.arte.statistic.sdmx.srm.core.category.domain.CategorySchemeVersion;
import com.arte.statistic.sdmx.srm.core.category.serviceapi.CategoriesService;
import com.arte.statistic.sdmx.srm.core.category.serviceimpl.utils.CategoriesInvocationValidator;
import com.arte.statistic.sdmx.srm.core.common.domain.InternationalStringRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResultSelection;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxVersionUtils;
import com.arte.statistic.sdmx.srm.core.organisation.domain.Organisation;

/**
 * Implementation of CategoriesMetamacService.
 */
@Service("categoriesMetamacService")
public class CategoriesMetamacServiceImpl extends CategoriesMetamacServiceImplBase {

    private static Logger                             logger = LoggerFactory.getLogger(CategoriesMetamacServiceImpl.class);

    @Autowired
    private BaseService                               baseService;

    @Autowired
    private CategoriesService                         categoriesService;

    @Autowired
    private OrganisationsMetamacService               organisationsService;

    @Autowired
    @Qualifier("categorySchemeLifeCycle")
    private LifeCycle                                 categorySchemeLifeCycle;

    @Autowired
    private SrmValidation                             srmValidation;

    @Autowired
    private SrmConfiguration                          srmConfiguration;

    @Autowired
    @Qualifier("categoriesVersioningCallbackMetamac")
    private ItemSchemesCopyCallback                   categoriesVersioningCallback;

    @Autowired
    @Qualifier("categoriesDummyVersioningCallbackMetamac")
    private ItemSchemesCopyCallback                   categoriesDummyVersioningCallback;

    @Autowired
    @Qualifier("categoriesCopyCallbackMetamac")
    private ItemSchemesCopyCallback                   categoriesCopyCallback;

    @Autowired
    private CategorySchemeStreamMessagingCallbackImpl categorySchemeStreamMessagingCallback;

    @Autowired
    private StreamMessagingService                    streamMessagingService;

    @Autowired
    private TasksMetamacService                       tasksMetamacService;

    @Autowired
    private MaintainableArtefactRepository            maintainableArtefactRepository;

    @Autowired
    private ItemSchemeVersionRepository               itemSchemeVersionRepository;

    @Autowired
    private CategorisationRepository                  categorisationRepository;

    @Autowired
    private InternationalStringRepository             internationalStringRepository;

    @Autowired
    private ItemSchemeRepository                      itemSchemeRepository;

    @Autowired
    private EniSchemaCatalogRepository                eniSchemaCatalogRepository;

    @Autowired
    private IdentifiableArtefactRepository            identifiableArtefactRepository;

    @Autowired
    private ResourceMetadataFileRepository            resourceMetadataFileRepository;

    @PersistenceContext(unitName = "SrmCoreEntityManagerFactory")
    protected EntityManager                           entityManager;

    public CategoriesMetamacServiceImpl() {
    }

    @Override
    public CategorySchemeVersionMetamac createCategoryScheme(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {

        preCreateCategoryScheme(ctx, categorySchemeVersion);

        // Save categoryScheme
        categorySchemeVersion = (CategorySchemeVersionMetamac) categoriesService.createCategoryScheme(ctx, categorySchemeVersion, SrmConstants.VERSION_PATTERN_METAMAC);

        // Post create actions
        CategorySchemeVersionMetamac categorySchemeVersionWithAlphabetical = createCategorySchemeOrderVisualisationAlphabetical(ctx, categorySchemeVersion);

        return getCategorySchemeVersionMetamacRepository().save(categorySchemeVersionWithAlphabetical);
    }

    @Override
    public CategorySchemeVersionMetamac preCreateCategoryScheme(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkCreateCategoryScheme(categorySchemeVersion, null);
        checkCategorySchemeToCreateOrUpdate(ctx, categorySchemeVersion);

        // Fill metadata
        categorySchemeVersion.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        categorySchemeVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);
        categorySchemeVersion.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
        categorySchemeVersion.setStreamMessageStatus(StreamMessageStatusEnum.PENDING);

        return categorySchemeVersion;
    }

    @Override
    public CategorySchemeVersionMetamac updateCategoryScheme(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkUpdateCategoryScheme(categorySchemeVersion, null);
        checkCategorySchemeToCreateOrUpdate(ctx, categorySchemeVersion);

        String oldUrn = categorySchemeVersion.getMaintainableArtefact().getUrn();

        // Save categoryScheme
        CategorySchemeVersionMetamac categorySchemeVersionMetamac = (CategorySchemeVersionMetamac) categoriesService.updateCategoryScheme(ctx, categorySchemeVersion);

        // Updates URNs of CategorySchemeOrderVisualisations
        // If code have been changed, update URN. In metamac not is possible to change the maintainer,
        // if this were not so, the URN will also be updated when the maintainer changes.
        if (categorySchemeVersionMetamac.getMaintainableArtefact().getIsCodeUpdated()) {
            // IMPORTANT: Update order urn efficiently to avoid one update for each code
            getCategorySchemeOrderVisualisationRepository().updateUrnAllCategorySchemeOrderVisualisationsByCategorySchemeEfficiently(categorySchemeVersionMetamac, oldUrn);
            categorySchemeVersionMetamac = retrieveCategorySchemeByUrn(ctx, categorySchemeVersionMetamac.getMaintainableArtefact().getUrn());
        }
        return categorySchemeVersionMetamac;
    }

    @Override
    public CategorySchemeVersionMetamac retrieveCategorySchemeByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CategoriesInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeVersionByUrn(urn);
        return categorySchemeVersion;
    }

    @Override
    public List<CategorySchemeVersionMetamac> retrieveCategorySchemeVersions(ServiceContext ctx, String urn) throws MetamacException {

        // Retrieve categorySchemeVersions
        List<CategorySchemeVersion> categorySchemeVersions = categoriesService.retrieveCategorySchemeVersions(ctx, urn);

        // Typecast to CategorySchemeVersionMetamac
        return categorySchemeVersionsToCategorySchemeVersionsMetamac(categorySchemeVersions);
    }

    @Override
    public PagedResult<CategorySchemeVersionMetamac> findCategorySchemesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        PagedResult<CategorySchemeVersion> categorySchemeVersionPagedResult = categoriesService.findCategorySchemesByCondition(ctx, conditions, pagingParameter);
        return pagedResultCategorySchemeVersionToMetamac(categorySchemeVersionPagedResult);
    }

    @Override
    public PagedResult<CategorySchemeVersionMetamac> findCategorySchemesWithCategoriesCanBeCategorisationCategoryByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter) throws MetamacException {
        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CategoryMetamac.class).distinctRoot().build();
        }
        // Category scheme internally or externally published
        ConditionalCriteria publishedCondition = ConditionalCriteriaBuilder.criteriaFor(CategorySchemeVersionMetamac.class)
                .withProperty(CategorySchemeVersionMetamacProperties.maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE).buildSingle();
        conditions.add(publishedCondition);

        return findCategorySchemesByCondition(ctx, conditions, pagingParameter);
    }

    @Override
    public CategorySchemeVersionMetamac sendCategorySchemeToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CategorySchemeVersionMetamac) categorySchemeLifeCycle.sendToProductionValidation(ctx, urn);
    }

    @Override
    public CategorySchemeVersionMetamac sendCategorySchemeToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CategorySchemeVersionMetamac) categorySchemeLifeCycle.sendToDiffusionValidation(ctx, urn);
    }

    @Override
    public CategorySchemeVersionMetamac rejectCategorySchemeProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CategorySchemeVersionMetamac) categorySchemeLifeCycle.rejectProductionValidation(ctx, urn);
    }

    @Override
    public CategorySchemeVersionMetamac rejectCategorySchemeDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (CategorySchemeVersionMetamac) categorySchemeLifeCycle.rejectDiffusionValidation(ctx, urn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResourceVersionPublicationResult<CategorySchemeVersionMetamac> publishInternallyCategoryScheme(ServiceContext ctx, String urn, Boolean forceLatestFinal) throws MetamacException {
        return (ResourceVersionPublicationResult<CategorySchemeVersionMetamac>) categorySchemeLifeCycle.publishInternally(ctx, urn, forceLatestFinal);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResourceVersionPublicationResult<CategorySchemeVersionMetamac> publishExternallyCategoryScheme(ServiceContext ctx, String urn) throws MetamacException {
        return (ResourceVersionPublicationResult<CategorySchemeVersionMetamac>) categorySchemeLifeCycle.publishExternally(ctx, urn);
    }

    @Override
    public SendStreamMessageResult resendCategoryScheme(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {
        return streamMessagingService.sendMessage(categorySchemeVersion, categorySchemeStreamMessagingCallback);
    }

    @Override
    public void deleteCategoryScheme(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, urn);
        checkCategorySchemeCanBeModified(categorySchemeVersion);

        // Delete
        categorySchemeVersion.setDefaultOrderVisualisation(null);
        categoriesService.deleteCategoryScheme(ctx, urn);
    }

    @Override
    public TaskInfo copyCategoryScheme(ServiceContext ctx, String urnToCopy, String newCode) throws MetamacException {
        String maintainerUrn = srmConfiguration.retrieveMaintainerUrnDefault();
        VersionPatternEnum versionPattern = SrmConstants.VERSION_PATTERN_METAMAC;
        return categoriesService.copyCategoryScheme(ctx, urnToCopy, newCode, maintainerUrn, versionPattern, categoriesCopyCallback);
    }

    @Override
    public TaskInfo versioningCategoryScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        return createVersionOfCategoryScheme(ctx, urnToCopy, categoriesVersioningCallback, versionType, false);
    }

    @Override
    public TaskInfo createTemporalVersionCategoryScheme(ServiceContext ctx, String urnToCopy) throws MetamacException {
        return createVersionOfCategoryScheme(ctx, urnToCopy, categoriesDummyVersioningCallback, null, true);
    }

    @Override
    public TaskInfo createVersionFromTemporalCategoryScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionTypeEnum) throws MetamacException {

        CategorySchemeVersionMetamac categorySchemeVersionTemporal = retrieveCategorySchemeByUrn(ctx, urnToCopy);

        // Check if is a temporal version
        SrmValidationUtils.checkArtefactIsTemporal(categorySchemeVersionTemporal.getMaintainableArtefact());

        // Retrieve the original artifact
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, GeneratorUrnUtils.makeUrnFromTemporal(urnToCopy));

        // Set the new version in the temporal artifact
        categorySchemeVersionTemporal.getMaintainableArtefact().setVersionLogic(
                SdmxVersionUtils.createNextVersion(categorySchemeVersion.getMaintainableArtefact().getVersionLogic(), categorySchemeVersion.getItemScheme().getVersionPattern(), versionTypeEnum));

        categorySchemeVersionTemporal.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE); // For calculates new urns
        categorySchemeVersionTemporal = (CategorySchemeVersionMetamac) categoriesService.updateCategoryScheme(ctx, categorySchemeVersionTemporal);

        // Set null replacedBy in the original entity
        categorySchemeVersion.getMaintainableArtefact().setReplacedByVersion(null);

        // Convert categorisations in no temporal
        createVersionFromTemporalCategorisations(ctx, categorySchemeVersionTemporal.getMaintainableArtefact());

        // Convert order in no temporal
        createOrderVisualisationVersionFromTemporalOrderVisualisation(categorySchemeVersionTemporal);

        TaskInfo versioningResult = new TaskInfo();
        versioningResult.setUrnResult(categorySchemeVersionTemporal.getMaintainableArtefact().getUrn());
        return versioningResult;
    }

    @Override
    public CategorySchemeVersionMetamac mergeTemporalVersion(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeTemporalVersion) throws MetamacException {
        // Check if is a temporal version
        SrmValidationUtils.checkArtefactIsTemporal(categorySchemeTemporalVersion.getMaintainableArtefact());
        SrmValidationUtils.checkArtefactProcStatus(categorySchemeTemporalVersion.getLifeCycleMetadata(), categorySchemeTemporalVersion.getMaintainableArtefact().getUrn(),
                ProcStatusEnum.DIFFUSION_VALIDATION);

        // Load original version
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, GeneratorUrnUtils.makeUrnFromTemporal(categorySchemeTemporalVersion.getMaintainableArtefact().getUrn()));

        // Inherit InternationalStrings
        BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToItemSchemeVersionWithoutItems(categorySchemeVersion, categorySchemeTemporalVersion, internationalStringRepository);

        // Merge Metamac metadata of ItemScheme
        categorySchemeVersion.setLifeCycleMetadata(BaseReplaceFromTemporalMetamac.replaceProductionAndDifussionLifeCycleMetadataFromTemporal(categorySchemeVersion.getLifeCycleMetadata(),
                categorySchemeTemporalVersion.getLifeCycleMetadata()));

        // Merge metadata of Item
        Map<String, Item> temporalItemMap = SrmServiceUtils.createMapOfItemsByOriginalUrn(categorySchemeTemporalVersion.getItems());
        List<ItemResult> categoriesFoundEfficiently = getCategoryMetamacRepository().findCategoriesByCategorySchemeUnordered(categorySchemeTemporalVersion.getId(), ItemMetamacResultSelection.ALL);
        Map<String, ItemResult> categoriesFoundEfficientlyByUrn = SdmxSrmUtils.createMapOfItemsResultByUrn(categoriesFoundEfficiently);
        for (Item item : categorySchemeVersion.getItems()) {
            CategoryMetamac category = (CategoryMetamac) item;
            CategoryMetamac categoryTemp = (CategoryMetamac) temporalItemMap.get(item.getNameableArtefact().getUrn());
            ItemResult categoryTempItemResult = categoriesFoundEfficientlyByUrn.get(categoryTemp.getNameableArtefact().getUrn());

            // Inherit InternationalStrings
            BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToItem(category, categoryTempItemResult, internationalStringRepository);

            category.removeAllEniSchemas();
            category.getEniSchemas().addAll(categoryTemp.getEniSchemas());

            category.setCategoryElement(categoryTemp.getCategoryElement());

            // Order Visualisation
            for (int i = 1; i <= SrmConstants.CATEGORY_SCHEME_ORDER_VISUALISATION_MAXIMUM_NUMBER; i++) {
                Integer order = SrmServiceUtils.getCategorySchemeOrder(categoryTemp, i);
                SrmServiceUtils.setCategorySchemeOrder(category, i, order);
            }

            // IMPORTANT! If any InternationalString is added, do an efficient query and retrieve from categoryTempItemResult
        }

        // Merge Categorizations
        Map<String, Categorisation> temporalCategorisationMap = SrmServiceUtils.createMapOfCategorisationsByOriginalUrn(categorySchemeTemporalVersion.getMaintainableArtefact().getCategorisations());
        for (Categorisation categorisation : categorySchemeVersion.getMaintainableArtefact().getCategorisations()) {
            Categorisation categorisationTemp = temporalCategorisationMap.get(categorisation.getMaintainableArtefact().getUrn());
            // Inherit InternationalStrings
            BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToCategorisation(categorisation, categorisationTemp, internationalStringRepository);

            // Valid To
            if (!categorisation.getMaintainableArtefact().getIsImported()) {
                categorisation.getMaintainableArtefact().setValidTo(categorisationTemp.getMaintainableArtefact().getValidTo());
            }
        }

        // Add Categorisations
        boolean thereAreNewCategorisations = CategorisationsUtils.addCategorisationsTemporalToItemScheme(categorySchemeTemporalVersion, categorySchemeVersion);
        boolean thereAreNewOrders = CategoriesUtils.addCategorySchemeOrderVisualisationTemporalToItemScheme(categorySchemeTemporalVersion, categorySchemeVersion, internationalStringRepository);

        setDefaultCategorySchemeOrderVisualisation(categorySchemeTemporalVersion, categorySchemeVersion);

        if (thereAreNewCategorisations || thereAreNewOrders) {
            // ===============================================================
            // DANGEROUS CODE: In spite of to remove item from temporal scheme and then associate to another scheme, hibernate delete this item when delete item scheme. For this, we need to clear the
            // context to avoid delete the temporary scheme with the previous temporary item when delete the temporary item scheme.
            entityManager.flush();
            entityManager.clear();
            // ===============================================================
        }

        // Delete temporal version
        deleteCategoryScheme(ctx, categorySchemeTemporalVersion.getMaintainableArtefact().getUrn());

        categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeVersion.getMaintainableArtefact().getUrn());
        return categorySchemeVersion;
    }

    @Override
    public CategorySchemeVersionMetamac startCategorySchemeValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkStartValidity(urn, null);
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, urn);
        srmValidation.checkStartValidity(ctx, categorySchemeVersion.getMaintainableArtefact(), categorySchemeVersion.getLifeCycleMetadata());

        // Start validity
        categorySchemeVersion = (CategorySchemeVersionMetamac) categoriesService.startCategorySchemeValidity(ctx, urn, null);
        return categorySchemeVersion;
    }

    @Override
    public CategorySchemeVersionMetamac endCategorySchemeValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkEndValidity(urn, null);
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, urn);
        srmValidation.checkEndValidity(ctx, categorySchemeVersion.getMaintainableArtefact(), categorySchemeVersion.getLifeCycleMetadata());

        // End validity
        categorySchemeVersion = (CategorySchemeVersionMetamac) categoriesService.endCategorySchemeValidity(ctx, urn, null);
        return categorySchemeVersion;
    }

    @Override
    public CategoryMetamac createCategory(ServiceContext ctx, String categorySchemeUrn, CategoryMetamac category) throws MetamacException {

        // Validation
        preCreateCategory(ctx, categorySchemeUrn, category);
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);
        srmValidation.checkItemsStructureCanBeModified(ctx, categorySchemeVersion);

        // Save category
        category = (CategoryMetamac) categoriesService.createCategory(ctx, categorySchemeUrn, category);
        // Set order to created category
        category = initVisualisationsToCategory(categorySchemeVersion, category, false);

        return category;
    }

    @Override
    public CategoryMetamac preCreateCategory(ServiceContext ctx, String categorySchemeUrn, CategoryMetamac category) throws MetamacException {
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);

        // Validation
        CategoriesMetamacInvocationValidator.checkCreateCategory(categorySchemeVersion, category, null);
        checkCategoryToCreateOrUpdate(ctx, categorySchemeVersion, category);

        return category;
    }

    @Override
    public CategoryMetamac updateCategory(ServiceContext ctx, CategoryMetamac category) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkUpdateCategory(category, null);
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByCategoryUrn(ctx, category.getNameableArtefact().getUrn());
        checkCategoryToCreateOrUpdate(ctx, categorySchemeVersion, category);

        CategoryMetamac categoryMetamac = (CategoryMetamac) categoriesService.updateCategory(ctx, category);

        // Set order to update category (recalculates alphabetical position if code changed)
        categoryMetamac = initVisualisationsToCategory(categorySchemeVersion, categoryMetamac, true);

        return categoryMetamac;
    }

    @Override
    public CategoryMetamac retrieveCategoryByUrn(ServiceContext ctx, String urn) throws MetamacException {
        CategoriesMetamacInvocationValidator.checkRetrieveByUrn(urn);
        CategoryMetamac category = getCategoryMetamacRepository().findByUrn(urn);
        if (category == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return category;
    }

    @Override
    public PagedResult<CategoryMetamac> findCategoriesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        PagedResult<Category> categoriesPagedResult = categoriesService.findCategoriesByCondition(ctx, conditions, pagingParameter);
        return pagedResultCategoryToMetamac(categoriesPagedResult);
    }

    @Override
    public void deleteCategory(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        CategoryMetamac category = retrieveCategoryByUrn(ctx, urn);
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByCategoryUrn(ctx, urn);
        checkCategorySchemeCanBeModified(categorySchemeVersion);
        srmValidation.checkItemsStructureCanBeModified(ctx, categorySchemeVersion);

        // Before delete category, update order of other categories in level
        for (CategorySchemeOrderVisualisation categorySchemeOrderVisualisation : categorySchemeVersion.getOrderVisualisations()) {
            reorderCategoriesDeletingOneCode(categorySchemeVersion, categorySchemeOrderVisualisation, (Category) category.getParent(), category);
        }

        // Delete
        categoriesService.deleteCategory(ctx, urn);
    }

    @Override
    public void updateCategoryParent(ServiceContext ctx, String categoryUrn, String newCategoryParentUrn) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkUpdateCategoryParent(categoryUrn, newCategoryParentUrn, null);
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByCategoryUrn(ctx, categoryUrn);
        checkCategorySchemeCanBeModified(categorySchemeVersion);
        srmValidation.checkItemsStructureCanBeModified(ctx, categorySchemeVersion);

        CategoryMetamac category = retrieveCategoryByUrn(ctx, categoryUrn);
        Category parentActual = category.getParent() != null ? category.getParent() : null;
        CategoryMetamac parentTarget = newCategoryParentUrn != null ? retrieveCategoryByUrn(ctx, newCategoryParentUrn) : null;
        if (!SdmxSrmUtils.isItemParentChanged(parentActual, parentTarget)) {
            // nothing
            return;
        }

        // Check target parent is not a child of this category
        if (newCategoryParentUrn != null) {
            checkCategoryIsNotChildren(ctx, category, newCategoryParentUrn);
        }

        // Update target parent
        if (parentTarget == null) {
            category.setParent(null);
            category.setItemSchemeVersionFirstLevel(categorySchemeVersion);
        } else {
            category.setParent(parentTarget);
            category.setItemSchemeVersionFirstLevel(null);
        }

        // Update orders in previous and new level
        Boolean alphabeticalAlreadyUpdated = Boolean.FALSE; // to avoid select to compare code of nameable
        Integer orderInLastPosition = null; // to avoid unnecessary extra queries
        for (CategorySchemeOrderVisualisation categorySchemeOrderVisualisation : categorySchemeVersion.getOrderVisualisations()) {
            // remove from previous level
            reorderCategoriesDeletingOneCode(categorySchemeVersion, categorySchemeOrderVisualisation, parentActual, category);
            // add to new level
            if (!alphabeticalAlreadyUpdated && SrmServiceUtils.isAlphabeticalOrderVisualisation(categorySchemeOrderVisualisation)) {
                setCategoryOrderInAlphabeticalPositionAndReorderCategoriesInLevel(categorySchemeVersion, categorySchemeOrderVisualisation, category.getParent(), category);
                alphabeticalAlreadyUpdated = Boolean.TRUE;
            } else {
                orderInLastPosition = setCategoryOrderInLastPositionInLevel(categorySchemeVersion, categorySchemeOrderVisualisation, category.getParent(), category, orderInLastPosition);
            }
        }

        // Update category
        baseService.updateItemSchemeLastUpdated(ctx, categorySchemeVersion);
        getCategoryMetamacRepository().save(category);
    }

    @Override
    public TaskImportationInfo importCategoriesTsv(ServiceContext ctx, String categorySchemeUrn, File file, String fileName, boolean updateAlreadyExisting, Boolean canBeBackground)
            throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkImportCategoriesTsv(categorySchemeUrn, file, fileName, updateAlreadyExisting, canBeBackground, null);
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);
        TsvImportationUtils.checkItemSchemeCanExecuteImportation(categorySchemeVersion, categorySchemeVersion.getLifeCycleMetadata(), canBeBackground);

        // Decide if task must be executed in background
        boolean executeInBackground = SrmServiceUtils.taskMustBeBackground(file, canBeBackground, SrmConstants.NUM_BYTES_TO_PLANNIFY_TSV_ITEMS_IMPORTATION);

        // Plannify task if background
        if (executeInBackground) {
            categorySchemeVersion.getItemScheme().setIsTaskInBackground(Boolean.TRUE);
            itemSchemeRepository.save(categorySchemeVersion.getItemScheme());

            String jobKey = tasksMetamacService.plannifyImportCategoriesTsvInBackground(ctx, categorySchemeUrn, file, fileName, updateAlreadyExisting);
            return new TaskImportationInfo(Boolean.TRUE, jobKey);
        }

        // Execute importation now
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        List<MetamacExceptionItem> informationItems = new ArrayList<MetamacExceptionItem>();
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        List<CategoryMetamac> categtoriesToPersist = null;
        Map<String, Item> categoriesToPersistByCode = null;
        Map<String, Item> categoriesPreviousInCategorySchemeByCode = null;
        try {
            InputStream stream = new FileInputStream(file);
            String charset = FileUtils.guessCharset(file);
            inputStreamReader = new InputStreamReader(stream, charset);
            bufferedReader = new BufferedReader(inputStreamReader);

            // Header
            String line = bufferedReader.readLine();
            ImportationCategoriesTsvHeader header = TsvImportationUtils.parseTsvHeaderToImportCategories(line, exceptionItems);

            // Categories
            if (CollectionUtils.isEmpty(exceptionItems)) {

                // Retrieve actual categories in category scheme
                categtoriesToPersist = new ArrayList<CategoryMetamac>(); // save in order is required (first parent and then children)
                categoriesToPersistByCode = new HashMap<String, Item>();
                categoriesPreviousInCategorySchemeByCode = new HashMap<String, Item>();
                for (Item item : categorySchemeVersion.getItems()) {
                    categoriesPreviousInCategorySchemeByCode.put(item.getNameableArtefact().getCodeFull(), item);
                }

                boolean isDefaultMaintainer = StringUtils.equals(categorySchemeVersion.getMaintainableArtefact().getMaintainer().getNameableArtefact().getUrn(),
                        srmConfiguration.retrieveMaintainerUrnDefault());
                boolean isTemporalVersion = VersionUtil.isTemporalVersion(categorySchemeVersion.getMaintainableArtefact().getVersionLogic());
                if (!isDefaultMaintainer || isTemporalVersion) {
                    updateAlreadyExisting = true;
                }

                LinkedHashMap<String, EniSchemaCatalog> eniSchemeMap = (LinkedHashMap<String, EniSchemaCatalog>) UtilsMap.getMapFromEniScheme(eniSchemaCatalogRepository.findAll());

                // store categoryElement found in each iteration for more efficient importation.
                Map<String, CategoryElement> categoryElementsByCode = new HashMap<String, CategoryElement>();

                int lineNumber = 2;
                while ((line = bufferedReader.readLine()) != null) {
                    if (StringUtils.isBlank(line)) {
                        continue;
                    }
                    String[] columns = StringUtils.splitPreserveAllTokens(TsvImportationUtils.replaceSeparatorLineInText(line), SrmConstants.TSV_SEPARATOR);
                    if (columns.length != header.getColumnsSize()) {
                        exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber));
                        continue;
                    }
                    // Transform category and add to list to persist
                    CategoryMetamac category = TsvImportationUtils.tsvLineToCategory(header, columns, lineNumber, categorySchemeVersion, isDefaultMaintainer, isTemporalVersion, updateAlreadyExisting,
                            categoriesPreviousInCategorySchemeByCode, categoriesToPersistByCode, exceptionItems, informationItems);
                    if (category != null) {
                        tsvLineToItemCategoryCategoryElement(category, categoryElementsByCode, header, columns, informationItems);
                        TsvImportationUtils.tsvLineToItemCategoryOwnFields(category, eniSchemeMap, header, columns, exceptionItems);
                        categtoriesToPersist.add(category);
                        categoriesToPersistByCode.put(category.getNameableArtefact().getCodeFull(), category);
                    }
                    lineNumber++;
                }
            }
        } catch (Exception e) {
            logger.error("Error importing tsv file", e);
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_FILE_PARSING, e.getMessage()));
        } finally {
            IOUtils.closeQuietly(inputStreamReader);
            IOUtils.closeQuietly(bufferedReader);
        }
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            // rollback and inform about errors
            throw MetamacExceptionBuilder.builder().withPrincipalException(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR, fileName)).withExceptionItems(exceptionItems).build();
        }

        // Recalculate order visualisations: pre-existing items + newly imported items
        List<Category> allItemsToUpdate = new ArrayList<Category>(categorySchemeVersion.getItems());
        if (categtoriesToPersist != null) {
            for (CategoryMetamac category : categtoriesToPersist) {
                if (!categoriesPreviousInCategorySchemeByCode.containsKey(category.getNameableArtefact().getCodeFull())) {
                    allItemsToUpdate.add(category);
                }
            }
        }
        recalculateCategoriesVisualisations(ctx, allItemsToUpdate, categorySchemeVersion.getOrderVisualisations(), false);

        // Save categories and scheme
        saveCategoriesEfficiently(categtoriesToPersist, categoriesToPersistByCode);
        baseService.updateItemSchemeLastUpdated(ctx, categorySchemeVersion);
        categorySchemeVersion.getItemScheme().setIsTaskInBackground(Boolean.FALSE);
        itemSchemeRepository.save(categorySchemeVersion.getItemScheme());

        return new TaskImportationInfo(Boolean.FALSE, informationItems);
    }

    public void tsvLineToItemCategoryCategoryElement(CategoryMetamac category, Map<String, CategoryElement> categoryElementsByCode, ImportationCategoriesTsvHeader header, String[] columns,
            List<MetamacExceptionItem> infoItems) {
        // category element field
        String categoryElementCodeToImport = TsvImportationUtils.getCategoryElementCode(header, columns);
        if (!StringUtils.isBlank(categoryElementCodeToImport)) {
            CategoryElement categoryElement = retrieveCategoryElementImportedByCode(categoryElementsByCode, categoryElementCodeToImport);
            if (categoryElement == null) {
                // do not abort importation. Only inform about this
                String categoryIdentifier = columns[header.getCodePosition()];
                infoItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_CATEGORY_ELEMENT_NOT_FOUND, categoryElementCodeToImport, categoryIdentifier));
            } else {
                category.setCategoryElement(categoryElement);
                categoryElementsByCode.put(categoryElementCodeToImport, categoryElement);
            }
        } else {
            category.setCategoryElement(null);
        }
    }

    public CategoryElement retrieveCategoryElementImportedByCode(Map<String, CategoryElement> categoryElementsByCode, String categoryElementCode) {
        // category element field

        if (categoryElementsByCode.containsKey(categoryElementCode)) {
            return categoryElementsByCode.get(categoryElementCode);
        } else {
            return getCategoryElementRepository().findByCode(categoryElementCode);
        }
    }

    @Override
    public String exportCategoriesTsv(ServiceContext ctx, String categorySchemetUrn) throws MetamacException {

        CategoriesMetamacInvocationValidator.checkExportCategoriesTsv(categorySchemetUrn, null);

        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemetUrn);

        CategorySchemeOrderVisualisation orderVisualisationToRetrieveCategories = categorySchemeVersion.getDefaultOrderVisualisation();
        if (orderVisualisationToRetrieveCategories == null) {
            orderVisualisationToRetrieveCategories = categorySchemeVersion.getOrderVisualisations().get(0); // any
        }
        Integer orderColumnIndex = orderVisualisationToRetrieveCategories.getColumnIndex();

        List<ItemResult> items = getCategoryMetamacRepository().findCategoriesByCategorySchemeOrderedInDepth(categorySchemeVersion.getId(), orderColumnIndex, CategoryMetamacResultSelection.EXPORT);

        List<String> languages = srmConfiguration.retrieveLanguages();

        return TsvExportationUtils.exportCategories(items, languages);
    }

    @Override
    public List<CategoryMetamacVisualisationResult> retrieveCategoriesByCategorySchemeUrn(ServiceContext ctx, String categorySchemeUrn, String locale, String categorySchemeOrderVisualisationUrn)
            throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkRetrieveCategoriesByCategorySchemeUrn(categorySchemeUrn, locale, null);

        // Retrieve
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);

        if (categorySchemeOrderVisualisationUrn == null) {
            categorySchemeOrderVisualisationUrn = categorySchemeVersion.getDefaultOrderVisualisation() != null
                    ? categorySchemeVersion.getDefaultOrderVisualisation().getNameableArtefact().getUrn()
                    : null;
        }
        Integer orderColumnIndex = null;
        if (categorySchemeOrderVisualisationUrn != null) {
            CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = retrieveCategorySchemeOrderVisualisationByUrn(categorySchemeOrderVisualisationUrn);
            orderColumnIndex = categorySchemeOrderVisualisation.getColumnIndex();
        }

        return getCategoryMetamacRepository().findCategoriesByCategorySchemeUnorderedToVisualisation(categorySchemeVersion.getId(), locale, orderColumnIndex);
    }

    @Override
    public List<ItemResult> retrieveCategoriesByCategorySchemeUrnUnordered(ServiceContext ctx, String categorySchemeUrn, ItemResultSelection itemResultSelection) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkRetrieveCategoriesByCategorySchemeUrnUnordered(categorySchemeUrn, itemResultSelection, null);

        if (itemResultSelection == null) {
            itemResultSelection = ItemResultSelection.RETRIEVE; // default
        }
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);
        return getCategoryMetamacRepository().findCategoriesByCategorySchemeUnordered(categorySchemeVersion.getId(), (ItemMetamacResultSelection) itemResultSelection);
    }

    @Override
    public CategorySchemeVersionMetamac retrieveCategorySchemeByCategoryUrn(ServiceContext ctx, String categoryUrn) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkRetrieveCategorySchemeByCategoryUrn(categoryUrn, null);

        // Retrieve
        CategorySchemeVersionMetamac categorySchemeVersion = getCategorySchemeVersionMetamacRepository().findByCategory(categoryUrn);
        if (categorySchemeVersion == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(categoryUrn).build();
        }
        return categorySchemeVersion;
    }

    @Override
    public void checkCategorySchemeVersionTranslations(ServiceContext ctx, Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByResourceUrn)
            throws MetamacException {
        getCategorySchemeVersionMetamacRepository().checkCategorySchemeVersionTranslations(itemSchemeVersionId, locale, exceptionItemsByResourceUrn);
        getCategorySchemeOrderVisualisationRepository().checkCategorySchemeOrderVisualisationTranslations(itemSchemeVersionId, locale, exceptionItemsByResourceUrn);
        getCategoryMetamacRepository().checkCategoryTranslations(itemSchemeVersionId, locale, exceptionItemsByResourceUrn);
    }

    @Override
    public void checkCategorySchemeWithRelatedResourcesExternallyPublished(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {
        String itemSchemeVersionUrn = categorySchemeVersion.getMaintainableArtefact().getUrn();
        Map<String, MetamacExceptionItem> exceptionItemsByUrn = new HashMap<String, MetamacExceptionItem>();
        checkCategorisationsWithRelatedResourcesExternallyPublished(ctx, itemSchemeVersionUrn, exceptionItemsByUrn);
        ExceptionUtils.throwIfException(new ArrayList<MetamacExceptionItem>(exceptionItemsByUrn.values()));
    }

    @Override
    public Categorisation createCategorisation(ServiceContext ctx, String categoryUrn, String artefactCategorisedUrn, String maintainerUrn) throws MetamacException {

        preCreateCategorisation(ctx, categoryUrn, artefactCategorisedUrn, maintainerUrn);
        srmValidation.checkMaintainerIsDefault(ctx, maintainerUrn);

        // Create
        Categorisation categorisation = categoriesService.createCategorisation(ctx, categoryUrn, artefactCategorisedUrn, maintainerUrn, SrmConstants.VERSION_PATTERN_METAMAC);
        categorisation = postCreateCategorisation(ctx, artefactCategorisedUrn, categorisation);
        return categorisation;
    }

    @Override
    public void preCreateCategorisation(ServiceContext ctx, String categoryUrn, String artefactCategorisedUrn, String maintainerUrn) throws MetamacException {
        // Validation
        CategoriesInvocationValidator.checkCreateCategorisation(categoryUrn, artefactCategorisedUrn, maintainerUrn, null);
        checkCategorisationToCreateOrUpdate(ctx, categoryUrn, artefactCategorisedUrn, maintainerUrn);
    }

    @Override
    public PagedResult<CategoryMetamac> findCategoriesCanBeCategorisationCategoryByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CategoryMetamac.class).distinctRoot().build();
        }
        // Category scheme internally or externally published
        ConditionalCriteria publishedCondition = ConditionalCriteriaBuilder.criteriaFor(CategoryMetamac.class)
                .withProperty(CategoryMetamacProperties.itemSchemeVersion().maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE).buildSingle();
        conditions.add(publishedCondition);

        return findCategoriesByCondition(ctx, conditions, pagingParameter);
    }

    @Override
    public Categorisation postCreateCategorisation(ServiceContext ctx, String artefactCategorisedUrn, Categorisation categorisation) throws MetamacException {
        // Automatically publication
        // Note: we can find artefact as maintainable instead of identifiable because in Metamac all artefacts with categorisation inherit to maintainable
        MaintainableArtefact artefact = maintainableArtefactRepository.findByUrn(artefactCategorisedUrn);

        if (artefact.getFinalLogicClient()) {
            categorisation = markCategorisationAsFinal(ctx, categorisation.getMaintainableArtefact().getUrn());
        } else {
            categorisation.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
            categorisationRepository.save(categorisation);
        }
        if (artefact.getPublicLogic()) {
            categorisation = markCategorisationAsPublic(ctx, categorisation.getMaintainableArtefact().getUrn());
        }
        if (artefact.getValidFrom() != null) {
            categorisation = startCategorisationValidity(ctx, categorisation.getMaintainableArtefact().getUrn(), null);
        }
        return categorisation;
    }

    @Override
    public void deleteCategorisation(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        Categorisation categorisation = retrieveCategorisationByUrn(ctx, urn);
        srmValidation.checkMaintainerIsDefault(ctx, categorisation.getMaintainableArtefact().getMaintainer().getNameableArtefact().getUrn());

        // Delete
        categoriesService.deleteCategorisation(ctx, urn);
    }

    /**
     * Important: Categorisations must be created with new code
     */
    @Override
    public void createVersionFromTemporalCategorisations(ServiceContext ctx, MaintainableArtefact maintainableArtefact) throws MetamacException {
        List<Categorisation> categorisationsToDelete = new ArrayList<Categorisation>(maintainableArtefact.getCategorisations());
        if (!CollectionUtils.isEmpty(categorisationsToDelete)) {
            for (Categorisation categorisationToDelete : categorisationsToDelete) {
                maintainableArtefact.removeCategorisation(categorisationToDelete);
            }
            String maintainerUrn = maintainableArtefact.getMaintainer().getNameableArtefact().getUrn();
            String artefactCategorisedUrn = maintainableArtefact.getUrn();
            for (Categorisation categorisationToDelete : categorisationsToDelete) {
                String categoryUrn = categorisationToDelete.getCategory().getNameableArtefact().getUrn();
                Categorisation categorisation = categoriesService.createCategorisation(ctx, categoryUrn, artefactCategorisedUrn, maintainerUrn, SrmConstants.VERSION_PATTERN_METAMAC);
                maintainableArtefact.getCategorisations().add(categorisation);
            }
        }
    }

    @Override
    public Categorisation markCategorisationAsFinal(ServiceContext ctx, String urn) throws MetamacException {
        Categorisation categorisation = retrieveCategorisationByUrn(ctx, urn);
        categorisation.getMaintainableArtefact().setFinalLogicClient(Boolean.TRUE);
        return categoriesService.markCategorisationAsFinal(ctx, urn);
    }

    @Override
    public Categorisation markCategorisationAsPublic(ServiceContext ctx, String urn) throws MetamacException {
        return categoriesService.markCategorisationAsPublic(ctx, urn);
    }

    @Override
    public Categorisation startCategorisationValidity(ServiceContext ctx, String urn, DateTime validFrom) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkEndValidity(urn, null);
        Categorisation categorisation = retrieveCategorisationByUrn(ctx, urn);
        srmValidation.checkModifyCategorisationValidity(ctx, categorisation.getMaintainableArtefact());

        return categoriesService.startCategorisationValidity(ctx, urn, validFrom);
    }

    @Override
    public Categorisation endCategorisationValidity(ServiceContext ctx, String urn, DateTime validTo) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkEndValidity(urn, null);
        Categorisation categorisation = retrieveCategorisationByUrn(ctx, urn);
        srmValidation.checkModifyCategorisationValidity(ctx, categorisation.getMaintainableArtefact());

        return categoriesService.endCategorisationValidity(ctx, urn, validTo);
    }

    @Override
    public Categorisation retrieveCategorisationByUrn(ServiceContext ctx, String urn) throws MetamacException {
        return categoriesService.retrieveCategorisationByUrn(ctx, urn);
    }

    @Override
    public List<Categorisation> retrieveCategorisationsByArtefact(ServiceContext ctx, String urn) throws MetamacException {
        return categoriesService.retrieveCategorisationsByArtefact(ctx, urn);
    }

    @Override
    public PagedResult<Categorisation> findCategorisationsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        PagedResult<Categorisation> pagedResults = categoriesService.findCategorisationsByCondition(ctx, conditions, pagingParameter);
        return pagedResults;
    }

    @Override
    public void checkCategorisationsWithRelatedResourcesExternallyPublished(ServiceContext ctx, String artefactCategorisedUrn, Map<String, MetamacExceptionItem> exceptionItemsByUrn)
            throws MetamacException {
        List<Categorisation> categorisations = retrieveCategorisationsByArtefact(ctx, artefactCategorisedUrn);
        for (Categorisation categorisation : categorisations) {
            Category category = categorisation.getCategory();
            if (!category.getItemSchemeVersion().getMaintainableArtefact().getPublicLogic()) {
                addExceptionToExceptionItemsByResource(exceptionItemsByUrn, ServiceExceptionType.CATEGORY_NOT_EXTERNALLY_PUBLISHED, category.getNameableArtefact().getUrn());
            }
            Organisation maintainer = categorisation.getMaintainableArtefact().getMaintainer();
            if (!maintainer.getItemSchemeVersion().getMaintainableArtefact().getPublicLogic()) {
                addExceptionToExceptionItemsByResource(exceptionItemsByUrn, ServiceExceptionType.ORGANISATION_SCHEME_NOT_EXTERNALLY_PUBLISHED, maintainer.getNameableArtefact().getUrn());
            }
        }
    }

    // ------------------------------------------------------------------------------------
    // CATEGORY ELEMENTS
    // ------------------------------------------------------------------------------------

    @Override
    public String exportCategoryElementsTsv(ServiceContext ctx, List<String> categoryElementUrns) throws MetamacException {

        CategoriesMetamacInvocationValidator.checkExportCategoryElementsTsv(categoryElementUrns, null);
        List<CategoryElement> elementsToExport = new ArrayList<CategoryElement>();
        if (categoryElementUrns.isEmpty()) {
            elementsToExport = getCategoryElementRepository().findAll();
        } else {
            elementsToExport = getCategoryElementRepository().findByUrnIn(categoryElementUrns);
        }

        List<String> languages = srmConfiguration.retrieveLanguages();
        return TsvExportationUtils.exportCategoryElements(elementsToExport, languages);
    }

    /**
     * Typecast to Metamac type
     */
    private List<CategoryMetamac> categoriesToCategoryMetamac(List<Category> sources) {
        List<CategoryMetamac> targets = new ArrayList<CategoryMetamac>(sources.size());
        for (Item source : sources) {
            targets.add((CategoryMetamac) source);
        }
        return targets;
    }

    /**
     * Typecast to Metamac type
     */
    private List<CategorySchemeVersionMetamac> categorySchemeVersionsToCategorySchemeVersionsMetamac(List<CategorySchemeVersion> sources) {
        List<CategorySchemeVersionMetamac> targets = new ArrayList<CategorySchemeVersionMetamac>(sources.size());
        for (ItemSchemeVersion source : sources) {
            targets.add((CategorySchemeVersionMetamac) source);
        }
        return targets;
    }

    /**
     * Typecast to Metamac type
     */
    private PagedResult<CategorySchemeVersionMetamac> pagedResultCategorySchemeVersionToMetamac(PagedResult<CategorySchemeVersion> source) {
        List<CategorySchemeVersionMetamac> categorySchemeVersionsMetamac = categorySchemeVersionsToCategorySchemeVersionsMetamac(source.getValues());
        return new PagedResult<CategorySchemeVersionMetamac>(categorySchemeVersionsMetamac, source.getStartRow(), source.getRowCount(), source.getPageSize(), source.getTotalRows(),
                source.getAdditionalResultRows());
    }

    /**
     * Typecast to Metamac type
     */
    private PagedResult<CategoryMetamac> pagedResultCategoryToMetamac(PagedResult<Category> source) {
        List<CategoryMetamac> categoriesMetamac = categoriesToCategoryMetamac(source.getValues());
        return new PagedResult<CategoryMetamac>(categoriesMetamac, source.getStartRow(), source.getRowCount(), source.getPageSize(), source.getTotalRows(), source.getAdditionalResultRows());
    }

    private void checkCategorisationCategory(ServiceContext ctx, String categoryUrn) throws MetamacException {
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        Category category = retrieveCategoryByUrn(ctx, categoryUrn);
        Long categoryId = category.getId();
        List<ConditionalCriteria> criteriaToVerifyCategory = ConditionalCriteriaBuilder.criteriaFor(CategoryMetamac.class).withProperty(CategoryMetamacProperties.id()).eq(categoryId).build();
        PagedResult<CategoryMetamac> result = findCategoriesCanBeCategorisationCategoryByCondition(ctx, criteriaToVerifyCategory, pagingParameter);
        if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(categoryId)) {
            throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CATEGORY);
        }
    }

    private void checkCategorisationMaintainer(ServiceContext ctx, String maintainerUrn) throws MetamacException {
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        Organisation organisation = organisationsService.retrieveOrganisationByUrn(ctx, maintainerUrn);
        Long organisationId = organisation.getId();
        List<ConditionalCriteria> criteriaToVerifyMaintainer = ConditionalCriteriaBuilder.criteriaFor(OrganisationMetamac.class).withProperty(OrganisationMetamacProperties.id()).eq(organisationId)
                .build();
        PagedResult<OrganisationMetamac> result = findOrganisationsCanBeCategorisationMaintainerByCondition(ctx, criteriaToVerifyMaintainer, pagingParameter);
        if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(organisationId)) {
            throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.MAINTAINER);
        }
    }

    private PagedResult<OrganisationMetamac> findOrganisationsCanBeCategorisationMaintainerByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(OrganisationMetamac.class).distinctRoot().build();
        }
        // Organisation scheme internally or externally published
        ConditionalCriteria publishedCondition = ConditionalCriteriaBuilder.criteriaFor(OrganisationMetamac.class)
                .withProperty(OrganisationMetamacProperties.itemSchemeVersion().maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE).buildSingle();
        conditions.add(publishedCondition);

        return organisationsService.findOrganisationsByCondition(ctx, conditions, pagingParameter);
    }

    /**
     * Common validations to create or update a category scheme
     */
    private void checkCategorySchemeToCreateOrUpdate(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {

        if (categorySchemeVersion.getId() != null) {
            // Proc status
            checkCategorySchemeCanBeModified(categorySchemeVersion);

            // Code
            SrmValidationUtils.checkMaintainableArtefactCanChangeCodeIfChanged(categorySchemeVersion.getMaintainableArtefact());
        }

        // Maintainer
        srmValidation.checkMaintainer(ctx, categorySchemeVersion.getMaintainableArtefact(), categorySchemeVersion.getMaintainableArtefact().getIsImported());
    }

    /**
     * Common validations to create or update a category
     */
    private void checkCategoryToCreateOrUpdate(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion, CategoryMetamac category) throws MetamacException {
        checkCategorySchemeCanBeModified(categorySchemeVersion);
    }

    /**
     * Common validations to create or update a categorisation
     */
    private void checkCategorisationToCreateOrUpdate(ServiceContext ctx, String categoryUrn, String artefactCategorisedUrn, String maintainerUrn) throws MetamacException {
        // Category
        checkCategorisationCategory(ctx, categoryUrn);
        // Maintainer
        checkCategorisationMaintainer(ctx, maintainerUrn);
    }

    private TaskInfo createVersionOfCategoryScheme(ServiceContext ctx, String urnToCopy, ItemSchemesCopyCallback itemSchemesCopyCallback, VersionTypeEnum versionType, boolean isTemporal)
            throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkVersioningCategoryScheme(urnToCopy, versionType, isTemporal, null, null);
        checkCategorySchemeToVersioning(ctx, urnToCopy, isTemporal);
        // Versioning
        return categoriesService.versioningCategoryScheme(ctx, urnToCopy, versionType, isTemporal, itemSchemesCopyCallback);
    }

    private void checkCategorySchemeToVersioning(ServiceContext ctx, String urnToCopy, boolean isTemporal) throws MetamacException {

        CategorySchemeVersionMetamac categorySchemeVersionToCopy = retrieveCategorySchemeByUrn(ctx, urnToCopy);
        // Check version to copy is published
        SrmValidationUtils.checkArtefactCanBeVersioned(categorySchemeVersionToCopy.getMaintainableArtefact(), categorySchemeVersionToCopy.getLifeCycleMetadata(), isTemporal);
        // Check does not exist any version 'no final'
        ItemSchemeVersion categorySchemeVersionNoFinal = itemSchemeVersionRepository.findItemSchemeVersionNoFinalClient(categorySchemeVersionToCopy.getItemScheme().getId());
        if (categorySchemeVersionNoFinal != null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED_VERSION_NOT_PUBLISHED)
                    .withMessageParameters(categorySchemeVersionNoFinal.getMaintainableArtefact().getUrn()).build();
        }
    }

    private void checkCategorySchemeCanBeModified(CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {
        SrmValidationUtils.checkArtefactCanBeModified(categorySchemeVersion.getLifeCycleMetadata(), categorySchemeVersion.getMaintainableArtefact().getUrn());
        SrmValidationUtils.checkArtefactWithoutTaskInBackground(categorySchemeVersion);
    }

    private void checkCategoryIsNotChildren(ServiceContext ctx, Item item, String newParentUrn) throws MetamacException {
        Item parentTarget = retrieveCategoryByUrn(ctx, newParentUrn);
        while (parentTarget != null) {
            if (parentTarget.getId().equals(item.getId())) {
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.ITEM_PARENT);
            }
            parentTarget = parentTarget.getParent();
        }
    }

    private CategorySchemeVersionMetamac retrieveCategorySchemeVersionByUrn(String urn) throws MetamacException {
        CategoriesInvocationValidator.checkRetrieveByUrn(urn);
        CategorySchemeVersionMetamac categorySchemeVersion = getCategorySchemeVersionMetamacRepository().findByUrn(urn);
        if (categorySchemeVersion == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return categorySchemeVersion;
    }

    private void saveCategoriesEfficiently(Collection<CategoryMetamac> categoriesToPersist, Map<String, Item> categoriesToPersistByCodeFull) {
        for (CategoryMetamac categoryMetamac : categoriesToPersist) {
            if (categoryMetamac.getParent() != null) {
                if (categoriesToPersistByCodeFull.containsKey(categoryMetamac.getParent().getNameableArtefact().getCodeFull())) {
                    // update reference because it was saved
                    categoryMetamac.setParent(categoriesToPersistByCodeFull.get(categoryMetamac.getParent().getNameableArtefact().getCodeFull()));
                }
            }
            categoryMetamac = getCategoryMetamacRepository().save(categoryMetamac);
            // update reference after save to assign to children
            categoriesToPersistByCodeFull.put(categoryMetamac.getNameableArtefact().getCodeFull(), categoryMetamac);
        }
    }

    public void deleteCategoryElement(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkDeleteArtefact(urn);
        CategoryElement categoryElementToDelete = retrieveCategoryElementByUrn(urn);
        checkCategoryElementCanBeDeleted(categoryElementToDelete);

        // Delete
        getCategoryElementRepository().delete(categoryElementToDelete);
    }

    private void checkCategoryElementCanBeDeleted(CategoryElement categoryElementToDelete) throws MetamacException {
        String categoryElementToDeleteUrn = categoryElementToDelete.getIdentifiableArtefact().getUrn();

        if (CollectionUtils.isNotEmpty(categoryElementToDelete.getCodes())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CATEGORY_ELEMENT_WITH_RELATIONS)
                    .withMessageParameters(categoryElementToDeleteUrn, categoryElementToDelete.getCodes().get(0).getNameableArtefact().getUrn()).build(); // say one
        }

    }

    public CategoryElement createCategoryElement(ServiceContext ctx, CategoryElement categoryElement) throws MetamacException {
        // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // IMPORTANT: If any restriction change, review importation of tsv file, because the importation doesnot call to service methods to avoid extra queries, flushing...
        // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Validation
        CategoriesMetamacInvocationValidator.checkCreateCategoryElement(categoryElement, null);
        checCategoryElementToCreateOrUpdate(ctx, categoryElement);

        // Create
        setCategoryElementUrnUnique(categoryElement, Boolean.TRUE);
        setIsVisible(categoryElement);
        categoryElement = getCategoryElementRepository().save(categoryElement);
        return categoryElement;
    }

    private void setIsVisible(CategoryElement categoryElement) {
        if (categoryElement.getIsVisible() == null) {
            categoryElement.setIsVisible(false);
        }
    }

    public CategoryElement updateCategoryElement(ServiceContext ctx, CategoryElement categoryElement) throws MetamacException {
        // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        // IMPORTANT: If any restriction change, review importation of tsv file, because the importation doesnot call to service methods to avoid extra queries, flushing...
        // ------------------------------------------------------------------------------------------------------------------------------------------------------------------------

        // Validation
        CategoriesMetamacInvocationValidator.checkUpdateCategoryElement(categoryElement, null);
        checCategoryElementToCreateOrUpdate(ctx, categoryElement);

        if (categoryElement.getIdentifiableArtefact().getIsCodeUpdated()) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_UNMODIFIABLE).withMessageParameters(ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE).build();
        }

        setIsVisible(categoryElement);
        categoryElement.setUpdateDate(new DateTime()); // Optimistic locking: Update "update date" attribute to force update to root entity, to increase attribute "version"

        // Update
        return getCategoryElementRepository().save(categoryElement);
    }

    public CategoryElement retrieveCategoryElementByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        CategoryElement categoryElement = retrieveCategoryElementByUrn(urn);
        return categoryElement;
    }

    public PagedResult<CategoryElement> findCategoryElementsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkFindByCondition(conditions, pagingParameter);

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CategoryElement.class).distinctRoot().build();
        }
        PagedResult<CategoryElement> categoryElementPagedResult = getCategoryElementRepository().findByCondition(conditions, pagingParameter);
        return categoryElementPagedResult;
    }

    private CategoryElement retrieveCategoryElementByUrn(String urn) throws MetamacException {
        CategoryElement categoryElement = getCategoryElementRepository().findByUrn(urn);
        if (categoryElement == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return categoryElement;
    }

    /**
     * Common validations to create or update a variable element
     */
    private void checCategoryElementToCreateOrUpdate(ServiceContext ctx, CategoryElement categoryElement) throws MetamacException {

        // Check translations
        String locale = srmConfiguration.retrieveLanguageDefault();
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        SrmServiceUtils.checkInternationalStringTranslationsWithoutSql(categoryElement.getShortName(), ServiceExceptionParameters.RESOURCE_ELEMENT_SHORT_NAME, locale, exceptionItems);
        if (exceptionItems.size() != 0) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(exceptionItems).build();
        }
    }

    /**
     * Generate urn, check it is unique and set to category element. Set also urnProvider
     */
    private void setCategoryElementUrnUnique(CategoryElement categoryElement, boolean checkUrnUnique) throws MetamacException {
        String urn = GeneratorUrnUtils.generateCategoryElementUrn(categoryElement);
        if (checkUrnUnique) {
            // In import cases this verification isn't performed for performance. In BBDD this constraint is checked
            identifiableArtefactRepository.checkUrnUnique(urn, categoryElement.getIdentifiableArtefact().getId());
        }

        categoryElement.getIdentifiableArtefact().setUrn(urn);
        categoryElement.getIdentifiableArtefact().setUrnProvider(urn);
    }

    @Override
    public TaskImportationInfo importCategoryElementsTsv(ServiceContext ctx, File file, String fileName, boolean updateAlreadyExisting, Boolean canBeBackground) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkImportCategoryElementsTsv(file, fileName, updateAlreadyExisting, canBeBackground, null);

        // Decide if task must be executed in background
        boolean executeInBackground = SrmServiceUtils.taskMustBeBackground(file, canBeBackground, SrmConstants.NUM_BYTES_TO_PLANNIFY_TSV_RESOURCE_ELEMENTS_IMPORTATION);

        // Plannify task if background
        if (executeInBackground) {
            String jobKey = tasksMetamacService.plannifyImportCategoryElementsTsvInBackground(ctx, file, fileName, updateAlreadyExisting);
            return new TaskImportationInfo(Boolean.TRUE, jobKey);
        }

        List<MetamacExceptionItem> exceptionItems = new ArrayList<>();
        List<MetamacExceptionItem> informationItems = new ArrayList<>();
        Map<String, CategoryElement> categoryElementsToPersistByCode = doImportCategoryElementTsv(file, updateAlreadyExisting, exceptionItems, informationItems);
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            throw MetamacExceptionBuilder.builder().withPrincipalException(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR, fileName)).withExceptionItems(exceptionItems).build();
        }
        // Persist
        for (CategoryElement categoryElement : categoryElementsToPersistByCode.values()) {
            getCategoryElementRepository().save(categoryElement);
        }

        return new TaskImportationInfo(Boolean.FALSE, informationItems);
    }

    private Map<String, CategoryElement> doImportCategoryElementTsv(File file, boolean updateAlreadyExisting, List<MetamacExceptionItem> exceptionItems, List<MetamacExceptionItem> informationItems) {
        // Import

        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        Map<String, CategoryElement> categoryElementsToPersistByCode = null;
        try {
            InputStream stream = new FileInputStream(file);
            String charset = FileUtils.guessCharset(file);
            inputStreamReader = new InputStreamReader(stream, charset);
            bufferedReader = new BufferedReader(inputStreamReader);

            // Header
            String line = bufferedReader.readLine();
            ImportationCategoryElementsTsvHeader header = TsvImportationUtils.parseTsvHeaderToImportCategoryElements(line, exceptionItems);

            // Category elements
            if (CollectionUtils.isEmpty(exceptionItems)) {
                categoryElementsToPersistByCode = new HashMap<String, CategoryElement>();

                List<String> fileLines = new ArrayList<>();
                int lineNumber = 2;

                line = bufferedReader.readLine();
                boolean allFileHasBeenProcessed = line == null;

                while (!allFileHasBeenProcessed) {
                    if (line != null) {
                        fileLines.add(line);
                    }

                    doImportLineCategoryElementTsv(updateAlreadyExisting, lineNumber, header, fileLines, line, categoryElementsToPersistByCode, exceptionItems, informationItems);
                    line = bufferedReader.readLine();
                    allFileHasBeenProcessed = line == null && fileLines.isEmpty();
                }
            }
        } catch (Exception e) {
            logger.error("Error importing tsv category elements file", e);
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_FILE_PARSING, e.getMessage()));
        } finally {
            try {
                inputStreamReader.close();
                bufferedReader.close();
            } catch (IOException e) {
                // do not relaunch error
                logger.error("Error closing streams", e);
            }
        }

        return categoryElementsToPersistByCode;
    }

    private void doImportLineCategoryElementTsv(boolean updateAlreadyExisting, int lineNumber, ImportationCategoryElementsTsvHeader header, List<String> fileLines, String line,
            Map<String, CategoryElement> categoryElementsToPersistByCode, List<MetamacExceptionItem> exceptionItems, List<MetamacExceptionItem> informationItems) throws MetamacException {
        if (fileLines.size() >= SrmConstants.RESOURCE_ELEMENT_CACHE_SIZE_TSV_IMPORT || line == null) {
            Map<String, CategoryElement> categoryElementCache = getCategoryElementCache(fileLines, header);

            for (String fileLine : fileLines) {
                if (StringUtils.isNotBlank(fileLine)) {
                    String[] columns = StringUtils.splitPreserveAllTokens(TsvImportationUtils.replaceSeparatorLineInText(fileLine), SrmConstants.TSV_SEPARATOR);
                    if (columns.length != header.getColumnsSize()) {
                        exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber));
                    } else {
                        // Transform category element and add to list to persist
                        CategoryElement categoryElement = tsvLineToCategoryElement(header, columns, lineNumber, categoryElementCache, updateAlreadyExisting, categoryElementsToPersistByCode,
                                exceptionItems, informationItems);
                        if (categoryElement != null) {
                            categoryElementsToPersistByCode.put(categoryElement.getIdentifiableArtefact().getCode(), categoryElement);
                        }
                    }
                }
                lineNumber++;
            }

            fileLines.clear();
        }
    }

    private Map<String, CategoryElement> getCategoryElementCache(List<String> filePartition, ImportationCategoryElementsTsvHeader header) {

        List<String> categoryElementCodes = new ArrayList<>();

        logger.debug("Number of lines from file partition {}", filePartition.size());

        for (String fileLine : filePartition) {
            String[] columns = StringUtils.splitPreserveAllTokens(TsvImportationUtils.replaceSeparatorLineInText(fileLine), SrmConstants.TSV_SEPARATOR);
            if (columns.length == header.getColumnsSize()) {
                String codeIdentifier = columns[header.getCodePosition()];
                if (!StringUtils.isBlank(codeIdentifier) && SemanticIdentifierValidationUtils.isResourceElementSemanticIdentifier(codeIdentifier)) {
                    categoryElementCodes.add(codeIdentifier);
                }
            }
        }

        logger.debug("Number of extracted codes from file partition: {}", categoryElementCodes.size());
        logger.trace("Extracted codes from file partition: {}", categoryElementCodes);

        List<CategoryElement> categoryElements = getCategoryElementRepository().findByCodeIn(categoryElementCodes);

        Map<String, CategoryElement> categoryElementsCache = new HashMap<>();
        for (CategoryElement categoryElement : categoryElements) {
            categoryElementsCache.put(categoryElement.getIdentifiableArtefact().getCode(), categoryElement);
        }

        logger.debug("Number of codes in cache: {}", categoryElementsCache.size());
        logger.trace("Codes in cache: {}", categoryElementsCache.keySet());

        return categoryElementsCache;
    }

    /**
     * Transforms tsv line to CategoryElement. IMPORTANT: Do not execute save or update operation
     */
    private CategoryElement tsvLineToCategoryElement(ImportationCategoryElementsTsvHeader header, String[] columns, int lineNumber, Map<String, CategoryElement> categoryElementCache,
            boolean updateAlreadyExisting, Map<String, CategoryElement> categoryElementsToPersistByCode, List<MetamacExceptionItem> exceptionItems, List<MetamacExceptionItem> infoItems)
            throws MetamacException {

        // semantic identifier
        String codeIdentifier = columns[header.getCodePosition()];
        if (StringUtils.isBlank(codeIdentifier)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber));
            return null; // do not add more errors in this line
        }
        if (categoryElementsToPersistByCode.containsKey(codeIdentifier)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_RESOURCE_DUPLICATED, codeIdentifier, lineNumber));
            return null;
        }
        if (!SemanticIdentifierValidationUtils.isResourceElementSemanticIdentifier(codeIdentifier)) {
            exceptionItems
                    .add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_INCORRECT_SEMANTIC_IDENTIFIER, codeIdentifier, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE));
            return null;
        }
        // init variable element
        CategoryElement categoryElement = categoryElementCache.get(codeIdentifier);
        if (categoryElement == null) {
            categoryElement = new CategoryElement();
            categoryElement.setIdentifiableArtefact(new IdentifiableArtefact());
            categoryElement.getIdentifiableArtefact().setCode(codeIdentifier);
            setCategoryElementUrnUnique(categoryElement, Boolean.FALSE);
        } else {
            if (!updateAlreadyExisting) {
                infoItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_NOT_UPDATED, categoryElement.getIdentifiableArtefact().getCode()));
                return null;
            } else {
                categoryElement.getIdentifiableArtefact().setIsCodeUpdated(Boolean.FALSE);
                categoryElement.setUpdateDate(new DateTime());
                infoItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_UPDATED, categoryElement.getIdentifiableArtefact().getCode()));
            }
        }
        // short name
        categoryElement.setShortName(TsvImportationUtils.tsvLineToInternationalString(header.getShortName(), columns, categoryElement.getShortName()));

        if (categoryElement.getShortName() == null) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_REQUIRED, categoryElement.getIdentifiableArtefact().getCode(),
                    ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_SHORT_NAME));
        }

        fillCategoryElementIsVisibleField(header, columns, categoryElement);

        // Do not persist if any error ocurrs
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            return null;
        }

        try {
            // extra validation to avoid save an incorrect variable element
            if (categoryElement.getId() == null) {
                CategoriesMetamacInvocationValidator.checkCreateCategoryElement(categoryElement, null);
            } else {
                CategoriesMetamacInvocationValidator.checkUpdateCategoryElement(categoryElement, null);
            }
        } catch (MetamacException metamacException) {
            logger.error("Error importing category element from tsv file", metamacException);
            MetamacExceptionItem metamacExceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
            metamacExceptionItem.setExceptionItems(metamacException.getExceptionItems());
            exceptionItems.add(metamacExceptionItem);
        }
        return categoryElement;
    }

    private void fillCategoryElementIsVisibleField(ImportationCategoryElementsTsvHeader header, String[] columns, CategoryElement categoryElement) {
        String isVisible = columns[header.getIsVisiblePosition()];
        if (!StringUtils.isBlank(isVisible)) {
            categoryElement.setIsVisible(Boolean.valueOf(isVisible));
        } else {
            categoryElement.setIsVisible(false);
        }
    }

    @Override
    public CategoryMetamac updateCategoryElementOfCategory(ServiceContext ctx, String categoryUrn, String categoryElementUrn) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkUpdateCategoryElementOfCategory(categoryUrn, categoryElementUrn, null);

        CategoryMetamac category = retrieveCategoryByUrn(ctx, categoryUrn);
        if (categoryElementUrn == null) {
            // Delete
            if (category.getCategoryElement() != null) {
                category.getCategoryElement().removeCode(category);
            }
        } else {
            // Add
            CategoryElement categoryElement = retrieveCategoryElementByUrn(categoryElementUrn);

            if (category.getCategoryElement() != null) {
                category.getCategoryElement().removeCode(category);
            }
            categoryElement.addCode(category);
        }
        baseService.updateItemSchemeLastUpdated(ctx, category.getItemSchemeVersion());
        return getCategoryMetamacRepository().save(category);
    }

    @Override
    public void updateCodesCategoryElements(ServiceContext ctx, String categorySchemeUrn, Map<Long, Long> categoryElementsIdByCodeId) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkUpdateCodesCategoryElements(categorySchemeUrn, categoryElementsIdByCodeId, null);

        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);

        // Update all
        getCategoryMetamacRepository().updateCodesCategoryElements(categoryElementsIdByCodeId, categorySchemeVersion.getId());
        baseService.updateItemSchemeLastUpdated(ctx, categorySchemeVersion);
    }

    @Override
    public List<CodeCategoryElementNormalisationResult> normaliseCategoryElementsToCategories(ServiceContext ctx, String categorySchemeUrn, String locale, boolean proposeOnlyWithoutCategoryElement)
            throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkNormaliseCategoryElementsToCategories(categorySchemeUrn, locale, null);

        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);

        // Find codes and category elements
        List<CategoryMetamacVisualisationResult> codes = retrieveCategoriesByCategorySchemeUrn(ctx, categorySchemeUrn, locale, null);
        List<CategoryElementVisualisationResult> categoryElements = retrieveCategoryElementsAll(locale);
        Map<String, CategoryElementVisualisationResult> categoryElementsByUrn = new HashMap<>(categoryElements.size());
        for (CategoryElementVisualisationResult categoryElement : categoryElements) {
            categoryElementsByUrn.put(categoryElement.getUrn(), categoryElement);
        }

        // Init dictionary with category elements
        Map<String, String> dictionary = new HashMap<>(categoryElements.size());
        for (CategoryElementVisualisationResult categoryElement : categoryElements) {
            if (categoryElement.getShortName() != null) {
                dictionary.put(categoryElement.getUrn(), categoryElement.getShortName());
            }
        }
        DiceLuceneRamAproxStringMatch luceneMatch = new DiceLuceneRamAproxStringMatch(dictionary);

        // Get suggestions or mantain actual category element
        List<CodeCategoryElementNormalisationResult> results = new ArrayList<>(codes.size());
        for (CategoryMetamacVisualisationResult code : codes) {
            CodeCategoryElementNormalisationResult result = new CodeCategoryElementNormalisationResult();
            result.setCode(code);
            if (proposeOnlyWithoutCategoryElement && code.getCategoryElement() != null) {
                // maintain actual
                result.setCategoryElementProposed(code.getCategoryElement());
            } else {
                // propose by lucene
                String name = code.getName();
                if (name != null) {
                    List<MatchResult> suggestedCategoryElements = luceneMatch.getSuggestedWords(name, 1);
                    if (suggestedCategoryElements.size() != 0) {
                        CategoryElementVisualisationResult categoryElement = categoryElementsByUrn.get(suggestedCategoryElements.get(0).getDictionaryKey());
                        result.setCategoryElementProposed(categoryElement);
                    }
                }
            }
            results.add(result);
        }

        luceneMatch.shutdown();
        return results;
    }

    private List<CategoryElementVisualisationResult> retrieveCategoryElementsAll(String locale) throws MetamacException {
        List<CategoryElement> categoryElements = getCategoryElementRepository().findAll();
        return categoryElementToCategoryElementVisualisationResult(locale, categoryElements);
    }

    private List<CategoryElementVisualisationResult> categoryElementToCategoryElementVisualisationResult(String locale, List<CategoryElement> categoryElements) {
        List<CategoryElementVisualisationResult> categoryElementVisualisationResult = new ArrayList<CategoryElementVisualisationResult>();
        for (CategoryElement categoryElement : categoryElements) {
            CategoryElementVisualisationResult categoryElementVisualisation = new CategoryElementVisualisationResult();
            categoryElementVisualisation.setIdDatabase(categoryElement.getId());
            categoryElementVisualisation.setUrn(categoryElement.getIdentifiableArtefact().getUrn());
            categoryElementVisualisation.setCode(categoryElement.getIdentifiableArtefact().getCode());
            categoryElementVisualisation.setShortName(InternationalStringUtils.getLocalisedString(categoryElement.getShortName(), locale));
            categoryElementVisualisationResult.add(categoryElementVisualisation);
        }
        return categoryElementVisualisationResult;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MetamacExceptionItem> resendKafkaMessageExternallyCategorySchemes(ServiceContext ctx) throws MetamacException {
        List<CategorySchemeVersionMetamac> categorySchemes = this.getCategorySchemeVersionMetamacRepository().retrieveCategorySchemesPublishedExternally();
        List<MetamacExceptionItem> exceptionsItems = new ArrayList<MetamacExceptionItem>();

        int totalCount = 0;
        int partialCount = 0;
        logger.info("resend all published last version category schemes kafka messages start at {} affected categories: {} ", new Date(), categorySchemes.size());

        for (CategorySchemeVersionMetamac categoryScheme : categorySchemes) {

            try {
                ResourceVersionPublicationResult<CategorySchemeVersionMetamac> result = (ResourceVersionPublicationResult<CategorySchemeVersionMetamac>) categorySchemeLifeCycle.resendMessage(ctx,
                        categoryScheme);

                totalCount++;
                partialCount++;

                if (partialCount >= 50) {
                    partialCount = 0;
                    logger.info("processed category schemes {} at {}", totalCount, new Date());
                }

                if (!result.isOk()) {
                    logger.info("error sending category schemes {} error status {} ", categoryScheme.getMaintainableArtefact().getUrn(),
                            result.groupExceptions() != null ? result.groupExceptions().getMessage() : "");
                    exceptionsItems.add(new MetamacExceptionItem(ServiceExceptionType.STREAM_MESSAGING_RESEND_CATEGORY_SCHEME_MESSAGE_ERROR, result.groupExceptions()));
                }

            } catch (MetamacException e) {
                totalCount++;
                partialCount++;
                logger.info("error sending category schemes {} ", categoryScheme.getMaintainableArtefact().getUrn());
                exceptionsItems.add(new MetamacExceptionItem(ServiceExceptionType.STREAM_MESSAGING_RESEND_CATEGORY_SCHEME_MESSAGE_ERROR, categoryScheme.getMaintainableArtefact().getUrn()));
            }

        }
        logger.info("resend all published last version category schemes kafka messages end at {} ", new Date());

        return exceptionsItems;

    }

    @Override
    public TaskImportationInfo importCategoryElementLogo(ServiceContext ctx, String categoryElementUrn, String fileName, InputStream imageStream, Boolean canBeBackground) throws MetamacException {

        List<MetamacExceptionItem> informationItems = new ArrayList<>();

        // Validation
        CategoriesMetamacInvocationValidator.checkImportCategoryElementLogo(categoryElementUrn, fileName, informationItems);
        checkAllowedImageExtension(categoryElementUrn, fileName);

        CategoryElement categoryElement = findCategoryElementByUrn(categoryElementUrn);

        try {
            byte[] logoData = IOUtils.toByteArray(imageStream);

            checkMaxImageSize(categoryElementUrn, logoData);

            saveCategoryElementLogo(categoryElement, logoData, fileName);

        } catch (IOException e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IMPORTATION_LOGO_CATEGORY_ELEMENT_ERROR).withMessageParameters(categoryElementUrn).build();
        } finally {
            IOUtils.closeQuietly(imageStream);
        }

        return new TaskImportationInfo(Boolean.FALSE, informationItems);

    }

    private void checkAllowedImageExtension(String categoryElementUrn, String fileName) throws MetamacException {
        String allowedImageExtensions = srmConfiguration.retrieveAllowedImageExtensions();
        String extensionFile = FilenameUtils.getExtension(fileName);
        String[] imageExtensions = StringUtils.split(allowedImageExtensions, ",");

        for (String extension : imageExtensions) {
            if (extension.trim().equals(extensionFile)) {
                return;
            }
        }
        throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IMPORTATION_LOGO_CATEGORY_ELEMENT_EXTENSION_ERROR)
                .withMessageParameters(categoryElementUrn, allowedImageExtensions).build();
    }

    private void checkMaxImageSize(String categoryElementUrn, byte[] logoData) throws MetamacException {
        int maxImageSizeInKB = Integer.parseInt(srmConfiguration.retrieveImageMaxSize());

        double sizeInKB = (double) logoData.length / (double) 1024;

        if (sizeInKB > maxImageSizeInKB) {

            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IMPORTATION_LOGO_CATEGORY_ELEMENT_MAX_SIZE_ERROR)
                    .withMessageParameters(categoryElementUrn, maxImageSizeInKB).build();
        }
    }

    private void saveCategoryElementLogo(CategoryElement categoryElement, byte[] logoData, String logoFilePath) {
        if (categoryElement.getLogo() != null && categoryElement.getLogo().getId() != null) {
            resourceMetadataFileRepository.delete(categoryElement.getLogo());
        }

        ResourceMetadataFile resourceMetadataFile = new ResourceMetadataFile();
        resourceMetadataFile.setFileName(FilenameUtils.getBaseName(logoFilePath));
        resourceMetadataFile.setExtension(FilenameUtils.getExtension(logoFilePath));
        ResourceFile resourceFile = new ResourceFile();
        resourceFile.setFileData(logoData);
        resourceMetadataFile.setFile(resourceFile);

        categoryElement.setLogo(resourceMetadataFile);

        getCategoryElementRepository().save(categoryElement);

    }

    @Override
    public String retrieveCategoryElementLogo(ServiceContext ctx, String categoryElementUrn) throws MetamacException {
        try {
            CategoryElement categoryElement = findCategoryElementByUrn(categoryElementUrn);

            if (categoryElement.getLogo() != null && categoryElement.getLogo().getFile() != null) {
                Path fileName = Files.createTempFile(categoryElement.getLogo().getFileName(), "." + categoryElement.getLogo().getExtension());

                Files.write(fileName, categoryElement.getLogo().getFile().getFileData(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
                return fileName.getFileName().toString();
            }

            return null;
        } catch (IOException e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.EXPORTATION_LOGO_CATEGORY_ELEMENT).withMessageParameters(e).build();
        }
    }

    @Override
    public void deleteCategoryElementLogo(ServiceContext ctx, String categoryElementUrn) throws MetamacException {

        CategoryElement categoryElement = findCategoryElementByUrn(categoryElementUrn);

        if (categoryElement.getLogo() != null && categoryElement.getLogo().getId() != null) {
            resourceMetadataFileRepository.delete(categoryElement.getLogo());
        }
        categoryElement.setLogo(null);
        getCategoryElementRepository().save(categoryElement);

    }

    @Override
    public List<ItemResult> retrieveCategoriesByCategorySchemeUrnOrderedInDepth(ServiceContext ctx, String categorySchemeUrn, ItemMetamacResultSelection itemMetamacResultSelection,
            String orderVisualisationCode) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkRetrieveCategoriesByCategorySchemeUrnOrderedInDepth(categorySchemeUrn, itemMetamacResultSelection, orderVisualisationCode, null);

        if (itemMetamacResultSelection == null) {
            itemMetamacResultSelection = CategoryMetamacResultSelection.RETRIEVE; // default
        }
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);

        CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = retrieveCategorySchemeOrderVisualisationByCode(categorySchemeVersion, orderVisualisationCode);
        Integer orderColumnIndex = categorySchemeOrderVisualisation.getColumnIndex();

        return getCategoryMetamacRepository().findCategoriesByCategorySchemeOrderedInDepth(categorySchemeVersion.getId(), orderColumnIndex, itemMetamacResultSelection);
    }

    @Override
    public CategorySchemeOrderVisualisation createCategorySchemeOrderVisualisation(ServiceContext ctx, String categorySchemeUrn, CategorySchemeOrderVisualisation categorySchemeOrderVisualisation)
            throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkCreateCategorySchemeOrderVisualisation(categorySchemeUrn, categorySchemeOrderVisualisation, null);
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);
        checkCategorySchemeCanBeModified(categorySchemeVersion);

        // Create order visualisation
        categorySchemeOrderVisualisation.setColumnIndex(getCategoryOrderVisualisationAvailable(categorySchemeVersion));
        categorySchemeOrderVisualisation.setCategorySchemeVersion(categorySchemeVersion);

        // Assign default order to all categories, by level (copy from alphabetical)
        getCategoryMetamacRepository().copyCategoriesOrderColumn(categorySchemeVersion, SrmConstants.CATEGORY_SCHEME_ORDER_VISUALISATION_ALPHABETICAL_COLUMN_INDEX,
                categorySchemeOrderVisualisation.getColumnIndex());

        // Create
        setCategorySchemeOrderVisualisationUrnUnique(categorySchemeVersion, categorySchemeOrderVisualisation, true);
        baseService.updateItemSchemeLastUpdated(ctx, categorySchemeVersion);
        return getCategorySchemeOrderVisualisationRepository().save(categorySchemeOrderVisualisation);
    }

    @Override
    public CategorySchemeVersionMetamac createCategorySchemeOrderVisualisationAlphabetical(ServiceContext ctx, CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {
        CategorySchemeOrderVisualisation alphabeticalOrderVisualisation = new CategorySchemeOrderVisualisation();
        alphabeticalOrderVisualisation.setNameableArtefact(new NameableArtefact());
        alphabeticalOrderVisualisation.getNameableArtefact().setCode(SrmConstants.CATEGORY_SCHEME_ORDER_VISUALISATION_ALPHABETICAL_CODE);
        // NameableArtefact
        alphabeticalOrderVisualisation.getNameableArtefact().setName(InternationalStringUtils.getAphabeticalInternationalString());
        alphabeticalOrderVisualisation.setColumnIndex(SrmConstants.CATEGORY_SCHEME_ORDER_VISUALISATION_ALPHABETICAL_COLUMN_INDEX);
        setCategorySchemeOrderVisualisationUrnUnique(categorySchemeVersion, alphabeticalOrderVisualisation, true);

        categorySchemeVersion.addOrderVisualisation(alphabeticalOrderVisualisation);
        categorySchemeVersion = getCategorySchemeVersionMetamacRepository().save(categorySchemeVersion);

        sortCategoriesInAlphabeticalOrder(categorySchemeVersion.getItems(), alphabeticalOrderVisualisation, true);
        return categorySchemeVersion;
    }

    @Override
    public CategorySchemeOrderVisualisation updateCategorySchemeOrderVisualisation(ServiceContext ctx, CategorySchemeOrderVisualisation categorySchemeOrderVisualisation) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkUpdateCategorySchemeOrderVisualisation(categorySchemeOrderVisualisation, null);
        CategorySchemeVersionMetamac categorySchemeVersion = categorySchemeOrderVisualisation.getCategorySchemeVersion();
        checkCategorySchemeCanBeModified(categorySchemeVersion);

        // If code has been changed, update URN
        if (categorySchemeOrderVisualisation.getNameableArtefact().getIsCodeUpdated()) {
            if (categorySchemeOrderVisualisation.getNameableArtefact().getUrn().endsWith("." + SrmConstants.CATEGORY_SCHEME_ORDER_VISUALISATION_ALPHABETICAL_CODE)) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_CODE);
            }
            setCategorySchemeOrderVisualisationUrnUnique(categorySchemeOrderVisualisation.getCategorySchemeVersion(), categorySchemeOrderVisualisation, true);
        }
        categorySchemeOrderVisualisation.setUpdateDate(new DateTime()); // Optimistic locking

        // Update
        baseService.updateItemSchemeLastUpdated(ctx, categorySchemeVersion);
        return getCategorySchemeOrderVisualisationRepository().save(categorySchemeOrderVisualisation);
    }

    @Override
    public CategorySchemeOrderVisualisation retrieveCategorySchemeOrderVisualisationByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkRetrieveByUrn(urn);

        CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = getCategorySchemeOrderVisualisationRepository().findByUrn(urn);
        if (categorySchemeOrderVisualisation == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return categorySchemeOrderVisualisation;
    }

    @Override
    public CategorySchemeOrderVisualisation retrieveCategorySchemeOrderVisualisationByCode(ServiceContext ctx, String categorySchemeUrn, String code) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkRetrieveCategorySchemeOrderVisualisationByCode(categorySchemeUrn, code, null);

        // Retrieve
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);
        return retrieveCategorySchemeOrderVisualisationByCode(categorySchemeVersion, code);
    }

    @Override
    public List<CategorySchemeOrderVisualisation> retrieveCategorySchemeOrderVisualisationsByCategoryScheme(ServiceContext ctx, String categorySchemeUrn) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkRetrieveByUrn(categorySchemeUrn);

        // Retrieve
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);
        return categorySchemeVersion.getOrderVisualisations();
    }

    @Override
    public void deleteCategorySchemeOrderVisualisation(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkDeleteArtefact(urn);
        CategorySchemeOrderVisualisation categorySchemeOrderVisualisationToDelete = retrieveCategorySchemeOrderVisualisationByUrn(urn);
        SrmValidationUtils.checkNotAlphabeticalOrderVisualisation(categorySchemeOrderVisualisationToDelete);

        // Check category scheme can be modified
        CategorySchemeVersionMetamac categorySchemeVersion = categorySchemeOrderVisualisationToDelete.getCategorySchemeVersion();
        checkCategorySchemeCanBeModified(categorySchemeVersion);

        // Check visualisation is not in original version
        if (VersionUtil.isTemporalVersion(categorySchemeVersion.getMaintainableArtefact().getVersionLogic())) {
            CategorySchemeVersionMetamac categorySchemeOriginalVersion = retrieveCategorySchemeOriginalFromUrnTemporal(ctx, categorySchemeVersion.getMaintainableArtefact().getUrn());
            CategorySchemeOrderVisualisation categorySchemeOrderVisualisationOriginal = getCategorySchemeOrderVisualisationRepository().findByCode(categorySchemeOriginalVersion.getId(),
                    categorySchemeOrderVisualisationToDelete.getNameableArtefact().getCode());
            if (categorySchemeOrderVisualisationOriginal != null) {
                throw new MetamacException(ServiceExceptionType.CATEGORY_SCHEME_ORDER_VISUALISATION_DELETING_NOT_SUPPORTED_CATEGORY_SCHEME_WAS_EVER_PUBLISHED, urn);
            }
        }

        // Clear if it is the default visualisation
        if (categorySchemeVersion.getDefaultOrderVisualisation() != null && categorySchemeVersion.getDefaultOrderVisualisation().getNameableArtefact().getUrn().equals(urn)) {
            categorySchemeVersion.setDefaultOrderVisualisation(null);
            getCategorySchemeVersionMetamacRepository().save(categorySchemeVersion);
        }

        // Delete
        baseService.updateItemSchemeLastUpdated(ctx, categorySchemeVersion);
        getCategorySchemeOrderVisualisationRepository().delete(categorySchemeOrderVisualisationToDelete);
        // Clear orders in category columns
        getCategoryMetamacRepository().clearCategoriesOrderColumn(categorySchemeVersion, categorySchemeOrderVisualisationToDelete.getColumnIndex());
    }

    @Override
    public CategorySchemeVersionMetamac versioningCategorySchemeOrderVisualisations(ServiceContext ctx, CategorySchemeVersionMetamac source, CategorySchemeVersionMetamac target)
            throws MetamacException {
        for (CategorySchemeOrderVisualisation categorySchemeOrderVisualisationSource : source.getOrderVisualisations()) {
            versioningCategorySchemeOrderVisualisation(categorySchemeOrderVisualisationSource, target);
        }
        target = getCategorySchemeVersionMetamacRepository().save(target);
        for (CategorySchemeOrderVisualisation categorySchemeOrderVisualisationTarget : target.getOrderVisualisations()) {
            if (source.getDefaultOrderVisualisation() != null
                    && source.getDefaultOrderVisualisation().getNameableArtefact().getCode().equals(categorySchemeOrderVisualisationTarget.getNameableArtefact().getCode())) {
                target.setDefaultOrderVisualisation(categorySchemeOrderVisualisationTarget);
                break;
            }
        }
        target = getCategorySchemeVersionMetamacRepository().save(target);
        return target;
    }

    @Override
    public void recalculateCategoriesVisualisations(ServiceContext ctx, List<Category> items, List<CategorySchemeOrderVisualisation> orderVisualisations, boolean executeSave) {

        // Recalculate all order codes
        Boolean alphabeticalAlreadyUpdated = Boolean.FALSE;
        for (CategorySchemeOrderVisualisation orderVisualisation : orderVisualisations) {
            if (!alphabeticalAlreadyUpdated && SrmServiceUtils.isAlphabeticalOrderVisualisation(orderVisualisation)) {
                sortCategoriesInAlphabeticalOrder(items, orderVisualisation, false);
                alphabeticalAlreadyUpdated = Boolean.TRUE;
            } else {
                sortCategoriesByOrder(items, orderVisualisation, false);
            }
        }
    }

    @Override
    public void updateCategoryInOrderVisualisation(ServiceContext ctx, String categoryUrn, String categorySchemeOrderVisualisationUrn, Integer newCategoryIndex) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkUpdateCategoryInOrder(categoryUrn, categorySchemeOrderVisualisationUrn, newCategoryIndex, null);
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByCategoryUrn(ctx, categoryUrn);
        checkCategorySchemeCanBeModified(categorySchemeVersion);

        CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = retrieveCategorySchemeOrderVisualisationByUrn(ctx, categorySchemeOrderVisualisationUrn);
        SrmValidationUtils.checkNotAlphabeticalOrderVisualisation(categorySchemeOrderVisualisation);

        CategoryMetamac category = retrieveCategoryByUrn(ctx, categoryUrn);

        reorderCategoriesDeletingOneCode(categorySchemeVersion, categorySchemeOrderVisualisation, (Category) category.getParent(), category);

        reorderCategoriesAddingOneInMiddle(categorySchemeVersion, categorySchemeOrderVisualisation, category, newCategoryIndex);

        // Update category
        SrmServiceUtils.setCategorySchemeOrder(category, categorySchemeOrderVisualisation.getColumnIndex(), newCategoryIndex);
        getCategoryMetamacRepository().save(category);
        baseService.updateItemSchemeLastUpdated(ctx, categorySchemeVersion);
    }

    @Override
    public TaskImportationInfo importCategorySchemeOrdersTsv(ServiceContext ctx, String categorySchemeUrn, File file, String fileName, Boolean canBeBackground) throws MetamacException {
        // Validation
        CategoriesMetamacInvocationValidator.checkImportCategoryOrdersTsv(categorySchemeUrn, file, fileName, canBeBackground, null);
        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);
        TsvImportationUtils.checkItemSchemeCanExecuteImportation(categorySchemeVersion, categorySchemeVersion.getLifeCycleMetadata(), canBeBackground);

        // Decide if task must be executed in background
        boolean executeInBackground = SrmServiceUtils.taskMustBeBackground(file, canBeBackground, SrmConstants.NUM_BYTES_TO_PLANNIFY_TSV_CATEGORIES_ORDERS_IMPORTATION);

        // Plannify task if can be in background
        if (executeInBackground) {
            categorySchemeVersion.getItemScheme().setIsTaskInBackground(Boolean.TRUE);
            itemSchemeRepository.save(categorySchemeVersion.getItemScheme());

            String jobKey = tasksMetamacService.plannifyImportCategorySchemeOrdersTsvInBackground(ctx, categorySchemeUrn, file, fileName);
            return new TaskImportationInfo(Boolean.TRUE, jobKey);
        }

        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        List<MetamacExceptionItem> informationItems = new ArrayList<MetamacExceptionItem>();
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        List<Category> categoriesInCategoryScheme = null;
        Set<String> categoriesInTsvToCheckAllCategoriesAreUpdated = null;
        List<CategorySchemeOrderVisualisation> orderVisualisations = null;
        try {
            InputStream stream = new FileInputStream(file);
            String charset = FileUtils.guessCharset(file);
            inputStreamReader = new InputStreamReader(stream, charset);
            bufferedReader = new BufferedReader(inputStreamReader);

            // Header
            String line = bufferedReader.readLine();
            ImportationCodeOrdersTsvHeader header = TsvImportationUtils.parseTsvHeaderToImportCodeOrders(line, exceptionItems);
            if (CollectionUtils.isEmpty(exceptionItems)) {
                orderVisualisations = tsvHeaderToCategoryOrderVisualisations(categorySchemeVersion, header, exceptionItems);
            }

            // Orders
            if (CollectionUtils.isEmpty(exceptionItems)) {

                // Categories in category scheme
                categoriesInCategoryScheme = categorySchemeVersion.getItems();
                Map<String, CategoryMetamac> categoriesInSchemeByCode = new HashMap<String, CategoryMetamac>();
                for (Item item : categoriesInCategoryScheme) {
                    categoriesInSchemeByCode.put(item.getNameableArtefact().getCode(), (CategoryMetamac) item);
                }
                categoriesInTsvToCheckAllCategoriesAreUpdated = new HashSet<String>();

                int lineNumber = 2;
                while ((line = bufferedReader.readLine()) != null) {
                    if (StringUtils.isBlank(line)) {
                        continue;
                    }
                    String[] columns = StringUtils.splitPreserveAllTokens(TsvImportationUtils.replaceSeparatorLineInText(line), SrmConstants.TSV_SEPARATOR);
                    if (columns.length != header.getColumnsSize()) {
                        exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber));
                        continue;
                    }
                    // Transform orders, setting value of columns
                    CategoryMetamac categoryMetamac = tsvLineToCategoryOrder(header, columns, lineNumber, categoriesInSchemeByCode, orderVisualisations, exceptionItems);
                    if (categoryMetamac != null) {
                        if (categoriesInTsvToCheckAllCategoriesAreUpdated.contains(categoryMetamac.getNameableArtefact().getCode())) {
                            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_RESOURCE_DUPLICATED, categoryMetamac.getNameableArtefact().getCode(), lineNumber));
                        } else {
                            categoriesInTsvToCheckAllCategoriesAreUpdated.add(categoryMetamac.getNameableArtefact().getCode());
                        }
                    }
                    lineNumber++;
                }
            }
        } catch (Exception e) {
            logger.error("Error importing tsv file", e);
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_FILE_PARSING, e.getMessage()));
        } finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                // do not relaunch error
                logger.error("Error closing streams", e);
            }
        }

        if (CollectionUtils.isEmpty(exceptionItems) && categoriesInTsvToCheckAllCategoriesAreUpdated.size() != categoriesInCategoryScheme.size()) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_INCORRECT_NUMBER_CATEGORIES, categoriesInCategoryScheme.size(),
                    categoriesInTsvToCheckAllCategoriesAreUpdated.size()));
        }
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            throw MetamacExceptionBuilder.builder().withPrincipalException(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR, fileName)).withExceptionItems(exceptionItems).build();
        }

        // Re-calculate orders
        for (CategorySchemeOrderVisualisation orderVisualisation : orderVisualisations) {
            sortCategoriesByOrder(categoriesInCategoryScheme, orderVisualisation, false);
        }

        // Persist
        for (Category category : categoriesInCategoryScheme) {
            CategoryMetamac categoryMetamac = (CategoryMetamac) category;
            getCategoryMetamacRepository().save(categoryMetamac);
        }
        baseService.updateItemSchemeLastUpdated(ctx, categorySchemeVersion);
        categorySchemeVersion.getItemScheme().setIsTaskInBackground(Boolean.FALSE);
        itemSchemeRepository.save(categorySchemeVersion.getItemScheme());

        return new TaskImportationInfo(Boolean.FALSE, informationItems);
    }

    @Override
    public String exportCategoryOrdersTsv(ServiceContext ctx, String categorySchemeUrn) throws MetamacException {

        CategoriesMetamacInvocationValidator.checkExportCategoriesTsv(categorySchemeUrn, null);

        CategorySchemeVersionMetamac categorySchemeVersion = retrieveCategorySchemeByUrn(ctx, categorySchemeUrn);
        List<CategorySchemeOrderVisualisation> orderVisualisations = categorySchemeVersion.getOrderVisualisations();
        String defaultLanguage = srmConfiguration.retrieveLanguageDefault();

        // Retrieve items in depth
        List<ItemResult> itemsResult = getCategoryMetamacRepository().findCategoriesByCategorySchemeOrderedInDepth(categorySchemeVersion.getId(), null, CategoryMetamacResultSelection.EXPORT);
        // Retrieve all items to retrieve all orders
        List<Category> categories = categorySchemeVersion.getItems();
        Map<Long, CategoryMetamac> categoriesById = new HashMap<Long, CategoryMetamac>(categories.size());
        for (Item category : categories) {
            categoriesById.put(category.getId(), (CategoryMetamac) category);
        }

        // Export
        OutputStream outputStream = null;
        OutputStreamWriter writer = null;
        try {
            File file = File.createTempFile("categories-orders", ".tsv");
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, SrmConstants.TSV_EXPORTATION_ENCODING);

            // header
            writer.write(SrmConstants.TSV_HEADER_CODE);
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_LABEL);
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_LEVEL);
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_PARENT);
            for (CategorySchemeOrderVisualisation orderVisualisation : orderVisualisations) {
                writer.write(SrmConstants.TSV_SEPARATOR);
                writer.write(orderVisualisation.getNameableArtefact().getCode());
            }

            // categories
            for (ItemResult itemResult : itemsResult) {
                CategoryMetamac categoryMetamac = categoriesById.get(itemResult.getItemIdDatabase());

                writer.write(SrmConstants.TSV_LINE_SEPARATOR);
                // Code
                writer.write(itemResult.getCode());
                // Label
                writer.write(SrmConstants.TSV_SEPARATOR);
                writer.write(itemResult.getName().get(defaultLanguage));
                // Level
                writer.write(SrmConstants.TSV_SEPARATOR);
                writer.write(String.valueOf(((org.siemac.metamac.srm.core.category.domain.CategoryMetamacResultExtensionPoint) itemResult.getExtensionPoint()).getLevel()));
                // Parent
                writer.write(SrmConstants.TSV_SEPARATOR);
                if (itemResult.getParent() != null) {
                    writer.write(itemResult.getParent().getCode());
                }
                // Order
                for (CategorySchemeOrderVisualisation orderVisualisation : orderVisualisations) {
                    writer.write(SrmConstants.TSV_SEPARATOR);
                    writer.write(String.valueOf(SrmServiceUtils.getCategorySchemeOrder(categoryMetamac, orderVisualisation.getColumnIndex()) + 1)); // +1 to start in 1
                }
            }
            writer.flush();
            return file.getName();
        } catch (Exception e) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.EXPORTATION_TSV_ERROR).withMessageParameters(e).build();
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(writer);
        }
    }

    /**
     * Transforms tsv header to list of CategorySchemeOrderVisualisations to import.
     */
    private List<CategorySchemeOrderVisualisation> tsvHeaderToCategoryOrderVisualisations(CategorySchemeVersionMetamac categorySchemeVersion, ImportationCodeOrdersTsvHeader header,
            List<MetamacExceptionItem> exceptionItems) {
        List<CategorySchemeOrderVisualisation> orderVisualisations = new ArrayList<CategorySchemeOrderVisualisation>();
        for (String orderVisualisationCode : header.getOrderVisualisations()) {
            CategorySchemeOrderVisualisation orderVisualisation = getCategorySchemeOrderVisualisationRepository().findByCode(categorySchemeVersion.getId(), orderVisualisationCode);
            if (orderVisualisation == null) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_ORDER_VISUALISATION_NOT_FOUND, orderVisualisationCode,
                        categorySchemeVersion.getMaintainableArtefact().getUrn()));
                break;
            } else if (SrmServiceUtils.isAlphabeticalOrderVisualisation(orderVisualisation)) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_ALPHABETICAL_VISUALISATION_NOT_SUPPORTED));
                break;
            } else {
                orderVisualisations.add(orderVisualisation);
            }
        }
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            return null;
        }
        return orderVisualisations;
    }

    /**
     * Transforms tsv line to category order values. IMPORTANT: Does not execute save or update operation.
     */
    private CategoryMetamac tsvLineToCategoryOrder(ImportationCodeOrdersTsvHeader header, String[] columns, int lineNumber, Map<String, CategoryMetamac> categoriesInSchemeByCode,
            List<CategorySchemeOrderVisualisation> orderVisualisations, List<MetamacExceptionItem> exceptionItems) throws MetamacException {

        // semantic identifier
        String codeIdentifier = columns[header.getCodePosition()];
        if (StringUtils.isBlank(codeIdentifier)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber));
            return null;
        }
        // update orders
        CategoryMetamac categoryMetamac = categoriesInSchemeByCode.get(codeIdentifier);
        if (categoryMetamac == null) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_CATEGORY_NOT_FOUND, codeIdentifier));
            return null;
        }
        for (CategorySchemeOrderVisualisation orderVisualisation : orderVisualisations) {
            int columnIndex = header.getOrderVisualisationColumn(orderVisualisation.getNameableArtefact().getCode());
            String orderValueString = columns[columnIndex];
            if (StringUtils.isBlank(orderValueString)) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_REQUIRED, categoryMetamac.getNameableArtefact().getCode(),
                        ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_ORDER));
                return null;
            }
            Integer orderValue = Integer.valueOf(orderValueString);
            SrmServiceUtils.setCategorySchemeOrder(categoryMetamac, orderVisualisation.getColumnIndex(), orderValue);
        }

        // Do not persist if any error occurred
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            return null;
        }

        return categoryMetamac;
    }

    // -------------------------------------------------------------------------
    // PRIVATE HELPERS - Category Scheme Order Visualisation
    // -------------------------------------------------------------------------

    private CategorySchemeOrderVisualisation versioningCategorySchemeOrderVisualisation(CategorySchemeOrderVisualisation source, CategorySchemeVersionMetamac categorySchemeTarget)
            throws MetamacException {
        CategorySchemeOrderVisualisation target = new CategorySchemeOrderVisualisation();
        target.setColumnIndex(source.getColumnIndex());
        target.setNameableArtefact(versioningCategorySchemeVisualisationNameableArtefact(source.getNameableArtefact()));
        setCategorySchemeOrderVisualisationUrnUnique(categorySchemeTarget, target, false);
        categorySchemeTarget.addOrderVisualisation(target);
        return target;
    }

    private NameableArtefact versioningCategorySchemeVisualisationNameableArtefact(NameableArtefact source) {
        NameableArtefact target = new NameableArtefact();
        target.setName(BaseCopyAllMetadataUtils.copy(source.getName()));
        target.setCode(source.getCode());
        target.setCodeFull(source.getCodeFull());
        return target;
    }

    private CategorySchemeOrderVisualisation retrieveCategorySchemeOrderVisualisationByUrn(String urn) throws MetamacException {
        CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = getCategorySchemeOrderVisualisationRepository().findByUrn(urn);
        if (categorySchemeOrderVisualisation == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return categorySchemeOrderVisualisation;
    }

    private CategorySchemeOrderVisualisation retrieveCategorySchemeOrderVisualisationByCode(CategorySchemeVersionMetamac categorySchemeVersion, String code) throws MetamacException {
        CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = getCategorySchemeOrderVisualisationRepository().findByCode(categorySchemeVersion.getId(), code);
        if (categorySchemeOrderVisualisation == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CATEGORY_SCHEME_ORDER_VISUALISATION_NOT_FOUND)
                    .withMessageParameters(code, categorySchemeVersion.getMaintainableArtefact().getUrn()).build();
        }
        return categorySchemeOrderVisualisation;
    }

    private void setCategorySchemeOrderVisualisationUrnUnique(CategorySchemeVersion categorySchemeVersion, CategorySchemeOrderVisualisation categorySchemeOrderVisualisation, boolean checkUrnUnique)
            throws MetamacException {
        String urn = GeneratorUrnUtils.generateCategorySchemeOrderVisualisationUrn(categorySchemeVersion, categorySchemeOrderVisualisation);

        if (checkUrnUnique) {
            identifiableArtefactRepository.checkUrnUnique(urn, categorySchemeOrderVisualisation.getNameableArtefact().getId());
        }

        categorySchemeOrderVisualisation.getNameableArtefact().setUrn(urn);
        categorySchemeOrderVisualisation.getNameableArtefact().setUrnProvider(urn);
    }

    private void sortCategoriesInAlphabeticalOrder(List<Category> items, CategorySchemeOrderVisualisation alphabeticalOrderVisualisation, boolean executeSave) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }
        List<Category> categoriesOrdered = new ArrayList<Category>(items);
        Collections.sort(categoriesOrdered, new AlphabeticalByLevelComparator());
        sortCategories(alphabeticalOrderVisualisation.getColumnIndex(), categoriesOrdered, executeSave);
    }

    private void sortCategoriesByOrder(List<Category> items, CategorySchemeOrderVisualisation orderVisualisation, boolean executeSave) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }
        List<Category> categoriesOrdered = new ArrayList<Category>(items);
        Collections.sort(categoriesOrdered, new CategorySchemeOrderIndexByLevelComparator(orderVisualisation.getColumnIndex()));
        sortCategories(orderVisualisation.getColumnIndex(), categoriesOrdered, executeSave);
    }

    private void sortCategories(Integer orderVisualisationIndex, List<Category> categoriesOrdered, boolean executeSave) {

        String previousParentUrn = null;
        int previousOrder = -1;
        for (Item item : categoriesOrdered) {
            CategoryMetamac category = (CategoryMetamac) item;
            String actualParentUrn = category.getParent() != null ? category.getParent().getNameableArtefact().getUrn() : null;
            if (!StringUtils.equals(previousParentUrn, actualParentUrn)) {
                previousOrder = -1; // another level
                previousParentUrn = category.getParent() != null ? category.getParent().getNameableArtefact().getUrn() : null;
            }
            int order = previousOrder == -1 ? 0 : previousOrder + 1;
            SrmServiceUtils.setCategorySchemeOrder(category, orderVisualisationIndex, Integer.valueOf(order));
            if (executeSave) {
                getCategoryMetamacRepository().save(category);
            }
            previousOrder = order;
        }
    }

    private void reorderCategoriesDeletingOneCode(CategorySchemeVersionMetamac categorySchemeVersion, CategorySchemeOrderVisualisation categorySchemeOrderVisualisation, Category parent,
            CategoryMetamac category) throws MetamacException {
        Integer columnIndex = categorySchemeOrderVisualisation.getColumnIndex();
        Integer previousOrder = SrmServiceUtils.getCategorySchemeOrder(category, columnIndex);
        getCategoryMetamacRepository().reorderCategoriesDeletingOneCode(categorySchemeVersion, parent, category, columnIndex, previousOrder);
    }

    private void reorderCategoriesAddingOneInMiddle(CategorySchemeVersionMetamac categorySchemeVersion, CategorySchemeOrderVisualisation categorySchemeOrderVisualisation, CategoryMetamac category,
            Integer order) throws MetamacException {
        Integer columnIndex = categorySchemeOrderVisualisation.getColumnIndex();
        getCategoryMetamacRepository().reorderCategoriesAddingOneCodeInMiddle(categorySchemeVersion, category, columnIndex, order);
    }

    private CategoryMetamac initVisualisationsToCategory(CategorySchemeVersionMetamac categorySchemeVersion, CategoryMetamac category, boolean categoryExists) throws MetamacException {

        // Order visualisations
        Boolean alphabeticalAlreadyUpdated = Boolean.FALSE; // to avoid select to compare code of nameable
        Integer orderInLastPosition = null; // to avoid unnecessary extra queries
        for (CategorySchemeOrderVisualisation categorySchemeOrderVisualisation : categorySchemeVersion.getOrderVisualisations()) {

            // remove from previous position
            if (categoryExists) {
                reorderCategoriesDeletingOneCode(categorySchemeVersion, categorySchemeOrderVisualisation, category.getParent(), category);
            }

            if (!alphabeticalAlreadyUpdated && SrmServiceUtils.isAlphabeticalOrderVisualisation(categorySchemeOrderVisualisation)) {
                setCategoryOrderInAlphabeticalPositionAndReorderCategoriesInLevel(categorySchemeVersion, categorySchemeOrderVisualisation, category.getParent(), category);
                alphabeticalAlreadyUpdated = Boolean.TRUE;
            } else {
                orderInLastPosition = setCategoryOrderInLastPositionInLevel(categorySchemeVersion, categorySchemeOrderVisualisation, category.getParent(), category, orderInLastPosition);
            }
        }

        category = getCategoryMetamacRepository().save(category);
        return category;
    }

    private void setCategoryOrderInAlphabeticalPositionAndReorderCategoriesInLevel(CategorySchemeVersionMetamac categorySchemeVersion,
            CategorySchemeOrderVisualisation categorySchemeOrderVisualisation, Category parent, CategoryMetamac category) throws MetamacException {
        if (!SrmServiceUtils.isAlphabeticalOrderVisualisation(categorySchemeOrderVisualisation)) {
            return;
        }
        Integer alphabeticalPosition = getCategoryMetamacRepository().getCategoryAlphabeticPositionInLevel(categorySchemeVersion, parent, category);
        Integer columnIndex = SrmConstants.CATEGORY_SCHEME_ORDER_VISUALISATION_ALPHABETICAL_COLUMN_INDEX;
        if (alphabeticalPosition == null) {
            alphabeticalPosition = Integer.valueOf(0);
        }
        reorderCategoriesAddingOneInMiddle(categorySchemeVersion, categorySchemeOrderVisualisation, category, alphabeticalPosition);
        SrmServiceUtils.setCategorySchemeOrder(category, columnIndex, alphabeticalPosition);
    }

    private Integer setCategoryOrderInLastPositionInLevel(CategorySchemeVersionMetamac categorySchemeVersion, CategorySchemeOrderVisualisation categorySchemeOrderVisualisation, Category parent,
            CategoryMetamac category, Integer precalculateOrder) throws MetamacException {
        Integer columnIndex = categorySchemeOrderVisualisation.getColumnIndex();
        Integer orderInNewLevel = null;
        if (precalculateOrder == null) {
            SrmServiceUtils.setCategorySchemeOrder(category, columnIndex, -1); // min value
            Integer actualMaximumOrderInLevel = getCategoryMetamacRepository().getMaximumOrderInLevel(categorySchemeVersion, parent, columnIndex);
            if (actualMaximumOrderInLevel == null) {
                orderInNewLevel = 0;
            } else {
                orderInNewLevel = actualMaximumOrderInLevel + 1;
            }
        } else {
            orderInNewLevel = precalculateOrder;
        }
        SrmServiceUtils.setCategorySchemeOrder(category, columnIndex, orderInNewLevel);
        return orderInNewLevel;
    }

    private Integer getCategoryOrderVisualisationAvailable(CategorySchemeVersionMetamac categorySchemeVersion) throws MetamacException {
        int index = -1;
        for (int i = 1; i <= SrmConstants.CATEGORY_SCHEME_ORDER_VISUALISATION_MAXIMUM_NUMBER; i++) {
            boolean alreadyExists = false;
            for (CategorySchemeOrderVisualisation categorySchemeOrderVisualisationPrevious : categorySchemeVersion.getOrderVisualisations()) {
                if (categorySchemeOrderVisualisationPrevious.getColumnIndex().intValue() == i) {
                    alreadyExists = true;
                    break;
                }
            }
            if (!alreadyExists) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            throw new MetamacException(ServiceExceptionType.CATEGORY_SCHEME_ORDER_VISUALISATION_MAXIMUM_REACHED, categorySchemeVersion.getMaintainableArtefact().getUrn());
        }
        return Integer.valueOf(index);
    }

    private CategorySchemeVersionMetamac retrieveCategorySchemeOriginalFromUrnTemporal(ServiceContext ctx, String urnTemporal) throws MetamacException {
        String urnOriginal = GeneratorUrnUtils.makeUrnFromTemporal(urnTemporal);
        return retrieveCategorySchemeByUrn(ctx, urnOriginal);
    }

    private void setDefaultCategorySchemeOrderVisualisation(CategorySchemeVersionMetamac temporaryCategoryScheme, CategorySchemeVersionMetamac categoryScheme) {
        String defaultOrderVisualisationCode = temporaryCategoryScheme.getDefaultOrderVisualisation() == null
                ? null
                : temporaryCategoryScheme.getDefaultOrderVisualisation().getNameableArtefact().getCode();

        CategorySchemeOrderVisualisation categorySchemeOrderVisualisation = null;
        for (CategorySchemeOrderVisualisation orderVisualisation : categoryScheme.getOrderVisualisations()) {
            if (StringUtils.equals(defaultOrderVisualisationCode, orderVisualisation.getNameableArtefact().getCode())) {
                categorySchemeOrderVisualisation = orderVisualisation;
            }
        }
        categoryScheme.setDefaultOrderVisualisation(categorySchemeOrderVisualisation);
    }

    private void createOrderVisualisationVersionFromTemporalOrderVisualisation(CategorySchemeVersionMetamac categorySchemeVersionTemporal) throws MetamacException {
        for (CategorySchemeOrderVisualisation categorySchemeOrderVisualisation : categorySchemeVersionTemporal.getOrderVisualisations()) {
            setCategorySchemeOrderVisualisationUrnUnique(categorySchemeVersionTemporal, categorySchemeOrderVisualisation, true);
        }
        getCategorySchemeVersionMetamacRepository().save(categorySchemeVersionTemporal);
    }

    private CategoryElement findCategoryElementByUrn(String categoryElementUrn) throws MetamacException {
        CategoriesMetamacInvocationValidator.checkRetrieveCategoryElement(categoryElementUrn, null);
        CategoryElement categoryElement = getCategoryElementRepository().findByUrn(categoryElementUrn);

        if (categoryElement == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(categoryElementUrn)
                    .withLoggedLevel(ExceptionLevelEnum.ERROR).build();
        }

        return categoryElement;
    }
}
