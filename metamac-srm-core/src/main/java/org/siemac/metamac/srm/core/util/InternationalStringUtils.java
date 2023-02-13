package org.siemac.metamac.srm.core.util;

import com.arte.statistic.sdmx.srm.core.common.domain.InternationalString;
import com.arte.statistic.sdmx.srm.core.common.domain.LocalisedString;

public class InternationalStringUtils {

   
    public static InternationalString copy(InternationalString source) {
        if (source == null) {
            return null;
        }
        InternationalString target = new InternationalString();

        for (LocalisedString sourceLocalisedString : source.getTexts()) {
            LocalisedString targetLocalisedString = copy(sourceLocalisedString);
            if (targetLocalisedString != null) {
                target.addText(targetLocalisedString);
            }
        }

        return target;
    }

    public static LocalisedString copy(LocalisedString source) {
        if (source == null) {
            return null;
        }
        LocalisedString target = new LocalisedString();
        target.setLabel(source.getLabel());
        target.setLocale(source.getLocale());
        target.setIsUnmodifiable(source.getIsUnmodifiable());

        return target;
    }
    
}
