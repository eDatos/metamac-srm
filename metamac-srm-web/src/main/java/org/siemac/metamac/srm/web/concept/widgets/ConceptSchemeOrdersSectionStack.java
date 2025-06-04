package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.List;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeVisualisationDto;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.client.widgets.BaseConceptSchemeVisualisationSectionStack;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeVisualisationDS;
import org.siemac.metamac.srm.web.concept.model.record.ConceptSchemeVisualisationRecord;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeUiHandlers;
import org.siemac.metamac.web.common.client.listener.UploadListener;
import org.siemac.metamac.web.common.client.widgets.CustomListGrid;
import org.siemac.metamac.web.common.client.widgets.InformationWindow;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;

public class ConceptSchemeOrdersSectionStack extends BaseConceptSchemeVisualisationSectionStack {

    private ImportConceptOrdersWindow importConceptOrdersWindow;

    public ConceptSchemeOrdersSectionStack() {
        super(new CustomListGrid(), getConstants().conceptSchemeOrder());

        // New

        newConceptSchemeVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                final EditConceptSchemeOrderVisualisationWindow newConceptSchemeOrderWindow = new EditConceptSchemeOrderVisualisationWindow(getConstants().conceptSchemeOrderCreate());
                newConceptSchemeOrderWindow.setConceptSchemeVisualisation(new ConceptSchemeVisualisationDto());
                newConceptSchemeOrderWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newConceptSchemeOrderWindow.validateForm()) {
                            uiHandlers.saveConceptSchemeOrder(newConceptSchemeOrderWindow.getConceptSchemeVisualisationDto());
                            newConceptSchemeOrderWindow.destroy();
                        }
                    }
                });
            }
        });

        // Edit

        editConceptSchemeVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ConceptSchemeVisualisationDto selectedOrder = getSelectedConceptSchemeVisualisation();
                final EditConceptSchemeOrderVisualisationWindow editConceptSchemeOrderWindow = new EditConceptSchemeOrderVisualisationWindow(getConstants().conceptSchemeOrderEdit());
                editConceptSchemeOrderWindow.setConceptSchemeVisualisation(selectedOrder);
                editConceptSchemeOrderWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (editConceptSchemeOrderWindow.validateForm()) {
                            uiHandlers.saveConceptSchemeOrder(editConceptSchemeOrderWindow.getConceptSchemeVisualisationDto());
                            editConceptSchemeOrderWindow.destroy();
                        }
                    }
                });
            }
        });

        // Delete

        deleteConceptSchemeVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (isAlphabeticalOrderSelected()) {
                    // Alphabetical order can not be deleted
                    InformationWindow informationWindow = new InformationWindow(getMessages().conceptSchemeOrderAlphabeticalInfoDeleteTitle(),
                            getMessages().conceptSchemeOrderAlphabeticalInfoDeleteMessage());
                    informationWindow.show();
                } else {
                    deleteConfirmationWindow.show();
                }
            }
        });

        deleteConfirmationWindow.getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.deleteConceptSchemeOrders(CommonUtils.getUrnsFromSelectedConceptSchemeVisualisations(listGrid.getSelectedRecords()));
                deleteConfirmationWindow.hide();
            }
        });

        // Import

        importConceptSchemeVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                importConceptOrdersWindow.show();
            }
        });

        importConceptOrdersWindow = new ImportConceptOrdersWindow();
        importConceptOrdersWindow.setUploadListener(new UploadListener() {

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

        exportConceptSchemeVisualisationButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.exportConceptsOrder(ConceptSchemeMetamacDto.getUrn());
            }
        });

        listGrid.addRecordClickHandler(new RecordClickHandler() {

            @Override
            public void onRecordClick(RecordClickEvent event) {
                if (event.getFieldNum() != 0) { // Clicking checkBox will be ignored
                    String orderUrn = ((ConceptSchemeVisualisationRecord) event.getRecord()).getAttribute(ConceptSchemeVisualisationDS.URN);
                    uiHandlers.retrieveConceptsWithOrder(orderUrn);
                }
            }
        });
    }

    @Override
    public void setConceptScheme(ConceptSchemeMetamacDto ConceptSchemeMetamacDto) {
        super.setConceptScheme(ConceptSchemeMetamacDto);
        importConceptOrdersWindow.setConceptScheme(ConceptSchemeMetamacDto);
    }

    @Override
    protected void updateListGridNewButtonVisibility() {
        if (ConceptsClientSecurityUtils.canCreateConceptSchemeOrderVisualisation(ConceptSchemeMetamacDto)) {
            newConceptSchemeVisualisationButton.show();
        } else {
            newConceptSchemeVisualisationButton.hide();
        }
    }

    @Override
    protected void updateListGridImportButtonVisibility() {
        importRestrictionConceptSchemeVisualisationButton.hide();
        importOpennessConceptSchemeVisualisationButton.hide();
        if (ConceptsClientSecurityUtils.canImportConceptSchemeOrderVisualisations(ConceptSchemeMetamacDto)) {
            importConceptSchemeVisualisationButton.show();
        } else {
            importConceptSchemeVisualisationButton.hide();
        }
    }

    @Override
    protected void updateListGridExportButtonVisibility() {
        exportRestrictionConceptSchemeVisualisationButton.hide();
        exportOpennessConceptSchemeVisualisationButton.hide();
        if (ConceptsClientSecurityUtils.canExportConceptsOrder(ConceptSchemeMetamacDto)) {
            exportConceptSchemeVisualisationButton.show();
        } else {
            exportConceptSchemeVisualisationButton.hide();
        }
    }

    @Override
    protected void showListGridEditButton() {
        if (ConceptsClientSecurityUtils.canUpdateConceptSchemeOrderVisualisation(ConceptSchemeMetamacDto)) {
            editConceptSchemeVisualisationButton.show();
        }
    }

    @Override
    protected void showListGridDeleteButton() {
        if (ConceptsClientSecurityUtils.canDeleteConceptSchemeOrderVisualisation(ConceptSchemeMetamacDto)) {
            deleteConceptSchemeVisualisationButton.show();
        }
    }

    @Override
    public void setUiHandlers(ConceptSchemeUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        importConceptOrdersWindow.setUiHandlers(uiHandlers);
    }

    private boolean isAlphabeticalOrderSelected() {
        List<String> orderCodes = CommonUtils.getVisualisationCodesFromSelectedConceptSchemeVisualisations(listGrid.getSelectedRecords());
        for (String orderCode : orderCodes) {
            if (SrmConstants.CONCEPT_SCHEME_ORDER_VISUALISATION_ALPHABETICAL_CODE.equals(orderCode)) {
                return true;
            }
        }
        return false;
    }
}
