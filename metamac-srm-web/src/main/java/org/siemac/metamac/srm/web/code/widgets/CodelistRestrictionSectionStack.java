package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.web.client.widgets.BaseCodelistVisualisationSectionStack;
import org.siemac.metamac.srm.web.code.model.ds.CodelistVisualisationDS;
import org.siemac.metamac.srm.web.code.model.record.CodelistVisualisationRecord;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.web.common.client.listener.UploadListener;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public class CodelistRestrictionSectionStack extends BaseCodelistVisualisationSectionStack {

    private ImportCodeRestrictionsWindow importCodeRestrictionsWindow;
    
    public CodelistRestrictionSectionStack() {
        super(new CustomListGrid(), getConstants().codelistRestrictions());

        newCodelistVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final EditCodelistRestrictionVisualisationWindow newCodelistRestrictionWindow = new EditCodelistRestrictionVisualisationWindow(getConstants().codelistRestrictionCreate());
                newCodelistRestrictionWindow.setCodelistVisualisation(new CodelistVisualisationDto());
                newCodelistRestrictionWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newCodelistRestrictionWindow.validateForm()) {
                            uiHandlers.saveCodelistRestrictions(newCodelistRestrictionWindow.getCodelistVisualisationDto());
                            newCodelistRestrictionWindow.destroy();
                        }
                    }
                });
            }
        });

        editCodelistVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                CodelistVisualisationDto selectedRestrictions = getSelectedCodelistVisualisation();
                final EditCodelistRestrictionVisualisationWindow editCodelistRestrictionWindow = new EditCodelistRestrictionVisualisationWindow(getConstants().codelistRestrictionEdit());
                editCodelistRestrictionWindow.setCodelistVisualisation(selectedRestrictions);
                editCodelistRestrictionWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (editCodelistRestrictionWindow.validateForm()) {
                            uiHandlers.saveCodelistRestrictions(editCodelistRestrictionWindow.getCodelistVisualisationDto());
                            editCodelistRestrictionWindow.destroy();
                        }
                    }
                });
            }
        });

        deleteCodelistVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.deleteCodelistRestrictions(CommonUtils.getUrnsFromSelectedCodelistVisualisations(listGrid.getSelectedRecords()));
                deleteConfirmationWindow.hide();
            }
        });

        listGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String restrictionUrn = ((CodelistVisualisationRecord) event.getRecord()).getAttribute(CodelistVisualisationDS.URN);
                    uiHandlers.retrieveCodesWithRestrictions(restrictionUrn);
                }
            }
        });
        
        // Import

        importRestrictionCodelistVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                importCodeRestrictionsWindow.show();
            }
        });

        importCodeRestrictionsWindow = new ImportCodeRestrictionsWindow();
        importCodeRestrictionsWindow.setUploadListener(new UploadListener() {

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

        exportRestrictionCodelistVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.exportCodesRestrictions(codelistMetamacDto.getUrn());
            }
        });

        listGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String orderUrn = ((CodelistVisualisationRecord) event.getRecord()).getAttribute(CodelistVisualisationDS.URN);
                    uiHandlers.retrieveCodesWithRestrictions(orderUrn);
                }
            }
        });
        
    }

    @Override
    protected void updateListGridNewButtonVisibility() {
        if (CodesClientSecurityUtils.canCreateCodelistRestrictionVisualisation(codelistMetamacDto)) {
            newCodelistVisualisationButton.show();
        } else {
            newCodelistVisualisationButton.hide();
        }
    }  

    @Override
    protected void updateListGridImportButtonVisibility() {
        importCodelistVisualisationButton.hide();
        importOpennessCodelistVisualisationButton.hide();
        if (CodesClientSecurityUtils.canImportCodelistRestrictionVisualisations(codelistMetamacDto)) {
            importRestrictionCodelistVisualisationButton.show();
        } else {
            importRestrictionCodelistVisualisationButton.hide();
        }
    }

    @Override
    protected void updateListGridExportButtonVisibility() {
        exportCodelistVisualisationButton.hide();
        exportOpennessCodelistVisualisationButton.hide();
        if (CodesClientSecurityUtils.canExportCodesRestriction(codelistMetamacDto)) {
            exportRestrictionCodelistVisualisationButton.show();
        } else {
            exportRestrictionCodelistVisualisationButton.hide();
        }
    }

    @Override
    protected void showListGridEditButton() {
        if (CodesClientSecurityUtils.canUpdateCodelistRestrictionVisualisation(codelistMetamacDto)) {
            editCodelistVisualisationButton.show();
        }
    }

    @Override
    protected void showListGridDeleteButton() {
        if (CodesClientSecurityUtils.canDeleteCodelistRestrictionVisualisation(codelistMetamacDto)) {
            deleteCodelistVisualisationButton.show();
        }
    }
    
    @Override
    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        importCodeRestrictionsWindow.setUiHandlers(uiHandlers);
    }
    
    @Override
    public void setCodelist(CodelistMetamacDto codelistMetamacDto) {
        super.setCodelist(codelistMetamacDto);
        importCodeRestrictionsWindow.setCodelist(codelistMetamacDto);
    }
}
