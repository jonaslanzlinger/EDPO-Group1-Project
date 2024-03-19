package ch.unisg.warehouse.services;

import ch.unisg.warehouse.dto.CamundaMessageDto;
import ch.unisg.warehouse.utils.VariablesUtil;
import ch.unisg.warehouse.utils.WorkflowLogger;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ExternalTaskSubscription(topicName = "StartWarehouseCommand")
public class CamundaListener implements ExternalTaskHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MessageService messageService;

    private final static String MESSAGE_START = "StartWarehouseCommand";

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        CamundaMessageDto message = VariablesUtil.buildCamundaMessageDto(externalTask.getBusinessKey(), "StartWarehouseCommand");
        messageService.correlateMessage(message, MESSAGE_START);

        WorkflowLogger.info(logger, "StartWarehouseCommand","Warehouse Process started");

        externalTaskService.complete(externalTask);
    }
}