package org.siemac.metamac.srm.web.category.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.category.view.handlers.CategoryElementsUiHandlers;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;
import org.siemac.metamac.web.common.client.widgets.ImportResourceWindow;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;

import com.smartgwt.client.widgets.form.fields.HiddenItem;

public class ImportCategoryElementsWindow extends ImportResourceWindow {

    private CategoryElementsUiHandlers uiHandlers;

    public ImportCategoryElementsWindow() {
        super(getConstants().actionImportCategoryElements());

        UploadOrdersForm form = new UploadOrdersForm();
        setForm(form);
    }

    private class UploadOrdersForm extends UploadForm {

        public UploadOrdersForm() {
            super(getConstants().categoryElements());

            HiddenItem fileTypeItem = new HiddenItem(SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE);
            fileTypeItem.setDefaultValue(ImportableResourceTypeEnum.CATEGORY_ELEMENTS.name());

            CustomCheckboxItem updateExistingElementsItem = new CustomCheckboxItem(SrmSharedTokens.UPLOAD_PARAM_UPDATE_EXISTING, getConstants().categoryElementsUploadUpdateExisting());

            addFieldsInThePenultimePosition(fileTypeItem, updateExistingElementsItem);
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

    public void setUiHandlers(CategoryElementsUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public CategoryElementsUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}
