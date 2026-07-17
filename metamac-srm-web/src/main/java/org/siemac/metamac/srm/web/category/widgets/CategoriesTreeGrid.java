package org.siemac.metamac.srm.web.category.widgets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.category.domain.shared.CategoryMetamacVisualisationResult;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.ResourceFieldUtils;
import org.siemac.metamac.srm.web.code.widgets.BaseCategoriesTreeGrid;
import org.siemac.metamac.web.common.client.widgets.DeleteConfirmationWindow;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.menu.events.ClickHandler;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.events.FolderClosedEvent;
import com.smartgwt.client.widgets.tree.events.FolderClosedHandler;
import com.smartgwt.client.widgets.tree.events.FolderOpenedEvent;
import com.smartgwt.client.widgets.tree.events.FolderOpenedHandler;

public class CategoriesTreeGrid extends BaseCategoriesTreeGrid {

    private NewCategoryWindow                  newCategoryWindow;
    private DeleteConfirmationWindow           deleteConfirmationWindow;

    private MenuItem                           createCategoryMenuItem;
    private MenuItem                           deleteCategoryMenuItem;

    private CategoryMetamacVisualisationResult selectedCategory;

    // Internal representation that helps us to recover opened nodes after reloading tree
    private Map<String, String>                treeOpenStates = new HashMap<String, String>();

    public CategoriesTreeGrid() {
        super(true, false);

        setCanReorderRecords(true);
        setCanAcceptDroppedRecords(true);
        setCanDragRecordsOut(false);
        setDragDataAction(DragDataAction.MOVE);
        setShowOpenIcons(true);
        setShowDropIcons(true);

        // Context menu

        createCategoryMenuItem = new MenuItem(MetamacSrmWeb.getConstants().categoryCreate());
        createCategoryMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                newCategoryWindow = new NewCategoryWindow(MetamacSrmWeb.getConstants().categoryCreate());
                newCategoryWindow.getSave().addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

                    @Override
                    public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                        if (newCategoryWindow.validateForm()) {
                            CategoryMetamacDto categoryMetamacDto = newCategoryWindow.getNewCategoryDto();
                            categoryMetamacDto.setItemSchemeVersionUrn(categorySchemeMetamacDto.getUrn()); // Set category scheme URN
                            categoryMetamacDto.setItemParentUrn(selectedCategory != null ? selectedCategory.getUrn() : null); // Set category parent URN
                            getBaseCategoryUiHandlers().saveCategory(categoryMetamacDto);
                            newCategoryWindow.destroy();
                        }
                    }
                });
            }
        });

        deleteCategoryMenuItem = new MenuItem(MetamacSrmWeb.getConstants().categoryDelete());
        deleteConfirmationWindow = new DeleteConfirmationWindow(MetamacSrmWeb.getConstants().categoryDeleteConfirmationTitle(), MetamacSrmWeb.getConstants().categoryDeleteConfirmation());
        deleteConfirmationWindow.getYesButton().addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getBaseCategoryUiHandlers().deleteCategory(selectedCategory);
            }
        });
        deleteCategoryMenuItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(MenuItemClickEvent event) {
                deleteConfirmationWindow.show();
            }
        });

        addItemsToContextMenu(createCategoryMenuItem, deleteCategoryMenuItem);

        // Tree open state

        addFolderOpenedHandler(new FolderOpenedHandler() {

            @Override
            public void onFolderOpened(FolderOpenedEvent event) {
                saveTreeOpenState();
            }
        });

        addFolderClosedHandler(new FolderClosedHandler() {

            @Override
            public void onFolderClosed(FolderClosedEvent event) {
                saveTreeOpenState();
            }
        });
    }

    public void setCategories(CategorySchemeMetamacDto categorySchemeMetamacDto, List<CategoryMetamacVisualisationResult> categoryVisualisationResults) {
        super.setItems(categorySchemeMetamacDto, categoryVisualisationResults);
    }

    @Override
    public void setItems(ItemSchemeDto itemSchemeDto, List<CategoryMetamacVisualisationResult> itemMetamacResults) {
        super.setItems(itemSchemeDto, itemMetamacResults);
        recoverOpenState();
    }

    @Override
    protected void onNodeContextClick(String nodeName, CategoryMetamacVisualisationResult category) {
        selectedCategory = category;
        createCategoryMenuItem.setEnabled(canCreateCategory());
        deleteCategoryMenuItem.setEnabled(canDeleteCategory(nodeName));
        showContextMenu();
    }

    @Override
    protected com.smartgwt.client.widgets.viewer.DetailViewerField[] getDetailViewerFields() {
        return ResourceFieldUtils.getCategoryDetailViewerFields();
    }

    @Override
    public void removeHandlerRegistrations() {
        super.removeHandlerRegistrations();
        folderDropHandlerRegistration.removeHandler();
    }

    private boolean canCreateCategory() {
        return CategoriesClientSecurityUtils.canCreateCategory(categorySchemeMetamacDto);
    }

    private boolean canDeleteCategory(String nodeName) {
        return !SCHEME_NODE_NAME.equals(nodeName) && CategoriesClientSecurityUtils.canDeleteCategory(categorySchemeMetamacDto);
    }

    private void recoverOpenState() {
        if (treeOpenStates.containsKey(itemSchemeDto.getUrn()) && !StringUtils.isBlank(treeOpenStates.get(itemSchemeDto.getUrn()))) {
            setOpenState(treeOpenStates.get(itemSchemeDto.getUrn()));
        }
    }

    private void saveTreeOpenState() {
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {

            @Override
            public void execute() {
                treeOpenStates.put(itemSchemeDto.getUrn(), getOpenState());
            }
        });
    }
}
