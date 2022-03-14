package org.siemac.metamac.srm.core.serviceimpl.result;

import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;

import java.util.List;

public class ResourceVersionPublicationResult<T> extends SendStreamMessageResult {

    private T resourceVersion;

    public ResourceVersionPublicationResult(StreamMessageStatusEnum status, List<MetamacException> exceptions, T resourceVersion) {
        super(status, exceptions);
        this.resourceVersion = resourceVersion;
    }

    public T getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(T resourceVersion) {
        this.resourceVersion = resourceVersion;
    }
}
