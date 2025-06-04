package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeVisualisationDto;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeVisualisationDS;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeVisualisationRecord;
import org.siemac.metamac.srm.web.concept.utils.ConceptsRecordUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeUiHandlers;
import org.siemac.metamac.web.common.client.widgets.CustomListGridSectionStack;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public abstract class BaseConceptSchemeVisualisationSectionStack extends CustomListGridSectionStack {

    protected ToolStrip                toolStrip;
    protected ToolStripButton          newConceptSchemeVisualisationButton;
    protected ToolStripButton          editConceptSchemeVisualisationButton;
    protected ToolStripButton          deleteConceptSchemeVisualisationButton;
    protected ToolStripButton          importConceptSchemeVisualisationButton;
    protected ToolStripButton          exportConceptSchemeVisualisationButton;
    protected ToolStripButton          importOpennessConceptSchemeVisualisationButton;
    protected ToolStripButton          exportOpennessConceptSchemeVisualisationButton;
    protected ToolStripButton          importRestrictionConceptSchemeVisualisationButton;
    protected ToolStripButton          exportRestrictionConceptSchemeVisualisationButton;

    protected DeleteConfirmationWindow deleteConfirmationWindow;

    protected ConceptSchemeUiHandlers  uiHandlers;

    protected ConceptSchemeMetamacDto  ConceptSchemeMetamacDto;

    public BaseConceptSchemeVisualisationSectionStack(final ListGrid listGrid, String title) {
        super(listGrid, title);

        // Add fields to the listGrid
        ListGridField codeField = new ListGridField(ConceptSchemeVisualisationDS.CODE, getConstants().identifiableArtefactCode());
        codeField.setWidth("30%");
        codeField.setShowHover(true);
        codeField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return ((ConceptSchemeVisualisationRecord) record).getCode();
            }
        });

        ListGridField nameField = new ListGridField(ConceptSchemeVisualisationDS.NAME, getConstants().nameableArtefactName());
        nameField.setShowHover(true);
        nameField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return ((ConceptSchemeVisualisationRecord) record).getName();
            }
        });

        ListGridField urnField = new ListGridField(ConceptSchemeVisualisationDS.URN, getConstants().identifiableArtefactUrn());
        urnField.setShowHover(true);
        urnField.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                return ((ConceptSchemeVisualisationRecord) record).getUrn();
            }
        });

        listGrid.setFields(codeField, nameField, urnField);

        // ToolBar to manage visualisations
        toolStrip = new ToolStrip();
        toolStrip.setWidth100();

        newConceptSchemeVisualisationButton = new ToolStripButton(getConstants().actionNew(), RESOURCE.newListGrid().getURL());

        editConceptSchemeVisualisationButton = new ToolStripButton(getConstants().actionEdit(), RESOURCE.editListGrid().getURL());
        editConceptSchemeVisualisationButton.setVisible(false);

        deleteConceptSchemeVisualisationButton = new ToolStripButton(getConstants().actionDelete(), RESOURCE.deleteListGrid().getURL());
        deleteConceptSchemeVisualisationButton.setVisible(false);

        importConceptSchemeVisualisationButton = new ToolStripButton(getConstants().actionImportCodeOrdersTsv(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.importResource().getURL());

        exportConceptSchemeVisualisationButton = new ToolStripButton(getConstants().codesOrderExport(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.exportResource().getURL());

        importRestrictionConceptSchemeVisualisationButton = new ToolStripButton(getConstants().actionImportCodeRestrictionsTsv(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.importResource().getURL());

        exportRestrictionConceptSchemeVisualisationButton = new ToolStripButton(getConstants().codesRestrictionExport(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.exportResource().getURL());

        importOpennessConceptSchemeVisualisationButton = new ToolStripButton(getConstants().actionImportCodeOpennessLevelsTsv(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.importResource().getURL());

        exportOpennessConceptSchemeVisualisationButton = new ToolStripButton(getConstants().codesOpennessExport(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.exportResource().getURL());

        toolStrip.addButton(newConceptSchemeVisualisationButton);
        toolStrip.addButton(editConceptSchemeVisualisationButton);
        toolStrip.addButton(deleteConceptSchemeVisualisationButton);
        toolStrip.addButton(importConceptSchemeVisualisationButton);
        toolStrip.addButton(exportConceptSchemeVisualisationButton);
        toolStrip.addButton(importRestrictionConceptSchemeVisualisationButton);
        toolStrip.addButton(exportRestrictionConceptSchemeVisualisationButton);
        toolStrip.addButton(importOpennessConceptSchemeVisualisationButton);
        toolStrip.addButton(exportOpennessConceptSchemeVisualisationButton);

        deleteConfirmationWindow = new DeleteConfirmationWindow(getConstants().conceptSchemeVisualisationDeleteConfirmationTitle(), getConstants().conceptSchemeVisualisationDeleteConfirmation());
        deleteConfirmationWindow.setVisible(false);

        listGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (listGrid.getSelectedRecords().length > 0) {
                    // Show edit and delete button
                    showListGridDeleteButton();
                    if (listGrid.getSelectedRecords().length == 1) {
                        showListGridEditButton();
                    } else {
                        editConceptSchemeVisualisationButton.hide();
                    }
                } else {
                    deleteConceptSchemeVisualisationButton.hide();
                    editConceptSchemeVisualisationButton.hide();
                }
            }
        });

        defaultSection.setItems(toolStrip, listGrid);

    }

    protected ConceptSchemeVisualisationDto getSelectedConceptSchemeVisualisation() {
        ConceptSchemeVisualisationDto ConceptSchemeVisualisationDto = new ConceptSchemeVisualisationDto();
        ListGridRecord record = listGrid.getSelectedRecord();
        if (record != null && record instanceof ConceptSchemeVisualisationRecord) {
            ConceptSchemeVisualisationDto = ((ConceptSchemeVisualisationRecord) record).getConceptSchemeVisualisationDto();
        }
        return ConceptSchemeVisualisationDto;
    }

    public void selectConceptSchemeVisualisation(String visualisationUrn) {
        RecordList recordList = listGrid.getRecordList();
        Record record = recordList.find(ConceptSchemeVisualisationDS.URN, visualisationUrn);
        if (record != null) {
            listGrid.selectRecord(record);
        }
    }

    public void setConceptScheme(ConceptSchemeMetamacDto ConceptSchemeMetamacDto) {
        this.ConceptSchemeMetamacDto = ConceptSchemeMetamacDto;
        updateButtonsVisibility();
    }

    public void setUiHandlers(ConceptSchemeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public void setConceptSchemeVisualisations(List<ConceptSchemeVisualisationDto> ConceptSchemeVisualisationDtos) {
        listGrid.setData(ConceptsRecordUtils.getConceptSchemeVisualisationRecords(ConceptSchemeVisualisationDtos));
    }

    private void updateButtonsVisibility() {
        updateListGridNewButtonVisibility();
        updateListGridImportButtonVisibility();
        updateListGridExportButtonVisibility();
    }

    protected abstract void showListGridEditButton();
    protected abstract void showListGridDeleteButton();
    protected abstract void updateListGridNewButtonVisibility();
    protected abstract void updateListGridImportButtonVisibility();
    protected abstract void updateListGridExportButtonVisibility();
}
