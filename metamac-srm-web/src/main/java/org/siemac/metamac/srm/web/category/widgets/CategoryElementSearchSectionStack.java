package org.siemac.metamac.srm.web.category.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.Date;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.category.model.ds.CategoryElementDS;
import org.siemac.metamac.srm.web.category.view.handlers.CategoryElementsUiHandlers;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.resource.element.model.ds.ResourceElementDS;
import org.siemac.metamac.srm.web.shared.criteria.CategoryElementWebCriteria;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.BaseAdvancedSearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.BooleanSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDateItem;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public class CategoryElementSearchSectionStack extends BaseAdvancedSearchSectionStack {

    private CategoryElementsUiHandlers uiHandlers;

    public CategoryElementSearchSectionStack() {
    }

    @Override
    protected void clearAdvancedSearchSection() {
        super.clearAdvancedSearchSection();
    }

    @Override
    protected void createAdvancedSearchForm() {
        advancedSearchForm = new GroupDynamicForm(StringUtils.EMPTY);
        advancedSearchForm.setPadding(5);
        advancedSearchForm.setMargin(5);
        advancedSearchForm.setVisible(false);

        CustomDateItem validFrom = new CustomDateItem(CategoryElementDS.VALID_FROM, getConstants().categoryElementValidFrom());
        CustomDateItem validTo = new CustomDateItem(CategoryElementDS.VALID_TO, getConstants().categoryElementValidTo());
        BooleanSelectItem isVisible = new BooleanSelectItem(ResourceElementDS.IS_VISIBLE, getConstants().categoryElementVisible(), false);

        CustomButtonItem searchItem = new CustomButtonItem(ADVANCED_SEARCH_ITEM_NAME, MetamacWebCommon.getConstants().search());
        searchItem.setColSpan(4);
        searchItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                retrieveResources();
            }
        });

        FormItem[] advancedSearchFormItems = new FormItem[]{validFrom, validTo, isVisible, searchItem};
        setFormItemsInAdvancedSearchForm(advancedSearchFormItems);
    }

    @Override
    public void retrieveResources() {
        getUiHandlers().retrieveCategoryElements(SrmWebConstants.ITEM_LIST_FIRST_RESULT, SrmWebConstants.ITEM_LIST_MAX_RESULTS, getCategoryElementWebCriteria());
    }

    public CategoryElementWebCriteria getCategoryElementWebCriteria() {
        CategoryElementWebCriteria categoryElementWebCriteria = new CategoryElementWebCriteria();
        categoryElementWebCriteria.setCriteria(searchForm.getValueAsString(SEARCH_ITEM_NAME));

        categoryElementWebCriteria.setValidFromDate(advancedSearchForm.getValue(CategoryElementDS.VALID_FROM) != null ? (Date) advancedSearchForm.getValue(CategoryElementDS.VALID_FROM) : null);
        categoryElementWebCriteria.setValidToDate(advancedSearchForm.getValue(CategoryElementDS.VALID_TO) != null ? (Date) advancedSearchForm.getValue(CategoryElementDS.VALID_TO) : null);
        categoryElementWebCriteria.setVisible(((BooleanSelectItem) advancedSearchForm.getItem(ResourceElementDS.IS_VISIBLE)).getBooleanValue());
        return categoryElementWebCriteria;
    }

    public void setUiHandlers(CategoryElementsUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public CategoryElementsUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}
