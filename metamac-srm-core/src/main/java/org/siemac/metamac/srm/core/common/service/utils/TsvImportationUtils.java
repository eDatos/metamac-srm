package org.siemac.metamac.srm.core.common.service.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.base.domain.SrmLifeCycleMetadata;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.category.serviceimpl.utils.CategoriesMetamacInvocationValidator;
import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.serviceimpl.utils.CodesMetamacInvocationValidator;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.ConceptsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationMetamac;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.organisation.serviceimpl.utils.OrganisationsMetamacInvocationValidator;
import org.siemac.metamac.srm.core.task.domain.ImportationAnnotationsTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationCategoriesTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationCategoryElementsTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationCodeOpennessLevelsTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationCodeOrdersTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationCodeRestrictionsTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationCodesTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationConceptsTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationItemsTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationOrganisationsTsvHeader;
import org.siemac.metamac.srm.core.task.domain.ImportationVariableElementsTsvHeader;
import org.siemac.metamac.srm.core.task.domain.InternationalStringTsv;
import org.siemac.metamac.srm.core.task.domain.RepresentationsTsv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arte.statistic.sdmx.srm.core.base.domain.AnnotableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.Annotation;
import com.arte.statistic.sdmx.srm.core.base.domain.AnnotationType;
import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseCopyAllMetadataUtils;
import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.common.domain.InternationalString;
import com.arte.statistic.sdmx.srm.core.common.domain.LocalisedString;
import com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils;
import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

public class TsvImportationUtils {

    private static Logger           logger                         = LoggerFactory.getLogger(TsvImportationUtils.class);

    protected static final String[] fieldForMagnitudeQuantityType  = {SrmConstants.TSV_HEADER_MINIMUM, SrmConstants.TSV_HEADER_MAXIMUM};
    protected static final String[] fieldForFractionQuantityType   = {SrmConstants.TSV_HEADER_MINIMUM, SrmConstants.TSV_HEADER_MAXIMUM, SrmConstants.TSV_HEADER_DENOMINATOR_CONCEPT_SCHEME,
            SrmConstants.TSV_HEADER_DENOMINATOR_CONCEPT, SrmConstants.TSV_HEADER_NUMERATOR_CONCEPT_SCHEME, SrmConstants.TSV_HEADER_NUMERATOR_CONCEPT};
    protected static final String[] fieldForRatioQuantityType      = {SrmConstants.TSV_HEADER_MINIMUM, SrmConstants.TSV_HEADER_MAXIMUM, SrmConstants.TSV_HEADER_DENOMINATOR_CONCEPT_SCHEME,
            SrmConstants.TSV_HEADER_DENOMINATOR_CONCEPT, SrmConstants.TSV_HEADER_NUMERATOR_CONCEPT_SCHEME, SrmConstants.TSV_HEADER_NUMERATOR_CONCEPT, SrmConstants.TSV_HEADER_IS_PERCENTAGE,
            SrmConstants.TSV_HEADER_PERCENTAGE_OF};
    protected static final String[] fieldForAmountQuantityType     = {SrmConstants.TSV_HEADER_MINIMUM, SrmConstants.TSV_HEADER_MAXIMUM, SrmConstants.TSV_HEADER_DENOMINATOR_CONCEPT_SCHEME,
            SrmConstants.TSV_HEADER_DENOMINATOR_CONCEPT, SrmConstants.TSV_HEADER_NUMERATOR_CONCEPT_SCHEME, SrmConstants.TSV_HEADER_NUMERATOR_CONCEPT, SrmConstants.TSV_HEADER_IS_PERCENTAGE,
            SrmConstants.TSV_HEADER_PERCENTAGE_OF};
    protected static final String[] fieldForIndexQuantityType      = {SrmConstants.TSV_HEADER_MINIMUM, SrmConstants.TSV_HEADER_MAXIMUM, SrmConstants.TSV_HEADER_DENOMINATOR_CONCEPT_SCHEME,
            SrmConstants.TSV_HEADER_DENOMINATOR_CONCEPT, SrmConstants.TSV_HEADER_NUMERATOR_CONCEPT_SCHEME, SrmConstants.TSV_HEADER_NUMERATOR_CONCEPT, SrmConstants.TSV_HEADER_IS_PERCENTAGE,
            SrmConstants.TSV_HEADER_PERCENTAGE_OF, SrmConstants.TSV_HEADER_INDEX_BASE_VALUE, SrmConstants.TSV_HEADER_INDEX_BASE_TIME, SrmConstants.TSV_HEADER_INDEX_BASE_LOCATION_CODELIST_URN,
            SrmConstants.TSV_HEADER_INDEX_BASE_LOCATION_CODE};
    protected static final String[] fieldForChangeRateQuantityType = {SrmConstants.TSV_HEADER_MINIMUM, SrmConstants.TSV_HEADER_MAXIMUM, SrmConstants.TSV_HEADER_DENOMINATOR_CONCEPT_SCHEME,
            SrmConstants.TSV_HEADER_DENOMINATOR_CONCEPT, SrmConstants.TSV_HEADER_NUMERATOR_CONCEPT_SCHEME, SrmConstants.TSV_HEADER_NUMERATOR_CONCEPT, SrmConstants.TSV_HEADER_IS_PERCENTAGE,
            SrmConstants.TSV_HEADER_PERCENTAGE_OF, SrmConstants.TSV_HEADER_BASE_QUANTITY_CONCEPT_SCHEME, SrmConstants.TSV_HEADER_BASE_QUANTITY_CONCEPT};
    // ---------------------------------------------------------------------------------------------------------------
    // VARIABLE ELEMENTS
    // ---------------------------------------------------------------------------------------------------------------

    public static ImportationVariableElementsTsvHeader parseTsvHeaderToImportVariableElements(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        List<String> headersExpected = Arrays.asList(SrmConstants.TSV_HEADER_CODE, SrmConstants.TSV_HEADER_GEOGRAPHICAL_GRANULARITY, SrmConstants.TSV_HEADER_SHORT_NAME,
                SrmConstants.TSV_HEADER_RENDERING_COLOR);

        String[] headerColumns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
        if (headerColumns == null || headerColumns.length < headersExpected.size()) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT, headersExpected.size()));
        }

        int headerExpectedIndex = 0;
        ImportationVariableElementsTsvHeader header = new ImportationVariableElementsTsvHeader();
        header.setColumnsSize(headerColumns.length);
        for (int i = 0; i < headerColumns.length; i++) {
            String column = headerColumns[i];
            String[] columnSplited = StringUtils.splitPreserveAllTokens(column, SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR); // some column can be "complex"
            String columnName = columnSplited[0];
            String headerExpected = headersExpected.get(headerExpectedIndex);

            if (SrmConstants.TSV_HEADER_CODE.equals(columnName)) {
                header.setCodePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_GEOGRAPHICAL_GRANULARITY.equals(columnName)) {
                header.setGeographicalGranularityPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_SHORT_NAME.equals(columnName)) {
                header.setShortName(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getShortName(), exceptions));
                if (header.getShortName() == null) {
                    break;
                }
                if (header.getShortName().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_RENDERING_COLOR.equals(columnName)) {
                header.setRenderingColorPosition(i);
                headerExpectedIndex++;
            } else {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
            }
            if (headersExpected.size() <= headerExpectedIndex) {
                break;
            }
        }

        return header;
    }

    // ---------------------------------------------------------------------------------------------------------------
    // ORGANISATIONS
    // ---------------------------------------------------------------------------------------------------------------

    public static ImportationOrganisationsTsvHeader parseTsvHeaderToImportOrganisations(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        List<String> headersExpected = Arrays.asList(SrmConstants.TSV_HEADER_CODE, SrmConstants.TSV_HEADER_PARENT, SrmConstants.TSV_HEADER_NAME, SrmConstants.TSV_HEADER_DESCRIPTION,
                SrmConstants.TSV_HEADER_COMMENT);
        return (ImportationOrganisationsTsvHeader) parseTsvHeaderToImportItems(line, exceptions, new ImportationOrganisationsTsvHeader(), headersExpected);
    }

    /**
     * Transforms TSV line to {@link OrganisationMetamac}. IMPORTANT: Do not execute save or update operation
     */
    public static OrganisationMetamac tsvLineToOrganisation(ImportationItemsTsvHeader header, String[] columns, int lineNumber, ItemSchemeVersion itemSchemeVersion, boolean isDefaultMaintainer,
            boolean isTemporalVersion, boolean updateAlreadyExisting, Map<String, Item> itemsPreviousInItemScheme, Map<String, Item> itemsToPersistByCode, List<MetamacExceptionItem> exceptionItems,
            List<MetamacExceptionItem> infoItems) throws MetamacException {
        return (OrganisationMetamac) tsvLineToItem(header, columns, lineNumber, itemSchemeVersion, isDefaultMaintainer, isTemporalVersion, updateAlreadyExisting, itemsPreviousInItemScheme,
                itemsToPersistByCode, exceptionItems, infoItems, getOrganisationType((OrganisationSchemeVersionMetamac) itemSchemeVersion));
    }

    private static TypeExternalArtefactsEnum getOrganisationType(OrganisationSchemeVersionMetamac organisationSchemeVersionMetamac) {
        if (OrganisationSchemeTypeEnum.AGENCY_SCHEME.equals(organisationSchemeVersionMetamac.getOrganisationSchemeType())) {
            return TypeExternalArtefactsEnum.AGENCY;
        } else if (OrganisationSchemeTypeEnum.DATA_CONSUMER_SCHEME.equals(organisationSchemeVersionMetamac.getOrganisationSchemeType())) {
            return TypeExternalArtefactsEnum.DATA_CONSUMER;
        } else if (OrganisationSchemeTypeEnum.DATA_PROVIDER_SCHEME.equals(organisationSchemeVersionMetamac.getOrganisationSchemeType())) {
            return TypeExternalArtefactsEnum.DATA_PROVIDER;
        } else if (OrganisationSchemeTypeEnum.ORGANISATION_UNIT_SCHEME.equals(organisationSchemeVersionMetamac.getOrganisationSchemeType())) {
            return TypeExternalArtefactsEnum.ORGANISATION_UNIT;
        } else {
            return null;
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // CONCEPTS
    // ---------------------------------------------------------------------------------------------------------------

    public static ImportationConceptsTsvHeader parseTsvHeaderToImportConcepts(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        List<String> headersExpected = Arrays.asList(SrmConstants.TSV_HEADER_CODE, SrmConstants.TSV_HEADER_PARENT, SrmConstants.TSV_HEADER_NAME, SrmConstants.TSV_HEADER_DESCRIPTION,
                SrmConstants.TSV_HEADER_COMMENT, SrmConstants.TSV_HEADER_PLURAL_NAME, SrmConstants.TSV_HEADER_ACRONYM, SrmConstants.TSV_HEADER_DESCRIPTION_SOURCE, SrmConstants.TSV_HEADER_CONTEXT,
                SrmConstants.TSV_HEADER_DOC_METHOD, SrmConstants.TSV_HEADER_DERIVATION, SrmConstants.TSV_HEADER_LEGAL_ACTS, SrmConstants.TSV_HEADER_CONCEPT_TYPE,
                SrmConstants.TSV_HEADER_REPRESENTATION, SrmConstants.TSV_HEADER_CONCEPT_EXTENDS, SrmConstants.TSV_HEADER_SDMX_RELATED_ARTEFACT, SrmConstants.TSV_HEADER_QUANTITY_TYPE,
                SrmConstants.TSV_HEADER_UNIT_CODELIST_URN, SrmConstants.TSV_HEADER_UNIT_CODE, SrmConstants.TSV_HEADER_UNIT_SYMBOL_POSITION, SrmConstants.TSV_HEADER_UNIT_MULTIPLIER_CODELIST_URN,
                SrmConstants.TSV_HEADER_UNIT_MULTIPLIER_CODE, SrmConstants.TSV_HEADER_SIGNIFICANT_DIGITS, SrmConstants.TSV_HEADER_DECIMAL_PLACES, SrmConstants.TSV_HEADER_MINIMUM,
                SrmConstants.TSV_HEADER_MAXIMUM, SrmConstants.TSV_HEADER_DENOMINATOR_CONCEPT_SCHEME, SrmConstants.TSV_HEADER_DENOMINATOR_CONCEPT, SrmConstants.TSV_HEADER_NUMERATOR_CONCEPT_SCHEME,
                SrmConstants.TSV_HEADER_NUMERATOR_CONCEPT, SrmConstants.TSV_HEADER_IS_PERCENTAGE, SrmConstants.TSV_HEADER_PERCENTAGE_OF, SrmConstants.TSV_HEADER_INDEX_BASE_VALUE,
                SrmConstants.TSV_HEADER_INDEX_BASE_TIME, SrmConstants.TSV_HEADER_INDEX_BASE_LOCATION_CODELIST_URN, SrmConstants.TSV_HEADER_INDEX_BASE_LOCATION_CODE,
                SrmConstants.TSV_HEADER_BASE_QUANTITY_CONCEPT_SCHEME, SrmConstants.TSV_HEADER_BASE_QUANTITY_CONCEPT);

        String[] headerColumns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
        if (headerColumns == null || headerColumns.length < headersExpected.size()) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT, headersExpected.size()));
        }

        int headerExpectedIndex = 0;
        ImportationConceptsTsvHeader header = new ImportationConceptsTsvHeader();
        header.setColumnsSize(headerColumns.length);
        for (int i = 0; i < headerColumns.length; i++) {
            String column = headerColumns[i];
            String[] columnSplited = StringUtils.splitPreserveAllTokens(column, SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR); // some column can be "complex"
            String columnName = columnSplited[0];

            String headerExpected = headersExpected.get(headerExpectedIndex);
            if (!headerExpected.equals(columnName)) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
                break;
            }
            if (SrmConstants.TSV_HEADER_CODE.equals(columnName)) {
                header.setCodePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_PARENT.equals(columnName)) {
                header.setParentPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_NAME.equals(columnName)) {
                header.setName(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getName(), exceptions));
                if (header.getName() == null) {
                    break;
                }
                if (header.getName().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_DESCRIPTION.equals(columnName)) {
                header.setDescription(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getDescription(), exceptions));
                if (header.getDescription() == null) {
                    break;
                }
                if (header.getDescription().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_COMMENT.equals(columnName)) {
                header.setComment(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getComment(), exceptions));
                if (header.getComment() == null) {
                    break;
                }
                if (header.getComment().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_PLURAL_NAME.equals(columnName)) {
                header.setPluralName(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getPluralName(), exceptions));
                if (header.getPluralName() == null) {
                    break;
                }
                if (header.getPluralName().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_ACRONYM.equals(columnName)) {
                header.setAcronym(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getAcronym(), exceptions));
                if (header.getAcronym() == null) {
                    break;
                }
                if (header.getAcronym().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_DESCRIPTION_SOURCE.equals(columnName)) {
                header.setDescriptionSource(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getDescriptionSource(), exceptions));
                if (header.getDescriptionSource() == null) {
                    break;
                }
                if (header.getDescriptionSource().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_CONTEXT.equals(columnName)) {
                header.setContext(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getContext(), exceptions));
                if (header.getContext() == null) {
                    break;
                }
                if (header.getContext().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_DOC_METHOD.equals(columnName)) {
                header.setDocMethod(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getDocMethod(), exceptions));
                if (header.getDocMethod() == null) {
                    break;
                }
                if (header.getDocMethod().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_DERIVATION.equals(columnName)) {
                header.setDerivation(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getDerivation(), exceptions));
                if (header.getDerivation() == null) {
                    break;
                }
                if (header.getDerivation().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_LEGAL_ACTS.equals(columnName)) {
                header.setLegalActs(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getLegalActs(), exceptions));
                if (header.getLegalActs() == null) {
                    break;
                }
                if (header.getLegalActs().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_CONCEPT_TYPE.equals(columnName)) {
                header.setConceptTypePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_REPRESENTATION.equals(columnName)) {
                header.setRepresentation(columnHeaderToRepresentationTsv(headerColumns, i, columnSplited, headerExpected, header.getRepresentation(), exceptions));
                if (header.getRepresentation().isRepresentationHeaderComplete()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_CONCEPT_EXTENDS.equals(columnName)) {
                header.setConceptExtendsPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_SDMX_RELATED_ARTEFACT.equals(columnName)) {
                header.setSdmxRelatedArtefactPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_QUANTITY_TYPE.equals(columnName)) {
                header.setQuantityTypePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_UNIT_CODELIST_URN.equals(columnName)) {
                header.setUnitCodelistUrnPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_UNIT_CODE.equals(columnName)) {
                header.setUnitCodePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_UNIT_SYMBOL_POSITION.equals(columnName)) {
                header.setUnitSymbolPositionPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_UNIT_MULTIPLIER_CODELIST_URN.equals(columnName)) {
                header.setUnitMultiplierCodelistUrnPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_UNIT_MULTIPLIER_CODE.equals(columnName)) {
                header.setUnitMultiplierCodePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_SIGNIFICANT_DIGITS.equals(columnName)) {
                header.setSignificantDigitsPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_DECIMAL_PLACES.equals(columnName)) {
                header.setDecimalPlacesPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_MINIMUM.equals(columnName)) {
                header.setMiminumPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_MAXIMUM.equals(columnName)) {
                header.setMaximumPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_DENOMINATOR_CONCEPT_SCHEME.equals(columnName)) {
                header.setDenominatorConceptSchemePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_DENOMINATOR_CONCEPT.equals(columnName)) {
                header.setDenominatorConceptPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_NUMERATOR_CONCEPT_SCHEME.equals(columnName)) {
                header.setNumeratorConceptSchemePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_NUMERATOR_CONCEPT.equals(columnName)) {
                header.setNumeratorConceptPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_IS_PERCENTAGE.equals(columnName)) {
                header.setIsPercentagePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_PERCENTAGE_OF.equals(columnName)) {
                header.setPercentageOf(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getPercentageOf(), exceptions));
                if (header.getPercentageOf() == null) {
                    break;
                }
                if (header.getPercentageOf().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_INDEX_BASE_VALUE.equals(columnName)) {
                header.setIndexBaseValuePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_INDEX_BASE_TIME.equals(columnName)) {
                header.setIndexBaseTimePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_INDEX_BASE_LOCATION_CODELIST_URN.equals(columnName)) {
                header.setIndexBaseLocationCodelistUrnPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_INDEX_BASE_LOCATION_CODE.equals(columnName)) {
                header.setIndexBaseLocationCodePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_BASE_QUANTITY_CONCEPT_SCHEME.equals(columnName)) {
                header.setIndexBaseQuantityConceptSchemePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_BASE_QUANTITY_CONCEPT.equals(columnName)) {
                header.setIndexBaseQuantityConceptPosition(i);
                headerExpectedIndex++;
            }

        }
        if (CollectionUtils.isEmpty(exceptions)) {
            if (header.getColumnsSize() == 0) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE));
            }
            if (header.getName() == null) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_NAME));
            }
            // description is optional
        }
        return header;
    }

    /**
     * Transforms TSV line to {@link ConceptMetamac}. IMPORTANT: Do not execute save or update operation
     */
    public static ConceptMetamac tsvLineToConcept(ImportationItemsTsvHeader header, String[] columns, int lineNumber, ItemSchemeVersion itemSchemeVersion, boolean isDefaultMaintainer,
            boolean isTemporalVersion, boolean updateAlreadyExisting, Map<String, Item> itemsPreviousInItemScheme, Map<String, Item> itemsToPersistByCode, List<MetamacExceptionItem> exceptionItems,
            List<MetamacExceptionItem> infoItems) throws MetamacException {
        return (ConceptMetamac) tsvLineToItem(header, columns, lineNumber, itemSchemeVersion, isDefaultMaintainer, isTemporalVersion, updateAlreadyExisting, itemsPreviousInItemScheme,
                itemsToPersistByCode, exceptionItems, infoItems, TypeExternalArtefactsEnum.CONCEPT);

    }

    public static ConceptMetamac setInternationalStringConceptFields(ImportationConceptsTsvHeader conceptTsvHeader, String[] columns, ConceptMetamac conceptMetamac, boolean isDefaultMaintainer)
            throws MetamacException {

        conceptMetamac.setAcronym(TsvImportationUtils.tsvLineToInternationalStringBase(conceptTsvHeader.getAcronym(), columns, conceptMetamac.getAcronym(), isDefaultMaintainer));
        conceptMetamac.setDescriptionSource(
                TsvImportationUtils.tsvLineToInternationalStringBase(conceptTsvHeader.getDescriptionSource(), columns, conceptMetamac.getDescriptionSource(), isDefaultMaintainer));
        conceptMetamac.setContext(TsvImportationUtils.tsvLineToInternationalStringBase(conceptTsvHeader.getContext(), columns, conceptMetamac.getContext(), isDefaultMaintainer));
        conceptMetamac.setDocMethod(TsvImportationUtils.tsvLineToInternationalStringBase(conceptTsvHeader.getDocMethod(), columns, conceptMetamac.getDocMethod(), isDefaultMaintainer));
        conceptMetamac.setDerivation(TsvImportationUtils.tsvLineToInternationalStringBase(conceptTsvHeader.getDerivation(), columns, conceptMetamac.getDerivation(), isDefaultMaintainer));
        conceptMetamac.setLegalActs(TsvImportationUtils.tsvLineToInternationalStringBase(conceptTsvHeader.getLegalActs(), columns, conceptMetamac.getLegalActs(), isDefaultMaintainer));

        return conceptMetamac;
    }

    // ---------------------------------------------------------------------------------------------------------------
    // CATEGORIES
    // ---------------------------------------------------------------------------------------------------------------

    public static ImportationCategoriesTsvHeader parseTsvHeaderToImportCategories(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        List<String> headersExpected = Arrays.asList(SrmConstants.TSV_HEADER_CODE, SrmConstants.TSV_HEADER_PARENT, SrmConstants.TSV_HEADER_CATEGORY_ELEMENT, SrmConstants.TSV_HEADER_ENI_SCHEME,
                SrmConstants.TSV_HEADER_NAME, SrmConstants.TSV_HEADER_DESCRIPTION, SrmConstants.TSV_HEADER_COMMENT);
        return (ImportationCategoriesTsvHeader) parseTsvHeaderToImportItems(line, exceptions, new ImportationCategoriesTsvHeader(), headersExpected);
    }

    /**
     * Transforms TSV line to {@link CategoryMetamac}. IMPORTANT: Do not execute save or update operation
     */
    public static CategoryMetamac tsvLineToCategory(ImportationItemsTsvHeader header, String[] columns, int lineNumber, ItemSchemeVersion itemSchemeVersion, boolean isDefaultMaintainer,
            boolean isTemporalVersion, boolean updateAlreadyExisting, Map<String, Item> itemsPreviousInItemScheme, Map<String, Item> itemsToPersistByCode, List<MetamacExceptionItem> exceptionItems,
            List<MetamacExceptionItem> infoItems) throws MetamacException {
        return (CategoryMetamac) tsvLineToItem(header, columns, lineNumber, itemSchemeVersion, isDefaultMaintainer, isTemporalVersion, updateAlreadyExisting, itemsPreviousInItemScheme,
                itemsToPersistByCode, exceptionItems, infoItems, TypeExternalArtefactsEnum.CATEGORY);

    }

    public static void tsvLineToItemCategoryOwnFields(CategoryMetamac category, LinkedHashMap<String, EniSchemaCatalog> eniSchemeMap, ImportationCategoriesTsvHeader header, String[] columns,
            List<MetamacExceptionItem> exceptionItems) {

        // eni scheme field
        String itemIdentifierEniSchemas = columns[header.getEniSchemePosition()];
        if (!StringUtils.isBlank(itemIdentifierEniSchemas)) {
            category.removeAllEniSchemas();
            String[] eniSchemaIdentifiers = itemIdentifierEniSchemas.split(",");
            for (String eniSchemaIdentifier : eniSchemaIdentifiers) {
                EniSchemaCatalog eniSchema = eniSchemeMap.get(eniSchemaIdentifier);
                if (eniSchema == null) {
                    exceptionItems
                            .add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_INCORRECT, eniSchemaIdentifier, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_ENI_SCHEMA));
                    return;

                } else {
                    category.getEniSchemas().add(eniSchema);
                }
            }

        } else {
            category.removeAllEniSchemas();
        }
    }

    public static String getCategoryElementCode(ImportationCategoriesTsvHeader header, String[] columns) {
        return columns[header.getCategoryElementPosition()];
    }

    // ---------------------------------------------------------------------------------------------------------------
    // CODES
    // ---------------------------------------------------------------------------------------------------------------

    public static ImportationCodesTsvHeader parseTsvHeaderToImportCodes(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        List<String> headersExpected = Arrays.asList(SrmConstants.TSV_HEADER_CODE, SrmConstants.TSV_HEADER_PARENT, SrmConstants.TSV_HEADER_IS_EXTENDED, SrmConstants.TSV_HEADER_VARIABLE_ELEMENT,
                SrmConstants.TSV_HEADER_NAME, SrmConstants.TSV_HEADER_DESCRIPTION, SrmConstants.TSV_HEADER_COMMENT, SrmConstants.TSV_HEADER_SHORT_NAME);

        String[] headerColumns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
        if (headerColumns == null || headerColumns.length < headersExpected.size()) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT, headersExpected.size()));
        }

        int headerExpectedIndex = 0;
        ImportationCodesTsvHeader header = new ImportationCodesTsvHeader();
        header.setColumnsSize(headerColumns.length);
        for (int i = 0; i < headerColumns.length; i++) {
            String column = headerColumns[i];
            String[] columnSplited = StringUtils.splitPreserveAllTokens(column, SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR); // some column can be "complex"
            String columnName = columnSplited[0];

            String headerExpected = headersExpected.get(headerExpectedIndex);
            if (!headerExpected.equals(columnName)) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
                break;
            }
            if (SrmConstants.TSV_HEADER_CODE.equals(columnName)) {
                header.setCodePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_PARENT.equals(columnName)) {
                header.setParentPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_VARIABLE_ELEMENT.equals(columnName)) {
                header.setVariableElementPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_NAME.equals(columnName)) {
                header.setName(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getName(), exceptions));
                if (header.getName() == null) {
                    break;
                }
                if (header.getName().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_DESCRIPTION.equals(columnName)) {
                header.setDescription(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getDescription(), exceptions));
                if (header.getDescription() == null) {
                    break;
                }
                if (header.getDescription().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_COMMENT.equals(columnName)) {
                header.setComment(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getComment(), exceptions));
                if (header.getComment() == null) {
                    break;
                }
                if (header.getComment().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_SHORT_NAME.equals(columnName)) {
                header.setShortName(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getShortName(), exceptions));
                if (header.getShortName() == null) {
                    break;
                }
                if (header.getShortName().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_IS_EXTENDED.equals(columnName)) {
                header.setIsExtendedPosition(i);
                headerExpectedIndex++;
            }

        }
        if (CollectionUtils.isEmpty(exceptions)) {
            if (header.getColumnsSize() == 0) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE));
            }
            if (header.getName() == null) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_NAME));
            }
            // description is optional
        }
        return header;
    }

    public static ImportationAnnotationsTsvHeader parseTsvHeaderToImportAnnotations(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        List<String> headersExpected = Arrays.asList(SrmConstants.TSV_HEADER_CODE, SrmConstants.TSV_HEADER_ANNOTATION_TYPE, SrmConstants.TSV_HEADER_ANNOTATION, SrmConstants.TSV_HEADER_ANNOTATION_URL);

        String[] headerColumns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
        if (headerColumns == null || headerColumns.length < headersExpected.size()) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT, headersExpected.size()));
        }
        ImportationAnnotationsTsvHeader importationAnnotationsTsvHeader = new ImportationAnnotationsTsvHeader();
        importationAnnotationsTsvHeader.setColumnsSize(headerColumns.length);
        int headerExpectedIndex = 0;
        for (int i = 0; i < headerColumns.length; i++) {
            String column = headerColumns[i];
            String[] columnSplited = StringUtils.splitPreserveAllTokens(column, SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR); // some column can be "complex"
            String columnName = columnSplited[0];

            String headerExpected = headersExpected.get(headerExpectedIndex);
            if (!headerExpected.equals(columnName)) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
                break;
            }

            if (SrmConstants.TSV_HEADER_CODE.equals(columnName)) {
                importationAnnotationsTsvHeader.setCodePosition(i);
                headerExpectedIndex++;
            }

            if (SrmConstants.TSV_HEADER_ANNOTATION_TYPE.equals(columnName)) {
                importationAnnotationsTsvHeader.setAnnotationTypePosition(i);
                headerExpectedIndex++;
            }

            if (SrmConstants.TSV_HEADER_ANNOTATION.equals(columnName)) {
                importationAnnotationsTsvHeader
                        .setAnnotation(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, importationAnnotationsTsvHeader.getAnnotation(), exceptions));
                if (importationAnnotationsTsvHeader.getAnnotation() == null) {
                    break;
                }
                if (importationAnnotationsTsvHeader.getAnnotation().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            }
            if (SrmConstants.TSV_HEADER_ANNOTATION_URL.equals(columnName)) {
                importationAnnotationsTsvHeader.setAnnotationUrlPosition(i);
                headerExpectedIndex++;
            }
        }
        return importationAnnotationsTsvHeader;
    }

    public static Annotation tsvLineToAnnotation(ImportationAnnotationsTsvHeader header, String[] columns, int lineNumber, List<MetamacExceptionItem> exceptionItems,
            List<MetamacExceptionItem> infoItems, List<AnnotationType> annotationsTypes, List<Code> codes) throws MetamacException {
        String codeIdentifier = columns[header.getCodePosition()];
        String annotationTypeCode = columns[header.getAnnotationTypePosition()];
        String annotationUrl = columns[header.getAnnotationUrlPosition()];
        if (!checkAllParametersInLine(exceptionItems, lineNumber, codeIdentifier, annotationTypeCode)) {
            return null;
        }
        AnnotationType annotationType = getAnnotationType(annotationsTypes, annotationTypeCode);
        AnnotableArtefact annotableArtefact = getAnnotableArtefact(codes, codeIdentifier);

        if (annotationType == null) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ANNOTATION_TYPE_INCORRECT, lineNumber));
            return null;
        }
        if (annotableArtefact == null) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_CODE_INCORRECT, lineNumber));
            return null;
        }
        Annotation annotation = new Annotation();
        annotation.setCode(codeIdentifier);
        annotation.setText(TsvImportationUtils.tsvLineToInternationalStringBase(header.getAnnotation(), columns, annotation.getText(), false));
        annotation.setAnnotationType(annotationType);
        annotation.setUrl(annotationUrl);
        annotation.setAnnotableArtefact(annotableArtefact);
        return annotation;
    }

    private static AnnotableArtefact getAnnotableArtefact(List<Code> codes, String codeIdentifier) {
        codeIdentifier = codeIdentifier.replace(" ", "");
        for (Code code : codes) {
            if (codeIdentifier.equals(code.getNameableArtefact().getCode())) {
                return code.getNameableArtefact();
            }
            AnnotableArtefact result = getAnnotableArtefact(code.getChildren(), codeIdentifier);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    private static Boolean checkAllParametersInLine(List<MetamacExceptionItem> exceptionItems, int lineNumber, String codeIdentifier, String annotationTypeCode) {
        if (StringUtils.isBlank(codeIdentifier) || StringUtils.isBlank(annotationTypeCode)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber));
            return false;
        }
        return true;
    }

    private static AnnotationType getAnnotationType(List<AnnotationType> annotationsTypes, String annotationTypeCode) {
        for (AnnotationType annotationType : annotationsTypes) {
            if (annotationType.getCode().equals(annotationTypeCode)) {
                return annotationType;
            }
        }
        return null;
    }

    /**
     * Transforms tsv line to Code. IMPORTANT: Do not execute save or update operation
     */
    public static CodeMetamac tsvLineToCode(ImportationCodesTsvHeader header, String[] columns, int lineNumber, CodelistVersionMetamac codelistVersion, boolean isDefaultMantainer,
            boolean isTemporalVersion, Map<String, VariableElement> variableElementsInVariableByCode, boolean updateAlreadyExisting, Map<String, CodeMetamac> codesPreviousInCodelist,
            Map<String, CodeMetamac> codesToPersistByCode, List<MetamacExceptionItem> exceptionItems, List<MetamacExceptionItem> infoItems) throws MetamacException {

        // semantic identifier
        String codeIdentifier = columns[header.getCodePosition()];
        if (StringUtils.isBlank(codeIdentifier)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber));
            return null;
        }
        if (codesToPersistByCode.containsKey(codeIdentifier)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_RESOURCE_DUPLICATED, codeIdentifier, lineNumber));
            return null;
        }
        if (!SemanticIdentifierValidationUtils.isCodeSemanticIdentifier(codeIdentifier)) {
            exceptionItems
                    .add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_INCORRECT_SEMANTIC_IDENTIFIER, codeIdentifier, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE));
            return null;
        }
        // parent
        CodeMetamac codeParent = null;
        String codeParentIdentifier = columns[header.getParentPosition()];
        if (!StringUtils.isBlank(codeParentIdentifier) && !isTemporalVersion) {
            if (codesToPersistByCode.containsKey(codeParentIdentifier)) {
                codeParent = codesToPersistByCode.get(codeParentIdentifier);
            } else {
                codeParent = codesPreviousInCodelist.get(codeParentIdentifier); // try code already exists in codelist before this importation
            }
            if (codeParent == null) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_PARENT_NOT_FOUND, codeParentIdentifier, codeIdentifier));
                return null;
            }
        }
        // variable element
        String variableElementIdentifier = columns[header.getVariableElementPosition()];
        VariableElement variableElement = null;
        boolean updateVariableElement = true;
        if (!StringUtils.isBlank(variableElementIdentifier)) {
            variableElement = variableElementsInVariableByCode.get(variableElementIdentifier);
            if (variableElement == null) {
                // do not abort importation. Only inform about this
                infoItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_VARIABLE_ELEMENT_NOT_FOUND, variableElementIdentifier, codeIdentifier));
                updateVariableElement = false;
            }

        }
        // init code
        CodeMetamac code = codesPreviousInCodelist.get(codeIdentifier);
        if (code == null) {
            if (!isDefaultMantainer || isTemporalVersion) {
                infoItems.add(new MetamacExceptionItem(
                        !isDefaultMantainer ? ServiceExceptionType.IMPORTATION_TSV_INFO_EXTERNAL_AGENCY_RESOURCE_NOT_UPDATED : ServiceExceptionType.IMPORTATION_TSV_INFO_TEMPORAL_VERSION_NOT_UPDATED,
                        codeIdentifier));
                return null;
            }
            code = new CodeMetamac();
            code.setNameableArtefact(new NameableArtefact());
            code.getNameableArtefact().setCode(codeIdentifier);
            String urn = GeneratorUrnUtils.generateCodeUrn(codelistVersion, code.getNameableArtefact().getCode());
            code.getNameableArtefact().setUrn(urn);
            code.getNameableArtefact().setUrnProvider(urn);
            code.setParent(codeParent);
        } else {
            if (!updateAlreadyExisting) {
                infoItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_NOT_UPDATED, code.getNameableArtefact().getCode()));
                return null;
            } else {
                infoItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_UPDATED, code.getNameableArtefact().getCode()));
                if (!isTemporalVersion) {
                    if (SdmxSrmUtils.isItemParentChanged(code.getParent(), codeParent)) {
                        // Clear all order columns to put at the end of new level
                        SrmServiceUtils.clearCodeOrders(code);
                    }
                    code.setParent(codeParent);
                }
                code.setUpdateDate(new DateTime());
            }
        }

        code.getNameableArtefact().setName(TsvImportationUtils.tsvLineToInternationalStringBase(header.getName(), columns, code.getNameableArtefact().getName(), isDefaultMantainer));
        if (code.getNameableArtefact().getName() == null) {
            exceptionItems.add(
                    new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_REQUIRED, code.getNameableArtefact().getCode(), ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_NAME));
        }
        code.getNameableArtefact()
                .setDescription(TsvImportationUtils.tsvLineToInternationalStringBase(header.getDescription(), columns, code.getNameableArtefact().getDescription(), isDefaultMantainer));
        code.getNameableArtefact().setComment(TsvImportationUtils.tsvLineToInternationalStringBase(header.getComment(), columns, code.getNameableArtefact().getComment(), isDefaultMantainer));
        if (updateVariableElement) {
            code.setVariableElement(variableElement);
            // Short name. Only filled if code has not related variable element
            if (variableElement != null) {
                code.setShortName(null);
            } else {
                code.setShortName(TsvImportationUtils.tsvLineToInternationalStringBase(header.getShortName(), columns, code.getShortName(), isDefaultMantainer));
            }
        }

        String isExtended = columns[header.getIsExtendedPosition()];
        if (!StringUtils.isBlank(isExtended)) {
            code.setIsExtended(Boolean.valueOf(isExtended));
        } else {
            code.setIsExtended(null);
        }

        // Do not persist if any error ocurrs
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            return null;
        }

        try {
            // extra validation to avoid save some incorrect
            if (code.getId() == null) {
                CodesMetamacInvocationValidator.checkCreateCode(codelistVersion, code, null);
            } else {
                CodesMetamacInvocationValidator.checkUpdateCode(codelistVersion, code, null);
            }
        } catch (MetamacException metamacException) {
            logger.error("Error importing code from tsv file", metamacException);
            MetamacExceptionItem metamacExceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
            metamacExceptionItem.setExceptionItems(metamacException.getExceptionItems());
            exceptionItems.add(metamacExceptionItem);
        }

        code.setItemSchemeVersion(codelistVersion);
        if (codeParent == null) {
            code.setItemSchemeVersionFirstLevel(codelistVersion);
        } else {
            code.setItemSchemeVersionFirstLevel(null);
        }

        return code;
    }

    // ---------------------------------------------------------------------------------------------------------------
    // CODE ORDERS
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * Imports header of tsv to import orders of codes and concepts.
     * Columns "label", "level" and "parent" are optionals.
     */
    public static ImportationCodeOrdersTsvHeader parseTsvHeaderToImportCodeOrders(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        String[] headerColumns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
        if (headerColumns == null || headerColumns.length < 2) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT, 2));
        }
        ImportationCodeOrdersTsvHeader header = new ImportationCodeOrdersTsvHeader();
        header.setColumnsSize(headerColumns.length);
        for (int i = 0; i < headerColumns.length; i++) {
            String columnName = headerColumns[i];
            if (i == 0) {
                if (!SrmConstants.TSV_HEADER_CODE.equals(columnName)) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE));
                    break;
                }
                header.setCodePosition(i);
            } else if (SrmConstants.TSV_HEADER_LABEL.equals(columnName)) {
                header.setLabelPosition(i);
            } else if (SrmConstants.TSV_HEADER_LEVEL.equals(columnName)) {
                header.setLevelPosition(i);
            } else if (SrmConstants.TSV_HEADER_PARENT.equals(columnName)) {
                header.setParentPosition(i);
            } else {
                // must be a code of order visualisation
                header.addOrderVisualisations(columnName, i);
            }
        }
        if (CollectionUtils.isEmpty(exceptions)) {
            if (!header.hasOrderVisualisations()) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_ORDER));
            }
        }
        return header;
    }

    // ---------------------------------------------------------------------------------------------------------------
    // CODE RESTRICTIONS
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * Imports header of tsv to import restrictions of codes.
     * Columns "label", "level" and "parent" are optionals.
     */
    public static ImportationCodeRestrictionsTsvHeader parseTsvHeaderToImportCodeRestrictions(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        String[] headerColumns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
        if (headerColumns == null || headerColumns.length < 2) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT, 2));
        }
        ImportationCodeRestrictionsTsvHeader header = new ImportationCodeRestrictionsTsvHeader();
        header.setColumnsSize(headerColumns.length);
        for (int i = 0; i < headerColumns.length; i++) {
            String columnName = headerColumns[i];
            if (i == 0) {
                if (!SrmConstants.TSV_HEADER_CODE.equals(columnName)) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE));
                    break;
                }
                header.setCodePosition(i);
            } else if (SrmConstants.TSV_HEADER_LABEL.equals(columnName)) {
                header.setLabelPosition(i);
            } else if (SrmConstants.TSV_HEADER_LEVEL.equals(columnName)) {
                header.setLevelPosition(i);
            } else if (SrmConstants.TSV_HEADER_PARENT.equals(columnName)) {
                header.setParentPosition(i);
            } else {
                // must be a code of order visualisation
                header.addRestrictionVisualisations(columnName, i);
            }
        }
        if (CollectionUtils.isEmpty(exceptions) && !header.hasRestrictionVisualisations()) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_RESTRICTION));
        }

        return header;
    }

    // ---------------------------------------------------------------------------------------------------------------
    // CODE OPENNESS LEVELS
    // ---------------------------------------------------------------------------------------------------------------

    /**
     * Imports header of tsv to import openness levels of codes.
     * Columns "label", "level" and "parent" are optionals.
     */
    public static ImportationCodeOpennessLevelsTsvHeader parseTsvHeaderToImportCodeOpennessLevels(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        String[] headerColumns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
        if (headerColumns == null || headerColumns.length < 2) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT, 2));
        }
        ImportationCodeOpennessLevelsTsvHeader header = new ImportationCodeOpennessLevelsTsvHeader();
        header.setColumnsSize(headerColumns.length);
        for (int i = 0; i < headerColumns.length; i++) {
            String columnName = headerColumns[i];
            if (i == 0) {
                if (!SrmConstants.TSV_HEADER_CODE.equals(columnName)) {
                    exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE));
                    break;
                }
                header.setCodePosition(i);
            } else if (SrmConstants.TSV_HEADER_LABEL.equals(columnName)) {
                header.setLabelPosition(i);
            } else if (SrmConstants.TSV_HEADER_LEVEL.equals(columnName)) {
                header.setLevelPosition(i);
            } else if (SrmConstants.TSV_HEADER_PARENT.equals(columnName)) {
                header.setParentPosition(i);
            } else {
                // must be a code of openness level visualisation
                header.addOpennessVisualisations(columnName, i);
            }
        }
        if (CollectionUtils.isEmpty(exceptions) && !header.hasOpennessVisualisations()) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_OPENNESS_LEVEL));
        }

        return header;
    }

    // ---------------------------------------------------------------------------------------------------------------
    // INTERNATIONAL STRINGS
    // ---------------------------------------------------------------------------------------------------------------

    public static InternationalString tsvLineToInternationalString(InternationalStringTsv internationalStringTsv, String[] columns, InternationalString target) {
        return tsvLineToInternationalStringBase(internationalStringTsv, columns, target, false);
    }

    public static InternationalString tsvLineToInternationalStringBase(InternationalStringTsv internationalStringTsv, String[] columns, InternationalString target, boolean changeUnmodifiables) {
        if (internationalStringTsv == null) {
            return null;
        }
        Set<LocalisedString> localisedStringTargets = new HashSet<LocalisedString>();
        int j = 0;
        for (int i = internationalStringTsv.getStartPosition(); i <= internationalStringTsv.getEndPosition(); i++) {
            String label = columns[i];
            String locale = internationalStringTsv.getLocale(j);
            boolean isUnmodifiable = isLocalisedStringUnmodifiable(target, locale, changeUnmodifiables);

            if (!StringUtils.isBlank(label) || isUnmodifiable) {
                if (target == null) {
                    target = new InternationalString();
                }
                LocalisedString localisedString = target.getLocalisedLabelEntity(locale);
                if (localisedString == null) {
                    localisedString = new LocalisedString(locale, label);
                } else {
                    if (!isUnmodifiable) {
                        localisedString.setLabel(label);
                    }
                }
                localisedStringTargets.add(localisedString);
            }
            j++;
        }
        if (localisedStringTargets.isEmpty()) {
            return null;
        }
        if (target != null) {
            target.removeAllTexts();
            for (LocalisedString localisedString : localisedStringTargets) {
                target.addText(localisedString);
            }
        }
        return target;
    }

    private static boolean isLocalisedStringUnmodifiable(InternationalString target, String locale, boolean changeUnmodifiables) {
        if (target != null) {
            LocalisedString localisedString = target.getLocalisedLabelEntity(locale);

            return localisedString != null && !changeUnmodifiables && Boolean.TRUE.equals(localisedString.getIsUnmodifiable());
        }
        return false;
    }

    private static InternationalStringTsv columnHeaderToInternationalStringTsv(String[] headerColumns, int headerColumnIndex, String[] columnSplited, String headerExpected,
            InternationalStringTsv target, List<MetamacExceptionItem> exceptions) {
        if (columnSplited == null || columnSplited.length != 2) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
            return null;
        }
        String locale = columnSplited[1];
        if (StringUtils.isBlank(locale)) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
        }
        if (target == null) {
            target = new InternationalStringTsv();
            target.setStartPosition(headerColumnIndex);
        }
        target.addLocale(locale);
        if (headerColumnIndex == headerColumns.length - 1 || !headerColumns[headerColumnIndex + 1].startsWith(headerExpected)) {
            target.setEndPosition(headerColumnIndex);
        }
        return target;
    }

    private static RepresentationsTsv columnHeaderToRepresentationTsv(String[] headerColumns, int headerColumnIndex, String[] columnSplited, String headerExpected, RepresentationsTsv target,
            List<MetamacExceptionItem> exceptions) {

        if (columnSplited == null || columnSplited.length != 2) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
            return null;
        }

        if (target == null) {
            target = new RepresentationsTsv();
        }

        String subfield = columnSplited[1];
        if (SrmConstants.TSV_HEADER_REPRESENTATION_SUB_TYPE.equals(subfield.toLowerCase())) {
            target.setTypePosition(headerColumnIndex);
        } else if (SrmConstants.TSV_HEADER_REPRESENTATION_SUB_VALUE.equals(subfield.toLowerCase())) {
            target.setValuePosition(headerColumnIndex);
        } else {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
            return null;
        }
        return target;
    }

    public static void checkItemSchemeCanExecuteImportation(ItemSchemeVersion itemSchemeVersion, SrmLifeCycleMetadata srmLifeCycleMetadata, Boolean canBeBackground) throws MetamacException {
        SrmValidationUtils.checkArtefactCanBeModified(srmLifeCycleMetadata, itemSchemeVersion.getMaintainableArtefact().getUrn());
        if (BooleanUtils.isTrue(canBeBackground)) {
            SrmValidationUtils.checkArtefactWithoutTaskInBackground(itemSchemeVersion);
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // ITEMS
    // ---------------------------------------------------------------------------------------------------------------

    private static ImportationItemsTsvHeader parseTsvHeaderToImportItems(String line, List<MetamacExceptionItem> exceptions, ImportationItemsTsvHeader header, List<String> headersExpected)
            throws MetamacException {
        String[] headerColumns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
        if (headerColumns == null || headerColumns.length < 4) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT, 4));
        }

        int headerExpectedIndex = 0;
        header.setColumnsSize(headerColumns.length);
        for (int i = 0; i < headerColumns.length; i++) {
            String column = headerColumns[i];
            String[] columnSplited = StringUtils.splitPreserveAllTokens(column, SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR); // some column can be "complex"
            String columnName = columnSplited[0];

            String headerExpected = headersExpected.get(headerExpectedIndex);
            if (!headerExpected.equals(columnName)) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
                break;
            }
            if (SrmConstants.TSV_HEADER_CODE.equals(columnName)) {
                header.setCodePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_PARENT.equals(columnName)) {
                header.setParentPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_CATEGORY_ELEMENT.equals(columnName)) {
                ((ImportationCategoriesTsvHeader) header).setCategoryElementPosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_ENI_SCHEME.equals(columnName)) {
                ((ImportationCategoriesTsvHeader) header).setEniSchemePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_NAME.equals(columnName)) {
                header.setName(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getName(), exceptions));
                if (header.getName() == null) {
                    break;
                }
                if (header.getName().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_DESCRIPTION.equals(columnName)) {
                header.setDescription(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getDescription(), exceptions));
                if (header.getDescription() == null) {
                    break;
                }
                if (header.getDescription().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_COMMENT.equals(columnName)) {
                header.setComment(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getComment(), exceptions));
                if (header.getComment() == null) {
                    break;
                }
                if (header.getComment().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            }
        }
        if (CollectionUtils.isEmpty(exceptions)) {
            if (header.getColumnsSize() == 0) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE));
            }
            if (header.getName() == null) {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_NAME));
            }
            // description is optional
        }
        return header;
    }

    private static Item tsvLineToItem(ImportationItemsTsvHeader header, String[] columns, int lineNumber, ItemSchemeVersion itemSchemeVersion, boolean isDefaultMaintainer, boolean isTemporalVersion,
            boolean updateAlreadyExisting, Map<String, Item> itemsPreviousInItemScheme, Map<String, Item> itemsToPersistByCode, List<MetamacExceptionItem> exceptionItems,
            List<MetamacExceptionItem> infoItems, TypeExternalArtefactsEnum itemType) throws MetamacException {

        // semantic identifier
        String itemIdentifier = columns[header.getCodePosition()];
        if (StringUtils.isBlank(itemIdentifier)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber));
            return null;
        }
        if (!TypeExternalArtefactsEnum.CATEGORY.equals(itemType) && itemsToPersistByCode.containsKey(itemIdentifier)) {
            exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_RESOURCE_DUPLICATED, itemIdentifier, lineNumber));
            return null;
        }
        if (!isSemanticIdentifier(itemIdentifier, itemType)) {

            exceptionItems
                    .add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_INCORRECT_SEMANTIC_IDENTIFIER, itemIdentifier, ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_CODE));
            return null;
        }
        // parent
        Item itemParent = null;

        // if temporal version, parent can not be modified.
        String itemParentIdentifier = columns[header.getParentPosition()];
        if (!StringUtils.isBlank(itemParentIdentifier) && !isTemporalVersion) {
            if (TypeExternalArtefactsEnum.AGENCY.equals(itemType) || TypeExternalArtefactsEnum.DATA_CONSUMER.equals(itemType) || TypeExternalArtefactsEnum.DATA_PROVIDER.equals(itemType)) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_UNEXPECTED_PARENT, itemIdentifier, itemParentIdentifier));
                return null;
            }
            if (itemsToPersistByCode.containsKey(itemParentIdentifier)) {
                itemParent = itemsToPersistByCode.get(itemParentIdentifier);
            } else {
                itemParent = itemsPreviousInItemScheme.get(itemParentIdentifier);
            }
            if (itemParent == null) {
                exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_ERROR_PARENT_NOT_FOUND, itemParentIdentifier, itemIdentifier));
                return null;
            }
        }
        // initialize item
        Item item = TypeExternalArtefactsEnum.CATEGORY.equals(itemType)
                ? (itemsPreviousInItemScheme.get(StringUtils.isNotBlank(itemParentIdentifier) ? itemParentIdentifier + "." + itemIdentifier : itemIdentifier))
                : itemsPreviousInItemScheme.get(itemIdentifier);
        if (item == null) {

            if (!isDefaultMaintainer || isTemporalVersion) {
                infoItems.add(new MetamacExceptionItem(
                        !isDefaultMaintainer ? ServiceExceptionType.IMPORTATION_TSV_INFO_EXTERNAL_AGENCY_RESOURCE_NOT_UPDATED : ServiceExceptionType.IMPORTATION_TSV_INFO_TEMPORAL_VERSION_NOT_UPDATED,
                        itemIdentifier));
                return null;
            }

            item = createItem(itemType);
            item.setNameableArtefact(new NameableArtefact());
            item.getNameableArtefact().setCode(itemIdentifier);
            item.setParent(itemParent);
            item.getNameableArtefact().setIsCodeUpdated(false);
            if (TypeExternalArtefactsEnum.CATEGORY.equals(itemType)) {
                String codeFull = GeneratorUrnUtils.generateCategoryCodeFull(item);
                item.getNameableArtefact().setCodeFull(codeFull);
                if (itemsToPersistByCode.containsKey(codeFull)) {
                    exceptionItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_RESOURCE_DUPLICATED, itemIdentifier, lineNumber));
                    return null;
                }
            }
            String urn = generateItemUrn(itemSchemeVersion, item, itemType);
            item.getNameableArtefact().setUrn(urn);
            item.getNameableArtefact().setUrnProvider(urn);
        } else {

            item = cloneItemInImport(item, itemType);
            if (!updateAlreadyExisting) {
                infoItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_NOT_UPDATED, itemIdentifier));
                return null;
            } else {
                infoItems.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_INFO_RESOURCE_UPDATED, itemIdentifier));

                if (!isTemporalVersion) {
                    item.setParent(itemParent);
                }

                item.setUpdateDate(new DateTime());
            }
            item.getNameableArtefact().setIsCodeUpdated(false);
        }
        item.getNameableArtefact().setName(TsvImportationUtils.tsvLineToInternationalStringBase(header.getName(), columns, item.getNameableArtefact().getName(), isDefaultMaintainer));
        if (item.getNameableArtefact().getName() == null) {
            exceptionItems.add(
                    new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_REQUIRED, item.getNameableArtefact().getCode(), ServiceExceptionParameters.IMPORTATION_TSV_COLUMN_NAME));
        }
        item.getNameableArtefact()
                .setDescription(TsvImportationUtils.tsvLineToInternationalStringBase(header.getDescription(), columns, item.getNameableArtefact().getDescription(), isDefaultMaintainer));
        item.getNameableArtefact().setComment(TsvImportationUtils.tsvLineToInternationalStringBase(header.getComment(), columns, item.getNameableArtefact().getComment(), isDefaultMaintainer));

        if (TypeExternalArtefactsEnum.CONCEPT.equals(itemType)) {
            ((ConceptMetamac) item).setSdmxRelatedArtefact(tsvLineToSdmxRelatedArtefact(header, columns, item, exceptionItems));
        }

        // Do not persist if any error ocurrs
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            return null;
        }

        try {
            if (item.getId() == null) {
                checkCanCreateItem(itemSchemeVersion, item, itemType);
            } else {
                checkCanUpdateItem(itemSchemeVersion, item, itemType);
            }
        } catch (MetamacException metamacException) {
            logger.error("Error importing item from tsv file", metamacException);
            MetamacExceptionItem metamacExceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
            metamacExceptionItem.setExceptionItems(metamacException.getExceptionItems());
            exceptionItems.add(metamacExceptionItem);
        }

        item.setItemSchemeVersion(itemSchemeVersion);
        if (itemParent == null) {
            item.setItemSchemeVersionFirstLevel(itemSchemeVersion);
        } else {
            item.setItemSchemeVersionFirstLevel(null);
        }

        return item;
    }

    private static ConceptRoleEnum tsvLineToSdmxRelatedArtefact(ImportationItemsTsvHeader header, String[] columns, Item item, List<MetamacExceptionItem> exceptionItems) {
        Integer sdmxRelatedArtefactPosition = ((ImportationConceptsTsvHeader) header).getSdmxRelatedArtefactPosition();

        if (sdmxRelatedArtefactPosition != null) {
            String sdmxRelatedArtefact = columns[sdmxRelatedArtefactPosition];
            if (!StringUtils.isBlank(sdmxRelatedArtefact)) {
                if (checkSdmxRelatedArtefactExists(sdmxRelatedArtefact)) {
                    return ConceptRoleEnum.valueOf(sdmxRelatedArtefact);
                } else {
                    exceptionItems
                            .add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_METADATA_UNEXPECTED_SDMX_RELATED_ARTEFACT, item.getNameableArtefact().getCode(), sdmxRelatedArtefact));
                    return null;
                }
            }
        }

        return null;
    }

    private static boolean checkSdmxRelatedArtefactExists(String sdmxRelatedArtefact) {
        for (ConceptRoleEnum concept : ConceptRoleEnum.values()) {
            if (concept.name().equals(sdmxRelatedArtefact)) {
                return true;
            }
        }

        return false;
    }

    public static void checkCanCreateOrUpdateConcept(ConceptSchemeVersionMetamac itemSchemeVersion, ConceptMetamac concept, int lineNumber, List<MetamacExceptionItem> exceptionItems) {
        try {
            if (concept.getId() == null) {
                checkCanCreateItem(itemSchemeVersion, concept, TypeExternalArtefactsEnum.CONCEPT);
            } else {
                checkCanUpdateItem(itemSchemeVersion, concept, TypeExternalArtefactsEnum.CONCEPT);
            }
        } catch (MetamacException metamacException) {
            logger.error("Error importing concept from tsv file", metamacException);
            MetamacExceptionItem metamacExceptionItem = new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_LINE_INCORRECT, lineNumber);
            metamacExceptionItem.setExceptionItems(metamacException.getExceptionItems());
            exceptionItems.add(metamacExceptionItem);
        }
    }

    private static void checkCanCreateItem(ItemSchemeVersion itemSchemeVersion, Item item, TypeExternalArtefactsEnum itemType) throws MetamacException {
        if (TypeExternalArtefactsEnum.CONCEPT.equals(itemType)) {
            ConceptsMetamacInvocationValidator.checkCreateConcept((ConceptSchemeVersionMetamac) itemSchemeVersion, (ConceptMetamac) item, null);
        } else if (TypeExternalArtefactsEnum.AGENCY.equals(itemType) || TypeExternalArtefactsEnum.DATA_CONSUMER.equals(itemType) || TypeExternalArtefactsEnum.DATA_PROVIDER.equals(itemType)
                || TypeExternalArtefactsEnum.ORGANISATION_UNIT.equals(itemType)) {
            OrganisationsMetamacInvocationValidator.checkCreateOrganisation((OrganisationSchemeVersionMetamac) itemSchemeVersion, (OrganisationMetamac) item, null);
        } else if (TypeExternalArtefactsEnum.CATEGORY.equals(itemType)) {
            CategoriesMetamacInvocationValidator.checkCreateCategory((CategorySchemeVersionMetamac) itemSchemeVersion, (CategoryMetamac) item, null);
        }
    }

    private static void checkCanUpdateItem(ItemSchemeVersion itemSchemeVersion, Item item, TypeExternalArtefactsEnum itemType) throws MetamacException {
        if (TypeExternalArtefactsEnum.CONCEPT.equals(itemType)) {
            ConceptsMetamacInvocationValidator.checkUpdateConcept((ConceptSchemeVersionMetamac) itemSchemeVersion, (ConceptMetamac) item, null);
        } else if (TypeExternalArtefactsEnum.AGENCY.equals(itemType) || TypeExternalArtefactsEnum.DATA_CONSUMER.equals(itemType) || TypeExternalArtefactsEnum.DATA_PROVIDER.equals(itemType)
                || TypeExternalArtefactsEnum.ORGANISATION_UNIT.equals(itemType)) {
            OrganisationsMetamacInvocationValidator.checkUpdateOrganisation((OrganisationMetamac) item, null);
        } else if (TypeExternalArtefactsEnum.CATEGORY.equals(itemType)) {
            CategoriesMetamacInvocationValidator.checkUpdateCategory((CategoryMetamac) item, null);
        }
    }

    private static Item createItem(TypeExternalArtefactsEnum type) {
        Item item = null;
        if (TypeExternalArtefactsEnum.CONCEPT.equals(type)) {
            item = new ConceptMetamac();
        } else if (TypeExternalArtefactsEnum.AGENCY.equals(type) || TypeExternalArtefactsEnum.DATA_CONSUMER.equals(type) || TypeExternalArtefactsEnum.DATA_PROVIDER.equals(type)
                || TypeExternalArtefactsEnum.ORGANISATION_UNIT.equals(type)) {
            item = new OrganisationMetamac();
        } else if (TypeExternalArtefactsEnum.CATEGORY.equals(type)) {
            item = new CategoryMetamac();
        }
        return item;
    }

    private static boolean isSemanticIdentifier(String itemIdentifier, TypeExternalArtefactsEnum type) {
        if (TypeExternalArtefactsEnum.CONCEPT.equals(type)) {
            return SemanticIdentifierValidationUtils.isConceptSemanticIdentifier(itemIdentifier);
        } else if (TypeExternalArtefactsEnum.AGENCY.equals(type)) {
            return SemanticIdentifierValidationUtils.isAgencySemanticIdentifier(itemIdentifier);
        } else if (TypeExternalArtefactsEnum.DATA_CONSUMER.equals(type)) {
            return SemanticIdentifierValidationUtils.isDataConsumerSemanticIdentifier(itemIdentifier);
        } else if (TypeExternalArtefactsEnum.DATA_PROVIDER.equals(type)) {
            return SemanticIdentifierValidationUtils.isDataProviderSemanticIdentifier(itemIdentifier);
        } else if (TypeExternalArtefactsEnum.ORGANISATION_UNIT.equals(type)) {
            return SemanticIdentifierValidationUtils.isOrganisationUnitSemanticIdentifier(itemIdentifier);
        } else if (TypeExternalArtefactsEnum.CATEGORY.equals(type)) {
            return SemanticIdentifierValidationUtils.isCategorySemanticIdentifier(itemIdentifier);
        }
        return false;
    }

    private static String generateItemUrn(ItemSchemeVersion itemSchemeVersion, Item item, TypeExternalArtefactsEnum itemType) {
        if (TypeExternalArtefactsEnum.CONCEPT.equals(itemType)) {
            return GeneratorUrnUtils.generateConceptUrn(itemSchemeVersion, item.getNameableArtefact().getCode());
        } else if (TypeExternalArtefactsEnum.AGENCY.equals(itemType) || TypeExternalArtefactsEnum.DATA_CONSUMER.equals(itemType) || TypeExternalArtefactsEnum.DATA_PROVIDER.equals(itemType)
                || TypeExternalArtefactsEnum.ORGANISATION_UNIT.equals(itemType)) {
            return GeneratorUrnUtils.generateOrganisationUrn(itemSchemeVersion, item.getNameableArtefact().getCode());
        } else if (TypeExternalArtefactsEnum.CATEGORY.equals(itemType)) {
            return GeneratorUrnUtils.generateCategoryUrn(itemSchemeVersion, item);
        }
        return null;
    }

    public static String replaceSeparatorLineInText(String line) {
        return line.replace(SrmConstants.TSV_DOUBLE_SPACE, SrmConstants.TSV_BR_TAG);
    }

    public static Item cloneItemInImport(Item source, TypeExternalArtefactsEnum itemType) {
        if (TypeExternalArtefactsEnum.CONCEPT.equals(itemType)) {
            ConceptMetamac target = new ConceptMetamac();
            ConceptMetamac conceptSource = (ConceptMetamac) source;
            target.setNameableArtefact(new NameableArtefact());

            // copy only common international strings metadata. To allow importation of concepts from external agencies without modify the source information.
            target.getNameableArtefact().setName(BaseCopyAllMetadataUtils.copy(conceptSource.getNameableArtefact().getName()));
            target.getNameableArtefact().setDescription(BaseCopyAllMetadataUtils.copy(conceptSource.getNameableArtefact().getDescription()));
            target.getNameableArtefact().setComment(BaseCopyAllMetadataUtils.copy(conceptSource.getNameableArtefact().getComment()));

            return target;
        }
        return source;
    }

    public static void updateOriginalConceptMetamacInImport(ConceptMetamac source, ConceptMetamac target, boolean isTemporalVersion) {

        if (!isTemporalVersion) {
            target.setParent(source.getParent());
            target.setCoreRepresentation(source.getCoreRepresentation());
        }
        target.setUpdateDate(source.getUpdateDate());
        target.setLastUpdated(source.getLastUpdated());
        target.setPluralName(source.getPluralName());
        target.setAcronym(source.getAcronym());
        target.setDescriptionSource(source.getDescriptionSource());
        target.setContext(source.getContext());
        target.setDocMethod(source.getDocMethod());
        target.setSdmxRelatedArtefact(source.getSdmxRelatedArtefact());
        target.setConceptType(source.getConceptType());
        target.setDerivation(source.getDerivation());
        target.setLegalActs(source.getLegalActs());
        target.setConceptExtends(source.getConceptExtends());
        target.setQuantity(source.getQuantity());
        target.setItemSchemeVersionFirstLevel(source.getItemSchemeVersionFirstLevel());
        target.getNameableArtefact().setName(source.getNameableArtefact().getName());
        target.getNameableArtefact().setIsCodeUpdated(source.getNameableArtefact().getIsCodeUpdated());
        target.getNameableArtefact().setDescription(source.getNameableArtefact().getDescription());
        target.getNameableArtefact().setComment(source.getNameableArtefact().getComment());
        target.setVariable(source.getVariable());
    }

    public static ImportationCategoryElementsTsvHeader parseTsvHeaderToImportCategoryElements(String line, List<MetamacExceptionItem> exceptions) throws MetamacException {
        List<String> headersExpected = Arrays.asList(SrmConstants.TSV_HEADER_CODE, SrmConstants.TSV_HEADER_SHORT_NAME, SrmConstants.TSV_HEADER_IS_VISIBLE);

        String[] headerColumns = StringUtils.splitPreserveAllTokens(line, SrmConstants.TSV_SEPARATOR);
        if (headerColumns == null || headerColumns.length < headersExpected.size()) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT, headersExpected.size()));
        }

        int headerExpectedIndex = 0;
        ImportationCategoryElementsTsvHeader header = new ImportationCategoryElementsTsvHeader();
        header.setColumnsSize(headerColumns.length);
        for (int i = 0; i < headerColumns.length; i++) {
            String column = headerColumns[i];
            String[] columnSplited = StringUtils.splitPreserveAllTokens(column, SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR); // some column can be "complex"
            String columnName = columnSplited[0];
            String headerExpected = headersExpected.get(headerExpectedIndex);

            if (SrmConstants.TSV_HEADER_CODE.equals(columnName)) {
                header.setCodePosition(i);
                headerExpectedIndex++;
            } else if (SrmConstants.TSV_HEADER_SHORT_NAME.equals(columnName)) {
                header.setShortName(columnHeaderToInternationalStringTsv(headerColumns, i, columnSplited, headerExpected, header.getShortName(), exceptions));
                if (header.getShortName() == null) {
                    break;
                }
                if (header.getShortName().isEndPositionSetted()) {
                    headerExpectedIndex++;
                }
            } else if (SrmConstants.TSV_HEADER_IS_VISIBLE.equals(columnName)) {
                header.setIsVisiblePosition(i);
                headerExpectedIndex++;
            } else {
                exceptions.add(new MetamacExceptionItem(ServiceExceptionType.IMPORTATION_TSV_HEADER_INCORRECT_COLUMN, ServiceExceptionParameters.createCodeImportation(headerExpected)));
            }
            if (headersExpected.size() <= headerExpectedIndex) {
                break;
            }
        }

        return header;
    }
}
