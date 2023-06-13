package org.siemac.metamac.srm.web.client.admin.view.handlers;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;
import com.gwtplatform.mvp.client.UiHandlers;

public interface AdminAnnotationTypeUiHandlers extends UiHandlers {

    void deleteAnnotationTypes(List<String> annotationTypesUuids, final int firstResult);

    void saveAnnotationType(int currentPage, AnnotationTypeDto annotationTypeDto);

    void retrieveAnnotationTypes(final int firstResult);
}
