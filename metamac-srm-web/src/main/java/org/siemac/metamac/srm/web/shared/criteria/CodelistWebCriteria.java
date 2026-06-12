package org.siemac.metamac.srm.web.shared.criteria;

import java.util.List;

import org.siemac.metamac.srm.core.code.enume.domain.AccessTypeEnum;
import org.siemac.metamac.srm.core.code.enume.domain.NormalizationTypeEnum;

public class CodelistWebCriteria extends VersionableResourceWebCriteria {

    private static final long serialVersionUID = 1L;

    private String conceptUrn;
    private String codelistFamilyUrn;
    private AccessTypeEnum accessType;
    private String isNotCodelistUrn;
    private String variableUrn;
    private String variableElementUrn;
    private String variableFamilyUrn;
    private String codelisUrnToReplaceCodelist;
    private NormalizationTypeEnum normalizationType;
    private List<Long> codeListIds;
    private boolean deduplicateIgnoringVersion;

    public CodelistWebCriteria() {
    }

    public CodelistWebCriteria(String criteria) {
        super(criteria);
    }

    public CodelistWebCriteria(String criteria, String conceptUrn) {
        super(criteria);
        this.conceptUrn = conceptUrn;
    }

    public String getConceptUrn() {
        return conceptUrn;
    }

    public void setConceptUrn(String conceptUrn) {
        this.conceptUrn = conceptUrn;
    }

    public String getCodelistFamilyUrn() {
        return codelistFamilyUrn;
    }

    public void setCodelistFamilyUrn(String codelistFamilyUrn) {
        this.codelistFamilyUrn = codelistFamilyUrn;
    }

    public AccessTypeEnum getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessTypeEnum accessType) {
        this.accessType = accessType;
    }

    public String getIsNotCodelistUrn() {
        return isNotCodelistUrn;
    }

    public void setIsNotCodelistUrn(String isNotCodelistUrn) {
        this.isNotCodelistUrn = isNotCodelistUrn;
    }

    public String getVariableUrn() {
        return variableUrn;
    }

    public void setVariableUrn(String variableUrn) {
        this.variableUrn = variableUrn;
    }

    public String getVariableElementUrn() {
        return variableElementUrn;
    }

    public void setVariableElementUrn(String variableElementUrn) {
        this.variableElementUrn = variableElementUrn;
    }

    public String getVariableFamilyUrn() {
        return variableFamilyUrn;
    }

    public void setVariableFamilyUrn(String variableFamilyUrn) {
        this.variableFamilyUrn = variableFamilyUrn;
    }

    public String getCodelisUrnToReplaceCodelist() {
        return codelisUrnToReplaceCodelist;
    }

    public void setCodelisUrnToReplaceCodelist(String codelisUrnToReplaceCodelist) {
        this.codelisUrnToReplaceCodelist = codelisUrnToReplaceCodelist;
    }

    public NormalizationTypeEnum getNormalizationType() {
        return normalizationType;
    }

    public void setNormalizationType(NormalizationTypeEnum normalizationType) {
        this.normalizationType = normalizationType;
    }

    public List<Long> getCodeListIds() {
        return codeListIds;
    }

    public void setCodeListIds(List<Long> codeListIds) {
        this.codeListIds = codeListIds;
    }

    public boolean isDeduplicateIgnoringVersion() {
        return deduplicateIgnoringVersion;
    }

    public void setDeduplicateIgnoringVersion(boolean deduplicateIgnoringVersion) {
        this.deduplicateIgnoringVersion = deduplicateIgnoringVersion;
    }
}
