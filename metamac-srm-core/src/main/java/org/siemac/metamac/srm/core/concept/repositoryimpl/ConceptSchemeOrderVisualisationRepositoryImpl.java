package org.siemac.metamac.srm.core.concept.repositoryimpl;

import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeOrderVisualisation;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;

import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Repository implementation for ConceptSchemeOrderVisualisation
 */
@Repository("conceptSchemeOrderVisualisationRepository")
public class ConceptSchemeOrderVisualisationRepositoryImpl
    extends ConceptSchemeOrderVisualisationRepositoryBase {
    public ConceptSchemeOrderVisualisationRepositoryImpl() {
    }

    public ConceptSchemeOrderVisualisation findByUrn(
        String orderVisualisationUrn) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("findByUrn not implemented");

    }

    public ConceptSchemeOrderVisualisation findByCode(Long conceptSchemeId,
        String orderVisualisationCode) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("findByCode not implemented");

    }

    public void updateUrnAllConceptSchemeOrderVisualisationsByConceptSchemeEfficiently(
        ConceptSchemeVersionMetamac conceptSchemeVersionMetamac,
        String oldUrnExample) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "updateUrnAllConceptSchemeOrderVisualisationsByConceptSchemeEfficiently not implemented");

    }

    public void checkConceptSchemeOrderVisualisationTranslations(
        Long itemSchemeVersionId, String locale,
        Map<String, MetamacExceptionItem> exceptionItemsByUrn) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "checkConceptSchemeOrderVisualisationTranslations not implemented");

    }
}
