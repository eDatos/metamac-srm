package org.siemac.metamac.srm.web.code.widgets;

import org.siemac.metamac.web.common.client.widgets.CustomWindow;

import com.smartgwt.client.widgets.layout.VLayout;

public class EditCodelistRestrictionsWindow extends CustomWindow {

    private CodesRestrictionsEditableTreeGrid codesRestrictionsEditableTreeGrid;

    public EditCodelistRestrictionsWindow(String title) {
        super(title);
        setAutoSize(true);

        int width = 650;

        VLayout vLayout = new VLayout();
        vLayout.setWidth(width);
        vLayout.setMargin(15);

        codesRestrictionsEditableTreeGrid = new CodesRestrictionsEditableTreeGrid();
        codesRestrictionsEditableTreeGrid.setWidth(width);
        vLayout.addMember(codesRestrictionsEditableTreeGrid);
        addItem(vLayout);
        show();
    }

    public CodesRestrictionsEditableTreeGrid getCodesRestrictionEditableTreeGrid() {
        return codesRestrictionsEditableTreeGrid;
    }
}
