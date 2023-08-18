package org.siemac.metamac.srm.core.common;

import org.joda.time.DateTime;
import org.siemac.metamac.common.test.utils.MetamacMocks;
import org.siemac.metamac.srm.core.base.domain.ResourceElement;
import org.siemac.metamac.srm.core.base.utils.BaseDoMocks;

import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;

public abstract class SrmResourceElementMock extends BaseDoMocks {

    // Resource element is abstract class and it can not be instantiate. An implementation must be passed by parameter

    public static void mockResourceElement(ResourceElement resourceElement) {
        resourceElement.setIdentifiableArtefact(new IdentifiableArtefact());
        mockIdentifiableArtefact(resourceElement.getIdentifiableArtefact(), "code-" + MetamacMocks.mockString(5), null);
        resourceElement.setShortName(BaseDoMocks.mockInternationalString());
        resourceElement.setComment(BaseDoMocks.mockInternationalString());
        resourceElement.setValidFrom(new DateTime(2012, 10, 1, 10, 12, 13, 14));
        resourceElement.setValidTo(new DateTime(2013, 10, 1, 10, 12, 13, 14));
    }

    public static void mockResourceElementAllMetadata(ResourceElement resourceElement, String codeCategoryElement) {
        resourceElement.getUuid();
        resourceElement.setVersion(Long.valueOf(1));
        resourceElement.setCreatedBy("userCreatedBy");
        resourceElement.setCreatedDate(new DateTime(2012, 10, 1, 10, 12, 13, 14));
        resourceElement.setLastUpdatedBy("userLastUpdated");
        resourceElement.setLastUpdated(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        resourceElement.setUpdateDate(new DateTime(2012, 11, 5, 10, 12, 13, 14));
        resourceElement.setShortName(mockInternationalStringFixedValues("shortName", codeCategoryElement));
        resourceElement.setComment(mockInternationalStringFixedValues("comment", codeCategoryElement));
        resourceElement.setValidFrom(new DateTime(2012, 10, 1, 10, 12, 13, 14));
        resourceElement.setValidTo(new DateTime(2013, 10, 1, 10, 12, 13, 14));
        resourceElement.setIdentifiableArtefact(new IdentifiableArtefact());
        mockIdentifiableArtefactFixedValues(resourceElement.getIdentifiableArtefact(), codeCategoryElement, null);
        resourceElement.getIdentifiableArtefact().setUrnProvider(resourceElement.getIdentifiableArtefact().getUrn());
    }
}
