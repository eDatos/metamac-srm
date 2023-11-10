package org.siemac.metamac.srm.web.server.utils;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.code.dto.VariableElementDto;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.serviceimpl.result.SendStreamMessageResult;
import org.siemac.metamac.srm.web.server.rest.NoticesRestInternalFacade;
import org.siemac.metamac.web.common.server.utils.WebExceptionUtils;
import org.siemac.metamac.web.common.shared.exception.MetamacWebException;

public class StreamUtils {

    private StreamUtils() {
        // nothing to do
    }

    public static MetamacWebException retrieveMultipleKafkaSendingErrors(ServiceContext serviceContext, NoticesRestInternalFacade noticesRestInternalFacade, List<MetamacExceptionItem> exceptionItems,
            List<VariableElementDto> variableElementsDto) {
        MetamacWebException webExceptions = null;
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            MetamacException exceptions = MetamacExceptionBuilder.builder().withExceptionItems(exceptionItems).build();
            webExceptions = WebExceptionUtils.createMetamacWebException(exceptions);
            try {
                noticesRestInternalFacade.createStreamMessageErrorNotification(serviceContext, variableElementsDto, TypeExternalArtefactsEnum.VARIABLE_ELEMENT);
            } catch (MetamacWebException noticeException) {
                webExceptions.getWebExceptionItems().addAll(noticeException.getWebExceptionItems());
            }
        }
        return webExceptions;
    }

    public static void getResendKafkaVariableElementMessageErrors(List<VariableElementDto> variableElementsDto, VariableElementDto variableElementDto, SendStreamMessageResult result,
            List<MetamacExceptionItem> exceptionItems) {
        if (!result.isOk()) {
            MetamacExceptionItem exceptionVariableElement = new MetamacExceptionItem(ServiceExceptionType.STREAM_MESSAGING_RESEND_VARIABLE_ELEMENT_MESSAGE_ERROR, variableElementDto.getCode());

            exceptionVariableElement.getExceptionItems().addAll(result.getMainException().getExceptionItems());
            for (MetamacException exception : result.getSecondaryExceptions()) {
                exceptionVariableElement.getExceptionItems().addAll(exception.getExceptionItems());
            }
            exceptionItems.add(exceptionVariableElement);
            variableElementsDto.add(variableElementDto);
        }
    }

}
