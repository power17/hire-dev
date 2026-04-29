package com.power.mq;

import com.power.api.mq.RabbitMQSMSConfig;
import com.power.pojo.mq.SMSConfigQO;
import com.power.utils.GsonUtils;
import com.power.utils.SMSUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class RabbitMQSMSConsumer {
    @Autowired
    private SMSUtils smsUtils;

    @RabbitListener(queues = {RabbitMQSMSConfig.SMS_QUEUE})
    public void watchQueue(Message message, Channel channel) throws Exception {
        try {
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            log.info("routingKey = " + routingKey);

//            int a = 1/0;

            String msg = new String(message.getBody());
            log.info("msg = " + msg);


            /**
             * deliveryTag: 消息投递的标签
             * multiple: 批量确认所有消费者获得的消息
             */
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),
                    true);
        } catch (Exception e) {
            e.printStackTrace();
            /**
             * requeue: true：重回队列 false：丢弃消息
             */
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),
                    true,
                    false);
//            channel.basicReject();
        }
    }
}
