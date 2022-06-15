package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistVisualisationDto;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * This tree is used to configure the restriction of the codes of a codelist. All the nodes are shown opened. An editable column lets the user to change the state of a node (opened or closed).
 */
public class CodesRestrictionVisualisationTreeGrid extends BaseCodesTreeGrid {

    public CodesRestrictionVisualisationTreeGrid() {
        super(false, true);

        setShowFilterEditor(false);
        setCanReorderRecords(false);
        setCanAcceptDroppedRecords(false);
        setCanDragRecordsOut(false);
        setShowOpenIcons(true);
        setShowDropIcons(false);
        setCustomIconProperty(CodeDS.RESTRICTION_ICON);

        // Add a field with the node restriction state
 
        setFields(getCodeFieldsRequired());

        // Do not show context menu
        super.removeHandlerRegistrations();
    }

    private ListGridField[] getCodeFieldsRequired() {
        
        TreeGridField restrictionField = new TreeGridField(CodeDS.RESTRICTION_CHECK, getConstants().codelistRestriction());
        restrictionField.setType(ListGridFieldType.BOOLEAN);
        restrictionField.setCanEdit(false);
        restrictionField.setCanToggle(false);
        restrictionField.setCanFilter(false);
        restrictionField.setShowHover(false);
        
        ListGridField[] itemFields = getAllFields();      
        ListGridField[] codeFields = new ListGridField[itemFields.length + 1];
        codeFields[0] = restrictionField;             
        System.arraycopy(itemFields, 0, codeFields, 1, itemFields.length);
        return codeFields;
    }
    
    @Override
    public void removeHandlerRegistrations() {
        super.removeHandlerRegistrations();
        folderDropHandlerRegistration.removeHandler();
    }

    public void setItems(ItemSchemeDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes, CodelistVisualisationDto codelistOrderVisualisationDto) {
        this.codelistVisualisationDto = codelistOrderVisualisationDto;
        setItems(codelistMetamacDto, codes);
    }

    public void setEditCheckCodes(boolean isEditable) {
        ListGridField[] fields = this.getAllFields();
        if (fields != null && fields[0] != null) {
            fields[0].setCanEdit(isEditable);
            fields[0].setCanToggle(isEditable);
        }
    }
    
    @Override
    public void addTreeNodesToTreeGrid(TreeNode[] treeNodes) {
        // The root node (codelist) should be always open
        treeNodes[0].setAttribute(CodeDS.RESTRICTION, true);

        tree = new Tree();
        tree.setOpenProperty(CodeDS.RESTRICTION); // Take into account the openness state of the nodes
        tree.setModelType(TreeModelType.PARENT);
        tree.linkNodes(treeNodes);
        setData(tree);
    }

    @Override
    protected void onNodeContextClick(String nodeName, CodeMetamacVisualisationResult code) {
        // Do nothing
    }
}
