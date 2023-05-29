package org.siemac.metamac.srm.web.client.admin.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.admin.AdminPresenter;
import org.siemac.metamac.srm.web.client.admin.view.handlers.AdminUiHandlers;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class AdminViewImpl extends ViewWithUiHandlers<AdminUiHandlers> implements AdminPresenter.AdminView {

    private VLayout    panel;
    private TitleLabel adminLabel;
    private TabSet     tabset;

    private Tab        annotationTypeTab;

    @Inject
    public AdminViewImpl(AdminAnnotationTypeTabViewImpl annotationTypeView) {

        adminLabel = new TitleLabel();
        adminLabel.setStyleName("sectionTitleLeftMargin");

        annotationTypeTab = new Tab(getConstants().adminAnnotationType());
        annotationTypeTab.setPane((Canvas) annotationTypeView.asWidget());

        tabset = new TabSet();
        tabset.addTab(annotationTypeTab);

        panel = new VLayout();
        panel.addMember(adminLabel);
        panel.addMember(tabset);

        bindEvents();
    }

    private void bindEvents() {
        annotationTypeTab.addTabSelectedHandler(new TabSelectedHandler() {

            @Override
            public void onTabSelected(TabSelectedEvent event) {
                getUiHandlers().goToAnnotationTypeTab();
            }
        });
    }

    @Override
    public void selectAnnotationTypeTab() {
        tabset.selectTab(annotationTypeTab);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == AdminPresenter.TYPE_SetContextAreaContentToolBar) {
            if (content != null) {
                panel.addMember(content, 0);
            }
        } else {
            // To support inheritance in your views it is good practice to call super.setInSlot when you can't handle the call.
            // Who knows, maybe the parent class knows what to do with this slot.
            super.setInSlot(slot, content);
        }
    }

}
