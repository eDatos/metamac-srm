package org.siemac.metamac.srm.web.client.admin.view;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.web.client.admin.AdminAnnotationTypeTabPresenter.AdminAnnotationTypeTabView;
import org.siemac.metamac.srm.web.client.admin.utils.AdminClientSecurityUtils;
import org.siemac.metamac.srm.web.client.admin.view.handlers.AdminAnnotationTypeUiHandlers;
import org.siemac.metamac.srm.web.client.constants.SrmWebConstants;
import org.siemac.metamac.srm.web.client.model.ds.AnnotationTypeDS;
import org.siemac.metamac.srm.web.client.model.record.AnnotationTypeRecord;
import org.siemac.metamac.srm.web.client.utils.RecordUtils;
import org.siemac.metamac.srm.web.shared.criteria.AnnotationTypeCriteria;
import org.siemac.metamac.web.common.client.widgets.ListGridToolStrip;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomIntegerItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Autofit;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;

public class AdminAnnotationTypeTabViewImpl extends ViewWithUiHandlers<AdminAnnotationTypeUiHandlers> implements AdminAnnotationTypeTabView {

    private Layout                 panel;

    private PaginatedCheckListGrid listGrid;

    private ListGridToolStrip      toolStrip;

    private UnitMultiplierPanel    unitMultiplierPanel;

    public AdminAnnotationTypeTabViewImpl() {
        super();

        // ToolStrip

        toolStrip = new ListGridToolStrip(getMessages().codelistOrderAlphabeticalInfoViewTitle(), getMessages().codelistOrderAlphabeticalInfoDeleteTitle());
        toolStrip.getNewButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                selectAnnotationType(new AnnotationTypeDto());
                unitMultiplierPanel.setEditionMode();
            }

        });
        toolStrip.getNewButton().setVisibility(AdminClientSecurityUtils.canCreateAnnotationType() ? Visibility.VISIBLE : Visibility.HIDDEN);

        toolStrip.getDeleteHandlerRegistration().removeHandler();
        toolStrip.getDeleteButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                toolStrip.getDeleteConfirmationWindow().show();
            }
        });

        toolStrip.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getUiHandlers().deleteAnnotationTypes(getSelectedAnnotationTypes());
            }
        });

        // ListGrid

        listGrid = new PaginatedCheckListGrid(SrmWebConstants.MAIN_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                AnnotationTypeCriteria criteria = new AnnotationTypeCriteria();
                criteria.setFirstResult(firstResult); // TODO-EDATOS-4082 ver si añadir
                getUiHandlers().retrieveAnnotationType(criteria);
            }
        });

        listGrid.getListGrid().setAutoFitData(Autofit.VERTICAL);
        listGrid.getListGrid().setAutoFitMaxRecords(SrmWebConstants.MAIN_LIST_MAX_RESULTS);
        ListGridField uuidField = new ListGridField(AnnotationTypeDS.UUID, "Code"); // TODO-EDATOS-4082 PONER CONSTANTE
        ListGridField titleField = new ListGridField(AnnotationTypeDS.TITLE, "IndicatorsWeb.getConstants().unitMultiplierTitle()"); // TODO-EDATOS-4082 PONER CONSTANTE
        ListGridField multiplierField = new ListGridField(AnnotationTypeDS.MULTIPLIER, "IndicatorsWeb.getConstants().unitMultiplierMultiplier()"); // TODO-EDATOS-4082 PONER CONSTANTE
        listGrid.getListGrid().setFields(uuidField, titleField, multiplierField);
        // Show data source details when record clicked
        listGrid.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (listGrid.getListGrid().getSelectedRecords() != null && listGrid.getListGrid().getSelectedRecords().length == 1) {
                    AnnotationTypeRecord record = (AnnotationTypeRecord) listGrid.getListGrid().getSelectedRecord();
                    AnnotationTypeDto selected = record.getDto();
                    selectAnnotationType(selected);
                } else {
                    // No record selected
                    deselectQuantityUnit();
                    if (listGrid.getListGrid().getSelectedRecords().length > 1) {
                        // Delete more than one dimension with one click
                        showToolStripDeleteButton();
                    }
                }
            }
        });

        unitMultiplierPanel = new UnitMultiplierPanel();
        unitMultiplierPanel.hide();

        VLayout listPanel = new VLayout();
        listPanel.addMember(toolStrip);
        listPanel.addMember(listGrid);

        HLayout leftLayout = new HLayout();
        leftLayout.setWidth("50%");
        leftLayout.addMember(listPanel);

        HLayout rightLayout = new HLayout();
        rightLayout.setWidth("50%");
        rightLayout.addMember(unitMultiplierPanel);

        panel = new HLayout();
        panel.setMargin(15);
        panel.setMembersMargin(5);
        panel.addMember(leftLayout);
        panel.addMember(rightLayout);

    }

    // UTILS
    private List<String> getSelectedAnnotationTypes() {
        List<String> codes = new ArrayList<String>();
        for (ListGridRecord record : listGrid.getListGrid().getSelectedRecords()) {
            codes.add(record.getAttribute(AnnotationTypeDS.UUID));
        }
        return codes;
    }

    private void showToolStripDeleteButton() {
        if (AdminClientSecurityUtils.canDeleteAnnotationType()) {
            toolStrip.getDeleteButton().show();
        }
    }

    private void selectAnnotationType(AnnotationTypeDto dto) {
        if (dto.getUuid() != null) {
            showToolStripDeleteButton();
        } else {
            toolStrip.getDeleteButton().hide();
            listGrid.getListGrid().deselectAllRecords();
        }

        unitMultiplierPanel.setAnnotationTypeDto(dto);
    }

    private void deselectQuantityUnit() {
        toolStrip.getDeleteButton().hide();

        unitMultiplierPanel.hide();
    }

    // DATA

    @Override
    public void setAnnotationTypes(int firstResult, List<AnnotationTypeDto> dtos, int totalResults) {
        unitMultiplierPanel.hide();

        AnnotationTypeRecord[] records = new AnnotationTypeRecord[dtos.size()];
        int index = 0;
        for (AnnotationTypeDto ds : dtos) {
            records[index++] = RecordUtils.getAnnotationTypeRecord(ds);
        }
        listGrid.getListGrid().setData(records);
        listGrid.refreshPaginationInfo(firstResult, dtos.size(), totalResults);
    }

    @Override
    public void onAnnotationTypeCreated(AnnotationTypeDto dto) {
        selectAnnotationType(dto);
    }

    @Override
    public void onAnnotationUpdated(AnnotationTypeDto dto) {
        selectAnnotationType(dto);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    public class UnitMultiplierPanel extends VLayout {

        private InternationalMainFormLayout mainFormLayout;

        private GroupDynamicForm            generalEditionForm;
        private GroupDynamicForm            generalForm;

        private AnnotationTypeDto           annotationTypeDto;

        public UnitMultiplierPanel() {
            mainFormLayout = new InternationalMainFormLayout(AdminClientSecurityUtils.canUpdateAnnotationType());
            mainFormLayout.setCanDelete(AdminClientSecurityUtils.canDeleteAnnotationType());
            mainFormLayout.setTitleLabelContents(getConstants().actionExportCategoriesTsv()); // TODO EDATOS-4082 cambiar mensaje

            // Edit: Add a custom handler to check indicator status before start editing
            mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    mainFormLayout.setEditionMode();
                }
            });

            mainFormLayout.getSave().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (generalEditionForm.validate(false)) {
                        getUiHandlers().saveAnnotationType(listGrid.getFirstResult(), getAnnotationTypeDto());
                    }
                }
            });

            mainFormLayout.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    List<String> uuids = new ArrayList<String>();
                    uuids.add(annotationTypeDto.getUuid());
                    getUiHandlers().deleteAnnotationTypes(uuids);
                }
            });

            mainFormLayout.getCancelToolStripButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (annotationTypeDto.getUuid() == null || (annotationTypeDto.getUuid() != null && annotationTypeDto.getUuid().isEmpty())) {
                        hide();
                    }
                }
            });

            mainFormLayout.getTranslateToolStripButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    boolean translationsShowed = mainFormLayout.getTranslateToolStripButton().isSelected();
                    generalForm.setTranslationsShowed(translationsShowed);
                    generalEditionForm.setTranslationsShowed(translationsShowed);
                }
            });

            mainFormLayout.setStyleName("mainFormSide");

            createViewForm();
            createEditionForm();

            addMember(mainFormLayout);
        }

        public void setEditionMode() {
            mainFormLayout.setEditionMode();
        }

        private void createViewForm() {
            generalForm = new GroupDynamicForm(getConstants().actionExportCategoriesTsv()); // TODO EDATOS-4082 cambiar mensaje

            ViewTextItem uuid = new ViewTextItem(AnnotationTypeDS.UUID, getConstants().actionEdit()); // TODO EDATOS-4082 cambiar mensaje
            ViewMultiLanguageTextItem title = new ViewMultiLanguageTextItem(AnnotationTypeDS.TITLE, getConstants().appTitle()); // TODO EDATOS-4082 cambiar mensaje
            ViewTextItem multiplier = new ViewTextItem(AnnotationTypeDS.MULTIPLIER, getConstants().actionExportCategoriesTsv()); // TODO EDATOS-4082 cambiar mensaje

            generalForm.setFields(uuid, title, multiplier);

            mainFormLayout.addViewCanvas(generalForm);
        }

        private void createEditionForm() {
            generalEditionForm = new GroupDynamicForm(getConstants().actionExportCategoriesTsv());// TODO EDATOS-4082 cambiar mensaje

            ViewTextItem uuid = new ViewTextItem(AnnotationTypeDS.UUID, getConstants().actionEdit()); // TODO EDATOS-4082 cambiar mensaje
            MultiLanguageTextItem title = new MultiLanguageTextItem(AnnotationTypeDS.TITLE, getConstants().appTitle()); // TODO EDATOS-4082 cambiar mensaje
            title.setRequired(true);
            CustomIntegerItem multiplier = new CustomIntegerItem(AnnotationTypeDS.MULTIPLIER, getConstants().actionExportCategoriesTsv()); // TODO EDATOS-4082 cambiar mensaje
            multiplier.setRequired(true);

            generalEditionForm.setFields(uuid, title, multiplier);

            mainFormLayout.addEditionCanvas(generalEditionForm);
        }

        public void setAnnotationTypeDto(AnnotationTypeDto dto) {
            this.annotationTypeDto = dto;

            fillViewForm(dto);
            fillEditForm(dto);

            mainFormLayout.setViewMode();

            show();
        }

        private void fillViewForm(AnnotationTypeDto dto) {
            generalForm.setValue(AnnotationTypeDS.UUID, dto.getUuid());
            generalForm.setValue(AnnotationTypeDS.MULTIPLIER, dto.getCode() != null ? dto.getCode() : null);
            generalForm.setValue(AnnotationTypeDS.TITLE, dto.getTitle());
        }

        private void fillEditForm(AnnotationTypeDto dto) {
            generalEditionForm.setValue(AnnotationTypeDS.UUID, dto.getUuid());
            if (dto.getCode() != null) {
                generalEditionForm.setValue(AnnotationTypeDS.MULTIPLIER, dto.getCode());
            } else {
                generalEditionForm.clearValue(AnnotationTypeDS.MULTIPLIER);
            }
            generalEditionForm.setValue(AnnotationTypeDS.TITLE, dto.getTitle());
        }

        private AnnotationTypeDto getAnnotationTypeDto() {
            annotationTypeDto.setTitle(generalEditionForm.getValueAsInternationalStringDto(AnnotationTypeDS.TITLE));

            String annotationType = generalEditionForm.getValueAsString(AnnotationTypeDS.MULTIPLIER);
            annotationTypeDto.setCode(annotationType);

            return annotationTypeDto;
        }
    }
}
