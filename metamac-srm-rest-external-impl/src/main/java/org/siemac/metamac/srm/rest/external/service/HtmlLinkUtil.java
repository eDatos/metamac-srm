package org.siemac.metamac.srm.rest.external.service;

import org.siemac.metamac.srm.core.util.SrmVisualizerWebUtils;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;

public class HtmlLinkUtil {
  public static String getCodelistVisualizerHtmlLink(MaintainableArtefact maintainableArtefact, String code, RelatedResourceTypeEnum resourceTypeEnum) {
      switch (resourceTypeEnum) {
        case CODELIST:
            return SrmVisualizerWebUtils.buildCodelistUrl(maintainableArtefact, "https://estadisticas.arte-consultores.com/edatos-srm-visualizer");
        case CONCEPT:
            return SrmVisualizerWebUtils.buildConceptsUrl(maintainableArtefact, code, "https://estadisticas.arte-consultores.com/edatos-srm-visualizer");
        case CODE:
            return SrmVisualizerWebUtils.buildCodesUrl(maintainableArtefact, code, "https://estadisticas.arte-consultores.com/edatos-srm-visualizer");
        case CONCEPT_SCHEME:
            return SrmVisualizerWebUtils.buildConceptSchemeUrl(maintainableArtefact, "https://estadisticas.arte-consultores.com/edatos-srm-visualizer");
        case DATA_STRUCTURE:
            return SrmVisualizerWebUtils.buildDataStructureUrl(maintainableArtefact, "https://estadisticas.arte-consultores.com/edatos-srm-visualizer");
        case ORGANISATION_SCHEME:
            return SrmVisualizerWebUtils.buildOrganizationSchemeSchemeUrl(maintainableArtefact, "https://estadisticas.arte-consultores.com/edatos-srm-visualizer");
        case ORGANISATION_UNIT_SCHEME:
            return SrmVisualizerWebUtils.buildOrganizationsUnitUrl(maintainableArtefact, code, "https://estadisticas.arte-consultores.com/edatos-srm-visualizer");
        default:
            return "";
    }
  }
}
