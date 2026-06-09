package org.siemac.metamac.srm.web.category.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.category.domain.shared.CategoryMetamacVisualisationResult;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeVisualisationDto;
import org.siemac.metamac.srm.web.category.view.handlers.CategorySchemeUiHandlers;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;

import com.smartgwt.client.widgets.layout.VLayout;

public class CategorySchemeOrdersPanel extends VLayout {

    private CategorySchemeOrdersSectionStack categorySchemeOrdersSectionStack;
    private TitleLabel                       orderTitle;
    private CategorySchemeOrderTreeGrid      categoriesOrderTreeGrid;

    public CategorySchemeOrdersPanel() {
        setMargin(15);
        setMembersMargin(10);

        categorySchemeOrdersSectionStack = new CategorySchemeOrdersSectionStack();
        addMember(categorySchemeOrdersSectionStack);

        orderTitle = new TitleLabel();
        orderTitle.setStyleName("subsectionTitleWithNoLeftMargin");
        addMember(orderTitle);

        categoriesOrderTreeGrid = new CategorySchemeOrderTreeGrid();
        addMember(categoriesOrderTreeGrid);
    }

    public void setOrders(List<CategorySchemeVisualisationDto> orders) {
        categorySchemeOrdersSectionStack.setCategorySchemeVisualisations(orders);
        hideCodes();
    }

    public void setCategories(CategorySchemeMetamacDto categorySchemeMetamacDto, List<CategoryMetamacVisualisationResult> categories,
            CategorySchemeVisualisationDto categorySchemeOrderVisualisationDto) {
        categorySchemeOrdersSectionStack.selectCategorySchemeVisualisation(categorySchemeOrderVisualisationDto.getUrn());
        orderTitle.setContents(CommonWebUtils.getElementName(categorySchemeOrderVisualisationDto.getCode(), categorySchemeOrderVisualisationDto.getName()));
        categoriesOrderTreeGrid.setItems(categorySchemeMetamacDto, categories, categorySchemeOrderVisualisationDto);
        categoriesOrderTreeGrid.setAutoFitMaxRecords(categories.size() + 1);

        showCodes();
    }

    public void setUiHandlers(CategorySchemeUiHandlers uiHandlers) {
        categorySchemeOrdersSectionStack.setUiHandlers(uiHandlers);
        categoriesOrderTreeGrid.setUiHandlers(uiHandlers);
    }

    public void setCategoryScheme(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        categorySchemeOrdersSectionStack.setCategoryScheme(categorySchemeMetamacDto);
        categoriesOrderTreeGrid.updateItemScheme(categorySchemeMetamacDto);
    }

    public CategorySchemeOrdersSectionStack getCategorySchemeOrdersSectionStack() {
        return categorySchemeOrdersSectionStack;
    }

    public TitleLabel getOrderTitle() {
        return orderTitle;
    }

    public CategorySchemeOrderTreeGrid getCategoriesOrderTreeGrid() {
        return categoriesOrderTreeGrid;
    }

    public void showCodes() {
        orderTitle.show();
        categoriesOrderTreeGrid.show();
    }

    public void hideCodes() {
        orderTitle.hide();
        categoriesOrderTreeGrid.hide();
    }
}
