package org.siemac.metamac.srm.web.server.listener;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.srm.core.facade.serviceapi.TasksMetamacServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class JobsSchedulerListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger             logger = LoggerFactory.getLogger(JobsSchedulerListener.class);

    @Autowired
    private TasksMetamacServiceFacade taskServiceFacade;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.debug("Scheduling jobs...");
        schedulingResendKafkaDatasetMessageJob();
    }

    private void schedulingResendKafkaDatasetMessageJob() {
        ServiceContext ctx = new ServiceContext("Metamac", "Tasks", "Metamac");

        taskServiceFacade.scheduleResendKafkaResourcesMessageJob(ctx);
    }

}
