package ch.unisg.order.services;

import ch.unisg.order.messages.Message;
import ch.unisg.order.messages.MessageSender;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StartWarehouseAdapter implements JavaDelegate {

    @Autowired
    private MessageSender messageSender;

    @Override
    public void execute(DelegateExecution context) {

        System.out.println("Start warehouse process for order " + context.getVariable("orderId"));

        messageSender.send(
                new Message<StartWarehouseCommandPayload>(
                        "StartWarehouseCommand",
                        new StartWarehouseCommandPayload(context.getVariable("orderId").toString())));
    }

}