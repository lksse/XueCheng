package com.xuecheng.test.rabbitMq;
import com.test.rabbitmq.TestRabbitmqApplication;
import com.test.rabbitmq.config.RabbitmqConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;



import java.io.IOException;
import java.util.concurrent.TimeoutException;
@SpringBootTest(classes = TestRabbitmqApplication.class)
@RunWith(SpringRunner.class)
public class Producer05_topics_springBoot {
    @Autowired
    RabbitTemplate rabbitTemplate;


    //使用RabbitTemplate发送消息
    @Test
    public void TestSendEmail(){
        /**
         * 参数：
         * 1.交换机名称
         * 2.RoutingKey
         * 3.消息内容
         */
        for (int i = 0; i < 3; i++) {
            String message = "sms email inform to user"+i;
            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_TOPICS_INFORM,"inform.email",message);
        }
    }

}
