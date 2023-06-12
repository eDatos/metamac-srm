package org.siemac.metamac.srm.core.stream.mappers.impl;

import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.core.stream.message.TypeExternalArtefactsEnumAvro;

public class TypeExternalArtefactsEnumDo2AvroMapper {

    protected TypeExternalArtefactsEnumDo2AvroMapper() {
    }

    public static TypeExternalArtefactsEnumAvro do2Avro(TypeExternalArtefactsEnum source) {
        TypeExternalArtefactsEnumAvro target = null;
        for (TypeExternalArtefactsEnum current : TypeExternalArtefactsEnum.values()) {
            if (current != null && source != null && source.name().equals(current.name())) {
                target = TypeExternalArtefactsEnumAvro.valueOf(current.name());
            }
        }
        return target;
    }

}
