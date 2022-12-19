package org.siemac.metamac.srm.web.category.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.view.handlers.CategorySchemeUiHandlers;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
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

public class ImportCategoriesWindow extends ImportResourceWindow {
    private static int formWidth = 600;
    
    private CategorySchemeUiHandlers uiHandlers;
    protected WarningLabel           warningLabel;
    protected InformationLabel informationTemporalVersionLabel;
    protected InformationLabel informationLabel;

    public ImportCategoriesWindow() {
        super(getConstants().actionImportCategoriesTsv());

        UploadCategoriesForm form = new UploadCategoriesForm();
        setForm(form);

        buildInformationTemporalVersionLabel();
        buildInformationlabel();
        buildWarningLabel();
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

    public void setCategorycheme(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        ((HiddenItem) form.getItem(SrmSharedTokens.UPLOAD_PARAM_CATEGORY_SCHEME_URN)).setDefaultValue(categorySchemeMetamacDto.getUrn());
    }

    public void initImportWindow(Boolean isTemporalVersion) {
        CustomCheckboxItem updateExistingCodesItem = (CustomCheckboxItem) this.form.getItem(SrmSharedTokens.UPLOAD_PARAM_UPDATE_EXISTING);

        if (Boolean.TRUE.equals(isTemporalVersion)) {
            updateExistingCodesItem.setDefaultValue(Boolean.TRUE);
            updateExistingCodesItem.setCanEdit(false);
            updateExistingCodesItem.hide();
            informationTemporalVersionLabel.show();
            informationLabel.hide();
        } else {
            updateExistingCodesItem.show();
            updateExistingCodesItem.setDefaultValue(Boolean.FALSE);
            updateExistingCodesItem.setCanEdit(true);
            informationTemporalVersionLabel.hide();
            informationLabel.show();
        }
    }
    
    private class UploadCategoriesForm extends UploadForm {

        public UploadCategoriesForm() {
            super(getConstants().categories());
            this.setWidth(formWidth);
            
            HiddenItem fileTypeItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE);
            fileTypeItem.setDefaultValue(ImportableResourceTypeEnum.CATEGORIES.name());

            HiddenItem categorySchemeUrnItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_CATEGORY_SCHEME_URN);

            CustomCheckboxItem updateExistingCategoriesItem = new CustomCheckboxItem(SrmSharedTokens.UPLOAD_PARAM_UPDATE_EXISTING, getConstants().categoriesUploadUpdateExisting());

            addFieldsInThePenultimePosition(fileTypeItem, categorySchemeUrnItem, updateExistingCategoriesItem);

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
                    String displayValue = ImportCategoriesWindow.this.form.getUploadItem().getDisplayValue();
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

    public void setUiHandlers(CategorySchemeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public CategorySchemeUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}
