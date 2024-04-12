package org.siemac.metamac.srm.core.task.domain;

public class ImportationAnnotationsTsvHeader {

    private int                    columnsSize;
    private int                    codePosition;
    private InternationalStringTsv annotation;
    private int                    annotationTypePosition;
    private int                    annotationUrlPosition;
    private String                 columnName;

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

    public InternationalStringTsv getAnnotation() {
        return annotation;
    }

    public void setAnnotation(InternationalStringTsv annotation) {
        this.annotation = annotation;
    }

    public int getAnnotationTypePosition() {
        return annotationTypePosition;
    }

    public void setAnnotationTypePosition(int annotationTypePosition) {
        this.annotationTypePosition = annotationTypePosition;
    }

    public int getAnnotationUrlPosition() {
        return annotationUrlPosition;
    }

    public void setAnnotationUrlPosition(int annotationUrlPosition) {
        this.annotationUrlPosition = annotationUrlPosition;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

}
