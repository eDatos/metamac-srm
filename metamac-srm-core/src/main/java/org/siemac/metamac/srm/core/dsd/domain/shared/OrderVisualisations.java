package org.siemac.metamac.srm.core.dsd.domain.shared;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.code.domain.CodelistOrderVisualisation;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeOrderVisualisation;

public class OrderVisualisations {

    private List<CodelistOrderVisualisation>      codelistOrderVisualisation      = new ArrayList<>();
    private List<ConceptSchemeOrderVisualisation> conceptSchemeOrderVisualisation = new ArrayList<>();

    public OrderVisualisations() {
        // without impl
    }

    public List<CodelistOrderVisualisation> getCodelistOrderVisualisation() {
        return codelistOrderVisualisation;
    }

    public void setCodelistOrderVisualisation(List<CodelistOrderVisualisation> codelistOrderVisualisation) {
        this.codelistOrderVisualisation = codelistOrderVisualisation;
    }

    public List<ConceptSchemeOrderVisualisation> getConceptSchemeOrderVisualisation() {
        return conceptSchemeOrderVisualisation;
    }

    public void setConceptSchemeOrderVisualisation(List<ConceptSchemeOrderVisualisation> conceptSchemeOrderVisualisation) {
        this.conceptSchemeOrderVisualisation = conceptSchemeOrderVisualisation;
    }

}
