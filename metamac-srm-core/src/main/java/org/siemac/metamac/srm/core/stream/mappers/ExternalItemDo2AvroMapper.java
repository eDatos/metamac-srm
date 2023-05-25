package org.siemac.metamac.srm.core.stream.mappers;

import org.siemac.metamac.srm.core.stream.mappers.impl.InternationalStringDo2AvroMapper;
import org.siemac.metamac.srm.core.stream.mappers.impl.TypeExternalArtefactsEnumDo2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.ExternalItemAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.arte.statistic.sdmx.srm.core.common.domain.ExternalItem;

@Component
public class ExternalItemDo2AvroMapper {

    @Autowired
    private InternationalStringDo2AvroMapper internationalStringMapper;

    protected ExternalItemDo2AvroMapper() {
    }

    public ExternalItemAvro do2Avro(ExternalItem source) {
        ExternalItemAvro target = null;
        if (source != null) {
            target = ExternalItemAvro.newBuilder().setCode(source.getCode()).setCodeNested(source.getCodeNested()).setManagementAppUrl(source.getManagementAppUrl())
                    .setTitle(internationalStringMapper.toAvro(source.getTitle())).setType(TypeExternalArtefactsEnumDo2AvroMapper.do2Avro(source.getType())).setUrn(source.getUrn())
                    .setUrnProvider(source.getUrnProvider()).build();
        }
        return target;

    }

}
