package org.siemac.metamac.srm.web.category.utils;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.category.domain.shared.CategoryElementVisualisationResult;
import org.siemac.metamac.srm.core.category.domain.shared.CategoryMetamacVisualisationResult;
import org.siemac.metamac.srm.core.category.domain.shared.CodeCategoryElementNormalisationResult;
import org.siemac.metamac.srm.web.category.model.ds.CategoryDS;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.utils.ItemsTreeGridUtils;
import org.siemac.metamac.srm.web.code.widgets.CategoryTreeNode;
import org.siemac.metamac.web.common.client.utils.DateUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.widgets.tree.TreeNode;

public class CategoriesTreeGridUtils extends ItemsTreeGridUtils {

    public static TreeNode createCategorySchemeTreeNode(String schemeNodeName, ItemSchemeDto itemSchemeDto) {
        return createItemSchemeTreeNode(schemeNodeName, itemSchemeDto);
    }

    public static CategoryTreeNode createCategoryTreeNode(String schemeNodeName, CategoryMetamacVisualisationResult code) {
        CategoryTreeNode node = new CategoryTreeNode(code.getName());
        node.setID(code.getUrn());
        node.setIsFolder(true);
        node.setParentID(code.getParent() != null ? code.getParent().getUrn() : schemeNodeName);
        node.setAttribute(CategoryDS.ID_DATABASE, code.getItemIdDatabase());
        node.setAttribute(ItemDS.CODE, code.getCode());
        node.setAttribute(ItemDS.NAME, code.getName() != null ? code.getName() : StringUtils.EMPTY);
        node.setAttribute(ItemDS.URN, code.getUrn());
        node.setAttribute(ItemDS.DESCRIPTION, code.getDescription());
        node.setAttribute(ItemDS.CREATION_DATE, DateUtils.getFormattedDate(code.getCreatedDate()));
        node.setAttribute(ItemDS.ITEM_PARENT_URN, code.getParent() != null ? code.getParent().getUrn() : schemeNodeName);
        node.setAttribute(ItemDS.DTO, code);

        // Only for resource elements tree grid
        node.setCategoryElement(code.getCategoryElement());

        // Info icon
        node.setAttribute(CategoryDS.INFO, org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.info().getURL());
        // Icons for the resource elements assignment tree)
        node.setAttribute(CategoryDS.CATEGORY_ELEMENT_EDITION, org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.search().getURL());
        node.setAttribute(CategoryDS.CATEGORY_ELEMENT_CLEAR, org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.deleteListGrid().getURL());

        if (code.getOrder() != null) {
            node.setOrder(CategoryDS.ORDER, code.getOrder());
        }

        return node;
    }

    public static CategoryTreeNode createCategoryTreeNode(String schemeNodeName, CodeCategoryElementNormalisationResult result) {
        CategoryMetamacVisualisationResult code = result.getCode();
        CategoryElementVisualisationResult categoryElement = result.getCategoryElementProposed();
        CategoryTreeNode node = createCategoryTreeNode(schemeNodeName, code);
        node.setCategoryElement(categoryElement);
        return node;
    }

    public static void setCategoryElementInRecord(CategoryTreeNode record, RelatedResourceDto categoryElement) {
        record.setCategoryElement(categoryElement);
    }

    public static void clearCategoryElementFromRecord(CategoryTreeNode record) {
        setCategoryElementInRecord(record, null);
    }
}
