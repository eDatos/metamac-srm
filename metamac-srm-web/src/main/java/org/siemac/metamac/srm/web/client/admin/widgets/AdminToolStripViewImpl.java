package org.siemac.metamac.srm.web.client.admin.widgets;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.admin.enums.AdminToolStripButtonEnum;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.HasClickHandlers;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class AdminToolStripViewImpl implements AdminToolStripPresenterWidget.AdminToolStripView {

    private final static String   RADIO_GROUP = "org-radio-group";

    private ToolStrip             toolStrip;

    private CustomToolStripButton annotationTypeButton;

    @Inject
    public AdminToolStripViewImpl() {
        super();
        toolStrip = new ToolStrip();
        toolStrip.setWidth100();
        toolStrip.setHeight(25);
        toolStrip.setAlign(Alignment.LEFT);

        annotationTypeButton = new CustomToolStripButton(MetamacSrmWeb.getConstants().adminMasterTables());
        annotationTypeButton.setID(AdminToolStripButtonEnum.ANNOTATION_TYPES.getValue());
        annotationTypeButton.setActionType(SelectionType.RADIO);
        annotationTypeButton.setRadioGroup(RADIO_GROUP);

        toolStrip.addButton(annotationTypeButton);
    }

    @Override
    public Widget asWidget() {
        return toolStrip;
    }

    @Override
    public void selectButton(AdminToolStripButtonEnum button) {
        Canvas[] canvas = toolStrip.getMembers();
        for (int i = 0; i < canvas.length; i++) {
            if (canvas[i] instanceof ToolStripButton) {
                if (button != null && button.getValue().equals(((ToolStripButton) canvas[i]).getID())) {
                    ((ToolStripButton) canvas[i]).select();
                } else {
                    ((ToolStripButton) canvas[i]).deselect();
                }
            }
        }
    }

    @Override
    public void addToSlot(Object slot, Widget content) {
    }

    @Override
    public void removeFromSlot(Object slot, Widget content) {
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
    }

    @Override
    public HasClickHandlers getAnnotationTypesButton() {
        return annotationTypeButton;
    }
}
