package com.xuecheng.test.rabbitMq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer03_routing {
    //private static final String QUEUE = "HEllO";
    //队列名
    private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    private static final String EXCHANGE_ROUTING_INFORM = "exchange_routing_inform";
    private static final String ROUTINGKEY_EMAIL= "inform_email";
    private static final String ROUTINGKEY_SMS = "inform_sms";


    public static void main(String[] args) throws IOException, TimeoutException {
        //生产者于MQ建立连接
        //通过建立连接工厂建立连接
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //配置连接信息
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);  //端口
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //设置虚拟机        实现多个虚拟MQ    每个虚拟机相当于一个独立的MQ
        connectionFactory.setVirtualHost("/");
        //建立新连接
        Connection connection = null;
        Channel channel = null;
        try {
            connection = connectionFactory.newConnection();
            //创建会话通道  生产者和mq服务所有通信都在通道中完成
            channel = connection.createChannel();
            /** 声明队列     如果队列在MQ中没有，则创建
             queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
             param1:队列名称
             * param2:是否持久化    （如果重启后，队列还在）
             * param3:队列是否独占此连接-------队列只允许在该连接中访问，连接关闭队列删除
             * param4:队列不再使用时是否自动删除此队列
             * param5:队列参数--------队列拓展参数
             * */
            channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
            channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);
            /**
             * 声明交换机
             * param1:交换机名称
             * param2:交换机类型
             *      fanout:对应的工作模式是：发布订阅publish/scribe
             *      direct:对应Routing工作模式
             *      topic:对应通配符工作模式
             *      headers:对应headers模式
             */
            channel.exchangeDeclare(EXCHANGE_ROUTING_INFORM, BuiltinExchangeType.DIRECT);
            //交换机和队列绑定String queue, String exchange, String routingKey
            /**
             * 参数明细
             * 1、队列名称
             * 2、交换机名称
             * 3、路由key     交换机根据路由KEY的值将消息转发到指定队列      在发布订阅工作模式时设置为空字符串
             */
            channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_ROUTING_INFORM,ROUTINGKEY_EMAIL);
            channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_ROUTING_INFORM,ROUTINGKEY_SMS);
            for (int i = 0; i < 5; i++) {
                //定义一个消息的内容
                String message = "Hello,Send EMAIL message to User";
                //发送消息
                /**
                 * void basicPublish(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body)
                 * param1：Exchange的名称，如果没有指定，则使用Default Exchange
                 * param2:routingKey,消息的路由Key，是用于Exchange（交换机）将消息转发到指定的消息队列,（如果是默认路由，routingKEY使用队列名）
                 * param3:消息包含的属性
                 * param4：消息体
                 * */
                //指定RoutingKey
                channel.basicPublish(EXCHANGE_ROUTING_INFORM,ROUTINGKEY_EMAIL,null,message.getBytes());
                System.out.println("Send to mq :'" + message + "'");
            }
            for (int i = 0; i < 5; i++) {
                //定义一个消息的内容
                String message = "Hello,Send INFORM message to User";
                //发送消息
                /**
                 * void basicPublish(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body)
                 * param1：Exchange的名称，如果没有指定，则使用Default Exchange
                 * param2:routingKey,消息的路由Key，是用于Exchange（交换机）将消息转发到指定的消息队列,（如果是默认路由，routingKEY使用队列名）
                 * param3:消息包含的属性
                 * param4：消息体
                 * */
                //指定RoutingKey
                channel.basicPublish(EXCHANGE_ROUTING_INFORM,"inform",null,message.getBytes());
                System.out.println("Send to mq :'" + message + "'");
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(channel != null)
            {
                channel.close();
            }
            if(connection != null)
            {
                connection.close();
            }
        }
    }
}
