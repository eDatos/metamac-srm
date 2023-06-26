package org.siemac.metamac.srm.web.category.view.handlers;

import java.util.List;

import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;

public interface BaseCategoryElementUiHandlers extends BaseUiHandlers {

    void createSegregation(String resourceElementUrn, List<String> resourceElementUrns);
    void createFusion(List<String> resourceElementUrns, String resourceElementUrn);
    void deleteCategoryElementOperations(List<String> codes);
}
