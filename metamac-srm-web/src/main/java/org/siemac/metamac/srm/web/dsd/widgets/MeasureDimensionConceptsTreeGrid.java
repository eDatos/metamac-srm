package org.siemac.metamac.srm.web.dsd.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.common.service.utils.shared.SrmUrnParserUtils;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.concept.dto.ConceptSchemeMetamacDto;
import org.siemac.metamac.srm.core.dsd.dto.MeasureDimensionPrecisionDto;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.view.handlers.BaseConceptUiHandlers;
import org.siemac.metamac.srm.web.concept.widgets.ConceptTreeNode;
import org.siemac.metamac.srm.web.concept.widgets.ConceptsTreeGrid;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.widgets.CustomLinkTreeGridField;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.RelatedResourceTypeEnum;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.tree.Tree;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

public class MeasureDimensionConceptsTreeGrid extends ConceptsTreeGrid {

    private final static String                CONCEPT_FIELD = "concept-field";

    private List<MeasureDimensionPrecisionDto> measureDimensionPrecisionDtos;

    public MeasureDimensionConceptsTreeGrid(boolean editionMode) {
        super();

        setShowFilterEditor(false);
        setEditEvent(ListGridEditEvent.CLICK);

        // This field will show the code and the name of the concept
        codeField = new CustomLinkTreeGridField(CONCEPT_FIELD, getConstants().identifiableArtefactCode());
        codeField.setWidth("30%");
        codeField.setCanEdit(false);

        TreeGridField showDecimalsPrecisionField = new TreeGridField(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION, getConstants().dsdDecimals());
        showDecimalsPrecisionField.setWidth("20%");
        showDecimalsPrecisionField.setCanEdit(editionMode);
        showDecimalsPrecisionField.setCanFilter(false);
        showDecimalsPrecisionField.setEditorType(new SelectItem(DataStructureDefinitionDS.SHOW_DECIMALS, getConstants().dsdShowDecimals()));
        showDecimalsPrecisionField.setValueMap(org.siemac.metamac.srm.web.dsd.utils.CommonUtils.getDsdShowDecimalsHashMap());
        showDecimalsPrecisionField.setShowHover(false);
        showDecimalsPrecisionField.setAlign(Alignment.CENTER);

        setFields(codeField, showDecimalsPrecisionField);

        // Add handler to enable the navigation

        cellClickHandlerRegistration.removeHandler();
        addCellClickHandler(new CellClickHandler() {

            @Override
            public void onCellClick(CellClickEvent event) {
                Record record = event.getRecord();
                ListGridField field = getField(event.getColNum());
                if (ListGridFieldType.LINK.equals(field.getType()) && CONCEPT_FIELD.equals(field.getName())) {
                    String urn = record.getAttribute(ConceptDS.URN);
                    if (SrmUrnParserUtils.isConceptSchemeUrn(urn)) {
                        MeasureDimensionConceptsTreeGrid.this.uiHandlers.goTo(PlaceRequestUtils.buildAbsoluteConceptSchemePlaceRequest(urn));
                    } else if (SrmUrnParserUtils.isConceptUrn(urn)) {
                        MeasureDimensionConceptsTreeGrid.this.uiHandlers.goTo(PlaceRequestUtils.buildAbsoluteConceptPlaceRequest(urn));
                    }
                }
            }
        });

    }

    @Override
    public boolean canEditCell(int rowNum, int colNum) {
        // Disable editing the first row (contains the concept scheme)
        return rowNum != 0;
    }

    public void setMeasureDimensionPrecisions(List<MeasureDimensionPrecisionDto> measureDimensionPrecisionDtos) {
        this.measureDimensionPrecisionDtos = measureDimensionPrecisionDtos;
        updateRecords(null);
    }

    public List<MeasureDimensionPrecisionDto> getMeasureDimensionPrecisionDtos() {
        List<MeasureDimensionPrecisionDto> measureDimensionPrecisionDtos = new ArrayList<MeasureDimensionPrecisionDto>();
        ListGridRecord listGridRecords[] = getRecords();
        for (ListGridRecord record : listGridRecords) {
            String value = record.getAttributeAsString(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION);
            if (!StringUtils.isBlank(value)) {
                Integer showDecimalsPrecision = Integer.valueOf(value);
                MeasureDimensionPrecisionDto measureDimensionPrecisionDto = new MeasureDimensionPrecisionDto();
                measureDimensionPrecisionDto.setShowDecimalPrecision(showDecimalsPrecision);
                measureDimensionPrecisionDto.setCode(record.getAttribute(ItemDS.CODE));
                measureDimensionPrecisionDto.setType(RelatedResourceTypeEnum.CONCEPT);
                measureDimensionPrecisionDto.setUrn(record.getAttributeAsString(ItemDS.URN));
                measureDimensionPrecisionDtos.add(measureDimensionPrecisionDto);
            }
        }
        return measureDimensionPrecisionDtos;
    }

    private void updateRecords(List<ConceptMetamacVisualisationResult> itemHierarchyDtos) {
        Tree tree = getTree();
        TreeNode[] allNodes = tree.getAllNodes();
        TreeNode[] sortedTreeNode = sortTreeNodesRecursively(allNodes);

        for (int i = 0; i < sortedTreeNode.length; i++) {
            TreeNode node = sortedTreeNode[i];

            String conceptField = CommonWebUtils.getElementName(node.getAttributeAsString(CodeDS.CODE), node.getAttribute(CodeDS.NAME));
            node.setAttribute(CONCEPT_FIELD, conceptField);
            node.setAttribute(DataStructureDefinitionDS.SHOW_DECIMALS_PRECISION, getDecimalsByConcept(node.getAttribute(ItemDS.URN), itemHierarchyDtos));
        }

        tree.linkNodes(sortedTreeNode);
        setData(tree);

        markForRedraw();
    }

    private TreeNode[] sortTreeNodesRecursively(TreeNode[] nodes) {
        if (nodes == null || nodes.length == 0) {
            return new TreeNode[0];
        }

        List<TreeNode> sortedNodes = new ArrayList<TreeNode>();

        for (int i = 0; i < nodes.length; i++) {
            TreeNode node = nodes[i];
            TreeNode[] children = getData().getChildren(node);
            TreeNode[] sortedChildren = sortTreeNodesRecursively(children);
            node.setChildren(sortedChildren);
            sortedNodes.add(node);
        }

        java.util.Collections.sort(sortedNodes, new java.util.Comparator<TreeNode>() {

            @Override
            public int compare(TreeNode n1, TreeNode n2) {
                if (n1 instanceof ConceptTreeNode && n2 instanceof ConceptTreeNode) {
                    Integer o1 = ((ConceptTreeNode) n1).getOrder();
                    Integer o2 = ((ConceptTreeNode) n2).getOrder();
                    if (o1 == null)
                        o1 = 0;
                    if (o2 == null)
                        o2 = 0;
                    return o1.compareTo(o2);
                }
                return 0;
            }
        });

        return sortedNodes.toArray(new TreeNode[sortedNodes.size()]);
    }

    public Integer getDecimalsByConcept(String conceptUrn, List<ConceptMetamacVisualisationResult> itemHierarchyDtos) {
        for (MeasureDimensionPrecisionDto measureDimensionPrecisionDto : measureDimensionPrecisionDtos) {
            if (StringUtils.equals(measureDimensionPrecisionDto.getUrn(), conceptUrn)) {
                return measureDimensionPrecisionDto.getShowDecimalPrecision();
            }
        }
        if (itemHierarchyDtos != null) {
            for (ConceptMetamacVisualisationResult concept : itemHierarchyDtos) {
                if (StringUtils.equals(concept.getUrn(), conceptUrn)) {
                    return concept.getDecimals();
                }
            }
        }
        return null;
    }

    public void clearTree() {
        // Clear filter editor
        setFilterEditorCriteria(null);

        this.conceptSchemeMetamacDto = null;
        this.itemSchemeDto = null;

        TreeNode[] treeNodes = new TreeNode[0];

        tree = new Tree();
        tree.setModelType(TreeModelType.PARENT);
        tree.linkNodes(treeNodes);
        setData(tree);
        getData().openAll();
    }

    @Override
    public void setConcepts(ConceptSchemeMetamacDto conceptSchemeMetamacDto, List<ConceptMetamacVisualisationResult> itemHierarchyDtos) {
        super.updateItemScheme(conceptSchemeMetamacDto);
        super.setConcepts(conceptSchemeMetamacDto, itemHierarchyDtos);
        updateRecords(itemHierarchyDtos);
    }

    @Override
    public void updateItemScheme(ItemSchemeDto itemSchemeDto) {
        // Update concept scheme in tree grid
        super.updateItemScheme(itemSchemeDto);
    }

    @Override
    public void setUiHandlers(BaseConceptUiHandlers uiHandlers) {
        // Overwrite this method to do nothing
    }

    @Override
    protected void onNodeClick(String nodeName, String conceptUrn) {
        // Overwrite this method to do nothing
    }
}
