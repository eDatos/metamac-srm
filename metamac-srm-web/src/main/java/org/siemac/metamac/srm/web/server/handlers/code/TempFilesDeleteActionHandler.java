package org.siemac.metamac.srm.web.server.handlers.code;

import java.io.File;
import java.security.AccessController;

import org.siemac.metamac.srm.web.shared.code.TempFilesDeleteAction;
import org.siemac.metamac.srm.web.shared.code.TempFilesDeleteResult;
import org.siemac.metamac.web.common.server.handlers.SecurityActionHandler;
import org.springframework.stereotype.Component;

import com.gwtplatform.dispatch.shared.ActionException;

import sun.security.action.GetPropertyAction;

@Component
public class TempFilesDeleteActionHandler extends SecurityActionHandler<TempFilesDeleteAction, TempFilesDeleteResult> {

    public TempFilesDeleteActionHandler() {
        super(TempFilesDeleteAction.class);
    }
    @Override
    public TempFilesDeleteResult executeSecurityAction(TempFilesDeleteAction action) throws ActionException {
        File tmpdir = new File(AccessController.doPrivileged(new GetPropertyAction("java.io.tmpdir")));
        File file = new File(tmpdir.getPath() + "\\" + action.getFileName());
        if (file.exists()) {
            file.delete();
        }
        return new TempFilesDeleteResult();
    }
}
