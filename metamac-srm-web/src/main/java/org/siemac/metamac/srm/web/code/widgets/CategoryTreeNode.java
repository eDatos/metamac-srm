package org.siemac.metamac.srm.web.code.widgets;

import java.util.Comparator;

import org.siemac.metamac.srm.web.client.widgets.CategoryNavigableTreeNode;

public class CategoryTreeNode extends CategoryNavigableTreeNode implements Comparable<CategoryTreeNode> {

    private Integer order;

    public CategoryTreeNode(String name) {
        super();
        setName(name);
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(String attribute, Integer order) {
        this.order = order;
        setAttribute(attribute, order);
    }

    @Override
    public int compareTo(CategoryTreeNode o) {
        return this.getOrder() - o.getOrder();
    }

    public static Comparator<CategoryTreeNode> OrderComparator = new Comparator<CategoryTreeNode>() {

        @Override
        public int compare(CategoryTreeNode codeNode1, CategoryTreeNode codeNode2) {

            Integer order1 = codeNode1.getOrder();
            Integer order2 = codeNode2.getOrder();

            // Ascending order
            return order1.compareTo(order2);
        }
    };
}
