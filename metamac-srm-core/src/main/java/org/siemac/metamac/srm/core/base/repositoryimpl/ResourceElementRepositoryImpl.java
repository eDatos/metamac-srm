package org.siemac.metamac.srm.core.base.repositoryimpl;

import org.siemac.metamac.srm.core.base.domain.ResourceElement;

import org.springframework.stereotype.Repository;

/**
 * Repository implementation for ResourceElement
 */
@Repository("resourceElementRepository")
public class ResourceElementRepositoryImpl extends ResourceElementRepositoryBase {
    public ResourceElementRepositoryImpl() {
    }

    public ResourceElement findByUrn(String urn) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("findByUrn not implemented");

    }
}
