package org.siemac.metamac.srm.web.code.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;

import com.smartgwt.client.widgets.layout.VLayout;

public class CodelistRestrictionsPanel extends VLayout {

    private CodelistRestrictionSectionStack codelistRestrictionSectionStack;
    private CodelistRestrictionTreePanel     codelistRestrictionTreePanel;
    
    public CodelistRestrictionsPanel() {
        setMargin(15);
        setMembersMargin(10);

        // LIST OF RESTRICTIONS

        codelistRestrictionSectionStack = new CodelistRestrictionSectionStack();
        addMember(codelistRestrictionSectionStack);

        // VISUALISATION OF ONE RESTRICTION (TREE)

        codelistRestrictionTreePanel = new CodelistRestrictionTreePanel();
        addMember(codelistRestrictionTreePanel);
                       
    }

    public void setRestrictions(List<CodelistVisualisationDto> restrictions) {
        codelistRestrictionSectionStack.setCodelistVisualisations(restrictions);

        hideCodes();
    }

    public void setCodes(CodelistMetamacDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes, CodelistVisualisationDto codelistVisualisationDto) {
        codelistRestrictionSectionStack.selectCodelistVisualisation(codelistVisualisationDto.getUrn());
        codelistRestrictionTreePanel.setCodes(codelistMetamacDto, codes, codelistVisualisationDto);

        showCodes();
    }

    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        codelistRestrictionSectionStack.setUiHandlers(uiHandlers);
        codelistRestrictionTreePanel.setUiHandlers(uiHandlers);
    }

    public CodelistRestrictionSectionStack getCodelistRestrictionSectionStack() {
        return codelistRestrictionSectionStack;
    }

    public void updateItemScheme(CodelistMetamacDto codelistMetamacDto) {
        codelistRestrictionTreePanel.updateItemScheme(codelistMetamacDto);
    }

    public void showCodes() {
        codelistRestrictionTreePanel.show();
    }

    public void hideCodes() {
        codelistRestrictionTreePanel.hide();
    }
}
