package org.siemac.metamac.srm.rest.external.domain.cache;

import org.joda.time.DateTime;
import org.siemac.metamac.rest.common.domain.CacheableResource;

/**
 * Type token for HTTP cache routing. Never instantiated.
 */
public abstract class CategorySchemeCacheableResource implements CacheableResource {
    @Override
    public DateTime getLastModifiedDate() {
        return null; // never called; resolution happens in SrmCacheableResourceServiceImpl
    }
}
