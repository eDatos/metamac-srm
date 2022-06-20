package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.client.widgets.BaseCodelistVisualisationSectionStack;
import org.siemac.metamac.srm.web.code.model.ds.CodelistVisualisationDS;
import org.siemac.metamac.srm.web.code.model.record.CodelistVisualisationRecord;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.utils.CommonUtils;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public class CodelistRestrictionSectionStack extends BaseCodelistVisualisationSectionStack {

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
                if (isAllExpandedRestrictionSelected()) {
                    // All expanded openness level can not be deleted
                    // TODO EDATOS-3647 cambiar las dos constantes
                    InformationWindow informationWindow = new InformationWindow(getMessages().codelistOpennessLevelAllExpandedInfoDeleteTitle(), getMessages()
                            .codelistOpennessLevelAllExpandedInfoDeleteMessage());
                    informationWindow.show();
                } else {
                    deleteConfirmationWindow.show();
                }
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
     // TODO EDATOS-3647 ver si restriction cannot be imported
        importCodelistVisualisationButton.hide();
    }

    @Override
    protected void updateListGridExportButtonVisibility() {
        // TODO EDATOS-3647 restriction cannot be exported
        exportCodelistVisualisationButton.hide();
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

    private boolean isAllExpandedRestrictionSelected() {
        List<String> restrictionCodes = CommonUtils.getVisualisationCodesFromSelectedCodelistVisualisations(listGrid.getSelectedRecords());
        for (String code : restrictionCodes) {
            if (SrmConstants.CODELIST_RESTRICTION_VISUALISATION_ALL_EXPANDED_CODE.equals(code)) {
                return true;
            }
        }
        return false;
    }
}
