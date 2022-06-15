package org.siemac.metamac.srm.core.code.repositoryimpl;

import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.code.domain.CodelistRestrictionVisualisation;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;

import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Repository implementation for CodelistRestrictionVisualisation
 */
@Repository("codelistRestrictionVisualisationRepository")
public class CodelistRestrictionVisualisationRepositoryImpl
    extends CodelistRestrictionVisualisationRepositoryBase {
    public CodelistRestrictionVisualisationRepositoryImpl() {
    }

    public CodelistRestrictionVisualisation findByCode(Long codelistId,
        String restrictionVisualisationUrn) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("findByCode not implemented");

    }

    public CodelistRestrictionVisualisation findByUrn(
        String restrictionVisualisationUrn) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("findByUrn not implemented");

    }

    public void updateUrnAllCodelistRestrictionVisualisationsByCodelistEfficiently(
        CodelistVersionMetamac codelistVersionMetamac, String oldUrnExample) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "updateUrnAllCodelistRestrictionVisualisationsByCodelistEfficiently not implemented");

    }

    public void checkCodelistRestrictionVisualisationTranslations(
        Long itemSchemeVersionId, String locale,
        Map<String, MetamacExceptionItem> exceptionItemsByUrn) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException(
            "checkCodelistRestrictionVisualisationTranslations not implemented");

    }
}
