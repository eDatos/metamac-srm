package org.siemac.metamac.srm.rest.external.utils;

import org.siemac.metamac.core.common.util.WebUtils;

public class SrmWebUtils extends WebUtils {

    private static String srmVisualizerUrl = null;

    public static String getSrmVisualizerUrl() {
        return srmVisualizerUrl;
    }

    public static void setSrmVisualizerUrl(String srmVisualizerUrl) {
        SrmWebUtils.srmVisualizerUrl = srmVisualizerUrl;
    }

}
