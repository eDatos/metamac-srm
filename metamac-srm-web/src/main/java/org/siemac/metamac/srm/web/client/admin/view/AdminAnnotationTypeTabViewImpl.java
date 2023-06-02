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
import org.siemac.metamac.web.common.client.widgets.ListGridToolStrip;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Alignment;
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

    private AnnotationTypePanel    annotationTypePanel;

    public AdminAnnotationTypeTabViewImpl() {
        super();

        // ToolStrip

        toolStrip = new ListGridToolStrip(getMessages().annotationTypeDeleteTitle(), getMessages().annotationTypeConfirmDelete());
        toolStrip.getNewButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                selectAnnotationType(new AnnotationTypeDto());
                annotationTypePanel.setEditionMode();
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
                getUiHandlers().deleteAnnotationTypes(getSelectedAnnotationTypes(), getFirstResultToReloadAfterDeletion(listGrid));
            }
        });

        // ListGrid

        listGrid = new PaginatedCheckListGrid(SrmWebConstants.MAIN_LIST_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveAnnotationTypes(firstResult);
            }
        });

        listGrid.getListGrid().setAutoFitData(Autofit.VERTICAL);
        listGrid.getListGrid().setAutoFitMaxRecords(SrmWebConstants.MAIN_LIST_MAX_RESULTS);
        ListGridField uuidField = new ListGridField(AnnotationTypeDS.UUID, getConstants().adminAnnotationTypeUuid());
        ListGridField titleField = new ListGridField(AnnotationTypeDS.TITLE, getConstants().adminAnnotationTypeTitle());
        ListGridField multiplierField = new ListGridField(AnnotationTypeDS.CODE, getConstants().adminAnnotationTypeType());
        ListGridField isExternal = new ListGridField(AnnotationTypeDS.EXTERNAL, getConstants().adminAnnotationTypeIsExternal());
        isExternal.setAlign(Alignment.CENTER);

        listGrid.getListGrid().setFields(titleField, multiplierField, uuidField, isExternal);
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
                    deselectAnnotationType();
                    if (listGrid.getListGrid().getSelectedRecords().length > 1) {
                        // Delete more than one dimension with one click
                        showToolStripDeleteButton();
                    }
                }
            }
        });

        annotationTypePanel = new AnnotationTypePanel();
        annotationTypePanel.hide();

        VLayout listPanel = new VLayout();
        listPanel.addMember(toolStrip);
        listPanel.addMember(listGrid);

        HLayout leftLayout = new HLayout();
        leftLayout.setWidth("50%");
        leftLayout.addMember(listPanel);

        HLayout rightLayout = new HLayout();
        rightLayout.setWidth("50%");
        rightLayout.addMember(annotationTypePanel);

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

    public static int getFirstResultToReloadAfterDeletion(PaginatedCheckListGrid listGrid) {
        int firstResult = 0;
        if (listGrid.getListGrid().getRecords() != null && listGrid.getListGrid().getRecords().length > 1) {
            firstResult = listGrid.getFirstResult();
        }
        return firstResult;
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

        annotationTypePanel.setAnnotationTypeDto(dto);
    }

    private void deselectAnnotationType() {
        toolStrip.getDeleteButton().hide();

        annotationTypePanel.hide();
    }

    // DATA

    @Override
    public void setAnnotationTypes(int firstResult, List<AnnotationTypeDto> dtos, int totalResults) {
        annotationTypePanel.hide();

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
    public void onAnnotationTypeUpdated(AnnotationTypeDto dto) {
        selectAnnotationType(dto);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    public class AnnotationTypePanel extends VLayout {

        private InternationalMainFormLayout mainFormLayout;

        private GroupDynamicForm            generalEditionForm;
        private GroupDynamicForm            generalForm;

        private AnnotationTypeDto           annotationTypeDto;

        public AnnotationTypePanel() {
            mainFormLayout = new InternationalMainFormLayout(AdminClientSecurityUtils.canUpdateAnnotationType());
            mainFormLayout.setCanDelete(AdminClientSecurityUtils.canDeleteAnnotationType());
            mainFormLayout.setTitleLabelContents(getConstants().adminAnnotationType());

            // Edit: Add a custom handler to check indicator status before start editing
            mainFormLayout.getEditToolStripButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    mainFormLayout.setEditionMode();
                    CustomTextItem code = (CustomTextItem) generalEditionForm.getItem(AnnotationTypeDS.CODE);
                    if (annotationTypeDto != null && annotationTypeDto.isExternalResource()) {
                        code.disable();
                    } else {
                        code.enable();;
                    }
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
                    getUiHandlers().deleteAnnotationTypes(uuids, getFirstResultToReloadAfterDeletion(listGrid));
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
            generalForm = new GroupDynamicForm(getConstants().adminAnnotationType());

            ViewTextItem uuid = new ViewTextItem(AnnotationTypeDS.UUID, getConstants().adminAnnotationTypeUuid());
            ViewMultiLanguageTextItem title = new ViewMultiLanguageTextItem(AnnotationTypeDS.TITLE, getConstants().adminAnnotationTypeTitle());
            ViewTextItem code = new ViewTextItem(AnnotationTypeDS.CODE, getConstants().adminAnnotationTypeType());

            generalForm.setFields(uuid, title, code);

            mainFormLayout.addViewCanvas(generalForm);
        }

        private void createEditionForm() {
            generalEditionForm = new GroupDynamicForm(getConstants().adminAnnotationType());

            ViewTextItem uuid = new ViewTextItem(AnnotationTypeDS.UUID, getConstants().adminAnnotationTypeUuid());
            MultiLanguageTextItem title = new MultiLanguageTextItem(AnnotationTypeDS.TITLE, getConstants().adminAnnotationTypeTitle());
            title.setRequired(true);
            CustomTextItem code = new CustomTextItem(AnnotationTypeDS.CODE, getConstants().adminAnnotationTypeType());
            code.setRequired(true);

            generalEditionForm.setFields(uuid, title, code);

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
            generalForm.setValue(AnnotationTypeDS.CODE, dto.getCode() != null ? dto.getCode() : null);
            generalForm.setValue(AnnotationTypeDS.TITLE, dto.getTitle());
        }

        private void fillEditForm(AnnotationTypeDto dto) {
            generalEditionForm.setValue(AnnotationTypeDS.UUID, dto.getUuid());
            if (dto.getCode() != null) {
                generalEditionForm.setValue(AnnotationTypeDS.CODE, dto.getCode());
            } else {
                generalEditionForm.clearValue(AnnotationTypeDS.CODE);
            }
            generalEditionForm.setValue(AnnotationTypeDS.TITLE, dto.getTitle());

        }

        private AnnotationTypeDto getAnnotationTypeDto() {
            annotationTypeDto.setTitle(generalEditionForm.getValueAsInternationalStringDto(AnnotationTypeDS.TITLE));

            String annotationType = generalEditionForm.getValueAsString(AnnotationTypeDS.CODE);
            annotationTypeDto.setCode(annotationType);

            return annotationTypeDto;
        }
    }
}
