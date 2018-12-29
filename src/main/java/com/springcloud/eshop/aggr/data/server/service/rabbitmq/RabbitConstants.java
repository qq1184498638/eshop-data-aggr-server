package com.springcloud.eshop.aggr.data.server.service.rabbitmq;

/**
 * 消息队列常量
 *
 * @author yuhao.wang
 * @Description
 * @return
 * @Date 2017/7/5 15:42
 */
public final class RabbitConstants {
    /**
     * 死性队列EXCHANGE名称
     */
    public static final String MQ_EXCHANGE_DEAD_QUEUE = "test-dead-queue-exchange";
    public static final String QUEUE_NAME_SYNC_DATA_DEAD_QUEUE = "test-aggr-data-dead-queue-exchange";
    /**
     * 死性队列名称
     */
    public static final String QUEUE_NAME_DEAD_QUEUE = "test-dead-queue";
    public static final String QUEUE_NAME_AGGR_DATA_DEAD_QUEUE = "test-aggr-data-dead-queue";

    /**
     * 死性队列路由名称
     */
    public static final String MQ_ROUTING_KEY_DEAD_QUEUE = "test-routing-key-dead-queue";
    public static final String MQ_ROUTING_KEY_AGGR_DATA_DEAD_QUEUE = "test-routing-key-aggr-data-dead-queue";
    /**
     * 发放奖励EXCHANGE名称
     */
    public static final String MQ_EXCHANGE_SEND_AWARD = "test-send-award-exchange";

    /**
     * 发放优惠券队列名称
     */
    public static final String QUEUE_NAME_SEND_COUPON = "test-send-coupon-queue";

    /**
     * 发放优惠券路由key
     */
    public static final String MQ_ROUTING_KEY_SEND_COUPON = "test-routing-key-send-coupon";

    /**
     * data sync 数据同步的Exchange名称
     */
    public static final String MQ_EXCHANGE_DATA_SYNC_QUEUE = "data-sync-exchange";

    /**
     * data sync 数据同步的队列的名称
     */
    public static final String QUEUE_NAME_DATA_SYNC = "send-data-sync";

    /**
     * data sync 数据同步路由key
     */
    public static final String MQ_ROUTING_KEY_DATA_SYNC = "routing-key-data-sync";

    /**
     * aggr 数据同步的Exchange名称
     */
    public static final String MQ_EXCHANGE_AGGR_DATA_QUEUE = "aggr-data-exchange";

    /**
     * data sync 数据同步的队列的名称
     */
    public static final String QUEUE_NAME_AGGR_DATA = "send-aggr-data";

    /**
     * data sync 数据同步路由key
     */
    public static final String MQ_ROUTING_KEY_AGGR_DATA = "routing-key-aggr-data";

}
