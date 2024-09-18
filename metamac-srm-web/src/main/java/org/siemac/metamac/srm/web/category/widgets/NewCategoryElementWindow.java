package org.siemac.metamac.srm.web.category.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.resource.element.model.ds.ResourceElementDS.CODE;
import static org.siemac.metamac.srm.web.resource.element.model.ds.ResourceElementDS.SHORT_NAME;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;
import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;

public class NewCategoryElementWindow extends CustomWindow {

    private static final int    FORM_ITEM_CUSTOM_WIDTH = 300;
    private static final String FIELD_SAVE             = "save-ele";

    private CustomDynamicForm   form;

    public NewCategoryElementWindow(String title) {
        super(title);

        setAutoSize(true);

        RequiredTextItem codeItem = new RequiredTextItem(CODE, getConstants().identifiableArtefactCode());
        codeItem.setValidators(SemanticIdentifiersUtils.getResourceElementIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        RequiredTextItem shortNameItem = new RequiredTextItem(SHORT_NAME, getConstants().categoryElementShortName());
        shortNameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        LengthRangeValidator lengthRangeValidator = new LengthRangeValidator();
        lengthRangeValidator.setMin(0);
        lengthRangeValidator.setMax(SrmConstants.METADATA_SHORT_NAME_MAXIMUM_LENGTH);
        shortNameItem.setValidators(lengthRangeValidator);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, getConstants().categoryElementCreate());

        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setFields(codeItem, shortNameItem, saveItem);

        addItem(form);
        show();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public boolean validateForm() {
        return form.validate(false);
    }

    public CategoryElementDto getNewCategoryElementDto() {
        CategoryElementDto categoryElementDto = new CategoryElementDto();
        categoryElementDto.setCode(form.getValueAsString(CODE));
        categoryElementDto.setShortName(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(SHORT_NAME)));
        categoryElementDto.setIsVisible(true);
        return categoryElementDto;
    }

}
