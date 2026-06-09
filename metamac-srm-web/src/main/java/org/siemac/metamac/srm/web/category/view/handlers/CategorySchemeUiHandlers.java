package org.siemac.metamac.srm.web.category.view.handlers;

import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.enume.domain.VersionTypeEnum;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeVisualisationDto;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.enums.ExportDetailEnum;
import org.siemac.metamac.srm.web.client.enums.ExportReferencesEnum;

public interface CategorySchemeUiHandlers extends BaseCategoryUiHandlers {

    void retrieveCategorySchemeVersions(String categorySchemeUrn);
    void saveCategoryScheme(CategorySchemeMetamacDto categoryScheme);
    void deleteCategoryScheme(String urn);
    void cancelValidity(String urn);
    void retrieveLatestCategoryScheme(CategorySchemeMetamacDto conceptSchemeMetamacDto);
    void exportCategoryScheme(String urn, ExportDetailEnum infoAmount, ExportReferencesEnum references);
    void copyCategoryScheme(String urn);
    void copyCategoryScheme(String urn, String code);

    // Categories
    void exportCategories(String categorySchemeUrn);
    void retrieveCategories();

    void normaliseCategoryElementsToCodes(String categorySchemeUrn, String locale, boolean onlyNormaliseCodesWithoutCategoryElement);
    void updateCodesCategoryElements(String categorySchemeUrn, Map<Long, Long> categoryElementsIdByCodeId);
    void retrieveCategoryElementsForManualNormalisation(int firstResult, int maxResults, String criteria);

    // Stream messages
    void reSendStreamMessageCategoryScheme(String urn);

    // Life cycle

    void sendToProductionValidation(String urn, ProcStatusEnum currentProcStatus);
    void sendToDiffusionValidation(String urn, ProcStatusEnum currentProcStatus);
    void rejectValidation(String urn, ProcStatusEnum currentProcStatus);
    void publishInternally(String urn, ProcStatusEnum currentProcStatus, Boolean forceLatestFinal);
    void publishExternally(String urn, ProcStatusEnum currentProcStatus);
    void versioning(String urn, VersionTypeEnum versionType);
    void createTemporalVersion(String urn);

    // Orders

    void retrieveCategoriesWithOrder(String orderVisualisationUrn);
    void saveCategorySchemeOrder(CategorySchemeVisualisationDto categorySchemeVisualisationDto);
    void deleteCategorySchemeOrders(List<String> orderUrns);
    void updateCategoryInOrder(String categoryUrn, String orderVisualisationUrn, int newCategoryIndex);
    void exportCategoriesOrder(String categorySchemeUrn);

    // Importation

    void resourceImportationFailed(String errorMessage);
    void resourceImportationSucceed(String fileName);

    void showWaitPopup();
}
