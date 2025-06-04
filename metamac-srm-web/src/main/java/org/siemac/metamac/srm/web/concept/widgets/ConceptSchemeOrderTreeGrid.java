package org.siemac.metamac.srm.web.concept.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeVisualisationDto;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.data.SortSpecifier;
import com.smartgwt.client.types.DragDataAction;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.SortChangedHandler;
import com.smartgwt.client.widgets.grid.events.SortEvent;

public class ConceptSchemeOrderTreeGrid extends BaseConceptsTreeGrid {

    public ConceptSchemeOrderTreeGrid() {
        super(false, true);
        setShowFilterEditor(false);

        getField(ItemDS.CODE).setWidth("45%");

        setCanReorderRecords(true);
        setCanAcceptDroppedRecords(true);
        setCanDragRecordsOut(false);
        setDragDataAction(DragDataAction.MOVE);
        setShowOpenIcons(true);
        setShowDropIcons(true);

        // Disable the option to order by CODE and NAME fields (do not enable the button to order by other fields than order field)

        ListGridField[] itemFields = getAllFields();
        for (ListGridField itemField : itemFields) {
            if (!ConceptDS.ORDER.equals(itemField.getName())) {
                itemField.setCanSort(false);
            }
        }

        // Do not let to order in descending direction!!
        addSortChangedHandler(new SortChangedHandler() {

            @Override
            public void onSortChanged(SortEvent event) {
                SortSpecifier[] sortSpecifiers = event.getSortSpecifiers();
                if (sortSpecifiers != null) {
                    for (SortSpecifier sortSpecifier : sortSpecifiers) {
                        if (SortDirection.DESCENDING.equals(sortSpecifier.getSortDirection())) {
                            // Instead of cancel the event (do not know if it is possible), the column is ordered again in ascending order
                            sort(ConceptDS.ORDER, SortDirection.ASCENDING);
                        }
                    }
                }
            }
        });

        // Do not show context menu
        super.removeHandlerRegistrations();
    }

    @Override
    public void removeHandlerRegistrations() {
        super.removeHandlerRegistrations();
        folderDropHandlerRegistration.removeHandler();
    }

    public void setItems(ItemSchemeDto conceptSchemeMetamacDto, List<ConceptMetamacVisualisationResult> codes, ConceptSchemeVisualisationDto conceptSchemeOrderVisualisationDto) {
        this.conceptSchemeVisualisationDto = conceptSchemeOrderVisualisationDto;
        setItems(conceptSchemeMetamacDto, codes);
    }

    @Override
    protected void onNodeContextClick(String nodeName, ConceptMetamacVisualisationResult itemMetamacResult) {
        // TODO Auto-generated method stub

    }
}
