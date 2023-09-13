package com.test.rabbitmq.mq;


import com.rabbitmq.client.Channel;
import com.test.rabbitmq.config.RabbitmqConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiveHandler {
    @RabbitListener(queues = {RabbitmqConfig.QUEUE_INFORM_EMAIL})
    public void send_email(String message0, Message message, Channel channel){
        System.out.println("receive message is:"+message0);
    }
}
