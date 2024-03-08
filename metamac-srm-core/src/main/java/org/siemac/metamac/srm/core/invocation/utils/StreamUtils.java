package org.siemac.metamac.srm.core.invocation.utils;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.invocation.service.NoticesRestInternalService;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.siemac.metamac.srm.core.notices.ServiceNoticeAction;
import org.siemac.metamac.srm.core.notices.ServiceNoticeMessage;
import org.siemac.metamac.srm.core.serviceimpl.result.SendStreamMessageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StreamUtils {

    private static Logger logger = LoggerFactory.getLogger(StreamUtils.class);

    public static void retrieveMultipleKafkaSendingErrors(ServiceContext serviceContext, NoticesRestInternalService noticesRestInternalService, String user, List<MetamacExceptionItem> exceptionItems,
            List<VariableElement> variableElements, String variableUrn) {
        if (!CollectionUtils.isEmpty(exceptionItems)) {
            MetamacException exceptions = MetamacExceptionBuilder.builder().withExceptionItems(exceptionItems).build();
            try {
                noticesRestInternalService.createVariableElementStreamMessageErrorNotification(serviceContext, user, variableElements, TypeExternalArtefactsEnum.VARIABLE_ELEMENT);
            } catch (MetamacException noticeException) {
                logger.error("Error sending errors notifications for resending kafka message for variable elements of variable {}", variableUrn, noticeException);
            }
        } else {
            noticesRestInternalService.createSuccessBackgroundNotification(null, ServiceNoticeAction.PUBLISH_KAFKA_VARIABLE_ELEMENT_MESSAGE_JOB,
                    ServiceNoticeMessage.PUBLISH_KAFKA_VARIABLE_ELEMENT_MESSAGE_JOB_OK, variableUrn);
        }
    }

    public static void getResendKafkaVariableElementMessageErrors(List<VariableElement> variableElements, VariableElement variableElement, SendStreamMessageResult result,
            List<MetamacExceptionItem> exceptionItems) {
        if (result != null && !result.isOk()) {
            MetamacExceptionItem exceptionVariableElement = new MetamacExceptionItem(ServiceExceptionType.STREAM_MESSAGING_RESEND_VARIABLE_ELEMENT_MESSAGE_ERROR,
                    variableElement.getIdentifiableArtefact().getCode());

            exceptionVariableElement.getExceptionItems().addAll(result.getMainException().getExceptionItems());
            for (MetamacException exception : result.getSecondaryExceptions()) {
                exceptionVariableElement.getExceptionItems().addAll(exception.getExceptionItems());
            }
            exceptionItems.add(exceptionVariableElement);
            variableElements.add(variableElement);
        }
    }
}
