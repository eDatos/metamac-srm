package org.siemac.metamac.srm.web.shared.master;

import java.util.List;

import org.siemac.metamac.srm.core.master.dto.EniSchemaCatalogDto;
import com.gwtplatform.dispatch.annotation.GenDispatch;
import com.gwtplatform.dispatch.annotation.Out;

@GenDispatch(isSecure = false)
public class GetEniSchemaCatalogs {

    @Out(1)
    List<EniSchemaCatalogDto>  eniSchemaCatalogs;
}
