package org.siemac.metamac.srm.web.client.model.ds;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class AnnotationTypeDS extends DataSource {

    public static String UUID  = "annotation-type-uuid";
    public static String TITLE = "annotation-type-title";
    public static String CODE  = "annotation-type-code";
    public static String DTO   = "annotation-type-dto";

    public AnnotationTypeDS() {
        DataSourceTextField uuid = new DataSourceTextField(UUID, getConstants().adminAnnotationTypeUuid());
        uuid.setPrimaryKey(true);
        addField(uuid);
    }

}
