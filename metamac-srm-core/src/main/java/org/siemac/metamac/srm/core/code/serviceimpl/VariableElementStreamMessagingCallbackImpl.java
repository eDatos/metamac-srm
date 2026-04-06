package org.siemac.metamac.srm.core.code.serviceimpl;

import static org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteriaBuilder.criteriaFor;

import java.util.List;

import org.fornax.cartridges.sculptor.framework.accessapi.ConditionalCriteria;
import org.fornax.cartridges.sculptor.framework.domain.PagingParameter;
import org.siemac.metamac.core.common.conf.ConfigurationService;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.code.domain.VariableElement;
import org.siemac.metamac.srm.core.code.domain.VariableElementProperties;
import org.siemac.metamac.srm.core.code.domain.VariableElementRepository;
import org.siemac.metamac.srm.core.enume.domain.StreamMessageStatusEnum;
import org.siemac.metamac.srm.core.serviceapi.StreamMessagingService.StreamMessagingCallback;
import org.siemac.metamac.srm.core.stream.mappers.impl.VariableElementDo2AvroMapper;
import org.siemac.metamac.srm.core.stream.message.VariableElementAvro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("variableElementStreamMessagingCallback")
class VariableElementStreamMessagingCallbackImpl implements StreamMessagingCallback<VariableElement, VariableElementAvro, VariableElementDo2AvroMapper> {

    @Autowired
    private VariableElementDo2AvroMapper variableElementDo2AvroMapper;

    @Autowired
    private ConfigurationService         configurationService;

    @Autowired
    private VariableElementRepository    variableElementRepository;

    @Override
    public String getUniqueIdentifier(VariableElement messageContent) {
        return messageContent.getIdentifiableArtefact().getCode();
    }

    @Override
    public String getProducerRecordKey(VariableElement messageContent) {
        return messageContent.getIdentifiableArtefact().getUrn();
    }

    @Override
    public List<VariableElement> getPendingOrFailedMessages() throws MetamacException {
        // @formatter:off
        List<ConditionalCriteria> criteria = criteriaFor(VariableElement.class)
                .lbrace()
                .withProperty(VariableElementProperties.streamMessageStatus()).eq(StreamMessageStatusEnum.PENDING)
                .or()
                .withProperty(VariableElementProperties.streamMessageStatus()).eq(StreamMessageStatusEnum.FAILED)
                .rbrace()
                .build();
        // @formatter:on
        return variableElementRepository.findByCondition(criteria, PagingParameter.noLimits()).getValues();
    }

    @Override
    public VariableElementDo2AvroMapper getMapper() {
        return variableElementDo2AvroMapper;
    }

    @Override
    public StreamMessageStatusEnum getStreamMessageStatus(VariableElement messageContent) {
        return messageContent.getStreamMessageStatus();
    }

    @Override
    public void setStreamMessageStatus(VariableElement messageContent, StreamMessageStatusEnum streamMessageStatus) {
        messageContent.setStreamMessageStatus(streamMessageStatus);
    }

    @Override
    public VariableElement saveMessage(VariableElement messageContent) {
        return variableElementRepository.save(messageContent);
    }

    @Override
    public String getTopic() throws MetamacException {
        return configurationService.retrieveKafkaTopicVariableElementPublication();
    }
}
