package org.siemac.metamac.srm.web.category.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;
import org.siemac.metamac.srm.web.category.view.handlers.CategoryElementUiHandlers;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;
import org.siemac.metamac.web.common.client.widgets.ImportResourceWindow;
import org.siemac.metamac.web.common.client.widgets.InformationLabel;

import com.smartgwt.client.widgets.form.fields.HiddenItem;

public class ImportCategoryElementLogoWindow extends ImportResourceWindow {

    private CategoryElementUiHandlers uiHandlers;
    private String                    fileName = null;
    protected InformationLabel        informationLabel;

    public ImportCategoryElementLogoWindow() {
        super(getConstants().actionImportLogo());

        buildInformationLabel();

        UploadOrdersForm form = new UploadOrdersForm();
        setForm(form);
    }

    private void buildInformationLabel() {
        String allowedImageExtensions = CommonUtils.getMetamacAllowedImageExtensions();

        if (StringUtils.isNotEmpty(allowedImageExtensions)) {
            informationLabel = new InformationLabel(getMessages().infoAllowedImageExtensions(getFormattedExtensions(allowedImageExtensions)));
            informationLabel.setWidth(getWidth());
            informationLabel.setMargin(5);
            body.addMember(informationLabel, 0);
        }
    }

    // String with extensions formatted like this: .png, .jpg, .svg
    private String getFormattedExtensions(String allowedImageExtensions) {
        String[] allowedImagesExtensionItems = allowedImageExtensions.split(",");
        String formattedExtension = "";
        for (String imageExtension : allowedImagesExtensionItems) {
            formattedExtension = formattedExtension + ", ." + imageExtension.trim();
        }
        return formattedExtension;
    }

    public void setCategoryElementUrn(CategoryElementDto categoryElementDto) {
        ((HiddenItem) form.getItem(SrmSharedTokens.UPLOAD_PARAM_CATEGORY_ELEMENT_URN)).setDefaultValue(categoryElementDto.getUrn());
    }

    private class UploadOrdersForm extends UploadForm {

        public UploadOrdersForm() {
            super(getConstants().logo());

            HiddenItem fileTypeItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE);
            fileTypeItem.setDefaultValue(ImportableResourceTypeEnum.CATEGORY_ELEMENT_LOGO.name());
            HiddenItem categoryElementUrnItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_CATEGORY_ELEMENT_URN);

            addFieldsInThePenultimePosition(fileTypeItem, categoryElementUrnItem);

            prepareRequiredFileCheck();
        }

        private void prepareRequiredFileCheck() {

            getUploadButton().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                @Override
                public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                    fileName = ImportCategoryElementLogoWindow.this.form.getUploadItem().getDisplayValue();
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

    public void setUiHandlers(CategoryElementUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public CategoryElementUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public String getFileName() {
        return fileName;
    }
}
