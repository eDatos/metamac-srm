package org.siemac.metamac.srm.core.stream.mappers.impl;

import org.siemac.metamac.srm.core.code.domain.CodeMetamac;
import org.siemac.metamac.srm.core.stream.mappers.Do2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.RelatedResourceAvro;
import org.siemac.metamac.srm.core.stream.message.RelatedResourceTypeEnumAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CodeMetamacDo2RelatedResourceAvroMapper implements Do2AvroMapper<CodeMetamac, RelatedResourceAvro> {

    @Autowired
    private InternationalStringDo2AvroMapper internationalStringMapper;

    @Override
    public RelatedResourceAvro toAvro(CodeMetamac source) {
        if (source == null) {
            return null;
        }
        return RelatedResourceAvro.newBuilder().setUuid(source.getUuid()).setUrn(source.getNameableArtefact().getUrn()).setCode(source.getNameableArtefact().getCode())
                .setUrnProvider(source.getNameableArtefact().getUrnProvider()).setVersion(source.getVersion()).setCodeNested(null)
                .setTitle(internationalStringMapper.toAvro(source.getNameableArtefact().getName())).setType(RelatedResourceTypeEnumAvro.CODE).build();
    }
}
