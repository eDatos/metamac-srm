package org.siemac.metamac.srm.core.category.repositoryimpl;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder;
import org.fornax.cartridges.sculptor.framework.domain.PagedResult;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.srm.core.category.domain.CategoryElement;
import org.siemac.metamac.srm.core.category.domain.CategoryElementProperties;
import org.springframework.stereotype.Repository;

/**
 * Repository implementation for CategoryElement
 */
@Repository("categoryElementRepository")
public class CategoryElementRepositoryImpl extends CategoryElementRepositoryBase {

    public CategoryElementRepositoryImpl() {
    }

    public CategoryElement findByUrn(String urn) {

        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CategoryElement.class).withProperty(CategoryElementProperties.identifiableArtefact().urn()).ignoreCaseEq(urn)
                .distinctRoot().build();

        PagingParameter pagingParameter = PagingParameter.pageAccess(1, 1);
        PagedResult<CategoryElement> categoryElement = findByCondition(conditions, pagingParameter);

        if (categoryElement.getValues().size() == 1) {
            return categoryElement.getValues().iterator().next();
        }

        return null;
    }

    public List<CategoryElement> findByUrnIn(List<String> urns) {
        List<ConditionalCriteria> conditions = ConditionalCriteriaBuilder.criteriaFor(CategoryElement.class).withProperty(CategoryElementProperties.identifiableArtefact().urn()).in(urns)
                .distinctRoot().build();

        PagingParameter pagingParameter = PagingParameter.noLimits();
        PagedResult<CategoryElement> categoryElement = findByCondition(conditions, pagingParameter);

        return categoryElement.getValues();
    }
}
