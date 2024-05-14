package org.siemac.metamac.srm.core.stream.mappers.impl;

import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.stream.mappers.Do2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.VariableElementAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VariableElementDo2AvroMapper implements Do2AvroMapper<VariableElement, VariableElementAvro> {

    @Autowired
    private InternationalStringDo2AvroMapper        internationalStringMapper;

    @Autowired
    private VariableDo2AvroMapper                   variableMapper;

    @Autowired
    private CodeMetamacDo2RelatedResourceAvroMapper codeMetamacMapper;

    @Override
    public VariableElementAvro toAvro(VariableElement source) {
        if (source == null) {
            return null;
        }

        return VariableElementAvro.newBuilder().setUuid(source.getUuid()).setCode(source.getIdentifiableArtefact().getCode()).setShortName(internationalStringMapper.toAvro(source.getShortName()))
                .setUrn(source.getIdentifiableArtefact().getUrn()).setUriProvider(source.getIdentifiableArtefact().getUriProvider()).setUrnProvider(source.getIdentifiableArtefact().getUrnProvider())
                .setVariable(variableMapper.toAvro(source.getVariable())).setGeographicGranularities(codeMetamacMapper.toAvro(source.getGeographicalGranularity())).setLatitud(source.getLatitude())
                .setLongitud(source.getLatitude()).setValidTo(DateTimeDo2AvroMapper.do2Avro(source.getValidTo())).setValidFrom(DateTimeDo2AvroMapper.do2Avro(source.getValidFrom())).build();
    }

}
