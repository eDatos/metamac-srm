package org.siemac.metamac.srm.core.category.repositoryimpl;

import org.siemac.metamac.srm.core.category.domain.CategoryElement;

import org.springframework.stereotype.Repository;

/**
 * Repository implementation for CategoryElement
 */
@Repository("categoryElementRepository")
public class CategoryElementRepositoryImpl extends CategoryElementRepositoryBase {
    public CategoryElementRepositoryImpl() {
    }

    public CategoryElement findByUrn(String urn) {

        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("findByUrn not implemented");

    }
}
