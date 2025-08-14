package org.siemac.metamac.srm.web.concept.utils;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.utils.ItemsTreeGridUtils;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.widgets.ConceptTreeNode;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.DateUtils;

public class ConceptsTreeGridUtils extends ItemsTreeGridUtils {

    public static ConceptTreeNode createConceptTreeNode(String schemeNodeName, ConceptMetamacVisualisationResult concept) {
        ConceptTreeNode node = new ConceptTreeNode(concept.getName());
        node.setAttribute(ConceptDS.ACRONYM, concept.getAcronym());
        node.setAttribute(ConceptDS.VARIABLE, RelatedResourceUtils.getRelatedResourceName(concept.getVariable()));
        node.setAttribute(ConceptDS.SDMX_RELATED_ARTEFACT, CommonUtils.getConceptRoleName(concept.getSdmxRelatedArtefact()));

        node.setID(concept.getUrn());
        node.setIsFolder(true);
        node.setParentID(concept.getParent() != null ? concept.getParent().getUrn() : schemeNodeName);
        node.setAttribute(ItemDS.CODE, concept.getCode());
        node.setAttribute(ItemDS.NAME, concept.getName() != null ? concept.getName().replace(SrmConstants.TSV_BR_TAG, SrmConstants.TSV_DOUBLE_SPACE) : StringUtils.EMPTY);
        node.setAttribute(ItemDS.URN, concept.getUrn());
        node.setAttribute(ItemDS.DESCRIPTION, concept.getDescription());
        node.setAttribute(ItemDS.CREATION_DATE, DateUtils.getFormattedDate(concept.getCreatedDate()));
        node.setAttribute(ItemDS.ITEM_PARENT_URN, concept.getParent() != null ? concept.getParent().getUrn() : schemeNodeName);
        node.setAttribute(ItemDS.DTO, concept);

        node.setOrder(concept.getOrder() == null ? 0 : concept.getOrder());

        node.setAttribute(ConceptDS.INFO, org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.info().getURL());

        return node;
    }
}
