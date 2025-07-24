package org.siemac.metamac.srm.core.util;

import com.arte.statistic.sdmx.srm.core.common.domain.InternationalString;
import com.arte.statistic.sdmx.srm.core.common.domain.LocalisedString;

public class InternationalStringUtils {

    private InternationalStringUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Returns a {@link String} from a {@link InternationalString} given a locale
     * 
     * @param internationalString
     * @return
     */
    public static String getLocalisedString(InternationalString internationalString, String locale) {
        if (internationalString != null && locale != null) {
            for (LocalisedString localisedString : internationalString.getTexts()) {
                if (locale.equals(localisedString.getLocale())) {
                    return localisedString.getLabel();
                }
            }
        }
        return null;
    }

    public static InternationalString getAphabeticalInternationalString() {
        InternationalString alphabeticalName = new InternationalString();
        alphabeticalName.addText(new LocalisedString("es", "Orden alfabético"));
        alphabeticalName.addText(new LocalisedString("en", "Alphabetical order"));
        alphabeticalName.addText(new LocalisedString("ca", "Ordre alfabètic"));
        alphabeticalName.addText(new LocalisedString("pt", "Ordem alfabética"));
        return alphabeticalName;
    }
}
