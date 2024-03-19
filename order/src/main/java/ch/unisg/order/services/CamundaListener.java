package ch.unisg.order.services;

import ch.unisg.order.dto.CamundaMessageDto;
import ch.unisg.order.util.VariablesUtil;
import ch.unisg.order.util.WorkflowLogger;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(topicName = "endWarehouse")
public class CamundaListener implements ExternalTaskHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageService messageService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        WorkflowLogger.info(logger, "endWarehouse","Warehouse Process started");
    }
}