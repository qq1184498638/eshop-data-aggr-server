package com.springcloud.eshop.aggr.data.server.service.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.springcloud.eshop.common.server.support.utils.ServerResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class AggrDataChangeQueueReceiver {
    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(queues = RabbitConstants.QUEUE_NAME_AGGR_DATA)
    public void process(ServerResponse sendMessage, Channel channel, Message message) throws Exception {
        String msg = (String) sendMessage.getData();
        log.info("[{}]处理列消息队列接收数据，消息体{}", RabbitConstants.QUEUE_NAME_AGGR_DATA, msg);

        System.out.println(message.getMessageProperties().getDeliveryTag());

        try {
            // 参数校验
            Assert.notNull(msg, "sendMessage 消息体不能为NULL");

            // TODO 处理消息
            JSONObject messageJSONObject = JSONObject.parseObject(msg);
            String dmiType = messageJSONObject.getString("dynamicType");
            if ("brand".equals(dmiType)) {
                processBrandDimDataChange(messageJSONObject);
            } else if ("category".equals(dmiType)) {
                processCategoryDimDataChange(messageJSONObject);
            } else if ("productIntroduce".equals(dmiType)) {
                processProductIntroDimDataChange(messageJSONObject);
            } else {
                processProductDataChange(messageJSONObject);
            }

            // 确认消息已经消费成功
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("MQ消息处理异常，消息体:{}", message.getMessageProperties().getCorrelationIdString(), JSON.toJSONString(sendMessage), e);

            // 确认消息已经消费消费失败，将消息发给下一个消费者
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
        }
    }


    private void processBrandDimDataChange(JSONObject messageJSONObject) {
        String id = messageJSONObject.getString("id");
        String dataJson = (String) redisTemplate.opsForValue().get("eshop:brand:" + id);
        // 多此一举，看一下，查出来一个品牌数据，然后直接就原样写redis
        // 实际上是这样子的，我们这里是简化了数据结构和业务，实际上任何一个维度数据都不可能只有一个原子数据
        // 品牌数据，肯定是结构多变的，结构比较复杂，有很多不同的表，不同的原子数据
        // 实际上这里肯定是要将一个品牌对应的多个原子数据都从redis查询出来，然后聚合之后写入redis
        if (StringUtils.isNotBlank(dataJson)) {
            redisTemplate.opsForValue().set("eshop:dynamic:brand:" + id, dataJson);
        }else{
            redisTemplate.delete("eshop:dynamic:brand:" + id);
        }
    }
    private void processCategoryDimDataChange(JSONObject messageJSONObject) {
        String id = messageJSONObject.getString("id");
        String dataJson = (String) redisTemplate.opsForValue().get("eshop:category:" + id);
        if (StringUtils.isNotBlank(dataJson)) {
            redisTemplate.opsForValue().set("eshop:dynamic:category:" + id, dataJson);
        }else{
            redisTemplate.delete("eshop:dynamic:category:" + id);
        }
    }
    private void processProductIntroDimDataChange(JSONObject messageJSONObject) {
        String id = messageJSONObject.getString("id");
        String dataJson = (String) redisTemplate.opsForValue().get("eshop:product-intro:" + id);
        if (StringUtils.isNotBlank(dataJson)) {
            redisTemplate.opsForValue().set("eshop:dynamic:product-intro:" + id, dataJson);
        }else{
            redisTemplate.delete("eshop:dynamic:product-intro:" + id);
        }
    }

    private void processProductDataChange(JSONObject messageJSONObject) {
        String id = messageJSONObject.getString("id");
        List<String> list =
                redisTemplate.opsForValue().multiGet(Arrays.asList("eshop:product:" + id, "eshop:productProperty:" + id, "eshop:productSpecification:" + id));
        log.info(list.toString());
        if (!CollectionUtils.isEmpty(list)) {
            JSONObject productData = JSONObject.parseObject(list.get(0));
            //商品属性
            String productPropertyStr = list.get(1);
            log.info(productPropertyStr);
            if (StringUtils.isNotBlank(productPropertyStr)) {
                productData.put("productProperty", JSONObject.parse(productPropertyStr));
            }
            //商品规格
            String productSpecificationStr = list.get(2);
            log.info(productSpecificationStr);
            if (StringUtils.isNotBlank(productSpecificationStr)) {
                productData.put("productSpecification", JSONObject.parse(productSpecificationStr));
            }
            redisTemplate.opsForValue().set("eshop:dynamic:product:" + id, productData.toJSONString());
        }else{
            redisTemplate.delete("eshop:dynamic:product:" + id);
        }


    }
}
