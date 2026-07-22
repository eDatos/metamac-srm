package org.siemac.metamac.srm.web.category.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.srm.core.category.domain.shared.CategoryMetamacVisualisationResult;
import org.siemac.metamac.srm.core.category.domain.shared.CodeCategoryElementNormalisationResult;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.model.ds.CategoryDS;
import org.siemac.metamac.srm.web.category.utils.CategoriesTreeGridUtils;
import org.siemac.metamac.srm.web.category.view.handlers.CategorySchemeUiHandlers;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.client.widgets.SearchRelatedResourcePaginatedWindow;
import org.siemac.metamac.srm.web.code.widgets.BaseCategoriesTreeGrid;
import org.siemac.metamac.srm.web.code.widgets.CategoryTreeNode;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.web.common.client.widgets.CustomLinkTreeGridField;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.actions.SearchPaginatedAction;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

public class CodesCategoryElementsAssignmentTreeGrid extends BaseCategoriesTreeGrid {

    private CategorySchemeUiHandlers             uiHandlers;

    private SearchRelatedResourcePaginatedWindow searchCategoryElementWindow;

    public CodesCategoryElementsAssignmentTreeGrid() {
        super(false, false);

        setShowFilterEditor(false);
        setCanReorderRecords(false);
        setCanAcceptDroppedRecords(false);
        setCanDragRecordsOut(false);
        setShowOpenIcons(true);
        setShowDropIcons(false);
        setCanSort(false);

        // Do not show info field
        infoField.setHidden(true);

        // Add a field with the category element

        CustomLinkTreeGridField categoryElementField = new CustomLinkTreeGridField(CategoryDS.CATEGORY_ELEMENT, getConstants().categoryElement());
        categoryElementField.setCanFilter(false);
        categoryElementField.setAlign(Alignment.RIGHT);
        categoryElementField.setShowHover(false);

        // Field to edit the category element

        TreeGridField editField = new TreeGridField(CategoryDS.CATEGORY_ELEMENT_EDITION, " "); // Do not show title in this column (an space is needed)
        editField.setCanFilter(false);
        editField.setCanEdit(false);
        editField.setShowHover(false);
        editField.setWidth(30);
        editField.setAlign(Alignment.CENTER);
        editField.setType(ListGridFieldType.IMAGE);
        editField.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getRecordNum() != 0) { // Skip the root node (codelist node)
                    Record record = event.getRecord();
                    if (record instanceof CategoryTreeNode) {
                        showSearchCategoryElementWindow((CategoryTreeNode) record);
                    }
                }
            }
        });

        // Field to clear the category element

        TreeGridField clearField = new TreeGridField(CategoryDS.CATEGORY_ELEMENT_CLEAR, " "); // Do not show title in this column (an space is needed)
        clearField.setCanFilter(false);
        clearField.setCanEdit(false);
        clearField.setShowHover(false);
        clearField.setWidth(30);
        clearField.setAlign(Alignment.CENTER);
        clearField.setType(ListGridFieldType.IMAGE);
        clearField.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getRecordNum() != 0) { // Skip the root node (category scheme node)
                    Record record = event.getRecord();
                    if (record instanceof CategoryTreeNode) {
                        CategoriesTreeGridUtils.clearCategoryElementFromRecord((CategoryTreeNode) record);
                        updateData(record);
                        markForRedraw();
                    }
                }
            }
        });

        ListGridField[] itemFields = getAllFields();

        // Set all fields non editable
        for (ListGridField field : itemFields) {
            field.setCanEdit(false);
        }

        ListGridField[] codeFields = new ListGridField[itemFields.length + 3];
        System.arraycopy(itemFields, 0, codeFields, 0, itemFields.length);
        codeFields[codeFields.length - 3] = categoryElementField;
        codeFields[codeFields.length - 2] = editField;
        codeFields[codeFields.length - 1] = clearField;

        setFields(codeFields);

        // Do not show context menu
        super.removeHandlerRegistrations();
        cellClickHandlerRegistration.removeHandler();
        addCellClickHandler(new CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                ListGridField field = getField(event.getColNum());
                if (ListGridFieldType.LINK.equals(field.getType()) && CategoryDS.CATEGORY_ELEMENT.equals(field.getName())) {
                    Record record = event.getRecord();
                    if (record instanceof CategoryTreeNode) {
                        String categoryElementUrn = ((CategoryTreeNode) record).getCategoryElementUrn();
                        uiHandlers.goTo(PlaceRequestUtils.buildAbsoluteCategoryElementByUrnPlaceRequest(categoryElementUrn));
                    }
                }
            }
        });
    }

    @Override
    protected void onNodeContextClick(String nodeName, CategoryMetamacVisualisationResult itemMetamacResult) {
        // Do nothing
    }

    public void setCodesCategoryElementsNormalised(CategorySchemeMetamacDto categorySchemeMetamacDto, List<CodeCategoryElementNormalisationResult> codeCategoryElementNormalisationResults) {
        setCodesAndCategoryElements(categorySchemeMetamacDto, codeCategoryElementNormalisationResults);
    }

    public void setCategoryElementsForManualNormalisation(GetRelatedResourcesResult categoryElementsRelatedResult) {
        if (searchCategoryElementWindow != null) {
            searchCategoryElementWindow.setRelatedResources(categoryElementsRelatedResult.getRelatedResourceDtos());
            searchCategoryElementWindow.refreshSourcePaginationInfo(categoryElementsRelatedResult.getFirstResultOut(), categoryElementsRelatedResult.getRelatedResourceDtos().size(),
                    categoryElementsRelatedResult.getTotalResults());
        }
    }

    public Map<Long, Long> getCodesCategoryElements() {
        Map<Long, Long> codesCategoryeElements = new HashMap<Long, Long>();
        TreeNode[] nodes = getTree().getAllNodes();
        for (TreeNode node : nodes) {
            if (!SCHEME_NODE_NAME.equals(node.getName())) {
                Long codeId = node.getAttributeAsLong(CategoryDS.ID_DATABASE);
                Long categoryElementId = node.getAttributeAsLong(CategoryDS.CATEGORY_ELEMENT_ID_DATABASE);
                codesCategoryeElements.put(codeId, categoryElementId);
            }
        }
        return codesCategoryeElements;
    }

    public void setViewMode() {
        hideField(CategoryDS.CATEGORY_ELEMENT_EDITION);
        hideField(CategoryDS.CATEGORY_ELEMENT_CLEAR);
    }

    public void setEditionMode() {
        showField(CategoryDS.CATEGORY_ELEMENT_EDITION);
        showField(CategoryDS.CATEGORY_ELEMENT_CLEAR);
    }

    public CategorySchemeUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public void setUiHandlers(CategorySchemeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    private void showSearchCategoryElementWindow(final CategoryTreeNode treeNode) {
        final int FIRST_RESULST = 0;
        final int MAX_RESULTS = 8;

        final String categorySchemetUrn = categorySchemeMetamacDto.getUrn();

        searchCategoryElementWindow = new SearchRelatedResourcePaginatedWindow(getConstants().categoryElementSelection(), MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveCategoryElementsForManualNormalisation(firstResult, maxResults, searchCategoryElementWindow.getRelatedResourceCriteria());
            }
        });

        getUiHandlers().retrieveCategoryElementsForManualNormalisation(FIRST_RESULST, MAX_RESULTS, null);

        searchCategoryElementWindow.getListGridItem().getListGrid().setSelectionType(SelectionStyle.SINGLE);
        searchCategoryElementWindow.getListGridItem().setSearchAction(new SearchPaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults, String criteria) {
                getUiHandlers().retrieveCategoryElementsForManualNormalisation(firstResult, maxResults, criteria);
            }
        });
        searchCategoryElementWindow.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                RelatedResourceDto selectedCategoryElement = searchCategoryElementWindow.getSelectedRelatedResource();

                CategoriesTreeGridUtils.setCategoryElementInRecord(treeNode, selectedCategoryElement);

                updateData(treeNode);

                markForRedraw();
                searchCategoryElementWindow.markForDestroy();
            }
        });
    }
}
