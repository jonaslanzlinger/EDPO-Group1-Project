package ch.unisg.warehouse.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FinishWarehouseAdapter implements JavaDelegate {

    @Autowired
    private MessageSender messageSender;

    @Override
    public void execute(DelegateExecution context) {

        System.out.println("Start warehouse process for order " + context.getVariable("orderId"));

        messageSender.send(
                new Message<FinishWarehouseCommandPayload>(
                        "FinishWarehouseCommand",
                        new FinishWarehouseCommandPayload(context.getVariable("orderId").toString())));
    }

}