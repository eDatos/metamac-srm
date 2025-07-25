package org.siemac.metamac.srm.core.concept.repositoryimpl;

import static com.arte.statistic.sdmx.srm.core.common.repository.utils.SdmxSrmRepositoryUtils.getString;
import static com.arte.statistic.sdmx.srm.core.common.service.utils.SdmxSrmUtils.addTranslationExceptionToExceptionItemsByResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.util.GeneratorUrnUtils;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeOrderVisualisation;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for ConceptSchemeOrderVisualisation
 */
@Repository("conceptSchemeOrderVisualisationRepository")
public class ConceptSchemeOrderVisualisationRepositoryImpl extends ConceptSchemeOrderVisualisationRepositoryBase {

    public ConceptSchemeOrderVisualisationRepositoryImpl() {
    }

    public ConceptSchemeOrderVisualisation findByUrn(String orderVisualisationUrn) {

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("urn", orderVisualisationUrn);
        List<ConceptSchemeOrderVisualisation> result = findByQuery("from ConceptSchemeOrderVisualisation where nameableArtefact.urn = :urn", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }

    }

    public ConceptSchemeOrderVisualisation findByCode(Long conceptSchemeId, String orderVisualisationCode) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("conceptSchemeId", conceptSchemeId);
        parameters.put("orderVisualisationCode", orderVisualisationCode);
        List<ConceptSchemeOrderVisualisation> result = findByQuery(
                "from ConceptSchemeOrderVisualisation where nameableArtefact.code = :orderVisualisationCode and conceptSchemeVersion.id = :conceptSchemeId", parameters, 1);
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result.get(0);
        }

    }

    public void updateUrnAllConceptSchemeOrderVisualisationsByConceptSchemeEfficiently(ConceptSchemeVersionMetamac conceptSchemeVersionMetamac, String oldUrnExample) {
        // Extract versionable Substring
        String replaceText = GeneratorUrnUtils.extractVersionableArtefactFragment(conceptSchemeVersionMetamac.getMaintainableArtefact().getUrn());
        String oldText = GeneratorUrnUtils.extractVersionableArtefactFragment(oldUrnExample);

        StringBuilder sb = new StringBuilder();
        sb.append("update TB_ANNOTABLE_ARTEFACTS ");
        sb.append("set URN=replace(urn, '").append(oldText).append("', '").append(replaceText).append("') ");
        sb.append("where ANNOTABLE_ARTEFACT_TYPE = 'NAMEABLE_ARTEFACT' ");
        sb.append("and (ID in (select ov.nameable_artefact_fk from tb_m_concept_scheme_order_vis ov where ov.concept_scheme_fk = :conceptSchemeVersion))");

        Query query = getEntityManager().createNativeQuery(sb.toString());
        query.setParameter("conceptSchemeVersion", conceptSchemeVersionMetamac.getId());
        query.executeUpdate();

        if (!conceptSchemeVersionMetamac.getMaintainableArtefact().getIsImported()) {
            sb = new StringBuilder();
            sb.append("update TB_ANNOTABLE_ARTEFACTS ");
            sb.append("set URN_PROVIDER=URN ");
            sb.append("where ANNOTABLE_ARTEFACT_TYPE = 'NAMEABLE_ARTEFACT' ");
            sb.append("and (ID in (select ov.nameable_artefact_fk from tb_m_concept_scheme_order_vis ov where ov.concept_scheme_fk = :conceptSchemeVersion))");

            query = getEntityManager().createNativeQuery(sb.toString());
            query.setParameter("conceptSchemeVersion", conceptSchemeVersionMetamac.getId());
            query.executeUpdate();
        }
    }

    public void checkConceptSchemeOrderVisualisationTranslations(Long itemSchemeVersionId, String locale, Map<String, MetamacExceptionItem> exceptionItemsByUrn) {

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT distinct(a.URN) ");
        sb.append("FROM TB_M_CONCEPT_SCHEME_ORDER_VIS ov ");
        sb.append("INNER JOIN TB_ANNOTABLE_ARTEFACTS a on ov.NAMEABLE_ARTEFACT_FK = a.ID ");
        sb.append("LEFT OUTER JOIN TB_LOCALISED_STRINGS ls on ls.INTERNATIONAL_STRING_FK = a.NAME_FK and ls.LOCALE = :locale ");
        sb.append("WHERE ");
        sb.append("ov.CONCEPT_SCHEME_FK = :itemSchemeVersionId ");
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
