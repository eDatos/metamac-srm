package org.siemac.metamac.srm.rest.external.v1_0.utils;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.rest.common.v1_0.domain.InternationalString;
import org.siemac.metamac.rest.common.v1_0.domain.LocalisedString;

public class RestMocks {

    public static InternationalString mockInternationalString(String resourceID) {
        InternationalString internationalString = new InternationalString();
        internationalString.getTexts().add(mockLocalisedString("es", resourceID + " en Español"));
        internationalString.getTexts().add(mockLocalisedString("en", resourceID + " in English"));
        return internationalString;
    }

    public static LocalisedString mockLocalisedString(String lang, String value) {
        LocalisedString localisedString = new LocalisedString();
        localisedString.setLang(lang);
        localisedString.setValue(value);
        return localisedString;
    }

    public static List<String> mockCommonMetadataLanguages() {
        List<String> languages = new ArrayList<String>();
        languages.add("es");
        languages.add("en");
        return languages;
    }
}