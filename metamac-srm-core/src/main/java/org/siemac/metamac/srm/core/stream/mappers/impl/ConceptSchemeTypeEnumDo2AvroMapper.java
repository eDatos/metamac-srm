package org.siemac.metamac.srm.core.stream.mappers.impl;

import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.core.stream.mappers.Do2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.ConceptSchemeTypeEnumAvro;
import org.springframework.stereotype.Component;

@Component
public class ConceptSchemeTypeEnumDo2AvroMapper implements Do2AvroMapper<ConceptSchemeTypeEnum, ConceptSchemeTypeEnumAvro> {

    @Override
    public ConceptSchemeTypeEnumAvro toAvro(ConceptSchemeTypeEnum source) {
        if (source == null) {
            return null;
        }
        return ConceptSchemeTypeEnumAvro.valueOf(source.name());
    }
}
