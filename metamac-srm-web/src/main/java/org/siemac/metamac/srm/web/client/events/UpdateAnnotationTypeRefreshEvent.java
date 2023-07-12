package org.siemac.metamac.srm.web.client.events;

import java.util.List;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.AnnotationTypeDto;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class UpdateAnnotationTypeRefreshEvent extends GwtEvent<UpdateAnnotationTypeRefreshEvent.UpdateAnnotationTypeRefreshHandler> {

    public interface UpdateAnnotationTypeRefreshHandler extends EventHandler {

        void onUpdateAnnotationUpdateNewType(UpdateAnnotationTypeRefreshEvent event);
    }

    private static Type<UpdateAnnotationTypeRefreshHandler> TYPE = new Type<UpdateAnnotationTypeRefreshHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<UpdateAnnotationTypeRefreshHandler> getAssociatedType() {
        return TYPE;
    }

    public static void fire(HasHandlers source, List<AnnotationTypeDto> annotationType) {
        if (TYPE != null) {
            source.fireEvent(new UpdateAnnotationTypeRefreshEvent(annotationType));
        }
    }

    @Override
    protected void dispatch(UpdateAnnotationTypeRefreshHandler handler) {
        handler.onUpdateAnnotationUpdateNewType(this);
    }

    private final List<AnnotationTypeDto> annotationTypes;

    public UpdateAnnotationTypeRefreshEvent(List<AnnotationTypeDto> annotationTypes) {
        this.annotationTypes = annotationTypes;

    }

    public List<AnnotationTypeDto> getAnnotationTypeDto() {
        return annotationTypes;
    }

    public static Type<UpdateAnnotationTypeRefreshHandler> getType() {
        return TYPE;
    }
}
