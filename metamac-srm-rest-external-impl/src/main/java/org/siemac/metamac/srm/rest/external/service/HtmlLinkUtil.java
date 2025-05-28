package org.siemac.metamac.srm.rest.external.service;

import org.siemac.metamac.srm.core.util.MetamacPortalWebUtils;

import com.arte.statistic.sdmx.srm.core.base.domain.MaintainableArtefact;
import com.arte.statistic.sdmx.srm.core.base.domain.NameableArtefact;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;

public class HtmlLinkUtil {
  public static String getCodelistVisualizerHtmlLink(MaintainableArtefact maintainableArtefact, NameableArtefact nameableArtefect, RelatedResourceTypeEnum resourceTypeEnum) {
      switch (resourceTypeEnum) {
        case CODELIST:
            return MetamacPortalWebUtils.buildCodelistUrl(maintainableArtefact, "https://estadisticas.arte-consultores.com/edatos-srm-visualizer");
        case CONCEPT:
            return MetamacPortalWebUtils.buildConceptsUrl(maintainableArtefact, nameableArtefect, "https://estadisticas.arte-consultores.com/edatos-srm-visualizer");
        default:
            return "";
    }
  }
}
