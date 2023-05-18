package org.siemac.metamac.srm.web.client.admin.view;

import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getConstants;
import static es.gobcan.istac.indicators.web.client.IndicatorsWeb.getMessages;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.srm.web.client.admin.view.handlers.AdminAnnotationTypeUiHandlers;
import org.siemac.metamac.web.common.client.widgets.ListGridToolStrip;
import org.siemac.metamac.web.common.client.widgets.PaginatedCheckListGrid;
import org.siemac.metamac.web.common.client.widgets.actions.PaginatedAction;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.InternationalMainFormLayout;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomIntegerItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.MultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewMultiLanguageTextItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.ViewTextItem;

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

import es.gobcan.istac.indicators.core.dto.UnitMultiplierDto;
import es.gobcan.istac.indicators.web.client.IndicatorsWeb;
import es.gobcan.istac.indicators.web.client.admin.presenter.AdminUnitMultipliersTabPresenter.AdminUnitMultipliersTabView;
import es.gobcan.istac.indicators.web.client.model.UnitMultiplierRecord;
import es.gobcan.istac.indicators.web.client.model.ds.UnitMultiplierDS;
import es.gobcan.istac.indicators.web.client.utils.ClientSecurityUtils;
import es.gobcan.istac.indicators.web.client.utils.IndicatorsWebConstants;

public class AdminAnnotationTypeTabViewImpl extends ViewWithUiHandlers<AdminAnnotationTypeUiHandlers> implements AdminUnitMultipliersTabView {

    private Layout                 panel;

    private PaginatedCheckListGrid listGrid;

    private ListGridToolStrip      toolStrip;

    private UnitMultiplierPanel    unitMultiplierPanel;

    public AdminAnnotationTypeTabViewImpl() {
        super();

        // ToolStrip

        toolStrip = new ListGridToolStrip(getMessages().unitMultipliersDeleteTitle(), getMessages().unitMultipliersConfirmDelete());
        toolStrip.getNewButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                selectUnitMultiplier(new UnitMultiplierDto());
                unitMultiplierPanel.setEditionMode();
            }

        });
        toolStrip.getNewButton().setVisibility(ClientSecurityUtils.canCreateUnitMultiplier() ? Visibility.VISIBLE : Visibility.HIDDEN);

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
                getUiHandlers().deleteUnitMultipliers(getSelectedUnitMultipliers(), CommonUtils.getFirstResultToReloadAfterDeletion(listGrid));
            }
        });

        // ListGrid

        listGrid = new PaginatedCheckListGrid(IndicatorsWebConstants.LISTGRID_MAX_RESULTS, new PaginatedAction() {

            @Override
            public void retrieveResultSet(int firstResult, int maxResults) {
                getUiHandlers().retrieveUnitMultipliers(firstResult);
            }
        });

        listGrid.getListGrid().setAutoFitData(Autofit.VERTICAL);
        listGrid.getListGrid().setAutoFitMaxRecords(IndicatorsWebConstants.LISTGRID_MAX_RESULTS);
        ListGridField uuidField = new ListGridField(UnitMultiplierDS.UUID, IndicatorsWeb.getConstants().unitMultiplierUuid());
        ListGridField titleField = new ListGridField(UnitMultiplierDS.TITLE, IndicatorsWeb.getConstants().unitMultiplierTitle());
        ListGridField multiplierField = new ListGridField(UnitMultiplierDS.MULTIPLIER, IndicatorsWeb.getConstants().unitMultiplierMultiplier());
        listGrid.getListGrid().setFields(uuidField, titleField, multiplierField);
        // Show data source details when record clicked
        listGrid.getListGrid().addSelectionChangedHandler(new SelectionChangedHandler() {

            @Override
            public void onSelectionChanged(SelectionEvent event) {
                if (listGrid.getListGrid().getSelectedRecords() != null && listGrid.getListGrid().getSelectedRecords().length == 1) {
                    UnitMultiplierRecord record = (UnitMultiplierRecord) listGrid.getListGrid().getSelectedRecord();
                    UnitMultiplierDto selected = record.getDto();
                    selectUnitMultiplier(selected);
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
    private List<String> getSelectedUnitMultipliers() {
        List<String> codes = new ArrayList<String>();
        for (ListGridRecord record : listGrid.getListGrid().getSelectedRecords()) {
            codes.add(record.getAttribute(UnitMultiplierDS.UUID));
        }
        return codes;
    }

    private void showToolStripDeleteButton() {
        if (ClientSecurityUtils.canDeleteUnitMultiplier()) {
            toolStrip.getDeleteButton().show();
        }
    }

    private void selectUnitMultiplier(UnitMultiplierDto dto) {
        if (dto.getUuid() != null) {
            showToolStripDeleteButton();
        } else {
            toolStrip.getDeleteButton().hide();
            listGrid.getListGrid().deselectAllRecords();
        }

        unitMultiplierPanel.setUnitMultiplierDto(dto);
    }

    private void deselectQuantityUnit() {
        toolStrip.getDeleteButton().hide();

        unitMultiplierPanel.hide();
    }

    // DATA

    @Override
    public void setUnitMultipliers(int firstResult, List<UnitMultiplierDto> dtos, int totalResults) {
        unitMultiplierPanel.hide();

        UnitMultiplierRecord[] records = new UnitMultiplierRecord[dtos.size()];
        int index = 0;
        for (UnitMultiplierDto ds : dtos) {
            records[index++] = RecordUtils.getUnitMultiplierRecord(ds);
        }
        listGrid.getListGrid().setData(records);
        listGrid.refreshPaginationInfo(firstResult, dtos.size(), totalResults);
    }

    @Override
    public void onUnitMultiplierCreated(UnitMultiplierDto dto) {
        selectUnitMultiplier(dto);
    }

    @Override
    public void onUnitMultiplierUpdated(UnitMultiplierDto dto) {
        selectUnitMultiplier(dto);
    }

    @Override
    public Widget asWidget() {
        return panel;
    }

    public class UnitMultiplierPanel extends VLayout {

        private InternationalMainFormLayout mainFormLayout;

        private GroupDynamicForm            generalEditionForm;
        private GroupDynamicForm            generalForm;

        private UnitMultiplierDto           unitMultiplierDto;

        public UnitMultiplierPanel() {
            mainFormLayout = new InternationalMainFormLayout(ClientSecurityUtils.canEditUnitMultiplier());
            mainFormLayout.setCanDelete(ClientSecurityUtils.canDeleteUnitMultiplier());
            mainFormLayout.setTitleLabelContents(getConstants().unitMultiplier());

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
                        getUiHandlers().saveUnitMultiplier(listGrid.getFirstResult(), getUnitMultiplierDto());
                    }
                }
            });

            mainFormLayout.getDeleteConfirmationWindow().getYesButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    List<String> uuids = new ArrayList<String>();
                    uuids.add(unitMultiplierDto.getUuid());
                    getUiHandlers().deleteUnitMultipliers(uuids, CommonUtils.getFirstResultToReloadAfterDeletion(listGrid));
                }
            });

            mainFormLayout.getCancelToolStripButton().addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    if (unitMultiplierDto.getUuid() == null || (unitMultiplierDto.getUuid() != null && unitMultiplierDto.getUuid().isEmpty())) {
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
            generalForm = new GroupDynamicForm(getConstants().unitMultiplierGeneral());

            ViewTextItem uuid = new ViewTextItem(UnitMultiplierDS.UUID, getConstants().unitMultiplierUuid());
            ViewMultiLanguageTextItem title = new ViewMultiLanguageTextItem(UnitMultiplierDS.TITLE, getConstants().unitMultiplierTitle());
            ViewTextItem multiplier = new ViewTextItem(UnitMultiplierDS.MULTIPLIER, getConstants().unitMultiplierMultiplier());

            generalForm.setFields(uuid, title, multiplier);

            mainFormLayout.addViewCanvas(generalForm);
        }

        private void createEditionForm() {
            generalEditionForm = new GroupDynamicForm(getConstants().unitMultiplierGeneral());

            ViewTextItem uuid = new ViewTextItem(UnitMultiplierDS.UUID, getConstants().unitMultiplierUuid());
            MultiLanguageTextItem title = new MultiLanguageTextItem(UnitMultiplierDS.TITLE, getConstants().unitMultiplierTitle());
            title.setRequired(true);
            CustomIntegerItem multiplier = new CustomIntegerItem(UnitMultiplierDS.MULTIPLIER, getConstants().unitMultiplierMultiplier());
            multiplier.setRequired(true);

            generalEditionForm.setFields(uuid, title, multiplier);

            mainFormLayout.addEditionCanvas(generalEditionForm);
        }

        public void setUnitMultiplierDto(UnitMultiplierDto dto) {
            this.unitMultiplierDto = dto;

            fillViewForm(dto);
            fillEditForm(dto);

            mainFormLayout.setViewMode();

            show();
        }

        private void fillViewForm(UnitMultiplierDto dto) {
            generalForm.setValue(UnitMultiplierDS.UUID, dto.getUuid());
            generalForm.setValue(UnitMultiplierDS.MULTIPLIER, dto.getUnitMultiplier() != null ? dto.getUnitMultiplier().toString() : null);
            generalForm.setValue(UnitMultiplierDS.TITLE, dto.getTitle());
        }

        private void fillEditForm(UnitMultiplierDto dto) {
            generalEditionForm.setValue(UnitMultiplierDS.UUID, dto.getUuid());
            if (dto.getUnitMultiplier() != null) {
                generalEditionForm.setValue(UnitMultiplierDS.MULTIPLIER, dto.getUnitMultiplier());
            } else {
                generalEditionForm.clearValue(UnitMultiplierDS.MULTIPLIER);
            }
            generalEditionForm.setValue(UnitMultiplierDS.TITLE, dto.getTitle());
        }

        private UnitMultiplierDto getUnitMultiplierDto() {
            unitMultiplierDto.setTitle(generalEditionForm.getValueAsInternationalStringDto(UnitMultiplierDS.TITLE));

            String multiplierStr = generalEditionForm.getValueAsString(UnitMultiplierDS.MULTIPLIER);
            unitMultiplierDto.setUnitMultiplier(Integer.valueOf(multiplierStr));

            return unitMultiplierDto;
        }
    }
}
