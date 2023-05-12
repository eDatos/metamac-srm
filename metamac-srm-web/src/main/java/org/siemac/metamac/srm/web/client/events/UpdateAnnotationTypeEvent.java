package org.siemac.metamac.srm.web.client.events;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class UpdateAnnotationTypeEvent extends GwtEvent<UpdateAnnotationTypeEvent.UpdateAnnotationTypeHandler> {

    public interface UpdateAnnotationTypeHandler extends EventHandler {

        void onUpdateAnnotationType(UpdateAnnotationTypeEvent event);
    }

    private static Type<UpdateAnnotationTypeHandler> TYPE = new Type<UpdateAnnotationTypeHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<UpdateAnnotationTypeHandler> getAssociatedType() {
        return TYPE;
    }

    public static void fire(HasHandlers source, List<AnnotationTypeDto> annotationType) {
        if (TYPE != null) {
            source.fireEvent(new UpdateAnnotationTypeEvent(annotationType));
        }
    }

    @Override
    protected void dispatch(UpdateAnnotationTypeHandler handler) {
        handler.onUpdateAnnotationType(this);
    }

    private final List<AnnotationTypeDto> annotationTypes;

    public UpdateAnnotationTypeEvent(List<AnnotationTypeDto> annotationTypes) {
        this.annotationTypes = annotationTypes;

    }

    public List<AnnotationTypeDto> getAnnotationTypeDto() {
        return annotationTypes;
    }

    public static Type<UpdateAnnotationTypeHandler> getType() {
        return TYPE;
    }
}
