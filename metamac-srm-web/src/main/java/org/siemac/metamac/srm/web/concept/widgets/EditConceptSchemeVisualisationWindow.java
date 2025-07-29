package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeVisualisationDto;
import org.siemac.metamac.srm.web.client.utils.SemanticIdentifiersUtils;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeVisualisationDS;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.TranslateToolStripButton;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

public abstract class EditConceptSchemeVisualisationWindow extends CustomWindow {

    private static final int                FORM_ITEM_CUSTOM_WIDTH = 300;
    private static final String             FIELD_SAVE             = "save-visualisation";

    private CustomDynamicForm               form;

    protected ConceptSchemeVisualisationDto conceptSchemeVisualisationDto;

    public EditConceptSchemeVisualisationWindow(String title) {
        super(title);
        setAutoSize(true);

        // ToolStrip
        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth(getWidth());
        final TranslateToolStripButton translateToolStripButton = new TranslateToolStripButton();
        toolStrip.addButton(translateToolStripButton);
        translateToolStripButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                form.setTranslationsShowed(translateToolStripButton.isSelected());
            }
        });

        // Create form
        RequiredTextItem codeItem = new RequiredTextItem(ConceptSchemeVisualisationDS.CODE, getConstants().identifiableArtefactCode());
        codeItem.setValidators(SemanticIdentifiersUtils.getVisualisationIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        codeItem.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return canCodeBeEdited();
            }
        });

        ViewTextItem codeItemView = new ViewTextItem(ConceptSchemeVisualisationDS.CODE_VIEW, getConstants().identifiableArtefactCode());
        codeItemView.setShowIfCondition(new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return !canCodeBeEdited();
            }
        });

        MultiLanguageTextItem nameItem = new MultiLanguageTextItem(ConceptSchemeVisualisationDS.NAME, getConstants().nameableArtefactName(), String.valueOf(FORM_ITEM_CUSTOM_WIDTH - 28));
        nameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH - 28);
        nameItem.setRequired(true);
        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, MetamacWebCommon.getConstants().actionSave());
        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setFields(codeItem, codeItemView, nameItem, saveItem);

        VLayout layout = new VLayout();
        layout.addMember(toolStrip);
        layout.addMember(form);
        layout.setMargin(10);

        addItem(layout);
        show();
    }

    public void setConceptSchemeVisualisation(ConceptSchemeVisualisationDto conceptSchemeVisualisationDto) {
        this.conceptSchemeVisualisationDto = conceptSchemeVisualisationDto;
        form.setValue(ConceptSchemeVisualisationDS.CODE, conceptSchemeVisualisationDto.getCode());
        form.setValue(ConceptSchemeVisualisationDS.CODE_VIEW, conceptSchemeVisualisationDto.getCode());
        form.setValue(ConceptSchemeVisualisationDS.NAME, conceptSchemeVisualisationDto.getName());
        form.markForRedraw();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public boolean validateForm() {
        return form.validate(false);
    }

    public ConceptSchemeVisualisationDto getConceptSchemeVisualisationDto() {
        conceptSchemeVisualisationDto.setCode(form.getValueAsString(ConceptSchemeVisualisationDS.CODE));
        conceptSchemeVisualisationDto.setName(form.getValueAsInternationalStringDto(ConceptSchemeVisualisationDS.NAME));
        return conceptSchemeVisualisationDto;
    }

    protected abstract boolean canCodeBeEdited();
}
