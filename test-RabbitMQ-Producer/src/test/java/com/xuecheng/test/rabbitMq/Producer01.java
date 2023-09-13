package com.xuecheng.test.rabbitMq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/*
 * MQ入门
 *
 * */
public class Producer01 {

    private static final String QUEUE = "HEllO";

    public static void main(String[] args) throws IOException, TimeoutException{
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
            //声明队列     如果队列在MQ中没有，则创建
            /** queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
                param1:队列名称
             * param2:是否持久化    （如果重启后，队列还在）
             * param3:队列是否独占此连接-------队列只允许在该连接中访问，连接关闭队列删除
             * param4:队列不再使用时是否自动删除此队列
             * param5:队列参数--------队列拓展参数
            * */
            channel.queueDeclare(QUEUE,true,false,false,null);
            //定义一个消息的内容
            String message = "Hello,This is a message!";
            //发送消息
            /**
            * void basicPublish(String exchange, String routingKey, AMQP.BasicProperties props, byte[] body)
             * param1：Exchange的名称，如果没有指定，则使用Default Exchange
             * param2:routingKey,消息的路由Key，是用于Exchange（交换机）将消息转发到指定的消息队列,（如果是默认路由，routingKEY使用队列名）
             * param3:消息包含的属性
             * param4：消息体
            * */
            channel.basicPublish("",QUEUE,null,message.getBytes());
            System.out.println("Send to mq :'" + message + "'");
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
