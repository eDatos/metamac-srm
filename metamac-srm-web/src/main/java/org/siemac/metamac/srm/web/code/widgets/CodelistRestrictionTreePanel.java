package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CodelistRestrictionTreePanel extends VLayout {

    private CodelistMetamacDto                      codelistMetamacDto;
    private List<CodeMetamacVisualisationResult>    codes;
    private CodelistVisualisationDto                codelistVisualisationDto;

    private CodelistUiHandlers                      uiHandlers;

    private TitleLabel                              rstrictionTitle;
    private ToolStripButton                         editButton;
    private ToolStripButton                         saveButton;
    private CodesRestrictionVisualisationTreeGrid codesRestrictionVisualisationTreeGrid;

    public CodelistRestrictionTreePanel() {
        // TITLE

        rstrictionTitle = new TitleLabel();
        rstrictionTitle.setStyleName("subsectionTitleWithNoLeftMargin");
        addMember(rstrictionTitle);

        // TOOL STRIP

        ToolStrip toolStrip = new ToolStrip();
        toolStrip.setWidth100();
       
        toolStrip.addButton(createEditButton());
        toolStrip.addButton(createSaveButton());
        addMember(toolStrip);
        // TREE

        codesRestrictionVisualisationTreeGrid = new CodesRestrictionVisualisationTreeGrid();
        addMember(codesRestrictionVisualisationTreeGrid);
    }

    public void setCodes(CodelistMetamacDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes, CodelistVisualisationDto codelistVisualisationDto) {
        this.codelistMetamacDto = codelistMetamacDto;
        this.codes = codes;
        this.codelistVisualisationDto = codelistVisualisationDto;

        rstrictionTitle.setContents(CommonWebUtils.getElementName(codelistVisualisationDto.getCode(), codelistVisualisationDto.getName()));
        codesRestrictionVisualisationTreeGrid.setItems(codelistMetamacDto, codes, codelistVisualisationDto);
        codesRestrictionVisualisationTreeGrid.setAutoFitMaxRecords(codes.size() + 1);

        // Security
        editButton.setVisible(canCodeRestrictionBeModified(codelistMetamacDto, codelistVisualisationDto));
    }

    
    private ToolStripButton createSaveButton() {
        saveButton = new ToolStripButton(getConstants().actionSave(), RESOURCE.saveListGrid().getURL());
        saveButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                editButton.setVisible(true);
                saveButton.setVisible(false);
            }
        });

        saveButton.setVisible(false);
        return saveButton;
    }

    private ToolStripButton createEditButton() {
        editButton = new ToolStripButton(getConstants().actionEdit(), RESOURCE.editListGrid().getURL());
        editButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                editButton.setVisible(false);
                saveButton.setVisible(true);
            }
        });
        return editButton;
    }
    
    private boolean canCodeRestrictionBeModified(CodelistMetamacDto codelistMetamacDto, CodelistVisualisationDto codelistVisualisationDto) {
        // Restriction can not be modified if is the ALL_EXPANDED one, or if the user is not allowed
        return !StringUtils.equals(SrmConstants.CODELIST_RESTRICTION_VISUALISATION_ALL_EXPANDED_CODE, codelistVisualisationDto.getCode()) && CodesClientSecurityUtils.canUpdateCode(codelistMetamacDto);
    }

    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
        codesRestrictionVisualisationTreeGrid.setUiHandlers(uiHandlers);
    }

    public void updateItemScheme(CodelistMetamacDto codelistMetamacDto) {
        codesRestrictionVisualisationTreeGrid.updateItemScheme(codelistMetamacDto);
    }
}
