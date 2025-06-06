package org.siemac.metamac.srm.core.util;

import java.util.Comparator;

import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.common.service.utils.SrmServiceUtils;
import org.siemac.metamac.srm.core.concept.domain.ConceptMetamac;

import com.arte.statistic.sdmx.srm.core.base.domain.Item;
import com.arte.statistic.sdmx.srm.core.code.domain.Code;
import com.arte.statistic.sdmx.srm.core.concept.domain.Concept;

public class AlphabeticalComparator {

    public static class AlphabeticalByLevelComparator implements Comparator<Item> {

        @Override
        public int compare(Item i1, Item i2) {
            if (isCodesSiblings(i1, i2)) {
                return calculeOrderCodeSiblingsAlphabetical(i1, i2);
            } else if (i1.getParent() == null) {
                return 1;
            } else if (i2.getParent() == null) {
                return -1;
            } else {
                // return i1.getParent().getId().compareTo(i2.getParent().getId()); // not used because id can be null when save operation is executed when transaction is closed
                return i1.getParent().getUuid().compareTo(i2.getParent().getUuid());
            }
        }

    }

    public static boolean isCodesSiblings(Item i1, Item i2) {
        return (i1.getParent() == null && i2.getParent() == null) || (i1.getParent() != null && i2.getParent() != null && i1.getParent().getUuid().equals(i2.getParent().getUuid()));
    }

    public static int calculeOrderCodeSiblingsAlphabetical(Item i1, Item i2) {
        return i1.getNameableArtefact().getCode().compareToIgnoreCase(i2.getNameableArtefact().getCode());
    }

    public static class CodelistOrderIndexByLevelComparator implements Comparator<Code> {

        private final int columnIndex;

        public CodelistOrderIndexByLevelComparator(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int compare(Code i1, Code i2) {
            CodeMetamac c1 = (CodeMetamac) i1;
            CodeMetamac c2 = (CodeMetamac) i2;
            if (AlphabeticalComparator.isCodesSiblings(i1, i2)) {
                // The elements with order set as null will set to the end.
                Integer c1Order = SrmServiceUtils.getCodeOrder(c1, columnIndex);
                Integer c2Order = SrmServiceUtils.getCodeOrder(c2, columnIndex);
                if (c1Order == null && c2Order == null) {
                    return AlphabeticalComparator.calculeOrderCodeSiblingsAlphabetical(i1, i2);
                } else if (c1Order == null) {
                    return 1;
                } else if (c2Order == null) {
                    return -1;
                } else {
                    return c1Order.compareTo(c2Order);
                }
            } else if (i1.getParent() == null) {
                return 1;
            } else if (i2.getParent() == null) {
                return -1;
            } else {
                // return i1.getParent().getId().compareTo(i2.getParent().getId()); // not used because id can be null when save operation is executed when transaction is closed
                return i1.getParent().getUuid().compareTo(i2.getParent().getUuid());
            }
        }

    }

    public static class ConceptSchemeOrderIndexByLevelComparator implements Comparator<Concept> {

        private final int columnIndex;

        public ConceptSchemeOrderIndexByLevelComparator(int columnIndex) {
            this.columnIndex = columnIndex;
        }

        @Override
        public int compare(Concept i1, Concept i2) {
            ConceptMetamac c1 = (ConceptMetamac) i1;
            ConceptMetamac c2 = (ConceptMetamac) i2;
            if (AlphabeticalComparator.isCodesSiblings(i1, i2)) {
                // The elements with order set as null will set to the end.
                Integer c1Order = SrmServiceUtils.getConceptSchemeOrder(c1, columnIndex);
                Integer c2Order = SrmServiceUtils.getConceptSchemeOrder(c2, columnIndex);
                if (c1Order == null && c2Order == null) {
                    return AlphabeticalComparator.calculeOrderCodeSiblingsAlphabetical(i1, i2);
                } else if (c1Order == null) {
                    return 1;
                } else if (c2Order == null) {
                    return -1;
                } else {
                    return c1Order.compareTo(c2Order);
                }
            } else if (i1.getParent() == null) {
                return 1;
            } else if (i2.getParent() == null) {
                return -1;
            } else {
                return i1.getParent().getUuid().compareTo(i2.getParent().getUuid());
            }
        }

    }
}
