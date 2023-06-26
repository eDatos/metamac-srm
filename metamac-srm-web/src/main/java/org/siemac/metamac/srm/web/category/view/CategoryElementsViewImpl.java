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
import org.siemac.metamac.srm.web.category.widgets.NewCategoryElementWindow;
import org.siemac.metamac.srm.web.shared.code.GetVariableElementsResult;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.PaginatedListGrid;
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

    private final PaginatedListGrid                 categoryElementsListGrid;

    private ToolStripButton                         deleteButton;

    private DeleteConfirmationWindow                deleteConfirmationWindow;
    private final CustomToolStripButton             createCategoryElementButton;
    private NewCategoryElementWindow                newCategoryElementWindow;

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

        toolStrip.addButton(createCategoryElementButton);
        toolStrip.addButton(deleteButton);

        // Search
        searchSectionStack = new CategoryElementSearchSectionStack();

        // Category elements

        categoryElementsListGrid = new PaginatedListGrid(CategoryElementsPresenter.ELEMENT_LIST_MAX_RESULTS, new PaginatedAction() {

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
                String categoryElementCode = ((CategoryElementRecord) event.getRecord()).getCode();
                getUiHandlers().goToCategoryElement(categoryElementCode);
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
        for (ListGridRecord record : records) {
            CategoryElementDto categoryElementDto = ((CategoryElementRecord) record).getCategoryElementDto();
            if (!CategoriesClientSecurityUtils.canDeleteCategoryElement()) {
                allSelectedCategoryElementsCanBeDeleted = false;
                break;
            }
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

    // TODO EDATOS-4150 CAMBIAR CUANDO SE RECUPERE
    @Override
    public void setCategoryElements(GetVariableElementsResult result) {
        // setCategoryElements(result.getCategoryElements());
        // categoryElementsListGrid.refreshPaginationInfo(result.getFirstResultOut(), result.getCategoryElements().size(), result.getTotalResults());
    }

    @Override
    public void clearSearchSection() {
        searchSectionStack.clearSearchSection();
    }
}
