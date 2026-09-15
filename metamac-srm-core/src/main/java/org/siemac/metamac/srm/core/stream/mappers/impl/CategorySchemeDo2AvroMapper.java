package org.siemac.metamac.srm.core.stream.mappers.impl;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.code.enume.domain.RelatedResourceSrmTypeEnum;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.stream.mappers.Do2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;
import org.siemac.metamac.srm.core.util.HtmlLinkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategorySchemeDo2AvroMapper implements Do2AvroMapper<CategorySchemeVersionMetamac, CategorySchemeAvro> {

    @Autowired
    private InternationalStringDo2AvroMapper internationalStringMapper;

    @Autowired
    private OrganisationDo2AvroMapper        maintainerMapper;

    @Autowired
    private CategorisationDo2AvroMapper      categorisationMapper;

    @Autowired
    private ProcStatusEnumDo2AvroMapper      procStatusMapper;

    @Autowired
    private SrmConfiguration                 srmConfiguration;

    @Override
    public CategorySchemeAvro toAvro(CategorySchemeVersionMetamac source) throws MetamacException {
        if (source == null) {
            return null;
        }
        return CategorySchemeAvro.newBuilder().setUuid(source.getUuid()).setName(internationalStringMapper.toAvro(source.getMaintainableArtefact().getName()))
                .setUrn(source.getMaintainableArtefact().getUrn()).setVersion(source.getVersion()).setUriProvider(source.getMaintainableArtefact().getUriProvider())
                .setUrnProvider(source.getMaintainableArtefact().getUrnProvider()).setDescription(internationalStringMapper.toAvro(source.getMaintainableArtefact().getDescription()))
                .setIsPartial(source.getIsPartial()).setFinalLogic(source.getMaintainableArtefact().getFinalLogic())
                .setLatestVersionNumberFinal(source.getMaintainableArtefact().getLatestVersionNumberFinal())
                .setLatestVersionNumberPublic(source.getMaintainableArtefact().getLatestVersionNumberPublic()).setMaintainer(maintainerMapper.toAvro(source.getMaintainableArtefact().getMaintainer()))
                .setProcStatus(procStatusMapper.toAvro(source.getLifeCycleMetadata().getProcStatus()))
                .setCategorizations(categorisationMapper.toAvros(source.getMaintainableArtefact().getCategorisations()))
                .setVisualizerHtmlLink(HtmlLinkUtil.getCodelistVisualizerHtmlLink(source.getMaintainableArtefact(), null, RelatedResourceSrmTypeEnum.CATEGORY_SCHEME, srmConfiguration.retrieveSrmVisualizerUrl())).build();
    }
}
