package org.siemac.metamac.srm.core.task.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ImportationCodeRestrictionsTsvHeader {

    private int                        columnsSize;
    private int                        codePosition;
    private int                        labelPosition;                                       // optional
    private int                        levelPosition;                                       // optional
    private int                        parentPosition;                                      // optional
    private final Map<String, Integer> restrictionVisualisations = new HashMap<String, Integer>();

    public int getColumnsSize() {
        return columnsSize;
    }

    public void setColumnsSize(int columnsSize) {
        this.columnsSize = columnsSize;
    }

    public int getCodePosition() {
        return codePosition;
    }

    public void setCodePosition(int codePosition) {
        this.codePosition = codePosition;
    }

    public int getParentPosition() {
        return parentPosition;
    }

    public void setParentPosition(int parentPosition) {
        this.parentPosition = parentPosition;
    }

    public int getLabelPosition() {
        return labelPosition;
    }

    public void setLabelPosition(int labelPosition) {
        this.labelPosition = labelPosition;
    }

    public int getLevelPosition() {
        return levelPosition;
    }

    public void setLevelPosition(int levelPosition) {
        this.levelPosition = levelPosition;
    }

    public int getRestrictionVisualisationColumn(String restrictionVisualisation) {
        return restrictionVisualisations.get(restrictionVisualisation);
    }

    public void addRestrictionVisualisations(String restrictionVisualisation, int column) {
        restrictionVisualisations.put(restrictionVisualisation, Integer.valueOf(column));
    }

    public Set<String> getRestrictionVisualisations() {
        return restrictionVisualisations.keySet();
    }

    public boolean hasRestrictionVisualisations() {
        return restrictionVisualisations.size() != 0;
    }
}
