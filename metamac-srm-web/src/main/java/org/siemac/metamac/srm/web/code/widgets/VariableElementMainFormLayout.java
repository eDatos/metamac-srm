package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.web.common.client.widgets.MainFormLayoutButton;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;

import com.smartgwt.client.widgets.events.HasClickHandlers;

public class VariableElementMainFormLayout extends InternationalMainFormLayout {

    private VariableElementDto     variableElementDto;
    protected MainFormLayoutButton segregateButton;
    protected MainFormLayoutButton resendVariableElementStreamMessageButton;

    public VariableElementMainFormLayout() {
        super();
        common();
    }

    public VariableElementMainFormLayout(boolean canEdit, boolean canDelete) {
        super(canEdit, canDelete);
        common();
    }

    public void setVariableElement(VariableElementDto variableElementDto) {
        this.variableElementDto = variableElementDto;
        setCanEdit();
        setCanDelete();
    }

    public void setCanEdit() {
        super.setCanEdit(CodesClientSecurityUtils.canUpdateVariableElement());
    }

    public void setCanDelete() {
        super.setCanDelete(CodesClientSecurityUtils.canDeleteVariableElement(variableElementDto));
    }

    private void common() {
        segregateButton = new MainFormLayoutButton(getConstants().actionSegregate(), GlobalResources.RESOURCE.segregate().getURL());
        resendVariableElementStreamMessageButton = new MainFormLayoutButton(getConstants().actionResendVariableElementKafkaMessage(), GlobalResources.RESOURCE.reload().getURL());
        toolStrip.addButton(segregateButton);
        toolStrip.addButton(resendVariableElementStreamMessageButton);
    }

    @Override
    public void setViewMode() {
        super.setViewMode();
        showSegregateButton();
        showResendVariableElementStreamMessageButton();
    }

    @Override
    public void setEditionMode() {
        super.setEditionMode();
        segregateButton.hide();
    }

    public HasClickHandlers getSegregate() {
        return segregateButton;
    }

    private void showSegregateButton() {
        if (CodesClientSecurityUtils.canSegregateVariableElement()) {
            segregateButton.show();
        }
    }

    private void showResendVariableElementStreamMessageButton() {
        if (CodesClientSecurityUtils.canResendVariableElementStreamMessage()) {
            resendVariableElementStreamMessageButton.show();
        }
    }

    public HasClickHandlers getResendVariableElementStreamMessage() {
        return resendVariableElementStreamMessageButton;
    }
}
