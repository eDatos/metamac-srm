package org.siemac.metamac.srm.rest.external.service;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.enume.domain.RelatedResourceSrmTypeEnum;
import org.siemac.metamac.srm.core.util.SrmVisualizerWebUtils;
import org.siemac.metamac.srm.rest.external.utils.SrmWebUtils;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;

public class HtmlLinkUtil {
  public static String getCodelistVisualizerHtmlLink(MaintainableArtefact maintainableArtefact, String code, RelatedResourceSrmTypeEnum resourceTypeEnum) throws MetamacException {
      
      String url = SrmWebUtils.getSrmVisualizerUrl();
      switch (resourceTypeEnum) {
        case CODELIST:
            return SrmVisualizerWebUtils.buildCodelistUrl(maintainableArtefact, url);
        case CONCEPT:
            return SrmVisualizerWebUtils.buildConceptsUrl(maintainableArtefact, code, url);
        case CODE:
            return SrmVisualizerWebUtils.buildCodesUrl(maintainableArtefact, code, url);
        case CONCEPT_SCHEME:
            return SrmVisualizerWebUtils.buildConceptSchemeUrl(maintainableArtefact, url);
        case DATA_STRUCTURE:
            return SrmVisualizerWebUtils.buildDataStructureUrl(maintainableArtefact, url);
        case ORGANISATION_SCHEME:
            return SrmVisualizerWebUtils.buildOrganizationSchemeSchemeUrl(maintainableArtefact, url);
        case ORGANISATION_UNIT_SCHEME:
            return SrmVisualizerWebUtils.buildOrganizationsUnitUrl(maintainableArtefact, code, url);
        case CATEGORY_SCHEME:
            return SrmVisualizerWebUtils.buildCategorySchemeUrl(maintainableArtefact, url);
        case CATEGORY:
            return SrmVisualizerWebUtils.buildCategoriesUrl(maintainableArtefact, code, url);
        case CODELIST_FAMILIES:
            return SrmVisualizerWebUtils.buildCodelistFamiliesUrl(code, url);
        default:
            throw new IllegalArgumentException("Srm-RelatedResourceTypeEnum " + resourceTypeEnum + " not valid.");
    }
  }
}
