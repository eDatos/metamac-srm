package org.siemac.metamac.srm.core.task.domain;

public class ImportationCodesTsvHeader extends ImportationItemsTsvHeader {

    private int                    variableElementPosition;
    private InternationalStringTsv shortName;
    private int isExtendedPosition;

    public int getVariableElementPosition() {
        return variableElementPosition;
    }

    public void setVariableElementPosition(int variableElementPosition) {
        this.variableElementPosition = variableElementPosition;
    }

    public InternationalStringTsv getShortName() {
        return shortName;
    }

    public void setShortName(InternationalStringTsv shortName) {
        this.shortName = shortName;
    }

    public int getIsExtendedPosition() {
        return isExtendedPosition;
    }

    public void setIsExtendedPosition(int isExtendedPosition) {
        this.isExtendedPosition = isExtendedPosition;
    }

}
