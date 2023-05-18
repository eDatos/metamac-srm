package org.siemac.metamac.srm.web.client.admin.view.handlers;

import java.util.List;

import org.siemac.metamac.srm.web.shared.criteria.AnnotationTypeCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface AdminAnnotationTypeUiHandlers extends UiHandlers {

    void deleteAnnotationTypes(List<String> annotationTypesCodes, int firstResult);

    void saveAnnotationType(int currentPage, AnnotationTypeDto annotationTypeDto);

    void retrieveAnnotationType(AnnotationTypeCriteria criteria);
}
