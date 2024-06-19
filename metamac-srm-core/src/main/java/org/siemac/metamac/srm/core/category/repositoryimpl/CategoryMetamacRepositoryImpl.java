package org.siemac.metamac.srm.core.category.repositoryimpl;

import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getBoolean;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getDate;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getLong;
import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.category.domain.CategoryElementResult;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamacResultSelection;
import org.siemac.metamac.srm.core.category.domain.shared.CategoryElementVisualisationResult;
import org.siemac.metamac.srm.core.category.domain.shared.CategoryMetamacVisualisationResult;
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

        return findCategoriesByCategorySchemeOrderedInDepth(categorySchemeVersionId, resultSelection);

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
            // Category element. Execute one independent query to retrieve category elements is more efficient than do a global query
            if (categoryResultSelection.isCategoryElementCode()) {
                executeQueryCategoryElementAndUpdateCategoryMetamacResult(itemSchemeVersionId, mapCategoriesByItemId);
            } else if (categoryResultSelection.isCategoryElementFull()) {
                executeQueryFullCategoryElementAndUpdateCategoryMetamacResult(itemSchemeVersionId, mapCategoriesByItemId);
            }
            if (categoryResultSelection.isEniSchemaCode()) {
                executeQueryEniSchemaAndUpdateCategoryMetamacResult(itemSchemeVersionId, mapCategoriesByItemId, false);
            } else if (categoryResultSelection.isEniSchemaFull()) {
                executeQueryEniSchemaAndUpdateCategoryMetamacResult(itemSchemeVersionId, mapCategoriesByItemId, true);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("WITH RECURSIVE parents(ID) AS ");
        sb.append("   (SELECT ID ");
        sb.append("    FROM TB_CATEGORIES ");
        sb.append("    WHERE PARENT_FK is null and ITEM_SCHEME_VERSION_FK = :categorySchemeVersion ");
        sb.append("        UNION ALL ");
        sb.append("    SELECT c2.ID ");
        sb.append("    FROM TB_CATEGORIES as c2, parents ");
        sb.append("    WHERE parents.ID = c2.PARENT_FK) ");
        sb.append("SELECT ID FROM parents ");
        Query queryOrder = getEntityManager().createNativeQuery(sb.toString());
        queryOrder.setParameter("categorySchemeVersion", itemSchemeVersionId);
        @SuppressWarnings("rawtypes")
        List resultsOrder = queryOrder.getResultList();

        // Order previous result list thanks to ordered list of items id
        List<ItemResult> ordered = new ArrayList<ItemResult>(categories.size());
        for (Object resultOrder : resultsOrder) {
            Long categoryId = getLong(resultOrder);
            ItemResult category = mapCategoriesByItemId.get(categoryId);
            ordered.add(category);
        }
        return ordered;
    }

    @Override
    public List<CategoryMetamacVisualisationResult> findCategoriesByCategorySchemeUnorderedToVisualisation(Long itemSchemeVersionId, String locale) throws MetamacException {

        // Find. NOTE: this query return null label if locale not exits for a code
        StringBuilder sbCategories = new StringBuilder();
        sbCategories.append("SELECT cb.ID as ITEM_ID, cb.CREATED_DATE, cb.CREATED_DATE_TZ, a.URN, a.CODE, cb.PARENT_FK as ITEM_PARENT_ID, ls.LABEL ");
        sbCategories.append("FROM TB_M_CATEGORIES c ");
        sbCategories.append("INNER JOIN TB_CATEGORIES cb on cb.ID = c.TB_CATEGORIES ");
        sbCategories.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on cb.NAMEABLE_ARTEFACT_FK = a.ID ");
        sbCategories.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = a.NAME_FK and ls.locale = :locale ");
        sbCategories.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :categorySchemeVersion");
        Query queryCategories = getEntityManager().createNativeQuery(sbCategories.toString());
        queryCategories.setParameter("categorySchemeVersion", itemSchemeVersionId);
        queryCategories.setParameter("locale", locale);
        List categoriesResultSql = queryCategories.getResultList();

        // Transform object[] results
        List<CategoryMetamacVisualisationResult> targets = new ArrayList<CategoryMetamacVisualisationResult>(categoriesResultSql.size());
        Map<Long, ItemVisualisationResult> mapCodeByItemId = new HashMap<Long, ItemVisualisationResult>(categoriesResultSql.size());
        for (Object categoryResultSql : categoriesResultSql) {
            Object[] categoryResultSqlArray = (Object[]) categoryResultSql;
            CategoryMetamacVisualisationResult target = categoryResultSqlToCategoryResult(categoryResultSqlArray, null);
            targets.add(target);
            mapCodeByItemId.put(target.getItemIdDatabase(), target);
        }

        // Add description. Execute one independent query to retrieve it is more efficient than do a global query
        itemRepository.executeQueryFillItemDescription(itemSchemeVersionId, Category.class, locale, mapCodeByItemId);

        List categoryElementsResultSql = getCategoryElementByCategoryScheme(itemSchemeVersionId, locale);
        for (Object categoryElementResultSql : categoryElementsResultSql) {
            Object[] categoryElementResultSqlArray = (Object[]) categoryElementResultSql;
            Long actualItemId = getLong(categoryElementResultSqlArray[0]);
            CategoryMetamacVisualisationResult target = (CategoryMetamacVisualisationResult) mapCodeByItemId.get(actualItemId);
            CategoryElementVisualisationResult categoryElement = categoryElementResultSqlToCategoryElementVisualisationResult(categoryElementResultSqlArray);
            target.setCategoryElement(categoryElement);
        }

        // Parent: fill manually with java code
        for (ItemVisualisationResult target : targets) {
            if (target.getParentIdDatabase() != null) {
                ItemVisualisationResult parent = mapCodeByItemId.get(target.getParentIdDatabase());
                target.setParent(parent);
            }
        }
        return targets;
    }

    private List getCategoryElementByCategoryScheme(Long itemSchemeVersionId, String locale) {
        // Category element. Execute one independent query to retrieve category elements is more efficient than do a global query
        StringBuilder sbCategoryElements = new StringBuilder();
        sbCategoryElements.append("SELECT cb.ID as ITEM_ID, ve.ID as VE_ID, ave.URN as VE_URN, ave.CODE as VE_CODE, ls.LABEL as VE_SHORT_NAME_FK  ");
        sbCategoryElements.append("FROM TB_M_CATEGORIES c ");
        sbCategoryElements.append("INNER JOIN TB_CATEGORIES cb on c.TB_CATEGORIES = cb.ID ");
        sbCategoryElements.append("INNER JOIN TB_CAT_RESOURCE_ELEMENTS cve on cve.tb_resource_elements = c.CATEGORY_ELEMENT_FK ");
        sbCategoryElements.append("INNER JOIN TB_RESOURCE_ELEMENTS ve on ve.ID = cve.tb_resource_elements ");
        sbCategoryElements.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS ave on ve.IDENTIFIABLE_ARTEFACT_FK = ave.ID ");
        sbCategoryElements.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = ve.SHORT_NAME_FK and ls.locale = :locale ");
        sbCategoryElements.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");
        Query queryCategoryElements = getEntityManager().createNativeQuery(sbCategoryElements.toString());
        queryCategoryElements.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        queryCategoryElements.setParameter("locale", locale);
        return queryCategoryElements.getResultList();
    }

    private CategoryElementVisualisationResult categoryElementResultSqlToCategoryElementVisualisationResult(Object[] source) {
        CategoryElementVisualisationResult target = new CategoryElementVisualisationResult();
        int i = 0;
        i++; // itemId
        target.setIdDatabase(getLong(source[i++]));
        target.setUrn(getString(source[i++]));
        target.setCode(getString(source[i++]));
        target.setShortName(getString(source[i++]));
        return target;
    }

    private CategoryMetamacVisualisationResult categoryResultSqlToCategoryResult(Object[] source, CategoryMetamacVisualisationResult target) throws MetamacException {
        if (target == null) {
            target = new CategoryMetamacVisualisationResult();
        }
        int i = 0;
        target.setItemIdDatabase(getLong(source[i++]));
        target.setCreatedDate(getDate(source[i++], source[i++]));
        target.setUrn(getString(source[i++]));
        target.setCode(getString(source[i++]));
        target.setParentIdDatabase(getLong(source[i++]));
        target.setName(getString(source[i++]));

        return target;
    }

    @Override
    public void checkCategoryTranslations(Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByUrn) throws MetamacException {
        categoryRepository.checkCategoryTranslations(itemSchemeVersionId, locale, exceptionItemsByUrn);
        // no metadata specific in metamac
    }

    /**
     * Category element. Execute one independent query to retrieve category elements is more efficient than do a global query
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
    private void executeQueryFullCategoryElementAndUpdateCategoryMetamacResult(Long itemSchemeVersionId, Map<Long, ItemResult> mapCodeByItemId) throws MetamacException {
        StringBuilder sbCategoryElements = new StringBuilder();
        sbCategoryElements
                .append("SELECT c.TB_CATEGORIES as ITEM_ID, ave.URN as VE_URN, ave.CODE as VE_CODE, cve.IS_VISIBLE as VE_IS_VISIBLE, ls.LOCALE as SHORT_NAME_LOCALE, ls.LABEL as SHORT_NAME_LABEL ");
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
                categoryElementResult.setIsVisible(getBoolean(categoryElementResultSqlArray[i++]));
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

    @SuppressWarnings("rawtypes")
    private void executeQueryEniSchemaAndUpdateCategoryMetamacResult(Long itemSchemeVersionId, Map<Long, ItemResult> mapCategoriesByItemId, boolean requiredFullEniInformation)
            throws MetamacException {
        StringBuilder sbCategoryElements = new StringBuilder();
        sbCategoryElements.append("SELECT c.TB_CATEGORIES as ITEM_ID, cve.ENI_SCHEMA as eniSchemaId, e.IDENTIFIER as eniSchemaCode ");
        sbCategoryElements.append("FROM TB_M_CATEGORIES c ");
        sbCategoryElements.append("INNER JOIN TB_CATEGORIES cb on c.TB_CATEGORIES = cb.ID ");
        sbCategoryElements.append("INNER JOIN TB_CAT_ENI_SCHEMA cve on cve.tb_m_categories = c.TB_CATEGORIES ");
        sbCategoryElements.append("INNER JOIN TB_LIS_ENI_SCHEMA_CATALOGS e on e.ID = cve.eni_schema ");
        sbCategoryElements.append("WHERE cb.ITEM_SCHEME_VERSION_FK = :itemSchemeVersionId ");

        Query queryCategoryElements = getEntityManager().createNativeQuery(sbCategoryElements.toString());
        queryCategoryElements.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        List categoryElementsResultSql = queryCategoryElements.getResultList();
        for (Object categoryElementResultSql : categoryElementsResultSql) {
            Object[] resultOrderArray = (Object[]) categoryElementResultSql;
            Long categoryId = getLong(resultOrderArray[0]);
            ItemResult category = mapCategoriesByItemId.get(categoryId);
            if (requiredFullEniInformation) {
                getFullEniSchemaInformation(resultOrderArray, category);
            } else {
                getCodeEniSchemaInformation(resultOrderArray, category);
            }

        }
    }

    private void getFullEniSchemaInformation(Object[] eniSchemasByCategoryId, ItemResult category) throws MetamacException {
        Long eniSchemeId = getLong(eniSchemasByCategoryId[1]);
        try {
            if (eniSchemeId != null) {
                EniSchemaCatalog eniSchemeCatalog = eniSchemeCatalogMetamacRepository.findById(eniSchemeId);
                if (eniSchemeCatalog != null) {
                    ((CategoryMetamacResultExtensionPoint) category.getExtensionPoint()).getEniSchemas().add(eniSchemeCatalog);
                }
            }
        } catch (EniSchemaCatalogNotFoundException e) {
            throw new MetamacException(ServiceExceptionType.ENI_SCHEMA_CATALOG_NOT_FOUND, eniSchemeId);
        }
    }

    private void getCodeEniSchemaInformation(Object[] eniSchemasByCategoryId, ItemResult category) throws MetamacException {
        String eniSchemeCode = getString(eniSchemasByCategoryId[2]);
        if (!StringUtils.isEmpty(eniSchemeCode)) {
            ((CategoryMetamacResultExtensionPoint) category.getExtensionPoint()).getEniSchemaCodes().add(eniSchemeCode);
        }

    }

    @Override
    public void updateCodesCategoryElements(Map<Long, Long> categoryElementsByCodes, Long categorySchemeVersionId) throws MetamacException {

        String sqlUpdateWithCategoryElement = buildQueryUpdateCodeCategoryElement(true);
        String sqlUpdateWithoutCategoryElement = buildQueryUpdateCodeCategoryElement(false);

        for (Long categoryId : categoryElementsByCodes.keySet()) {
            Long categoryElementId = categoryElementsByCodes.get(categoryId);

            // Update
            String sql = categoryElementId != null ? sqlUpdateWithCategoryElement : sqlUpdateWithoutCategoryElement;
            Query queryUpdate = getEntityManager().createNativeQuery(sql);
            queryUpdate.setParameter("categorySchemeVersionId", categorySchemeVersionId);
            queryUpdate.setParameter("categoryId", categoryId);
            if (categoryElementId != null) {
                queryUpdate.setParameter("categoryElementId", categoryElementId);
            }
            int updateResult = queryUpdate.executeUpdate();

            if (updateResult > 1) {
                throw new MetamacException(ServiceExceptionType.UNKNOWN,
                        "Update result can not be more than one when updating category element of code: code = " + categoryId + "; categoryElementId = " + categoryElementId);
            } else if (updateResult == 0) {
                throw new MetamacException(ServiceExceptionType.UNKNOWN, "Code or category element incorrect: code = " + categoryId + "; categoryElementId = " + categoryElementId);
            }

        }
    }

    private String buildQueryUpdateCodeCategoryElement(boolean categoryElementNotNull) {
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE TB_M_CATEGORIES SET ");
        if (categoryElementNotNull) {
            sb.append("CATEGORY_ELEMENT_FK = :categoryElementId ");
        } else {
            sb.append("CATEGORY_ELEMENT_FK = null ");
        }
        sb.append("WHERE ");
        sb.append("    TB_CATEGORIES = :categoryId ");
        sb.append("   AND ");
        // Check code belongs to category scheme
        sb.append("    TB_CATEGORIES = ");
        sb.append("   (SELECT cb.ID ");
        sb.append("    FROM TB_CATEGORIES cb ");
        sb.append("    WHERE cb.ITEM_SCHEME_VERSION_FK = :categorySchemeVersionId ");
        sb.append("    AND cb.ID = :categoryId) ");
        // Check id is a category element
        if (categoryElementNotNull) {
            sb.append("   AND ");
            sb.append("   :categoryElementId = ");
            sb.append("   (SELECT ve.TB_RESOURCE_ELEMENTS ");
            sb.append("   FROM TB_CAT_RESOURCE_ELEMENTS ve ");
            sb.append("   WHERE ve.TB_RESOURCE_ELEMENTS = :categoryElementId) ");
        }
        return sb.toString();
    }
}
