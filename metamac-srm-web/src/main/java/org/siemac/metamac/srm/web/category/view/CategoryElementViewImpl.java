package org.siemac.metamac.srm.web.category.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.category.dto.CategoryElementDto;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.category.presenter.CategoryElementPresenter;
import org.siemac.metamac.srm.web.category.view.handlers.CategoryElementUiHandlers;
import org.siemac.metamac.srm.web.category.widgets.CategoryElementMainFormLayout;
import org.siemac.metamac.srm.web.category.widgets.ImportCategoryElementLogoWindow;
import org.siemac.metamac.srm.web.resource.element.model.ds.ResourceElementDS;
import org.siemac.metamac.web.common.client.listener.UploadListener;
import org.siemac.metamac.web.common.client.utils.DateUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDateItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageRichTextEditorItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.SearchViewTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.layout.VLayout;

public class CategoryElementViewImpl extends ViewWithUiHandlers<CategoryElementUiHandlers> implements CategoryElementPresenter.CategoryElementView {

    private VLayout                               panel;
    private CategoryElementMainFormLayout         mainFormLayout;

    // View forms
    private GroupDynamicForm                      identifiersForm;
    private GroupDynamicForm                      diffusionDescriptorsForm;
    private GroupDynamicForm                      annotationsForm;

    // Edition forms
    private GroupDynamicForm                      identifiersEditionForm;
    private GroupDynamicForm                      diffusionDescriptorsEditionForm;
    private GroupDynamicForm                      annotationsEditionForm;

    private CategoryElementDto                    categoryElementDto;
    private final ImportCategoryElementLogoWindow importCategoryElementLogoWindow;
    SearchViewTextItem                            importLogoButton;

    @Inject
    public CategoryElementViewImpl() {
        super();
        panel = new VLayout();
        panel.setHeight100();

        //
        // CATEGORY ELEMENT
        //

        mainFormLayout = new CategoryElementMainFormLayout();
        importCategoryElementLogoWindow = createImportVariableElementLogoWindow();
        bindMainFormLayoutEvents();
        createViewForm();
        createEditionForm();

        VLayout subPanel = new VLayout();
        subPanel.setOverflow(Overflow.SCROLL);
        subPanel.addMember(mainFormLayout);

        panel.addMember(subPanel);
    }

    @Override
    public void setUiHandlers(CategoryElementUiHandlers uiHandlers) {
        super.setUiHandlers(uiHandlers);
        importCategoryElementLogoWindow.setUiHandlers(uiHandlers);
    }

    private void bindMainFormLayoutEvents() {
        mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                identifiersForm.setTranslationsShowed(translationsShowed);
                identifiersEditionForm.setTranslationsShowed(translationsShowed);

                diffusionDescriptorsForm.setTranslationsShowed(translationsShowed);
                diffusionDescriptorsEditionForm.setTranslationsShowed(translationsShowed);

                annotationsForm.setTranslationsShowed(translationsShowed);
                annotationsEditionForm.setTranslationsShowed(translationsShowed);
            }
        });

        // Save
        mainFormLayout.getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (identifiersEditionForm.validate(false) && diffusionDescriptorsEditionForm.validate(false) && annotationsEditionForm.validate(false)) {
                    getCategoryElementDto();
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                        @Override
                        public void execute() {
                            getCategoryElementDto();
                            Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                                @Override
                                public void execute() {
                                    getUiHandlers().saveCategoryElement(getCategoryElementDto());
                                }
                            });
                        }
                    });

                }
            }
        });

        // Delete
        mainFormLayout.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteCategoryElement(categoryElementDto.getUrn());
            }
        });
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    @Override
    public void setInSlot(Object slot, Widget content) {
        if (slot == CategoryElementPresenter.TYPE_SetContextAreaContentCategoriesToolBar) {
            if (content != null) {
                panel.addMember(content, 0);
            }
        } else {
            // To support inheritance in your views it is good practice to call super.setInSlot when you can't handle the call.
            // Who knows, maybe the parent class knows what to do with this slot.
            super.setInSlot(slot, content);
        }
    }

    @Override
    public void setCategoryElement(CategoryElementDto categoryElementDto) {
        this.categoryElementDto = categoryElementDto;

        String defaultLocalized = InternationalStringUtils.getLocalisedString(categoryElementDto.getShortName());
        String title = defaultLocalized != null ? defaultLocalized : StringUtils.EMPTY;
        mainFormLayout.setTitleLabelContents(title);

        mainFormLayout.setViewMode();

        setCategoryElementViewMode(categoryElementDto);
        setCategoryElementEditionMode(categoryElementDto);

    }

    private void createViewForm() {
        // Identifiers Form
        identifiersForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(ResourceElementDS.CODE, getConstants().identifiableArtefactCode());
        ViewMultiLanguageTextItem shortName = new ViewMultiLanguageTextItem(ResourceElementDS.SHORT_NAME, getConstants().categoryElementShortName());
        ViewTextItem urn = new ViewTextItem(ResourceElementDS.URN, getConstants().identifiableArtefactUrn());
        identifiersForm.setFields(code, shortName, urn);

        // Diffusion descriptors
        diffusionDescriptorsForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        ViewTextItem validFrom = new ViewTextItem(ResourceElementDS.VALID_FROM, getConstants().categoryElementValidFrom());
        ViewTextItem validTo = new ViewTextItem(ResourceElementDS.VALID_TO, getConstants().categoryElementValidTo());
        diffusionDescriptorsForm.setFields(validFrom, validTo);
        // Annotations
        annotationsForm = new GroupDynamicForm(getConstants().formAnnotations());
        ViewMultiLanguageTextItem comments = new ViewMultiLanguageTextItem(ResourceElementDS.COMMENTS, getConstants().categoryElementComment());
        annotationsForm.setFields(comments);

        mainFormLayout.addViewCanvas(identifiersForm);
        mainFormLayout.addViewCanvas(diffusionDescriptorsForm);
        mainFormLayout.addViewCanvas(annotationsForm);
    }

    private void createEditionForm() {
        // Identifiers
        identifiersEditionForm = new GroupDynamicForm(getConstants().formIdentifiers());
        ViewTextItem code = new ViewTextItem(ResourceElementDS.CODE, getConstants().identifiableArtefactCode());
        MultiLanguageTextItem shortName = new MultiLanguageTextItem(ResourceElementDS.SHORT_NAME, getConstants().categoryElementShortName(), SrmConstants.METADATA_SHORT_NAME_MAXIMUM_LENGTH);
        shortName.setRequired(true);
        ViewTextItem urn = new ViewTextItem(ResourceElementDS.URN, getConstants().identifiableArtefactUrn());
        identifiersEditionForm.setFields(code, shortName, urn);

        // Diffusion descriptors
        diffusionDescriptorsEditionForm = new GroupDynamicForm(getConstants().formDiffusionDescriptors());
        CustomDateItem validFrom = new CustomDateItem(ResourceElementDS.VALID_FROM, getConstants().categoryElementValidFrom());
        CustomDateItem validTo = new CustomDateItem(ResourceElementDS.VALID_TO, getConstants().categoryElementValidTo());
        importLogoButton = loadLogo();

        SearchViewTextItem exportLogoButton = createGetCategoryElementLogoButton();

        diffusionDescriptorsEditionForm.setFields(validFrom, validTo, importLogoButton, exportLogoButton);

        // Annotations
        annotationsEditionForm = new GroupDynamicForm(getConstants().formAnnotations());
        MultiLanguageRichTextEditorItem comments = new MultiLanguageRichTextEditorItem(ResourceElementDS.COMMENTS, getConstants().categoryElementComment());
        annotationsEditionForm.setFields(comments);

        mainFormLayout.addEditionCanvas(identifiersEditionForm);
        mainFormLayout.addEditionCanvas(diffusionDescriptorsEditionForm);
        mainFormLayout.addEditionCanvas(annotationsEditionForm);
    }

    public void setEditionMode() {
        mainFormLayout.setEditionMode();
    }

    public void setCategoryElementViewMode(CategoryElementDto categoryElementDto) {
        // Identifiers
        identifiersForm.setValue(ResourceElementDS.CODE, categoryElementDto.getCode());
        identifiersForm.setValue(ResourceElementDS.URN, categoryElementDto.getUrn());
        identifiersForm.setValue(ResourceElementDS.SHORT_NAME, categoryElementDto.getShortName());

        // Diffusion descriptors
        diffusionDescriptorsForm.setValue(ResourceElementDS.VALID_FROM, DateUtils.getFormattedDate(categoryElementDto.getValidFrom()));
        diffusionDescriptorsForm.setValue(ResourceElementDS.VALID_TO, DateUtils.getFormattedDate(categoryElementDto.getValidTo()));

        // Annotations
        annotationsForm.setValue(ResourceElementDS.COMMENTS, categoryElementDto.getComment());
    }

    public void setCategoryElementEditionMode(CategoryElementDto categoryElementDto) {
        // Identifiers
        identifiersEditionForm.setValue(ResourceElementDS.CODE, categoryElementDto.getCode());
        identifiersEditionForm.setValue(ResourceElementDS.URN, categoryElementDto.getUrn());
        identifiersEditionForm.setValue(ResourceElementDS.SHORT_NAME, categoryElementDto.getShortName());

        // Diffusion descriptors
        diffusionDescriptorsEditionForm.setValue(ResourceElementDS.VALID_FROM, categoryElementDto.getValidFrom());
        diffusionDescriptorsEditionForm.setValue(ResourceElementDS.VALID_TO, categoryElementDto.getValidTo());

        // Annotations
        annotationsEditionForm.setValue(ResourceElementDS.COMMENTS, categoryElementDto.getComment());
    }

    private CategoryElementDto getCategoryElementDto() {
        // Identifiers
        categoryElementDto.setShortName(identifiersEditionForm.getValueAsInternationalStringDto(ResourceElementDS.SHORT_NAME));

        // Diffusion descriptors
        categoryElementDto.setValidFrom(((CustomDateItem) diffusionDescriptorsEditionForm.getItem(ResourceElementDS.VALID_FROM)).getValueAsDate());
        categoryElementDto.setValidTo(((CustomDateItem) diffusionDescriptorsEditionForm.getItem(ResourceElementDS.VALID_TO)).getValueAsDate());

        // Annotations
        categoryElementDto.setComment(annotationsEditionForm.getValueAsInternationalStringDto(ResourceElementDS.COMMENTS));

        return categoryElementDto;
    }

    private ImportCategoryElementLogoWindow createImportVariableElementLogoWindow() {
        ImportCategoryElementLogoWindow importLogoWindow = new ImportCategoryElementLogoWindow();
        importLogoWindow.setUploadListener(new UploadListener() {

            @Override
            public void uploadFailed(String errorMessage) {
                getUiHandlers().resourceImportationFailed(errorMessage);
            }

            @Override
            public void uploadComplete(String fileName) {
                diffusionDescriptorsEditionForm.setValue(ResourceElementDS.LOGO, importCategoryElementLogoWindow.getFileName());
                getUiHandlers().resourceImportationSucceed(fileName);
            }

        });

        return importLogoWindow;
    }

    private SearchViewTextItem loadLogo() {
        SearchViewTextItem createLogoButton = new SearchViewTextItem(ResourceElementDS.LOGO, getConstants().logo());
        createLogoButton.setRequired(true);

        createLogoButton.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                importCategoryElementLogoWindow.show();
            }
        });

        return createLogoButton;
    }

    private SearchViewTextItem createGetCategoryElementLogoButton() {

        SearchViewTextItem exportLogoButton = new SearchViewTextItem(ResourceElementDS.LOGO_EXPORT, getConstants().download());
        exportLogoButton.setRequired(true);

        exportLogoButton.getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

            @Override
            public void onFormItemClick(FormItemIconClickEvent event) {
                getUiHandlers().retrieveCategoryElementLogo(categoryElementDto.getUrn());
            }
        });

        return exportLogoButton;
    }
}
