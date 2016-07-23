package com.lsxy.framework.mq.kafka;

import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.mq.api.AbstractMQConsumer;
import com.lsxy.framework.mq.api.MQEvent;
import com.lsxy.framework.mq.api.MQMessageHandler;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings({"rawtypes","unchecked"})
public class KafkaConsumer extends AbstractMQConsumer {
	private static final Log logger = LogFactory.getLog(KafkaConsumer.class);

	
	private ConsumerConnector consumerConnector;
	private ExecutorService threadPools = null;
	int thread_number_per_topic = 4;
	
	private String[] topics = null;
	
	@Override
	public void init(){
		logger.debug("初始化全局事件订阅组件");
		this.topics = getTopics();
		if(this.topics == null || this.topics.length <= 0){
			logger.debug("未配置mq.subscribe.topics项，不启动订阅服务");
			return;
		}
		Properties props = new Properties();
		props.put("zookeeper.connect", SystemConfig.getProperty("mq.kafka.zookeeper.connect","localhost:2181"));
		props.put("group.id", SystemConfig.getProperty("system.id"));
		props.put("zookeeper.session.timeout.ms", "400");
		props.put("zookeeper.sync.time.ms", "200");
		props.put("auto.commit.interval.ms", "1000");
		ConsumerConfig config = new ConsumerConfig(props);
		consumerConnector = Consumer.createJavaConsumerConnector(config);
		logger.debug("kafka消费者端连接成功");
		this.start();
	}


	/**
	 * 订阅主题
	 * 
//	 * @param topics
	 *            topic逗号分隔
	 */
	private void subscribTopics() {

		HashMap<String, Integer> topicMap = new HashMap<String, Integer>();

		for (String topic : this.topics) {
			topicMap.put(topic, thread_number_per_topic);
		}

		Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumerConnector.createMessageStreams(topicMap);
		Set<String> topicStreams = consumerMap.keySet();
		threadPools = Executors.newFixedThreadPool(thread_number_per_topic * topicMap.size());
		final KafkaConsumer consumer = this;
		
		for (String topicStream : topicStreams) {
			List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(topicStream);
			// create list of 4 threads to consume from each of the partitions
			for (final KafkaStream<byte[], byte[]> stream : streams) {
				threadPools.submit(new Runnable() {
					public void run() {
						ConsumerIterator<byte[], byte[]> it = stream.iterator();
						while (it.hasNext()) {
							String msg = new String(it.next().message());
							logger.debug("recivied msg :" + msg);
							try {
								MQEvent event = consumer.parseMessage(msg);
								if (event != null) {
									logger.debug("parse msg to MQEvent object and id is :"	+ event.getId());
									MQMessageHandler handler = getMqHandlerFactory().getHandler(event);
									if (handler != null) {
										logger.debug("found a handler for the event:" + event.getId() + "--" + handler.getClass().getName());
										handler.handleMessage(event);
									} else {
										logger.debug("have not defined a handler for the event:" + event.getEventName());
									}
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
						}
					}
				});
			}
		}
	}
	
	@Override
	public void destroy() {
		consumerConnector.shutdown();
		threadPools.shutdownNow();
	}	
	

//	@Override
//	public void await() {
//		try {
//			while (!threadPools.awaitTermination(1, TimeUnit.MINUTES)) {
//				logger.debug("------------------------");
//			}
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}


	@Override
	public void start() {
		if(this.topics == null || this.topics.length <= 0){
			return;
		}
		subscribTopics();
		logger.debug("启动消息监听成功:订阅主题:"+topics);
	}
	
//	public String[] getTopics(){
//		String t = SystemConfig.getProperty("mq.subscribe.topics");
//		String[] topics = null;
//		if(StringUtil.isNotEmpty(t)){
//			topics = t.split(",");
//		}
//		return topics;
//	}
}
