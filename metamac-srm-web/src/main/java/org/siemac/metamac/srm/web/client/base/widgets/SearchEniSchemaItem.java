package org.siemac.metamac.srm.web.client.base.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.master.dto.EniSchemaCatalogDto;
import org.siemac.metamac.srm.web.category.model.ds.EniSchemaDS;
import org.siemac.metamac.srm.web.category.model.record.EniSchemaRecord;
import org.siemac.metamac.srm.web.client.utils.RecordUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.BaseSearchWindow;
import org.siemac.metamac.web.common.client.widgets.CustomListGridField;
import org.siemac.metamac.web.common.client.widgets.form.fields.DragAndDropItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.ExternalItemListItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class SearchEniSchemaItem extends ExternalItemListItem {

    protected static final int          FORM_ITEM_CUSTOM_WIDTH = 1000;

    protected SearchEniSchemaWindow     searchEniSchemaWindow;
    protected List<EniSchemaCatalogDto> eniSchemaDtos;

    public SearchEniSchemaItem(final String name, String title, boolean editionMode) {
        super(name, title, editionMode);

        CustomListGridField valueField = new CustomListGridField(EniSchemaDS.VALUE, getConstants().selectedEniSchema());
        listGrid.setFields(valueField);

        if (editionMode) {
            getSearchIcon().addFormItemClickHandler(new FormItemClickHandler() {

                @Override
                public void onFormItemClick(FormItemIconClickEvent event) {
                    searchEniSchemaWindow = new SearchEniSchemaWindow(name);
                    searchEniSchemaWindow.resetValues();
                    searchEniSchemaWindow.setSourceEniSchemaDtos(getSourceEniSchemaSelectedTypes());
                    searchEniSchemaWindow.setTargetEniSchemaDtos(getSelectedEniSchemaDtos());
                    searchEniSchemaWindow.getSaveButton().addClickHandler(new ClickHandler() {

                        @Override
                        public void onClick(ClickEvent event) {
                            List<EniSchemaCatalogDto> selectedTypeDtos = searchEniSchemaWindow.getSelectedEniSchemaDtos();
                            searchEniSchemaWindow.markForDestroy();

                            setEniSchema(selectedTypeDtos);
                            SearchEniSchemaItem.this.validate();
                        }
                    });
                }
            });
        }
    }

    public void setEniSchemaCatalogList(List<EniSchemaCatalogDto> eniSchemaCatalogDto) {
        this.eniSchemaDtos = eniSchemaCatalogDto;
    }

    private List<EniSchemaCatalogDto> getSourceEniSchemaSelectedTypes() {
        List<EniSchemaCatalogDto> sourceTypeDtosWihtoutSelectedTypes = new ArrayList<EniSchemaCatalogDto>();
        List<EniSchemaCatalogDto> selectedTypeDtos = getSelectedEniSchemaDtos();
        for (EniSchemaCatalogDto typeDto : eniSchemaDtos) {
            if (!isTypeInList(typeDto, selectedTypeDtos)) {
                sourceTypeDtosWihtoutSelectedTypes.add(typeDto);
            }
        }
        return sourceTypeDtosWihtoutSelectedTypes;
    }

    private boolean isTypeInList(EniSchemaCatalogDto eniSchemaDto, List<EniSchemaCatalogDto> eniSchemaDtos) {
        for (EniSchemaCatalogDto type : eniSchemaDtos) {
            if (eniSchemaDto.getIdentifier() != null && type.getIdentifier() != null) {
                if (StringUtils.equals(eniSchemaDto.getIdentifier(), type.getIdentifier())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setEniSchema(List<EniSchemaCatalogDto> eniSchemaDto) {
        EniSchemaRecord[] records = RecordUtils.getEniSchemaRecords(eniSchemaDto);
        getListGrid().setData(records);
    }

    public List<EniSchemaCatalogDto> getSelectedEniSchemaDtos() {
        List<EniSchemaCatalogDto> eniSchemaDtos = new ArrayList<EniSchemaCatalogDto>();
        ListGridRecord[] records = getListGrid().getRecords();
        for (ListGridRecord record : records) {
            eniSchemaDtos.add(((EniSchemaRecord) record).getEniSchemaDto());
        }
        return eniSchemaDtos;
    }

    private class SearchEniSchemaWindow extends BaseSearchWindow {

        protected EniSchemaDragAndDropItem eniSchemaDragAndDropItem;
        protected ButtonItem               saveItem;

        public SearchEniSchemaWindow(String name) {
            super(getConstants().eniSchemaSelection());
            setWidth(FORM_ITEM_CUSTOM_WIDTH);

            eniSchemaDragAndDropItem = new EniSchemaDragAndDropItem(name, "", name);
            eniSchemaDragAndDropItem.setShowTitle(false);

            saveItem = new ButtonItem("save-button", MetamacWebCommon.getConstants().actionSave());
            saveItem.setAlign(Alignment.CENTER);

            getForm().setFields(eniSchemaDragAndDropItem, saveItem);
        }

        public void setSourceEniSchemaDtos(List<EniSchemaCatalogDto> eniSchemaDtos) {
            eniSchemaDragAndDropItem.setSourceEniSchemaDtos(eniSchemaDtos);
        }

        public void setTargetEniSchemaDtos(List<EniSchemaCatalogDto> eniSchemaDtos) {
            eniSchemaDragAndDropItem.setTargetEniSchemaDtos(eniSchemaDtos);
        }

        public void resetValues() {
            eniSchemaDragAndDropItem.resetValues();
        }

        public ButtonItem getSaveButton() {
            return saveItem;
        }

        public List<EniSchemaCatalogDto> getSelectedEniSchemaDtos() {
            return eniSchemaDragAndDropItem.getSelectedEniSchemaDtos();
        }
    }

    private class EniSchemaDragAndDropItem extends DragAndDropItem {

        public EniSchemaDragAndDropItem(String name, String title, String dragDropType) {
            super(name, title);

            CustomListGridField titleField = new CustomListGridField(EniSchemaDS.VALUE, getConstants().selectedEniSchema());

            sourceList.setHeight(250);
            sourceList.setWidth(FORM_ITEM_CUSTOM_WIDTH / 2);
            sourceList.setDataSource(new EniSchemaDS());
            sourceList.setUseAllDataSourceFields(false);
            sourceList.setFields(titleField);

            targetList.setHeight(250);
            targetList.setWidth(FORM_ITEM_CUSTOM_WIDTH / 2);
            targetList.setDataSource(new EniSchemaDS());
            targetList.setUseAllDataSourceFields(false);
            targetList.setFields(titleField);
        }

        public void setSourceEniSchemaDtos(List<EniSchemaCatalogDto> eniSchemaDtos) {
            EniSchemaRecord[] records = RecordUtils.getEniSchemaRecords(eniSchemaDtos);
            sourceList.setData(records);
        }

        public void setTargetEniSchemaDtos(List<EniSchemaCatalogDto> eniSchemaDtos) {
            EniSchemaRecord[] records = RecordUtils.getEniSchemaRecords(eniSchemaDtos);
            targetList.setData(records);
        }

        public List<EniSchemaCatalogDto> getSelectedEniSchemaDtos() {
            List<EniSchemaCatalogDto> selectedItems = new ArrayList<EniSchemaCatalogDto>();
            ListGridRecord[] records = targetList.getRecords();
            for (int i = 0; i < records.length; i++) {
                EniSchemaRecord record = (EniSchemaRecord) records[i];
                selectedItems.add(record.getEniSchemaDto());
            }
            return selectedItems;
        }

        public void resetValues() {
            clearValue();
        }
    }
}
