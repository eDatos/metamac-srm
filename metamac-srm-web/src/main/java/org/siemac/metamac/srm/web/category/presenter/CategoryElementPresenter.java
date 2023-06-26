package org.siemac.metamac.srm.web.category.presenter;

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
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
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

        // TODO EDATOS-4150 VER SEGRACIÓN
        // void setResourceElementsForSegregation(GetVariableElementsResult result);
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
        String elementIdentifier = PlaceRequestUtils.getVariableElementParamFromUrl(placeManager);
        if (!StringUtils.isBlank(elementIdentifier)) {
            retrieveCategoryElement(elementIdentifier);
        } else {
            MetamacSrmWeb.showErrorPage();
        }
    }

    private void retrieveCategoryElement(String elementIdentifier) {
        String urn = UrnUtils.generateUrn(UrnConstants.URN_SIEMAC_CLASS_CATEGORY_ELEMENT_PREFIX, elementIdentifier);
        if (!StringUtils.isBlank(urn)) {
            retrieveResourceElementByUrn(urn);
            retrieveCategoryElementOperations(urn);
        }
    }

    private void retrieveResourceElementByUrn(String urn) {
        /*
         * dispatcher.execute(new GetCategoryElementAction(urn), new WaitingAsyncCallbackHandlingError<GetCategoryElementResult>(this) {
         * @Override
         * public void onWaitFailure(Throwable caught) {
         * ShowMessageEvent.fireErrorMessage(CategoryElementPresenter.this, caught);
         * }
         * @Override
         * public void onWaitSuccess(GetCategoryElementResult result) {
         * // TODO EDATOS-4150 RELLENAR
         * // CategoryElementPresenter.this.resourceElementDto = result.getResourceElementDto();
         * // getView().setResourceElement(result.getResourceElementDto());
         * }
         * });
         */
    }

    @Override
    public void saveCategoryElement(CategoryElementDto categoryElementDto) {
        /*
         * dispatcher.execute(new SaveCategoryElementAction(categoryElementDto), new WaitingAsyncCallbackHandlingError<SaveCategoryElementResult>(this) {
         * @Override
         * public void onWaitFailure(Throwable caught) {
         * ShowMessageEvent.fireErrorMessage(CategoryElementPresenter.this, caught);
         * }
         * @Override
         * public void onWaitSuccess(SaveCategoryElementResult result) {
         * CategoryElementPresenter.this.categoryElementDto = result.getSavedCategoryElementDto();
         * fireSuccessMessage(getMessages().variableElementSaved());
         * CategoryElementDto categoryElementDto = result.getSavedCategoryElementDto();
         * getView().setCategoryElement(categoryElementDto);
         * // Update URL
         * PlaceRequest placeRequest = PlaceRequestUtils.buildRelativeCategoryElementPlaceRequest(result.getSavedCategoryElementDto().getCode());
         * placeManager.updateHistory(placeRequest, true);
         * }
         * });
         */
    }

    @Override
    public void deleteCategoryElement(String urn) {
        /*
         * dispatcher.execute(new DeleteCategoryElementsAction(Arrays.asList(urn)), new WaitingAsyncCallbackHandlingError<DeleteCategoryElementsResult>(this) {
         * @Override
         * public void onWaitFailure(Throwable caught) {
         * ShowMessageEvent.fireErrorMessage(CategoryElementPresenter.this, caught);
         * }
         * @Override
         * public void onWaitSuccess(DeleteCategoryElementsResult result) {
         * fireSuccessMessage(MetamacSrmWeb.getMessages().categoryElementDeleted());
         * placeManager.revealRelativePlace(-1);
         * }
         * });
         */
    }

    @Override
    public void createSegregation(String categoryElementUrn, List<String> categoryElementUrns) {
        /*
         * dispatcher.execute(new CreateCategoryElementOperationAction(VariableElementOperationTypeEnum.SEGREGATION, categoryElementUrns, categoryElementUrn),
         * new WaitingAsyncCallbackHandlingError<CreateCategoryElementOperationResult>(this) {
         * @Override
         * public void onWaitFailure(Throwable caught) {
         * ShowMessageEvent.fireErrorMessage(CategoryElementPresenter.this, caught);
         * }
         * @Override
         * public void onWaitSuccess(CreateCategoryElementOperationResult result) {
         * fireSuccessMessage(MetamacSrmWeb.getMessages().categoryElementSegregated());
         * retrieveCategoryElementOperations(categoryElementDto.getUrn());
         * }
         * });
         */
    }

    @Override
    public void createFusion(List<String> variableElementUrn, String variableElementUrns) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteCategoryElementOperations(List<String> codes) {
        /*
         * dispatcher.execute(new DeleteCategoryElementOperationsAction(codes), new WaitingAsyncCallbackHandlingError<DeleteCategoryElementOperationsResult>(this) {
         * @Override
         * public void onWaitFailure(Throwable caught) {
         * ShowMessageEvent.fireErrorMessage(CategoryElementPresenter.this, caught);
         * }
         * @Override
         * public void onWaitSuccess(DeleteCategoryElementOperationsResult result) {
         * retrieveCategoryElementOperations(categoryElementDto.getUrn());
         * }
         * });
         */
    }

    private void retrieveCategoryElementOperations(String categoryElementUrn) {
        /*
         * dispatcher.execute(new GetCategoryElementOperationsByCategoryElementAction(categoryElementUrn),
         * new WaitingAsyncCallbackHandlingError<GetCategoryElementOperationsByVariableElementResult>(this) {
         * @Override
         * public void onWaitFailure(Throwable caught) {
         * ShowMessageEvent.fireErrorMessage(CategoryElementPresenter.this, caught);
         * }
         * @Override
         * public void onWaitSuccess(GetCategoryElementOperationsByVariableElementResult result) {
         * getView().setCategoryElementOperations(result.getOperations());
         * }
         * });
         */
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
