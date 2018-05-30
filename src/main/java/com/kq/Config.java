package com.kq;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class Config {

	public static final String ZK_SERVERS = "192.168.6.233:2001";

	public static final String CACHE_NODE = "/king";

	/**
	 * 默认启动了
	 * @return
	 */
	public static CuratorFramework getClient() {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.builder().connectString(ZK_SERVERS)
				.retryPolicy(retryPolicy).sessionTimeoutMs(6000).connectionTimeoutMs(3000).build();
		
//		.namespace("demo")
		client.start();
		return client;
	}

}
