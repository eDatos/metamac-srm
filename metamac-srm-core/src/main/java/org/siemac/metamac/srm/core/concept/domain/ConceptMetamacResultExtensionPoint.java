package org.siemac.metamac.srm.core.concept.domain;

import java.util.HashMap;
import java.util.Map;

import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityTypeEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityUnitSymbolPositionEnum;
import org.siemac.metamac.srm.core.constants.SrmConstants;
import org.siemac.metamac.srm.core.task.domain.RepresentationsTsv;

public class ConceptMetamacResultExtensionPoint {

    private final Map<String, String>            pluralName                     = new HashMap<String, String>();
    private final Map<String, String>            acronym                        = new HashMap<String, String>();
    private final Map<String, String>            descriptionSource              = new HashMap<String, String>();
    private final Map<String, String>            context                        = new HashMap<String, String>();
    private final Map<String, String>            docMethod                      = new HashMap<String, String>();
    private final Map<String, String>            derivation                     = new HashMap<String, String>();
    private final Map<String, String>            legalActs                      = new HashMap<String, String>();

    private String                               conceptTypeIdentifier          = null;
    private String                               conceptExtendsUrn              = null;
    private RepresentationsTsv.RepresentationEum representationType             = null;
    private String                               representationValue            = null;
    private ConceptRoleEnum                      sdmxRelatedArtefact            = null;

    // Quantity metadata
    private QuantityTypeEnum                     quantityType                   = null;
    private String                               unitCodelistUrn                = null;
    private String                               unitCode                       = null;
    private QuantityUnitSymbolPositionEnum       unitSymbolPosition             = null;
    private String                               unitMultiplierCodelistUrn      = null;
    private String                               unitMultiplierCode             = null;
    private Integer                              significantDigits              = null;
    private Integer                              decimalPlaces                  = null;
    private Integer                              miminum                        = null;
    private Integer                              maximum                        = null;
    private String                               denominatorConceptScheme       = null;
    private String                               denominatorConcept             = null;
    private String                               numeratorConceptScheme         = null;
    private String                               numeratorConcept               = null;
    private Boolean                              isPercentage                   = Boolean.FALSE;
    private final Map<String, String>            percentageOf                   = new HashMap<String, String>();
    private Integer                              indexBaseValue                 = null;
    private String                               indexBaseTime                  = null;
    private String                               indexBaseLocationCodelistUrn   = null;
    private String                               indexBaseLocationCode          = null;
    private String                               indexBaseQuantityConceptScheme = null;
    private String                               indexBaseQuantityConcept       = null;
    private Integer                              orderConfiguration;
    private String                               orderConfigurationConcatenatedByLevel;

    public Map<String, String> getPluralName() {
        return pluralName;
    }

    public Map<String, String> getAcronym() {
        return acronym;
    }

    public Map<String, String> getDescriptionSource() {
        return descriptionSource;
    }

    public Map<String, String> getContext() {
        return context;
    }

    public Map<String, String> getDocMethod() {
        return docMethod;
    }

    public Map<String, String> getDerivation() {
        return derivation;
    }

    public Map<String, String> getLegalActs() {
        return legalActs;
    }

    public String getConceptTypeIdentifier() {
        return conceptTypeIdentifier;
    }

    public void setConceptTypeIdentifier(String conceptTypeIdentifier) {
        this.conceptTypeIdentifier = conceptTypeIdentifier;
    }

    public String getConceptExtendsUrn() {
        return conceptExtendsUrn;
    }

    public void setConceptExtendsUrn(String conceptExtendsUrn) {
        this.conceptExtendsUrn = conceptExtendsUrn;
    }

    public RepresentationsTsv.RepresentationEum getRepresentationType() {
        return representationType;
    }

    public void setRepresentationType(RepresentationsTsv.RepresentationEum representationType) {
        this.representationType = representationType;
    }

    public String getRepresentationValue() {
        return representationValue;
    }

    public void setRepresentationValue(String representationValue) {
        this.representationValue = representationValue;
    }

    public ConceptRoleEnum getSdmxRelatedArtefact() {
        return sdmxRelatedArtefact;
    }

    public void setSdmxRelatedArtefact(ConceptRoleEnum sdmxRelatedArtefact) {
        this.sdmxRelatedArtefact = sdmxRelatedArtefact;
    }

    public QuantityTypeEnum getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(QuantityTypeEnum quantityType) {
        this.quantityType = quantityType;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public QuantityUnitSymbolPositionEnum getUnitSymbolPosition() {
        return unitSymbolPosition;
    }

    public void setUnitSymbolPosition(QuantityUnitSymbolPositionEnum unitSymbolPosition) {
        this.unitSymbolPosition = unitSymbolPosition;
    }

    public String getUnitMultiplierCode() {
        return unitMultiplierCode;
    }

    public void setUnitMultiplierCode(String unitMultiplierCode) {
        this.unitMultiplierCode = unitMultiplierCode;
    }

    public Integer getSignificantDigits() {
        return significantDigits;
    }

    public void setSignificantDigits(Integer significantDigits) {
        this.significantDigits = significantDigits;
    }

    public Integer getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(Integer decimalPlaces) {
        this.decimalPlaces = decimalPlaces;
    }

    public Integer getMiminum() {
        return miminum;
    }

    public void setMiminum(Integer miminum) {
        this.miminum = miminum;
    }

    public Integer getMaximum() {
        return maximum;
    }

    public void setMaximum(Integer maximum) {
        this.maximum = maximum;
    }

    public String getDenominatorConceptScheme() {
        return denominatorConceptScheme;
    }

    public void setDenominatorConceptScheme(String denominatorConceptScheme) {
        this.denominatorConceptScheme = denominatorConceptScheme;
    }

    public String getDenominatorConcept() {
        return denominatorConcept;
    }

    public void setDenominatorConcept(String denominatorConcept) {
        this.denominatorConcept = denominatorConcept;
    }

    public String getNumeratorConceptScheme() {
        return numeratorConceptScheme;
    }

    public void setNumeratorConceptScheme(String numeratorConceptScheme) {
        this.numeratorConceptScheme = numeratorConceptScheme;
    }

    public String getNumeratorConcept() {
        return numeratorConcept;
    }

    public void setNumeratorConcept(String numeratorConcept) {
        this.numeratorConcept = numeratorConcept;
    }

    public Boolean getIsPercentage() {
        return isPercentage;
    }

    public void setIsPercentage(Boolean isPercentage) {
        this.isPercentage = isPercentage;
    }

    public Map<String, String> getPercentageOf() {
        return percentageOf;
    }

    public Integer getIndexBaseValue() {
        return indexBaseValue;
    }

    public void setIndexBaseValue(Integer indexBaseValue) {
        this.indexBaseValue = indexBaseValue;
    }

    public String getIndexBaseTime() {
        return indexBaseTime;
    }

    public void setIndexBaseTime(String indexBaseTime) {
        this.indexBaseTime = indexBaseTime;
    }

    public String getIndexBaseLocationCode() {
        return indexBaseLocationCode;
    }

    public void setIndexBaseLocationCode(String indexBaseLocationCode) {
        this.indexBaseLocationCode = indexBaseLocationCode;
    }

    public String getIndexBaseQuantityConceptScheme() {
        return indexBaseQuantityConceptScheme;
    }

    public void setIndexBaseQuantityConceptScheme(String indexBaseQuantityConceptScheme) {
        this.indexBaseQuantityConceptScheme = indexBaseQuantityConceptScheme;
    }

    public String getIndexBaseQuantityConcept() {
        return indexBaseQuantityConcept;
    }

    public void setIndexBaseQuantityConcept(String indexBaseQuantityConcept) {
        this.indexBaseQuantityConcept = indexBaseQuantityConcept;
    }

    public String getUnitCodelistUrn() {
        return unitCodelistUrn;
    }

    public void setUnitCodelistUrn(String unitCodelistUrn) {
        this.unitCodelistUrn = unitCodelistUrn;
    }

    public String getUnitMultiplierCodelistUrn() {
        return unitMultiplierCodelistUrn;
    }

    public void setUnitMultiplierCodelistUrn(String unitMultiplierCodelistUrn) {
        this.unitMultiplierCodelistUrn = unitMultiplierCodelistUrn;
    }

    public String getIndexBaseLocationCodelistUrn() {
        return indexBaseLocationCodelistUrn;
    }

    public void setIndexBaseLocationCodelistUrn(String indexBaseLocationCodelistUrn) {
        this.indexBaseLocationCodelistUrn = indexBaseLocationCodelistUrn;
    }

    public Integer getOrderConfiguration() {
        return orderConfiguration;
    }

    public void setOrderConfiguration(Integer orderConfiguration) {
        this.orderConfiguration = orderConfiguration;
    }

    public String getOrderConfigurationConcatenatedByLevel() {
        return orderConfigurationConcatenatedByLevel;
    }

    public void setOrderConfigurationConcatenatedByLevel(String orderConfigurationConcatenatedByLevel) {
        this.orderConfigurationConcatenatedByLevel = orderConfigurationConcatenatedByLevel;
    }

    public int getLevel() {
        return orderConfigurationConcatenatedByLevel.length() / (SrmConstants.CONCEPT_QUERY_COLUMN_ORDER_LENGTH + 1); // 6 of order + 1 of dot
    }
}
