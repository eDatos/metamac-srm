package org.siemac.metamac.srm.web.concept.widgets;

import java.util.Comparator;

import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.web.common.client.widgets.NavigableTreeNode;

public class ConceptTreeNode extends NavigableTreeNode implements Comparable<ConceptTreeNode> {

    private Integer order;

    public ConceptTreeNode(String name) {
        super();
        setName(name);
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
        setAttribute(ConceptDS.ORDER, order);
    }

    @Override
    public int compareTo(ConceptTreeNode o) {
        return this.getOrder() - o.getOrder();
    }

    public static Comparator<ConceptTreeNode> OrderComparator = new Comparator<ConceptTreeNode>() {

        @Override
        public int compare(ConceptTreeNode codeNode1, ConceptTreeNode codeNode2) {

            Integer order1 = codeNode1.getOrder();
            Integer order2 = codeNode2.getOrder();

            // Ascending order
            return order1.compareTo(order2);
        }
    };
}
