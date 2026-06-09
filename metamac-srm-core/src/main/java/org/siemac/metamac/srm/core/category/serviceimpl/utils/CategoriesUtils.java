package org.siemac.metamac.srm.core.category.serviceimpl.utils;

import java.util.ArrayList;
import java.util.Map;

import org.siemac.metamac.srm.core.base.serviceimpl.utils.BaseReplaceFromTemporalMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeOrderVisualisation;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;

import com.arte.statistic.sdmx.srm.core.common.domain.InternationalStringRepository;
import com.arte.statistic.sdmx.srm.core.common.service.utils.GeneratorUrnUtils;

public class CategoriesUtils {

    public static boolean addCategorySchemeOrderVisualisationTemporalToItemScheme(CategorySchemeVersionMetamac itemSchemeTemporalVersion, CategorySchemeVersionMetamac itemSchemeVersion,
            InternationalStringRepository internationalStringRepository) {
        boolean thereAreNewOrders = false;
        Map<String, CategorySchemeOrderVisualisation> currentOrderMap = SrmServiceUtils.createMapOfCategorySchemeOrderVisualisationByOriginalUrn(itemSchemeVersion.getOrderVisualisations());

        for (CategorySchemeOrderVisualisation categorySchemeOrderVisualisationTemp : new ArrayList<CategorySchemeOrderVisualisation>(itemSchemeTemporalVersion.getOrderVisualisations())) {
            String urnFromTemporal = GeneratorUrnUtils.makeUrnFromTemporal(categorySchemeOrderVisualisationTemp.getNameableArtefact().getUrn());
            if (!currentOrderMap.containsKey(urnFromTemporal)) {
                // Add
                itemSchemeVersion.addOrderVisualisation(categorySchemeOrderVisualisationTemp);

                categorySchemeOrderVisualisationTemp.getNameableArtefact().setUrn(urnFromTemporal);
                categorySchemeOrderVisualisationTemp.getNameableArtefact()
                        .setUrnProvider(GeneratorUrnUtils.makeUrnFromTemporal(categorySchemeOrderVisualisationTemp.getNameableArtefact().getUrnProvider()));
                thereAreNewOrders = true;
            } else {
                // Update International Strings is permitted
                BaseReplaceFromTemporalMetamac.replaceInternationalStringFromTemporalToCategorySchemeOrderVisualisation(currentOrderMap.get(urnFromTemporal), categorySchemeOrderVisualisationTemp,
                        internationalStringRepository);
            }
        }
        return thereAreNewOrders;
    }

}
