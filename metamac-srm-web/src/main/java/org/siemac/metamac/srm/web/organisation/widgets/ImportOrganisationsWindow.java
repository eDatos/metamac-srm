package org.siemac.metamac.srm.web.organisation.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.organisation.dto.OrganisationSchemeMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.organisation.view.handlers.OrganisationSchemeUiHandlers;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;
import org.siemac.metamac.web.common.client.widgets.ImportResourceWindow;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.WarningLabel;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;

import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public class ImportOrganisationsWindow extends ImportResourceWindow {
    private static int formWidth = 600;
    
    private OrganisationSchemeUiHandlers uiHandlers;
    protected WarningLabel               warningLabel;
    protected InformationLabel informationTemporalVersionLabel;
    protected InformationLabel informationExternalResourceLabel;
    protected InformationLabel informationLabel;
    
    public ImportOrganisationsWindow() {
        super(getConstants().actionImportOrganisationsTsv());

        UploadOrganisationsForm form = new UploadOrganisationsForm();
        setForm(form);

        buildInformationTemporalVersionLabel();
        buildInformationExternalResourceLabel();
        buildInformationLabel();
        buildWarningLabel();
    }

    private void buildInformationExternalResourceLabel() {
        informationExternalResourceLabel = new InformationLabel(getConstants().itemImportationNotDeletingElementsInfoMessage() + " " + getConstants().itemImportationExternalResourcesInfoMessage());
        informationExternalResourceLabel.setWidth(this.form.getWidth());
        informationExternalResourceLabel.setMargin(10);
        informationExternalResourceLabel.hide();
        body.addMember(informationExternalResourceLabel, 1);
    }
    
    private void buildInformationTemporalVersionLabel() {
        informationTemporalVersionLabel = new InformationLabel(getConstants().itemImportationNotDeletingElementsInfoMessage() + " " + getConstants().itemImportationTemporalVersionInfoMessage());
        informationTemporalVersionLabel.setWidth(this.form.getWidth());
        informationTemporalVersionLabel.setMargin(10);
        informationTemporalVersionLabel.hide();
        body.addMember(informationTemporalVersionLabel, 1);
    }
    
    private void buildInformationLabel() {
        informationLabel = new InformationLabel(getConstants().itemImportationNotDeletingElementsInfoMessage());
        informationLabel.setWidth(getWidth());
        informationLabel.setMargin(10);
        body.addMember(informationLabel, 1);
    }

    private void buildWarningLabel() {
        warningLabel = new WarningLabel(getMessages().errorFileRequired());
        warningLabel.setWidth(getWidth());
        warningLabel.setMargin(5);
        warningLabel.hide();
        body.addMember(warningLabel, 0);
    }

    public void setOrganisationScheme(OrganisationSchemeMetamacDto organisationSchemeMetamacDto) {
        ((HiddenItem) form.getItem(SrmSharedTokens.UPLOAD_PARAM_ORGANISATION_SCHEME_URN)).setDefaultValue(organisationSchemeMetamacDto.getUrn());
    }

    public void initImportWindow(Boolean isLocalResource, Boolean isTemporalVersionUnitScheme) {
        CustomCheckboxItem updateExistingCodesItem = (CustomCheckboxItem) this.form.getItem(SrmSharedTokens.UPLOAD_PARAM_UPDATE_EXISTING);

        if (Boolean.FALSE.equals(isLocalResource)) {
            updateExistingCodesItem.setDefaultValue(Boolean.TRUE);
            updateExistingCodesItem.setCanEdit(false);
            updateExistingCodesItem.hide();
            informationExternalResourceLabel.show();
            informationTemporalVersionLabel.hide();
            informationLabel.hide();
        } else if (Boolean.TRUE.equals(isTemporalVersionUnitScheme)) {
            updateExistingCodesItem.setDefaultValue(Boolean.TRUE);
            updateExistingCodesItem.setCanEdit(false);
            updateExistingCodesItem.hide();
            informationExternalResourceLabel.hide();
            informationTemporalVersionLabel.show();
            informationLabel.hide();
        } else {
            updateExistingCodesItem.show();
            updateExistingCodesItem.setDefaultValue(Boolean.FALSE);
            updateExistingCodesItem.setCanEdit(true);
            informationExternalResourceLabel.hide();
            informationTemporalVersionLabel.hide();
            informationLabel.show();
        }
    }
    
    private class UploadOrganisationsForm extends UploadForm {

        public UploadOrganisationsForm() {
            super(getConstants().organisations());
            this.setWidth(formWidth);
            
            HiddenItem fileTypeItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE);
            fileTypeItem.setDefaultValue(ImportableResourceTypeEnum.ORGANISATIONS.name());

            HiddenItem organisationSchemeUrnItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_ORGANISATION_SCHEME_URN);

            CustomCheckboxItem updateExistingOrganisationsItem = new CustomCheckboxItem(SrmSharedTokens.UPLOAD_PARAM_UPDATE_EXISTING, getConstants().organisationsUploadUpdateExisting());

            addFieldsInThePenultimePosition(fileTypeItem, organisationSchemeUrnItem, updateExistingOrganisationsItem);

            prepareRequiredFileCheck();
        }

        private void prepareRequiredFileCheck() {
            getUploadItem().addChangeHandler(new ChangeHandler() {

                @Override
                public void onChange(ChangeEvent event) {
                    warningLabel.hide();
                }
            });

            getUploadButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    String displayValue = ImportOrganisationsWindow.this.form.getUploadItem().getDisplayValue();
                    if (StringUtils.isBlank(displayValue)) {
                        warningLabel.show();
                    }
                }
            });
        }
    }

    @Override
    public String getRelativeURL(String url) {
        return MetamacSrmWeb.getRelativeURL(url);
    }

    @Override
    public void showWaitPopup() {
        getUiHandlers().showWaitPopup();
    }

    public void setUiHandlers(OrganisationSchemeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public OrganisationSchemeUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}
