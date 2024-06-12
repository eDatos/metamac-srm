package org.siemac.metamac.srm.core.category.serviceimpl.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.srm.core.category.domain.CategoryElement;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.common.service.utils.SemanticIdentifierValidationUtils;
import org.siemac.metamac.srm.core.constants.SrmConstants;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;
import com.arte.statistic.sdmx.srm.core.category.serviceimpl.utils.CategoriesInvocationValidator;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResultSelection;

public class CategoriesMetamacInvocationValidator extends CategoriesInvocationValidator {

    public static void checkCreateCategoryScheme(CategorySchemeVersionMetamac categorySchemeVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(categorySchemeVersion, ServiceExceptionParameters.CATEGORY_SCHEME, exceptions);
        if (categorySchemeVersion != null) {
            checkCategoryScheme(categorySchemeVersion, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateCategoryScheme(CategorySchemeVersionMetamac categorySchemeVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(categorySchemeVersion, ServiceExceptionParameters.CATEGORY_SCHEME, exceptions);
        if (categorySchemeVersion != null) {
            checkCategoryScheme(categorySchemeVersion, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCreateCategory(CategorySchemeVersionMetamac categorySchemeVersion, CategoryMetamac category, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(categorySchemeVersion, ServiceExceptionParameters.CATEGORY_SCHEME, exceptions);
        ValidationUtils.checkParameterRequired(category, ServiceExceptionParameters.CATEGORY, exceptions);
        if (category != null && categorySchemeVersion != null) {
            checkCategory(categorySchemeVersion, category, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateCategory(CategoryMetamac category, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(category, ServiceExceptionParameters.CATEGORY, exceptions);
        if (category != null) {
            checkCategory(category.getItemSchemeVersion(), category, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveCategoriesByCategorySchemeUrn(String categorySchemeUrn, String locale, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(categorySchemeUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(locale, ServiceExceptionParameters.LOCALE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveCategoriesByCategorySchemeUrnUnordered(String categorySchemeUrn, ItemResultSelection itemResultSelection, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(categorySchemeUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkImportCategoriesTsv(String categorySchemeUrn, File file, String fileName, boolean updateAlreadyExisting, Boolean canBeBackground, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(categorySchemeUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(file, ServiceExceptionParameters.STREAM, exceptions);
        ValidationUtils.checkParameterRequired(fileName, ServiceExceptionParameters.FILE_NAME, exceptions);
        ValidationUtils.checkParameterRequired(canBeBackground, ServiceExceptionParameters.CAN_BE_BACKGROUND, exceptions);
        ValidationUtils.checkParameterRequired(updateAlreadyExisting, ServiceExceptionParameters.IMPORTATION_TSV_UPDATE_ALREADY_EXISTING, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkExportCategoriesTsv(String categorySchemeUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(categorySchemeUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveCategorySchemeByCategoryUrn(String categoryUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(categoryUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    // ---------------------------------------------------------------------------
    // CATEGORY ELEMENTS
    // ---------------------------------------------------------------------------

    public static void checkCreateCategoryElement(CategoryElement categoryElement, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        checkCategoryElement(categoryElement, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateCategoryElement(CategoryElement categoryElement, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        if (categoryElement != null) {
            ValidationUtils.checkMetadataRequired(categoryElement.getIdentifiableArtefact().getUrn(), ServiceExceptionParameters.IDENTIFIABLE_ARTEFACT_URN, exceptions);
        }
        checkCategoryElement(categoryElement, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateCategoryElementOfCategory(String categoryUrn, String categoryElementUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(categoryUrn, ServiceExceptionParameters.URN, exceptions);
        // categoryElementUrn is optional

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveCategoryElement(String categoryElementUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(categoryElementUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateCodesCategoryElements(String codelistUrn, Map<Long, Long> categoryElementsIdByCodeId, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(codelistUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(categoryElementsIdByCodeId, ServiceExceptionParameters.ID, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkExportCategoryElementsTsv(List<String> categoryElementUrns, List<MetamacExceptionItem> exceptions) throws MetamacException {
        // no check required.
    }

    public static void checkImportCategoryElementsTsv(File file, String fileName, Boolean updateAlreadyExisting, Boolean canBeBackground, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(file, ServiceExceptionParameters.STREAM, exceptions);
        ValidationUtils.checkParameterRequired(canBeBackground, ServiceExceptionParameters.CAN_BE_BACKGROUND, exceptions);
        ValidationUtils.checkParameterRequired(updateAlreadyExisting, ServiceExceptionParameters.IMPORTATION_TSV_UPDATE_ALREADY_EXISTING, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkCategoryElement(CategoryElement categoryElement, List<MetamacExceptionItem> exceptions) {
        ValidationUtils.checkParameterRequired(categoryElement, ServiceExceptionParameters.RESOURCE_ELEMENT, exceptions);
        if (categoryElement == null) {
            return;
        }

        // Check required metadata
        checkIdentifiableArtefact(categoryElement.getIdentifiableArtefact(), exceptions);
        if (categoryElement.getIdentifiableArtefact() != null) {
            SemanticIdentifierValidationUtils.checkCategoryElementSemanticIdentifier(categoryElement, exceptions);
        }
        ValidationUtils.checkMetadataRequired(categoryElement.getShortName(), ServiceExceptionParameters.RESOURCE_ELEMENT_SHORT_NAME, exceptions);
        ValidationUtils.checkInternationalStringMaximumLength(categoryElement.getShortName(), ServiceExceptionParameters.RESOURCE_ELEMENT_SHORT_NAME, SrmConstants.METADATA_SHORT_NAME_MAXIMUM_LENGTH,
                exceptions);

        ValidationUtils.checkMetadataOptionalIsValid(categoryElement.getComment(), ServiceExceptionParameters.RESOURCE_ELEMENT_COMMENT, exceptions);

        // Check dates: validFrom value must be lower than validTo value
        ValidationUtils.checkDateTimeBeforeDateTime(categoryElement.getValidFrom(), categoryElement.getValidTo(), ServiceExceptionParameters.RESOURCE_ELEMENT_VALID_FROM,
                ServiceExceptionParameters.RESOURCE_ELEMENT_VALID_TO, exceptions);
    }

    public static void checkNormaliseCategoryElementsToCategories(String categorySchemeVersionUrn, String locale, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(categorySchemeVersionUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(locale, ServiceExceptionParameters.LOCALE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkCategoryScheme(CategorySchemeVersionMetamac categorySchemeVersion, List<MetamacExceptionItem> exceptions) {
        if (categorySchemeVersion.getMaintainableArtefact() != null && BooleanUtils.isTrue(categorySchemeVersion.getMaintainableArtefact().getIsExternalReference())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_IS_EXTERNAL_REFERENCE));
        }
        if (BooleanUtils.isTrue(categorySchemeVersion.getIsPartial())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL));
        }
    }

    private static void checkCategory(ItemSchemeVersion categorySchemeVersion, CategoryMetamac category, List<MetamacExceptionItem> exceptions) {

        // common metadata in sdmx are checked in Sdmx module
    }

    public static void checkImportCategoryElementLogo(String categoryElementUrn, String fileName, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(categoryElementUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(fileName, ServiceExceptionParameters.RESOURCE_ELEMENT_LOGO, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }
}
