package org.siemac.metamac.srm.web.dsd.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.widgets.RelatedResourceListItem;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourceLinkItem;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.client.widgets.SearchStatisticalOperationLinkItem;
import org.siemac.metamac.srm.web.client.widgets.VersionableResourceSearchSectionStack;
import org.siemac.metamac.srm.web.client.widgets.search.SearchMultiRelatedResourceSimpleItem;
import org.siemac.metamac.srm.web.code.model.ds.VariableDS;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdListUiHandlers;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.srm.web.shared.concept.GetConceptsResult;
import org.siemac.metamac.srm.web.shared.concept.GetStatisticalOperationsResult;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.web.shared.criteria.DataStructureDefinitionWebCriteria;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchExternalItemLinkItem;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;
import org.siemac.metamac.web.common.client.widgets.handlers.ListRecordNavigationClickHandler;
import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.validator.CustomValidator;

public class DsdSearchSectionStack extends VersionableResourceSearchSectionStack {

    private final int FIRST_RESULST = 0;
    private final int MAX_RESULTS = 8;

    private DsdListUiHandlers uiHandlers;

    private SearchRelatedResourcePaginatedWindow searchAttributeConceptWindow;
    private SearchRelatedResourcePaginatedWindow searchCodelistWindow;
    private SearchStatisticalOperationLinkItem statisticalOperationItem;
    private SearchMultiRelatedResourceSimpleItem searchDimensionConceptItem;

    public DsdSearchSectionStack() {
    }

    @Override
    protected void setFormItemsInAdvancedSearchForm(FormItem[] advancedSearchFormItems) {
        statisticalOperationItem = createStatisticalOperationItem(DataStructureDefinitionDS.STATISTICAL_OPERATION, getConstants().dsdOperation());
        searchDimensionConceptItem = createDimensionConceptItem();

        // Attribute concept item
        SearchRelatedResourceLinkItem attributeConcept = createAttributeConceptItem(DataStructureDefinitionDS.ATTRIBUTE_CONCEPT, getConstants().dsdAttributeConcept());

        // Codelist item
        SearchRelatedResourceLinkItem codelistItem = createCodelistItem(DataStructureDefinitionDS.CODELIST_ITEM, getConstants().dsdCodelist());

        // Add items to advanvedSearchForm (before the save button in the advancedSearchFormItems)
        FormItem[] dsdFields = new FormItem[advancedSearchFormItems.length + 4];
        System.arraycopy(advancedSearchFormItems, 0, dsdFields, 0, advancedSearchFormItems.length - 1);
        System.arraycopy(advancedSearchFormItems, advancedSearchFormItems.length - 1, dsdFields, dsdFields.length - 1, 1);
        dsdFields[dsdFields.length - 5] = statisticalOperationItem;
        dsdFields[dsdFields.length - 4] = searchDimensionConceptItem;
        dsdFields[dsdFields.length - 3] = attributeConcept;
        dsdFields[dsdFields.length - 2] = codelistItem;
        advancedSearchForm.setFields(dsdFields);
    }

    public DataStructureDefinitionWebCriteria getDataStructureDefinitionWebCriteria() {
        DataStructureDefinitionWebCriteria dataStructureDefinitionWebCriteria = (DataStructureDefinitionWebCriteria) getVersionableResourceWebCriteria(new DataStructureDefinitionWebCriteria());

        ExternalItemDto statisticalOperation = ((SearchExternalItemLinkItem) advancedSearchForm.getItem(DataStructureDefinitionDS.STATISTICAL_OPERATION)).getExternalItemDto();
        dataStructureDefinitionWebCriteria.setStatisticalOperationUrn(statisticalOperation != null ? statisticalOperation.getUrn() : null);

        List<String> dimensionConceptList = new ArrayList<String>();
        for (RelatedResourceDto relatedResource : searchDimensionConceptItem.getRelatedResourceDtos()) {
            dimensionConceptList.add(relatedResource.getUrn());
        }
        dataStructureDefinitionWebCriteria.setDimensionConceptUrn(dimensionConceptList);

        RelatedResourceDto attributeConcept = ((SearchRelatedResourceLinkItem) advancedSearchForm.getItem(DataStructureDefinitionDS.ATTRIBUTE_CONCEPT)).getRelatedResourceDto();
        dataStructureDefinitionWebCriteria.setAttributeConceptUrn(attributeConcept != null ? attributeConcept.getUrn() : null);

        RelatedResourceDto codelist = ((SearchRelatedResourceLinkItem) advancedSearchForm.getItem(DataStructureDefinitionDS.CODELIST_ITEM)).getRelatedResourceDto();
        dataStructureDefinitionWebCriteria.setCodelistUrn(codelist != null ? codelist.getUrn() : null);

        return dataStructureDefinitionWebCriteria;
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveDsdList(SrmWebConstants.SCHEME_LIST_FIRST_RESULT, SrmWebConstants.SCHEME_LIST_MAX_RESULTS, getDataStructureDefinitionWebCriteria());
    }

    public void setOperations(GetStatisticalOperationsResult result) {
        statisticalOperationItem.setOperations(result.getOperations(), result.getFirstResultOut(), result.getTotalResults());
    }

    public void setDimensionConcepts(GetConceptsResult result) {
        if (searchDimensionConceptItem != null) {
            List<RelatedResourceDto> relatedResourceDtos = RelatedResourceUtils.getConceptMetamacBasicDtosAsRelatedResourceDtos(result.getConcepts());
            stripVersionFromUrns(relatedResourceDtos);
            searchDimensionConceptItem.setResources(relatedResourceDtos, result.getFirstResultOut(), result.getTotalResults());
        }
    }

    public void setAttributeConcepts(GetConceptsResult result) {
        if (searchAttributeConceptWindow != null) {
            List<RelatedResourceDto> relatedResourceDtos = RelatedResourceUtils.getConceptMetamacBasicDtosAsRelatedResourceDtos(result.getConcepts());
            stripVersionFromUrns(relatedResourceDtos);
            searchAttributeConceptWindow.setRelatedResources(relatedResourceDtos);
            searchAttributeConceptWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), relatedResourceDtos.size(), result.getTotalResults());
        }
    }

    public void setCodelist(GetCodelistsResult result) {
        if (searchCodelistWindow != null) {
            List<RelatedResourceDto> relatedResourceDtos = RelatedResourceUtils.getCodelistMetamacBasicDtoAsRelatedResourceDtos(result.getCodelists());
            stripVersionFromUrns(relatedResourceDtos);
            searchCodelistWindow.setRelatedResources(relatedResourceDtos);
            searchCodelistWindow.refreshSourcePaginationInfo(result.getFirstResultOut(), relatedResourceDtos.size(), result.getTotalResults());
        }
    }

    private void stripVersionFromUrns(List<RelatedResourceDto> items) {
        for (RelatedResourceDto item : items) {
            if (item.getUrn() != null) {
                item.setUrn(UrnUtils.removeVersion(item.getUrn()));
            }
        }
    }

    public void setUiHandlers(DsdListUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public DsdListUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    private SearchStatisticalOperationLinkItem createStatisticalOperationItem(String name, String title) {
        SearchStatisticalOperationLinkItem operationItem = new SearchStatisticalOperationLinkItem(name, title) {

            @Override
            protected void retrieveStatisticalOperations(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                getUiHandlers().retrieveStatisticalOperationsForSearchSection(firstResult, maxResults, webCriteria != null ? webCriteria.getCriteria() : null);
            }
        };
        return operationItem;
    }

    private SearchMultiRelatedResourceSimpleItem createDimensionConceptItem() {
        final SearchMultiRelatedResourceSimpleItem conceptoDimension = new SearchMultiRelatedResourceSimpleItem(DataStructureDefinitionDS.DIMENSION_CONCEPT, getConstants().dsdDimensionConcept(),
                MAX_RESULTS, getListRecordNavigationClickHandler(), false) {

            @Override
            protected void retrieveResources(int firstResult, int maxResults, MetamacWebCriteria webCriteria) {
                getUiHandlers().retrieveDimensionConceptsForSearchSection(firstResult, maxResults, webCriteria.getCriteria());
            }
        };

        // Set required with a customValidator
        CustomValidator customValidator = new CustomValidator() {

            @Override
            protected boolean condition(Object value) {
                return ((conceptoDimension.getSelectedRelatedResources() != null) && (!conceptoDimension.getSelectedRelatedResources().isEmpty()));
            }
        };

        conceptoDimension.setValidators(customValidator);
        conceptoDimension.setTitleStyle("staticFormItemTitle");

        return conceptoDimension;
    }

    private SearchRelatedResourceLinkItem createAttributeConceptItem(String name, String title) {
        final SearchRelatedResourceLinkItem attributeConceptItem = new SearchRelatedResourceLinkItem(name, title, getCustomLinkItemNavigationClickHandler());
        attributeConceptItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchAttributeConceptWindow = new SearchRelatedResourcePaginatedWindow(getConstants().conceptSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveAttributeConceptsForSearchSection(firstResult, maxResults, searchAttributeConceptWindow.getRelatedResourceCriteria());
                    }
                });

                // Load concepts (to populate the selection window)
                getUiHandlers().retrieveAttributeConceptsForSearchSection(FIRST_RESULST, MAX_RESULTS, null);

                searchAttributeConceptWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchAttributeConceptWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveAttributeConceptsForSearchSection(firstResult, maxResults, criteria);
                    }
                });
                searchAttributeConceptWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedConcept = searchAttributeConceptWindow.getSelectedRelatedResource();
                        searchAttributeConceptWindow.markForDestroy();
                        // Set selected concept in form
                        ((SearchRelatedResourceLinkItem) advancedSearchForm.getItem(DataStructureDefinitionDS.ATTRIBUTE_CONCEPT)).setRelatedResource(selectedConcept);
                    }
                });
            }
        });
        return attributeConceptItem;
    }

    private SearchRelatedResourceLinkItem createCodelistItem(String name, String title) {
        final SearchRelatedResourceLinkItem codelistItem = new SearchRelatedResourceLinkItem(name, title, getCustomLinkItemNavigationClickHandler());
        codelistItem.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {

                searchCodelistWindow = new SearchRelatedResourcePaginatedWindow(getConstants().codelistSelection(), MAX_RESULTS, new PaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults) {
                        getUiHandlers().retrieveCodelistForSearchSection(firstResult, maxResults, searchCodelistWindow.getRelatedResourceCriteria());
                    }
                });

                // Load codelist (to populate the selection window)
                getUiHandlers().retrieveCodelistForSearchSection(FIRST_RESULST, MAX_RESULTS, null);

                searchCodelistWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
                searchCodelistWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

                    @Override
                    public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                        getUiHandlers().retrieveCodelistForSearchSection(firstResult, maxResults, criteria);
                    }
                });
                searchCodelistWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent arg0) {
                        RelatedResourceDto selectedCodelist = searchCodelistWindow.getSelectedRelatedResource();
                        searchCodelistWindow.markForDestroy();
                        // Set selected concept in form
                        ((SearchRelatedResourceLinkItem) advancedSearchForm.getItem(DataStructureDefinitionDS.CODELIST_ITEM)).setRelatedResource(selectedCodelist);
                    }
                });
            }
        });
        return codelistItem;
    }

    @Override
    protected void clearAdvancedSearchSection() {
        super.clearAdvancedSearchSection();
        ((SearchMultiRelatedResourceSimpleItem) advancedSearchForm.getItem(DataStructureDefinitionDS.DIMENSION_CONCEPT)).clearRelatedResourceList();
        ((SearchRelatedResourceLinkItem) advancedSearchForm.getItem(DataStructureDefinitionDS.ATTRIBUTE_CONCEPT)).clearRelatedResource();
        ((SearchRelatedResourceLinkItem) advancedSearchForm.getItem(DataStructureDefinitionDS.CODELIST_ITEM)).clearRelatedResource();
    }

    // ------------------------------------------------------------------------------------------------------------
    // CLICK HANDLERS
    // ------------------------------------------------------------------------------------------------------------

    private CustomLinkItemNavigationClickHandler getCustomLinkItemNavigationClickHandler() {
        return new CustomLinkItemNavigationClickHandler() {

            @Override
            public BaseUiHandlers getBaseUiHandlers() {
                return uiHandlers;
            }
        };
    }

    private ListRecordNavigationClickHandler getListRecordNavigationClickHandler() {
        return new ListRecordNavigationClickHandler() {

            @Override
            public BaseUiHandlers getBaseUiHandlers() {
                return getUiHandlers();
            }
        };
    }
}
