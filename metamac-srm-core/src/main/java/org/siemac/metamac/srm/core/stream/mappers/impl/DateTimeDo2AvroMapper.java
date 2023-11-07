package org.siemac.metamac.srm.core.stream.mappers.impl;

import org.joda.time.DateTime;
import org.siemac.metamac.srm.core.stream.message.DatetimeAvro;

public class DateTimeDo2AvroMapper {

    protected DateTimeDo2AvroMapper() {
    }

    public static DatetimeAvro do2Avro(DateTime source) {
        DatetimeAvro target = null;
        if (source != null) {
            target = DatetimeAvro.newBuilder().setInstant(source.getMillis()).build();
        }
        return target;
    }

}
