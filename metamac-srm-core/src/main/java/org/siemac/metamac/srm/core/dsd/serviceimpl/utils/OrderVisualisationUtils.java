package org.siemac.metamac.srm.core.dsd.serviceimpl.utils;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.exception.MetamacException;

import com.arte.statistic.sdmx.srm.core.base.domain.ComponentRepository;
import com.arte.statistic.sdmx.srm.core.base.domain.Representation;
import com.arte.statistic.sdmx.srm.core.structure.domain.DimensionComponent;

public class OrderVisualisationUtils {

    private OrderVisualisationUtils() {
        // without impl
    }

    public static Representation findOrderVisualisationCanBeDisplayOrderForDsdDimension(ComponentRepository componentRepository, List<ConditionalCriteria> conditions, PagingParameter pagingParameter,
            String dimensionUrn) throws MetamacException {

        // Validation
        DsdsMetamacInvocationValidator.findOrderVisualisationCanBeDisplayOrderForDsdDimensionByCondition(conditions, pagingParameter, dimensionUrn, null);

        DimensionComponent dimensionComponent = (DimensionComponent) componentRepository.findByUrn(dimensionUrn);

        Representation representation = dimensionComponent.getLocalRepresentation();
        if (representation == null) {
            representation = dimensionComponent.getCptIdRef().getCoreRepresentation();
        }

        return representation;

    }
}
