package org.siemac.metamac.srm.core.stream.mappers.impl;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.CodelistVersionMetamac;
import org.siemac.metamac.srm.core.code.enume.domain.RelatedResourceSrmTypeEnum;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.stream.mappers.Do2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.CodelistAvro;
import org.siemac.metamac.srm.core.util.HtmlLinkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CodelistDo2AvroMapper implements Do2AvroMapper<CodelistVersionMetamac, CodelistAvro> {

    @Autowired
    private InternationalStringDo2AvroMapper internationalStringMapper;

    @Autowired
    private AccessTypeEnumDo2AvroMapper      accessTypeMapper;

    @Autowired
    private OrganisationDo2AvroMapper        maintainerMapper;

    @Autowired
    private VariableDo2AvroMapper            variableMapper;

    @Autowired
    private CodelistFamilyDo2AvroMapper      codelistFamilyMapper;

    @Autowired
    private CategorisationDo2AvroMapper      categorisationMapper;

    @Autowired
    private ProcStatusEnumDo2AvroMapper      procStatusMapper;

    @Autowired
    private SrmConfiguration                 srmConfiguration;

    @Override
    public CodelistAvro toAvro(CodelistVersionMetamac source) throws MetamacException {
        if (source == null) {
            return null;
        }
        return CodelistAvro.newBuilder().setUuid(source.getUuid()).setName(internationalStringMapper.toAvro(source.getMaintainableArtefact().getName()))
                .setShortName(internationalStringMapper.toAvro(source.getShortName())).setUrn(source.getMaintainableArtefact().getUrn()).setVersion(source.getVersion())
                .setUriProvider(source.getMaintainableArtefact().getUriProvider()).setUrnProvider(source.getMaintainableArtefact().getUrnProvider())
                .setDescription(internationalStringMapper.toAvro(source.getMaintainableArtefact().getDescription()))
                .setDescriptionSource(internationalStringMapper.toAvro(source.getDescriptionSource())).setIsPartial(source.getIsPartial())
                .setFinalLogic(source.getMaintainableArtefact().getFinalLogic()).setLatestVersionNumberFinal(source.getMaintainableArtefact().getLatestVersionNumberFinal())
                .setLatestVersionNumberPublic(source.getMaintainableArtefact().getLatestVersionNumberPublic()).setMaintainer(maintainerMapper.toAvro(source.getMaintainableArtefact().getMaintainer()))
                .setProcStatus(procStatusMapper.toAvro(source.getLifeCycleMetadata().getProcStatus()))
                .setCategorizations(categorisationMapper.toAvros(source.getMaintainableArtefact().getCategorisations())).setAccessType(accessTypeMapper.toAvro(source.getAccessType()))
                .setIsRecommended(source.getIsRecommended()).setFamily(codelistFamilyMapper.toAvro(source.getFamily())).setVariable(variableMapper.toAvro(source.getVariable()))
                .setVisualizerHtmlLink(HtmlLinkUtil.getCodelistVisualizerHtmlLink(source.getMaintainableArtefact(), null, RelatedResourceSrmTypeEnum.CODELIST, srmConfiguration)).build();
    }
}
