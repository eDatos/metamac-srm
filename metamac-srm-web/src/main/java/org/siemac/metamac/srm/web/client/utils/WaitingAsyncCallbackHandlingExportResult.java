package org.siemac.metamac.srm.web.client.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getMessages;

import org.siemac.metamac.srm.web.shared.ExportSDMXResourceResult;
import org.siemac.metamac.srm.web.shared.code.TempFilesDeleteAction;
import org.siemac.metamac.srm.web.shared.code.TempFilesDeleteResult;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;

import com.google.gwt.event.shared.HasHandlers;
import com.gwtplatform.dispatch.shared.DispatchAsync;

public class WaitingAsyncCallbackHandlingExportResult extends WaitingAsyncCallbackHandlingError<ExportSDMXResourceResult> {

    private final HasHandlers   source;
    private final DispatchAsync dispatcher;

    public WaitingAsyncCallbackHandlingExportResult(HasHandlers source) {
        super(source);
        this.source = source;
        this.dispatcher = null;
    }

    public WaitingAsyncCallbackHandlingExportResult(HasHandlers source, DispatchAsync dispatcher) {
        super(source);
        this.source = source;
        this.dispatcher = dispatcher;
    }

    @Override
    public void onWaitSuccess(ExportSDMXResourceResult result) {
        if (result.getFileName() != null && result.getException() != null) {
            ShowMessageEvent.fireWarningMessageWithError(source, getMessages().exportationWithErrorsWarning(), result.getException());
            org.siemac.metamac.srm.web.client.utils.CommonUtils.downloadFile(result.getFileName());
            if (dispatcher != null) {
                deleteTemporalFile(result.getFileName());
            }
        } else if (result.getFileName() != null && result.getException() == null) {
            org.siemac.metamac.srm.web.client.utils.CommonUtils.downloadFile(result.getFileName());
            if (dispatcher != null) {
                deleteTemporalFile(result.getFileName());
            }
        } else {
            ShowMessageEvent.fireErrorMessage(source, result.getException());
        }
    }

    private void deleteTemporalFile(String fileName) {
        dispatcher.execute(new TempFilesDeleteAction(fileName), new WaitingAsyncCallbackHandlingError<TempFilesDeleteResult>(source) {
            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(source, caught);
            }
            @Override
            public void onWaitSuccess(TempFilesDeleteResult result) {
            }
        });
    }
}
