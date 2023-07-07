package org.siemac.metamac.srm.core.common;

import org.joda.time.DateTime;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.srm.core.base.domain.ResourceElement;
import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;

import com.arte.statistic.sdmx.srm.core.base.domain.AnnotationType;
import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;

public abstract class SrmResourceElementMock extends BaseDoMocks {

    public static void mockResourceElement(ResourceElement resourceElement, AnnotationType annotationType) {
        resourceElement.setIdentifiableArtefact(new IdentifiableArtefact());
        mockIdentifiableArtefact(resourceElement.getIdentifiableArtefact(), "code-" + MetamacMocks.mockString(5), annotationType);
        resourceElement.setShortName(BaseDoMocks.mockInternationalString());
        resourceElement.setComment(BaseDoMocks.mockInternationalString());
        resourceElement.setValidFrom(new DateTime(2012, 10, 1, 10, 12, 13, 14));
        resourceElement.setValidTo(new DateTime(2013, 10, 1, 10, 12, 13, 14));
    }
}
