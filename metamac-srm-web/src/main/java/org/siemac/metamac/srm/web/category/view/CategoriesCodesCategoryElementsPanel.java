package org.siemac.metamac.srm.web.category.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.srm.core.category.domain.shared.CategoryMetamacVisualisationResult;
import org.siemac.metamac.srm.core.category.domain.shared.CodeCategoryElementNormalisationResult;
import org.siemac.metamac.srm.core.category.dto.CategorySchemeMetamacDto;
import org.siemac.metamac.srm.web.category.utils.CategoriesClientSecurityUtils;
import org.siemac.metamac.srm.web.category.view.handlers.CategorySchemeUiHandlers;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.client.widgets.ConfirmationWindow;
import org.siemac.metamac.srm.web.shared.GetRelatedResourcesResult;
import org.siemac.metamac.web.common.client.utils.ApplicationEditionLanguages;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CategoriesCodesCategoryElementsPanel extends VLayout {

    private CategorySchemeUiHandlers                uiHandlers;

    private CategorySchemeMetamacDto                categorySchemeMetamacDto;

    protected ToolStrip                             toolStrip;
    protected ToolStripButton                       editButton;
    protected ToolStripButton                       saveButton;
    protected ToolStripButton                       cancelButton;
    protected ToolStripButton                       normaliseButton;

    private CodesCategoryElementsAssignmentTreeGrid codesCategoryElementsAssignmentTreeGrid;

    public CategoriesCodesCategoryElementsPanel() {
        setMargin(15);
        setMembersMargin(2);

        toolStrip = new ToolStrip();

        // Edit

        editButton = createEditButton();
        toolStrip.addButton(editButton);

        // Save

        saveButton = createSaveButton();
        toolStrip.addButton(saveButton);

        // Cancel

        cancelButton = createCancelButton();
        toolStrip.addButton(cancelButton);

        // Normalise

        normaliseButton = createNormaliseButton();
        toolStrip.addButton(normaliseButton);

        addMember(toolStrip);

        codesCategoryElementsAssignmentTreeGrid = new CodesCategoryElementsAssignmentTreeGrid();
        addMember(codesCategoryElementsAssignmentTreeGrid);
    }

    public void setCodes(CategorySchemeMetamacDto categoryScheme, List<CategoryMetamacVisualisationResult> codes) {
        this.categorySchemeMetamacDto = categoryScheme;
        codesCategoryElementsAssignmentTreeGrid.setItems(categoryScheme, codes);
        codesCategoryElementsAssignmentTreeGrid.setAutoFitMaxRecords(codes.size() + 1);
        setViewMode();
    }

    public void setCodesCategoryElementsNormalised(List<CodeCategoryElementNormalisationResult> codeCategoryElementNormalisationResults) {
        codesCategoryElementsAssignmentTreeGrid.setCodesCategoryElementsNormalised(categorySchemeMetamacDto, codeCategoryElementNormalisationResults);
    }

    public void setCategoryElementsForManualNormalisation(GetRelatedResourcesResult result) {
        codesCategoryElementsAssignmentTreeGrid.setCategoryElementsForManualNormalisation(result);
    }

    private void setViewMode() {
        showEditButton();
        saveButton.hide();
        cancelButton.hide();
        normaliseButton.hide();

        codesCategoryElementsAssignmentTreeGrid.setViewMode();
    }

    private void setEditionMode() {
        editButton.hide();
        saveButton.show();
        cancelButton.show();
        normaliseButton.show();

        codesCategoryElementsAssignmentTreeGrid.setEditionMode();
    }

    private void showEditButton() {
        if (CategoriesClientSecurityUtils.canUpdateCategoryCategoryElement(categorySchemeMetamacDto)) {
            editButton.show();
        } else {
            editButton.hide();
        }
    }

    public void updateItemScheme(CategorySchemeMetamacDto categorySchemeMetamacDto) {
        this.categorySchemeMetamacDto = categorySchemeMetamacDto;
        codesCategoryElementsAssignmentTreeGrid.updateItemScheme(categorySchemeMetamacDto);
    }

    public CategorySchemeUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    public void setUiHandlers(CategorySchemeUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
        codesCategoryElementsAssignmentTreeGrid.setUiHandlers(uiHandlers);
    }

    //
    // TOOLSTRIP BUTTONS
    //

    private ToolStripButton createEditButton() {
        ToolStripButton editButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionEdit(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.editListGrid().getURL());
        editButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                setEditionMode();
            }
        });
        return editButton;
    }

    private ToolStripButton createSaveButton() {
        ToolStripButton saveButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionSave(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.saveListGrid().getURL());
        saveButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().updateCodesCategoryElements(categorySchemeMetamacDto.getUrn(), codesCategoryElementsAssignmentTreeGrid.getCodesCategoryElements());
            }
        });
        return saveButton;
    }

    private ToolStripButton createCancelButton() {
        ToolStripButton cancelButton = new ToolStripButton(MetamacSrmWeb.getConstants().actionCancel(),
                org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.cancelListGrid().getURL());
        cancelButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // It is necessary to reload the codes to undo the changes that the user may have done
                getUiHandlers().retrieveCategories();
            }
        });
        return cancelButton;
    }

    private ToolStripButton createNormaliseButton() {
        ToolStripButton normaliseButton = new ToolStripButton(getConstants().actionCategoriesNormalise(), GlobalResources.RESOURCE.fit().getURL());
        normaliseButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ConfirmationWindow versionCategorySchemeWindow = new ConfirmationWindow(getConstants().categoriesCategoryElementAssignment(),
                        getConstants().categoriesCategoryElementAssignmentMessage());

                versionCategorySchemeWindow.getYesButton().addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        // Normalise all the codes
                        getUiHandlers().normaliseCategoryElementsToCodes(categorySchemeMetamacDto.getUrn(), ApplicationEditionLanguages.getCurrentLocale(), false);
                    }
                });
                versionCategorySchemeWindow.getNoButton().addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        // Only normalise the codes without category element
                        getUiHandlers().normaliseCategoryElementsToCodes(categorySchemeMetamacDto.getUrn(), ApplicationEditionLanguages.getCurrentLocale(), true);
                    }
                });
            }
        });
        return normaliseButton;
    }
}
