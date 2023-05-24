package org.siemac.metamac.srm.web.client.admin;

import java.util.List;

import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.admin.view.handlers.AdminAnnotationTypeUiHandlers;
import org.siemac.metamac.srm.web.shared.criteria.AnnotationTypeCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class AdminAnnotationTypeTabPresenter extends Presenter<AdminAnnotationTypeTabPresenter.AdminAnnotationTypeTabView, AdminAnnotationTypeTabPresenter.AdminAnnotationTypeTabProxy>
        implements
            AdminAnnotationTypeUiHandlers {

    private DispatchAsync dispatcher;

    public interface AdminAnnotationTypeTabView extends View, HasUiHandlers<AdminAnnotationTypeUiHandlers> {

        void onAnnotationTypeCreated(AnnotationTypeDto dto);
        void onAnnotationUpdated(AnnotationTypeDto dto);
        void setAnnotationTypes(int firstResult, List<AnnotationTypeDto> dtos, int totalResults);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.annotationTypePage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface AdminAnnotationTypeTabProxy extends Proxy<AdminAnnotationTypeTabPresenter>, Place {
    }

    @Inject
    public AdminAnnotationTypeTabPresenter(EventBus eventBus, AdminAnnotationTypeTabView view, AdminAnnotationTypeTabProxy proxy, DispatchAsync dispatcher) {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, AdminPresenter.TYPE_SetContextAreaAdmin, this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);

        reloadAnnotationTypes(0);
    }

    private void reloadAnnotationTypes(int firstResult) {

    }

    // ACTIONS

    @Override
    public void deleteAnnotationTypes(List<String> annotationTypesCodes) {
        // dispatcher.execute(new DeleteUnitMultipliersAction(uuids), new WaitingAsyncCallbackHandlingError<DeleteUnitMultipliersResult>(this) {
    }

    @Override
    public void saveAnnotationType(int currentPage, AnnotationTypeDto annotationTypeDto) {
        // dispatcher.execute(new DeleteUnitMultipliersAction(uuids), new WaitingAsyncCallbackHandlingError<DeleteUnitMultipliersResult>(this) {
    }

    @Override
    public void retrieveAnnotationType(AnnotationTypeCriteria criteria) {
        // dispatcher.execute(new DeleteUnitMultipliersAction(uuids), new WaitingAsyncCallbackHandlingError<DeleteUnitMultipliersResult>(this) {
    }

    private static interface Action {

        void run();
    }

}