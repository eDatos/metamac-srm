package org.siemac.metamac.srm.web.category.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;
import org.siemac.metamac.web.common.client.widgets.ImportResourceWindow;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.WarningLabel;

import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public class ImportAnnotationsWindow extends ImportResourceWindow {

    private static int         formWidth = 600;

    private CodelistUiHandlers uiHandlers;
    protected WarningLabel     warningLabel;
    protected InformationLabel informationTemporalVersionLabel;
    protected InformationLabel informationExternalResourceLabel;
    protected InformationLabel informationLabel;

    public ImportAnnotationsWindow(String title) {
        super(title);

        UploadAnnotationsForm form = new UploadAnnotationsForm();
        setForm(form);

        buildInformationTemporalVersionLabel();
        buildInformationExternalResourceLabel();
        buildInformationlabel();
        buildWarningLabel();
    }

    @Override
    public String getRelativeURL(String url) {
        return MetamacSrmWeb.getRelativeURL(url);
    }

    @Override
    public void showWaitPopup() {
        getUiHandlers().showWaitPopup();
    }

    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public CodelistUiHandlers getUiHandlers() {
        return uiHandlers;
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

    private void buildInformationlabel() {
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

    public void setCodelist(CodelistMetamacDto codelistMetamacDto) {
        ((HiddenItem) form.getItem(SrmSharedTokens.UPLOAD_PARAM_CODELIST_URN)).setDefaultValue(codelistMetamacDto.getUrn());
    }

    private class UploadAnnotationsForm extends UploadForm {

        public UploadAnnotationsForm() {
            super(getConstants().annotations());
            this.setWidth(formWidth);

            HiddenItem fileTypeItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE);
            fileTypeItem.setDefaultValue(ImportableResourceTypeEnum.ANNOTATIONS.name());

            HiddenItem codelistUrnItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_CODELIST_URN);

            addFieldsInThePenultimePosition(fileTypeItem, codelistUrnItem);
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
                    String displayValue = ImportAnnotationsWindow.this.form.getUploadItem().getDisplayValue();
                    if (StringUtils.isBlank(displayValue)) {
                        warningLabel.show();
                    }
                }
            });
        }
    }

    public void initImportWindow(Boolean isLocalResource, Boolean isTemporalVersion) {
        if (Boolean.FALSE.equals(isLocalResource)) {
            informationExternalResourceLabel.show();
            informationTemporalVersionLabel.hide();
            informationLabel.hide();
        } else if (Boolean.TRUE.equals(isTemporalVersion)) {
            informationExternalResourceLabel.hide();
            informationTemporalVersionLabel.show();
            informationLabel.hide();
        } else {
            informationExternalResourceLabel.hide();
            informationTemporalVersionLabel.hide();
            informationLabel.show();
        }
    }
}
