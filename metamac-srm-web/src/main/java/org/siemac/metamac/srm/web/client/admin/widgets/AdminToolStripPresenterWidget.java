package org.siemac.metamac.srm.web.client.admin.widgets;

import org.siemac.metamac.srm.web.client.admin.enums.AdminToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;

public class AdminToolStripPresenterWidget extends PresenterWidget<AdminToolStripPresenterWidget.AdminToolStripView> {

    private final PlaceManager placeManager;

    public interface AdminToolStripView extends View {

        HasClickHandlers getAnnotationTypesButton();

        void selectButton(AdminToolStripButtonEnum button);
    }

    @Inject
    public AdminToolStripPresenterWidget(EventBus eventBus, AdminToolStripView toolStripView, PlaceManager placeManager) {
        super(eventBus, toolStripView);
        this.placeManager = placeManager;
    }

    @Override
    protected void onBind() {
        super.onBind();

        registerHandler(getView().getAnnotationTypesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteAnnotationTypePlaceRequest());
            }
        }));

    }

    public void selectCategoriesMenuButton(AdminToolStripButtonEnum button) {
        getView().selectButton(button);
    }
}
