package org.siemac.metamac.srm.core.code.domain;

import org.siemac.metamac.srm.core.common.domain.ItemMetamacResultSelection;

public class ConceptMetamacResultSelection extends ItemMetamacResultSelection {

    public static final ConceptMetamacResultSelection API           = new ConceptMetamacResultSelection(false, false, false, false, false);
    public static final ConceptMetamacResultSelection EXPORT        = new ConceptMetamacResultSelection(true, true, true, true, false);
    public static final ConceptMetamacResultSelection RETRIEVE      = new ConceptMetamacResultSelection(true, false, true, true, false);
    public static final ConceptMetamacResultSelection EXPORT_ORDERS = new ConceptMetamacResultSelection(true, true, true, true, true);

    private boolean                                   pluralName;

    public ConceptMetamacResultSelection(boolean descriptions, boolean comments, boolean annotations, boolean internationalStringsMetamac, boolean pluralName) {
        super(descriptions, comments, annotations, internationalStringsMetamac);
        this.pluralName = pluralName;
    }

    public boolean isPluralName() {
        return pluralName;
    }

    public void setPluralName(boolean pluralName) {
        this.pluralName = pluralName;
    }
}
