package org.siemac.metamac.srm.web.client.admin.widgets;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.admin.view.handlers.AdminAnnotationTypeUiHandlers;
import org.siemac.metamac.srm.web.shared.criteria.AnnotationTypeCriteria;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.BaseAdvancedSearchSectionStack;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public class AnnotationTypeSearchSectionStack extends BaseAdvancedSearchSectionStack {

    private AdminAnnotationTypeUiHandlers uiHandlers;

    public AnnotationTypeSearchSectionStack() {
        searchForm.getItem(SEARCH_ITEM_NAME).setWidth(180);
    }

    @Override
    protected void createAdvancedSearchForm() {
        advancedSearchForm = new GroupDynamicForm(StringUtils.EMPTY);
        advancedSearchForm.setPadding(5);
        advancedSearchForm.setMargin(5);
        advancedSearchForm.setVisible(false);

        CustomButtonItem searchItem = new CustomButtonItem(ADVANCED_SEARCH_ITEM_NAME, MetamacWebCommon.getConstants().search());
        searchItem.setColSpan(4);
        searchItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                retrieveResources();
            }
        });

        FormItem[] advancedSearchFormItems = new FormItem[]{searchItem};
        setFormItemsInAdvancedSearchForm(advancedSearchFormItems);
    }

    @Override
    protected void retrieveResources() {
        AnnotationTypeCriteria criteria = new AnnotationTypeCriteria();
        criteria.setCriteria(searchForm.getValueAsString(SEARCH_ITEM_NAME));
        getUiHandlers().retrieveAnnotationTypes(getAnnotationTypesCriteria());
    }

    public AnnotationTypeCriteria getAnnotationTypesCriteria() {
        AnnotationTypeCriteria criteria = new AnnotationTypeCriteria();
        criteria.setCriteria(searchForm.getValueAsString(SEARCH_ITEM_NAME));

        return criteria;
    }

    public void setUiHandlers(AdminAnnotationTypeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }

    public AdminAnnotationTypeUiHandlers getUiHandlers() {
        return uiHandlers;
    }
}
