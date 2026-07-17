package org.siemac.metamac.srm.web.category.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeVisualisationDto;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.category.model.ds.CategorySchemeVisualisationDS;
import org.siemac.metamac.srm.web.category.model.record.CategorySchemeVisualisationRecord;
import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.category.utils.CategoriesRecordUtils;
import org.siemac.metamac.srm.web.category.view.handlers.CategorySchemeUiHandlers;
import org.siemac.metamac.web.common.client.listener.UploadListener;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.CustomListGridSectionStack;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CategorySchemeOrdersSectionStack extends CustomListGridSectionStack {

    protected ToolStripButton          newCategorySchemeVisualisationButton;
    protected ToolStripButton          editCategorySchemeVisualisationButton;
    protected ToolStripButton          deleteCategorySchemeVisualisationButton;
    protected ToolStripButton          importCategorySchemeVisualisationButton;
    protected ToolStripButton          exportCategorySchemeVisualisationButton;

    protected DeleteConfirmationWindow deleteConfirmationWindow;

    protected CategorySchemeUiHandlers uiHandlers;

    protected CategorySchemeMetamacDto categorySchemeMetamacDto;

    private ImportCategoryOrdersWindow importCategoryOrdersWindow;

    public CategorySchemeOrdersSectionStack() {
        super(new CustomListGrid(), getConstants().categorySchemeOrder());

        // Add fields to the listGrid
        ListGridField codeField = new ListGridField(CategorySchemeVisualisationDS.CODE, getConstants().identifiableArtefactCode());
        codeField.setWidth("30%");
        codeField.setShowHover(true);
        codeField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return ((CategorySchemeVisualisationRecord) record).getCode();
            }
        });

        ListGridField nameField = new ListGridField(CategorySchemeVisualisationDS.NAME, getConstants().nameableArtefactName());
        nameField.setShowHover(true);
        nameField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return ((CategorySchemeVisualisationRecord) record).getName();
            }
        });

        ListGridField urnField = new ListGridField(CategorySchemeVisualisationDS.URN, getConstants().identifiableArtefactUrn());
        urnField.setShowHover(true);
        urnField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return ((CategorySchemeVisualisationRecord) record).getUrn();
            }
        });

        listGrid.setFields(codeField, nameField, urnField);

        // ToolBar
        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newCategorySchemeVisualisationButton = new ToolStripButton(getConstants().actionNew(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.newListGrid().getURL());

        editCategorySchemeVisualisationButton = new ToolStripButton(getConstants().actionEdit(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.editListGrid().getURL());
        editCategorySchemeVisualisationButton.setVisible(false);

        deleteCategorySchemeVisualisationButton = new ToolStripButton(getConstants().actionDelete(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.deleteListGrid().getURL());
        deleteCategorySchemeVisualisationButton.setVisible(false);

        importCategorySchemeVisualisationButton = new ToolStripButton(getConstants().actionImportCategorySchemeOrdersTsv(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.importResource().getURL());

        exportCategorySchemeVisualisationButton = new ToolStripButton(getConstants().categoriesOrderExport(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.exportResource().getURL());

        toolStrip.addButton(newCategorySchemeVisualisationButton);
        toolStrip.addButton(editCategorySchemeVisualisationButton);
        toolStrip.addButton(deleteCategorySchemeVisualisationButton);
        toolStrip.addButton(importCategorySchemeVisualisationButton);
        toolStrip.addButton(exportCategorySchemeVisualisationButton);

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().categorySchemeVisualisationDeleteConfirmationTitle(),
                getConstants().categorySchemeVisualisationDeleteConfirmation());
        deleteConfirmationWindow.setVisible(false);

        listGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (listGrid.getSelectedRecords().length > 0) {
                    showListGridDeleteButton();
                    if (listGrid.getSelectedRecords().length == 1) {
                        showListGridEditButton();
                    } else {
                        editCategorySchemeVisualisationButton.hide();
                    }
                } else {
                    deleteCategorySchemeVisualisationButton.hide();
                    editCategorySchemeVisualisationButton.hide();
                }
            }
        });

        defaultSection.setItems(toolStrip, listGrid);

        // New

        newCategorySchemeVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final EditCategorySchemeOrderVisualisationWindow newCategorySchemeOrderWindow = new EditCategorySchemeOrderVisualisationWindow(getConstants().categorySchemeOrderCreate());
                newCategorySchemeOrderWindow.setConceptSchemeVisualisation(new CategorySchemeVisualisationDto());
                newCategorySchemeOrderWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newCategorySchemeOrderWindow.validateForm()) {
                            uiHandlers.saveCategorySchemeOrder(newCategorySchemeOrderWindow.getConceptSchemeVisualisationDto());
                            newCategorySchemeOrderWindow.destroy();
                        }
                    }
                });
            }
        });

        // Edit

        editCategorySchemeVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                CategorySchemeVisualisationDto selectedOrder = getSelectedCategorySchemeVisualisation();
                final EditCategorySchemeOrderVisualisationWindow editCategorySchemeOrderWindow = new EditCategorySchemeOrderVisualisationWindow(getConstants().categorySchemeOrderEdit());
                editCategorySchemeOrderWindow.setConceptSchemeVisualisation(selectedOrder);
                editCategorySchemeOrderWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (editCategorySchemeOrderWindow.validateForm()) {
                            uiHandlers.saveCategorySchemeOrder(editCategorySchemeOrderWindow.getConceptSchemeVisualisationDto());
                            editCategorySchemeOrderWindow.destroy();
                        }
                    }
                });
            }
        });

        // Delete

        deleteCategorySchemeVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (isAlphabeticalOrderSelected()) {
                    InformationWindow informationWindow = new InformationWindow(getMessages().categorySchemeOrderAlphabeticalInfoDeleteTitle(),
                            getMessages().categorySchemeOrderAlphabeticalInfoDeleteMessage());
                    informationWindow.show();
                } else {
                    deleteConfirmationWindow.show();
                }
            }
        });

        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.deleteCategorySchemeOrders(getUrnsFromSelectedCategorySchemeVisualisations());
                deleteConfirmationWindow.hide();
            }
        });

        // Import

        importCategorySchemeVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                importCategoryOrdersWindow.show();
            }
        });

        importCategoryOrdersWindow = new ImportCategoryOrdersWindow();
        importCategoryOrdersWindow.setUploadListener(new UploadListener() {

            @Override
            public void uploadFailed(String errorMessage) {
                uiHandlers.resourceImportationFailed(errorMessage);
            }

            @Override
            public void uploadComplete(String fileName) {
                uiHandlers.resourceImportationSucceed(fileName);
            }
        });

        // Export

        exportCategorySchemeVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.exportCategoriesOrder(categorySchemeMetamacDto.getUrn());
            }
        });

        listGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkbox will be ignored
                    String orderUrn = ((CategorySchemeVisualisationRecord) event.getRecord()).getAttribute(CategorySchemeVisualisationDS.URN);
                    uiHandlers.retrieveCategoriesWithOrder(orderUrn);
                }
            }
        });
    }

    public void setCategoryScheme(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        this.categorySchemeMetamacDto = categorySchemeMetamacDto;
        importCategoryOrdersWindow.setCategoryScheme(categorySchemeMetamacDto);
        updateButtonsVisibility();
    }

    public void setUiHandlers(CategorySchemeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
        importCategoryOrdersWindow.setUiHandlers(uiHandlers);
    }

    public void setCategorySchemeVisualisations(List<CategorySchemeVisualisationDto> categorySchemeVisualisationDtos) {
        listGrid.setData(CategoriesRecordUtils.getCategorySchemeVisualisationRecords(categorySchemeVisualisationDtos));
    }

    public void selectCategorySchemeVisualisation(String visualisationUrn) {
        RecordList recordList = listGrid.getRecordList();
        Record record = recordList.find(CategorySchemeVisualisationDS.URN, visualisationUrn);
        if (record != null) {
            listGrid.selectRecord(record);
        }
    }

    protected CategorySchemeVisualisationDto getSelectedCategorySchemeVisualisation() {
        CategorySchemeVisualisationDto categorySchemeVisualisationDto = new CategorySchemeVisualisationDto();
        ListGridRecord record = listGrid.getSelectedRecord();
        if (record != null && record instanceof CategorySchemeVisualisationRecord) {
            categorySchemeVisualisationDto = ((CategorySchemeVisualisationRecord) record).getCategorySchemeVisualisationDto();
        }
        return categorySchemeVisualisationDto;
    }

    private void updateButtonsVisibility() {
        if (CategoriesClientSecurityUtils.canCreateCategorySchemeOrderVisualisation(categorySchemeMetamacDto)) {
            newCategorySchemeVisualisationButton.show();
        } else {
            newCategorySchemeVisualisationButton.hide();
        }
        if (CategoriesClientSecurityUtils.canImportCategorySchemeOrderVisualisations(categorySchemeMetamacDto)) {
            importCategorySchemeVisualisationButton.show();
        } else {
            importCategorySchemeVisualisationButton.hide();
        }
        if (CategoriesClientSecurityUtils.canExportCategoriesOrder(categorySchemeMetamacDto)) {
            exportCategorySchemeVisualisationButton.show();
        } else {
            exportCategorySchemeVisualisationButton.hide();
        }
    }

    protected void showListGridEditButton() {
        if (CategoriesClientSecurityUtils.canUpdateCategorySchemeOrderVisualisation(categorySchemeMetamacDto)) {
            editCategorySchemeVisualisationButton.show();
        }
    }

    protected void showListGridDeleteButton() {
        if (CategoriesClientSecurityUtils.canDeleteCategorySchemeOrderVisualisation(categorySchemeMetamacDto)) {
            deleteCategorySchemeVisualisationButton.show();
        }
    }

    private boolean isAlphabeticalOrderSelected() {
        List<String> orderCodes = getVisualisationCodesFromSelectedCategorySchemeVisualisations();
        for (String orderCode : orderCodes) {
            if (SrmConstants.CATEGORY_SCHEME_ORDER_VISUALISATION_ALPHABETICAL_CODE.equals(orderCode)) {
                return true;
            }
        }
        return false;
    }

    private List<String> getUrnsFromSelectedCategorySchemeVisualisations() {
        java.util.List<String> urns = new java.util.ArrayList<String>();
        for (ListGridRecord record : listGrid.getSelectedRecords()) {
            urns.add(((CategorySchemeVisualisationRecord) record).getUrn());
        }
        return urns;
    }

    private List<String> getVisualisationCodesFromSelectedCategorySchemeVisualisations() {
        java.util.List<String> codes = new java.util.ArrayList<String>();
        for (ListGridRecord record : listGrid.getSelectedRecords()) {
            codes.add(((CategorySchemeVisualisationRecord) record).getCode());
        }
        return codes;
    }
}
