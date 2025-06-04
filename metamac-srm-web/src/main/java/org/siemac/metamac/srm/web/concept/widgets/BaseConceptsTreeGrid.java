package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeVisualisationDto;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.client.widgets.BaseItemsTreeGrid;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.utils.ConceptsClientSecurityUtils;
import org.siemac.metamac.srm.web.concept.utils.ConceptsTreeGridUtils;
import org.siemac.metamac.srm.web.concept.view.handlers.BaseConceptUiHandlers;
import org.siemac.metamac.web.common.client.utils.ListGridUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderContextClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderContextClickHandler;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;
import com.smartgwt.client.widgets.tree.events.LeafContextClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafContextClickHandler;

public abstract class BaseConceptsTreeGrid extends BaseItemsTreeGrid {

    protected ConceptSchemeMetamacDto       conceptSchemeMetamacDto;

    protected HandlerRegistration           folderDropHandlerRegistration;

    protected ConceptSchemeVisualisationDto conceptSchemeVisualisationDto;

    /**
     * Create a {@link BaseConceptsTreeGrid}. This tree will be extended by {@link ConceptsTreeGrid} and {@link ConceptSchemeOrderTreeGrid}.
     * 
     * @param canStructureBeModified specifies if the structure of this tree can be modified (mark as <code>true</code> by {@link ConceptsTreeGrid})
     * @param canOrderBeModified specified if the order of this tree can be modified (mark as <code>true</code> by {@link ConceptSchemeOrderTreeGrid})
     */
    public BaseConceptsTreeGrid(final boolean canStructureBeModified, final boolean canOrderBeModified) {

        // Add the orderField to the previous fields

        ListGridField[] itemFields = getAllFields();

        TreeGridField orderField = new TreeGridField(ConceptDS.ORDER, getConstants().conceptOrder());
        orderField.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());
        orderField.setCanSort(true);
        orderField.setShowHover(false);

        ListGridField[] codeFields = new ListGridField[itemFields.length + 1];
        System.arraycopy(itemFields, 0, codeFields, 0, itemFields.length);
        codeFields[codeFields.length - 1] = orderField;

        setFields(codeFields);

        // Order by ORDER field

        setCanSort(true);
        setSortField(ConceptDS.ORDER);

        // Bind events

        folderContextHandlerRegistration = addFolderContextClickHandler(new FolderContextClickHandler() {

            @Override
            public void onFolderContextClick(final FolderContextClickEvent event) {
                onNodeContextClick(event.getFolder().getName(), (ConceptMetamacVisualisationResult) event.getFolder().getAttributeAsObject(ItemDS.DTO));
            }
        });
        leafContextHandlerRegistration = addLeafContextClickHandler(new LeafContextClickHandler() {

            @Override
            public void onLeafContextClick(LeafContextClickEvent event) {
                onNodeContextClick(event.getLeaf().getName(), (ConceptMetamacVisualisationResult) event.getLeaf().getAttributeAsObject(ItemDS.DTO));
            }
        });

        folderDropHandlerRegistration = addFolderDropHandler(new FolderDropHandler() {

            @Override
            public void onFolderDrop(FolderDropEvent event) {
                TreeNode dropFolder = event.getFolder();
                TreeNode droppedNode = event.getNodes().length > 0 ? event.getNodes()[0] : null;
                int position = event.getIndex(); // Absolute position
                if (isDroppable(dropFolder)) {
                    TreeNode[] siblings = getData().getChildren(dropFolder);

                    // We find out the position of the node under the dropFolder
                    int relativePosition = position; // Used to update position
                    int pos = -1;
                    for (int i = 0; i < siblings.length; i++) {
                        if (siblings[i] == droppedNode) {
                            pos = i;
                        }
                    }
                    if (pos >= 0 && pos < position) { // If moved node is before final position, the position must be updated
                        relativePosition--;
                    }

                    String oldItemParent = droppedNode.getAttribute(ConceptDS.ITEM_PARENT_URN);
                    String newItemParent = SCHEME_NODE_NAME.equals(dropFolder.getName()) ? SCHEME_NODE_NAME : dropFolder.getAttribute(ConceptDS.URN);

                    if (!StringUtils.equals(oldItemParent, newItemParent)) {

                        // UPDATE CODE PARENT

                        if (canStructureBeModified && ConceptsClientSecurityUtils.canUpdateConceptParent(conceptSchemeMetamacDto)) {

                            if (SCHEME_NODE_NAME.equals(newItemParent)) {
                                // The code will be moved to the first level. The parent is null.
                                newItemParent = null;
                            }
                            getBaseConceptUiHandlers().updateConceptParent(droppedNode.getAttribute(ConceptDS.URN), newItemParent, relativePosition);
                        }
                    } else {

                        // UPDATE ORDER

                        if (canOrderBeModified && ConceptsClientSecurityUtils.canUpdateConcept(conceptSchemeMetamacDto)) {

                            // Only update order if there is an order selected and it is not the alphabetical one
                            if (conceptSchemeVisualisationDto != null && !SrmConstants.CONCEPT_SCHEME_ORDER_VISUALISATION_ALPHABETICAL_CODE.equals(conceptSchemeVisualisationDto.getCode())) {
                                getBaseConceptUiHandlers().updateConceptInOrder(droppedNode.getAttribute(ConceptDS.URN), conceptSchemeVisualisationDto.getUrn(), relativePosition);
                            }
                        }
                    }
                }
                event.cancel();
            }
        });
    }

    public void setItems(ItemSchemeDto itemSchemeDto, List<ConceptMetamacVisualisationResult> itemMetamacResults) {
        this.conceptSchemeMetamacDto = (ConceptSchemeMetamacDto) itemSchemeDto;

        // Clear filter editor
        setFilterEditorCriteria(null);

        this.itemSchemeDto = itemSchemeDto;

        TreeNode[] treeNodes = new TreeNode[itemMetamacResults.size() + 1];
        treeNodes[0] = createItemSchemeTreeNode(itemSchemeDto);

        for (int i = 0; i < itemMetamacResults.size(); i++) {
            treeNodes[i + 1] = createItemTreeNode(itemMetamacResults.get(i));
        }

        addTreeNodesToTreeGrid(treeNodes);
    }

    public void addTreeNodesToTreeGrid(TreeNode[] treeNodes) {
        tree = new Tree();
        tree.setModelType(TreeModelType.PARENT);
        tree.linkNodes(treeNodes);
        setData(tree);
        getData().openAll();
    }

    @Override
    public void updateItemScheme(ItemSchemeDto itemSchemeDto) {
        this.conceptSchemeMetamacDto = (ConceptSchemeMetamacDto) itemSchemeDto;
        super.updateItemScheme(itemSchemeDto);
    }

    public void setUiHandlers(BaseConceptUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
    }

    protected ConceptTreeNode createItemTreeNode(ConceptMetamacVisualisationResult item) {
        return ConceptsTreeGridUtils.createConceptTreeNode(SCHEME_NODE_NAME, item);
    }

    protected boolean isDroppable(TreeNode dropFolder) {
        return !("/".equals(getDropFolder().getName()));
    }

    @Override
    protected void onNodeClick(String nodeName, String urn) {
        if (SCHEME_NODE_NAME.equals(nodeName)) {
            getBaseConceptUiHandlers().goToConceptScheme(urn);
        } else {
            getBaseConceptUiHandlers().goToConcept(urn);
        }
    }

    protected abstract void onNodeContextClick(String nodeName, ConceptMetamacVisualisationResult itemMetamacResult);

    @Override
    protected com.smartgwt.client.widgets.viewer.DetailViewerField[] getDetailViewerFields() {
        return ResourceFieldUtils.getCodeDetailViewerFields();
    }

    protected BaseConceptUiHandlers getBaseConceptUiHandlers() {
        return (BaseConceptUiHandlers) uiHandlers;
    }
}
