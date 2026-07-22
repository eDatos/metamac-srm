package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.category.domain.shared.CategoryMetamacVisualisationResult;
import org.siemac.metamac.srm.core.category.domain.shared.CodeCategoryElementNormalisationResult;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.model.ds.CategoryDS;
import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.category.utils.CategoriesTreeGridUtils;
import org.siemac.metamac.srm.web.category.view.handlers.BaseCategoryUiHandlers;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.client.widgets.BaseItemsTreeGrid;
import org.siemac.metamac.web.common.client.utils.ListGridUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderContextClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderContextClickHandler;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;
import com.smartgwt.client.widgets.tree.events.LeafContextClickEvent;
import com.smartgwt.client.widgets.tree.events.LeafContextClickHandler;

public abstract class BaseCategoriesTreeGrid extends BaseItemsTreeGrid {

    protected CategorySchemeMetamacDto categorySchemeMetamacDto;

    protected HandlerRegistration      folderDropHandlerRegistration;

    /**
     * Create a {@link BaseCategoriesTreeGrid}. This tree will be extended by {@link CategoriesTreeGrid} and {@link CategorySchemeOrderTreeGrid}.
     *
     * @param canStructureBeModified specifies if the structure of this tree can be modified (mark as <code>true</code> by {@link CategoriesTreeGrid})
     * @param canOrderBeModified specifies if the order of this tree can be modified (mark as <code>true</code> by {@link CategorySchemeOrderTreeGrid})
     */
    public BaseCategoriesTreeGrid(final boolean canStructureBeModified, final boolean canOrderBeModified) {

        // Add the ORDER field (hidden) for sorting by visualisation order
        ListGridField[] itemFields = getAllFields();
        TreeGridField orderField = new TreeGridField(CategoryDS.ORDER, getConstants().codeOrder());
        orderField.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());
        orderField.setCanSort(true);
        orderField.setShowHover(false);

        ListGridField[] categoryFields = new ListGridField[itemFields.length + 1];
        System.arraycopy(itemFields, 0, categoryFields, 0, itemFields.length);
        categoryFields[categoryFields.length - 1] = orderField;

        setFields(categoryFields);

        // Sort siblings by ORDER to respect the configured visualisation order
        setCanSort(true);
        setSortField(CategoryDS.ORDER);

        // Bind events

        folderContextHandlerRegistration = addFolderContextClickHandler(new FolderContextClickHandler() {

            @Override
            public void onFolderContextClick(final FolderContextClickEvent event) {
                onNodeContextClick(event.getFolder().getName(), (CategoryMetamacVisualisationResult) event.getFolder().getAttributeAsObject(ItemDS.DTO));
            }
        });
        leafContextHandlerRegistration = addLeafContextClickHandler(new LeafContextClickHandler() {

            @Override
            public void onLeafContextClick(LeafContextClickEvent event) {
                onNodeContextClick(event.getLeaf().getName(), (CategoryMetamacVisualisationResult) event.getLeaf().getAttributeAsObject(ItemDS.DTO));
            }
        });

        folderDropHandlerRegistration = addFolderDropHandler(new FolderDropHandler() {

            @Override
            public void onFolderDrop(FolderDropEvent event) {
                TreeNode dropFolder = event.getFolder();
                TreeNode droppedNode = event.getNodes().length > 0 ? event.getNodes()[0] : null;
                int position = event.getIndex();
                if (isDroppable(dropFolder)) {
                    TreeNode[] siblings = getData().getChildren(dropFolder);

                    int relativePosition = position;
                    int pos = -1;
                    for (int i = 0; i < siblings.length; i++) {
                        if (siblings[i] == droppedNode) {
                            pos = i;
                        }
                    }

                    if (pos >= 0 && pos < position) {
                        relativePosition--;
                    }

                    if (position == 0 && pos == 0 && relativePosition == 0) {
                        // avoid first item being moved to scheme root node position
                        event.cancel();
                        return;
                    }

                    String oldItemParent = droppedNode.getAttribute(ItemDS.ITEM_PARENT_URN);
                    String newItemParent = SCHEME_NODE_NAME.equals(dropFolder.getName()) ? SCHEME_NODE_NAME : dropFolder.getAttribute(ItemDS.URN);

                    if (!StringUtils.equals(oldItemParent, newItemParent)) {

                        // UPDATE CATEGORY PARENT

                        if (canStructureBeModified && categorySchemeMetamacDto != null && CategoriesClientSecurityUtils.canUpdateCategoryParent(categorySchemeMetamacDto)) {
                            if (SCHEME_NODE_NAME.equals(newItemParent)) {
                                // The category will be moved to the first level. The parent is null.
                                newItemParent = null;
                            }
                            getBaseCategoryUiHandlers().updateCategoryParent(droppedNode.getAttribute(ItemDS.URN), newItemParent);
                        }
                    }
                }
                event.cancel();
            }
        });
    }

    public void setItems(ItemSchemeDto itemSchemeDto, List<CategoryMetamacVisualisationResult> itemMetamacResults) {
        this.categorySchemeMetamacDto = (CategorySchemeMetamacDto) itemSchemeDto;

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

    /**
     * This method is used only by the tree that show the codes and their variable elements
     * 
     * @param itemSchemeDto
     * @param codeVariableElementNormalisationResults
     */
    public void setCodesAndCategoryElements(CategorySchemeMetamacDto itemSchemeDto, List<CodeCategoryElementNormalisationResult> codeCategoryElementNormalisationResults) {
        this.categorySchemeMetamacDto = itemSchemeDto;

        // Clear filter editor
        setFilterEditorCriteria(null);

        this.itemSchemeDto = itemSchemeDto;

        TreeNode[] treeNodes = new TreeNode[codeCategoryElementNormalisationResults.size() + 1];
        treeNodes[0] = createItemSchemeTreeNode(itemSchemeDto);

        for (int i = 0; i < codeCategoryElementNormalisationResults.size(); i++) {
            treeNodes[i + 1] = createItemTreeNode(codeCategoryElementNormalisationResults.get(i));
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
        this.categorySchemeMetamacDto = (CategorySchemeMetamacDto) itemSchemeDto;
        super.updateItemScheme(itemSchemeDto);
    }

    public void setUiHandlers(BaseCategoryUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
    }

    protected CategoryTreeNode createItemTreeNode(CategoryMetamacVisualisationResult item) {
        return CategoriesTreeGridUtils.createCategoryTreeNode(SCHEME_NODE_NAME, item);
    }

    protected CategoryTreeNode createItemTreeNode(CodeCategoryElementNormalisationResult result) {
        return CategoriesTreeGridUtils.createCategoryTreeNode(SCHEME_NODE_NAME, result);
    }

    protected boolean isDroppable(TreeNode dropFolder) {
        return !("/".equals(getDropFolder().getName()));
    }

    @Override
    protected void onNodeClick(String nodeName, String urn) {
        if (SCHEME_NODE_NAME.equals(nodeName)) {
            getBaseCategoryUiHandlers().goToCategoryScheme(urn);
        } else {
            getBaseCategoryUiHandlers().goToCategory(urn);
        }
    }

    protected abstract void onNodeContextClick(String nodeName, CategoryMetamacVisualisationResult itemMetamacResult);

    @Override
    protected com.smartgwt.client.widgets.viewer.DetailViewerField[] getDetailViewerFields() {
        return ResourceFieldUtils.getCodeDetailViewerFields();
    }

    protected BaseCategoryUiHandlers getBaseCategoryUiHandlers() {
        return (BaseCategoryUiHandlers) uiHandlers;
    }
}
