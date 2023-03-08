package org.siemac.metamac.srm.core.stream.mappers.impl;

import org.siemac.metamac.srm.core.stream.mappers.Do2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.OrganisationSchemeTypeEnumAvro;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.v2_1.domain.enume.organisation.domain.OrganisationSchemeTypeEnum;

@Component
public class OrganisationSchemeTypeEnumDo2AvroMapper implements Do2AvroMapper<OrganisationSchemeTypeEnum, OrganisationSchemeTypeEnumAvro> {

    @Override
    public OrganisationSchemeTypeEnumAvro toAvro(OrganisationSchemeTypeEnum source) {
        if (source == null) {
            return null;
        }
        return OrganisationSchemeTypeEnumAvro.valueOf(source.name());
    }
}
