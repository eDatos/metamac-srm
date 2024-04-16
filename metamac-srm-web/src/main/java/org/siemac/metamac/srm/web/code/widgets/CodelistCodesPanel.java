package org.siemac.metamac.srm.web.code.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.List;

import org.siemac.metamac.core.common.util.shared.VersionUtil;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.dto.CodelistMetamacDto;
import org.siemac.metamac.srm.web.category.widgets.ImportAnnotationsWindow;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.code.utils.CodesClientSecurityUtils;
import org.siemac.metamac.srm.web.code.view.handlers.CodelistUiHandlers;
import org.siemac.metamac.srm.web.shared.code.GetCodelistsResult;
import org.siemac.metamac.web.common.client.listener.UploadListener;
import org.siemac.metamac.web.common.client.widgets.CustomToolStripButton;

import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class CodelistCodesPanel extends VLayout {

    protected ToolStrip        toolStrip;
    protected ToolStripButton  importCodesButton;
    protected ToolStripButton  exportCodesButton;
    protected ToolStripButton  importAnnotationsButton;
    protected ToolStripButton  exportAnnotationsButton;

    private ImportCodesWindow  importCodesWindow;
    private ImportAnnotationsWindow importAnnotationsWindows;

    private CodesTreeGrid      codesTreeGrid;

    private CodelistUiHandlers uiHandlers;

    private CodelistMetamacDto codelistMetamacDto;

    public CodelistCodesPanel() {
        setMargin(15);
        setMembersMargin(2);

        importCodesWindow = new ImportCodesWindow();
        importCodesWindow.setUploadListener(new UploadListener() {

            @Override
            public void uploadFailed(String errorMessage) {
                uiHandlers.resourceImportationFailed(errorMessage);
            }
            @Override
            public void uploadComplete(String fileName) {
                uiHandlers.resourceImportationSucceed(fileName);
            }
        });
        importAnnotationsWindows = new ImportAnnotationsWindow(getConstants().actionImportAnnotationsTsv());
        importAnnotationsWindows.setUploadListener(new UploadListener() {

            @Override
            public void uploadFailed(String errorMessage) {
                uiHandlers.resourceImportationFailed(errorMessage);
            }
            @Override
            public void uploadComplete(String fileName) {
                uiHandlers.resourceImportationSucceed(fileName);
            }
        });
        toolStrip = new ToolStrip();

        importCodesButton = new CustomToolStripButton(getConstants().actionImportCodesTsv(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.importResource().getURL());
        importCodesButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                importCodesWindow.initImportWindow(CommonUtils.isDefaultMaintainer(codelistMetamacDto.getMaintainer()), VersionUtil.isTemporalVersion(codelistMetamacDto.getVersionLogic()));
                importCodesWindow.show();
            }
        });
        toolStrip.addButton(importCodesButton);

        exportCodesButton = new CustomToolStripButton(getConstants().actionExportCodesTsv(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.exportResource().getURL());
        exportCodesButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.exportCodes(codelistMetamacDto.getUrn());
            }
        });
        toolStrip.addButton(exportCodesButton);

        importAnnotationsButton = new CustomToolStripButton(getConstants().actionImportAnnotationsTsv(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.importResource().getURL());
        importAnnotationsButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                importAnnotationsWindows.initImportWindow(CommonUtils.isDefaultMaintainer(codelistMetamacDto.getMaintainer()), VersionUtil.isTemporalVersion(codelistMetamacDto.getVersionLogic()));
                importAnnotationsWindows.show();
            }});
        toolStrip.addButton(importAnnotationsButton);

        exportAnnotationsButton = new CustomToolStripButton(getConstants().actionExportAnnotationsTsv(), org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.exportResource().getURL());
        exportAnnotationsButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                uiHandlers.exportAnnotations(codelistMetamacDto.getUrn());
            }
        });
        toolStrip.addButton(exportAnnotationsButton);


        addMember(toolStrip);

        codesTreeGrid = new CodesTreeGrid();
        addMember(codesTreeGrid);
    }

    public void setUiHandlers(CodelistUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
        codesTreeGrid.setUiHandlers(uiHandlers);
        importCodesWindow.setUiHandlers(uiHandlers);
        importAnnotationsWindows.setUiHandlers(uiHandlers);
    }

    public void updateItemScheme(CodelistMetamacDto codelistMetamacDto) {
        this.codelistMetamacDto = codelistMetamacDto;

        codesTreeGrid.updateItemScheme(codelistMetamacDto);

        updateButtonsVisibility(codelistMetamacDto);
        importCodesWindow.setCodelist(codelistMetamacDto);
        importAnnotationsWindows.setCodelist(codelistMetamacDto);
    }

    public void setCodes(CodelistMetamacDto codelist, List<CodeMetamacVisualisationResult> codes) {
        this.codelistMetamacDto = codelist;
        codesTreeGrid.setItems(codelist, codes);
        codesTreeGrid.setAutoFitMaxRecords(codes.size() + 1);
    }

    public void setCodelistsToCreateComplexCodelist(GetCodelistsResult result) {
        codesTreeGrid.setCodelistsToCreateComplexCodelist(result);
    }

    public void setCodesToCreateComplexCodelist(CodelistMetamacDto codelistMetamacDto, List<CodeMetamacVisualisationResult> codes) {
        codesTreeGrid.setCodesToCreateComplexCodelist(codelistMetamacDto, codes);
    }

    private void updateButtonsVisibility(CodelistMetamacDto codelistMetamacDto) {
        updateImportCodesButtonVisibility(codelistMetamacDto);
        updateImportAnnotationsButtonVisibility(codelistMetamacDto);
        updateExportCodesButtonVisibility(codelistMetamacDto);
        updateExportAnnotationsButtonVisibility(codelistMetamacDto);
    }

    private void updateImportCodesButtonVisibility(CodelistMetamacDto codelistMetamacDto) {
        if (CodesClientSecurityUtils.canImportCodes(codelistMetamacDto)) {
            importCodesButton.show();
        } else {
            importCodesButton.hide();
        }
    }

    private void updateImportAnnotationsButtonVisibility(CodelistMetamacDto codelistMetamacDto) {
        if (CodesClientSecurityUtils.canImportCodes(codelistMetamacDto)) {
            importAnnotationsButton.show();
        } else {
            importAnnotationsButton.hide();
        }
    }

    private void updateExportCodesButtonVisibility(CodelistMetamacDto codelistMetamacDto) {
        if (CodesClientSecurityUtils.canExportCodes(codelistMetamacDto)) {
            exportCodesButton.show();
        } else {
            exportCodesButton.hide();
        }
    }

    private void updateExportAnnotationsButtonVisibility(CodelistMetamacDto codelistMetamacDto) {
        if (CodesClientSecurityUtils.canExportCodes(codelistMetamacDto)) {
            exportAnnotationsButton.show();
        } else {
            exportAnnotationsButton.hide();
        }
    }
}
