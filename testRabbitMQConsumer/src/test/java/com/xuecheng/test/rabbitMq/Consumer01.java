package com.xuecheng.test.rabbitMq;


import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/*
 * MQ入门消费者
 *
 * */
public class Consumer01 {

    private static final String QUEUE = "HEllO";

    public static void main(String[] args) throws IOException, TimeoutException {
//消费者于MQ建立连接
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
        Connection connection = connectionFactory.newConnection();
        //创建会话通道
        Channel channel = connection.createChannel();

        //声明队列     如果队列在MQ中没有，则创建
            /** queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
                param1:队列名称
             * param2:是否持久化    （如果重启后，队列还在）
             * param3:队列是否独占此连接-------队列只允许在该连接中访问，连接关闭队列删除
             * param4:队列不再使用时是否自动删除此队列
             * param5:队列参数--------队列拓展参数
            * */
        channel.queueDeclare(QUEUE, true, false, false, null);
        //实现消费方法
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {

            /**
             当接收到消息后，此方法被调用
             * @param consumerTag  消费者标签：用来标识消费者
             * @param envelope  信封:可以拿到信息
             * @param properties    消息属性
             * @param body  消息内容
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //super.handleDelivery(consumerTag, envelope, properties, body);
                //交换机
                String exchange = envelope.getExchange();
                //消息ID：在channel中标识消息ID，可用于消息被确认接受
                long deliveryTag = envelope.getDeliveryTag();
                //消息内容
                String message = new String(body,"UTF-8");
                System.out.println("receive a message: " + message);
            }
        };

        //监听队列
        /**
         * 监听队列String queue, boolean autoAck,Consumer callback
         * 参数明细
         * 1、队列名称
         * 2、是否自动回复，设置为true为表示消息接收到自动向mq回复接收到了，mq接收到回复会删除消息，设置
         为false则需要手动回复
         * 3、消费消息的方法，消费者接收到消息后调用此方法
         */
        channel.basicConsume(QUEUE, true, defaultConsumer);
    }
}
