package org.siemac.metamac.srm.core.category.repositoryimpl;

import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getString;
import static com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils.addTranslationExceptionToExceptionItemsByResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.edatos.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeOrderVisualisation;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for CategorySchemeOrderVisualisation
 */
@Repository("categorySchemeOrderVisualisationRepository")
public class CategorySchemeOrderVisualisationRepositoryImpl extends CategorySchemeOrderVisualisationRepositoryBase {

    public CategorySchemeOrderVisualisationRepositoryImpl() {
    }

    public CategorySchemeOrderVisualisation findByUrn(String orderVisualisationUrn) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", orderVisualisationUrn);
        List<CategorySchemeOrderVisualisation> result = findByQuery("from CategorySchemeOrderVisualisation where nameableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    public CategorySchemeOrderVisualisation findByCode(Long categorySchemeId, String orderVisualisationCode) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("categorySchemeId", categorySchemeId);
        parameters.put("orderVisualisationCode", orderVisualisationCode);
        List<CategorySchemeOrderVisualisation> result = findByQuery(
                "from CategorySchemeOrderVisualisation where nameableArtefact.code = :orderVisualisationCode and categorySchemeVersion.id = :categorySchemeId", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }
    }

    public void updateUrnAllCategorySchemeOrderVisualisationsByCategorySchemeEfficiently(CategorySchemeVersionMetamac categorySchemeVersionMetamac, String oldUrnExample) {
        // Extract versionable substring
        String replaceText = GeneratorUrnUtils.extractVersionableArtefactFragment(categorySchemeVersionMetamac.getMaintainableArtefact().getUrn());
        String oldText = GeneratorUrnUtils.extractVersionableArtefactFragment(oldUrnExample);

        StringBuilder sb = new StringBuilder();
        sb.append("update TB_ANNOTABLE_ARTEFACTS ");
        sb.append("set URN=replace(urn, '").append(oldText).append("', '").append(replaceText).append("') ");
        sb.append("where ANNOTABLE_ARTEFACT_TYPE = 'NAMEABLE_ARTEFACT' ");
        sb.append("and (ID in (select ov.nameable_artefact_fk from tb_m_cat_scheme_order_vis ov where ov.category_scheme_fk = :categorySchemeVersion))");

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("categorySchemeVersion", categorySchemeVersionMetamac.getId());
        query.executeUpdate();

        if (!categorySchemeVersionMetamac.getMaintainableArtefact().getIsImported()) {
            sb = new StringBuilder();
            sb.append("update TB_ANNOTABLE_ARTEFACTS ");
            sb.append("set URN_PROVIDER=URN ");
            sb.append("where ANNOTABLE_ARTEFACT_TYPE = 'NAMEABLE_ARTEFACT' ");
            sb.append("and (ID in (select ov.nameable_artefact_fk from tb_m_cat_scheme_order_vis ov where ov.category_scheme_fk = :categorySchemeVersion))");

            query = getEntityManager().createNativeQuery(sb.toString());
            query.setParameter("categorySchemeVersion", categorySchemeVersionMetamac.getId());
            query.executeUpdate();
        }
    }

    public void checkCategorySchemeOrderVisualisationTranslations(Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByUrn) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT distinct(a.URN) ");
        sb.append("FROM TB_M_CAT_SCHEME_ORDER_VIS ov ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on ov.NAMEABLE_ARTEFACT_FK = a.ID ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = a.NAME_FK and ls.LOCALE = :locale ");
        sb.append("WHERE ");
        sb.append("ov.CATEGORY_SCHEME_FK = :itemSchemeVersionId ");
        sb.append("AND a.NAME_FK IS NOT NULL AND ls.LABEL IS NULL ");

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("itemSchemeVersionId", itemSchemeVersionId);
        query.setParameter("locale", locale);
        List resultsSql = query.getResultList();
        for (Object resultSql : resultsSql) {
            String urn = getString(resultSql);
            addTranslationExceptionToExceptionItemsByResource(exceptionItemsByUrn, urn, ServiceExceptionParameters.NAMEABLE_ARTEFACT_NAME);
        }
    }
}
