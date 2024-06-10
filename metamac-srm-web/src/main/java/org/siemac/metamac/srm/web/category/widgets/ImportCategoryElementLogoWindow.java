package org.siemac.metamac.srm.web.category.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.category.view.handlers.CategoryElementUiHandlers;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;
import org.siemac.metamac.web.common.client.widgets.ImportResourceWindow;

import com.smartgwt.client.widgets.form.fields.HiddenItem;

public class ImportCategoryElementLogoWindow extends ImportResourceWindow {

    private CategoryElementUiHandlers uiHandlers;
    private String                    fileName = null;

    public ImportCategoryElementLogoWindow() {
        super(getConstants().actionImportLogo());

        UploadOrdersForm form = new UploadOrdersForm();
        setForm(form);
    }

    private class UploadOrdersForm extends UploadForm {

        public UploadOrdersForm() {
            super(getConstants().logo());

            HiddenItem fileTypeItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE);
            fileTypeItem.setDefaultValue(ImportableResourceTypeEnum.CATEGORY_ELEMENT_LOGO.name());

            addFieldsInThePenultimePosition(fileTypeItem);

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
