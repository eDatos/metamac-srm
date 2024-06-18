package org.siemac.metamac.srm.web.category.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;
import org.siemac.metamac.srm.web.category.model.ds.CategoryElementDS;
import org.siemac.metamac.srm.web.category.model.record.CategoryElementRecord;
import org.siemac.metamac.srm.web.category.presenter.CategoryElementsPresenter;
import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.category.view.handlers.CategoryElementsUiHandlers;
import org.siemac.metamac.srm.web.category.widgets.CategoryElementSearchSectionStack;
import org.siemac.metamac.srm.web.category.widgets.ImportCategoryElementsWindow;
import org.siemac.metamac.srm.web.category.widgets.NewCategoryElementWindow;
import org.siemac.metamac.srm.web.shared.category.GetCategoryElementsResult;
import org.siemac.metamac.web.common.client.listener.UploadListener;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CategoryElementsViewImpl extends ViewWithUiHandlers<CategoryElementsUiHandlers> implements CategoryElementsPresenter.CategoryElementsView {

    private final VLayout                           panel;

    private final CategoryElementSearchSectionStack searchSectionStack;

    private final PaginatedCheckListGrid            categoryElementsListGrid;

    private ToolStripButton                         deleteButton;

    private DeleteConfirmationWindow                deleteConfirmationWindow;
    private final CustomToolStripButton             createCategoryElementButton;
    private NewCategoryElementWindow                newCategoryElementWindow;
    private final ImportCategoryElementsWindow      importCategoryElementsWindow;

    @Inject
    public CategoryElementsViewImpl() {
        super();

        // ToolStrip

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        createCategoryElementButton = createCategoryElementButton();

        deleteButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteButton.setVisible(false);
        deleteButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        CustomToolStripButton exportCategoryElementButton = createExportCategoryElementsButton();

        importCategoryElementsWindow = createImportVariableElementsWindow();
        CustomToolStripButton importCategoryElementButton = createImportVariableElementsButton();

        toolStrip.addButton(createCategoryElementButton);
        toolStrip.addButton(deleteButton);
        toolStrip.addButton(importCategoryElementButton);
        toolStrip.addButton(exportCategoryElementButton);

        // Search
        searchSectionStack = new CategoryElementSearchSectionStack();

        // Category elements

        categoryElementsListGrid = new PaginatedCheckListGrid(CategoryElementsPresenter.ELEMENT_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveCategoryElements(firstResult, maxResults, searchSectionStack.getCategoryElementWebCriteria());
            }
        });
        categoryElementsListGrid.getListGrid().setDataSource(new CategoryElementDS());
        categoryElementsListGrid.getListGrid().setUseAllDataSourceFields(false);

        categoryElementsListGrid.getListGrid().addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String categoryElementCode = ((CategoryElementRecord) event.getRecord()).getCode();
                    getUiHandlers().goToCategoryElement(categoryElementCode);
                }
            }
        });

        categoryElementsListGrid.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (categoryElementsListGrid.getListGrid().getSelectedRecords().length > 0) {
                    // Show delete button
                    showListGridDeleteButton(categoryElementsListGrid.getListGrid().getSelectedRecords());

                } else {
                    hideSelectionDependentButtons();
                }
            }
        });

        ListGridField fieldCode = new ListGridField(CategoryElementDS.CODE, getConstants().identifiableArtefactCode());
        fieldCode.setAlign(Alignment.LEFT);
        ListGridField fieldShortName = new ListGridField(CategoryElementDS.SHORT_NAME, getConstants().categoryElementShortName());
        ListGridField urn = new ListGridField(CategoryElementDS.URN, getConstants().identifiableArtefactUrn());
        categoryElementsListGrid.getListGrid().setFields(fieldCode, fieldShortName, urn);

        categoryElementsListGrid.setHeight100();

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().categoryElementDeleteConfirmationTitle(), getConstants().categoryElementDeleteConfirmation());
        deleteConfirmationWindow.setVisible(false);
        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteCategoryElements(getUrnsFromSelectedCategoryElements());
                deleteConfirmationWindow.hide();
            }
        });

        panel = new VLayout();
        panel.addMember(toolStrip);
        panel.addMember(searchSectionStack);
        panel.addMember(categoryElementsListGrid);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setUiHandlers(CategoryElementsUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        searchSectionStack.setUiHandlers(uiHandlers);
        importCategoryElementsWindow.setUiHandlers(uiHandlers);
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == CategoryElementsPresenter.TYPE_SetContextAreaContentCategoriesToolBar) {
            if (content != null) {
                panel.addMember(content, 0);
            }
        } else {
            // To support inheritance in your views it is good practice to call super.setInSlot when you can't handle the call.
            // Who knows, maybe the parent class knows what to do with this slot.
            super.setInSlot(slot, content);
        }
    }

    private CustomToolStripButton createCategoryElementButton() {
        CustomToolStripButton createButton = new CustomToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());
        createButton.setVisible(CategoriesClientSecurityUtils.canCreateCategoryElement());

        createButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showNewCategoryElementWindow();
            }
        });
        return createButton;
    }

    private CustomToolStripButton createExportCategoryElementsButton() {
        CustomToolStripButton exportButton = new CustomToolStripButton(getConstants().actionExportCategoryElements(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.exportResource().getURL());
        exportButton.setVisible(CategoriesClientSecurityUtils.canExportCategoryElementsTsv());
        exportButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                List<String> categoryElementUrnsSelected = getUrnsFromSelectedCategoryElements();
                getUiHandlers().exportCategoryElements(categoryElementUrnsSelected);
            }
        });
        return exportButton;
    }

    private CustomToolStripButton createImportVariableElementsButton() {
        CustomToolStripButton importButton = new CustomToolStripButton(getConstants().actionImportCategoryElements(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.importResource().getURL());
        importButton.setVisible(CategoriesClientSecurityUtils.canImportCategoryElements());

        importButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                importCategoryElementsWindow.show();
            }
        });
        return importButton;
    }

    private ImportCategoryElementsWindow createImportVariableElementsWindow() {
        ImportCategoryElementsWindow importCategoryElementsWindow = new ImportCategoryElementsWindow();
        importCategoryElementsWindow.setUploadListener(new UploadListener() {

            @Override
            public void uploadFailed(String errorMessage) {
                getUiHandlers().resourceImportationFailed(errorMessage);
            }

            @Override
            public void uploadComplete(String fileName) {
                getUiHandlers().resourceImportationSucceed(fileName);
            }

        });

        return importCategoryElementsWindow;
    }

    private void showNewCategoryElementWindow() {
        newCategoryElementWindow = new NewCategoryElementWindow(getConstants().categoryElementCreate());
        newCategoryElementWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                if (newCategoryElementWindow.validateForm()) {
                    CategoryElementDto categoryElementToCreate = newCategoryElementWindow.getNewCategoryElementDto();
                    getUiHandlers().createCategoryElement(categoryElementToCreate);
                    newCategoryElementWindow.destroy();
                }
            }
        });
    }

    private void showListGridDeleteButton(ListGridRecord[] records) {
        boolean allSelectedCategoryElementsCanBeDeleted = true;

        if (!CategoriesClientSecurityUtils.canDeleteCategoryElement()) {
            allSelectedCategoryElementsCanBeDeleted = false;
        }

        if (allSelectedCategoryElementsCanBeDeleted) {
            deleteButton.show();
        } else {
            deleteButton.hide();
        }
    }

    private void hideSelectionDependentButtons() {
        deleteButton.hide();
    }

    private List<String> getUrnsFromSelectedCategoryElements() {
        List<String> urns = new ArrayList<String>();
        for (ListGridRecord record : categoryElementsListGrid.getListGrid().getSelectedRecords()) {
            CategoryElementRecord schemeRecord = (CategoryElementRecord) record;
            urns.add(schemeRecord.getUrn());
        }
        return urns;
    }

    @Override
    public void setCategoryElements(GetCategoryElementsResult result) {
        setCategoryElements(result.getCategoryElements());
        categoryElementsListGrid.refreshPaginationInfo(result.getFirstResultOut(), result.getCategoryElements().size(), result.getTotalResults());
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }

    private void setCategoryElements(List<CategoryElementDto> categoryElementDtos) {
        CategoryElementRecord[] records = new CategoryElementRecord[categoryElementDtos.size()];
        int index = 0;
        for (CategoryElementDto variableElement : categoryElementDtos) {
            records[index++] = org.siemac.metamac.srm.web.category.utils.CategoriesRecordUtils.getCategoryElementRecord(variableElement);
        }
        categoryElementsListGrid.getListGrid().setData(records);
    }
}
