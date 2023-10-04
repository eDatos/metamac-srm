package org.siemac.metamac.srm.core.task.serviceimpl;

import java.util.Date;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.util.ApplicationContextProvider;
import org.siemac.metamac.srm.core.facade.serviceapi.TasksMetamacServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@DisallowConcurrentExecution
public class ResendPublishedResourcesKafkaMessageJob implements Job {

    private static Logger             logger            = LoggerFactory.getLogger(ResendPublishedResourcesKafkaMessageJob.class);

    private TasksMetamacServiceFacade taskServiceFacade = null;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            logger.debug("resend all published last version srm resources kafka messages at {}", new Date());
            ServiceContext serviceContext = new ServiceContext("Metamac", context.getFireInstanceId(), "structural-resources-core");
            getTaskServiceFacade().executeResendPublishedResourcesKafkaMessageTask(serviceContext);
            logger.debug("resend all published last version srm resources kafka messages at {}", new Date());
        } catch (MetamacException e) {
            logger.error("An unexpected error has occurred during resend all published last version srm resources kafka messages  job execution", e);
        }
    }

    private TasksMetamacServiceFacade getTaskServiceFacade() {
        if (taskServiceFacade == null) {
            taskServiceFacade = (TasksMetamacServiceFacade) ApplicationContextProvider.getApplicationContext().getBean(TasksMetamacServiceFacade.BEAN_ID);
        }

        return taskServiceFacade;
    }

}
