package org.siemac.metamac.srm.web.category.presenter;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.Arrays;
import java.util.List;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.category.view.handlers.CategoryElementUiHandlers;
import org.siemac.metamac.srm.web.category.widgets.presenter.CategoriesToolStripPresenterWidget;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.enums.ToolStripButtonEnum;
import org.siemac.metamac.srm.web.client.events.SelectMenuButtonEvent;
import org.siemac.metamac.srm.web.client.presenter.MainPagePresenter;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.shared.category.DeleteCategoryElementLogoAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategoryElementLogoResult;
import org.siemac.metamac.srm.web.shared.category.DeleteCategoryElementsAction;
import org.siemac.metamac.srm.web.shared.category.DeleteCategoryElementsResult;
import org.siemac.metamac.srm.web.shared.category.GetCategoryElementAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoryElementLogoAction;
import org.siemac.metamac.srm.web.shared.category.GetCategoryElementLogoResult;
import org.siemac.metamac.srm.web.shared.category.GetCategoryElementResult;
import org.siemac.metamac.srm.web.shared.category.SaveCategoryElementAction;
import org.siemac.metamac.srm.web.shared.category.SaveCategoryElementResult;
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

public class CategoryElementPresenter extends Presenter<CategoryElementPresenter.CategoryElementView, CategoryElementPresenter.CategoryElementProxy> implements CategoryElementUiHandlers {

    private final DispatchAsync                dispatcher;
    private final PlaceManager                 placeManager;

    private CategoriesToolStripPresenterWidget categoriesToolStripPresenterWidget;

    private CategoryElementDto                 categoryElementDto;

    @TitleFunction
    public static String getTranslatedTitle(PlaceRequest placeRequest) {
        return PlaceRequestUtils.getCategoryElementBreadCrumbTitle(placeRequest);
    }

    @ProxyCodeSplit
    @NameToken(NameTokens.categoryElementPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface CategoryElementProxy extends Proxy<CategoryElementPresenter>, Place {
    }

    public interface CategoryElementView extends View, HasUiHandlers<CategoryElementUiHandlers> {

        void setCategoryElement(CategoryElementDto categoryElementDto);
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> TYPE_SetContextAreaContentcategory          = new Type<RevealContentHandler<?>>();

    public static final Object                        TYPE_SetContextAreaContentCategoriesToolBar = new Object();

    @Inject
    public CategoryElementPresenter(EventBus eventBus, CategoryElementView view, CategoryElementProxy proxy, DispatchAsync dispatcher, PlaceManager placeManager,
            CategoriesToolStripPresenterWidget categoriesToolStripPresenterWidget) {
        super(eventBus, view, proxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        this.categoriesToolStripPresenterWidget = categoriesToolStripPresenterWidget;
        getView().setUiHandlers(this);
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetContextAreaContent, this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        setInSlot(TYPE_SetContextAreaContentCategoriesToolBar, categoriesToolStripPresenterWidget);

        SetTitleEvent.fire(this, MetamacSrmWeb.getConstants().categoryElement());
        SelectMenuButtonEvent.fire(this, ToolStripButtonEnum.CATEGORIES);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String elementIdentifier = PlaceRequestUtils.getCategoryElementParamFromUrl(placeManager);
        if (!StringUtils.isBlank(elementIdentifier)) {
            retrieveCategoryElement(elementIdentifier);
        } else {
            MetamacSrmWeb.showErrorPage();
        }
    }

    private void retrieveCategoryElement(String elementIdentifier) {
        String urn = UrnUtils.generateUrn(UrnConstants.URN_SIEMAC_CLASS_CATEGORY_ELEMENT_PREFIX, elementIdentifier);
        if (!StringUtils.isBlank(urn)) {
            retrieveCategoryElementByUrn(urn);
        }
    }

    private void retrieveCategoryElementByUrn(String urn) {

        dispatcher.execute(new GetCategoryElementAction(urn), new WaitingAsyncCallbackHandlingError<GetCategoryElementResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CategoryElementPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetCategoryElementResult result) {
                CategoryElementPresenter.this.categoryElementDto = result.getCategoryElementDto();
                getView().setCategoryElement(result.getCategoryElementDto());
            }
        });

    }

    @Override
    public void saveCategoryElement(CategoryElementDto categoryElementDto) {

        dispatcher.execute(new SaveCategoryElementAction(categoryElementDto), new WaitingAsyncCallbackHandlingError<SaveCategoryElementResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CategoryElementPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(SaveCategoryElementResult result) {
                CategoryElementPresenter.this.categoryElementDto = result.getSavedCategoryElementDto();
                fireSuccessMessage(getMessages().categoryElementSaved());
                CategoryElementDto categoryElementDto = result.getSavedCategoryElementDto();
                getView().setCategoryElement(categoryElementDto);
                // Update URL
                PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeCategoryElementPlaceRequest(result.getSavedCategoryElementDto().getCode());
                placeManager.updateHistory(placeRequest, true);
            }
        });

    }

    @Override
    public void deleteCategoryElement(String urn) {

        dispatcher.execute(new DeleteCategoryElementsAction(Arrays.asList(urn)), new WaitingAsyncCallbackHandlingError<DeleteCategoryElementsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CategoryElementPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(DeleteCategoryElementsResult result) {
                fireSuccessMessage(MetamacSrmWeb.getMessages().categoryElementDeleted());
                placeManager.revealRelativePlace(-1);
            }
        });

    }

    @Override
    public void retrieveCategoryElementLogo(String categoryElementUrn) {

        dispatcher.execute(new GetCategoryElementLogoAction(categoryElementUrn), new WaitingAsyncCallbackHandlingError<GetCategoryElementLogoResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CategoryElementPresenter.this, caught);
            }

            @Override
            public void onWaitSuccess(GetCategoryElementLogoResult result) {
                CommonUtils.downloadFile(result.getFileName());
            }
        });

    }

    @Override
    public void deleteCategoryElementLogo(String categoryElementUrn) {

        dispatcher.execute(new DeleteCategoryElementLogoAction(categoryElementUrn), new WaitingAsyncCallbackHandlingError<DeleteCategoryElementLogoResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(CategoryElementPresenter.this, caught);
            }

            @Override
            public void onWaitSuccess(DeleteCategoryElementLogoResult result) {
                fireSuccessMessage(MetamacSrmWeb.getMessages().categoryElementLogoDeleted()); // TODO EDATOS-4513 PONER MENSAJE APROPIADO
            }
        });

    }

    //
    // LOGO IMPORTATION
    //

    @Override
    public void resourceImportationSucceed(String successMessage) {
        ShowMessageEvent.fireSuccessMessage(CategoryElementPresenter.this, successMessage);
        ChangeWaitPopupVisibilityEvent.fire(this, false);
    }

    @Override
    public void resourceImportationFailed(String errorMessage) {
        ShowMessageEvent.fireErrorMessage(CategoryElementPresenter.this, errorMessage);
        ChangeWaitPopupVisibilityEvent.fire(this, false);
    }

    @Override
    public void showWaitPopup() {
        ChangeWaitPopupVisibilityEvent.fire(this, true);
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
