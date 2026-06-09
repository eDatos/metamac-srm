package org.siemac.metamac.srm.web.category.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.category.domain.shared.CategoryMetamacVisualisationResult;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeVisualisationDto;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.category.model.ds.CategoryDS;
import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.category.view.handlers.CategorySchemeUiHandlers;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.google.gwt.event.shared.HandlerRegistration;
import com.smartgwt.client.data.SortSpecifier;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.tree.events.FolderDropEvent;
import com.smartgwt.client.widgets.tree.events.FolderDropHandler;
import com.smartgwt.client.widgets.grid.events.SortChangedHandler;
import com.smartgwt.client.widgets.grid.events.SortEvent;

public class CategorySchemeOrderTreeGrid extends CategoriesTreeGrid {

    private CategorySchemeVisualisationDto categorySchemeVisualisationDto;
    private CategorySchemeMetamacDto       localCategorySchemeMetamacDto;
    protected HandlerRegistration          folderDropHandlerRegistration;

    public CategorySchemeOrderTreeGrid() {
        setShowFilterEditor(false);

        getField(ItemDS.CODE).setWidth("45%");

        setCanReorderRecords(true);
        setCanAcceptDroppedRecords(true);
        setCanDragRecordsOut(false);
        setDragDataAction(DragDataAction.MOVE);
        setShowOpenIcons(true);
        setShowDropIcons(true);

        // Disable sorting by fields other than the order field
        ListGridField[] itemFields = getAllFields();
        for (ListGridField itemField : itemFields) {
            if (!CategoryDS.ORDER.equals(itemField.getName())) {
                itemField.setCanSort(false);
            }
        }

        // Do not allow descending sort
        addSortChangedHandler(new SortChangedHandler() {

            @Override
            public void onSortChanged(SortEvent event) {
                SortSpecifier[] sortSpecifiers = event.getSortSpecifiers();
                if (sortSpecifiers != null) {
                    for (SortSpecifier sortSpecifier : sortSpecifiers) {
                        if (SortDirection.DESCENDING.equals(sortSpecifier.getSortDirection())) {
                            sort(CategoryDS.ORDER, SortDirection.ASCENDING);
                        }
                    }
                }
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
                        event.cancel();
                        return;
                    }

                    String oldItemParent = droppedNode.getAttribute(CategoryDS.ITEM_PARENT_URN);
                    String newItemParent = SCHEME_NODE_NAME.equals(dropFolder.getName()) ? SCHEME_NODE_NAME : dropFolder.getAttribute(ItemDS.URN);

                    if (org.siemac.metamac.core.common.util.shared.StringUtils.equals(oldItemParent, newItemParent)) {
                        // UPDATE ORDER
                        if (categorySchemeVisualisationDto != null
                                && !SrmConstants.CATEGORY_SCHEME_ORDER_VISUALISATION_ALPHABETICAL_CODE.equals(categorySchemeVisualisationDto.getCode())
                                && localCategorySchemeMetamacDto != null
                                && CategoriesClientSecurityUtils.canUpdateCategory(localCategorySchemeMetamacDto.getLifeCycle().getProcStatus())) {
                            getCategorySchemeUiHandlers().updateCategoryInOrder(droppedNode.getAttribute(ItemDS.URN), categorySchemeVisualisationDto.getUrn(), relativePosition);
                        }
                    }
                }
                event.cancel();
            }
        });

        // Do not show context menu
        super.removeHandlerRegistrations();
    }

    @Override
    public void removeHandlerRegistrations() {
        super.removeHandlerRegistrations();
        folderDropHandlerRegistration.removeHandler();
    }

    @Override
    public void setCategories(CategorySchemeMetamacDto categorySchemeMetamacDto, List<CategoryMetamacVisualisationResult> categoryVisualisationResults) {
        this.localCategorySchemeMetamacDto = categorySchemeMetamacDto;
        super.setCategories(categorySchemeMetamacDto, categoryVisualisationResults);
    }

    public void setItems(ItemSchemeDto categorySchemeMetamacDto, List<CategoryMetamacVisualisationResult> categories, CategorySchemeVisualisationDto categorySchemeOrderVisualisationDto) {
        this.categorySchemeVisualisationDto = categorySchemeOrderVisualisationDto;
        setCategories((CategorySchemeMetamacDto) categorySchemeMetamacDto, categories);
    }

    @Override
    protected void onNodeContextClick(String nodeName, CategoryMetamacVisualisationResult category) {
        // Do nothing
    }

    protected boolean isDroppable(TreeNode dropFolder) {
        return !("/".equals(getDropFolder().getName()));
    }

    private CategorySchemeUiHandlers getCategorySchemeUiHandlers() {
        return (CategorySchemeUiHandlers) uiHandlers;
    }
}
