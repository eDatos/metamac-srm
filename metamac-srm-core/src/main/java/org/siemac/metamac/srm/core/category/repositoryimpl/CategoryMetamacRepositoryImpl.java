package org.siemac.metamac.srm.core.category.repositoryimpl;

import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getLong;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.category.domain.CategoryElementResult;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacResultSelection;
import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalog;
import org.siemac.metamac.srm.core.master.domain.EniSchemaCatalogRepository;
import org.siemac.metamac.srm.core.master.exception.EniSchemaCatalogNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemRepository;
import com.arte.statistic.sdmx.srm.core.category.domain.Category;
import com.arte.statistic.sdmx.srm.core.category.domain.CategoryRepository;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;

/**
 * Repository implementation for CategoryMetamac
 */
@Repository("categoryMetamacRepository")
public class CategoryMetamacRepositoryImpl extends CategoryMetamacRepositoryBase {

    @Autowired
    private CategoryRepository         categoryRepository;

    @Autowired
    private ItemRepository             itemRepository;

    @Autowired
    private SrmConfiguration           srmConfiguration;

    @Autowired
    private EniSchemaCatalogRepository eniSchemeCatalogMetamacRepository;

    public CategoryMetamacRepositoryImpl() {
    }

    @Override
    public CategoryMetamac findByUrn(String urn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", urn);
        List<CategoryMetamac> result = findByQuery("from CategoryMetamac where nameableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    @Override
    public List<ItemResult> findCategoriesByCategorySchemeUnordered(Long categorySchemeVersionId, ItemMetamacResultSelection resultSelection) throws MetamacException {
        // Find items
        List<ItemResult> categories = categoryRepository.findCategoriesByCategorySchemeUnordered(categorySchemeVersionId, resultSelection);

        // eniScheme extension point
        executeQueryCategoryEniSchemeAndUpdateCodeMetamacResult(categorySchemeVersionId, categories);

        return categories;
    }

    private void executeQueryCategoryEniSchemeAndUpdateCodeMetamacResult(Long categorySchemeVersionId, List<ItemResult> categories) throws MetamacException {
        Map<Long, ItemResult> mapCategoriesByItemId = new HashMap<Long, ItemResult>(categories.size());

        for (ItemResult itemResult : categories) {
            itemResult.setExtensionPoint(new CategoryMetamacResultExtensionPoint());
            mapCategoriesByItemId.put(itemResult.getItemIdDatabase(), itemResult);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("   SELECT c.ID, eni.ID as ID_ENI_SCHEME");
        sb.append("    FROM TB_CATEGORIES AS c");
        sb.append("    INNER JOIN TB_M_CATEGORIES AS mc ON mc.TB_CATEGORIES = c.ID ");
        sb.append("    LEFT OUTER JOIN TB_LIS_ENI_SCHEMA_CATALOGS AS eni ON mc.ENI_SCHEMA_FK = eni.ID ");
        sb.append("    WHERE ITEM_SCHEME_VERSION_FK = :categorySchemeVersion and mc.ENI_SCHEMA_FK is not null ");

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("categorySchemeVersion", categorySchemeVersionId);
        List eniSchemeResultsSql = query.getResultList();
        for (Object eniSchemeResultSql : eniSchemeResultsSql) {
            Long eniSchemeId = 0L;
            try {
                Object[] eniSchemeResultSqlArray = (Object[]) eniSchemeResultSql;
                Long categoryId = getLong(eniSchemeResultSqlArray[0]);
                ItemResult category = mapCategoriesByItemId.get(categoryId);
                eniSchemeId = getLong(eniSchemeResultSqlArray[1]);

                EniSchemaCatalog eniSchemeCatalog = null;
                eniSchemeCatalog = eniSchemeCatalogMetamacRepository.findById(eniSchemeId);
                category.setExtensionPoint(new CategoryMetamacResultExtensionPoint());
                ((CategoryMetamacResultExtensionPoint) category.getExtensionPoint()).setEniSchemeCatalog(eniSchemeCatalog);
                ((CategoryMetamacResultExtensionPoint) category.getExtensionPoint()).setEniSchemeIdentifier(eniSchemeCatalog.getIdentifier());

            } catch (EniSchemaCatalogNotFoundException e) {
                throw new MetamacException(ServiceExceptionType.ENI_SCHEMA_CATALOG_NOT_FOUND, eniSchemeId);
            }
        }

    }

    @Override
    public List<ItemResult> findCategoriesByCategorySchemeOrderedInDepth(Long itemSchemeVersionId, ItemMetamacResultSelection resultSelection) throws MetamacException {
        // Find categories
        List<ItemResult> categories = categoryRepository.findCategoriesByCategorySchemeUnordered(itemSchemeVersionId, resultSelection);

        // Init index by id
        Map<Long, ItemResult> mapCategoriesByItemId = new HashMap<Long, ItemResult>(categories.size());
        for (ItemResult itemResult : categories) {
            itemResult.setExtensionPoint(new CategoryMetamacResultExtensionPoint());
            mapCategoriesByItemId.put(itemResult.getItemIdDatabase(), itemResult);
        }

        if (resultSelection instanceof CategoryMetamacResultSelection) {
            CategoryMetamacResultSelection categoryResultSelection = (CategoryMetamacResultSelection) resultSelection;
            // Category element. Execute one independent query to retrieve variable elements is more efficient than do a global query
            if (categoryResultSelection.isCategoryElementCode()) {
                executeQueryCategoryElementAndUpdateCategoryMetamacResult(itemSchemeVersionId, mapCategoriesByItemId);
            } else if (categoryResultSelection.isCategoryElementFull()) {
                executeQueryFullCategoryElementAndUpdateCategoryMetamacResult(itemSchemeVersionId, mapCategoriesByItemId);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("WITH RECURSIVE parents(ID, ENI_SCHEME_IDENTIFIER) AS ");
        sb.append("   (SELECT c.ID, eni.IDENTIFIER as ENI_SCHEME_IDENTIFIER");
        sb.append("    FROM TB_CATEGORIES AS c");
        sb.append("    INNER JOIN TB_M_CATEGORIES AS mc ON mc.TB_CATEGORIES = c.ID ");
        sb.append("    LEFT OUTER JOIN TB_LIS_ENI_SCHEMA_CATALOGS AS eni ON mc.ENI_SCHEMA_FK = eni.ID ");
        sb.append("    WHERE PARENT_FK is null and ITEM_SCHEME_VERSION_FK = :categorySchemeVersion ");
        sb.append("        UNION ALL ");
        sb.append("    SELECT c2.ID, eni2.IDENTIFIER as ENI_SCHEME_IDENTIFIER");
        sb.append("    FROM TB_CATEGORIES AS c2");
        sb.append("    INNER JOIN PARENTS AS p ON p.ID = c2.PARENT_FK ");
        sb.append("    INNER JOIN TB_M_CATEGORIES AS mc2 ON mc2.TB_CATEGORIES = c2.ID ");
        sb.append("    LEFT OUTER JOIN TB_LIS_ENI_SCHEMA_CATALOGS AS eni2 ON mc2.ENI_SCHEMA_FK = eni2.ID) ");
        sb.append("SELECT ID, ENI_SCHEME_IDENTIFIER FROM parents ");
        Query queryOrder = getEntityManager().createNativeQuery(sb.toString());
        queryOrder.setParameter("categorySchemeVersion", itemSchemeVersionId);
        @SuppressWarnings("rawtypes")
        List resultsOrder = queryOrder.getResultList();

        // Order previous result list thanks to ordered list of items id
        List<ItemResult> ordered = new ArrayList<ItemResult>(categories.size());
        for (Object resultOrder : resultsOrder) {
            Object[] resultOrderArray = (Object[]) resultOrder;
            Long categoryId = getLong(resultOrderArray[0]);
            ItemResult category = mapCategoriesByItemId.get(categoryId);
            String eniScheme = (String) resultOrderArray[1];
            ((CategoryMetamacResultExtensionPoint) category.getExtensionPoint()).setEniSchemeIdentifier(eniScheme);
            ordered.add(category);
        }
        return ordered;
    }

    @Override
    public List<ItemVisualisationResult> findCategoriesByCategorySchemeUnorderedToVisualisation(Long itemSchemeVersionId, String locale) throws MetamacException {
        return itemRepository.findItemsByItemSchemeUnorderedToVisualisation(itemSchemeVersionId, Category.class, locale);
    }

    @Override
    public void checkCategoryTranslations(Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByUrn) throws MetamacException {
        categoryRepository.checkCategoryTranslations(itemSchemeVersionId, locale, exceptionItemsByUrn);
        // no metadata specific in metamac
    }

    /**
     * Variable element. Execute one independent query to retrieve variable elements is more efficient than do a global query
     */
    @SuppressWarnings("rawtypes")
    private void executeQueryCategoryElementAndUpdateCategoryMetamacResult(Long itemSchemeVersionId, Map<Long, ItemResult> mapCodeByItemId) {
        StringBuilder sbCategoryElements = new StringBuilder();
        sbCategoryElements.append("SELECT c.TB_CATEGORIES as ITEM_ID, ave.CODE as VE_CODE ");
        sbCategoryElements.append("FROM TB_M_CATEGORIES c ");
        sbCategoryElements.append("INNER JOIN TB_CATEGORIES cb on c.TB_CATEGORIES = cb.ID ");
        sbCategoryElements.append("INNER JOIN TB_CAT_RESOURCE_ELEMENTS cve on cve.tb_resource_elements = c.CATEGORY_ELEMENT_FK ");
        sbCategoryElements.append("INNER JOIN TB_RESOURCE_ELEMENTS ve on ve.ID = cve.tb_resource_elements ");
        sbCategoryElements.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS ave on ve.IDENTIFIABLE_ARTEFACT_FK = ave.ID ");
        sbCategoryElements.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");
        Query queryCategoryElements = getEntityManager().createNativeQuery(sbCategoryElements.toString());
        queryCategoryElements.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        List categoryElementsResultSql = queryCategoryElements.getResultList();
        for (Object categoryElementResultSql : categoryElementsResultSql) {
            Object[] categoryElementResultSqlArray = (Object[]) categoryElementResultSql;
            Long actualItemId = getLong(categoryElementResultSqlArray[0]);
            String categoryElementCode = getString(categoryElementResultSqlArray[1]);
            ItemResult target = mapCodeByItemId.get(actualItemId);
            ((CategoryMetamacResultExtensionPoint) target.getExtensionPoint()).setCategoryElementCode(categoryElementCode);
        }
    }

    @SuppressWarnings("rawtypes")
    private void executeQueryFullCategoryElementAndUpdateCategoryMetamacResult(Long itemSchemeVersionId, Map<Long, ItemResult> mapCodeByItemId) {
        StringBuilder sbCategoryElements = new StringBuilder();
        sbCategoryElements.append("SELECT c.TB_CATEGORIES as ITEM_ID, ave.URN as VE_URN, ave.CODE as VE_CODE, ls.LOCALE as SHORT_NAME_LOCALE, ls.LABEL as SHORT_NAME_LABEL ");
        sbCategoryElements.append("FROM TB_M_CATEGORIES c ");
        sbCategoryElements.append("INNER JOIN TB_CATEGORIES cb on c.TB_CATEGORIES = cb.ID ");
        sbCategoryElements.append("INNER JOIN TB_CAT_RESOURCE_ELEMENTS cve on cve.tb_resource_elements = c.CATEGORY_ELEMENT_FK ");
        sbCategoryElements.append("INNER JOIN TB_RESOURCE_ELEMENTS ve on ve.ID = cve.tb_resource_elements ");
        sbCategoryElements.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS ave on ve.IDENTIFIABLE_ARTEFACT_FK = ave.ID ");
        sbCategoryElements.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls ON ls.INTERNATIONAL_STRING_FK = ve.SHORT_NAME_FK ");
        sbCategoryElements.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");

        Query queryCategoryElements = getEntityManager().createNativeQuery(sbCategoryElements.toString());
        queryCategoryElements.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        List categoryElementsResultSql = queryCategoryElements.getResultList();
        for (Object categoryElementResultSql : categoryElementsResultSql) {
            Object[] categoryElementResultSqlArray = (Object[]) categoryElementResultSql;
            int i = 0;
            Long actualItemId = getLong(categoryElementResultSqlArray[i++]);
            ItemResult target = mapCodeByItemId.get(actualItemId);
            CategoryElementResult categoryElementResult = ((CategoryMetamacResultExtensionPoint) target.getExtensionPoint()).getCategoryElement();
            if (categoryElementResult == null) {
                categoryElementResult = new CategoryElementResult();
                ((CategoryMetamacResultExtensionPoint) target.getExtensionPoint()).setCategoryElement(categoryElementResult);
                categoryElementResult.setUrn(getString(categoryElementResultSqlArray[i++]));
                categoryElementResult.setCode(getString(categoryElementResultSqlArray[i++]));
            } else {
                i++; // code
                i++; // urn
            }
            String locale = getString(categoryElementResultSqlArray[i++]);
            if (locale == null) {
                i++;
            } else {
                String label = getString(categoryElementResultSqlArray[i++]);
                categoryElementResult.getShortName().put(locale, label);
            }
        }
    }
}
