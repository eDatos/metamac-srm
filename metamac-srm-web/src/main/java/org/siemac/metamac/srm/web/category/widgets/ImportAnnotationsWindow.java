package org.siemac.metamac.srm.web.category.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.srm.web.category.view.handlers.CategorySchemeUiHandlers;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;
import org.siemac.metamac.web.common.client.widgets.ImportResourceWindow;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;
import org.siemac.metamac.web.common.client.widgets.WarningLabel;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;

import com.smartgwt.client.widgets.form.fields.HiddenItem;

public class ImportAnnotationsWindow extends ImportResourceWindow {

    private static int               formWidth = 600;

    private CategorySchemeUiHandlers uiHandlers;
    protected WarningLabel           warningLabel;
    protected InformationLabel       informationTemporalVersionLabel;
    protected InformationLabel       informationExternalResourceLabel;
    protected InformationLabel       informationLabel;

    public ImportAnnotationsWindow(String title) {
        super(getConstants().actionImportCategoriesTsv());

        UploadAnnotationsForm form = new UploadAnnotationsForm();
        setForm(form);

        buildInformationTemporalVersionLabel();
        buildInformationExternalResourceLabel();
        buildInformationlabel();
        buildWarningLabel();
    }

    @Override
    public String getRelativeURL(String url) {
        return null;
    }

    @Override
    public void showWaitPopup() {

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

    private class UploadAnnotationsForm extends UploadForm {

        public UploadAnnotationsForm() {
            super(getConstants().categories());
            this.setWidth(formWidth);

            HiddenItem fileTypeItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE);
            fileTypeItem.setDefaultValue(ImportableResourceTypeEnum.CATEGORIES.name());

            HiddenItem categorySchemeUrnItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_CATEGORY_SCHEME_URN);

            CustomCheckboxItem updateExistingCategoriesItem = new CustomCheckboxItem(SrmSharedTokens.UPLOAD_PARAM_UPDATE_EXISTING, getConstants().categoriesUploadUpdateExisting());

            addFieldsInThePenultimePosition(fileTypeItem, categorySchemeUrnItem, updateExistingCategoriesItem);

            //prepareRequiredFileCheck();
        }
    }

    public void initImportWindow(Boolean isLocalResource, Boolean isTemporalVersion) {
        CustomCheckboxItem updateExistingCodesItem = (CustomCheckboxItem) this.form.getItem(SrmSharedTokens.UPLOAD_PARAM_UPDATE_EXISTING);

        if (Boolean.FALSE.equals(isLocalResource)) {
            updateExistingCodesItem.setDefaultValue(Boolean.TRUE);
            updateExistingCodesItem.setCanEdit(false);
            updateExistingCodesItem.hide();
            informationExternalResourceLabel.show();
            informationTemporalVersionLabel.hide();
            informationLabel.hide();
        } else if (Boolean.TRUE.equals(isTemporalVersion)) {
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
}
