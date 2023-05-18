package org.siemac.metamac.srm.core.task.domain;

public class ImportationConceptsTsvHeader extends ImportationItemsTsvHeader {

    private InternationalStringTsv         pluralName;
    private InternationalStringTsv         acronym;
    private InternationalStringTsv         descriptionSource;
    private InternationalStringTsv         context;
    private InternationalStringTsv         docMethod;
    private InternationalStringTsv         derivation;
    private InternationalStringTsv         legalActs;

    private int                    conceptTypePosition;                   // Number of columns of TSV File
    private RepresentationsTsv     representation;
    private int                    conceptExtendsPosition;                // Number of column for Parent field (zero-index based)
    private Integer                sdmxRelatedArtefactPosition = null;

    private int                    quantityTypePosition;
    private int                    unitCodelistCodePosition;
    private int                    unitCodePosition;
    private int                    unitSymbolPositionPosition;
    private int                    unitMultiplierPosition;
    private int                    significantDigitsPosition;
    private int                    decimalPlacesPosition;
    private int                    miminumPosition;
    private int                    maximumPosition;
    private int                    denominatorConceptSchemePosition;
    private int                    denominatorConceptPosition;
    private int                    numeratorConceptSchemePosition;
    private int                    numeratorConceptPosition;
    private int                    isPercentagePosition;
    private InternationalStringTsv percentageOf;
    private int                    indexBaseValuePosition;
    private int                    indexBaseTimePosition;
    private int                    indexBaseLocationCodelistCodePosition;
    private int                    indexBaseLocationCodePosition;
    private int                    indexBaseQuantityConceptSchemePosition;
    private int                    indexBaseQuantityConceptPosition;

    public InternationalStringTsv getPluralName() {
        return pluralName;
    }

    public void setPluralName(InternationalStringTsv pluralName) {
        this.pluralName = pluralName;
    }

    public InternationalStringTsv getAcronym() {
        return acronym;
    }

    public void setAcronym(InternationalStringTsv acronym) {
        this.acronym = acronym;
    }

    public InternationalStringTsv getDescriptionSource() {
        return descriptionSource;
    }

    public void setDescriptionSource(InternationalStringTsv descriptionSource) {
        this.descriptionSource = descriptionSource;
    }

    public InternationalStringTsv getContext() {
        return context;
    }

    public void setContext(InternationalStringTsv context) {
        this.context = context;
    }

    public InternationalStringTsv getDocMethod() {
        return docMethod;
    }

    public void setDocMethod(InternationalStringTsv docMethod) {
        this.docMethod = docMethod;
    }

    public InternationalStringTsv getDerivation() {
        return derivation;
    }

    public void setDerivation(InternationalStringTsv derivation) {
        this.derivation = derivation;
    }

    public InternationalStringTsv getLegalActs() {
        return legalActs;
    }

    public void setLegalActs(InternationalStringTsv legalActs) {
        this.legalActs = legalActs;
    }

    public int getConceptTypePosition() {
        return conceptTypePosition;
    }

    public void setConceptTypePosition(int conceptTypePosition) {
        this.conceptTypePosition = conceptTypePosition;
    }

    public RepresentationsTsv getRepresentation() {
        return representation;
    }

    public void setRepresentation(RepresentationsTsv representation) {
        this.representation = representation;
    }

    public int getConceptExtendsPosition() {
        return conceptExtendsPosition;
    }

    public void setConceptExtendsPosition(int conceptExtendsPosition) {
        this.conceptExtendsPosition = conceptExtendsPosition;
    }

    public Integer getSdmxRelatedArtefactPosition() {
        return sdmxRelatedArtefactPosition;
    }

    public void setSdmxRelatedArtefactPosition(Integer sdmxRelatedArtefactPosition) {
        this.sdmxRelatedArtefactPosition = sdmxRelatedArtefactPosition;
    }

    public int getQuantityTypePosition() {
        return quantityTypePosition;
    }

    public void setQuantityTypePosition(int quantityTypePosition) {
        this.quantityTypePosition = quantityTypePosition;
    }

    public int getUnitCodelistCodePosition() {
        return unitCodelistCodePosition;
    }

    public void setUnitCodelistCodePosition(int unitCodelistCodePosition) {
        this.unitCodelistCodePosition = unitCodelistCodePosition;
    }

    public int getUnitCodePosition() {
        return unitCodePosition;
    }

    public void setUnitCodePosition(int unitCodePosition) {
        this.unitCodePosition = unitCodePosition;
    }

    public int getUnitSymbolPositionPosition() {
        return unitSymbolPositionPosition;
    }

    public void setUnitSymbolPositionPosition(int unitSymbolPositionPosition) {
        this.unitSymbolPositionPosition = unitSymbolPositionPosition;
    }

    public int getUnitMultiplierPosition() {
        return unitMultiplierPosition;
    }

    public void setUnitMultiplierPosition(int unitMultiplierPosition) {
        this.unitMultiplierPosition = unitMultiplierPosition;
    }

    public int getSignificantDigitsPosition() {
        return significantDigitsPosition;
    }

    public void setSignificantDigitsPosition(int significantDigitsPosition) {
        this.significantDigitsPosition = significantDigitsPosition;
    }

    public int getDecimalPlacesPosition() {
        return decimalPlacesPosition;
    }

    public void setDecimalPlacesPosition(int decimalPlacesPosition) {
        this.decimalPlacesPosition = decimalPlacesPosition;
    }

    public int getMiminumPosition() {
        return miminumPosition;
    }

    public void setMiminumPosition(int miminumPosition) {
        this.miminumPosition = miminumPosition;
    }

    public int getMaximumPosition() {
        return maximumPosition;
    }

    public void setMaximumPosition(int maximumPosition) {
        this.maximumPosition = maximumPosition;
    }

    public int getDenominatorConceptSchemePosition() {
        return denominatorConceptSchemePosition;
    }

    public void setDenominatorConceptSchemePosition(int denominatorConceptSchemePosition) {
        this.denominatorConceptSchemePosition = denominatorConceptSchemePosition;
    }

    public int getDenominatorConceptPosition() {
        return denominatorConceptPosition;
    }

    public void setDenominatorConceptPosition(int denominatorConceptPosition) {
        this.denominatorConceptPosition = denominatorConceptPosition;
    }

    public int getNumeratorConceptSchemePosition() {
        return numeratorConceptSchemePosition;
    }

    public void setNumeratorConceptSchemePosition(int numeratorConceptSchemePosition) {
        this.numeratorConceptSchemePosition = numeratorConceptSchemePosition;
    }

    public int getNumeratorConceptPosition() {
        return numeratorConceptPosition;
    }

    public void setNumeratorConceptPosition(int numeratorConceptPosition) {
        this.numeratorConceptPosition = numeratorConceptPosition;
    }

    public int getIsPercentagePosition() {
        return isPercentagePosition;
    }

    public void setIsPercentagePosition(int isPercentagePosition) {
        this.isPercentagePosition = isPercentagePosition;
    }

    public int getIndexBaseValuePosition() {
        return indexBaseValuePosition;
    }

    public void setIndexBaseValuePosition(int indexBaseValuePosition) {
        this.indexBaseValuePosition = indexBaseValuePosition;
    }

    public int getIndexBaseTimePosition() {
        return indexBaseTimePosition;
    }

    public void setIndexBaseTimePosition(int indexBaseTimePosition) {
        this.indexBaseTimePosition = indexBaseTimePosition;
    }

    public int getIndexBaseLocationCodelistCodePosition() {
        return indexBaseLocationCodelistCodePosition;
    }

    public void setIndexBaseLocationCodelistCodePosition(int indexBaseLocationCodelistCodePosition) {
        this.indexBaseLocationCodelistCodePosition = indexBaseLocationCodelistCodePosition;
    }

    public int getIndexBaseLocationCodePosition() {
        return indexBaseLocationCodePosition;
    }

    public void setIndexBaseLocationCodePosition(int indexBaseLocationCodePosition) {
        this.indexBaseLocationCodePosition = indexBaseLocationCodePosition;
    }

    public int getIndexBaseQuantityConceptSchemePosition() {
        return indexBaseQuantityConceptSchemePosition;
    }

    public void setIndexBaseQuantityConceptSchemePosition(int indexBaseQuantityConceptSchemePosition) {
        this.indexBaseQuantityConceptSchemePosition = indexBaseQuantityConceptSchemePosition;
    }

    public int getIndexBaseQuantityConceptPosition() {
        return indexBaseQuantityConceptPosition;
    }

    public void setIndexBaseQuantityConceptPosition(int indexBaseQuantityConceptPosition) {
        this.indexBaseQuantityConceptPosition = indexBaseQuantityConceptPosition;
    }

    public InternationalStringTsv getPercentageOf() {
        return percentageOf;
    }

    public void setPercentageOf(InternationalStringTsv percentageOf) {
        this.percentageOf = percentageOf;
    }

}
