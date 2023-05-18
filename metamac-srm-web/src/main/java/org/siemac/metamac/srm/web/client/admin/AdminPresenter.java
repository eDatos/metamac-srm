package org.siemac.metamac.srm.web.client.admin;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;

import java.util.List;
import java.util.logging.Logger;

import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.admin.view.handlers.AdminUiHandlers;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.client.widgets.presenter.ToolStripPresenterWidget;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class AdminPresenter extends Presenter<AdminPresenter.AdminView, AdminPresenter.AdminProxy> implements AdminUiHandlers {

    private Logger                                    logger                            = Logger.getLogger(AdminPresenter.class.getName());

    private DispatchAsync                             dispatcher;

    private PlaceManager                              placeManager;

    private ToolStripPresenterWidget                  toolStripPresenterWidget;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentToolBar = new Type<RevealContentHandler<?>>();

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaAdmin          = new Type<RevealContentHandler<?>>();

    public interface AdminView extends View, HasUiHandlers<AdminUiHandlers> {

        void selectAnnotationTypeTab();
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.adminPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface AdminProxy extends Proxy<AdminPresenter>, Place {
    }

    @Inject
    public AdminPresenter(EventBus eventBus, AdminView view, AdminProxy proxy, PlaceManager placeManager, DispatchAsync dispatcher, ToolStripPresenterWidget toolStripPresenterWidget) {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
        this.toolStripPresenterWidget = toolStripPresenterWidget;
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);

        // Redirect to metadata tab
        // getView().selectQuantityUnitsTab();
        if (NameTokens.adminPage.equals(placeManager.getCurrentPlaceRequest().getNameToken())) {
            goToQuantityUnitsTab();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        SetTitleEvent.fire(AdminPresenter.this, getConstants().admin());
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        setInSlot(TYPE_SetContextAreaContentToolBar, toolStripPresenterWidget);
    }

    // NAVIGATION
    @Override
    public void goToAnnotationTypeTab() {
        List<PlaceRequest> hierarchy = PlaceRequestUtils.getHierarchyUntilNameToken(placeManager, NameTokens.adminPage);
        hierarchy.add(new PlaceRequest(NameTokens.adminUnitMultipliersPage));
        placeManager.revealPlaceHierarchy(hierarchy);
    }
}
