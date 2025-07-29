package org.siemac.metamac.srm.core.concept.serviceimpl.utils;

import java.util.ArrayList;
import java.util.Map;

import org.siemac.metamac.srm.core.base.serviceimpl.utils.BaseReplaceFromTemporalMetamac;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeOrderVisualisation;
import org.siemac.metamac.srm.core.concept.domain.ConceptSchemeVersionMetamac;

import com.arte.statistic.sdmx.srm.core.common.domain.InternationalStringRepository;
import com.arte.statistic.sdmx.srm.core.common.service.utils.GeneratorUrnUtils;

public class ConceptsUtils {

    public static boolean addConceptSchemeOrderVisualisationTemporalToItemScheme(ConceptSchemeVersionMetamac itemSchemeTemporalVersion, ConceptSchemeVersionMetamac itemSchemeVersion,
            InternationalStringRepository internationalStringRepository) {
        boolean thereAreNewOrders = false;
        Map<String, ConceptSchemeOrderVisualisation> currentOrderMap = SrmServiceUtils.createMapOfConceptSchemeOrderVisualisationByOriginalUrn(itemSchemeVersion.getOrderVisualisations());

        for (ConceptSchemeOrderVisualisation conceptSchemeOrderVisualisationTemp : new ArrayList<ConceptSchemeOrderVisualisation>(itemSchemeTemporalVersion.getOrderVisualisations())) {
            String urnFromTemporal = GeneratorUrnUtils.makeUrnFromTemporal(conceptSchemeOrderVisualisationTemp.getNameableArtefact().getUrn());
            if (!currentOrderMap.containsKey(urnFromTemporal)) {
                // Add
                itemSchemeVersion.addOrderVisualisation(conceptSchemeOrderVisualisationTemp);

                conceptSchemeOrderVisualisationTemp.getNameableArtefact().setUrn(urnFromTemporal);
                conceptSchemeOrderVisualisationTemp.getNameableArtefact()
                        .setUrnProvider(GeneratorUrnUtils.makeUrnFromTemporal(conceptSchemeOrderVisualisationTemp.getNameableArtefact().getUrnProvider()));
                thereAreNewOrders = true;
            } else {
                // Update International Strings is permitted
                BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToConceptSchemeOrderVisualisation(currentOrderMap.get(urnFromTemporal), conceptSchemeOrderVisualisationTemp,
                        internationalStringRepository);
            }
        }
        return thereAreNewOrders;
    }

}
