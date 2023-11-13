package org.siemac.metamac.srm.core.code.invocation.service;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.utils.TranslateExceptions;
import org.siemac.metamac.core.common.lang.LocaleUtil;
import org.siemac.metamac.core.common.util.ServiceContextUtils;
import org.siemac.metamac.rest.notices.v1_0.domain.Message;
import org.siemac.metamac.rest.notices.v1_0.domain.Notice;
import org.siemac.metamac.rest.notices.v1_0.domain.ResourceInternal;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacApplicationsEnum;
import org.siemac.metamac.rest.notices.v1_0.domain.enume.MetamacRolesEnum;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.MessageBuilder;
import org.siemac.metamac.rest.notices.v1_0.domain.utils.NoticeBuilder;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionUtils;
import org.siemac.metamac.srm.core.conf.SrmConfiguration;
import org.siemac.metamac.srm.core.invocation.MetamacApisLocator;
import org.siemac.metamac.srm.core.invocation.utils.RestMapper;
import org.siemac.metamac.srm.core.notices.ServiceNoticeAction;
import org.siemac.metamac.srm.core.notices.ServiceNoticeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(NoticesRestInternalService.BEAN_ID)
public class NoticesRestInternalServiceImpl implements NoticesRestInternalService {

    private static final String                           ERROR                                  = "ERROR";

    private static Logger                                 logger                                 = LoggerFactory.getLogger(NoticesRestInternalServiceImpl.class);
    private static Map<TypeExternalArtefactsEnum, String> resourceTypeExceptionParameterMappings = new HashMap<TypeExternalArtefactsEnum, String>();

    static {
        resourceTypeExceptionParameterMappings.put(TypeExternalArtefactsEnum.VARIABLE_ELEMENT, ServiceExceptionParameters.VARIABLE_ELEMENT);
    }

    @Autowired
    private MetamacApisLocator  restApiLocator;

    @Autowired
    private SrmConfiguration    configurationService;

    @Autowired
    private TranslateExceptions translateExceptions;

    @Autowired
    private RestMapper          restMapper;

    @Override
    public void createErrorBackgroundNotification(String user, String actionCode, MetamacException exception) {
        try {
            Locale locale = configurationService.retrieveLanguageDefaultLocale();

            Throwable localisedException = translateExceptions.translateException(locale, exception);
            String localisedMessage = localisedException.getMessage();
            localisedMessage = ERROR + localisedMessage;

            createBackgroundNotification(actionCode, localisedMessage, user);
        } catch (MetamacException e) {
            logger.error("Error creating createErrorBackgroundNotification:", e);
        }

    }

    @Override
    public void createSuccessBackgroundNotification(String user, String actionCode, String successMessageCode, MetamacException exception, Serializable... successMessageParameters) {
        try {
            Locale locale = configurationService.retrieveLanguageDefaultLocale();
            String localisedMessage = LocaleUtil.getMessageForCode(successMessageCode, locale);
            localisedMessage = MessageFormat.format(localisedMessage, successMessageParameters);

            Throwable localisedException = translateExceptions.translateException(locale, exception);
            localisedMessage += "\r\n" + localisedException.getMessage();

            createBackgroundNotification(actionCode, localisedMessage, user);
        } catch (MetamacException e) {
            logger.error("Error creating createSuccessBackgroundNotification:", e);
        }
    }

    @Override
    public void createSuccessBackgroundNotification(String user, String actionCode, String successMessageCode, Serializable... successMessageParameters) {
        try {
            Locale locale = configurationService.retrieveLanguageDefaultLocale();
            String localisedMessage = LocaleUtil.getMessageForCode(successMessageCode, locale);
            localisedMessage = MessageFormat.format(localisedMessage, successMessageParameters);
            createBackgroundNotification(actionCode, localisedMessage, user);
        } catch (MetamacException e) {
            logger.error("Error creating createSuccessBackgroundNotification:", e);
        }
    }

    private void createBackgroundNotification(String actionCode, String message, String user) throws MetamacException {
        try {
            Locale locale = configurationService.retrieveLanguageDefaultLocale();
            String subject = LocaleUtil.getMessageForCode(actionCode, locale);
            String sendingApp = MetamacApplicationsEnum.GESTOR_RECURSOS_ESTRUCTURALES.getName();
            Notice notification = null;
            if (user != null) {
            // @formatter:off
             notification = NoticeBuilder.notification()
                                                .withMessagesWithoutResources(message)
                                                .withSendingApplication(sendingApp)
                                                .withReceivers(user)
                                                .withSendingUser(user)
                                                .withSubject(subject)
                                                .build();
            // @formatter:on
            } else {
             // @formatter:off
                notification = NoticeBuilder.notification()
                                                    .withMessagesWithoutResources(message)
                                                    .withSendingApplication(sendingApp)
                                                    .withRoles(MetamacRolesEnum.ADMINISTRADOR)
                                                    .withSubject(subject)
                                                    .build();
                // @formatter:on
            }

            restApiLocator.getNoticesRestInternalFacadeV10().createNotice(notification);
        } catch (Exception e) {
            throw manageNoticesInternalRestException(e);
        }
    }

    private void createNotificationForResources(ServiceContext ctx, String user, String actionCode, String messageCode, ResourceInternal[] resources, TypeExternalArtefactsEnum resourceType,
            MetamacRolesEnum[] roles) throws MetamacException {

        MetamacApplicationsEnum[] applications = {MetamacApplicationsEnum.GESTOR_RECURSOS_ESTRUCTURALES};

        String sendingApp = MetamacApplicationsEnum.GESTOR_RECURSOS_ESTRUCTURALES.getName();
        String subject = buildSubject(ctx, actionCode, resourceType);
        String messageText = buildMessage(ctx, messageCode, resourceType);

        Message message = MessageBuilder.message().withText(messageText).withResources(resources).build();

        NoticeBuilder notification = NoticeBuilder.notification().withMessages(message).withSendingApplication(sendingApp).withRoles(MetamacRolesEnum.ADMINISTRADOR).withSubject(subject)
                .withApplications(applications);
        if (roles != null) {
            notification.withRoles(roles);
        }

        try {
            restApiLocator.getNoticesRestInternalFacadeV10().createNotice(notification.build());
        } catch (Exception e) {
            throw manageNoticesInternalRestException(e);
        }
    }

    private String buildSubject(ServiceContext ctx, String actionCode, TypeExternalArtefactsEnum resourceType) {
        Locale locale = ServiceContextUtils.getLocale(ctx);
        String localizedMessage = getLocalizedMessage(locale, actionCode);
        String localizedResourceTypeName = getLocalizedResourceTypeName(locale, resourceType);
        localizedMessage = MessageFormat.format(localizedMessage, localizedResourceTypeName);
        return localizedMessage;
    }

    private String buildMessage(ServiceContext ctx, String messageCode, TypeExternalArtefactsEnum resourceType) {
        Locale locale = ServiceContextUtils.getLocale(ctx);
        return getLocalizedMessage(locale, messageCode);
    }

    private String getLocalizedResourceTypeName(Locale locale, TypeExternalArtefactsEnum resourceType) {
        String resourceTypeCode = getResourceNameParameter(resourceType);
        return getLocalizedMessage(locale, resourceTypeCode);
    }

    private String getLocalizedMessage(Locale locale, String code) {
        return LocaleUtil.getMessageForCode(code, locale);
    }

    @Override
    public void createVariableElementStreamMessageErrorNotification(ServiceContext serviceContext, String user, List<VariableElement> variableElements, TypeExternalArtefactsEnum resourceType)
            throws MetamacException {

        List<ResourceInternal> resources = new ArrayList<ResourceInternal>();
        for (VariableElement variableElement : variableElements) {
            resources.add(restMapper.variableElementToResourceInternal(variableElement));
        }

        createNotificationForResources(serviceContext, user, ServiceNoticeAction.STREAM_MESSAGE_SEND, ServiceNoticeMessage.STREAM_MESSAGE_SEND_VARIABLE_ELEMENT_ERROR,
                resources.toArray(new ResourceInternal[resources.size()]), resourceType, null);

    }

    // -------------------------------------------------------------------------------------------------
    // PRIVATE UTILS
    // -------------------------------------------------------------------------------------------------

    private MetamacException manageNoticesInternalRestException(Exception e) throws MetamacException {
        return ServiceExceptionUtils.manageMetamacRestException(e, ServiceExceptionParameters.API_NOTICES_INTERNAL, restApiLocator.getNoticesRestInternalFacadeV10());
    }

    public static String getResourceNameParameter(TypeExternalArtefactsEnum type) {
        return resourceTypeExceptionParameterMappings.get(type);
    }
}
