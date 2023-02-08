package org.siemac.metamac.srm.web.client.events;

import java.util.List;

import org.siemac.metamac.srm.core.master.dto.EniSchemaCatalogDto;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class UpdateEniSchemaCatalogEvent extends GwtEvent<UpdateEniSchemaCatalogEvent.UpdateEniSchemaCatalogHandler> {

    public interface UpdateEniSchemaCatalogHandler extends EventHandler {

        void onUpdateEniSchemaCatalog(UpdateEniSchemaCatalogEvent event);
    }

    private static Type<UpdateEniSchemaCatalogHandler> TYPE = new Type<UpdateEniSchemaCatalogHandler>();

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<UpdateEniSchemaCatalogHandler> getAssociatedType() {
        return TYPE;
    }

    public static void fire(HasHandlers source, List<EniSchemaCatalogDto> eniSchemaCatalog) {
        if (TYPE != null) {
            source.fireEvent(new UpdateEniSchemaCatalogEvent(eniSchemaCatalog));
        }
    }

    @Override
    protected void dispatch(UpdateEniSchemaCatalogHandler handler) {
        handler.onUpdateEniSchemaCatalog(this);
    }

    private final List<EniSchemaCatalogDto> eniSchemaCatalogs;

    public UpdateEniSchemaCatalogEvent(List<EniSchemaCatalogDto> eniSchemaCatalogs) {
        this.eniSchemaCatalogs = eniSchemaCatalogs;

    }

    public List<EniSchemaCatalogDto> getEniSchemaCatalogDto() {
        return eniSchemaCatalogs;
    }

    public static Type<UpdateEniSchemaCatalogHandler> getType() {
        return TYPE;
    }
}
