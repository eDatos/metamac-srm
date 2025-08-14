package org.siemac.metamac.srm.web.concept.widgets;

import java.util.List;

import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeVisualisationDto;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptSchemeUiHandlers;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.widgets.TitleLabel;

import com.smartgwt.client.widgets.layout.VLayout;

public class ConceptSchemeOrdersPanel extends VLayout {

    private ConceptSchemeOrdersSectionStack conceptSchemeOrdersSectionStack;
    private TitleLabel                      orderTitle;
    private ConceptsOrderTreeGrid           codesOrderTreeGrid;

    public ConceptSchemeOrdersPanel() {
        setMargin(15);
        setMembersMargin(10);

        conceptSchemeOrdersSectionStack = new ConceptSchemeOrdersSectionStack();
        addMember(conceptSchemeOrdersSectionStack);

        orderTitle = new TitleLabel();
        orderTitle.setStyleName("subsectionTitleWithNoLeftMargin");
        addMember(orderTitle);

        codesOrderTreeGrid = new ConceptsOrderTreeGrid();
        addMember(codesOrderTreeGrid);
    }

    public void setOrders(List<ConceptSchemeVisualisationDto> orders) {
        conceptSchemeOrdersSectionStack.setConceptSchemeVisualisations(orders);

        hideCodes();
    }

    public void setCodes(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ConceptMetamacVisualisationResult> codes, ConceptSchemeVisualisationDto conceptSchemeOrderVisualisationDto) {
        conceptSchemeOrdersSectionStack.selectConceptSchemeVisualisation(conceptSchemeOrderVisualisationDto.getUrn());
        orderTitle.setContents(CommonWebUtils.getElementName(conceptSchemeOrderVisualisationDto.getCode(), conceptSchemeOrderVisualisationDto.getName()));
        codesOrderTreeGrid.setItems(conceptSchemeMetamacDto, codes, conceptSchemeOrderVisualisationDto);
        codesOrderTreeGrid.setAutoFitMaxRecords(codes.size() + 1);

        showCodes();
    }

    public void setUiHandlers(ConceptSchemeUiHandlers uiHandlers) {
        conceptSchemeOrdersSectionStack.setUiHandlers(uiHandlers);
        codesOrderTreeGrid.setUiHandlers(uiHandlers);
    }

    public ConceptSchemeOrdersSectionStack getConceptSchemeOrdersSectionStack() {
        return conceptSchemeOrdersSectionStack;
    }

    public TitleLabel getOrderTitle() {
        return orderTitle;
    }

    public ConceptsOrderTreeGrid getCodesTreeGrid() {
        return codesOrderTreeGrid;
    }

    public void updateItemScheme(ConceptSchemeMetamacDto conceptSchemeMetamacDto) {
        codesOrderTreeGrid.updateItemScheme(conceptSchemeMetamacDto);
    }

    public void showCodes() {
        orderTitle.show();
        codesOrderTreeGrid.show();
    }

    public void hideCodes() {
        orderTitle.hide();
        codesOrderTreeGrid.hide();
    }
}
