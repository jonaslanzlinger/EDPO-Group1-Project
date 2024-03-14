package ch.unisg.delivery.worker;

import ch.unisg.delivery.utils.WorkflowLogger;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(topicName = "delivery")
public class DeliveryTaskHandler implements ExternalTaskHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        WorkflowLogger.info(logger, "DeliveryTaskHandler", "Received task from Camunda: " + externalTask.getActivityId());
        WorkflowLogger.info(logger, "DeliveryTaskHandler", "Completed task to Camunda: " + externalTask.getActivityId());

        externalTaskService.complete(externalTask);
    }
}