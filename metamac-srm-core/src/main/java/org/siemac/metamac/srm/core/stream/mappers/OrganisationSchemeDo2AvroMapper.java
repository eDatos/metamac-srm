package org.siemac.metamac.srm.core.stream.mappers;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.enume.domain.RelatedResourceSrmTypeEnum;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.organisation.domain.OrganisationSchemeVersionMetamac;
import org.siemac.metamac.srm.core.stream.mappers.impl.CategorisationDo2AvroMapper;
import org.siemac.metamac.srm.core.stream.mappers.impl.InternationalStringDo2AvroMapper;
import org.siemac.metamac.srm.core.stream.mappers.impl.OrganisationDo2AvroMapper;
import org.siemac.metamac.srm.core.stream.mappers.impl.OrganisationSchemeTypeEnumDo2AvroMapper;
import org.siemac.metamac.srm.core.stream.mappers.impl.ProcStatusEnumDo2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeAvro;
import org.siemac.metamac.srm.core.util.HtmlLinkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganisationSchemeDo2AvroMapper implements Do2AvroMapper<OrganisationSchemeVersionMetamac, OrganisationSchemeAvro> {

    @Autowired
    private InternationalStringDo2AvroMapper        internationalStringMapper;

    @Autowired
    private OrganisationDo2AvroMapper               maintainerMapper;

    @Autowired
    private CategorisationDo2AvroMapper             categorisationMapper;

    @Autowired
    private ProcStatusEnumDo2AvroMapper             procStatusMapper;

    @Autowired
    private OrganisationSchemeTypeEnumDo2AvroMapper organisationSchemeTypeEnumDo2AvroMapper;

    @Autowired
    private SrmConfiguration                        srmConfiguration;

    @Override
    public OrganisationSchemeAvro toAvro(OrganisationSchemeVersionMetamac source) throws MetamacException {
        if (source == null) {
            return null;
        }
        return OrganisationSchemeAvro.newBuilder().setUuid(source.getUuid()).setName(internationalStringMapper.toAvro(source.getMaintainableArtefact().getName()))
                .setUrn(source.getMaintainableArtefact().getUrn()).setVersion(source.getVersion()).setUriProvider(source.getMaintainableArtefact().getUriProvider())
                .setUrnProvider(source.getMaintainableArtefact().getUrnProvider()).setDescription(internationalStringMapper.toAvro(source.getMaintainableArtefact().getDescription()))
                .setIsPartial(source.getIsPartial()).setFinalLogic(source.getMaintainableArtefact().getFinalLogic())
                .setLatestVersionNumberFinal(source.getMaintainableArtefact().getLatestVersionNumberFinal())
                .setLatestVersionNumberPublic(source.getMaintainableArtefact().getLatestVersionNumberPublic()).setMaintainer(maintainerMapper.toAvro(source.getMaintainableArtefact().getMaintainer()))
                .setProcStatus(procStatusMapper.toAvro(source.getLifeCycleMetadata().getProcStatus()))
                .setCategorizations(categorisationMapper.toAvros(source.getMaintainableArtefact().getCategorisations()))
                .setOrganisationSchemeType(organisationSchemeTypeEnumDo2AvroMapper.toAvro(source.getOrganisationSchemeType()))
                .setVisualizerHtmlLink(HtmlLinkUtil.getCodelistVisualizerHtmlLink(source.getMaintainableArtefact(), null, RelatedResourceSrmTypeEnum.ORGANISATION_SCHEME, srmConfiguration)).build();
    }
}
