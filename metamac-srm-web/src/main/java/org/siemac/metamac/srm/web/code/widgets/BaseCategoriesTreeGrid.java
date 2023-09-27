package org.siemac.metamac.srm.web.code.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.category.domain.shared.CategoryMetamacVisualisationResult;
import org.siemac.metamac.srm.core.category.domain.shared.CodeCategoryElementNormalisationResult;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.utils.CategoriesTreeGridUtils;
import org.siemac.metamac.srm.web.category.view.handlers.BaseCategoryUiHandlers;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.client.widgets.BaseItemsTreeGrid;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.smartgwt.client.types.TreeModelType;
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

    public BaseCategoriesTreeGrid() {

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
