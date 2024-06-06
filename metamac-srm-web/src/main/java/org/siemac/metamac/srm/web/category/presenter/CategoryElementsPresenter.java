package org.siemac.metamac.srm.web.category.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.category.enums.CategoriesToolStripButtonEnum;
import org.siemac.metamac.srm.web.category.view.handlers.CategoryElementsUiHandlers;
import org.siemac.metamac.srm.web.category.widgets.presenter.CategoriesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.shared.category.DeleteCategoryElementsAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategoryElementsResult;
import org.siemac.metamac.srm.web.shared.category.ExportCategoryElementsAction;
import org.siemac.metamac.srm.web.shared.category.ExportCategoryElementsResult;
import org.siemac.metamac.srm.web.shared.category.GetCategoryElementsAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoryElementsResult;
import org.siemac.metamac.srm.web.shared.category.SaveCategoryElementAction;
import org.siemac.metamac.srm.web.shared.category.SaveCategoryElementResult;
import org.siemac.metamac.srm.web.shared.code.TempFilesDeleteResult;
import org.siemac.metamac.srm.web.shared.code.TempFilesDeleteAction;
import org.siemac.metamac.srm.web.shared.criteria.CategoryElementWebCriteria;
import org.siemac.metamac.web.common.client.events.ChangeWaitPopupVisibilityEvent;
import org.siemac.metamac.web.common.client.events.SetTitleEvent;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;

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
import com.gwtplatform.mvp.client.annotations.TitleFunction;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;

public class CategoryElementsPresenter extends Presenter<CategoryElementsPresenter.CategoryElementsView, CategoryElementsPresenter.CategoryElementsProxy> implements CategoryElementsUiHandlers {

    public final static int                           ELEMENT_LIST_FIRST_RESULT                   = 0;
    public final static int                           ELEMENT_LIST_MAX_RESULTS                    = 200;

    private final DispatchAsync                       dispatcher;
    private final PlaceManager                        placeManager;

    private CategoriesToolStripPresenterWidget        categoriesToolStripPresenterWidget;

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetStructuralResourcesToolBar          = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCategoriesToolBar = new Object();

    @ProxyCodeSplit
    @NameToken(NameTokens.categoryElementsPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface CategoryElementsProxy extends Proxy<CategoryElementsPresenter>, Place {
    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbCategoryElements();
    }

    public interface CategoryElementsView extends View, HasUiHandlers<CategoryElementsUiHandlers> {

        void setCategoryElements(GetCategoryElementsResult result);
        void clearSearchSection();
    }

    @Inject
    public CategoryElementsPresenter(EventBus eventBus, CategoryElementsView categoryElementsView, CategoryElementsProxy categoryElementsProxy, DispatchAsync dispatcher, PlaceManager placeManager,
            CategoriesToolStripPresenterWidget categoriesToolStripPresenterWidget) {
        super(eventBus, categoryElementsView, categoryElementsProxy);
        this.placeManager = placeManager;
        this.dispatcher = dispatcher;
        this.categoriesToolStripPresenterWidget = categoriesToolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        // Load category elements
        retrieveCategoryElements(ELEMENT_LIST_FIRST_RESULT, ELEMENT_LIST_MAX_RESULTS, null);
        // Clear search section
        getView().clearSearchSection();
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentCategoriesToolBar, categoriesToolStripPresenterWidget);

        SetTitleEvent.fire(this, getConstants().categoryElements());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CATEGORIES);
        categoriesToolStripPresenterWidget.selectCategoriesMenuButton(CategoriesToolStripButtonEnum.CATEGORY_ELEMENTS);
    }

    @Override
    public void retrieveCategoryElements(int firstResult, int maxResults, CategoryElementWebCriteria criteria) {

        dispatcher.execute(new GetCategoryElementsAction(firstResult, maxResults, criteria), new WaitingAsyncCallbackHandlingError<GetCategoryElementsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CategoryElementsPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetCategoryElementsResult result) {
                getView().setCategoryElements(result);
            }
        });

    }

    @Override
    public void deleteCategoryElements(List<String> urns) {

        dispatcher.execute(new DeleteCategoryElementsAction(urns), new WaitingAsyncCallbackHandlingError<DeleteCategoryElementsResult>(this) {

            @Override
            public void onWaitSuccess(DeleteCategoryElementsResult result) {
                retrieveCategoryElements(ELEMENT_LIST_FIRST_RESULT, ELEMENT_LIST_MAX_RESULTS, null);
                fireSuccessMessage(MetamacSrmWeb.getMessages().categoryElementDeleted());
            }
        });

    }

    //
    // IMPORTATION
    //

    @Override
    public void resourceImportationSucceed(String successMessage) {
        ShowMessageEvent.fireSuccessMessage(CategoryElementsPresenter.this, successMessage);
        retrieveCategoryElements(ELEMENT_LIST_FIRST_RESULT, ELEMENT_LIST_MAX_RESULTS, null);
        ChangeWaitPopupVisibilityEvent.fire(this, false);
    }

    @Override
    public void resourceImportationFailed(String errorMessage) {
        ShowMessageEvent.fireErrorMessage(CategoryElementsPresenter.this, errorMessage);
        ChangeWaitPopupVisibilityEvent.fire(this, false);
    }

    @Override
    public void showWaitPopup() {
        ChangeWaitPopupVisibilityEvent.fire(this, true);
    }

    //
    // EXPORTATION
    //

    @Override
    public void exportCategoryElements(List<String> urns) {

        dispatcher.execute(new ExportCategoryElementsAction(urns), new WaitingAsyncCallbackHandlingError<ExportCategoryElementsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CategoryElementsPresenter.this, caught);
            }

            @Override
            public void onWaitSuccess(ExportCategoryElementsResult result) {
                CommonUtils.downloadFile(result.getFileName());
            }
        });

    }

    @Override
    public void createCategoryElement(CategoryElementDto categoryElementDto) {

        dispatcher.execute(new SaveCategoryElementAction(categoryElementDto), new WaitingAsyncCallbackHandlingError<SaveCategoryElementResult>(this) {

            @Override
            public void onWaitSuccess(SaveCategoryElementResult result) {
                retrieveCategoryElements(ELEMENT_LIST_FIRST_RESULT, ELEMENT_LIST_MAX_RESULTS, null);
                fireSuccessMessage(MetamacSrmWeb.getMessages().categoryElementSaved());
            }
        });

    }

    @Override
    public void goToCategoryElement(String categoryElementCode) {
        if (!StringUtils.isBlank(categoryElementCode)) {
            placeManager.revealPlaceHierarchy(PlaceRequestUtils.buildAbsoluteCategoryElementPlaceRequest(categoryElementCode));
        }
    }

    //
    // NAVIGATION
    //

    @Override
    public void goTo(List<PlaceRequest> location) {
        if (location != null && !location.isEmpty()) {
            placeManager.revealPlaceHierarchy(location);
        }
    }
}
