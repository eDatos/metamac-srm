package org.siemac.metamac.srm.web.client.admin;

import java.util.List;

import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.admin.view.handlers.AdminAnnotationTypeUiHandlers;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.events.UpdateAnnotationTypeRefreshEvent;
import org.siemac.metamac.srm.web.shared.criteria.AnnotationTypeCriteria;
import org.siemac.metamac.srm.web.shared.master.DeleteAnnotationTypesAction;
import org.siemac.metamac.srm.web.shared.master.DeleteAnnotationTypesResult;
import org.siemac.metamac.srm.web.shared.master.GetAnnotationTypesAction;
import org.siemac.metamac.srm.web.shared.master.GetAnnotationTypesPaginatedListAction;
import org.siemac.metamac.srm.web.shared.master.GetAnnotationTypesPaginatedListResult;
import org.siemac.metamac.srm.web.shared.master.GetAnnotationTypesResult;
import org.siemac.metamac.srm.web.shared.master.SaveAnnotationTypeAction;
import org.siemac.metamac.srm.web.shared.master.SaveAnnotationTypeResult;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;

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
        void onAnnotationTypeUpdated(AnnotationTypeDto dto);
        void setAnnotationTypes(int firstResult, List<AnnotationTypeDto> dtos, int totalResults);
        void clearSearchSection();
        AnnotationTypeCriteria getAnnotationTypeCriteria();
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
        getView().clearSearchSection();
        reloadAnnotationTypes(0);
    }

    private void reloadAnnotationTypes(int firstResult) {
        AnnotationTypeCriteria criteria = getView().getAnnotationTypeCriteria();
        criteria.setFirstResult(firstResult);
        retrieveAnnotationTypes(criteria);
    }

    // ACTIONS

    @Override
    public void deleteAnnotationTypes(List<String> annotationTypesUuids, final int firstResult) {
        dispatcher.execute(new DeleteAnnotationTypesAction(annotationTypesUuids), new WaitingAsyncCallbackHandlingError<DeleteAnnotationTypesResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                super.onWaitFailure(caught);
                reloadAnnotationTypes(firstResult);
            }

            @Override
            public void onWaitSuccess(DeleteAnnotationTypesResult result) {
                reloadAnnotationTypes(firstResult);
            }
        });
    }

    @Override
    public void saveAnnotationType(final int currentPage, AnnotationTypeDto dto) {
        final boolean creation = dto.getUuid() == null;

        dispatcher.execute(new SaveAnnotationTypeAction(dto), new WaitingAsyncCallbackHandlingError<SaveAnnotationTypeResult>(this) {

            @Override
            public void onWaitSuccess(final SaveAnnotationTypeResult result) {
                AnnotationTypeCriteria criteria = getView().getAnnotationTypeCriteria();
                criteria.setFirstResult(currentPage);

                retrieveAnnotationTypesAndDoAction(criteria, new Action() {

                    @Override
                    public void run() {
                        if (creation) {
                            getView().onAnnotationTypeCreated(result.getOutputDto());

                        } else {
                            getView().onAnnotationTypeUpdated(result.getOutputDto());
                        }
                        retrieveAnnotationTypes();
                    }
                });
            }
        });
    }

    @Override
    public void retrieveAnnotationTypes(final AnnotationTypeCriteria criteria) {
        retrieveAnnotationTypesAndDoAction(criteria, null);
    }

    private void retrieveAnnotationTypesAndDoAction(final AnnotationTypeCriteria criteria, final Action action) {
        criteria.setMaxResults(SrmWebConstants.MAIN_LIST_MAX_RESULTS);
        dispatcher.execute(new GetAnnotationTypesPaginatedListAction(criteria), new WaitingAsyncCallbackHandlingError<GetAnnotationTypesPaginatedListResult>(this) {

            @Override
            public void onWaitSuccess(GetAnnotationTypesPaginatedListResult result) {
                getView().setAnnotationTypes(criteria.getFirstResult(), result.getDtos(), result.getTotalResults());
                if (action != null) {
                    action.run();
                }
            }
        });
    }

    public void retrieveAnnotationTypes() {
        dispatcher.execute(new GetAnnotationTypesAction(), new WaitingAsyncCallbackHandlingError<GetAnnotationTypesResult>(this) {

            @Override
            public void onWaitSuccess(GetAnnotationTypesResult result) {
                UpdateAnnotationTypeRefreshEvent.fire(AdminAnnotationTypeTabPresenter.this, result.getAnnotationTypes());

            }
        });
    }

    private static interface Action {

        void run();
    }

}