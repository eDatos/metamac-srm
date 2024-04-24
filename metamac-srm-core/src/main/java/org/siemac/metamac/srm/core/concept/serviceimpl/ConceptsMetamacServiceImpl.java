package org.siemac.metamac.srm.core.concept.serviceimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.LeafProperty;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.VersionPatternEnum;
import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.core.common.io.FileUtils;
import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.base.serviceimpl.utils.BaseReplaceFromTemporalMetamac;
import org.siemac.metamac.srm.core.category.serviceapi.CategoriesMetamacService;
import org.siemac.metamac.srm.core.category.serviceimpl.utils.CategoriesMetamacInvocationValidator;
import org.siemac.metamac.srm.core.category.serviceimpl.utils.CategorisationsUtils;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacProperties;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamacRepository;
import org.siemac.metamac.srm.core.code.domain.ConceptMetamacResultSelection;
import org.siemac.metamac.srm.core.code.domain.TaskImportationInfo;
import org.siemac.metamac.srm.core.code.domain.Variable;
import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.code.enume.domain.VariableTypeEnum;
import org.siemac.metamac.srm.core.code.serviceapi.CodesMetamacService;
import org.siemac.metamac.srm.core.common.LifeCycle;
import org.siemac.metamac.srm.core.common.SrmValidation;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.common.service.utils.SrmValidationUtils;
import org.siemac.metamac.srm.core.common.service.utils.TsvExportationUtils;
import org.siemac.metamac.srm.core.common.service.utils.TsvImportationUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacProperties;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamacRepository;
import org.siemac.metamac.srm.core.concept.domain.ConceptType;
import org.siemac.metamac.srm.core.concept.domain.Quantity;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityTypeEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityUnitSymbolPositionEnum;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.ConceptsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.dsd.serviceapi.DataStructureDefinitionMetamacService;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.srm.core.serviceapi.StreamMessagingService;
import org.siemac.metamac.srm.core.serviceimpl.result.ResourceVersionPublicationResult;
import org.siemac.metamac.srm.core.serviceimpl.result.SendStreamMessageResult;
import org.siemac.metamac.srm.core.task.domain.ImportationConceptsTsvHeader;
import org.siemac.metamac.srm.core.task.domain.RepresentationsTsv;
import org.siemac.metamac.srm.core.task.domain.RepresentationsTsv.RepresentationEum;
import org.siemac.metamac.srm.core.task.serviceapi.TasksMetamacService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.arte.statistic.sdmx.srm.core.base.domain.Facet;
import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersionRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.Representation;
import com.arte.statistic.sdmx.srm.core.base.serviceapi.BaseService;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.ItemSchemesCopyCallback;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseCopyAllMetadataUtils;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseMergeAssert;
import com.arte.statistic.sdmx.srm.core.common.domain.ExternalItem;
import com.arte.statistic.sdmx.srm.core.common.domain.ExternalItemRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.InternationalString;
import com.arte.statistic.sdmx.srm.core.common.domain.InternationalStringRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.TaskInfo;
import com.arte.statistic.sdmx.srm.core.common.service.utils.GeneratorUrnUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxSrmUrnParserUtils;
import com.arte.statistic.sdmx.srm.core.common.service.utils.shared.SdmxVersionUtils;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptRepository;
import com.arte.statistic.sdmx.srm.core.concept.domain.ConceptSchemeVersion;
import com.arte.statistic.sdmx.srm.core.concept.serviceapi.ConceptsService;
import com.arte.statistic.sdmx.srm.core.concept.serviceimpl.utils.ConceptsInvocationValidator;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.FacetValueTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RepresentationTypeEnum;

/**
 * Implementation of ConceptsMetamacService.
 */
@Service("conceptsMetamacService")
public class ConceptsMetamacServiceImpl extends ConceptsMetamacServiceImplBase {

    private static Logger                            logger = LoggerFactory.getLogger(ConceptsMetamacServiceImpl.class);

    @Autowired
    private BaseService                              baseService;

    @Autowired
    private ConceptsService                          conceptsService;

    @Autowired
    private CategoriesMetamacService                 categoriesMetamacService;

    @Autowired
    private CodesMetamacService                      codesMetamacService;

    @Autowired
    private ItemSchemeVersionRepository              itemSchemeVersionRepository;

    @Autowired
    private ConceptRepository                        conceptRepository;

    @Autowired
    private ItemSchemeRepository                     itemSchemeRepository;

    @Autowired
    @Qualifier("conceptSchemeLifeCycle")
    private LifeCycle                                conceptSchemeLifeCycle;

    @Autowired
    private SrmConfiguration                         srmConfiguration;

    @Autowired
    private SrmValidation                            srmValidation;

    @Autowired
    private CodelistVersionMetamacRepository         codelistVersionMetamacRepository;

    @Autowired
    private ConceptSchemeVersionMetamacRepository    conceptSchemeVersionMetamacRepository;

    @Autowired
    @Qualifier("conceptsVersioningCallbackMetamac")
    private ItemSchemesCopyCallback                  conceptsVersioningCallback;

    @Autowired
    @Qualifier("conceptsCopyCallbackMetamac")
    private ItemSchemesCopyCallback                  conceptsCopyCallback;

    @Autowired
    @Qualifier("conceptsDummyVersioningCallbackMetamac")
    private ItemSchemesCopyCallback                  conceptsDummyVersioningCallback;

    @Autowired
    private ConceptSchemeStreamMessagingCallbackImpl conceptSchemeStreamMessagingCallback;

    @Autowired
    private StreamMessagingService                   streamMessagingService;

    @Autowired
    private TasksMetamacService                      tasksMetamacService;

    @Autowired
    private InternationalStringRepository            internationalStringRepository;

    @Autowired
    private ExternalItemRepository                   externalItemRepository;

    @Autowired
    private DataStructureDefinitionMetamacService    dataStructureDefinitionMetamacService;

    @PersistenceContext(unitName = "SrmCoreEntityManagerFactory")
    protected EntityManager                          entityManager;

    public ConceptsMetamacServiceImpl() {
    }

    @Override
    public ConceptSchemeVersionMetamac createConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        preCreateConceptScheme(ctx, conceptSchemeVersion);

        // Save conceptScheme
        return (ConceptSchemeVersionMetamac) conceptsService.createConceptScheme(ctx, conceptSchemeVersion, SrmConstants.VERSION_PATTERN_METAMAC);
    }

    @Override
    public ConceptSchemeVersionMetamac preCreateConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {
        // Validation
        ConceptsMetamacInvocationValidator.checkCreateConceptScheme(conceptSchemeVersion, null);
        checkConceptSchemeToCreateOrUpdate(ctx, conceptSchemeVersion);

        // Fill metadata
        conceptSchemeVersion.setLifeCycleMetadata(new SrmLifeCycleMetadata(ProcStatusEnum.DRAFT));
        conceptSchemeVersion.getMaintainableArtefact().setIsExternalReference(Boolean.FALSE);
        conceptSchemeVersion.getMaintainableArtefact().setFinalLogicClient(Boolean.FALSE);
        conceptSchemeVersion.setStreamMessageStatus(StreamMessageStatusEnum.PENDING);

        return conceptSchemeVersion;
    }

    @Override
    public ConceptSchemeVersionMetamac updateConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkUpdateConceptScheme(conceptSchemeVersion, null);
        checkConceptSchemeToCreateOrUpdate(ctx, conceptSchemeVersion);

        // Save conceptScheme
        return (ConceptSchemeVersionMetamac) conceptsService.updateConceptScheme(ctx, conceptSchemeVersion);
    }

    @Override
    public ConceptSchemeVersionMetamac retrieveConceptSchemeByUrn(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        ConceptsInvocationValidator.checkRetrieveByUrn(urn);

        // Retrieve
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeVersionByUrn(urn);
        return conceptSchemeVersion;
    }

    @Override
    public List<ConceptSchemeVersionMetamac> retrieveConceptSchemeVersions(ServiceContext ctx, String urn) throws MetamacException {

        // Retrieve conceptSchemeVersions
        List<ConceptSchemeVersion> conceptSchemeVersions = conceptsService.retrieveConceptSchemeVersions(ctx, urn);

        // Typecast to ConceptSchemeVersionMetamac
        return conceptSchemeVersionsToConceptSchemeVersionsMetamac(conceptSchemeVersions);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        PagedResult<ConceptSchemeVersion> conceptSchemeVersionPagedResult = conceptsService.findConceptSchemesByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptSchemeVersionToMetamac(conceptSchemeVersionPagedResult);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesWithConceptsCanBeRoleByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).distinctRoot().build();
        }
        // Add restrictions to be extended
        // concept scheme must be Glossary
        ConditionalCriteria roleCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.type())
                .eq(ConceptSchemeTypeEnum.ROLE).buildSingle();
        conditions.add(roleCondition);
        // Concept scheme internally or externally published
        ConditionalCriteria publishedCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE).buildSingle();
        conditions.add(publishedCondition);

        PagedResult<ConceptSchemeVersion> conceptsPagedResult = conceptsService.findConceptSchemesByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptSchemeVersionToMetamac(conceptsPagedResult);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesWithConceptsCanBeExtendedByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).distinctRoot().build();
        }
        // Concept scheme must be Glossary
        ConditionalCriteria roleCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.type())
                .eq(ConceptSchemeTypeEnum.GLOSSARY).buildSingle();
        conditions.add(roleCondition);
        // Concept scheme internally or externally published
        ConditionalCriteria publishedCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE).buildSingle();
        conditions.add(publishedCondition);

        PagedResult<ConceptSchemeVersion> conceptsPagedResult = conceptsService.findConceptSchemesByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptSchemeVersionToMetamac(conceptsPagedResult);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesCanBeEnumeratedRepresentationForConceptsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String conceptUrn) throws MetamacException {

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).distinctRoot().build();
        }

        String conceptSchemeRelatedOperationUrn = null;
        if (StringUtils.isNotEmpty(conceptUrn)) {
            ConceptSchemeVersionMetamac conceptScheme = retrieveConceptSchemeByConceptUrn(ctx, conceptUrn);
            ExternalItem relatedOperation = conceptScheme.getRelatedOperation();
            if (relatedOperation != null) {
                conceptSchemeRelatedOperationUrn = relatedOperation.getUrn();
            }
        }

        ConditionalCriteria measureCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).withProperty(ConceptSchemeVersionMetamacProperties.type())
                .eq(ConceptSchemeTypeEnum.MEASURE).buildSingle();
        conditions.add(measureCondition);

        ConditionalCriteria relatedOperationCondition = null;
        if (conceptSchemeRelatedOperationUrn == null) {
            relatedOperationCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)//
                    .withProperty(ConceptSchemeVersionMetamacProperties.relatedOperation())//
                    .isNull()//
                    .buildSingle();
        } else {
            relatedOperationCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)//
                    .lbrace()//
                    .withProperty(ConceptSchemeVersionMetamacProperties.relatedOperation())//
                    .isNull()//
                    .or()//
                    .lbrace()//
                    .withProperty(ConceptSchemeVersionMetamacProperties.relatedOperation()).isNotNull()//
                    .and()//
                    .withProperty(ConceptSchemeVersionMetamacProperties.relatedOperation().urn())//
                    .eq(conceptSchemeRelatedOperationUrn)//
                    .rbrace() //
                    .rbrace()//
                    .buildSingle();
        }
        conditions.add(relatedOperationCondition);

        // concept scheme internally or externally published
        ConditionalCriteria publishedCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE).buildSingle();
        conditions.add(publishedCondition);

        PagedResult<ConceptSchemeVersion> conceptsPagedResult = conceptsService.findConceptSchemesByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptSchemeVersionToMetamac(conceptsPagedResult);

    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesWithConceptsCanBeQuantityBaseQuantityByCondition(ServiceContext ctx, String conceptSchemeUrn,
            List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        return findConceptSchemesCanBeQuantity(ctx, conceptSchemeUrn, conditions, pagingParameter);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesWithConceptsCanBeQuantityNumeratorByCondition(ServiceContext ctx, String conceptSchemeUrn, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter) throws MetamacException {
        return findConceptSchemesCanBeQuantity(ctx, conceptSchemeUrn, conditions, pagingParameter);
    }

    @Override
    public PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesWithConceptsCanBeQuantityDenominatorByCondition(ServiceContext ctx, String conceptSchemeUrn, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter) throws MetamacException {
        return findConceptSchemesCanBeQuantity(ctx, conceptSchemeUrn, conditions, pagingParameter);
    }

    @Override
    public PagedResult<CodelistVersionMetamac> findCodelistsWithCodesCanBeQuantityUnitByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).distinctRoot().build();
        }
        // scheme internally or externally published
        ConditionalCriteria publishedCondition = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class)
                .withProperty(CodelistVersionMetamacProperties.maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE).buildSingle();
        conditions.add(publishedCondition);
        // Codelist with access type = PUBLIC
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.accessType()).eq(AccessTypeEnum.PUBLIC).buildSingle());

        return codesMetamacService.findCodelistsByCondition(ctx, conditions, pagingParameter);
    }

    @Override
    public PagedResult<CodelistVersionMetamac> findCodelistsWithCodesCanBeQuantityBaseLocationByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).distinctRoot().build();
        }
        // scheme internally or externally published
        ConditionalCriteria publishedCondition = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class)
                .withProperty(CodelistVersionMetamacProperties.maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE).buildSingle();
        conditions.add(publishedCondition);
        // Codelist with access type = PUBLIC
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.accessType()).eq(AccessTypeEnum.PUBLIC).buildSingle());
        // Codelist with geographical variable
        conditions.add(
                ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.variable().type()).eq(VariableTypeEnum.GEOGRAPHICAL).buildSingle());
        return codesMetamacService.findCodelistsByCondition(ctx, conditions, pagingParameter);
    }

    @Override
    public ConceptSchemeVersionMetamac sendConceptSchemeToProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (ConceptSchemeVersionMetamac) conceptSchemeLifeCycle.sendToProductionValidation(ctx, urn);
    }

    @Override
    public ConceptSchemeVersionMetamac sendConceptSchemeToDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (ConceptSchemeVersionMetamac) conceptSchemeLifeCycle.sendToDiffusionValidation(ctx, urn);
    }

    @Override
    public ConceptSchemeVersionMetamac rejectConceptSchemeProductionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (ConceptSchemeVersionMetamac) conceptSchemeLifeCycle.rejectProductionValidation(ctx, urn);
    }

    @Override
    public ConceptSchemeVersionMetamac rejectConceptSchemeDiffusionValidation(ServiceContext ctx, String urn) throws MetamacException {
        return (ConceptSchemeVersionMetamac) conceptSchemeLifeCycle.rejectDiffusionValidation(ctx, urn);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResourceVersionPublicationResult<ConceptSchemeVersionMetamac> publishInternallyConceptScheme(ServiceContext ctx, String urn, Boolean forceLatestFinal) throws MetamacException {
        return (ResourceVersionPublicationResult<ConceptSchemeVersionMetamac>) conceptSchemeLifeCycle.publishInternally(ctx, urn, forceLatestFinal);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResourceVersionPublicationResult<ConceptSchemeVersionMetamac> publishExternallyConceptScheme(ServiceContext ctx, String urn) throws MetamacException {
        return (ResourceVersionPublicationResult<ConceptSchemeVersionMetamac>) conceptSchemeLifeCycle.publishExternally(ctx, urn);
    }

    @Override
    public SendStreamMessageResult resendConceptScheme(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {
        return streamMessagingService.sendMessage(conceptSchemeVersion, conceptSchemeStreamMessagingCallback);
    }

    @Override
    public void checkConceptSchemeWithRelatedResourcesExternallyPublished(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {
        Long itemSchemeVersionId = conceptSchemeVersion.getId();
        String itemSchemeVersionUrn = conceptSchemeVersion.getMaintainableArtefact().getUrn();
        Map<String, MetamacExceptionItem> exceptionItemsByUrn = new HashMap<String, MetamacExceptionItem>();
        getConceptMetamacRepository().checkConceptsWithConceptExtendsExternallyPublished(itemSchemeVersionId, exceptionItemsByUrn);
        getConceptMetamacRepository().checkConceptsWithConceptRoleExternallyPublished(itemSchemeVersionId, exceptionItemsByUrn);
        getConceptMetamacRepository().checkConceptsWithQuantityExternallyPublished(itemSchemeVersionId, exceptionItemsByUrn);
        getConceptMetamacRepository().checkConceptsWithRepresentationExternallyPublished(itemSchemeVersionId, exceptionItemsByUrn);
        categoriesMetamacService.checkCategorisationsWithRelatedResourcesExternallyPublished(ctx, itemSchemeVersionUrn, exceptionItemsByUrn);
        ExceptionUtils.throwIfException(new ArrayList<MetamacExceptionItem>(exceptionItemsByUrn.values()));
    }

    @Override
    public void deleteConceptScheme(ServiceContext ctx, String urn) throws MetamacException {
        // Validation
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, urn);
        checkConceptSchemeCanBeModified(conceptSchemeVersion);

        // Delete
        conceptsService.deleteConceptScheme(ctx, urn);
    }

    @Override
    public TaskInfo copyConceptScheme(ServiceContext ctx, String urnToCopy, String newCode) throws MetamacException {
        String maintainerUrn = srmConfiguration.retrieveMaintainerUrnDefault();
        VersionPatternEnum versionPattern = SrmConstants.VERSION_PATTERN_METAMAC;
        return conceptsService.copyConceptScheme(ctx, urnToCopy, newCode, maintainerUrn, versionPattern, conceptsCopyCallback);
    }

    @Override
    public TaskInfo versioningConceptScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionType) throws MetamacException {
        return createVersionOfConceptScheme(ctx, urnToCopy, conceptsVersioningCallback, versionType, false);
    }

    @Override
    public TaskInfo createTemporalVersionConceptScheme(ServiceContext ctx, String urnToCopy) throws MetamacException {
        return createVersionOfConceptScheme(ctx, urnToCopy, conceptsDummyVersioningCallback, null, true);
    }

    @Override
    public TaskInfo createVersionFromTemporalConceptScheme(ServiceContext ctx, String urnToCopy, VersionTypeEnum versionTypeEnum) throws MetamacException {

        ConceptSchemeVersionMetamac conceptSchemeVersionTemporal = retrieveConceptSchemeByUrn(ctx, urnToCopy);

        // Check if is a temporal version
        SrmValidationUtils.checkArtefactIsTemporal(conceptSchemeVersionTemporal.getMaintainableArtefact());

        // Retrieve the original artifact
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, GeneratorUrnUtils.makeUrnFromTemporal(urnToCopy));

        // Set the new version in the temporal artifact
        conceptSchemeVersionTemporal.getMaintainableArtefact().setVersionLogic(
                SdmxVersionUtils.createNextVersion(conceptSchemeVersion.getMaintainableArtefact().getVersionLogic(), conceptSchemeVersion.getItemScheme().getVersionPattern(), versionTypeEnum));

        conceptSchemeVersionTemporal.getMaintainableArtefact().setIsCodeUpdated(Boolean.TRUE); // For calculates new urns
        conceptSchemeVersionTemporal = (ConceptSchemeVersionMetamac) conceptsService.updateConceptScheme(ctx, conceptSchemeVersionTemporal);

        // Set null replacedBy in the original entity
        conceptSchemeVersion.getMaintainableArtefact().setReplacedByVersion(null);

        // Convert categorisations in no temporal
        categoriesMetamacService.createVersionFromTemporalCategorisations(ctx, conceptSchemeVersionTemporal.getMaintainableArtefact());

        TaskInfo versioningResult = new TaskInfo();
        versioningResult.setUrnResult(conceptSchemeVersionTemporal.getMaintainableArtefact().getUrn());
        return versioningResult;
    }

    private void checkIfConceptVariableUsedIsChanged(ServiceContext ctx, List<MetamacExceptionItem> exceptions, ConceptMetamac concept, ConceptMetamac conceptTemporal) throws MetamacException {
        String conceptUrn = concept.getNameableArtefact().getUrn();
        String urnTemporal = conceptTemporal.getVariable() != null ? conceptTemporal.getVariable().getNameableArtefact().getUrn() : null;
        String urn = concept.getVariable() != null ? concept.getVariable().getNameableArtefact().getUrn() : null;
        if (!Objects.equals(urn, urnTemporal)) {
            boolean isUsed = dataStructureDefinitionMetamacService.checkConceptIsUsedByDsd(ctx, conceptUrn);
            if (isUsed) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.CONCEPT_SCHEME_CANNOT_BE_REPLACE_CONCEPT_IS_USED, conceptUrn));
            }
        }
    }

    @Override
    public ConceptSchemeVersionMetamac mergeTemporalVersion(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeTemporalVersion) throws MetamacException {
        // Check if is a temporal version
        SrmValidationUtils.checkArtefactIsTemporal(conceptSchemeTemporalVersion.getMaintainableArtefact());
        SrmValidationUtils.checkArtefactProcStatus(conceptSchemeTemporalVersion.getLifeCycleMetadata(), conceptSchemeTemporalVersion.getMaintainableArtefact().getUrn(),
                ProcStatusEnum.DIFFUSION_VALIDATION);

        // Load original version
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, GeneratorUrnUtils.makeUrnFromTemporal(conceptSchemeTemporalVersion.getMaintainableArtefact().getUrn()));

        // Inherit InternationalStrings
        BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToItemSchemeVersionWithoutItems(conceptSchemeVersion, conceptSchemeTemporalVersion, internationalStringRepository);

        // Merge Metamac metadata of ItemScheme
        conceptSchemeVersion.setLifeCycleMetadata(BaseReplaceFromTemporalMetamac.replaceProductionAndDifussionLifeCycleMetadataFromTemporal(conceptSchemeVersion.getLifeCycleMetadata(),
                conceptSchemeTemporalVersion.getLifeCycleMetadata()));
        conceptSchemeVersion.setType(conceptSchemeTemporalVersion.getType());
        conceptSchemeVersion.setRelatedOperation(BaseReplaceFromTemporalMetamac.replaceExternalItemFromTemporal(conceptSchemeVersion.getRelatedOperation(),
                conceptSchemeTemporalVersion.getRelatedOperation(), internationalStringRepository, externalItemRepository));

        // Merge metadata of Item
        Map<String, Item> temporalItemMap = SrmServiceUtils.createMapOfItemsByOriginalUrn(conceptSchemeTemporalVersion.getItems());
        List<ItemResult> conceptsFoundEfficiently = getConceptMetamacRepository().findConceptsByConceptSchemeUnordered(conceptSchemeTemporalVersion.getId(), ItemMetamacResultSelection.ALL);
        Map<String, ItemResult> conceptsFoundEfficientlyByUrn = SdmxSrmUtils.createMapOfItemsResultByUrn(conceptsFoundEfficiently);
        List<MetamacExceptionItem> exceptions = new ArrayList<>();
        for (Item item : conceptSchemeVersion.getItems()) {
            ConceptMetamac concept = (ConceptMetamac) item;
            ConceptMetamac conceptTemp = (ConceptMetamac) temporalItemMap.get(item.getNameableArtefact().getUrn());
            ItemResult conceptTempItemResult = conceptsFoundEfficientlyByUrn.get(conceptTemp.getNameableArtefact().getUrn());

            // Inherit InternationalStrings
            BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToItem(concept, conceptTempItemResult, internationalStringRepository);

            // Metamac Metadata
            ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) conceptTempItemResult.getExtensionPoint();

            // Plural Name
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getPluralName(), extensionPoint.getPluralName())) {
                concept.setPluralName(BaseMergeAssert.mergeUpdateInternationalString(concept.getPluralName(), extensionPoint.getPluralName(), internationalStringRepository));
            }

            // Acronym
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getAcronym(), extensionPoint.getAcronym())) {
                concept.setAcronym(BaseMergeAssert.mergeUpdateInternationalString(concept.getAcronym(), extensionPoint.getAcronym(), internationalStringRepository));
            }

            // Description Source
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getDescriptionSource(), extensionPoint.getDescriptionSource())) {
                concept.setDescriptionSource(BaseMergeAssert.mergeUpdateInternationalString(concept.getDescriptionSource(), extensionPoint.getDescriptionSource(), internationalStringRepository));
            }

            // Context
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getContext(), extensionPoint.getContext())) {
                concept.setContext(BaseMergeAssert.mergeUpdateInternationalString(concept.getContext(), extensionPoint.getContext(), internationalStringRepository));
            }

            // Doc Method
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getDocMethod(), extensionPoint.getDocMethod())) {
                concept.setDocMethod(BaseMergeAssert.mergeUpdateInternationalString(concept.getDocMethod(), extensionPoint.getDocMethod(), internationalStringRepository));
            }

            // Derivation
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getDerivation(), extensionPoint.getDerivation())) {
                concept.setDerivation(BaseMergeAssert.mergeUpdateInternationalString(concept.getDerivation(), extensionPoint.getDerivation(), internationalStringRepository));
            }

            // Legal Acts
            if (!BaseMergeAssert.assertEqualsInternationalString(concept.getLegalActs(), extensionPoint.getLegalActs())) {
                concept.setLegalActs(BaseMergeAssert.mergeUpdateInternationalString(concept.getLegalActs(), extensionPoint.getLegalActs(), internationalStringRepository));
            }

            concept.setSdmxRelatedArtefact(conceptTemp.getSdmxRelatedArtefact());
            concept.setConceptType(conceptTemp.getConceptType());

            // Roles : can copy "roles" because they are concepts in another concept scheme
            concept.removeAllRoleConcepts();
            for (ConceptMetamac conceptRole : conceptTemp.getRoleConcepts()) {
                concept.addRoleConcept(conceptRole);
            }

            checkIfConceptVariableUsedIsChanged(ctx, exceptions, concept, conceptTemp);
            concept.setVariable(conceptTemp.getVariable());

            // Extends : can copy "extend" because they are concepts in another concept scheme
            concept.setConceptExtends(conceptTemp.getConceptExtends());

            // Order value
            concept.setOrderValue(conceptTemp.getOrderValue());

            concept.removeAllRelatedConcepts(); // Clean related concepts, at the final the new related concepts will be added
        }
        ExceptionUtils.throwIfException(exceptions);

        // Versioning related concepts
        for (Item conceptToCopyRelatedConcepts : conceptSchemeTemporalVersion.getItems()) {
            versioningRelatedConcepts((ConceptMetamac) conceptToCopyRelatedConcepts, conceptSchemeVersion);
        }

        // Versioning quantity
        versioningConceptsQuantity(ctx, conceptSchemeTemporalVersion, conceptSchemeVersion);

        // Add Categorisations
        boolean thereAreNewCategorisations = false;
        thereAreNewCategorisations = CategorisationsUtils.addCategorisationsTemporalToItemScheme(conceptSchemeTemporalVersion, conceptSchemeVersion);
        if (thereAreNewCategorisations) {
            // ===============================================================
            // DANGEROUS CODE: In spite of to remove item from temporal scheme and then associate to another scheme, hibernate delete this item when delete item scheme. For this, we need to clear the
            // context to avoid delete the temporary scheme with the previous temporary item when delete the temporary item scheme.
            entityManager.flush();
            entityManager.clear();
            // ===============================================================
        }

        // Delete temporal version
        deleteConceptScheme(ctx, conceptSchemeTemporalVersion.getMaintainableArtefact().getUrn());

        conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, conceptSchemeVersion.getMaintainableArtefact().getUrn());
        return conceptSchemeVersion;
    }

    @Override
    public void versioningRelatedConcepts(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersionToCopy, ConceptSchemeVersionMetamac conceptSchemeNewVersion) throws MetamacException {
        // Versioning related concepts (metadata of Metamac 'relatedConcepts'). Note: other relations are copied in copy callback
        if (conceptSchemeVersionToCopy == null) {
            // source only can be null when importing
            SrmValidationUtils.checkMaintainableArtefactImported(conceptSchemeNewVersion.getMaintainableArtefact());
            return;
        }
        for (Item conceptToCopyRelatedConcepts : conceptSchemeVersionToCopy.getItems()) {
            versioningRelatedConcepts((ConceptMetamac) conceptToCopyRelatedConcepts, conceptSchemeNewVersion);
        }
    }
    @Override
    public void versioningConceptsQuantity(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersionToCopy, ConceptSchemeVersionMetamac conceptSchemeNewVersion) throws MetamacException {
        // Versioning quantity. Note: other relations are copied in copy callback
        if (conceptSchemeVersionToCopy == null) {
            // source only can be null when importing
            SrmValidationUtils.checkMaintainableArtefactImported(conceptSchemeNewVersion.getMaintainableArtefact());
            return;
        }
        for (Item conceptToCopyQuantity : conceptSchemeVersionToCopy.getItems()) {
            versioningConceptQuantity((ConceptMetamac) conceptToCopyQuantity, conceptSchemeNewVersion);
        }
    }

    @Override
    public ConceptSchemeVersionMetamac startConceptSchemeValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkStartValidity(urn, null);
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, urn);
        srmValidation.checkStartValidity(ctx, conceptSchemeVersion.getMaintainableArtefact(), conceptSchemeVersion.getLifeCycleMetadata());

        // Start validity
        conceptSchemeVersion = (ConceptSchemeVersionMetamac) conceptsService.startConceptSchemeValidity(ctx, urn, null);
        return conceptSchemeVersion;
    }

    @Override
    public ConceptSchemeVersionMetamac endConceptSchemeValidity(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        CategoriesMetamacInvocationValidator.checkEndValidity(urn, null);
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, urn);
        srmValidation.checkEndValidity(ctx, conceptSchemeVersion.getMaintainableArtefact(), conceptSchemeVersion.getLifeCycleMetadata());

        // End validity
        conceptSchemeVersion = (ConceptSchemeVersionMetamac) conceptsService.endConceptSchemeValidity(ctx, urn, null);
        return conceptSchemeVersion;
    }

    @Override
    public ConceptMetamac createConcept(ServiceContext ctx, String conceptSchemeUrn, ConceptMetamac concept) throws MetamacException {

        // Validation
        preCreateConcept(ctx, conceptSchemeUrn, concept);
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, conceptSchemeUrn);
        srmValidation.checkItemsStructureCanBeModified(ctx, conceptSchemeVersion);

        // Save concept
        concept = (ConceptMetamac) conceptsService.createConcept(ctx, conceptSchemeUrn, concept);
        // Set order to created concept
        concept = setOrderToCodeCreated(conceptSchemeVersion, concept);

        return concept;
    }

    private ConceptMetamac setOrderToCodeCreated(ConceptSchemeVersionMetamac conceptSchemeVersion, ConceptMetamac concept) {
        Integer actualMaximumOrderInLevel = getConceptMetamacRepository().getConceptMaximumOrderInLevel(conceptSchemeVersion, concept.getParent());

        concept.setOrderValue((actualMaximumOrderInLevel == null) ? 0 : actualMaximumOrderInLevel + 1);

        concept = getConceptMetamacRepository().save(concept);

        return concept;
    }

    @Override
    public ConceptMetamac preCreateConcept(ServiceContext ctx, String conceptSchemeUrn, ConceptMetamac concept) throws MetamacException {

        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, conceptSchemeUrn);

        // Automatically updates pre-validation
        SrmServiceUtils.assignToConceptSameVariableOfCodelist(conceptSchemeVersion, concept);

        // Validation
        ConceptsMetamacInvocationValidator.checkCreateConcept(conceptSchemeVersion, concept, null);
        checkConceptToCreateOrUpdate(ctx, conceptSchemeVersion, concept);

        return concept;
    }

    @Override
    public ConceptMetamac updateConcept(ServiceContext ctx, ConceptMetamac concept) throws MetamacException {

        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByConceptUrn(ctx, concept.getNameableArtefact().getUrn());

        // Automatically updates pre-validation
        SrmServiceUtils.assignToConceptSameVariableOfCodelist(conceptSchemeVersion, concept);

        // Validation
        ConceptsMetamacInvocationValidator.checkUpdateConcept(conceptSchemeVersion, concept, null);
        checkConceptToCreateOrUpdate(ctx, conceptSchemeVersion, concept);

        return (ConceptMetamac) conceptsService.updateConcept(ctx, concept);
    }

    /**
     * Checks representation, to imported and not imported artefact
     * If it is an enumerated representation must be a codelist of same variable of concept and must be published
     */
    @Override
    public MetamacExceptionItem checkConceptRepresentation(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion, ConceptMetamac concept, boolean throwException)
            throws MetamacException {

        if (conceptSchemeVersion.getMaintainableArtefact().getIsImported() && concept.getId() == null) {
            return null; // when importing (but only when creating), skip validations
        }
        if (concept.getCoreRepresentation() == null) {
            return null;
        }

        if (ConceptSchemeTypeEnum.ROLE.equals(conceptSchemeVersion.getType()) || ConceptSchemeTypeEnum.GLOSSARY.equals(conceptSchemeVersion.getType())) {
            return SrmServiceUtils.returnOrThrowException(ServiceExceptionType.METADATA_UNEXPECTED, throwException, ServiceExceptionParameters.CONCEPT_REPRESENTATION);
        }

        if (RepresentationTypeEnum.TEXT_FORMAT.equals(concept.getCoreRepresentation().getRepresentationType())) {
            // no extra validation
        } else if (RepresentationTypeEnum.ENUMERATION.equals(concept.getCoreRepresentation().getRepresentationType())) {
            if (concept.getCoreRepresentation().getEnumerationCodelist() != null) {
                if (concept.getVariable() == null) {
                    return SrmServiceUtils.returnOrThrowException(ServiceExceptionType.CONCEPT_REPRESENTATION_ENUMERATED_CODELIST_VARIABLE_REQUIRED, throwException);
                }
                PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
                Long codelistId = concept.getCoreRepresentation().getEnumerationCodelist().getId();
                List<ConditionalCriteria> criteriaToVerifyCodelistRepresentation = ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class)
                        .withProperty(CodelistVersionMetamacProperties.id()).eq(codelistId).build();
                PagedResult<CodelistVersionMetamac> result = findCodelistsCanBeEnumeratedRepresentationForConceptByCondition(ctx, criteriaToVerifyCodelistRepresentation, pagingParameter,
                        concept.getVariable().getId());
                if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(codelistId)) {
                    return SrmServiceUtils.returnOrThrowException(ServiceExceptionType.METADATA_INCORRECT, throwException, ServiceExceptionParameters.CONCEPT_REPRESENTATION);
                }
            } else if (concept.getCoreRepresentation().getEnumerationConceptScheme() != null) {
                if (concept.getCoreRepresentation().getEnumerationConceptScheme().getId().equals(conceptSchemeVersion.getId())) {
                    return SrmServiceUtils.returnOrThrowException(ServiceExceptionType.METADATA_INCORRECT, throwException, ServiceExceptionParameters.CONCEPT_REPRESENTATION);
                }
                PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
                Long conceptSchemeId = concept.getCoreRepresentation().getEnumerationConceptScheme().getId();
                List<ConditionalCriteria> criteriaToVerifyConceptSchemeRepresentation = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                        .withProperty(ConceptSchemeVersionMetamacProperties.id()).eq(conceptSchemeId).build();
                PagedResult<ConceptSchemeVersionMetamac> result = findConceptSchemesCanBeEnumeratedRepresentationForConceptsByCondition(ctx, criteriaToVerifyConceptSchemeRepresentation,
                        pagingParameter, concept.getNameableArtefact().getUrn());
                if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(conceptSchemeId)) {
                    return SrmServiceUtils.returnOrThrowException(ServiceExceptionType.METADATA_INCORRECT, throwException, ServiceExceptionParameters.CONCEPT_REPRESENTATION);
                }
            } else {
                return SrmServiceUtils.returnOrThrowException(ServiceExceptionType.METADATA_INCORRECT, throwException, ServiceExceptionParameters.CONCEPT_REPRESENTATION);
            }
        }
        return null; // no fail
    }

    @Override
    public ConceptMetamac retrieveConceptByUrn(ServiceContext ctx, String urn) throws MetamacException {
        ConceptsMetamacInvocationValidator.checkRetrieveByUrn(urn);
        ConceptMetamac concept = getConceptMetamacRepository().findByUrn(urn);
        if (concept == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return concept;
    }

    @Override
    public TaskImportationInfo importConceptsTsv(ServiceContext ctx, String conceptSchemeUrn, File file, String fileName, boolean updateAlreadyExisting, Boolean canBeBackground)
            throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkImportConceptsTsv(conceptSchemeUrn, file, fileName, updateAlreadyExisting, canBeBackground, null);
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, conceptSchemeUrn);
        TsvImportationUtils.checkItemSchemeCanExecuteImportation(conceptSchemeVersion, conceptSchemeVersion.getLifeCycleMetadata(), canBeBackground);

        // Decide if task must be executed in background
        boolean executeInBackground = SrmServiceUtils.taskMustBeBackground(file, canBeBackground, SrmConstants.NUM_BYTES_TO_PLANNIFY_TSV_ITEMS_IMPORTATION);

        // Plannify task if background
        if (executeInBackground) {
            conceptSchemeVersion.getItemScheme().setIsTaskInBackground(Boolean.TRUE);
            itemSchemeRepository.save(conceptSchemeVersion.getItemScheme());

            String jobKey = tasksMetamacService.plannifyImportConceptsTsvInBackground(ctx, conceptSchemeUrn, file, fileName, updateAlreadyExisting);
            return new TaskImportationInfo(Boolean.TRUE, jobKey);
        }

        // Execute importation now
        List<MetamacExceptionItem> exceptionItems = new ArrayList<MetamacExceptionItem>();
        List<MetamacExceptionItem> informationItems = new ArrayList<MetamacExceptionItem>();
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        List<ConceptMetamac> conceptsToPersist = null;
        Map<String, Item> conceptsToPersistByCode = null;
        Map<String, Item> conceptsPreviousInConceptSchemeByCode = null;
        try {
            InputStream stream = new FileInputStream(file);
            String charset = FileUtils.guessCharset(file);
            inputStreamReader = new InputStreamReader(stream, charset);
            bufferedReader = new BufferedReader(inputStreamReader);

            // Header
            String line = bufferedReader.readLine();
            ImportationConceptsTsvHeader header = TsvImportationUtils.parseTsvHeaderToImportConcepts(line, exceptionItems);

            // Concepts
            if (CollectionUtils.isEmpty(exceptionItems)) {

                // Retrieve actual concepts in concept scheme
                conceptsToPersist = new ArrayList<ConceptMetamac>(); // save in order is required (first parent and then children)
                conceptsToPersistByCode = new HashMap<String, Item>();
                conceptsPreviousInConceptSchemeByCode = new HashMap<String, Item>();
                for (Item item : conceptSchemeVersion.getItems()) {
                    conceptsPreviousInConceptSchemeByCode.put(item.getNameableArtefact().getCode(), item);
                }

                boolean isDefaultMaintainer = StringUtils.equals(conceptSchemeVersion.getMaintainableArtefact().getMaintainer().getNameableArtefact().getUrn(),
                        srmConfiguration.retrieveMaintainerUrnDefault());
                boolean isTemporalVersion = VersionUtil.isTemporalVersion(conceptSchemeVersion.getMaintainableArtefact().getVersionLogic());
                if (!isDefaultMaintainer || isTemporalVersion) {
                    updateAlreadyExisting = true;
                }

                int lineNumber = 2;
                Map<String, ConceptType> cachedConceptTypeMap = new HashMap<String, ConceptType>();
                while ((line = bufferedReader.readLine()) != null) {
                    if (StringUtils.isBlank(line)) {
                        continue;
                    }
                    String[] columns = StringUtils.splitPreserveAllTokens(TsvImportationUtils.replaceSeparatorLineInText(line), SrmConstants.TSV_SEPARATOR);
                    String itemIdentifier = columns[header.getCodePosition()];
                    if (columns.length != header.getColumnsSize()) {
                        MetamacExceptionItem infoItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_TEMPORAL_VERSION_NOT_UPDATED_WITH_ERRORS,
                                !StringUtils.isEmpty(itemIdentifier) ? itemIdentifier : "DESCONOCIDO");
                        infoItem.getExceptionItems().add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber++));
                        informationItems.add(infoItem);
                        continue;
                    }

                    // Transform concept and add to list to persist
                    ConceptMetamac concept = TsvImportationUtils.tsvLineToConcept(header, columns, lineNumber, conceptSchemeVersion, isDefaultMaintainer, isTemporalVersion, updateAlreadyExisting,
                            conceptsPreviousInConceptSchemeByCode, conceptsToPersistByCode, exceptionItems, informationItems);
                    // Import other specials Metadata

                    if (concept != null) {
                        updateConceptMetadata(ctx, itemIdentifier, concept, header, columns, lineNumber, conceptSchemeVersion, isDefaultMaintainer, isTemporalVersion, cachedConceptTypeMap,
                                exceptionItems);
                    }

                    boolean existsConcept = conceptsPreviousInConceptSchemeByCode.containsKey(itemIdentifier);
                    if (!CollectionUtils.isEmpty(exceptionItems)) {
                        if (!existsConcept) {
                            MetamacExceptionItem infoItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
                            informationItems.add(infoItem);
                        }
                        setImportErrors(exceptionItems, informationItems, fileName, lineNumber);
                        concept = null;
                    }

                    if (concept != null) {
                        if (existsConcept) {
                            ConceptMetamac originalConcept = (ConceptMetamac) conceptsPreviousInConceptSchemeByCode.get(itemIdentifier);
                            TsvImportationUtils.updateOriginalConceptMetamacInImport(concept, originalConcept, isTemporalVersion);
                            concept = originalConcept;
                        }
                        conceptsToPersist.add(concept);
                        conceptsToPersistByCode.put(concept.getNameableArtefact().getCode(), concept);
                    }
                    lineNumber++;
                }
            }
        } catch (Exception e) {
            logger.error("Error importing tsv file", e);
            informationItems = null;
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_FILE_PARSING, e.getMessage()));
        } finally {
            IOUtils.closeQuietly(inputStreamReader);
            IOUtils.closeQuietly(bufferedReader);
        }

        if (CollectionUtils.isEmpty(informationItems) && !CollectionUtils.isEmpty(exceptionItems)) {
            // rollback and inform about errors
            throw MetamacExceptionBuilder.builder().withPrincipalException(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR, fileName)).withExceptionItems(exceptionItems).build();
        }

        List<Concept> allItemsToUpdate = new ArrayList<Concept>(conceptSchemeVersion.getItems());
        for (ConceptMetamac concept : conceptsToPersist) {
            if (!conceptsPreviousInConceptSchemeByCode.containsKey(concept.getNameableArtefact().getCode())) {
                allItemsToUpdate.add(concept);
            }
        }
        sortConceptsByOrder(allItemsToUpdate);

        if (conceptsToPersist != null) {
            saveConceptsEfficiently(conceptsToPersist, conceptsToPersistByCode);
            baseService.updateItemSchemeLastUpdated(ctx, conceptSchemeVersion);
            conceptSchemeVersion.getItemScheme().setIsTaskInBackground(Boolean.FALSE);
            itemSchemeRepository.save(conceptSchemeVersion.getItemScheme());
        }

        return new TaskImportationInfo(Boolean.FALSE, informationItems);
    }

    private void updateConceptMetadata(ServiceContext ctx, String conceptIdentifier, ConceptMetamac concept, ImportationConceptsTsvHeader header, String[] columns, int lineNumber,
            ConceptSchemeVersionMetamac conceptSchemeVersion, boolean isDefaultMaintainer, boolean isTemporalVersion, Map<String, ConceptType> cachedConceptTypeMap,
            List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        try {
            TsvImportationUtils.setInternationalStringConceptFields(header, columns, concept, isDefaultMaintainer);

            // PluralName
            concept.setPluralName(tsvLineToPluralName(header, columns, isDefaultMaintainer, conceptSchemeVersion, concept, exceptionItems));

            // ConceptType
            concept.setConceptType(tsvLineToConceptType(ctx, header, columns, cachedConceptTypeMap));

            // Representation
            if (!isTemporalVersion) {
                concept.setCoreRepresentation(tsvLineToRepresentation(ctx, header.getRepresentation(), columns, lineNumber, conceptIdentifier, concept, exceptionItems));
            }

            // ConceptsExtends
            concept.setConceptExtends(tsvLineToConceptExtends(ctx, header, columns, lineNumber, conceptIdentifier, exceptionItems));
            concept.setQuantity(setQuantityMetadata(ctx, lineNumber, header, columns, concept.getQuantity() != null ? concept.getQuantity() : new Quantity(), exceptionItems));

            // Variable
            setVariable(conceptSchemeVersion, concept);

            TsvImportationUtils.checkCanCreateOrUpdateConcept(conceptSchemeVersion, concept, lineNumber, exceptionItems);
        } catch (Exception e) {
            MetamacExceptionItem exceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
            exceptionItem.getExceptionItems().add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_FILE_PARSING, ServiceExceptionParameters.CONCEPT));
            exceptionItems.add(exceptionItem);
        }
    }

    private void setVariable(ConceptSchemeVersionMetamac conceptSchemeVersion, ConceptMetamac concept) {
        if (ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeVersion.getType()) || ConceptSchemeTypeEnum.TRANSVERSAL.equals(conceptSchemeVersion.getType())) {

            if (concept.getCoreRepresentation() != null && concept.getCoreRepresentation().getEnumerationCodelist() != null) {
                Variable variable = ((CodelistVersionMetamac) concept.getCoreRepresentation().getEnumerationCodelist()).getVariable();
                concept.setVariable(variable);
            }
        }
    }

    private void setImportErrors(List<MetamacExceptionItem> exceptionItems, List<MetamacExceptionItem> informationItems, String fileName, int lineNumber) throws MetamacException {
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            if (!informationItems.isEmpty()) {
                MetamacExceptionItem info = informationItems.get(informationItems.size() - 1);

                for (MetamacExceptionItem exception : exceptionItems) {
                    if (exception.getExceptionItems().isEmpty() && exception.getMessage() != null) {
                        MetamacExceptionItem metamacExceptionItem = new MetamacExceptionItem();
                        metamacExceptionItem.setMessage(exception.getMessage());
                        metamacExceptionItem.setCode(exception.getCode());
                        exception.getExceptionItems().add(metamacExceptionItem);
                    }
                    info.getExceptionItems().addAll(exception.getExceptionItems());
                }
                MetamacExceptionItem metamacExceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
                info.setMessage(metamacExceptionItem.getMessage());
                info.setCode(metamacExceptionItem.getCode());
                info.setMessageParameters(metamacExceptionItem.getMessageParameters());
                exceptionItems.clear();

            } else {
                throw MetamacExceptionBuilder.builder().withPrincipalException(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR, fileName)).withExceptionItems(exceptionItems)
                        .build();
            }
        }
    }

    private CodeMetamac findCodesByCodelistUrnByCondition(ServiceContext ctx, String codeListUrn, String codeInCodelist) throws MetamacException {

        // Prepare conditions

        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1, false);

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class).distinctRoot().build();

        // codelist internally or externally published
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class).withProperty(CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE)
                .buildSingle());
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class).withProperty(CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().urn()).eq(codeListUrn).buildSingle());
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class).withProperty(CodeMetamacProperties.nameableArtefact().code()).eq(codeInCodelist).buildSingle());

        PagedResult<CodeMetamac> result = codesMetamacService.findCodesByCondition(ctx, conditions, pagingParameter);

        if (result.getValues() != null && result.getValues().size() == 1) {
            return result.getValues().get(0);
        }

        return null;
    }

    private ConceptMetamac findConceptsByConceptSchemesCodeByCondiction(ServiceContext ctx, String conceptSchemeUrn, String conceptCodeInConceptScheme) throws MetamacException {

        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1, false);

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class).distinctRoot().build();

        conditions.add(ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().finalLogicClient())
                .eq(Boolean.TRUE).buildSingle());
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().urn()).eq(conceptSchemeUrn)
                .buildSingle());
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.nameableArtefact().code()).eq(conceptCodeInConceptScheme).buildSingle());

        PagedResult<ConceptMetamac> result = findConceptsByCondition(ctx, conditions, pagingParameter);
        if (result.getValues() != null && result.getValues().size() == 1) {
            return result.getValues().get(0);
        }

        return null;
    }

    private Quantity setQuantityMetadata(ServiceContext ctx, int lineNumber, ImportationConceptsTsvHeader header, String[] columns, Quantity quantity, List<MetamacExceptionItem> exceptionItems) {
        try {

            if (StringUtils.isEmpty(columns[header.getQuantityTypePosition()])) {
                return null;
            }

            setQuantityCommonMetadata(ctx, lineNumber, header, columns, quantity, exceptionItems);

            String intValue = columns[header.getMiminumPosition()];
            if (!StringUtils.isBlank(intValue)) {
                try {
                    quantity.setMinimum(Integer.valueOf(intValue));
                } catch (IllegalArgumentException e) {
                    quantity.setMinimum(null);
                    MetamacExceptionItem exceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
                    exceptionItem.getExceptionItems().add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_FILE_PARSING, ServiceExceptionParameters.CONCEPT_QUANTITY_MIN));
                    exceptionItems.add(exceptionItem);
                }
            } else {
                quantity.setMinimum(null);
            }

            intValue = columns[header.getMaximumPosition()];
            if (!StringUtils.isBlank(intValue)) {
                try {
                    quantity.setMaximum(Integer.valueOf(intValue));
                } catch (IllegalArgumentException e) {
                    quantity.setMaximum(null);
                    MetamacExceptionItem exceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
                    exceptionItem.getExceptionItems().add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_FILE_PARSING, ServiceExceptionParameters.CONCEPT_QUANTITY_MAX));
                    exceptionItems.add(exceptionItem);
                }
            } else {
                quantity.setMaximum(null);
            }

            String denominatorConceptScheme = columns[header.getDenominatorConceptSchemePosition()];
            String denominatorConcept = columns[header.getDenominatorConceptPosition()];
            if (denominatorConceptScheme != null && denominatorConcept != null) {
                ConceptMetamac concept = findConceptsByConceptSchemesCodeByCondiction(ctx, denominatorConceptScheme, denominatorConcept);
                quantity.setDenominator(concept);
            } else {
                quantity.setDenominator(null);
            }

            String numeratorConceptScheme = columns[header.getNumeratorConceptSchemePosition()];
            String numeratorConcept = columns[header.getNumeratorConceptPosition()];
            if (numeratorConceptScheme != null && numeratorConcept != null) {
                ConceptMetamac concept = findConceptsByConceptSchemesCodeByCondiction(ctx, numeratorConceptScheme, numeratorConcept);
                quantity.setNumerator(concept);
            } else {
                quantity.setNumerator(null);
            }

            String isPercentage = columns[header.getIsPercentagePosition()];
            if (!StringUtils.isBlank(isPercentage)) {
                quantity.setIsPercentage(Boolean.valueOf(isPercentage));
                if (Boolean.TRUE.equals(quantity.getIsPercentage())) {
                    quantity.setPercentageOf(TsvImportationUtils.tsvLineToInternationalString(header.getPercentageOf(), columns, quantity.getPercentageOf()));
                } else {
                    quantity.setPercentageOf(null);
                }
            } else {
                quantity.setIsPercentage(null);
                quantity.setPercentageOf(null);
            }

            setIndexBaseType(ctx, lineNumber, header, columns, quantity, exceptionItems);

            String indexBaseQuantityConceptScheme = columns[header.getIndexBaseQuantityConceptSchemePosition()];
            String indexBaseQuantityConcept = columns[header.getIndexBaseQuantityConceptPosition()];
            if (indexBaseQuantityConceptScheme != null && indexBaseQuantityConcept != null) {
                ConceptMetamac concept = findConceptsByConceptSchemesCodeByCondiction(ctx, indexBaseQuantityConceptScheme, indexBaseQuantityConcept);
                quantity.setBaseQuantity(concept);
            } else {
                quantity.setBaseQuantity(null);
            }

        } catch (Exception e) {
            MetamacExceptionItem exceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
            exceptionItem.getExceptionItems().add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_QUANTITY_METADATA_GENERAL_ERROR));
            exceptionItems.add(exceptionItem);
        }
        return quantity;
    }

    private void setQuantityCommonMetadata(ServiceContext ctx, int lineNumber, ImportationConceptsTsvHeader header, String[] columns, Quantity quantity, List<MetamacExceptionItem> exceptionItems)
            throws MetamacException {

        String quatityType = columns[header.getQuantityTypePosition()];
        try {
            QuantityTypeEnum quantityTypeEnum = QuantityTypeEnum.valueOf(quatityType);
            quantity.setQuantityType(quantityTypeEnum);
        } catch (IllegalArgumentException e) {
            quantity.setQuantityType(null);
            MetamacExceptionItem exceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
            exceptionItem.getExceptionItems().add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_FILE_PARSING, ServiceExceptionParameters.CONCEPT_QUANTITY_TYPE));
            exceptionItems.add(exceptionItem);
        }

        String unitCodelistUrn = columns[header.getUnitCodelistUrnPosition()];
        String unitCode = columns[header.getUnitCodePosition()];
        if (unitCodelistUrn != null && unitCode != null) {
            CodeMetamac code = findCodesByCodelistUrnByCondition(ctx, unitCodelistUrn, unitCode);
            quantity.setUnitCode(code);
        }

        String unitSymbolPosition = columns[header.getUnitSymbolPositionPosition()];
        QuantityUnitSymbolPositionEnum unitSymbolPositionEnum = QuantityUnitSymbolPositionEnum.valueOf(unitSymbolPosition);
        quantity.setUnitSymbolPosition(unitSymbolPositionEnum);

        String unitMultiplierCodelistUrn = columns[header.getUnitMultiplierCodelistUrnPosition()];
        String unitMultiplierCode = columns[header.getUnitMultiplierCodePosition()];
        if (unitMultiplierCodelistUrn != null && unitMultiplierCode != null) {
            CodeMetamac code = findCodesByCodelistUrnByCondition(ctx, unitMultiplierCodelistUrn, unitMultiplierCode);
            quantity.setUnitMultiplier(code);
        }

        String intValue = columns[header.getSignificantDigitsPosition()];
        if (!StringUtils.isBlank(intValue)) {
            try {
                quantity.setSignificantDigits(Integer.valueOf(intValue));
            } catch (IllegalArgumentException e) {
                quantity.setSignificantDigits(null);
                MetamacExceptionItem exceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
                exceptionItem.getExceptionItems()
                        .add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_FILE_PARSING, ServiceExceptionParameters.CONCEPT_QUANTITY_SIGNIFICANT_DIGITS));
                exceptionItems.add(exceptionItem);
            }
        } else {
            quantity.setSignificantDigits(null);
        }

        intValue = columns[header.getDecimalPlacesPosition()];
        if (!StringUtils.isBlank(intValue)) {
            try {
                quantity.setDecimalPlaces(Integer.valueOf(intValue));
            } catch (IllegalArgumentException e) {
                quantity.setDecimalPlaces(null);
                MetamacExceptionItem exceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
                exceptionItem.getExceptionItems().add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_FILE_PARSING, ServiceExceptionParameters.CONCEPT_QUANTITY_DECIMAL_PLACES));
                exceptionItems.add(exceptionItem);
            }
        } else {
            quantity.setDecimalPlaces(null);
        }

    }

    private void setIndexBaseType(ServiceContext ctx, int lineNumber, ImportationConceptsTsvHeader header, String[] columns, Quantity quantity, List<MetamacExceptionItem> exceptionItems)
            throws MetamacException {
        String intValue = columns[header.getIndexBaseValuePosition()];
        if (!StringUtils.isBlank(intValue)) {
            try {
                quantity.setBaseValue(Integer.valueOf(intValue));
            } catch (IllegalArgumentException e) {
                quantity.setBaseValue(null);
                MetamacExceptionItem exceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
                exceptionItem.getExceptionItems().add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_FILE_PARSING, ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_VALUE));
                exceptionItems.add(exceptionItem);
            }
            return;
        } else {
            quantity.setBaseValue(null);
        }

        String indexBaseTime = columns[header.getIndexBaseTimePosition()];
        if (!StringUtils.isBlank(indexBaseTime)) {
            quantity.setBaseTime(indexBaseTime);
            return;
        } else {
            quantity.setBaseTime(null);
        }

        String indexBaseLocationCodelistUrn = columns[header.getIndexBaseLocationCodelistUrnPosition()];
        String indexBaseLocationCode = columns[header.getIndexBaseLocationCodePosition()];
        if (indexBaseLocationCodelistUrn != null && indexBaseLocationCode != null) {
            CodeMetamac code = findCodesByCodelistUrnByCondition(ctx, indexBaseLocationCodelistUrn, indexBaseLocationCode);
            quantity.setBaseLocation(code);
        } else {
            quantity.setBaseLocation(null);
        }
    }

    private void sortConceptsByOrder(List<Concept> allItemsToUpdate) {
        if (CollectionUtils.isEmpty(allItemsToUpdate)) {
            return;
        }

        List<Concept> conceptsOrdered = new ArrayList<Concept>(allItemsToUpdate);
        Collections.sort(conceptsOrdered, new OrderComparator());

        String previousParentUrn = null;
        int previousOrder = -1;
        for (Concept concept : conceptsOrdered) {

            ConceptMetamac conceptMetamac = (ConceptMetamac) concept;

            String actualParentUrn = conceptMetamac.getParent() != null ? conceptMetamac.getParent().getNameableArtefact().getUrn() : null;
            if (!StringUtils.equals(previousParentUrn, actualParentUrn)) {
                previousOrder = -1; // another level
                previousParentUrn = conceptMetamac.getParent() != null ? conceptMetamac.getParent().getNameableArtefact().getUrn() : null;
            }
            int order = previousOrder == -1 ? 0 : previousOrder + 1;
            conceptMetamac.setOrderValue(order);
            previousOrder = order;
        }
    }

    private class OrderComparator implements Comparator<Concept> {

        @Override
        public int compare(Concept c1, Concept c2) {
            ConceptMetamac cm1 = (ConceptMetamac) c1;
            ConceptMetamac cm2 = (ConceptMetamac) c2;

            if (isConceptsSiblings(cm1, cm2)) {

                Integer cm1OrderValue = cm1.getOrderValue();
                Integer cm2OrderValue = cm2.getOrderValue();
                if (cm1OrderValue == null && cm2OrderValue == null) {
                    return calculeOrderConceptsSiblingsAlphabetical(cm1, cm2);
                } else if (cm1OrderValue == null) {
                    return 1;
                } else if (cm2OrderValue == null) {
                    return -1;
                } else {
                    return cm1OrderValue.compareTo(cm2OrderValue);
                }
            } else if (cm1.getParent() == null) {
                return 1;
            } else if (cm2.getParent() == null) {
                return -1;
            } else {
                return cm1.getParent().getUuid().compareTo(cm2.getParent().getUuid());
            }
        }
    };

    private int calculeOrderConceptsSiblingsAlphabetical(Item i1, Item i2) {
        return i1.getNameableArtefact().getCode().compareToIgnoreCase(i2.getNameableArtefact().getCode());
    }
    private boolean isConceptsSiblings(Item i1, Item i2) {
        if (i1.getParent() == null && i2.getParent() == null) {
            return true;
            // id can be null when save operation is executed when transaction is closed
        } else if (i1.getParent() != null && i2.getParent() != null && i1.getParent().getUuid().equals(i2.getParent().getUuid())) {
            return true;
        }
        return false;
    }

    private ConceptType tsvLineToConceptType(ServiceContext ctx, ImportationConceptsTsvHeader conceptTsvHeader, String[] columns, Map<String, ConceptType> cachedConceptTypeMap)
            throws MetamacException {
        String conceptTypeIdentifier = columns[conceptTsvHeader.getConceptTypePosition()];
        if (!StringUtils.isBlank(conceptTypeIdentifier)) {
            if (cachedConceptTypeMap.containsKey(conceptTypeIdentifier)) {
                return cachedConceptTypeMap.get(conceptTypeIdentifier);
            } else {
                ConceptType conceptType = retrieveConceptTypeByIdentifier(ctx, conceptTypeIdentifier);
                cachedConceptTypeMap.put(conceptTypeIdentifier, conceptType);
                return conceptType;
            }
        }
        return null;
    }

    private Representation tsvLineToRepresentation(ServiceContext ctx, RepresentationsTsv representationsTsv, String[] columns, int lineNumber, String conceptIdentifier, ConceptMetamac conceptMetamac,
            List<MetamacExceptionItem> exceptionItems) {
        if (representationsTsv == null) {
            return null;
        }

        // Representation
        Representation target = conceptMetamac.getCoreRepresentation();
        String representationType = columns[representationsTsv.getTypePosition()];
        String representationValue = columns[representationsTsv.getValuePosition()];
        if (!StringUtils.isBlank(representationType)) {
            if (target == null) {
                target = new Representation();
            }

            if (StringUtils.isBlank(representationValue)) {
                MetamacExceptionItem exceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
                exceptionItem.getExceptionItems().add(
                        new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_REQUIRED, conceptIdentifier, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_REPRESENTATION_VALUE));
                exceptionItems.add(exceptionItem);
                return null;
            }

            RepresentationEum representationEum = RepresentationsTsv.RepresentationEum.valueOf(representationType);
            if (RepresentationEum.ENUMERATED.equals(representationEum)) {
                target.setRepresentationType(RepresentationTypeEnum.ENUMERATION);
                if (SdmxSrmUrnParserUtils.isCodelistUrn(representationValue)) {
                    CodelistVersionMetamac enumeratedCodelist = codelistVersionMetamacRepository.findByUrn(representationValue);
                    if (enumeratedCodelist != null && (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(enumeratedCodelist.getLifeCycleMetadata().getProcStatus())
                            || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(enumeratedCodelist.getLifeCycleMetadata().getProcStatus()))) {
                        target.setEnumerationCodelist(enumeratedCodelist);
                    } else {
                        MetamacExceptionItem exceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
                        exceptionItem.getExceptionItems().add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_CODELIST_NOT_FOUND, representationValue));
                        exceptionItems.add(exceptionItem);
                        return null;
                    }
                } else if (SdmxSrmUrnParserUtils.isConceptSchemeUrn(representationValue)) {
                    ConceptSchemeVersionMetamac enumeratedConceptScheme = conceptSchemeVersionMetamacRepository.findByUrn(representationValue);
                    if (enumeratedConceptScheme != null && (ProcStatusEnum.INTERNALLY_PUBLISHED.equals(enumeratedConceptScheme.getLifeCycleMetadata().getProcStatus())
                            || ProcStatusEnum.EXTERNALLY_PUBLISHED.equals(enumeratedConceptScheme.getLifeCycleMetadata().getProcStatus()))) {
                        target.setEnumerationConceptScheme(enumeratedConceptScheme);
                    } else {
                        MetamacExceptionItem exceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
                        exceptionItem.getExceptionItems().add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_CONCEPT_SCHEME_NOT_FOUND, representationValue));
                        exceptionItems.add(exceptionItem);
                        return null;
                    }
                } else {
                    MetamacExceptionItem exceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
                    exceptionItem.getExceptionItems().add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_INCORRECT, conceptIdentifier,
                            ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_REPRESENTATION_VALUE));
                    exceptionItems.add(exceptionItem);
                    return null;
                }
            } else {
                target.setRepresentationType(RepresentationTypeEnum.TEXT_FORMAT);
                // Facet
                target.setTextFormat(new Facet(FacetValueTypeEnum.fromValue(representationValue)));
            }
        }

        return target;
    }

    private ConceptMetamac tsvLineToConceptExtends(ServiceContext ctx, ImportationConceptsTsvHeader conceptTsvHeader, String[] columns, int lineNumber, String conceptIdentifier,
            List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        String conceptExtendsUrn = columns[conceptTsvHeader.getConceptExtendsPosition()];
        if (!StringUtils.isBlank(conceptExtendsUrn)) {
            List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.nameableArtefact().urn()).eq(conceptExtendsUrn)
                    .build();

            PagedResult<ConceptMetamac> result = findConceptsCanBeExtendedByCondition(ctx, conditions, PagingParameter.pageAccess(1, 1));
            if (result.getValues().size() != 1) {
                MetamacExceptionItem exceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
                exceptionItem.getExceptionItems().add(
                        new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_INCORRECT, conceptIdentifier, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CONCEPTS_EXTENDS_VALUE));
                exceptionItems.add(exceptionItem);
                return null;
            }
            return result.getValues().get(0);
        }

        return null;
    }

    private InternationalString tsvLineToPluralName(ImportationConceptsTsvHeader conceptTsvHeader, String[] columns, boolean isDefaultMaintainer, ConceptSchemeVersionMetamac conceptSchemeVersion,
            ConceptMetamac concept, List<MetamacExceptionItem> exceptionItems) throws MetamacException {
        try {
            InternationalString pluralName = TsvImportationUtils.tsvLineToInternationalStringBase(conceptTsvHeader.getPluralName(), columns, concept.getPluralName(), isDefaultMaintainer);
            checkConceptPluralName(conceptSchemeVersion.getType(), pluralName);

            return pluralName;
        } catch (MetamacException e) {
            exceptionItems.addAll(e.getExceptionItems());
            return null;
        }
    }

    @Override
    public String exportConceptsTsv(ServiceContext ctx, String conceptSchemetUrn) throws MetamacException {

        ConceptsMetamacInvocationValidator.checkExportConceptsTsv(conceptSchemetUrn, null);

        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, conceptSchemetUrn);

        List<ItemResult> items = getConceptMetamacRepository().findConceptsByConceptSchemeOrderedInDepth(conceptSchemeVersion.getId(), ConceptMetamacResultSelection.EXPORT);

        List<String> languages = srmConfiguration.retrieveLanguages();

        return TsvExportationUtils.exportConcepts(conceptSchemeVersion, items, languages);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {
        PagedResult<Concept> conceptsPagedResult = conceptsService.findConceptsByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptToMetamac(conceptsPagedResult);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsCanBeRoleByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).distinctRoot().build();
        }
        // Add restrictions to be Role
        // concept scheme must be Role
        ConditionalCriteria roleCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class)
                .withProperty(
                        new LeafProperty<ConceptMetamac>(ConceptMetamacProperties.itemSchemeVersion().getName(), ConceptSchemeVersionMetamacProperties.type().getName(), true, ConceptMetamac.class))
                .eq(ConceptSchemeTypeEnum.ROLE).buildSingle();
        conditions.add(roleCondition);
        // Concept scheme internally or externally published
        ConditionalCriteria publishedCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class)
                .withProperty(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE).buildSingle();
        conditions.add(publishedCondition);

        PagedResult<Concept> conceptsPagedResult = conceptsService.findConceptsByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptToMetamac(conceptsPagedResult);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsCanBeExtendedByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).distinctRoot().build();
        }
        // concept scheme must be Glossary
        ConditionalCriteria roleCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class)
                .withProperty(
                        new LeafProperty<ConceptMetamac>(ConceptMetamacProperties.itemSchemeVersion().getName(), ConceptSchemeVersionMetamacProperties.type().getName(), true, ConceptMetamac.class))
                .eq(ConceptSchemeTypeEnum.GLOSSARY).buildSingle();
        conditions.add(roleCondition);
        // Concept scheme internally or externally published
        ConditionalCriteria publishedCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class)
                .withProperty(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE).buildSingle();
        conditions.add(publishedCondition);

        PagedResult<Concept> conceptsPagedResult = conceptsService.findConceptsByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptToMetamac(conceptsPagedResult);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsCanBeQuantityNumeratorByCondition(ServiceContext ctx, String conceptSchemeUrn, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {
        return findConceptsCanBeQuantityByCondition(ctx, conceptSchemeUrn, conditions, pagingParameter);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsCanBeQuantityDenominatorByCondition(ServiceContext ctx, String conceptSchemeUrn, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter) throws MetamacException {
        return findConceptsCanBeQuantityByCondition(ctx, conceptSchemeUrn, conditions, pagingParameter);
    }

    @Override
    public PagedResult<ConceptMetamac> findConceptsCanBeQuantityBaseQuantityByCondition(ServiceContext ctx, String conceptSchemeUrn, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter) throws MetamacException {
        return findConceptsCanBeQuantityByCondition(ctx, conceptSchemeUrn, conditions, pagingParameter);
    }

    @Override
    public PagedResult<CodeMetamac> findCodesCanBeQuantityUnitByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class).distinctRoot().build();
        }
        // codelist internally or externally published
        ConditionalCriteria publishedCondition = ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class)
                .withProperty(CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE).buildSingle();
        conditions.add(publishedCondition);
        // Only codelists with access == public
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class)
                .withProperty(new LeafProperty<CodeMetamac>(CodeMetamacProperties.itemSchemeVersion().getName(), CodelistVersionMetamacProperties.accessType().getName(), false, CodeMetamac.class))
                .eq(AccessTypeEnum.PUBLIC).buildSingle());

        return codesMetamacService.findCodesByCondition(ctx, conditions, pagingParameter);
    }

    @Override
    public PagedResult<CodeMetamac> findCodesCanBeQuantityBaseLocationByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions, PagingParameter pagingParameter) throws MetamacException {

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class).distinctRoot().build();
        }
        // codelist internally or externally published
        ConditionalCriteria publishedCondition = ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class)
                .withProperty(CodeMetamacProperties.itemSchemeVersion().maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE).buildSingle();
        conditions.add(publishedCondition);
        // codelist with access == public
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class)
                .withProperty(new LeafProperty<CodeMetamac>(CodeMetamacProperties.itemSchemeVersion().getName(), CodelistVersionMetamacProperties.accessType().getName(), false, CodeMetamac.class))
                .eq(AccessTypeEnum.PUBLIC).buildSingle());
        // codelist with geographical variable
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class)
                .withProperty(
                        new LeafProperty<CodeMetamac>(CodeMetamacProperties.itemSchemeVersion().getName(), CodelistVersionMetamacProperties.variable().type().getName(), false, CodeMetamac.class))
                .eq(VariableTypeEnum.GEOGRAPHICAL).buildSingle());

        return codesMetamacService.findCodesByCondition(ctx, conditions, pagingParameter);
    }

    @Override
    public void deleteConcept(ServiceContext ctx, String urn) throws MetamacException {

        ConceptMetamac concept = retrieveConceptByUrn(ctx, urn);
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByConceptUrn(ctx, urn);
        checkConceptSchemeCanBeModified(conceptSchemeVersion);
        srmValidation.checkItemsStructureCanBeModified(ctx, conceptSchemeVersion);
        checkNoQuantityRelated(concept);

        // Before delete concept, update order of other concepts in level
        getConceptMetamacRepository().reorderConceptsDeletingOneConcept(conceptSchemeVersion, concept);

        // Delete bidirectional relations of concepts relate this concept and its children (will be removed in cascade)
        removeRelatedConceptsBidirectional(concept);

        // note: do not check if it is role, extends or quantity of another concept, because one concept must be published to be related to another resource

        conceptsService.deleteConcept(ctx, urn);
    }

    @Override
    public List<ConceptMetamacVisualisationResult> retrieveConceptsByConceptSchemeUrn(ServiceContext ctx, String conceptSchemeUrn, String locale) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkRetrieveConceptsByConceptSchemeUrn(conceptSchemeUrn, locale, null);

        // Retrieve
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, conceptSchemeUrn);
        return getConceptMetamacRepository().findConceptsByConceptSchemeUnorderedToVisualisation(conceptSchemeVersion.getId(), locale);
    }

    @Override
    public List<ItemResult> retrieveConceptsByConceptSchemeUrnOrderedInDepth(ServiceContext ctx, String conceptSchemeUrn, ConceptMetamacResultSelection itemMetamacResultSelection)
            throws MetamacException {
        // Validation
        ConceptsMetamacInvocationValidator.checkRetrieveConceptsByConceptSchemeUrnOrderedInDepth(conceptSchemeUrn, itemMetamacResultSelection, null);

        if (itemMetamacResultSelection == null) {
            itemMetamacResultSelection = ConceptMetamacResultSelection.RETRIEVE; // default
        }
        ConceptSchemeVersionMetamac conceptSchemeVersion = retrieveConceptSchemeByUrn(ctx, conceptSchemeUrn);
        return getConceptMetamacRepository().findConceptsByConceptSchemeOrderedInDepth(conceptSchemeVersion.getId(), itemMetamacResultSelection);
    }

    @Override
    public void checkConceptSchemeVersionTranslations(ServiceContext ctx, Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByResourceUrn)
            throws MetamacException {
        getConceptSchemeVersionMetamacRepository().checkConceptSchemeVersionTranslations(itemSchemeVersionId, locale, exceptionItemsByResourceUrn);
        getConceptMetamacRepository().checkConceptTranslations(itemSchemeVersionId, locale, exceptionItemsByResourceUrn);
    }

    @Override
    public void addRelatedConcept(ServiceContext ctx, String urn1, String urn2) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkAddRelatedConcept(urn1, urn2, null);

        // Not same concept
        if (urn1.equals(urn2)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PARAMETER_INCORRECT).withMessageParameters("Concepts must be different").build();
        }
        ConceptMetamac concept1 = retrieveConceptByUrn(ctx, urn1);
        ConceptMetamac concept2 = retrieveConceptByUrn(ctx, urn2);
        // Same concept scheme
        if (!concept1.getItemSchemeVersion().getId().equals(concept2.getItemSchemeVersion().getId())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.PARAMETER_INCORRECT).withMessageParameters("Concept scheme must be same in two concepts").build();
        }
        // Concept scheme not published
        ItemSchemeVersion conceptSchemeVersion = concept1.getItemSchemeVersion();// note: itemScheme is the same to two concepts
        retrieveConceptSchemeVersionCanBeModified(ctx, conceptSchemeVersion.getMaintainableArtefact().getUrn());

        // Not add relation if is already added
        for (Concept conceptRelatedActual : concept1.getRelatedConcepts()) {
            if (conceptRelatedActual.getNameableArtefact().getUrn().equals(concept2.getNameableArtefact().getUrn())) {
                return;
            }
        }

        // Create bidirectional relation
        concept1.addRelatedConcept(concept2);
        getConceptMetamacRepository().save(concept1);
        concept2.addRelatedConcept(concept1);
        getConceptMetamacRepository().save(concept2);
        // Mark last update date
        baseService.updateItemSchemeLastUpdated(ctx, conceptSchemeVersion);
    }

    @Override
    public void deleteRelatedConcept(ServiceContext ctx, String urn1, String urn2) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkDeleteRelatedConcept(urn1, urn2, null);

        // Concept scheme not published
        ConceptMetamac concept1 = retrieveConceptByUrn(ctx, urn1);
        ItemSchemeVersion conceptSchemeVersion = concept1.getItemSchemeVersion();
        retrieveConceptSchemeVersionCanBeModified(ctx, conceptSchemeVersion.getMaintainableArtefact().getUrn());

        ConceptMetamac concept2 = retrieveConceptByUrn(ctx, urn2);

        // Delete
        concept1.removeRelatedConcept(concept2);
        getConceptMetamacRepository().save(concept1);
        concept2.removeRelatedConcept(concept1);
        getConceptMetamacRepository().save(concept2);
        // Mark last update date
        baseService.updateItemSchemeLastUpdated(ctx, conceptSchemeVersion);
    }

    @Override
    public List<ConceptMetamac> retrieveRelatedConcepts(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkRetrieveRelatedConcepts(urn, null);

        // Retrieve
        ConceptMetamac concept = retrieveConceptByUrn(ctx, urn);
        return concept.getRelatedConcepts();
    }

    @Override
    public void addRoleConcept(ServiceContext ctx, String urn, String conceptRoleUrn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkAddRoleConcept(urn, conceptRoleUrn, null);
        ConceptMetamac concept = retrieveConceptByUrn(ctx, urn);
        ItemSchemeVersion conceptSchemeVersion = concept.getItemSchemeVersion();
        ConceptSchemeVersionMetamac conceptSchemeVersionOfConcept = retrieveConceptSchemeVersionCanBeModified(ctx, conceptSchemeVersion.getMaintainableArtefact().getUrn());

        // Check type of concept scheme source
        if (!ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeVersionOfConcept.getType()) && !ConceptSchemeTypeEnum.TRANSVERSAL.equals(conceptSchemeVersionOfConcept.getType())
                && !ConceptSchemeTypeEnum.MEASURE.equals(conceptSchemeVersionOfConcept.getType())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_UNEXPECTED).withMessageParameters(ServiceExceptionParameters.CONCEPT_ROLE).build();
        }

        // Check role
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        ConceptMetamac conceptRole = retrieveConceptByUrn(ctx, conceptRoleUrn);
        Long conceptRoleId = conceptRole.getId();
        List<ConditionalCriteria> criteriaToVerifyConceptRole = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.id()).eq(conceptRoleId).build();
        PagedResult<ConceptMetamac> result = findConceptsCanBeRoleByCondition(ctx, criteriaToVerifyConceptRole, pagingParameter);
        if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(conceptRoleId)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.CONCEPT_ROLE).build();
        }

        // Add Role
        // Not add relation if Role is already added
        for (Concept conceptRoleActual : concept.getRoleConcepts()) {
            if (conceptRoleActual.getId().equals(conceptRole.getId())) {
                return;
            }
        }
        concept.addRoleConcept(conceptRole);
        getConceptMetamacRepository().save(concept);

        // Mark last update date
        baseService.updateItemSchemeLastUpdated(ctx, conceptSchemeVersionOfConcept);
    }

    @Override
    public void deleteRoleConcept(ServiceContext ctx, String urn, String conceptRoleUrn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkDeleteRoleConcept(urn, conceptRoleUrn, null);
        ConceptMetamac concept = retrieveConceptByUrn(ctx, urn);
        ItemSchemeVersion conceptSchemeVersion = concept.getItemSchemeVersion();
        retrieveConceptSchemeVersionCanBeModified(ctx, conceptSchemeVersion.getMaintainableArtefact().getUrn());

        // Delete role
        ConceptMetamac conceptRole = retrieveConceptByUrn(ctx, conceptRoleUrn);
        concept.removeRoleConcept(conceptRole);
        getConceptMetamacRepository().save(concept);

        // Mark last update date
        baseService.updateItemSchemeLastUpdated(ctx, conceptSchemeVersion);
    }

    @Override
    public List<ConceptMetamac> retrieveRoleConcepts(ServiceContext ctx, String urn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkRetrieveRoleConcepts(urn, null);

        // Retrieve
        ConceptMetamac concept = retrieveConceptByUrn(ctx, urn);
        return concept.getRoleConcepts();
    }

    @Override
    public List<ConceptType> findAllConceptTypes(ServiceContext ctx) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkFindAllConceptTypes(null);

        // Find
        return getConceptTypeRepository().findAll();
    }

    @Override
    public ConceptType retrieveConceptTypeByIdentifier(ServiceContext ctx, String identifier) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkRetrieveConceptTypeByIdentifier(identifier, null);

        // Retrieve
        ConceptType conceptType = getConceptTypeRepository().findByIdentifier(identifier);
        if (conceptType == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_TYPE_NOT_FOUND).withMessageParameters(identifier).build();
        }
        return conceptType;
    }

    @Override
    public ConceptSchemeVersionMetamac retrieveConceptSchemeByConceptUrn(ServiceContext ctx, String conceptUrn) throws MetamacException {
        // Validation
        ConceptsMetamacInvocationValidator.checkRetrieveConceptSchemeByConceptUrn(conceptUrn, null);

        // Retrieve
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptSchemeVersionMetamacRepository().findByConcept(conceptUrn);
        if (conceptSchemeVersion == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(conceptUrn).build();
        }
        return conceptSchemeVersion;
    }

    @Override
    public PagedResult<CodelistVersionMetamac> findCodelistsCanBeEnumeratedRepresentationForConceptByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, String conceptUrn, String variableUrn) throws MetamacException {

        // Validation
        ConceptsMetamacInvocationValidator.checkFindCodelistsCanBeEnumeratedRepresentationForConceptByCondition(conditions, pagingParameter, conceptUrn, variableUrn, null);

        // Find
        Variable variable = null;
        if (conceptUrn != null) {
            ConceptMetamac concept = retrieveConceptByUrn(ctx, conceptUrn);
            variable = concept.getVariable();
        } else if (variableUrn != null) {
            variable = codesMetamacService.retrieveVariableByUrn(ctx, variableUrn);
        }
        Long variableId = variable != null ? variable.getId() : null;
        return findCodelistsCanBeEnumeratedRepresentationForConceptByCondition(ctx, conditions, pagingParameter, variableId);
    }

    @SuppressWarnings({"unchecked"})
    private PagedResult<CodelistVersionMetamac> findCodelistsCanBeEnumeratedRepresentationForConceptByCondition(ServiceContext ctx, List<ConditionalCriteria> conditions,
            PagingParameter pagingParameter, Long variableId) throws MetamacException {

        if (variableId == null) {
            return SrmServiceUtils.pagedResultZeroResults(pagingParameter);
        }
        // Prepare conditions
        if (conditions == null) {
            conditions = new ArrayList<ConditionalCriteria>();
        }
        // Codelist internally or externally published
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE)
                .buildSingle());
        // Codelist with access type = PUBLIC
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.accessType()).eq(AccessTypeEnum.PUBLIC).buildSingle());
        // Same variable
        conditions.add(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).withProperty(CodelistVersionMetamacProperties.variable().id()).eq(variableId).buildSingle());

        // Find
        conditions.addAll(ConditionalCriteriaBuilder.criteriaFor(CodelistVersionMetamac.class).distinctRoot().build());
        return codelistVersionMetamacRepository.findByCondition(conditions, pagingParameter); // call to Metamac Repository to avoid ClassCastException
    }

    /**
     * Retrieves version of a concept scheme, checking that can be modified
     */
    private ConceptSchemeVersionMetamac retrieveConceptSchemeVersionCanBeModified(ServiceContext ctx, String urn) throws MetamacException {
        return getConceptSchemeVersionMetamacRepository().retrieveConceptSchemeVersionByProcStatus(urn,
                new ProcStatusEnum[]{ProcStatusEnum.DRAFT, ProcStatusEnum.VALIDATION_REJECTED, ProcStatusEnum.PRODUCTION_VALIDATION, ProcStatusEnum.DIFFUSION_VALIDATION});
    }

    /**
     * Typecast to Metamac type
     */
    private List<ConceptMetamac> conceptsToConceptMetamac(List<Concept> sources) {
        List<ConceptMetamac> targets = new ArrayList<ConceptMetamac>(sources.size());
        for (Item source : sources) {
            targets.add((ConceptMetamac) source);
        }
        return targets;
    }

    /**
     * Typecast to Metamac type
     */
    private List<ConceptSchemeVersionMetamac> conceptSchemeVersionsToConceptSchemeVersionsMetamac(List<ConceptSchemeVersion> sources) {
        List<ConceptSchemeVersionMetamac> targets = new ArrayList<ConceptSchemeVersionMetamac>(sources.size());
        for (ItemSchemeVersion source : sources) {
            targets.add((ConceptSchemeVersionMetamac) source);
        }
        return targets;
    }

    /**
     * Typecast to Metamac type
     */
    private PagedResult<ConceptSchemeVersionMetamac> pagedResultConceptSchemeVersionToMetamac(PagedResult<ConceptSchemeVersion> source) {
        List<ConceptSchemeVersionMetamac> conceptSchemeVersionsMetamac = conceptSchemeVersionsToConceptSchemeVersionsMetamac(source.getValues());
        return new PagedResult<ConceptSchemeVersionMetamac>(conceptSchemeVersionsMetamac, source.getStartRow(), source.getRowCount(), source.getPageSize(), source.getTotalRows(),
                source.getAdditionalResultRows());
    }

    /**
     * Typecast to Metamac type
     */
    private PagedResult<ConceptMetamac> pagedResultConceptToMetamac(PagedResult<Concept> source) {
        List<ConceptMetamac> conceptsMetamac = conceptsToConceptMetamac(source.getValues());
        return new PagedResult<ConceptMetamac>(conceptsMetamac, source.getStartRow(), source.getRowCount(), source.getPageSize(), source.getTotalRows(), source.getAdditionalResultRows());
    }

    private void checkConceptSchemeToVersioning(ServiceContext ctx, String urnToCopy, boolean isTemporal) throws MetamacException {

        ConceptSchemeVersionMetamac conceptSchemeVersionToCopy = retrieveConceptSchemeByUrn(ctx, urnToCopy);

        if (isTemporal) {
            // Check version to copy is published
            SrmValidationUtils.checkArtefactCanBeVersionedAsTemporal(conceptSchemeVersionToCopy.getMaintainableArtefact(), conceptSchemeVersionToCopy.getLifeCycleMetadata());
        } else {
            // Check version to copy is published and not imported
            SrmValidationUtils.checkArtefactCanBeVersioned(conceptSchemeVersionToCopy.getMaintainableArtefact(), conceptSchemeVersionToCopy.getLifeCycleMetadata(), isTemporal);
        }

        // Check does not exist any version 'no final'
        ItemSchemeVersion conceptSchemeVersionNoFinal = itemSchemeVersionRepository.findItemSchemeVersionNoFinalClient(conceptSchemeVersionToCopy.getItemScheme().getId());
        if (conceptSchemeVersionNoFinal != null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.MAINTAINABLE_ARTEFACT_VERSIONING_NOT_SUPPORTED_VERSION_NOT_PUBLISHED)
                    .withMessageParameters(conceptSchemeVersionNoFinal.getMaintainableArtefact().getUrn()).build();
        }
    }

    private void removeRelatedConceptsBidirectional(ConceptMetamac relatedConceptToRemove) {
        for (ConceptMetamac relatedConcept : relatedConceptToRemove.getRelatedConcepts()) {
            relatedConcept.removeRelatedConcept(relatedConceptToRemove);
            getConceptMetamacRepository().save(relatedConcept);
        }
        // Children (will be removed in cascade)
        for (Item child : relatedConceptToRemove.getChildren()) {
            removeRelatedConceptsBidirectional((ConceptMetamac) child);
        }
    }

    /**
     * When concept is going to be deleted, checks the concept is not as quantity in another concept
     */
    private void checkNoQuantityRelated(ConceptMetamac concept) throws MetamacException {
        Long quantityRelatedId = getQuantityRepository().findOneQuantityRelatedWithConcept(concept.getId());
        if (quantityRelatedId != null) {
            ConceptMetamac conceptQuantity = getConceptMetamacRepository().retrieveConceptWithQuantityId(quantityRelatedId);
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_DELETE_NOT_SUPPORTED_CONCEPT_IN_QUANTITY)
                    .withMessageParameters(concept.getNameableArtefact().getUrn(), conceptQuantity.getNameableArtefact().getUrn()).build();
        }

        // Children (will be removed in cascade)
        for (Item child : concept.getChildren()) {
            checkNoQuantityRelated((ConceptMetamac) child);
        }
    }

    private TaskInfo createVersionOfConceptScheme(ServiceContext ctx, String urnToCopy, ItemSchemesCopyCallback itemSchemesCopyCallback, VersionTypeEnum versionType, boolean isTemporal)
            throws MetamacException {

        // Validation
        checkConceptSchemeToVersioning(ctx, urnToCopy, isTemporal);

        // Versioning
        return conceptsService.versioningConceptScheme(ctx, urnToCopy, versionType, isTemporal, itemSchemesCopyCallback);
    }

    private void versioningRelatedConcepts(ConceptMetamac conceptToCopy, ConceptSchemeVersionMetamac conceptSchemeNewVersion) throws MetamacException {
        if (conceptToCopy.getRelatedConcepts().size() == 0) {
            return;
        }
        ConceptMetamac conceptInNewVersion = (ConceptMetamac) conceptRepository.findByCodeInConceptSchemeVersion(conceptToCopy.getNameableArtefact().getCode(),
                conceptSchemeNewVersion.getMaintainableArtefact().getUrn());

        if (conceptInNewVersion == null) {
            // concept only can not exist in new version when importing: in this case, do not copy related concepts to this concept
            SrmValidationUtils.checkMaintainableArtefactImported(conceptSchemeNewVersion.getMaintainableArtefact());
            return;
        }

        // Copy relations with concepts in new version
        for (ConceptMetamac relatedConcept : conceptToCopy.getRelatedConcepts()) {
            ConceptMetamac relatedConceptIntNewVersion = (ConceptMetamac) conceptRepository.findByCodeInConceptSchemeVersion(relatedConcept.getNameableArtefact().getCode(),
                    conceptSchemeNewVersion.getMaintainableArtefact().getUrn());
            if (relatedConceptIntNewVersion == null) {
                // concept only can not exist in new version when importing: in this case, do not copy
                SrmValidationUtils.checkMaintainableArtefactImported(conceptSchemeNewVersion.getMaintainableArtefact());
                continue;
            }
            conceptInNewVersion.addRelatedConcept(relatedConceptIntNewVersion);
        }
    }

    private void versioningConceptQuantity(ConceptMetamac conceptToCopy, ConceptSchemeVersionMetamac conceptSchemeNewVersion) throws MetamacException {

        ConceptMetamac conceptInNewVersion = (ConceptMetamac) conceptRepository.findByCodeInConceptSchemeVersion(conceptToCopy.getNameableArtefact().getCode(),
                conceptSchemeNewVersion.getMaintainableArtefact().getUrn());
        if (conceptInNewVersion == null) {
            // concept only can not exist in new version when importing: in this case, do not copy quantity to this concept
            SrmValidationUtils.checkMaintainableArtefactImported(conceptSchemeNewVersion.getMaintainableArtefact());
            return;
        }

        if (conceptToCopy.getQuantity() == null) {
            if (conceptInNewVersion.getQuantity() != null) {
                getQuantityRepository().delete(conceptInNewVersion.getQuantity());
                conceptInNewVersion.setQuantity(null);
            }
            return;
        }

        // Copy quantity in new version
        Quantity quantityToCopy = conceptToCopy.getQuantity();
        Long itemSchemeVersionIdToCopy = conceptToCopy.getItemSchemeVersion().getId();

        Quantity quantityCopied = conceptInNewVersion.getQuantity();
        if (quantityCopied == null) {
            quantityCopied = new Quantity();
        }
        quantityCopied.setQuantityType(quantityToCopy.getQuantityType());
        quantityCopied.setUnitCode(quantityToCopy.getUnitCode());
        quantityCopied.setUnitSymbolPosition(quantityToCopy.getUnitSymbolPosition());
        quantityCopied.setSignificantDigits(quantityToCopy.getSignificantDigits());
        quantityCopied.setDecimalPlaces(quantityToCopy.getDecimalPlaces());
        quantityCopied.setUnitMultiplier(quantityToCopy.getUnitMultiplier());
        quantityCopied.setMinimum(quantityToCopy.getMinimum());
        quantityCopied.setMaximum(quantityToCopy.getMaximum());
        quantityCopied.setNumerator(versioningConceptRelatedInQuantity(itemSchemeVersionIdToCopy, conceptSchemeNewVersion, quantityToCopy.getNumerator()));
        quantityCopied.setDenominator(versioningConceptRelatedInQuantity(itemSchemeVersionIdToCopy, conceptSchemeNewVersion, quantityToCopy.getDenominator()));
        quantityCopied.setIsPercentage(quantityToCopy.getIsPercentage());
        quantityCopied.setPercentageOf(BaseCopyAllMetadataUtils.copy(quantityToCopy.getPercentageOf()));
        quantityCopied.setBaseValue(quantityToCopy.getBaseValue());
        quantityCopied.setBaseTime(quantityToCopy.getBaseTime());
        quantityCopied.setBaseLocation(quantityToCopy.getBaseLocation());
        quantityCopied.setBaseQuantity(versioningConceptRelatedInQuantity(itemSchemeVersionIdToCopy, conceptSchemeNewVersion, quantityToCopy.getBaseQuantity()));

        conceptInNewVersion.setQuantity(quantityCopied);
    }

    // IDEA: To improve efficiency we can receive a map in target in order to avoid making the search.
    private ConceptMetamac versioningConceptRelatedInQuantity(Long itemSchemeVersionIdToCopy, ConceptSchemeVersionMetamac conceptSchemeNewVersion, ConceptMetamac conceptInQuantityToCopy)
            throws MetamacException {
        if (conceptInQuantityToCopy == null) {
            return null;
        }
        if (!conceptInQuantityToCopy.getItemSchemeVersion().getId().equals(itemSchemeVersionIdToCopy)) {
            // belong to another concept scheme. So, can copy link
            return conceptInQuantityToCopy;
        } else {
            // belong to same concept scheme
            ConceptMetamac numeratorInNewVersion = (ConceptMetamac) conceptRepository.findByCodeInConceptSchemeVersion(conceptInQuantityToCopy.getNameableArtefact().getCode(),
                    conceptSchemeNewVersion.getMaintainableArtefact().getUrn());
            if (numeratorInNewVersion == null) {
                // concept only can not exist in new version when importing: in this case, do not copy
                SrmValidationUtils.checkMaintainableArtefactImported(conceptSchemeNewVersion.getMaintainableArtefact());
                return null;
            } else {
                return numeratorInNewVersion;
            }
        }
    }

    /**
     * Common validations to create or update a concept scheme
     */
    private void checkConceptSchemeToCreateOrUpdate(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {

        if (conceptSchemeVersion.getId() != null) {
            // Proc status
            checkConceptSchemeCanBeModified(conceptSchemeVersion);

            // Code
            SrmValidationUtils.checkMaintainableArtefactCanChangeCodeIfChanged(conceptSchemeVersion.getMaintainableArtefact());
        }

        // Maintainer
        srmValidation.checkMaintainer(ctx, conceptSchemeVersion.getMaintainableArtefact(), conceptSchemeVersion.getMaintainableArtefact().getIsImported());

        // Type
        // if this version is not the first one, check not modify 'type' respect previous version
        if (!SrmServiceUtils.isItemSchemeFirstVersion(conceptSchemeVersion)) {
            ConceptSchemeVersionMetamac conceptSchemePreviousVersion = (ConceptSchemeVersionMetamac) itemSchemeVersionRepository.retrieveByVersion(conceptSchemeVersion.getItemScheme().getId(),
                    conceptSchemeVersion.getMaintainableArtefact().getReplaceToVersion());
            if (conceptSchemePreviousVersion.getType() != null && !conceptSchemePreviousVersion.getType().equals(conceptSchemeVersion.getType())) {
                throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_UNMODIFIABLE).withMessageParameters(ServiceExceptionParameters.CONCEPT_SCHEME_TYPE).build();
            }
        } else {
            // Type can be modified if type was never established previously or if the scheme has not items (concepts)
            if (conceptSchemeVersion.getId() != null && conceptSchemeVersion.getIsTypeUpdated()) {
                boolean canModifyType = false;
                if (BooleanUtils.isTrue(conceptSchemeVersion.getIsTypeEmptyPreviously())) {
                    canModifyType = true;
                } else {
                    Long itemsCount = conceptRepository.countItems(conceptSchemeVersion.getId());
                    if (itemsCount == 0) {
                        canModifyType = true;
                    }
                }
                if (!canModifyType) {
                    throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_UNMODIFIABLE).withMessageParameters(ServiceExceptionParameters.CONCEPT_SCHEME_TYPE)
                            .build();
                }
            }
        }
    }

    /**
     * Common validations to create or update a concept
     */
    private void checkConceptToCreateOrUpdate(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersion, ConceptMetamac concept) throws MetamacException {
        checkConceptSchemeCanBeModified(conceptSchemeVersion);
        checkConceptMetadataExtends(ctx, conceptSchemeVersion, concept);
        checkConceptRepresentation(ctx, conceptSchemeVersion, concept, true);
        checkConceptQuantity(ctx, conceptSchemeVersion, concept);
        checkConceptPluralName(conceptSchemeVersion, concept);
    }

    private void checkConceptMetadataExtends(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersionSource, ConceptMetamac concept) throws MetamacException {
        if (concept.getConceptExtends() == null) {
            return;
        }

        // Check type of concept scheme source
        if (!ConceptSchemeTypeEnum.GLOSSARY.equals(conceptSchemeVersionSource.getType()) && !ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeVersionSource.getType())
                && !ConceptSchemeTypeEnum.TRANSVERSAL.equals(conceptSchemeVersionSource.getType()) && !ConceptSchemeTypeEnum.MEASURE.equals(conceptSchemeVersionSource.getType())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_UNEXPECTED).withMessageParameters(ServiceExceptionParameters.CONCEPT_EXTENDS).build();
        }

        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        Long conceptExtendId = concept.getConceptExtends().getId();
        List<ConditionalCriteria> criteriaToVerifyConceptExtends = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class)
                // concept extends select must be in result
                .withProperty(ConceptMetamacProperties.id()).eq(conceptExtendId)
                // must belong to different schemes
                .not().withProperty(ConceptMetamacProperties.itemSchemeVersion().itemScheme().id()).eq(conceptSchemeVersionSource.getItemScheme().getId()).build();
        PagedResult<ConceptMetamac> result = findConceptsCanBeExtendedByCondition(ctx, criteriaToVerifyConceptExtends, pagingParameter);
        if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(conceptExtendId)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_INCORRECT).withMessageParameters(ServiceExceptionParameters.CONCEPT_EXTENDS).build();
        }
    }

    /**
     * Numerator, denominator and base quantity can be a concept of same concept scheme or a concept of another concept scheme type measure without operation
     */
    private void checkConceptQuantity(ServiceContext ctx, ConceptSchemeVersionMetamac conceptSchemeVersionSource, ConceptMetamac concept) throws MetamacException {
        Quantity quantity = concept.getQuantity();
        if (quantity == null) {
            return;
        }
        String conceptSchemeVersionSourceUrn = conceptSchemeVersionSource.getMaintainableArtefact().getUrn();
        // Check concept scheme source: type
        if (!ConceptSchemeTypeEnum.MEASURE.equals(conceptSchemeVersionSource.getType())) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.CONCEPT_SCHEME_WRONG_TYPE)
                    .withMessageParameters(conceptSchemeVersionSourceUrn, new String[]{ServiceExceptionParameters.CONCEPT_SCHEME_TYPE_MEASURE}).build();

        }

        // Check quantity concepts and codes
        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        if (quantity.getUnitCode() != null) {
            Long unitCodeId = quantity.getUnitCode().getId();
            List<ConditionalCriteria> criteriaToVerifyUnitCode = ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class).withProperty(CodeMetamacProperties.id()).eq(unitCodeId).build();
            PagedResult<CodeMetamac> result = findCodesCanBeQuantityUnitByCondition(ctx, criteriaToVerifyUnitCode, pagingParameter);
            if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(unitCodeId)) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CONCEPT_QUANTITY_UNIT_CODE);
            }
        }
        if (quantity.getNumerator() != null) {
            Long conceptNumeratorId = quantity.getNumerator().getId();
            List<ConditionalCriteria> criteriaToVerifyNumerator = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.id()).eq(conceptNumeratorId)
                    .build();
            PagedResult<ConceptMetamac> result = findConceptsCanBeQuantityNumeratorByCondition(ctx, conceptSchemeVersionSourceUrn, criteriaToVerifyNumerator, pagingParameter);
            if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(conceptNumeratorId)) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CONCEPT_QUANTITY_NUMERATOR);
            }
        }
        if (quantity.getDenominator() != null) {
            Long conceptDenominatorId = quantity.getDenominator().getId();
            List<ConditionalCriteria> criteriaToVerifyDenominator = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.id()).eq(conceptDenominatorId)
                    .build();
            PagedResult<ConceptMetamac> result = findConceptsCanBeQuantityDenominatorByCondition(ctx, conceptSchemeVersionSourceUrn, criteriaToVerifyDenominator, pagingParameter);
            if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(conceptDenominatorId)) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CONCEPT_QUANTITY_DENOMINATOR);
            }
        }
        if (quantity.getBaseQuantity() != null) {
            Long conceptBaseQuantityId = quantity.getBaseQuantity().getId();
            List<ConditionalCriteria> criteriaToVerifyBaseQuantity = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).withProperty(ConceptMetamacProperties.id()).eq(conceptBaseQuantityId)
                    .build();
            PagedResult<ConceptMetamac> result = findConceptsCanBeQuantityBaseQuantityByCondition(ctx, conceptSchemeVersionSourceUrn, criteriaToVerifyBaseQuantity, pagingParameter);
            if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(conceptBaseQuantityId)) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_QUANTITY);
            }
        }
        if (quantity.getBaseLocation() != null) {
            Long codeBaseLocationId = quantity.getBaseLocation().getId();
            List<ConditionalCriteria> criteriaToVerifyBaseLocation = ConditionalCriteriaBuilder.criteriaFor(CodeMetamac.class).withProperty(CodeMetamacProperties.id()).eq(codeBaseLocationId).build();
            PagedResult<CodeMetamac> result = findCodesCanBeQuantityBaseLocationByCondition(ctx, criteriaToVerifyBaseLocation, pagingParameter);
            if (result.getValues().size() != 1 || !result.getValues().get(0).getId().equals(codeBaseLocationId)) {
                throw new MetamacException(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.CONCEPT_QUANTITY_BASE_LOCATION);
            }
        }
    }

    private void checkConceptPluralName(ConceptSchemeVersionMetamac conceptSchemeVersion, ConceptMetamac concept) throws MetamacException {
        checkConceptPluralName(conceptSchemeVersion.getType(), concept.getPluralName());
    }

    private void checkConceptPluralName(ConceptSchemeTypeEnum conceptSchemeType, InternationalString pluralName) throws MetamacException {
        boolean isTypeOperationOrTransversal = ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeType) || ConceptSchemeTypeEnum.TRANSVERSAL.equals(conceptSchemeType);
        if (isTypeOperationOrTransversal && SrmValidationUtils.isInternationalStringEmpty(pluralName)) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.METADATA_REQUIRED).withMessageParameters(new String[]{ServiceExceptionParameters.CONCEPT_PLURAL_NAME})
                    .build();
        }
    }

    private void checkConceptSchemeCanBeModified(ConceptSchemeVersionMetamac conceptSchemeVersion) throws MetamacException {
        SrmValidationUtils.checkArtefactCanBeModified(conceptSchemeVersion.getLifeCycleMetadata(), conceptSchemeVersion.getMaintainableArtefact().getUrn());
        SrmValidationUtils.checkArtefactWithoutTaskInBackground(conceptSchemeVersion);
    }

    /**
     * At the moment, conditions to be numerator, numerator o base quantity are identical.
     */
    private PagedResult<ConceptMetamac> findConceptsCanBeQuantityByCondition(ServiceContext ctx, String conceptSchemeUrn, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class).distinctRoot().build();
        }
        // Add restrictions to be numerated. IMPORTANT! If any condition change, review findConceptSchemesCanBeQuantity
        // concept scheme can be same scheme or another concept scheme type Measure without operation
        ConditionalCriteria quantityCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class)
                // same scheme
                .withProperty(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().urn()).eq(conceptSchemeUrn).or()
                // in another scheme, internally or externally published and specific type
                .lbrace()
                .withProperty(
                        new LeafProperty<ConceptMetamac>(ConceptMetamacProperties.itemSchemeVersion().getName(), ConceptSchemeVersionMetamacProperties.type().getName(), true, ConceptMetamac.class))
                .eq(ConceptSchemeTypeEnum.MEASURE).and()
                .withProperty(new LeafProperty<ConceptMetamac>(ConceptMetamacProperties.itemSchemeVersion().getName(), ConceptSchemeVersionMetamacProperties.relatedOperation().getName(), true,
                        ConceptMetamac.class))
                .isNull().and().withProperty(ConceptMetamacProperties.itemSchemeVersion().maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE).rbrace().buildSingle();
        conditions.add(quantityCondition);

        PagedResult<Concept> conceptsPagedResult = conceptsService.findConceptsByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptToMetamac(conceptsPagedResult);
    }

    /**
     * At the moment, conditions to be numerator, numerator o base quantity are identical
     */
    private PagedResult<ConceptSchemeVersionMetamac> findConceptSchemesCanBeQuantity(ServiceContext ctx, String conceptSchemeUrn, List<ConditionalCriteria> conditions, PagingParameter pagingParameter)
            throws MetamacException {

        // Find
        if (conditions == null) {
            conditions = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class).distinctRoot().build();
        }
        // Add restrictions to be numerated. IMPORTANT! If any condition change, review findConceptsCanBeQuantity
        // concept scheme can be same scheme or another concept scheme type Measure without operation
        ConditionalCriteria quantityCondition = ConditionalCriteriaBuilder.criteriaFor(ConceptSchemeVersionMetamac.class)
                // 1) same scheme
                .withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().urn()).eq(conceptSchemeUrn).or()
                // 2) or another scheme...
                .lbrace()
                // 2a) scheme type
                .withProperty(ConceptSchemeVersionMetamacProperties.type()).eq(ConceptSchemeTypeEnum.MEASURE).and().withProperty(ConceptSchemeVersionMetamacProperties.relatedOperation()).isNull()
                // 2b) scheme internally or externally published
                .and().withProperty(ConceptSchemeVersionMetamacProperties.maintainableArtefact().finalLogicClient()).eq(Boolean.TRUE).rbrace().buildSingle();
        conditions.add(quantityCondition);

        PagedResult<ConceptSchemeVersion> conceptSchemeVersionsPagedResult = conceptsService.findConceptSchemesByCondition(ctx, conditions, pagingParameter);
        return pagedResultConceptSchemeVersionToMetamac(conceptSchemeVersionsPagedResult);
    }

    private ConceptSchemeVersionMetamac retrieveConceptSchemeVersionByUrn(String urn) throws MetamacException {
        ConceptsInvocationValidator.checkRetrieveByUrn(urn);
        ConceptSchemeVersionMetamac conceptSchemeVersion = getConceptSchemeVersionMetamacRepository().findByUrn(urn);
        if (conceptSchemeVersion == null) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IDENTIFIABLE_ARTEFACT_NOT_FOUND).withMessageParameters(urn).build();
        }
        return conceptSchemeVersion;
    }

    private void saveConceptsEfficiently(Collection<ConceptMetamac> conceptsToPersist, Map<String, Item> conceptsToPersistByCode) {
        for (ConceptMetamac conceptMetamac : conceptsToPersist) {
            if (conceptMetamac.getParent() != null) {
                if (conceptsToPersistByCode.containsKey(conceptMetamac.getParent().getNameableArtefact().getCode())) {
                    // update reference because it was saved
                    conceptMetamac.setParent(conceptsToPersistByCode.get(conceptMetamac.getParent().getNameableArtefact().getCode()));
                }
            }
            conceptMetamac = getConceptMetamacRepository().save(conceptMetamac);
            // update reference after save to assign to children
            conceptsToPersistByCode.put(conceptMetamac.getNameableArtefact().getCode(), conceptMetamac);
        }
    }

    @Override
    public void updateConceptParent(ServiceContext ctx, String conceptUrn, String newConceptParentUrn, Integer newConceptIndex) throws MetamacException {
        // Validation
        ConceptsMetamacInvocationValidator.checkUpdateConceptParent(conceptUrn, newConceptParentUrn, null);
        ConceptSchemeVersionMetamac conceptSchemeVersionMetamac = retrieveConceptSchemeByConceptUrn(ctx, conceptUrn);
        checkConceptSchemeCanBeModified(conceptSchemeVersionMetamac);
        srmValidation.checkItemsStructureCanBeModified(ctx, conceptSchemeVersionMetamac);

        ConceptMetamac concept = retrieveConceptByUrn(ctx, conceptUrn);
        Concept parentActual = concept.getParent() != null ? concept.getParent() : null;
        ConceptMetamac parentTarget = newConceptParentUrn != null ? retrieveConceptByUrn(ctx, newConceptParentUrn) : null;
        if (!SdmxSrmUtils.isItemParentChanged(parentActual, parentTarget)) {
            // nothing
            return;
        }

        // Check target parent is not children of this code
        if (newConceptParentUrn != null) {
            checkItemIsNotChildren(ctx, concept, newConceptParentUrn);
        }

        // Update orders in previous level
        getConceptMetamacRepository().reorderConceptsDeletingOneConcept(conceptSchemeVersionMetamac, concept);

        // Update target parent
        if (parentTarget == null) {
            concept.setParent(null);
            concept.setItemSchemeVersionFirstLevel(conceptSchemeVersionMetamac);
        } else {
            concept.setParent(parentTarget);
            concept.setItemSchemeVersionFirstLevel(null);
        }

        // Update orders in new level
        getConceptMetamacRepository().reorderConceptsAddingOneConceptInMiddle(conceptSchemeVersionMetamac, concept, newConceptIndex);

        // Update concept
        concept.setOrderValue(newConceptIndex);
        getConceptMetamacRepository().save(concept);
        baseService.updateItemSchemeLastUpdated(ctx, conceptSchemeVersionMetamac);
    }

    private void checkItemIsNotChildren(ServiceContext ctx, Item item, String newParentUrn) throws MetamacException {
        Item parentTarget = retrieveConceptByUrn(ctx, newParentUrn);
        while (parentTarget != null) {
            if (parentTarget.getId().equals(item.getId())) {
                throw new MetamacException(ServiceExceptionType.PARAMETER_INCORRECT, ServiceExceptionParameters.ITEM_PARENT);
            }
            parentTarget = parentTarget.getParent();
        }
    }

    @Override
    public void updateConceptInOrder(ServiceContext ctx, String conceptUrn, String conceptSchemeUrn, Integer newConceptIndex) throws MetamacException {
        // Validation
        ConceptsMetamacInvocationValidator.checkUpdateConceptInOrder(conceptUrn, conceptSchemeUrn, newConceptIndex, null);
        ConceptSchemeVersionMetamac conceptSchemeVersionMetamac = retrieveConceptSchemeByConceptUrn(ctx, conceptUrn);
        checkConceptSchemeCanBeModified(conceptSchemeVersionMetamac);

        ConceptMetamac concept = retrieveConceptByUrn(ctx, conceptUrn);

        // To avoid unexpected behaviors, only reorder codes if the concept order value changes
        if (newConceptIndex.compareTo(concept.getOrderValue()) != 0) {
            // Reorder concepts in same level
            int numberOfReorderedConceptsDeletingOne = getConceptMetamacRepository().reorderConceptsDeletingOneConcept(conceptSchemeVersionMetamac, concept);
            logger.debug("Number of reordered concepts after deleting one at the old position ({}): {}", concept.getOrderValue(), numberOfReorderedConceptsDeletingOne);

            int numberOfReorderedConceptsAddingOne = getConceptMetamacRepository().reorderConceptsAddingOneConceptInMiddle(conceptSchemeVersionMetamac, concept, newConceptIndex);
            logger.debug("Number of reordered concepts after adding one at the new position ({}): {}", newConceptIndex, numberOfReorderedConceptsAddingOne);

            // Update concept
            concept.setOrderValue(newConceptIndex);
            getConceptMetamacRepository().save(concept);
            baseService.updateItemSchemeLastUpdated(ctx, conceptSchemeVersionMetamac);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<MetamacExceptionItem> resendKafkaMessageExternallyConceptSchemes(ServiceContext ctx) throws MetamacException {
        List<ConceptSchemeVersionMetamac> conceptSchemes = this.getConceptSchemeVersionMetamacRepository().retrieveConceptsSchemesPublishedExternally();
        List<MetamacExceptionItem> exceptionsItems = new ArrayList<MetamacExceptionItem>();

        int totalCount = 0;
        int partialCount = 0;
        logger.info("resend all published last version concept schemes kafka messages start at {} affected concepts: {} ", new Date(), conceptSchemes.size());

        for (ConceptSchemeVersionMetamac conceptScheme : conceptSchemes) {

            try {
                ResourceVersionPublicationResult<ConceptSchemeVersionMetamac> result = (ResourceVersionPublicationResult<ConceptSchemeVersionMetamac>) conceptSchemeLifeCycle.resendMessage(ctx,
                        conceptScheme);

                totalCount++;
                partialCount++;

                if (partialCount >= 50) {
                    partialCount = 0;
                    logger.info("processed concept schemes {} at {}", totalCount, new Date());
                }

                if (!result.isOk()) {
                    logger.info("error sending concept schemes {} error status {} ", conceptScheme.getMaintainableArtefact().getUrn(),
                            result.groupExceptions() != null ? result.groupExceptions().getMessage() : "");
                    exceptionsItems.add(new MetamacExceptionItem(ServiceExceptionType.STREAM_MESSAGING_RESEND_CONCEPT_SCHEME_MESSAGE_ERROR, result.groupExceptions()));
                }

            } catch (MetamacException e) {
                totalCount++;
                partialCount++;
                logger.info("error sending concept schemes {} ", conceptScheme.getMaintainableArtefact().getUrn());
                exceptionsItems.add(new MetamacExceptionItem(ServiceExceptionType.STREAM_MESSAGING_RESEND_CONCEPT_SCHEME_MESSAGE_ERROR, conceptScheme.getMaintainableArtefact().getUrn()));
            }

        }
        logger.info("resend all published last version concept schemes kafka messages end at {} ", new Date());

        return exceptionsItems;
    }

    @Override
    public boolean checkCodelistIsUsedByConcept(ServiceContext ctx, String codelistUrn) throws MetamacException {
        codelistUrn = GeneratorUrnUtils.makeUrnFromTemporal(codelistUrn);
        PagingParameter pagingParameter = PagingParameter.rowAccess(0, Integer.MAX_VALUE, true);
        List<ConditionalCriteria> criteria = ConditionalCriteriaBuilder.criteriaFor(ConceptMetamac.class)
                .withProperty(ConceptMetamacProperties.coreRepresentation().enumerationCodelist().maintainableArtefact().urn()).eq(codelistUrn).build();
        PagedResult<Concept> conceptWithCodelist = conceptsService.findConceptsByCondition(ctx, criteria, pagingParameter);
        return !conceptWithCodelist.getValues().isEmpty();
    }
}