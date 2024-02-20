package org.siemac.metamac.srm.core.common.service.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.base.domain.ResourceElement;
import org.siemac.metamac.srm.core.category.domain.CategoryElement;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.code.domain.VariableElementResult;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.constants.SrmConstants;

import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.LocalisedString;

public class TsvExportationUtils {

    private TsvExportationUtils() {
    }

    // ---------------------------------------------------------------------------------------------------------------
    // CODES
    // ---------------------------------------------------------------------------------------------------------------

    public static String exportCodes(List<ItemResult> items, List<String> languages) throws MetamacException {
        OutputStream outputStream = null;
        OutputStreamWriter writer = null;
        File file = null;
        try {
            file = File.createTempFile("codes", ".tsv");
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, SrmConstants.TSV_EXPORTATION_ENCODING);

            writeCodesHeader(writer, languages);

            for (ItemResult itemResult : items) {
                writer.write(SrmConstants.TSV_LINE_SEPARATOR);
                writeItemCode(writer, itemResult);
                writeParentCode(writer, itemResult);
                writeItemExtensionPointIsExtended(writer, itemResult);
                writeCodeVariableElement(writer, itemResult);
                writeItemName(writer, itemResult, languages);
                writeItemDescription(writer, itemResult, languages);
                writeItemComment(writer, itemResult, languages);
                writeItemExtensionPointShortName(writer, itemResult, languages);
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

    private static void writeCodesHeader(OutputStreamWriter writer, List<String> languages) throws IOException {
        writer.write(SrmConstants.TSV_HEADER_CODE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_PARENT);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_IS_EXTENDED);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_VARIABLE_ELEMENT);
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_NAME + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_DESCRIPTION + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_COMMENT + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_SHORT_NAME + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }

    }

    private static void writeCodeVariableElement(OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        writer.write(SrmConstants.TSV_SEPARATOR);
        String variableElement = SrmServiceUtils.getCodeItemResultVariableElementCode(itemResult);
        if (variableElement != null) {
            writer.write(variableElement);
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // CONCEPTS
    // ---------------------------------------------------------------------------------------------------------------

    public static String exportConcepts(ConceptSchemeVersionMetamac conceptSchemeVersion, List<ItemResult> items, List<String> languages) throws MetamacException {
        OutputStream outputStream = null;
        OutputStreamWriter writer = null;
        File file = null;
        try {
            file = File.createTempFile("concepts", ".tsv");
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, SrmConstants.TSV_EXPORTATION_ENCODING);

            writeConceptsHeader(conceptSchemeVersion, writer, languages);

            for (ItemResult itemResult : items) {
                writer.write(SrmConstants.TSV_LINE_SEPARATOR);
                writeItemCode(writer, itemResult);
                writeParentCode(writer, itemResult);
                writeItemName(writer, itemResult, languages);
                writeItemDescription(writer, itemResult, languages);
                writeItemComment(writer, itemResult, languages);
                writeItemExtensionPointPluralName(writer, itemResult, languages);
                writeItemExtensionPointAcronym(writer, itemResult, languages);
                writeItemExtensionPointDescriptionSource(writer, itemResult, languages);
                writeItemExtensionPointContext(writer, itemResult, languages);
                writeItemExtensionPointDocMethod(writer, itemResult, languages);
                writeItemExtensionPointDerivation(writer, itemResult, languages);
                writeItemExtensionPointLegalActs(writer, itemResult, languages);
                writeConceptType(writer, itemResult);
                writeRepresentation(writer, itemResult);
                writeConceptExtends(writer, itemResult);
                writeSdmxRelatedArtefact(conceptSchemeVersion, writer, itemResult);

                writeQuantityMetadata(writer, itemResult, languages);

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

    private static void writeConceptsHeader(ConceptSchemeVersionMetamac conceptSchemeVersion, OutputStreamWriter writer, List<String> languages) throws IOException {
        writer.write(SrmConstants.TSV_HEADER_CODE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_PARENT);
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_NAME + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_DESCRIPTION + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_COMMENT + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_PLURAL_NAME + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_ACRONYM + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_DESCRIPTION_SOURCE + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_CONTEXT + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_DOC_METHOD + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_DERIVATION + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_LEGAL_ACTS + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_CONCEPT_TYPE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_REPRESENTATION + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + SrmConstants.TSV_HEADER_REPRESENTATION_SUB_TYPE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_REPRESENTATION + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + SrmConstants.TSV_HEADER_REPRESENTATION_SUB_VALUE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_CONCEPT_EXTENDS);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_SDMX_RELATED_ARTEFACT);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_QUANTITY_TYPE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_UNIT_CODELIST_URN);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_UNIT_CODE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_UNIT_SYMBOL_POSITION);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_UNIT_MULTIPLIER_CODELIST_URN);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_UNIT_MULTIPLIER_CODE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_SIGNIFICANT_DIGITS);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_DECIMAL_PLACES);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_MINIMUM);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_MAXIMUM);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_DENOMINATOR_CONCEPT_SCHEME);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_DENOMINATOR_CONCEPT);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_NUMERATOR_CONCEPT_SCHEME);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_NUMERATOR_CONCEPT);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_IS_PERCENTAGE);
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_PERCENTAGE_OF + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_INDEX_BASE_VALUE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_INDEX_BASE_TIME);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_INDEX_BASE_LOCATION_CODELIST_URN);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_INDEX_BASE_LOCATION_CODE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_BASE_QUANTITY_CONCEPT_SCHEME);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_BASE_QUANTITY_CONCEPT);
    }

    // ---------------------------------------------------------------------------------------------------------------
    // ORGANISATIONS
    // ---------------------------------------------------------------------------------------------------------------

    public static String exportOrganisations(List<ItemResult> items, List<String> languages) throws MetamacException {
        OutputStream outputStream = null;
        OutputStreamWriter writer = null;
        File file = null;
        try {
            file = File.createTempFile("organisations", ".tsv");
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, SrmConstants.TSV_EXPORTATION_ENCODING);

            writeOrganisationsHeader(writer, languages);

            for (ItemResult itemResult : items) {
                writer.write(SrmConstants.TSV_LINE_SEPARATOR);
                writeItemCode(writer, itemResult);
                writeParentCode(writer, itemResult);
                writeItemName(writer, itemResult, languages);
                writeItemDescription(writer, itemResult, languages);
                writeItemComment(writer, itemResult, languages);
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

    private static void writeOrganisationsHeader(OutputStreamWriter writer, List<String> languages) throws IOException {
        writer.write(SrmConstants.TSV_HEADER_CODE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_PARENT);
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_NAME + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_DESCRIPTION + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_COMMENT + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // CATEGORIES
    // ---------------------------------------------------------------------------------------------------------------

    public static String exportCategories(List<ItemResult> items, List<String> languages) throws MetamacException {
        OutputStream outputStream = null;
        OutputStreamWriter writer = null;
        File file = null;
        try {
            file = File.createTempFile("categories", ".tsv");
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, SrmConstants.TSV_EXPORTATION_ENCODING);

            writeCategoriesHeader(writer, languages);

            for (ItemResult itemResult : items) {
                writer.write(SrmConstants.TSV_LINE_SEPARATOR);
                writeItemCode(writer, itemResult);
                writeParentCodeFull(writer, itemResult);
                writeCategoryElementByCategory(writer, itemResult);
                writeItemExtensionPointEniSchemeIdentifier(writer, itemResult);
                writeItemName(writer, itemResult, languages);
                writeItemDescription(writer, itemResult, languages);
                writeItemComment(writer, itemResult, languages);
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

    private static void writeCategoriesHeader(OutputStreamWriter writer, List<String> languages) throws IOException {
        writer.write(SrmConstants.TSV_HEADER_CODE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_PARENT);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_CATEGORY_ELEMENT);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_ENI_SCHEME);
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_NAME + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_DESCRIPTION + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_COMMENT + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
    }

    private static void writeCategoryElementByCategory(OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        writer.write(SrmConstants.TSV_SEPARATOR);
        String categoryElement = SrmServiceUtils.getCategoryItemResultCategoryElementCode(itemResult);
        if (categoryElement != null) {
            writer.write(categoryElement);
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // VARIABLE ELEMENTS
    // ---------------------------------------------------------------------------------------------------------------

    public static String exportVariableElements(List<VariableElementResult> variableElements, List<String> languages) throws MetamacException {
        OutputStream outputStream = null;
        OutputStreamWriter writer = null;
        File file = null;
        try {
            file = File.createTempFile("variable_elements", ".tsv");
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, SrmConstants.TSV_EXPORTATION_ENCODING);

            writeVariableElementsHeader(writer, languages);

            for (VariableElementResult variableElementResult : variableElements) {
                writer.write(SrmConstants.TSV_LINE_SEPARATOR);
                writeVariableElementCode(writer, variableElementResult);
                writeVariableElementGeographicalGranularity(writer, variableElementResult);
                writeVariableElementShortName(writer, variableElementResult, languages);
                writeVariableElementRenderingColor(writer, variableElementResult);
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

    private static void writeVariableElementsHeader(OutputStreamWriter writer, List<String> languages) throws IOException {
        writer.write(SrmConstants.TSV_HEADER_CODE);
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_GEOGRAPHICAL_GRANULARITY);
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_SHORT_NAME + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
        writer.write(SrmConstants.TSV_SEPARATOR);
        writer.write(SrmConstants.TSV_HEADER_RENDERING_COLOR);
    }

    // ---------------------------------------------------------------------------------------------------------------
    // COMMON UTILS
    // ---------------------------------------------------------------------------------------------------------------

    private static void writeItemCode(OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        writer.write(itemResult.getCode());
    }

    private static void writeParentCode(OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        writer.write(SrmConstants.TSV_SEPARATOR);
        if (itemResult.getParent() != null) {
            writer.write(itemResult.getParent().getCode());
        }
    }

    private static void writeParentCodeFull(OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        writer.write(SrmConstants.TSV_SEPARATOR);
        if (itemResult.getParent() != null) {
            writer.write(itemResult.getParent().getCodeFull());
        }
    }

    private static void writeItemExtensionPointEniSchemeIdentifier(OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        CategoryMetamacResultExtensionPoint extensionPoint = (CategoryMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        writer.write(SrmConstants.TSV_SEPARATOR);
        if (extensionPoint.getEniSchemaCodes() != null && !extensionPoint.getEniSchemaCodes().isEmpty()) {
            writer.write(String.join(",", extensionPoint.getEniSchemaCodes()));
        }
    }

    private static void writeItemName(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        for (String language : languages) {
            String nameInLocale = itemResult.getName().get(language);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (nameInLocale != null) {
                writer.write(nameInLocale);
            }
        }
    }

    private static void writeItemDescription(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        for (String language : languages) {
            String descriptionInLocale = itemResult.getDescription().get(language);
            descriptionInLocale = removeUnsupportedCharaters(descriptionInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (descriptionInLocale != null) {
                writer.write(descriptionInLocale);
            }
        }
    }

    private static void writeItemComment(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        for (String language : languages) {
            String textInLocale = itemResult.getComment().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }

    private static void writeItemExtensionPointPluralName(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        for (String language : languages) {
            String textInLocale = extensionPoint.getPluralName().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }

    private static void writeItemExtensionPointAcronym(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        for (String language : languages) {
            String textInLocale = extensionPoint.getAcronym().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }
    private static void writeItemExtensionPointDescriptionSource(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        for (String language : languages) {
            String textInLocale = extensionPoint.getDescriptionSource().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }
    private static void writeItemExtensionPointContext(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        for (String language : languages) {
            String textInLocale = extensionPoint.getContext().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }
    private static void writeItemExtensionPointDocMethod(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        for (String language : languages) {
            String textInLocale = extensionPoint.getDocMethod().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }
    private static void writeItemExtensionPointDerivation(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        for (String language : languages) {
            String textInLocale = extensionPoint.getDerivation().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }
    private static void writeItemExtensionPointLegalActs(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        for (String language : languages) {
            String textInLocale = extensionPoint.getLegalActs().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }

    private static void writeItemExtensionPointShortName(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        CodeMetamacResultExtensionPoint extensionPoint = (CodeMetamacResultExtensionPoint) itemResult.getExtensionPoint();

        for (String language : languages) {
            String textInLocale = extensionPoint.getShortName().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }

    private static void writeItemExtensionPointIsExtended(OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        writer.write(SrmConstants.TSV_SEPARATOR);
        CodeMetamacResultExtensionPoint extensionPoint = (CodeMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        if (extensionPoint.getIsExtended() != null) {
            writer.write(String.valueOf(extensionPoint.getIsExtended()));
        }
    }

    private static void writeConceptType(OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        writer.write(SrmConstants.TSV_SEPARATOR);
        if (!StringUtils.isBlank(extensionPoint.getConceptTypeIdentifier())) {
            writer.write(extensionPoint.getConceptTypeIdentifier());
        }
    }

    private static void writeRepresentation(OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        writer.write(SrmConstants.TSV_SEPARATOR);
        if (extensionPoint.getRepresentationType() != null) {
            writer.write(extensionPoint.getRepresentationType().name());
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(extensionPoint.getRepresentationValue());
        } else {
            writer.write(SrmConstants.TSV_SEPARATOR);
        }
    }

    private static void writeQuantityMetadata(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        writer.write(SrmConstants.TSV_SEPARATOR);

        if (extensionPoint.getQuantityType() != null) {
            writer.write(extensionPoint.getQuantityType().name());
        }

        writer.write(SrmConstants.TSV_SEPARATOR);

        if (!StringUtils.isBlank(extensionPoint.getUnitCode()) && !StringUtils.isBlank(extensionPoint.getUnitCodelistUrn())) {
            writer.write(extensionPoint.getUnitCodelistUrn());
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(extensionPoint.getUnitCode());
        } else {
            writer.write(SrmConstants.TSV_SEPARATOR);
        }

        writer.write(SrmConstants.TSV_SEPARATOR);
        if (extensionPoint.getUnitSymbolPosition() != null) {
            writer.write(extensionPoint.getUnitSymbolPosition().getName());
        }

        writer.write(SrmConstants.TSV_SEPARATOR);
        if (!StringUtils.isBlank(extensionPoint.getUnitMultiplierCode()) && !StringUtils.isBlank(extensionPoint.getUnitMultiplierCodelistUrn())) {
            writer.write(extensionPoint.getUnitMultiplierCodelistUrn());
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(extensionPoint.getUnitMultiplierCode());
        } else {
            writer.write(SrmConstants.TSV_SEPARATOR);
        }

        writer.write(SrmConstants.TSV_SEPARATOR);
        if (extensionPoint.getSignificantDigits() != null) {
            writer.write(Integer.toString(extensionPoint.getSignificantDigits()));
        }

        writer.write(SrmConstants.TSV_SEPARATOR);
        if (extensionPoint.getDecimalPlaces() != null) {
            writer.write(Integer.toString(extensionPoint.getDecimalPlaces()));
        }

        writer.write(SrmConstants.TSV_SEPARATOR);
        if (extensionPoint.getMiminum() != null) {
            writer.write(Integer.toString(extensionPoint.getMiminum()));
        }

        writer.write(SrmConstants.TSV_SEPARATOR);
        if (extensionPoint.getMaximum() != null) {
            writer.write(Integer.toString(extensionPoint.getMaximum()));
        }

        writer.write(SrmConstants.TSV_SEPARATOR);

        if (!StringUtils.isBlank(extensionPoint.getDenominatorConcept()) && !StringUtils.isBlank(extensionPoint.getDenominatorConceptScheme())) {
            writer.write(extensionPoint.getDenominatorConceptScheme());
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(extensionPoint.getDenominatorConcept());
        } else {
            writer.write(SrmConstants.TSV_SEPARATOR);
        }

        writer.write(SrmConstants.TSV_SEPARATOR);

        if (!StringUtils.isBlank(extensionPoint.getNumeratorConcept()) && !StringUtils.isBlank(extensionPoint.getNumeratorConceptScheme())) {
            writer.write(extensionPoint.getNumeratorConceptScheme());
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(extensionPoint.getNumeratorConcept());
        } else {
            writer.write(SrmConstants.TSV_SEPARATOR);
        }

        writer.write(SrmConstants.TSV_SEPARATOR);
        if (extensionPoint.getIsPercentage() != null) {
            writer.write(extensionPoint.getIsPercentage().toString());
        }

        writeItemExtensionPointPercentageOf(writer, itemResult, languages);

        writer.write(SrmConstants.TSV_SEPARATOR);
        if (extensionPoint.getIndexBaseValue() != null) {
            writer.write(Integer.toString(extensionPoint.getIndexBaseValue()));
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_SEPARATOR);
        } else if (!StringUtils.isBlank(extensionPoint.getIndexBaseTime())) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(extensionPoint.getIndexBaseTime());
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_SEPARATOR);
        } else if (!StringUtils.isBlank(extensionPoint.getIndexBaseLocationCode()) && !StringUtils.isBlank(extensionPoint.getIndexBaseLocationCodelistUrn())) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(extensionPoint.getIndexBaseLocationCodelistUrn());
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(extensionPoint.getIndexBaseLocationCode());
        } else {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_SEPARATOR);
        }

        writer.write(SrmConstants.TSV_SEPARATOR);
        if (!StringUtils.isBlank(extensionPoint.getIndexBaseQuantityConceptScheme()) && !StringUtils.isBlank(extensionPoint.getIndexBaseQuantityConcept())) {
            writer.write(extensionPoint.getIndexBaseQuantityConceptScheme());
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(extensionPoint.getIndexBaseQuantityConcept());
        } else {
            writer.write(SrmConstants.TSV_SEPARATOR);
        }
    }

    private static void writeItemExtensionPointPercentageOf(OutputStreamWriter writer, ItemResult itemResult, List<String> languages) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        for (String language : languages) {
            String textInLocale = extensionPoint.getPercentageOf().get(language);
            textInLocale = removeUnsupportedCharaters(textInLocale);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (textInLocale != null) {
                writer.write(textInLocale);
            }
        }
    }

    private static void writeConceptExtends(OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
        writer.write(SrmConstants.TSV_SEPARATOR);
        if (!StringUtils.isBlank(extensionPoint.getConceptExtendsUrn())) {
            writer.write(extensionPoint.getConceptExtendsUrn());
        }
    }

    private static void writeSdmxRelatedArtefact(ConceptSchemeVersionMetamac conceptSchemeVersion, OutputStreamWriter writer, ItemResult itemResult) throws IOException {
        if (ConceptSchemeTypeEnum.OPERATION.equals(conceptSchemeVersion.getType()) || ConceptSchemeTypeEnum.TRANSVERSAL.equals(conceptSchemeVersion.getType())) {
            ConceptMetamacResultExtensionPoint extensionPoint = (ConceptMetamacResultExtensionPoint) itemResult.getExtensionPoint();
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (extensionPoint.getSdmxRelatedArtefact() != null) {
                writer.write(extensionPoint.getSdmxRelatedArtefact().toString());
            }
        } else {
            writer.write(SrmConstants.TSV_SEPARATOR);
        }
    }

    public static String removeUnsupportedCharaters(String string) {
        if (StringUtils.isNotBlank(string)) {
            string = string.replace('\n', ' ');
            string = string.replace('\t', ' ');
            string = string.replace('\r', ' ');
            string = string.replace('\b', ' ');
            string = string.replace('\f', ' ');
        }
        return string;
    }

    private static void writeVariableElementCode(OutputStreamWriter writer, VariableElementResult variableElementResult) throws IOException {
        writer.write(variableElementResult.getCode());
    }

    private static void writeVariableElementGeographicalGranularity(OutputStreamWriter writer, VariableElementResult variableElementResult) throws IOException {
        ItemResult geographicalGranularity = variableElementResult.getGeographicalGranularity();
        writer.write(SrmConstants.TSV_SEPARATOR);
        if (geographicalGranularity != null) {
            writer.write(geographicalGranularity.getCode());
        }
    }

    private static void writeVariableElementShortName(OutputStreamWriter writer, VariableElementResult variableElementResult, List<String> languages) throws IOException {
        for (String language : languages) {
            String nameInLocale = variableElementResult.getShortName().get(language);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (nameInLocale != null) {
                writer.write(nameInLocale);
            }
        }
    }

    private static void writeVariableElementRenderingColor(OutputStreamWriter writer, VariableElementResult variableElementResult) throws IOException {
        writer.write(SrmConstants.TSV_SEPARATOR);
        String renderingColor = variableElementResult.getRenderingColor();
        if (renderingColor != null) {
            writer.write(renderingColor);
        }
    }

    // ---------------------------------------------------------------------------------------------------------------
    // CATEGORY ELEMENTS
    // ---------------------------------------------------------------------------------------------------------------

    public static String exportCategoryElements(List<CategoryElement> categoryElements, List<String> languages) throws MetamacException {

        OutputStream outputStream = null;
        OutputStreamWriter writer = null;
        File file = null;
        try {
            file = File.createTempFile("category_elements", ".tsv");
            outputStream = new FileOutputStream(file);
            writer = new OutputStreamWriter(outputStream, SrmConstants.TSV_EXPORTATION_ENCODING);
            writeResourceElementsHeader(writer, languages);
            for (CategoryElement categoryElement : categoryElements) {
                writer.write(SrmConstants.TSV_LINE_SEPARATOR);
                writeResourceElementCode(writer, categoryElement);
                writeResourceElementShortName(writer, categoryElement, languages);
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

    private static void writeResourceElementsHeader(OutputStreamWriter writer, List<String> languages) throws IOException {
        writer.write(SrmConstants.TSV_HEADER_CODE);
        for (String language : languages) {
            writer.write(SrmConstants.TSV_SEPARATOR);
            writer.write(SrmConstants.TSV_HEADER_SHORT_NAME + SrmConstants.TSV_HEADER_INTERNATIONAL_STRING_SEPARATOR + language);
        }
    }

    private static void writeResourceElementCode(OutputStreamWriter writer, ResourceElement resourceElement) throws IOException {
        writer.write(resourceElement.getIdentifiableArtefact().getCode());
    }

    private static void writeResourceElementShortName(OutputStreamWriter writer, ResourceElement resourceElement, List<String> languages) throws IOException {
        for (String language : languages) {
            String nameInLocale = getLocalisedStringFormattedByLang(resourceElement.getShortName().getTexts(), language);
            writer.write(SrmConstants.TSV_SEPARATOR);
            if (nameInLocale != null) {
                writer.write(nameInLocale);
            }
        }
    }

    private static String getLocalisedStringFormattedByLang(Set<LocalisedString> source, String language) {
        for (LocalisedString loc : source) {
            if (language.equals(loc.getLocale())) {
                return removeUnsupportedCharaters(loc.getLabel());
            }
        }
        return null;
    }
}
