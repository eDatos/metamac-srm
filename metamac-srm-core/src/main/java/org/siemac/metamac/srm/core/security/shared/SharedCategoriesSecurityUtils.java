package org.siemac.metamac.srm.core.security.shared;

import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.JEFE_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_APOYO_NORMALIZACION;
import static org.siemac.metamac.srm.core.enume.domain.SrmRoleEnum.TECNICO_NORMALIZACION;

import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.sso.client.MetamacPrincipal;

public class SharedCategoriesSecurityUtils extends SharedItemsSecurityUtils {

    //
    // CATEGORY SCHEMES
    //

    public static boolean canRetrieveCategorySchemeByUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canFindCategorySchemesByCondition(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canRetrieveCategorySchemeVersions(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCreateCategoryScheme(MetamacPrincipal metamacPrincipal) {
        return canCreateItemScheme(metamacPrincipal);
    }

    public static boolean canUpdateCategoryScheme(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canUpdateItemScheme(metamacPrincipal, procStatus);
    }

    public static boolean canDeleteCategoryScheme(MetamacPrincipal metamacPrincipal) {
        return canDeleteItemScheme(metamacPrincipal);
    }

    public static boolean canSendCategorySchemeToProductionValidation(MetamacPrincipal metamacPrincipal) {
        return canSendItemSchemeToProductionValidation(metamacPrincipal);
    }

    public static boolean canSendCategorySchemeToDiffusionValidation(MetamacPrincipal metamacPrincipal) {
        return canSendItemSchemeToDiffusionValidation(metamacPrincipal);
    }

    public static boolean canRejectCategorySchemeValidation(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canRejectItemSchemeValidation(metamacPrincipal, procStatus);
    }

    public static boolean canPublishCategorySchemeInternally(MetamacPrincipal metamacPrincipal) {
        return canPublishItemSchemeInternally(metamacPrincipal);
    }

    public static boolean canPublishCategorySchemeExternally(MetamacPrincipal metamacPrincipal) {
        return canPublishItemSchemeExternally(metamacPrincipal);
    }

    public static boolean canResendCategoryScheme(MetamacPrincipal metamacPrincipal) {
        return canResendItemSchemeStreamMessage(metamacPrincipal);
    }

    public static boolean canCopyCategoryScheme(MetamacPrincipal metamacPrincipal) {
        return canCopyItemScheme(metamacPrincipal);
    }

    public static boolean canVersioningCategoryScheme(MetamacPrincipal metamacPrincipal) {
        return canVersioningItemScheme(metamacPrincipal);
    }

    public static boolean canCreateCategorySchemeTemporalVersion(MetamacPrincipal metamacPrincipal) {
        return canVersioningItemScheme(metamacPrincipal);
    }

    public static boolean canAnnounceCategoryScheme(MetamacPrincipal metamacPrincipal) {
        return canAnnounceItemScheme(metamacPrincipal);
    }

    public static boolean canEndCategorySchemeValidity(MetamacPrincipal metamacPrincipal) {
        return canEndItemSchemeValidity(metamacPrincipal);
    }

    //
    // ITEMS
    //

    public static boolean canRetrieveCategoryByUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canRetrieveCategoriesByCategorySchemeUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canFindCategoriesByCondition(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    /**
     * Determines if items from an category scheme can be created, deleted or updated
     */
    public static boolean canModifyCategoryFromCategoryScheme(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canModifyItemFromItemScheme(metamacPrincipal, procStatus);
    }

    public static boolean canRetrieveCategorisationByUrn(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    /**
     * Determines if categorisations from an category scheme can be created or deleted
     */
    public static boolean canModifyCategorisationFromCategoryScheme(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canUpdateCategoryScheme(metamacPrincipal, procStatus);
    }

    public static boolean canExportCategoriesTsv(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canImportCategoriesTsv(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canModifyCategoryFromCategoryScheme(metamacPrincipal, procStatus);
    }

    /**
     * Determines if items from an category scheme can be created, deleted or updated
     */
    public static boolean canModifyCategoryElementFromCategoryScheme(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canModifyCategoryElement(metamacPrincipal, procStatus);
    }

    public static boolean canCrudCategoryElement(MetamacPrincipal metamacPrincipal) {
        return isSrmRoleAllowed(metamacPrincipal, TECNICO_APOYO_NORMALIZACION, TECNICO_NORMALIZACION, JEFE_NORMALIZACION);
    }

    public static boolean canRetrieveOrFindCategoryElement(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canUpdateCategoryCategoryElement(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canModifyItemFromItemScheme(metamacPrincipal, procStatus);
    }

    public static boolean canExportCategoryElementsTsv(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canImportCategoryElements(MetamacPrincipal metamacPrincipal) {
        return canCrudCategoryElement(metamacPrincipal);
    }

    public static boolean canNormaliseCategoryElementsToCategories(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    // CATEGORY SCHEME ORDER VISUALISATIONS

    public static boolean canRetrieveOrFindCategorySchemeOrderVisualisation(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

    public static boolean canCrudCategorySchemeOrderVisualisation(MetamacPrincipal metamacPrincipal, ProcStatusEnum categorySchemeVersionProcStatus) {
        return canUpdateItemScheme(metamacPrincipal, categorySchemeVersionProcStatus);
    }

    public static boolean canImportCategorySchemeOrderVisualisations(MetamacPrincipal metamacPrincipal, ProcStatusEnum procStatus) {
        return canModifyCategoryFromCategoryScheme(metamacPrincipal, procStatus);
    }

    public static boolean canExportCategoryOrdersTsv(MetamacPrincipal metamacPrincipal) {
        return canRetrieveOrFindResource(metamacPrincipal);
    }

}
